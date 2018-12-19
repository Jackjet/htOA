package com.kwchina.oa.purchase.sanfang.service.impl;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.core.util.DateHelper;
import com.kwchina.oa.purchase.sanfang.VO.SupplierVO;
import com.kwchina.oa.purchase.sanfang.dao.CertifyInfoDAO;
import com.kwchina.oa.purchase.sanfang.dao.SupplierInforDAO;
import com.kwchina.oa.purchase.sanfang.entity.CertifyInfo;
import com.kwchina.oa.purchase.sanfang.entity.SupplierInfor;
import com.kwchina.oa.purchase.sanfang.enums.PurchaseTypeEnum;
import com.kwchina.oa.purchase.sanfang.enums.SupplierStatusEnum;
import com.kwchina.oa.purchase.sanfang.service.CertifyInfoManager;
import com.kwchina.oa.purchase.sanfang.service.SupplierInforManager;
import com.kwchina.oa.purchase.sanfang.utils.EnumUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class CertifyInfoManagerImpl extends BasicManagerImpl<CertifyInfo> implements CertifyInfoManager {
    @Autowired
    private CertifyInfoDAO certifyInfoDAO;

    @Autowired
    public void setSanfangInforDAO(CertifyInfoDAO certifyInfoDAO) {
        this.certifyInfoDAO = certifyInfoDAO;
        super.setDao(certifyInfoDAO);
    }

}
