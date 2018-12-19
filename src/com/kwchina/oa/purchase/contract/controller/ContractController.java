package com.kwchina.oa.purchase.contract.controller;

import com.kwchina.core.base.entity.OrganizeInfor;
import com.kwchina.core.base.entity.RoleInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.OrganizeManager;
import com.kwchina.core.base.service.PersonInforManager;
import com.kwchina.core.base.service.RoleManager;
import com.kwchina.core.base.service.SystemUserManager;
import com.kwchina.core.common.controller.BasicController;
import com.kwchina.core.common.page.PageList;
import com.kwchina.core.common.page.Pages;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.core.util.json.JSONConvert;
import com.kwchina.core.util.string.StringUtil;
import com.kwchina.oa.purchase.contract.VO.ContractCheckVo;
import com.kwchina.oa.purchase.contract.VO.ContractListVo;
import com.kwchina.oa.purchase.contract.VO.ContractVo;
import com.kwchina.oa.purchase.contract.entity.ContractCheckInfo;
import com.kwchina.oa.purchase.contract.entity.ContractInfo;
import com.kwchina.oa.purchase.contract.enums.ContractStatusEnum;
import com.kwchina.oa.purchase.contract.service.ContractCheckManager;
import com.kwchina.oa.purchase.contract.service.ContractInfoManager;
import com.kwchina.oa.purchase.sanfang.enums.PurchaseTypeEnum;
import com.kwchina.oa.purchase.sanfang.service.SupplierInforManager;
import com.kwchina.oa.purchase.sanfang.utils.Convert;
import com.kwchina.oa.purchase.sanfang.utils.EnumUtil;
import com.kwchina.oa.purchase.yiban.entity.PurchaseCheckInfor;
import com.kwchina.oa.purchase.yiban.entity.PurchaseInfor;
import com.kwchina.oa.purchase.yiban.service.PurchaseManager;
import com.kwchina.oa.util.SysCommonMethod;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by asus on 2018/7/23.
 */
@Controller
@RequestMapping(value = "contract.do")
public class ContractController extends BasicController {
    @Autowired
    private ContractInfoManager contractInfoManager;
    @Autowired
    private OrganizeManager organizeManager;
    @Autowired
    private PersonInforManager personInforManager;
    @Autowired
    private SupplierInforManager supplierInforManager;
    @Autowired
    private SystemUserManager systemUserManager;
    @Autowired
    private ContractCheckManager contractCheckManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private PurchaseManager purchaseManager;
    @Autowired
    HttpSession session;
    public static final String SESSION_ORDER_TOKEN = "SESSION_ORDER_TOKEN";//订单提交令牌，防止重复提交

    @RequestMapping(params = "method=start")
    public String start(ModelMap modelMap, HttpServletRequest request) throws Exception {
        String pId = request.getParameter("purchaseId");
        Integer purchaseId = Integer.parseInt(pId);
        if (purchaseId != null && !purchaseId.equals("")) {
            PurchaseInfor purchaseInfor = (PurchaseInfor) purchaseManager.get(purchaseId);
            modelMap.addAttribute("purchaseInfor", purchaseInfor);
            List allSupplier = this.supplierInforManager.getInSupplier(EnumUtil.getByMsg(purchaseInfor.getFlowId().getFlowName(),PurchaseTypeEnum.class).getCode());
            modelMap.addAttribute("suppliers", allSupplier);
        }
        List departments = this.organizeManager.getDepartments();
        modelMap.addAttribute("departments", departments);
        return "/contract/contractInstance";
    }

