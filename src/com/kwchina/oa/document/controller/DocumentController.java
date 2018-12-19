package com.kwchina.oa.document.controller;

import java.io.PrintWriter;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.kwchina.core.base.entity.OrganizeInfor;
import com.kwchina.core.base.entity.PersonInfor;
import com.kwchina.core.base.entity.RoleInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.OrganizeManager;
import com.kwchina.core.base.service.RoleManager;
import com.kwchina.core.base.service.SystemUserManager;
import com.kwchina.core.cms.entity.InforCategory;
import com.kwchina.core.common.controller.BasicController;
import com.kwchina.core.common.page.PageList;
import com.kwchina.core.common.page.Pages;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.core.util.json.JSONConvert;
import com.kwchina.core.util.multisearch.ConditionUtils;
import com.kwchina.oa.document.entity.DocumentCategory;
import com.kwchina.oa.document.entity.DocumentCategoryRight;
import com.kwchina.oa.document.entity.DocumentInfor;
import com.kwchina.oa.document.entity.DocumentInforRight;
import com.kwchina.oa.document.entity.DocumentInforRoleRight;
import com.kwchina.oa.document.entity.DocumentInforUserRight;
import com.kwchina.oa.document.service.CategoryRightManager;
import com.kwchina.oa.document.service.DocumentCategoryManager;
import com.kwchina.oa.document.service.DocumentInforManager;
import com.kwchina.oa.document.service.DocumentInforRightManager;
import com.kwchina.oa.document.util.DocumentConstant;
import com.kwchina.oa.document.vo.DocumentCategoryVo;
import com.kwchina.oa.document.vo.DocumentInforVo;
import com.kwchina.oa.submit.util.SubmitConstant;
import com.kwchina.oa.sys.SystemConstant;
import com.kwchina.oa.util.SysCommonMethod;
import com.kwchina.oa.workflow.customfields.service.CommonManager;
import com.kwchina.oa.workflow.customfields.util.CnToSpell;
import com.kwchina.oa.workflow.entity.FlowDefinition;
import com.kwchina.oa.workflow.entity.FlowInstanceInfor;
import com.kwchina.oa.workflow.entity.InstanceLayerInfor;
import com.kwchina.oa.workflow.service.FlowDefinitionManager;
import com.kwchina.oa.workflow.service.FlowInstanceManager;

@Controller
@RequestMapping("/document/document.do")
public class DocumentController extends BasicController {

//	private static Log log = LogFactory.getLog(DocumentController.class);

	@Autowired
	private DocumentInforManager documentInforManager;

	@Autowired
	private DocumentCategoryManager documentCategoryManager;

	@Autowired
	private SystemUserManager systemUserManager;

	@Autowired
	private OrganizeManager organizeManager;

	@Autowired
	private RoleManager roleManager;

	@Autowired
	private DocumentInforRightManager documentInforRightManager;

	@Resource
	private FlowInstanceManager flowInstanceManager;

	@Resource
	private FlowDefinitionManager flowManager;

	@Resource
	private CommonManager commonManager;

	@Resource
	private CategoryRightManager documentCategoryRightManager;
	
	/**
	 * 按照树状结构获取文档分类信息
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=list")
	public void listTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		if (log.isDebugEnabled()) {
//			log.debug("entering 'DocumentAction.listTree' method...");
//		}
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);

		ArrayList returnArray = this.documentCategoryManager.getCategoryAsTree(DocumentConstant._Root_Category_Id, systemUser);

		// 将set集置空
		Iterator it = returnArray.iterator();
		List returnList = new ArrayList();
		while (it.hasNext()) {
			DocumentCategory dc = (DocumentCategory) it.next();

			dc.setChilds(null);
			dc.setDocuments(null);
			dc.setRights(null);
			if(dc.getCategoryName().equals("党群收文") || dc.getCategoryName().equals("党群发文") || dc.getCategoryName().equals("党群内部报告")){

			}else {
				returnList.add(dc);
			}

		}

		String page = request.getParameter("page"); // 当前页
		String rowsNum = request.getParameter("rows"); // 每页显示的行数
		Pages pages = new Pages(request);
		pages.setPage(Integer.valueOf(page));
		pages.setPerPageNum(Integer.valueOf(rowsNum));

		// 定义返回的数据类型：json，使用了json-lib
		JSONObject jsonObj = new JSONObject();

		// 定义rows，存放数据
		JSONArray rows = new JSONArray();

		JSONConvert convert = new JSONConvert();
		rows = convert.modelCollect2JSONArray(returnList, new ArrayList());
		jsonObj.put("rows", rows); // 返回到前台每页显示的数据(名称必须为rows)

		// 设置字符编码
		response.setContentType(CoreConstant.CONTENT_TYPE);
		response.getWriter().print(jsonObj);
	}

	/**
	 * 获取查询条件数据(分类名称信息)
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=getCategoryName")
	public void getCategoryName(HttpServletRequest request, HttpServletResponse response) throws Exception {

		JSONObject jsonObj = new JSONObject();
		JSONConvert convert = new JSONConvert();

		// 当前登录用户ID
		// SystemUserInfor user = SysCommonMethod.getSystemUser(request);
		// int personId = user.getPersonId();

		// 分类信息
		JSONArray categoryArray = new JSONArray();
		List categoryNames = this.documentInforManager.getCategoryName();
		categoryArray = convert.modelCollect2JSONArray(categoryNames, new ArrayList());
		jsonObj.put("_CategoryNames", categoryArray);

		// 设置字符编码
		response.setContentType(CoreConstant.CONTENT_TYPE);
		response.getWriter().print(jsonObj);
	}

	/**
	 * 修改分类名称
	 * 
	 * @param request
	 * @param response
	 * @param vo
	 * @throws Exception
	 */
	@RequestMapping(params = "method=rename")
	public void save(HttpServletRequest request, HttpServletResponse response, DocumentCategoryVo vo) throws Exception {

		DocumentCategory category = new DocumentCategory();
		Integer categoryId = Integer.valueOf(request.getParameter("cateId"));

		if (categoryId != null && categoryId.intValue() != 0) {
			category = (DocumentCategory) this.documentCategoryManager.get(categoryId.intValue());
			category.setCategoryName(request.getParameter("categoryName"));
		}

		this.documentCategoryManager.save(category);

	}

