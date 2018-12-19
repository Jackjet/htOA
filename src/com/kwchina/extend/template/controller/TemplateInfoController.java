package com.kwchina.extend.template.controller;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.SystemUserManager;
import com.kwchina.core.common.controller.BasicController;
import com.kwchina.core.common.page.PageList;
import com.kwchina.core.common.page.Pages;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.core.util.ExcelObject;
import com.kwchina.core.util.ExcelOperate;
import com.kwchina.core.util.json.JSONConvert;
import com.kwchina.core.util.string.StringUtil;
import com.kwchina.extend.template.entity.ZhaotouTemplate;
import com.kwchina.extend.template.entity.ZhaotouTemplateInfo;
import com.kwchina.extend.template.service.TemplateInfoManager;
import com.kwchina.extend.template.service.TemplateManager;
import com.kwchina.extend.template.vo.TemplateInfoVo;
import com.kwchina.oa.util.SysCommonMethod;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.collections.comparators.ComparableComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping(value="/extend/templateInfo.do")
public class TemplateInfoController extends BasicController {

	
	@Autowired
	private TemplateInfoManager templateInfoManager;

	@Autowired
	private SystemUserManager systemUserManager;

	@Autowired
	private TemplateManager templateManager;
	
	
	public JSONObject getInstances(HttpServletRequest request, HttpServletResponse response, boolean isExcel){
		JSONObject jsonObj = new JSONObject();
		
		//构造查询语句
		String[] queryString = new String[2];
		queryString[0] = "from ZhaotouTemplateInfo info where valid=1";
		queryString[1] = "select count(zhaotouTemplateInfoId) from ZhaotouTemplateInfo info where valid=1";
		
		
		queryString = this.templateInfoManager.generateQueryString(queryString, getSearchParams(request));
		
		String page = request.getParameter("page");		//当前页
		String rowsNum = request.getParameter("rows"); 	//每页显示的行数
		Pages pages = new Pages(request);
		pages.setPage(Integer.valueOf(page));
		pages.setPerPageNum(Integer.valueOf(rowsNum));
		
		PageList pl = this.templateInfoManager.getResultByQueryString(queryString[0], queryString[1], true, pages);
		List<ZhaotouTemplateInfo> list = pl.getObjectList();
		List<TemplateInfoVo> infoVos=new ArrayList<>();
		for(ZhaotouTemplateInfo template:list){
			TemplateInfoVo templateVo=new TemplateInfoVo();
			templateVo.setZhaotouTemplateInfoId(template.getZhaotouTemplateInfoId());
			templateVo.setAdderId(template.getAdder().getPersonId());
			templateVo.setAdderName(template.getAdder().getPerson().getPersonName());
			templateVo.setScore(template.getScore());
			templateVo.setStandard(template.getStandard());
			templateVo.setTarget(template.getTarget());
			templateVo.setType(template.getType());
			infoVos.add(templateVo);
		}


		//定义返回的数据类型：json，使用了json-lib
        
                  
        //定义rows，存放数据
        JSONArray rows = new JSONArray();
        jsonObj.put("page", pl.getPages().getCurrPage());   //当前页(名称必须为page)
        jsonObj.put("total", pl.getPages().getTotalPage()); //总页数(名称必须为total)
        jsonObj.put("records", pl.getPages().getTotals());	//总记录数(名称必须为records)        
        
		JSONConvert convert = new JSONConvert();
		//通知Convert，哪些关联对象需要获取
		List awareObject = new ArrayList();
		awareObject.add("author");
		rows = convert.modelCollect2JSONArray(infoVos, awareObject);
		jsonObj.put("rows", rows);							//返回到前台每页显示的数据(名称必须为rows)
		
		return jsonObj;
	}
	

	//显示所有
	@RequestMapping(params="method=list")
	public void list(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		JSONObject jsonObj = getInstances(request, response, false);
		
		//设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);
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
			JSONObject jsonObj = getInstances(request, response, false);
			
			JSONArray listArray = jsonObj.getJSONArray("rows");
			
			List<ZhaotouTemplateInfo> infors = (List)JSONArray.toCollection(listArray, ZhaotouTemplateInfo.class);


			
			//按照orderNo排序
	 		//Collections.sort(vos, new BeanComparator("logCount"));
			Comparator mycmp = ComparableComparator.getInstance();       
	        mycmp = ComparatorUtils.nullLowComparator(mycmp);  //允许null       
	        mycmp = ComparatorUtils.reversedComparator(mycmp); //逆序       
	        Comparator cmp = new BeanComparator("zhaotouTemplateInfoId", mycmp);
	        Collections.sort(infors, cmp);
			
			
			/******************导出Excel********************/
			long time = System.currentTimeMillis();
			String filePath = "/"+CoreConstant.Attachment_Path + "templateInfo/";
			String fileTitle = "模板基础信息";

			ExcelObject object = new ExcelObject();
			object.setFilePath(filePath);
			object.setFileName(fileTitle);
			object.setTitle(fileTitle);

			List rowName = new ArrayList();
			String[][] data = new String[6][infors.size()];
			int k = 0;// 列数

			rowName.add("序号");
			rowName.add("指标");
			rowName.add("分值");
			rowName.add("标准");
			rowName.add("类型");

			k = 5;

			for (int i = 0; i < infors.size(); i++) {
				ZhaotouTemplateInfo tmp = infors.get(i);
				data[0][i] = String.valueOf(i + 1);
				data[1][i] = tmp.getTarget();
				data[2][i] = String.valueOf(tmp.getScore());
				data[3][i] = tmp.getStandard();
				data[4][i] = tmp.getType();
			}

			for (int i = 0; i < k; i++) {
				object.addContentListByList(data[i]);
			}
			object.setRowName(rowName);
			ExcelOperate operate = new ExcelOperate();
			try {
				operate.exportExcel(object, infors.size(), true, request);
			} catch (IOException e) {
				e.printStackTrace();
			}

			filePath = filePath + fileTitle + ".xls";
//			request.getSession().setAttribute("_File_Path", "");
			request.getSession().removeAttribute("_File_Path_login");
			request.getSession().setAttribute("_File_Path_login", filePath);
			
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "/common/download_login";
	}

