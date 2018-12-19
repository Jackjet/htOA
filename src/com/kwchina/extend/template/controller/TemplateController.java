package com.kwchina.extend.template.controller;

import com.kwchina.core.base.entity.PersonInfor;
import com.kwchina.core.base.entity.RoleInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.*;
import com.kwchina.core.base.vo.RoleInforVo;
import com.kwchina.core.common.controller.BasicController;
import com.kwchina.core.common.page.PageList;
import com.kwchina.core.common.page.Pages;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.core.util.json.JSONConvert;
import com.kwchina.core.util.string.StringUtil;
import com.kwchina.extend.template.entity.ZhaotouTemplate;
import com.kwchina.extend.template.entity.ZhaotouTemplateInfo;
import com.kwchina.extend.template.service.TemplateInfoManager;
import com.kwchina.extend.template.service.TemplateManager;
import com.kwchina.extend.template.vo.TemplateVo;
import com.kwchina.oa.purchase.zhaotou.entity.BidInfo;
import com.kwchina.oa.purchase.zhaotou.service.BidInfoManager;
import com.kwchina.oa.util.SysCommonMethod;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.sql.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping("extend/template.do")
public class TemplateController extends BasicController {
    @Autowired
    private TemplateManager templateManager;
    @Autowired
    private TemplateInfoManager templateInfoManager;
    @Autowired
    private DataRightInforManager dataRightInforManager;
    @Autowired
    private SystemUserManager systemUserManager;
    @Autowired
    private BidInfoManager bidInfoManager;


    //显示所有角色
    @RequestMapping(params = "method=list")
    public void list(HttpServletRequest request, HttpServletResponse response) throws Exception {

//        //构造查询语句
//        String[] queryString = this.templateManager.generateQueryString("ZhaotouTemplate", "templateId", getSearchParams(request));
        String[] queryString = new String[2];
        queryString[0] = "from ZhaotouTemplate template where valid=1";
        queryString[1] = "select count(templateId) from ZhaotouTemplate template where valid=1";


        queryString = this.templateInfoManager.generateQueryString(queryString, getSearchParams(request));

        String page = request.getParameter("page");        //当前页
        String rowsNum = request.getParameter("rows");    //每页显示的行数
        Pages pages = new Pages(request);
        pages.setPage(Integer.valueOf(page));
        pages.setPerPageNum(Integer.valueOf(rowsNum));

        PageList pl = this.templateManager.getResultByQueryString(queryString[0], queryString[1], true, pages);
        List<ZhaotouTemplate> list = pl.getObjectList();
        List<TemplateVo> templateVos = new ArrayList<>();
        for (ZhaotouTemplate template : list) {
            TemplateVo templateVo = new TemplateVo();
            templateVo.setTemplateId(template.getTemplateId());
            templateVo.setAdderId(template.getAdder().getPersonId());
            templateVo.setAdderName(template.getAdder().getPerson().getPersonName());
            templateVo.setTemplateName(template.getTemplateName());
            templateVos.add(templateVo);
        }

        //定义返回的数据类型：json，使用了json-lib
        JSONObject jsonObj = new JSONObject();

        //定义rows，存放数据
        JSONArray rows = new JSONArray();
        jsonObj.put("page", pl.getPages().getCurrPage());   //当前页(名称必须为page)
        jsonObj.put("total", pl.getPages().getTotalPage()); //总页数(名称必须为total)
        jsonObj.put("records", pl.getPages().getTotals());    //总记录数(名称必须为records)

        JSONConvert convert = new JSONConvert();
        rows = convert.modelCollect2JSONArray(templateVos, new ArrayList());
        jsonObj.put("rows", rows);                            //返回到前台每页显示的数据(名称必须为rows)
        System.out.println(jsonObj);
        //设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);
    }


