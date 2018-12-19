package com.kwchina.oa.purchase.sanfang.controller;

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
import com.kwchina.oa.purchase.sanfang.VO.FormVo;
import com.kwchina.oa.purchase.sanfang.VO.SanfangListVO;
import com.kwchina.oa.purchase.sanfang.entity.SanfangCheckInfor;
import com.kwchina.oa.purchase.sanfang.entity.SanfangInfor;
import com.kwchina.oa.purchase.sanfang.entity.SupplierInfor;
import com.kwchina.oa.purchase.sanfang.entity.SupplierMsg;
import com.kwchina.oa.purchase.sanfang.enums.PurchaseTypeEnum;
import com.kwchina.oa.purchase.sanfang.enums.SanfangfStatusEnum;
import com.kwchina.oa.purchase.sanfang.service.SanfangCheckManager;
import com.kwchina.oa.purchase.sanfang.service.SanfangInforManager;
import com.kwchina.oa.purchase.sanfang.service.SupplierInforManager;
import com.kwchina.oa.purchase.sanfang.utils.Convert;
import com.kwchina.oa.purchase.sanfang.utils.EnumUtil;
import com.kwchina.oa.purchase.yiban.entity.PurchaseInfor;
import com.kwchina.oa.purchase.yiban.service.PurchaseManager;
import com.kwchina.oa.util.SysCommonMethod;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by asus on 2018/7/23.
 */
@Controller
@RequestMapping(value = "review.do")
public class SanfangController extends BasicController {
    @Autowired
    private SanfangInforManager sanfangInforManager;
    @Autowired
    private OrganizeManager organizeManager;
    @Autowired
    private PersonInforManager personInforManager;
    @Autowired
    private SupplierInforManager supplierInforManager;
    @Autowired
    private SystemUserManager systemUserManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private PurchaseManager purchaseManager;
    @Autowired
    private SanfangCheckManager sanfangCheckManager;
    @Autowired
    HttpSession session;
    public static final String SESSION_ORDER_TOKEN = "SESSION_ORDER_TOKEN";//订单提交令牌，防止重复提交

    @RequestMapping(params = "method=start")
    public String start(ModelMap modelMap, HttpServletRequest request) {
        String pId = request.getParameter("purchaseId");
        Integer purchaseId = Integer.parseInt(pId);
//        modelMap.addAttribute("purchaseId", purchaseId);
        if (purchaseId != null && !purchaseId.equals("")) {
            PurchaseInfor purchaseInfor = (PurchaseInfor) purchaseManager.get(purchaseId);
            String memo = purchaseInfor.getApplication();
            String purchaseType = purchaseInfor.getFlowId().getFlowName();
            SystemUserInfor applier = purchaseInfor.getApplier();
            OrganizeInfor applyDept = purchaseInfor.getDepartment();
            Timestamp startTime = purchaseInfor.getStartTime();
            List allSupplier = this.supplierInforManager.getInSupplier(EnumUtil.getByMsg(purchaseType,PurchaseTypeEnum.class).getCode());
            modelMap.addAttribute("suppliers", allSupplier);
            modelMap.addAttribute("purchaseId", purchaseId);
            modelMap.addAttribute("memo", memo);
            modelMap.addAttribute("purchaseType", purchaseType);
            modelMap.addAttribute("applier", applier);
            modelMap.addAttribute("applyDept", applyDept);
            modelMap.addAttribute("applyDate", startTime);
        }
        List departments = this.organizeManager.getDepartments();
        modelMap.addAttribute("departments", departments);

        return "/sfbj/sanfangInstance";
    }