    @RequestMapping(params = "method=view")
    public String view(ModelMap modelMap, HttpServletRequest request) {
        int contractID = Integer.parseInt(request.getParameter("contractID"));

        ContractInfo contractInfo = (ContractInfo) this.contractInfoManager.get(contractID);
        PurchaseInfor purchaseInfor = (PurchaseInfor) this.purchaseManager.get(contractInfo.getPurchaseId());

        int layer1 = 0;
        int layer2 = 0;
        List<ContractCheckInfo> checkInfors = contractInfo.getCheckInfos();
        for (ContractCheckInfo checkInfor : checkInfors) {
            if (checkInfor.getLayer() == 3) {
                layer1++;
            }
            if (checkInfor.getLayer() == 2) {
                layer2++;
            }
        }
        modelMap.addAttribute("layers", layer1);
        modelMap.addAttribute("layer2", layer2);

        modelMap.addAttribute("contractInfo", contractInfo);
        modelMap.addAttribute("purchaseInfo", purchaseInfor);
        modelMap.addAttribute("purchaseLeader", ((OrganizeInfor) this.organizeManager.get(89)).getDirector().getPersonName());
        if(purchaseInfor.getGuikouDepartment()!=null&&purchaseInfor.getGuikouDepartment().getOrganizeName().equals("技术规划部")){
            if(StringUtil.isNotEmpty(purchaseInfor.getJigui())){
                List<SystemUserInfor> jgers=new ArrayList<>();
                String jg=purchaseInfor.getJigui();
                for(int i=0;i<jg.split(",").length;i++){
                    SystemUserInfor jger  = (SystemUserInfor) this.systemUserManager.get(Integer.parseInt(jg.split(",")[i]));
                    jgers.add(jger);
                }
                modelMap.addAttribute("jgers",jgers);
            }
        }
        String contractAttach = contractInfo.getContractAttach();
        String solutionAttach = contractInfo.getSolutionAttach();
        if (solutionAttach != null && !solutionAttach.equals("")) {
            String[][] attachment = processFile(solutionAttach);
            modelMap.addAttribute("solutionAttachNames", attachment[1]);
            modelMap.addAttribute("solutionAttachments", attachment[0]);
        }
        if (contractAttach != null && !contractAttach.equals("")) {
            String[][] attachment = processFile(contractAttach);
            modelMap.addAttribute("contractAttachNames", attachment[1]);
            modelMap.addAttribute("contractAttachments", attachment[0]);
        }

        boolean canReview = false;
        SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
        Iterator<ContractCheckInfo> iterator = contractInfo.getCheckInfos().iterator();
        boolean isChecked = false;//判断是否已经审核过
        boolean inGroup = false;
        Iterator<SystemUserInfor> iterator1 = ((RoleInfor) this.roleManager.get(57)).getUsers().iterator();
        while (iterator1.hasNext()) {
            if (iterator1.next().getPersonId().equals(systemUser.getPersonId())) {
                inGroup = true;
                break;
            }
        }
        while (iterator.hasNext()) {
            ContractCheckInfo next = iterator.next();
            if (next.getChecker().getPersonId().equals(systemUser.getPersonId()) && next.getCheckResult() != null) {
                isChecked = false;
                break;
            } else {
                isChecked = true;
            }
        }
        if ((systemUser.getPersonId().equals(((OrganizeInfor) this.organizeManager.get(89)).getDirector().getPersonId()) && contractInfo.getContractStatus() == 1) || (systemUser.getPersonId().equals(purchaseInfor.getGuikouDepartment().getDirector().getPersonId()) && contractInfo.getContractStatus() == 2 && !purchaseInfor.getGuikouDepartment().getOrganizeName().equals("技术规划部")) || (inGroup && contractInfo.getContractStatus() == 3 && isChecked)) {
            canReview = true;
        }
        if (purchaseInfor.getGuikouDepartment() != null && purchaseInfor.getGuikouDepartment().getOrganizeName().equals("技术规划部")&&contractInfo.getContractStatus()==2) {
            if (StringUtil.isNotEmpty(purchaseInfor.getJigui())) {
                String jg = purchaseInfor.getJigui();
                for (int i = 0; i < jg.split(",").length; i++) {
                    SystemUserInfor jiger = (SystemUserInfor) this.systemUserManager.get(Integer.parseInt(jg.split(",")[i]));
                    if (systemUser.getPerson().getPersonName().equals(jiger.getPerson().getPersonName())&& isChecked) {
                        canReview = true;
                    }
                }
            }
        }
        modelMap.addAttribute("_CanReview", canReview);
        return "/contract/viewInstance";
    }

