package com.kwchina.oa.purchase.yiban.controller;

import com.kwchina.core.base.entity.OrganizeInfor;
import com.kwchina.core.base.entity.PersonInfor;
import com.kwchina.core.base.entity.RoleInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.*;

import com.kwchina.core.cms.service.InforCategoryManager;
import com.kwchina.core.cms.service.InforDocumentManager;

import com.kwchina.core.common.page.PageList;
import com.kwchina.core.common.page.Pages;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.core.util.ArrayUtil;

import com.kwchina.core.util.json.JSONConvert;
import com.kwchina.core.util.multisearch.ConditionUtils;
import com.kwchina.core.util.string.StringUtil;
import com.kwchina.extend.loginLog.entity.AppModuleLog;
import com.kwchina.extend.loginLog.service.AppModuleLogManager;
import com.kwchina.oa.document.entity.*;
import com.kwchina.oa.document.service.CategoryRightManager;
import com.kwchina.oa.document.service.DocumentCategoryManager;
import com.kwchina.oa.document.service.DocumentInforManager;
import com.kwchina.oa.document.service.DocumentInforRightManager;

import com.kwchina.oa.personal.message.service.MessageInforManager;
import com.kwchina.oa.purchase.yiban.dao.PurchaseCheckDAO;
import com.kwchina.oa.purchase.yiban.dao.PurchaseLayerDAO;
import com.kwchina.oa.purchase.yiban.dao.PurchasePackageDao;
import com.kwchina.oa.purchase.yiban.entity.*;
import com.kwchina.oa.purchase.yiban.service.*;
import com.kwchina.oa.purchase.yiban.vo.PurchaseInforVo;
import com.kwchina.oa.submit.util.SubmitConstant;

import com.kwchina.oa.util.MakeReceive;
import com.kwchina.oa.util.SysCommonMethod;

import com.kwchina.oa.workflow.customfields.service.CommonManager;
import com.kwchina.oa.workflow.customfields.util.CnToSpell;

import com.kwchina.oa.workflow.entity.*;
import com.kwchina.oa.workflow.exception.InstanceSuspendLayerException;

import com.kwchina.oa.workflow.service.InstanceInforRightManager;
import com.kwchina.oa.workflow.vo.*;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;



@Controller
@RequestMapping("/ybpurchase/purchaseInfor.do")
public class YbPurchaseController extends PurchaseBaseController {
	
	@Resource
	private PurchaseManager purchaseManager;
	@Autowired
	private PersonInforManager personInforManager;
	@Autowired
	private PurchasePackageDao purchasePackageDao;
	@Autowired
	private PersonInforManager personManager;
	@Autowired
	private PurchaseCheckDAO purchaseCheckDAO;
	@Autowired
	private PurchaseLayerDAO purchaseLayerDAO;
	@Resource
	private PurchaseCheckInforManager purchaseCheckInforManager;
	@Resource
	private PurchaseFlowDefinitionManager purchaseflowManager;
	@Resource
	private PurchaseLayerInforManager purchaseLayerInforManager;
	@Resource
	private PurchaseCommonManager purchaseCommonManager;
	@Resource
	private SystemUserManager systemUserManager;
	@Resource
	private DocumentCategoryManager documentCategoryManager;
	@Resource
	private DocumentInforManager documentInforManager;
	@Resource
	private OrganizeManager organizeManager;
	@Resource
	private InforCategoryManager inforCategoryManager;
	@Resource
	private InforDocumentManager inforDocumentManager;
	@Resource
	private MessageInforManager messageInforManager;
	@Resource
	private CategoryRightManager documentCategoryRightManager;
	@Resource
	private DocumentInforRightManager documentInforRightManager;
	@Resource
	private ApproveSentenceManager approveSentenceManager;
	@Resource
	private RoleManager roleManager;
	@Autowired
	private InstanceInforRightManager instanceInforRightManager;
	@Resource
	private AppModuleLogManager appModuleLogManager;
	@Resource
	private PackageManager packageManager;




