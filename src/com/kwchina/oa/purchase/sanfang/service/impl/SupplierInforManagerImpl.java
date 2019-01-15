package com.kwchina.oa.purchase.sanfang.service.impl;

import com.kwchina.core.base.entity.OrganizeInfor;
import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.core.util.DateHelper;
import com.kwchina.core.util.string.StringUtil;
import com.kwchina.oa.purchase.sanfang.VO.BackgroundVO;
import com.kwchina.oa.purchase.sanfang.VO.QualificationVO;
import com.kwchina.oa.purchase.sanfang.VO.SupplierCheckVO;
import com.kwchina.oa.purchase.sanfang.VO.SupplierVO;
import com.kwchina.oa.purchase.sanfang.dao.SupplierInforDAO;
import com.kwchina.oa.purchase.sanfang.entity.BackGround;
import com.kwchina.oa.purchase.sanfang.entity.Qualification;
import com.kwchina.oa.purchase.sanfang.entity.SupplierCheckInfor;
import com.kwchina.oa.purchase.sanfang.entity.SupplierInfor;
import com.kwchina.oa.purchase.sanfang.enums.PurchaseTypeEnum;
import com.kwchina.oa.purchase.sanfang.enums.SupplierStatusEnum;
import com.kwchina.oa.purchase.sanfang.service.SupplierInforManager;
import com.kwchina.oa.purchase.sanfang.utils.Convert;
import com.kwchina.oa.purchase.sanfang.utils.EnumUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springmodules.validation.bean.conf.loader.annotation.handler.Max;

import javax.swing.text.html.HTMLDocument;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
public class SupplierInforManagerImpl extends BasicManagerImpl<SupplierInfor> implements SupplierInforManager {
    @Autowired
    private SupplierInforDAO supplierInforDAO;

    @Autowired
    public void setSanfangInforDAO(SupplierInforDAO supplierInforDAO) {
        this.supplierInforDAO = supplierInforDAO;
        super.setDao(supplierInforDAO);
    }

    @Override
    public List<SupplierInfor> getAllSupplier() {

        String hql = "from SupplierInfor where valid=1";
        List<SupplierInfor> result = getResultByQueryString(hql);
        return result;
    }

    @Override
    public List<SupplierInfor> getInSupplier(Integer purchaseType,Integer deptId) {

        String hql = "from SupplierInfor where valid=1 and status>0 and purchaseType=" + purchaseType;
        List<SupplierInfor> result = getResultByQueryString(hql);
        if(deptId!=null&&deptId!=0){
            List<SupplierInfor> result1 = new ArrayList<>();
            for(SupplierInfor supplierInfor:result){
                Set<OrganizeInfor> organizes = supplierInfor.getOrganizes();
                for(OrganizeInfor organizeInfor:organizes){
                    System.out.println(organizeInfor.getOrganizeId().intValue());
                    if(deptId.intValue()==organizeInfor.getOrganizeId().intValue()){
                        result1.add(supplierInfor);
                        break;
                    }
                }
            }
            return result1;
        }else{
            return result;
        }
    }

    @Override
    public String getContactByName(String supplierName) {
        return supplierInforDAO.getContactByName(supplierName);
    }