	/**
	 * 删除分类名称
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=deleteCategoryName")
	public void deleteCategoryName(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Integer categoryId = Integer.valueOf(request.getParameter("cateId"));
		if (categoryId != null && categoryId.intValue() > 0) {
			DocumentCategory category = (DocumentCategory) this.documentCategoryManager.get(categoryId);
			deleteChildren(category);

		}
	}

	/**
	 * 
	 * @param category
	 */
	private void deleteChildren(DocumentCategory category) {
		Set childs = category.getChilds();

		if (childs != null && childs.size() > 0) {
			List tmpList = new ArrayList(childs);
			for (Iterator it = tmpList.iterator(); it.hasNext();) {
				DocumentCategory tpS = (DocumentCategory) it.next();

				// 从父对象移除
				childs.remove(tpS);

				deleteChildren(tpS);
			}
		}
		this.documentCategoryManager.remove(category);
	}

	/**
	 * 根据path获取categoryId
	 * 
	 * @param inforPath
	 * @return
	 */
	public Integer getCategoryId(String inforPath) {

		Integer categoryId = null;

		// 如果没有带入categoryId,则根据path去判断
		List allCategory = this.documentCategoryManager.getAll();
		for (Iterator it = allCategory.iterator(); it.hasNext();) {
			InforCategory category = (InforCategory) it.next();
			String urlPath = category.getUrlPath();
			if (urlPath != null && urlPath.length() > 0) {
				if (inforPath.equals(urlPath)) {
					categoryId = category.getCategoryId();
					break;
				}
			}
		}
		return categoryId;
	}

	/**
	 * 获取文档信息
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=getDocumentInfor")
	public void getInforDocument(HttpServletRequest request, HttpServletResponse response) throws Exception {

		/**
		 * 判断逻辑: 1. 通过url或参数categoryId对该分类进行判断: a. 如果该分类为叶子分类,则查看该分类的信息 b.
		 * 如果该分类不是叶子分类,则查看其所有子分类的信息 2.判断该分类是否为归档分类 a.若果不是,则直接查看文档大全下面的信息
		 * b.如果是,则通过reportId获取到工作流里的信息
		 */
		String ids = ""; // 所有子分类的Id
		String categoryId = request.getParameter("categoryId");
		int fromReport = 0; // 判断该分类是否为归档分类 0-否,1-是.

		// 当点击某个分类时
		if (categoryId != null && categoryId.length() > 0) {

			DocumentCategory category = (DocumentCategory) this.documentCategoryManager.get(Integer.valueOf(categoryId));
			fromReport = category.getFromReport();
			request.setAttribute("_Category", category);

			if (category.getChilds() != null && category.getChilds().size() > 0) {
				// 需要获取该分类所有的子分类Id
				ids = this.documentCategoryManager.getChildIds(Integer.valueOf(categoryId));
			}
		}

		// 构造查询语句
		String[] queryString = new String[2];
		queryString[0] = " from DocumentInfor document where 1=1";
		queryString[1] = " select count(document.documentId) from DocumentInfor document where 1=1";
		String condition = "";

		// 分类及其子分类
		if (ids.length() > 0) {
			condition += " and document.category.categoryId in (" + ids + ")";
		} else if (categoryId != null && categoryId.length() > 0) {
			condition += " and document.category.categoryId = " + categoryId;
		}

		/**
		 * 判断模块是否设置了权限: a.若设置了权限,则只显示用户有权限看的文档; b.若未设置权限,则显示该模块所有文档.
		 */
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		int userId = systemUser.getPersonId().intValue();
		RoleInfor role2 = (RoleInfor)this.roleManager.get(53);
		boolean canEdit2=roleManager.belongRole(systemUser,role2);
		if (systemUser.getUserType() != SystemConstant._User_Type_Admin) {
			if (categoryId.equals("164") && canEdit2){
			}else
			condition += " and (document.author.personId = " + userId + " or document.editor.personId = " + userId
					+ " or (document.documentId in (select userRight.document.documentId from DocumentInforUserRight userRight where userRight.systemUser.personId =" + userId + "))";
			condition += " or (document.documentId in " + "(select roleRight.document.documentId from DocumentInforRoleRight roleRight,SystemUserInfor user where roleRight.role in " + "elements(user.roles) and user.personId = " + userId + ")))";
		}
		
		queryString[0] += condition;

		// 用于获取该类中所有DocumentInfor的reportId
		String getReportIdStr = queryString[0];

		String page = request.getParameter("page"); // 当前页
		String rowsNum = request.getParameter("rows"); // 每页显示的行数