    @RequestMapping(params = "method=view")
    public String view(ModelMap modelMap, HttpServletRequest request) {
        int sanfangID = Integer.parseInt(request.getParameter("sanfangID"));
        SanfangInfor sanfangInfor = (SanfangInfor) sanfangInforManager.get(sanfangID);
        PurchaseInfor purchaseInfor = (PurchaseInfor) this.purchaseManager.get(sanfangInfor.getPurchaseId());
        int layer1 = 0;
        int layer2 = 0;
        List<SanfangCheckInfor> checkInfors = sanfangInfor.getCheckInfors();
        for (SanfangCheckInfor sanfangCheckInfor : checkInfors) {
            if (sanfangCheckInfor.getLayer() == 3) {
                layer1++;
            }
            if (sanfangCheckInfor.getLayer() == 2) {
                layer2++;
            }
        }
        modelMap.addAttribute("layers", layer1);
        modelMap.addAttribute("layer2", layer2);
        modelMap.addAttribute("sanfangInfor", sanfangInfor);
        modelMap.addAttribute("purchaseInfo", purchaseInfor);
        if (purchaseInfor.getGuikouDepartment() != null) {
            if (purchaseInfor.getGuikouDepartment().getOrganizeName().equals("技术规划部")) {
                if (StringUtil.isNotEmpty(purchaseInfor.getJigui())) {
                    List<SystemUserInfor> jgers = new ArrayList<>();
                    String jg = purchaseInfor.getJigui();
                    for (int i = 0; i < jg.split(",").length; i++) {
                        SystemUserInfor jger = (SystemUserInfor) this.systemUserManager.get(Integer.parseInt(jg.split(",")[i]));
                        jgers.add(jger);
                    }
                    modelMap.addAttribute("jgers", jgers);
                }
            }
        }
        modelMap.addAttribute("purchaseLeader", ((OrganizeInfor) organizeManager.get(89)).getDirector().getPersonName());
        String sanfangAttachFile = sanfangInfor.getSanfangAttach();
        if (sanfangAttachFile != null && !sanfangAttachFile.equals("")) {
            String[][] attachment = processFile(sanfangAttachFile);
            modelMap.addAttribute("sanfangAttachNames", attachment[1]);
            modelMap.addAttribute("sanfangAttachments", attachment[0]);
        }
        List<SupplierMsg> supplierMsgs = sanfangInfor.getSupplierInfors();
        for (int i = 0; i < supplierMsgs.size(); i++) {
            String supplierAttach = supplierMsgs.get(i).getSupplierAttach();
            if (supplierAttach != null && !supplierAttach.equals("")) {
                String[][] attachment = processFile(supplierAttach);
                modelMap.addAttribute("supplierAttachNames" + i, attachment[1]);
                modelMap.addAttribute("supplierAttachments" + i, attachment[0]);
            }
        }
        boolean canReview = false;
        SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
        Iterator<SanfangCheckInfor> iterator = sanfangInfor.getCheckInfors().iterator();
        boolean isChecked = false;//判断是否已经审核过
        boolean inGroup = false;
        Iterator<SystemUserInfor> iterator1 = ((RoleInfor) roleManager.get(57)).getUsers().iterator();
        while (iterator1.hasNext()) {
            if (iterator1.next().getPersonId().equals(systemUser.getPersonId())) {
                inGroup = true;
                break;
            }
        }
        while (iterator.hasNext()) {
            SanfangCheckInfor next = iterator.next();
            if (next.getChecker().getPersonId().equals(systemUser.getPersonId()) && next.getCheckResult() != null) {
                isChecked = false;
                break;
            } else {
                isChecked = true;
            }
        }
        if ((systemUser.getPersonId().equals(((OrganizeInfor) organizeManager.get(89)).getDirector().getPersonId()) && sanfangInfor.getSanfangStatus() == 1) || (purchaseInfor.getGuikouDepartment() != null && systemUser.getPersonId().equals(purchaseInfor.getGuikouDepartment().getDirector().getPersonId()) && sanfangInfor.getSanfangStatus() == 2 && !purchaseInfor.getGuikouDepartment().getOrganizeName().equals("技术规划部")) || (inGroup && sanfangInfor.getSanfangStatus() == 3 && isChecked)) {
            canReview = true;
        }
        if (purchaseInfor.getGuikouDepartment() != null && purchaseInfor.getGuikouDepartment().getOrganizeName().equals("技术规划部") && sanfangInfor.getSanfangStatus() == 2) {
            if (StringUtil.isNotEmpty(purchaseInfor.getJigui())) {
                String jg = purchaseInfor.getJigui();
                for (int i = 0; i < jg.split(",").length; i++) {
                    SystemUserInfor jiger = (SystemUserInfor) this.systemUserManager.get(Integer.parseInt(jg.split(",")[i]));
                    if (systemUser.getPerson().getPersonName().equals(jiger.getPerson().getPersonName()) && isChecked) {
                        canReview = true;
                    }
                }
            }
        }

        modelMap.addAttribute("_CanReview", canReview);
        return "sfbj/viewInstance";
    }