    @RequestMapping(params = "method=save", method = RequestMethod.POST)
    public String save(ContractVo contractVo, ModelMap modelMap, HttpServletRequest request, DefaultMultipartHttpServletRequest multipartRequest) throws Exception {

        PurchaseInfor purchaseInfor = (PurchaseInfor) this.purchaseManager.get(contractVo.getPurchaseId());
        ContractInfo contractInfo = new ContractInfo();
        BeanUtils.copyProperties(contractVo, contractInfo);
        contractInfo.setContractStatus(ContractStatusEnum.PURCHASE_LEADR.getCode());
        contractInfo.setContractTime(Convert.StrToDate(contractVo.getContractDate(),"yyyy-MM-dd HH:mm:ss"));
        contractInfo.setExecutor((SystemUserInfor) this.systemUserManager.get(this.personInforManager.findPersonByName(contractVo.getExecutorName()).getPersonId()));
        contractInfo.setSubmitDate(Convert.StrToDate(contractVo.getApplyDate(),"yyyy-MM-dd"));
        contractInfo.setPurchaseType((EnumUtil.getByMsg(contractVo.getPurchaseTypeName(), PurchaseTypeEnum.class).getCode()));
        contractInfo.setContractStatus(ContractStatusEnum.PURCHASE_LEADR.getCode());
        contractInfo.setSubmitDepartment(organizeManager.findByOrganizeName(contractVo.getApplyDept()));
        contractInfo.setContractApplier((SystemUserInfor) this.systemUserManager.get(this.personInforManager.findPersonByName(contractVo.getApplierName()).getPersonId()));
        String attachment = this.uploadAttachment(multipartRequest, "contract");
        String[] attachs = cutOffattach(attachment);
        int attNum = 0;
        if (!contractVo.getCav().equals("") && attNum < attachs.length) {
            contractInfo.setContractAttach(attachs[attNum]);
            attNum++;
        }
        if (!contractVo.getSav().equals("") && attNum < attachs.length) {
            contractInfo.setSolutionAttach(attachs[attNum]);
            attNum++;
        }

        List<ContractCheckInfo> checkList = new ArrayList<>();
        ContractCheckInfo purchaseCharge = new ContractCheckInfo();
        purchaseCharge.setChecker(this.organizeManager.findByOrganizeName("采购部").getDirector().getUser());
        purchaseCharge.setContractInfo(contractInfo);
        purchaseCharge.setLayer(1);
        checkList.add(purchaseCharge);
        if(purchaseInfor.getGuikouDepartment().getOrganizeName().equals("技术规划部")) {
            if(StringUtil.isNotEmpty(purchaseInfor.getJigui())){
                String[] split = purchaseInfor.getJigui().split(",");
                for(int i=0;i<split.length;i++){
                    ContractCheckInfo relatate = new ContractCheckInfo();
                    relatate.setLayer(2);
                    relatate.setContractInfo(contractInfo);
                    relatate.setChecker((SystemUserInfor)this.systemUserManager.get(Integer.parseInt(split[i])));
                    checkList.add(relatate);
                }
            }

        }else if(purchaseInfor.getGuikouDepartment()!=null) {
            ContractCheckInfo relatate = new ContractCheckInfo();
            relatate.setLayer(2);
            relatate.setContractInfo(contractInfo);
            relatate.setChecker(purchaseInfor.getGuikouDepartment().getDirector().getUser());
            checkList.add(relatate);
        }
        RoleInfor roleInfor = (RoleInfor) this.roleManager.get(57);
        Iterator<SystemUserInfor> iterator = roleInfor.getUsers().iterator();
        while (iterator.hasNext()) {
            ContractCheckInfo checkInfo = new ContractCheckInfo();
            checkInfo.setChecker(iterator.next());
            checkInfo.setContractInfo(contractInfo);
            checkInfo.setLayer(3);
            checkList.add(checkInfo);
        }

        contractInfo.setCheckInfos(checkList);
        ContractInfo save = (ContractInfo) this.contractInfoManager.save(contractInfo);
        purchaseInfor.setHetongId(save.getContractID());
        purchaseManager.save(purchaseInfor);
        return "core/success";
    }