		// 如果该类为归档类，则通过reportId获取FlowInstanceInfor
		if (fromReport == 1) {
			// 构造查询语句
			String[] queryStringw = new String[2];
			queryStringw[0] = " from FlowInstanceInfor instance where 1=1";
			queryStringw[1] = " select count(instanceId) from FlowInstanceInfor instance where 1=1";
			String conditionw = "";
			
			//工作流里面的类别Id
			Integer flowId = null; 
			if (categoryId != null) {
				int categoryIdInt = Integer.parseInt(categoryId);
				if (categoryIdInt != 0) {
					if (categoryIdInt == DocumentConstant.SubmitFlow_Report_Receive) {
						flowId = SubmitConstant.SubmitFlow_Report_Receive;
					} else if (categoryIdInt == DocumentConstant.SubmitFlow_Report_Publish) {
						flowId = SubmitConstant.SubmitFlow_Report_Publish;
					} else if (categoryIdInt == DocumentConstant.SubmitFlow_Party_Receive) {
						flowId = SubmitConstant.SubmitFlow_Party_Receive;
					} else if (categoryIdInt == DocumentConstant.SubmitFlow_Party_Publish) {
						flowId = SubmitConstant.SubmitFlow_Party_Publish;
					} else if (categoryIdInt == DocumentConstant.SubmitFlow_Report_Inside) {
						flowId = SubmitConstant.SubmitFlow_Report_Inside;
					} else if (categoryIdInt == DocumentConstant.SubmitFlow_Party_Inside) {
						flowId = SubmitConstant.SubmitFlow_Party_Inside;
					} else if (categoryIdInt == DocumentConstant.SubmitFlow_Report_Board) {
						flowId = SubmitConstant.SubmitFlow_Report_Board;
					} else if (categoryIdInt == DocumentConstant.SubmitFlow_HR_ResignApproval) {
						flowId = SubmitConstant.SubmitFlow_HR_ResignApproval;
					} else if (categoryIdInt == DocumentConstant.SubmitFlow_HR_ResignProcedure) {
						flowId = SubmitConstant.SubmitFlow_HR_ResignProcedure;
					} else if (categoryIdInt == DocumentConstant.SubmitFlow_HR_DynamicPersonnel) {
						flowId = SubmitConstant.SubmitFlow_HR_DynamicPersonnel;
					} else if (categoryIdInt == DocumentConstant.SubmitFlow_HR_TrainingApproval) {
						flowId = SubmitConstant.SubmitFlow_HR_TrainingApproval;
					} else if (categoryIdInt == DocumentConstant.SubmitFlow_Report_Contract) {
						flowId = SubmitConstant.SubmitFlow_Report_Contract;
					}else if (categoryIdInt == DocumentConstant.SubmitFlow_Report_Order) {
						flowId = SubmitConstant.SubmitFlow_Report_Order;
					}
				}
			}

			// 所属流程Id
			String tableName = "";
			String flowName = "";
			if (flowId != null && flowId != 0) {
				conditionw += " and flowDefinition.flowId = " + flowId;
				FlowDefinition flow = (FlowDefinition) this.flowManager.get(Integer.valueOf(flowId));
				flowName = CnToSpell.getFullSpell(flow.getFlowName());
				tableName = "Customize_" + flowName;
			}
			
			//根据流程名获取表列名
			List columnNames = this.commonManager.getColumnNames(flowName);

			queryStringw[0] += conditionw;
			queryStringw[1] += conditionw;

			// 获取自定义表单查询字段
			Map map = getSearchFields(request, response);
			JSONArray searchFields = (JSONArray) map.get("_SearchFields");

			// 构造查询条件
			String[] params = getSearchParams(request);
			String cusCondition = ""; // 自定义查询条件
			if (params[2].equals("true")) {
				if (params[3] != null && params[3].length() > 0) {

					JSONObject filter = JSONObject.fromObject(params[3]);
					JSONArray rules = filter.getJSONArray("rules"); // 取数据中的查询信息:查询字段,查询条件,查询数据
					if (rules != null && rules.size() > 0) {
						for (int i = 0; i < rules.size(); i++) {
							JSONObject tmpObj = (JSONObject) rules.get(i);
							String fieldValue = tmpObj.getString("field"); // 查询字段
							String opValue = tmpObj.getString("op"); // 查询条件:大于,等于,小于..
							String dataValue = tmpObj.getString("data"); // 查询数据

							// 处理查询条件
							boolean has = false;
							if (searchFields != null && searchFields.size() > 0) {
								for (int j = 0; j < searchFields.size(); j++) {
									JSONArray fieldArray = (JSONArray) searchFields.get(j);
									JSONObject tmpField = (JSONObject) fieldArray.get(1);
									String searchField = tmpField.getString("searchField");
									if (searchField.equals(fieldValue)) {
										has = true;
										// 组织自定义表单查询条件
										String tmpCondition = ConditionUtils.getCondition(fieldValue, opValue, dataValue);
										cusCondition += " and " + tmpCondition;
										break;
									}
								}
							}
							if (!has) {
								// 过滤自定义表单查询字段
								String tmpCondition = ConditionUtils.getCondition(fieldValue, opValue, dataValue);
								queryStringw[0] += " and " + tmpCondition;
								queryStringw[1] += " and " + tmpCondition;
							}
						}
					}
				}
			}

			// 加上自定义表单查询条件
			if (cusCondition.length() > 0 && tableName.length() > 0) {
				String sql = "select instanceId from " + tableName + " where 1=1" + cusCondition;
				List instancIds = this.flowInstanceManager.getResultBySQLQuery(sql);
				String instancIdsStr = "";
				if (instancIds != null && instancIds.size() > 0) {
					int index = 0;
					for (Iterator it = instancIds.iterator(); it.hasNext();) {
						instancIdsStr += ((Integer) it.next()).toString();
						if (index < instancIds.size() - 1) {
							instancIdsStr += ",";
						}
						index++;
					}
					queryStringw[0] += " and instanceId in(" + instancIdsStr + ")";
					queryStringw[1] += " and instanceId in(" + instancIdsStr + ")";
					getReportIdStr += " and reportId in (" + instancIdsStr + ")";
				} else {
					//没有满足查询条件的数据
					queryStringw[0] += " and instanceId in(0)";
					queryStringw[1] += " and instanceId in(0)";
					getReportIdStr += " and reportId in (0)";
				}
			}

			// 找出归档了的文件
			List listReportId = this.documentInforManager.getResultByQueryString(getReportIdStr);
			if (listReportId != null && listReportId.size() != 0) {
				String instancIdsStr = "";
				int index = 0;
				for (Iterator it = listReportId.iterator(); it.hasNext();) {
					DocumentInfor docu = (DocumentInfor) it.next();
					instancIdsStr += ((Integer) docu.getReportId()).toString();
					if (index < listReportId.size() - 1) {
						instancIdsStr += ",";
					}
					index++;
				}
				queryStringw[0] += " and instanceId in(" + instancIdsStr + ")";
				queryStringw[1] += " and instanceId in(" + instancIdsStr + ")";
			}else{
				queryStringw[0] += " and instanceId in (0)";
				queryStringw[1] += " and instanceId in (0)";
			}

			queryStringw[0] += " order by " + params[0] + " " + params[1];

			Pages pages = new Pages(request);
			pages.setPage(Integer.valueOf(page));
			pages.setPerPageNum(Integer.valueOf(rowsNum));

			PageList pl = this.flowInstanceManager.getResultByQueryString(queryStringw[0], queryStringw[1], true, pages);
			List infors = pl.getObjectList();
			// 定义返回的数据类型：json，使用了json-lib
			JSONObject jsonObj = new JSONObject();

			// 定义rows，存放数据
			JSONArray rows = new JSONArray();
			jsonObj.put("page", pl.getPages().getCurrPage()); // 当前页(名称必须为page)
			jsonObj.put("total", pl.getPages().getTotalPage()); // 总页数(名称必须为total)
			jsonObj.put("records", pl.getPages().getTotals()); // 总记录数(名称必须为records)
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (Iterator it = infors.iterator(); it.hasNext();) {
				FlowInstanceInfor fi = (FlowInstanceInfor) it.next();

				// 获取对应的DocumentInfor
				String sql = "from DocumentInfor document where 1=1 and document.reportId = " + fi.getInstanceId();
				DocumentInfor doc = (DocumentInfor) this.documentInforManager.getResultByQueryString(sql).get(0);
				if (fi != null) {
					JSONObject obj = new JSONObject();
					obj.put("instanceId", fi.getInstanceId());
					obj.put("instanceTitle", fi.getInstanceTitle());
					String startTime = null;
					if (fi.getStartTime() != null) {
						startTime = sdf.format(fi.getStartTime());
					}
					obj.put("documentId", doc.getDocumentId());
					obj.put("startTime", startTime);
					obj.put("updateTime", sdf.format(fi.getUpdateTime()));
					obj.put("applier", fi.getApplier().getPerson().getPersonName());
					obj.put("department", fi.getDepartment().getOrganizeName());
					obj.put("flow", fi.getFlowDefinition().getFlowName());
					if (fi.getCharger() != null) {
						obj.put("charger", fi.getCharger().getPerson().getPersonName());
					} else {
						obj.put("charger", null);
					}
					
					/** 自定义表单数据 */
					if (columnNames != null && columnNames.size() > 0) {
						List customizeDatas = this.commonManager.getFormData(flowName, fi.getInstanceId());
						if (customizeDatas != null && customizeDatas.size() > 0) {
							Object[] dataArray = (Object[])customizeDatas.get(0);
							
							int i=0;
							for (Iterator col=columnNames.iterator();col.hasNext();) {
								String colName = (String)col.next();
								obj.put(colName, dataArray[i]);
								i++;
							}
						}
					}
					/*****/
					
					rows.add(obj);
				}
			}
			jsonObj.put("rows", rows); // 返回到前台每页显示的数据(名称必须为rows)

			// 全部分类树状显示(需要判断分类的权限)
			ArrayList returnArray = this.documentCategoryManager.getCategoryAsTree(DocumentConstant._Root_Category_Id);
			request.setAttribute("_TREE", returnArray);

			// 所属部门
			List departments = this.organizeManager.getDepartments();
			request.setAttribute("_Departments", departments);

			// 设置字符编码
			response.setContentType(CoreConstant.CONTENT_TYPE);
			response.getWriter().print(jsonObj);

		} else {
			
//			queryString[0] += " order by updateTime desc";
			queryString[1] += condition;
			
			queryString = this.documentInforManager.generateQueryString(queryString, getSearchParams(request));
			
			Pages pages = new Pages(request);
			pages.setPage(Integer.valueOf(page));
			pages.setPerPageNum(Integer.valueOf(rowsNum));

			PageList pl = this.documentInforManager.getResultByQueryString(queryString[0], queryString[1], true, pages);
			List infors = pl.getObjectList();

			// 定义返回的数据类型：json，使用了json-lib
			JSONObject jsonObj = new JSONObject();

			// 定义rows，存放数据
			JSONArray rows = new JSONArray();
			jsonObj.put("page", pl.getPages().getCurrPage()); // 当前页(名称必须为page)
			jsonObj.put("total", pl.getPages().getTotalPage()); // 总页数(名称必须为total)
			jsonObj.put("records", pl.getPages().getTotals()); // 总记录数(名称必须为records)

			JSONConvert convert = new JSONConvert();
			// 通知Convert，哪些关联对象需要获取
			List awareObject = new ArrayList();
			awareObject.add("category");
			awareObject.add("author.person");
			awareObject.add("editor.person");
			awareObject.add("department");
			rows = convert.modelCollect2JSONArray(infors, awareObject);
			jsonObj.put("rows", rows); // 返回到前台每页显示的数据(名称必须为rows)

			// 全部分类树状显示(需要判断分类的权限)
			ArrayList returnArray = this.documentCategoryManager.getCategoryAsTree(DocumentConstant._Root_Category_Id);
			request.setAttribute("_TREE", returnArray);

			// 所属部门
			List departments = this.organizeManager.getDepartments();
			request.setAttribute("_Departments", departments);

			// 设置字符编码
			response.setContentType(CoreConstant.CONTENT_TYPE);
			response.getWriter().print(jsonObj);
		}

	}

	/**
	 * 新增或者修改信息
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=edit")
	public String edit(HttpServletRequest request, HttpServletResponse response, DocumentInforVo vo, Model model) throws Exception {

		DocumentInfor document = new DocumentInfor();
		// String documentId = request.getParameter("rowId");
		Integer documentId = vo.getDocumentId();
		// 创建时间
		String createTime = new Date(System.currentTimeMillis()).toString();
		// String categoryId = "";
		// DocumentCategory category = null;
		
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);

		if (documentId != null && documentId.intValue() > 0) {
			// 修改信息时
			document = (DocumentInfor) documentInforManager.get(Integer.valueOf(documentId));
			// request.setAttribute("_DocumentInfor", document);
			
			/** 判断对该文档是否有编辑权限 */
			boolean canEdit = this.documentInforRightManager.hasRight(document, systemUser, DocumentInforRight._Right_Edit);
			if (!canEdit) {
				request.setAttribute("_ErrorMessage", "对不起,您无权进行该操作,请与系统管理员联系!");
				return "/common/error";
			}
			BeanUtils.copyProperties(vo, document);

			// 所属分类、所属部门、作者
			vo.setCategoryId(document.getCategory().getCategoryId());
			vo.setDepartmentId(document.getDepartment().getOrganizeId());
			vo.setAuthorId(document.getAuthor().getPersonId());

			// 创建时间
			if (document.getCreateTime() != null) {
				createTime = document.getCreateTime().toString();
			}

			/*
			 * categoryId = document.getCategory().getCategoryId().toString();
			 * int categoryIdd = Integer.valueOf(categoryId);
			 * 
			 * //所属分类 category = (DocumentCategory)
			 * this.documentCategoryManager.get(categoryIdd);
			 * BeanUtils.copyProperties(vo, category);
			 * 
			 * //父分类 if (category.getParent() != null) {
			 * vo.setParentId(category.getParent().getCategoryId().intValue()); }
			 * //是否叶子分类 if (category.isLeaf()){ vo.setLeaf(true); }else{
			 * vo.setLeaf(false); }
			 */
			// 对附件信息进行处理
			String attachmentFile = document.getAttachment();
			if (attachmentFile != null && !attachmentFile.equals("")) {
				String[][] attachment = processFile(attachmentFile);
				request.setAttribute("_Attachment_Names", attachment[1]);
				request.setAttribute("_Attachments", attachment[0]);
			}
		}else {
			
			/** 判断在该分类下是否有添加文档的权限 */
			boolean canAdd = false;
			String categoryId = request.getParameter("categoryId");
			if (categoryId != null && categoryId.length() > 0) {
				DocumentCategory category = (DocumentCategory)this.documentCategoryManager.get(Integer.valueOf(categoryId));
				canAdd = this.documentCategoryRightManager.hasRight(category, systemUser, DocumentCategoryRight._Right_Create);
			}
			if (!canAdd) {
				request.setAttribute("_ErrorMessage", "对不起,您无权进行该操作,请与系统管理员联系!");
				return "/common/error";
			}
		}
		request.setAttribute("_CreateTime", createTime);

		// 获取部门信息
		List departments = this.organizeManager.getDepartments();
		// model.addAttribute("_Departments", departments);
		request.setAttribute("_Departments", departments);

		/*
		 * //获取部门信息 List organizes = this.organizeManager.getDepartments();
		 * request.setAttribute("_Organizes", organizes);
		 */

		/*
		 * //获取用户信息 List users = this.systemUserManager.getAll();
		 * request.setAttribute("_Users", users);
		 */

		// 全部分类树状显示(需要判断分类的权限)
		ArrayList returnArray = this.documentCategoryManager.getCategoryAsTree(DocumentConstant._Root_Category_Id);
		request.setAttribute("_TREE", returnArray);
		// request.setAttribute("_Category", category);

		/*
		 * //当前登录用户 SystemUserInfor systemUser =
		 * SysCommonMethod.getSystemUser(request);
		 * request.setAttribute("_SysUser", systemUser);
		 */

		// 所有用户
		List allSystemUsers = systemUserManager.findUserOrderByPersonName();
		request.setAttribute("_AllSystemUsers", allSystemUsers);

		/*
		 * //当前时间 SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		 * java.util.Date date = new java.util.Date(); String today =
		 * sf.format(date); request.setAttribute("_today", today);
		 */

		return "editDocumentInfor";
	}

	/**
	 * 查看信息
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=view")
	public String view(HttpServletRequest request, HttpServletResponse response, Model model, DocumentCategoryVo vo) throws Exception {

		try{
		DocumentInfor document = new DocumentInfor();
		String rowId = request.getParameter("rowId");
		String categoryId = request.getParameter("categoryId");
			DocumentCategory category = null;
		// 获取用户信息
		// List users = this.systemUserManager.getAll();



		if (rowId != null && rowId.length() > 0) {
			if (categoryId == null){
				document = (DocumentInfor) documentInforManager.get(Integer.valueOf(rowId));
			}else {
				String sql = "from DocumentInfor document where 1=1 and document.reportId = " + rowId;
				document = (DocumentInfor) this.documentInforManager.getResultByQueryString(sql).get(0);
			}

			request.setAttribute("_DocumentInfor", document);

			categoryId = document.getCategory().getCategoryId().toString();
			int categoryIdd = Integer.valueOf(categoryId);
			// 所属分类
			category = (DocumentCategory) this.documentCategoryManager.get(categoryIdd);
			BeanUtils.copyProperties(vo, category);

			// 父分类
			if (category.getParent() != null) {
				vo.setParentId(category.getParent().getCategoryId().intValue());
			}
			// 是否叶子分类
			if (category.isLeaf()) {
				vo.setLeaf(true);
			} else {
				vo.setLeaf(false);
			}
			// 对附件信息进行处理
			String attachmentFile = document.getAttachment();
			if (attachmentFile != null && !attachmentFile.equals("")) {
				String[][] attachment = processFile(attachmentFile);
				request.setAttribute("_Attachment_Names", attachment[1]);
				request.setAttribute("_Attachments", attachment[0]);
			}

			// 获取对应的公文信息
			if (document.getReportId() > 0) {
				FlowInstanceInfor flowInstanceInfor = (FlowInstanceInfor) this.flowInstanceManager.get(document.getReportId());
				request.setAttribute("_FlowInstanceInfor", flowInstanceInfor);

				// 对应公文信息的附件
				String attachmentFile2 = flowInstanceInfor.getAttach();
				if (attachmentFile2 != null && !attachmentFile2.equals("")) {
					String[][] attachment = processFile(attachmentFile2);
					request.setAttribute("_Attachment_Names", attachment[1]);
					request.setAttribute("_Attachments", attachment[0]);
				}

				// 对应公文信息的正式附件
				String formalAttach = flowInstanceInfor.getFormalAttach();
				if (formalAttach != null && !formalAttach.equals("")) {
					String[][] attachment = processFile(formalAttach);
					request.setAttribute("_FormalAttach_Names", attachment[1]);
					request.setAttribute("_FormalAttachs", attachment[0]);
				}
			}

		}

		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "viewDocumentInfor";
	}

	/**
	 * 保存信息
	 * 
	 * @param request
	 * @param response
	 * @param multipartRequest
	 * @throws Exception
	 */
	@RequestMapping(params = "method=save")
	public void save(HttpServletRequest request, HttpServletResponse response, Model model, DocumentInforVo vo, DefaultMultipartHttpServletRequest multipartRequest) throws Exception {

		DocumentInfor documentInfor = new DocumentInfor();
		// String documentId = request.getParameter("documentId");
		Integer documentId = vo.getDocumentId();

		// 当前登录用户
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);

		String oldFiles = "";

		// 修改保存时
		if (documentId != null && documentId.intValue() > 0) {
			documentInfor = (DocumentInfor) this.documentInforManager.get(documentId);

			// 修改信息时,对附件进行修改
			String filePaths = documentInfor.getAttachment();
			oldFiles = deleteOldFile(request, filePaths, "attatchmentArray");
		}

		// 上传附件
		String attachment = this.uploadAttachment(multipartRequest, "document");

		BeanUtils.copyProperties(documentInfor, vo);

		// 更新时间、创建时间
		documentInfor.setUpdateTime(new Date(System.currentTimeMillis()));
		String createTime = request.getParameter("createTime");
		if (createTime != null && createTime.length() > 0) {
			documentInfor.setCreateTime(Date.valueOf(createTime));
		}

		// 保存作者信息
		SystemUserInfor author = (SystemUserInfor) this.systemUserManager.get(Integer.valueOf(request.getParameter("authorId")));
		documentInfor.setAuthor(author);

		// 保存修改者信息
		documentInfor.setEditor(systemUser);

		// 保存部门信息
		OrganizeInfor department = new OrganizeInfor();
		department.setOrganizeId(Integer.valueOf(request.getParameter("departmentId")));

		documentInfor.setDepartment(department);

		// 保存文档分类
		DocumentCategory category = new DocumentCategory();
		category.setCategoryId(Integer.valueOf(request.getParameter("categoryId")));

		documentInfor.setCategory(category);

		// 对附件信息的判断
		if (attachment == null || attachment.equals("")) {
			attachment = oldFiles;
		} else {
			if (oldFiles == null || oldFiles.equals("")) {
				// attachment = attachment;
			} else {
				attachment = attachment + "|" + oldFiles;
			}
		}

		// 保存附件
		documentInfor.setAttachment(attachment);

		this.documentInforManager.save(documentInfor);

		/**
		 * 该语句不放在编辑页面的原因: 若在编辑页面提交数据后立即执行window.close()操作, 则后台无法取到编辑页面的数据.
		 * (此情况仅在页面包含附件操作时存在)
		 */
		PrintWriter out = response.getWriter();
		out.print("<script language='javascript'>");
		out.print("var returnArray = [\"refresh\"," + documentInfor.getCategory().getCategoryId() + "];");
		out.print("window.returnValue = returnArray;");
		out.print("window.close();");
		out.print("</script>");

	}

	/**
	 * 编辑信息权限
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(params = "method=editInforRight")
	public String editInforRight(HttpServletRequest request, HttpServletResponse response) {

		String rowId = request.getParameter("rowId");
		String categoryId = request.getParameter("categoryId");
//		DocumentCategory category = null;
		DocumentInfor documentInfor = new DocumentInfor();
		if (rowId != null && rowId.length() > 0) {

			if (categoryId == null){
				documentInfor = (DocumentInfor) documentInforManager.get(Integer.valueOf(rowId));
			}else {
				String sql = "from DocumentInfor document where 1=1 and document.reportId = " + rowId;
				documentInfor = (DocumentInfor) this.documentInforManager.getResultByQueryString(sql).get(0);
//				documentInfor = (DocumentInfor) documentInforManager.getDocumentByReportId(Integer.valueOf(rowId)).get(0);

			}
//			DocumentInfor documentInfor = (DocumentInfor) this.documentInforManager.get(Integer.valueOf(rowId));
			
			/** 判断对该文档是否有编辑权限 */
			SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
			boolean canEdit = this.documentInforRightManager.hasRight(documentInfor, systemUser, DocumentInforRight._Right_Edit);
			boolean canEdit2 = false;
			boolean canEdit3 = false;