    @RequestMapping(params = "method=contact", method = RequestMethod.POST)
    @ResponseBody
    public String contact(HttpServletRequest request) {
        String supplierName = request.getParameter("supplierName");
        String contact = supplierInforManager.getContactByName(supplierName);
        return contact;
    }

    @RequestMapping(params = "method=save", method = RequestMethod.POST)
    public String save(FormVo formVo, ModelMap modelMap, HttpServletRequest request, DefaultMultipartHttpServletRequest multipartRequest) throws Exception {
        SanfangInfor sanfangInfor = new SanfangInfor();
        sanfangInfor.setSanfangStatus(SanfangfStatusEnum.PURCHASE_LEADR.getCode());
        sanfangInfor.setApplyDate(Convert.StrToDate(formVo.getApplyDate(), "yyyy-MM-dd"));
        SystemUserInfor purchaseCharge = (SystemUserInfor) this.systemUserManager.get(personInforManager.findPersonByName(formVo.getPurchaseCharge()).getPersonId());
        sanfangInfor.setPurchaseCharge(purchaseCharge);
        SystemUserInfor applier = (SystemUserInfor) this.systemUserManager.get(personInforManager.findPersonByName(formVo.getSanfangApplier()).getPersonId());
        sanfangInfor.setSanfangApplier(applier);
        OrganizeInfor department = this.organizeManager.findByOrganizeName(formVo.getSanfangDepartment());
        sanfangInfor.setSanfangDepartment(department);
        sanfangInfor.setPurchaseType(formVo.getPurchaseType());
        sanfangInfor.setSanfangConclusion(formVo.getSanfangConclusion());
        sanfangInfor.setSanfangFinalSupplier(formVo.getSanfangFinalSupplier());
        sanfangInfor.setSanfangDesc(formVo.getSanfangDesc());
        sanfangInfor.setStartDate(Convert.StrToDate(formVo.getStartDate(), "yyyy-MM-dd HH:mm:ss"));
        sanfangInfor.setSanfangTitle(formVo.getSanfangTitle());
        sanfangInfor.setPurchaseId(formVo.getPurchaseId());
        String attachment = this.uploadAttachment(multipartRequest, "sanfang");
        String[] attachs = cutOffattach(attachment);
        int attNum = 0;
        if (!formVo.getSav().equals("") && attNum < attachs.length) {
            sanfangInfor.setSanfangAttach(attachs[attNum]);
            attNum++;
        }
        List<SupplierMsg> supplierMsgs = new ArrayList<>();
        SupplierMsg supplierMsg1 = new SupplierMsg();
        SupplierInfor supplierInfor1 = supplierInforManager.findBySupplierName(formVo.getSupplier1());
        supplierMsg1.setSupplierInfor(supplierInfor1);
        supplierMsg1.setPrice(formVo.getPrice1());
        supplierMsg1.setSupplierDesc(formVo.getSupplierMemo1());
        if (!formVo.getSav1().equals("") && attNum < attachs.length) {
            supplierMsg1.setSupplierAttach(attachs[attNum]);
            attNum++;
        }
        supplierMsg1.setSanfangInfor(sanfangInfor);
        SupplierMsg supplierMsg2 = new SupplierMsg();
        SupplierInfor supplierInfor2 = supplierInforManager.findBySupplierName(formVo.getSupplier2());
        supplierMsg2.setSupplierInfor(supplierInfor2);
        supplierMsg2.setPrice(formVo.getPrice2());
        supplierMsg2.setSupplierDesc(formVo.getSupplierMemo2());
        if (!formVo.getSav2().equals("") && attNum < attachs.length) {
            supplierMsg2.setSupplierAttach(attachs[attNum]);
            attNum++;
        }

        supplierMsg2.setSanfangInfor(sanfangInfor);
        SupplierMsg supplierMsg3 = new SupplierMsg();
        SupplierInfor supplierInfor3 = supplierInforManager.findBySupplierName(formVo.getSupplier3());
        supplierMsg3.setSupplierInfor(supplierInfor3);
        supplierMsg3.setPrice(formVo.getPrice3());
        supplierMsg3.setSupplierDesc(formVo.getSupplierMemo3());
        if (!formVo.getSav3().equals("") && attNum < attachs.length) {
            supplierMsg3.setSupplierAttach(attachs[attNum]);
            attNum++;
        }
        supplierMsg3.setSanfangInfor(sanfangInfor);
        supplierMsgs.add(supplierMsg1);
        supplierMsgs.add(supplierMsg2);
        supplierMsgs.add(supplierMsg3);
        sanfangInfor.setSupplierInfors(supplierMsgs);
        List<SanfangCheckInfor> list = new ArrayList<SanfangCheckInfor>();
        sanfangInfor.setCheckInfors(list);
        SanfangInfor save = (SanfangInfor) sanfangInforManager.save(sanfangInfor);
        PurchaseInfor purchaseInfor = (PurchaseInfor) purchaseManager.get(formVo.getPurchaseId());
        purchaseInfor.setSanfangId(save.getSanfangID());
        purchaseManager.save(purchaseInfor);
        return "core/success";
    }