	//新增或者修改角色信息
	@RequestMapping(params = "method=view")
	public String view(HttpServletRequest request, HttpServletResponse response, TemplateInfoVo vo, Model model) throws Exception {
		String rowId = request.getParameter("rowId");
		if (rowId != null && rowId.length() > 0) {
			vo.setZhaotouTemplateInfoId((Integer.valueOf(rowId)));
			ZhaotouTemplateInfo zhaotouTemplateInfo = (ZhaotouTemplateInfo)this.templateInfoManager.get(vo.getZhaotouTemplateInfoId());
			model.addAttribute("templateInfo",zhaotouTemplateInfo);
		}
		return "/extend/template/viewInfor";
	}
	//新增或者修改角色信息
	@RequestMapping(params = "method=edit")
	public String edit(HttpServletRequest request, HttpServletResponse response, TemplateInfoVo vo, Model model) throws Exception {
		String rowId = request.getParameter("rowId");
		if (rowId != null && rowId.length() > 0) {
			vo.setZhaotouTemplateInfoId((Integer.valueOf(rowId)));
			ZhaotouTemplateInfo zhaotouTemplateInfo = (ZhaotouTemplateInfo)this.templateInfoManager.get(vo.getZhaotouTemplateInfoId());
			model.addAttribute("templateInfo",zhaotouTemplateInfo);
		}
		return "/extend/template/editInfo";
	}

	@RequestMapping(params = "method=delete")
	public void delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String infoId = request.getParameter("rowId");
		JSONObject jsonObj = new JSONObject();
		boolean using=false;
		if (StringUtil.isNotEmpty(infoId)) {
			ZhaotouTemplateInfo templateInfo= (ZhaotouTemplateInfo) this.templateInfoManager.get(Integer.parseInt(infoId));
			List<ZhaotouTemplate> allTemplates = this.templateManager.getAllTemplates();
			for(ZhaotouTemplate template:allTemplates){
				for(ZhaotouTemplateInfo info :template.getTemplateInfos()){
					if(info.getZhaotouTemplateInfoId().intValue()==templateInfo.getZhaotouTemplateInfoId().intValue()){
						using=true;
						break;
					}
				}
			}
			if(!using){
				templateInfo.setValid(false);
				this.templateInfoManager.save(templateInfo);
			}
		}
		jsonObj.put("msg", using);
		response.setContentType(CoreConstant.CONTENT_TYPE);
		response.getWriter().print(jsonObj);
	}

//	/**
//	 * 编辑
//	 * @param request
//	 * @param response
//	 * @param vo
//	 * @param model
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping(params="method=edit")
//	public String edit(HttpServletRequest request, HttpServletResponse response, AppImgVo vo, Model model) throws Exception {
//
//		Integer imgId = vo.getImgId();
//		AppImg appImg = new AppImg();
//		
//		if (imgId != null && imgId.intValue() > 0) {
//			//编辑
//			appImg = (AppImg)this.appImgManager.get(imgId);	
//			
//			//属性从model到vo
//			BeanUtils.copyProperties(vo, appImg);			
//			
//			
//			// 对附件信息进行处理
//			String attachmentFile = appImg.getAttachment();
//			if (attachmentFile != null && !attachmentFile.equals("")) {
//				String[][] attachment = processFile(attachmentFile);
//				request.setAttribute("_Attachment_Names", attachment[1]);
//				request.setAttribute("_Attachments", attachment[0]);
//			}
//		}
//		
//		
//		return "base/editAppImg";
//	}
//	
	/**
	 * 保存
	 * @throws Exception
	 */
	@RequestMapping(params="method=save")
	public String save(ZhaotouTemplateInfo zhaotouTemplateInfo,HttpServletRequest request,ModelMap modelMap) throws Exception {
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		zhaotouTemplateInfo.setAdder(systemUser);
		zhaotouTemplateInfo.setValid(true);
		ZhaotouTemplateInfo save = (ZhaotouTemplateInfo)templateInfoManager.save(zhaotouTemplateInfo);
		if (save!=null){
			modelMap.addAttribute("msg","success");
		}else {
			modelMap.addAttribute("msg","fail");
		}
		return "/core/success";
	}


	@RequestMapping(params="method=validTarget")
	public void validTarget(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String target=request.getParameter("target");
		boolean valid=true;
		List<ZhaotouTemplateInfo> allInfo = this.templateInfoManager.getAllInfo();
		for(ZhaotouTemplateInfo templateInfo:allInfo){
			if(target.equals(templateInfo.getTarget())){
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
