package com.kwchina.oa.workflow.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kwchina.core.base.vo.RoleInforVo;
import com.kwchina.oa.workflow.vo.*;
import javassist.expr.NewArray;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.google.common.collect.Lists;
import com.kwchina.core.base.entity.OrganizeInfor;
import com.kwchina.core.base.entity.PersonInfor;
import com.kwchina.core.base.entity.RoleInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.ApproveSentenceManager;
import com.kwchina.core.base.service.OrganizeManager;
import com.kwchina.core.base.service.RoleManager;
import com.kwchina.core.base.service.SystemUserManager;
import com.kwchina.core.cms.entity.InforCategory;
import com.kwchina.core.cms.entity.InforComment;
import com.kwchina.core.cms.entity.InforDocument;
import com.kwchina.core.cms.service.InforCategoryManager;
import com.kwchina.core.cms.service.InforDocumentManager;
import com.kwchina.core.cms.util.DocumentConverter;
import com.kwchina.core.cms.util.InforConstant;
import com.kwchina.core.cms.vo.InforCommentVo;
import com.kwchina.core.common.page.PageList;
import com.kwchina.core.common.page.Pages;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.core.util.ArrayUtil;
import com.kwchina.core.util.DateHelper;
import com.kwchina.core.util.ExcelObject;
import com.kwchina.core.util.ExcelOperate;
import com.kwchina.core.util.file.FtpUtils;
import com.kwchina.core.util.file.LanUtil;
import com.kwchina.core.util.json.JSONConvert;
import com.kwchina.core.util.multisearch.ConditionUtils;
import com.kwchina.core.util.string.StringUtil;
import com.kwchina.extend.loginLog.entity.AppModuleLog;
import com.kwchina.extend.loginLog.entity.LoginLog;
import com.kwchina.extend.loginLog.service.AppModuleLogManager;
import com.kwchina.extend.supervise.entity.SuperviseInfor;
import com.kwchina.extend.supervise.entity.SuperviseReport;
import com.kwchina.extend.supervise.entity.TaskCategory;
import com.kwchina.oa.document.entity.DocumentCategory;
import com.kwchina.oa.document.entity.DocumentCategoryRight;
import com.kwchina.oa.document.entity.DocumentInfor;
import com.kwchina.oa.document.entity.DocumentInforRight;
import com.kwchina.oa.document.entity.DocumentInforUserRight;
import com.kwchina.oa.document.service.CategoryRightManager;
import com.kwchina.oa.document.service.DocumentCategoryManager;
import com.kwchina.oa.document.service.DocumentInforManager;
import com.kwchina.oa.document.service.DocumentInforRightManager;
import com.kwchina.oa.personal.message.entity.MessageInfor;
import com.kwchina.oa.personal.message.entity.ReceiveMessage;
import com.kwchina.oa.personal.message.service.MessageInforManager;
import com.kwchina.oa.submit.util.SubmitConstant;
import com.kwchina.oa.sys.PushUtil;
import com.kwchina.oa.util.MakeReceive;
import com.kwchina.oa.util.SysCommonMethod;
import com.kwchina.oa.workflow.ArchiveConnectionManager;
import com.kwchina.oa.workflow.FlowConnectionManager;
import com.kwchina.oa.workflow.customfields.service.CommonManager;
import com.kwchina.oa.workflow.customfields.util.CnToSpell;
import com.kwchina.oa.workflow.customfields.util.FlowConstant;
import com.kwchina.oa.workflow.entity.FlowDefinition;
import com.kwchina.oa.workflow.entity.FlowInstanceInfor;
import com.kwchina.oa.workflow.entity.InstanceCheckInfor;
import com.kwchina.oa.workflow.entity.InstanceInforRight;
import com.kwchina.oa.workflow.entity.InstanceInforRoleRight;
import com.kwchina.oa.workflow.entity.InstanceInforUserRight;
import com.kwchina.oa.workflow.entity.InstanceLayerInfor;
import com.kwchina.oa.workflow.exception.InstanceSuspendLayerException;
import com.kwchina.oa.workflow.service.FlowDefinitionManager;
import com.kwchina.oa.workflow.service.FlowInstanceManager;
import com.kwchina.oa.workflow.service.FlowLayerInforManager;
import com.kwchina.oa.workflow.service.InstanceInforRightManager;

import freemarker.template.Template;

@Controller
@RequestMapping("/workflow/instanceInfor.do")
public class InstanceController extends WorkflowBaseController {
	
	@Resource
	private FlowInstanceManager flowInstanceManager;
	
	@Resource
	private FlowDefinitionManager flowManager;
	
	@Resource
	private FlowLayerInforManager flowLayerInforManager;
	
	@Resource
	private CommonManager commonManager;
	
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

	//显示审核实例 
	@RequestMapping(params = "method=list")
	public void list(HttpServletRequest request, HttpServletResponse response)throws Exception {
		try {
			String flowId = request.getParameter("flowId");
			SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
			String flowName = "";

			// 构造查询语句
			String[] queryString = new String[2];
			queryString[0] = "from FlowInstanceInfor instance where 1=1";
			queryString[1] = "select count(instanceId) from FlowInstanceInfor instance where 1=1";
			String condition = "";

			// 所属流程Id
			FlowDefinition flow = new FlowDefinition();
			String tableName = "";
			if (flowId != null && flowId.length() > 0) {
				condition += " and flowDefinition.flowId = " + flowId;
				flow = (FlowDefinition) this.flowManager.get(Integer.valueOf(flowId));
				flowName = CnToSpell.getFullSpell(flow.getFlowName());
				tableName = "Customize_" + flowName;

				if (StringUtil.isNotEmpty(SysCommonMethod.getPlatform(request))) {
					/************记录app模块使用日志************/
					AppModuleLog appModuleLog = new AppModuleLog();

					int flowIdInt = Integer.valueOf(flowId);
					String moduleName = "发文管理";
					if (flowIdInt == 85) {
						moduleName = "发文管理";
					} else if (flowIdInt == 86) {
						moduleName = "合同审批";
					} else if (flowIdInt == 87) {
						moduleName = "内部报告";
					} else if (flowIdInt == 88) {
						moduleName = "制度评审";
					} else if (flowIdInt == 84) {
						moduleName = "收文管理";
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
			List columnNames = this.commonManager.getColumnNames(flowName);

			// 是否删除
			String deleted = request.getParameter("deleted");
			if (deleted != null && ("true").equals(deleted)) {
				condition += " and deleteFlag = 1";
			} else {
				condition += " and deleteFlag = 0";
			}

			/********************************/
			//获取董事会特定角色的角色信息（ID为固定）
			RoleInfor boardRole = (RoleInfor) roleManager.get(22);

			//获取合同审批特定角色的角色信息（ID为固定）
			RoleInfor contractRole = (RoleInfor) roleManager.get(24);

			//获取合同审批特定角色的角色信息（ID为固定）
			RoleInfor lawRole = (RoleInfor) roleManager.get(26);

			//可查看所有工作流的角色
			RoleInfor allRole = (RoleInfor) roleManager.get(32);

//		boolean isBoard  = roleManager.belongRole(systemUser, boardRole);
			boolean isContract = roleManager.belongRole(systemUser, contractRole);
			boolean isLaw = roleManager.belongRole(systemUser, lawRole);
			boolean isAll = roleManager.belongRole(systemUser, allRole);

//		if(isBoard){
//			
//		}

			/********************************/
//		合同查看角色
			if (isContract && ((flowId != null && flowId.length() > 0) && (flowId.equals("86") || flowId.equals("87")))) {

			} else if (isAll) {

			} else {

				// 申请人
				condition += " and (applier.personId = " + systemUser.getPersonId();

				// 部门审核人(A.未中止;B.中止前已审.)
				condition += " or ((manager.personId = " + systemUser.getPersonId() + " or viceManager.personId = " + systemUser.getPersonId() + ") and submiterWord is null" +
						" or (manager.personId = " + systemUser.getPersonId() + " and managerChecked = 1) or (viceManager.personId = " + systemUser.getPersonId() + " and viceManagerChecked = 1))";

				// 主办人
				condition += " or (charger.personId = " + systemUser.getPersonId() + " and startTime is not null)";

				// 如果本流程的归档人为固定角色时，同样有查看权限
				if (flow.getFilerType() == 1 && flow.getFileRole() != null) {
					String sysRoleStr = "";

					Set<RoleInfor> tmpRoleSet = systemUser.getRoles();
					List<RoleInfor> tmpRoleList = new ArrayList<RoleInfor>();

					//			for(Iterator it = tmpRoleSet.iterator();it.hasNext();){
					//				tmpRoleList.add((RoleInfor)it.next());
					//			}
					tmpRoleList.addAll(tmpRoleSet);
					for (int i = 0; i < tmpRoleList.size(); i++) {
						sysRoleStr += tmpRoleList.get(i).getRoleId().toString();
						if (i < tmpRoleList.size() - 1) {
							sysRoleStr += ",";
						}
					}

					if (!sysRoleStr.equals("") && sysRoleStr != null) {
						condition += " or (instance.flowDefinition.fileRole.roleId in (" + sysRoleStr + "))";
					}

				}

				// 审核人
				condition += " or (instanceId in (select layer.instance.instanceId from InstanceLayerInfor layer where layerId in " +
						"(select checkInfor.layerInfor.layerId from InstanceCheckInfor checkInfor where checkInfor.status = 1 and checkInfor.checker.personId = " + systemUser.getPersonId() + " )))";

				//	权限表中设置的浏览权限
				condition += " or (instanceId in (select userRight.instance.instanceId from InstanceInforUserRight userRight where userRight.systemUser.personId =" + systemUser.getPersonId() + "))";
//			condition += " or (instanceId in " + "(select roleRight.instance.instanceId from InstanceInforRoleRight roleRight,SystemUserInfor user where roleRight.role in " + "elements(user.roles) and user.personId = " + systemUser.getPersonId() + "))";

//			condition += " or (exists (select userRight.instance.instanceId from InstanceInforUserRight userRight where userRight.instance.instanceId=instance.instanceId and userRight.systemUser.personId =" + systemUser.getPersonId() + "))";
//			condition += " or (exists (select roleRight.instance.instanceId from InstanceInforRoleRight roleRight,SystemUserInfor user where roleRight.instance.instanceId=instance.instanceId and roleRight.role in elements(user.roles) and user.personId = " + systemUser.getPersonId() + "))";

//			condition += " or (exists (select userRight.instance.instanceId from InstanceInforUserRight userRight where userRight.instance.instanceId=instance.instanceId and userRight.systemUser.personId =" + systemUser.getPersonId() + "))";
//			condition += " or (exists (select roleRight.instance.instanceId from InstanceInforRoleRight roleRight,SystemUserInfor user where roleRight.instance.instanceId=instance.instanceId and roleRight.role in elements(user.roles) and user.personId = " + systemUser.getPersonId() + "))";

//			condition += " or (exists (select userRight.dataId from InstanceInforUserRight userRight inner join fetch userRight.instance where userRight.instance.instanceId=instance.instanceId and userRight.systemUser.personId =" + systemUser.getPersonId() + "))";
//			condition += " or (exists (select roleRight.dataId from InstanceInforRoleRight roleRight inner join fetch roleRight.instance,SystemUserInfor user where roleRight.instance.instanceId=instance.instanceId and roleRight.role in elements(user.roles) and user.personId = " + systemUser.getPersonId() + "))";
				if (flowId.equals("86")) {

					// 添加：部门中设定的部门经理，可查看本部门发起的所有合同流转
					condition += " or (department.organizeId in(select organizeId from OrganizeInfor where director.personId=" + systemUser.getPersonId() + "))";

					//2018.03.26 yuye(32)可以看到liusheng(154)的所有合同
					if(systemUser.getPersonId() == 32){
						// 申请人
						condition += " or (applier.personId = 154)";

						//部门审核人
						condition += " or ((manager.personId = " + 154 + " or viceManager.personId = " + 154 + ") and submiterWord is null" +
								" or (manager.personId = " + 154 + " and managerChecked = 1) or (viceManager.personId = " + 154 + " and viceManagerChecked = 1))";
						// 主办人
						condition += " or (charger.personId = " + 154 + " and startTime is not null)";

						// 审核人
						condition += " or (instanceId in (select layer.instance.instanceId from InstanceLayerInfor layer where layerId in " +
								"(select checkInfor.layerInfor.layerId from InstanceCheckInfor checkInfor where checkInfor.checker.personId = " + 154 + " )))";

						//权限列表
						condition += " or (instanceId in (select userRight.instance.instanceId from InstanceInforUserRight userRight where userRight.systemUser.personId =" + 154 + "))";


					}
				}

				condition += ") ";
			}
			queryString[0] += condition;
			queryString[1] += condition;

			//获取自定义表单查询字段
			Map map = getSearchFields(request, response);
			JSONArray searchFields = (JSONArray) map.get("_SearchFields");

			//Map maps = new HashMap();
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
				List instancIds = this.flowInstanceManager.getResultBySQLQuery(appsql);
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
					List instancIds = this.flowInstanceManager.getResultBySQLQuery(sql);
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

			queryString[0] += " order by " + params[0] + " " + params[1];

			//queryString = this.flowInstanceManager.generateQueryString(queryString, getSearchParams(request));

			String page = request.getParameter("page"); // 当前页
			String rowsNum = request.getParameter("rows"); // 每页显示的行数

			Pages pages = new Pages(request);
			pages.setPage(Integer.valueOf(page));
			pages.setPerPageNum(Integer.valueOf(rowsNum));

			List listCount = this.flowInstanceManager.getResultByQueryString(queryString[1]);
			String ss = listCount.get(0).toString();

			Pages pagesExcel = new Pages(request);
			pagesExcel.setPage(Integer.valueOf(page));
			pagesExcel.setPerPageNum(Integer.valueOf(ss) + 1);
			List list = new ArrayList();
			JSONObject jsonObj = new JSONObject();

			// 定义rows，存放数据
			JSONArray rows = new JSONArray();
			PageList plExcel = new PageList();
			if (params[2].equals("true") && isLaw && flowId.equals("1000")) {
				plExcel = this.flowInstanceManager.getResultByQueryString(queryString[0], queryString[1], true, pagesExcel);

				list = plExcel.getObjectList();
				// plExcel.setPages(pages);
				// plExcel.setPageShowString(pageShowString);
				// 定义返回的数据类型：json，使用了json-lib
				//pagesExcel.setPage(Integer.valueOf(page));
				//pagesExcel.setPerPageNum(Integer.valueOf(rowsNum));

				jsonObj.put("page", plExcel.getPages().getCurrPage()); // 当前页(名称必须为page)
				jsonObj.put("total", plExcel.getPages().getTotalPage()); // 总页数(名称必须为total)
				jsonObj.put("records", plExcel.getPages().getTotals()); // 总记录数(名称必须为records)
				//List list = pl.getObjectList();
				//plExcel.setPages(pagesExcel);

			} else {
				PageList pl = this.flowInstanceManager.getResultByQueryString(queryString[0], queryString[1], true, pages);
				list = pl.getObjectList();

				// 定义返回的数据类型：json，使用了json-lib

				jsonObj.put("page", pl.getPages().getCurrPage()); // 当前页(名称必须为page)
				jsonObj.put("total", pl.getPages().getTotalPage()); // 总页数(名称必须为total)
				jsonObj.put("records", pl.getPages().getTotals()); // 总记录数(名称必须为records)
			}


			//取需要的字段信息返回
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Iterator it = list.iterator();
			while (it.hasNext()) {
				FlowInstanceInfor fi = (FlowInstanceInfor) it.next();

				boolean canAdd = false;

				// 判断是否为当前审核人
				InstanceCheckInfor tmpCheck = this.flowInstanceManager.isChecker(fi, systemUser);
				if (isLaw) {
					canAdd = true;
				} else if (tmpCheck != null) {
					// 能否看见
					if (tmpCheck.getEndDate() != null
							|| (tmpCheck.getEndDate() == null
							&& tmpCheck.getLayerInfor().getStatus() != InstanceLayerInfor.Layer_Status_End
							&& tmpCheck.getLayerInfor().getStatus() != InstanceLayerInfor.Layer_Status_Suspended
							&& !fi.isSuspended())) {
						canAdd = true;
					}
				} else {
					canAdd = true;
				}


				if (canAdd) {
					JSONObject obj = new JSONObject();
					obj.put("instanceId", fi.getInstanceId());
					obj.put("instanceTitle", fi.getInstanceTitle());
					String startTime = " ";
					if (fi.getStartTime() != null) {
						startTime = sdf.format(fi.getStartTime());
					}
					obj.put("startTime", startTime);
					String checker = " ";
					if(fi.getManager() != null) {
						checker = fi.getManager().getPerson().getPersonName();
					}

					obj.put("checker", checker);
					obj.put("updateTime", sdf.format(fi.getUpdateTime()));
					obj.put("applier", fi.getApplier().getPerson().getPersonName());
					obj.put("applierPhoto", StringUtil.isNotEmpty(fi.getApplier().getPerson().getPhotoAttachment()) ? fi.getApplier().getPerson().getPhotoAttachment() : "");
					obj.put("department", fi.getDepartment().getOrganizeName());
					obj.put("flow", fi.getFlowDefinition().getFlowName());
					//obj.put("charger", fi.getFlowDefinition().getCharger().getPerson().getPersonName());
					if (fi.getCharger() != null) {
						obj.put("charger", fi.getCharger().getPerson().getPersonName());
					} else {

						obj.put("charger", null);
					}
					String status = "草稿";
					if (fi.isFiled()) {
						status = "已归档";
					} else if (fi.getEndTime() != null) {
						status = "已结束待归档";
					} else if (fi.isSuspended()) {
						status = "已暂停";
					} else if (fi.getStamped()==1) {
						status = "申请盖章";
					}else if (fi.getStamped()==2) {
						status = "已盖章";
					}else if (fi.getStartTime() != null) {

						//获取当前处理层,并判断处理情况:进行中,已完毕
						String layerStatus = "";
						List currentLayers = this.flowInstanceManager.getCurrentProcessLayers(fi);
						int i = 0;
						for (Iterator cl = currentLayers.iterator(); cl.hasNext(); ) {
							InstanceLayerInfor layer = (InstanceLayerInfor) cl.next();
							if (layer.getStatus() == InstanceLayerInfor.Layer_Status_Normal) {
								layerStatus += layer.getLayerName();
							} else if (layer.getStatus() == InstanceLayerInfor.Layer_Status_End) {
								layerStatus += layer.getLayerName() + "已暂停";
							} else if (layer.getStatus() == InstanceLayerInfor.Layer_Status_Suspended) {
								layerStatus += layer.getLayerName() + "已终止";
							} else if (layer.getStatus() == InstanceLayerInfor.Layer_Status_Finished) {
								layerStatus += layer.getLayerName() + "已完毕";
							}

							if (i != currentLayers.size() - 1) {
								layerStatus += ",";
							}

							i++;
						}
						status = layerStatus;
					} else if (fi.getManager() !=null && fi.getViceManager() == null && fi.isManagerChecked() || fi.getViceManager() != null && fi.getManager() == null && fi.isViceManagerChecked() || fi.getManager() != null && fi.getViceManager() != null && fi.isManagerChecked() && fi.isViceManagerChecked() && fi.getSubmiterWord() == null) {
						status = "部门已审核";
					} else if ((fi.getManager() != null || fi.getViceManager() != null) && fi.getSubmiterWord() == null) {
						status = "部门审核中";
					}
					obj.put("status", status);
					obj.put("attach", fi.getAttach());

					/** 自定义表单数据 */
					if (columnNames != null && columnNames.size() > 0) {
						List customizeDatas = this.commonManager.getFormData(flowName, fi.getInstanceId());
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
			}
			jsonObj.put("rows", rows); // 返回到前台每页显示的数据(名称必须为rows)
//			System.out.println(request.getParameter("nd"));
			// 设置字符编码
			response.setContentType(CoreConstant.CONTENT_TYPE);
			response.getWriter().print(jsonObj);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
//	显示审核实例
	@RequestMapping(params = "method=list_File")
	public void list_File(HttpServletRequest request, HttpServletResponse response)throws Exception {
		try {
			SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);

			List list = new ArrayList();
			JSONObject jsonObj = new JSONObject();

			// 定义rows，存放数据
			JSONArray rows = new JSONArray();
			PageList plExcel = new PageList();

			String[] queryString = new String[2];
			queryString[0] = "from FlowInstanceInfor instance where 1=1";
			queryString[1] = "select count(instanceId) from FlowInstanceInfor instance where 1=1";
			String condition = " and deleteFlag = 0 and flowDefinition.flowId>89 and flowDefinition.flowId<101";


			//可查看所有工作流的角色
			RoleInfor allRole = (RoleInfor) roleManager.get(32);
			RoleInfor allViewFile = (RoleInfor) roleManager.get(53);


			boolean isAll = roleManager.belongRole(systemUser, allRole);
			boolean isAllFile = roleManager.belongRole(systemUser, allViewFile);

			if(isAll || isAllFile){

			}else {
				// 申请人
				condition += " and (applier.personId = " + systemUser.getPersonId();

				// 部门审核人(A.未中止;B.中止前已审.)
				condition += " or ((manager.personId = " + systemUser.getPersonId() + " or viceManager.personId = " + systemUser.getPersonId() + ") and submiterWord is null" +
						" or (manager.personId = " + systemUser.getPersonId() + " and managerChecked = 1) or (viceManager.personId = " + systemUser.getPersonId() + " and viceManagerChecked = 1))";

				// 主办人
				condition += " or (charger.personId = " + systemUser.getPersonId() + " and startTime is not null)";

				// 审核人
				condition += " or (instanceId in (select layer.instance.instanceId from InstanceLayerInfor layer where layerId in " +
						"(select checkInfor.layerInfor.layerId from InstanceCheckInfor checkInfor where checkInfor.status in (0,1,2) and checkInfor.checker.personId = " + systemUser.getPersonId() + " )))";

				//	权限表中设置的浏览权限
				condition += " or (instanceId in (select userRight.instance.instanceId from InstanceInforUserRight userRight where userRight.systemUser.personId =" + systemUser.getPersonId() + "))";

				// 添加：部门中设定的部门经理，可查看本部门发起的所有合同流转
				condition += " or (department.organizeId in(select organizeId from OrganizeInfor where director.personId=" + systemUser.getPersonId() + "))";


				condition += ") ";
			}

//			String conditionOr = " order by instance.instanceId desc";
			queryString[0] += condition;
//			queryString[0] += conditionOr;
			queryString[1] += condition;

			//获取自定义表单查询字段

			Map map = getSearchFields(request, response);
			JSONArray searchFields = (JSONArray) map.get("_SearchFields");
			//构造查询条件
			String[] params = getSearchParams(request);
			String cusCondition = "";    //自定义查询条件
			String tableName = "";
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
							if(fieldValue!=null && fieldValue.equals("type")){
								tableName = dataValue;
								continue;
							}
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
			boolean noneFlag1 = true;
			boolean noneFlag2 = true;
			boolean noneFlag3 = true;
			boolean noneFlag4 = true;
			boolean noneFlag5 = true;
			boolean noneFlag6 = true;
			boolean noneFlag7 = true;
			boolean noneFlag8 = true;
			boolean noneFlag9 = true;
			String tableName1 = "Customize_Xinzengwenjian";
			String tableName2 = "Customize_Xiugaiwenjian";
			String tableName3 = "Customize_Xiaohuiwenjian";
			String tableName4 = "Customize_Xinzengliucheng";
			String tableName5 = "Customize_Xiugailiucheng";
			String tableName6 = "Customize_Xiaohuiliucheng";
			String tableName7 = "Customize_Xinzengwenjianliucheng";
			String tableName8 = "Customize_Xiugaiwenjianliucheng";
			String tableName9 = "Customize_Xiaohuiwenjianliucheng";

			queryTableType(tableName,cusCondition,queryString);

			String instanceIdsStrAll = "";
			if (cusCondition.length() > 0 && tableName1.length() > 0) {
				String sql = "select instanceId from " + tableName1 + " where 1=1" + cusCondition;
				List instancIds = null;
				try{
					instancIds = this.flowInstanceManager.getResultBySQLQuery(sql);
				}catch (Exception e){
//					System.out.println("");
					instancIds = null;
				}

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
					instanceIdsStrAll += instancIdsStr;
//					queryString[0] += " and instanceId in(" + instancIdsStr + ")";
//					queryString[1] += " and instanceId in(" + instancIdsStr + ")";
				} else {
					// 没有满足查询条件的数据
					noneFlag1 = false;
				}
			}
			if (cusCondition.length() > 0 && tableName2.length() > 0) {
				String sql = "select instanceId from " + tableName2 + " where 1=1" + cusCondition;
				List instancIds = null;
				try{
					instancIds = this.flowInstanceManager.getResultBySQLQuery(sql);
				}catch (Exception e){
//					System.out.println("");
					instancIds = null;
				}
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
					if(noneFlag1){
						instanceIdsStrAll += ","+instancIdsStr ;

					}else {
						instanceIdsStrAll += instancIdsStr ;
					}

//					queryString[0] += " or instanceId in(" + instancIdsStr + ") and deleteFlag = 0";
//					queryString[1] += " or instanceId in(" + instancIdsStr + ") and deleteFlag = 0";
				} else {
					// 没有满足查询条件的数据
					noneFlag2 = false;
				}
			}
			if (cusCondition.length() > 0 && tableName3.length() > 0) {
				String sql = "select instanceId from " + tableName3 + " where 1=1" + cusCondition;
				List instancIds = null;
				try{
					instancIds = this.flowInstanceManager.getResultBySQLQuery(sql);
				}catch (Exception e){
//					System.out.println("");
					instancIds = null;
				}
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
					if(noneFlag2){
						instanceIdsStrAll += ","+instancIdsStr ;

					}else {
						instanceIdsStrAll += instancIdsStr ;
					}

				} else {
					// 没有满足查询条件的数据
					noneFlag3 = false;
				}
			}
			if (cusCondition.length() > 0 && tableName4.length() > 0) {
				String sql = "select instanceId from " + tableName4 + " where 1=1" + cusCondition;
				List instancIds = null;
				try{
					instancIds = this.flowInstanceManager.getResultBySQLQuery(sql);
				}catch (Exception e){
//					System.out.println("");
					instancIds = null;
				}
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
					if(noneFlag3){
						instanceIdsStrAll += ","+instancIdsStr ;

					}else {
						instanceIdsStrAll += instancIdsStr ;
					}

				} else {
					// 没有满足查询条件的数据
					noneFlag4 = false;
				}
			}
			if (cusCondition.length() > 0 && tableName5.length() > 0) {
				String sql = "select instanceId from " + tableName5 + " where 1=1" + cusCondition;
				List instancIds = null;
				try{
					instancIds = this.flowInstanceManager.getResultBySQLQuery(sql);
				}catch (Exception e){
//					System.out.println("");
					instancIds = null;
				}
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
					if(noneFlag4){
						instanceIdsStrAll += ","+instancIdsStr ;

					}else {
						instanceIdsStrAll += instancIdsStr ;
					}

				} else {
					// 没有满足查询条件的数据
					noneFlag5 = false;
				}
			}
			if (cusCondition.length() > 0 && tableName6.length() > 0) {
				String sql = "select instanceId from " + tableName6 + " where 1=1" + cusCondition;
				List instancIds = null;
				try{
					instancIds = this.flowInstanceManager.getResultBySQLQuery(sql);
				}catch (Exception e){
//					System.out.println("");
					instancIds = null;
				}
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
					if(noneFlag5){
						instanceIdsStrAll += ","+instancIdsStr ;

					}else {
						instanceIdsStrAll += instancIdsStr ;
					}

				} else {
					// 没有满足查询条件的数据
					noneFlag6 = false;
				}
			}
			if (cusCondition.length() > 0 && tableName7.length() > 0) {
				String sql = "select instanceId from " + tableName7 + " where 1=1" + cusCondition;
				List instancIds = null;
				try{
					instancIds = this.flowInstanceManager.getResultBySQLQuery(sql);
				}catch (Exception e){
//					System.out.println("");
					instancIds = null;
				}
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
					if(noneFlag6){
						instanceIdsStrAll += ","+instancIdsStr ;

					}else {
						instanceIdsStrAll += instancIdsStr ;
					}

				} else {
					// 没有满足查询条件的数据
					noneFlag7 = false;
				}
			}
			if (cusCondition.length() > 0 && tableName8.length() > 0) {
				String sql = "select instanceId from " + tableName8 + " where 1=1" + cusCondition;
				List instancIds = null;
				try{
					instancIds = this.flowInstanceManager.getResultBySQLQuery(sql);
				}catch (Exception e){
//					System.out.println("");
					instancIds = null;
				}
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
					if(noneFlag7){
						instanceIdsStrAll += ","+instancIdsStr ;

					}else {
						instanceIdsStrAll += instancIdsStr ;
					}

				} else {
					// 没有满足查询条件的数据
					noneFlag8 = false;
				}
			}