	@RequestMapping(params = "method=listchild")
	public String listchild(HttpServletRequest request, HttpServletResponse response)throws Exception {
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		RoleInfor caiwu = (RoleInfor) roleManager.get(PurchaseCheckInfor.Check_Role_Caiwu);
		RoleInfor lingdao = (RoleInfor) roleManager.get(PurchaseCheckInfor.Check_Role_Gongsilingdao);
		RoleInfor guikouf = (RoleInfor) roleManager.get(PurchaseCheckInfor.Check_Role_Guikou);
		RoleInfor caigouf = (RoleInfor) roleManager.get(PurchaseCheckInfor.Check_Role_Caigou);
		RoleInfor jiguilingdao = (RoleInfor) roleManager.get(PurchaseCheckInfor.Check_Role_Jiguibulingdao);
		RoleInfor cgjs = (RoleInfor) roleManager.get(PurchaseCheckInfor.Check_Role_Caigoujuese);
		boolean canUpdates = false;
		String flowId = request.getParameter("flowId");
//		int directorId = 0;
//		if (roleManager.belongRole(systemUser, lingdao)){
//
//		}else{
//			directorId = systemUser.getPerson().getDepartment().getDirector().getPersonId();
//		}
//
//		String flowId = request.getParameter("flowId");
//		OrganizeInfor org = (OrganizeInfor)this.organizeManager.get(PurchaseCheckInfor.Check_Org_Caigou);
//		int caigoulingdao = org.getDirector().getPersonId();
//		if (systemUser.getPersonId() == PurchaseCheckInfor.Check_Person_Guikoulingdao
//				||systemUser.getPersonId() == caigoulingdao
//				||roleManager.belongRole(systemUser, caiwu)
//				||roleManager.belongRole(systemUser, lingdao)
//				||roleManager.belongRole(systemUser, jiguilingdao)
//				||systemUser.getPersonId() == directorId){
//			if (systemUser.getPersonId() == directorId){
//				canUpdates = true;
//			}
//
//		}else{
//			if (roleManager.belongRole(systemUser, guikouf)||roleManager.belongRole(systemUser, caigouf)){
//				canUpdates = true;
//			}
//
//			boolean iscgjs = roleManager.belongRole(systemUser, cgjs);
//			request.setAttribute("cgjs",iscgjs);
//			request.setAttribute("flowId",flowId);
//			request.setAttribute("canUpdates",canUpdates);
//			return "yiban/purchaseInfor";
//		}
		if (roleManager.belongRole(systemUser, guikouf)||roleManager.belongRole(systemUser, caigouf)||roleManager.belongRole(systemUser, cgjs)){
			if (roleManager.belongRole(systemUser, guikouf)||roleManager.belongRole(systemUser, caigouf)){
				canUpdates = true;
			}
			boolean iscgjs = roleManager.belongRole(systemUser, cgjs);
			request.setAttribute("cgjs",iscgjs);
			request.setAttribute("flowId",flowId);
			request.setAttribute("canUpdates",canUpdates);
			return "yiban/purchaseInfor";
		}else{
			request.setAttribute("flowId",flowId);
			return "yiban/purchasePackageInfor";
		}

	}
	//显示审核实例
	@RequestMapping(params = "method=list")
	public void list(HttpServletRequest request, HttpServletResponse response)throws Exception {
		try {
			String flowId = request.getParameter("flowId");
			SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
			String flowName = "";
			String condition = "";

			// 构造查询语句
			String[] queryString = new String[2];
			queryString[0] = "from PurchaseInfor instance where 1=1";
			queryString[1] = "select count(purchaseId) from PurchaseInfor instance where 1=1";

			// 所属流程Id
			PurchaseFlowDefinition flow = new PurchaseFlowDefinition();
			String tableName = "";
			if (flowId != null && flowId.length() > 0) {
				condition += " and flowId.flowId = " + flowId;
				flow = (PurchaseFlowDefinition) this.purchaseflowManager.get(Integer.valueOf(flowId));
				flowName = "InstanceInfor";
				tableName = "Purchase_" + flowName;

				if (StringUtil.isNotEmpty(SysCommonMethod.getPlatform(request))) {
					/************记录app模块使用日志************/
					AppModuleLog appModuleLog = new AppModuleLog();

					int flowIdInt = Integer.valueOf(flowId);
					String moduleName = "一般采购";
					if (flowIdInt == 1) {
						moduleName = "一般采购";
					} else if (flowIdInt == 2) {
						moduleName = "业务采购";
					} else if (flowIdInt == 3) {
						moduleName = "工程采购";
					} else if (flowIdInt == 4) {
						moduleName = "零星工程维修";
					}

					appModuleLog.setModuleName(moduleName);
					appModuleLog.setPlatform(SysCommonMethod.getPlatform(request));
					appModuleLog.setLogTime(new Timestamp(System.currentTimeMillis()));
					appModuleLog.setUserName(systemUser.getUserName());
					this.appModuleLogManager.save(appModuleLog);
					/*****************************************/
				}
			}

			//根据流程名获取表列名
			List columnNames = this.purchaseCommonManager.getColumnNames(flowName);

			// 是否删除
			String deleted = request.getParameter("deleted");
			if (deleted != null && ("true").equals(deleted)) {
				condition += " and deleteFlag = 1";
			} else {
				condition += " and deleteFlag = 0";
			}

			/********************************/

			//可查看所有的角色
			RoleInfor allRole = (RoleInfor) roleManager.get(62);
			boolean isAll = roleManager.belongRole(systemUser, allRole);

//		if(isBoard){
//
//		}

			/********************************/
			RoleInfor guikou = (RoleInfor)this.roleManager.get(PurchaseCheckInfor.Check_Role_Guikou);
			RoleInfor caigou = (RoleInfor)this.roleManager.get(PurchaseCheckInfor.Check_Role_Caigou);
			if (isAll || systemUser.getUserType() == 1) {

			} else {
				//归口负责人筛选列表
				OrganizeInfor org = systemUser.getPerson().getDepartment();
				if (this.roleManager.belongRole(systemUser,guikou) || this.roleManager.belongRole(systemUser,caigou)){
					condition += " and managerCheckStatus != 2 and viceManagerCheckStatus != 2 ";
				}

				// 申请人
				condition += " and (applier.personId = " + systemUser.getPersonId();

				// 部门审核人(A.未中止;B.中止前已审.)
				condition += " or (manager.personId = " + systemUser.getPersonId() + " or viceManager.personId = " + systemUser.getPersonId() +")";
//						" or (manager.personId = " + systemUser.getPersonId() + " and managerChecked = 1))";

				// 审核人
				condition += " or (purchaseId in (select layer.purchase.purchaseId from PurchaseLayerInfor layer where layerId in " +
						"(select checkInfor.layerInfor.layerId from PurchaseCheckInfor checkInfor where checkInfor.status in (0,1,2,3) and checkInfor.checker.personId = " + systemUser.getPersonId() + " )))";

				//	权限表中设置的浏览权限
//				condition += " or (purchaseId in (select userRight.purchase.purchaseId from PurchaseInforUserRight userRight where userRight.systemUser.personId =" + systemUser.getPersonId() + "))";

				// 添加：部门中设定的部门经理，可查看本部门发起的所有合同流转
				condition += " or (guikouDepartment.organizeId in(select organizeId from OrganizeInfor where director.personId=" + systemUser.getPersonId() + "))";

				if (this.roleManager.belongRole(systemUser,guikou)){
					condition += " or (instance.purchaseStatus > 0 and instance.guikouDepartment.organizeId ="+org.getOrganizeId()+") ";
				}
				if (this.roleManager.belongRole(systemUser,caigou)){
					condition += " or instance.purchaseStatus > 3 ";
				}

				condition += ") ";
			}
			queryString[0] += condition;
			queryString[1] += condition;

			//获取自定义表单查询字段
			Map map = getSearchFields(request, response);
			JSONArray searchFields = (JSONArray) map.get("_SearchFields");
			//构造查询条件
			String[] params = getSearchParams(request);
			String cusCondition = "";    //自定义查询条件
			//手机端nd=null
			if (request.getParameter("nd") == null && request.getParameter("_search").equals("true")){
//				String appparam = request.getParameter("filters");
				String fieldValue = null;
				String dataValue = null;
				if (params[3] != null && params[3].length() > 0) {
					JSONObject filter = JSONObject.fromObject(params[3]);
					JSONArray rules = filter.getJSONArray("rules");        //取数据中的查询信息:查询字段,查询条件,查询数据
					if (rules != null && rules.size() > 0) {
						for (int i = 0; i < rules.size(); i++) {
							JSONObject tmpObj = (JSONObject) rules.get(i);
							fieldValue = tmpObj.getString("field");    //查询字段
							String opValue = tmpObj.getString("op");        //查询条件:大于,等于,小于..
							dataValue = tmpObj.getString("data");    //查询数据
						}
					}
				}
				cusCondition = "(oppositeUnit like '%"+ dataValue+"%' or indexNo like '%"+dataValue +"%' or contractNo like '%"+dataValue+"%')";
				String appsql = "select instanceId from " + tableName + " where 1=1 and " + cusCondition;
				List instancIds = this.purchaseManager.getResultBySQLQuery(appsql);
				if (instancIds != null && instancIds.size() > 0) {
					String instancIdsStr = "";
					int index = 0;
					for (Iterator it = instancIds.iterator(); it.hasNext(); ) {
						instancIdsStr += ((Integer) it.next()).toString();
						if (index < instancIds.size() - 1) {
							instancIdsStr += ",";
						}
						index++;
					}
					queryString[0] += " and (instanceId in(" + instancIdsStr + ") or instanceTitle like '%"+ dataValue +"%')";
					queryString[1] += " and instanceId in(" + instancIdsStr + ") or instanceTitle like '%"+ dataValue +"%')";
				} else {
					// 没有满足查询条件的数据
					queryString[0] += " and instanceId in(0)";
					queryString[1] += " and instanceId in(0)";
				}

			}else {
				if (params[2].equals("true")) {
					if (params[3] != null && params[3].length() > 0) {

						JSONObject filter = JSONObject.fromObject(params[3]);
						JSONArray rules = filter.getJSONArray("rules");        //取数据中的查询信息:查询字段,查询条件,查询数据
						if (rules != null && rules.size() > 0) {
							for (int i = 0; i < rules.size(); i++) {
								JSONObject tmpObj = (JSONObject) rules.get(i);
								String fieldValue = tmpObj.getString("field");    //查询字段
								String opValue = tmpObj.getString("op");        //查询条件:大于,等于,小于..
								String dataValue = tmpObj.getString("data");    //查询数据

								//处理查询条件
								boolean has = false;
								if (searchFields != null && searchFields.size() > 0) {
									for (int j = 0; j < searchFields.size(); j++) {
										JSONArray fieldArray = (JSONArray) searchFields.get(j);
										JSONObject tmpField = (JSONObject) fieldArray.get(1);
										String searchField = tmpField.getString("searchField");
										if (searchField.equals(fieldValue)) {
											has = true;
											//组织自定义表单查询条件
											String tmpCondition = ConditionUtils.getCondition(fieldValue, opValue, dataValue);
											cusCondition += " and " + tmpCondition;
											break;
										}
									}
								}
								if (!has) {
									//过滤自定义表单查询字段
									//过滤自定义表单查询字段
									String tmpCondition = ConditionUtils.getCondition(fieldValue, opValue, dataValue);
									queryString[0] += " and " + tmpCondition;
									queryString[1] += " and " + tmpCondition;
								}
							}
						}
					}
				}

				//加上自定义表单查询条件
				if (cusCondition.length() > 0 && tableName.length() > 0) {
					String sql = "select instanceId from " + tableName + " where 1=1" + cusCondition;
					List instancIds = this.purchaseManager.getResultBySQLQuery(sql);
					if (instancIds != null && instancIds.size() > 0) {
						String instancIdsStr = "";
						int index = 0;
						for (Iterator it = instancIds.iterator(); it.hasNext(); ) {
							instancIdsStr += ((Integer) it.next()).toString();
							if (index < instancIds.size() - 1) {
								instancIdsStr += ",";
							}
							index++;
						}
						queryString[0] += " and instanceId in(" + instancIdsStr + ")";
						queryString[1] += " and instanceId in(" + instancIdsStr + ")";
					} else {
						// 没有满足查询条件的数据
						queryString[0] += " and instanceId in(0)";
						queryString[1] += " and instanceId in(0)";
					}
				}
			}

//			queryString[0] += " order by " + params[0] + " " + params[1];

			//queryString = this.purchaseManager.generateQueryString(queryString, getSearchParams(request));
			queryString[0] += " order by purchaseStatus,startTime desc" ;
			String page = request.getParameter("page"); // 当前页
			String rowsNum = request.getParameter("rows"); // 每页显示的行数

			Pages pages = new Pages(request);
			pages.setPage(Integer.valueOf(page));
			pages.setPerPageNum(Integer.valueOf(rowsNum));

			List listCount = this.purchaseManager.getResultByQueryString(queryString[1]);
			String ss = listCount.get(0).toString();

			Pages pagesExcel = new Pages(request);
			pagesExcel.setPage(Integer.valueOf(page));
			pagesExcel.setPerPageNum(Integer.valueOf(ss) + 1);
			List list = new ArrayList();
			JSONObject jsonObj = new JSONObject();

			// 定义rows，存放数据
			JSONArray rows = new JSONArray();
			PageList plExcel = new PageList();
//			if (params[2].equals("true")  && flowId.equals("1000")) {
//				plExcel = this.purchaseManager.getResultByQueryString(queryString[0], queryString[1], true, pagesExcel);
//
//				list = plExcel.getObjectList();
//				// plExcel.setPages(pages);
//				// plExcel.setPageShowString(pageShowString);
//				// 定义返回的数据类型：json，使用了json-lib
//				//pagesExcel.setPage(Integer.valueOf(page));
//				//pagesExcel.setPerPageNum(Integer.valueOf(rowsNum));
//
//				jsonObj.put("page", plExcel.getPages().getCurrPage()); // 当前页(名称必须为page)
//				jsonObj.put("total", plExcel.getPages().getTotalPage()); // 总页数(名称必须为total)
//				jsonObj.put("records", plExcel.getPages().getTotals()); // 总记录数(名称必须为records)
//				//List list = pl.getObjectList();
//				//plExcel.setPages(pagesExcel);
//
//			} else {
			PageList pl = this.purchaseManager.getResultByQueryString(queryString[0], queryString[1], true, pages);
			list = pl.getObjectList();

			// 定义返回的数据类型：json，使用了json-lib

			jsonObj.put("page", pl.getPages().getCurrPage()); // 当前页(名称必须为page)
			jsonObj.put("total", pl.getPages().getTotalPage()); // 总页数(名称必须为total)
			jsonObj.put("records", pl.getPages().getTotals()); // 总记录数(名称必须为records)
//			}


			//取需要的字段信息返回
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Iterator it = list.iterator();
			while (it.hasNext()) {
				PurchaseInfor fi = (PurchaseInfor) it.next();

				JSONObject obj = new JSONObject();
				obj.put("purchaseId", fi.getPurchaseId());
				obj.put("purchaseTitle", fi.getPurchaseTitle());
				obj.put("purchaseMoney",fi.getPurchaseMoney());
				String startTime = "";
				if (fi.getStartTime() != null) {
					startTime = sdf.format(fi.getStartTime());
				}
				obj.put("guige", fi.getGuige());
				obj.put("ysType", fi.getYsType());
				obj.put("purchaseStr1", fi.getPurchaseStr1());
				obj.put("batchNumber", fi.getBatchNumber());
				obj.put("purchaseNumber", fi.getPurchaseNumber());
				obj.put("purchaseGoods", fi.getPurchaseGoods());
				obj.put("startTime", startTime);
				obj.put("purchaseStr2", fi.getPurchaseStr2());
//					String checker = " ";
//					if(fi.getManager() != null) {
//						checker = fi.getManager().getPerson().getPersonName();
//					}

//					obj.put("checker", checker);
//					obj.put("updateTime", sdf.format(fi.getUpdateTime()));
				obj.put("applier", fi.getApplier().getPerson().getPersonName());
//					obj.put("applierPhoto", StringUtil.isNotEmpty(fi.getApplier().getPerson().getPhotoAttachment()) ? fi.getApplier().getPerson().getPhotoAttachment() : "");
				obj.put("department", fi.getDepartment().getOrganizeName());
				obj.put("flow", fi.getFlowId().getFlowName());
				//obj.put("charger", fi.getFlowDefinition().getCharger().getPerson().getPersonName());
//					if (fi.getCharger() != null) {
//						obj.put("charger", fi.getCharger().getPerson().getPersonName());
//					} else {

//						obj.put("charger", null);
//					}
				String status = "采购结束";
				if (fi.getPurchaseStatus() == 0) {
					status = "部门领导审核中";
				} else if (fi.getPurchaseStatus() == 1) {
					status = "归口待处理";
				} else if (fi.getPurchaseStatus() == 2) {
					status = "归口已处理待提交";
				} else if (fi.getPurchaseStatus() == 3) {
					status = "归口领导审核中";
				} else if (fi.getPurchaseStatus() == 4) {
					status = "采购待处理";
				} else if (fi.getPurchaseStatus() == 5) {
					status = "采购已处理待提交";
				} else if (fi.getPurchaseStatus() == 6) {
					status = "采购部领导审批中";
				} else if (fi.getPurchaseStatus() == 7) {
					status = "财务预算审核中";
				} else if (fi.getPurchaseStatus() == 8) {
					status = "公司领导审核中";
				} else if (fi.getPurchaseStatus() == 9) {
					status = "子流程中";
				} else if (fi.getPurchaseStatus() == 10) {
					status = "采购部确认中";
				} else if (fi.getPurchaseStatus() == 11) {
					status = "采购结束";
				} else if (fi.getPurchaseStatus() == PurchaseInfor.Purchase_Status_No) {
					status = "采购计划未通过，结束";
				}
				obj.put("status", status);
				obj.put("attach", fi.getAttach());

				//校验能否选中
				String canCheck = "false";
				PurchaseCheckInfor tmpCheck = this.purchaseManager.isChecker(fi, systemUser);
				if (tmpCheck != null) {
					// 能否审核
					if (tmpCheck.getEndDate() == null && fi.getPurchaseStatus() != 1 && fi.getPurchaseStatus() != 4) {
						canCheck = "true";
					}else if (systemUser.getPersonId() == 1){
						canCheck = "true";
					}
				}
				if ( fi.getPurchaseStatus() == PurchaseInfor.Purchase_Status_No
						||  fi.getPurchaseStatus() == 11 ||  fi.getPurchaseStatus() == 10){
					canCheck = "false";
				}
				obj.put("canCheck", canCheck);

				if (roleManager.belongRole(systemUser, guikou)){
					obj.put("jiguibu", fi.getJigui());
				}else{
					obj.put("jiguibu", "");
				}


				/** 自定义表单数据 */
				if (columnNames != null && columnNames.size() > 0) {
					List customizeDatas = this.purchaseCommonManager.getFormData(flowName, fi.getPurchaseId());
					if (customizeDatas != null && customizeDatas.size() > 0) {
						Object[] dataArray = (Object[]) customizeDatas.get(0);

						int i = 0;
						for (Iterator col = columnNames.iterator(); col.hasNext(); ) {
							String colName = (String) col.next();
							obj.put(colName, dataArray[i]);
							i++;
						}
					}
				}
				/*****/
				rows.add(obj);
			}



			jsonObj.put("rows", rows); // 返回到前台每页显示的数据(名称必须为rows)
			// 设置字符编码
			response.setContentType(CoreConstant.CONTENT_TYPE);
			response.getWriter().print(jsonObj);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	//显示包实例
	@RequestMapping(params = "method=listPackage")
	public void listPackage(HttpServletRequest request, HttpServletResponse response)throws Exception {
		try {
			String flowId = request.getParameter("flowId");
			SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
			RoleInfor caiwu = (RoleInfor) roleManager.get(PurchaseCheckInfor.Check_Role_Caiwu);
			RoleInfor lingdao = (RoleInfor) roleManager.get(PurchaseCheckInfor.Check_Role_Gongsilingdao);
			RoleInfor jiguilingdao = (RoleInfor) roleManager.get(PurchaseCheckInfor.Check_Role_Jiguibulingdao);
			// 构造查询语句
			String[] queryString = new String[2];
//			System.out.println(roleManager.belongRole(systemUser, caiwu));
			if (roleManager.belongRole(systemUser, caiwu)){
				//财务预算审核
				queryString[0] = "from PurchasePackage where flowId=1 and (roleId.roleId="+ caiwu.getRoleId()+" or manager.personId="+ systemUser.getPersonId()+")";
			}
			else if(roleManager.belongRole(systemUser, lingdao)){
				//公司领导审核
				queryString[0] = "from PurchasePackage where flowId=1 and (roleId.roleId="+ lingdao.getRoleId()+" or manager.personId="+ systemUser.getPersonId()+")";
			}else if(roleManager.belongRole(systemUser, jiguilingdao)){
				//技术规划部领导
				queryString[0] = "from PurchasePackage where flowId=1 and (roleId.roleId="+ jiguilingdao.getRoleId()+" or manager.personId="+ systemUser.getPersonId()+")";
			}else{
				queryString[0] = "from PurchasePackage where flowId=1 and (manager.personId="+ systemUser.getPersonId()+" or vicemanager.personId="+ systemUser.getPersonId()+")";
			}
			queryString[1] = "select count(packageId) from PurchasePackage where flowId=1 ";

			//获取自定义表单查询字段
			Map map = getSearchFields(request, response);
			JSONArray searchFields = (JSONArray) map.get("_SearchFields");
			//构造查询条件
			String[] params = getSearchParams(request);
			String cusCondition = "";    //自定义查询条件

			if (params[2].equals("true")) {
				if (params[3] != null && params[3].length() > 0) {

					JSONObject filter = JSONObject.fromObject(params[3]);
					JSONArray rules = filter.getJSONArray("rules");        //取数据中的查询信息:查询字段,查询条件,查询数据
					if (rules != null && rules.size() > 0) {
						for (int i = 0; i < rules.size(); i++) {
							JSONObject tmpObj = (JSONObject) rules.get(i);
							String fieldValue = tmpObj.getString("field");    //查询字段
							String opValue = tmpObj.getString("op");        //查询条件:大于,等于,小于..
							String dataValue = tmpObj.getString("data");    //查询数据

							//处理查询条件
							boolean has = false;
							if (searchFields != null && searchFields.size() > 0) {
								for (int j = 0; j < searchFields.size(); j++) {
									JSONArray fieldArray = (JSONArray) searchFields.get(j);
									JSONObject tmpField = (JSONObject) fieldArray.get(1);
									String searchField = tmpField.getString("searchField");
									if (searchField.equals(fieldValue)) {
										has = true;
										//组织自定义表单查询条件
										String tmpCondition = ConditionUtils.getCondition(fieldValue, opValue, dataValue);
										cusCondition += " and " + tmpCondition;
										break;
									}
								}
							}
							if (!has) {
								//过滤自定义表单查询字段
								//过滤自定义表单查询字段
								String tmpCondition = ConditionUtils.getCondition(fieldValue, opValue, dataValue);
								queryString[0] += " and " + tmpCondition;
								queryString[1] += " and " + tmpCondition;
							}
						}
					}
				}
			}




			queryString[0] += " order by packageId desc";
			String page = request.getParameter("page"); // 当前页
			String rowsNum = request.getParameter("rows"); // 每页显示的行数

			Pages pages = new Pages(request);
			pages.setPage(Integer.valueOf(page));
			pages.setPerPageNum(Integer.valueOf(rowsNum));

			List listCount = this.purchaseManager.getResultByQueryString(queryString[1]);
			String ss = listCount.get(0).toString();

			Pages pagesExcel = new Pages(request);
			pagesExcel.setPage(Integer.valueOf(page));
			pagesExcel.setPerPageNum(Integer.valueOf(ss) + 1);
			List list = new ArrayList();
			JSONObject jsonObj = new JSONObject();

			// 定义rows，存放数据
			JSONArray rows = new JSONArray();
			PageList plExcel = new PageList();
//			if (params[2].equals("true")  && flowId.equals("1000")) {
//				plExcel = this.purchaseManager.getResultByQueryString(queryString[0], queryString[1], true, pagesExcel);
//
//				list = plExcel.getObjectList();
//				// plExcel.setPages(pages);
//				// plExcel.setPageShowString(pageShowString);
//				// 定义返回的数据类型：json，使用了json-lib
//				//pagesExcel.setPage(Integer.valueOf(page));
//				//pagesExcel.setPerPageNum(Integer.valueOf(rowsNum));
//
//				jsonObj.put("page", plExcel.getPages().getCurrPage()); // 当前页(名称必须为page)
//				jsonObj.put("total", plExcel.getPages().getTotalPage()); // 总页数(名称必须为total)
//				jsonObj.put("records", plExcel.getPages().getTotals()); // 总记录数(名称必须为records)
//				//List list = pl.getObjectList();
//				//plExcel.setPages(pagesExcel);
//
//			} else {
			PageList pl = this.purchaseManager.getResultByQueryString(queryString[0], queryString[1], true, pages);
			list = pl.getObjectList();

			// 定义返回的数据类型：json，使用了json-lib

			jsonObj.put("page", pl.getPages().getCurrPage()); // 当前页(名称必须为page)
			jsonObj.put("total", pl.getPages().getTotalPage()); // 总页数(名称必须为total)
			jsonObj.put("records", pl.getPages().getTotals()); // 总记录数(名称必须为records)
//			}


			//取需要的字段信息返回
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Iterator it = list.iterator();
			while (it.hasNext()) {
				PurchasePackage fi = (PurchasePackage) it.next();

				JSONObject obj = new JSONObject();
				obj.put("purchaseId", "0");
				obj.put("packageId", fi.getPackageId());
				obj.put("packageName", fi.getPackageName());
				String startDate = "";
				if (fi.getStartDate() != null) {
					startDate = sdf.format(fi.getStartDate());
				}
				obj.put("startDate", startDate);
//					String checker = " ";
//					if(fi.getManager() != null) {
//						checker = fi.getManager().getPerson().getPersonName();
//					}

//					obj.put("checker", checker);
//					obj.put("updateTime", sdf.format(fi.getUpdateTime()));
//				obj.put("applier", fi.getApplier().getPerson().getPersonName());
//					obj.put("applierPhoto", StringUtil.isNotEmpty(fi.getApplier().getPerson().getPhotoAttachment()) ? fi.getApplier().getPerson().getPhotoAttachment() : "");
//				obj.put("department", fi.getDepartment().getOrganizeName());
//				obj.put("flow", fi.getFlowId().getFlowName());
				//obj.put("charger", fi.getFlowDefinition().getCharger().getPerson().getPersonName());
//					if (fi.getCharger() != null) {
//						obj.put("charger", fi.getCharger().getPerson().getPersonName());
//					} else {

//						obj.put("charger", null);
//					}
				String status = "未审核";
				if (fi.getStatus() == 1) {
					status = "存在未审批";
				}else if (fi.getStatus() == 2) {
					//获取当前处理层,并判断处理情况:进行中,已完毕
//					if (fi.getPurchaseStatus() == 0)
					status = "已全部审批";
				}else if (fi.getStatus() == 3){
					status = "作废";
				}
				obj.put("status", status);
//				obj.put("attach", fi.getAttach());

				/** 自定义表单数据 */
//				if (columnNames != null && columnNames.size() > 0) {
//					List customizeDatas = this.purchaseCommonManager.getFormData(flowName, fi.getPurchaseId());
//					if (customizeDatas != null && customizeDatas.size() > 0) {
//						Object[] dataArray = (Object[]) customizeDatas.get(0);
//
//						int i = 0;
//						for (Iterator col = columnNames.iterator(); col.hasNext(); ) {
//							String colName = (String) col.next();
//							obj.put(colName, dataArray[i]);
//							i++;
//						}
//					}
//				}
//				/*****/
				//校验能否选中

				rows.add(obj);
			}

			jsonObj.put("rows", rows); // 返回到前台每页显示的数据(名称必须为rows)
			// 设置字符编码
			response.setContentType(CoreConstant.CONTENT_TYPE);
			response.getWriter().print(jsonObj);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@RequestMapping(params = "method=listPackageInclude")
	public void listPackageInclude(HttpServletRequest request, HttpServletResponse response)throws Exception {
		try {
			String flowId = request.getParameter("flowId");
			SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
			String packageId = request.getParameter("packageId");
//			request.setAttribute("packageId",packageId);

			// 构造查询语句
			String[] queryString = new String[2];
//			queryString[0] = "from PurchasePackage a,PurchaseInfor b, where a.packageId="+ packageId +" and a.packageId = b.packages.packageId";
			queryString[0] = "select b from PurchaseInfor b inner join b.packages a where a.packageId="+packageId;
			queryString[1] = "select count(b) from PurchaseInfor b inner join b.packages a where a.packageId="+packageId;

			//获取自定义表单查询字段
			Map map = getSearchFields(request, response);
			JSONArray searchFields = (JSONArray) map.get("_SearchFields");
			//构造查询条件
			String[] params = getSearchParams(request);
			String cusCondition = "";    //自定义查询条件

			if (params[2].equals("true")) {
				if (params[3] != null && params[3].length() > 0) {

					JSONObject filter = JSONObject.fromObject(params[3]);
					JSONArray rules = filter.getJSONArray("rules");        //取数据中的查询信息:查询字段,查询条件,查询数据
					if (rules != null && rules.size() > 0) {
						for (int i = 0; i < rules.size(); i++) {
							JSONObject tmpObj = (JSONObject) rules.get(i);
							String fieldValue = tmpObj.getString("field");    //查询字段
							String opValue = tmpObj.getString("op");        //查询条件:大于,等于,小于..
							String dataValue = tmpObj.getString("data");    //查询数据

							//处理查询条件
							boolean has = false;
							if (searchFields != null && searchFields.size() > 0) {
								for (int j = 0; j < searchFields.size(); j++) {
									JSONArray fieldArray = (JSONArray) searchFields.get(j);
									JSONObject tmpField = (JSONObject) fieldArray.get(1);
									String searchField = tmpField.getString("searchField");
									if (searchField.equals(fieldValue)) {
										has = true;
										//组织自定义表单查询条件
										String tmpCondition = ConditionUtils.getCondition(fieldValue, opValue, dataValue);
										cusCondition += " and " + tmpCondition;
										break;
									}
								}
							}
							if (!has) {
								//过滤自定义表单查询字段
								//过滤自定义表单查询字段
								String tmpCondition = ConditionUtils.getCondition(fieldValue, opValue, dataValue);
								queryString[0] += " and " + tmpCondition;
								queryString[1] += " and " + tmpCondition;
							}
						}
					}
				}
			}

			String page = request.getParameter("page"); // 当前页
			String rowsNum = request.getParameter("rows"); // 每页显示的行数

			Pages pages = new Pages(request);
			pages.setPage(Integer.valueOf(page));
			pages.setPerPageNum(Integer.valueOf(rowsNum));

			List listCount = this.purchaseManager.getResultByQueryString(queryString[1]);
			String ss = listCount.get(0).toString();

			Pages pagesExcel = new Pages(request);
			pagesExcel.setPage(Integer.valueOf(page));
			pagesExcel.setPerPageNum(Integer.valueOf(ss) + 1);
			List list = new ArrayList();
			JSONObject jsonObj = new JSONObject();

			// 定义rows，存放数据
			JSONArray rows = new JSONArray();
			PageList plExcel = new PageList();
//			if (params[2].equals("true")  && flowId.equals("1000")) {
//				plExcel = this.purchaseManager.getResultByQueryString(queryString[0], queryString[1], true, pagesExcel);
//
//				list = plExcel.getObjectList();
//				// plExcel.setPages(pages);
//				// plExcel.setPageShowString(pageShowString);
//				// 定义返回的数据类型：json，使用了json-lib
//				//pagesExcel.setPage(Integer.valueOf(page));
//				//pagesExcel.setPerPageNum(Integer.valueOf(rowsNum));
//
//				jsonObj.put("page", plExcel.getPages().getCurrPage()); // 当前页(名称必须为page)
//				jsonObj.put("total", plExcel.getPages().getTotalPage()); // 总页数(名称必须为total)
//				jsonObj.put("records", plExcel.getPages().getTotals()); // 总记录数(名称必须为records)
//				//List list = pl.getObjectList();
//				//plExcel.setPages(pagesExcel);
//
//			} else {
			PageList pl = this.purchaseManager.getResultByQueryString(queryString[0], queryString[1], true, pages);
			list = pl.getObjectList();

			// 定义返回的数据类型：json，使用了json-lib

			jsonObj.put("page", pl.getPages().getCurrPage()); // 当前页(名称必须为page)
			jsonObj.put("total", pl.getPages().getTotalPage()); // 总页数(名称必须为total)
			jsonObj.put("records", pl.getPages().getTotals()); // 总记录数(名称必须为records)
//			}


			//取需要的字段信息返回
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Iterator it = list.iterator();
			while (it.hasNext()) {
				PurchaseInfor fi = (PurchaseInfor) it.next();

				JSONObject obj = new JSONObject();
				obj.put("purchaseId", fi.getPurchaseId());
				obj.put("purchaseTitle", fi.getPurchaseTitle());
				obj.put("purchaseMoney",fi.getPurchaseMoney());
				String startTime = "";
				if (fi.getStartTime() != null) {
					startTime = sdf.format(fi.getStartTime());
				}
				obj.put("guige", fi.getGuige());
				obj.put("ysType", fi.getYsType());
				obj.put("purchaseStr1", fi.getPurchaseStr1());
				obj.put("batchNumber", fi.getBatchNumber());
				obj.put("purchaseNumber", fi.getPurchaseNumber());
				obj.put("purchaseGoods", fi.getPurchaseGoods());
				obj.put("purchaseStr2", fi.getPurchaseStr2());
				obj.put("startTime", startTime);
//					String checker = " ";
//					if(fi.getManager() != null) {
//						checker = fi.getManager().getPerson().getPersonName();
//					}

//					obj.put("checker", checker);
//					obj.put("updateTime", sdf.format(fi.getUpdateTime()));
				obj.put("applier", fi.getApplier().getPerson().getPersonName());
//					obj.put("applierPhoto", StringUtil.isNotEmpty(fi.getApplier().getPerson().getPhotoAttachment()) ? fi.getApplier().getPerson().getPhotoAttachment() : "");
				obj.put("department", fi.getDepartment().getOrganizeName());
				obj.put("flow", fi.getFlowId().getFlowName());
				//obj.put("charger", fi.getFlowDefinition().getCharger().getPerson().getPersonName());
//					if (fi.getCharger() != null) {
//						obj.put("charger", fi.getCharger().getPerson().getPersonName());
//					} else {

//						obj.put("charger", null);
//					}
				String status = "采购结束";
				if (fi.getPurchaseStatus() == 0) {
					status = "部门领导审核中";
				} else if (fi.getPurchaseStatus() == 1) {
					status = "归口待处理";
				} else if (fi.getPurchaseStatus() == 2) {
					status = "归口已处理待提交";
				} else if (fi.getPurchaseStatus() == 3) {
					status = "归口领导审核中";
				} else if (fi.getPurchaseStatus() == 4) {
					status = "采购待处理";
				} else if (fi.getPurchaseStatus() == 5) {
					status = "采购已处理待提交";
				} else if (fi.getPurchaseStatus() == 6) {
					status = "采购部领导审批中";
				} else if (fi.getPurchaseStatus() == 7) {
					status = "财务预算审核中";
				} else if (fi.getPurchaseStatus() == 8) {
					status = "公司领导审核中";
				} else if (fi.getPurchaseStatus() == 9) {
					status = "子流程中";
				} else if (fi.getPurchaseStatus() == 10) {
					status = "采购部确认中";
				} else if (fi.getPurchaseStatus() == 11) {
					status = "采购结束";
				} else if (fi.getPurchaseStatus() == PurchaseInfor.Purchase_Status_No) {
					status = "采购计划未通过，结束";
				}
				//校验能否选中
				String canCheck = "false";
				PurchaseCheckInfor tmpCheck = this.purchaseManager.isChecker(fi, systemUser);
				if (tmpCheck != null) {
					// 能否审核
					if (tmpCheck.getEndDate() == null && fi.getPurchaseStatus() != 1 && fi.getPurchaseStatus() != 4) {
						canCheck = "true";
					}else if (systemUser.getPersonId() == 1){
						canCheck = "true";
					}
				}
				int managerId = fi.getManager().getPersonId();
				int vicemanagerId = 0;
				if (fi.getViceManager() != null){
					vicemanagerId= fi.getViceManager().getPersonId();
				}
				if (fi.getPurchaseStatus() == 0 ){
					if (systemUser.getPersonId() == managerId && !fi.isManagerChecked()){
						canCheck = "true";
					}
					if (systemUser.getPersonId() == vicemanagerId && !fi.isViceManagerChecked()){
						canCheck = "true";
					}
				}
				obj.put("canCheck", canCheck);

				obj.put("status", status);
				obj.put("attach", fi.getAttach());
				/*****/
				rows.add(obj);
			}
			jsonObj.put("rows", rows); // 返回到前台每页显示的数据(名称必须为rows)
			// 设置字符编码
			response.setContentType(CoreConstant.CONTENT_TYPE);
			response.getWriter().print(jsonObj);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}


	private void queryTableType(String tableName, String cusCondition,String[] queryString) {
		String tableName1 = "Customize_Xinzengwenjian";
		String tableName2 = "Customize_Xiugaiwenjian";
		String tableName3 = "Customize_Xiaohuiwenjian";
		String tableName4 = "Customize_Xinzengliucheng";
		String tableName5 = "Customize_Xiugailiucheng";
		String tableName6 = "Customize_Xiaohuiliucheng";
		String tableName7 = "Customize_Xinzengwenjianliucheng";
		String tableName8 = "Customize_Xiugaiwenjianliucheng";
		String tableName9 = "Customize_Xiaohuiwenjianliucheng";
		if(tableName!=null){
			switch (tableName){
				case "新增文件":
					String sql = "select instanceId from " + tableName1 + " where 1=1" + cusCondition;
					List instancIds = this.purchaseManager.getResultBySQLQuery(sql);
					if (instancIds != null && instancIds.size() > 0) {
						String instancIdsStr = "";
						int index = 0;
						for (Iterator it = instancIds.iterator(); it.hasNext(); ) {
							instancIdsStr += ((Integer) it.next()).toString();
							if (index < instancIds.size() - 1) {
								instancIdsStr += ",";
							}
							index++;
						}
						queryString[0] += " and instanceId in(" + instancIdsStr + ")";
						queryString[1] += " and instanceId in(" + instancIdsStr + ")";
					} else {
						queryString[0] += " and instanceId in(0)";
						queryString[1] += " and instanceId in(0)";
					}
					break;
				case "修改文件":
					 sql = "select instanceId from " + tableName2 + " where 1=1" + cusCondition;
					instancIds = this.purchaseManager.getResultBySQLQuery(sql);
					if (instancIds != null && instancIds.size() > 0) {
						String instancIdsStr = "";
						int index = 0;
						for (Iterator it = instancIds.iterator(); it.hasNext(); ) {
							instancIdsStr += ((Integer) it.next()).toString();
							if (index < instancIds.size() - 1) {
								instancIdsStr += ",";
							}
							index++;
						}
						queryString[0] += " and instanceId in(" + instancIdsStr + ")";
						queryString[1] += " and instanceId in(" + instancIdsStr + ")";
					} else {
						queryString[0] += " and instanceId in(0)";
						queryString[1] += " and instanceId in(0)";
					}
					break;
				case "销毁文件":
					 sql = "select instanceId from " + tableName3 + " where 1=1" + cusCondition;
					 instancIds = this.purchaseManager.getResultBySQLQuery(sql);
					if (instancIds != null && instancIds.size() > 0) {
						String instancIdsStr = "";
						int index = 0;
						for (Iterator it = instancIds.iterator(); it.hasNext(); ) {
							instancIdsStr += ((Integer) it.next()).toString();
							if (index < instancIds.size() - 1) {
								instancIdsStr += ",";
							}
							index++;
						}
						queryString[0] += " and instanceId in(" + instancIdsStr + ")";
						queryString[1] += " and instanceId in(" + instancIdsStr + ")";
					} else {
						queryString[0] += " and instanceId in(0)";
						queryString[1] += " and instanceId in(0)";
					}
					break;
				case "新增流程":
					 sql = "select instanceId from " + tableName4 + " where 1=1" + cusCondition;
					 instancIds = this.purchaseManager.getResultBySQLQuery(sql);
					if (instancIds != null && instancIds.size() > 0) {
						String instancIdsStr = "";
						int index = 0;
						for (Iterator it = instancIds.iterator(); it.hasNext(); ) {
							instancIdsStr += ((Integer) it.next()).toString();
							if (index < instancIds.size() - 1) {
								instancIdsStr += ",";
							}
							index++;
						}
						queryString[0] += " and instanceId in(" + instancIdsStr + ")";
						queryString[1] += " and instanceId in(" + instancIdsStr + ")";
					} else {
						queryString[0] += " and instanceId in(0)";
						queryString[1] += " and instanceId in(0)";
					}
					break;
				case "修改流程":
					 sql = "select instanceId from " + tableName5 + " where 1=1" + cusCondition;
					 instancIds = this.purchaseManager.getResultBySQLQuery(sql);
					if (instancIds != null && instancIds.size() > 0) {
						String instancIdsStr = "";
						int index = 0;
						for (Iterator it = instancIds.iterator(); it.hasNext(); ) {
							instancIdsStr += ((Integer) it.next()).toString();
							if (index < instancIds.size() - 1) {
								instancIdsStr += ",";
							}
							index++;
						}
						queryString[0] += " and instanceId in(" + instancIdsStr + ")";
						queryString[1] += " and instanceId in(" + instancIdsStr + ")";
					} else {
						queryString[0] += " and instanceId in(0)";
						queryString[1] += " and instanceId in(0)";
					}
					break;
				case "销毁流程":
					 sql = "select instanceId from " + tableName6 + " where 1=1" + cusCondition;
					 instancIds = this.purchaseManager.getResultBySQLQuery(sql);
					if (instancIds != null && instancIds.size() > 0) {
						String instancIdsStr = "";
						int index = 0;
						for (Iterator it = instancIds.iterator(); it.hasNext(); ) {
							instancIdsStr += ((Integer) it.next()).toString();
							if (index < instancIds.size() - 1) {
								instancIdsStr += ",";
							}
							index++;
						}
						queryString[0] += " and instanceId in(" + instancIdsStr + ")";
						queryString[1] += " and instanceId in(" + instancIdsStr + ")";
					} else {
						queryString[0] += " and instanceId in(0)";
						queryString[1] += " and instanceId in(0)";
					}
					break;
				case "新增文件流程":
					 sql = "select instanceId from " + tableName7 + " where 1=1" + cusCondition;
					 instancIds = this.purchaseManager.getResultBySQLQuery(sql);
					if (instancIds != null && instancIds.size() > 0) {
						String instancIdsStr = "";
						int index = 0;
						for (Iterator it = instancIds.iterator(); it.hasNext(); ) {
							instancIdsStr += ((Integer) it.next()).toString();
							if (index < instancIds.size() - 1) {
								instancIdsStr += ",";
							}
							index++;
						}
						queryString[0] += " and instanceId in(" + instancIdsStr + ")";
						queryString[1] += " and instanceId in(" + instancIdsStr + ")";
					} else {
						queryString[0] += " and instanceId in(0)";
						queryString[1] += " and instanceId in(0)";
					}
					break;
				case "修改文件流程":
					 sql = "select instanceId from " + tableName8 + " where 1=1" + cusCondition;
					 instancIds = this.purchaseManager.getResultBySQLQuery(sql);
					if (instancIds != null && instancIds.size() > 0) {
						String instancIdsStr = "";
						int index = 0;
						for (Iterator it = instancIds.iterator(); it.hasNext(); ) {
							instancIdsStr += ((Integer) it.next()).toString();
							if (index < instancIds.size() - 1) {
								instancIdsStr += ",";
							}
							index++;
						}
						queryString[0] += " and instanceId in(" + instancIdsStr + ")";
						queryString[1] += " and instanceId in(" + instancIdsStr + ")";
					} else {
						queryString[0] += " and instanceId in(0)";
						queryString[1] += " and instanceId in(0)";
					}
					break;
				case "销毁文件流程":
					 sql = "select instanceId from " + tableName9 + " where 1=1" + cusCondition;
					 instancIds = this.purchaseManager.getResultBySQLQuery(sql);
					if (instancIds != null && instancIds.size() > 0) {
						String instancIdsStr = "";
						int index = 0;
						for (Iterator it = instancIds.iterator(); it.hasNext(); ) {
							instancIdsStr += ((Integer) it.next()).toString();
							if (index < instancIds.size() - 1) {
								instancIdsStr += ",";
							}
							index++;
						}
						queryString[0] += " and instanceId in(" + instancIdsStr + ")";
						queryString[1] += " and instanceId in(" + instancIdsStr + ")";
					} else {
						queryString[0] += " and instanceId in(0)";
						queryString[1] += " and instanceId in(0)";
					}
					break;
					default:
						break;
			}
		}
	}


	//获取自定义表单的查询字段
	@RequestMapping(params = "method=getSearchFields")
	@ResponseBody
	public Map<String, Object> getSearchFields(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		String flowId = request.getParameter("flowId");
		String ftlName = null;
		if (flowId != null && flowId.length() > 0) {
			PurchaseFlowDefinition flow = (PurchaseFlowDefinition)this.purchaseflowManager.get(Integer.valueOf(flowId));

		}
		if (ftlName != null) {
			JSONArray searchFields = this.purchaseCommonManager.getSearchFields(ftlName);
			map.put("_SearchFields", searchFields);
		}

		return map;
	}


	//编辑审核实例
	@RequestMapping(params = "method=edit")
	public String edit(HttpServletRequest request, HttpServletResponse response, PurchaseInforVo vo) throws Exception {

		PurchaseFlowDefinition flow = new PurchaseFlowDefinition();
		Integer purchaseId = vo.getPurchaseId();
		PurchaseInfor purchase = new PurchaseInfor();
		if (purchaseId != null && purchaseId.intValue() > 0) {
			purchase = (PurchaseInfor)this.purchaseManager.get(purchaseId);
			flow = purchase.getFlowId();
			BeanUtils.copyProperties(vo, purchase);
			//部门审核人
//			SystemUserInfor manager = purchase.getManager();
//			SystemUserInfor viceManager = instance.getViceManager();
//			if (manager != null) {
//				vo.setManagerId(manager.getPersonId());
//			}
//			if (viceManager != null) {
//				vo.setViceManagerId(viceManager.getPersonId());
//			}

			//对附件信息进行处理
			String attachmentFile = purchase.getAttach();
			if (attachmentFile != null && !attachmentFile.equals("")) {
				String[][] attachment = processFile(attachmentFile);
				request.setAttribute("_Attachment_Names", attachment[1]);
				request.setAttribute("_Attachments", attachment[0]);
			}
		}else {
			String flowIdStr = request.getParameter("flowId");
			if (flowIdStr != null && flowIdStr.length() > 0) {
				flow = (PurchaseFlowDefinition)this.purchaseflowManager.get(Integer.valueOf(flowIdStr));
			}
		}
		request.setAttribute("_Purchase", purchase);
		request.setAttribute("_Flow", flow);

		//获得归口部门


		//判断是否存在权限
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		OrganizeInfor org = systemUser.getPerson().getDepartment();
		int directorId = org.getDirector().getPersonId();
		PersonInfor directorInfor =(PersonInfor) this.personInforManager.get(directorId);
		SystemUserInfor director = directorInfor.getUser();

		request.setAttribute("_User", systemUser);
		request.setAttribute("_director", director);

		String organizeName  =  systemUser.getPerson().getDepartment().getOrganizeName();
		request.setAttribute("OrganizeName",organizeName);
//		boolean hasRight = this.purchaseManager.judgeRight(request, "edit", flow.getFlowId(), systemUser);
//		if (!hasRight) {
//			request.setAttribute("_ErrorMessage", "对不起,您无权进行该操作,请与系统管理员联系!");
//
//			return "/common/error";
//		}

		//获取用户信息
		List userss = this.systemUserManager.getUserByOrganize(systemUser.getPerson().getDepartment().getOrganizeId(), null);
		request.setAttribute("_Userss", userss);
		List users = this.systemUserManager.getAllValid();
		request.setAttribute("_Users", users);
//		申请人、所属部门

		//归口部门
		String sql = "from OrganizeInfor org where org.guikou = '是'";
		List guikou = this.organizeManager.getResultByQueryString(sql);
		request.setAttribute("_guikouDepartment",guikou);

		return "yiban/editPurchases";
	}



	//保存审核实例
	@RequestMapping(params = "method=save")
	public String save(HttpServletRequest request, HttpServletResponse response,
					   PurchaseInforVo vo, DefaultMultipartHttpServletRequest multipartRequest) throws Exception {

		PurchaseInfor purchase = new PurchaseInfor();
		Integer purchaseId = vo.getPurchaseId();
		String flowId = request.getParameter("flowId");
		PurchaseFlowDefinition flow = (PurchaseFlowDefinition)this.purchaseflowManager.get(Integer.valueOf(flowId));

		String oldFiles = "";
		Timestamp sysTime = new Timestamp(System.currentTimeMillis());

		boolean isNew = false;
		if (purchaseId != null && purchaseId.intValue() > 0) {
			purchase = (PurchaseInfor)this.purchaseManager.get(purchaseId);

			//修改信息时,对附件进行修改
			String filePaths = purchase.getAttach();
			oldFiles = deleteOldFile(request, filePaths, "attatchmentArray");

		}else {

			isNew = true;
			//创建时间
			purchase.setStartTime(sysTime);

			//申请人、所属部门
			SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
			purchase.setApplier(systemUser);
			purchase.setDepartment(systemUser.getPerson().getDepartment());

			//所属流程
			purchase.setFlowId(flow);
		}

//		BeanUtils.copyProperties(purchase, vo);

		//添加部门审核人推送待办提醒
		boolean hasManager = false;
		boolean hasViceManager = false;


		//部门审核人
		Integer managerId = vo.getManager();
		Integer viceManagerId = vo.getViceManagerId();

//		purchase.setSubmiterWord(null);
		if (managerId != null && managerId.intValue() > 0) {
			SystemUserInfor manager = (SystemUserInfor)this.systemUserManager.get(managerId);
			if (purchase.getManager() == null || purchase.getManager().getPersonId().intValue() != manager.getPersonId().intValue()) {
				purchase.setManager(manager);
				purchase.setManagerChecked(false);
				purchase.setManagerWord(null);
				purchase.setCheckTime(null);
				String managerAttachment = purchase.getManagerAttachment();
				if (managerAttachment != null && managerAttachment.length() > 0) {
					deleteFiles(managerAttachment);
				}
				purchase.setManagerAttachment(null);

				hasManager = true;
			}
		}else {
			purchase.setManager(null);
			purchase.setManagerChecked(false);
			purchase.setManagerWord(null);
			purchase.setCheckTime(null);
			String managerAttachment = purchase.getManagerAttachment();
			if (managerAttachment != null && managerAttachment.length() > 0) {
				deleteFiles(managerAttachment);
			}
			purchase.setManagerAttachment(null);
		}
		if (viceManagerId != null && viceManagerId.intValue() > 0) {
			SystemUserInfor viceManager = (SystemUserInfor)this.systemUserManager.get(viceManagerId);
			if (purchase.getViceManager() == null || purchase.getViceManager().getPersonId().intValue() != viceManager.getPersonId().intValue()) {
				purchase.setViceManager(viceManager);
				purchase.setViceManagerChecked(false);
				purchase.setViceManagerWord(null);
				purchase.setViceManagercheckTime(null);
				String ViceManagerAttachment = purchase.getViceManagerAttachment();
				if (ViceManagerAttachment != null && ViceManagerAttachment.length() > 0) {
					deleteFiles(ViceManagerAttachment);
				}
				purchase.setViceManagerAttachment(null);

				hasViceManager = true;
			}
		}else {
			purchase.setViceManager(null);
			purchase.setViceManagerChecked(false);
			purchase.setViceManagerWord(null);
			purchase.setViceManagercheckTime(null);
			String VicemanagerAttachment = purchase.getViceManagerAttachment();
			if (VicemanagerAttachment != null && VicemanagerAttachment.length() > 0) {
				deleteFiles(VicemanagerAttachment);
			}
			purchase.setViceManagerAttachment(null);
		}


		//申请人提交的附件
		String attachment = this.uploadAttachment(multipartRequest, "purchase");
		if (attachment == null || attachment.equals("")) {
			attachment = oldFiles;
		} else {
			if (oldFiles == null || oldFiles.equals("")) {
				// attachment = attachment;
			} else {
				attachment = attachment + "|" + oldFiles;
			}
		}
		purchase.setAttach(attachment);

 		int batchNumber = 1;
		String sql2 ="select max(a.batchNumber) from PurchaseInfor a where 1=1 and a.deleteFlag = 0 and a.flowId.flowId = 1 ";
		List list =this.purchaseManager.getResultByQueryString(sql2);
		if(list.get(0) != null){
			batchNumber =(int)list.get(0) + 1;
		}

		purchase.setBatchNumber(batchNumber);
		purchase.setGuige(vo.getGuige());
		purchase.setYsType(vo.getYsType());
		purchase.setApplication(vo.getApplication());
		purchase.setPurchaseNumber(vo.getPurchaseNumber());
		purchase.setPurchaseTitle(vo.getPurchaseTitle());
		purchase.setPurchaseGoods(vo.getPurchaseGoods());
		OrganizeInfor guikou = (OrganizeInfor)this.organizeManager.get(Integer.parseInt(vo.getGuikouDepartment()));
		purchase.setGuikouDepartment(guikou);

		/** 通过自定义表单模板生成html */
//		String templateName = null;
//		if (flow.getTemplate() != null && flow.getTemplate().length() > 0) {
//			//模板路径
//			String[] templateArray = flow.getTemplate().split("/");
//			templateName = templateArray[templateArray.length-1];
//			Template template = this.purchaseCommonManager.getTemplate(templateName, FlowConstant.Flow_FormTemplate_Path, true);
//
//			//根据时间定义文件名
//			Calendar calendar = Calendar.getInstance();
//			String fileName = String.valueOf(calendar.getTimeInMillis()) + ".html";
//
//			//删除原来的html页面
//			String oldHtmlPath = CoreConstant.Context_Real_Path + instance.getContentPath();
//			File htmlFile = new File(oldHtmlPath);
//			if(htmlFile.exists()) {
//				htmlFile.delete();
//			}
//
//
//
//			//保存新的html文件
//			String filePath = CoreConstant.Context_Real_Path + FlowConstant.Flow_Html_Path;	//生成的html文件保存路径
//			com.kwchina.core.util.File fileOperator = new com.kwchina.core.util.File();
//			File ioFile = new File(filePath);
//			fileOperator.createFilePath(ioFile);
//			String htmlPath = filePath + "/" + fileName;
//			FileOutputStream fos = new FileOutputStream(htmlPath);
//			Writer wOut = new OutputStreamWriter(fos, CoreConstant.ENCODING);
//			Map map = new HashMap();
//			map.put("base", request.getContextPath());	//工程所在路径
//			map.put("tagValue", false);	//在模板中用于区分是新增/修改还是生成html
//
//
//			Map maps = new HashMap();
//			maps = request.getParameterMap();
//			String[] contractTypes = (String[]) maps.get("contractType");
//			if(contractTypes!=null && contractTypes.length!=0){
//				String contractType = contractTypes[0];
//				if(contractType!=null && !contractType.equals("销售合同")){
//					maps.remove("saleType");
//				}
//			}
//
//			String indexNo = "";
//			if ((flow.getFlowId().intValue() == 86 || flow.getFlowId().intValue() == 87 || flow.getFlowId().intValue() == 88 || flow.getFlowId().intValue() == 90 || flow.getFlowId().intValue() == 92 || flow.getFlowId().intValue() == 93 || flow.getFlowId().intValue() == 94 || flow.getFlowId().intValue() == 95
//					|| flow.getFlowId().intValue() == 96 || flow.getFlowId().intValue() == 98 || flow.getFlowId().intValue() == 99 || flow.getFlowId().intValue() == 100
//			) && instanceId == null) {
//
//				//List customizeDatas = this.purchaseCommonManager.getFormData(flowNames, contractNo,colm);
//
//				String serialNo = "001";
//				String tableName = "";
//				String fieldName = "";
//				if (flow.getFlowId().intValue() == 86) {
//					tableName = "Customize_Hetongshenpi";
//					fieldName = "indexNo";
//				} else if (flow.getFlowId().intValue() == 87) {
//					tableName = "Customize_neibubaogao";
//					fieldName = "fileNo";
//				} else if (flow.getFlowId().intValue() == 88) {
//					tableName = "Customize_ZhiDuPingShen";
//					fieldName = "orderNo";
//				} else if (flow.getFlowId().intValue() == 90) {
//					tableName = "Customize_Xinzengwenjian";
//					fieldName = "fileNo";
//				} else if (flow.getFlowId().intValue() == 92) {
//					tableName = "Customize_Xiugaiwenjian";
//					fieldName = "fileEdition";
//				} else if (flow.getFlowId().intValue() == 93) {
//					tableName = "Customize_Xiaohuiwenjian";
//					fieldName = "fileEdition";
//				} else if (flow.getFlowId().intValue() == 94) {
//					tableName = "Customize_Xinzengliucheng";
//					fieldName = "flowEdition";
//				} else if (flow.getFlowId().intValue() == 95) {
//					tableName = "Customize_Xiugailiucheng";
//					fieldName = "flowEdition";
//				} else if (flow.getFlowId().intValue() == 96) {
//					tableName = "Customize_Xiaohuiliucheng";
//					fieldName = "flowEdition";
//				} else if (flow.getFlowId().intValue() == 98) {
//					tableName = "Customize_Xinzengwenjianliucheng";
//					fieldName = "fileEdition";
//				} else if (flow.getFlowId().intValue() == 99) {
//					tableName = "Customize_Xiugaiwenjianliucheng";
//					fieldName = "fileEdition";
//				} else if (flow.getFlowId().intValue() == 100) {
//					tableName = "Customize_Xiaohuiwenjianliucheng";
//					fieldName = "fileEdition";
//				}
//
//				String categoryName =request.getParameter("category");
//
//				//String categoryName = new String(request.getParameter("categoryName").getBytes("ISO-8859-1"), "utf-8");
//				String fieldYear = request.getParameter("fieldYear");
//
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
//				Calendar cal = Calendar.getInstance();
//				java.util.Date sDate = cal.getTime();
//				String sDateStr= sdf.format(sDate);
//				String reportYear = sDateStr;
//
//				if(flow.getFlowId().intValue() == 86){
//					if(categoryName.equals("物流")){
//						categoryName="WL";
//					}else if (categoryName.equals("码头")){
//						categoryName="MT";
//					}
//					indexNo=categoryName+reportYear;
//				}else{
//					indexNo=categoryName+"-"+reportYear;
//				}
//				//indexNo=categoryName+reportYear;
//				String sql = "select " + fieldName + " from " + tableName + " where "+fieldName+"  like '" + indexNo + "%' and "+fieldName+"  is not null ";
//
//					   sql+=" and instanceId not in(select instanceId from Workflow_InstanceInfor where deleteFlag = 1) group by "+fieldName+" order by "+ fieldName+" desc";
//				List maxSerialNos = this.purchaseManager.getResultBySQLQuery(sql);
//				if (maxSerialNos != null && maxSerialNos.size() > 0) {
//
//					String maxSerialNo = (String)maxSerialNos.get(0);
//
//					String documentLastNo = "";
//					if(flow.getFlowId().intValue() == 86){
//						documentLastNo = maxSerialNo.substring(6, 9);
//					}else{
//						//documentLastNo = maxSerialNo.substring(8, 11);
//						if(categoryName.equals("码头和物流")){
//							documentLastNo = maxSerialNo.substring(11, 14);
//						}else {
//							documentLastNo = maxSerialNo.substring(8, 11);
//						}
//					}
//
//
//					Integer maxSerialNoInt = Integer.valueOf(documentLastNo)+1;
//					String zero = "";
//					for(int i=0;i<3-maxSerialNoInt.toString().length();i++) {
//						zero += "0";
//					}
//					serialNo = zero + maxSerialNoInt.toString();
//					//serialNo = maxSerialNoInt.toString();
//				}
//
//				if(flow.getFlowId().intValue() == 86){
//					indexNo+=serialNo;
//				}else{
//					indexNo+="-"+serialNo;
//				}
//
//
//				String[] values = new String[1];
//				values[0]=indexNo;
//
//
//				if(flow.getFlowId().intValue() == 86){
//					maps.put("indexNo", values);
//				}else if(flow.getFlowId().intValue() == 87){
//					maps.put("fileNo", values);
//				}else if(flow.getFlowId().intValue() == 88){
//					maps.put("orderNo", values);
//				}
//
//			}
//
//
//
//			map.put("formValues", maps);	//从页面取得的值(键值对的方式)
//			map.put("_GLOBAL_PERSON", request.getSession().getAttribute("_GLOBAL_PERSON"));
//			if (instanceId != null) {
//				map.put("_InstanceId", instanceId);
//			}else {
//				map.put("_InstanceId", 0);
//			}
//			template.process(map, wOut);
//			wOut.flush();
//			wOut.close();
//			instance.setContentPath(FlowConstant.Flow_Html_Path +  "/" + fileName);
//		}
		/********* */
//		String flowNames = CnToSpell.getFullSpell(flow.getFlowName());
		String redirectStr = "";
//		String colm ="";
//		if((flow.getFlowId().intValue() == 1) && purchaseId == null ){
//
//			String serialNo = "001";
//			//String serialNo = "1";
//			//response.setCharacterEncoding("utf-8");
//			//request.setCharacterEncoding("utf-8");
//			String tableName = "";
//			String fieldName = "";
//			if(flow.getFlowId().intValue() == 86){
//				 tableName = "Customize_Hetongshenpi";
//				 fieldName = "indexNo";
//			}else if(flow.getFlowId().intValue() == 87){
//				 tableName = "Customize_neibubaogao";
//				 fieldName = "fileNo";
//			}else if(flow.getFlowId().intValue() == 88){
//				 tableName = "Customize_ZhiDuPingShen";
//				 fieldName = "orderNo";
//			}
//
//			String categoryName =request.getParameter("category");
//
//			//String categoryName = new String(request.getParameter("categoryName").getBytes("ISO-8859-1"), "utf-8");
//			String fieldYear = request.getParameter("fieldYear");
//
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
//			Calendar cal = Calendar.getInstance();
//			java.util.Date sDate = cal.getTime();
//			String sDateStr= sdf.format(sDate);
//			String reportYear = sDateStr;
//			String indexNo="";
//			if(flow.getFlowId().intValue() == 86){
//				if(categoryName.equals("物流")){
//					categoryName="WL";
//				}else if (categoryName.equals("码头")){
//					categoryName="MT";
//				}
//				indexNo=categoryName+reportYear;
//			}else{
//				indexNo=categoryName+"-"+reportYear;
//			}
//
//
//
//
//			String sql = "select " + fieldName + " from " + tableName + " where "+fieldName+"  like '" + indexNo + "%' and "+fieldName+"  is not null ";
//
//			 sql+=" and instanceId not in(select instanceId from Workflow_InstanceInfor where deleteFlag = 1) group by "+fieldName+" order by "+ fieldName+" desc";
//			List maxSerialNos = this.purchaseManager.getResultBySQLQuery(sql);
//			if (maxSerialNos != null && maxSerialNos.size() > 0) {
//				String maxSerialNo = (String)maxSerialNos.get(0);
//
//				String documentLastNo = "";
//				if(flow.getFlowId().intValue() == 86){
//					documentLastNo = maxSerialNo.substring(6, 9);
//				}else{
//					//documentLastNo = maxSerialNo.substring(8, 11);
//					if(categoryName.equals("码头和物流")){
//						documentLastNo = maxSerialNo.substring(11, 14);
//					}else {
//						documentLastNo = maxSerialNo.substring(8, 11);
//					}
//				}
//				Integer maxSerialNoInt = Integer.valueOf(documentLastNo)+1;
//				String zero = "";
//				for(int i=0;i<3-maxSerialNoInt.toString().length();i++) {
//					zero += "0";
//				}
//				serialNo = zero + maxSerialNoInt.toString();
//				//serialNo = maxSerialNoInt.toString();
//			}
//
//			if(flow.getFlowId().intValue() == 86){
//				indexNo+=serialNo;
//			}else{
//				indexNo+="-"+serialNo;
//			}
//
//
////			if (customizeDatas.size() != 0){
////
////				redirectStr = "/common/error";
////				request.setAttribute("_ErrorMessage", "对不起,合同编号重复,请重新提交!");
////			}else{
//				instance = (FlowInstanceInfor)this.purchaseManager.save(instance);
//
//				if (flow.getTemplate() != null && flow.getTemplate().length() > 0) {
//					//根据模板获取自定义表单数据
//					List fieldNames = this.purchaseCommonManager.getFieldNames(templateName);
//					String[] fieldValues = new String[fieldNames.size()];
//					for (int i=0;i<fieldNames.size();i++) {
//						if((flow.getFlowId().intValue() == 86||flow.getFlowId().intValue() == 87)&& i==1){
//							fieldValues[i] = indexNo;
//						}else if(flow.getFlowId().intValue() == 88 && i==2){
//							fieldValues[i] = indexNo;
//						}else{
//							fieldValues[i] = request.getParameter(fieldNames.get(i).toString());
//						}
//
//					}
//
//					//将汉字的流程名处理为拼音
//					String flowName = CnToSpell.getFullSpell(flow.getFlowName());
//
//
//					//新增或修改自定义表单数据
//					if (instanceId != null && instanceId.intValue() > 0) {
//						//修改
//						this.purchaseCommonManager.updateFormData(flowName, fieldNames, fieldValues, instance.getInstanceId(), 1);
//					}else {
//						//新增
//						this.purchaseCommonManager.updateFormData(flowName, fieldNames, fieldValues, instance.getInstanceId(), 0);
//					}
//				}
//
//
//				/** 该语句不放在编辑页面的原因:
//				 * 若在编辑页面提交数据后立即执行window.close()操作,
//				 * 则后台无法取到编辑页面的数据.
//				 * (此情况仅在页面包含附件操作时存在)
//				 * */
//				/*PrintWriter out = response.getWriter();
//				out.print("<script language='javascript'>");
//				out.print("var returnArray = [\"refresh\","+flowId+"];");
//				out.print("window.returnValue = returnArray;");
//				out.print("window.close();");
//				out.print("</script>");*/
//
//				redirectStr= "redirect:instanceInfor.do?method=view&instanceId=" + instance.getInstanceId();
//			//}
//		}else if(flow.getFlowId().intValue() == 87 && instanceId == null){
//				colm="fileNo";
//
//				String fileNo= request.getParameter("fileNo");
//
//				List customizeDatass = this.purchaseCommonManager.getFormData(flowNames, fileNo,colm);
//				if (customizeDatass.size() != 0){
//
//					redirectStr = "/common/error";
//					request.setAttribute("_ErrorMessage", "对不起,内部报告编号重复,请重新提交!");
//				}else{
//					instance = (FlowInstanceInfor)this.purchaseManager.save(instance);
//
//					if (flow.getTemplate() != null && flow.getTemplate().length() > 0) {
//						//根据模板获取自定义表单数据
//						List fieldNames = this.purchaseCommonManager.getFieldNames(templateName);
//						String[] fieldValues = new String[fieldNames.size()];
//						for (int i=0;i<fieldNames.size();i++) {
//							fieldValues[i] = request.getParameter(fieldNames.get(i).toString());
//						}
//
//						//将汉字的流程名处理为拼音
//						String flowName = CnToSpell.getFullSpell(flow.getFlowName());
//
//
//						//新增或修改自定义表单数据
//						if (instanceId != null && instanceId.intValue() > 0) {
//							//修改
//							this.purchaseCommonManager.updateFormData(flowName, fieldNames, fieldValues, instance.getInstanceId(), 1);
//						}else {
//							//新增
//							this.purchaseCommonManager.updateFormData(flowName, fieldNames, fieldValues, instance.getInstanceId(), 0);
//						}
//					}
//
//
//					/** 该语句不放在编辑页面的原因:
//					 * 若在编辑页面提交数据后立即执行window.close()操作,
//					 * 则后台无法取到编辑页面的数据.
//					 * (此情况仅在页面包含附件操作时存在)
//					 **/
//					/*PrintWriter out = response.getWriter();
//					out.print("<script language='javascript'>");
//					out.print("var returnArray = [\"refresh\","+flowId+"];");
//					out.print("window.returnValue = returnArray;");
//					out.print("window.close();");
//					out.print("</script>");*/
//
//					redirectStr= "redirect:instanceInfor.do?method=view&instanceId=" + instance.getInstanceId();
//				}
//		}else{

		purchase = (PurchaseInfor)this.purchaseManager.save(purchase);


//		if (flow.getTemplate() != null && flow.getTemplate().length() > 0) {
//			//根据模板获取自定义表单数据
//			List fieldNames = this.purchaseCommonManager.getFieldNames(templateName);
//			String[] fieldValues = new String[fieldNames.size()];
//			for (int i=0;i<fieldNames.size();i++) {
//				fieldValues[i] = request.getParameter(fieldNames.get(i).toString());
//			}

			//将汉字的流程名处理为拼音
//			String flowName = CnToSpell.getFullSpell(flow.getFlowName());
//
//
//			//新增或修改自定义表单数据
//			if (purchaseId != null && purchaseId.intValue() > 0) {
//
//				//修改
//				this.purchaseCommonManager.updateFormData(flowName, fieldNames, fieldValues, instance.getInstanceId(), 1);
//			}else {
//				//新增
//				this.purchaseCommonManager.updateFormData(flowName, fieldNames, fieldValues, instance.getInstanceId(), 0);
//			}
//		}



		/** 该语句不放在编辑页面的原因:
		 * 若在编辑页面提交数据后立即执行window.close()操作,
		 * 则后台无法取到编辑页面的数据.
		 * (此情况仅在页面包含附件操作时存在)
		 * */
		/*PrintWriter out = response.getWriter();
		out.print("<script language='javascript'>");
		out.print("var returnArray = [\"refresh\","+flowId+"];");
		out.print("window.returnValue = returnArray;");
		out.print("window.close();");
		out.print("</script>");*/

		redirectStr= "redirect:purchaseInfor.do?method=view&purchaseId=" + purchase.getPurchaseId();
//		}

		/******添加对部门审核人的app推送*****/
//		Timestamp current = new Timestamp(System.currentTimeMillis());
//		if(hasManager){
//			List<PurchaseInfor> returnInstances_m = PurchaseInfor.getNeedPushInstances(purchase.getManager(), sysTime);
//			String alias = purchase.getManager().getUserName();
//			int badge = returnInstances_m.size();
//
//			PushUtil pushUtil = new PushUtil();
//			pushUtil.pushNeedDealInstances(purchase, badge, alias);
//		}



		/******推送结束******/

		return redirectStr;
	}

	//批量保存审核实例
	@RequestMapping(params = "method=saves")
	public void saves(HttpServletRequest request, HttpServletResponse response,
					   PurchaseInforVo vo, DefaultMultipartHttpServletRequest multipartRequest) throws Exception {

		PurchasePackage purchasePackage = new PurchasePackage();
		Set<PurchaseInfor> purchaseInfors = new HashSet<PurchaseInfor>();
		String purchaseStr1 = request.getParameter("purchaseStr1");
		String [] purchaseTitles = request.getParameterValues("purchaseTitles");
		String [] purchaseNumbers = request.getParameterValues("purchaseNumbers");
		String [] purchaseGoodss = request.getParameterValues("purchaseGoodss");
		String [] guiges = request.getParameterValues("guiges");
		String [] applications = request.getParameterValues("applications");
		String flowId = request.getParameter("flowId");
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		PurchaseFlowDefinition flow = (PurchaseFlowDefinition)this.purchaseflowManager.get(Integer.valueOf(flowId));

		Calendar date = Calendar.getInstance();
		String year = String.valueOf(date.get(Calendar.YEAR));
		year+= "0001";
		int batchNumber = Integer.parseInt(year);
		String sql2 ="select max(a.batchNumber) from PurchaseInfor a where 1=1 and a.deleteFlag = 0 and a.flowId.flowId = 1";
		List list =this.purchaseManager.getResultByQueryString(sql2);
		if(list.get(0) != null){
			batchNumber =(int)list.get(0) + 1;
		}

//		int size = purchaseTitles.length;
		for(int i=0;i<purchaseTitles.length;i++) {
			if (purchaseNumbers[i] == null || purchaseNumbers[i].equals("")) {

			}else{
				PurchaseInfor purchase = new PurchaseInfor();
				//		Integer purchaseId = vo.getPurchaseId();
				String oldFiles = "";
				Timestamp sysTime = new Timestamp(System.currentTimeMillis());
				boolean isNew = false;
				isNew = true;
				//创建时间
				purchase.setStartTime(sysTime);

				//申请人、所属部门

				purchase.setApplier(systemUser);
				purchase.setDepartment(systemUser.getPerson().getDepartment());

				//所属流程
				purchase.setFlowId(flow);
				//		}

				//		BeanUtils.copyProperties(purchase, vo);

				//添加部门审核人推送待办提醒
				boolean hasManager = false;
				boolean hasViceManager = false;


				//部门审核人
				Integer managerId = vo.getManager();
				Integer viceManagerId = vo.getViceManagerId();

				//		purchase.setSubmiterWord(null);
				if (managerId != null && managerId.intValue() > 0) {
					SystemUserInfor manager = (SystemUserInfor) this.systemUserManager.get(managerId);
					if (purchase.getManager() == null || purchase.getManager().getPersonId().intValue() != manager.getPersonId().intValue()) {
						purchase.setManager(manager);
						purchase.setManagerChecked(false);
						purchase.setManagerWord(null);
						purchase.setCheckTime(null);

						purchasePackage.setManager(manager);

						String managerAttachment = purchase.getManagerAttachment();
						if (managerAttachment != null && managerAttachment.length() > 0) {
							deleteFiles(managerAttachment);
						}
						purchase.setManagerAttachment(null);

						hasManager = true;
					}
				} else {
					purchase.setManager(null);
					purchase.setManagerChecked(false);
					purchase.setManagerWord(null);
					purchase.setCheckTime(null);
					String managerAttachment = purchase.getManagerAttachment();
					if (managerAttachment != null && managerAttachment.length() > 0) {
						deleteFiles(managerAttachment);
					}
					purchase.setManagerAttachment(null);
				}
				if (viceManagerId != null && viceManagerId.intValue() > 0) {
					SystemUserInfor viceManager = (SystemUserInfor) this.systemUserManager.get(viceManagerId);
					if (purchase.getViceManager() == null || purchase.getViceManager().getPersonId().intValue() != viceManager.getPersonId().intValue()) {
						purchase.setViceManager(viceManager);
						purchase.setViceManagerChecked(false);
						purchase.setViceManagerWord(null);
						purchase.setViceManagercheckTime(null);

						purchasePackage.setVicemanager(viceManager);

						String ViceManagerAttachment = purchase.getViceManagerAttachment();
						if (ViceManagerAttachment != null && ViceManagerAttachment.length() > 0) {
							deleteFiles(ViceManagerAttachment);
						}
						purchase.setViceManagerAttachment(null);

						hasViceManager = true;
					}
				} else {
					purchase.setViceManager(null);
					purchase.setViceManagerChecked(false);
					purchase.setViceManagerWord(null);
					purchase.setViceManagercheckTime(null);
					String VicemanagerAttachment = purchase.getViceManagerAttachment();
					if (VicemanagerAttachment != null && VicemanagerAttachment.length() > 0) {
						deleteFiles(VicemanagerAttachment);
					}
					purchase.setViceManagerAttachment(null);
				}


				//申请人提交的附件
				String attachment = this.uploadAttachment(multipartRequest, "purchase");
				if (attachment == null || attachment.equals("")) {
					attachment = oldFiles;
				} else {
					if (oldFiles == null || oldFiles.equals("")) {
						// attachment = attachment;
					} else {
						attachment = attachment + "|" + oldFiles;
					}
				}
				purchase.setAttach(attachment);
				OrganizeInfor guikou = (OrganizeInfor) this.organizeManager.get(Integer.parseInt(vo.getGuikouDepartment()));
				purchase.setGuikouDepartment(guikou);


//			PurchaseInfor purchases = new PurchaseInfor();
//			purchases.setStartTime(sysTime);
				purchase.setBatchNumber(batchNumber);
				purchase.setPurchaseStr1(purchaseStr1);
				purchase.setGuige(guiges[i]);
				purchase.setApplication(applications[i]);
				purchase.setPurchaseNumber(Integer.parseInt(purchaseNumbers[i]));
				purchase.setPurchaseTitle(purchaseTitles[i]);
				purchase.setPurchaseGoods(purchaseGoodss[i]);
				purchase = (PurchaseInfor) this.purchaseManager.save(purchase);
				purchaseInfors.add(purchase);
			}
		}
		purchasePackage.setStartDate(new Timestamp(System.currentTimeMillis()));
		purchasePackage.setStatus(0);
		purchasePackage.setPackageName(purchaseStr1);
		purchasePackage.setCheckerType(2);
		purchasePackage.setPurchaseInfors(purchaseInfors);
		purchasePackage.setFlowId(1);
		this.purchasePackageDao.save(purchasePackage);


		PrintWriter out = response.getWriter();
		out.print("<script language='javascript'>");
		out.print("var returnArray = ['refresh',"+flowId+"];");
		out.print("window.returnValue = returnArray;");
		out.print("opener.location.reload();");
		out.print("window.close();");
		out.print("</script>");
		String redirectStr = "";
//		redirectStr= "redirect:purchaseInfor.do?method=view&purchaseId=" + purchase.getPurchaseId();
//		return redirectStr;
	}

	//删除审核实例
	@RequestMapping(params = "method=delete")
	public void delete(HttpServletRequest request, HttpServletResponse response) throws Exception {

		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);

		String rowIds = request.getParameter("rowIds");
		if (rowIds != null && rowIds.length() > 0) {
			String[] deleteIds = rowIds.split(",");
			if (deleteIds.length > 0) {
				for (int i = 0; i < deleteIds.length; i++) {
					int deleteId = Integer.valueOf(deleteIds[i]);
					PurchaseInfor instance = (PurchaseInfor) this.purchaseManager.get(deleteId);

					// 获取附件
					String attach = instance.getAttach();
					String formalAttach = instance.getFormalAttach();

					this.purchaseManager.deleteInstance(instance, systemUser);

					// 删除附件
					deleteFiles(attach);
					deleteFiles(formalAttach);

					// 删除关联的自定义表内的数据
//					FlowDefinition flow = instance.getFlowDefinition();
//					String template = flow.getTemplate();
//					if (template != null && template.length() > 0) {
//						String flowName = CnToSpell.getFullSpell(flow.getFlowName());
//						flowName = flowName.substring(0,1).toUpperCase() + flowName.substring(1);
//						this.purchaseCommonManager.delFormData(flowName, deleteId);
//					}
				}
			}
		}
	}

	//放入回收站或恢复
	@RequestMapping(params = "method=recycle")
	@ResponseBody
	public Map<String, Object> recycle(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();

		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		String flowId = request.getParameter("flowId");

		boolean hasRight = true;
		String deleteFlagStr = request.getParameter("deleteFlag");
		int deleteFlag = 1;
		if (deleteFlagStr != null && ("0").equals(deleteFlagStr) ) {
			deleteFlag = 0;
			//恢复操作时不做权限判断
		}else {
			//判断是否存在权限
			if(flowId.equals("80")){
				deleteFlag = 1;
			}else {
				hasRight = this.purchaseManager.judgeRight(request, "recycle", Integer.valueOf(flowId), systemUser);
			}

		}

		if (!hasRight) {
			map.put("_CanRecycle", false);
		}else {
			String rowIds = request.getParameter("rowIds");
			if (rowIds != null && rowIds.length() > 0) {
				String[] deleteIds = rowIds.split(",");
				if (deleteIds.length > 0) {
					for (int i = 0; i < deleteIds.length; i++) {
						int deleteId = Integer.valueOf(deleteIds[i]);
						PurchaseInfor instance = (PurchaseInfor) this.purchaseManager.get(deleteId);

						boolean canDelete = false;
						if(deleteFlag == 0){
							canDelete = true;
						}else {
							canDelete = this.purchaseManager.canDeleteFlowInstance(instance, systemUser);
						}

						if (canDelete) {
							instance.setDeleteFlag(deleteFlag);
							this.purchaseManager.save(instance);
						}

					}
				}
			}
			map.put("_CanRecycle", true);
		}

		return map;
	}

	//查看审核实例
	@RequestMapping(params = "method=view")
	public String view(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try{
		String purchaseId = request.getParameter("purchaseId");
		if (purchaseId != null && purchaseId.length() > 0) {
			PurchaseInfor purchase = (PurchaseInfor)this.purchaseManager.get(Integer.valueOf(purchaseId));
			SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
			int userId = systemUser.getPersonId().intValue();
			//判断是否有浏览权限
//			boolean canViewFlowInstance = this.purchaseManager.canViewFlowInstance(purchase, systemUser);
			boolean canViewFlowInstance = true;
			//2018.03.26 yuye(32)可以看到liusheng(154)的所有合同
//			if(systemUser.getPersonId()==32 && !canViewFlowInstance){
//				systemUser = (SystemUserInfor) systemUserManager.get(154);
//				canViewFlowInstance = this.purchaseManager.canViewFlowInstance(purchase, systemUser);
//			}
			if (canViewFlowInstance) {
				//判断是否为主办人
//				boolean isCharger = false;
				//int chargerId = instance.getFlowDefinition().getCharger().getPersonId();
//				int chargerId = instance.getCharger().getPersonId();
//				if (chargerId == userId) {
//					isCharger = true;
//				}
//				request.setAttribute("_IsCharger", isCharger);
				boolean isAdmin = false;
				int type = systemUser.getUserType();
				if(type==1){
					isAdmin = true;
				}
				request.setAttribute("_IsAdmin", isAdmin);
//				request.setAttribute("_CanEndFile",false);
				//判断是否为该流程的归档人
//				boolean isFileRole = false;
//				if(instance.getFlowDefinition().getFlowId()<90 || instance.getFlowDefinition().getFlowId()>100){
//					//非体系文件正常判断角色
//					int filerType = instance.getFlowDefinition().getFilerType();
//					if(filerType == 1){
//						RoleInfor tmpRole = instance.getFlowDefinition().getFileRole();
//						if(tmpRole != null){
//							int tmpRoleId = tmpRole.getRoleId();
//
//							Set<RoleInfor> sysRoleSet = systemUser.getRoles();
//							for(Iterator it = sysRoleSet.iterator();it.hasNext();){
//								RoleInfor sysRole = (RoleInfor)it.next();
//								if(tmpRoleId == sysRole.getRoleId()){
//									isFileRole = true;
//								}
//							}
//						}
//					}
//
//				}else {
//					//体系文件归档人为 文件流转审核人  谁审核谁归档
//					Set<InstanceLayerInfor> instanceLayerInfors= instance.getLayers();
//					InstanceLayerInfor layerInfor = new InstanceLayerInfor();
//					if(instanceLayerInfors!=null && !instanceLayerInfors.isEmpty()){
//						for(InstanceLayerInfor instanceLayerInfor:instanceLayerInfors){
//							if(instanceLayerInfor.getLayerName().equals("文件流转审核人")){
//								layerInfor = instanceLayerInfor;
//							}
//						}
//					}
//
//					Set<InstanceCheckInfor> checkInfors = layerInfor.getCheckInfors();
//					if(checkInfors!=null && !checkInfors.isEmpty()){
//						for(InstanceCheckInfor instanceCheckInfor : checkInfors){
//
//							SystemUserInfor systemUserInfor = instanceCheckInfor.getChecker();
//							if(systemUserInfor!=null && systemUserInfor.getPersonId().intValue() == systemUser.getPersonId().intValue() && (instanceCheckInfor.getStatus() == 1 ) ){
//
//								isFileRole = true;
//							}
//						}
//					}
//				}
//				request.setAttribute("_IsFileRole", isFileRole);


				//判断能否开始流转
//				boolean canStart = false;
//				if (systemUser.getPersonId().intValue() == instance.getApplier().getPersonId().intValue() && instance.getStartTime() == null
//						&& (instance.getSubmiterWord() != null
//								|| (instance.getSubmiterWord() == null && (instance.isManagerChecked()||instance.isViceManagerChecked()))
//								|| (instance.getManager() == null && instance.getViceManager() == null)) &&
//						((instance.getManager()!=null && instance.isManagerChecked() && instance.getViceManager()!=null && instance.isViceManagerChecked())
//								|| (instance.getManager()==null && instance.getViceManager()!=null && instance.isViceManagerChecked())
//								|| (instance.getManager()!=null && instance.isManagerChecked() && instance.getViceManager()==null ))) {
//					canStart = true;
//				}
//				request.setAttribute("_CanStart", canStart);


				//判断能否进行部门审核
				boolean canDepCheck = false;   //部门审核文件
				SystemUserInfor manager = purchase.getManager();
				SystemUserInfor viceManager = purchase.getViceManager();
//				System.out.println(viceManager.getPersonId());
//				if ((manager != null && manager.getPersonId() == userId && !purchase.isManagerChecked())
//						||(viceManager != null && viceManager.getPersonId() == userId && !purchase.isViceManagerChecked())) {
//					canDepCheck = true;
//				}
				if ((manager.getPersonId() == userId
						||(viceManager != null && viceManager.getPersonId() == userId)) && purchase.getPurchaseStatus() == 0) {
					canDepCheck = true;
				}

				request.setAttribute("_CanDepCheck", canDepCheck);
//				//判断能否中止部门审核
//				boolean canStopDepCheck = false;
//				if (((manager != null && !instance.isManagerChecked()))
//						&& instance.getSubmiterWord()==null && userId == instance.getApplier().getPersonId().intValue()) {
////				if(manager != null && viceManager != null && (instance.isManagerChecked() ||instance.isViceManagerChecked() )){
//						canStopDepCheck = true;
//				}
//				request.setAttribute("_CanStopDepCheck", canStopDepCheck);


				//判断是否可以添加审核层次
//				boolean canAddLayer = this.purchaseManager.canAddLayerInfor(instance, systemUser);
//				request.setAttribute("_CanAddLayer", canAddLayer);


				//判断是否有审核权限
				boolean canCheck = false;
				PurchaseCheckInfor tmpCheck = this.purchaseManager.isChecker(purchase, systemUser);
				if (tmpCheck != null) {
					// 能否审核
					if (tmpCheck.getEndDate() == null) {
						canCheck = true;
					}else if (systemUser.getPersonId() == 1){
						canCheck = true;
					}
				}
				request.setAttribute("_CanCheck", canCheck);

//				if (instance.getInstanceId() == 7026) {
//					System.out.println();
//				}
				//判断能否结束审核
				boolean canEndCheck = false;
				if (systemUser.getPersonId() == 1){
					canEndCheck = true;
				}
				request.setAttribute("_CanEndCheck", canEndCheck);
				//判断能否申请盖章
//				boolean canApplyStamp = false;
//				if (instance.getStartTime() != null&&instance.getEndTime() == null && isCharger && (instance.getFlowDefinition().getFlowId() == SubmitConstant.SubmitFlow_Report_Contract || instance.getFlowDefinition().getFlowId()>89 && instance.getFlowDefinition().getFlowId() < 101)) {
//
//					//判断当前审核层是否为"公司领导意见",且状态为已经完毕或中止,若是则可以执行盖章操作,否则不行
//					List rLayers = this.purchaseManager.getCurrentProcessLayers(instance);
//					if (rLayers != null && rLayers.size() > 0) {
//						for (Iterator itLayer=rLayers.iterator();itLayer.hasNext();) {
//							InstanceLayerInfor layer = (InstanceLayerInfor)itLayer.next();
//							if ((("公司总经理").equals(layer.getLayerName()) || ("公司领导意见").equals(layer.getLayerName()) || ("公司领导").equals(layer.getLayerName())) && instance.getStamped()==0 && (layer.getStatus() == InstanceLayerInfor.Layer_Status_End||layer.getStatus() == InstanceLayerInfor.Layer_Status_Finished)) {
//								canApplyStamp = true;
//								break;
//							}
//						}
//					}
//				}
//				request.setAttribute("_CanApplyStamp", canApplyStamp);

//				if(instance.getOldInstanceId()!=null && instance.getOldInstanceId()!=0){
//					FlowInstanceInfor oldinstance = (FlowInstanceInfor)this.purchaseManager.get(instance.getOldInstanceId());
//					request.setAttribute("_Oldinstance", oldinstance);
//				}
				getProcessInfors(request, response, purchase);//获取审核实例相关信息

				boolean canSave = false;
				RoleInfor caigou = (RoleInfor) roleManager.get(PurchaseCheckInfor.Check_Role_Caigou);
				RoleInfor guikou = (RoleInfor) roleManager.get(PurchaseCheckInfor.Check_Role_Guikou);
				if(roleManager.belongRole(systemUser,caigou) && (purchase.getPurchaseStatus() == 4 || purchase.getPurchaseStatus() == 5)){
					canSave = true;
				}
				if(roleManager.belongRole(systemUser,guikou) && (purchase.getPurchaseStatus() == 1 || purchase.getPurchaseStatus() == 2)){
					canSave = true;
				}
				request.setAttribute("_CanSave", canSave);

				String packageId = request.getParameter("packageId");
				if (packageId == null || packageId == ""){
					request.setAttribute("packageId","-1");
				}else{
					request.setAttribute("packageId",packageId);
				}
			}
		}
		}catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return "yiban/viewPurchase";
	}

	//查看包实例
	@RequestMapping(params = "method=viewPackage")
	public String viewPackage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try{
			SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
			String flowId = request.getParameter("flowId");
			String packageId = request.getParameter("packageId");
			PurchasePackage purchasePackage = (PurchasePackage)this.packageManager.get(Integer.parseInt(packageId));
			int checkerType = purchasePackage.getCheckerType();
			if (packageId != null && packageId.length() > 0) {
				request.setAttribute("packageId",packageId);
//				request.setAttribute("flowId",flowId);
			}
			RoleInfor guikou = (RoleInfor)this.roleManager.get(PurchaseCheckInfor.Check_Role_Guikou);
			RoleInfor caiwu = (RoleInfor)this.roleManager.get(PurchaseCheckInfor.Check_Role_Caiwu);
			RoleInfor gongsilingdao = (RoleInfor)this.roleManager.get(PurchaseCheckInfor.Check_Role_Gongsilingdao);
			RoleInfor jiguilingdao = (RoleInfor)this.roleManager.get(PurchaseCheckInfor.Check_Role_Jiguibulingdao);
			int directorId = 0;
			if (systemUser.getPerson().getDepartment().getDirector() != null){
				directorId = systemUser.getPerson().getDepartment().getDirector().getPersonId();
			}

			boolean candis = false;
			boolean canback = false;
			boolean canpackageName = false;
			if (systemUser.getPersonId() == directorId && checkerType != 2){
				canback =true;
				candis = true;
			}
			OrganizeInfor org = (OrganizeInfor)this.organizeManager.get(PurchaseCheckInfor.Check_Org_Caigou);
			int caigoulingdao = org.getDirector().getPersonId();
			if (systemUser.getPersonId() == caigoulingdao){
				canback =true;
				canpackageName =true;
			}
			if (roleManager.belongRole(systemUser, caiwu)){
				canback =true;
//				canpackageName =true;
			}
			if (roleManager.belongRole(systemUser, jiguilingdao)){
				canback =true;
				candis = true;
			}
			if (roleManager.belongRole(systemUser, gongsilingdao)){
				candis = true;
			}
			if (checkerType == 2){
				candis = true;
			}

			request.setAttribute("_candis",candis);
			request.setAttribute("_canback",canback);
			request.setAttribute("_canpackageName",canpackageName);


		}catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return "yiban/purchasePackageInforInclude";
//		return "redirect:/FramePurchase5.jsp";
	}

	//编辑部门审核
	@RequestMapping(params = "method=editDepCheck")
	public String editDepCheck(HttpServletRequest request, HttpServletResponse response, PurchaseInforVo vo) throws Exception {

		String purchaseId = request.getParameter("purchaseId");
		String packageId = request.getParameter("packageId");
		request.setAttribute("packageId",packageId);
//		List approve = this.approveSentenceManager.getApproveSentenceOrderBy();
//		request.setAttribute("_Approves", approve);
//
//		if (purchaseId != null && purchaseId.length() > 0) {
//			PurchaseInfor purchase = (PurchaseInfor)this.purchaseManager.get(Integer.valueOf(purchaseId));
//			SystemUserInfor manager = purchase.getManager();
//
//			SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
//			if (manager != null && manager.getPersonId().intValue() == systemUser.getPersonId().intValue()) {
//				//审核人
//				request.setAttribute("_CheckWord", purchase.getManagerWord());
//				//对附件信息进行处理
//				String managerAttachment = purchase.getManagerAttachment();
//				if (managerAttachment != null && !managerAttachment.equals("")) {
//					String[][] attachment = processFile(managerAttachment);
//					request.setAttribute("_DepAttachment_Names", attachment[1]);
//					request.setAttribute("_DepAttachments", attachment[0]);
//				}
//			}
//
//			//获取审核实例相关信息
//			getProcessInfors(request, response, purchase);
//		}
		List approve = this.approveSentenceManager.getApproveSentenceOrderBy();
		request.setAttribute("_Approves", approve);

		if (purchaseId != null && purchaseId.length() > 0) {
			PurchaseInfor purchase = (PurchaseInfor)this.purchaseManager.get(Integer.valueOf(purchaseId));
			SystemUserInfor manager = purchase.getManager();
			SystemUserInfor viceManager = purchase.getViceManager();

			SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
			if (manager != null && manager.getPersonId().intValue() == systemUser.getPersonId().intValue()) {
				//审核人
				request.setAttribute("_CheckWord", purchase.getManagerWord());
				//对附件信息进行处理
				String managerAttachment = purchase.getManagerAttachment();
				if (managerAttachment != null && !managerAttachment.equals("")) {
					String[][] attachment = processFile(managerAttachment);
					request.setAttribute("_DepAttachment_Names", attachment[1]);
					request.setAttribute("_DepAttachments", attachment[0]);
				}
			}else if (viceManager != null && viceManager.getPersonId().intValue() == systemUser.getPersonId().intValue()) {
				//审核人二
				request.setAttribute("_CheckWord", purchase.getViceManagerWord());
				//对附件信息进行处理
				String viceManagerAttachment = purchase.getViceManagerAttachment();
				if (viceManagerAttachment != null && !viceManagerAttachment.equals("")) {
					String[][] attachment = processFile(viceManagerAttachment);
					request.setAttribute("_DepAttachment_Names", attachment[1]);
					request.setAttribute("_DepAttachments", attachment[0]);
				}
			}

			//获取审核实例相关信息
			getProcessInfors(request, response, purchase);
		}

		return "yiban/editDepCheck";
	}

	//保存部门审核
	@RequestMapping(params = "method=saveDepCheck")
	public String saveDepCheck(HttpServletRequest request, HttpServletResponse response,
			PurchaseInforVo vo, DefaultMultipartHttpServletRequest multipartRequest) throws Exception {

		String packageId2 = request.getParameter("packageId");
		Integer purchaseId = vo.getPurchaseId();
		String checkWord = request.getParameter("checkWord");
		int managerCheckStatus = Integer.parseInt(request.getParameter("checkWordR"));
		Timestamp checkTime = new Timestamp(System.currentTimeMillis());
		if (purchaseId != null && purchaseId.intValue() > 0) {
			PurchaseInfor purchase = (PurchaseInfor)this.purchaseManager.get(purchaseId);
			SystemUserInfor manager = purchase.getManager();
			SystemUserInfor viceManager = purchase.getViceManager();

			SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
			if (manager != null && manager.getPersonId().intValue() == systemUser.getPersonId().intValue() ) {
				//审核人一
				purchase.setManagerWord(checkWord);
				purchase.setCheckTime(checkTime);
//				purchase.setLastNode(1);
				purchase.setManagerChecked(true);
				purchase.setManagerCheckStatus(managerCheckStatus);
				if (managerCheckStatus == 2) {
					purchase.setPurchaseStatus(PurchaseInfor.Purchase_Status_No);
				}
				//修改信息时,对附件进行修改
				String filePaths = purchase.getManagerAttachment();
				String oldFiles = deleteOldFile(request, filePaths, "attatchmentArray");

				//附件
				String attachment = this.uploadAttachment(multipartRequest, "workflow");
				if (attachment == null || attachment.equals("")) {
					attachment = oldFiles;
				} else {
					if (oldFiles == null || oldFiles.equals("")) {
						// attachment = attachment;
					} else {
						attachment = attachment + "|" + oldFiles;
					}
				}
				purchase.setManagerAttachment(attachment);
			}else if (viceManager != null && viceManager.getPersonId().intValue() == systemUser.getPersonId().intValue()) {
				//审核人二
				purchase.setViceManagerWord(checkWord);
				purchase.setViceManagercheckTime(checkTime);
				purchase.setViceManagerChecked(true);
				purchase.setViceManagerCheckStatus(managerCheckStatus);
				if (managerCheckStatus == 2) {
					purchase.setPurchaseStatus(PurchaseInfor.Purchase_Status_No);
				}

				//修改信息时,对附件进行修改
				String filePaths = purchase.getViceManagerAttachment();
				String oldFiles = deleteOldFile(request, filePaths, "attatchmentArray");

				//附件
				String attachment = this.uploadAttachment(multipartRequest, "workflow");
				if (attachment == null || attachment.equals("")) {
					attachment = oldFiles;
				} else {
					if (oldFiles == null || oldFiles.equals("")) {
						// attachment = attachment;
					} else {
						attachment = attachment + "|" + oldFiles;
					}
				}
				purchase.setViceManagerAttachment(attachment);
			}
			if (purchase.getManagerCheckStatus() == 1 && (purchase.getViceManagerCheckStatus() == 1 || viceManager == null)) {
				purchase.setPurchaseStatus(1);
			}
			purchase = (PurchaseInfor) this.purchaseManager.save(purchase);

			if (purchase.getManagerCheckStatus() == 1 && (purchase.getViceManagerCheckStatus() == 1 || viceManager == null)) {
				this.purchaseManager.startNode(request, response, purchase);
			}
			turnPackageStatus(systemUser, packageId2);
		}else{
			return "/common/error";
		}

		return "redirect:purchaseInfor.do?method=view&purchaseId=" + purchaseId;
	}
	//批量保存部门审核
	@RequestMapping(params = "method=saveDepChecks")
	public void saveDepChecks(HttpServletRequest request, HttpServletResponse response,
							   PurchaseInforVo vo, DefaultMultipartHttpServletRequest multipartRequest) throws Exception {

		String rowIds = request.getParameter("rowIds");
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		if (rowIds != null &&rowIds.length() > 0) {
			String[] updateIds = rowIds.split(",");
			if (updateIds.length > 0) {
				for (int j = 0; j < updateIds.length; j++) {
					int purchaseId = Integer.valueOf(updateIds[j]);
					PurchaseInfor purchase = (PurchaseInfor) this.purchaseManager.get(purchaseId);
					String checkWord = "";
					int managerCheckStatus = 1;
					Timestamp checkTime = new Timestamp(System.currentTimeMillis());
					SystemUserInfor manager = purchase.getManager();
					SystemUserInfor viceManager = purchase.getViceManager();
					if (manager != null && manager.getPersonId().intValue() == systemUser.getPersonId().intValue() ) {
						//审核人一
						purchase.setManagerWord(checkWord);
						purchase.setCheckTime(checkTime);
//				purchase.setLastNode(1);
						purchase.setManagerChecked(true);
						purchase.setManagerCheckStatus(managerCheckStatus);
						if (managerCheckStatus == 2) {
							purchase.setPurchaseStatus(PurchaseInfor.Purchase_Status_No);
						}



					}else if (viceManager != null && viceManager.getPersonId().intValue() == systemUser.getPersonId().intValue()) {
						//审核人二
						purchase.setViceManagerWord(checkWord);
						purchase.setViceManagercheckTime(checkTime);
						purchase.setViceManagerChecked(true);
						purchase.setViceManagerCheckStatus(managerCheckStatus);
						if (managerCheckStatus == 2) {
							purchase.setPurchaseStatus(PurchaseInfor.Purchase_Status_No);
						}


					}
					if (purchase.getManagerCheckStatus() == 1 && (purchase.getViceManagerCheckStatus() == 1 || viceManager == null)) {
						purchase.setPurchaseStatus(1);
					}
					purchase = (PurchaseInfor) this.purchaseManager.save(purchase);

					if (purchase.getManagerCheckStatus() == 1 && (purchase.getViceManagerCheckStatus() == 1 || viceManager == null)) {
						this.purchaseManager.startNode(request, response, purchase);
					}
				}
			}
		}
//		Integer purchaseId = vo.getPurchaseId();
//		String checkWord = request.getParameter("checkWord");
//		int managerCheckStatus = Integer.parseInt(request.getParameter("checkWordR"));
//		Timestamp checkTime = new Timestamp(System.currentTimeMillis());
//		if (purchaseId != null && purchaseId.intValue() > 0) {
//			PurchaseInfor purchase = (PurchaseInfor)this.purchaseManager.get(purchaseId);
//			SystemUserInfor manager = purchase.getManager();
//			SystemUserInfor viceManager = purchase.getViceManager();
//
//			SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
//			if (manager != null && manager.getPersonId().intValue() == systemUser.getPersonId().intValue() ) {
//				//审核人一
//				purchase.setManagerWord(checkWord);
//				purchase.setCheckTime(checkTime);
////				purchase.setLastNode(1);
//				purchase.setManagerChecked(true);
//				purchase.setManagerCheckStatus(managerCheckStatus);
//				if (managerCheckStatus == 2) {
//					purchase.setPurchaseStatus(PurchaseInfor.Purchase_Status_No);
//				}
//				//修改信息时,对附件进行修改
//				String filePaths = purchase.getManagerAttachment();
//				String oldFiles = deleteOldFile(request, filePaths, "attatchmentArray");
//
//				//附件
//				String attachment = this.uploadAttachment(multipartRequest, "workflow");
//				if (attachment == null || attachment.equals("")) {
//					attachment = oldFiles;
//				} else {
//					if (oldFiles == null || oldFiles.equals("")) {
//						// attachment = attachment;
//					} else {
//						attachment = attachment + "|" + oldFiles;
//					}
//				}
//				purchase.setManagerAttachment(attachment);
//			}else if (viceManager != null && viceManager.getPersonId().intValue() == systemUser.getPersonId().intValue()) {
//				//审核人二
//				purchase.setViceManagerWord(checkWord);
//				purchase.setViceManagercheckTime(checkTime);
//				purchase.setViceManagerChecked(true);
//				purchase.setViceManagerCheckStatus(managerCheckStatus);
//				if (managerCheckStatus == 2) {
//					purchase.setPurchaseStatus(PurchaseInfor.Purchase_Status_No);
//				}
//
//				//修改信息时,对附件进行修改
//				String filePaths = purchase.getViceManagerAttachment();
//				String oldFiles = deleteOldFile(request, filePaths, "attatchmentArray");
//
//				//附件
//				String attachment = this.uploadAttachment(multipartRequest, "workflow");
//				if (attachment == null || attachment.equals("")) {
//					attachment = oldFiles;
//				} else {
//					if (oldFiles == null || oldFiles.equals("")) {
//						// attachment = attachment;
//					} else {
//						attachment = attachment + "|" + oldFiles;
//					}
//				}
//				purchase.setViceManagerAttachment(attachment);
//			}
//			if (purchase.getManagerCheckStatus() == 1 && (purchase.getViceManagerCheckStatus() == 1 || viceManager == null)) {
//				purchase.setPurchaseStatus(1);
//			}
//			purchase = (PurchaseInfor) this.purchaseManager.save(purchase);
//
//			if (purchase.getManagerCheckStatus() == 1 && (purchase.getViceManagerCheckStatus() == 1 || viceManager == null)) {
//				this.purchaseManager.startNode(request, response, purchase);
//			}
//		}
//		else{
//			return "/common/error";
//		}

//		return "redirect:purchaseInfor.do?method=view&purchaseId=" + purchaseId;
	}

	//中止部门审核
	@RequestMapping(params = "method=saveDepStop")
	@ResponseBody
	public Map<String, Object> saveDepStop(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		String instanceId = request.getParameter("instanceId");
		if (instanceId != null && instanceId.length() > 0) {
			FlowInstanceInfor instance = (FlowInstanceInfor)this.purchaseManager.get(Integer.valueOf(instanceId));
			SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
			if (systemUser.getPersonId().intValue() == instance.getApplier().getPersonId().intValue()) {
				String submiterWord = request.getParameter("submiterWord");
//				submiterWord = new String(submiterWord.getBytes("ISO-8859-1"), "gbk");
				instance.setSubmiterWord(submiterWord);
				this.purchaseManager.save(instance);
				map.put("message", "部门审核已被中止！");
			}
		}

		return map;
	}

	//开始流转
//	@RequestMapping(params = "method=startInstance")
//	public String startInstance(HttpServletRequest request, HttpServletResponse response) throws Exception {
//
//		String instanceId = request.getParameter("instanceId");
//		FlowInstanceInfor instance = new FlowInstanceInfor();
//		if (instanceId != null && instanceId.length() > 0) {
//			instance = (FlowInstanceInfor)this.purchaseManager.get(Integer.valueOf(instanceId));
//			if (instance.getStartTime() == null) {
//				instance = (FlowInstanceInfor)this.purchaseManager.startInstance(instance);
//			}
//		}
//
//		return "redirect:instanceInfor.do?method=view&instanceId=" + instance.getInstanceId();
//	}

	//中止审核(判断中止的审核层有一个还是多个,只有一个时,程序直接中止)
	@RequestMapping(params="method=endCheck")
	@ResponseBody
	public Map<String, Object> endCheck(HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> map = new HashMap<String, Object>();
		String PurchaseId =  request.getParameter("PurchaseId");

		if (PurchaseId != null && PurchaseId.length() > 0) {
			PurchaseInfor purchase = (PurchaseInfor)this.purchaseManager.get(Integer.valueOf(PurchaseId));

			//获取当前正在处理的未中止的审核层
			List processLayers = this.purchaseManager.getCurrentProcessLayers(purchase);
			List currentLayers = new ArrayList(processLayers.size());
			for (Iterator it = processLayers.iterator(); it.hasNext();) {
				InstanceLayerInfor tmpLayer = (InstanceLayerInfor)it.next();
				if (tmpLayer.getStatus() == InstanceLayerInfor.Layer_Status_Normal) {
					currentLayers.add(tmpLayer);
				}
			}

			if (currentLayers != null && currentLayers.size() > 0) {

				if (currentLayers.size() > 1) {
					//当中止的审核层数量大于1时,需要由用户手动选取
					JSONArray layerArray = new JSONArray();
					for (Iterator it=currentLayers.iterator();it.hasNext();) {
						InstanceLayerInfor layer = (InstanceLayerInfor)it.next();
						Map<String, Object> tmpMap = new HashMap<String, Object>();
						tmpMap.put("layerId", layer.getLayerId());
						tmpMap.put("layerName", layer.getLayerName());
						layerArray.add(tmpMap);
					}
					map.put("processLayers", layerArray);
					map.put("needChoose", true);
				}else {

					//当中止的审核层只有一个时,程序直接中止
					InstanceLayerInfor layer = (InstanceLayerInfor)currentLayers.get(0);
					String layerName = layer.getLayerName();
					try {
//						this.purchaseLayerInforManager.endCheckLayer(currentLayers, purchase);
						map.put("needChoose", false);
						map.put("message", "审核层'"+layerName+"'已经被中止！");
					} catch (Exception e) {
						map.put("needChoose", false);
						map.put("message", "每个层次至少有一个人审核过才能中止！");
					}
				}

			}else {
				map.put("needChoose", false);
				map.put("message", "没有可以中止的审核层！");
			}
		}

		return map;
	}

	//中止审核(中止用户所选审核层)
	@RequestMapping(params="method=saveStop")
	@ResponseBody
	public Map<String, Object> saveStop(HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> map = new HashMap<String, Object>();
		String layerIds =  request.getParameter("layerIds");

		if (layerIds != null && layerIds.length() > 0) {
			FlowInstanceInfor instance = null;
			String[] layerArray = layerIds.split(",");

			List endLayerList = new ArrayList(layerArray.length);
			for (int i=0;i<layerArray.length;i++) {
				if (layerArray[i].length() > 0) {
					InstanceLayerInfor layer = (InstanceLayerInfor)this.purchaseLayerInforManager.get(Integer.valueOf(layerArray[i]));
					endLayerList.add(layer);

					instance = layer.getInstance();
				}
			}

			try {
				this.purchaseLayerInforManager.endCheckLayer(endLayerList, instance);
				map.put("message", "所选审核层已经被中止！");
			} catch (InstanceSuspendLayerException e) {
				map.put("message", "每个层次至少有一个人审核过才能中止！");
			}

		}

		return map;
	}

	//跳转到下一节点(判断预设的下一步节点是否相同,假如相同则直接跳转,否则需要用户选择)
//	@RequestMapping(params="method=nextNode")
//	@ResponseBody
//	public Map<String, Object> nextNode(HttpServletRequest request, HttpServletResponse response) {
//
//		Map<String, Object> map = new HashMap<String, Object>();
//		String instanceId =  request.getParameter("instanceId");
//
//		if (instanceId != null && instanceId.length() > 0) {
//			FlowInstanceInfor instance = (FlowInstanceInfor)this.purchaseManager.get(Integer.valueOf(instanceId));
//
//			map = this.purchaseManager.nextValidate(instance);
//		}
//		return map;
//	}

	//跳转到下一节点(跳转到用户所选的下一节点)
//	@RequestMapping(params="method=saveNext")
//	@ResponseBody
//	public Map<String, Object> saveNext(HttpServletRequest request, HttpServletResponse response) {
//
//		Map<String, Object> map = new HashMap<String, Object>();
//		String fromLayerId =  request.getParameter("fromLayerId");
//
//		if (fromLayerId != null && fromLayerId.length() > 0){
//
//			InstanceLayerInfor layerInfor = (InstanceLayerInfor)this.purchaseLayerInforManager.get(Integer.valueOf(fromLayerId));
//			this.purchaseManager.flowToNextNode(layerInfor);
//			map.put("message", "已转到所选节点！");
//		}
//
//		return map;
//	}

	//编辑结束信息
	@RequestMapping(params = "method=editEnd")
	public String editEnd(HttpServletRequest request, HttpServletResponse response, PurchaseInforVo vo) throws Exception {


		Integer purchaseId = vo.getPurchaseId();
		if (purchaseId != null && purchaseId.intValue() > 0) {
			PurchaseInfor purchase = (PurchaseInfor)this.purchaseManager.get(purchaseId);

			//获取相关信息
			getProcessInfors(request, response, purchase);

			//结束时间默认为系统时间
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			request.setAttribute("_SystemTime", sdf.format(new Timestamp(System.currentTimeMillis())));
		}

		return "yiban/endPurchase";
	}

	//保存结束信息
	@RequestMapping(params = "method=saveEnd")
	public String saveEnd(HttpServletRequest request, HttpServletResponse response,
			PurchaseInforVo vo, DefaultMultipartHttpServletRequest multipartRequest) throws Exception {

		Integer purchaseId = vo.getPurchaseId();
		if (purchaseId != null && purchaseId.intValue() > 0) {
			PurchaseInfor purchase = (PurchaseInfor)this.purchaseManager.get(purchaseId);

			//保存结束时间
			String endTimeStr = request.getParameter("endTime");
			Timestamp endTime = null;
			if (endTimeStr != null && endTimeStr.length() > 0) {
				endTime = Timestamp.valueOf(endTimeStr);
			}
			purchase.setEndTime(endTime);
			purchase.setDeleteFlag(1);
			//正式附件
			String formalAttach = this.uploadAttachment(multipartRequest, "workflow");
			purchase.setFormalAttach(formalAttach);

			this.purchaseManager.save(purchase);

		}
		return "redirect:purchaseInfor.do?method=view&purchaseId=" + purchaseId;
//		return list(request, response);
	}


	/**
	 * 获取需要处理的审核实例(用于首页显示)
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=getNeedDealInstances")
	public void getNeedDealInstances(HttpServletRequest request, HttpServletResponse response) throws Exception {
		/************判断是否移动端进来的***********/
		boolean isMobile = false;
		String dTag = request.getParameter("dTag");
		if(dTag != null && !dTag.equals("")){
			if(dTag.equals("true")){
				isMobile = true;
			}
		}

		//判断是否是自动定时器，如果是，则不记录
		String isAutoStr = request.getParameter("autoGet");
		boolean isAuto = false;
		if(StringUtil.isNotEmpty(isAutoStr)){
			if(isAutoStr.equals("1")){
				isAuto = true;
			}
		}

		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		if(systemUser != null){

			if(isMobile && !isAuto && StringUtil.isNotEmpty(SysCommonMethod.getPlatform(request))){
				/************记录app模块使用日志************/
				AppModuleLog appModuleLog = new AppModuleLog();
				appModuleLog.setModuleName("待办事项");
				appModuleLog.setPlatform(SysCommonMethod.getPlatform(request));
				appModuleLog.setLogTime(new Timestamp(System.currentTimeMillis()));
				appModuleLog.setUserName(systemUser.getUserName());
				this.appModuleLogManager.save(appModuleLog);
				/*****************************************/
			}

			String type = null;
			List<PurchaseInfor> returnInstances_m = new ArrayList<PurchaseInfor>();
			List<PurchasePackage> returnInstances = new ArrayList<PurchasePackage>();
			List<String> warningStrs = new ArrayList<String>();
			if(isMobile){
				//移动端需要显示的
				returnInstances_m = this.purchaseManager.getNeedPushInstances(systemUser, null);
			}else {
				//得到待办
				Map<String, Object> map = this.purchaseManager.getNeedDealInstances(systemUser,1);

				//PC端需要显示的待办
				returnInstances = (List<PurchasePackage>)map.get("ReturnInstances");
				type =(String)map.get("type");
//				warningStrs = (List<String>)map.get("WarningStrs");
			}


//			//移动端需要显示的
//			List<FlowInstanceInfor> returnInstances_m = (List<FlowInstanceInfor>)map.get("ReturnInstances_m");


			JSONObject jsonObj = new JSONObject();
			JSONConvert convert = new JSONConvert();
			JSONArray jsonArray = new JSONArray();
			//通知Convert，哪些关联对象需要获取
			List awareObject = new ArrayList();
//			awareObject.add("flowId.flowName");
			awareObject.add("manager");
//			awareObject.add("applier.person");



			if (returnInstances != null) {
				if (isMobile) {
					jsonArray = convert.modelCollect2JSONArray(returnInstances_m, awareObject);
				} else {
					jsonArray = convert.modelCollect2JSONArray(returnInstances, awareObject);
				}
			}
			jsonObj.put("_Instances", jsonArray);
			jsonObj.put("_type", type);
//			jsonObj.put("_WarningStrs", warningStrs);
//			jsonObj.put("_InstanceCount", returnInstances_m.size());

			//设置字符编码
	        response.setContentType(CoreConstant.CONTENT_TYPE);
	        response.getWriter().print(jsonObj);
		}



	}

//	/** 判断是否当前审核人
//	 * @param instance 实例信息
//	 * @param systemUser 当前用户
//	 * @return tmpCheck 当前审核层
//	 * */
//	private InstanceCheckInfor isChecker(FlowInstanceInfor instance, SystemUserInfor systemUser) {
//
//		InstanceCheckInfor tmpCheck = null;
//		List rLayers = this.purchaseManager.getCurrentProcessLayers(instance);
//		if (rLayers != null && rLayers.size() > 0) {
//			for (Iterator itLayer=rLayers.iterator();itLayer.hasNext();) {
//				InstanceLayerInfor layer = (InstanceLayerInfor)itLayer.next();
//				Set checks = layer.getCheckInfors();
//
//				for (Iterator itCheck=checks.iterator();itCheck.hasNext();) {
//					InstanceCheckInfor check = (InstanceCheckInfor)itCheck.next();
//
//					int checkerId = check.getChecker().getPersonId().intValue();
//					if (systemUser.getPersonId().intValue() == checkerId) {
//						tmpCheck = check;
//						break;
//					}
//				}
//			}
//		}
//		return tmpCheck;
//	}


	//设置选中分类的默认权限
	private void setCategoryDefaultRights(SystemUserInfor user, DocumentCategory category, int right) throws Exception {
		if (user.getUserType() != SystemUserInfor._Type_Admin) {
			DocumentCategoryRight categoryRight = this.documentCategoryRightManager.getRightsByCondition(user.getPersonId(), category.getCategoryId());
			if (categoryRight != null) {
				if (!this.documentCategoryRightManager.hasRight(categoryRight, right)) {
					categoryRight.setOperateRight(categoryRight.getOperateRight() + (1 << (right-1)));
					this.documentCategoryRightManager.save(categoryRight);
				}
			}else {
				DocumentCategoryRight tmpRight = new DocumentCategoryRight();
				tmpRight.setCategory(category);
				tmpRight.setUser(user);
				tmpRight.setOperateRight(1 << (right-1));
				this.documentCategoryRightManager.save(tmpRight);
			}
		}
	}

	//设对应文档的默认权限
	private void setDocumentDefaultRights(DocumentInfor document, SystemUserInfor user, int[] rights) throws Exception {
		if (user.getUserType() != SystemUserInfor._Type_Admin) {
			int right = 0;
			for (int i=0;i<rights.length;i++) {
				right += 1 << (rights[i]-1);
			}

			DocumentInforUserRight tmpRight = new DocumentInforUserRight();
			tmpRight.setDocument(document);
			tmpRight.setSystemUser(user);
			tmpRight.setOperateRight(right);
			this.documentInforRightManager.save(tmpRight);
		}
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
		if (rowId != null && rowId.length() > 0) {
			FlowInstanceInfor instanceInfor = (FlowInstanceInfor) this.purchaseManager.get(Integer.valueOf(rowId));

			/** 判断对该文档是否有编辑权限 */
			SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
//			boolean canEdit = this.instanceInforRightManager.hasRight(instanceInfor, systemUser, InstanceInforRight._Right_Edit);
//			if (!canEdit) {
//				request.setAttribute("_ErrorMessage", "对不起,您无权进行该操作,请与系统管理员联系!");
//				return "/common/error";
//			}

			//request.setAttribute("_CategoryId", instanceInfor.getCategory().getCategoryId());

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
			//int[] editUserIds = new int[users.size()];
			//int[] deleteUserIds = new int[users.size()];
			int[] viewUserIds = new int[users.size()];
			//int[] editRoleIds = new int[roles.size()];
			//int[] deleteRoleIds = new int[roles.size()];
			int[] viewRoleIds = new int[roles.size()];
			int rightType = 0;

			int k = 0;
			Set rights = instanceInfor.getRights();
			for (Iterator it = rights.iterator(); it.hasNext();) {
				InstanceInforRight right = (InstanceInforRight) it.next();
				if (right instanceof InstanceInforUserRight) {
					rightType = 1;
					/** 用户权限 */
					InstanceInforUserRight userRight = (InstanceInforUserRight) right;
					int userId = userRight.getSystemUser().getPersonId().intValue();

					/*// 编辑权限
					if (this.instanceInforRightManager.hasRight(right, InstanceInforRight._Right_Edit)) {
						editUserIds[k] = userId;
					}

					// 删除权限
					if (this.instanceInforRightManager.hasRight(right, InstanceInforRight._Right_Delete)) {
						deleteUserIds[k] = userId;
					}*/

					// 浏览权限
					if (this.instanceInforRightManager.hasRight(right, InstanceInforRight._Right_View)) {
						viewUserIds[k] = userId;
					}
				} else if (right instanceof InstanceInforRoleRight) {
					/** 角色权限 */
					InstanceInforRoleRight roleRight = (InstanceInforRoleRight) right;
					int roleId = roleRight.getRole().getRoleId().intValue();

					/*// 编辑权限
					if (this.instanceInforRightManager.hasRight(right, InstanceInforRight._Right_Edit)) {
						editRoleIds[k] = roleId;
					}

					// 删除权限
					if (this.instanceInforRightManager.hasRight(right, InstanceInforRight._Right_Delete)) {
						deleteRoleIds[k] = roleId;
					}*/

					// 浏览权限
					if (this.instanceInforRightManager.hasRight(right, InstanceInforRight._Right_View)) {
						viewRoleIds[k] = roleId;
					}
				}
				k += 1;
			}

			request.setAttribute("_InstanceId", rowId);
			/*request.setAttribute("_EditUserIds", editUserIds);
			request.setAttribute("_DeleteUserIds", deleteUserIds);*/
			request.setAttribute("_ViewUserIds", viewUserIds);
			/*request.setAttribute("_EditRoleIds", editRoleIds);
			request.setAttribute("_DeleteRoleIds", deleteRoleIds);*/
			request.setAttribute("_ViewRoleIds", viewRoleIds);
			request.setAttribute("_RightType", rightType);
		}
		return "editInstanceRight";
	}

	/**
	 * 保存信息权限
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "method=saveInforRight")
	public void saveInforRight(HttpServletRequest request, HttpServletResponse response) {

		String instanceId = request.getParameter("instanceId");
		String rightType = request.getParameter("rightType");
		FlowInstanceInfor instanceInfor = new FlowInstanceInfor();

		if (instanceId != null && instanceId.length() > 0) {
			instanceInfor = (FlowInstanceInfor) this.purchaseManager.get(Integer.valueOf(instanceId));

			Set rights = instanceInfor.getRights();
			instanceInfor.getRights().removeAll(rights);

			if (rightType.equals("0")) {
				/** 为角色权限时 */
				/*String[] editRoleIds = request.getParameterValues("editRoleIds");
				String[] deleteRoleIds = request.getParameterValues("deleteRoleIds");*/
				String[] viewRoleIds = request.getParameterValues("viewRoleIds");

				// 新增,修改
				/*if (editRoleIds != null && editRoleIds.length > 0) {
					for (int i = 0; i < editRoleIds.length; i++) {
						RoleInfor role = (RoleInfor) this.roleManager.get(Integer.valueOf(editRoleIds[i]));
						InstanceInforRoleRight roleRight = new InstanceInforRoleRight();
						roleRight.setInstance(instanceInfor);
						roleRight.setOperateRight(1);
						roleRight.setRole(role);
						instanceInfor.getRights().add(roleRight);
					}
				}
				// 删除
				if (deleteRoleIds != null && deleteRoleIds.length > 0) {
					Set oldRights = instanceInfor.getRights();
					for (int i = 0; i < deleteRoleIds.length; i++) {
						boolean has = false;
						for (Iterator it = oldRights.iterator(); it.hasNext();) {
							// 假如前面的权限操作中已包含该角色权限,则需要在此基础上进行修改
							InstanceInforRoleRight roleRight = (InstanceInforRoleRight) it.next();
							if (roleRight.getRole().getRoleId().intValue() == Integer.valueOf(deleteRoleIds[i])) {
								roleRight.setOperateRight(roleRight.getOperateRight() + 2);

								has = true;
								break;
							}
						}

						if (!has) {
							// 假如前面的权限操作中不包含该角色权限,则创建该权限
							RoleInfor role = (RoleInfor) this.roleManager.get(Integer.valueOf(deleteRoleIds[i]));
							InstanceInforRoleRight roleRight = new InstanceInforRoleRight();
							roleRight.setInstance(instanceInfor);
							roleRight.setOperateRight(2);
							roleRight.setRole(role);
							instanceInfor.getRights().add(roleRight);
						}
					}
				}*/
				// 浏览
				if (viewRoleIds != null && viewRoleIds.length > 0) {
					Set oldRights = instanceInfor.getRights();
					for (int i = 0; i < viewRoleIds.length; i++) {
						boolean has = false;
						for (Iterator it = oldRights.iterator(); it.hasNext();) {
							// 假如前面的权限操作中已包含该角色权限,则需要在此基础上进行修改
							InstanceInforRoleRight roleRight = (InstanceInforRoleRight) it.next();
							if (roleRight.getRole().getRoleId().intValue() == Integer.valueOf(viewRoleIds[i])) {
								roleRight.setOperateRight(roleRight.getOperateRight() + 4);

								has = true;
								break;
							}
						}

						if (!has) {
							// 假如前面的权限操作中不包含该角色权限,则创建该权限
							RoleInfor role = (RoleInfor) this.roleManager.get(Integer.valueOf(viewRoleIds[i]));
							InstanceInforRoleRight roleRight = new InstanceInforRoleRight();
							roleRight.setInstance(instanceInfor);
							roleRight.setOperateRight(4);
							roleRight.setRole(role);
							instanceInfor.getRights().add(roleRight);
						}
					}
				}
			} else {
				/** 为用户权限时 */
				/*String[] editUserIds = request.getParameterValues("editUserIds");
				String[] deleteUserIds = request.getParameterValues("deleteUserIds");*/
				String[] viewUserIds = request.getParameterValues("viewUserIds");

				// 新增,修改
				/*if (editUserIds != null && editUserIds.length > 0) {
					for (int i = 0; i < editUserIds.length; i++) {
						SystemUserInfor user = (SystemUserInfor) this.systemUserManager.get(Integer.valueOf(editUserIds[i]));
						InstanceInforUserRight userRight = new InstanceInforUserRight();
						userRight.setInstance(instanceInfor);
						userRight.setOperateRight(1);
						userRight.setSystemUser(user);
						instanceInfor.getRights().add(userRight);
					}
				}
				// 删除
				if (deleteUserIds != null && deleteUserIds.length > 0) {
					Set oldRights = instanceInfor.getRights();
					for (int i = 0; i < deleteUserIds.length; i++) {
						boolean has = false;
						for (Iterator it = oldRights.iterator(); it.hasNext();) {
							// 假如前面的权限操作中已包含该角色权限,则需要在此基础上进行修改
							InstanceInforUserRight userRight = (InstanceInforUserRight) it.next();
							if (userRight.getSystemUser().getPersonId().intValue() == Integer.valueOf(deleteUserIds[i])) {
								userRight.setOperateRight(userRight.getOperateRight() + 2);

								has = true;
								break;
							}
						}

						if (!has) {
							// 假如前面的权限操作中不包含该角色权限,则创建该权限
							SystemUserInfor user = (SystemUserInfor) this.systemUserManager.get(Integer.valueOf(deleteUserIds[i]));
							InstanceInforUserRight userRight = new InstanceInforUserRight();
							userRight.setInstance(instanceInfor);
							userRight.setOperateRight(2);
							userRight.setSystemUser(user);
							instanceInfor.getRights().add(userRight);
						}
					}
				}*/
				// 浏览
				if (viewUserIds != null && viewUserIds.length > 0) {
					Set oldRights = instanceInfor.getRights();
					for (int i = 0; i < viewUserIds.length; i++) {
						boolean has = false;
						for (Iterator it = oldRights.iterator(); it.hasNext();) {
							// 假如前面的权限操作中已包含该角色权限,则需要在此基础上进行修改
							InstanceInforUserRight userRight = (InstanceInforUserRight) it.next();
							if (userRight.getSystemUser().getPersonId().intValue() == Integer.valueOf(viewUserIds[i])) {
								userRight.setOperateRight(userRight.getOperateRight() + 4);

								has = true;
								break;
							}
						}

						if (!has) {
							// 假如前面的权限操作中不包含该角色权限,则创建该权限
							SystemUserInfor user = (SystemUserInfor) this.systemUserManager.get(Integer.valueOf(viewUserIds[i]));
							InstanceInforUserRight userRight = new InstanceInforUserRight();
							userRight.setInstance(instanceInfor);
							userRight.setOperateRight(4);
							userRight.setSystemUser(user);
							instanceInfor.getRights().add(userRight);
						}
					}
				}

			}
			this.purchaseManager.save(instanceInfor);
		}
	}




	/**
	 * 批量编辑信息权限
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(params = "method=editInforsRight")
	public String editInforsRight(HttpServletRequest request, HttpServletResponse response) {

		String rowIds = request.getParameter("rowIds");
		request.setAttribute("_InstanceIds", rowIds);
//
//		if (rowIds != null && rowIds.length() > 0) {
//			String[] editIds = rowIds.split(",");
//			if (editIds.length > 0) {
//				for (int i = 0; i < editIds.length; i++) {
//					int editId = Integer.valueOf(editIds[i]);
//					FlowInstanceInfor instance = (FlowInstanceInfor) this.purchaseManager.get(editId);
//
//					/** 判断对该文档是否有编辑权限 */
//					SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);

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

