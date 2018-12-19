package com.kwchina.core.cms.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kwchina.core.cms.util.HtmlUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.ObjectUtils.Null;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.kwchina.core.base.entity.PersonInfor;
import com.kwchina.core.base.entity.RoleInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.OrganizeManager;
import com.kwchina.core.base.service.RoleManager;
import com.kwchina.core.base.service.SystemUserManager;
import com.kwchina.core.cms.entity.InforCategory;
import com.kwchina.core.cms.entity.InforCategoryRight;
import com.kwchina.core.cms.entity.InforDocument;
import com.kwchina.core.cms.entity.InforDocumentRight;
import com.kwchina.core.cms.entity.InforDocumentRoleRight;
import com.kwchina.core.cms.entity.InforDocumentUserRight;
import com.kwchina.core.cms.entity.InforField;
import com.kwchina.core.cms.entity.InforPraise;
import com.kwchina.core.cms.service.InforCategoryManager;
import com.kwchina.core.cms.service.InforCategoryRightManager;
import com.kwchina.core.cms.service.InforDocumentManager;
import com.kwchina.core.cms.service.InforDocumentRightManager;
import com.kwchina.core.cms.util.DocumentConverter;
import com.kwchina.core.cms.vo.InforDocumentM;
import com.kwchina.core.cms.vo.InforDocumentVo;
import com.kwchina.core.common.controller.BasicController;
import com.kwchina.core.common.page.PageList;
import com.kwchina.core.common.page.Pages;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.core.util.BeanToMapUtil;
import com.kwchina.core.util.DateHelper;
import com.kwchina.core.util.ExcelObject;
import com.kwchina.core.util.ExcelOperate;
import com.kwchina.core.util.json.JSONConvert;
import com.kwchina.core.util.string.StringUtil;
import com.kwchina.extend.club.entity.ActAttendInfor;
import com.kwchina.extend.club.entity.ClubInfor;
import com.kwchina.extend.club.entity.RegisterInfor;
import com.kwchina.extend.club.vo.ClubExcelVo;
import com.kwchina.extend.loginLog.entity.AppModuleLog;
import com.kwchina.extend.loginLog.service.AppModuleLogManager;
import com.kwchina.oa.sys.SystemConstant;
import com.kwchina.oa.util.SendInforDocument;
import com.kwchina.oa.util.SysCommonMethod;

@Controller
@RequestMapping("/cms/{inforPath}")
public class InforDocumentController extends BasicController{

	@Resource
	private InforDocumentManager inforDocumentManager;

	@Resource
	private InforCategoryManager inforCategoryManager;
	
	@Resource
	private RoleManager roleManager;
	
	@Resource
	private SystemUserManager systemUserManager;
	
	@Resource
	private OrganizeManager organizeManager;
	
	@Resource
	private InforDocumentRightManager inforDocumentRightManager;
	
	@Resource
	private InforCategoryRightManager inforCategoryRightManager;
	
	@Resource
	private AppModuleLogManager appModuleLogManager;
	

	//根据不同分类跳转到不同浏览页面
	@RequestMapping(params = "method=list")
	public String list(@PathVariable String inforPath, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//根据path获取categoryId
		String categoryId = request.getParameter("categoryId");
		if (categoryId == null || categoryId.length() == 0) {
			Integer categoryIdInt = getCategoryId(inforPath);
			if (categoryIdInt != null && categoryIdInt.intValue() > 0){
				categoryId = categoryIdInt.toString();
			}
		}
		String returnPath = "listInforDocument";
		
		//假如该分类下包含子分类,则跳转到包含左边树状分类的inforBaseList页面
		if(categoryId != null && categoryId.length() > 0){
			InforCategory category = (InforCategory)this.inforCategoryManager.get(Integer.valueOf(categoryId));
			request.setAttribute("_Category", category);
			
			if (category.getChilds() != null && category.getChilds().size() > 0) {
				returnPath = "inforBaseList";
			}
		}
		
		return returnPath;
	}
	
	//根据path获取categoryId
	public Integer getCategoryId(String inforPath) {
		
		Integer categoryId = null;//path
		//如果没有带入categoryId,则根据path去判断
		List allCategory = this.inforCategoryManager.getAll();
		for(Iterator it = allCategory.iterator();it.hasNext();){
			InforCategory category = (InforCategory)it.next();
			String urlPath = category.getUrlPath();
			if (urlPath != null && urlPath.length() > 0) {
				if(inforPath.equals(urlPath)){
					categoryId = category.getCategoryId();
					break;
				}
			}
		}
		return categoryId;
	}
	