			if (cusCondition.length() > 0 && tableName9.length() > 0) {
				String sql = "select instanceId from " + tableName9 + " where 1=1" + cusCondition;
				List instancIds = null;
				try{
					instancIds = this.flowInstanceManager.getResultBySQLQuery(sql);
				}catch (Exception e){
//					System.out.println("");
					instancIds = null;
				}
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
					if(noneFlag8){
						instanceIdsStrAll += ","+instancIdsStr ;

					}else {
						instanceIdsStrAll += instancIdsStr ;
					}

				} else {
					// 没有满足查询条件的数据
					noneFlag9 = false;
				}
			}
			if(instanceIdsStrAll.length()>0){
				queryString[0] += " and instanceId in(" + instanceIdsStrAll + ")";
				queryString[1] += " and instanceId in(" + instanceIdsStrAll + ")";
			}

			if(!noneFlag1 && !noneFlag2 && !noneFlag3 && !noneFlag4 && !noneFlag5 && !noneFlag6 && !noneFlag7 && !noneFlag8 && !noneFlag9){
				queryString[0] += " and instanceId in(0)";
				queryString[1] += " and instanceId in(0)";
			}

			queryString[0] += " order by " + params[0] + " " + params[1];


			String page = request.getParameter("page"); // 当前页
			String rowsNum = request.getParameter("rows"); // 每页显示的行数

			Pages pages = new Pages(request);
			pages.setPage(Integer.valueOf(page));
			pages.setPerPageNum(Integer.valueOf(rowsNum));