    //新增或者修改角色信息
    @RequestMapping(params = "method=edit")
    public String edit(HttpServletRequest request, HttpServletResponse response, TemplateVo vo, Model model) throws Exception {
//
        String rowId = request.getParameter("rowId");
        String type = request.getParameter("type");
        if (rowId != null && rowId.length() > 0) {
            vo.setTemplateId(Integer.valueOf(rowId));
        }
        Integer templateId = vo.getTemplateId();
        List<ZhaotouTemplateInfo> all = this.templateInfoManager.getAllInfo();
//
        //修改
        if (templateId != null && templateId.intValue() != 0) {

            //判断是否有编辑权限
//			this.dataRightInforManager.haveDataRight(request, response, "templateId", templateId, "edit");
//
            ZhaotouTemplate template = (ZhaotouTemplate) this.templateManager.get(templateId);

            //属性,从model到vo
            BeanUtils.copyProperties(vo, template);

//			//系统用户
            int[] InfoIds = new int[all.size()];

            List<ZhaotouTemplateInfo> templateInfos = template.getTemplateInfos();

            for (int i = 0; i < all.size(); i++) {
                ZhaotouTemplateInfo templateInfo = all.get(i);

                int tempInfoId = templateInfo.getZhaotouTemplateInfoId().intValue();

                for (Iterator it = templateInfos.iterator(); it.hasNext(); ) {
                    ZhaotouTemplateInfo info = (ZhaotouTemplateInfo) it.next();
                    int templateInfoId = info.getZhaotouTemplateInfoId().intValue();

                    if (tempInfoId == templateInfoId) {
                        //该用户属于该角色
                        InfoIds[i] = templateInfoId;
                        break;
                    }
                }
            }
            vo.setTemplateInfoIds(InfoIds);
            System.out.println(vo);
            model.addAttribute("_TemplateInfos", templateInfos);
        }

        //获取某标类型下的所有基础信息
        List<ZhaotouTemplateInfo> tech = new ArrayList<>();
        List<ZhaotouTemplateInfo> business = new ArrayList<>();
        if (all.size() > 0 && all != null) {
            for (ZhaotouTemplateInfo templateInfo : all) {
                if (templateInfo.getType().equals("技术标")) {
                    tech.add(templateInfo);
                } else if (templateInfo.getType().equals("商务标")) {
                    business.add(templateInfo);
                }
            }
        }
        model.addAttribute("_Tech", tech);
        model.addAttribute("_Business", business);
        if(StringUtil.isNotEmpty(type)&&type.equals("view")){
            return "/extend/template/viewTemplate";
        }else {
            return "/extend/template/editTemplate";
        }
    }

    //保存角色信息
    @RequestMapping(params = "method=save")
    public String save(HttpServletRequest request, HttpServletResponse response, TemplateVo vo) throws Exception {
        SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
        ZhaotouTemplate template = new ZhaotouTemplate();

        BeanUtils.copyProperties(template, vo);

        List<ZhaotouTemplateInfo> templateInfos = new ArrayList<ZhaotouTemplateInfo>();
        /** 获得用户对应的用户 */
        int[] infoIds = vo.getTemplateInfoIds();
        if (infoIds != null) {
            for (int k = 0; k < infoIds.length; k++) {
                ZhaotouTemplateInfo info = (ZhaotouTemplateInfo) this.templateInfoManager.get(infoIds[k]);
                templateInfos.add(info);
            }
        }
        template.setTemplateInfos(templateInfos);
        template.setAdder(systemUser);
        template.setValid(true);
        this.templateManager.save(template);
        return "/core/success";
    }


    @RequestMapping(params = "method=delete")
    public void delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String templateId = request.getParameter("rowId");

        JSONObject jsonObj = new JSONObject();
        boolean using = false;
        if (StringUtil.isNotEmpty(templateId)) {
            ZhaotouTemplate template = (ZhaotouTemplate) this.templateManager.get(Integer.parseInt(templateId));
            List<BidInfo> allBids = this.bidInfoManager.getAllBidInfo();
            for (BidInfo bidInfo : allBids) {
                if (bidInfo.getTemplate().getTemplateId().intValue() == template.getTemplateId().intValue()) {
                    using = true;
                    break;
                }
            }
            if (!using) {
                template.setValid(false);
                this.templateManager.save(template);
            }
        }
        jsonObj.put("msg", using);
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);
    }

    @RequestMapping(params="method=validName")
    public void validName(HttpServletRequest request,HttpServletResponse response) throws Exception {
        String templateName=request.getParameter("templateName");
        boolean valid=true;
        List<ZhaotouTemplate> allTemplates = this.templateManager.getAllTemplates();
        for(ZhaotouTemplate template:allTemplates){
            if(templateName.equals(template.getTemplateName())){
                valid=false;
                break;
            }
        }
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("_Valid", valid);
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);
    }
}