	//获取文档信息
	@RequestMapping(params = "method=getInforDocument")
	public void getInforDocument(@PathVariable String inforPath, HttpServletRequest request, HttpServletResponse response) throws Exception {

		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		int userId = systemUser.getPersonId().intValue();
		/**
		 * 判断逻辑:
		 * 1. 通过url或参数categoryId对该分类进行判断:
		 * 	a. 如果该分类为叶子分类,则查看该分类的信息
		 * 	b. 如果该分类不是叶子分类,则查看其所有子分类的信息 
		 */		
		String ids = "";		//所有子分类的Id
		String categoryId = request.getParameter("categoryId");
		if (categoryId == null || categoryId.length() == 0) {
			//如果没有带入categoryId,则根据path去判断
			Integer categoryIdInt = getCategoryId(inforPath);
			if (categoryIdInt != null && categoryIdInt.intValue() > 0) {
				categoryId = categoryIdInt.toString();
			}
		}
		
		if(categoryId != null && categoryId.length() > 0){
			
			
			if(StringUtil.isNotEmpty(SysCommonMethod.getPlatform(request))){
				/************记录app模块使用日志************/ 
				AppModuleLog appModuleLog = new AppModuleLog();
				
				String moduleName = "公司公告";
				int categoryIdInt = Integer.valueOf(categoryId);
				if(categoryIdInt == 2){
					moduleName = "公司公告";
				}else if(categoryIdInt == 538){
					moduleName = "海通简报";
				}else if(categoryIdInt == 543){
					moduleName = "管理工作";
				}else if(categoryIdInt == 548){
					moduleName = "市场信息";
				}else if(categoryIdInt == 554){
					moduleName = "党群园地";
				}else if(categoryIdInt == 600){
					moduleName = "员工手册";
				}
				
				appModuleLog.setModuleName(moduleName);
				appModuleLog.setPlatform(SysCommonMethod.getPlatform(request));
				appModuleLog.setLogTime(new Timestamp(System.currentTimeMillis()));
				appModuleLog.setUserName(systemUser.getUserName());
				this.appModuleLogManager.save(appModuleLog);
				/*****************************************/
			}
			
			
			InforCategory category = (InforCategory)this.inforCategoryManager.get(Integer.valueOf(categoryId));
			
			if(category.getChilds() != null && category.getChilds().size() > 0){
				//需要获取该分类所有的子分类Id
				ids = this.inforCategoryManager.getChildIds(Integer.valueOf(categoryId));
			}
			int categoryIdIntaaa = Integer.valueOf(categoryId);
			if(categoryIdIntaaa == 543){
//				System.out.println("111");
			}
			//构造查询语句
			String[] queryString = new String[2];
			queryString[0] = " from InforDocument document where 1=1";
			queryString[1] = " select count(document.inforId) from InforDocument document where 1=1";
			String condition = "";
			
			//分类及其子分类
			if(ids.length() > 0){
				condition += " and document.category.categoryId in (" + ids + ")";
			}else if (categoryId != null && categoryId.length() > 0){
				condition += " and document.category.categoryId = " + categoryId;
			}
			
			/** 
			 * 判断模块是否设置了权限:
			 * a.若设置了权限,则只显示用户有权限看的文档;
			 * b.若未设置权限,则显示该模块所有文档.
			 */
//			if(category.getRights() != null && category.getRights().size() > 0){
//				condition +=" and (document.author.personId = " + userId +" or (document.inforId in (select userRight.document.inforId from InforDocumentUserRight userRight where userRight.systemUser.personId ="+userId +"))";
//				condition += " or (document.inforId in " +
//								"(select roleRight.document.inforId from InforDocumentRoleRight roleRight,SystemUserInfor user where roleRight.role in " +
//									"elements(user.roles) and user.personId = "+userId +")))";
//			}
//			
			queryString[0] += condition ;
			queryString[1] += condition;
			String sord ="desc";
			queryString = this.inforDocumentManager.generateQueryString(queryString, getSearchParams(request));
			if (categoryIdIntaaa == 554){
				queryString[0] += ",document.createTime desc ";
			}else if (categoryIdIntaaa == 600){
				queryString[0] += ",document.createTime desc ";
			}else{
				queryString[0] += ",document.inforId desc ";
			}

			String page = request.getParameter("page");		//当前页
			String rowsNum = request.getParameter("rows"); 	//每页显示的行数
			Pages pages = new Pages(request);
			pages.setPage(Integer.valueOf(page));
			pages.setPerPageNum(Integer.valueOf(rowsNum));

			PageList pl = this.inforDocumentManager.getResultByQueryString(queryString[0], queryString[1], true, pages);
			List infors = pl.getObjectList();
			
			List inforList =  new ArrayList<InforDocument>();
			Iterator it = infors.iterator();
			while(it.hasNext()){
				InforDocument inforDocument = (InforDocument)it.next();
				if(inforDocument.getInforContent()!=null){
					inforDocument.setInforContent(HtmlUtil.delHTMLTag(inforDocument.getInforContent()));//去除返回内容中的标签
				}

				if(inforDocument.getRights() != null && inforDocument.getRights().size() > 0){
					boolean hasViewRight = this.inforDocumentRightManager.hasRight(inforDocument, systemUser, InforDocumentRight._Right_View);
					boolean hasEditRight = this.inforDocumentRightManager.hasRight(inforDocument, systemUser, InforDocumentRight._Right_Edit);
					boolean hasDelRight = this.inforDocumentRightManager.hasRight(inforDocument, systemUser, InforDocumentRight._Right_Delete);
					if(hasViewRight || hasEditRight || hasDelRight || inforDocument.getAuthor().getPersonId() == userId 
							|| systemUser.getUserType() == SystemConstant._User_Type_Admin){
						inforList.add(inforDocument);
					}
				}else{
					inforList.add(inforDocument);
				}
			}
			
			//定义返回的数据类型：json，使用了json-lib
	        JSONObject jsonObj = new JSONObject();
	                  
	        //定义rows，存放数据
	        JSONArray rows = new JSONArray();
	        jsonObj.put("page", pl.getPages().getCurrPage());   //当前页(名称必须为page)
	        jsonObj.put("total", pl.getPages().getTotalPage()); //总页数(名称必须为total)
	        jsonObj.put("records", pl.getPages().getTotals());	//总记录数(名称必须为records)        
	        
			JSONConvert convert = new JSONConvert();
			//通知Convert，哪些关联对象需要获取
			List awareObject = new ArrayList();
			awareObject.add("author.person");
			awareObject.add("zuozhe.person");
			awareObject.add("author.person.department");
			awareObject.add("zuozhe.person.department");
			awareObject.add("category");
			rows = convert.modelCollect2JSONArray(infors, awareObject);



			jsonObj.put("rows", rows);							//返回到前台每页显示的数据(名称必须为rows)
//			inforContent
			//设置字符编码
	        response.setContentType(CoreConstant.CONTENT_TYPE);
	        response.getWriter().print(jsonObj);

		}
	}
	
	/**
	 * 导出excel
	 * 
	 * @param inforPath
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=expertExcel")
	public String expertExcel(@PathVariable String inforPath, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			/**
			 * 判断逻辑:
			 * 1. 通过url或参数categoryId对该分类进行判断:
			 * 	a. 如果该分类为叶子分类,则查看该分类的信息
			 * 	b. 如果该分类不是叶子分类,则查看其所有子分类的信息 
			 */		
			String ids = "";		//所有子分类的Id
			String categoryId = request.getParameter("categoryId");
			if (categoryId == null || categoryId.length() == 0) {
				//如果没有带入categoryId,则根据path去判断
				Integer categoryIdInt = getCategoryId(inforPath);
				if (categoryIdInt != null && categoryIdInt.intValue() > 0) {
					categoryId = categoryIdInt.toString();
				}
			}
			
			if(categoryId != null && categoryId.length() > 0){
				
				InforCategory category = (InforCategory)this.inforCategoryManager.get(Integer.valueOf(categoryId));
				
				
				SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
				
				//判断该用户在此分类下是否具有编辑权限，若有，则也有编辑权限
				boolean hasRight = this.inforCategoryRightManager.hasRight(category, systemUser, InforCategoryRight._Right_Edit);
				
				if(hasRight){
					if(category.getChilds() != null && category.getChilds().size() > 0){
						//需要获取该分类所有的子分类Id
						ids = this.inforCategoryManager.getChildIds(Integer.valueOf(categoryId));
					}
					
					//构造查询语句
					String[] queryString = new String[2];
					queryString[0] = " from InforDocument document where 1=1";
					queryString[1] = " select count(document.inforId) from InforDocument document where 1=1";
					String condition = "";
					
					//分类及其子分类
					if(ids.length() > 0){
						condition += " and document.category.categoryId in (" + ids + ")";
					}else if (categoryId != null && categoryId.length() > 0){
						condition += " and document.category.categoryId = " + categoryId;
					}
					
					/** 
					 * 判断模块是否设置了权限:
					 * a.若设置了权限,则只显示用户有权限看的文档;
					 * b.若未设置权限,则显示该模块所有文档.
					 */
//					SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
					int userId = systemUser.getPersonId().intValue();
//					if(category.getRights() != null && category.getRights().size() > 0){
//						condition +=" and (document.author.personId = " + userId +" or (document.inforId in (select userRight.document.inforId from InforDocumentUserRight userRight where userRight.systemUser.personId ="+userId +"))";
//						condition += " or (document.inforId in " +
//										"(select roleRight.document.inforId from InforDocumentRoleRight roleRight,SystemUserInfor user where roleRight.role in " +
//											"elements(user.roles) and user.personId = "+userId +")))";
//					}
//					
					queryString[0] += condition ;
					queryString[1] += condition;
					String sord ="desc";
					queryString = this.inforDocumentManager.generateQueryString(queryString, getSearchParams(request));
					queryString[0] += " document.createTime desc,document.inforId desc";
					String page = request.getParameter("page");		//当前页
					String rowsNum = request.getParameter("rows"); 	//每页显示的行数
					Pages pages = new Pages(request);
					pages.setPage(Integer.valueOf(page));
					pages.setPerPageNum(Integer.valueOf(rowsNum));

					PageList pl = this.inforDocumentManager.getResultByQueryString(queryString[0], queryString[1], true, pages);
					List infors = pl.getObjectList();
					
					List inforList =  new ArrayList<InforDocument>();
					Iterator it = infors.iterator();
//					while(it.hasNext()){
//						InforDocument inforDocument = (InforDocument)it.next();
//						if(inforDocument.getRights() != null && inforDocument.getRights().size() > 0){
//							boolean hasViewRight = this.inforDocumentRightManager.hasRight(inforDocument, systemUser, InforDocumentRight._Right_View);
//							boolean hasEditRight = this.inforDocumentRightManager.hasRight(inforDocument, systemUser, InforDocumentRight._Right_Edit);
//							boolean hasDelRight = this.inforDocumentRightManager.hasRight(inforDocument, systemUser, InforDocumentRight._Right_Delete);
//							if(hasViewRight || hasEditRight || hasDelRight || inforDocument.getAuthor().getPersonId() == userId 
//									|| systemUser.getUserType() == SystemConstant._User_Type_Admin){
//								inforList.add(inforDocument);
//							}
//						}else{
//							inforList.add(inforDocument);
//						}
//					}
					
					
					SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
					SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					
					/******************导出Excel********************/
//					String filePath = SystemConstant.Submit_Path + time + "/";
					String filePath = "/"+CoreConstant.Attachment_Path + "cms/";
					String fileTitle = "荣誉室";

					ExcelObject object = new ExcelObject();
					object.setFilePath(filePath);
					object.setFileName(fileTitle);
					object.setTitle(fileTitle);

					List<String> rowName = new ArrayList<String>();
					
					int k = 0;// 列数

					rowName.add("序号");
					rowName.add("主题");
					rowName.add("获奖时间");
					rowName.add("荣誉类别");
					rowName.add("荣誉类型");
					k = 9;

					
					String[][] data = new String[9][infors.size()];
					
					for (int i = 0; i < infors.size(); i++) {
						InforDocument infor = (InforDocument)infors.get(i);
						
						data[0][i] = String.valueOf(i + 1);
						data[1][i] = infor.getInforTitle();
						data[2][i] = infor.getInforTime() != null ? sf.format(infor.getInforTime()) : "";
						data[3][i] = infor.getKind();
						data[4][i] = infor.getHonorKind();
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
//					request.getSession().setAttribute("_File_Path", "");
					request.getSession().removeAttribute("_File_Path");
					request.getSession().setAttribute("_File_Path", filePath);
				}else {
					request.setAttribute("_ErrorMessage", "对不起,您无权进行该操作,请与系统管理员联系!");
					return "/common/error";
				}
				
				
				

			}
			
			
			
			
			
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "/common/download";
	}
	