    @RequestMapping(params = "method=check")
    public String check(FormVo formVo, HttpServletRequest request, DefaultMultipartHttpServletRequest multipartRequest) throws Exception {
        Object obj = session.getAttribute(SESSION_ORDER_TOKEN);//获得令牌
        if (obj == null) return "homepage";
        //移除令牌  无论成功还是失败
        session.removeAttribute(SESSION_ORDER_TOKEN);
        Integer sanfangID = formVo.getSanfangID();
        SanfangInfor sanfangInfor = (SanfangInfor) this.sanfangInforManager.get(sanfangID);
        PurchaseInfor purchaseInfor = (PurchaseInfor) purchaseManager.get(sanfangInfor.getPurchaseId());
        List<SanfangCheckInfor> checkInfors = sanfangInfor.getCheckInfors();
        String attachment = this.uploadAttachment(multipartRequest, "sfCheck");
        if (sanfangInfor.getSanfangStatus() == 1) {
            SanfangCheckInfor sanfangCheckInfor = new SanfangCheckInfor();
            sanfangCheckInfor.setChecker(SysCommonMethod.getSystemUser(request));
            sanfangCheckInfor.setCheckAttach(attachment);
            sanfangCheckInfor.setCheckOpinion(formVo.getCheckOpinion());
            sanfangCheckInfor.setCheckTime(Convert.StrToDate(formVo.getCheckDate(), "yyyy-MM-dd HH:mm:ss"));
            sanfangCheckInfor.setCheckResult(formVo.getCheckResult());
            sanfangCheckInfor.setSanfangInfor(sanfangInfor);
            sanfangCheckInfor.setType(1);
            sanfangCheckInfor.setLayer(1);
            checkInfors.add(sanfangCheckInfor);
        } else if (sanfangInfor.getSanfangStatus() == 2) {
            SanfangCheckInfor sanfangCheckInfor = new SanfangCheckInfor();
            sanfangCheckInfor.setChecker(SysCommonMethod.getSystemUser(request));
            sanfangCheckInfor.setCheckAttach(attachment);
            sanfangCheckInfor.setCheckOpinion(formVo.getCheckOpinion());
            sanfangCheckInfor.setCheckTime(Convert.StrToDate(formVo.getCheckDate(), "yyyy-MM-dd HH:mm:ss"));
            sanfangCheckInfor.setCheckResult(formVo.getCheckResult());
            sanfangCheckInfor.setSanfangInfor(sanfangInfor);
            sanfangCheckInfor.setType(1);
            sanfangCheckInfor.setLayer(2);
            checkInfors.add(sanfangCheckInfor);
            if (purchaseInfor.getGuikouDepartment() != null && purchaseInfor.getGuikouDepartment().getOrganizeName().equals("技术规划部")) {
                if (StringUtil.isNotEmpty(purchaseInfor.getJigui())) {
                    String jg = purchaseInfor.getJigui();
                    if (sanfangInfor.getCheckInfors().size() - 1 == jg.split(",").length) {
                        Iterator<SystemUserInfor> iterator = ((RoleInfor) roleManager.get(57)).getUsers().iterator();
                        while (iterator.hasNext()) {
                            SanfangCheckInfor checkInfor = new SanfangCheckInfor();
                            checkInfor.setChecker(iterator.next());
                            checkInfor.setSanfangInfor(sanfangInfor);
                            checkInfor.setRoleInfor((RoleInfor) roleManager.get(57));
                            checkInfor.setType(1);
                            checkInfor.setLayer(3);
                            checkInfors.add(checkInfor);
                        }
                        sanfangInfor.setSanfangStatus(sanfangInfor.getSanfangStatus() + 1);
                    }
                }
            }
            if (formVo.getCheckResult() == 1 && (!purchaseInfor.getGuikouDepartment().getOrganizeName().equals("技术规划部"))) {
                Iterator<SystemUserInfor> iterator = ((RoleInfor) roleManager.get(57)).getUsers().iterator();
                while (iterator.hasNext()) {
                    SanfangCheckInfor checkInfor = new SanfangCheckInfor();
                    checkInfor.setChecker(iterator.next());
                    checkInfor.setSanfangInfor(sanfangInfor);
                    checkInfor.setRoleInfor((RoleInfor) roleManager.get(57));
                    checkInfor.setType(1);
                    checkInfor.setLayer(3);
                    checkInfors.add(checkInfor);
                }
                sanfangInfor.setSanfangStatus(sanfangInfor.getSanfangStatus() + 1);
            }
        } else if (sanfangInfor.getSanfangStatus() == 3) {
            SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
            Iterator<SanfangCheckInfor> iterator = sanfangInfor.getCheckInfors().iterator();
            while (iterator.hasNext()) {
                SanfangCheckInfor checkInfor = iterator.next();
                if (checkInfor.getChecker().getPersonId().equals(systemUser.getPersonId())) {
                    checkInfor.setSanfangInfor(sanfangInfor);
                    checkInfor.setCheckResult(formVo.getCheckResult());
                    checkInfor.setCheckTime(Convert.StrToDate(formVo.getCheckDate(), "yyyy-MM-dd HH:mm:ss"));
                    checkInfor.setCheckOpinion(formVo.getCheckOpinion());
                    checkInfor.setCheckAttach(attachment);
                    break;
                }
            }
        }
        sanfangInfor.setCheckInfors(checkInfors);
        if (sanfangInfor.getSanfangStatus() == 3) {
            int i = 0;
            Iterator<SanfangCheckInfor> iterator = sanfangInfor.getCheckInfors().iterator();
            while (iterator.hasNext()) {
                SanfangCheckInfor checkInfor = iterator.next();
                if (checkInfor.getCheckResult() != null && checkInfor.getCheckResult() == 1) {
                    i++;
                } else {
                    break;
                }
                if (i == sanfangInfor.getCheckInfors().size()) {
                    sanfangInfor.setSanfangStatus(sanfangInfor.getSanfangStatus() + 1);
                    purchaseInfor.setPurchaseStatus(10);
                }
            }
        }
        if (formVo.getCheckResult() == 1 && sanfangInfor.getSanfangStatus() == 1) {
            if (sanfangInfor.getPurchaseType().equals("业务采购")) {
                Iterator<SystemUserInfor> iterator = ((RoleInfor) roleManager.get(57)).getUsers().iterator();
                while (iterator.hasNext()) {
                    SanfangCheckInfor checkInfor = new SanfangCheckInfor();
                    checkInfor.setChecker(iterator.next());
                    checkInfor.setSanfangInfor(sanfangInfor);
                    checkInfor.setRoleInfor((RoleInfor) roleManager.get(57));
                    checkInfor.setType(1);
                    checkInfor.setLayer(3);
                    checkInfors.add(checkInfor);
                }
                sanfangInfor.setSanfangStatus(sanfangInfor.getSanfangStatus() + 2);
            } else {
                sanfangInfor.setSanfangStatus(sanfangInfor.getSanfangStatus() + 1);
            }
        } else if (formVo.getCheckResult() == 0) {
            sanfangInfor.setSanfangStatus(-1);
            purchaseInfor.setSanfangId(-1);
            sanfangInfor.setEndDate(new Date());
        }

        purchaseManager.save(purchaseInfor);
        sanfangInforManager.save(sanfangInfor);
        return "redirect:review.do?method=view&sanfangID=" + sanfangID;
    }

