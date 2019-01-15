package com.kwchina.oa.purchase.zhaotou.controller;

import com.kwchina.core.base.entity.PersonInfor;
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
import com.kwchina.extend.template.entity.ZhaotouTemplate;
import com.kwchina.extend.template.service.TemplateManager;
import com.kwchina.oa.purchase.sanfang.entity.SanfangInfor;
import com.kwchina.oa.purchase.sanfang.entity.SupplierInfor;
import com.kwchina.oa.purchase.sanfang.enums.PurchaseTypeEnum;
import com.kwchina.oa.purchase.sanfang.service.SupplierInforManager;
import com.kwchina.oa.purchase.sanfang.utils.Convert;
import com.kwchina.oa.purchase.sanfang.utils.EnumUtil;
import com.kwchina.oa.purchase.yiban.entity.PurchaseCheckInfor;
import com.kwchina.oa.purchase.yiban.entity.PurchaseInfor;
import com.kwchina.oa.purchase.yiban.service.PurchaseManager;
import com.kwchina.oa.purchase.zhaotou.VO.BidInfoVO;
import com.kwchina.oa.purchase.zhaotou.VO.BidListVO;
import com.kwchina.oa.purchase.zhaotou.VO.CheckVO;
import com.kwchina.oa.purchase.zhaotou.VO.DingbiaoVO;
import com.kwchina.oa.purchase.zhaotou.entity.*;
import com.kwchina.oa.purchase.zhaotou.enums.ZhaotouStatusEnum;
import com.kwchina.oa.purchase.zhaotou.service.BidInfoManager;
import com.kwchina.oa.purchase.zhaotou.service.ZhaotouCheckManager;
import com.kwchina.oa.purchase.zhaotou.service.ZhaotouScoreManager;
import com.kwchina.oa.util.SysCommonMethod;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping(value = "bid.do")
public class ZhaotouController extends BasicController {
    @Autowired
    private OrganizeManager organizeManager;
    @Autowired
    private SupplierInforManager supplierInforManager;
    @Autowired
    private SystemUserManager systemUserManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private ZhaotouScoreManager zhaotouScoreManager;
    @Autowired
    private ZhaotouCheckManager zhaotouCheckManager;

    @Autowired
    HttpSession session;
    @Autowired
    private TemplateManager templateManager;
    @Autowired
    private PurchaseManager purchaseManager;
    @Autowired
    private BidInfoManager bidInfoManager;
    /**提交令牌，防止重复提交*/
    public static final String SESSION_ORDER_TOKEN = "SESSION_ORDER_TOKEN";

    @RequestMapping(params = "method=start")
    public String start(ModelMap modelMap, HttpServletRequest request) {
        String pId = request.getParameter("purchaseId");
        if (StringUtil.isNotEmpty(pId)) {
            Integer purchaseId = Integer.parseInt(pId);
            PurchaseInfor purchaseInfo = (PurchaseInfor) purchaseManager.get(purchaseId);
            modelMap.addAttribute("purchase", purchaseInfo);
            List allSupplier = this.supplierInforManager.getInSupplier(EnumUtil.getByMsg(purchaseInfo.getFlowId().getFlowName(),PurchaseTypeEnum.class).getCode(),purchaseInfo.getGuikouDepartment()!=null?purchaseInfo.getGuikouDepartment().getOrganizeId():null);
            modelMap.addAttribute("suppliers", allSupplier);
        }
        //根据职级获取用户
        List users = this.systemUserManager.getUserByPosition(PersonInfor._Position_Tag);
        modelMap.addAttribute("_Users", users);

        //获取职级大于一定值的用户
        List otherUsers = this.systemUserManager.getOtherPositionUser(PersonInfor._Position_Tag);
        modelMap.addAttribute("_OtherUsers", otherUsers);

        //全部部门信息
        List departments = this.organizeManager.getDepartments();
        modelMap.addAttribute("_Departments", departments);
        List<ZhaotouTemplate> allTemplates = this.templateManager.getAllTemplates();
        modelMap.addAttribute("templates", allTemplates);
        return "zhaotou/kaibiao";
    }