    @RequestMapping(params = "method=contractList")
    public void contractList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //构造查询语句
        String[] queryString = this.contractInfoManager.generateQueryString("ContractInfo", "contractID", getSearchParams(request));

        String page = request.getParameter("page");        //当前页
        String rowsNum = request.getParameter("rows");    //每页显示的行数
        String type=request.getParameter("type");
        Pages pages = new Pages(request);
        pages.setPage(Integer.valueOf(page));
        pages.setPerPageNum(Integer.valueOf(rowsNum));
        SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);

        PageList pl = this.contractInfoManager.getResultByQueryString(queryString[0], queryString[1], true, pages);
        List list = pl.getObjectList();
        List fList = new ArrayList<>();
        if (StringUtil.isNotEmpty(type)) {
            Integer state = Integer.parseInt(type);
            for (Iterator it = list.iterator(); it.hasNext(); ) {
                ContractInfo contractInfo= (ContractInfo) it.next();
                if (contractInfo.getPurchaseType().equals(state)) {
                    fList.add(contractInfo);
                }
            }
        } else {
            fList = list;
        }
        // 把查询到的结果转化为VO
        Set<ContractListVo> contractListVos = new HashSet<>();
        if (fList.size() > 0) {
            for (Iterator it = fList.iterator(); it.hasNext(); ) {
                ContractInfo contractInfo = (ContractInfo) it.next();
                // 把查询到的结果转化为VO
                if(contractInfo.getExecutor().getPersonId().intValue()==systemUser.getPersonId().intValue()||systemUser.getUserName().equals("admin")){
                    ContractListVo contractListVo = this.contractInfoManager.transPOToVO(contractInfo);
                    contractListVos.add(contractListVo);
                }
                List<ContractCheckInfo> checkInfos = contractInfo.getCheckInfos();
                for(ContractCheckInfo checkInfo:checkInfos){
                    if(checkInfo.getChecker().getPersonId().intValue()==systemUser.getPersonId().intValue()){
                        ContractListVo contractListVo = this.contractInfoManager.transPOToVO(contractInfo);
                        contractListVos.add(contractListVo);
                    }
                }
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
        rows = convert.modelCollect2JSONArray(contractListVos, new ArrayList());
        jsonObj.put("rows", rows);                            //返回到前台每页显示的数据(名称必须为rows)
        //设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);
    }

    //
    @RequestMapping(params = "method=check")
    public String check(ContractCheckVo checkVo, HttpServletRequest request, DefaultMultipartHttpServletRequest multipartRequest) throws Exception {
        Object obj = session.getAttribute(SESSION_ORDER_TOKEN);//获得令牌
        if (obj == null) return "homepage";
        //移除令牌  无论成功还是失败
        session.removeAttribute(SESSION_ORDER_TOKEN);
        Integer contractID = checkVo.getContractID();
        if (contractID != null) {
            ContractInfo contractInfo = (ContractInfo) this.contractInfoManager.get(contractID);
            PurchaseInfor purchaseInfor = (PurchaseInfor) this.purchaseManager.get(contractInfo.getPurchaseId());
            List<ContractCheckInfo> checkInfos = contractInfo.getCheckInfos();
            int i = 0;
            int j=0;
            String attachment = this.uploadAttachment(multipartRequest, "htCheck");
            for (ContractCheckInfo checkInfo : checkInfos) {
                if (checkInfo.getChecker().getPersonId().equals(SysCommonMethod.getSystemUser(request).getPersonId())) {
                    checkInfo.setCheckResult(checkVo.getCheckResult());
                    checkInfo.setCheckTime(Convert.StrToDate(checkVo.getCheckDate(),"yyyy-MM-dd HH:mm:ss"));
                    checkInfo.setCheckOpinion(checkVo.getCheckMemo());
                    checkInfo.setCheckAttach(attachment);
                }
                if (checkInfo.getCheckResult() != null) {
                    i++;
                }
                if(checkInfo.getLayer()==2){
                    j++;
                }
                if (checkInfo.getCheckResult() != null && checkInfo.getCheckResult().equals(0)) {
                    contractInfo.setContractStatus(ContractStatusEnum.STOP.getCode());
                    purchaseInfor.setHetongId(-1);
                    break;
                }
            }
            if (i==1){
                contractInfo.setContractStatus(ContractStatusEnum.RELATED_DEPT.getCode());
            }
            if((i==2&&j==1)||(i==3&&j==2)){
                contractInfo.setContractStatus(ContractStatusEnum.LEAD_GROUP.getCode());
            }
            if (i == checkInfos.size()) {
                contractInfo.setContractStatus(ContractStatusEnum.ENDING.getCode());
                purchaseInfor.setPurchaseStatus(10);
            }
            this.purchaseManager.save(purchaseInfor);
            this.contractInfoManager.save(contractInfo);
        }
        return "/core/success";

    }

    @RequestMapping(params = "method=edit")
    public String edit(HttpServletRequest request, ModelMap modelMap) {
        session.setAttribute(SESSION_ORDER_TOKEN, UUID.randomUUID().toString());
        int contractID = Integer.parseInt(request.getParameter("contractID"));
        ContractInfo contractInfo = (ContractInfo) this.contractInfoManager.get(contractID);
        PurchaseInfor purchaseInfo = (PurchaseInfor) this.purchaseManager.get(contractInfo.getPurchaseId());
        modelMap.addAttribute("contractInfo", contractInfo);
        modelMap.addAttribute("purchaseInfo", purchaseInfo);
        if(purchaseInfo.getGuikouDepartment()!=null&&purchaseInfo.getGuikouDepartment().getOrganizeName().equals("技术规划部")){
            if(StringUtil.isNotEmpty(purchaseInfo.getJigui())){
                List<SystemUserInfor> jgers=new ArrayList<>();
                String jg=purchaseInfo.getJigui();
                for(int i=0;i<jg.split(",").length;i++){
                    SystemUserInfor jger  = (SystemUserInfor) this.systemUserManager.get(Integer.parseInt(jg.split(",")[i]));
                    jgers.add(jger);
                }
                modelMap.addAttribute("jgers",jgers);
            }
        }
        return "contract/editCheck";
    }

    @RequestMapping(params = "method=getNeedDeal")
    public void getNeedDeal(HttpServletRequest request,HttpServletResponse response) throws Exception {
        JSONObject jsonObj = new JSONObject();
        JSONConvert convert = new JSONConvert();
        JSONArray jsonArray = new JSONArray();
        //通知Convert，哪些关联对象需要获取
        List awareObject = new ArrayList();
        SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
        if (systemUser!=null){
            Integer personId = systemUser.getPersonId();
            Set<ContractInfo> allResult = new HashSet<>();

            String checkHql = "from ContractCheckInfo where checkResult is null  and checker.personId=" + personId;
            List<ContractCheckInfo> checkInfors = this.contractCheckManager.getResultByQueryString(checkHql);
            for (ContractCheckInfo checkInfor : checkInfors) {
                ContractInfo contractInfo = checkInfor.getContractInfo();
                allResult.add(contractInfo);
            }

            if (allResult.size() > 0) {
                jsonArray = convert.modelCollect2JSONArray(allResult, awareObject);
            }
        }
        jsonObj.put("_Instances", jsonArray);
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);
    }
}