//			if (documentInfor.getCategory().getCategoryId() == 164){
				RoleInfor role2 = (RoleInfor)this.roleManager.get(47);
				canEdit2=roleManager.belongRole(systemUser,role2);
				RoleInfor role3 = (RoleInfor)this.roleManager.get(53);
				canEdit3=roleManager.belongRole(systemUser,role3);
//			}
			if (!canEdit ) {
				if (canEdit2 || canEdit3) {
				}else{
					request.setAttribute("_ErrorMessage", "对不起,您无权进行该操作,请与系统管理员联系!");
					return "/common/error";
				}
			}
			int fromReport = documentInfor.getCategory().getFromReport();
			request.setAttribute("_CategoryId", documentInfor.getCategory().getCategoryId());
			request.setAttribute("fromReport",fromReport);
			// 根据职级获取用户
			List persons = this.systemUserManager.getUserByPosition(PersonInfor._Position_Tag);
			request.setAttribute("_Persons", persons);

			// 获取职级大于一定值的用户
			List otherPersons = this.systemUserManager.getOtherPositionUser(PersonInfor._Position_Tag);
			request.setAttribute("_OtherPersons", otherPersons);

			// 部门信息
			List organizes = this.organizeManager.getDepartments();
			request.setAttribute("_Organizes", organizes);

			// 角色信息
			List roles = this.roleManager.getAll();
			request.setAttribute("_Roles", roles);

			// 用户信息
			List users = this.systemUserManager.getAllValid();
			request.setAttribute("_Users", users);

			// 权限信息
			int[] editUserIds = new int[users.size()];
			int[] deleteUserIds = new int[users.size()];
			int[] viewUserIds = new int[users.size()];
			int[] editRoleIds = new int[roles.size()];
			int[] deleteRoleIds = new int[roles.size()];
			int[] viewRoleIds = new int[roles.size()];
			int rightType = 0;

			int k = 0;
			Set rights = documentInfor.getRights();