    @RequestMapping(params = "method=detail")
    public String detail(HttpServletRequest request, ModelMap modelMap) throws Exception {
        Integer bidInfoId = Integer.parseInt(request.getParameter("bidInfoId"));
        BidInfo bidInfo = (BidInfo) bidInfoManager.get(bidInfoId);
        String checkerId = request.getParameter("checkerId");
        SystemUserInfor checker = (SystemUserInfor) systemUserManager.get(Integer.parseInt(checkerId));
        modelMap.addAttribute("bidInfo", bidInfo);
        modelMap.addAttribute("canEdit", false);
        modelMap.addAttribute("checker", checker);
        return "/zhaotou/score";
    }

    @RequestMapping(params = "method=view")
    public String view(ModelMap modelMap, HttpServletRequest request) throws Exception {
        Integer bidInfoId = Integer.parseInt(request.getParameter("bidInfoId"));
        BidInfo bidInfo = (BidInfo) bidInfoManager.get(bidInfoId);
        SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
        RoleInfor charge = (RoleInfor) this.roleManager.get(PurchaseCheckInfor.Check_Role_Caigou);
        PurchaseInfor purchase = (PurchaseInfor) purchaseManager.get(bidInfo.getPurchaseId());
        modelMap.addAttribute("bidInfo", bidInfo);
        modelMap.addAttribute("purchase", purchase);
        boolean canReview = false;
        boolean inGroup = false;
        boolean isChecked=false;
        boolean hasChecked=false;
        SystemUserInfor checker = SysCommonMethod.getSystemUser(request);
        Iterator<ZhaotouScore> iterator2 = bidInfo.getScores().iterator();
        while (iterator2.hasNext()) {
            ZhaotouScore next = iterator2.next();
            if (next.getChecker().getPersonId().equals(systemUser.getPersonId()) && next.getScore() != null) {
                isChecked = false;
                break;
            } else {
                isChecked = true;
            }
        }

        modelMap.addAttribute("checker", checker);
        modelMap.addAttribute("canEdit", isChecked);

        Iterator<ZhaotouCheckInfor> iterator3 = bidInfo.getCheckInfors().iterator();
        while (iterator3.hasNext()) {
            ZhaotouCheckInfor next = iterator3.next();
            if (next.getChecker().getPersonId().equals(systemUser.getPersonId()) && next.getCheckResult() != null) {
                hasChecked = false;
                break;
            } else {
                hasChecked = true;
            }
        }
        Set<SystemUserInfor> checker1s = ((RoleInfor) roleManager.get(59)).getUsers();
        modelMap.addAttribute("checkers", checker1s);
        Iterator<SystemUserInfor> iterator1 = checker1s.iterator();
        while (iterator1.hasNext()) {
            SystemUserInfor next = iterator1.next();
            if (next.getPersonId().equals(checker.getPersonId()) && bidInfo.getZhaotouStatus() == 3&&hasChecked) {
                canReview = true;
                break;
            }
        }

        modelMap.addAttribute("canReview", canReview);
        boolean canSave = false;
        if (checker.getPersonId().equals(bidInfo.getPurchaseExecutor().getPersonId()) || this.roleManager.belongRole(systemUser, charge)) {
            if (bidInfo.getZhaotouStatus().equals(ZhaotouStatusEnum.SET_BID.getCode())) {
                canSave = true;
            }
        }
        modelMap.addAttribute("canSave", canSave);

        Set<SystemUserInfor> checkers = new HashSet<>();
        List<ZhaotouScore> scores = bidInfo.getScores();
        for(ZhaotouScore score:scores){
            checkers.add(score.getChecker());
        }
        modelMap.addAttribute("checkers", checkers);
        Iterator<SystemUserInfor> iterator = checkers.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getPersonId().equals(checker.getPersonId()) && bidInfo.getZhaotouStatus() < 3 && bidInfo.getZhaotouStatus() != -1) {
                inGroup = true;
                break;
            }
        }

        if (inGroup && bidInfo.getZhaotouStatus() == 1) {
            return "/zhaotou/score";
        }
        return "/zhaotou/viewInstance";
    }

    @RequestMapping(params = "method=save", method = RequestMethod.POST)
    public String save(BidInfoVO bidInfoVo, HttpServletRequest request, DefaultMultipartHttpServletRequest multipartRequest) throws Exception {
        BidInfo bidInfo = new BidInfo();
        bidInfo.setProjectName(bidInfoVo.getProjectName());
        bidInfo.setZbCode(bidInfoVo.getZbCode());
        PurchaseInfor purchaseInfor = (PurchaseInfor) this.purchaseManager.get(bidInfoVo.getPurchaseId());
        bidInfo.setZhaotouMemo(purchaseInfor.getApplication());
        bidInfo.setStartTime(Convert.StrToDate(bidInfoVo.getStartDate(), "yyyy-MM-dd HH:mm"));
        bidInfo.setZhaotouApplier(purchaseInfor.getApplier());
        bidInfo.setPurchaseExecutor(SysCommonMethod.getSystemUser(request));
        bidInfo.setZhaotouDepartment(purchaseInfor.getDepartment());
        bidInfo.setPurchaseType(EnumUtil.getByMsg(bidInfoVo.getPurchaseTypeMsg(), PurchaseTypeEnum.class).getCode());
        bidInfo.setPurchaseId(bidInfoVo.getPurchaseId());
        bidInfo.setReaderName(bidInfoVo.getReaderName());
        bidInfo.setSupervisorName(bidInfoVo.getSupervisorName());
        ZhaotouTemplate template = this.templateManager.findByTemplateName(bidInfoVo.getTemplateName());
        bidInfo.setTemplate(template);
        String attachment = this.uploadAttachment(multipartRequest, "zhaotou");
        String[] attachs = cutOffattach(attachment);
        int attNum = 0;
        List<SupplierDesc> supplierDescs = new ArrayList<>();
        if (!(bidInfoVo.getSupplierName1() == null || "".equals(bidInfoVo.getSupplierName1()))) {
            SupplierDesc supplierDesc1 = new SupplierDesc();
            supplierDesc1.setBidInfo(bidInfo);
            supplierDesc1.setSupplierName(bidInfoVo.getSupplierName1());
            supplierDesc1.setResponseTime(bidInfoVo.getResponseTime1());
            supplierDesc1.setUnitPrice(bidInfoVo.getUnitPrice1());
            supplierDesc1.setQualification(bidInfoVo.getQualification1());
            supplierDesc1.setTotalPrice(bidInfoVo.getTotalPrice1());
            supplierDesc1.setMemo(bidInfoVo.getMemo1());
            if (StringUtil.isNotEmpty(bidInfoVo.getSav1()) && attNum < attachs.length) {
                supplierDesc1.setAttach(attachs[attNum]);
                attNum++;
            }
            supplierDescs.add(supplierDesc1);
        }
        if (!(bidInfoVo.getSupplierName2() == null || bidInfoVo.getSupplierName2().equals(""))) {
            SupplierDesc supplierDesc2 = new SupplierDesc();
            supplierDesc2.setBidInfo(bidInfo);
            supplierDesc2.setSupplierName(bidInfoVo.getSupplierName2());
            supplierDesc2.setResponseTime(bidInfoVo.getResponseTime2());
            supplierDesc2.setUnitPrice(bidInfoVo.getUnitPrice2());
            supplierDesc2.setQualification(bidInfoVo.getQualification2());
            supplierDesc2.setTotalPrice(bidInfoVo.getTotalPrice2());
            supplierDesc2.setMemo(bidInfoVo.getMemo2());
            if (StringUtil.isNotEmpty(bidInfoVo.getSav2()) && attNum < attachs.length) {
                supplierDesc2.setAttach(attachs[attNum]);
                attNum++;
            }
            supplierDescs.add(supplierDesc2);
        }
        if (!(bidInfoVo.getSupplierName3() == null || bidInfoVo.getSupplierName3().equals(""))) {
            SupplierDesc supplierDesc3 = new SupplierDesc();
            supplierDesc3.setBidInfo(bidInfo);
            supplierDesc3.setSupplierName(bidInfoVo.getSupplierName3());
            supplierDesc3.setResponseTime(bidInfoVo.getResponseTime3());
            supplierDesc3.setUnitPrice(bidInfoVo.getUnitPrice3());
            supplierDesc3.setQualification(bidInfoVo.getQualification3());
            supplierDesc3.setTotalPrice(bidInfoVo.getTotalPrice3());
            supplierDesc3.setMemo(bidInfoVo.getMemo3());
            if (StringUtil.isNotEmpty(bidInfoVo.getSav3()) && attNum < attachs.length) {
                supplierDesc3.setAttach(attachs[attNum]);
                attNum++;
            }
            supplierDescs.add(supplierDesc3);
        }
        if (!(bidInfoVo.getSupplierName4() == null || bidInfoVo.getSupplierName4().equals(""))) {
            SupplierDesc supplierDesc4 = new SupplierDesc();
            supplierDesc4.setBidInfo(bidInfo);
            supplierDesc4.setSupplierName(bidInfoVo.getSupplierName4());
            supplierDesc4.setResponseTime(bidInfoVo.getResponseTime4());
            supplierDesc4.setUnitPrice(bidInfoVo.getUnitPrice4());
            supplierDesc4.setQualification(bidInfoVo.getQualification4());
            supplierDesc4.setTotalPrice(bidInfoVo.getTotalPrice4());
            supplierDesc4.setMemo(bidInfoVo.getMemo4());
            if (StringUtil.isNotEmpty(bidInfoVo.getSav4()) && attNum < attachs.length) {
                supplierDesc4.setAttach(attachs[attNum]);
                attNum++;
            }
            supplierDescs.add(supplierDesc4);
        }
        if (!(bidInfoVo.getSupplierName5() == null || bidInfoVo.getSupplierName5().equals(""))) {
            SupplierDesc supplierDesc5 = new SupplierDesc();
            supplierDesc5.setBidInfo(bidInfo);
            supplierDesc5.setSupplierName(bidInfoVo.getSupplierName5());
            supplierDesc5.setResponseTime(bidInfoVo.getResponseTime5());
            supplierDesc5.setUnitPrice(bidInfoVo.getUnitPrice5());
            supplierDesc5.setQualification(bidInfoVo.getQualification5());
            supplierDesc5.setTotalPrice(bidInfoVo.getTotalPrice5());
            supplierDesc5.setMemo(bidInfoVo.getMemo5());
            if (StringUtil.isNotEmpty(bidInfoVo.getSav5()) && attNum < attachs.length) {
                supplierDesc5.setAttach(attachs[attNum]);
                attNum++;
            }
            supplierDescs.add(supplierDesc5);
        }
        bidInfo.setSuppliers(supplierDescs);
        if (StringUtil.isNotEmpty(bidInfoVo.getBav()) && attNum < attachs.length) {
            bidInfo.setBidAttach(attachs[attNum]);
            attNum++;
        }
        bidInfo.setZhaotouStatus(ZhaotouStatusEnum.CHECK_GROUP.getCode());
        List<ZhaotouCheckInfor> checkInfors = new ArrayList<>();

        Set<SystemUserInfor> users = ((RoleInfor) roleManager.get(59)).getUsers();
        for (SystemUserInfor checker : users) {
            ZhaotouCheckInfor zhaotouCheckInfor = new ZhaotouCheckInfor();
            zhaotouCheckInfor.setChecker(checker);
            zhaotouCheckInfor.setBidInfo(bidInfo);
            checkInfors.add(zhaotouCheckInfor);
        }
        bidInfo.setCheckInfors(checkInfors);
        List<ZhaotouScore> scores = new ArrayList<>();

        int[] personIds = bidInfoVo.getPersonIds();
        if (personIds != null) {
            for (int k = 0; k < personIds.length; k++) {
                for (int i = 1; i <= bidInfo.getTemplate().getTemplateInfos().size(); i++) {
                    for (int j = 1; j <= bidInfo.getSuppliers().size(); j++) {
                        ZhaotouScore zhaotouScore = new ZhaotouScore();
                        zhaotouScore.setSupplierDesc(bidInfo.getSuppliers().get(j - 1));
                        zhaotouScore.setBidInfo(bidInfo);
                        zhaotouScore.setZhaotouTemplateInfo(bidInfo.getTemplate().getTemplateInfos().get(i - 1));
                        zhaotouScore.setChecker((SystemUserInfor) this.systemUserManager.get(personIds[k]));
                        scores.add(zhaotouScore);
                    }
                }
            }
        }

        bidInfo.setScores(scores);
        List<ZhaotouScoreTotal> totals = new ArrayList<>();
        for (SupplierDesc supplierDesc : bidInfo.getSuppliers()) {
            ZhaotouScoreTotal zhaotouScoreTotal = new ZhaotouScoreTotal();
            zhaotouScoreTotal.setSupplierName(supplierDesc.getSupplierName());
            zhaotouScoreTotal.setPrice(supplierDesc.getTotalPrice());
            zhaotouScoreTotal.setBidInfo(bidInfo);
            List<EachTotal> eachTotals=new ArrayList<>();
            for(int i=0;i< personIds.length; i++){
                EachTotal eachTotal=new EachTotal();
                eachTotal.setScorer((SystemUserInfor) this.systemUserManager.get(personIds[i]));
                eachTotal.setScoreTotal(zhaotouScoreTotal);
                eachTotals.add(eachTotal);
            }
            zhaotouScoreTotal.setEachTotals(eachTotals);
            totals.add(zhaotouScoreTotal);
        }
        bidInfo.setTotals(totals);
        BidInfo save = (BidInfo) this.bidInfoManager.save(bidInfo);
        purchaseInfor.setZhaotouId(save.getBidInfoId());
        purchaseManager.save(purchaseInfor);
        return "core/success";
    }

    @RequestMapping(params = "method=dingbiao", method = RequestMethod.POST)
    public String dingbiao(DingbiaoVO dingbiaoVO, ModelMap modelMap, HttpServletRequest request) throws Exception {
        BidInfo bidInfo = (BidInfo) bidInfoManager.get(dingbiaoVO.getBidInfoId());
        BeanUtils.copyProperties(dingbiaoVO, bidInfo);
        bidInfo.setZhaotouFinalSupplierInfor(supplierInforManager.findBySupplierName(dingbiaoVO.getZhaotouFinalSupplierName()));
        List<ZhaotouScoreTotal> totals = bidInfo.getTotals();
        if (!(dingbiaoVO.getSupplierName1() == null || dingbiaoVO.getSupplierName1().equals(""))) {
            totals.get(0).setJsAvgScore(dingbiaoVO.getJsAvgScore1());
            totals.get(0).setPrice(dingbiaoVO.getPrice1());
            totals.get(0).setSwAvgScore(dingbiaoVO.getSwAvgScore1());
            totals.get(0).setTotalScore(dingbiaoVO.getTotalScore1());
        }
        if (!(dingbiaoVO.getSupplierName2() == null || dingbiaoVO.getSupplierName2().equals(""))) {
            totals.get(1).setJsAvgScore(dingbiaoVO.getJsAvgScore2());
            totals.get(1).setPrice(dingbiaoVO.getPrice2());
            totals.get(1).setSwAvgScore(dingbiaoVO.getSwAvgScore2());
            totals.get(1).setTotalScore(dingbiaoVO.getTotalScore2());
        }
        if (!(dingbiaoVO.getSupplierName3() == null || dingbiaoVO.getSupplierName3().equals(""))) {
            totals.get(2).setJsAvgScore(dingbiaoVO.getJsAvgScore3());
            totals.get(2).setPrice(dingbiaoVO.getPrice3());
            totals.get(2).setSwAvgScore(dingbiaoVO.getSwAvgScore3());
            totals.get(2).setTotalScore(dingbiaoVO.getTotalScore3());
        }
        if (!(dingbiaoVO.getSupplierName4() == null || dingbiaoVO.getSupplierName4().equals(""))) {
            totals.get(3).setJsAvgScore(dingbiaoVO.getJsAvgScore4());
            totals.get(3).setPrice(dingbiaoVO.getPrice4());
            totals.get(3).setSwAvgScore(dingbiaoVO.getSwAvgScore4());
            totals.get(3).setTotalScore(dingbiaoVO.getTotalScore4());
        }
        if (!(dingbiaoVO.getSupplierName5() == null || dingbiaoVO.getSupplierName5().equals(""))) {
            totals.get(4).setJsAvgScore(dingbiaoVO.getJsAvgScore5());
            totals.get(4).setPrice(dingbiaoVO.getPrice5());
            totals.get(4).setSwAvgScore(dingbiaoVO.getSwAvgScore5());
            totals.get(4).setTotalScore(dingbiaoVO.getTotalScore5());
        }
        bidInfo.setTotals(totals);
        bidInfo.setZhaotouStatus(ZhaotouStatusEnum.CHECK_COMMITTEE.getCode());
        bidInfoManager.save(bidInfo);
        return "/core/success";
    }

    @RequestMapping(params = "method=check")
    public String check(CheckVO checkVO, HttpServletRequest request) throws Exception {
        Object obj = session.getAttribute(SESSION_ORDER_TOKEN);
        if (obj == null) {
            return "homepage";
        }
        //移除令牌  无论成功还是失败
        session.removeAttribute(SESSION_ORDER_TOKEN);
        Integer bidInfoId = checkVO.getBidInfoId();
        BidInfo bidInfo = (BidInfo) this.bidInfoManager.get(bidInfoId);
        PurchaseInfor purchaseInfor = (PurchaseInfor) this.purchaseManager.get(bidInfo.getPurchaseId());
        int i = 0;
        List<ZhaotouCheckInfor> checkInfors = bidInfo.getCheckInfors();
        for (ZhaotouCheckInfor zhaotouCheckInfor : checkInfors) {
            if (zhaotouCheckInfor.getChecker().getPersonId().equals(SysCommonMethod.getSystemUser(request).getPersonId())) {
                zhaotouCheckInfor.setCheckResult(checkVO.getCheckResult());
                zhaotouCheckInfor.setCheckTime(Convert.StrToDate(checkVO.getCheckDate(), "yyyy-MM-dd HH:mm:ss"));
                zhaotouCheckInfor.setCheckMemo(checkVO.getCheckMemo());
            }
            if (zhaotouCheckInfor.getCheckResult() != null) {
                i++;
            }
            if (zhaotouCheckInfor.getCheckResult() != null && zhaotouCheckInfor.getCheckResult() == 0) {
                bidInfo.setZhaotouStatus(ZhaotouStatusEnum.STOP.getCode());
                purchaseInfor.setZhaotouId(-1);
                break;
            }
        }
        if (i == ((RoleInfor) roleManager.get(59)).getUsers().size()) {
            bidInfo.setZhaotouStatus(ZhaotouStatusEnum.COMPLETE.getCode());
            bidInfo.setZhaotouEndTime(new Date());
            purchaseInfor.setPurchaseStatus(10);
        }
        purchaseManager.save(purchaseInfor);
        bidInfoManager.save(bidInfo);
        return "/core/success";
    }

    @RequestMapping(params = "method=score")
    public String score(HttpServletRequest request) throws Exception {
        Integer bidInfoId = Integer.parseInt(request.getParameter("bidInfoId"));
        BidInfo bidInfo = (BidInfo) this.bidInfoManager.get(bidInfoId);
        Integer checkerId = Integer.parseInt(request.getParameter("checkerId"));
        SystemUserInfor checker = (SystemUserInfor) this.systemUserManager.get(checkerId);
        List<ZhaotouScore> scores = bidInfo.getScores();
        int size = bidInfo.getTemplate().getTemplateInfos().size();
        int size1 = bidInfo.getSuppliers().size();
        int total = 0;
        for (int k = 1; k <= scores.size()/(size*size1); k++) {
            for (int i = 1; i <= size; i++) {
                for (int j = 1; j <= size1; j++) {
                    StringBuffer pname = new StringBuffer("score");
                    pname.append(i).append(j);
                    System.out.println(pname);
                    Integer score = Integer.parseInt(request.getParameter(pname.toString()));
                    if (scores.get(total).getChecker().getPersonId().equals(checkerId)) {
                        scores.get(total).setScore(score);
                    }
                    total++;
                }
            }
        }
        List<ZhaotouScoreTotal> totals = bidInfo.getTotals();
        for(int i=0;i<totals.size();i++){
            for(EachTotal eachTotal:totals.get(i).getEachTotals()){
                if(eachTotal.getScorer().getPerson().getPersonId().intValue()==checkerId.intValue()){
                    eachTotal.setTotalTech(Integer.parseInt(request.getParameter("totalTech"+i)));
                    eachTotal.setTotalBiz(Integer.parseInt(request.getParameter("totalBiz"+i)));
                }
            }
        }

        int count = 0;
        for (int i = 0; i < scores.size(); i++) {
            if (scores.get(i).getScore() != null) {
                count++;
            }
        }
        if (count == scores.size()) {
            bidInfo.setZhaotouStatus(ZhaotouStatusEnum.SET_BID.getCode());
            for(ZhaotouScoreTotal zhaotouScoreTotal:totals){
                List<EachTotal> eachTotals = zhaotouScoreTotal.getEachTotals();
                double allTech=0;
                double allBiz=0;
                int eachSize=eachTotals.size();
                for(EachTotal eachTotal:eachTotals){
                    allTech+=eachTotal.getTotalTech();
                    allBiz+=eachTotal.getTotalBiz();
                }
                double techAvg=allTech/eachSize;
                double bizAvg=allBiz/eachSize;
                zhaotouScoreTotal.setJsAvgScore(techAvg);
                zhaotouScoreTotal.setSwAvgScore(bizAvg);
                zhaotouScoreTotal.setTotalScore(techAvg+bizAvg);
            }
        }
        bidInfoManager.save(bidInfo);
        return "/core/success";
    }

    @RequestMapping(params = "method=edit")
    public String edit(HttpServletRequest request, ModelMap modelMap) throws Exception {
        session.setAttribute(SESSION_ORDER_TOKEN, UUID.randomUUID().toString());
        int bidInfoId = Integer.parseInt(request.getParameter("bidInfoId"));
        BidInfo bidInfo = (BidInfo) bidInfoManager.get(bidInfoId);
        modelMap.addAttribute("bidInfo", bidInfo);
        return "zhaotou/editCheck";
    }

    @RequestMapping(params = "method=outPrice", method = RequestMethod.POST)
    @ResponseBody
    public double contact(HttpServletRequest request) {
        String supplierName = request.getParameter("supplierName");
        String bidId = request.getParameter("bidInfoId");
        BidInfo bidInfo = (BidInfo) this.bidInfoManager.get(Integer.parseInt(bidId));
        List<SupplierDesc> suppliers = bidInfo.getSuppliers();
        double finalPrice=0;
        for(SupplierDesc desc:suppliers){
            if(desc.getSupplierName().equals(supplierName)){
                finalPrice = desc.getTotalPrice();
            }
        }
        return finalPrice;
    }

    @RequestMapping(params = "method=bidInfoList")
    public void bidInfoList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //构造查询语句
        String[] queryString = this.bidInfoManager.generateQueryString("BidInfo", "bidInfoId", getSearchParams(request));
        String type = request.getParameter("type");
        String page = request.getParameter("page");        //当前页
        String rowsNum = request.getParameter("rows");    //每页显示的行数
        Pages pages = new Pages(request);
        pages.setPage(Integer.valueOf(page));
        pages.setPerPageNum(Integer.valueOf(rowsNum));
        SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);

        PageList pl = this.bidInfoManager.getResultByQueryString(queryString[0], queryString[1], true, pages);
        List list = pl.getObjectList();
        List fList = new ArrayList<>();
        if (StringUtil.isNotEmpty(type)) {
            Integer state = Integer.parseInt(type);
            for (Iterator it = list.iterator(); it.hasNext(); ) {
                BidInfo bidInfo = (BidInfo) it.next();
                if (bidInfo.getPurchaseType().equals(state)) {
                    fList.add(bidInfo);
                }
            }
        } else {
            fList = list;
        }
        // 把查询到的结果转化为VO
        Set<BidListVO> bidListVos = new HashSet<>();
        if (fList.size() > 0) {
            for (Iterator it = fList.iterator(); it.hasNext(); ) {
                BidInfo bidInfo = (BidInfo) it.next();
                // 把查询到的结果转化为VO
                if (bidInfo.getPurchaseExecutor().getPersonId() == systemUser.getPersonId().intValue() || systemUser.getPerson().getPersonName().equals(bidInfo.getReaderName()) || systemUser.getUserName().equals("admin")) {
                    BidListVO bidListVO = this.bidInfoManager.transPOToVO(bidInfo);
                    bidListVos.add(bidListVO);
                }
                List<ZhaotouCheckInfor> checkInfors = bidInfo.getCheckInfors();
                for (ZhaotouCheckInfor checkInfor : checkInfors) {
                    if (checkInfor.getChecker().getPersonId().intValue() == systemUser.getPersonId().intValue()) {
                        BidListVO bidListVO = this.bidInfoManager.transPOToVO(bidInfo);
                        bidListVos.add(bidListVO);
                    }
                }
                List<ZhaotouScore> scores = bidInfo.getScores();
                for (ZhaotouScore score : scores) {
                    if (score.getChecker().getPersonId().intValue() == systemUser.getPersonId().intValue()) {
                        BidListVO bidListVO = this.bidInfoManager.transPOToVO(bidInfo);
                        bidListVos.add(bidListVO);
                    }
                }
            }
        }
        //定义返回的数据类型：json，使用了json-lib
        JSONObject jsonObj = new JSONObject();
        //定义rows，存放数据
        JSONArray rows = new JSONArray();
        jsonObj.put("page", pl.getPages().getCurrPage());
        jsonObj.put("total", pl.getPages().getTotalPage());
        jsonObj.put("records", pl.getPages().getTotals());

        JSONConvert convert = new JSONConvert();
        rows = convert.modelCollect2JSONArray(bidListVos, new ArrayList());
        jsonObj.put("rows", rows);
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
            Integer personId = systemUser.getPersonId();
            Set<BidInfo> allResult = new HashSet<>();
            String queryHQL = "from BidInfo where zhaotouStatus=2 and purchaseExecutor.personId="+personId;
            if (StringUtil.isNotEmpty(queryHQL)) {
                List bidInfos = this.bidInfoManager.getResultByQueryString(queryHQL);
                allResult.addAll(bidInfos);
            }
            String scoreHql = "from ZhaotouScore where score is null  and checker.personId=" + personId;
            List<ZhaotouScore> scores = this.zhaotouScoreManager.getResultByQueryString(scoreHql);
            for (ZhaotouScore score : scores) {
                BidInfo bidInfo = score.getBidInfo();
                if (bidInfo.getZhaotouStatus()==1) {
                    allResult.add(bidInfo);
                }
            }
            String checkHql = "from ZhaotouCheckInfor where checkResult is null  and checker.personId=" + personId;
            List<ZhaotouCheckInfor> checkInfors = this.zhaotouCheckManager.getResultByQueryString(checkHql);
            for (ZhaotouCheckInfor checkInfor:checkInfors ) {
                BidInfo bidInfo = checkInfor.getBidInfo();
                if (bidInfo.getZhaotouStatus()==3) {
                    allResult.add(bidInfo);
                }
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
