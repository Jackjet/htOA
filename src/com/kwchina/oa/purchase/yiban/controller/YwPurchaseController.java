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
import com.kwchina.oa.document.entity.DocumentCategory;
import com.kwchina.oa.document.entity.DocumentCategoryRight;
import com.kwchina.oa.document.entity.DocumentInfor;
import com.kwchina.oa.document.entity.DocumentInforUserRight;
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
import com.kwchina.oa.util.MakeReceive;
import com.kwchina.oa.util.SysCommonMethod;
import com.kwchina.oa.workflow.customfields.util.CnToSpell;
import com.kwchina.oa.workflow.entity.*;
import com.kwchina.oa.workflow.exception.InstanceSuspendLayerException;
import com.kwchina.oa.workflow.service.InstanceInforRightManager;
import com.kwchina.oa.workflow.vo.FlowInstanceInforVo;
import com.kwchina.oa.workflow.vo.InstanceCountLayerVo;
import com.kwchina.oa.workflow.vo.InstanceCountVo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
@RequestMapping("/ywpurchase/purchaseInfor.do")
public class YwPurchaseController extends PurchaseBaseController {
	
	@Resource
	private PurchaseManager purchaseManager;
	@Autowired
	private PurchasePackageDao purchasePackageDao;
	@Autowired
	private PersonInforManager personInforManager;
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