    @RequestMapping(params = "method=edit")
    public String edit(FormVo formVo, HttpServletRequest request, ModelMap modelMap) {
        session.setAttribute(SESSION_ORDER_TOKEN, UUID.randomUUID().toString());
        int sanfangID = Integer.parseInt(request.getParameter("sanfangID"));
        SanfangInfor sanfangInfor = (SanfangInfor) sanfangInforManager.get(sanfangID);
        PurchaseInfor purchaseInfor = (PurchaseInfor) this.purchaseManager.get(sanfangInfor.getPurchaseId());
        modelMap.addAttribute("sanfangInfor", sanfangInfor);
        modelMap.addAttribute("purchaseInfo", purchaseInfor);
        if (purchaseInfor.getGuikouDepartment() != null && purchaseInfor.getGuikouDepartment().getOrganizeName().equals("技术规划部")) {
            if (StringUtil.isNotEmpty(purchaseInfor.getJigui())) {
                List<SystemUserInfor> jgers = new ArrayList<>();
                String jg = purchaseInfor.getJigui();
                for (int i = 0; i < jg.split(",").length; i++) {
                    SystemUserInfor jger = (SystemUserInfor) this.systemUserManager.get(Integer.parseInt(jg.split(",")[i]));
                    jgers.add(jger);
                }
                modelMap.addAttribute("jgers", jgers);
            }
        }
        return "sfbj/editCheck";
    }