//					// 权限信息
//					int[] viewUserIds = new int[users.size()];
//					int[] viewRoleIds = new int[roles.size()];
//					int rightType = 0;
//
//					int k = 0;
//					Set rights = instance.getRights();
//					for (Iterator it = rights.iterator(); it.hasNext();) {
//						InstanceInforRight right = (InstanceInforRight) it.next();
//						if (right instanceof InstanceInforUserRight) {
//							rightType = 1;
//							/** 用户权限 */
//							InstanceInforUserRight userRight = (InstanceInforUserRight) right;
//							int userId = userRight.getSystemUser().getPersonId().intValue();
//
//							// 浏览权限
//							if (this.instanceInforRightManager.hasRight(right, InstanceInforRight._Right_View)) {
//								viewUserIds[k] = userId;
//							}
//						} else if (right instanceof InstanceInforRoleRight) {
//							/** 角色权限 */
//							InstanceInforRoleRight roleRight = (InstanceInforRoleRight) right;
//							int roleId = roleRight.getRole().getRoleId().intValue();
//
//							// 浏览权限
//							if (this.instanceInforRightManager.hasRight(right, InstanceInforRight._Right_View)) {
//								viewRoleIds[k] = roleId;
//							}
//						}
//						k += 1;
//					}

//					request.setAttribute("_InstanceId", editId);
//					request.setAttribute("_ViewUserIds", viewUserIds);
//					request.setAttribute("_ViewRoleIds", viewRoleIds);
//					request.setAttribute("_RightType", rightType);
//				}
//			}
//		}
		return "editInstancesRight";
	}

	/**
	 * 批量保存信息权限
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "method=saveInforsRight")
	public void saveInforsRight(HttpServletRequest request, HttpServletResponse response) {

		String instanceIds = request.getParameter("instanceIds");
		String rightType = request.getParameter("rightType");

		if (instanceIds != null &&instanceIds.length() > 0) {
			String[] editIds = instanceIds.split(",");
			if (editIds.length > 0) {
				for (int j = 0; j < editIds.length; j++) {
					int instanceId = Integer.valueOf(editIds[j]);
					FlowInstanceInfor instanceInfor = (FlowInstanceInfor) this.purchaseManager.get(instanceId);

					if (instanceId > 0) {
						instanceInfor = (FlowInstanceInfor) this.purchaseManager.get(Integer.valueOf(instanceId));

						//此处不做修改和删除，仅做追加
//						Set rights = instanceInfor.getRights();
//						instanceInfor.getRights().removeAll(rights);

						if (rightType.equals("0")) {
							/** 为角色权限时 */
							String[] viewRoleIds = request.getParameterValues("viewRoleIds");

							// 浏览
							if (viewRoleIds != null && viewRoleIds.length > 0) {
								Set oldRights = instanceInfor.getRights();
								for (int i = 0; i < viewRoleIds.length; i++) {
//									boolean has = false;
//									for (Iterator it = oldRights.iterator(); it.hasNext();) {
//										// 假如前面的权限操作中已包含该角色权限,则需要在此基础上进行修改
//										InstanceInforRoleRight roleRight = (InstanceInforRoleRight) it.next();
//										if (roleRight.getRole().getRoleId().intValue() == Integer.valueOf(viewRoleIds[i])) {
//											roleRight.setOperateRight(roleRight.getOperateRight() + 4);
//
//											has = true;
//											break;
//										}
//									}
//
//									if (!has) {
										// 假如前面的权限操作中不包含该角色权限,则创建该权限
										RoleInfor role = (RoleInfor) this.roleManager.get(Integer.valueOf(viewRoleIds[i]));
										InstanceInforRoleRight roleRight = new InstanceInforRoleRight();
										roleRight.setInstance(instanceInfor);
										roleRight.setOperateRight(4);
										roleRight.setRole(role);
										instanceInfor.getRights().add(roleRight);
//									}
								}
							}
						} else {
							/** 为用户权限时 */
							String[] viewUserIds = request.getParameterValues("viewUserIds");

							// 浏览
							if (viewUserIds != null && viewUserIds.length > 0) {
								Set oldRights = instanceInfor.getRights();
								for (int i = 0; i < viewUserIds.length; i++) {
//									boolean has = false;
//									for (Iterator it = oldRights.iterator(); it.hasNext();) {
//										// 假如前面的权限操作中已包含该角色权限,则需要在此基础上进行修改
//										InstanceInforUserRight userRight = (InstanceInforUserRight) it.next();
//										if (userRight.getSystemUser().getPersonId().intValue() == Integer.valueOf(viewUserIds[i])) {
//											userRight.setOperateRight(userRight.getOperateRight() + 4);
//
//											has = true;
//											break;
//										}
//									}
//
//									if (!has) {
										// 假如前面的权限操作中不包含该角色权限,则创建该权限
										SystemUserInfor user = (SystemUserInfor) this.systemUserManager.get(Integer.valueOf(viewUserIds[i]));
										InstanceInforUserRight userRight = new InstanceInforUserRight();
										userRight.setInstance(instanceInfor);
										userRight.setOperateRight(4);
										userRight.setSystemUser(user);
										instanceInfor.getRights().add(userRight);
//									}
								}
							}

						}
						this.purchaseManager.save(instanceInfor);
					}
				}
			}
		}