	//获取某分类下的所有子分类树状显示
	@RequestMapping(params = "method=getCategoryTree")
	public void getCategoryTree(@PathVariable String inforPath, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String categoryId = request.getParameter("categoryId");
		
		if (categoryId == null || categoryId.length() == 0) {
			//如果没有带入categoryId,则根据path去判断
			Integer categoryIdInt = getCategoryId(inforPath);
			if (categoryIdInt != null && categoryIdInt.intValue() > 0) {
				categoryId = categoryIdInt.toString();
			}
		}
		
		if (categoryId != null && categoryId.length() > 0) {
			
			SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);

			ArrayList returnArray = this.inforCategoryManager.getCategoryAsTree(Integer.valueOf(categoryId), systemUser);
			
			/*InforCategory category = (InforCategory)this.inforCategoryManager.get(Integer.valueOf(categoryId));
			Set childs = category.getChilds();*/
			
			JSONObject jsonObj = new JSONObject();
			JSONConvert convert = new JSONConvert();
			
			JSONArray array = new JSONArray();
			array = convert.modelCollect2JSONArray(returnArray, new ArrayList());
			jsonObj.put("rows", array);
			
			//设置字符编码
	        response.setContentType(CoreConstant.CONTENT_TYPE);
	        response.getWriter().print(jsonObj);
		}
		
	}
	
	//获取某分类下的列表里需要显示的字段信息
	@RequestMapping(params = "method=getColData")
	public void getColData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String categoryId = request.getParameter("categoryId");
		if (categoryId != null && categoryId.length() > 0) {
			
			InforCategory category = (InforCategory)this.inforCategoryManager.get(Integer.valueOf(categoryId));
			
			//获取列表里需要显示的字段信息
			JSONArray nameArray = new JSONArray();
			JSONArray modelArray = new JSONArray();
			Set fields = category.getFields();
			
			//按listOrder排序
			List fieldList = new ArrayList(fields);
			Collections.sort(fieldList, new BeanComparator("listOrder"));
			
			nameArray.add("Id");
			modelArray.add("inforId");
			for (Iterator it=fieldList.iterator();it.hasNext();) {
				InforField field = (InforField)it.next();
				if (field.isListDisplayed()) {
					nameArray.add(field.getDisplayTitle());
					modelArray.add(field.getFieldName());
				}
			}
			nameArray.add("发布时间");
//			nameArray.add("发布人");
			modelArray.add("createTime");
//			modelArray.add("author.person.personName");
			//若是根分类,则显示＂所属分类＂信息
			if (!category.isLeaf()) {
				nameArray.add("所属分类");
				modelArray.add("category.categoryName");
			}
			
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("names", nameArray);
			jsonObj.put("model", modelArray);
			
			//设置字符编码
	        response.setContentType(CoreConstant.CONTENT_TYPE);
	        response.getWriter().print(jsonObj);
		}
		
	}
	
	//新增或者修改信息
	@RequestMapping(params = "method=edit")
	public String edit(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String redirectStr = "";
		
		InforDocument inforDocument = new InforDocument();
		String rowId = request.getParameter("rowId");
		String categoryId = request.getParameter("categoryId");
		
		
		//此处添加判断，用于判断是否是从“所有信息”中点击的或者是从具体分类下点击的，因为如果是从“所有信息”
		//点击进来的话，将判断的是根目录分类，不正确，帮需要得到此信息所属的具体分类再判断
		//针对有子分类的分类
		String isRoot = request.getParameter("isRootCategory");
		
		if(rowId!=null && rowId.length()>0){
			inforDocument = (InforDocument)this.inforDocumentManager.get(Integer.valueOf(rowId));
			
			if(isRoot != null && !isRoot.equals("")){
				if(isRoot.equals("true")){
					categoryId = inforDocument.getCategory().getCategoryId().toString();
				}
			}
		}
		
		
		
		InforCategory categoryEdit = (InforCategory)this.inforCategoryManager.get(Integer.valueOf(categoryId));
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		
		//判断该用户在此分类下是否具有编辑权限
		boolean hasRight = this.inforCategoryRightManager.hasRight(categoryEdit, systemUser, InforCategoryRight._Right_Edit);
		
		//判断该用户在此文档信息下是否具有编辑权限
		boolean hasEditRight = this.inforDocumentRightManager.hasRight(inforDocument, systemUser, InforDocumentRight._Right_Edit);
		
		Set rights = inforDocument.getRights();
		boolean hasRightToEdit = false;
		
		if (hasRight) {
			hasRightToEdit = true;
		}else if(!hasRight && hasEditRight && (rights == null || rights.size() == 0)){
			hasRightToEdit = false;
		}else if(!hasRight && hasEditRight && (rights != null && rights.size() != 0)){
			hasRightToEdit = true;
		}else if(!hasRight || !hasEditRight){
			hasRightToEdit = false;
		}
		
		if(hasRightToEdit){
			if (rowId != null && rowId.length() > 0) {
				//修改信息时
				inforDocument = (InforDocument)this.inforDocumentManager.get(Integer.valueOf(rowId));
				request.setAttribute("_InforDocument", inforDocument);
				categoryId = inforDocument.getCategory().getCategoryId().toString();
					
				//对附件信息进行处理
				/*String attachmentFile = inforDocument.getAttachment();
				String[] attachmentNames = processFile(attachmentFile);
				request.setAttribute("_Attachment_Names", attachmentNames);*/
			}
			
			//根据所属分类构造添加页面里的字段信息
			if (categoryId != null && categoryId.length() > 0) {

				
				InforCategory category = (InforCategory)this.inforCategoryManager.get(Integer.valueOf(categoryId));
				String addFields = DocumentConverter.createAddFields(category, inforDocument);
				
				request.setAttribute("_CategoryAlias", category.getUrlPath());
				request.setAttribute("_AddFields", addFields);
			}
			
			redirectStr = "editInforDocument";
		}else{
			
			redirectStr = "/common/error";
			request.setAttribute("_ErrorMessage", "对不起,您无权进行该操作,请与系统管理员联系!");
		}
		
		return redirectStr;
	}

	/**
	 * 保存信息
	 * @param request
	 * @param response
	 * @param multipartRequest
	 * @throws Exception
	 */
	@RequestMapping(params = "method=save")
	public void save(HttpServletRequest request, HttpServletResponse response, DefaultMultipartHttpServletRequest multipartRequest) throws Exception {

		InforDocument inforDocument = new InforDocument();
		String inforId = request.getParameter("inforId");
		String categoryId = request.getParameter("categoryId");
		String zuozhe = request.getParameter("zuozhe");

		String templateName = "";	//所属分类选择的内容模板
		String pagePath = "";		//所属分类的页面保存路径
		
		long nowTime = System.currentTimeMillis();
		String oldFiles = "";
		String oldPicFiles = "";
		if (inforId != null && inforId.length() > 0) {
			
			inforDocument = (InforDocument) this.inforDocumentManager.get(Integer.valueOf(inforId));
			
			categoryId = inforDocument.getCategory().getCategoryId().toString();
			
			//删除原来的html页面
			String htmlPath = CoreConstant.Context_Real_Path + inforDocument.getHtmlFilePath();
			java.io.File htmlFile = new java.io.File(htmlPath);
			if(htmlFile.exists()) {
				htmlFile.delete();
			}
			
			//修改信息时,对附件进行修改
			String filePaths = inforDocument.getAttachment();
			oldFiles = deleteOldFile(request, filePaths, "filebox");
			
			//修改信息时,对图片附件进行修改
			String picUrls = inforDocument.getDefaultPicUrl();
			oldPicFiles = deleteOldFile(request, picUrls, "picbox");
		}
		
		//保存必须字段信息(作者,是否首页显示,首页开始时间,首页结束时间,发布时间,所属分类)
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		inforDocument.setAuthor(systemUser);
		String homepage = request.getParameter("homepage");

		if (("true").equals(homepage)) {
			inforDocument.setHomepage(true);
		}else {
			inforDocument.setHomepage(false);
		}

		String top = request.getParameter("topp");
		inforDocument.setTopp(top);

		
		//是否互通
		boolean isHandOut = false;
		
		String handOut = request.getParameter("handOut");
		if (("true").equals(handOut)) {
			isHandOut = true;
			inforDocument.setHandOut(true);
		}else {
			inforDocument.setHandOut(false);
		}
		
		
		
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		if (startDate != null && startDate.length() > 0) {
			inforDocument.setStartDate(Date.valueOf(startDate));
		}else {
			inforDocument.setStartDate(null);
		}
		if (endDate != null && endDate.length() > 0) {
			inforDocument.setEndDate(Date.valueOf(endDate));
		}else {
			inforDocument.setEndDate(null);
		}
		inforDocument.setCreateTime(new Date(nowTime));
		inforDocument.setZuozhe(zuozhe);
		InforCategory category = new InforCategory();
		if (categoryId != null && categoryId.length() > 0) {
			category = (InforCategory)this.inforCategoryManager.get(Integer.valueOf(categoryId));
			templateName = category.getContentTemplate();
			pagePath = category.getPagePath();
		}
		inforDocument.setCategory(category);
		
		
		/******荣誉室时,保存新加的两个字段*****/
		if(category != null && category.getUrlPath().equals("honorroom")){
			inforDocument.setKind(request.getParameter("kind"));
			inforDocument.setHonorKind(request.getParameter("honorKind"));
		}
		
		//保存其他字段信息(信息主题,发布单位,是否重要,关键字,信息内容,信息时间,相关链接,信息来源)
		inforDocument.setInforTitle(request.getParameter("inforTitle"));
		inforDocument.setIssueUnit(request.getParameter("issueUnit"));
		String important = request.getParameter("important");
		if (("true").equals(important)) {
			inforDocument.setImportant(true);
		}else {
			inforDocument.setImportant(false);
		}
		inforDocument.setKeyword(request.getParameter("keyword"));
		inforDocument.setInforContent(request.getParameter("inforContent"));
		String inforTime = request.getParameter("inforTime");
		if (inforTime != null && inforTime.length() > 0) {
			inforDocument.setInforTime(Date.valueOf(inforTime));
		}else {
			inforDocument.setInforTime(null);
		}
		inforDocument.setRelateUrl(request.getParameter("relateUrl"));
		inforDocument.setSource(request.getParameter("source"));
		
		//保存附件(一般附件和图片)
		String attachment = "";		//附件路径
		if (multipartRequest != null) {
			String defaultPicUrl = "";	//图片路径
			Iterator iterator = multipartRequest.getFileNames();
			
			while (iterator.hasNext()) {
				MultipartFile multifile = multipartRequest.getFile((String) iterator.next());
				String fieldName = multifile.getName();
				String folder = "";
				if (fieldName.indexOf("attachment")>-1) {
					folder = "cms/attach";
				}else if (fieldName.indexOf("defaultPicUrl")>-1) {
					folder = "cms/picture";
				}

				if (multifile != null && multifile.getSize() != 0) {						
					String savePath = CoreConstant.Attachment_Path + folder;
					
					//类似于 D:\tomcat55\webapps\ROOT/upload\message;
					File file = new File(CoreConstant.Context_Real_Path + savePath);
					if(!file.exists()){
						file.mkdir();
					}
					
					//在folder下面建立目录，以当前时间为目录
					long current = System.currentTimeMillis();
					savePath += "/" + current;
					file = new File(CoreConstant.Context_Real_Path + savePath);
					if(!file.exists()){
						file.mkdir();
					}
					
					//获取文件名，并保存到该目录下
					String fileName = multifile.getOriginalFilename();
					savePath +=  "/" + fileName;
					String filePath = CoreConstant.Context_Real_Path + savePath;
					file = new File(filePath);
					if (!file.exists()) {
						file.mkdirs();
					}
					
					multifile.transferTo(file);			
					
					//保存到数据库中的信息(仅记录Contextpath之后的路径)
					if (fieldName.indexOf("attachment")>-1) {
						attachment = attachment + savePath + "|";
					}else if (fieldName.indexOf("defaultPicUrl")>-1) {
						defaultPicUrl = defaultPicUrl + savePath + "|";
					}
				} 
			}
			//现有路径加上原有附件路径
			if(attachment == null || attachment.length() == 0){
				attachment = oldFiles;
			}else{
				if(oldFiles != null && oldFiles.length() > 0){
					attachment += oldFiles;
				}				
			}
			//现有路径加上原有图片路径
			if(defaultPicUrl == null || defaultPicUrl.length() == 0){
				defaultPicUrl = oldPicFiles;
			}else{
				if(oldPicFiles != null && oldPicFiles.length() > 0){
					defaultPicUrl += oldPicFiles;
				}				
			}
			inforDocument.setAttachment(attachment);
			inforDocument.setDefaultPicUrl(defaultPicUrl);
		}
		
		//保存自定义字段信息(自定义字符1,自定义字符2,自定义字符3,自定义时间1,自定义时间2,自定义Boolean型,自定义Decimal型)
		inforDocument.setDefStr1(request.getParameter("defStr1"));
		inforDocument.setDefStr2(request.getParameter("defStr2"));
		inforDocument.setDefStr3(request.getParameter("defStr3"));
		String defDate1 = request.getParameter("defDate1");
		if (defDate1 != null && defDate1.length() > 0) {
			inforDocument.setDefDate1(Date.valueOf(defDate1));
		}
		String defDate2 = request.getParameter("defDate2");
		if (defDate2 != null && defDate2.length() > 0) {
			inforDocument.setDefDate2(Date.valueOf(defDate2));
		}
		String defBool1 = request.getParameter("defBool1");
		if (("true").equals(defBool1)) {
			inforDocument.setDefBool1(true);
		}else {
			inforDocument.setDefBool1(false);
		}
		String defDec1 = request.getParameter("defDec1");
		if (defDec1 != null && defDec1.length() > 0) {
			inforDocument.setDefDec1(new BigDecimal(defDec1));
		}
		
		//自动生成html静态页面并返回html文件保存路径
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("_InforDocument", inforDocument);
		
		//附件信息
		String attachmentFile = inforDocument.getAttachment();
		if (attachmentFile != null && attachmentFile.length() > 0) {
			String[] arrayFiles = attachmentFile.split("\\|");
			//字符串转换为utf-8编码
			String[] newArrayFiles = new String[arrayFiles.length];
			for (int i=0;i<arrayFiles.length;i++) {
				newArrayFiles[i] = java.net.URLEncoder.encode(arrayFiles[i],CoreConstant.ENCODING);
			}
			map.put("_ArrayFiles", newArrayFiles);

			String[] attachmentNames = new String[arrayFiles.length];
			for (int k = 0; k < arrayFiles.length; k++) {
				attachmentNames[k] = com.kwchina.core.util.File.getFileName(arrayFiles[k]);
			}
			map.put("_AttachmentNames", attachmentNames);
		}
		
		//图片附件
		String defaultPicUrl = inforDocument.getDefaultPicUrl();
		if (defaultPicUrl != null && defaultPicUrl.length() > 0) {
			String[] arrayPics = defaultPicUrl.split("\\|");
			//字符串转换为utf-8编码
			String[] newArrayPics = new String[arrayPics.length];
			for (int i=0;i<arrayPics.length;i++) {
				newArrayPics[i] = java.net.URLEncoder.encode(arrayPics[i],CoreConstant.ENCODING);
			}
			map.put("_ArrayPics", newArrayPics);

			String[] picNames = new String[arrayPics.length];
			for (int k = 0; k < arrayPics.length; k++) {
				picNames[k] = com.kwchina.core.util.File.getFileName(arrayPics[k]);
			}
			map.put("_PicNames", picNames);
		}

		//this.inforDocumentManager.save(inforDocument);
	
		map.put("base", request.getContextPath());
		if (templateName != null && templateName.length() > 0 && pagePath != null && pagePath.length() > 0) {
			templateName = templateName.split("/")[templateName.split("/").length-1];
			String htmlPath = DocumentConverter.createHtml(templateName, pagePath, map);
			inforDocument.setHtmlFilePath(htmlPath);
			//String ss = DocumentConverter.createHtml(templateName, pagePath, map);
		}
		
		this.inforDocumentManager.save(inforDocument);
		//int ss= inforDocument.getInforId();
		
		try {
			//互通时，调用webservice接口
			if(isHandOut){
				SendInforDocument sendInforDocument = new SendInforDocument();
				InforDocumentVo vo = new InforDocumentVo();
				BeanUtils.copyProperties(vo, inforDocument);
				vo.setCategoryId(inforDocument.getCategory().getCategoryId());
				vo.setStartDateStr(inforDocument.getStartDate() == null ? "" : inforDocument.getStartDate().toString());
				vo.setEndDateStr(inforDocument.getEndDate() == null ? "" : inforDocument.getEndDate().toString());
				vo.setCreateTimeStr(inforDocument.getCreateTime() == null ? "" : inforDocument.getCreateTime().toString());
				vo.setInforTimeStr(inforDocument.getInforTime() == null ? "" : inforDocument.getInforTime().toString());
				vo.setDefDate1Str(inforDocument.getDefDate1() == null ? "" : inforDocument.getDefDate1().toString());
				vo.setDefDate2Str(inforDocument.getDefDate2() == null ? "" : inforDocument.getDefDate2().toString());
				vo.setDefDec1D(inforDocument.getDefDec1() == null ? 0 : inforDocument.getDefDec1().doubleValue());
				Map<String,Object> dataMap = BeanToMapUtil.convertBean(vo);
				sendInforDocument.sendInforDocument(dataMap,attachment,"192.168.61.20");
				sendInforDocument.sendInforDocument(dataMap,attachment,"192.168.61.21");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/** 该语句不放在编辑页面的原因:
		 * 若在编辑页面提交数据后立即执行window.close()操作,
		 * 则后台无法取到编辑页面的数据.
		 * (此情况仅在页面包含附件操作时存在) 
		 * */
		PrintWriter out = response.getWriter();
		out.print("<script language='javascript'>");
		out.print("var returnArray = [\"refresh\","+categoryId+"];");
		out.print("window.returnValue = returnArray;");
		out.print("window.close();");
		out.print("</script>");
		
	}
	
	
//	/**
//	 * 查看信息--手机版所需格式
//	 * 
//	 * @param request
//	 * @param response
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping(params = "method=view_m")
//	public void view_m(HttpServletRequest request, HttpServletResponse response) throws Exception {
//
//		InforDocument inforDocument = new InforDocument();
//		
//		JSONObject jsonObj = new JSONObject();
//		JSONConvert convert = new JSONConvert();
//		String inforIdStr = request.getParameter("inforId");
//		if(inforIdStr != null && !inforIdStr.equals("")){
//			Integer inforId = Integer.valueOf(inforIdStr);
//			inforDocument = (InforDocument)this.inforDocumentManager.get(inforId);
//		}
//			
////		jsonObj.put("_InforDocument", inforDocument);
//		JSONArray inforArray = new JSONArray();
//		List inforList = new ArrayList();
//		inforList.add(inforDocument);
//		inforArray = convert.modelCollect2JSONArray(inforList, new ArrayList());
//		jsonObj.put("_InforDocument", inforArray);
//		
//		//查找当前人是否已点赞
//		boolean hasPraised = false;
//		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
//		Set<InforPraise> praises = inforDocument.getPraises();
//		for(InforPraise praise : praises){
//			int praiserId = praise.getPraiser().getPersonId().intValue();
//			if(praiserId == systemUser.getPersonId().intValue() && praise.getPraised() == 1){
//				hasPraised = true;
//				break;
//			}
//		}
//		jsonObj.put("_HasPraised", hasPraised);
//		
//		//点赞数
//		int praiseCount = 0;
//		for(InforPraise praise : praises){
//			if(praise.getPraised() == 1){
//				praiseCount += 1;
//			}
//		}
//		jsonObj.put("_PraisedCount", praiseCount);
//		
//
//		jsonObj.put("_CommentCount", inforDocument.getComments().size());
//		
//		//设置字符编码
//        response.setContentType(CoreConstant.CONTENT_TYPE);
//        response.getWriter().print(jsonObj);		
//	}
	
	/**
	 * 查看信息--手机版所需格式
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=view_m")
	public void view_m(HttpServletRequest request, HttpServletResponse response) throws Exception {

		InforDocument inforDocument = new InforDocument();
		
		JSONObject jsonObj = new JSONObject();
		JSONConvert convert = new JSONConvert();
		String inforIdStr = request.getParameter("inforId");
		if(inforIdStr != null && !inforIdStr.equals("")){
			Integer inforId = Integer.valueOf(inforIdStr);
			inforDocument = (InforDocument)this.inforDocumentManager.get(inforId);
		}
		
		//转换对象
		InforDocumentM infor_m = new InforDocumentM();
		if(infor_m != null){
			infor_m.setInforId(inforDocument.getInforId());
			infor_m.setInforTitle(inforDocument.getInforTitle());
			infor_m.setIssueUnit(inforDocument.getIssueUnit());
			infor_m.setHomepage(inforDocument.isHomepage());
			if(inforDocument.getStartDate() != null){
				infor_m.setStartDate(DateHelper.getString(inforDocument.getStartDate()));
			}else {
				infor_m.setStartDate("");
			}
			if(inforDocument.getEndDate() != null){
				infor_m.setEndDate(DateHelper.getString(inforDocument.getEndDate()));
			}else {
				infor_m.setEndDate("");
			}
			infor_m.setImportant(inforDocument.isImportant());
			infor_m.setKeyword(inforDocument.getKeyword());
			
			if(StringUtil.isNotEmpty(inforDocument.getInforContent())){
				String tmpContent = inforDocument.getInforContent().replaceAll("http://htoa.haitongauto.com:80", "http://htoa.haitongauto.com");
				infor_m.setInforContent(tmpContent);
			}else {
				infor_m.setInforContent("");
			}
			
			
			infor_m.setAttachment(inforDocument.getAttachment());
			if(inforDocument.getCreateTime() != null){
				infor_m.setCreateTime(DateHelper.getString(inforDocument.getCreateTime()));
			}else {
				infor_m.setCreateTime("");
			}
			
			if(inforDocument.getInforTime() != null){
				infor_m.setInforTime(DateHelper.getString(inforDocument.getInforTime()));
			}else {
				infor_m.setInforTime("");
			}
			
			infor_m.setRelateUrl(inforDocument.getRelateUrl());
			infor_m.setHits(inforDocument.getHits());
			infor_m.setSource(inforDocument.getSource());
			infor_m.setDefaultPicUrl(inforDocument.getDefaultPicUrl());
			infor_m.setHtmlFilePath(inforDocument.getHtmlFilePath());
			infor_m.setDefStr1(inforDocument.getDefStr1());
			infor_m.setDefStr2(inforDocument.getDefStr2());
			infor_m.setDefStr3(inforDocument.getDefStr3());
			if(inforDocument.getDefDate1() != null){
				infor_m.setDefDate1(DateHelper.getString(inforDocument.getDefDate1()));
			}else {
				infor_m.setDefDate1("");
			}
			
			if(inforDocument.getDefDate2() != null){
				infor_m.setDefDate2(DateHelper.getString(inforDocument.getDefDate2()));
			}else {
				infor_m.setDefDate2("");
			}
			
			infor_m.setDefBool1(inforDocument.isDefBool1());
			infor_m.setDefDec1(inforDocument.getDefDec1());
			if(inforDocument.getAuthor() != null){
				infor_m.setAuthor(inforDocument.getAuthor().getPerson().getPersonName());
			}else {
				infor_m.setAuthor("");
			}
			
			infor_m.setKind(inforDocument.getKind());
			infor_m.setHonorKind(inforDocument.getHonorKind());
			infor_m.setHandOut(inforDocument.isHandOut());
			infor_m.setReceived(inforDocument.isReceived());
			
		}
		
//		jsonObj.put("_InforDocument", inforDocument);
		JSONArray inforArray = new JSONArray();
		List inforList = new ArrayList();
		inforList.add(infor_m);
		inforArray = convert.modelCollect2JSONArray(inforList, new ArrayList());
		jsonObj.put("_InforDocument", inforArray);
		
		//查找当前人是否已点赞
		boolean hasPraised = false;
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		Set<InforPraise> praises = inforDocument.getPraises();
		for(InforPraise praise : praises){
			int praiserId = praise.getPraiser().getPersonId().intValue();
			if(praiserId == systemUser.getPersonId().intValue() && praise.getPraised() == 1){
				hasPraised = true;
				break;
			}
		}
		jsonObj.put("_HasPraised", hasPraised);
		
		//点赞数
		int praiseCount = 0;
		for(InforPraise praise : praises){
			if(praise.getPraised() == 1){
				praiseCount += 1;
			}
		}
		jsonObj.put("_PraisedCount", praiseCount);
		

		jsonObj.put("_CommentCount", inforDocument.getComments().size());
		
		//设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);		
	}
	
	
	//删除相关信息
	@RequestMapping(params = "method=delete")
	public void delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		String rowIds = request.getParameter("rowIds");
		PrintWriter out = response.getWriter();
		int categoryId = 0;
		
		List rightsList = new ArrayList();
		
		if (rowIds != null && rowIds.length() > 0) {
			String[] detleteIds = rowIds.split(",");
			if (detleteIds.length > 0) {
				for (int i = 0; i < detleteIds.length; i++) {

					Integer inforId = Integer.valueOf(detleteIds[i]);
					InforDocument inforDocument = (InforDocument)this.inforDocumentManager.get(inforId);
					
					//查出所属分类
					categoryId = inforDocument.getCategory().getCategoryId();
					InforCategory categoryDelete = (InforCategory)this.inforCategoryManager.get(Integer.valueOf(categoryId));
					
					//判断该用户在此分类下是否具有删除权限
					boolean hasRight = this.inforCategoryRightManager.hasRight(categoryDelete, systemUser, InforCategoryRight._Right_Delete);
					
					//判断该用户在此文档信息下是否具有删除权限
					boolean hasEditRight = this.inforDocumentRightManager.hasRight(inforDocument, systemUser, InforDocumentRight._Right_Delete);
					
					Set rights = inforDocument.getRights();
					boolean hasRightToDelete = false;
					
					if (hasRight) {
						hasRightToDelete = true;
					}else if(!hasRight && hasEditRight && (rights == null || rights.size() == 0)){
						hasRightToDelete = false;
					}else if(!hasRight && hasEditRight && (rights != null && rights.size() != 0)){
						hasRightToDelete = true;
					}else if(!hasRight || !hasEditRight){
						hasRightToDelete = false;
					}
					
					rightsList.add(hasRightToDelete);
					
					if(hasRightToDelete){
						//删除生成的html页面
						String filePath = CoreConstant.Context_Real_Path + inforDocument.getHtmlFilePath();
						java.io.File file = new java.io.File(filePath);
						if(file.exists()) {
							file.delete();
						}
						
						//删除附件
						String filePaths = inforDocument.getAttachment();
						deleteFiles(filePaths);
						
						//删除图片附件
						String defaultPicUrls = inforDocument.getDefaultPicUrl();
						deleteFiles(defaultPicUrls);
						
						this.inforDocumentManager.remove(inforDocument);
//						out.print(1);
					}else{
//						out.print(0);
					}
				}
			}
		}
		//如果是批量删除，其中有一个有权限删除，即提示成功
		int outString = 0;
		if(rightsList.contains(true)){
			outString = 1;
		}
		out.print(outString);
	}
	
	//编辑信息权限
	@RequestMapping(params = "method=editInforRight")
	public String editInforRight(HttpServletRequest request, HttpServletResponse response) {
		
		
		String redirectStr = "";
		
		InforDocument inforDocument = new InforDocument();
		String rowId = request.getParameter("rowId");
		String categoryId = request.getParameter("categoryId");
		
		
		//此处添加判断，用于判断是否是从“所有信息”中点击的或者是从具体分类下点击的，因为如果是从“所有信息”
		//点击进来的话，将判断的是根目录分类，不正确，帮需要得到此信息所属的具体分类再判断
		//针对有子分类的分类
		String isRoot = request.getParameter("isRootCategory");
		
		if(rowId!=null && rowId.length()>0){
			inforDocument = (InforDocument)this.inforDocumentManager.get(Integer.valueOf(rowId));
			
			if(isRoot != null && !isRoot.equals("")){
				if(isRoot.equals("true")){
					categoryId = inforDocument.getCategory().getCategoryId().toString();
				}
			}
		}
		
		InforCategory categoryEdit = (InforCategory)this.inforCategoryManager.get(Integer.valueOf(categoryId));
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		
		//判断该用户在此分类下是否具有授权权限（默认和是否有编辑权限同权限）
		boolean hasRight = this.inforCategoryRightManager.hasRight(categoryEdit, systemUser, InforCategoryRight._Right_Edit);

		//判断该用户在此文档信息下是否具有编辑权限
		boolean hasEditRight = this.inforDocumentRightManager.hasRight(inforDocument, systemUser, InforDocumentRight._Right_Edit);
		
		Set authorRights = inforDocument.getRights();
		boolean hasRightToEdit = false;
		
		if (hasRight) {
			hasRightToEdit = true;
		}else if(!hasRight && hasEditRight && (authorRights == null || authorRights.size() == 0)){
			hasRightToEdit = false;
		}else if(!hasRight && hasEditRight && (authorRights != null && authorRights.size() != 0)){
			hasRightToEdit = true;
		}else if(!hasRight || !hasEditRight){
			hasRightToEdit = false;
		}
		
		if(hasRightToEdit){
			if (rowId != null && rowId.length() > 0) {
				inforDocument = (InforDocument)this.inforDocumentManager.get(Integer.valueOf(rowId));
				request.setAttribute("_CategoryId", inforDocument.getCategory().getCategoryId());
				
				//根据职级获取用户
				List persons = this.systemUserManager.getUserByPosition(PersonInfor._Position_Tag);		
				request.setAttribute("_Persons", persons);
				
				//获取职级大于一定值的用户
				List otherPersons = this.systemUserManager.getOtherPositionUser(PersonInfor._Position_Tag);		
				request.setAttribute("_OtherPersons", otherPersons);

				//部门信息
				List organizes = this.organizeManager.getDepartments();
				request.setAttribute("_Organizes", organizes);
				
				//角色信息
				List roles = this.roleManager.getAll(); 
				request.setAttribute("_Roles", roles);
				
				//用户信息
				List users = this.systemUserManager.getAllValid();
				request.setAttribute("_Users", users);

				//权限信息
				int[] editUserIds = new int[users.size()];
				int[] deleteUserIds = new int[users.size()];
				int[] viewUserIds = new int[users.size()];
				int[] editRoleIds = new int[roles.size()];
				int[] deleteRoleIds = new int[roles.size()];
				int[] viewRoleIds = new int[roles.size()];
				int rightType = 0;

				int k = 0;
				Set rights = inforDocument.getRights();
				for (Iterator it = rights.iterator(); it.hasNext();) {
					InforDocumentRight right = (InforDocumentRight) it.next();
					if (right instanceof InforDocumentUserRight) {
						rightType = 1;
						/** 用户权限 */
						InforDocumentUserRight userRight = (InforDocumentUserRight)right;
						int userId = userRight.getSystemUser().getPersonId().intValue();
							
						//编辑权限
						if (this.inforDocumentRightManager.hasRight(right, InforDocumentRight._Right_Edit)) {
							editUserIds[k] = userId;
						}

						//删除权限
						if (this.inforDocumentRightManager.hasRight(right, InforDocumentRight._Right_Delete)) {
							deleteUserIds[k] = userId;
						}

						//浏览权限
						if (this.inforDocumentRightManager.hasRight(right, InforDocumentRight._Right_View)) {
							viewUserIds[k] = userId;
						}
					}else if (right instanceof InforDocumentRoleRight) {
						/** 角色权限 */
						InforDocumentRoleRight roleRight = (InforDocumentRoleRight)right;
						int roleId = roleRight.getRole().getRoleId().intValue();

						//编辑权限
						if (this.inforDocumentRightManager.hasRight(right, InforDocumentRight._Right_Edit)) {
							editRoleIds[k] = roleId;
						}

						//删除权限
						if (this.inforDocumentRightManager.hasRight(right, InforDocumentRight._Right_Delete)) {
							deleteRoleIds[k] = roleId;
						}

						//浏览权限
						if (this.inforDocumentRightManager.hasRight(right, InforDocumentRight._Right_View)) {
							viewRoleIds[k] = roleId;
						}
					}
					k += 1;
				}
				
				request.setAttribute("_EditUserIds", editUserIds);
				request.setAttribute("_DeleteUserIds", deleteUserIds);
				request.setAttribute("_ViewUserIds", viewUserIds);
				request.setAttribute("_EditRoleIds", editRoleIds);
				request.setAttribute("_DeleteRoleIds", deleteRoleIds);
				request.setAttribute("_ViewRoleIds", viewRoleIds);
				request.setAttribute("_RightType", rightType);
			}
			
			
			redirectStr = "editInforDocumentRight";
		}else{
			
			redirectStr = "/common/error";
			request.setAttribute("_ErrorMessage", "对不起,您无权进行该操作,请与系统管理员联系!");
		}
		
		return redirectStr;
	}
	
	
	//保存信息权限
	@RequestMapping(params = "method=saveInforRight")
	public void saveInforRight(HttpServletRequest request, HttpServletResponse response) {
		
		String inforId = request.getParameter("inforId");
		String rightType = request.getParameter("rightType");
		InforDocument inforDocument = new InforDocument();
		
		if (inforId != null && inforId.length() > 0) {
			inforDocument = (InforDocument)this.inforDocumentManager.get(Integer.valueOf(inforId));
			
			Set rights = inforDocument.getRights();
			inforDocument.getRights().removeAll(rights);
				
			if (rightType.equals("0")) {
				/** 为角色权限时 */
				String[] editRoleIds = request.getParameterValues("editRoleIds");
				String[] deleteRoleIds = request.getParameterValues("deleteRoleIds");
				String[] viewRoleIds = request.getParameterValues("viewRoleIds");
					
				//新增,修改
				if (editRoleIds != null && editRoleIds.length > 0) {
					for (int i=0;i<editRoleIds.length;i++) {
						RoleInfor role = (RoleInfor)this.roleManager.get(Integer.valueOf(editRoleIds[i]));
						InforDocumentRoleRight roleRight = new InforDocumentRoleRight();
						roleRight.setDocument(inforDocument);
						roleRight.setOperateRight(1);
						roleRight.setRole(role);
						inforDocument.getRights().add(roleRight);
					}
				}
				//删除
				if (deleteRoleIds != null && deleteRoleIds.length > 0) {
					Set oldRights = inforDocument.getRights();
					for (int i=0;i<deleteRoleIds.length;i++) {
						boolean has = false;
						for (Iterator it=oldRights.iterator();it.hasNext();) {
							//假如前面的权限操作中已包含该角色权限,则需要在此基础上进行修改
							InforDocumentRoleRight roleRight = (InforDocumentRoleRight)it.next();
							if (roleRight.getRole().getRoleId().intValue() == Integer.valueOf(deleteRoleIds[i])) {
								roleRight.setOperateRight(roleRight.getOperateRight() + 2);
									
								has = true;
								break;
							}
						}
							
						if (!has) {
							//假如前面的权限操作中不包含该角色权限,则创建该权限
							RoleInfor role = (RoleInfor)this.roleManager.get(Integer.valueOf(deleteRoleIds[i]));
							InforDocumentRoleRight roleRight = new InforDocumentRoleRight();
							roleRight.setDocument(inforDocument);
							roleRight.setOperateRight(2);
							roleRight.setRole(role);
							inforDocument.getRights().add(roleRight);
						}
					}
				}
				//浏览
				if (viewRoleIds != null && viewRoleIds.length > 0) {
					Set oldRights = inforDocument.getRights();
					for (int i=0;i<viewRoleIds.length;i++) {
						boolean has = false;
						for (Iterator it=oldRights.iterator();it.hasNext();) {
							//假如前面的权限操作中已包含该角色权限,则需要在此基础上进行修改
							InforDocumentRoleRight roleRight = (InforDocumentRoleRight)it.next();
							if (roleRight.getRole().getRoleId().intValue() == Integer.valueOf(viewRoleIds[i])) {
								roleRight.setOperateRight(roleRight.getOperateRight() + 4);
									
								has = true;
								break;
							}
						}
							
						if (!has) {
							//假如前面的权限操作中不包含该角色权限,则创建该权限
							RoleInfor role = (RoleInfor)this.roleManager.get(Integer.valueOf(viewRoleIds[i]));
							InforDocumentRoleRight roleRight = new InforDocumentRoleRight();
							roleRight.setDocument(inforDocument);
							roleRight.setOperateRight(4);
							roleRight.setRole(role);
							inforDocument.getRights().add(roleRight);
						}
					}
				}
			}else {
				/** 为用户权限时 */
				String[] editUserIds = request.getParameterValues("editUserIds");
				String[] deleteUserIds = request.getParameterValues("deleteUserIds");
				String[] viewUserIds = request.getParameterValues("viewUserIds");
					
				//新增,修改
				if (editUserIds != null && editUserIds.length > 0) {
					for (int i=0;i<editUserIds.length;i++) {
						SystemUserInfor user = (SystemUserInfor)this.systemUserManager.get(Integer.valueOf(editUserIds[i]));
						InforDocumentUserRight userRight = new InforDocumentUserRight();
						userRight.setDocument(inforDocument);
						userRight.setOperateRight(1);
						userRight.setSystemUser(user);
						inforDocument.getRights().add(userRight);
					}
				}
				//删除
				if (deleteUserIds != null && deleteUserIds.length > 0) {
					Set oldRights = inforDocument.getRights();
					for (int i=0;i<deleteUserIds.length;i++) {
						boolean has = false;
						for (Iterator it=oldRights.iterator();it.hasNext();) {
							//假如前面的权限操作中已包含该角色权限,则需要在此基础上进行修改
							InforDocumentUserRight userRight = (InforDocumentUserRight)it.next();
							if (userRight.getSystemUser().getPersonId().intValue() == Integer.valueOf(deleteUserIds[i])) {
								userRight.setOperateRight(userRight.getOperateRight() + 2);
									
								has = true;
								break;
							}
						}
							
						if (!has) {
							//假如前面的权限操作中不包含该角色权限,则创建该权限
							SystemUserInfor user = (SystemUserInfor)this.systemUserManager.get(Integer.valueOf(deleteUserIds[i]));
							InforDocumentUserRight userRight = new InforDocumentUserRight();
							userRight.setDocument(inforDocument);
							userRight.setOperateRight(2);
							userRight.setSystemUser(user);
							inforDocument.getRights().add(userRight);
						}
					}
				}
				//浏览
				if (viewUserIds != null && viewUserIds.length > 0) {
					Set oldRights = inforDocument.getRights();
					for (int i=0;i<viewUserIds.length;i++) {
						boolean has = false;
						for (Iterator it=oldRights.iterator();it.hasNext();) {
							//假如前面的权限操作中已包含该角色权限,则需要在此基础上进行修改
							InforDocumentUserRight userRight = (InforDocumentUserRight)it.next();
							if (userRight.getSystemUser().getPersonId().intValue() == Integer.valueOf(viewUserIds[i])) {
								userRight.setOperateRight(userRight.getOperateRight() + 4);
									
								has = true;
								break;
							}
						}
							
						if (!has) {
							//假如前面的权限操作中不包含该角色权限,则创建该权限
							SystemUserInfor user = (SystemUserInfor)this.systemUserManager.get(Integer.valueOf(viewUserIds[i]));
							InforDocumentUserRight userRight = new InforDocumentUserRight();
							userRight.setDocument(inforDocument);
							userRight.setOperateRight(4);
							userRight.setSystemUser(user);
							inforDocument.getRights().add(userRight);
						}
					}
				}
					
			}
			this.inforDocumentManager.save(inforDocument);
		}
	}
	
	
	/**
	 * 获取需要首页显示的发布信息(用于首页显示)
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=getInfors")
	public void getInfors(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Date sysDate = new Date(System.currentTimeMillis());
		
		String queryHQL = " from InforDocument inforDocument where inforDocument.homepage = 1";
		String condition = "";
		
		String categoryId = request.getParameter("categoryId");
		if (categoryId != null && categoryId.length() > 0) {
			condition += " and category.categoryId = " + categoryId;
		}
		queryHQL += condition;
		queryHQL += " and '" + sysDate + "' between inforDocument.startDate and inforDocument.endDate order by inforDocument.createTime desc";
		
		List infors = this.inforDocumentManager.getResultByQueryString(queryHQL);
		
		JSONObject jsonObj = new JSONObject();
		JSONConvert convert = new JSONConvert();
		JSONArray jsonArray = new JSONArray();
		
		//通知Convert，哪些关联对象需要获取
		List awareObject = new ArrayList();
		awareObject.add("author.person");
		
		jsonArray = convert.modelCollect2JSONArray(infors, awareObject);
		jsonObj.put("_Infors", jsonArray);
		
		//设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);

	}


	/**
	 * 点击量
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=getHits")
	public void getHits(HttpServletRequest request, HttpServletResponse response) throws Exception {
		InforDocument inforDocument = new InforDocument();
		request.setCharacterEncoding("gbk");
		String  inforId = request.getParameter("inforId").replace(",", "");

		
		
	if (inforId != null && inforId.length() > 0) {
			
			inforDocument = (InforDocument) this.inforDocumentManager.get(Integer.valueOf(inforId));
			inforDocument.setHits(inforDocument.getHits()+1);
	}
	this.inforDocumentManager.save(inforDocument);
	}
	
	/**
	 * 显示点击量
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=getNos")
	public void getNos(HttpServletRequest request, HttpServletResponse response) throws Exception {
		InforDocument inforDocument = new InforDocument();
		String  inforId = request.getParameter("inforId").replace(",", "");

		String ss= request.getRequestURL().toString();
		
	if (inforId != null && inforId.length() > 0) {
			
		inforDocument = (InforDocument) this.inforDocumentManager.get(Integer.valueOf(inforId));
			
			JSONObject jsonObj = new JSONObject();
			JSONConvert convert = new JSONConvert();
			JSONArray jsonArray = new JSONArray();
			
			//通知Convert，哪些关联对象需要获取
			//AwareObject awareObject = new AwareObject();
			//awareObject.add("author.person");
			
			//jsonArray = convert.model2JSON(inforDocument, jsonObj);
			jsonObj.put("_Infors", inforDocument.getHits());
			
			//设置字符编码
	        response.setContentType(CoreConstant.CONTENT_TYPE);
	        response.getWriter().print(jsonObj);
			
	}

	}
	
	
	@RequestMapping(params = "method=createHTML")
	public void createHTML(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//得到信息发布类的所有列表
		List allInforDocuments = this.inforDocumentManager.getAll();
		System.out.println("----总数为："+allInforDocuments.size());
		
		int count=0;
		Iterator allIterator = allInforDocuments.iterator();
		while(allIterator.hasNext()){
			InforDocument inforDocument = (InforDocument)allIterator.next();
			InforCategory category = inforDocument.getCategory();
			String templateName = category.getContentTemplate();
			String pagePath = category.getPagePath();
			
			
			
			
			//自动生成html静态页面并返回html文件保存路径
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("_InforDocument", inforDocument);
			
			//附件信息
			String attachmentFile = inforDocument.getAttachment();
			if (attachmentFile != null && attachmentFile.length() > 0) {
				String[] arrayFiles = attachmentFile.split("\\|");
				//字符串转换为utf-8编码
				String[] newArrayFiles = new String[arrayFiles.length];
				for (int i=0;i<arrayFiles.length;i++) {
					newArrayFiles[i] = java.net.URLEncoder.encode(arrayFiles[i],CoreConstant.ENCODING);
				}
				map.put("_ArrayFiles", newArrayFiles);

				String[] attachmentNames = new String[arrayFiles.length];
				for (int k = 0; k < arrayFiles.length; k++) {
					attachmentNames[k] = com.kwchina.core.util.File.getFileName(arrayFiles[k]);
				}
				map.put("_AttachmentNames", attachmentNames);
			}
			
			//图片附件
			String defaultPicUrl = inforDocument.getDefaultPicUrl();
			if (defaultPicUrl != null && defaultPicUrl.length() > 0) {
				String[] arrayPics = defaultPicUrl.split("\\|");
				//字符串转换为utf-8编码
				String[] newArrayPics = new String[arrayPics.length];
				for (int i=0;i<arrayPics.length;i++) {
					newArrayPics[i] = java.net.URLEncoder.encode(arrayPics[i],CoreConstant.ENCODING);
				}
				map.put("_ArrayPics", newArrayPics);

				String[] picNames = new String[arrayPics.length];
				for (int k = 0; k < arrayPics.length; k++) {
					picNames[k] = com.kwchina.core.util.File.getFileName(arrayPics[k]);
				}
				map.put("_PicNames", picNames);
			}
			
			map.put("base", request.getContextPath());
			if (templateName != null && templateName.length() > 0 && pagePath != null && pagePath.length() > 0) {
				templateName = templateName.split("/")[templateName.split("/").length-1];
				String htmlPath = DocumentConverter.createHtml(templateName, pagePath, map);
				inforDocument.setHtmlFilePath(htmlPath);
			}
			this.inforDocumentManager.save(inforDocument);
			count += 1;
			System.out.println(count+"----"+inforDocument.getInforId());
		}
		
		
	}

}