//			queryString[0] +=conditionOr;
			List listCount = this.flowInstanceManager.getResultByQueryString(queryString[1]);
			String ss = listCount.get(0).toString();

			Pages pagesExcel = new Pages(request);
			pagesExcel.setPage(Integer.valueOf(page));
			pagesExcel.setPerPageNum(Integer.valueOf(ss) + 1);

			plExcel = this.flowInstanceManager.getResultByQueryString(queryString[0], queryString[1], true, pages);

			list = plExcel.getObjectList();
			jsonObj.put("page", plExcel.getPages().getCurrPage()); // 当前页(名称必须为page)
			jsonObj.put("total", plExcel.getPages().getTotalPage()); // 总页数(名称必须为total)
			jsonObj.put("records", plExcel.getPages().getTotals()); // 总记录数(名称必须为records)

			//获取合同审批特定角色的角色信息（ID为固定）
			RoleInfor lawRole = (RoleInfor) roleManager.get(26);
			boolean isLaw = roleManager.belongRole(systemUser, lawRole);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			Iterator it = list.iterator();
			while (it.hasNext()) {
				FlowInstanceInfor fi = (FlowInstanceInfor) it.next();

				boolean canAdd = false;

				// 判断是否为当前审核人
				InstanceCheckInfor tmpCheck = this.flowInstanceManager.isChecker(fi, systemUser);


				if (isLaw) {
					canAdd = true;
				} else if (tmpCheck != null) {
					// 能否看见
					if (tmpCheck.getEndDate() != null
							|| (tmpCheck.getEndDate() == null
							&& tmpCheck.getLayerInfor().getStatus() != InstanceLayerInfor.Layer_Status_End
							&& tmpCheck.getLayerInfor().getStatus() != InstanceLayerInfor.Layer_Status_Suspended
							&& !fi.isSuspended())) {
						canAdd = true;
					}
				} else {
					canAdd = true;
				}


				if (canAdd) {
					JSONObject obj = new JSONObject();
					obj.put("instanceId", fi.getInstanceId());
					obj.put("instanceTitle", fi.getInstanceTitle());
					String startTime = " ";
					if (fi.getStartTime() != null) {
						startTime = sdf.format(fi.getStartTime());
					}
					obj.put("startTime", startTime);
					String checker = " ";
					if(fi.getManager() != null) {
						checker = fi.getManager().getPerson().getPersonName();
					}

					obj.put("checker", checker);
					obj.put("updateTime", sdf.format(fi.getUpdateTime()));
					obj.put("applier", fi.getApplier().getPerson().getPersonName());
					obj.put("applierPhoto", StringUtil.isNotEmpty(fi.getApplier().getPerson().getPhotoAttachment()) ? fi.getApplier().getPerson().getPhotoAttachment() : "");
					obj.put("department", fi.getDepartment().getOrganizeName());
					obj.put("flow", fi.getFlowDefinition().getFlowName());
					//obj.put("charger", fi.getFlowDefinition().getCharger().getPerson().getPersonName());
					if (fi.getCharger() != null) {
						obj.put("charger", fi.getCharger().getPerson().getPersonName());
					} else {
						obj.put("charger", null);
					}
					String status = "草稿";
					if (fi.isFiled()) {
						status = "已归档";
					} else if (fi.getEndTime() != null) {
						status = "已结束待归档";
					} else if (fi.isSuspended()) {
						status = "已暂停";
					}else if (fi.getStamped()==1) {
						status = "申请提交文档";
					}else if (fi.getStamped()==2) {
						status = "文档已提交";
					} else if (fi.getStartTime() != null) {

						//获取当前处理层,并判断处理情况:进行中,已完毕
						String layerStatus = "";
						List currentLayers = this.flowInstanceManager.getCurrentProcessLayers(fi);
						int i = 0;
						for (Iterator cl = currentLayers.iterator(); cl.hasNext(); ) {
							InstanceLayerInfor layer = (InstanceLayerInfor) cl.next();
							if (layer.getStatus() == InstanceLayerInfor.Layer_Status_Normal) {
								layerStatus += layer.getLayerName();
							} else if (layer.getStatus() == InstanceLayerInfor.Layer_Status_End) {
								layerStatus += layer.getLayerName() + "已暂停";
							} else if (layer.getStatus() == InstanceLayerInfor.Layer_Status_Suspended) {
								layerStatus += layer.getLayerName() + "已终止";
							} else if (layer.getStatus() == InstanceLayerInfor.Layer_Status_Finished) {
								layerStatus += layer.getLayerName() + "已完毕";
							}

							if (i != currentLayers.size() - 1) {
								layerStatus += ",";
							}

							i++;
						}
						status = layerStatus;
					} else if (fi.getManager() !=null && fi.getViceManager() == null && fi.isManagerChecked() || fi.getViceManager() != null && fi.getManager() == null && fi.isViceManagerChecked() || fi.getManager() != null && fi.getViceManager() != null && fi.isManagerChecked() && fi.isViceManagerChecked() && fi.getSubmiterWord() == null) {
						status = "部门已审核";
					} else if ((fi.getManager() != null || fi.getViceManager() != null) && fi.getSubmiterWord() == null) {
						status = "部门审核中";
					}
					obj.put("status", status);
					obj.put("attach", fi.getAttach());

					/** 自定义表单数据 */


					List customizeDatas = new ArrayList();
					Integer flowId = fi.getFlowDefinition().getFlowId();
					switch (flowId){
						case 90:
							customizeDatas = this.commonManager.getFormData("Xinzengwenjian", fi.getInstanceId());
							if (customizeDatas != null && customizeDatas.size() > 0) {
								Object[] dataArray = (Object[]) customizeDatas.get(0);
								obj.put("type","新增文件");
								obj.put("fid",dataArray[0]);
								obj.put("organizeIdw",dataArray[2]);
								obj.put("fileNo",dataArray[3]);
								obj.put("fileEdition",dataArray[4]);
								obj.put("compactor",dataArray[5]);
							}
							break;
						case 92:
							customizeDatas = this.commonManager.getFormData("Xiugaiwenjian", fi.getInstanceId());
							if (customizeDatas != null && customizeDatas.size() > 0) {
								Object[] dataArray = (Object[]) customizeDatas.get(0);
								obj.put("type","修改文件");
								obj.put("fid",dataArray[0]);
								obj.put("organizeIdw",dataArray[2]);
								obj.put("fileEdition",dataArray[3]);
								obj.put("fileUpdatePo",dataArray[4]);
								obj.put("fileNew",dataArray[5]);
								obj.put("beAff",dataArray[6]);
								obj.put("fileName",dataArray[7]);
								obj.put("compactor",dataArray[8]);
							}
							break;
						case 93:
							customizeDatas = this.commonManager.getFormData("Xiaohuiwenjian", fi.getInstanceId());
							if (customizeDatas != null && customizeDatas.size() > 0) {
								Object[] dataArray = (Object[]) customizeDatas.get(0);
								obj.put("type","销毁文件");
								obj.put("fid",dataArray[0]);
								obj.put("organizeIdw",dataArray[2]);
								obj.put("fileEdition",dataArray[3]);
								obj.put("fileDe",dataArray[4]);
								obj.put("fileName",dataArray[5]);
								obj.put("compactor",dataArray[6]);
							}
							break;
						case 94:
							customizeDatas = this.commonManager.getFormData("Xinzengliucheng", fi.getInstanceId());
							if (customizeDatas != null && customizeDatas.size() > 0) {
								Object[] dataArray = (Object[]) customizeDatas.get(0);
								obj.put("type","新增流程");
								obj.put("fid",dataArray[0]);
								obj.put("organizeIdl",dataArray[2]);
								obj.put("flowEdition",dataArray[3]);
								obj.put("flowSup",dataArray[4]);
								obj.put("flowName",dataArray[5]);
								obj.put("compactor",dataArray[6]);
							}
							break;
						case 95:
							customizeDatas = this.commonManager.getFormData("Xiugailiucheng", fi.getInstanceId());
							if (customizeDatas != null && customizeDatas.size() > 0) {
								Object[] dataArray = (Object[]) customizeDatas.get(0);
								obj.put("type","修改流程");
								obj.put("fid",dataArray[0]);
								obj.put("organizeIdl",dataArray[2]);
								obj.put("flowEdition",dataArray[3]);
								obj.put("flowSup",dataArray[4]);
								obj.put("flowUpdatePo",dataArray[5]);
								obj.put("flowNew",dataArray[6]);
								obj.put("beAffl",dataArray[7]);
								obj.put("flowName",dataArray[8]);
								obj.put("compactor",dataArray[9]);
							}
							break;
						case 96:
							customizeDatas = this.commonManager.getFormData("Xiaohuiliucheng", fi.getInstanceId());
							if (customizeDatas != null && customizeDatas.size() > 0) {
								Object[] dataArray = (Object[]) customizeDatas.get(0);
								obj.put("type","销毁流程");
								obj.put("fid",dataArray[0]);
								obj.put("organizeIdl",dataArray[2]);
								obj.put("flowEdition",dataArray[3]);
								obj.put("flowSup",dataArray[4]);
								obj.put("flowDe",dataArray[5]);
								obj.put("flowName",dataArray[6]);
								obj.put("compactor",dataArray[7]);
							}
							break;
						case 98:
							customizeDatas = this.commonManager.getFormData("Xinzengwenjianliucheng", fi.getInstanceId());
							if (customizeDatas != null && customizeDatas.size() > 0) {
								Object[] dataArray = (Object[]) customizeDatas.get(0);
								obj.put("type","新增文件流程");
								obj.put("fid",dataArray[0]);
								obj.put("organizeIdw",dataArray[2]);
								obj.put("organizeIdl",dataArray[3]);
								obj.put("fileNo",dataArray[4]);
								obj.put("fileEdition",dataArray[5]);
								obj.put("flowEdition",dataArray[6]);
								obj.put("flowSup",dataArray[7]);
								obj.put("flowName",dataArray[8]);
								obj.put("compactor",dataArray[9]);
							}
							break;
						case 99:
							customizeDatas = this.commonManager.getFormData("Xiugaiwenjianliucheng", fi.getInstanceId());
							if (customizeDatas != null && customizeDatas.size() > 0) {
								Object[] dataArray = (Object[]) customizeDatas.get(0);
								obj.put("type","修改文件流程");
								obj.put("fid",dataArray[0]);
								obj.put("organizeIdw",dataArray[2]);
								obj.put("fileEdition",dataArray[3]);
								obj.put("fileUpdatePo",dataArray[4]);
								obj.put("fileNew",dataArray[5]);
								obj.put("beAffw",dataArray[6]);
								obj.put("organizeIdl",dataArray[7]);
								obj.put("flowEdition",dataArray[8]);
								obj.put("flowSup",dataArray[9]);
								obj.put("flowUpdatePo",dataArray[10]);
								obj.put("flowNew",dataArray[11]);
								obj.put("beAffl",dataArray[12]);
								obj.put("fileName",dataArray[13]);
								obj.put("flowName",dataArray[14]);
								obj.put("compactor",dataArray[15]);
							}
							break;
						case 100:
							customizeDatas = this.commonManager.getFormData("Xiaohuiwenjianliucheng", fi.getInstanceId());
							if (customizeDatas != null && customizeDatas.size() > 0) {
								Object[] dataArray = (Object[]) customizeDatas.get(0);
								obj.put("type","销毁文件流程");
								obj.put("fid",dataArray[0]);
								obj.put("organizeIdw",dataArray[2]);
								obj.put("fileEdition",dataArray[3]);
								obj.put("fileDe",dataArray[4]);
								obj.put("organizeIdl",dataArray[5]);
								obj.put("flowEdition",dataArray[6]);
								obj.put("flowSup",dataArray[7]);
								obj.put("flowDe",dataArray[8]);
								obj.put("fileName",dataArray[9]);
								obj.put("flowName",dataArray[10]);
								obj.put("compactor",dataArray[11]);
							}
							break;
					}

					rows.add(obj);
				}
			}

			jsonObj.put("rows", rows); // 返回到前台每页显示的数据(名称必须为rows)
			// 设置字符编码
			response.setContentType(CoreConstant.CONTENT_TYPE);
			response.getWriter().print(jsonObj);
		} catch (Exception e) {
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
					List instancIds = this.flowInstanceManager.getResultBySQLQuery(sql);
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
					instancIds = this.flowInstanceManager.getResultBySQLQuery(sql);
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
					 instancIds = this.flowInstanceManager.getResultBySQLQuery(sql);
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
					 instancIds = this.flowInstanceManager.getResultBySQLQuery(sql);
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
					 instancIds = this.flowInstanceManager.getResultBySQLQuery(sql);
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
					 instancIds = this.flowInstanceManager.getResultBySQLQuery(sql);
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
					 instancIds = this.flowInstanceManager.getResultBySQLQuery(sql);
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
					 instancIds = this.flowInstanceManager.getResultBySQLQuery(sql);
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
					 instancIds = this.flowInstanceManager.getResultBySQLQuery(sql);
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
	
//	boolean isExport=true;
//	if (params[2].equals("true")&& isLaw && flowId.equals("1000")) {
//		
//		
//		 List<Map<String, String>> maps = (List<Map<String, String>>) JSONArray.toCollection(rows, Map.class);
//		
//		
//		
//		long time = System.currentTimeMillis();
//		String filePath ="/uploadfiles/submit/excel/";		
//		String fileTitle = "";
//		
//		
//			fileTitle = "contract" ;
//	
//		
//		ExcelObject object = new ExcelObject();
//		object.setFilePath(filePath);
//		object.setFileName(fileTitle);
//		object.setTitle(fileTitle);
//		
//		List rowName = new ArrayList();
//		String[][] data = new String[17][rows.size()];
//		int k = 0;//列数
//		
//	
//			rowName.add("序号");
//			rowName.add("归属公司");
//			rowName.add("合同编号");
//			rowName.add("合同名称");			
//			rowName.add("合同类型");
//			rowName.add("申请人");
//			rowName.add("状态");
//			rowName.add("合同相对方");
//			rowName.add("合同总金额");
//			rowName.add("合同时限");
//			rowName.add("经办部门");
//			rowName.add("会审部门");
//			rowName.add("批准人");
//			rowName.add("签订日期");
//			rowName.add("流水号");
//			rowName.add("备注");
//			k = 17;
//		
//		
//		
//		for (int i = 0; i < maps.size(); i++) {
//			Map map1 = new HashMap();
//			
//			map1 = maps.get(i);
//			
//			
//				
//				data[0][i] = String.valueOf(i+1);
//				data[1][i] = map1.get("category").toString();
//				data[2][i] = map1.get("contractNo").toString();
//				data[3][i] = map1.get("instanceTitle").toString();
//				data[4][i] = map1.get("contractType").toString();
//				data[5][i] = map1.get("applier").toString();
//				data[6][i] = map1.get("status").toString();
//				data[7][i] = map1.get("oppositeUnit").toString();
//				data[8][i] = map1.get("contractPrice").toString();
//				data[9][i] = map1.get("contractPeriod").toString();
//				data[10][i] = map1.get("department").toString();
//				data[11][i] = map1.get("involvedDeps").toString();
//				data[12][i] = map1.get("checker").toString();
//				data[13][i] = map1.get("startTime").toString();
//				data[14][i] = map1.get("indexNo").toString();
//				data[15][i] = map1.get("memo").toString();
//			
//				
//		
//		
//			
//		}
//			
//		for (int i = 0; i < k; i++) {
//			object.addContentListByList(data[i]);
//		}
//		object.setRowName(rowName);
//		ExcelOperate operate = new ExcelOperate();
//		try {
//			operate.exportExcel(object, plExcel.getObjectList().size(), true, request);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//
//		filePath = filePath + fileTitle +".xls";
//		request.getSession().setAttribute("_File_Path", filePath);
//		//return mapping.findForward("exportExcel");
//	}
	
	/**
	 * 导出excel
	 * @param
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=expertExcel")
	public String expertExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String flowId = request.getParameter("flowId");
			SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
			String flowName = "";
			
			// 构造查询语句
			String[] queryString = new String[2];
			queryString[0] = "from FlowInstanceInfor instance where 1=1";
			queryString[1] = "select count(instanceId) from FlowInstanceInfor instance where 1=1";
			String condition = "";
			
			// 所属流程Id
			FlowDefinition flow = new FlowDefinition();
			String tableName = "";
			if (flowId != null && flowId.length() > 0) {
				condition += " and flowDefinition.flowId = " + flowId;
				flow = (FlowDefinition)this.flowManager.get(Integer.valueOf(flowId));
				flowName = CnToSpell.getFullSpell(flow.getFlowName());
				tableName = "Customize_" + flowName;
			}
			
			//根据流程名获取表列名
			List columnNames = this.commonManager.getColumnNames(flowName);
			
			// 是否删除
			String deleted = request.getParameter("deleted");
			if (deleted != null && ("true").equals(deleted)) {
				condition += " and deleteFlag = 1";
			}else {
				condition += " and deleteFlag = 0";
			}

			/********************************/
			//获取董事会特定角色的角色信息（ID为固定）
			RoleInfor boardRole = (RoleInfor)roleManager.get(22);
			
			//获取合同审批特定角色的角色信息（ID为固定）
			RoleInfor contractRole = (RoleInfor)roleManager.get(24);
			
			//获取合同审批特定角色的角色信息（ID为固定）
			RoleInfor lawRole = (RoleInfor)roleManager.get(26);
			
			//可查看所有工作流的角色
			RoleInfor allRole = (RoleInfor)roleManager.get(32);
			
//			boolean isBoard  = roleManager.belongRole(systemUser, boardRole);
			boolean isContract = roleManager.belongRole(systemUser, contractRole);
			boolean isLaw = roleManager.belongRole(systemUser, lawRole);
			boolean isAll = roleManager.belongRole(systemUser, allRole);
			

			
			/********************************/
//			合同查看角色
			if(isContract && ((flowId != null && flowId.length() > 0) && (flowId.equals("86")||flowId.equals("87")))){
				
			}else if(isAll){
				
			}else{
			
				// 申请人
				condition += " and (applier.personId = " + systemUser.getPersonId();
				
				// 部门审核人(A.未中止;B.中止前已审.)
				condition += " or ((manager.personId = " + systemUser.getPersonId() + " or viceManager.personId = " + systemUser.getPersonId() + ") and submiterWord is null" +
						" or (manager.personId = " + systemUser.getPersonId() + " and managerChecked = 1) or (viceManager.personId = " + systemUser.getPersonId() + " and viceManagerChecked = 1))";
				
				// 主办人
				condition += " or (charger.personId = " + systemUser.getPersonId() + " and startTime is not null)";
				
				
				// 如果本流程的归档人为固定角色时，同样有查看权限
				if(flow.getFilerType() == 1 && flow.getFileRole() != null){
					String sysRoleStr = "";
					
					Set<RoleInfor> tmpRoleSet = systemUser.getRoles();
					List<RoleInfor> tmpRoleList = new ArrayList<RoleInfor>();
					tmpRoleList.addAll(tmpRoleSet);
					for(int i=0;i<tmpRoleList.size();i++){
						sysRoleStr += tmpRoleList.get(i).getRoleId().toString();
						if(i<tmpRoleList.size()-1){
							sysRoleStr += ",";
						}
					}
					
					if(!sysRoleStr.equals("") && sysRoleStr != null){
						condition += " or (instance.flowDefinition.fileRole.roleId in ("+sysRoleStr+"))";
					}
					
				}
				

				// 审核人
				condition += " or (instanceId in (select layer.instance.instanceId from InstanceLayerInfor layer where layerId in " +
						"(select checkInfor.layerInfor.layerId from InstanceCheckInfor checkInfor where checkInfor.checker.personId = " + systemUser.getPersonId() + " )))";
				
				//	权限表中设置的浏览权限
				condition += " or (instanceId in (select userRight.instance.instanceId from InstanceInforUserRight userRight where userRight.systemUser.personId =" + systemUser.getPersonId() + "))";
				
				if(flowId.equals("86")){
					// 添加：部门中设定的部门经理，可查看本部门发起的所有合同流转
					condition += " or (department.organizeId in(select organizeId from OrganizeInfor where director.personId="+systemUser.getPersonId()+"))";
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
			String cusCondition	= "";	//自定义查询条件
			if (params[2].equals("true")) {
				if (params[3] != null && params[3].length() > 0) {
					
					JSONObject filter = JSONObject.fromObject(params[3]);
					JSONArray rules = filter.getJSONArray("rules");		//取数据中的查询信息:查询字段,查询条件,查询数据
					if (rules != null && rules.size() > 0) {
						for (int i=0;i<rules.size();i++) {
							JSONObject tmpObj = (JSONObject)rules.get(i);
							String fieldValue = tmpObj.getString("field");	//查询字段
							String opValue = tmpObj.getString("op");		//查询条件:大于,等于,小于..
							String dataValue = tmpObj.getString("data");	//查询数据
							
							//处理查询条件
							boolean has = false;
							if (searchFields != null && searchFields.size() > 0) {
								for (int j=0;j<searchFields.size();j++) {
									JSONArray fieldArray = (JSONArray) searchFields.get(j);
									JSONObject tmpField = (JSONObject)fieldArray.get(1);
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
				List instancIds = this.flowInstanceManager.getResultBySQLQuery(sql);
				if (instancIds != null && instancIds.size() > 0) {
					String instancIdsStr = "";
					int index = 0;
					for (Iterator it=instancIds.iterator();it.hasNext();) {
						instancIdsStr += ((Integer)it.next()).toString();
						if (index < instancIds.size()-1) {
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
			
			queryString[0] += " order by " + params[0] + " " + params[1];
			

			String page = request.getParameter("page"); // 当前页
			String rowsNum = request.getParameter("rows"); // 每页显示的行数
			
			Pages pages = new Pages(request);
			pages.setPage(Integer.valueOf(page));
			pages.setPerPageNum(Integer.valueOf(rowsNum));
			
			List list = new ArrayList();
			JSONObject jsonObj = new JSONObject();

			// 定义rows，存放数据
			JSONArray rows = new JSONArray();
			PageList pl = this.flowInstanceManager.getResultByQueryString(queryString[0], queryString[1], true, pages);
			list = pl.getObjectList();
			
			
			
			/******************导出Excel********************/
			boolean isContractFlow = flowId.equals(String.valueOf(SubmitConstant.SubmitFlow_Report_Contract));
			boolean isPublishFlow = flowId.equals(String.valueOf(SubmitConstant.SubmitFlow_Report_Publish));
			boolean isReceiveFlow = flowId.equals(String.valueOf(SubmitConstant.SubmitFlow_Report_Receive));
			
			long time = System.currentTimeMillis();
//			String filePath = SystemConstant.Submit_Path + time + "/";
			String filePath = "/"+CoreConstant.Attachment_Path + "supervise/";
			String fileTitle = "合同审批";
			if(flowId.equals(String.valueOf(SubmitConstant.SubmitFlow_Report_Publish))){   // 发文
				fileTitle = "发文管理";
			}else if(flowId.equals(String.valueOf(SubmitConstant.SubmitFlow_Report_Receive))){   // 收文
				fileTitle = "收文管理";
			}

			ExcelObject object = new ExcelObject();
			object.setFilePath(filePath);
			object.setFileName(fileTitle);
			object.setTitle(fileTitle);

			List rowName = new ArrayList();
			String[][] data = new String[21][list.size()];
			int k = 0;// 列数

			// 合同审批
			if(isContractFlow){ 
				rowName.add("序号");
				rowName.add("合同名称");	
				rowName.add("归属公司");
				rowName.add("流水号");
				rowName.add("合同编号");
				rowName.add("合同类型");
				rowName.add("是否使用了公司合同模板");
				rowName.add("我方对外签署名称");
				rowName.add("对方单位");
				rowName.add("履约期限");
				rowName.add("合同金额");
				rowName.add("涉及部门");
				rowName.add("经办人");
				rowName.add("合同说明");
				rowName.add("创建时间");
				rowName.add("申请人");
				rowName.add("所属部门");
				rowName.add("状态");
				rowName.add("所含附件");
				k = 21;
			}
			
			// 发文管理
			if(isPublishFlow){
				rowName.add("序号");
				rowName.add("正文标题");	
				rowName.add("发文日期");
				rowName.add("流水号");
				rowName.add("发文号");
				rowName.add("主办科室");
				rowName.add("拟稿人");
				rowName.add("催办等级");
				rowName.add("校对");
				rowName.add("主题词");
				rowName.add("主送");
				rowName.add("抄送");
				rowName.add("创建时间");
				rowName.add("申请人");
				rowName.add("所属部门");
				rowName.add("状态");
				k = 18;
			}
			
			// 收文管理
			if(isReceiveFlow){
				rowName.add("序号");
				rowName.add("正文标题");	
				rowName.add("收文日期");
				rowName.add("收文号");
				rowName.add("收件人");		
				rowName.add("文件字号");
				rowName.add("文件日期");
				rowName.add("份数");
				rowName.add("密级");
				rowName.add("催办等级");
				rowName.add("来文单位");
				rowName.add("来文部门");
				rowName.add("创建时间");
				rowName.add("申请人");
				rowName.add("所属部门");
				rowName.add("状态");
				k = 18;
			}

			//取需要的字段信息返回
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (int i = 0; i < list.size(); i++) {
				FlowInstanceInfor fi = (FlowInstanceInfor)list.get(i);
				
				data[0][i] = String.valueOf(i + 1);
				data[1][i] = fi.getInstanceTitle();
				
				/** 自定义表单数据 */
				if (columnNames != null && columnNames.size() > 0) {
					List customizeDatas = this.commonManager.getFormData(flowName, fi.getInstanceId());
					if (customizeDatas != null && customizeDatas.size() > 0) {
						Object[] dataArray = (Object[])customizeDatas.get(0);
						
						// 合同审批
						if(isContractFlow){
							int m=0;
							for (Iterator col=columnNames.iterator();col.hasNext();) {
								String colName = (String)col.next();
								String colValue = dataArray[m].toString();
								
								if(colName.equals("category")){
									data[2][i] = colValue;
								}
								if(colName.equals("indexNo")){
									data[3][i] = colValue;
								}
								if(colName.equals("contractNo")){
									data[4][i] = colValue;
								}
								if(colName.equals("contractType")){
									data[5][i] = colValue;
								}
								if(colName.equals("useModel")){
									data[6][i] = colValue;
								}
								if(colName.equals("foreignName")){
									data[7][i] = colValue;
								}
								if(colName.equals("oppositeUnit")){
									data[8][i] = colValue;
								}
								if(colName.equals("contractPeriod")){
									data[9][i] = colValue;
								}
								if(colName.equals("contractPrice")){
									data[10][i] = colValue;
								}
								if(colName.equals("involvedDeps")){
									data[11][i] = colValue;
								}
								if(colName.equals("attner")){
									data[12][i] = colValue;
								}
								if(colName.equals("memo")){
									data[13][i] = colValue;
								}
								m++;
							}
						}
						
						// 发文管理
						if(isPublishFlow){
							int m=0;
							for (Iterator col=columnNames.iterator();col.hasNext();) {
								String colName = (String)col.next();
								String colValue = dataArray[m].toString();
								
								if(colName.equals("sendDate")){
									data[2][i] = colValue;
								}
								if(colName.equals("documentNo")){
									data[3][i] = colValue;
								}
								if(colName.equals("organizeId")){
									if(colValue != null && !colValue.equals("") && !colValue.equals("0")){
										OrganizeInfor organize = (OrganizeInfor)this.organizeManager.get(Integer.valueOf(colValue));
										data[4][i] = organize.getOrganizeName();
									}else {
										data[4][i] = "";
									}
									
								}
								if(colName.equals("writerId")){
									if(colValue != null && !colValue.equals("") && !colValue.equals("0")){
										SystemUserInfor tmpUser = (SystemUserInfor)this.systemUserManager.get(Integer.valueOf(colValue));
										data[5][i] = tmpUser.getPerson().getPersonName();
									}else {
										data[5][i] = "";
									}
									
								}
								if(colName.equals("urgencyName")){
									data[6][i] = colValue;
								}
								if(colName.equals("reviewerId")){
									if(colValue != null && !colValue.equals("") && !colValue.equals("0")){
										SystemUserInfor tmpUser = (SystemUserInfor)this.systemUserManager.get(Integer.valueOf(colValue));
										data[7][i] = tmpUser.getPerson().getPersonName();
									}else {
										data[7][i] = "";
									}
								}
								if(colName.equals("keywords")){
									data[8][i] = colValue;
								}
								if(colName.equals("sendPersons")){
									data[9][i] = colValue;
								}
								if(colName.equals("copyPersons")){
									data[10][i] = colValue;
								}
								m++;
							}
						}
						
						// 收文管理
						if(isReceiveFlow){
							int m=0;
							for (Iterator col=columnNames.iterator();col.hasNext();) {
								String colName = (String)col.next();
								String colValue = dataArray[m].toString();
								
								if(colName.equals("receiveDate")){
									data[2][i] = colValue;
								}
								if(colName.equals("documentNo")){
									data[3][i] = colValue;
								}
								if(colName.equals("receiverId")){
									if(colValue != null && !colValue.equals("") && !colValue.equals("0")){
										SystemUserInfor tmpUser = (SystemUserInfor)this.systemUserManager.get(Integer.valueOf(colValue));
										data[4][i] = tmpUser.getPerson().getPersonName();
									}else {
										data[4][i] = "";
									}
								}
								if(colName.equals("reportNo")){
									data[5][i] = colValue;
								}
								if(colName.equals("fileDate")){
									data[6][i] = colValue;
								}
								if(colName.equals("fileNum")){
									data[7][i] = colValue;
								}
								if(colName.equals("secretName")){
									data[8][i] = colValue;
								}
								if(colName.equals("urgencyName")){
									data[9][i] = colValue;
								}
								if(colName.equals("unitName")){
									data[10][i] = colValue;
								}
								if(colName.equals("selUnitName")){
									data[11][i] = colValue;
								}
								m++;
							}
						}
						
						
					}
				}
				
				
				String status = "草稿";
				if (fi.isFiled()) {
					status = "已归档";
				}else if (fi.getEndTime() != null) {
					status = "已结束待归档";
				}else if (fi.isSuspended()) {
					status = "已暂停";
				}else if (fi.getStartTime() != null) {
					
					//获取当前处理层,并判断处理情况:进行中,已完毕
					String layerStatus = "";
					List currentLayers = this.flowInstanceManager.getCurrentProcessLayers(fi);
					int n = 0;
					for (Iterator cl=currentLayers.iterator();cl.hasNext();) {
						InstanceLayerInfor layer = (InstanceLayerInfor)cl.next();
						if (layer.getStatus() == InstanceLayerInfor.Layer_Status_Normal) {
							layerStatus += layer.getLayerName();
						}else if (layer.getStatus() == InstanceLayerInfor.Layer_Status_End) {
							layerStatus += layer.getLayerName() + "已暂停";
						}else if (layer.getStatus() == InstanceLayerInfor.Layer_Status_Suspended) {
							layerStatus += layer.getLayerName() + "已终止";
						}else if (layer.getStatus() == InstanceLayerInfor.Layer_Status_Finished) {
							layerStatus += layer.getLayerName() + "已完毕";
						}
						
						if (n != currentLayers.size()-1) {
							layerStatus += ",";
						}
						
						n++;
					}
					status = layerStatus;
				}else if (fi.getManager() !=null && fi.getViceManager() == null && fi.isViceManagerChecked() || fi.getViceManager() != null && fi.getManager() == null && fi.isViceManagerChecked() || fi.getManager() != null && fi.getViceManager() != null && fi.isManagerChecked() && fi.isViceManagerChecked() && fi.getSubmiterWord() == null) {
					status = "部门已审核";
				}else if ((fi.getManager() != null || fi.getViceManager() != null) && fi.getSubmiterWord() == null) {
					status = "部门审核中";
				}
				
				// 合同审批
				if(isContractFlow){ 
					data[14][i] = sdf.format(fi.getUpdateTime());
					data[15][i] = fi.getApplier().getPerson().getPersonName();
					data[16][i] = fi.getDepartment().getOrganizeName();
					data[17][i] = status;
					
					//附件,加超链接
					String hyperLinkStr = "";
					String tmpAttach = fi.getAttach();
					if(StringUtil.isNotEmpty(tmpAttach)){
						String[] attachList = tmpAttach.split("\\|");
						for(int m=0;m<attachList.length;m++){
							String tmpAttachLink = attachList[m];
//							hyperLinkStr += "http://htoa.haitongauto.com/common/download.jsp?filepath=" + tmpAttachLink;
							hyperLinkStr += "http://htoa.haitongauto.com/download.do?instanceId="+fi.getInstanceId()+"&filePathIndex=" + m + "&filePath=" + tmpAttachLink;
							if(m < attachList.length - 1){
								hyperLinkStr += "|";
							}
						}
					}
					data[18][i] = "hyperlink-" + hyperLinkStr;
				}
				
				// 发文管理
				if(isPublishFlow){ 
					data[11][i] = sdf.format(fi.getUpdateTime());
					data[12][i] = fi.getApplier().getPerson().getPersonName();
					data[13][i] = fi.getDepartment().getOrganizeName();
					data[14][i] = status;
				}
				
				// 收文管理
				if(isReceiveFlow){ 
					data[12][i] = sdf.format(fi.getUpdateTime());
					data[13][i] = fi.getApplier().getPerson().getPersonName();
					data[14][i] = fi.getDepartment().getOrganizeName();
					data[15][i] = status;
				}
				
			
				
			}
			
			
			/****************************/


			for (int i = 0; i < k; i++) {
				object.addContentListByList(data[i]);
			}
			object.setRowName(rowName);
			ExcelOperate operate = new ExcelOperate();
			try {
				operate.exportExcelWithLink(object, list.size(),null, true, request);
			} catch (IOException e) {
				e.printStackTrace();
			}

			filePath = filePath + fileTitle + ".xls";
//			request.getSession().setAttribute("_File_Path", "");
			request.getSession().removeAttribute("_File_Path");
			request.getSession().setAttribute("_File_Path", filePath);
			
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "/common/download";
	}
	
	//获取自定义表单的查询字段
	@RequestMapping(params = "method=getSearchFields")
	@ResponseBody
	public Map<String, Object> getSearchFields(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		String flowId = request.getParameter("flowId");
		String ftlName = null;
		if (flowId != null && flowId.length() > 0) {
			FlowDefinition flow = (FlowDefinition)this.flowManager.get(Integer.valueOf(flowId));
			if (flow.getTemplate() != null && flow.getTemplate().length() > 0) {
				String[] templateArray = flow.getTemplate().split("/");
				ftlName = templateArray[templateArray.length-1];
			}
		}
		if (ftlName != null) {
			JSONArray searchFields = this.commonManager.getSearchFields(ftlName);
			map.put("_SearchFields", searchFields);
		}
		
		return map;
	}
	//获取自定义表单的查询字段
	@RequestMapping(params = "method=getSearchFields_File")
	@ResponseBody
	public Map<String, Object> getSearchFields_File(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
//		FlowFileVo flowFileVo = new FlowFileVo();
		String flowId = request.getParameter("flowId");
		String ftlName = null;
		if (flowId != null && flowId.length() > 0) {
			FlowDefinition flow = (FlowDefinition)this.flowManager.get(Integer.valueOf(flowId));
			if (flow.getTemplate() != null && flow.getTemplate().length() > 0) {
				String[] templateArray = flow.getTemplate().split("/");
				ftlName = templateArray[templateArray.length-1];
			}
		}
		if (ftlName != null) {
			JSONArray searchFields = this.commonManager.getSearchFields(ftlName);
			map.put("_SearchFields", searchFields);
		}
//		JSONObject jsonObj = new JSONObject();
//		jsonObj.put(fields[0], fields[1]);
//		jsonArray.add(jsonObj);

		return map;
	}
	
	
	//编辑审核实例
	@RequestMapping(params = "method=edit")
	public String edit(HttpServletRequest request, HttpServletResponse response, FlowInstanceInforVo vo) throws Exception {
		
		FlowDefinition flow = new FlowDefinition();
		Integer instanceId = vo.getInstanceId();
		if (instanceId != null && instanceId.intValue() > 0) {
			FlowInstanceInfor instance = (FlowInstanceInfor)this.flowInstanceManager.get(instanceId);
			flow = instance.getFlowDefinition();
			
			BeanUtils.copyProperties(vo, instance);
			
			//主办人
			if (instance.getCharger() != null) {
				vo.setChargerId(instance.getCharger().getPersonId());
			}
			
			//部门审核人一、部门审核人二
			SystemUserInfor manager = instance.getManager();
			SystemUserInfor viceManager = instance.getViceManager();
			if (manager != null) {
				vo.setManagerId(manager.getPersonId());
			}
			if (viceManager != null) {
				vo.setViceManagerId(viceManager.getPersonId());
			}
			
			//对附件信息进行处理
			String attachmentFile = instance.getAttach();
			if (attachmentFile != null && !attachmentFile.equals("")) {
				String[][] attachment = processFile(attachmentFile);
				request.setAttribute("_Attachment_Names", attachment[1]);
				request.setAttribute("_Attachments", attachment[0]);
			}
			request.setAttribute("_Instance", instance);
		}else {
			String flowIdStr = request.getParameter("flowId");
			if (flowIdStr != null && flowIdStr.length() > 0) {
				flow = (FlowDefinition)this.flowManager.get(Integer.valueOf(flowIdStr));
				
				//主办人
				vo.setChargerId(flow.getCharger().getPersonId());
			}
		}
		request.setAttribute("_Flow", flow);
		
		//判断是否存在权限
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		request.setAttribute("_User", systemUser);
		String organizeName  =  systemUser.getPerson().getDepartment().getOrganizeName();
		request.setAttribute("OrganizeName",organizeName);
		boolean hasRight = this.flowInstanceManager.judgeRight(request, "edit", flow.getFlowId(), systemUser);
		if (!hasRight) {
			request.setAttribute("_ErrorMessage", "对不起,您无权进行该操作,请与系统管理员联系!");
			
			return "/common/error";
		}
		
		//获取用户信息
		List userss = this.systemUserManager.getUserByOrganize(systemUser.getPerson().getDepartment().getOrganizeId(), null);
		request.setAttribute("_Userss", userss);
		List users = this.systemUserManager.getAllValid();
		request.setAttribute("_Users", users);
//		申请人、所属部门
		
		return "editInstance";
	}
	
	
	//转向--重新流转
	@RequestMapping(params = "method=readyToNewEdit")
	public String readyToNewEdit(HttpServletRequest request, HttpServletResponse response, FlowInstanceInforVo vo) throws Exception {
		
		
		Integer instanceId = vo.getInstanceId();
		if (instanceId != null && instanceId.intValue() > 0) {
			FlowInstanceInfor instance = (FlowInstanceInfor)this.flowInstanceManager.get(instanceId);

			//获取相关信息
			getProcessInfors(request, response, instance);
			
			//结束时间默认为系统时间
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			request.setAttribute("_SystemTime", sdf.format(new Timestamp(System.currentTimeMillis())));
		}
		
		
		vo.setOldInstanceId(instanceId);
		
		return "newEditInstance";
	}

	//	保存-重新流转
	@RequestMapping(params = "method=newEdit")
	public String newEdit(HttpServletRequest request, HttpServletResponse response, FlowInstanceInforVo vo) throws Exception {
		
		FlowDefinition flow = new FlowDefinition();
		Integer oldInstanceId = vo.getOldInstanceId();
		
		//理由
		String reason = request.getParameter("suspendedReason");
		
		if (oldInstanceId != null && oldInstanceId.intValue() > 0) {
			FlowInstanceInfor instance = (FlowInstanceInfor)this.flowInstanceManager.get(oldInstanceId);
			flow = instance.getFlowDefinition();
			
			BeanUtils.copyProperties(vo, instance);
			
			//主办人
			if (instance.getCharger() != null) {
				vo.setChargerId(instance.getCharger().getPersonId());
			}
			
			//部门审核人一、部门审核人二
			SystemUserInfor manager = instance.getManager();
			SystemUserInfor viceManager = instance.getViceManager();
			if (manager != null) {
				vo.setManagerId(manager.getPersonId());
			}
			if (viceManager != null) {
				vo.setViceManagerId(viceManager.getPersonId());
			}
			vo.setInstanceId(null);
			vo.setOldInstanceId(oldInstanceId);
			//对附件信息进行处理
//			String attachmentFile = instance.getAttach();
//			if (attachmentFile != null && !attachmentFile.equals("")) {
//				String[][] attachment = processFile(attachmentFile);
//				request.setAttribute("_Attachment_Names", attachment[1]);
//				request.setAttribute("_Attachments", attachment[0]);
//			}
			request.setAttribute("_Instance", instance);
			
			instance.setSuspended(true);
			instance.setSuspendedReason(reason);
			this.flowInstanceManager.save(instance);
			
		}else {
			String flowIdStr = request.getParameter("flowId");
			if (flowIdStr != null && flowIdStr.length() > 0) {
				flow = (FlowDefinition)this.flowManager.get(Integer.valueOf(flowIdStr));
				
				//主办人
				vo.setChargerId(flow.getCharger().getPersonId());
			}
		}
		request.setAttribute("_Flow", flow);
		
		//判断是否存在权限
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		request.setAttribute("_User", systemUser);
		boolean hasRight = this.flowInstanceManager.judgeRight(request, "edit", flow.getFlowId(), systemUser);
		if (!hasRight) {
			request.setAttribute("_ErrorMessage", "对不起,您无权进行该操作,请与系统管理员联系!");
			
			return "/common/error";
		}
		
		//获取用户信息
		List userss = this.systemUserManager.getUserByOrganize(systemUser.getPerson().getDepartment().getOrganizeId(), null);
		request.setAttribute("_Userss", userss);
		List users = this.systemUserManager.getAllValid();
		request.setAttribute("_Users", users);
//		申请人、所属部门
		
		return "editInstance";
	}
	
	//保存审核实例
	@RequestMapping(params = "method=save")
	public String save(HttpServletRequest request, HttpServletResponse response, 
			FlowInstanceInforVo vo, DefaultMultipartHttpServletRequest multipartRequest) throws Exception {
		
		FlowInstanceInfor instance = new FlowInstanceInfor();
		Integer instanceId = vo.getInstanceId();
		String flowId = request.getParameter("flowId");
		FlowDefinition flow = (FlowDefinition)this.flowManager.get(Integer.valueOf(flowId));
		
		String oldFiles = "";
		Timestamp sysTime = new Timestamp(System.currentTimeMillis());
		
		boolean isNew = false;
		if (instanceId != null && instanceId.intValue() > 0) {
			instance = (FlowInstanceInfor)this.flowInstanceManager.get(instanceId);
			
			//修改信息时,对附件进行修改
			String filePaths = instance.getAttach();
			oldFiles = deleteOldFile(request, filePaths, "attatchmentArray");
			
		}else {

			isNew = true;
			//创建时间
			instance.setUpdateTime(sysTime);
			
			//申请人、所属部门
			SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
			instance.setApplier(systemUser);
			instance.setDepartment(systemUser.getPerson().getDepartment());
			
			//所属流程
			instance.setFlowDefinition(flow);
			
			//主办人
			Integer chargerId = vo.getChargerId();
			if (chargerId != null && chargerId.intValue() > 0) {
				SystemUserInfor charger = (SystemUserInfor)this.systemUserManager.get(chargerId);
				instance.setCharger(charger);
			}else {
				instance.setCharger(flow.getCharger());
			}
		}
		
		BeanUtils.copyProperties(instance, vo);
		
		//添加部门审核人推送待办提醒
		boolean hasManager = false;
		boolean hasViceManager = false;
		
		//部门审核人
		Integer managerId = vo.getManagerId();
		Integer viceManagerId = vo.getViceManagerId();
		instance.setSubmiterWord(null);
		if (managerId != null && managerId.intValue() > 0) {
			SystemUserInfor manager = (SystemUserInfor)this.systemUserManager.get(managerId);
			if (instance.getManager() == null || instance.getManager().getPersonId().intValue() != manager.getPersonId().intValue()) {
				instance.setManager(manager);
				instance.setManagerChecked(false);
				instance.setManagerWord(null);
				instance.setCheckTime(null);
				String managerAttachment = instance.getManagerAttachment();
				if (managerAttachment != null && managerAttachment.length() > 0) {
					deleteFiles(managerAttachment);
				}
				instance.setManagerAttachment(null);
				
				hasManager = true;
			}
		}else {
			instance.setManager(null);
			instance.setManagerChecked(false);
			instance.setManagerWord(null);
			instance.setCheckTime(null);
			String managerAttachment = instance.getManagerAttachment();
			if (managerAttachment != null && managerAttachment.length() > 0) {
				deleteFiles(managerAttachment);
			}
			instance.setManagerAttachment(null);
		}
		if (viceManagerId != null && viceManagerId.intValue() > 0) {
			SystemUserInfor viceManager = (SystemUserInfor)this.systemUserManager.get(viceManagerId);
			if (instance.getViceManager() == null || instance.getViceManager().getPersonId().intValue() != viceManager.getPersonId().intValue()) {
				instance.setViceManager(viceManager);
				instance.setViceManagerChecked(false);
				instance.setViceManagerWord(null);
				instance.setViceCheckTime(null);
				String viceManagerAttachment = instance.getViceManagerAttachment();
				if (viceManagerAttachment != null && viceManagerAttachment.length() > 0) {
					deleteFiles(viceManagerAttachment);
				}
				instance.setViceManagerAttachment(null);
				
				hasViceManager = true;
			}
		}else {
			instance.setViceManager(null);
			instance.setViceManagerChecked(false);
			instance.setViceManagerWord(null);
			instance.setViceCheckTime(null);
			String viceManagerAttachment = instance.getViceManagerAttachment();
			if (viceManagerAttachment != null && viceManagerAttachment.length() > 0) {
				deleteFiles(viceManagerAttachment);
			}
			instance.setViceManagerAttachment(null);
		}

		//申请人提交的附件
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
		instance.setAttach(attachment);
		
		/** 通过自定义表单模板生成html */
		String templateName = null;
		if (flow.getTemplate() != null && flow.getTemplate().length() > 0) {
			//模板路径
			String[] templateArray = flow.getTemplate().split("/");
			templateName = templateArray[templateArray.length-1];
			Template template = this.commonManager.getTemplate(templateName, FlowConstant.Flow_FormTemplate_Path, true);
				
			//根据时间定义文件名
			Calendar calendar = Calendar.getInstance();
			String fileName = String.valueOf(calendar.getTimeInMillis()) + ".html";
			
			//删除原来的html页面
			String oldHtmlPath = CoreConstant.Context_Real_Path + instance.getContentPath();
			File htmlFile = new File(oldHtmlPath);
			if(htmlFile.exists()) {
				htmlFile.delete();
			}
			
		
			
			//保存新的html文件
			String filePath = CoreConstant.Context_Real_Path + FlowConstant.Flow_Html_Path;	//生成的html文件保存路径
			com.kwchina.core.util.File fileOperator = new com.kwchina.core.util.File();
			File ioFile = new File(filePath);
			fileOperator.createFilePath(ioFile);
			String htmlPath = filePath + "/" + fileName;
			FileOutputStream fos = new FileOutputStream(htmlPath);
			Writer wOut = new OutputStreamWriter(fos, CoreConstant.ENCODING);
			Map map = new HashMap();
			map.put("base", request.getContextPath());	//工程所在路径
			map.put("tagValue", false);	//在模板中用于区分是新增/修改还是生成html
			

			Map maps = new HashMap();
			maps = request.getParameterMap();
			String[] contractTypes = (String[]) maps.get("contractType");
			if(contractTypes!=null && contractTypes.length!=0){
				String contractType = contractTypes[0];
				if(contractType!=null && !contractType.equals("销售合同")){
					maps.remove("saleType");
				}
			}

			String indexNo = "";
			if ((flow.getFlowId().intValue() == 86 || flow.getFlowId().intValue() == 87 || flow.getFlowId().intValue() == 88 || flow.getFlowId().intValue() == 90 || flow.getFlowId().intValue() == 92 || flow.getFlowId().intValue() == 93 || flow.getFlowId().intValue() == 94 || flow.getFlowId().intValue() == 95
					|| flow.getFlowId().intValue() == 96 || flow.getFlowId().intValue() == 98 || flow.getFlowId().intValue() == 99 || flow.getFlowId().intValue() == 100
			) && instanceId == null) {

				//List customizeDatas = this.commonManager.getFormData(flowNames, contractNo,colm);

				String serialNo = "001";
				String tableName = "";
				String fieldName = "";
				if (flow.getFlowId().intValue() == 86) {
					tableName = "Customize_Hetongshenpi";
					fieldName = "indexNo";
				} else if (flow.getFlowId().intValue() == 87) {
					tableName = "Customize_neibubaogao";
					fieldName = "fileNo";
				} else if (flow.getFlowId().intValue() == 88) {
					tableName = "Customize_ZhiDuPingShen";
					fieldName = "orderNo";
				} else if (flow.getFlowId().intValue() == 90) {
					tableName = "Customize_Xinzengwenjian";
					fieldName = "fileNo";
				} else if (flow.getFlowId().intValue() == 92) {
					tableName = "Customize_Xiugaiwenjian";
					fieldName = "fileEdition";
				} else if (flow.getFlowId().intValue() == 93) {
					tableName = "Customize_Xiaohuiwenjian";
					fieldName = "fileEdition";
				} else if (flow.getFlowId().intValue() == 94) {
					tableName = "Customize_Xinzengliucheng";
					fieldName = "flowEdition";
				} else if (flow.getFlowId().intValue() == 95) {
					tableName = "Customize_Xiugailiucheng";
					fieldName = "flowEdition";
				} else if (flow.getFlowId().intValue() == 96) {
					tableName = "Customize_Xiaohuiliucheng";
					fieldName = "flowEdition";
				} else if (flow.getFlowId().intValue() == 98) {
					tableName = "Customize_Xinzengwenjianliucheng";
					fieldName = "fileEdition";
				} else if (flow.getFlowId().intValue() == 99) {
					tableName = "Customize_Xiugaiwenjianliucheng";
					fieldName = "fileEdition";
				} else if (flow.getFlowId().intValue() == 100) {
					tableName = "Customize_Xiaohuiwenjianliucheng";
					fieldName = "fileEdition";
				}
			
				String categoryName =request.getParameter("category");
				
				//String categoryName = new String(request.getParameter("categoryName").getBytes("ISO-8859-1"), "utf-8");
				String fieldYear = request.getParameter("fieldYear");
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
				Calendar cal = Calendar.getInstance();
				java.util.Date sDate = cal.getTime();
				String sDateStr= sdf.format(sDate);
				String reportYear = sDateStr;
				
				if(flow.getFlowId().intValue() == 86){
					if(categoryName.equals("物流")){
						categoryName="WL";
					}else if (categoryName.equals("码头")){
						categoryName="MT";
					}
					indexNo=categoryName+reportYear;
				}else{
					indexNo=categoryName+"-"+reportYear;
				}
				//indexNo=categoryName+reportYear;
				String sql = "select " + fieldName + " from " + tableName + " where "+fieldName+"  like '" + indexNo + "%' and "+fieldName+"  is not null ";
			
					   sql+=" and instanceId not in(select instanceId from Workflow_InstanceInfor where deleteFlag = 1) group by "+fieldName+" order by "+ fieldName+" desc";
				List maxSerialNos = this.flowInstanceManager.getResultBySQLQuery(sql);
				if (maxSerialNos != null && maxSerialNos.size() > 0) {
					
					String maxSerialNo = (String)maxSerialNos.get(0);
					
					String documentLastNo = "";
					if(flow.getFlowId().intValue() == 86){
						documentLastNo = maxSerialNo.substring(6, 9);
					}else{
						//documentLastNo = maxSerialNo.substring(8, 11);
						if(categoryName.equals("码头和物流")){
							documentLastNo = maxSerialNo.substring(11, 14);
						}else {
							documentLastNo = maxSerialNo.substring(8, 11);
						}
					}
				
					 
					Integer maxSerialNoInt = Integer.valueOf(documentLastNo)+1;
					String zero = "";
					for(int i=0;i<3-maxSerialNoInt.toString().length();i++) {
						zero += "0";
					}
					serialNo = zero + maxSerialNoInt.toString();
					//serialNo = maxSerialNoInt.toString();
				}
				
				if(flow.getFlowId().intValue() == 86){
					indexNo+=serialNo;
				}else{
					indexNo+="-"+serialNo;
				}
			
				
				String[] values = new String[1];
				values[0]=indexNo;
				
				
				if(flow.getFlowId().intValue() == 86){
					maps.put("indexNo", values);
				}else if(flow.getFlowId().intValue() == 87){
					maps.put("fileNo", values);
				}else if(flow.getFlowId().intValue() == 88){
					maps.put("orderNo", values);
				}
			
			}
			
		
			
			map.put("formValues", maps);	//从页面取得的值(键值对的方式)
			map.put("_GLOBAL_PERSON", request.getSession().getAttribute("_GLOBAL_PERSON"));
			if (instanceId != null) {
				map.put("_InstanceId", instanceId);
			}else {
				map.put("_InstanceId", 0);
			}
			template.process(map, wOut);
			wOut.flush();
			wOut.close();
			instance.setContentPath(FlowConstant.Flow_Html_Path +  "/" + fileName);
		}
		/********* */
		String flowNames = CnToSpell.getFullSpell(flow.getFlowName());
		String redirectStr = "";
		String colm ="";
		if((flow.getFlowId().intValue() == 86 || flow.getFlowId().intValue() == 87 ||flow.getFlowId().intValue() == 88) && instanceId == null ){
			
			
			
			String serialNo = "001";
			//String serialNo = "1";
			//response.setCharacterEncoding("utf-8");
			//request.setCharacterEncoding("utf-8");
			String tableName = "";
			String fieldName = "";
			if(flow.getFlowId().intValue() == 86){
				 tableName = "Customize_Hetongshenpi";
				 fieldName = "indexNo";	
			}else if(flow.getFlowId().intValue() == 87){
				 tableName = "Customize_neibubaogao";
				 fieldName = "fileNo";	
			}else if(flow.getFlowId().intValue() == 88){
				 tableName = "Customize_ZhiDuPingShen";
				 fieldName = "orderNo";	
			}
		
			String categoryName =request.getParameter("category");
			
			//String categoryName = new String(request.getParameter("categoryName").getBytes("ISO-8859-1"), "utf-8");
			String fieldYear = request.getParameter("fieldYear");
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			Calendar cal = Calendar.getInstance();
			java.util.Date sDate = cal.getTime();
			String sDateStr= sdf.format(sDate);
			String reportYear = sDateStr;
			String indexNo="";
			if(flow.getFlowId().intValue() == 86){
				if(categoryName.equals("物流")){
					categoryName="WL";
				}else if (categoryName.equals("码头")){
					categoryName="MT";
				}
				indexNo=categoryName+reportYear;
			}else{
				indexNo=categoryName+"-"+reportYear;
			}
		
			
			
			
			String sql = "select " + fieldName + " from " + tableName + " where "+fieldName+"  like '" + indexNo + "%' and "+fieldName+"  is not null ";
		
			 sql+=" and instanceId not in(select instanceId from Workflow_InstanceInfor where deleteFlag = 1) group by "+fieldName+" order by "+ fieldName+" desc";
			List maxSerialNos = this.flowInstanceManager.getResultBySQLQuery(sql);
			if (maxSerialNos != null && maxSerialNos.size() > 0) {
				String maxSerialNo = (String)maxSerialNos.get(0);
				
				String documentLastNo = "";
				if(flow.getFlowId().intValue() == 86){
					documentLastNo = maxSerialNo.substring(6, 9);
				}else{
					//documentLastNo = maxSerialNo.substring(8, 11);
					if(categoryName.equals("码头和物流")){
						documentLastNo = maxSerialNo.substring(11, 14);
					}else {
						documentLastNo = maxSerialNo.substring(8, 11);
					}
				}
				Integer maxSerialNoInt = Integer.valueOf(documentLastNo)+1;
				String zero = "";
				for(int i=0;i<3-maxSerialNoInt.toString().length();i++) {
					zero += "0";
				}
				serialNo = zero + maxSerialNoInt.toString();
				//serialNo = maxSerialNoInt.toString();
			}
			
			if(flow.getFlowId().intValue() == 86){
				indexNo+=serialNo;
			}else{
				indexNo+="-"+serialNo;
			}
		

//			if (customizeDatas.size() != 0){
//
//				redirectStr = "/common/error";
//				request.setAttribute("_ErrorMessage", "对不起,合同编号重复,请重新提交!");
//			}else{
				instance = (FlowInstanceInfor)this.flowInstanceManager.save(instance);
				
				if (flow.getTemplate() != null && flow.getTemplate().length() > 0) {
					//根据模板获取自定义表单数据
					List fieldNames = this.commonManager.getFieldNames(templateName);
					String[] fieldValues = new String[fieldNames.size()];
					for (int i=0;i<fieldNames.size();i++) {
						if((flow.getFlowId().intValue() == 86||flow.getFlowId().intValue() == 87)&& i==1){
							fieldValues[i] = indexNo;
						}else if(flow.getFlowId().intValue() == 88 && i==2){
							fieldValues[i] = indexNo;
						}else{
							fieldValues[i] = request.getParameter(fieldNames.get(i).toString());
						}
						
					}
					
					//将汉字的流程名处理为拼音
					String flowName = CnToSpell.getFullSpell(flow.getFlowName());


					//新增或修改自定义表单数据
					if (instanceId != null && instanceId.intValue() > 0) {
						//修改
						this.commonManager.updateFormData(flowName, fieldNames, fieldValues, instance.getInstanceId(), 1);
					}else {
						//新增
						this.commonManager.updateFormData(flowName, fieldNames, fieldValues, instance.getInstanceId(), 0);
					}
				}
				
				
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
				
				redirectStr= "redirect:instanceInfor.do?method=view&instanceId=" + instance.getInstanceId();
			//}
		}else if(flow.getFlowId().intValue() == 87 && instanceId == null){
				colm="fileNo";

				String fileNo= request.getParameter("fileNo");
				
				List customizeDatass = this.commonManager.getFormData(flowNames, fileNo,colm);
				if (customizeDatass.size() != 0){

					redirectStr = "/common/error";
					request.setAttribute("_ErrorMessage", "对不起,内部报告编号重复,请重新提交!");	
				}else{
					instance = (FlowInstanceInfor)this.flowInstanceManager.save(instance);
					
					if (flow.getTemplate() != null && flow.getTemplate().length() > 0) {
						//根据模板获取自定义表单数据
						List fieldNames = this.commonManager.getFieldNames(templateName);
						String[] fieldValues = new String[fieldNames.size()];
						for (int i=0;i<fieldNames.size();i++) {
							fieldValues[i] = request.getParameter(fieldNames.get(i).toString());
						}
						
						//将汉字的流程名处理为拼音
						String flowName = CnToSpell.getFullSpell(flow.getFlowName());


						//新增或修改自定义表单数据
						if (instanceId != null && instanceId.intValue() > 0) {
							//修改
							this.commonManager.updateFormData(flowName, fieldNames, fieldValues, instance.getInstanceId(), 1);
						}else {
							//新增
							this.commonManager.updateFormData(flowName, fieldNames, fieldValues, instance.getInstanceId(), 0);
						}
					}
					
					
					/** 该语句不放在编辑页面的原因:
					 * 若在编辑页面提交数据后立即执行window.close()操作,
					 * 则后台无法取到编辑页面的数据.
					 * (此情况仅在页面包含附件操作时存在) 
					 **/
					/*PrintWriter out = response.getWriter();
					out.print("<script language='javascript'>");
					out.print("var returnArray = [\"refresh\","+flowId+"];");
					out.print("window.returnValue = returnArray;");
					out.print("window.close();");
					out.print("</script>");*/
					
					redirectStr= "redirect:instanceInfor.do?method=view&instanceId=" + instance.getInstanceId();
				}
		}else{

		instance = (FlowInstanceInfor)this.flowInstanceManager.save(instance);
		
		
		if (flow.getTemplate() != null && flow.getTemplate().length() > 0) {
			//根据模板获取自定义表单数据
			List fieldNames = this.commonManager.getFieldNames(templateName);
			String[] fieldValues = new String[fieldNames.size()];
			for (int i=0;i<fieldNames.size();i++) {
				fieldValues[i] = request.getParameter(fieldNames.get(i).toString());
			}
			
			//将汉字的流程名处理为拼音
			String flowName = CnToSpell.getFullSpell(flow.getFlowName());


			//新增或修改自定义表单数据
			if (instanceId != null && instanceId.intValue() > 0) {

				//修改
				this.commonManager.updateFormData(flowName, fieldNames, fieldValues, instance.getInstanceId(), 1);
			}else {
				//新增
				this.commonManager.updateFormData(flowName, fieldNames, fieldValues, instance.getInstanceId(), 0);
			}
		}
		
		
		
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
		
		redirectStr= "redirect:instanceInfor.do?method=view&instanceId=" + instance.getInstanceId();
		}
		
		/******添加对部门审核人的app推送*****/
//		Timestamp current = new Timestamp(System.currentTimeMillis());
		if(hasManager){
			List<FlowInstanceInfor> returnInstances_m = flowInstanceManager.getNeedPushInstances(instance.getManager(), sysTime);
			String alias = instance.getManager().getUserName();
			int badge = returnInstances_m.size();
			
			PushUtil pushUtil = new PushUtil();
			pushUtil.pushNeedDealInstances(instance, badge, alias);
		}
		
		if(hasViceManager){
			List<FlowInstanceInfor> returnInstances_m = flowInstanceManager.getNeedPushInstances(instance.getViceManager(), sysTime);
			String alias = instance.getViceManager().getUserName();
			int badge = returnInstances_m.size();
			
			PushUtil pushUtil = new PushUtil();
			pushUtil.pushNeedDealInstances(instance, badge, alias);
		}
		
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
					FlowInstanceInfor instance = (FlowInstanceInfor) this.flowInstanceManager.get(deleteId);
					
					// 获取附件
					String attach = instance.getAttach();
					String formalAttach = instance.getFormalAttach();
						
					this.flowInstanceManager.deleteInstance(instance, systemUser);
						
					// 删除附件
					deleteFiles(attach);
					deleteFiles(formalAttach);
					
					// 删除关联的自定义表内的数据
					FlowDefinition flow = instance.getFlowDefinition();
					String template = flow.getTemplate();
					if (template != null && template.length() > 0) {
						String flowName = CnToSpell.getFullSpell(flow.getFlowName());
						flowName = flowName.substring(0,1).toUpperCase() + flowName.substring(1);
						this.commonManager.delFormData(flowName, deleteId);
					}
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
				hasRight = this.flowInstanceManager.judgeRight(request, "recycle", Integer.valueOf(flowId), systemUser);
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
						FlowInstanceInfor instance = (FlowInstanceInfor) this.flowInstanceManager.get(deleteId);
						
						boolean canDelete = false;
						if(deleteFlag == 0){
							canDelete = true;
						}else {
							canDelete = this.flowInstanceManager.canDeleteFlowInstance(instance, systemUser);
						}
						
						if (canDelete) {
							instance.setDeleteFlag(deleteFlag);
							this.flowInstanceManager.save(instance);
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
		String instanceId = request.getParameter("instanceId");
		if (instanceId != null && instanceId.length() > 0) {
			FlowInstanceInfor instance = (FlowInstanceInfor)this.flowInstanceManager.get(Integer.valueOf(instanceId));
			
			SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
			int userId = systemUser.getPersonId().intValue();
			
			//判断是否有浏览权限
			boolean canViewFlowInstance = this.flowInstanceManager.canViewFlowInstance(instance, systemUser);
			//2018.03.26 yuye(32)可以看到liusheng(154)的所有合同
			if(systemUser.getPersonId()==32 && !canViewFlowInstance){
				systemUser = (SystemUserInfor) systemUserManager.get(154);
				canViewFlowInstance = this.flowInstanceManager.canViewFlowInstance(instance, systemUser);
			}
			if (canViewFlowInstance) {
				
				//判断是否为主办人
				boolean isCharger = false;
				//int chargerId = instance.getFlowDefinition().getCharger().getPersonId();
				int chargerId = instance.getCharger().getPersonId();
				if (chargerId == userId) {
					isCharger = true;
				}
				request.setAttribute("_IsCharger", isCharger);
				boolean isViewalls = false;
				//增加查看所有体系文件角色的授权功能
				if (instance.getFlowDefinition().getFlowId()>89 && instance.getFlowDefinition().getFlowId()<101){
				RoleInfor role2 = (RoleInfor)this.roleManager.get(53);
				isViewalls = roleManager.belongRole(systemUser,role2);}


				boolean isAdmin = false;
				
				int type = systemUser.getUserType();
				if(type==1){
					isAdmin = true;
				}

				
				request.setAttribute("_IsAdmin", isAdmin);
//				request.setAttribute("_CanEndFile",false);
				//判断是否为该流程的归档人
				boolean isFileRole = false;
				if(instance.getFlowDefinition().getFlowId()<90 || instance.getFlowDefinition().getFlowId()>100){
					//非体系文件正常判断角色
					int filerType = instance.getFlowDefinition().getFilerType();
					if(filerType == 1){
						RoleInfor tmpRole = instance.getFlowDefinition().getFileRole();
						if(tmpRole != null){
							int tmpRoleId = tmpRole.getRoleId();

							Set<RoleInfor> sysRoleSet = systemUser.getRoles();
							for(Iterator it = sysRoleSet.iterator();it.hasNext();){
								RoleInfor sysRole = (RoleInfor)it.next();
								if(tmpRoleId == sysRole.getRoleId()){
									isFileRole = true;
								}
							}
						}
					}

				}else {
					//体系文件归档人为 文件流转审核人  谁审核谁归档
					Set<InstanceLayerInfor> instanceLayerInfors= instance.getLayers();
					InstanceLayerInfor layerInfor = new InstanceLayerInfor();
					if(instanceLayerInfors!=null && !instanceLayerInfors.isEmpty()){
						for(InstanceLayerInfor instanceLayerInfor:instanceLayerInfors){
							if(instanceLayerInfor.getLayerName().equals("文件流转审核人")){
								layerInfor = instanceLayerInfor;
							}
						}
					}

					Set<InstanceCheckInfor> checkInfors = layerInfor.getCheckInfors();
					if(checkInfors!=null && !checkInfors.isEmpty()){
						for(InstanceCheckInfor instanceCheckInfor : checkInfors){

							SystemUserInfor systemUserInfor = instanceCheckInfor.getChecker();
							if(systemUserInfor!=null && systemUserInfor.getPersonId().intValue() == systemUser.getPersonId().intValue() && (instanceCheckInfor.getStatus() == 1 ) ){

								isFileRole = true;
							}
						}
					}
				}
				request.setAttribute("_IsFileRole", isFileRole);
				request.setAttribute("_IsViewalls",isViewalls);
				
				//判断能否开始流转
				boolean canStart = false;
				if (systemUser.getPersonId().intValue() == instance.getApplier().getPersonId().intValue() && instance.getStartTime() == null
						&& (instance.getSubmiterWord() != null 
								|| (instance.getSubmiterWord() == null && (instance.isManagerChecked()||instance.isViceManagerChecked()))
								|| (instance.getManager() == null && instance.getViceManager() == null)) &&
						((instance.getManager()!=null && instance.isManagerChecked() && instance.getViceManager()!=null && instance.isViceManagerChecked()) || (instance.getManager()==null && instance.getViceManager()!=null && instance.isViceManagerChecked()) || (instance.getManager()!=null && instance.isManagerChecked() && instance.getViceManager()==null ))) {
					canStart = true;
				}
				request.setAttribute("_CanStart", canStart);


				//判断能否进行部门审核
				boolean canDepCheck = false;
				SystemUserInfor manager = instance.getManager();
				SystemUserInfor viceManager = instance.getViceManager();
				if (((manager != null && manager.getPersonId().intValue() == userId && !instance.isManagerChecked())||(viceManager != null && viceManager.getPersonId().intValue() == userId && !instance.isViceManagerChecked()))
						&&instance.getSubmiterWord()==null) {
					canDepCheck = true;
				}
				request.setAttribute("_CanDepCheck", canDepCheck);
				
				
				//判断能否中止部门审核
				boolean canStopDepCheck = false;
				if (((manager != null && !instance.isManagerChecked())||(viceManager != null && !instance.isViceManagerChecked()))
						&& instance.getSubmiterWord()==null && userId == instance.getApplier().getPersonId().intValue()) {
//				if(manager != null && viceManager != null && (instance.isManagerChecked() ||instance.isViceManagerChecked() )){
						canStopDepCheck = true;
				}
				request.setAttribute("_CanStopDepCheck", canStopDepCheck);
				
				
				//判断是否可以添加审核层次
				boolean canAddLayer = this.flowInstanceManager.canAddLayerInfor(instance, systemUser);
				request.setAttribute("_CanAddLayer", canAddLayer);
				
				
				//判断是否有审核权限
				boolean canCheck = false;
				InstanceCheckInfor tmpCheck = this.flowInstanceManager.isChecker(instance, systemUser);
				if (tmpCheck != null) {
					// 能否审核
					if (tmpCheck.getLayerInfor().getStatus() != InstanceLayerInfor.Layer_Status_End
							&& tmpCheck.getLayerInfor().getStatus() != InstanceLayerInfor.Layer_Status_Suspended
							&& !instance.isSuspended() && tmpCheck.getEndDate() == null) {
						canCheck = true;
					}
				}
				request.setAttribute("_CanCheck", canCheck);

//				if (instance.getInstanceId() == 7026) {
//					System.out.println();
//				}
				//判断能否中止审核、能否进行下一步操作
				boolean canEndCheck = false;
				boolean canNext = false;
				boolean canNext_File = false;
				boolean canNext_FW = true;//合同里只有在法务意见时 不能出现下一步
				boolean hasChecker = true;
				boolean canNext_GSLD = false;
				boolean canNext_FGLD = false;
				boolean canNext_HQC = false;
				if (isCharger && instance.getStartTime() != null && instance.getEndTime() == null) {

					boolean allHasNotChecked = true;
					List rLayers = this.flowInstanceManager.getCurrentProcessLayers(instance);
					if (rLayers != null && rLayers.size() > 0) {
						for (Iterator itLayer=rLayers.iterator();itLayer.hasNext();) {
							InstanceLayerInfor layer = (InstanceLayerInfor)itLayer.next();
							Set checks = layer.getCheckInfors();
							boolean hasNotChecked = false;
							if(layer.getLayerName().equals("法务意见") && layer.getStatus()!=3){
									//合同只有在法务层 选人时 但法务没审时 不能出现下一步
									canNext_FW = false;
							}
							if(layer.getLayerName().equals("公司总经理")){
								//合同公司总经理时 不能出现下一步
								canNext_FW = false;
							}
							if(layer.getLayerName().equals("公司领导")){
								//体系公司领导时 不能出现下一步
								canNext_GSLD = true;
							}
							if(layer.getLayerName().equals("分管领导") && (instance.getFlowDefinition().getFlowId()>89 && instance.getFlowDefinition().getFlowId()<101)){
								//体系公司领导时 能跳层
								canNext_FGLD = true;
							}
							if(layer.getLayerName().equals("会签层") && (instance.getFlowDefinition().getFlowId()>89 && instance.getFlowDefinition().getFlowId()<101)){
								//体系会签层时 能跳层
								canNext_HQC = true;
							}
							if (checks != null && !checks.isEmpty()) {
								for (Iterator itCheck=checks.iterator();itCheck.hasNext();) {
									InstanceCheckInfor check = (InstanceCheckInfor)itCheck.next();

									if (checks.size() > 1 && check.getEndDate() != null) {
										//如果有人已审,则可以执行中止审核操作
										canEndCheck = true;


									}
									
//									如果只有一个审核人时，未审核时，也可执行中止审核操作
									//只有一个人时，不出现终止审核
									if(checks.size() == 1  ){
									//&& check.getEndDate() == null
										canEndCheck = false;
									}
									
									if (check.getEndDate() == null && check.getLayerInfor().getStatus() != InstanceLayerInfor.Layer_Status_End) {
										//若该审核层未审核,且未设置为中止,则不能进行下一步操作(hasNotChecked为true)
										hasNotChecked = true;
									}
								}
								
							}else {
								//没有一个审核人
								hasChecker = false;
							}
							if (!hasNotChecked) {
								//若存在有审核层全部审核完毕,则allHasNotChecked为fasle
								allHasNotChecked = false;
							}
						}
					}

					if (!allHasNotChecked) {
						canNext = true;
					}
					if(!hasChecker){
						if (canNext_FGLD){
							canNext_File=true;
						}else if (canNext_HQC){
							canNext_File=true;
						}else{
							canNext_File = false;
						}
					}else {
						if (!allHasNotChecked) {
							if (canNext_GSLD){
								canNext_File = false;
							}else{
								canNext_File = true;
							}
						}
					}
//					System.out.println(canEndCheck);
//					System.out.println(canNext);
//					System.out.println(canNext_File);
//					System.out.println(canNext_FW);
				}
				request.setAttribute("_CanEndCheck", canEndCheck);
				request.setAttribute("_CanNext", canNext);
				request.setAttribute("_CanNext_File", canNext_File);
				request.setAttribute("_CanNext_FW",canNext_FW);
				
				//判断能否添加决议附件
				boolean canAddResAttach = false;
				//判断能否修改
				boolean canMody = false;
				if (instance.getStartTime() != null&&(isCharger||userId==instance.getApplier().getPersonId().intValue())
						&& instance.getFlowDefinition().getFlowId() == SubmitConstant.SubmitFlow_Report_Contract) {
					
					//判断当前审核层是否为"领导批示",且状态为已经完毕或中止,若是则可以添加决议附件,否则不行
					List rLayers = this.flowInstanceManager.getAllProcessLayers(instance);
					
					if (rLayers != null && rLayers.size() > 0) {
						for (Iterator itLayer=rLayers.iterator();itLayer.hasNext();) {
							InstanceLayerInfor layer = (InstanceLayerInfor)itLayer.next();


							if (("法务意见").equals(layer.getLayerName())&& layer.getEndTime()!=null) {
								canMody = true;
								break;
							}
						}
					}
				}

				//判断能否修改
				boolean canModyFF = false;
				if (instance.getStartTime() != null && (isCharger || userId == instance.getApplier().getPersonId().intValue())
						&& instance.getFlowDefinition().getFlowId() > 89 && instance.getFlowDefinition().getFlowId() < 101) {

					//判断当前审核层是否为"领导批示",且状态为已经完毕或中止,若是则可以添加决议附件,否则不行
					List rLayers = this.flowInstanceManager.getAllProcessLayers(instance);

					if (rLayers != null && rLayers.size() > 0) {
						for (Iterator itLayer = rLayers.iterator(); itLayer.hasNext(); ) {
							InstanceLayerInfor layer = (InstanceLayerInfor) itLayer.next();

							if (("文件流转审核人").equals(layer.getLayerName()) && (layer.getEndTime() != null || layer.getStatus()==1)) {
								canModyFF = true;  //不能修改
								break;
							}
						}
					}
				}
				
				//判断制度评审能否修改
				boolean canEditZdps = false;
				if (instance.getStartTime() != null&&(isCharger||userId==instance.getApplier().getPersonId().intValue())
						&& instance.getFlowDefinition().getFlowId() == SubmitConstant.SubmitFlow_Report_Order) {
					
					//判断当前审核层是否为"综合审查意见",且状态为已经完毕或中止,
					List rLayers = this.flowInstanceManager.getAllProcessLayers(instance);
					
					if (rLayers != null && rLayers.size() > 0) {
						for (Iterator itLayer=rLayers.iterator();itLayer.hasNext();) {
							InstanceLayerInfor layer = (InstanceLayerInfor)itLayer.next();


							if (("综合审查意见").equals(layer.getLayerName())&& layer.getEndTime()!=null) {
								canEditZdps = true;
								break;
							}
						}
					}
				}
				
				if (instance.getStartTime() != null&&instance.getEndTime() == null&&(isCharger||userId==instance.getApplier().getPersonId().intValue())
						&& instance.getFlowDefinition().getFlowId() == SubmitConstant.SubmitFlow_Report_Board) {
					
					//判断当前审核层是否为"领导批示",且状态为已经完毕或中止,若是则可以添加决议附件,否则不行
					List rLayers = this.flowInstanceManager.getCurrentProcessLayers(instance);
					if (rLayers != null && rLayers.size() > 0) {
						for (Iterator itLayer=rLayers.iterator();itLayer.hasNext();) {
							InstanceLayerInfor layer = (InstanceLayerInfor)itLayer.next();
							if ((("领导批示").equals(layer.getLayerName())||("信息反馈").equals(layer.getLayerName())) && (layer.getStatus() == InstanceLayerInfor.Layer_Status_End||layer.getStatus() == InstanceLayerInfor.Layer_Status_Finished)) {
								canAddResAttach = true;
								break;
							}
						
						}
					}
				}
				
				request.setAttribute("_CanMody", canMody);
				request.setAttribute("_CanModyFF", canModyFF);
				request.setAttribute("_CanEditZdps", canEditZdps);
				request.setAttribute("_CanAddResAttach", canAddResAttach);
				
				//判断能否申请盖章
				boolean canApplyStamp = false;
				if (instance.getStartTime() != null&&instance.getEndTime() == null && isCharger && (instance.getFlowDefinition().getFlowId() == SubmitConstant.SubmitFlow_Report_Contract || instance.getFlowDefinition().getFlowId()>89 && instance.getFlowDefinition().getFlowId() < 101)) {
					
					//判断当前审核层是否为"公司领导意见",且状态为已经完毕或中止,若是则可以执行盖章操作,否则不行
					List rLayers = this.flowInstanceManager.getCurrentProcessLayers(instance);
					if (rLayers != null && rLayers.size() > 0) {
						for (Iterator itLayer=rLayers.iterator();itLayer.hasNext();) {
							InstanceLayerInfor layer = (InstanceLayerInfor)itLayer.next();
							if ((("公司总经理").equals(layer.getLayerName()) || ("公司领导意见").equals(layer.getLayerName()) || ("公司领导").equals(layer.getLayerName())) && instance.getStamped()==0 && (layer.getStatus() == InstanceLayerInfor.Layer_Status_End||layer.getStatus() == InstanceLayerInfor.Layer_Status_Finished)) {
								canApplyStamp = true;
								break;
							}
						}
					}
				}
				request.setAttribute("_CanApplyStamp", canApplyStamp);
				
				//判断能否盖章
				boolean canStamp = false;
				if (instance.getStartTime() != null&&instance.getEndTime() == null && isFileRole && instance.getFlowDefinition().getFlowId() == SubmitConstant.SubmitFlow_Report_Contract || instance.getFlowDefinition().getFlowId()>89 && instance.getFlowDefinition().getFlowId() < 101) {
					
					//判断当前审核层是否为"公司领导意见",且状态为已经完毕或中止,若是则可以执行盖章操作,否则不行
					List rLayers = this.flowInstanceManager.getCurrentProcessLayers(instance);
					if (rLayers != null && rLayers.size() > 0) {
						for (Iterator itLayer=rLayers.iterator();itLayer.hasNext();) {
							InstanceLayerInfor layer = (InstanceLayerInfor)itLayer.next();
							if ((("公司总经理").equals(layer.getLayerName()) || ("公司领导意见").equals(layer.getLayerName()) || ("公司领导").equals(layer.getLayerName())) && instance.getStamped()==1 && (layer.getStatus() == InstanceLayerInfor.Layer_Status_End||layer.getStatus() == InstanceLayerInfor.Layer_Status_Finished)) {
								if ( instance.isSuspended()) {
									canStamp = false;
								}else {
									canStamp = true;
									break;
								}
							}

						}
					}
				}
				request.setAttribute("_CanStamp", canStamp);
				
				FlowDefinition flow = instance.getFlowDefinition();
				
				if(flow.getFlowId().intValue() == SubmitConstant.SubmitFlow_Report_Contract){
					String sqlc ="select contractNo from Customize_Hetongshenpi where instanceId="+instanceId;
					List nowListc = this.flowInstanceManager.getResultBySQLQuery(sqlc);
					
					String contractNo =(String)nowListc.get(0);
					request.setAttribute("_ContractNo", contractNo);
					if(instance.getOldInstanceId() !=null && instance.getOldInstanceId()!=0){
						String sqlo ="select indexNo from Customize_Hetongshenpi where instanceId="+instance.getOldInstanceId();
						List nowListo = this.flowInstanceManager.getResultBySQLQuery(sqlo);
						
						String indexNo =(String)nowListo.get(0);
						request.setAttribute("_IndexNo", indexNo);
					}
				
				}
				
//				if(instance.getOldInstanceId()!=null && instance.getOldInstanceId()!=0){
//					FlowInstanceInfor oldinstance = (FlowInstanceInfor)this.flowInstanceManager.get(instance.getOldInstanceId());
//					request.setAttribute("_Oldinstance", oldinstance);
//				}
				
				//获取审核实例相关信息
				getProcessInfors(request, response, instance);
//				request.setAttribute("_Instance",instance);
			}
			
		}
		}catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return "viewInstance";
	}
	
	
	//编辑部门审核
	@RequestMapping(params = "method=editDepCheck")
	public String editDepCheck(HttpServletRequest request, HttpServletResponse response, FlowInstanceInforVo vo) throws Exception {
		
		String instanceId = request.getParameter("instanceId");
		
		List approve = this.approveSentenceManager.getApproveSentenceOrderBy();
		request.setAttribute("_Approves", approve);
		
		if (instanceId != null && instanceId.length() > 0) {
			FlowInstanceInfor instance = (FlowInstanceInfor)this.flowInstanceManager.get(Integer.valueOf(instanceId));
			SystemUserInfor manager = instance.getManager();
			SystemUserInfor viceManager = instance.getViceManager();
			
			SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
			if (manager != null && manager.getPersonId().intValue() == systemUser.getPersonId().intValue()) {
				//审核人一
				request.setAttribute("_CheckWord", instance.getManagerWord());
				//对附件信息进行处理
				String managerAttachment = instance.getManagerAttachment();
				if (managerAttachment != null && !managerAttachment.equals("")) {
					String[][] attachment = processFile(managerAttachment);
					request.setAttribute("_DepAttachment_Names", attachment[1]);
					request.setAttribute("_DepAttachments", attachment[0]);
				}
			}else if (viceManager != null && viceManager.getPersonId().intValue() == systemUser.getPersonId().intValue()) {
				//审核人二
				request.setAttribute("_CheckWord", instance.getViceManagerWord());
				//对附件信息进行处理
				String viceManagerAttachment = instance.getViceManagerAttachment();
				if (viceManagerAttachment != null && !viceManagerAttachment.equals("")) {
					String[][] attachment = processFile(viceManagerAttachment);
					request.setAttribute("_DepAttachment_Names", attachment[1]);
					request.setAttribute("_DepAttachments", attachment[0]);
				}
			}
			
			//获取审核实例相关信息
			getProcessInfors(request, response, instance);
		}
		
		return "editDepCheck";
	}
	
	//保存部门审核
	@RequestMapping(params = "method=saveDepCheck")
	public String saveDepCheck(HttpServletRequest request, HttpServletResponse response,
			FlowInstanceInforVo vo, DefaultMultipartHttpServletRequest multipartRequest) throws Exception {
		
		Integer instanceId = vo.getInstanceId();
		String checkWord = request.getParameter("checkWord");
		Timestamp checkTime = new Timestamp(System.currentTimeMillis());
		if (instanceId != null && instanceId.intValue() > 0) {
			FlowInstanceInfor instance = (FlowInstanceInfor)this.flowInstanceManager.get(instanceId);
			SystemUserInfor manager = instance.getManager();
			SystemUserInfor viceManager = instance.getViceManager();
			
			SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
			if (manager != null && manager.getPersonId().intValue() == systemUser.getPersonId().intValue()) {
				//审核人一
				instance.setManagerWord(checkWord);
				instance.setCheckTime(checkTime);
				instance.setManagerChecked(true);
				
				//修改信息时,对附件进行修改
				String filePaths = instance.getManagerAttachment();
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
				instance.setManagerAttachment(attachment);
			}else if (viceManager != null && viceManager.getPersonId().intValue() == systemUser.getPersonId().intValue()) {
				//审核人二
				instance.setViceManagerWord(checkWord);
				instance.setViceCheckTime(checkTime);
				instance.setViceManagerChecked(true);
				
				//修改信息时,对附件进行修改
				String filePaths = instance.getViceManagerAttachment();
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
				instance.setViceManagerAttachment(attachment);
			}
			
			this.flowInstanceManager.save(instance);
		}
		
		return "redirect:instanceInfor.do?method=view&instanceId=" + instanceId;
	}
	
	//中止部门审核
	@RequestMapping(params = "method=saveDepStop")
	@ResponseBody
	public Map<String, Object> saveDepStop(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		String instanceId = request.getParameter("instanceId");
		if (instanceId != null && instanceId.length() > 0) {
			FlowInstanceInfor instance = (FlowInstanceInfor)this.flowInstanceManager.get(Integer.valueOf(instanceId));
			SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
			if (systemUser.getPersonId().intValue() == instance.getApplier().getPersonId().intValue()) {
				String submiterWord = request.getParameter("submiterWord");
//				submiterWord = new String(submiterWord.getBytes("ISO-8859-1"), "gbk");
				instance.setSubmiterWord(submiterWord);
				this.flowInstanceManager.save(instance);
				map.put("message", "部门审核已被中止！");
			}
		}
		
		return map;
	}
	
	
	//开始流转
	@RequestMapping(params = "method=startInstance")
	public String startInstance(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String instanceId = request.getParameter("instanceId");
		FlowInstanceInfor instance = new FlowInstanceInfor();
		if (instanceId != null && instanceId.length() > 0) {
			instance = (FlowInstanceInfor)this.flowInstanceManager.get(Integer.valueOf(instanceId));
			if (instance.getStartTime() == null) {
				instance = (FlowInstanceInfor)this.flowInstanceManager.startInstance(instance);
			}
		}

		return "redirect:instanceInfor.do?method=view&instanceId=" + instance.getInstanceId();
	}

	//中止审核(判断中止的审核层有一个还是多个,只有一个时,程序直接中止)
	@RequestMapping(params="method=endCheck")
	@ResponseBody
	public Map<String, Object> endCheck(HttpServletRequest request, HttpServletResponse response) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		String instanceId =  request.getParameter("instanceId");
		
		if (instanceId != null && instanceId.length() > 0) {
			FlowInstanceInfor instance = (FlowInstanceInfor)this.flowInstanceManager.get(Integer.valueOf(instanceId));
			
			//获取当前正在处理的未中止的审核层
			List processLayers = this.flowInstanceManager.getCurrentProcessLayers(instance);
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
						this.flowLayerInforManager.endCheckLayer(currentLayers, instance);
						map.put("needChoose", false);
						map.put("message", "审核层'"+layerName+"'已经被中止！");
					} catch (InstanceSuspendLayerException e) {
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
					InstanceLayerInfor layer = (InstanceLayerInfor)this.flowLayerInforManager.get(Integer.valueOf(layerArray[i]));
					endLayerList.add(layer);
					
					instance = layer.getInstance();
				}
			}
			
			try {
				this.flowLayerInforManager.endCheckLayer(endLayerList, instance);
				map.put("message", "所选审核层已经被中止！");
			} catch (InstanceSuspendLayerException e) {
				map.put("message", "每个层次至少有一个人审核过才能中止！");
			}
			
		}
		
		return map;
	}
	
	
	//跳转到下一节点(判断预设的下一步节点是否相同,假如相同则直接跳转,否则需要用户选择)
	@RequestMapping(params="method=nextNode")
	@ResponseBody
	public Map<String, Object> nextNode(HttpServletRequest request, HttpServletResponse response) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		String instanceId =  request.getParameter("instanceId");
		
		if (instanceId != null && instanceId.length() > 0) {
			FlowInstanceInfor instance = (FlowInstanceInfor)this.flowInstanceManager.get(Integer.valueOf(instanceId));
			
			map = this.flowInstanceManager.nextValidate(instance);
		}
		return map;
	}
	
	
	//跳转到下一节点(跳转到用户所选的下一节点)
	@RequestMapping(params="method=saveNext")
	@ResponseBody
	public Map<String, Object> saveNext(HttpServletRequest request, HttpServletResponse response) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		String fromLayerId =  request.getParameter("fromLayerId");
		
		if (fromLayerId != null && fromLayerId.length() > 0){
			
			InstanceLayerInfor layerInfor = (InstanceLayerInfor)this.flowLayerInforManager.get(Integer.valueOf(fromLayerId));
			this.flowInstanceManager.flowToNextNode(layerInfor);
			map.put("message", "已转到所选节点！");
		}
		
		return map;
	}
	
	
	//编辑结束信息
	@RequestMapping(params = "method=editEnd")
	public String editEnd(HttpServletRequest request, HttpServletResponse response, FlowInstanceInforVo vo) throws Exception {
		
		
		Integer instanceId = vo.getInstanceId();
		if (instanceId != null && instanceId.intValue() > 0) {
			FlowInstanceInfor instance = (FlowInstanceInfor)this.flowInstanceManager.get(instanceId);

			//获取相关信息
			getProcessInfors(request, response, instance);
			
			//结束时间默认为系统时间
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			request.setAttribute("_SystemTime", sdf.format(new Timestamp(System.currentTimeMillis())));
		}

		return "endInstance";
	}
	
	
	//保存结束信息
	@RequestMapping(params = "method=saveEnd")
	public String saveEnd(HttpServletRequest request, HttpServletResponse response, 
			FlowInstanceInforVo vo, DefaultMultipartHttpServletRequest multipartRequest) throws Exception {
		
		Integer instanceId = vo.getInstanceId();
		if (instanceId != null && instanceId.intValue() > 0) {
			FlowInstanceInfor instance = (FlowInstanceInfor)this.flowInstanceManager.get(instanceId);
			
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

			this.flowInstanceManager.save(instance);
			
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
					List cols = this.commonManager.getColumnNames(flowName);
					//获取审核实例的相关数据
					Map<Object, Object> publishMap = new HashMap<Object, Object>();
					List formDatas = this.commonManager.getFormData(flowName, Integer.valueOf(instanceId));
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
					formDataMap.put("receiveDate", new String[]{sf.format(new java.util.Date())});
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
	
	
	//添加决议附件
	@RequestMapping(params = "method=addResAttach")
	public String addResAttach(HttpServletRequest request, HttpServletResponse response, FlowInstanceInforVo vo) throws Exception {
		
		Integer instanceId = vo.getInstanceId();
		if (instanceId != null && instanceId.intValue() > 0) {
			FlowInstanceInfor instance = (FlowInstanceInfor)this.flowInstanceManager.get(instanceId);

			//获取相关信息
			getProcessInfors(request, response, instance);
			
			//决议附件信息
			String resAttach = instance.getResAttach();
			if (resAttach != null && !resAttach.equals("")) {
				String[][] attachment = processFile(resAttach);
				request.setAttribute("_ResAttachment_Names", attachment[1]);
				request.setAttribute("_ResAttachments", attachment[0]);
			}
		}
		
		return "addResAttach";
	}
	
	
	//保存决议附件
	@RequestMapping(params = "method=saveResAttach")
	public String saveResAttach(HttpServletRequest request, HttpServletResponse response, 
			FlowInstanceInforVo vo, DefaultMultipartHttpServletRequest multipartRequest) throws Exception {
		
		Integer instanceId = vo.getInstanceId();
		if (instanceId != null && instanceId.intValue() > 0) {
			FlowInstanceInfor instance = (FlowInstanceInfor)this.flowInstanceManager.get(instanceId);
			
			String oldFiles = "";
			String filePaths = instance.getResAttach();
			oldFiles = deleteOldFile(request, filePaths, "attatchmentArray");
			
			//决议附件
			String resAttach = this.uploadAttachment(multipartRequest, "workflow");
			if (resAttach == null || resAttach.equals("")) {
				resAttach = oldFiles;
			} else {
				if (oldFiles == null || oldFiles.equals("")) {
					// attachment = attachment;
				} else {
					resAttach = resAttach + "|" + oldFiles;
				}
			}
			instance.setResAttach(resAttach);
			
			//备注
			String attachMemo = request.getParameter("attachMemo");
			instance.setAttachMemo(attachMemo);
			
			this.flowInstanceManager.save(instance);
		}
		
		return view(request, response);
		
	}
	
	
	//还原审核实例,恢复流转
	@RequestMapping(params = "method=restore")
	public String restore(HttpServletRequest request, HttpServletResponse response) {
		
		String instanceId = request.getParameter("instanceId");
		if (instanceId != null && instanceId.length() > 0) {
			FlowInstanceInfor instance = (FlowInstanceInfor)this.flowInstanceManager.get(Integer.valueOf(instanceId));
			
			//清空结束时间、正式附件
			instance.setEndTime(null);
			String formalAttach = instance.getFormalAttach();
			if (formalAttach != null) {
				deleteFiles(formalAttach);
				instance.setFormalAttach(null);
			}
			
			//修改归档状态
			instance.setFiled(false);
			
			this.flowInstanceManager.save(instance);
		}
		return "redirect:instanceInfor.do?method=view&instanceId=" + instanceId;
	}
	
	
	//转向--暂停
	@RequestMapping(params = "method=readyToAlterStatus")
	public String readyToAlterStatus(HttpServletRequest request, HttpServletResponse response, FlowInstanceInforVo vo) throws Exception {
		
		
		Integer instanceId = vo.getInstanceId();
		if (instanceId != null && instanceId.intValue() > 0) {
			FlowInstanceInfor instance = (FlowInstanceInfor)this.flowInstanceManager.get(instanceId);

			//获取相关信息
			getProcessInfors(request, response, instance);
			Integer flowId = instance.getFlowDefinition().getFlowId();

			request.setAttribute("_FlowId",flowId);
			//结束时间默认为系统时间
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//				request.setAttribute("_SystemTime", sdf.format(new Timestamp(System.currentTimeMillis())));
		}


		
		
		
		return "alterStatusInstance";
	}
	
	//暂停或恢复
	@RequestMapping(params = "method=alterStatus")
	public String alterStatus(HttpServletRequest request, HttpServletResponse response) {
		
		String suspended = request.getParameter("suspended");
		boolean isSuspended = true;
		if (suspended != null && ("false").equals(suspended)) {
			isSuspended = false;
		}
		
		String instanceId = request.getParameter("instanceId");
		String reason = request.getParameter("suspendedReason");
		if (instanceId != null && instanceId.length() > 0) {
			FlowInstanceInfor instance = (FlowInstanceInfor)this.flowInstanceManager.get(Integer.valueOf(instanceId));
			
			instance.setSuspended(isSuspended);
			instance.setSuspendedReason(reason);
			
			this.flowInstanceManager.save(instance);
		}

		return "redirect:instanceInfor.do?method=view&instanceId=" + instanceId;
	}
	
	//编辑归档信息
	@RequestMapping(params = "method=editFiled")
	public String editFiled(HttpServletRequest request, HttpServletResponse response, FlowInstanceInforVo vo) throws Exception {
		
		String instanceId = request.getParameter("instanceId");
		if (instanceId != null && instanceId.length() > 0) {
			FlowInstanceInfor instance = (FlowInstanceInfor)this.flowInstanceManager.get(Integer.valueOf(instanceId));
			if(instance.getFlowDefinition().getFlowId()>89 && instance.getFlowDefinition().getFlowId()<101){
				request.setAttribute("_FileCan",false);
			}else {
				request.setAttribute("_FileCan",true);
			}
			if (instance.getFlowDefinition().getFlowId()>89 && instance.getFlowDefinition().getFlowId()<101){
				request.setAttribute("_IsFile",true);
			}else {
				request.setAttribute("_IsFile",false);
			}
			//获取相关信息
			getProcessInfors(request, response, instance);
			
			/*//获取文档大全分类信息
			DocumentCategory leavesCategory = null;
			DocumentCategory parentsCategory = null;
			List returnList = new ArrayList();
			List allParentCategory = this.documentCategoryManager.getParentCategoryFromReport();
			request.setAttribute("_Parent_Category", allParentCategory);
			List allLeafCategory = this.documentCategoryManager.getAllLeafCategory();
			*//** 将取得的子类与父类进行对比,看是否是其叶子 *//*
			for (int i = 0; i < allParentCategory.size(); i++) {
				List tmp = new ArrayList();
				for (int j = 0; j < allLeafCategory.size(); j++) {
					parentsCategory = (DocumentCategory) allParentCategory.get(i);
					leavesCategory = (DocumentCategory) allLeafCategory.get(j);
					if (leavesCategory.getParent().getCategoryId() == parentsCategory.getCategoryId()&&leavesCategory.getFromReport()==1) {
						tmp.add(allLeafCategory.get(j));
					}
				}
				returnList.add(tmp);
			}
			request.setAttribute("_ReturnList", returnList.toArray());*/
			
			//获取用于归档的分类信息
			List<DocumentCategory> filedCategory = this.documentCategoryManager.getCategoryFromReport();
			List<DocumentCategory> filedCategory_FF = new ArrayList<>();
			request.setAttribute("_FiledCategory", filedCategory);
			if (instance.getFlowDefinition().getFlowId() > 89 && instance.getFlowDefinition().getFlowId() < 101) {
				for (DocumentCategory documentCategory : filedCategory) {
					if (documentCategory.getCategoryName().equals("体系文件")) {
						filedCategory_FF.add(documentCategory);
						request.setAttribute("_FiledCategory", filedCategory_FF);
						break;
					}
				}
			}
			List<DocumentCategory> filedCategory_HT = new ArrayList<>();
			if (instance.getFlowDefinition().getFlowId() == 86) {
				for (DocumentCategory documentCategory : filedCategory) {
					if (documentCategory.getCategoryName().equals("合同审批")) {
						filedCategory_HT.add(documentCategory);
						request.setAttribute("_FiledCategory", filedCategory_HT);
						break;
					}
				}
			}

			
			/** 若是发文(行政/党群),则获取部门、用户信息,用于发送讯息通知 */
			boolean bulletinAndMessage = false;
			boolean contract = false;
			FlowDefinition flow = instance.getFlowDefinition();
			String contractNo="";
			if(flow.getFlowId().intValue() == SubmitConstant.SubmitFlow_Report_Contract){
				
				String serialNo = "001";
				//String serialNo = "1";
				//response.setCharacterEncoding("utf-8");
				//request.setCharacterEncoding("utf-8");
				String tableName = "Customize_Hetongshenpi";
				String fieldName = "contractNo";
				
				String sqls ="select category from Customize_Hetongshenpi where instanceId="+instanceId;
				List nowList = this.flowInstanceManager.getResultBySQLQuery(sqls);
				
				String categoryName =(String)nowList.get(0);
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
				Calendar cal = Calendar.getInstance();
				java.util.Date sDate = cal.getTime();
				String sDateStr= sdf.format(sDate);
				
				//String categoryName = new String(request.getParameter("categoryName").getBytes("ISO-8859-1"), "utf-8");
				String fieldYear = "reportYear";
				String reportYear = sDateStr;
				
				contractNo= categoryName+"-"+sDateStr;
				String sql = "select " + fieldName + " from " + tableName + " where " + fieldName + " like '%" + contractNo + "%' and " + fieldName + " is not null ";
			
					   sql+=" and instanceId not in(select instanceId from Workflow_InstanceInfor where deleteFlag = 1) group by " + fieldName + " order by " + fieldName +" desc";
				List maxSerialNos = this.flowInstanceManager.getResultBySQLQuery(sql);
				if (maxSerialNos != null && maxSerialNos.size() > 0) {
					String maxSerialNo = (String)maxSerialNos.get(0);
					String[] documentNoArray = maxSerialNo.split("-");
					String documentLastNo = documentNoArray[2];
					Integer maxSerialNoInt = Integer.valueOf(documentLastNo)+1;
					String zero = "";
					for(int i=0;i<3-maxSerialNoInt.toString().length();i++) {
						zero += "0";
					}
					serialNo = zero + maxSerialNoInt.toString();
					//serialNo = maxSerialNoInt.toString();
				}
				String sqlc ="select contractNo from Customize_Hetongshenpi where instanceId="+instanceId;
				List nowListc = this.flowInstanceManager.getResultBySQLQuery(sqlc);
				
				String contractNoOld =(String)nowListc.get(0);
				if (contractNoOld !=null && contractNoOld.length()>0){
					contractNo = contractNoOld;
				}else{
					contractNo = categoryName +"-"+reportYear+"-"+serialNo;
				}
			
				request.setAttribute("_ContractNo", contractNo);
				contract =true;
				
			}
			request.setAttribute("_Contract", contract);
			
			if (flow.getFlowId().intValue() == SubmitConstant.SubmitFlow_Report_Publish || flow.getFlowId().intValue() == SubmitConstant.SubmitFlow_Party_Publish) {
				//部门信息
				List departments = this.organizeManager.getDepartments();
				request.setAttribute("_Departments", departments);
				
				//用户信息
				List users = this.systemUserManager.getAllValid();
				request.setAttribute("_Users", users);
				
				//系统时间
				request.setAttribute("_SystemDate", new Date(System.currentTimeMillis()));
				
				bulletinAndMessage = true;
			}
			request.setAttribute("_BulletinAndMessage", bulletinAndMessage);
		}

		return "filedInstance";
	}
	
	//保存归档信息
	@RequestMapping(params = "method=saveFiled")
	public String saveFiled(HttpServletRequest request, HttpServletResponse response, FlowInstanceInforVo vo,DefaultMultipartHttpServletRequest multipartRequest) throws Exception {
		
		String instanceId = request.getParameter("instanceId");
		String contractNo = vo.getContractNo();
		if (instanceId != null && instanceId.length() > 0) {
			FlowInstanceInfor instance = (FlowInstanceInfor)this.flowInstanceManager.get(Integer.valueOf(instanceId));
			SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
			if(!instance.isFiled()) {
				// 删除文档中原有的归档信息
				List documentList = this.documentInforManager.getDocumentByReportId(Integer.valueOf(instanceId));
				if (documentList != null && documentList.size() > 0) {
					for (Iterator it = documentList.iterator(); it.hasNext(); ) {
						DocumentInfor tmpDocument = (DocumentInfor) it.next();
						this.documentInforManager.remove(tmpDocument);
					}
				}

				int[] categoryIds = vo.getCategoryIds();
				for (int k = 0; k < categoryIds.length; k++) {
					// 分类
					DocumentCategory category = (DocumentCategory) this.documentCategoryManager.get(categoryIds[k]);
					if (category != null) {
						DocumentInfor tempDocument = new DocumentInfor();
						tempDocument.setDocumentTitle(instance.getInstanceTitle());
						tempDocument.setAuthor(instance.getApplier());
						tempDocument.setEditor(systemUser);
						tempDocument.setDepartment(instance.getDepartment());
						tempDocument.setUpdateTime(new Date(System.currentTimeMillis()));
						tempDocument.setCreateTime(new Date(System.currentTimeMillis()));
						tempDocument.setReportId(Integer.valueOf(instanceId));
						tempDocument.setCategory(category);
						tempDocument = (DocumentInfor) this.documentInforManager.save(tempDocument);


						/** 给选中的归档分类和对应的文档信息添加默认权限:
						 * A.主办人:对归档分类的增删改浏览权限,对该文档的增删改浏览权限;
						 * B.提交人:对归档分类的浏览权限,对该文档的增删改浏览权限;
						 * C.审核人:对归档分类的浏览权限,对该文档的浏览权限;
						 * D.部门审核人(行发/党发/党收/内部报告/合同):对归档分类的浏览权限,对该文档的浏览权限;
						 * */
						List users = new ArrayList();
						//主办人
						SystemUserInfor charger = instance.getCharger();
						setCategoryDefaultRights(charger, category, DocumentCategoryRight._Right_Create);
						setCategoryDefaultRights(charger, category, DocumentCategoryRight._Right_Delete);
						setCategoryDefaultRights(charger, category, DocumentCategoryRight._Right_View);
						int[] rights = {DocumentInforRight._Right_Edit, DocumentInforRight._Right_Delete, DocumentInforRight._Right_View};
						setDocumentDefaultRights(tempDocument, charger, rights);
						users.add(charger);

						//提交人
						SystemUserInfor applier = instance.getApplier();
						if (!users.contains(applier)) {
							setCategoryDefaultRights(applier, category, DocumentCategoryRight._Right_View);
							setDocumentDefaultRights(tempDocument, applier, rights);
							users.add(applier);
						}

						//审核人
						int[] rights2 = {DocumentInforRight._Right_View};
						Set layers = instance.getLayers();
						for (Iterator it = layers.iterator(); it.hasNext(); ) {
							InstanceLayerInfor layer = (InstanceLayerInfor) it.next();
							Set checkInfors = layer.getCheckInfors();
							for (Iterator ci = checkInfors.iterator(); ci.hasNext(); ) {
								InstanceCheckInfor check = (InstanceCheckInfor) ci.next();
								SystemUserInfor checker = check.getChecker();
								if (!users.contains(checker)) {
									setCategoryDefaultRights(checker, category, DocumentCategoryRight._Right_View);
									setDocumentDefaultRights(tempDocument, checker, rights2);
									users.add(checker);
								}
							}
						}

						//部门审核人
						SystemUserInfor manager = instance.getManager();
						if (manager != null && instance.isManagerChecked() && !users.contains(manager)) {
							setCategoryDefaultRights(manager, category, DocumentCategoryRight._Right_View);
							setDocumentDefaultRights(tempDocument, manager, rights2);
							users.add(manager);
						}
						SystemUserInfor viceManager = instance.getViceManager();
						if (viceManager != null && instance.isViceManagerChecked() && !users.contains(viceManager)) {
							setCategoryDefaultRights(viceManager, category, DocumentCategoryRight._Right_View);
							setDocumentDefaultRights(tempDocument, viceManager, rights2);
						}
						/** ******* */
					}
				}
				instance.setFiled(true);
				this.flowInstanceManager.save(instance);

				/** 若是发文(行政/党群),则判断是否需要公告和发送讯息通知 */
				FlowDefinition flow = instance.getFlowDefinition();

				if (flow.getFlowId().intValue() == SubmitConstant.SubmitFlow_Report_Contract) {

					String flowName = "hetongshenpi";

					List<String> fieldNames = new ArrayList<String>();
					fieldNames.add("contractNo");
					String[] fieldValues = new String[1];
					for (int i = 0; i < 1; i++) {
						fieldValues[i] = vo.getContractNo().replaceAll(",", "");
					}

					this.commonManager.updateFormData(flowName, fieldNames, fieldValues, instance.getInstanceId(), 1);
				}
				if (flow.getFlowId().intValue() == SubmitConstant.SubmitFlow_Report_Publish || flow.getFlowId().intValue() == SubmitConstant.SubmitFlow_Party_Publish) {
					Date systemDate = new Date(System.currentTimeMillis());

					//发布公告
					String isBulletin = request.getParameter("isBulletin");
					if (("1").equals(isBulletin)) {
						InforDocument infor = new InforDocument();
						infor.setCreateTime(systemDate);
						infor.setHits(0);
						infor.setImportant(true);
						infor.setInforTime(systemDate);
						infor.setInforTitle(instance.getInstanceTitle());

						infor.setAuthor(instance.getApplier());
						if (instance.getDepartment() != null) {
							infor.setIssueUnit(instance.getDepartment().getOrganizeName());
						}

						InforCategory category = (InforCategory) this.inforCategoryManager.get(InforConstant.Cms_Category_Annouce);
						infor.setCategory(category);
						String templateName = category.getContentTemplate();
						String pagePath = category.getPagePath();

						//首页起止时间
						String startDate = request.getParameter("startDate");
						String endDate = request.getParameter("endDate");
						if (startDate != null && startDate.length() > 0) {
							infor.setStartDate(Date.valueOf(startDate));
						}
						if (endDate != null && endDate.length() > 0) {
							infor.setEndDate(Date.valueOf(endDate));
						}

						if (startDate != null && startDate.length() > 0 && endDate != null && endDate.length() > 0) {
							infor.setHomepage(true);
						}

						//附件
						String bulletinFiles = "";
						String attachment = instance.getAttach();
						String formalAttach = instance.getFormalAttach();
						String oldFiles = attachment;
						if (formalAttach != null && formalAttach.length() > 0) {
							oldFiles += "|" + formalAttach;
						}
						if (oldFiles != null && oldFiles.length() > 0) {
							//把该文件拷贝到CMS路径下
							String[] pathArray = oldFiles.split("\\|");
							for (int i = 0; i < pathArray.length; i++) {
								String oldPath = CoreConstant.Context_Real_Path + pathArray[i];
								File attachmentFile = new File(oldPath);
								if (attachmentFile != null) {
									String name = attachmentFile.getName();
									String tempPath = CoreConstant.Attachment_Path + "cms/attach/" + String.valueOf(System.currentTimeMillis());
									String tempFolder = CoreConstant.Context_Real_Path + tempPath;
									String newDbPath = tempPath + "/" + name;
									String newPath = CoreConstant.Context_Real_Path + newDbPath;

									//目的文件夹
									File thisFolder = new File(tempFolder);
									com.kwchina.core.util.File.createFilePath(thisFolder);
									com.kwchina.core.util.File.copy(oldPath, newPath);

									if (pathArray[i] != null && pathArray[i].length() > 0 && bulletinFiles.length() > 0) {
										bulletinFiles += "|" + newDbPath;
									} else {
										bulletinFiles += newDbPath;
									}
								}
							}
						}
						infor.setAttachment(bulletinFiles);

						//自动生成html静态页面并返回html文件保存路径
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("_InforDocument", infor);

						//附件信息
						if (bulletinFiles != null && bulletinFiles.length() > 0) {
							String[] arrayFiles = bulletinFiles.split("\\|");
							//字符串转换为utf-8编码
							String[] newArrayFiles = new String[arrayFiles.length];
							for (int i = 0; i < arrayFiles.length; i++) {
								newArrayFiles[i] = URLEncoder.encode(arrayFiles[i], CoreConstant.ENCODING);
							}
							map.put("_ArrayFiles", newArrayFiles);

							String[] attachmentNames = new String[arrayFiles.length];
							for (int k = 0; k < arrayFiles.length; k++) {
								attachmentNames[k] = com.kwchina.core.util.File.getFileName(arrayFiles[k]);
							}
							map.put("_AttachmentNames", attachmentNames);
						}

						map.put("base", request.getContextPath());
						if (templateName != null && templateName.length() > 0 && pagePath != null && pagePath.length() > 0) {
							templateName = templateName.split("/")[templateName.split("/").length - 1];
							String htmlPath = DocumentConverter.createHtml(templateName, pagePath, map);
							infor.setHtmlFilePath(htmlPath);
						}

						// 保存
						this.inforDocumentManager.save(infor);
					}


					//发送讯息
					int[] personIds = vo.getPersonIds();
					if (personIds != null && personIds.length > 0) {
						MessageInfor message = new MessageInfor();
						message.setSender(systemUser);
						message.setSendTime(new Timestamp(systemDate.getTime()));
						message.setIsImportant(1);
						message.setMessageContent(instance.getInstanceTitle());
						message.setMessageTitle(instance.getInstanceTitle());
						message.setReceiveType(1);

						//附件
						String messageFiles = "";
						String attachment = instance.getAttach();
						String formalAttach = instance.getFormalAttach();
						String oldFiles = attachment;
						if (formalAttach != null && formalAttach.length() > 0) {
							oldFiles += "|" + formalAttach;
						}
						if (oldFiles != null && oldFiles.length() > 0) {
							//把该文件拷贝到message路径下
							String[] pathArray = oldFiles.split("\\|");
							for (int i = 0; i < pathArray.length; i++) {
								String oldPath = CoreConstant.Context_Real_Path + pathArray[i];
								File attachmentFile = new File(oldPath);
								if (attachmentFile != null) {
									String name = attachmentFile.getName();
									String tempPath = CoreConstant.Attachment_Path + "message/" + String.valueOf(System.currentTimeMillis());
									String tempFolder = CoreConstant.Context_Real_Path + tempPath;
									String newDbPath = tempPath + "/" + name;
									String newPath = CoreConstant.Context_Real_Path + newDbPath;

									//目的文件夹
									File thisFolder = new File(tempFolder);
									com.kwchina.core.util.File.createFilePath(thisFolder);
									com.kwchina.core.util.File.copy(oldPath, newPath);

									if (pathArray[i] != null && pathArray[i].length() > 0 && messageFiles.length() > 0) {
										messageFiles += "|" + newDbPath;
									} else {
										messageFiles += newDbPath;
									}
								}
							}
						}
						message.setAttachment(messageFiles);

						Set messageReceives = new HashSet();
						for (int k = 0; k < personIds.length; k++) {
							ReceiveMessage tpReceive = new ReceiveMessage();
							tpReceive.setIsReaded(0);
							tpReceive.setMessage(message);
							SystemUserInfor tpUser = (SystemUserInfor) this.systemUserManager.get(personIds[k]);
							tpReceive.setReceiver(tpUser);

							messageReceives.add(tpReceive);
						}
						message.setReceives(messageReceives);

						this.messageInforManager.save(message);
					}
				}
			}
			/** ***********以下是归档到档案系统中的操作*************** */
			if(instance.getFlowDefinition().getFlowId()<90 || instance.getFlowDefinition().getFlowId()>100){
				filedToArchive(request, instance, false);
			}

			
			
			
		}
		return view(request, response);
	}
	
	
	/**
	 * 归档到档案系统中的方法 
	 * @param request
	 * @param instance
	 * @param isBatch
	 */

	public void filedToArchive(HttpServletRequest request,FlowInstanceInfor instance,boolean isBatch){
//		Connection conn = null;
//		Statement stmt_admin = null;
//		ResultSet rs_admin = null;
//
//		Statement stmt_contract = null;
//		ResultSet rs_contract = null;
//		try {
//			FlowDefinition flow = instance.getFlowDefinition();
//			int instanceId = instance.getInstanceId();
//			//所有附件
//			String[] allFiles = request.getParameterValues("attatchmentArray");
//
//			if(isBatch){
//				allFiles = instance.getAttach().split("\\|");
//			}
//
//			/*
//			 * 发文管理、收文管理、内部报告、制度评审 四类归入文书档案，合同审批归入合同档案
//			 */
//
//
//			conn=ArchiveConnectionManager.getInstance().getConnection();
//			stmt_admin = conn.createStatement();
//			stmt_contract = conn.createStatement();
//
//			/** 自定义表单数据 */
//			String flowName = CnToSpell.getFullSpell(flow.getFlowName());
//			String tableName = "Customize_" + flowName;
//
//			//根据流程名获取表列名
//			List columnNames = this.commonManager.getColumnNames(flowName);
//			if (columnNames != null && columnNames.size() > 0) {
//				List customizeDatas = this.commonManager.getFormData(flowName, instance.getInstanceId());
//				if (customizeDatas != null && customizeDatas.size() > 0) {
//					Object[] dataArray = (Object[])customizeDatas.get(0);
//
//
//					//获取档案系统FTP相关的信息
//					ServletContext context = request.getSession().getServletContext();
//					String ftpUrl = context.getInitParameter("ftpUrl");
//					int ftpPort = Integer.valueOf(context.getInitParameter("ftpPort"));
//					String ftpUserName = context.getInitParameter("ftpUserName");
//					String ftpPassword = context.getInitParameter("ftpPassword");
//
//					//把该文件拷贝到档案系统FTP中
//					String newAttachPath = "";
//					for (int i=0;i<allFiles.length;i++) {
//						String oldPath = CoreConstant.Context_Real_Path + allFiles[i];
//						oldPath = URLDecoder.decode(oldPath,"utf-8");
//						File attachmentFile = new File(oldPath);
//						if (attachmentFile != null) {
//
//							String nowTimeFolder = String.valueOf(System.currentTimeMillis());
//							String name = attachmentFile.getName();
//							String destFolder = CoreConstant.Administration_EveFile_Path + nowTimeFolder;
//							String newDbPath = destFolder + "/" + name;
////							String newPath = CoreConstant.Context_Real_Path + newDbPath;
//
//							// ftp创建目的文件夹
//							FtpUtils.makeDirecotory("", ftpUrl, ftpUserName, ftpPort, ftpPassword, CoreConstant.Administration_EveFile_Path, nowTimeFolder);
//
//							// ftp传输文件到档案系统服务器
//							//System.out.println(destFolder+"------");
//							//System.out.println(oldPath+"------====");
//							FtpUtils.upload("", ftpUrl, ftpUserName, ftpPort, ftpPassword, destFolder, oldPath, name);
//
//							if (allFiles[i] != null && allFiles[i].length() > 0 && newAttachPath.length() > 0) {
//								newAttachPath += "|" + newDbPath;
//							} else {
//								newAttachPath += newDbPath;
//							}
//						}
//					}
//
//					if (flow.getFlowId().intValue() == SubmitConstant.SubmitFlow_Report_Receive || flow.getFlowId().intValue() == SubmitConstant.SubmitFlow_Report_Publish
//							 || flow.getFlowId().intValue() == SubmitConstant.SubmitFlow_Report_Inside || flow.getFlowId().intValue() == SubmitConstant.SubmitFlow_Report_Order) {
//						/***文书档案相关字段***/
//						String fileTitle = "";   //文件题名
//						String dispatchNo = "";  //收发号
//						String fileDate = "";    //文件日期
//						int pageNumber = 0;      //页数
//						String fileCategory = "";//文件分类
//						String topic = "";       //主题词
//						String secretType = "";  //密级
//						String companyName = ""; //来文单位
//						String dutyName = "";    //责任者
//						String fileNumber = "";  //文件字号
//						String urgencyName = ""; //催办等级
//						String attchmentPath = newAttachPath;//附件
//
//
//						/**发文**/
//						if(flow.getFlowId().intValue() == SubmitConstant.SubmitFlow_Report_Publish){
//							fileTitle = instance.getInstanceTitle();
//
//							int m=0;
//							for (Iterator col=columnNames.iterator();col.hasNext();) {
//								String colName = (String)col.next();
//								String colValue = dataArray[m].toString();
//
//								if(colName.equals("documentNo")){
//									dispatchNo = colValue;
//								}
//								if(colName.equals("sendDate")){
//									fileDate = colValue;
//								}
//								if(colName.equals("organizeId")){
//									if(StringUtil.isNotEmpty(colValue) && !colValue.equals("0")){
//										OrganizeInfor organizeInfor = (OrganizeInfor)this.organizeManager.get(Integer.valueOf(colValue));
//										if(organizeInfor != null){
//											fileCategory = organizeInfor.getOrganizeName();
//										}
//
//									}
//								}
//								if(colName.equals("keywords")){
//									topic = colValue;
//								}
//								if(colName.equals("writerId")){
//									if(StringUtil.isNotEmpty(colValue) && !colValue.equals("0")){
//										SystemUserInfor tmpUser = (SystemUserInfor)this.systemUserManager.get(Integer.valueOf(colValue));
//										dutyName = tmpUser.getPerson().getPersonName();
//									}
//								}
//								if(colName.equals("urgencyName")){
//									urgencyName = colValue;
//								}
//
//								m++;
//							}
//						}
//
//						/**收文**/
//						if(flow.getFlowId().intValue() == SubmitConstant.SubmitFlow_Report_Receive){
//							fileTitle = instance.getInstanceTitle();
//
//							int m=0;
//							for (Iterator col=columnNames.iterator();col.hasNext();) {
//								String colName = (String)col.next();
//								String colValue = dataArray[m].toString();
//
//								if(colName.equals("documentNo")){
//									dispatchNo = colValue;
//								}
//								if(colName.equals("receiveDate")){
//									fileDate = colValue;
//								}
//								if(colName.equals("fileNum")){
//									if(StringUtil.isNotEmpty(colValue)){
//										if(StringUtil.isNumeric(colValue)){
//											pageNumber = Integer.valueOf(colValue);
//										}
//									}
//								}
//
//								if(colName.equals("secretName")){
//									secretType = colValue;
//								}
//
//								if(colName.equals("unitName") || colName.equals("selUnitName")){
//									companyName += colValue + "  ";
//								}
//
//								if(colName.equals("receiverId")){
//									if(StringUtil.isNotEmpty(colValue) && !colValue.equals("0")){
//										SystemUserInfor tmpUser = (SystemUserInfor)this.systemUserManager.get(Integer.valueOf(colValue));
//										dutyName = tmpUser.getPerson().getPersonName();
//									}
//								}
//								if(colName.equals("reportNo")){
//									fileNumber = colValue;
//								}
//								if(colName.equals("urgencyName")){
//									urgencyName = colValue;
//								}
//
//								m++;
//							}
//						}
//
//						/**内部报告**/
//						if(flow.getFlowId().intValue() == SubmitConstant.SubmitFlow_Report_Inside){
//							fileTitle = instance.getInstanceTitle();
//							dutyName = instance.getCharger() != null ? instance.getCharger().getPerson().getPersonName() : "";
//							int m=0;
//							for (Iterator col=columnNames.iterator();col.hasNext();) {
//								String colName = (String)col.next();
//								String colValue = dataArray[m].toString();
//
//								if(colName.equals("fileDate")){
//									fileDate = colValue;
//								}
//								if(colName.equals("category")){
//									fileCategory = colValue;
//								}
//								if(colName.equals("fromDepartment")){
//									companyName = colValue;
//								}
//								if(colName.equals("fileNo")){
//									fileNumber = colValue;
//								}
//
//								if(colName.equals("urgencyName")){
//									urgencyName = colValue;
//								}
//
//								m++;
//							}
//						}
//
//						/**制度评审**/
//						if(flow.getFlowId().intValue() == SubmitConstant.SubmitFlow_Report_Order){
//							fileTitle = instance.getInstanceTitle();
//							int m=0;
//							for (Iterator col=columnNames.iterator();col.hasNext();) {
//								String colName = (String)col.next();
//								String colValue = dataArray[m].toString();
//
//								if(colName.equals("orderDate")){
//									fileDate = colValue;
//								}
//								if(colName.equals("leibie")){
//									fileCategory = colValue;
//								}
//								if(colName.equals("attner")){
//									dutyName = colValue;
//								}
//								if(colName.equals("orderNo")){
//									fileNumber = colValue;
//								}
//
//								if(colName.equals("urgencyName")){
//									urgencyName = colValue;
//								}
//
//								m++;
//							}
//						}
//
//						//先判断档案系统中是否已存在
//						String adminSql_exit = "select * from Admin_Archive_FileInfor where reportId = '" + instanceId + "'";
//						rs_admin = stmt_admin.executeQuery(adminSql_exit);
//						if(rs_admin.next()){  //已存在 ，修改
//							String adminSql_update = "update Admin_Archive_FileInfor set dispatchNo='" + dispatchNo + "',companyName='" + companyName + "',fileDate='" + fileDate + "'," +
//									"fileTitle='" + fileTitle + "',fileNumber='" + fileNumber + "',attachmentNumber=" + attchmentPath.split("\\|").length + ",secretType='" + secretType + "'," +
//									"attchmentPath='" + attchmentPath + "',topic='" + topic + "',pageNumber=" + pageNumber + ",fileCategory='" + fileCategory + "',urgencyName='" + urgencyName+"'," +
//									"dutyName='" + dutyName + "' where reportId="+instanceId;
//
//							stmt_admin.executeUpdate(adminSql_update);
//							//System.out.println("========"+instanceId+"========已插入");
//						}else {   //不存在 ，新增
//							String adminSql = "insert into Admin_Archive_FileInfor (dispatchNo,archiveNo,archiveYear,term,fileNo," +
//									"boxNo,companyName,fileDate,fileTitle,fileNumber,attachmentNumber,attachmentNo,secretType," +
//									"attchmentPath,keyWord,topic,status,startPage,pageNumber,endPage,sourceType,reportId," +
//									"organizeId,wordId,dutyId,userId,borrowStatus,fileCategory,subjectNo,urgencyName,dutyName) " +
//									"values ('"+dispatchNo+"','',0,-1,'','','"+companyName+"','"+fileDate+"','"+fileTitle+"','"+fileNumber+"'," +
//										attchmentPath.split("\\|").length+",'','"+secretType+"','"+attchmentPath+"','','"+topic+"'," +
//										"0,0,"+pageNumber+",0,1,"+instanceId+",'',null,null,1,0,'"+fileCategory+"','','"+urgencyName+"','"+dutyName+"')";
//
////							System.out.println(adminSql);
//							boolean re = stmt_admin.execute(adminSql);
//						}
//
//
////						System.out.println(re);
//
//					}else if (flow.getFlowId().intValue() == SubmitConstant.SubmitFlow_Report_Contract){
//						/***合同***/
//						String contractName = "";     //合同名称
//						String contractNo_archive = "";       //合同号
//						String contractMoney = "";    //合同金额
////					String startDate = "";        //合同开始日期
////					String endDate = "";          //合同结束日期
//						String duringDate = "";       //履约期限
//						String signPerson = "";       //签约人
//						String toCompany = "";        //合同单位
//						String memo = "";             //备注
//						String contractType = "";     //合同类型
//						String attchmentPath = newAttachPath;//附件
//
//
//						contractName = instance.getInstanceTitle();
//						int m=0;
//						for (Iterator col=columnNames.iterator();col.hasNext();) {
//							String colName = (String)col.next();
//							String colValue = dataArray[m].toString();
//
//							if(colName.equals("contractNo")){
//								contractNo_archive = colValue;
//							}
//							if(colName.equals("contractPrice")){
//								contractMoney = colValue;
//							}
//							if(colName.equals("contractPeriod")){
//								duringDate = colValue;
//							}
//							if(colName.equals("attner")){
//								signPerson = colValue;
//							}
//							if(colName.equals("oppositeUnit")){
//								toCompany = colValue;
//							}
//							if(colName.equals("memo")){
//								memo = colValue;
//							}
//							if(colName.equals("contractType")){
//								contractType = colValue;
//							}
//
//							m++;
//						}
//
//
//						//先判断档案系统中是否已存在
//						String contractSql_exit = "select * from Admin_Archive_ContractInfor where reportId = '" + instanceId + "'";
//						rs_contract = stmt_contract.executeQuery(contractSql_exit);
//						if(rs_contract.next()){  //已存在 ，修改
//							String contractSql_update = "update Admin_Archive_ContractInfor set fileDate='" + DateHelper.getString(new java.util.Date()) + "',contractName='" + contractName + "'" +
//									",contractNo='" + contractNo_archive + "',contractType='" + contractType + "',toCompany='" + toCompany + "'," +
//									"contractMoney='" + contractMoney + "',signPerson='" + signPerson + "',attachmentNumber=" + attchmentPath.split("\\|").length + "," +
//									"attchmentPath='" + attchmentPath + "',memo='" + memo + "',duringDate='" + duringDate + "' where reportId="+instanceId;
//
//							stmt_contract.executeUpdate(contractSql_update);
//						}else {   //不存在 ，新增
//							String contractSql = "insert into Admin_Archive_ContractInfor (dutyDepartment,fileDate,contractName," +
//									"contractNo,accountNo,contractType,toCompany,contractMoney,amount,startDate,endDate,approvePerson," +
//									"signPerson,signDate,realAmount,realDate,archiveNo,fileNo,pageNumber,charge,tax,archiveYear,term," +
//									"boxNo,attachmentNumber,secretType,attchmentPath,status,sourceType,reportId,memo,userId,subjectNo,duringDate) " +
//									"values('','"+DateHelper.getString(new java.util.Date())+"','"+contractName+"','"+contractNo_archive+"','','"+contractType+"'," +
//											"'"+toCompany+"','"+contractMoney+"','',null,null,'','"+signPerson+"',null,'','','','',0,'','',0,-1," +
//											"'',"+attchmentPath.split("\\|").length+",1,'"+attchmentPath+"',0,1,"+instanceId+",'"+memo+"',1,'','"+duringDate+"')";
//							boolean b = stmt_contract.execute(contractSql);
//						}
//
//
//					}
//
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				conn.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
		System.out.println("暂时关闭导入数据到档案系统");
	}
	
	
	/**
	 * 批量导入数据到档案系统
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=batchToArchive")
	public void batchToArchive(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jsonObj = new JSONObject();
		int suc = 0;
		try {
			String beginDate = request.getParameter("beginDate");
			String endDate = request.getParameter("endDate");
			
			//类型
			String flowIdStr = request.getParameter("flowIds");
			String[] flowIds = flowIdStr.split(",");
			
			//得到时间范围内的文
			for(String tmpFlowId : flowIds){
				String queryString = "from FlowInstanceInfor instance where instance.deleteFlag=0 and suspended=0 and instance.updateTime>='"+beginDate+" 00:00:00' and instance.updateTime<='"+endDate+" 23:59:59' and instance.flowDefinition.flowId = " + tmpFlowId;
				List<FlowInstanceInfor> instances = this.flowInstanceManager.getResultByQueryString(queryString);
				
				System.out.println("flowId----"+tmpFlowId+"------" + instances.size());
				for(FlowInstanceInfor instance : instances){
					//System.out.println(instance.getInstanceId()+"---------------");
					filedToArchive(request, instance, true);
				}
			}
			suc = 1;
		} catch (Exception e) {
			suc = 0;
			e.printStackTrace();
		}
		
		//jsonObj.put("suc", suc);
		
		//设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(suc);		
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
			
			
			List<FlowInstanceInfor> returnInstances_m = new ArrayList<FlowInstanceInfor>();
			List<FlowInstanceInfor> returnInstances = new ArrayList<FlowInstanceInfor>();
			List<String> warningStrs = new ArrayList<String>();
			if(isMobile){
				//移动端需要显示的
				returnInstances_m = this.flowInstanceManager.getNeedPushInstances(systemUser, null);
			}else {
				//得到待办
				Map<String, Object> map = this.flowInstanceManager.getNeedDealInstances(systemUser);
				
				//PC端需要显示的待办
				returnInstances = (List<FlowInstanceInfor>)map.get("ReturnInstances");
				warningStrs = (List<String>)map.get("WarningStrs");
			}
			
			
//			//移动端需要显示的
//			List<FlowInstanceInfor> returnInstances_m = (List<FlowInstanceInfor>)map.get("ReturnInstances_m");
			
			
			JSONObject jsonObj = new JSONObject();
			JSONConvert convert = new JSONConvert();
			JSONArray jsonArray = new JSONArray();
			//通知Convert，哪些关联对象需要获取
			List awareObject = new ArrayList();
			awareObject.add("flowDefinition.fileRole");
			awareObject.add("department");
			awareObject.add("applier.person");
			
			
			
			
			if(isMobile){
				jsonArray = convert.modelCollect2JSONArray(returnInstances_m, awareObject);
			}else {
				jsonArray = convert.modelCollect2JSONArray(returnInstances, awareObject);
			}
			
			jsonObj.put("_Instances", jsonArray);
			jsonObj.put("_WarningStrs", warningStrs);
			jsonObj.put("_InstanceCount", returnInstances_m.size());
			
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
//		List rLayers = this.flowInstanceManager.getCurrentProcessLayers(instance);
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
	
	//申请盖章
	@RequestMapping(params = "method=applyStamp")
	public String applyStamp(HttpServletRequest request, HttpServletResponse response) {
		
		String instanceId = request.getParameter("instanceId");
		if (instanceId != null && instanceId.length() > 0) {
			FlowInstanceInfor instance = (FlowInstanceInfor)this.flowInstanceManager.get(Integer.valueOf(instanceId));

			instance.setStamped(1);
			
			this.flowInstanceManager.save(instance);
		}
		return "redirect:instanceInfor.do?method=view&instanceId=" + instanceId;
	}
	
	
	//盖章
	@RequestMapping(params = "method=stamp")
	public String stamp(HttpServletRequest request, HttpServletResponse response) {
		
		String instanceId = request.getParameter("instanceId");

		if (instanceId != null && instanceId.length() > 0) {
			FlowInstanceInfor instance = (FlowInstanceInfor)this.flowInstanceManager.get(Integer.valueOf(instanceId));
			
			
			boolean contract = false;
			FlowDefinition flow = instance.getFlowDefinition();
			String contractNo="";
			if(flow.getFlowId().intValue() == SubmitConstant.SubmitFlow_Report_Contract){
				
				String serialNo = "001";
				//String serialNo = "1";
				//response.setCharacterEncoding("utf-8");
				//request.setCharacterEncoding("utf-8");
				String tableName = "Customize_Hetongshenpi";
				String fieldName = "contractNo";
				
				String sqls ="select category from Customize_Hetongshenpi where instanceId="+instanceId;
				List nowList = this.flowInstanceManager.getResultBySQLQuery(sqls);
				
				String categoryName =(String)nowList.get(0);
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
				Calendar cal = Calendar.getInstance();
				java.util.Date sDate = cal.getTime();
				String sDateStr= sdf.format(sDate);
				
				//String categoryName = new String(request.getParameter("categoryName").getBytes("ISO-8859-1"), "utf-8");
				String fieldYear = "reportYear";
				String reportYear = sDateStr;
				
				contractNo= categoryName+"-"+sDateStr;
				String sql = "select " + fieldName + " from " + tableName + " where " + fieldName + " like '%" + contractNo + "%' and " + fieldName + " is not null ";
			
					   sql+=" and instanceId not in(select instanceId from Workflow_InstanceInfor where deleteFlag = 1) group by " + fieldName + " order by " + fieldName +" desc";
				List maxSerialNos = this.flowInstanceManager.getResultBySQLQuery(sql);
				if (maxSerialNos != null && maxSerialNos.size() > 0) {
					String maxSerialNo = (String)maxSerialNos.get(0);
					String[] documentNoArray = maxSerialNo.split("-");
					String documentLastNo = documentNoArray[2];
					Integer maxSerialNoInt = Integer.valueOf(documentLastNo)+1;
					String zero = "";
					for(int i=0;i<3-maxSerialNoInt.toString().length();i++) {
						zero += "0";
					}
					serialNo = zero + maxSerialNoInt.toString();
					//serialNo = maxSerialNoInt.toString();
				}
				String sqlc ="select contractNo from Customize_Hetongshenpi where instanceId="+instanceId;
				List nowListc = this.flowInstanceManager.getResultBySQLQuery(sqlc);
				
				String contractNoOld =(String)nowListc.get(0);
				if (contractNoOld !=null && contractNoOld.length()>0){
					contractNo = contractNoOld;
				}else{
					contractNo = categoryName +"-"+reportYear+"-"+serialNo;
				}
			
				request.setAttribute("_ContractNo", contractNo);
				contract =true;
		
				
				String flowName = "hetongshenpi";
				
				List<String> fieldNames = new ArrayList<String>();
				fieldNames.add("contractNo");
				String[] fieldValues =new String[1];
				for (int i=0;i<1;i++) {
					fieldValues[i] = contractNo.replaceAll(",", "");
				}
				
				this.commonManager.updateFormData(flowName, fieldNames, fieldValues, instance.getInstanceId(), 1);
			}
			

			instance.setStamped(2);
			
			this.flowInstanceManager.save(instance);
		}
		return "redirect:instanceInfor.do?method=view&instanceId=" + instanceId;
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
			FlowInstanceInfor instanceInfor = (FlowInstanceInfor) this.flowInstanceManager.get(Integer.valueOf(rowId));
			
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
			int j = 3;
			Set rights = instanceInfor.getRights();
			if (instanceInfor.getFlowDefinition().getFlowId() >83 && instanceInfor.getFlowDefinition().getFlowId() < 89) {
				for (Iterator it = rights.iterator(); it.hasNext(); ) {
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
				System.out.println(viewUserIds);
				request.setAttribute("_InstanceId", rowId);
				/*request.setAttribute("_EditUserIds", editUserIds);
				request.setAttribute("_DeleteUserIds", deleteUserIds);*/
				request.setAttribute("_ViewUserIds", viewUserIds);
				/*request.setAttribute("_EditRoleIds", editRoleIds);
				request.setAttribute("_DeleteRoleIds", deleteRoleIds);*/
				request.setAttribute("_ViewRoleIds", viewRoleIds);
				request.setAttribute("_RightType", rightType);
			}else if(instanceInfor.getFlowDefinition().getFlowId() >89 && instanceInfor.getFlowDefinition().getFlowId() <101){

				viewUserIds[0] = instanceInfor.getManager().getPersonId();
				viewUserIds[1] = instanceInfor.getApplier().getPersonId();
				if (instanceInfor.getViceManager() != null){
					viewUserIds[2] = instanceInfor.getViceManager().getPersonId();
				}else{
					viewUserIds[2] = 0;
				}
				Set<InstanceLayerInfor> layers = instanceInfor.getLayers();
				for (Iterator it = layers.iterator(); it.hasNext(); ) {
					InstanceLayerInfor layer = (InstanceLayerInfor) it.next();
					Set<InstanceCheckInfor> checkInfors = layer.getCheckInfors();
					for (Iterator cit = checkInfors.iterator(); cit.hasNext(); ) {
						InstanceCheckInfor checkInfor = (InstanceCheckInfor)cit.next();
						int userId = checkInfor.getChecker().getPersonId();
						viewUserIds[j] = userId;
						j += 1;
					}
				}
				int[] viewUserIdss = new int[users.size()];

				for (Iterator it = rights.iterator(); it.hasNext(); ) {
					InstanceInforRight right = (InstanceInforRight) it.next();
					if (right instanceof InstanceInforUserRight) {
						rightType = 1;
						/** 用户权限 */
						InstanceInforUserRight userRight = (InstanceInforUserRight) right;
						int userId = userRight.getSystemUser().getPersonId().intValue();

						// 浏览权限
						if (this.instanceInforRightManager.hasRight(right, InstanceInforRight._Right_View)) {
							viewUserIdss[k] = userId;
						}
					} else if (right instanceof InstanceInforRoleRight) {
						/** 角色权限 */
						InstanceInforRoleRight roleRight = (InstanceInforRoleRight) right;
						int roleId = roleRight.getRole().getRoleId().intValue();

						// 浏览权限
						if (this.instanceInforRightManager.hasRight(right, InstanceInforRight._Right_View)) {
							viewRoleIds[k] = roleId;
						}
					}
					k += 1;
				}
				int[] allviewUserIds = ArrayUtils.addAll(viewUserIds,viewUserIdss);


//				viewUserIds[viewUserIds.length] = instanceInfor.getViceManager().getPersonId();
//				viewUserIds[viewUserIds.length] = instanceInfor.getManager().getPersonId();
//				viewUserIds[viewUserIds.length] = instanceInfor.getApplier().getPersonId();
//				System.out.println(viewUserIds);
				request.setAttribute("_InstanceId", rowId);
				/*request.setAttribute("_EditUserIds", editUserIds);
				request.setAttribute("_DeleteUserIds", deleteUserIds);*/
				request.setAttribute("_ViewUserIds", allviewUserIds);
				/*request.setAttribute("_EditRoleIds", editRoleIds);
				request.setAttribute("_DeleteRoleIds", deleteRoleIds);*/
				request.setAttribute("_ViewRoleIds", viewRoleIds);
				request.setAttribute("_RightType", rightType);

			}
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
			instanceInfor = (FlowInstanceInfor) this.flowInstanceManager.get(Integer.valueOf(instanceId));

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
			this.flowInstanceManager.save(instanceInfor);
		}
	}
	
	

//	导出excel 
	@RequestMapping(params = "method=excel")
	public String excel(HttpServletRequest request, HttpServletResponse response)throws Exception {
		try{
		String flowId = request.getParameter("flowId");
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		String flowName = "";
		
		// 构造查询语句
		String[] queryString = new String[2];
		queryString[0] = "from FlowInstanceInfor instance where 1=1";
		queryString[1] = "select count(instanceId) from FlowInstanceInfor instance where 1=1";
		String condition = "";
		
		// 所属流程Id
		FlowDefinition flow = new FlowDefinition();
		String tableName = "";
		if (flowId != null && flowId.length() > 0) {
			condition += " and flowDefinition.flowId = " + flowId;
			flow = (FlowDefinition)this.flowManager.get(Integer.valueOf(flowId));
			flowName = CnToSpell.getFullSpell(flow.getFlowName());
			tableName = "Customize_" + flowName;
		}
		
		//根据流程名获取表列名
		List columnNames = this.commonManager.getColumnNames(flowName);
		
		// 是否删除
		String deleted = request.getParameter("deleted");
		if (deleted != null && ("true").equals(deleted)) {
			condition += " and deleteFlag = 1";
		}else {
			condition += " and deleteFlag = 0";
		}

		/********************************/
		//获取董事会特定角色的角色信息（ID为固定）
		RoleInfor boardRole = (RoleInfor)roleManager.get(22);
		
		//获取合同审批特定角色的角色信息（ID为固定）
		RoleInfor contractRole = (RoleInfor)roleManager.get(24);
		
		//获取合同审批特定角色的角色信息（ID为固定）
		RoleInfor lawRole = (RoleInfor)roleManager.get(26);
		
//		boolean isBoard  = roleManager.belongRole(systemUser, boardRole);
		boolean isContract = roleManager.belongRole(systemUser, contractRole);
		boolean isLaw = roleManager.belongRole(systemUser, lawRole);
		
//		if(isBoard){
//			
//		}
		
		/********************************/
//		合同查看角色
		if(isContract && (flowId.equals("86")||flowId.equals("87"))){
			
		}else{
		
		// 申请人
		condition += " and (applier.personId = " + systemUser.getPersonId();
		
		// 部门审核人(A.未中止;B.中止前已审.)
		condition += " or ((manager.personId = " + systemUser.getPersonId() + " or viceManager.personId = " + systemUser.getPersonId() + ") and submiterWord is null" +
				" or (manager.personId = " + systemUser.getPersonId() + " and managerChecked = 1) or (viceManager.personId = " + systemUser.getPersonId() + " and viceManagerChecked = 1))";
		
		// 主办人
		condition += " or (charger.personId = " + systemUser.getPersonId() + " and startTime is not null)";
		
		
		// 如果本流程的归档人为固定角色时，同样有查看权限
		if(flow.getFilerType() == 1 && flow.getFileRole() != null){
			String sysRoleStr = "";
			
			Set<RoleInfor> tmpRoleSet = systemUser.getRoles();
			List<RoleInfor> tmpRoleList = new ArrayList<RoleInfor>();
			
			for(Iterator it = tmpRoleSet.iterator();it.hasNext();){
				tmpRoleList.add((RoleInfor)it.next());
			}
			for(int i=0;i<tmpRoleList.size();i++){
				sysRoleStr += tmpRoleList.get(i).getRoleId().toString();
				if(i<tmpRoleList.size()-1){
					sysRoleStr += ",";
				}
			}
			
			if(!sysRoleStr.equals("") && sysRoleStr != null){
				condition += " or (instance.flowDefinition.fileRole.roleId in ("+sysRoleStr+"))";
			}
			
		}
		
		
		// 审核人
		condition += " or (instanceId in (select layer.instance.instanceId from InstanceLayerInfor layer where layerId in " +
				"(select checkInfor.layerInfor.layerId from InstanceCheckInfor checkInfor where checkInfor.checker.personId = " + systemUser.getPersonId() + " )))";
		
		//	权限表中设置的浏览权限
		condition +=  " or (instanceId in (select userRight.instance.instanceId from InstanceInforUserRight userRight where userRight.systemUser.personId =" + systemUser.getPersonId() + "))";
		condition += " or (instanceId in " + "(select roleRight.instance.instanceId from InstanceInforRoleRight roleRight,SystemUserInfor user where roleRight.role in " + "elements(user.roles) and user.personId = " + systemUser.getPersonId() + ")))";
		
		
		}
		queryString[0] += condition;
		queryString[1] += condition;
		
		//获取自定义表单查询字段
		Map map = getSearchFields(request, response);
		JSONArray searchFields = (JSONArray) map.get("_SearchFields");
		
		//Map maps = new HashMap();
		  
		
		//构造查询条件
		String[] params = getSearchParams(request);
		String cusCondition	= "";	//自定义查询条件
		if (params[2].equals("true")) {
			if (params[3] != null && params[3].length() > 0) {
				
				JSONObject filter = JSONObject.fromObject(params[3]);
				JSONArray rules = filter.getJSONArray("rules");		//取数据中的查询信息:查询字段,查询条件,查询数据
				if (rules != null && rules.size() > 0) {
					for (int i=0;i<rules.size();i++) {
						JSONObject tmpObj = (JSONObject)rules.get(i);
						String fieldValue = tmpObj.getString("field");	//查询字段
						String opValue = tmpObj.getString("op");		//查询条件:大于,等于,小于..
						String dataValue = tmpObj.getString("data");	//查询数据
						
						//处理查询条件
						boolean has = false;
						if (searchFields != null && searchFields.size() > 0) {
							for (int j=0;j<searchFields.size();j++) {
								JSONArray fieldArray = (JSONArray) searchFields.get(j);
								JSONObject tmpField = (JSONObject)fieldArray.get(1);
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
			List instancIds = this.flowInstanceManager.getResultBySQLQuery(sql);
			if (instancIds != null && instancIds.size() > 0) {
				String instancIdsStr = "";
				int index = 0;
				for (Iterator it=instancIds.iterator();it.hasNext();) {
					instancIdsStr += ((Integer)it.next()).toString();
					if (index < instancIds.size()-1) {
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
		
		queryString[0] += " order by " + params[0] + " " + params[1];
		
		//queryString = this.flowInstanceManager.generateQueryString(queryString, getSearchParams(request));

		String page = "1"; // 当前页
		String rowsNum = "10000"; // 每页显示的行数
		
		Pages pages = new Pages(request);
		pages.setPage(Integer.valueOf(page));
		pages.setPerPageNum(Integer.valueOf(rowsNum));
		
		
		List listCount = this.flowInstanceManager.getResultByQueryString(queryString[1]);
		String ss = listCount.get(0).toString();
		
		Pages pagesExcel = new Pages(request);
		pagesExcel.setPage(Integer.valueOf(page));
		pagesExcel.setPerPageNum(Integer.valueOf(ss)+1);
		List list = new ArrayList();
		JSONObject jsonObj = new JSONObject();

		// 定义rows，存放数据
		JSONArray rows = new JSONArray();
		PageList plExcel = new  PageList();
		if (params[2].equals("true") && isLaw && flowId.equals("86") ) {
			 plExcel = this.flowInstanceManager.getResultByQueryString(queryString[0], queryString[1], true, pagesExcel);
			
			 list = plExcel.getObjectList();
			// plExcel.setPages(pages);
			// plExcel.setPageShowString(pageShowString);
			// 定义返回的数据类型：json，使用了json-lib
			//pagesExcel.setPage(Integer.valueOf(page));
			//pagesExcel.setPerPageNum(Integer.valueOf(rowsNum));
			
			jsonObj.put("page", plExcel.getPages().getCurrPage()); // 当前页(名称必须为page)
			jsonObj.put("total", plExcel.getPages().getTotalPage()); // 总页数(名称必须为total)
			jsonObj.put("records", plExcel.getPages().getTotals()); // 总记录数(名称必须为records)
			//List list = pl.getObjectList();
			//plExcel.setPages(pagesExcel);
		
		}else{
			PageList pl = this.flowInstanceManager.getResultByQueryString(queryString[0], queryString[1], true, pages);
			 list = pl.getObjectList();
			
			// 定义返回的数据类型：json，使用了json-lib
		
			jsonObj.put("page", pl.getPages().getCurrPage()); // 当前页(名称必须为page)
			jsonObj.put("total", pl.getPages().getTotalPage()); // 总页数(名称必须为total)
			jsonObj.put("records", pl.getPages().getTotals()); // 总记录数(名称必须为records)
		}
	
	
		
	
	

		//取需要的字段信息返回
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Iterator it = list.iterator();
		while(it.hasNext()){
			FlowInstanceInfor fi = (FlowInstanceInfor)it.next();
			
			boolean canAdd = false;
			
			// 判断是否为当前审核人
			InstanceCheckInfor tmpCheck = this.flowInstanceManager.isChecker(fi, systemUser);
			if(isLaw){
				canAdd = true;
			}else if (tmpCheck != null) {
				// 能否看见
				if (tmpCheck.getEndDate() != null 
						|| (tmpCheck.getEndDate() == null 
							&& tmpCheck.getLayerInfor().getStatus() != InstanceLayerInfor.Layer_Status_End
							&& tmpCheck.getLayerInfor().getStatus() != InstanceLayerInfor.Layer_Status_Suspended
							&& !fi.isSuspended())) {
					canAdd = true;
				}
			}else {
				canAdd = true;
			}
			
		
			
			if (canAdd) {
				JSONObject obj = new JSONObject();
				obj.put("instanceId", fi.getInstanceId());
				obj.put("instanceTitle", fi.getInstanceTitle());
				String startTime = " ";
				if (fi.getStartTime() != null) {
					startTime = sdf.format(fi.getStartTime());
				}
				obj.put("startTime", startTime);
				String checker = " ";
				if(fi.getManager() != null){
					 checker= fi.getManager().getPerson().getPersonName();
				}
			
				obj.put("checker", checker);
				obj.put("updateTime", sdf.format(fi.getUpdateTime()));
				obj.put("applier", fi.getApplier().getPerson().getPersonName());
				obj.put("department", fi.getDepartment().getOrganizeName());
				obj.put("flow", fi.getFlowDefinition().getFlowName());
				//obj.put("charger", fi.getFlowDefinition().getCharger().getPerson().getPersonName());
				if (fi.getCharger() != null) {
					obj.put("charger", fi.getCharger().getPerson().getPersonName());
				}else {
					obj.put("charger", null);
				}
				String status = "草稿";
				if (fi.isFiled()) {
					status = "已归档";
				}else if (fi.getEndTime() != null) {
					status = "已结束待归档";
				}else if (fi.isSuspended()) {
					status = "已暂停";
				}else if (fi.getStartTime() != null) {
					
					//获取当前处理层,并判断处理情况:进行中,已完毕
					String layerStatus = "";
					List currentLayers = this.flowInstanceManager.getCurrentProcessLayers(fi);
					int i = 0;
					for (Iterator cl=currentLayers.iterator();cl.hasNext();) {
						InstanceLayerInfor layer = (InstanceLayerInfor)cl.next();
						if (layer.getStatus() == InstanceLayerInfor.Layer_Status_Normal) {
							layerStatus += layer.getLayerName();
						}else if (layer.getStatus() == InstanceLayerInfor.Layer_Status_End) {
							layerStatus += layer.getLayerName() + "已暂停";
						}else if (layer.getStatus() == InstanceLayerInfor.Layer_Status_Suspended) {
							layerStatus += layer.getLayerName() + "已终止";
						}else if (layer.getStatus() == InstanceLayerInfor.Layer_Status_Finished) {
							layerStatus += layer.getLayerName() + "已完毕";
						}
						
						if (i != currentLayers.size()-1) {
							layerStatus += ",";
						}
						
						i++;
					}
					status = layerStatus;
				}else if (fi.getManager() !=null && fi.getViceManager() == null && fi.isViceManagerChecked() || fi.getViceManager() != null && fi.getManager() == null && fi.isViceManagerChecked() || fi.getManager() != null && fi.getViceManager() != null && fi.isManagerChecked() && fi.isViceManagerChecked() && fi.getSubmiterWord() == null) {
					status = "部门已审核";
				}else if ((fi.getManager() != null || fi.getViceManager() != null) && fi.getSubmiterWord() == null) {
					status = "部门审核中";
				}
				obj.put("status", status);
				obj.put("attach", fi.getAttach());
				
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
		//jsonObj.put("rows", rows); // 返回到前台每页显示的数据(名称必须为rows)
		
		
/** ********导出Excel操作****** */
		
		boolean isExport=true;
		if (params[2].equals("true")&& isLaw && flowId.equals("86")) {
			
			
			 List<Map<String, String>> maps = (List<Map<String, String>>) JSONArray.toCollection(rows, Map.class);
			
			
			
			long time = System.currentTimeMillis();
			String filePath ="/uploadfiles/submit/excel/";		
			String fileTitle = "";
			
			
				fileTitle = "contract" ;
		
			
			ExcelObject object = new ExcelObject();
			object.setFilePath(filePath);
			object.setFileName(fileTitle);
			object.setTitle(fileTitle);
			
			List rowName = new ArrayList();
			String[][] data = new String[17][rows.size()];
			int k = 0;//列数
			
		
				rowName.add("序号");
				rowName.add("归属公司");
				rowName.add("合同编号");
				rowName.add("合同名称");			
				rowName.add("合同类型");
				rowName.add("申请人");
				rowName.add("状态");
				rowName.add("合同相对方");
				rowName.add("合同总金额");
				rowName.add("合同时限");
				rowName.add("经办部门");
				rowName.add("会审部门");
				rowName.add("批准人");
				rowName.add("签订日期");
				rowName.add("流水号");
				rowName.add("备注");
				k = 17;
			
			
			
			for (int i = 0; i < maps.size(); i++) {
				Map map1 = new HashMap();
				
				map1 = maps.get(i);
				
				
					
					data[0][i] = String.valueOf(i+1);
					data[1][i] = map1.get("category").toString();
					data[2][i] = map1.get("contractNo").toString();
					data[3][i] = map1.get("instanceTitle").toString();
					data[4][i] = map1.get("contractType").toString();
					data[5][i] = map1.get("applier").toString();
					data[6][i] = map1.get("status").toString();
					data[7][i] = map1.get("oppositeUnit").toString();
					data[8][i] = map1.get("contractPrice").toString();
					data[9][i] = map1.get("contractPeriod").toString();
					data[10][i] = map1.get("department").toString();
					data[11][i] = map1.get("involvedDeps").toString();
					data[12][i] = map1.get("checker").toString();
					data[13][i] = map1.get("startTime").toString();
					data[14][i] = map1.get("indexNo").toString();
					data[15][i] = map1.get("memo").toString();
				
					
			
			
				
			}
				
			for (int i = 0; i < k; i++) {
				object.addContentListByList(data[i]);
			}
			object.setRowName(rowName);
			ExcelOperate operate = new ExcelOperate();
			try {
				operate.exportExcel(object, plExcel.getObjectList().size(), true, request);
			} catch (IOException e) {
				e.printStackTrace();
			}
			

			filePath = filePath + fileTitle +".xls";
//			request.getSession().setAttribute("_File_Path", "");
			request.getSession().removeAttribute("_File_Path");
			request.getSession().setAttribute("_File_Path", filePath);
			//return mapping.findForward("exportExcel");
		}
		

		// 设置字符编码
		response.setContentType(CoreConstant.CONTENT_TYPE);
		response.getWriter().print(jsonObj);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return "redirect:/common/download.jsp?filepath=uploadfiles/submit/excel/contract.xls";
		
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
//					FlowInstanceInfor instance = (FlowInstanceInfor) this.flowInstanceManager.get(editId);
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
					FlowInstanceInfor instanceInfor = (FlowInstanceInfor) this.flowInstanceManager.get(instanceId);
					
					if (instanceId > 0) {
						instanceInfor = (FlowInstanceInfor) this.flowInstanceManager.get(Integer.valueOf(instanceId));

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
						this.flowInstanceManager.save(instanceInfor);
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
			FlowInstanceInfor instance = (FlowInstanceInfor)this.flowInstanceManager.get(instanceId);
			
			List users = this.flowInstanceManager.getViewUsers(instance);
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
	@RequestMapping(params="method=view_m")
	public void view_m(HttpServletRequest request, HttpServletResponse response) throws Exception {
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		int userId = systemUser.getPersonId().intValue();
		
		JSONObject jsonObj = new JSONObject();
		JSONConvert convert = new JSONConvert();
		
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		String instanceIdStr  = request.getParameter("instanceId");
		if(!instanceIdStr.equals("") && instanceIdStr.length() > 0){
			int instanceId = Integer.valueOf(instanceIdStr);
			FlowInstanceInfor instance = (FlowInstanceInfor)this.flowInstanceManager.get(instanceId);
			//将instance信息先放入
//			request.setAttribute("_Instance", instance);
			//instance转化为vo
			MFlowInstanceInforVo vo = new MFlowInstanceInforVo();
			BeanUtils.copyProperties(vo, instance);
			vo.setUpdateTimeStr(String.valueOf(instance.getUpdateTime()));
			vo.setStartTimeStr(String.valueOf(instance.getStartTime()));
			vo.setEndTimeStr(String.valueOf(instance.getEndTime()));
			vo.setApplierId(instance.getApplier().getPersonId().intValue());
			vo.setApplierName(instance.getApplier().getPerson().getPersonName());
			vo.setDepartmentId(instance.getDepartment().getOrganizeId().intValue());
			vo.setDepartmentName(instance.getDepartment().getOrganizeName());
			vo.setFlowId(instance.getFlowDefinition().getFlowId().intValue());
			vo.setFlowName(instance.getFlowDefinition().getFlowName());
			vo.setChargerId(instance.getCharger().getPersonId().intValue());
			vo.setChargerName(instance.getCharger().getPerson().getPersonName());
			if(instance.getManager() != null){
				vo.setManagerId(instance.getManager().getPersonId().intValue());
				vo.setManagerName(instance.getManager().getPerson().getPersonName());
			}
			if(instance.getCheckTime() != null){
				vo.setCheckTimeStr(sf.format(instance.getCheckTime()));
			}else {
				vo.setCheckTimeStr("");
			}
			
			if(instance.getViceManager() != null){
				vo.setViceManagerId(instance.getViceManager().getPersonId().intValue());
				vo.setViceManagerName(instance.getViceManager().getPerson().getPersonName());
//				vo.setViceCheckTimeStr(String.valueOf(instance.getViceCheckTime()));
				
				if(instance.getViceCheckTime() != null){
					vo.setViceCheckTimeStr(sf.format(instance.getViceCheckTime()));
				}else {
					vo.setViceCheckTimeStr("");
				}
			}
			
			
			//审核层
			List<MLayerInforVo> layerVos = new ArrayList<MLayerInforVo>();
			Set<InstanceLayerInfor> layerSet = instance.getLayers();
			for(InstanceLayerInfor layer : layerSet){
				MLayerInforVo layerVo = new MLayerInforVo();
				
				BeanUtils.copyProperties(layerVo, layer);
				layerVo.setStartTimeStr(String.valueOf(layer.getStartTime()));
				layerVo.setEndTimeStr(String.valueOf(layer.getEndTime()));
//				layerVo.setFlowNodeName(layer.getFlowNode().getNodeName());
				
				//审核意见
				List<MCheckInforVo> checkVos = new ArrayList<MCheckInforVo>();
				Set<InstanceCheckInfor> checkSet = layer.getCheckInfors();
				for(InstanceCheckInfor check : checkSet){
					MCheckInforVo checkVo = new MCheckInforVo();
					BeanUtils.copyProperties(checkVo, check);
					
					checkVo.setStartDateStr(String.valueOf(check.getStartDate()));
//					checkVo.setEndDateStr(String.valueOf(check.getEndDate()));
					
					if(check.getEndDate() != null){
						checkVo.setEndDateStr(sf.format(check.getEndDate()));
					}else {
						checkVo.setEndDateStr("");
					}
					
					checkVo.setCheckerId(check.getChecker().getPersonId().intValue());
					checkVo.setCheckerName(check.getChecker().getPerson().getPersonName());
					
					checkVos.add(checkVo);
				}
				
				layerVo.setMCheckInfors(checkVos);
				
				layerVos.add(layerVo);
			}
			
			vo.setMLayers(layerVos);
			

//			List instanceList = new ArrayList();
//			instanceList.add(instance);
//			JSONArray instanceJsonArray = new JSONArray();
//			instanceJsonArray = convert.modelCollect2JSONArray(instanceList, new ArrayList());
			jsonObj.put("_Instance", vo);
			
			//将审核环节放入
//			Set<InstanceLayerInfor> layers = instance.getLayers();
//			List layerList = new ArrayList();
//			layerList.addAll(layers);
//			JSONArray layerJsonArray = new JSONArray();
//			layerJsonArray = convert.modelCollect2JSONArray(layerList, new ArrayList());
//			jsonObj.put("_Layers", layerJsonArray);
			
			/*List checkList = new ArrayList();
			for(InstanceLayerInfor layer : layers){
				checkList.addAll(layer.getCheckInfors());
			}
			JSONArray checkJsonArray = new JSONArray();
			checkJsonArray = convert.modelCollect2JSONArray(checkList, new ArrayList());
			jsonObj.put("_Checks", checkJsonArray);*/
			
			
			FlowDefinition flow = instance.getFlowDefinition();
			//将汉字的流程名处理为拼音
			String flowName = CnToSpell.getFullSpell(flow.getFlowName());
			
			//得到所字段名
			List columnNames = this.commonManager.getColumnNames(flowName);
			
			//得到所有值 
			List formDatas = this.commonManager.getFormData(flowName, instanceId);
			
			//循环将各流程中相应的值放入json中
			Map<String,Object> map = new HashMap();
			if (formDatas != null && formDatas.size() > 0) {
				Object[] dataArray = (Object[])formDatas.get(0);
				int i=0;
				for (Iterator col=columnNames.iterator();col.hasNext();) {
					String colName = (String)col.next();
					map.put(colName, dataArray[i]);
					i++;
				}
			}
			
			//将关联的为ID的改为name（部门及用户）
			String[] templateArray = flow.getTemplate().split("/");
			String templateName = templateArray[templateArray.length-1];
			//1、部门
			List<String> dptNameList = this.commonManager.getDynamicFieldNames(templateName, "[@depSelect");
			for(String key : map.keySet()){
				for(String fieldName : dptNameList){
					if(fieldName.equals(key)){
						String dptIdStr = String.valueOf(map.get(key));
						if(dptIdStr != null && !dptIdStr.equals("") && !dptIdStr.equals("0")){
							OrganizeInfor dpt = (OrganizeInfor)this.organizeManager.get(Integer.valueOf(dptIdStr));
							map.put(key, dpt.getOrganizeName());
						}else if(dptIdStr.equals("0")){
							map.put(key, "");
						}
						
					}
				}
			}
			
			//1、人员
			List<String> personNameList = this.commonManager.getDynamicFieldNames(templateName, "[@usrSelect");
			for(String key : map.keySet()){
				for(String fieldName : personNameList){
					if(fieldName.equals(key)){
						String userIdStr = String.valueOf(map.get(key));
						if(userIdStr != null && !userIdStr.equals("") && !userIdStr.equals("0")){
							SystemUserInfor user = (SystemUserInfor)this.systemUserManager.get(Integer.valueOf(userIdStr));
							map.put(key, user.getPerson().getPersonName());
						}else if(userIdStr.equals("0")){
							map.put(key, "");
						}
						
					}
				}
			}
			
			
			//动态表单内容
			if (instance.getFlowDefinition().getFlowId().intValue() == 85){
				String a =(String)map.get("indexNo");
				String b =(String)map.get("documentNo");

				map.put("indexNo",b);
				map.put("documentNo",a);
			}
			JSONArray customJson = JSONArray.fromObject(map);
			jsonObj.put("_CustomDatas", customJson);
			
			//判断能否进行部门审核
			boolean canDepCheck = false;
			SystemUserInfor manager = instance.getManager();
			SystemUserInfor viceManager = instance.getViceManager();
			if (((manager != null && manager.getPersonId().intValue() == userId && !instance.isManagerChecked())||(viceManager != null && viceManager.getPersonId().intValue() == userId && !instance.isViceManagerChecked()))
					&&instance.getSubmiterWord()==null && instance.getStartTime() == null) {
				canDepCheck = true;
			}
			jsonObj.put("_CanDepCheck", canDepCheck);
			
			//判断是否有审核权限
			boolean canCheck = false;
			InstanceCheckInfor tmpCheck = this.flowInstanceManager.isChecker(instance, systemUser);
			if (tmpCheck != null) {
				// 能否审核
				if (tmpCheck.getLayerInfor().getStatus() != InstanceLayerInfor.Layer_Status_End
						&& tmpCheck.getLayerInfor().getStatus() != InstanceLayerInfor.Layer_Status_Suspended
						&& !instance.isSuspended() && tmpCheck.getEndDate() == null) {
					canCheck = true;
				}
			}
			jsonObj.put("_CanCheck", canCheck);
			
			
			/** 审核人审核时 获取该审核实例目前正在处理的审核层 */
			int checkId = 0;
			List rLayers = this.flowInstanceManager.getCurrentProcessLayers(instance);
			
			if (rLayers != null && rLayers.size() > 0) {
				for (Iterator it=rLayers.iterator();it.hasNext();) {
					InstanceLayerInfor layer = (InstanceLayerInfor)it.next();
					Set checks = layer.getCheckInfors();
					
					//判断是否存在该用户需要审核的信息
					boolean isChecker = false;
					for (Iterator itCheck=checks.iterator();itCheck.hasNext();) {
						InstanceCheckInfor check = (InstanceCheckInfor)itCheck.next();
						if(check.getStatus()==0){
							int ss=layer.getStatus();
							int checkerId = check.getChecker().getPersonId().intValue();
							if (userId == checkerId && ss !=3) {
								BeanUtils.copyProperties(vo, check);
								checkId = check.getCheckId();
								isChecker = true;
								break;
							}
						}
						if (isChecker) {
							break;
						}
					}
				}
			}
			
			jsonObj.put("_CheckId", checkId);
		}
		
		//设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);		
		
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
				FlowInstanceInfor instance = (FlowInstanceInfor)this.flowInstanceManager.get(instanceId);
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
				
				this.flowInstanceManager.save(instance);
				
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
				FlowInstanceInfor instance = (FlowInstanceInfor)this.flowInstanceManager.get(Integer.valueOf(instanceId));
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
			
			List<InstanceLayerInfor> layerList = this.flowLayerInforManager.getResultByQueryString(queryString);
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
	
}