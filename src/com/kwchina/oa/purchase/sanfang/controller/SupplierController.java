package com.kwchina.oa.purchase.sanfang.controller;

import com.kwchina.core.base.entity.OrganizeInfor;
import com.kwchina.core.base.entity.PersonInfor;
import com.kwchina.core.base.entity.RoleInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.OrganizeManager;
import com.kwchina.core.base.service.RoleManager;
import com.kwchina.core.base.service.SystemUserManager;
import com.kwchina.core.common.controller.BasicController;
import com.kwchina.core.common.page.PageList;
import com.kwchina.core.common.page.Pages;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.core.util.DateHelper;
import com.kwchina.core.util.ExcelObject;
import com.kwchina.core.util.ExcelOperate;
import com.kwchina.core.util.json.JSONConvert;
import com.kwchina.core.util.string.StringUtil;
import com.kwchina.oa.purchase.sanfang.VO.CertifyVO;
import com.kwchina.oa.purchase.sanfang.VO.SupplierCheckVO;
import com.kwchina.oa.purchase.sanfang.VO.SupplierVO;
import com.kwchina.oa.purchase.sanfang.entity.CertifyInfo;
import com.kwchina.oa.purchase.sanfang.entity.SupplierCheckInfor;
import com.kwchina.oa.purchase.sanfang.entity.SupplierInfor;
import com.kwchina.oa.purchase.sanfang.enums.PurchaseTypeEnum;
import com.kwchina.oa.purchase.sanfang.enums.SupplierStatusEnum;
import com.kwchina.oa.purchase.sanfang.service.CertifyInfoManager;
import com.kwchina.oa.purchase.sanfang.service.SupplierCheckManager;
import com.kwchina.oa.purchase.sanfang.service.SupplierInforManager;
import com.kwchina.oa.purchase.sanfang.utils.Convert;
import com.kwchina.oa.purchase.sanfang.utils.EnumUtil;
import com.kwchina.oa.util.SysCommonMethod;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by asus on 2018/7/23.
 */
@Controller
@RequestMapping(value = "supplier.do")
public class SupplierController extends BasicController {
    @Autowired
    private SupplierInforManager supplierInforManager;
    @Autowired
    private OrganizeManager organizeManager;
    @Autowired
    private SystemUserManager systemUserManager;
    @Autowired
    private CertifyInfoManager certifyInfoManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private SupplierCheckManager supplierCheckManager;
    @Autowired
    HttpSession session;
    public static final String SESSION_ORDER_TOKEN = "SESSION_ORDER_TOKEN";//订单提交令牌，防止重复提交

    @RequestMapping(params = "method=supplierList")
    public void supplierList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //构造查询语句
        String[] queryString = this.supplierInforManager.generateQueryString1("SupplierInfor", "supplierID", getSearchParams(request));
        String page = request.getParameter("page");        //当前页
        String rowsNum = request.getParameter("rows");    //每页显示的行数
        String status = request.getParameter("status");
        Pages pages = new Pages(request);
        pages.setPage(Integer.valueOf(page));
        pages.setPerPageNum(Integer.valueOf(rowsNum));
        PageList pl = this.supplierInforManager.getResultByQueryString(queryString[0], queryString[1], true, pages);
        List list = pl.getObjectList();
        List fList = new ArrayList<>();
        if (StringUtil.isNotEmpty(status)) {
            Integer state = Integer.parseInt(status);
            for (Iterator it = list.iterator(); it.hasNext(); ) {
                SupplierInfor supplierInfor = (SupplierInfor) it.next();
                if (supplierInfor.getStatus().equals(state)) {
                    fList.add(supplierInfor);
                }
            }
        } else {
            fList = list;
        }

        // 把查询到的结果转化为VO
        List supplierVos = new ArrayList();
        if (fList.size() > 0) {
            for (Iterator it = fList.iterator(); it.hasNext(); ) {
                SupplierInfor supplierInfor = (SupplierInfor) it.next();
                SupplierVO supplierVO = this.supplierInforManager.transPOToVO(supplierInfor);
                supplierVos.add(supplierVO);
            }
        }
        //定义返回的数据类型：json，使用了json-lib
        JSONObject jsonObj = new JSONObject();
        //定义rows，存放数据
        JSONArray rows = new JSONArray();
        jsonObj.put("page", pl.getPages().getCurrPage());   //当前页(名称必须为page)
        jsonObj.put("total", pl.getPages().getTotalPage()); //总页数(名称必须为total)
        jsonObj.put("records", pl.getPages().getTotals());    //总记录数(名称必须为records)