//		FlowInstanceInfor instanceInfor = new FlowInstanceInfor();


	}
	//获取审核实例当前具有浏览权限的人员
	@RequestMapping(params="method=getViewUsers")
	public void getViewUsers(HttpServletRequest request, HttpServletResponse response) throws Exception {

		JSONObject jsonObj = new JSONObject();
		JSONConvert convert = new JSONConvert();

		//具有浏览权限的人员信息
		String instanceIdStr  = request.getParameter("instanceId");
		if(!instanceIdStr.equals("") && instanceIdStr.length() > 0){
			int instanceId = Integer.valueOf(instanceIdStr);
			FlowInstanceInfor instance = (FlowInstanceInfor)this.purchaseManager.get(instanceId);

			List users = this.purchaseManager.getViewUsers(instance);
			users = ArrayUtil.removeDuplicateWithOrder(users);

			JSONArray userArray = new JSONArray();
			userArray = convert.modelCollect2JSONArray(users, new ArrayList());
			jsonObj.put("_ViewUsers", userArray);

			//设置字符编码
	        response.setContentType(CoreConstant.CONTENT_TYPE);
	        response.getWriter().print(jsonObj);
		}

	}



	/**
	 * 提交部门审核(移动端)
	 * @param vo
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=saveDepCheck_m")
	public void saveDepCheck_m(FlowInstanceInforVo vo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject jsonObj = new JSONObject();

		Integer instanceId = vo.getInstanceId();
		String checkWord = request.getParameter("checkWord");
		Timestamp checkTime = new Timestamp(System.currentTimeMillis());
		try {
			if (instanceId != null && instanceId.intValue() > 0) {
				FlowInstanceInfor instance = (FlowInstanceInfor)this.purchaseManager.get(instanceId);
				SystemUserInfor manager = instance.getManager();
				SystemUserInfor viceManager = instance.getViceManager();

				SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
				if (manager != null && manager.getPersonId().intValue() == systemUser.getPersonId().intValue()) {
					//审核人一
					instance.setManagerWord(checkWord);
					instance.setCheckTime(checkTime);
					instance.setManagerChecked(true);

					instance.setManagerAttachment("");
				}else if (viceManager != null && viceManager.getPersonId().intValue() == systemUser.getPersonId().intValue()) {
					//审核人二
					instance.setViceManagerWord(checkWord);
					instance.setViceCheckTime(checkTime);
					instance.setViceManagerChecked(true);

					instance.setViceManagerAttachment("");
				}

				this.purchaseManager.save(instance);

				//是否成功标志
				jsonObj.put("_RtnTag", 1);
			}
		} catch (Exception e) {
			jsonObj.put("_RtnTag", 0);
			e.printStackTrace();
		}

		//设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);
	}

	/**
	 * 查看审核时间统计
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=viewCount")
	public String viewCount(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try{
			String instanceId = request.getParameter("instanceId");
			if (instanceId != null && instanceId.length() > 0) {
				FlowInstanceInfor instance = (FlowInstanceInfor)this.purchaseManager.get(Integer.valueOf(instanceId));
				InstanceCountVo countVo = new InstanceCountVo();

				SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
				int userId = systemUser.getPersonId().intValue();
				FlowDefinition flow = instance.getFlowDefinition();

				countVo.setInstanceId(instance.getInstanceId());
				countVo.setInstanceTitle(instance.getInstanceTitle());
				countVo.setCategoryId(flow.getFlowId());
				countVo.setCategoryName(flow.getFlowName());
				
				//得到审核层
				List<InstanceCountLayerVo> layerVos = new ArrayList<InstanceCountLayerVo>();
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Set<InstanceLayerInfor> layerSet = instance.getLayers();
				for(InstanceLayerInfor layerInfor : layerSet){
					InstanceCountLayerVo layerVo = new InstanceCountLayerVo();
					layerVo.setLayerId(layerInfor.getLayerId());
					layerVo.setLayerName(layerInfor.getLayerName());
					
					String statusName = "";
					String beginTime = "";
					String endTime = "";
					String duration = "";
					
					beginTime = sf.format(layerInfor.getStartTime());
					if(layerInfor.getStatus() == InstanceLayerInfor.Layer_Status_Normal){
						statusName = "审核中";
						endTime = "<font color=red>审核尚未结束</font>";
						long times = new Timestamp(System.currentTimeMillis()).getTime() - layerInfor.getStartTime().getTime();
						
						duration = formatTime(times);
					}else if(layerInfor.getStatus() == InstanceLayerInfor.Layer_Status_Finished){
						statusName = "审核完成";
						endTime = sf.format(layerInfor.getEndTime());
						long times = layerInfor.getEndTime().getTime() - layerInfor.getStartTime().getTime();
						
						duration = formatTime(times);
					}
					layerVo.setStatus(statusName);
					layerVo.setBeginTime(beginTime);
					layerVo.setEndTime(endTime);
					layerVo.setDuration(duration);
					
					layerVos.add(layerVo);
				}
				
				countVo.setLayers(layerVos);
				
				request.setAttribute("_Instance", countVo);
			}	
		}catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return "viewCount";
	}

	/**
	 * 查询某段时间内，某审核层的平均审核时间
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params="method=getDuration")
	public void getDuration(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
//		JSONObject jsonObj = new JSONObject();
//		JSONConvert convert = new JSONConvert();
		
		String duration = "";
		
		//参数信息
		String beginCheckTimeStr = request.getParameter("beginCheckTime");
		String endCheckTimeStr = request.getParameter("endCheckTime");
		String layerName = request.getParameter("layerName");
		String categoryIdStr = request.getParameter("categoryId");
		
		if(StringUtil.isNotEmpty(beginCheckTimeStr) && StringUtil.isNotEmpty(endCheckTimeStr) && StringUtil.isNotEmpty(layerName) && StringUtil.isNotEmpty(categoryIdStr)){
			String queryString = "from InstanceLayerInfor layer where layer.startTime >= '" + beginCheckTimeStr + " 00:00:00' " +
					" and layer.endTime != null and layer.endTime != '' and layer.endTime <= '" + endCheckTimeStr + " 23:59:59' " +
					" and layer.layerName = '" + layerName + "' and layer.instance.flowDefinition.flowId = " + Integer.valueOf(categoryIdStr);
			
			List<InstanceLayerInfor> layerList = this.purchaseLayerInforManager.getResultByQueryString(queryString);
			long allTimes = 0;
			for(InstanceLayerInfor tmpLayerInfor : layerList){
				long times = tmpLayerInfor.getEndTime().getTime() - tmpLayerInfor.getStartTime().getTime();
				allTimes += times;
			}
			duration = formatTime(allTimes / layerList.size());
		}else {
			duration = "<font color=red>参数不完整！</font>";
		}
		
		
//		jsonObj.put("_AverageDuration", duration);
		
		//部门经理
//			OrganizeInfor department = (OrganizeInfor)this.organizeManager.get(Integer.valueOf(departmentIdStr));
//			jsonObj.put("_Director", department.getDirector());
		
		//设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(duration);		
	}

	public static String formatTime(long ms) {

		int ss = 1000;
		int mi = ss * 60;
		int hh = mi * 60;
		int dd = hh * 24;

		long day = ms / dd;
		long hour = (ms - day * dd) / hh;
		long minute = (ms - day * dd - hour * hh) / mi;
		long second = (ms - day * dd - hour * hh - minute * mi) / ss;
		long milliSecond = ms - day * dd - hour * hh - minute * mi - second
				* ss;

		String strDay = day < 10 ? "0" + day : "" + day; // 天
		String strHour = hour < 10 ? "0" + hour : "" + hour;// 小时
		String strMinute = minute < 10 ? "0" + minute : "" + minute;// 分钟
		String strSecond = second < 10 ? "0" + second : "" + second;// 秒
		String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : ""
				+ milliSecond;// 毫秒
		strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : ""
				+ strMilliSecond;

		return strDay + " 天 " + strHour + " 时 " + strMinute + " 分钟  " + strSecond + " 秒";
	}

	/**
	 * 批量提交
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "method=updates")
	public void updates(HttpServletRequest request, HttpServletResponse response) {

		Set<PurchaseInfor> purchaseInfors = new HashSet<PurchaseInfor>();

		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		RoleInfor guikouf = (RoleInfor) roleManager.get(PurchaseCheckInfor.Check_Role_Guikou);
		RoleInfor caigouf = (RoleInfor) roleManager.get(PurchaseCheckInfor.Check_Role_Caigou);
		RoleInfor gongsilingdao = (RoleInfor) roleManager.get(PurchaseCheckInfor.Check_Role_Gongsilingdao);
		OrganizeInfor org = (OrganizeInfor) this.organizeManager.get(PurchaseCheckInfor.Check_Org_Caigou);
		int caigoulingdao = org.getDirector().getPersonId();
		RoleInfor caiwu = (RoleInfor) roleManager.get(PurchaseCheckInfor.Check_Role_Caiwu);
		PurchaseNode node = new PurchaseNode();

		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		String rowIds = request.getParameter("rowIds");
		if (rowIds != null && rowIds.length() > 0) {
			String[] updateIds = rowIds.split(",");
			if (updateIds.length > 0) {
				for (int j = 0; j < updateIds.length; j++) {
					int purchaseId = Integer.valueOf(updateIds[j]);
					PurchaseInfor purchase = (PurchaseInfor) this.purchaseManager.get(purchaseId);
					List rLayers = this.purchaseManager.getCurrentProcessLayers(purchase);
					PurchaseLayerInfor layer = (PurchaseLayerInfor) rLayers.get(0);
					if (layer.getStatus() == 1) {
						Set checks = layer.getCheckInfors();
						//结束本层
						for (Iterator itCheck = checks.iterator(); itCheck.hasNext(); ) {
							PurchaseCheckInfor check = (PurchaseCheckInfor) itCheck.next();
							if (check.getStatus() == 0) {

								Timestamp checkDate = null;
								check.setChecker(systemUser);
								check.setStatus(1);
								this.purchaseCheckInforManager.saveCheckInfor(check, checkDate);
								int pstatus = purchase.getPurchaseStatus();
								pstatus = pstatus + 1;
								purchase.setPurchaseStatus(pstatus);
								this.purchaseManager.save(purchase);
							}
						}
						//审核新层
						int currentLayer = layer.getLayer();
						int newcurrentLayer = currentLayer + 1;
						int layerNode = layer.getPurchaseNode().getNodeId();
						int newlayerNode = layerNode + 1;
						String hql = "from PurchaseNode node where flowId = 1 and nodeId =" + newlayerNode;
						node = (PurchaseNode) this.purchaseLayerInforManager.getResultByQueryString(hql).get(0);
						String nodeName = node.getNodeName();
						PurchaseLayerInfor newLayer = new PurchaseLayerInfor();
						newLayer.setLayer(newcurrentLayer);
						newLayer.setStatus(1);
						newLayer.setStartTime(currentTime);
						newLayer.setPurchase(purchase);
						newLayer.setPurchaseNode(node);
						newLayer.setLayerName(nodeName);
						PurchaseLayerInfor newpurchaseLayerInfor = (PurchaseLayerInfor) this.purchaseLayerDAO.save(newLayer);

						//审核新信息
						int status = 0;
						int checkerType = node.getCheckerType();
						PurchaseCheckInfor purchaseCheckInfor = new PurchaseCheckInfor();
						SystemUserInfor user = new SystemUserInfor();
						if (checkerType == 0) {
							if (layer.getLayerName().equals("归口负责人提交") && purchase.getGuikouDepartment().getOrganizeId() != 68) {
								user = purchase.getGuikouDepartment().getDirector().getUser();
								purchaseCheckInfor.setStartDate(currentTime);
								purchaseCheckInfor.setStatus(status);
								purchaseCheckInfor.setLayerInfor(newpurchaseLayerInfor);
								purchaseCheckInfor.setChecker(user);
								this.purchaseCheckDAO.save(purchaseCheckInfor);
							} else if (layer.getLayerName().equals("归口负责人提交") && purchase.getGuikouDepartment().getOrganizeId() == 68) {

								String jiguilingdao = purchase.getJigui();
								String[] jigui = jiguilingdao.split(",");
								for (int x = 0; x < jigui.length; x++) {
									int y = Integer.valueOf(jigui[x]);
									PersonInfor userInfor = (PersonInfor) this.personManager.get(y);
									SystemUserInfor u = userInfor.getUser();
									purchaseCheckInfor.setStartDate(currentTime);
									purchaseCheckInfor.setStatus(status);
									purchaseCheckInfor.setLayerInfor(newpurchaseLayerInfor);
									purchaseCheckInfor.setChecker(u);
									this.purchaseCheckDAO.save(purchaseCheckInfor);
								}
							} else {
								user = node.getUser();
								purchaseCheckInfor.setStartDate(currentTime);
								purchaseCheckInfor.setStatus(status);
								purchaseCheckInfor.setLayerInfor(newpurchaseLayerInfor);
								purchaseCheckInfor.setChecker(user);
								this.purchaseCheckDAO.save(purchaseCheckInfor);
							}

						} else {
							RoleInfor role = node.getRoleId();
							purchaseCheckInfor.setStartDate(currentTime);
							purchaseCheckInfor.setStatus(status);
							purchaseCheckInfor.setLayerInfor(newpurchaseLayerInfor);
							purchaseCheckInfor.setCheckRoler(role);
							this.purchaseCheckDAO.save(purchaseCheckInfor);
						}
						purchaseInfors.add(purchase);
					}
				}
				//新包
				String packageName = request.getParameter("packageName");
				if (roleManager.belongRole(systemUser, caigouf)){
					PurchasePackage purchasePackage = new PurchasePackage();
					purchasePackage.setStatus(0);
					purchasePackage.setCheckerType(0);
					purchasePackage.setManager(node.getUser());
					purchasePackage.setFlowId(1);
					purchasePackage.setPurchaseInfors(purchaseInfors);
					purchasePackage.setStartDate(currentTime);
//					String strn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentTime);
//					purchasePackage.setPackageName(strn +"   采购计划");
					purchasePackage.setPackageName(packageName);
					this.purchasePackageDao.save(purchasePackage);
				}else if(roleManager.belongRole(systemUser, guikouf)){
					if (systemUser.getPerson().getDepartment().getOrganizeId() == 68){
					    PurchaseInfor purchase1 = purchaseInfors.iterator().next();
						String jiguilingdao = purchase1.getJigui();
						String[] jigui = jiguilingdao.split(",");
						for (int x = 0; x < jigui.length; x++) {
							int y = Integer.valueOf(jigui[x]);
							PersonInfor userInfor = (PersonInfor) this.personManager.get(y);
							SystemUserInfor u = userInfor.getUser();
							PurchasePackage purchasePackage = new PurchasePackage();
							purchasePackage.setStatus(0);
							purchasePackage.setCheckerType(0);
							purchasePackage.setManager(u);
							purchasePackage.setFlowId(1);
							purchasePackage.setPurchaseInfors(purchaseInfors);
							purchasePackage.setStartDate(currentTime);
//							String strn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentTime);
//							purchasePackage.setPackageName(strn +"   采购计划");
							purchasePackage.setPackageName(packageName);
							this.purchasePackageDao.save(purchasePackage);
						}
					}else{
						PurchasePackage purchasePackage = new PurchasePackage();
						purchasePackage.setStatus(0);
						purchasePackage.setCheckerType(0);
						OrganizeInfor organizeInfor = systemUser.getPerson().getDepartment();
						PersonInfor dir = organizeInfor.getDirector();
						SystemUserInfor s = dir.getUser();
						purchasePackage.setManager(s);
						purchasePackage.setFlowId(1);
						purchasePackage.setPurchaseInfors(purchaseInfors);
						purchasePackage.setStartDate(currentTime);
//						String strn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentTime);
//						purchasePackage.setPackageName(strn +"   采购计划");
						purchasePackage.setPackageName(packageName);
						this.purchasePackageDao.save(purchasePackage);
					}
				}else if (systemUser.getPersonId() == caigoulingdao){
					Set<SystemUserInfor> users = caiwu.getUsers();
					for (Iterator it = users.iterator();it.hasNext();){
						PurchasePackage purchasePackage = new PurchasePackage();
						SystemUserInfor user = (SystemUserInfor)it.next();
						purchasePackage.setStatus(0);
						purchasePackage.setCheckerType(0);
						purchasePackage.setManager(user);
						purchasePackage.setFlowId(1);
						purchasePackage.setPurchaseInfors(purchaseInfors);
						purchasePackage.setStartDate(currentTime);
//						String strn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentTime);
//						purchasePackage.setPackageName(strn +"   采购计划");
						purchasePackage.setPackageName(packageName);
						this.purchasePackageDao.save(purchasePackage);
					}
				}else if(roleManager.belongRole(systemUser, caiwu)){
					Set<SystemUserInfor> users = gongsilingdao.getUsers();
					for (Iterator it = users.iterator();it.hasNext();){
						PurchasePackage purchasePackage = new PurchasePackage();
						SystemUserInfor user = (SystemUserInfor)it.next();
						purchasePackage.setStatus(0);
						purchasePackage.setCheckerType(0);
						purchasePackage.setManager(user);
						purchasePackage.setFlowId(1);
						purchasePackage.setPurchaseInfors(purchaseInfors);
						purchasePackage.setStartDate(currentTime);
//						String strn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentTime);
//						purchasePackage.setPackageName(strn +"   采购计划");
						purchasePackage.setPackageName(packageName);
						this.purchasePackageDao.save(purchasePackage);
					}
				}
			}
		}
	}

	/**
	 * 包内批量提交
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "method=updatesPackage")
	public void updatesPackage(HttpServletRequest request, HttpServletResponse response) {

		RoleInfor gongsilingdao = (RoleInfor) roleManager.get(PurchaseCheckInfor.Check_Role_Gongsilingdao);
		String packageId2 = request.getParameter("packageId");
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		int personId = systemUser.getPersonId();
		PurchasePackage purchasePackage2 = (PurchasePackage)this.packageManager.get(Integer.parseInt(packageId2));
		int checkerType2 =  purchasePackage2.getCheckerType();
		RoleInfor caiwu = (RoleInfor) roleManager.get(PurchaseCheckInfor.Check_Role_Caiwu);
//		DefaultMultipartHttpServletRequest multipartRequest = new DefaultMultipartHttpServletRequest(null);
		if (checkerType2 == 2){
			try {
				saveDepChecks(request,response,new PurchaseInforVo(),null);
				turnPackageStatus(systemUser, packageId2);
			}catch (Exception e){
				// TODO: handle exception
				e.printStackTrace();
			}
		}else {
			Set<PurchaseInfor> purchaseInfors = new HashSet<PurchaseInfor>();
			String packageId = request.getParameter("packageId");
			Timestamp currentTime = new Timestamp(System.currentTimeMillis());
			String rowIds = request.getParameter("rowIds");
			if (rowIds != null && rowIds.length() > 0) {
				String[] updateIds = rowIds.split(",");
				if (updateIds.length > 0) {
					for (int j = 0; j < updateIds.length; j++) {
						int purchaseId = Integer.valueOf(updateIds[j]);
						PurchaseInfor purchase = (PurchaseInfor) this.purchaseManager.get(purchaseId);
						List rLayers = this.purchaseManager.getCurrentProcessLayers(purchase);
						PurchaseLayerInfor layer = (PurchaseLayerInfor) rLayers.get(0);
						if (layer.getStatus() == 1) {
							Set checks = layer.getCheckInfors();
							boolean hasAllchecked = false;
							//结束本层
							for (Iterator itCheck = checks.iterator(); itCheck.hasNext(); ) {
								PurchaseCheckInfor check = (PurchaseCheckInfor) itCheck.next();
								//人员审批
								if (check.getChecker() != null) {
									if (check.getStatus() == 0 && check.getChecker().getPersonId() == personId) {
										Timestamp checkDate = null;
										check.setChecker(systemUser);
										check.setStatus(1);
										this.purchaseCheckInforManager.saveCheckInfor(check, checkDate);
										hasAllchecked = this.purchaseManager.hasAllchecked(purchase, check);
										if (hasAllchecked) {
											int pstatus = purchase.getPurchaseStatus();
											if (purchase.getPurchaseWay() == 0 && purchase.getPurchaseStatus() == 8) {
												pstatus = pstatus + 2;
											} else {
												pstatus = pstatus + 1;
											}
											purchase.setPurchaseStatus(pstatus);
											this.purchaseManager.save(purchase);
											break;
										}
									}
									//角色审批
								}else{
									RoleInfor juese = (RoleInfor) roleManager.get(check.getCheckRoler().getRoleId());
									if (check.getStatus() == 0 && roleManager.belongRole(systemUser,juese)) {
										Timestamp checkDate = null;
										check.setChecker(systemUser);
										check.setStatus(1);
										this.purchaseCheckInforManager.saveCheckInfor(check, checkDate);
										hasAllchecked = this.purchaseManager.hasAllchecked(purchase, check);
										if (hasAllchecked) {
											int pstatus = purchase.getPurchaseStatus();
											if (purchase.getPurchaseWay() == 0 && purchase.getPurchaseStatus() == 8) {
												pstatus = pstatus + 2;
											} else {
												pstatus = pstatus + 1;
											}
											purchase.setPurchaseStatus(pstatus);
											this.purchaseManager.save(purchase);
										}
									}
								}
							}
							if (hasAllchecked) {
								//审核新层
								int currentLayer = layer.getLayer();
								int newcurrentLayer = currentLayer + 1;
								int layerNode = layer.getPurchaseNode().getNodeId();
								int newlayerNode = layerNode + 1;
								String hql = "from PurchaseNode node where nodeId =" + newlayerNode;
								PurchaseNode node = (PurchaseNode) this.purchaseLayerInforManager.getResultByQueryString(hql).get(0);
								String nodeName = node.getNodeName();
								PurchaseLayerInfor newLayer = new PurchaseLayerInfor();
								newLayer.setLayer(newcurrentLayer);
								newLayer.setStatus(1);
								newLayer.setStartTime(currentTime);
								newLayer.setPurchase(purchase);
								newLayer.setPurchaseNode(node);
								newLayer.setLayerName(nodeName);
								PurchaseLayerInfor newpurchaseLayerInfor = (PurchaseLayerInfor) this.purchaseLayerDAO.save(newLayer);

								//审核新信息
								int status = 0;
								int checkerType = node.getCheckerType();
								PurchaseCheckInfor purchaseCheckInfor = new PurchaseCheckInfor();
								if (checkerType == 0) {
									SystemUserInfor user = node.getUser();
									purchaseCheckInfor.setStartDate(currentTime);
									purchaseCheckInfor.setStatus(status);
									purchaseCheckInfor.setLayerInfor(newpurchaseLayerInfor);
									purchaseCheckInfor.setChecker(user);
									this.purchaseCheckDAO.save(purchaseCheckInfor);
								} else {
									RoleInfor role = node.getRoleId();
									Set users = role.getUsers();
									if (layer.getLayerName().equals("财务预算审核") ||layer.getLayerName().equals("采购部领导审批") ) {
										for (Iterator it = users.iterator(); it.hasNext(); ) {
											SystemUserInfor user = (SystemUserInfor) it.next();
											purchaseCheckInfor.setStartDate(currentTime);
											purchaseCheckInfor.setStatus(status);
											purchaseCheckInfor.setLayerInfor(newpurchaseLayerInfor);
											purchaseCheckInfor.setChecker(user);
											this.purchaseCheckDAO.save(purchaseCheckInfor);
										}
									} else {
										purchaseCheckInfor.setStartDate(currentTime);
										purchaseCheckInfor.setStatus(status);
										purchaseCheckInfor.setLayerInfor(newpurchaseLayerInfor);
										purchaseCheckInfor.setCheckRoler(role);
										this.purchaseCheckDAO.save(purchaseCheckInfor);
									}
								}
								purchaseInfors.add(purchase);
							}
						}
						//改变包状态
						if (packageId == null || packageId.equals("")){
						}else{
							List myList = new ArrayList();
							Set<PurchasePackage> packages = purchase.getPackages();
							for (Iterator it = packages.iterator();it.hasNext();) {
								PurchasePackage pg = (PurchasePackage)it.next();
								int pgId = pg.getPackageId();
								if ((pg.getStatus() == 0 || pg.getStatus() == 1) && pg.getManager().getPersonId().intValue() == systemUser.getPersonId()) {
									myList.add(String.valueOf(pgId));
								}
							}
							for (Iterator it = myList.iterator();it.hasNext();) {
								String pgId = (String)it.next();
								turnPackageStatus(systemUser, String.valueOf(pgId));
							}
						}

					}
					//新包
					OrganizeInfor org = (OrganizeInfor) this.organizeManager.get(PurchaseCheckInfor.Check_Org_Caigou);
					int caigoulingdao = org.getDirector().getPersonId();
					if (systemUser.getPersonId() == caigoulingdao) {
						Set<SystemUserInfor> users = caiwu.getUsers();
						for (Iterator it = users.iterator();it.hasNext();){
							PurchasePackage purchasePackage = new PurchasePackage();
							SystemUserInfor user = (SystemUserInfor)it.next();
							purchasePackage.setStatus(0);
							purchasePackage.setCheckerType(0);
							purchasePackage.setManager(user);
							purchasePackage.setFlowId(1);
							purchasePackage.setPurchaseInfors(purchaseInfors);
							purchasePackage.setStartDate(currentTime);
//							String strn = new SimpleDateFormat("yyyy-MM-dd ").format(currentTime);
//							purchasePackage.setPackageName(strn +"   采购计划");
							purchasePackage.setPackageName(purchasePackage2.getPackageName());
							this.purchasePackageDao.save(purchasePackage);
						}
					}else if(roleManager.belongRole(systemUser, caiwu) && purchaseInfors.size() != 0){
						Set<SystemUserInfor> users = gongsilingdao.getUsers();
						for (Iterator it = users.iterator();it.hasNext();){
							PurchasePackage purchasePackage = new PurchasePackage();
							SystemUserInfor user = (SystemUserInfor)it.next();
							purchasePackage.setStatus(0);
							purchasePackage.setCheckerType(0);
							purchasePackage.setManager(user);
							purchasePackage.setFlowId(1);
							purchasePackage.setPurchaseInfors(purchaseInfors);
							purchasePackage.setStartDate(currentTime);
//							String strn = new SimpleDateFormat("yyyy-MM-dd").format(currentTime);
//							purchasePackage.setPackageName(strn +"   采购计划");
							purchasePackage.setPackageName(purchasePackage2.getPackageName());
							this.purchasePackageDao.save(purchasePackage);
						}
					}
				}
			}
			turnPackageStatus(systemUser, packageId);
		}

	}

	@RequestMapping(params = "method=disagreePackage")
	public void disagreePackage(HttpServletRequest request, HttpServletResponse response) {


		String packageId2 = request.getParameter("packageId");
		PurchasePackage purchasePackage2 = (PurchasePackage)this.packageManager.get(Integer.parseInt(packageId2));
		int checkerType2 =  purchasePackage2.getCheckerType();
//		DefaultMultipartHttpServletRequest multipartRequest = new DefaultMultipartHttpServletRequest(null);
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);;
		if (checkerType2 == 2){
			String rowIds = request.getParameter("rowIds");

			if (rowIds != null &&rowIds.length() > 0) {
				String[] updateIds = rowIds.split(",");
				if (updateIds.length > 0) {
					for (int j = 0; j < updateIds.length; j++) {
						int purchaseId = Integer.valueOf(updateIds[j]);
						PurchaseInfor purchase = (PurchaseInfor) this.purchaseManager.get(purchaseId);


							String checkWord = "";
							int managerCheckStatus = 2;
							Timestamp checkTime = new Timestamp(System.currentTimeMillis());
							SystemUserInfor manager = purchase.getManager();
							SystemUserInfor viceManager = purchase.getViceManager();
							if (manager != null && manager.getPersonId().intValue() == systemUser.getPersonId().intValue()) {
								//审核人一
								purchase.setManagerWord(checkWord);
								purchase.setCheckTime(checkTime);
//				purchase.setLastNode(1);
								purchase.setManagerChecked(true);
								purchase.setManagerCheckStatus(managerCheckStatus);
								if (managerCheckStatus == 2) {
									purchase.setPurchaseStatus(PurchaseInfor.Purchase_Status_No);
								}

							} else if (viceManager != null && viceManager.getPersonId().intValue() == systemUser.getPersonId().intValue()) {
								//审核人二
								purchase.setViceManagerWord(checkWord);
								purchase.setViceManagercheckTime(checkTime);
								purchase.setViceManagerChecked(true);
								purchase.setViceManagerCheckStatus(managerCheckStatus);
								if (managerCheckStatus == 2) {
									purchase.setPurchaseStatus(PurchaseInfor.Purchase_Status_No);
								}


							}
							if (purchase.getManagerCheckStatus() == 1 && (purchase.getViceManagerCheckStatus() == 1 || viceManager == null)) {
								purchase.setPurchaseStatus(1);
							}
							purchase = (PurchaseInfor) this.purchaseManager.save(purchase);

							if (purchase.getManagerCheckStatus() == 1 && (purchase.getViceManagerCheckStatus() == 1 || viceManager == null)) {
								this.purchaseManager.startNode(request, response, purchase);
							}

					}
				}
			}
			turnPackageStatus(systemUser, packageId2);
		}else {

			Set<PurchaseInfor> purchaseInfors = new HashSet<PurchaseInfor>();
			String packageId = request.getParameter("packageId");
//		PurchasePackage purchasePackage = this.purchasePackageDao.get(Integer.parseInt(packageId));
			PurchasePackage purchasePackage = new PurchasePackage();
//		boolean hasAllCheck = true;
			Timestamp currentTime = new Timestamp(System.currentTimeMillis());
			String rowIds = request.getParameter("rowIds");
			if (rowIds != null && rowIds.length() > 0) {
				String[] updateIds = rowIds.split(",");
				if (updateIds.length > 0) {
					for (int j = 0; j < updateIds.length; j++) {
						int purchaseId = Integer.valueOf(updateIds[j]);
						PurchaseInfor purchase = (PurchaseInfor) this.purchaseManager.get(purchaseId);

						boolean candisagree =true;
						if (purchase.getPurchaseStatus() == 6 ||purchase.getPurchaseStatus() == 7 ){
							candisagree = false;
						}
						if (candisagree) {
							List rLayers = this.purchaseManager.getCurrentProcessLayers(purchase);
							PurchaseLayerInfor layer = (PurchaseLayerInfor) rLayers.get(0);
							if (layer.getStatus() == 1) {
								Set checks = layer.getCheckInfors();
								//结束本层
								for (Iterator itCheck = checks.iterator(); itCheck.hasNext(); ) {
									PurchaseCheckInfor check = (PurchaseCheckInfor) itCheck.next();
									if (check.getStatus() == 0) {
										Timestamp checkDate = null;
										if (check.getChecker().getPersonId().intValue() == systemUser.getPersonId()) {
											check.setStatus(2);
											check.setChecker(systemUser);
										} else {
											check.setStatus(4);
										}
										this.purchaseCheckInforManager.saveCheckInfor(check, checkDate);
									}
								}
								int pstatus = PurchaseInfor.Purchase_Status_No;
								purchase.setPurchaseStatus(pstatus);
								this.purchaseManager.save(purchase);

								//改变包状态
								if (packageId == null || packageId.equals("")) {
								} else {
									List myList = new ArrayList();
									Set<PurchasePackage> packages = purchase.getPackages();
									for (Iterator it = packages.iterator(); it.hasNext(); ) {
										PurchasePackage pg = (PurchasePackage) it.next();
										int pgId = pg.getPackageId();
										if ((pg.getStatus() == 0 || pg.getStatus() == 1) && pg.getManager().getPersonId().intValue() == systemUser.getPersonId()) {
											myList.add(String.valueOf(pgId));
										}
									}
									for (Iterator it = myList.iterator(); it.hasNext(); ) {
										String pgId = (String) it.next();
										turnPackageStatus(systemUser, String.valueOf(pgId));
									}
								}
							}
						}
					}
				}
			}
//			turnPackageStatus(systemUser, packageId);
		}
	}

	@RequestMapping(params = "method=backPackage")
	public void backPackage(HttpServletRequest request, HttpServletResponse response) {

		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		Set<PurchaseInfor> purchaseInfors = new HashSet<PurchaseInfor>();
		String packageId = request.getParameter("packageId");
//		PurchasePackage purchasePackage = this.purchasePackageDao.get(Integer.parseInt(packageId));
		PurchasePackage purchasePackage = new PurchasePackage();
//		boolean hasAllCheck = true;
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		String rowIds = request.getParameter("rowIds");
		if (rowIds != null &&rowIds.length() > 0) {
			String[] updateIds = rowIds.split(",");
			if (updateIds.length > 0) {
				for (int j = 0; j < updateIds.length; j++) {
					int purchaseId = Integer.valueOf(updateIds[j]);
					PurchaseInfor purchase = (PurchaseInfor) this.purchaseManager.get(purchaseId);

					boolean canback =true;
					if (purchase.getPurchaseStatus() == 0 ||purchase.getPurchaseStatus() == 8 ){
						canback = false;
					}
					if (canback) {
						List rLayers = this.purchaseManager.getCurrentProcessLayers(purchase);
						PurchaseLayerInfor layer = (PurchaseLayerInfor)rLayers.get(0);
						if (layer.getStatus() == 1) {
							Set checks = layer.getCheckInfors();
							//结束本层
							for (Iterator itCheck = checks.iterator(); itCheck.hasNext(); ) {
								PurchaseCheckInfor check = (PurchaseCheckInfor) itCheck.next();
								if (check.getStatus() == 0) {
									Timestamp checkDate = null;
									if (check.getChecker().getPersonId().intValue() == systemUser.getPersonId()) {
										check.setStatus(3);
										check.setChecker(systemUser);
									} else {
										check.setStatus(4);
									}

									this.purchaseCheckInforManager.saveCheckInfor(check, checkDate);
								}
							}
							RoleInfor caiwu = (RoleInfor) roleManager.get(PurchaseCheckInfor.Check_Role_Caiwu);
							RoleInfor jigui = (RoleInfor) roleManager.get(PurchaseCheckInfor.Check_Role_Jiguibulingdao);
	//				SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
							OrganizeInfor org = (OrganizeInfor) this.organizeManager.get(PurchaseCheckInfor.Check_Org_Caigou);
	//						OrganizeInfor organizeInfor = purchase.getGuikouDepartment();
							int caigoulingdao = org.getDirector().getPersonId();
							if (systemUser.getPersonId() == caigoulingdao) {
								int pstatus = 4;
								purchase.setPurchaseStatus(pstatus);
								this.purchaseManager.save(purchase);
								purchaseId = purchase.getPurchaseId();
							} else if (systemUser.getPersonId().intValue() == purchase.getGuikouDepartment().getDirector().getPersonId()) {
								int pstatus = 1;
								purchase.setPurchaseStatus(pstatus);
								this.purchaseManager.save(purchase);
								purchaseId = purchase.getPurchaseId();
							} else if (roleManager.belongRole(systemUser, jigui)) {
								int pstatus = 1;
								purchase.setPurchaseStatus(pstatus);
								this.purchaseManager.save(purchase);
								purchaseId = purchase.getPurchaseId();
							} else if (roleManager.belongRole(systemUser, caiwu)) {
								int pstatus = 4;
								purchase.setPurchaseStatus(pstatus);
								this.purchaseManager.save(purchase);
								purchaseId = purchase.getPurchaseId();
							}
							PurchaseCheckInfor checkInfor = new PurchaseCheckInfor();

							//改变包状态
							if (packageId == null || packageId.equals("")) {
							} else {
								//退回从包里把采此条购删除
								PurchasePackage purchasePackage1 =(PurchasePackage)this.packageManager.get(Integer.parseInt(packageId));
								purchasePackage1.getPurchaseInfors().remove(purchase);

								List myList = new ArrayList();
								Set<PurchasePackage> packages = purchase.getPackages();
								for (Iterator it = packages.iterator(); it.hasNext(); ) {
									PurchasePackage pg = (PurchasePackage) it.next();
									int pgId = pg.getPackageId();
									if ((pg.getStatus() == 0 || pg.getStatus() == 1) && pg.getManager().getPersonId().intValue() == systemUser.getPersonId()) {
										myList.add(String.valueOf(pgId));
									}
								}
								for (Iterator it = myList.iterator(); it.hasNext(); ) {
									String pgId = (String) it.next();
									turnPackageStatus(systemUser, String.valueOf(pgId));
								}

							}
							this.purchaseManager.beforeNode(request, response, purchase, checkInfor);
						}
					}
				}
			}
		}
//		turnPackageStatus(systemUser,packageId);
	}

	@RequestMapping(params = "method=open")
	public String open(HttpServletRequest request, HttpServletResponse response) throws Exception {

		RoleInfor cgjs = (RoleInfor) roleManager.get(PurchaseCheckInfor.Check_Role_Caigoujuese);
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		String type = request.getParameter("type");
		String url = "";
		try{

			String yblb = "/ybpurchase/purchaseInfor.do?method=listchild&flowId=1";
			String ywlb = "/ywpurchase/purchaseInfor.do?method=listchild&flowId=2";
			String gclb = "/gcpurchase/purchaseInfor.do?method=listchild&flowId=3";
			String lxlb = "/lxpurchase/purchaseInfor.do?method=listchild&flowId=4";

			String ybsf = "/sfbj/sanfanglist.jsp?type=0";
			String ywsf = "/sfbj/sanfanglist.jsp?type=1";
			String gcsf = "/sfbj/sanfanglist.jsp?type=2";

			String ybzt = "/zhaotou/bidlist.jsp?type=0";
			String ywzt = "/zhaotou/bidlist.jsp?type=1";
			String gczt = "/zhaotou/bidlist.jsp?type=2";

			String ybht = "/contract/contractList.jsp?type=0";
			String ywht = "/contract/contractList.jsp?type=1";
			String gcht = "/contract/contractList.jsp?type=2";

			String db = "/FrameCaigouDaiban.jsp";

			String ybxz = "/ybpurchase/purchaseInfor.do?method=edit&flowId=1";
			String ywxz = "/ywpurchase/purchaseInfor.do?method=edit&flowId=2";
			String gcxz = "/gcpurchase/purchaseInfor.do?method=edit&flowId=3";
			String lxxz = "/lxpurchase/purchaseInfor.do?method=edit&flowId=4";
			String packageId = null;
			if (type != null){
				switch (type){
					case "1":
						url = yblb;
						break;
					case "2":
						url = ywlb;
						break;
					case "3":
						url = gclb;
						break;
					case "4":
						url = lxlb;
						break;
					case "5":
						url = ybsf;
						break;
					case "6":
						url = ywsf;
						break;
					case "7":
						url = gcsf;
						break;
					case "8":
						url = ybzt;
						break;
					case "9":
						url = ywzt;
						break;
					case "10":
						url = gczt;
						break;
					case "11":
						url = ybht;
						break;
					case "12":
						url = ywht;
						break;
					case "13":
						url = gcht;
						break;
					case "14":
						url = db;
						break;
					case "15":
						url = ybxz;
						break;
					case "16":
						url = ywxz;
						break;
					case "17":
						url = gcxz;
						break;
					case "18":
						url = lxxz;
						break;
					case "19":
						packageId = request.getParameter("packageId");
						url = "/ybpurchase/purchaseInfor.do?method=viewPackage&flowId=1&packageId="+packageId;
						break;
					case "20":
						packageId = request.getParameter("packageId");
						url = "/lxpurchase/purchaseInfor.do?method=viewPackage&flowId=4&packageId="+packageId;
						break;
					default:
						break;
				}
				request.setAttribute("_URL",url);
			}
		}catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		if (type.equals("15") || type.equals("16") ||type.equals("17") ||type.equals("18")){
			if (!roleManager.belongRole(systemUser, cgjs)){
				request.setAttribute("_ErrorMessage", "对不起,您无权进行该操作,请与系统管理员联系!");
				return "/common/error";
			}
			return "redirect:"+url;
		}
//		if (type.equals("15") || type.equals("16") ||type.equals("17") ||type.equals("18")){
//			return "redirect:"+url;
//		}else{
//			return "../indexHome";
//		}
		return "../indexHome";
	}


	public void turnPackageStatus(SystemUserInfor systemUser,String packageId){
		boolean hasAllCheck = true;
		RoleInfor caiwu = (RoleInfor) roleManager.get(PurchaseCheckInfor.Check_Role_Caiwu);
		RoleInfor lingdao = (RoleInfor) roleManager.get(PurchaseCheckInfor.Check_Role_Gongsilingdao);
		PurchasePackage purchasePackage = this.purchasePackageDao.get(Integer.parseInt(packageId));

		OrganizeInfor org = (OrganizeInfor)this.organizeManager.get(PurchaseCheckInfor.Check_Org_Caigou);
		int caigoulingdao = org.getDirector().getPersonId();
		if (systemUser.getPersonId() == caigoulingdao) {
			Set<PurchaseInfor> purchaseInfors = purchasePackage.getPurchaseInfors();
			for (Iterator itpurchase = purchaseInfors.iterator(); itpurchase.hasNext(); ) {
				PurchaseInfor purchaseInfor = (PurchaseInfor) itpurchase.next();
				if (purchaseInfor.getPurchaseStatus() == 6){
					List rLayers = this.purchaseManager.getCurrentProcessLayers(purchaseInfor);
					PurchaseLayerInfor layer = (PurchaseLayerInfor)rLayers.get(0);
					Set checks = layer.getCheckInfors();
					for (Iterator itCheck = checks.iterator(); itCheck.hasNext(); ) {
						PurchaseCheckInfor check = (PurchaseCheckInfor) itCheck.next();
						if (check.getChecker().getPersonId().intValue() == systemUser.getPersonId()){
							if (check.getStatus() == 0){
								hasAllCheck =false;
								break;
							}
						}
					}
					if (!hasAllCheck){break;}
				}
			}
			if (hasAllCheck){
				purchasePackage.setStatus(2);
			}else{
				purchasePackage.setStatus(1);
			}
			this.purchasePackageDao.save(purchasePackage);

		}else if (roleManager.belongRole(systemUser,caiwu)){
			Set<PurchaseInfor> purchaseInfors = purchasePackage.getPurchaseInfors();
			for (Iterator itpurchase = purchaseInfors.iterator(); itpurchase.hasNext(); ) {
				PurchaseInfor purchaseInfor = (PurchaseInfor) itpurchase.next();
				if (purchaseInfor.getPurchaseStatus() == 7 ){
					List rLayers = this.purchaseManager.getCurrentProcessLayers(purchaseInfor);
					PurchaseLayerInfor layer = (PurchaseLayerInfor)rLayers.get(0);
					Set checks = layer.getCheckInfors();
					for (Iterator itCheck = checks.iterator(); itCheck.hasNext(); ) {
						PurchaseCheckInfor check = (PurchaseCheckInfor) itCheck.next();
						if (check.getChecker().getPersonId().intValue() == systemUser.getPersonId()){
							if (check.getStatus() == 0){
								hasAllCheck =false;
								break;
							}
						}
					}
					if (!hasAllCheck){break;}
				}
			}
			if (hasAllCheck){
				purchasePackage.setStatus(2);
			}else{
				purchasePackage.setStatus(1);
			}
			this.purchasePackageDao.save(purchasePackage);
		}else if (roleManager.belongRole(systemUser,lingdao)){
			Set<PurchaseInfor> purchaseInfors = purchasePackage.getPurchaseInfors();
			for (Iterator itpurchase = purchaseInfors.iterator(); itpurchase.hasNext(); ) {
				PurchaseInfor purchaseInfor = (PurchaseInfor) itpurchase.next();
				if (purchaseInfor.getPurchaseStatus() == 8){
					List rLayers = this.purchaseManager.getCurrentProcessLayers(purchaseInfor);
					PurchaseLayerInfor layer = (PurchaseLayerInfor)rLayers.get(0);
					Set checks = layer.getCheckInfors();
					for (Iterator itCheck = checks.iterator(); itCheck.hasNext(); ) {
						PurchaseCheckInfor check = (PurchaseCheckInfor) itCheck.next();
						if (check.getChecker().getPersonId().intValue() == systemUser.getPersonId()){
							if (check.getStatus() == 0){
								hasAllCheck =false;
								break;
							}
						}
					}
					if (!hasAllCheck){break;}
				}
			}
			if (hasAllCheck){
				purchasePackage.setStatus(2);
			}else{
				purchasePackage.setStatus(1);
			}
			this.purchasePackageDao.save(purchasePackage);
		}else if(purchasePackage.getCheckerType() == 2){
			Set<PurchaseInfor> purchaseInfors = purchasePackage.getPurchaseInfors();
			for (Iterator itpurchase = purchaseInfors.iterator(); itpurchase.hasNext(); ) {
				PurchaseInfor purchaseInfor = (PurchaseInfor) itpurchase.next();
				if (purchaseInfor.getPurchaseStatus() == 0){
					List rLayers = this.purchaseManager.getCurrentProcessLayers(purchaseInfor);
					PurchaseLayerInfor layer = (PurchaseLayerInfor)rLayers.get(0);
					Set checks = layer.getCheckInfors();
					for (Iterator itCheck = checks.iterator(); itCheck.hasNext(); ) {
						PurchaseCheckInfor check = (PurchaseCheckInfor) itCheck.next();
						if (check.getChecker().getPersonId().intValue() == systemUser.getPersonId()){
							if (check.getStatus() == 0){
								hasAllCheck =false;
								break;
							}
						}
					}
					if (!hasAllCheck){break;}
				}
			}
			if (hasAllCheck){
				purchasePackage.setStatus(2);
			}else{
				purchasePackage.setStatus(1);
			}
			this.purchasePackageDao.save(purchasePackage);
		}else {
			Set<PurchaseInfor> purchaseInfors = purchasePackage.getPurchaseInfors();
			for (Iterator itpurchase = purchaseInfors.iterator(); itpurchase.hasNext(); ) {
				PurchaseInfor purchaseInfor = (PurchaseInfor) itpurchase.next();
				if (purchaseInfor.getPurchaseStatus() == 3) {
					List rLayers = this.purchaseManager.getCurrentProcessLayers(purchaseInfor);
					PurchaseLayerInfor layer = (PurchaseLayerInfor)rLayers.get(0);
					Set checks = layer.getCheckInfors();
					for (Iterator itCheck = checks.iterator(); itCheck.hasNext(); ) {
						PurchaseCheckInfor check = (PurchaseCheckInfor) itCheck.next();
						if (check.getChecker().getPersonId().intValue() == systemUser.getPersonId()){
							if (check.getStatus() == 0){
								hasAllCheck =false;
								break;
							}
						}
					}
					if (!hasAllCheck){break;}
				}
			}
			if (hasAllCheck) {
				purchasePackage.setStatus(2);
			} else {
				purchasePackage.setStatus(1);
			}
			this.purchasePackageDao.save(purchasePackage);
		}


	}

	
}