    @Override
    public SupplierInfor findBySupplierName(String supplierName) {
        String queryString = "from SupplierInfor supplier where supplier.supplierName = '" + supplierName + "'";
        List list = this.supplierInforDAO.getResultByQueryString(queryString);

        if (list != null && list.size() > 0 && list.get(0) != null) {
            return (SupplierInfor) list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public SupplierVO transPOToVO(SupplierInfor supplierInfor) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SupplierVO supplierVO = new SupplierVO();
        BeanUtils.copyProperties(supplierInfor,supplierVO);
        supplierVO.setSupplierTel(supplierInfor.getContactName()+"/"+supplierInfor.getSupplierContact());
        if (supplierInfor.getExpiration() != null) {
            supplierVO.setExpirationTime(sdf.format(supplierInfor.getExpiration()));
        }
        supplierVO.setPurchaseTypeMsg(EnumUtil.getByCode(supplierInfor.getPurchaseType(), PurchaseTypeEnum.class).getMsg());
        if (supplierInfor.isPassISO()) {
            supplierVO.setPass("是");
        } else {
            supplierVO.setPass("否");
        }
        if(supplierInfor.isSingle()){
            supplierVO.setSingleOne("是");
        }else {
            supplierVO.setSingleOne("否");
        }
        if (supplierInfor.getStatus() != null) {
            supplierVO.setSupplierStatus(EnumUtil.getByCode(supplierInfor.getStatus(), SupplierStatusEnum.class).getMsg());
        }
        if (supplierInfor.getSponsor() != null) {
            supplierVO.setSponsorName(supplierInfor.getSponsor().getPerson().getPersonName());
            supplierVO.setSponsorId(supplierInfor.getSponsor().getPersonId());
        }
        if (supplierInfor.getStartDate() != null) {
            supplierVO.setStartTime(DateHelper.getString(supplierInfor.getStartDate()));
        }
        List<SupplierCheckInfor> checkInfors = supplierInfor.getCheckInfors();
        List<SupplierCheckVO> checkVOS = new ArrayList<>();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (SupplierCheckInfor checkInfor : checkInfors) {
            if (checkInfor.isLastOne()) {
                SupplierCheckVO checkVO = new SupplierCheckVO();
                checkVO.setSupplierID(supplierInfor.getSupplierID());
                if (checkInfor.getCheckTime() != null) {
                    checkVO.setCheckDate(sdf.format(checkInfor.getCheckTime()));
                }
                if (StringUtil.isNotEmpty(checkInfor.getCheckOpinion())) {
                    checkVO.setCheckOpinion(checkInfor.getCheckOpinion());
                }
                checkVO.setCheckResult(checkInfor.getCheckResult());
                checkVO.setLayer(checkInfor.getLayer());
                checkVO.setLastOne(checkInfor.isLastOne());
                checkVO.setCheckerName(checkInfor.getChecker().getPerson().getPersonName());
                checkVOS.add(checkVO);
            }
        }
        supplierVO.setCheckVOS(checkVOS);
        List<Qualification> qualifications = supplierInfor.getQualifications();
        List<QualificationVO> qualificationVOS = new ArrayList<>();
        for (Qualification qualification : qualifications) {
            QualificationVO qualificationVO = new QualificationVO();
            BeanUtils.copyProperties(qualification, qualificationVO);
            if(StringUtil.isNotEmpty(qualification.getQualificationAttach())){
                qualificationVO.setAttach(qualification.getQualificationAttach());
            }
            qualificationVO.setSupplierID(qualification.getSupplierInfor().getSupplierID());
            qualificationVOS.add(qualificationVO);
        }
        supplierVO.setQualificationVOS(qualificationVOS);
        List<BackGround> backGrounds = supplierInfor.getBackGrounds();
        List<BackgroundVO> backgroundVOS= new ArrayList<>();
        for (BackGround backGround : backGrounds) {
            BackgroundVO backgroundVO = new BackgroundVO();
            BeanUtils.copyProperties(backGround, backgroundVO);
            backgroundVO.setSupplierID(backGround.getSupplierInfor().getSupplierID());
            backgroundVOS.add(backgroundVO);
        }
        supplierVO.setBackgroundVOS(backgroundVOS);
        Iterator<OrganizeInfor> iterator = supplierInfor.getOrganizes().iterator();
        List<String> names=new ArrayList<>();
        List<Integer> organizeIDs=new ArrayList<>();
        while (iterator.hasNext()){
            OrganizeInfor next = iterator.next();
            names.add(next.getOrganizeName());
            organizeIDs.add(next.getOrganizeId());
        }
        supplierVO.setOrganizeIDs(organizeIDs);
        supplierVO.setOrganizeNames(names);
        return supplierVO;
    }

    @Override
    public boolean validName(String supplierName) {
        List<SupplierInfor> allSupplier = getAllSupplier();
        for (SupplierInfor supplierInfor : allSupplier) {
            if (supplierInfor.getSupplierName().equals(supplierName)) {
                return false;
            }
        }
        return true;
    }
}