        JSONConvert convert = new JSONConvert();
        rows = convert.modelCollect2JSONArray(supplierVos, new ArrayList());
        jsonObj.put("rows", rows);                            //返回到前台每页显示的数据(名称必须为rows)
        //设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);

    }

    @RequestMapping(params = "method=save")
    public String save(HttpServletRequest request, HttpServletResponse response, SupplierVO supplierVO) throws Exception {

        SupplierInfor supplierInfor = new SupplierInfor();

        BeanUtils.copyProperties(supplierInfor, supplierVO);
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        supplierInfor.setPurchaseType(EnumUtil.getByMsg(supplierVO.getPurchaseTypeMsg(), PurchaseTypeEnum.class).getCode());
        supplierInfor.setSingleSupplier(supplierVO.getSingle().equals("是") ? true : false);
        supplierInfor.setStatus(SupplierStatusEnum.PENDING_REVIEW.getCode());
        supplierInfor.setValid(true);
        SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
        supplierInfor.setSponsor(systemUser);
        supplierInfor.setStartDate(date);
        this.supplierInforManager.save(supplierInfor);
        return "/core/success";
    }

    @RequestMapping(params = "method=saveEdit")
    public String saveEdit(HttpServletRequest request, HttpServletResponse response, SupplierVO supplierVO) throws Exception {

        SupplierInfor supplierInfor = (SupplierInfor) this.supplierInforManager.get(supplierVO.getSupplierID());
        supplierInfor.setPurchaseType(EnumUtil.getByMsg(supplierVO.getPurchaseTypeMsg(), PurchaseTypeEnum.class).getCode());
        supplierInfor.setSingleSupplier(supplierVO.getSingle().equals("是") ? true : false);
        supplierInfor.setSupplierName(supplierVO.getSupplierName());
        supplierInfor.setServiceDetail(supplierVO.getServiceDetail());
        supplierInfor.setServiceYear(supplierVO.getServiceYear());
        supplierInfor.setSupplierContact(supplierVO.getSupplierContact());
        supplierInfor.setSupplierAddress(supplierVO.getSupplierAddress());
        SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
//        supplierInfor.setSponsor(systemUser);
        this.supplierInforManager.save(supplierInfor);
        return "/core/success";
    }

    @RequestMapping(params = "method=canAdd")
    public void canAdd(HttpServletRequest request, HttpServletResponse response) throws Exception {
        RoleInfor roleInfor = (RoleInfor) this.roleManager.get(67);
        SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
        boolean canAdd = false;
        boolean canDelete = false;
        if (this.roleManager.belongRole(systemUser, roleInfor)||systemUser.getUserName().equals("admin")) {
            canAdd = true;
        }
        if (this.roleManager.belongRole(systemUser, roleInfor) || systemUser.getUserName().equals("admin")) {
            canDelete = true;
        }
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("canAdd", canAdd);
        jsonObj.put("canDelete", canDelete);
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);
    }

    //新增或者修改角色信息
    @RequestMapping(params = "method=delete")
    public void delete(HttpServletRequest request, Model model) throws Exception {
        String supplierID = request.getParameter("supplierID");
        if (StringUtil.isNotEmpty(supplierID)) {
            SupplierInfor supplierInfor = (SupplierInfor) this.supplierInforManager.get(Integer.parseInt(supplierID));
            supplierInfor.setValid(false);
            this.supplierInforManager.save(supplierInfor);
        }
    }


    //新增或者修改角色信息
    @RequestMapping(params = "method=edit")
    public String edit(HttpServletRequest request, Model model) throws Exception {
        String supplierID = request.getParameter("supplierID");
        if (StringUtil.isNotEmpty(supplierID)) {
            SupplierInfor supplierInfor = (SupplierInfor) this.supplierInforManager.get(Integer.parseInt(supplierID));
            SupplierVO supplierVO = this.supplierInforManager.transPOToVO(supplierInfor);
            model.addAttribute("supplier", supplierVO);
        }
        return "supplier/modifyInfor";
    }


    //新增或者修改角色信息
    @RequestMapping(params = "method=detail")
    public String detail(HttpServletRequest request, Model model) throws Exception {
        String supplierID = request.getParameter("supplierID");
        if (StringUtil.isNotEmpty(supplierID)) {
            SupplierInfor supplierInfor = (SupplierInfor) this.supplierInforManager.get(Integer.parseInt(supplierID));
            SupplierVO supplierVO = this.supplierInforManager.transPOToVO(supplierInfor);
            model.addAttribute("supplier", supplierVO);
        }
        return "supplier/detail";
    }

    //新增或者修改角色信息
    @RequestMapping(params = "method=editCheck")
    public String editCheck(HttpServletRequest request, Model model) throws Exception {
        session.setAttribute(SESSION_ORDER_TOKEN, UUID.randomUUID().toString());
        String supplierID = request.getParameter("supplierID");
        if (StringUtil.isNotEmpty(supplierID)) {
            SupplierInfor supplierInfor = (SupplierInfor) this.supplierInforManager.get(Integer.parseInt(supplierID));
            if(supplierInfor.getCertifyInfos()!=null&&supplierInfor.getCertifyInfos().size()>0){
                CertifyInfo certifyInfo = supplierInfor.getCertifyInfos().get(supplierInfor.getCertifyInfos().size() - 1);
                model.addAttribute("certify", certifyInfo);
            }
            SupplierVO supplierVO = this.supplierInforManager.transPOToVO(supplierInfor);
            List<SupplierCheckVO> checkVOS = supplierVO.getCheckVOS();
            int i=0;
            int j=0;
            for(SupplierCheckVO checkVO:checkVOS){
                if(checkVO.getLayer()==2&&checkVO.isLastOne()){
                    i++;
                }
                if(checkVO.getLayer()==1&&checkVO.isLastOne()){
                    j++;
                }
            }
            model.addAttribute("lv_1",j);
            model.addAttribute("rows",i);
            model.addAttribute("supplier", supplierVO);
        }
        return "supplier/editCheck";
    }

    //新增或者修改角色信息
    @RequestMapping(params = "method=certify")
    public String certify(HttpServletRequest request, Model model) throws Exception {
        String supplierID = request.getParameter("supplierID");
        if (StringUtil.isNotEmpty(supplierID)) {
            SupplierInfor supplierInfor = (SupplierInfor) this.supplierInforManager.get(Integer.parseInt(supplierID));
            SupplierVO supplierVO = this.supplierInforManager.transPOToVO(supplierInfor);
            model.addAttribute("supplier", supplierVO);
        }
        //根据职级获取用户
        List users = this.systemUserManager.getUserByPosition(PersonInfor._Position_Tag);
        model.addAttribute("_Users", users);

        //获取职级大于一定值的用户
        List otherUsers = this.systemUserManager.getOtherPositionUser(PersonInfor._Position_Tag);
        model.addAttribute("_OtherUsers", otherUsers);

        //全部部门信息
        List departments = this.organizeManager.getDepartments();
        model.addAttribute("_Departments", departments);
        return "supplier/certifyInfo";
    }

    @RequestMapping(params = "method=saveCertify")
    public String saveCertify(HttpServletRequest request, CertifyVO vo) throws Exception {
        CertifyInfo certifyInfo = new CertifyInfo();
        SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
        BeanUtils.copyProperties(certifyInfo, vo);
        certifyInfo.setEndDate(Convert.StrToDate(vo.getEndTime()+" 23:59:59","yyyy-MM-dd HH:mm:ss"));
        certifyInfo.setSponsor(systemUser);
        certifyInfo.setSpoDate(new Date());
        SupplierInfor supplierInfor = (SupplierInfor) this.supplierInforManager.get(vo.getSupplierID());
        supplierInfor.setStatus(SupplierStatusEnum.CERTIFYING.getCode());
        certifyInfo.setSupplierInfor(supplierInfor);
        List<SupplierCheckInfor> checkInfors = supplierInfor.getCheckInfors();
        for(SupplierCheckInfor checkInfor:checkInfors){
            if(checkInfor.getLayer()==2||checkInfor.getLayer()==3){
                checkInfor.setLastOne(false);
                supplierInfor.setExpiration(null);
            }
        }
        int[] personIds = vo.getPersonIds();
        if (personIds != null) {
            for (int k = 0; k < personIds.length; k++) {
                SupplierCheckInfor checkInfor = new SupplierCheckInfor();
                checkInfor.setChecker((SystemUserInfor) this.systemUserManager.get(personIds[k]));
                checkInfor.setSupplierInfor(supplierInfor);
                checkInfor.setLayer(2);
                checkInfor.setLastOne(true);
                this.supplierCheckManager.save(checkInfor);
            }
        }

        this.supplierInforManager.save(supplierInfor);
        this.certifyInfoManager.save(certifyInfo);
        return "core/success";
    }

    @RequestMapping(params = "method=validName")
    public void validName(HttpServletRequest request,HttpServletResponse response)throws Exception{
        String supplierName = request.getParameter("supplierName");
        boolean valid = this.supplierInforManager.validName(supplierName);
        JSONObject jsonObj = new JSONObject();
            jsonObj.put("_Valid", valid);
            response.setContentType(CoreConstant.CONTENT_TYPE);
            response.getWriter().print(jsonObj);
    }
    @RequestMapping(params = "method=view")
    public String view(HttpServletRequest request, Model model) throws Exception {
        String supplierID = request.getParameter("supplierID");
        if (StringUtil.isNotEmpty(supplierID)) {
            SupplierInfor supplierInfor = (SupplierInfor) this.supplierInforManager.get(Integer.parseInt(supplierID));
            SupplierVO supplierVO = this.supplierInforManager.transPOToVO(supplierInfor);
            List<SupplierCheckVO> checkVOS = supplierVO.getCheckVOS();
            int i=0;
            int j=0;
            for(SupplierCheckVO checkVO:checkVOS){
                if(checkVO.getLayer()==2){
                    i++;
                }
                if(checkVO.getLayer()==1){
                    j++;
                }
            }
            model.addAttribute("lv_1",j);
            model.addAttribute("rows",i);
            SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
            model.addAttribute("supplier", supplierVO);

            boolean canReview = false;
            boolean canCetify = false;
            boolean canstartCetify = false;
            boolean notChecked = true;
            Iterator<SupplierCheckInfor> iterator = supplierInfor.getCheckInfors().iterator();
            while (iterator.hasNext()) {
                SupplierCheckInfor next = iterator.next();
                if (next.getChecker().getPersonId().equals(systemUser.getPersonId()) && next.getCheckResult() != null) {
                    notChecked = false;
                    break;
                } else {
                    notChecked = true;
                }
            }

            if (((systemUser.getPersonId().equals(((OrganizeInfor) organizeManager.get(89)).getDirector().getPersonId()) || systemUser.getPersonId().equals(supplierInfor.getSponsor().getPerson().getDepartment().getDirector().getPersonId())) && supplierInfor.getStatus() == 0 && notChecked) || (systemUser.getPersonId().equals(((OrganizeInfor) organizeManager.get(89)).getDirector().getPersonId()) && supplierInfor.getStatus() == 3)) {
                canReview = true;
            }
            if (systemUser.getPersonId().equals(supplierInfor.getSponsor().getPersonId())) {
                canstartCetify = true;
            }
            if (supplierInfor.getStatus() == 2) {
                for (SupplierCheckInfor supplierCheckInfor : supplierInfor.getCheckInfors()) {
                    if (supplierCheckInfor.getChecker().getPersonId().equals(systemUser.getPersonId()) && supplierCheckInfor.getCheckResult() == null && supplierCheckInfor.getLayer() == 2&&supplierCheckInfor.isLastOne()) {
                        canCetify = true;
                        break;
                    }
                }
            }
            if (supplierInfor.getCertifyInfos().size() > 0) {
                CertifyInfo certifyInfo = supplierInfor.getCertifyInfos().get(supplierInfor.getCertifyInfos().size() - 1);
                model.addAttribute("certify", certifyInfo);
            }
            model.addAttribute("_CanCertify", canCetify);
            model.addAttribute("_CanReview", canReview);
            model.addAttribute("_CanStartCetify", canstartCetify);
        }
        return "supplier/viewInstance";
    }

    @RequestMapping(params = "method=check")
    public String check(HttpServletRequest request, SupplierCheckVO checkVO) throws Exception {
        Object obj = session.getAttribute(SESSION_ORDER_TOKEN);//获得令牌
        if (obj == null) return "cggl";
        //移除令牌  无论成功还是失败
        session.removeAttribute(SESSION_ORDER_TOKEN);
        SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
        Integer supplierID = checkVO.getSupplierID();
        Integer checkResult = checkVO.getCheckResult();
        SupplierCheckInfor supplierCheckInfor = new SupplierCheckInfor();
        if (supplierID != null) {
            SupplierInfor supplierInfor = (SupplierInfor) this.supplierInforManager.get(supplierID);
            if (checkResult != null) {
                if (supplierInfor.getStatus().equals(0)) {
                    if (checkResult == 1) {
                        if (supplierInfor.getSponsor().getPerson().getDepartment().getOrganizeName().equals("采购部")) {
                            supplierCheckInfor.setLayer(1);
                            supplierInfor.setStatus(1);
                            supplierCheckInfor.setLastOne(true);
                        } else if (supplierInfor.getCheckInfors().size() == 1) {
                            supplierCheckInfor.setLayer(1);
                            supplierInfor.setStatus(1);
                            supplierCheckInfor.setLastOne(true);
                        }else{
                            supplierCheckInfor.setLayer(1);
                            supplierCheckInfor.setLastOne(true);
                        }
                    } else {
                        supplierCheckInfor.setLayer(1);
                        supplierInfor.setStatus(-1);
                        supplierCheckInfor.setLastOne(true);
                    }
                } else if (supplierInfor.getStatus().equals(3)) {
                    if (checkResult == 1) {
                        supplierCheckInfor.setLayer(3);
                        supplierInfor.setStatus(4);
                        supplierCheckInfor.setLastOne(true);
                        supplierInfor.setExpiration(supplierInfor.getCertifyInfos().get(supplierInfor.getCertifyInfos().size() - 1).getEndDate());
                    } else if (checkResult == 0) {
                        supplierCheckInfor.setLayer(3);
                        supplierInfor.setStatus(1);
                        supplierCheckInfor.setLastOne(true);
                    }
                }

            }
            supplierCheckInfor.setChecker(systemUser);
            supplierCheckInfor.setCheckResult(checkVO.getCheckResult());
            supplierCheckInfor.setCheckOpinion(checkVO.getCheckOpinion());
            supplierCheckInfor.setCheckTime(Convert.StrToDate(checkVO.getCheckDate(),"yyyy-MM-dd HH:mm:ss"));
            supplierCheckInfor.setSupplierInfor(supplierInfor);
            supplierInfor.getCheckInfors().add(supplierCheckInfor);
            this.supplierInforManager.save(supplierInfor);
        }
        return "core/success";
    }

    @RequestMapping(params = "method=cetifing")
    public String cetifing(HttpServletRequest request, SupplierCheckVO checkVO) throws Exception {
        Object obj = session.getAttribute(SESSION_ORDER_TOKEN);//获得令牌
        if (obj == null) return "cggl";
        //移除令牌  无论成功还是失败
        session.removeAttribute(SESSION_ORDER_TOKEN);
        SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
        Integer supplierID = checkVO.getSupplierID();
        if (supplierID != null) {
            SupplierInfor supplierInfor = (SupplierInfor) this.supplierInforManager.get(supplierID);
            SupplierVO supplierVO = this.supplierInforManager.transPOToVO(supplierInfor);
            for (SupplierCheckInfor checkInfor : supplierInfor.getCheckInfors()) {
                if (checkInfor.getChecker().getPersonId().equals(systemUser.getPersonId()) && checkInfor.getLayer() == 2&&checkInfor.isLastOne()) {
                    checkInfor.setChecker(systemUser);
                    checkInfor.setCheckResult(checkVO.getCheckResult());
                    checkInfor.setCheckOpinion(checkVO.getCheckOpinion());
                    checkInfor.setCheckTime(Convert.StrToDate(checkVO.getCheckDate(),"yyyy-MM-dd"));
                }
            }

            if (checkVO.getCheckResult() == 0) {
                supplierInfor.setStatus(SupplierStatusEnum.POTENTIAL.getCode());
            }
            int i = 0;
            for (SupplierCheckInfor checkInfor : supplierInfor.getCheckInfors()) {
                if (checkInfor.getCheckResult() != null && checkInfor.getCheckResult() == 1&&checkInfor.isLastOne()) {
                    i++;
                }
            }
            if (i ==supplierVO.getCheckVOS().size()) {
                supplierInfor.setStatus(SupplierStatusEnum.LEADCHECK.getCode());
            }
            this.supplierInforManager.save(supplierInfor);
        }
        return "core/success";
    }

    //合格认证
    @RequestMapping(params = "method=editCertify")
    public String editCertify(HttpServletRequest request, Model model) throws Exception {
        session.setAttribute(SESSION_ORDER_TOKEN, UUID.randomUUID().toString());
        String supplierID = request.getParameter("supplierID");
        if (StringUtil.isNotEmpty(supplierID)) {
            SupplierInfor supplierInfor = (SupplierInfor) this.supplierInforManager.get(Integer.parseInt(supplierID));
            SupplierVO supplierVO = this.supplierInforManager.transPOToVO(supplierInfor);
            List<SupplierCheckVO> checkVOS = supplierVO.getCheckVOS();
            int i=0;
            int j=0;
            for(SupplierCheckVO checkVO:checkVOS){
                if(checkVO.getLayer()==2&&checkVO.isLastOne()){
                    i++;
                }
                if(checkVO.getLayer()==1&&checkVO.isLastOne()){
                    j++;
                }
            }
            model.addAttribute("lv_1",j);
            model.addAttribute("rows",i);
            model.addAttribute("supplier", supplierVO);
            CertifyInfo certifyInfo = supplierInfor.getCertifyInfos().get(supplierInfor.getCertifyInfos().size() - 1);
            model.addAttribute("certify", certifyInfo);
        }
        return "supplier/editCertify";
    }


    /**
     * 导出excel
     *
     * @param
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(params = "method=expertExcel")
    public String expertExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String[] queryString = new String[2];
            queryString[0] = "from SupplierInfor supplier where 1=1 and valid=1";
            queryString[1] = "select count(supplierID) from SupplierInfor supplier where 1=1 and valid=1";
            queryString = this.supplierInforManager.generateQueryString(queryString, getSearchParams(request));
            String page = request.getParameter("page");        //当前页
            String rowsNum = request.getParameter("rows");    //每页显示的行数
            Pages pages = new Pages(request);
            pages.setPage(Integer.valueOf(page));
            pages.setPerPageNum(Integer.valueOf(rowsNum));

            PageList pl = this.supplierInforManager.getResultByQueryString(queryString[0], queryString[1], true, pages);
            List list = pl.getObjectList();
            //转为以用户名为索引，计数总次数
            /******************导出Excel********************/
            String filePath = "/" + CoreConstant.Attachment_Path + "supplierInfo/";
            String fileTitle = "供应商信息";

            ExcelObject object = new ExcelObject();
            object.setFilePath(filePath);
            object.setFileName(fileTitle);
            object.setTitle(fileTitle);

            List rowName = new ArrayList();
            String[][] data = new String[9][list.size()];
            int k = 9;// 列数

            rowName.add("序号");
            rowName.add("供应商名称");
            rowName.add("联系电话");
            rowName.add("服务明细");
            rowName.add("服务时间");
            rowName.add("采购类型");
            rowName.add("单一供方");
            rowName.add("状态");
            rowName.add("有效期");

            for (int i = 0; i < list.size(); i++) {
                SupplierInfor infor = (SupplierInfor) list.get(i);
                SupplierVO tmpVo = this.supplierInforManager.transPOToVO(infor);
                data[0][i] = String.valueOf(i + 1);
                data[1][i] = tmpVo.getSupplierName();
                data[2][i] = tmpVo.getSupplierContact();
                data[3][i] = tmpVo.getServiceDetail();
                data[4][i] = tmpVo.getServiceYear();
                data[5][i] = tmpVo.getPurchaseTypeMsg();
                data[6][i] = tmpVo.getSingle();
                data[7][i] = tmpVo.getSupplierStatus();
                data[8][i] = tmpVo.getExpirationTime();
            }

            for (int i = 0; i < k; i++) {
                object.addContentListByList(data[i]);
            }
            object.setRowName(rowName);
            ExcelOperate operate = new ExcelOperate();
            try {
                operate.exportExcel(object, list.size(), true, request);
            } catch (IOException e) {
                e.printStackTrace();
            }

            filePath = filePath + fileTitle + ".xls";
            request.getSession().removeAttribute("_File_Path");
            request.getSession().setAttribute("_File_Path", filePath);

        } catch (RuntimeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "/common/download";
    }

    @RequestMapping(params = "method=getNeedDeal")
    public void getNeedDeal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject jsonObj = new JSONObject();
        JSONConvert convert = new JSONConvert();
        JSONArray jsonArray = new JSONArray();
        //通知Convert，哪些关联对象需要获取
        List awareObject = new ArrayList();
        SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
        if (systemUser != null) {
            Integer personId = systemUser.getPersonId();
            String queryHQL = "";
            if (personId == this.organizeManager.findByOrganizeName("采购部").getDirector().getPersonId().intValue()) {
                queryHQL = "from SupplierInfor supplier where (status=0 or status=3) and valid=1";
            }
            List<SupplierInfor> suppliers1 = new ArrayList<>();
            if (StringUtil.isNotEmpty(queryHQL)) {
                suppliers1 = this.supplierInforManager.getResultByQueryString(queryHQL);
            }
            Iterator<SupplierInfor> it = suppliers1.iterator();
            while(it.hasNext()){
                SupplierInfor supplierInfor = it.next();
                if(supplierInfor.getStatus()==0){
                    List<SupplierCheckInfor> checkInfors = supplierInfor.getCheckInfors();
                    for(SupplierCheckInfor checkInfor:checkInfors){
                        if(checkInfor.getChecker().getPersonId().intValue()==systemUser.getPersonId().intValue()){
                            it.remove();
                        }
                    }
                }
            }

            String query1 = "from SupplierInfor supplier where valid=1 and status=0 and supplier.sponsor.person.department.director.personId=" + personId;
            String query2 = "from SupplierInfor supplier where valid=1 and status=5 and (supplier.sponsor.personId=" + personId + " or " +
                    "supplier.sponsor.person.department.director.personId=" + personId + ")";
            String checkHql = "from SupplierCheckInfor where lastOne=1 and checkResult is null  and checker.personId=" + personId;
            List<SupplierInfor> suppliers2 = this.supplierInforManager.getResultByQueryString(query1);
            Iterator<SupplierInfor> it2 = suppliers2.iterator();
            while(it2.hasNext()){
                SupplierInfor supplierInfor = it2.next();
                if(supplierInfor.getStatus()==0){
                    List<SupplierCheckInfor> checkInfors = supplierInfor.getCheckInfors();
                    for(SupplierCheckInfor checkInfor:checkInfors){
                        if(checkInfor.getChecker().getPersonId().intValue()==systemUser.getPersonId().intValue()){
                            it2.remove();
                        }
                    }
                }
            }
            List<SupplierInfor> suppliers3 = this.supplierInforManager.getResultByQueryString(query2);
            List<SupplierCheckInfor> checkInfors = this.supplierCheckManager.getResultByQueryString(checkHql);
            Set<SupplierInfor> suppliers = new HashSet<>();
            suppliers.addAll(suppliers1);
            suppliers.addAll(suppliers2);
            suppliers.addAll(suppliers3);
            for (SupplierCheckInfor checkInfor : checkInfors) {
                SupplierInfor supplierInfor = checkInfor.getSupplierInfor();
                if (supplierInfor.getStatus() == 2 && supplierInfor.isValid()) {
                    suppliers.add(supplierInfor);
                }
            }
            if (suppliers.size() > 0) {
                jsonArray = convert.modelCollect2JSONArray(suppliers, awareObject);
            }
        }
        jsonObj.put("_Instances", jsonArray);
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);
    }

}