    @RequestMapping(params = "method=sanfangList")
    public void sanfangList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String[] queryString = this.sanfangInforManager.generateQueryString("SanfangInfor", "sanfangID", getSearchParams(request));
        String page = request.getParameter("page");        //当前页
        String rowsNum = request.getParameter("rows");    //每页显示的行数
        String type = request.getParameter("type");
        Pages pages = new Pages(request);
        pages.setPage(Integer.valueOf(page));
        pages.setPerPageNum(Integer.valueOf(rowsNum));
        SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);

        PageList pl = this.sanfangInforManager.getResultByQueryString(queryString[0], queryString[1], true, pages);
        List list = pl.getObjectList();
        List fList = new ArrayList<>();

        if (StringUtil.isNotEmpty(type)) {
            Integer state = Integer.parseInt(type);
            for (Iterator it = list.iterator(); it.hasNext(); ) {
                SanfangInfor sanfangInfor = (SanfangInfor) it.next();
                if (sanfangInfor.getPurchaseType().equals(EnumUtil.getByCode(state, PurchaseTypeEnum.class).getMsg())) {
                    if (sanfangInfor.getPurchaseCharge().getPersonId().intValue() == systemUser.getPersonId().intValue() || systemUser.getUserName().equals("admin")) {
                        fList.add(sanfangInfor);
                    }
                    List<SanfangCheckInfor> checkInfors = sanfangInfor.getCheckInfors();
                    for (SanfangCheckInfor checkInfor : checkInfors) {
                        if (checkInfor.getChecker().getPersonId().intValue() == systemUser.getPersonId().intValue()) {
                            fList.add(sanfangInfor);
                        }
                    }
                }
            }
        } else {
            fList = list;
        }

        // 把查询到的结果转化为VO
        Set sanfangListVos = new HashSet();
        if (fList.size() > 0) {
            for (Iterator it = fList.iterator(); it.hasNext(); ) {
                SanfangInfor sanfangInfor = (SanfangInfor) it.next();
                // 把查询到的结果转化为VO
                SanfangListVO vo = this.sanfangInforManager.transToVo(sanfangInfor);
                sanfangListVos.add(vo);
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
        rows = convert.modelCollect2JSONArray(sanfangListVos, new ArrayList());
        jsonObj.put("rows", rows);                            //返回到前台每页显示的数据(名称必须为rows)
        //设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);
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
            String queryHQL = "";
            Set<SanfangInfor> allResult = new HashSet<>();
            Integer personId = systemUser.getPersonId();
//            RoleInfor gk = (RoleInfor) this.roleManager.get(PurchaseCheckInfor.Check_Role_Guikou);
//            RoleInfor cgGroup = (RoleInfor) this.roleManager.get(PurchaseCheckInfor.Check_Role_caigouGroup);
            if (personId == this.organizeManager.findByOrganizeName("采购部").getDirector().getPersonId().intValue()) {
                queryHQL = "from SanfangInfor where sanfangStatus=1";
            }
            String queryHQL1 = "from SanfangInfor where sanfangStatus=2";

            List<SanfangInfor> resultByQueryString = this.sanfangInforManager.getResultByQueryString(queryHQL1);
            List<SanfangInfor> result = new ArrayList<>();
            for (SanfangInfor sanfangInfor : resultByQueryString){
                PurchaseInfor purchaseInfor = (PurchaseInfor) this.purchaseManager.get(sanfangInfor.getPurchaseId());
                if (purchaseInfor.getJigui() != null) {
                    for (int i = 0; i < purchaseInfor.getJigui().split(",").length; i++) {
                        if (personId.intValue() == Integer.parseInt(purchaseInfor.getJigui().split(",")[i])) {
                            result.add(sanfangInfor);
                        }
                    }
                } else if (purchaseInfor.getGuikouDepartment().getDirector().getPersonId().intValue() == personId.intValue()) {
                    result.add(sanfangInfor);
                }
            }
            Iterator<SanfangInfor> it = result.iterator();
            while(it.hasNext()){
                SanfangInfor sanfangInfor = it.next();
                if(sanfangInfor.getSanfangStatus()==2){
                    List<SanfangCheckInfor> checkInfors = sanfangInfor.getCheckInfors();
                    for(SanfangCheckInfor checkInfor:checkInfors){
                        if(checkInfor.getChecker().getPersonId().intValue()==systemUser.getPersonId().intValue()){
                            it.remove();
                        }
                    }
                }
            }

            allResult.addAll(result);

            String checkHql = "from SanfangCheckInfor where checkResult is null  and checker.personId=" + personId;
            List<SanfangCheckInfor> checkInfors = this.sanfangCheckManager.getResultByQueryString(checkHql);
            for (SanfangCheckInfor checkInfor : checkInfors) {
                SanfangInfor sanfangInfor = checkInfor.getSanfangInfor();
                if (sanfangInfor.getSanfangStatus() == 3) {
                    allResult.add(sanfangInfor);
                }
            }

            if (StringUtil.isNotEmpty(queryHQL)) {
                List<SanfangInfor> sanfangInfors = this.sanfangInforManager.getResultByQueryString(queryHQL);
                allResult.addAll(sanfangInfors);
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