//			System.out.println(documentInfor.getDocumentId());
//			for (Iterator it = rights.iterator(); it.hasNext();) {
//				DocumentInforRight right = (DocumentInforRight) it.next();
//				System.out.println(right.getRightId());
//			}
			for (Iterator it = rights.iterator(); it.hasNext();) {
				DocumentInforRight right = (DocumentInforRight) it.next();
				if (right instanceof DocumentInforUserRight) {
					rightType = 1;
					/** 用户权限 */
					DocumentInforUserRight userRight = (DocumentInforUserRight) right;
					int userId = userRight.getSystemUser().getPersonId().intValue();

					// 编辑权限
					if (this.documentInforRightManager.hasRight(right, DocumentInforRight._Right_Edit)) {
						editUserIds[k] = userId;
					}

					// 删除权限
					if (this.documentInforRightManager.hasRight(right, DocumentInforRight._Right_Delete)) {
						deleteUserIds[k] = userId;
					}

					// 浏览权限
					if (this.documentInforRightManager.hasRight(right, DocumentInforRight._Right_View)) {
						viewUserIds[k] = userId;
					}
//					if (this.documentInforRightManager.hasRight(right, 7)) {
//						viewUserIds[k] = userId;
//						deleteUserIds[k] = userId;
//						editUserIds[k] = userId;
//					}
//					if (this.documentInforRightManager.hasRight(right, 4)) {
//						viewUserIds[k] = userId;
//
//						editUserIds[k] = userId;
//					}
				} else if (right instanceof DocumentInforRoleRight) {
					/** 角色权限 */
					DocumentInforRoleRight roleRight = (DocumentInforRoleRight) right;
					int roleId = roleRight.getRole().getRoleId().intValue();

					// 编辑权限
					if (this.documentInforRightManager.hasRight(right, DocumentInforRight._Right_Edit)) {
						editRoleIds[k] = roleId;
					}

					// 删除权限
					if (this.documentInforRightManager.hasRight(right, DocumentInforRight._Right_Delete)) {
						deleteRoleIds[k] = roleId;
					}

					// 浏览权限
					if (this.documentInforRightManager.hasRight(right, DocumentInforRight._Right_View)) {
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
		return "editDocumentRight";
	}

	/**
	 * 保存信息权限
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "method=saveInforRight")
	public void saveInforRight(HttpServletRequest request, HttpServletResponse response) {

		String documentId = request.getParameter("documentId");
		String rightType = request.getParameter("rightType");
		DocumentInfor documentInfor = new DocumentInfor();
		String fromReport = request.getParameter("fromReport");
		if (documentId != null && documentId.length() > 0) {
//			documentInfor = (DocumentInfor) this.documentInforManager.get(Integer.valueOf(documentId));
			if (Integer.parseInt(fromReport) == 0){
				documentInfor = (DocumentInfor) documentInforManager.get(Integer.valueOf(documentId));
			}else {
				String sql = "from DocumentInfor document where 1=1 and document.reportId = " + documentId;
				documentInfor = (DocumentInfor) this.documentInforManager.getResultByQueryString(sql).get(0);
			}
			Set rights = documentInfor.getRights();
			documentInfor.getRights().removeAll(rights);

			if (rightType.equals("0")) {
				/** 为角色权限时 */
				String[] editRoleIds = request.getParameterValues("editRoleIds");
				String[] deleteRoleIds = request.getParameterValues("deleteRoleIds");
				String[] viewRoleIds = request.getParameterValues("viewRoleIds");

				// 新增,修改
				if (editRoleIds != null && editRoleIds.length > 0) {
					for (int i = 0; i < editRoleIds.length; i++) {
						RoleInfor role = (RoleInfor) this.roleManager.get(Integer.valueOf(editRoleIds[i]));
						DocumentInforRoleRight roleRight = new DocumentInforRoleRight();
						roleRight.setDocument(documentInfor);
						roleRight.setOperateRight(1);
						roleRight.setRole(role);
						documentInfor.getRights().add(roleRight);
					}
				}
				// 删除
				if (deleteRoleIds != null && deleteRoleIds.length > 0) {
					Set oldRights = documentInfor.getRights();
					for (int i = 0; i < deleteRoleIds.length; i++) {
						boolean has = false;
						for (Iterator it = oldRights.iterator(); it.hasNext();) {
							// 假如前面的权限操作中已包含该角色权限,则需要在此基础上进行修改
							DocumentInforRoleRight roleRight = (DocumentInforRoleRight) it.next();
							if (roleRight.getRole().getRoleId().intValue() == Integer.valueOf(deleteRoleIds[i])) {
								roleRight.setOperateRight(roleRight.getOperateRight() + 2);

								has = true;
								break;
							}
						}

						if (!has) {
							// 假如前面的权限操作中不包含该角色权限,则创建该权限
							RoleInfor role = (RoleInfor) this.roleManager.get(Integer.valueOf(deleteRoleIds[i]));
							DocumentInforRoleRight roleRight = new DocumentInforRoleRight();
							roleRight.setDocument(documentInfor);
							roleRight.setOperateRight(2);
							roleRight.setRole(role);
							documentInfor.getRights().add(roleRight);
						}
					}
				}
				// 浏览
				if (viewRoleIds != null && viewRoleIds.length > 0) {
					Set oldRights = documentInfor.getRights();
					for (int i = 0; i < viewRoleIds.length; i++) {
						boolean has = false;
						for (Iterator it = oldRights.iterator(); it.hasNext();) {
							// 假如前面的权限操作中已包含该角色权限,则需要在此基础上进行修改
							DocumentInforRoleRight roleRight = (DocumentInforRoleRight) it.next();
							if (roleRight.getRole().getRoleId().intValue() == Integer.valueOf(viewRoleIds[i])) {
								roleRight.setOperateRight(roleRight.getOperateRight() + 4);

								has = true;
								break;
							}
						}

						if (!has) {
							// 假如前面的权限操作中不包含该角色权限,则创建该权限
							RoleInfor role = (RoleInfor) this.roleManager.get(Integer.valueOf(viewRoleIds[i]));
							DocumentInforRoleRight roleRight = new DocumentInforRoleRight();
							roleRight.setDocument(documentInfor);
							roleRight.setOperateRight(4);
							roleRight.setRole(role);
							documentInfor.getRights().add(roleRight);
						}
					}
				}
			} else {
				/** 为用户权限时 */
				String[] editUserIds = request.getParameterValues("editUserIds");
				String[] deleteUserIds = request.getParameterValues("deleteUserIds");
				String[] viewUserIds = request.getParameterValues("viewUserIds");

				// 新增,修改
				if (editUserIds != null && editUserIds.length > 0) {
					for (int i = 0; i < editUserIds.length; i++) {
						SystemUserInfor user = (SystemUserInfor) this.systemUserManager.get(Integer.valueOf(editUserIds[i]));
						DocumentInforUserRight userRight = new DocumentInforUserRight();
						userRight.setDocument(documentInfor);
						userRight.setOperateRight(1);
						userRight.setSystemUser(user);
						documentInfor.getRights().add(userRight);
					}
				}
				// 删除
				if (deleteUserIds != null && deleteUserIds.length > 0) {
					Set oldRights = documentInfor.getRights();
					for (int i = 0; i < deleteUserIds.length; i++) {
						boolean has = false;
						for (Iterator it = oldRights.iterator(); it.hasNext();) {
							// 假如前面的权限操作中已包含该角色权限,则需要在此基础上进行修改
							DocumentInforUserRight userRight = (DocumentInforUserRight) it.next();
							if (userRight.getSystemUser().getPersonId().intValue() == Integer.valueOf(deleteUserIds[i])) {
								userRight.setOperateRight(userRight.getOperateRight() + 2);

								has = true;
								break;
							}
						}

						if (!has) {
							// 假如前面的权限操作中不包含该角色权限,则创建该权限
							SystemUserInfor user = (SystemUserInfor) this.systemUserManager.get(Integer.valueOf(deleteUserIds[i]));
							DocumentInforUserRight userRight = new DocumentInforUserRight();
							userRight.setDocument(documentInfor);
							userRight.setOperateRight(2);
							userRight.setSystemUser(user);
							documentInfor.getRights().add(userRight);
						}
					}
				}
				// 浏览
				if (viewUserIds != null && viewUserIds.length > 0) {
					Set oldRights = documentInfor.getRights();
					for (int i = 0; i < viewUserIds.length; i++) {
						boolean has = false;
						for (Iterator it = oldRights.iterator(); it.hasNext();) {
							// 假如前面的权限操作中已包含该角色权限,则需要在此基础上进行修改
							DocumentInforUserRight userRight = (DocumentInforUserRight) it.next();
							if (userRight.getSystemUser().getPersonId().intValue() == Integer.valueOf(viewUserIds[i])) {
								userRight.setOperateRight(userRight.getOperateRight() + 4);

								has = true;
								break;
							}
						}

						if (!has) {
							// 假如前面的权限操作中不包含该角色权限,则创建该权限
							SystemUserInfor user = (SystemUserInfor) this.systemUserManager.get(Integer.valueOf(viewUserIds[i]));
							DocumentInforUserRight userRight = new DocumentInforUserRight();
							userRight.setDocument(documentInfor);
							userRight.setOperateRight(4);
							userRight.setSystemUser(user);
							documentInfor.getRights().add(userRight);
						}
					}
				}

			}
			this.documentInforManager.save(documentInfor);
		}
	}

	/**
	 * 删除相关信息
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=delete")
	public void delete(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String rowIds = request.getParameter("rowIds");
		if (rowIds != null && rowIds.length() > 0) {
			String[] detleteIds = rowIds.split(",");
			if (detleteIds.length > 0) {
				for (int i = 0; i < detleteIds.length; i++) {

					Integer documentId = Integer.valueOf(detleteIds[i]);
					DocumentInfor documentInfor = (DocumentInfor) this.documentInforManager.get(documentId);

					// 删除附件
					String filePaths = documentInfor.getAttachment();
					deleteFiles(filePaths);

					this.documentInforManager.remove(documentInfor);
				}
			}
		}
	}

	// 获取自定义表单的查询字段
	@SuppressWarnings("static-access")
	@RequestMapping(params = "method=getSearchFields")
	@ResponseBody
	public Map<String, Object> getSearchFields(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		String categoryIdStr = request.getParameter("categoryId");
		String flowId = null;
		DocumentConstant docConstant = new DocumentConstant();
		SubmitConstant submitConstant = new SubmitConstant();
		Integer categoryId = null;
		if (categoryIdStr != null && !categoryIdStr.equals("")) {
			categoryId = Integer.parseInt(categoryIdStr);
		}
		if (categoryId != null && categoryId != 0) {
			if (categoryId == docConstant.SubmitFlow_Report_Receive) {
				flowId = String.valueOf(submitConstant.SubmitFlow_Report_Receive);
			} else if (categoryId == docConstant.SubmitFlow_Report_Publish) {
				flowId = String.valueOf(submitConstant.SubmitFlow_Report_Publish);
			} else if (categoryId == docConstant.SubmitFlow_Party_Receive) {
				flowId = String.valueOf(submitConstant.SubmitFlow_Party_Receive);
			} else if (categoryId == docConstant.SubmitFlow_Party_Publish) {
				flowId = String.valueOf(submitConstant.SubmitFlow_Party_Publish);
			} else if (categoryId == docConstant.SubmitFlow_Report_Inside) {
				flowId = String.valueOf(submitConstant.SubmitFlow_Report_Inside);
			} else if (categoryId == docConstant.SubmitFlow_Party_Inside) {
				flowId = String.valueOf(submitConstant.SubmitFlow_Party_Inside);
			} else if (categoryId == docConstant.SubmitFlow_Report_Board) {
				flowId = String.valueOf(submitConstant.SubmitFlow_Report_Board);
			} else if (categoryId == docConstant.SubmitFlow_HR_ResignApproval) {
				flowId = String.valueOf(submitConstant.SubmitFlow_HR_ResignApproval);
			} else if (categoryId == docConstant.SubmitFlow_HR_ResignProcedure) {
				flowId = String.valueOf(submitConstant.SubmitFlow_HR_ResignProcedure);
			} else if (categoryId == docConstant.SubmitFlow_HR_DynamicPersonnel) {
				flowId = String.valueOf(submitConstant.SubmitFlow_HR_DynamicPersonnel);
			} else if (categoryId == docConstant.SubmitFlow_HR_TrainingApproval) {
				flowId = String.valueOf(submitConstant.SubmitFlow_HR_TrainingApproval);
			} else if (categoryId == docConstant.SubmitFlow_Report_Contract) {
				flowId = String.valueOf(submitConstant.SubmitFlow_Report_Contract);
			}
		}
		String ftlName = null;
		if (flowId != null && flowId.length() > 0) {
			FlowDefinition flow = (FlowDefinition) this.flowManager.get(Integer.valueOf(flowId));
			if (flow.getTemplate() != null && flow.getTemplate().length() > 0) {
				String[] templateArray = flow.getTemplate().split("/");
				ftlName = templateArray[templateArray.length - 1];
			}
		}
		if (ftlName != null) {
			JSONArray searchFields = this.commonManager.getSearchFields(ftlName);
			map.put("_SearchFields", searchFields);
		}

		return map;
	}
}