	@RequestMapping(params = "method=listchild")
	public String listchild(HttpServletRequest request, HttpServletResponse response)throws Exception {
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		String flowId = request.getParameter("flowId");
		boolean canUpdates = false;
		RoleInfor cgjs = (RoleInfor) roleManager.get(PurchaseCheckInfor.Check_Role_Caigoujuese);
		boolean iscgjs = roleManager.belongRole(systemUser, cgjs);
		request.setAttribute("cgjs",iscgjs);
		request.setAttribute("flowId",flowId);
		request.setAttribute("canUpdates",canUpdates);
		return "yiban/purchaseInfor";
	}
	//显示审核实例
	@RequestMapping(params = "method=list")
	public void list(HttpServletRequest request, HttpServletResponse response)throws Exception {
		try {
			String flowId = request.getParameter("flowId");
			SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
			String flowName = "";

			// 构造查询语句
			String[] queryString = new String[2];
			queryString[0] = "from PurchaseInfor instance where 1=1";
			queryString[1] = "select count(purchaseId) from PurchaseInfor instance where 1=1";
			String condition = "";

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
			RoleInfor caigou = (RoleInfor)this.roleManager.get(PurchaseCheckInfor.Check_Role_Caigou);

			boolean isAll = roleManager.belongRole(systemUser, allRole);

//		if(isBoard){
//
//		}

			/********************************/

			if (isAll) {

			} else {
				if (this.roleManager.belongRole(systemUser,caigou)){
					condition += " and managerCheckStatus != 2 and viceManagerCheckStatus != 2 ";
				}
				// 申请人
				condition += " and (applier.personId = " + systemUser.getPersonId();

				// 部门审核人(A.未中止;B.中止前已审.)
//				condition += " or ((manager.personId = " + systemUser.getPersonId() + " and managerWord is null)" +
//						" or (manager.personId = " + systemUser.getPersonId() + " and managerChecked = 1))";
				condition += " or (manager.personId = " + systemUser.getPersonId() + " or viceManager.personId = " + systemUser.getPersonId() +")";
				// 审核人
				condition += " or (purchaseId in (select layer.purchase.purchaseId from PurchaseLayerInfor layer where layerId in " +
						"(select checkInfor.layerInfor.layerId from PurchaseCheckInfor checkInfor where checkInfor.status in (0,1,2,3) and checkInfor.checker.personId = " + systemUser.getPersonId() + " )))";

				//	权限表中设置的浏览权限
//				condition += " or (purchaseId in (select userRight.purchase.purchaseId from PurchaseInforUserRight userRight where userRight.systemUser.personId =" + systemUser.getPersonId() + "))";

				// 添加：部门中设定的部门经理，可查看本部门发起的所有合同流转
				condition += " or (guikouDepartment.organizeId in(select organizeId from OrganizeInfor where director.personId=" + systemUser.getPersonId() + "))";

				if (this.roleManager.belongRole(systemUser,caigou)){
					condition += " or instance.purchaseStatus > 0 ";
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
				obj.put("purchaseStr2",fi.getPurchaseStr2());
				obj.put("purchaseStr1",fi.getPurchaseStr1());
				obj.put("purchaseGoods",fi.getPurchaseGoods());
				obj.put("ysType", fi.getYsType());
				String startTime = "";
				if (fi.getStartTime() != null) {
					startTime = sdf.format(fi.getStartTime());
				}
//				obj.put("guige", fi.getGuige());
//				obj.put("batchNumber", fi.getBatchNumber());
//				obj.put("purchaseNumber", fi.getPurchaseNumber());
//				obj.put("purchaseGoods", fi.getPurchaseGoods());
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
				String purchaseWay = "";
				if(fi.getPurchaseWay() == 0 && fi.getPurchaseStatus() < 2){
					purchaseWay = "";
				}else if (fi.getPurchaseWay() == 0 && fi.getPurchaseStatus() > 1)	{
					purchaseWay = "直接采购";
				}else if (fi.getPurchaseWay() == 1)	{
					purchaseWay = "三方比价";
				}else if (fi.getPurchaseWay() == 2)	{
					purchaseWay = "招投标";
				}else if (fi.getPurchaseWay() == 3)	{
					purchaseWay = "合同变更";
				}
				obj.put("purchaseWay", purchaseWay);

				String status = "采购结束";
				if (fi.getPurchaseStatus() == 0) {
					status = "部门领导审核中";
				} else if (fi.getPurchaseStatus() == 1) {
					status = "采购待处理";
				} else if (fi.getPurchaseStatus() == 2) {
					status = "采购已处理待提交";
				} else if (fi.getPurchaseStatus() == 3) {
					status = "采购部领导审批中";
				} else if (fi.getPurchaseStatus() == 4) {
					status = "领导审核中";
				} else if (fi.getPurchaseStatus() == 5) {
					status = "子流程中";
				} else if (fi.getPurchaseStatus() == 6) {
					status = "采购部确认中";
				} else if (fi.getPurchaseStatus() == 7) {
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
					if (tmpCheck.getEndDate() == null && fi.getPurchaseStatus() != 1 ) {
						canCheck = "true";
					}else if (systemUser.getPersonId() == 1){
						canCheck = "true";
					}
				}
				obj.put("canCheck", canCheck);
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
	/** ********导出Excel操作****** */

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

		return "yiban/editPurchase";
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

		purchase.setGuige(vo.getGuige());
		purchase.setYsType(vo.getYsType());
		purchase.setPurchaseStr2(vo.getPurchaseStr2());
		purchase.setPurchaseStr1(vo.getPurchaseStr1());
		purchase.setApplication(vo.getApplication());
		purchase.setPurchaseNumber(vo.getPurchaseNumber());
		purchase.setPurchaseTitle(vo.getPurchaseTitle());
		purchase.setPurchaseGoods(vo.getPurchaseGoods());
//		OrganizeInfor guikou = (OrganizeInfor)this.organizeManager.get(Integer.parseInt(vo.getGuikouDepartment()));
//		purchase.setGuikouDepartment(guikou);


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
						PurchaseInfor purchase = (PurchaseInfor) this.purchaseManager.get(deleteId);

						boolean canDelete = false;
						if(deleteFlag == 0){
							canDelete = true;
						}else {
							canDelete = this.purchaseManager.canDeleteFlowInstance(purchase, systemUser);
						}

						if (canDelete) {
							purchase.setDeleteFlag(deleteFlag);
							this.purchaseManager.save(purchase);
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
				if(roleManager.belongRole(systemUser,caigou)){
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


	//编辑部门审核
	@RequestMapping(params = "method=editDepCheck")
	public String editDepCheck(HttpServletRequest request, HttpServletResponse response, PurchaseInforVo vo) throws Exception {

		String purchaseId = request.getParameter("purchaseId");

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
		}else{
			return "/common/error";
		}

		return "redirect:purchaseInfor.do?method=view&purchaseId=" + purchaseId;
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
			FlowInstanceInforVo vo, DefaultMultipartHttpServletRequest multipartRequest) throws Exception {

		Integer instanceId = vo.getInstanceId();
		if (instanceId != null && instanceId.intValue() > 0) {
			FlowInstanceInfor instance = (FlowInstanceInfor)this.purchaseManager.get(instanceId);

			//保存结束时间
			String endTimeStr = request.getParameter("endTime");
			Timestamp endTime = null;
			if (endTimeStr != null && endTimeStr.length() > 0) {
				endTime = Timestamp.valueOf(endTimeStr);
			}
			instance.setEndTime(endTime);

			//正式附件
			String formalAttach = this.uploadAttachment(multipartRequest, "workflow");
			instance.setFormalAttach(formalAttach);

			this.purchaseManager.save(instance);

			//是否下发到分公司收文
			String handOut = request.getParameter("handOut");
			if(handOut != null && !handOut.equals("")){
				if(handOut.equals("1") && instance.getFlowDefinition().getFlowId().intValue() == 85){
					instance.setHandOut(true);
					//System.out.println(handOut);
					//写入分公司收文表中
					Map<String, Object[]> formDataMap = new HashMap<String, Object[]>();


					//获取发文的表单数据
					String flowName = CnToSpell.getFullSpell(instance.getFlowDefinition().getFlowName());
					List cols = this.purchaseCommonManager.getColumnNames(flowName);
					//获取审核实例的相关数据
					Map<Object, Object> publishMap = new HashMap<Object, Object>();
					List formDatas = this.purchaseCommonManager.getFormData(flowName, Integer.valueOf(instanceId));
					if (formDatas != null && formDatas.size() > 0) {
						Object[] obj = (Object[]) formDatas.get(0);
						for (int i=0;i<obj.length;i++) {
							publishMap.put(cols.get(i), obj[i]);
						}
					}

//					formDataMap.put("instanceId", "");
					formDataMap.put("flowId", new String[]{"84"});
					formDataMap.put("instanceTitle", new String[]{instance.getInstanceTitle()});

					/*收文表单数据*/
					SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
					formDataMap.put("receiveDate", new String[]{sf.format(new Date())});
					formDataMap.put("documentNo", new String[]{""});
					formDataMap.put("reportYear", new String[]{publishMap.get("reportYear").toString()});
					formDataMap.put("serialNo", new String[]{""});
					formDataMap.put("receiverId", new String[]{"0"});
					formDataMap.put("reportNo", new String[]{publishMap.get("documentNo").toString()});
					formDataMap.put("fileDate", new String[]{publishMap.get("sendDate").toString()});
					formDataMap.put("fileNum", new String[]{""});
					formDataMap.put("secretName", new String[]{""});
					formDataMap.put("urgencyName", new String[]{""});
					formDataMap.put("unitName", new String[]{"上海海通国际汽车物流（码头）有限公司"});
					formDataMap.put("selUnitName", new String[]{""});

//					formDataMap.put("managerId", new String[]{""});
//					formDataMap.put("viceManagerId", new String[]{""});

					//附件
//					formDataMap.put("attachment", multipartRequest);

					/*instanceId	874
					oldInstanceId	0
					flowId	84
					instanceTitle	收文测试一一一
					receiveDate	2014-10-22
					documentNo	HT-SW-2014-001
					reportYear	2014
					serialNo	001
					receiverId	8
					reportNo	沪港务工程发（2014）0194号
					fileDate	2014-10-22
					fileNum
					secretName
					urgencyName
					unitName
					selUnitName	安全监督部
					managerId	33
					viceManagerId	0
					attachment*/

					MakeReceive makeLocalReceive = new MakeReceive();
					makeLocalReceive.doMakeReceive(formDataMap,formalAttach,"192.168.61.20");
					makeLocalReceive.doMakeReceive(formDataMap,formalAttach,"192.168.61.21");
				}
			}

		}
		return view(request, response);
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
			List<PurchaseInfor> returnInstances = new ArrayList<PurchaseInfor>();
			List<String> warningStrs = new ArrayList<String>();
			if(isMobile){
				//移动端需要显示的
				returnInstances_m = this.purchaseManager.getNeedPushInstances(systemUser, null);
			}else {
				//得到待办
				Map<String, Object> map = this.purchaseManager.getNeedDealInstances(systemUser,2);

				//PC端需要显示的待办
				returnInstances = (List<PurchaseInfor>)map.get("ReturnInstances");
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


//	导出excel
//	@RequestMapping(params = "method=excel")
//	public String excel(HttpServletRequest request, HttpServletResponse response)throws Exception {
//		try{
//		String flowId = request.getParameter("flowId");
//		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
//		String flowName = "";
//
//		// 构造查询语句
//		String[] queryString = new String[2];
//		queryString[0] = "from FlowInstanceInfor instance where 1=1";
//		queryString[1] = "select count(instanceId) from FlowInstanceInfor instance where 1=1";
//		String condition = "";
//
//		// 所属流程Id
//		FlowDefinition flow = new FlowDefinition();
//		String tableName = "";
//		if (flowId != null && flowId.length() > 0) {
//			condition += " and flowDefinition.flowId = " + flowId;
//			flow = (FlowDefinition)this.purchaseflowManager.get(Integer.valueOf(flowId));
//			flowName = CnToSpell.getFullSpell(flow.getFlowName());
//			tableName = "Customize_" + flowName;
//		}
//
//		//根据流程名获取表列名
//		List columnNames = this.purchaseCommonManager.getColumnNames(flowName);
//
//		// 是否删除
//		String deleted = request.getParameter("deleted");
//		if (deleted != null && ("true").equals(deleted)) {
//			condition += " and deleteFlag = 1";
//		}else {
//			condition += " and deleteFlag = 0";
//		}
//
//		/********************************/
//		//获取董事会特定角色的角色信息（ID为固定）
//		RoleInfor boardRole = (RoleInfor)roleManager.get(22);
//
//		//获取合同审批特定角色的角色信息（ID为固定）
//		RoleInfor contractRole = (RoleInfor)roleManager.get(24);
//
//		//获取合同审批特定角色的角色信息（ID为固定）
//		RoleInfor lawRole = (RoleInfor)roleManager.get(26);
//
////		boolean isBoard  = roleManager.belongRole(systemUser, boardRole);
//		boolean isContract = roleManager.belongRole(systemUser, contractRole);
//		boolean isLaw = roleManager.belongRole(systemUser, lawRole);
//
////		if(isBoard){
////
////		}
//
//		/********************************/
////		合同查看角色
//		if(isContract && (flowId.equals("86")||flowId.equals("87"))){
//
//		}else{
//
//		// 申请人
//		condition += " and (applier.personId = " + systemUser.getPersonId();
//
//		// 部门审核人(A.未中止;B.中止前已审.)
//		condition += " or ((manager.personId = " + systemUser.getPersonId() + " or viceManager.personId = " + systemUser.getPersonId() + ") and submiterWord is null" +
//				" or (manager.personId = " + systemUser.getPersonId() + " and managerChecked = 1) or (viceManager.personId = " + systemUser.getPersonId() + " and viceManagerChecked = 1))";
//
//		// 主办人
//		condition += " or (charger.personId = " + systemUser.getPersonId() + " and startTime is not null)";
//
//
//		// 如果本流程的归档人为固定角色时，同样有查看权限
//		if(flow.getFilerType() == 1 && flow.getFileRole() != null){
//			String sysRoleStr = "";
//
//			Set<RoleInfor> tmpRoleSet = systemUser.getRoles();
//			List<RoleInfor> tmpRoleList = new ArrayList<RoleInfor>();
//
//			for(Iterator it = tmpRoleSet.iterator();it.hasNext();){
//				tmpRoleList.add((RoleInfor)it.next());
//			}
//			for(int i=0;i<tmpRoleList.size();i++){
//				sysRoleStr += tmpRoleList.get(i).getRoleId().toString();
//				if(i<tmpRoleList.size()-1){
//					sysRoleStr += ",";
//				}
//			}
//
//			if(!sysRoleStr.equals("") && sysRoleStr != null){
//				condition += " or (instance.flowDefinition.fileRole.roleId in ("+sysRoleStr+"))";
//			}
//
//		}
//
//
//		// 审核人
//		condition += " or (instanceId in (select layer.instance.instanceId from InstanceLayerInfor layer where layerId in " +
//				"(select checkInfor.layerInfor.layerId from InstanceCheckInfor checkInfor where checkInfor.checker.personId = " + systemUser.getPersonId() + " )))";
//
//		//	权限表中设置的浏览权限
//		condition +=  " or (instanceId in (select userRight.instance.instanceId from InstanceInforUserRight userRight where userRight.systemUser.personId =" + systemUser.getPersonId() + "))";
//		condition += " or (instanceId in " + "(select roleRight.instance.instanceId from InstanceInforRoleRight roleRight,SystemUserInfor user where roleRight.role in " + "elements(user.roles) and user.personId = " + systemUser.getPersonId() + ")))";
//
//
//		}
//		queryString[0] += condition;
//		queryString[1] += condition;
//
//		//获取自定义表单查询字段
//		Map map = getSearchFields(request, response);
//		JSONArray searchFields = (JSONArray) map.get("_SearchFields");
//
//		//Map maps = new HashMap();
//
//
//		//构造查询条件
//		String[] params = getSearchParams(request);
//		String cusCondition	= "";	//自定义查询条件
//		if (params[2].equals("true")) {
//			if (params[3] != null && params[3].length() > 0) {
//
//				JSONObject filter = JSONObject.fromObject(params[3]);
//				JSONArray rules = filter.getJSONArray("rules");		//取数据中的查询信息:查询字段,查询条件,查询数据
//				if (rules != null && rules.size() > 0) {
//					for (int i=0;i<rules.size();i++) {
//						JSONObject tmpObj = (JSONObject)rules.get(i);
//						String fieldValue = tmpObj.getString("field");	//查询字段
//						String opValue = tmpObj.getString("op");		//查询条件:大于,等于,小于..
//						String dataValue = tmpObj.getString("data");	//查询数据
//
//						//处理查询条件
//						boolean has = false;
//						if (searchFields != null && searchFields.size() > 0) {
//							for (int j=0;j<searchFields.size();j++) {
//								JSONArray fieldArray = (JSONArray) searchFields.get(j);
//								JSONObject tmpField = (JSONObject)fieldArray.get(1);
//								String searchField = tmpField.getString("searchField");
//								if (searchField.equals(fieldValue)) {
//									has = true;
//									//组织自定义表单查询条件
//									String tmpCondition = ConditionUtils.getCondition(fieldValue, opValue, dataValue);
//									cusCondition += " and " + tmpCondition;
//									break;
//								}
//							}
//						}
//						if (!has) {
//							//过滤自定义表单查询字段
//							String tmpCondition = ConditionUtils.getCondition(fieldValue, opValue, dataValue);
//							queryString[0] += " and " + tmpCondition;
//							queryString[1] += " and " + tmpCondition;
//						}
//					}
//				}
//			}
//		}
//
//		//加上自定义表单查询条件
//		if (cusCondition.length() > 0 && tableName.length() > 0) {
//			String sql = "select instanceId from " + tableName + " where 1=1" + cusCondition;
//			List instancIds = this.purchaseManager.getResultBySQLQuery(sql);
//			if (instancIds != null && instancIds.size() > 0) {
//				String instancIdsStr = "";
//				int index = 0;
//				for (Iterator it=instancIds.iterator();it.hasNext();) {
//					instancIdsStr += ((Integer)it.next()).toString();
//					if (index < instancIds.size()-1) {
//						instancIdsStr += ",";
//					}
//					index++;
//				}
//				queryString[0] += " and instanceId in(" + instancIdsStr + ")";
//				queryString[1] += " and instanceId in(" + instancIdsStr + ")";
//			} else {
//				// 没有满足查询条件的数据
//				queryString[0] += " and instanceId in(0)";
//				queryString[1] += " and instanceId in(0)";
//			}
//		}
//
//		queryString[0] += " order by " + params[0] + " " + params[1];
//
//		//queryString = this.purchaseManager.generateQueryString(queryString, getSearchParams(request));
//
//		String page = "1"; // 当前页
//		String rowsNum = "10000"; // 每页显示的行数
//
//		Pages pages = new Pages(request);
//		pages.setPage(Integer.valueOf(page));
//		pages.setPerPageNum(Integer.valueOf(rowsNum));
//
//
//		List listCount = this.purchaseManager.getResultByQueryString(queryString[1]);
//		String ss = listCount.get(0).toString();
//
//		Pages pagesExcel = new Pages(request);
//		pagesExcel.setPage(Integer.valueOf(page));
//		pagesExcel.setPerPageNum(Integer.valueOf(ss)+1);
//		List list = new ArrayList();
//		JSONObject jsonObj = new JSONObject();
//
//		// 定义rows，存放数据
//		JSONArray rows = new JSONArray();
//		PageList plExcel = new  PageList();
//		if (params[2].equals("true") && isLaw && flowId.equals("86") ) {
//			 plExcel = this.purchaseManager.getResultByQueryString(queryString[0], queryString[1], true, pagesExcel);
//
//			 list = plExcel.getObjectList();
//			// plExcel.setPages(pages);
//			// plExcel.setPageShowString(pageShowString);
//			// 定义返回的数据类型：json，使用了json-lib
//			//pagesExcel.setPage(Integer.valueOf(page));
//			//pagesExcel.setPerPageNum(Integer.valueOf(rowsNum));
//
//			jsonObj.put("page", plExcel.getPages().getCurrPage()); // 当前页(名称必须为page)
//			jsonObj.put("total", plExcel.getPages().getTotalPage()); // 总页数(名称必须为total)
//			jsonObj.put("records", plExcel.getPages().getTotals()); // 总记录数(名称必须为records)
//			//List list = pl.getObjectList();
//			//plExcel.setPages(pagesExcel);
//
//		}else{
//			PageList pl = this.purchaseManager.getResultByQueryString(queryString[0], queryString[1], true, pages);
//			 list = pl.getObjectList();
//
//			// 定义返回的数据类型：json，使用了json-lib
//
//			jsonObj.put("page", pl.getPages().getCurrPage()); // 当前页(名称必须为page)
//			jsonObj.put("total", pl.getPages().getTotalPage()); // 总页数(名称必须为total)
//			jsonObj.put("records", pl.getPages().getTotals()); // 总记录数(名称必须为records)
//		}
//
//
//
//
//
//
//		//取需要的字段信息返回
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		Iterator it = list.iterator();
//		while(it.hasNext()){
//			FlowInstanceInfor fi = (FlowInstanceInfor)it.next();
//
//			boolean canAdd = false;
//
//			// 判断是否为当前审核人
//			InstanceCheckInfor tmpCheck = this.purchaseManager.isChecker(fi, systemUser);
//			if(isLaw){
//				canAdd = true;
//			}else if (tmpCheck != null) {
//				// 能否看见
//				if (tmpCheck.getEndDate() != null
//						|| (tmpCheck.getEndDate() == null
//							&& tmpCheck.getLayerInfor().getStatus() != InstanceLayerInfor.Layer_Status_End
//							&& tmpCheck.getLayerInfor().getStatus() != InstanceLayerInfor.Layer_Status_Suspended
//							&& !fi.isSuspended())) {
//					canAdd = true;
//				}
//			}else {
//				canAdd = true;
//			}
//
//
//
//			if (canAdd) {
//				JSONObject obj = new JSONObject();
//				obj.put("instanceId", fi.getInstanceId());
//				obj.put("instanceTitle", fi.getInstanceTitle());
//				String startTime = " ";
//				if (fi.getStartTime() != null) {
//					startTime = sdf.format(fi.getStartTime());
//				}
//				obj.put("startTime", startTime);
//				String checker = " ";
//				if(fi.getManager() != null){
//					 checker= fi.getManager().getPerson().getPersonName();
//				}
//
//				obj.put("checker", checker);
//				obj.put("updateTime", sdf.format(fi.getUpdateTime()));
//				obj.put("applier", fi.getApplier().getPerson().getPersonName());
//				obj.put("department", fi.getDepartment().getOrganizeName());
//				obj.put("flow", fi.getFlowDefinition().getFlowName());
//				//obj.put("charger", fi.getFlowDefinition().getCharger().getPerson().getPersonName());
//				if (fi.getCharger() != null) {
//					obj.put("charger", fi.getCharger().getPerson().getPersonName());
//				}else {
//					obj.put("charger", null);
//				}
//				String status = "草稿";
//				if (fi.isFiled()) {
//					status = "已归档";
//				}else if (fi.getEndTime() != null) {
//					status = "已结束待归档";
//				}else if (fi.isSuspended()) {
//					status = "已暂停";
//				}else if (fi.getStartTime() != null) {
//
//					//获取当前处理层,并判断处理情况:进行中,已完毕
//					String layerStatus = "";
//					List currentLayers = this.purchaseManager.getCurrentProcessLayers(fi);
//					int i = 0;
//					for (Iterator cl=currentLayers.iterator();cl.hasNext();) {
//						InstanceLayerInfor layer = (InstanceLayerInfor)cl.next();
//						if (layer.getStatus() == InstanceLayerInfor.Layer_Status_Normal) {
//							layerStatus += layer.getLayerName();
//						}else if (layer.getStatus() == InstanceLayerInfor.Layer_Status_End) {
//							layerStatus += layer.getLayerName() + "已暂停";
//						}else if (layer.getStatus() == InstanceLayerInfor.Layer_Status_Suspended) {
//							layerStatus += layer.getLayerName() + "已终止";
//						}else if (layer.getStatus() == InstanceLayerInfor.Layer_Status_Finished) {
//							layerStatus += layer.getLayerName() + "已完毕";
//						}
//
//						if (i != currentLayers.size()-1) {
//							layerStatus += ",";
//						}
//
//						i++;
//					}
//					status = layerStatus;
//				}else if (fi.getManager() !=null && fi.getViceManager() == null && fi.isViceManagerChecked() || fi.getViceManager() != null && fi.getManager() == null && fi.isViceManagerChecked() || fi.getManager() != null && fi.getViceManager() != null && fi.isManagerChecked() && fi.isViceManagerChecked() && fi.getSubmiterWord() == null) {
//					status = "部门已审核";
//				}else if ((fi.getManager() != null || fi.getViceManager() != null) && fi.getSubmiterWord() == null) {
//					status = "部门审核中";
//				}
//				obj.put("status", status);
//				obj.put("attach", fi.getAttach());
//
//				/** 自定义表单数据 */
//				if (columnNames != null && columnNames.size() > 0) {
//					List customizeDatas = this.purchaseCommonManager.getFormData(flowName, fi.getInstanceId());
//					if (customizeDatas != null && customizeDatas.size() > 0) {
//						Object[] dataArray = (Object[])customizeDatas.get(0);
//
//						int i=0;
//						for (Iterator col=columnNames.iterator();col.hasNext();) {
//							String colName = (String)col.next();
//							obj.put(colName, dataArray[i]);
//							i++;
//						}
//					}
//				}
//				/*****/
//
//				rows.add(obj);
//			}
//		}
//		//jsonObj.put("rows", rows); // 返回到前台每页显示的数据(名称必须为rows)
//
//
///** ********导出Excel操作****** */
//
//		boolean isExport=true;
//		if (params[2].equals("true")&& isLaw && flowId.equals("86")) {
//
//
//			 List<Map<String, String>> maps = (List<Map<String, String>>) JSONArray.toCollection(rows, Map.class);
//
//
//
//			long time = System.currentTimeMillis();
//			String filePath ="/uploadfiles/submit/excel/";
//			String fileTitle = "";
//
//
//				fileTitle = "contract" ;
//
//
//			ExcelObject object = new ExcelObject();
//			object.setFilePath(filePath);
//			object.setFileName(fileTitle);
//			object.setTitle(fileTitle);
//
//			List rowName = new ArrayList();
//			String[][] data = new String[17][rows.size()];
//			int k = 0;//列数
//
//
//				rowName.add("序号");
//				rowName.add("归属公司");
//				rowName.add("合同编号");
//				rowName.add("合同名称");
//				rowName.add("合同类型");
//				rowName.add("申请人");
//				rowName.add("状态");
//				rowName.add("合同相对方");
//				rowName.add("合同总金额");
//				rowName.add("合同时限");
//				rowName.add("经办部门");
//				rowName.add("会审部门");
//				rowName.add("批准人");
//				rowName.add("签订日期");
//				rowName.add("流水号");
//				rowName.add("备注");
//				k = 17;
//
//
//
//			for (int i = 0; i < maps.size(); i++) {
//				Map map1 = new HashMap();
//
//				map1 = maps.get(i);
//
//
//
//					data[0][i] = String.valueOf(i+1);
//					data[1][i] = map1.get("category").toString();
//					data[2][i] = map1.get("contractNo").toString();
//					data[3][i] = map1.get("instanceTitle").toString();
//					data[4][i] = map1.get("contractType").toString();
//					data[5][i] = map1.get("applier").toString();
//					data[6][i] = map1.get("status").toString();
//					data[7][i] = map1.get("oppositeUnit").toString();
//					data[8][i] = map1.get("contractPrice").toString();
//					data[9][i] = map1.get("contractPeriod").toString();
//					data[10][i] = map1.get("department").toString();
//					data[11][i] = map1.get("involvedDeps").toString();
//					data[12][i] = map1.get("checker").toString();
//					data[13][i] = map1.get("startTime").toString();
//					data[14][i] = map1.get("indexNo").toString();
//					data[15][i] = map1.get("memo").toString();
//
//
//
//
//
//			}
//
//			for (int i = 0; i < k; i++) {
//				object.addContentListByList(data[i]);
//			}
//			object.setRowName(rowName);
//			ExcelOperate operate = new ExcelOperate();
//			try {
//				operate.exportExcel(object, plExcel.getObjectList().size(), true, request);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//
//
//			filePath = filePath + fileTitle +".xls";
////			request.getSession().setAttribute("_File_Path", "");
//			request.getSession().removeAttribute("_File_Path");
//			request.getSession().setAttribute("_File_Path", filePath);
//			//return mapping.findForward("exportExcel");
//		}
//
//
//		// 设置字符编码
//		response.setContentType(CoreConstant.CONTENT_TYPE);
//		response.getWriter().print(jsonObj);
//		}catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//
//		return "redirect:/common/download.jsp?filepath=uploadfiles/submit/excel/contract.xls";
//
//	}

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
	 * app端查看方法
	 * @param request
	 * @param response
	 * @throws Exception
	 */
//	@RequestMapping(params="method=view_m")
//	public void view_m(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
//		int userId = systemUser.getPersonId().intValue();
//
//		JSONObject jsonObj = new JSONObject();
//		JSONConvert convert = new JSONConvert();
//
//		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//		String instanceIdStr  = request.getParameter("instanceId");
//		if(!instanceIdStr.equals("") && instanceIdStr.length() > 0){
//			int instanceId = Integer.valueOf(instanceIdStr);
//			FlowInstanceInfor instance = (FlowInstanceInfor)this.purchaseManager.get(instanceId);
//
//			//将instance信息先放入
////			request.setAttribute("_Instance", instance);
//			//instance转化为vo
//			MFlowInstanceInforVo vo = new MFlowInstanceInforVo();
//			BeanUtils.copyProperties(vo, instance);
//			vo.setUpdateTimeStr(String.valueOf(instance.getUpdateTime()));
//			vo.setStartTimeStr(String.valueOf(instance.getStartTime()));
//			vo.setEndTimeStr(String.valueOf(instance.getEndTime()));
//			vo.setApplierId(instance.getApplier().getPersonId().intValue());
//			vo.setApplierName(instance.getApplier().getPerson().getPersonName());
//			vo.setDepartmentId(instance.getDepartment().getOrganizeId().intValue());
//			vo.setDepartmentName(instance.getDepartment().getOrganizeName());
//			vo.setFlowId(instance.getFlowDefinition().getFlowId().intValue());
//			vo.setFlowName(instance.getFlowDefinition().getFlowName());
//			vo.setChargerId(instance.getCharger().getPersonId().intValue());
//			vo.setChargerName(instance.getCharger().getPerson().getPersonName());
//			if(instance.getManager() != null){
//				vo.setManagerId(instance.getManager().getPersonId().intValue());
//				vo.setManagerName(instance.getManager().getPerson().getPersonName());
//			}
//			if(instance.getCheckTime() != null){
//				vo.setCheckTimeStr(sf.format(instance.getCheckTime()));
//			}else {
//				vo.setCheckTimeStr("");
//			}
//
//			if(instance.getViceManager() != null){
//				vo.setViceManagerId(instance.getViceManager().getPersonId().intValue());
//				vo.setViceManagerName(instance.getViceManager().getPerson().getPersonName());
////				vo.setViceCheckTimeStr(String.valueOf(instance.getViceCheckTime()));
//
//				if(instance.getViceCheckTime() != null){
//					vo.setViceCheckTimeStr(sf.format(instance.getViceCheckTime()));
//				}else {
//					vo.setViceCheckTimeStr("");
//				}
//			}
//
//
//			//审核层
//			List<MLayerInforVo> layerVos = new ArrayList<MLayerInforVo>();
//			Set<InstanceLayerInfor> layerSet = instance.getLayers();
//			for(InstanceLayerInfor layer : layerSet){
//				MLayerInforVo layerVo = new MLayerInforVo();
//
//				BeanUtils.copyProperties(layerVo, layer);
//				layerVo.setStartTimeStr(String.valueOf(layer.getStartTime()));
//				layerVo.setEndTimeStr(String.valueOf(layer.getEndTime()));
////				layerVo.setFlowNodeName(layer.getFlowNode().getNodeName());
//
//				//审核意见
//				List<MCheckInforVo> checkVos = new ArrayList<MCheckInforVo>();
//				Set<InstanceCheckInfor> checkSet = layer.getCheckInfors();
//				for(InstanceCheckInfor check : checkSet){
//					MCheckInforVo checkVo = new MCheckInforVo();
//					BeanUtils.copyProperties(checkVo, check);
//
//					checkVo.setStartDateStr(String.valueOf(check.getStartDate()));
////					checkVo.setEndDateStr(String.valueOf(check.getEndDate()));
//
//					if(check.getEndDate() != null){
//						checkVo.setEndDateStr(sf.format(check.getEndDate()));
//					}else {
//						checkVo.setEndDateStr("");
//					}
//
//					checkVo.setCheckerId(check.getChecker().getPersonId().intValue());
//					checkVo.setCheckerName(check.getChecker().getPerson().getPersonName());
//
//					checkVos.add(checkVo);
//				}
//
//				layerVo.setMCheckInfors(checkVos);
//
//				layerVos.add(layerVo);
//			}
//
//			vo.setMLayers(layerVos);
//
//
////			List instanceList = new ArrayList();
////			instanceList.add(instance);
////			JSONArray instanceJsonArray = new JSONArray();
////			instanceJsonArray = convert.modelCollect2JSONArray(instanceList, new ArrayList());
//			jsonObj.put("_Instance", vo);
//
//			//将审核环节放入
////			Set<InstanceLayerInfor> layers = instance.getLayers();
////			List layerList = new ArrayList();
////			layerList.addAll(layers);
////			JSONArray layerJsonArray = new JSONArray();
////			layerJsonArray = convert.modelCollect2JSONArray(layerList, new ArrayList());
////			jsonObj.put("_Layers", layerJsonArray);
//
//			/*List checkList = new ArrayList();
//			for(InstanceLayerInfor layer : layers){
//				checkList.addAll(layer.getCheckInfors());
//			}
//			JSONArray checkJsonArray = new JSONArray();
//			checkJsonArray = convert.modelCollect2JSONArray(checkList, new ArrayList());
//			jsonObj.put("_Checks", checkJsonArray);*/
//
//
//			FlowDefinition flow = instance.getFlowDefinition();
//			//将汉字的流程名处理为拼音
//			String flowName = CnToSpell.getFullSpell(flow.getFlowName());
//
//			//得到所字段名
//			List columnNames = this.purchaseCommonManager.getColumnNames(flowName);
//
//			//得到所有值
//			List formDatas = this.purchaseCommonManager.getFormData(flowName, instanceId);
//
//			//循环将各流程中相应的值放入json中
//			Map<String,Object> map = new HashMap();
//			if (formDatas != null && formDatas.size() > 0) {
//				Object[] dataArray = (Object[])formDatas.get(0);
//				int i=0;
//				for (Iterator col=columnNames.iterator();col.hasNext();) {
//					String colName = (String)col.next();
//					map.put(colName, dataArray[i]);
//					i++;
//				}
//			}
//
//			//将关联的为ID的改为name（部门及用户）
//			String[] templateArray = flow.getTemplate().split("/");
//			String templateName = templateArray[templateArray.length-1];
//			//1、部门
//			List<String> dptNameList = this.purchaseCommonManager.getDynamicFieldNames(templateName, "[@depSelect");
//			for(String key : map.keySet()){
//				for(String fieldName : dptNameList){
//					if(fieldName.equals(key)){
//						String dptIdStr = String.valueOf(map.get(key));
//						if(dptIdStr != null && !dptIdStr.equals("") && !dptIdStr.equals("0")){
//							OrganizeInfor dpt = (OrganizeInfor)this.organizeManager.get(Integer.valueOf(dptIdStr));
//							map.put(key, dpt.getOrganizeName());
//						}else if(dptIdStr.equals("0")){
//							map.put(key, "");
//						}
//
//					}
//				}
//			}
//
//			//1、人员
//			List<String> personNameList = this.purchaseCommonManager.getDynamicFieldNames(templateName, "[@usrSelect");
//			for(String key : map.keySet()){
//				for(String fieldName : personNameList){
//					if(fieldName.equals(key)){
//						String userIdStr = String.valueOf(map.get(key));
//						if(userIdStr != null && !userIdStr.equals("") && !userIdStr.equals("0")){
//							SystemUserInfor user = (SystemUserInfor)this.systemUserManager.get(Integer.valueOf(userIdStr));
//							map.put(key, user.getPerson().getPersonName());
//						}else if(userIdStr.equals("0")){
//							map.put(key, "");
//						}
//
//					}
//				}
//			}
//
//
//			//动态表单内容
//			JSONArray customJson = JSONArray.fromObject(map);
//			jsonObj.put("_CustomDatas", customJson);
//
//			//判断能否进行部门审核
//			boolean canDepCheck = false;
//			SystemUserInfor manager = instance.getManager();
//			SystemUserInfor viceManager = instance.getViceManager();
//			if (((manager != null && manager.getPersonId().intValue() == userId && !instance.isManagerChecked())||(viceManager != null && viceManager.getPersonId().intValue() == userId && !instance.isViceManagerChecked()))
//					&&instance.getSubmiterWord()==null && instance.getStartTime() == null) {
//				canDepCheck = true;
//			}
//			jsonObj.put("_CanDepCheck", canDepCheck);
//
//			//判断是否有审核权限
//			boolean canCheck = false;
//			InstanceCheckInfor tmpCheck = this.purchaseManager.isChecker(instance, systemUser);
//			if (tmpCheck != null) {
//				// 能否审核
//				if (tmpCheck.getLayerInfor().getStatus() != InstanceLayerInfor.Layer_Status_End
//						&& tmpCheck.getLayerInfor().getStatus() != InstanceLayerInfor.Layer_Status_Suspended
//						&& !instance.isSuspended() && tmpCheck.getEndDate() == null) {
//					canCheck = true;
//				}
//			}
//			jsonObj.put("_CanCheck", canCheck);
//
//
//			/** 审核人审核时 获取该审核实例目前正在处理的审核层 */
//			int checkId = 0;
//			List rLayers = this.purchaseManager.getCurrentProcessLayers(instance);
//
//			if (rLayers != null && rLayers.size() > 0) {
//				for (Iterator it=rLayers.iterator();it.hasNext();) {
//					InstanceLayerInfor layer = (InstanceLayerInfor)it.next();
//					Set checks = layer.getCheckInfors();
//
//					//判断是否存在该用户需要审核的信息
//					boolean isChecker = false;
//					for (Iterator itCheck=checks.iterator();itCheck.hasNext();) {
//						InstanceCheckInfor check = (InstanceCheckInfor)itCheck.next();
//						if(check.getStatus()==0){
//							int ss=layer.getStatus();
//							int checkerId = check.getChecker().getPersonId().intValue();
//							if (userId == checkerId && ss !=3) {
//								BeanUtils.copyProperties(vo, check);
//								checkId = check.getCheckId();
//								isChecker = true;
//								break;
//							}
//						}
//						if (isChecker) {
//							break;
//						}
//					}
//				}
//			}
//
//			jsonObj.put("_CheckId", checkId);
//		}
//
//		//设置字符编码
//        response.setContentType(CoreConstant.CONTENT_TYPE);
//        response.getWriter().print(jsonObj);
//
//	}

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
		PurchasePackage purchasePackage = new PurchasePackage();
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		String rowIds = request.getParameter("rowIds");
		if (rowIds != null &&rowIds.length() > 0) {
			String[] updateIds = rowIds.split(",");
			if (updateIds.length > 0) {
				for (int j = 0; j < updateIds.length; j++) {
					int purchaseId = Integer.valueOf(updateIds[j]);
					PurchaseInfor purchase = (PurchaseInfor) this.purchaseManager.get(purchaseId);
					List rLayers = this.purchaseManager.getCurrentProcessLayers(purchase);
					PurchaseLayerInfor layer = (PurchaseLayerInfor)rLayers.get(0);
					if (layer.getStatus() == 1) {
						Set checks = layer.getCheckInfors();
						//结束本层
						for (Iterator itCheck = checks.iterator(); itCheck.hasNext(); ) {
							PurchaseCheckInfor check = (PurchaseCheckInfor) itCheck.next();
							if (check.getStatus() == 0) {
								SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
								Timestamp checkDate = null;
								check.setChecker(systemUser);
								check.setStatus(1);
								this.purchaseCheckInforManager.saveCheckInfor(check, checkDate);
								int pstatus = purchase.getPurchaseStatus();
								if (purchase.getPurchaseWay() == 0 && purchase.getPurchaseStatus() == 4){
									pstatus = pstatus+2;
								}else{
									pstatus = pstatus+1;
								}
								purchase.setPurchaseStatus(pstatus);
								this.purchaseManager.save(purchase);
							}
						}
						//审核新层
						int currentLayer =layer.getLayer();
						int newcurrentLayer = currentLayer + 1;
						int layerNode = layer.getPurchaseNode().getNodeId();
						int newlayerNode = layerNode + 1;
						String hql = "from PurchaseNode node where flowId = 2 and nodeId =" + newlayerNode ;
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
						if (checkerType == 0){
							SystemUserInfor user =  node.getUser();
							purchaseCheckInfor.setStartDate(currentTime);
							purchaseCheckInfor.setStatus(status);
							purchaseCheckInfor.setLayerInfor(newpurchaseLayerInfor);
							purchaseCheckInfor.setChecker(user);
						}else{
							RoleInfor role =node.getRoleId();
							purchaseCheckInfor.setStartDate(currentTime);
							purchaseCheckInfor.setStatus(status);
							purchaseCheckInfor.setLayerInfor(newpurchaseLayerInfor);
							purchaseCheckInfor.setCheckRoler(role);
						}
						this.purchaseCheckDAO.save(purchaseCheckInfor);

						//新包
						boolean adddd = true;
						purchaseInfors.add(purchase);
						if (adddd) {
							if (layer.getLayerName().equals("归口负责人提交") || layer.getLayerName().equals("采购负责人提交")) {
								purchasePackage.setStatus(0);
								purchasePackage.setManager(node.getUser());
								purchasePackage.setCheckerType(0);
//								purchasePackage.setPackageName(currentTime.toString() + "   采购申请");
								purchasePackage.setPackageName(purchase.getGuikouDepartment().getOrganizeName() + "   采购计划");
								purchasePackage.setStartDate(currentTime);
								adddd =false;
							} else if (layer.getLayerName().equals("采购部领导审批") || layer.getLayerName().equals("财务预算审核")) {
								purchasePackage.setStatus(0);
								purchasePackage.setRoleId(node.getRoleId());
								purchasePackage.setCheckerType(1);
								purchasePackage.setPackageName(purchase.getGuikouDepartment().getOrganizeName() + "   采购计划");
								purchasePackage.setStartDate(currentTime);
								adddd =false;
							}
						}
					}
				}
				purchasePackage.setPurchaseInfors(purchaseInfors);
				this.purchasePackageDao.save(purchasePackage);
			}
		}

	}

}