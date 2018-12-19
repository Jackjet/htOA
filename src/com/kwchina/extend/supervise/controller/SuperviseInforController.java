package com.kwchina.extend.supervise.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.kwchina.core.base.entity.OrganizeInfor;
import com.kwchina.core.base.entity.PersonInfor;
import com.kwchina.core.base.entity.RoleInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.OrganizeManager;
import com.kwchina.core.base.service.RoleManager;
import com.kwchina.core.base.service.SystemUserManager;
import com.kwchina.core.common.page.PageList;
import com.kwchina.core.common.page.Pages;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.core.util.DateHelper;
import com.kwchina.core.util.ExcelObject;
import com.kwchina.core.util.ExcelOperate;
import com.kwchina.core.util.File;
import com.kwchina.core.util.json.JSONConvert;
import com.kwchina.core.util.string.StringUtil;
import com.kwchina.extend.loginLog.entity.AppModuleLog;
import com.kwchina.extend.loginLog.service.AppModuleLogManager;
import com.kwchina.extend.supervise.entity.SuperviseInfor;
import com.kwchina.extend.supervise.entity.SuperviseReport;
import com.kwchina.extend.supervise.entity.TaskCategory;
import com.kwchina.extend.supervise.entity.TaskCategoryRight;
import com.kwchina.extend.supervise.entity.TaskLeader;
import com.kwchina.extend.supervise.service.SuperviseInforManager;
import com.kwchina.extend.supervise.service.SuperviseReportManager;
import com.kwchina.extend.supervise.service.TaskCategoryManager;
import com.kwchina.extend.supervise.service.TaskCategoryRightManager;
import com.kwchina.extend.supervise.service.TaskLeaderManager;
import com.kwchina.extend.supervise.vo.MSuperviseInforVo;
import com.kwchina.extend.supervise.vo.MSuperviseReportVo;
import com.kwchina.extend.supervise.vo.SuperviseInforVo;
import com.kwchina.extend.supervise.vo.TaskLeaderVo;
import com.kwchina.oa.document.util.DocumentConstant;
import com.kwchina.oa.util.SysCommonMethod;

@Controller
@RequestMapping("/supervise/superviseInfor.do")
public class SuperviseInforController extends TaskBaseController {

	@Resource
	private TaskCategoryManager taskCategoryManager;

	@Resource
	private SystemUserManager systemUserManager;

	@Resource
	private TaskCategoryRightManager taskCategoryRightManager;

	@Resource
	private OrganizeManager organizeManager;
	
	@Resource
	private SuperviseInforManager superviseInforManager;

	@Resource
	private TaskLeaderManager taskLeaderManager;
	
	@Resource
	private SuperviseReportManager superviseReportManager;
	
	@Resource
	private RoleManager roleManager;
	
	@Resource
	private AppModuleLogManager appModuleLogManager;
	
	
	public JSONObject getInstances(HttpServletRequest request, HttpServletResponse response, boolean isExcel, boolean isMobile){
		JSONObject jsonObj = new JSONObject();
		
		SystemUserInfor userInfor = SysCommonMethod.getSystemUser(request);
		int personId = userInfor.getPersonId().intValue();
		
		boolean canSeeAll = false;
		boolean canSeeXz = false;     //可查看所有方针目录（原行政类）
		boolean canSeeDq = false;     //可查看所有党群
		
		boolean canSeeBmjs = false;   //可查看所有部门建设类
		boolean canSeeNk = false;     //可查看所有内控类
		
		Set<RoleInfor> roles = userInfor.getRoles();
		for(RoleInfor role : roles){
			if(role.getRoleName().contains("查看所有工作跟踪")){
				canSeeAll = true;
				//break;
			}
			
			if(role.getRoleName().contains("行政督办查看人员")){
				canSeeXz = true;
				//break;
			}
			
			if(role.getRoleName().contains("党群督办查看人员")){
				canSeeDq = true;
				//break;
			}
			
			if(role.getRoleName().contains("部门建设督办查看人员")){
				canSeeBmjs = true;
				//break;
			}
			
			if(role.getRoleName().contains("内控督办查看人员")){
				canSeeNk = true;
				//break;
			}
		}
		
		try {
			String ids = ""; // 所有子分类的Id
			String categoryId = request.getParameter("categoryId");
			String departmentId = request.getParameter("departmentId");
			
			int categoryType = 1;

			// 当点击某个分类时
			if (categoryId != null && categoryId.length() > 0 && !categoryId.equals("") && !categoryId.equals("0")) {
				TaskCategory category = (TaskCategory) this.taskCategoryManager.get(Integer.valueOf(categoryId));
				request.setAttribute("_Category", category);
				
				categoryType = category.getCategoryType();

				if (category.getChilds() != null && category.getChilds().size() > 0) {
					// 需要获取该分类所有的子分类Id
					ids = this.taskCategoryManager.getChildIds(Integer.valueOf(categoryId));
				}
			}
			
			//判断是否是部门总监
			RoleInfor managerRole = (RoleInfor)this.roleManager.get(CoreConstant.Role_Manager_Above);
			boolean isManager = this.roleManager.belongRole(userInfor, managerRole);

			// 构造查询语句
			String[] queryString = new String[2];
			queryString[0] = " from SuperviseInfor superviseInfor where isDeleted=0 ";
			queryString[1] = " select count(superviseInfor.taskId) from SuperviseInfor superviseInfor where isDeleted=0 ";
			String condition = "";
			
			/*else if(categoryType == 1 && canSeeXz){
			
			}else if(categoryType == 2 && canSeeDq){
				
			}else if(categoryType == 3 && canSeeBmjs){
				
			}else if(categoryType == 4 && canSeeNk){
				
			}*/
			
			if(userInfor.getUserType() != 1){
				//公司领导可看到全部  或者 行政、党群督办查看人员
				if(userInfor.getPerson().getDepartment().getOrganizeId() == 75 || canSeeAll){// || canSeeAll){
					
				}else if(isManager){
					condition += " and superviseInfor.organizeInfor.organizeId = " + userInfor.getPerson().getDepartment().getOrganizeId();
				}else {
					//新增条件：同一部门的“部门总监”角色中的人均可看到本部门的
					condition += " and ((superviseInfor.manager.personId="+personId+") or (superviseInfor.operator.personId="+personId+") " +
					"or (superviseInfor.leader.personId="+personId+") or (superviseInfor.creater.personId="+personId+")" +
					" or (superviseInfor.taskId in (select taskLeader.superviseInfor.taskId from TaskLeader taskLeader where taskLeader.leader.personId="+personId+")))";
				}
				
			}

			// 分类及其子分类
			if (ids.length() > 0) {
				condition += " and superviseInfor.taskCategory.categoryId in (" + ids + ")";
			} else if (categoryId != null && categoryId.length() > 0 && !categoryId.equals("0")) {
				condition += " and superviseInfor.taskCategory.categoryId = " + categoryId;
			}
			
			//部门
			if(departmentId != null && !departmentId.equals("") && !departmentId.equals("0")){
				condition += " and superviseInfor.organizeInfor.organizeId="+Integer.valueOf(departmentId);
			}

			/**
			 * 判断模块是否设置了权限: a.若设置了权限,则只显示用户有权限看的文档; b.若未设置权限,则显示该模块所有文档.
			 *//*
			SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
			int userId = systemUser.getPersonId().intValue();
			if (systemUser.getUserType() != SystemConstant._User_Type_Admin) {
				condition += " and (superviseInfor.creater.personId = " + userId + ")";
			}*/
			
			//queryString[0] += condition;

			// 用于获取该类中所有DocumentInfor的reportId
			//String getReportIdStr = queryString[0];

			String page = request.getParameter("page"); // 当前页
			String rowsNum = request.getParameter("rows"); // 每页显示的行数

			queryString[0] += condition;
			queryString[1] += condition;
			String[] searchParams = getSearchParams(request);
			if(searchParams!=null && searchParams.length>0){
				//jqgrid 排序问题
				if(searchParams[0].equals("organizeName")){
					searchParams[0] = "departmentId";
				}
				if(searchParams[0].equals("operatorName")){
					searchParams[0] = "operatorId";
				}
				if(searchParams[0].equals("categoryName")){
					searchParams[0] = "categoryId";
				}
				if(searchParams[0].equals("statusName")){
					searchParams[0] = "status";
				}
				if(searchParams[0].equals("createDateStr")){
					searchParams[0] = "createDate";
				}
				if(searchParams[0].equals("finishDateStr")){
					searchParams[0] = "finishDate";
				}
				if(searchParams[0].equals("endTimeStr")){
					searchParams[0] = "endTime";
				}
			}
			queryString = this.superviseInforManager.generateQueryString(queryString, searchParams);
			
			Pages pages = new Pages(request);
			pages.setPage(Integer.valueOf(page));
			pages.setPerPageNum(Integer.valueOf(rowsNum));

			PageList pl = this.superviseInforManager.getResultByQueryString(queryString[0], queryString[1], true, pages);
			List infors = pl.getObjectList();
//			System.out.println(infors.toString());
			
			//将结果转换为vo
			List<SuperviseInforVo> vos = new ArrayList<SuperviseInforVo>();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			
			for(Iterator it=infors.iterator();it.hasNext();){
				SuperviseInfor tmpInfor = (SuperviseInfor)it.next();
				SuperviseInforVo vo = new SuperviseInforVo();
//				String a = tmpInfor.getNameScore();
//				System.out.println(a);
				vo.setTaskId(tmpInfor.getTaskId());
				vo.setTaskName(tmpInfor.getTaskName());
				vo.setCategoryName(tmpInfor.getTaskCategory().getCategoryName());
				if(tmpInfor.getOperator() != null){
					vo.setOperatorName(tmpInfor.getOperator().getPerson().getPersonName());
				}else {
					vo.setOperatorName("");
				}
				vo.setCreateDateStr(sf.format(tmpInfor.getCreateDate()));
				/****pc端时才加入以下字段*****/
				if(!isMobile){

					if(tmpInfor.getFinishDate() != null){
						vo.setFinishDateStr(sf.format(tmpInfor.getFinishDate()));
					}
					if(tmpInfor.getEndTime() != null){
						vo.setEndTimeStr(sf.format(tmpInfor.getEndTime()));
					}
					vo.setOrganizeName(tmpInfor.getOrganizeInfor().getOrganizeName());
					
					
					vo.setCreaterName(tmpInfor.getCreater().getPerson().getPersonName());

					//vo.setCreateDepartment(tmpInfor.getCreater().getPerson().getDepartment().getOrganizeName());
					vo.setDutyDepartment(tmpInfor.getDutyDepartment() == null || tmpInfor.getDutyDepartment().equals("") ? tmpInfor.getCreater().getPerson().getDepartment().getOrganizeName() : tmpInfor.getDutyDepartment());
					vo.setReportPeriod(tmpInfor.getReportPeriod());
					vo.setWorkType(tmpInfor.getWorkType());
					vo.setContactPerson(tmpInfor.getContactPerson());
					
					//状态值 
					int status = tmpInfor.getStatus();
					String statusName = "";
					if(status == SuperviseInfor.Task_Status_SetCharger){
						statusName = "任务已下达，待指定责任人";
					}else if(status == SuperviseInfor.Task_Status_Processed){
						statusName = "工作进行中";
						if(new java.util.Date().after(tmpInfor.getFinishDate()) && new java.util.Date().getDate() != tmpInfor.getFinishDate().getDate()){
							statusName = "延迟工作进行中";
						}
					}else if(status == SuperviseInfor.Task_Status_ToFinish){
						if(tmpInfor.getDelayDate() != null && !tmpInfor.getDelayDate().equals("") && !tmpInfor.getDelayDate().equals("undefined")){
							statusName = "延迟工作进行中";
						}else if(tmpInfor.getDelayDate() == null && tmpInfor.getDelayDate2() == null && tmpInfor.getDelayDate3() == null){
							statusName = "已提交完成报告，部门审核中";
						}
					}else if(status == SuperviseInfor.Task_Status_Check){
						statusName = "行政助理审核中";
						
						//其下所有报告
						Set<SuperviseReport> reports = tmpInfor.getReports();
						List<SuperviseReport> reportList = new ArrayList<SuperviseReport>(reports);
						
						//先判断是否已有行政助理意见
						boolean notPass = false;
						boolean isDelay = false;
						for(SuperviseReport report : reportList){
							if(report.getReportType() == SuperviseReport.Report_Type_Done && report.getJudgeDate() != null && !report.getJudgeDate().toString().equals("") && report.getIsDeleted() == 0){
								//有预判，但未通过时
								if((report.getParent() == null && report.getChilds().size() == 0 && report.getIsJudged() == 1 && report.getIsJudgePassed() == 0)
										|| (report.getParent() != null && report.getIsJudged() == 1 && report.getIsJudgePassed() == 0)){
									notPass = true;
									if(report.getJudgeDate().after(tmpInfor.getFinishDate()) && report.getJudgeDate().getDate() != tmpInfor.getFinishDate().getDate()){
										isDelay = true;
									}
								}
							}
						}
						
						if(notPass){
							statusName = "工作进行中";
							if(isDelay){
								statusName = "延迟工作进行中";
							}
						}
						
					}else if(status == SuperviseInfor.Task_Status_LeaderCheck){
						statusName = "领导审核中";
					}else if(status == SuperviseInfor.Task_Status_Delay){
						if(tmpInfor.getDelayDate() == null || tmpInfor.getDelayDate().equals("") || tmpInfor.getDelayDate().equals("undefined")){
							statusName = "延迟时间设定中";
						}else{
							statusName = "延迟工作进行中";
						}
					}else if(status == SuperviseInfor.Task_Status_Done){
						if(tmpInfor.getEndTime().before(tmpInfor.getFinishDate()) || tmpInfor.getEndTime().equals(tmpInfor.getFinishDate())){
							statusName = "按时完成";
						}
//						System.out.println(tmpInfor.getEndTime());
//						System.out.println(tmpInfor.getFinishDate());
//						System.out.println(tmpInfor.getEndTime().getDate());
//						System.out.println(tmpInfor.getEndTime());
//						System.out.println(tmpInfor.getFinishDate().getDate());
//						System.out.println(tmpInfor.getFinishDate());
//						System.out.println(tmpInfor.getEndTime().after(tmpInfor.getFinishDate()));
//						System.out.println(tmpInfor.getEndTime().getDate() != tmpInfor.getFinishDate().getDate());
//						System.out.println(tmpInfor.getEndTime() != tmpInfor.getFinishDate());

						if(tmpInfor.getEndTime().after(tmpInfor.getFinishDate())) {
//							&& tmpInfor.getEndTime().getDate() != tmpInfor.getFinishDate().getDate()
							int taskId = tmpInfor.getTaskId();
							java.util.Date lastTime = new java.util.Date();
							if (taskId > 0) {
								String hql = "from SuperviseReport report where report.superviseInfor.taskId="+taskId+" and (report.reportType=2 or report.reportType=3) order by checkDate desc";
								List<SuperviseReport> reports = this.superviseReportManager.getResultByQueryString(hql);
								SuperviseReport report = reports.get(0);
								lastTime = report.getCheckDate();
							}

							//System.out.println(tmpInfor.getTaskId());
							
							lastTime = DateHelper.getDate(sf.format(lastTime));
							
							if(lastTime.before(tmpInfor.getFinishDate()) || lastTime.equals(tmpInfor.getFinishDate())){
								statusName = "按时完成";
							}
							if(lastTime.after(tmpInfor.getFinishDate())){
								if(isMobile){
									statusName = "延迟完成"; 
								}else {
									statusName = "<font color=red>延迟完成</font>";
								}
								
							}
						}
					}
//					System.out.println("完成状态：：：：");
//					System.out.println(statusName);
//					if(isMobile){
//						vo.setStatusName(statusName);
//					}else {
						vo.setStatusName(statusName);
//					}
					vo.setNameScore(tmpInfor.getNameScore());
//					System.out.println(tmpInfor.getNameScore());
//				vo.setNameScore("werwe");
				}



				vos.add(vo);
			}

			// 定义返回的数据类型：json，使用了json-lib
//			JSONObject jsonObj = new JSONObject();

			// 定义rows，存放数据
			JSONArray rows = new JSONArray();
			jsonObj.put("page", pl.getPages().getCurrPage()); // 当前页(名称必须为page)
			jsonObj.put("total", pl.getPages().getTotalPage()); // 总页数(名称必须为total)
			jsonObj.put("records", pl.getPages().getTotals()); // 总记录数(名称必须为records)

			JSONConvert convert = new JSONConvert();
			// 通知Convert，哪些关联对象需要获取
			List awareObject = new ArrayList();
			awareObject.add("taskCategory");
			awareObject.add("creater.person");
			awareObject.add("leader.person");
			awareObject.add("organizeInfor");
			awareObject.add("manager.person");
			awareObject.add("operator.person");
			rows = convert.modelCollect2JSONArray(vos, awareObject);
			jsonObj.put("rows", rows); // 返回到前台每页显示的数据(名称必须为rows)

//			// 全部分类树状显示(需要判断分类的权限)
//			ArrayList returnArray = this.taskCategoryManager.getCategoryAsTree(DocumentConstant._Root_Category_Id);
//			request.setAttribute("_TREE", returnArray);
//
//			// 所属部门
//			List departments = this.organizeManager.getDepartments();
//			request.setAttribute("_Departments", departments);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonObj;
	}

	/**
	 * 获取任务督办信息
	 * 
	 * @param inforPath
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=getTaskInfor")
	public void getTaskInfor(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		JSONObject jsonObj = getInstances(request, response, false, false);
		
		
		// 设置字符编码
		response.setContentType(CoreConstant.CONTENT_TYPE);
		response.getWriter().print(jsonObj);
	}
	
	/**
	 * 移动端获取任务督办信息
	 * 
	 * @param inforPath
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=getTaskInfor_m")
	public void getTaskInfor_m(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		if(StringUtil.isNotEmpty(SysCommonMethod.getPlatform(request))){
			SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
			/************记录app模块使用日志************/ 
			AppModuleLog appModuleLog = new AppModuleLog();
			appModuleLog.setModuleName("工作跟踪列表");
			appModuleLog.setPlatform(SysCommonMethod.getPlatform(request));
			appModuleLog.setLogTime(new Timestamp(System.currentTimeMillis()));
			appModuleLog.setUserName(systemUser.getUserName());
			this.appModuleLogManager.save(appModuleLog);
			/*****************************************/
		}
		
		JSONObject jsonObj = getInstances(request, response, false, true);
		
		
		// 设置字符编码
		response.setContentType(CoreConstant.CONTENT_TYPE);
		response.getWriter().print(jsonObj);
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
	public String expertExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		SystemUserInfor userInfor = SysCommonMethod.getSystemUser(request);
		int personId = userInfor.getPersonId().intValue();
		try {
			String ids = ""; // 所有子分类的Id
			String categoryId = request.getParameter("categoryId");
			String departmentId = request.getParameter("departmentId");


			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			
			// 当点击某个分类时
			if (categoryId != null && categoryId.length() > 0 && !categoryId.equals("") && !categoryId.equals("0")) {
				TaskCategory category = (TaskCategory) this.taskCategoryManager.get(Integer.valueOf(categoryId));
				request.setAttribute("_Category", category);

				if (category.getChilds() != null && category.getChilds().size() > 0) {
					// 需要获取该分类所有的子分类Id
					ids = this.taskCategoryManager.getChildIds(Integer.valueOf(categoryId));
				}
			}

			// 构造查询语句
			String[] queryString = new String[2];
			queryString[0] = " from SuperviseInfor superviseInfor where isDeleted=0 ";
			queryString[1] = " select count(superviseInfor.taskId) from SuperviseInfor superviseInfor where isDeleted=0 ";
			String condition = "";
			
			if(userInfor.getUserType() != 1){
				//公司领导可看到全部
				if(userInfor.getPerson().getDepartment().getOrganizeId() == 75){
					
				}else {
					condition += " and ((superviseInfor.manager.personId="+personId+") or (superviseInfor.operator.personId="+personId+") " +
					"or (superviseInfor.leader.personId="+personId+") or (superviseInfor.creater.personId="+personId+")" +
					" or (superviseInfor.taskId in (select taskLeader.superviseInfor.taskId from TaskLeader taskLeader where taskLeader.leader.personId="+personId+")))";
				}
				
			}

			// 分类及其子分类
			if (ids.length() > 0) {
				condition += " and superviseInfor.taskCategory.categoryId in (" + ids + ")";
			} else if (categoryId != null && categoryId.length() > 0 && !categoryId.equals("0")) {
				condition += " and superviseInfor.taskCategory.categoryId = " + categoryId;
			}
			
			//部门
			if(departmentId != null && !departmentId.equals("") && !departmentId.equals("0")){
				condition += " and superviseInfor.organizeInfor.organizeId="+Integer.valueOf(departmentId);
			}

			/**
			 * 判断模块是否设置了权限: a.若设置了权限,则只显示用户有权限看的文档; b.若未设置权限,则显示该模块所有文档.
			 *//*
			SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
			int userId = systemUser.getPersonId().intValue();
			if (systemUser.getUserType() != SystemConstant._User_Type_Admin) {
				condition += " and (superviseInfor.creater.personId = " + userId + ")";
			}*/
			
			//queryString[0] += condition;

			// 用于获取该类中所有DocumentInfor的reportId
			//String getReportIdStr = queryString[0];

			String page = request.getParameter("page"); // 当前页
			String rowsNum = request.getParameter("rows"); // 每页显示的行数

			queryString[0] += condition;
			queryString[1] += condition;
			
			queryString = this.superviseInforManager.generateQueryString(queryString, getSearchParams(request));
			
			Pages pages = new Pages(request);
			pages.setPage(Integer.valueOf(page));
			pages.setPerPageNum(Integer.valueOf(rowsNum));

			PageList pl = this.superviseInforManager.getResultByQueryString(queryString[0], queryString[1], true, pages);
			List infors = pl.getObjectList();
			
			
			/******************导出Excel********************/
			long time = System.currentTimeMillis();
//			String filePath = SystemConstant.Submit_Path + time + "/";
			String filePath = "/"+CoreConstant.Attachment_Path + "supervise/";
			String fileTitle = "工作跟踪";

			ExcelObject object = new ExcelObject();
			object.setFilePath(filePath);
			object.setFileName(fileTitle);
			object.setTitle(fileTitle);

			List rowName = new ArrayList();
			String[][] data = new String[13][infors.size()];
			int k = 0;// 列数

			rowName.add("序号");
			rowName.add("分类");
			rowName.add("工作类别");
			rowName.add("工作内容和要求");
			rowName.add("下达时间");
			rowName.add("汇报周期");
			rowName.add("计划完成时间");
			rowName.add("实际完成时间");
			rowName.add("执行部门");
			rowName.add("执行人");
			rowName.add("下达部门");
			rowName.add("下达人");
			rowName.add("状态");
			k = 13;

			for (int i = 0; i < infors.size(); i++) {
				SuperviseInfor superviseInfor = (SuperviseInfor)infors.get(i);

				data[0][i] = String.valueOf(i + 1);
				data[1][i] = superviseInfor.getTaskCategory().getCategoryName();
				data[2][i] = superviseInfor.getWorkType();
				data[3][i] = superviseInfor.getTaskName();
				data[4][i] = DateHelper.getString(superviseInfor.getCreateDate());
				data[5][i] = superviseInfor.getReportPeriod();
				if(superviseInfor.getFinishDate() != null){
					data[6][i] = DateHelper.getString(superviseInfor.getFinishDate());
				}else {
					data[6][i] = "";
				}
				
				if(superviseInfor.getEndTime() != null){
					data[7][i] = DateHelper.getString(superviseInfor.getEndTime());
				}else {
					data[7][i] = "";
				}
				
				data[8][i] = superviseInfor.getOrganizeInfor().getOrganizeName();
				
				if(superviseInfor.getOperator() != null){
					data[9][i] = superviseInfor.getOperator().getPerson().getPersonName(); 
				}else {
					data[9][i] = "";
				}
				
				
				data[10][i] = superviseInfor.getDutyDepartment();
				data[11][i] = superviseInfor.getContactPerson();
				
				
				
				//状态值 
				int status = superviseInfor.getStatus();
				String statusName = "";
				if(status == SuperviseInfor.Task_Status_SetCharger){
					statusName = "任务已下达，待指定责任人";
				}else if(status == SuperviseInfor.Task_Status_Processed){
					statusName = "工作进行中";
					if(new java.util.Date().after(superviseInfor.getFinishDate()) && new java.util.Date().getDate() != superviseInfor.getFinishDate().getDate()){
						statusName = "延迟工作进行中";
					}
				}else if(status == SuperviseInfor.Task_Status_ToFinish){
					if(superviseInfor.getDelayDate() != null && !superviseInfor.getDelayDate().equals("") && !superviseInfor.getDelayDate().equals("undefined")){
						statusName = "延迟工作进行中";
					}else if(superviseInfor.getDelayDate() == null && superviseInfor.getDelayDate2() == null && superviseInfor.getDelayDate3() == null){
						statusName = "已提交完成报告，部门审核中";
					}
				}else if(status == SuperviseInfor.Task_Status_Check){
					statusName = "行政助理审核中";
					
					//其下所有报告
					Set<SuperviseReport> reports = superviseInfor.getReports();
					List<SuperviseReport> reportList = new ArrayList<SuperviseReport>(reports);
					
					//先判断是否已有行政助理意见
					boolean notPass = false;
					boolean isDelay = false;
					for(SuperviseReport report : reportList){
						if(report.getReportType() == SuperviseReport.Report_Type_Done && report.getJudgeDate() != null && !report.getJudgeDate().toString().equals("") && report.getIsDeleted() == 0){
							//有预判，但未通过时
							if((report.getParent() == null && report.getChilds().size() == 0 && report.getIsJudged() == 1 && report.getIsJudgePassed() == 0)
									|| (report.getParent() != null && report.getIsJudged() == 1 && report.getIsJudgePassed() == 0)){
								notPass = true;
								if(report.getJudgeDate().after(superviseInfor.getFinishDate()) && report.getJudgeDate().getDate() != superviseInfor.getFinishDate().getDate()){
									isDelay = true;
								}
							}
						}
					}
					
					if(notPass){
						statusName = "工作进行中";
						if(isDelay){
							statusName = "延迟工作进行中";
						}
					}
					
				}else if(status == SuperviseInfor.Task_Status_LeaderCheck){
					statusName = "领导审核中";
				}else if(status == SuperviseInfor.Task_Status_Delay){
					if(superviseInfor.getDelayDate() == null || superviseInfor.getDelayDate().equals("") || superviseInfor.getDelayDate().equals("undefined")){
						statusName = "延迟时间设定中";
					}else{
						statusName = "延迟工作进行中";
					}
				}else if(status == SuperviseInfor.Task_Status_Done){
					if(superviseInfor.getEndTime().before(superviseInfor.getFinishDate()) || superviseInfor.getEndTime().equals(superviseInfor.getFinishDate())){
						statusName = "按时完成";
					}
					if(superviseInfor.getEndTime().after(superviseInfor.getFinishDate()) && superviseInfor.getEndTime().getDate() != superviseInfor.getFinishDate().getDate()){
						int taskId = superviseInfor.getTaskId();
						java.util.Date lastTime = new java.util.Date();
						if (taskId > 0) {
							String hql = "from SuperviseReport report where report.superviseInfor.taskId="+taskId+" and (report.reportType=2 or report.reportType=3) order by checkDate desc";
							List<SuperviseReport> reports = this.superviseReportManager.getResultByQueryString(hql);
							SuperviseReport report = reports.get(0);
							lastTime = report.getCheckDate();
						}

						lastTime = DateHelper.getDate(sf.format(lastTime));
						
						if(lastTime.before(superviseInfor.getFinishDate()) || lastTime.equals(superviseInfor.getFinishDate())){
							statusName = "按时完成";
						}
						if(lastTime.after(superviseInfor.getFinishDate()) && lastTime.getDate() != superviseInfor.getFinishDate().getDate()){
							statusName = "延迟完成"; 
						}
					}
				}
				
				data[12][i] = statusName;
				
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
			request.getSession().removeAttribute("_File_Path");
			request.getSession().setAttribute("_File_Path", filePath);
			
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "/common/download";
	}
	
	
	/**
	 * 取最后一次提交完成报告的时间，用于判断是否为延迟完成
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params="method=getLastReportTime")
	public void getLastReportTime(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		JSONObject jsonObj = new JSONObject();
		
		//部门名称
		String taskId = request.getParameter("taskId");
		String lastTime = "";
		if (taskId != null && taskId.length() > 0) {
//			SuperviseInfor superviseInfor = (SuperviseInfor)this.superviseInforManager.get(Integer.valueOf(taskId));
			String hql = "from SuperviseReport report where report.superviseInfor.taskId="+taskId+" and (report.reportType=2 or report.reportType=3) order by checkDate desc";
			List<SuperviseReport> reports = this.superviseReportManager.getResultByQueryString(hql);
			SuperviseReport report = reports.get(0);
			lastTime = DateHelper.getString(report.getCheckDate());
		}
		jsonObj.put("_LastTime", lastTime);
		
		//设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);		
	}
	
	/**
	 * 编辑任务信息
	 * 
	 * @param meetInforVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=edit")
	public String edit(@ModelAttribute("superviseInforVo")
	SuperviseInforVo superviseInforVo, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		SuperviseInfor superviseInfor = new SuperviseInfor();
		Integer taskId = superviseInforVo.getTaskId();


		boolean isParty = false;
		
		// 获取全部用户
//		List users = this.systemUserManager.getAll();
		List users = new ArrayList();

		// 根据职级获取用户
		List persons = this.systemUserManager.getUserByPosition(PersonInfor._Position_Tag);
		request.setAttribute("_Persons", persons);

		// 获取职级大于一定值的用户
		List otherPersons = this.systemUserManager.getOtherPositionUser(PersonInfor._Position_Tag);
		request.setAttribute("_OtherPersons", otherPersons);
		
		//类别
		String categoryIdStr = request.getParameter("categoryId");
		if(categoryIdStr != null && !categoryIdStr.equals("")){
			TaskCategory category = (TaskCategory)this.taskCategoryManager.get(Integer.valueOf(categoryIdStr));
			request.setAttribute("_Category", category);
		}
//		//部门
		String departmentIdStr = request.getParameter("departmentId");
		
		TaskCategory categoryEdit = (TaskCategory)this.taskCategoryManager.get(Integer.valueOf(Integer.valueOf(categoryIdStr)));
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		
		if(categoryEdit != null && categoryEdit.getCategoryId().intValue() > 0){
			if(categoryEdit.getCategoryType() == 2){
				isParty = true;
			}
		}
		
		//下发部门
		int dutyDepartId = systemUser.getPerson().getDepartment().getOrganizeId().intValue();
		
		// 全部部门信息
		List departments = this.organizeManager.getDepartments();//去掉特殊部门
		List<String> validDeparts = new ArrayList<String>();
//					validDeparts.add("滚装业务部");
//					validDeparts.add("计划财务部");
//					validDeparts.add("技术规划部");
//					validDeparts.add("人力资源部");
//					validDeparts.add("信息技术部");
//					//validDeparts.add("整车服务中心");
//					//validDeparts.add("整车物流部");
//					validDeparts.add("总经理办公室");
//					validDeparts.add("整车服务部");
//					validDeparts.add("码头运营部");
//					validDeparts.add("市场发展部");
//					validDeparts.add("党群工作部");
//					validDeparts.add("安全质量部");
//					validDeparts.add("零部件物流部");
		
		validDeparts.add("总经理办公室");
		validDeparts.add("党群工作部");
		validDeparts.add("人力资源部");
		validDeparts.add("计划财务部");
		validDeparts.add("技术规划部");
		validDeparts.add("安全质量部");
		validDeparts.add("信息技术部");
		validDeparts.add("市场营销部");
		validDeparts.add("码头运营部");
		validDeparts.add("滚装业务部");
		//validDeparts.add("整车服务中心");
		//validDeparts.add("整车物流部");
		validDeparts.add("整车物流部");
		validDeparts.add("零部件物流部");
		validDeparts.add("采购部");
		validDeparts.add("数据业务部");
		
		if(isParty){
			validDeparts.add("洋山码头");
			validDeparts.add("太仓码头");
		}

		//下达部门
		List xdDeparts = new ArrayList();
		//执行部门
		List zxDeparts = new ArrayList();
		
		
		//普通人员添加修改时，只取登录人所在部门，管理员取全部
		if(systemUser.getUserType() == SystemUserInfor._Type_Admin){
			for(String depart : validDeparts){
				for(int i=0;i<departments.size();i++){
					OrganizeInfor department = (OrganizeInfor)departments.get(i);
					if(department.getOrganizeName().equals(depart)){
						xdDeparts.add(department);
						zxDeparts.add(department);
					}
				}
			}
		}else {
			xdDeparts.add(systemUser.getPerson().getDepartment());
			
			
			for(String depart : validDeparts){
				for(int i=0;i<departments.size();i++){
					OrganizeInfor department = (OrganizeInfor)departments.get(i);
					if(department.getOrganizeName().equals(depart)){
						zxDeparts.add(department);
					}
				}
			}
		}
		
//					request.setAttribute("_Departs", actualDeparts);
		model.addAttribute("_Departments", xdDeparts);
		model.addAttribute("_Departments_ZX", zxDeparts);
		
		
		
		//判断该用户在此分类下是否具有编辑权限
		boolean hasRight = this.taskCategoryRightManager.hasRight(categoryEdit, systemUser, TaskCategoryRight._Right_Edit);
		String redirectStr = "";
		if(hasRight){
			if (taskId != null && taskId.intValue() != 0) {
				// 修改的情况
				superviseInfor = (SuperviseInfor) this.superviseInforManager.get(taskId);
				request.setAttribute("_SuperviseInfor", superviseInfor);
				
				List thisOrgUsers = this.systemUserManager.getUserByOrganize(superviseInfor.getOrganizeInfor().getOrganizeId(), null);
				model.addAttribute("_ThisUsers", thisOrgUsers);

				try {
					BeanUtils.copyProperties(superviseInforVo, superviseInfor);
					
					if (superviseInfor.getTaskCategory() != null) {
						superviseInforVo.setTaskCategoryId(superviseInfor.getTaskCategory().getCategoryId());
					}
					if (superviseInfor.getCreater() != null) {
						superviseInforVo.setCreaterId(superviseInfor.getCreater().getPersonId());
					}
					if (superviseInfor.getLeader() != null) {
						superviseInforVo.setLeaderId(superviseInfor.getLeader().getPersonId());
					}
					if (superviseInfor.getOrganizeInfor() != null) {
						superviseInforVo.setDepartmentId(superviseInfor.getOrganizeInfor().getOrganizeId());
					}
					if (superviseInfor.getManager() != null) {
						superviseInforVo.setManagerId(superviseInfor.getManager().getPersonId());
					}
					if (superviseInfor.getOperator() != null) {
						superviseInforVo.setOperatorId(superviseInfor.getOperator().getPersonId());
					}
					superviseInforVo.setAttachmentStr(superviseInfor.getAttachment());
					superviseInforVo.setFinishDateStr(superviseInfor.getFinishDate().toString());
					
//					users = this.systemUserManager.getUserByOrganize(superviseInfor.getOrganizeInfor().getOrganizeId(), null);
					/*//部门经理
					OrganizeInfor department = superviseInfor.getOrganizeInfor();
					PersonInfor director = department.getDirector();
					//如果部门经理为空，则再查出所有
					if(director == null){
						users = this.systemUserManager.getUserByOrganize(superviseInfor.getOrganizeInfor().getOrganizeId(), null);
					}else {
						users.add(director.getUser());
					}*/
					
					
					
					//修改时，查出下发部门
					String dutyDepartName = superviseInfor.getDutyDepartment();
					OrganizeInfor dutyDepart = this.organizeManager.findByOrganizeName(dutyDepartName);
					if(dutyDepart != null && dutyDepart.getOrganizeId() != null && dutyDepart.getOrganizeId().intValue() > 0){
						dutyDepartId = dutyDepart.getOrganizeId().intValue();
					}
					
					
					
					/********************部门负责人改为取“部门总监助理及以上人员”角色中的，并按部门过滤********************/
					//修改时，把选中的部门中，属于此角色的人员添加
					RoleInfor managerRole = (RoleInfor)this.roleManager.get(CoreConstant.Role_Manager_Above);
					if(managerRole != null && managerRole.getRoleId().intValue() > 0){
						Set<SystemUserInfor> userSet = managerRole.getUsers();
						if(userSet != null && userSet.size() > 0){
							for(SystemUserInfor tmpUser : userSet){
								if(tmpUser.getPerson().getDepartment().getOrganizeId().intValue() == superviseInfor.getOrganizeInfor().getOrganizeId().intValue()){
									users.add(tmpUser);
								}
							}
						}
						
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				
			}else {
				//新增时，把categoryId代入
				superviseInforVo.setTaskCategoryId(Integer.valueOf(categoryIdStr));
				
				//把下发人部门及下发人代入
				superviseInforVo.setDutyDepartment(systemUser.getPerson().getDepartment().getOrganizeName());
				superviseInforVo.setContactPerson(systemUser.getPerson().getPersonName());
				
				//根据传入的departmentId先找到该部门下的人员
				/*//部门经理
				OrganizeInfor department = (OrganizeInfor)this.organizeManager.get(Integer.valueOf(departmentIdStr));
				PersonInfor director = department.getDirector();
				//如果部门经理为空，则再查出所有
				if(director == null){
					users = this.systemUserManager.getUserByOrganize(Integer.valueOf(departmentIdStr), null);
				}else {
					users.add(director.getUser());
				}*/
				
				/********************部门负责人改为取“部门总监助理及以上人员”角色中的，并按部门过滤********************/
				
				//新增时，把选中的部门中（第一个部门)，属于此角色的人员添加-----------单独新增
				RoleInfor managerRole = (RoleInfor)this.roleManager.get(CoreConstant.Role_Manager_Above);
				
				if(superviseInforVo.getDepartmentId() != null && superviseInforVo.getDepartmentId().intValue() > 0){
					if(managerRole != null && managerRole.getRoleId().intValue() > 0){
						Set<SystemUserInfor> userSet = managerRole.getUsers();
						if(userSet != null && userSet.size() > 0){
							for(SystemUserInfor tmpUser : userSet){
								if(tmpUser.getPerson().getDepartment().getOrganizeId().intValue() == superviseInforVo.getDepartmentId()){
									users.add(tmpUser);
								}
							}
						}
						
					}
				}else {
					//新增时，把选中的部门中（第一个部门)，属于此角色的人员添加-----------批量新增
					if(managerRole != null && managerRole.getRoleId().intValue() > 0){
						Set<SystemUserInfor> userSet = managerRole.getUsers();
						if(userSet != null && userSet.size() > 0){
							for(SystemUserInfor tmpUser : userSet){
								if(tmpUser.getPerson().getDepartment().getOrganizeId().intValue() == ((OrganizeInfor)zxDeparts.get(0)).getOrganizeId().intValue()){
									users.add(tmpUser);
								}
							}
						}
						
					}
					
					model.addAttribute("_Batch", "1");
				}
				
				
			}

			
			model.addAttribute("_Users", users);
			
			//下发部门
			List dutyUserList = this.systemUserManager.getUserByOrganize(dutyDepartId, 0);
			model.addAttribute("_DutyUsers",dutyUserList);

			

			String attachmentFile = superviseInfor.getAttachment();
			if (attachmentFile != null && (!attachmentFile.equals(""))) {
				String[] arrayFiles = attachmentFile.split("\\|");
				superviseInforVo.setAttatchmentArray(arrayFiles);

				String[] attachmentNames = new String[arrayFiles.length];
				for (int k = 0; k < arrayFiles.length; k++) {
					attachmentNames[k] = File.getFileName(arrayFiles[k]);
				}
				request.setAttribute("_Attachment_Names", attachmentNames);
			}
			request.setAttribute("_FinishDate", superviseInfor.getFinishDate());
			
			redirectStr = "editTask";
		}else{
			redirectStr = "/common/error";
			request.setAttribute("_ErrorMessage", "对不起,您无权进行该操作,请与系统管理员联系!");
		}
		
		
		return redirectStr;
		
	}
	
	/**
	 * 保存督办内容
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=save")
	public String save(@ModelAttribute("superviseInforVo")
		SuperviseInforVo superviseInforVo, Model model, HttpServletRequest request, HttpServletResponse response, DefaultMultipartHttpServletRequest multipartRequest) throws Exception {

		try {

			SystemUserInfor user = SysCommonMethod.getSystemUser(request);
			
			/********判断是否是批量添加的********/
			boolean isBatch = false;
			String batchTag = request.getParameter("batch");
			if(batchTag != null && !batchTag.equals("")){
				if(batchTag.equals("1")){
					isBatch = true;
				}
			}
			
			
			if(isBatch){
				int[] departmentIds = superviseInforVo.getDepartmentIds();
				int[] managerIds = superviseInforVo.getManagerIds();

				//批量时，先保存单一的，再按选择的执行部门数量循环保存
				try {
					saveTask(user,superviseInforVo,request,multipartRequest);
				} catch (Exception e) {
					e.printStackTrace();
					model.addAttribute("_ErrorMessage", "添加或修改该信息发生错误！<br>错误信息如下:<br>" + e.toString());
					return "/common/error";
				}
				
				
				for(int i=0;i<departmentIds.length;i++){
					SuperviseInforVo tmpVo = new SuperviseInforVo();
					
					BeanUtils.copyProperties(tmpVo, superviseInforVo);
					tmpVo.setDepartmentId(departmentIds[i]);
					tmpVo.setManagerId(managerIds[i]);
					
					try {
						saveTask(user,tmpVo,request,multipartRequest);
					} catch (Exception e) {
						e.printStackTrace();
						
						model.addAttribute("_ErrorMessage", "添加或修改该信息发生错误！<br>错误信息如下:<br>" + e.toString());
						return "/common/error";
					}
				}
				
				//System.out.println("=====");
			}else {
				try {
					saveTask(user,superviseInforVo,request,multipartRequest);
				} catch (Exception e) {
					e.printStackTrace();
					
					model.addAttribute("_ErrorMessage", "添加或修改该信息发生错误！<br>错误信息如下:<br>" + e.toString());
					return "/common/error";
				}
			}
			

			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>");
			// out.print("window.returnValue = 'Y';");
			out.print("window.opener.location.reload();");
			out.print("window.close();");
			out.print("</script>");
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	
	public void saveTask (SystemUserInfor user,SuperviseInforVo superviseInforVo, HttpServletRequest request, DefaultMultipartHttpServletRequest multipartRequest){
		SuperviseInfor superviseInfor = new SuperviseInfor();
		Integer taskId = superviseInforVo.getTaskId();
		Integer categoryId = superviseInforVo.getTaskCategoryId();
		Integer leaderId = superviseInforVo.getLeaderId();
		Integer departmentId = superviseInforVo.getDepartmentId();
		Integer managerId = superviseInforVo.getManagerId();
		//int operatorId = superviseInforVo.getOperatorId();
		
		String oldFiles = "";
		
		//是否新增
		boolean isNew = false;
		if (taskId != null && taskId.intValue() > 0) {
			//修改时
			superviseInfor = (SuperviseInfor) superviseInforManager.get(taskId);
			//判断是否修改了计划完成时间
			Date finishDate = superviseInfor.getFinishDate();
			Date voFinishdate = Date.valueOf(superviseInforVo.getFinishDateStr().toString());
			if(finishDate.getTime()<voFinishdate.getTime()){
				//如果延后了计划完成时间  1.修改 superviseInfor.Task_Status_ToFinish;已提交完成报告，部门审核中---->superviseInfor.Task_Status_Processed;已指定，工作进行中
				//2.把完成报告转换成进度报告
				superviseInforVo.setStatus(superviseInfor.Task_Status_Processed);
				superviseInfor.setStatus(superviseInfor.Task_Status_Processed);

				Set<SuperviseReport> reports = superviseInfor.getReports();
				for(SuperviseReport report:reports){
					if(report.getReportType()==2){
						report.setReportType(1);
						superviseReportManager.save(report);
						break;
					}
				}


			}

//			superviseInfor = (SuperviseInfor) this.superviseInforManager.get(taskId);
			// 修改信息时,对附件进行修改
			String filePaths = superviseInfor.getAttachment();
			oldFiles = deleteOldFile(request, filePaths, "attatchmentArray");
			
			//修改时，1-修改督办内容（未改部门）时，提示部门负责人内容已改变，2-修改责任部门时，提示原部门负责人任务已修改，将原责任人清空
			if(superviseInfor.getOrganizeInfor().getOrganizeId().intValue() == superviseInforVo.getDepartmentId().intValue()){
				superviseInfor.setRemindStatus(1);
			}
			
			if(superviseInfor.getOrganizeInfor().getOrganizeId().intValue() != superviseInforVo.getDepartmentId().intValue()){
				superviseInfor.setRemindStatus(2);
				superviseInfor.setFormerManager(superviseInfor.getManager());
				superviseInfor.setOperator(null);
			}
		} else { 
			isNew = true;
			superviseInfor.setCreater(user);
			superviseInfor.setCreateDate(new Date(System.currentTimeMillis()));
		}
			 
		superviseInfor.setFinishDate(Date.valueOf(superviseInforVo.getFinishDateStr().toString()));
		

		if (categoryId != null && categoryId.intValue() > 0) {
			TaskCategory category = (TaskCategory)this.taskCategoryManager.get(categoryId);
			superviseInfor.setTaskCategory(category);
		}

		if (leaderId != null && leaderId.intValue() > 0) {
			SystemUserInfor leader = (SystemUserInfor) this.systemUserManager.get(leaderId);
			superviseInfor.setLeader(leader);
		}

		if (departmentId != null && departmentId.intValue() > 0) {
			OrganizeInfor organizeInfor = (OrganizeInfor)this.organizeManager.get(departmentId);
			superviseInfor.setOrganizeInfor(organizeInfor);
		}

		if (managerId != null && managerId.intValue() > 0) {
			SystemUserInfor manager = (SystemUserInfor) this.systemUserManager.get(managerId);
			superviseInfor.setManager(manager);
		}

		try {
			// 上传附件
			String attachment = this.uploadAttachment(multipartRequest, "supervise");

			BeanUtils.copyProperties(superviseInfor, superviseInforVo);
			
			if (attachment == null || attachment.equals("")) {
				attachment = oldFiles;
			} else {
				if (oldFiles == null || oldFiles.equals("")) {
					// attachment = attachment;
				} else {
					attachment = attachment + "|" + oldFiles;
				}
			}
			superviseInfor.setAttachment(attachment);
			superviseInfor.setIsDeleted(0);
			
			//新增时，保存初始状态
			if(isNew){
				superviseInfor.setStatus(SuperviseInfor.Task_Status_SetCharger);
			}else {
				//修改时，是否修改了责任人
				if(superviseInforVo.getOperatorId() != null && superviseInforVo.getOperatorId() > 0){
					SystemUserInfor userInfor = (SystemUserInfor)this.systemUserManager.get(superviseInforVo.getOperatorId());
					superviseInfor.setOperator(userInfor);
				}
				
			}
			
			this.superviseInforManager.save(superviseInfor);
		} catch (Exception ex) {
			ex.printStackTrace();
//			model.addAttribute("_ErrorMessage", "添加或修改该信息发生错误！<br>错误信息如下:<br>" + ex.toString());
//			return "/common/error";
		}
	}
	
	
	
	
	/**
	 * 批量删除督办
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=delete")
	public void delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		String rowIds = request.getParameter("rowIds");
		PrintWriter out = response.getWriter();
		int categoryId = 0;
		
		String outString = "fail";
		
		try {
			boolean hasRight = false;
			String categoryIdStr = request.getParameter("categoryId");
			if(categoryIdStr != null && !categoryIdStr.equals("")){
				//查出所属分类
				categoryId = Integer.valueOf(categoryIdStr);
				TaskCategory categoryDelete = (TaskCategory)this.taskCategoryManager.get(Integer.valueOf(categoryId));
				
				//判断该用户在此分类下是否具有删除权限
				hasRight = this.taskCategoryRightManager.hasRight(categoryDelete, systemUser, TaskCategoryRight._Right_Delete);
			}
			
			
			if(hasRight){
				if (rowIds != null && rowIds.length() > 0) {
					String[] detleteIds = rowIds.split(",");
					if (detleteIds.length > 0) {
						for (int i = 0; i < detleteIds.length; i++) {

							Integer inforId = Integer.valueOf(detleteIds[i]);
							SuperviseInfor superviseInfor = (SuperviseInfor)this.superviseInforManager.get(inforId);
							superviseInfor.setIsDeleted(1);
							this.superviseInforManager.save(superviseInfor);
						}
						outString = "suc";
					}
				}
			}else {
				outString = "fail";
			}
		} catch (Exception e) {
			outString = "fail";
			e.printStackTrace();
		}
		out.print(outString);
	}
	
	/**
	 * 已读督办内容改变
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=saveReadStatus")
	public void saveReadStatus(@ModelAttribute("superviseInforVo")
		SuperviseInforVo superviseInforVo, HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {

			SuperviseInfor superviseInfor = new SuperviseInfor();
			Integer taskId = superviseInforVo.getTaskId();
			if(taskId != null && taskId.intValue() > 0){
				superviseInfor = (SuperviseInfor)this.superviseInforManager.get(taskId);
				superviseInfor.setReadStatus(1);
				this.superviseInforManager.save(superviseInfor);
			}
			
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/**
	 * 查看督办
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=viewTask")
	public String viewTask(@ModelAttribute("superviseInforVo")
		SuperviseInforVo superviseInforVo, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		int taskId = Integer.parseInt(request.getParameter("rowId"));
		SuperviseInfor superviseInfor = (SuperviseInfor)this.superviseInforManager.get(taskId);
//		request.setAttribute("_SuperviseInfor", superviseInfor);
//		
//		// 附件信息
//		String taskAttachment = superviseInfor.getAttachment();
//		if (taskAttachment != null && !taskAttachment.equals("")) {
//			String[][] attachment = processFile(taskAttachment);
//			request.setAttribute("_Attachment_Names", attachment[1]);
//			request.setAttribute("_Attachments", attachment[0]);
//		}
		getTaskBaseInfor(request, response, superviseInfor);
		
		//当前登录人所在部门的所有人员
		SystemUserInfor userInfor = SysCommonMethod.getSystemUser(request);
		List ownUsers = this.systemUserManager.getUserByOrganize(userInfor.getPerson().getDepartment().getOrganizeId(), 0);
		request.setAttribute("_OwnUsers", ownUsers);

		return "viewTask";
	}
	
	
	/**
	 * 查看督办信息--手机版所需格式
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=view_m")
	public void view_m(HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {
			SystemUserInfor userInfor = SysCommonMethod.getSystemUser(request);
			int personId = userInfor.getPersonId().intValue();
			
			JSONObject jsonObj = new JSONObject();
//		JSONConvert convert = new JSONConvert();
			
			SuperviseInfor superviseInfor = new SuperviseInfor();
			
			String taskIdStr = request.getParameter("taskId");
			if(taskIdStr != null && !taskIdStr.equals("")){
				int taskId = Integer.parseInt(taskIdStr);
				superviseInfor = (SuperviseInfor)this.superviseInforManager.get(taskId);
				
				//当目前所有进度报告已被部门经理审批，且全部为通过，才能再填写
				boolean canSubmitReport = true;
				Set<SuperviseReport> reports = superviseInfor.getReports();
				for(SuperviseReport report : reports){
					//为进度报告时
					if(report.getReportType() == SuperviseReport.Report_Type_Ing && report.getIsDeleted() == 0){
						if(report.getParent() == null && report.getIsDone() == 0){
							canSubmitReport = false;
							break;
						}
					}
				}
				
				//是否可提交工作完成报告
				boolean canSubmitFinalReport = false;
				int remainDays = DateHelper.daysBetween(new java.util.Date(), superviseInfor.getFinishDate());
				if(remainDays <= 20 && superviseInfor.getStatus() == SuperviseInfor.Task_Status_Processed){
					//是否已有完成报告
					boolean hasFinalReport = false;
					Set<SuperviseReport> reportList = superviseInfor.getReports();
					
					for(SuperviseReport report : reportList){
						if(report.getReportType() == SuperviseReport.Report_Type_Done && report.getIsDeleted() == 0){
							hasFinalReport = true;
							break;
						}
					}
					
					if(!hasFinalReport && superviseInfor.getOperator().getPersonId().intValue() == personId){
						canSubmitFinalReport = true;
					}
				}
				
				//是否可审核完成报告（即是行政助理）
				boolean canCheckFinal = false;
				boolean hasChecked = false;
				for(SuperviseReport report : reports){
					//为助理审核时
					if(report.getReportType() == SuperviseReport.Report_Type_Done && report.getJudgeDate() != null && !report.getJudgeDate().toString().equals("") && report.getIsDeleted() == 0){
						hasChecked = true;
						//break;
					}
				}
				
				if(!hasChecked && superviseInfor.getCreater().getPersonId().intValue() == personId){
					canCheckFinal = true;
				}else if(hasChecked){
					for(SuperviseReport report : reports){
						if(report.getReportType() == SuperviseReport.Report_Type_Done && (report.getJudgeDate() == null || report.getJudgeDate().equals("")) && report.getIsDone() == 1 && report.getIsDeleted() == 0){
							canCheckFinal = true;
						}
					}
				}
				
				//是否可打分（领导）
				boolean canNameScore = false;
				
				if(superviseInfor.getStatus() == SuperviseInfor.Task_Status_LeaderCheck){
//				//首先由助理选择需要打分的领导
//				if(superviseInfor.getLeaders() == null || superviseInfor.getLeaders().size() == 0 && superviseInfor.getCreater().getPersonId() == personId && superviseInfor.getIsDeleted() == 0){
//					canChooseLeaders = true;
//				}
					
					if(superviseInfor.getLeaders() != null && superviseInfor.getLeaders().size() > 0){
						//已设定时，提醒对应的领导去审核或者打分
						boolean hasAlChecked = false;
						for(TaskLeader taskLeader : superviseInfor.getLeaders()){
							if(taskLeader.getOperateDate() != null && !taskLeader.getOperateDate().toString().equals("")){
								hasAlChecked = true;
								break;
							}
						}
						
						//未有操作时间的，即第一次，全都未审，此时提醒所有涉及领导
						if(!hasAlChecked){
							for(TaskLeader taskLeader : superviseInfor.getLeaders()){
								SystemUserInfor leader = taskLeader.getLeader();
								int leaderId = leader.getPersonId().intValue();
								
								if(leaderId == personId){
									canNameScore = true;
								}
							}
						}
						
						//第二次及以后时，如果有此状态的、已完成的、未通过的，则表示是第二次或者更多的领导审核层，则可以通知
						boolean canWarn = false;
						for(SuperviseReport report : reports){
							if(report.getReportType() == SuperviseReport.Report_Type_Leader && report.getIsDone() == 1 && report.getIsPassed() == 0 && report.getIsDeleted() == 0){
								canWarn = true;
								break;
							}
						}
						if(canWarn){
							for(TaskLeader taskLeader : superviseInfor.getLeaders()){
								SystemUserInfor leader = taskLeader.getLeader();
								int leaderId = leader.getPersonId().intValue();
								
								if(leaderId == personId){
									canNameScore = true;
								}
							}
						}
					}
				}
				
				
				
				
				//可以做进度报告的部门审核
				boolean canCheckIng = false;
				if(superviseInfor.getStatus() == SuperviseInfor.Task_Status_Processed && superviseInfor.getManager().getPersonId().intValue() == personId && !canSubmitReport){
					canCheckIng = true;
				}
				
				//可以做完成报告的部门审核
				boolean canCheckDone = false;
				if((superviseInfor.getStatus() == SuperviseInfor.Task_Status_ToFinish || superviseInfor.getStatus() == SuperviseInfor.Task_Status_Check) && superviseInfor.getManager().getPersonId().intValue() == personId && !canSubmitFinalReport){
					canCheckDone = true;
				}
				
				/*******赋值到vo对象******/
				MSuperviseInforVo vo = new MSuperviseInforVo();
				
				BeanUtils.copyProperties(vo, superviseInfor);
				
				vo.setCategoryName(superviseInfor.getTaskCategory().getCategoryName());
				
				//执行部门
				vo.setDepartmentId(superviseInfor.getOrganizeInfor().getOrganizeId());
				vo.setDepartmentName(superviseInfor.getOrganizeInfor().getOrganizeName());
				
				//执行部门负责人
				vo.setManagerId(superviseInfor.getManager().getPersonId());
				vo.setManagerName(superviseInfor.getManager().getPerson().getPersonName());
				
				//计划完成时间
				if(superviseInfor.getFinishDate() != null){
					vo.setFinishDateStr(String.valueOf(superviseInfor.getFinishDate()));
				}else {
					vo.setFinishDateStr("");
				}
				
				//执行人
				if(superviseInfor.getOperator() != null){
					vo.setOperatorId(superviseInfor.getOperator().getPersonId());
					vo.setOperatorName(superviseInfor.getOperator().getPerson().getPersonName());
				}else {
					vo.setOperatorId(0);
					vo.setOperatorName("");
				}
				
				
				//实际完成时间
				if(superviseInfor.getEndTime() != null){
					vo.setEndTimeStr(String.valueOf(superviseInfor.getEndTime()));
				}else {
					vo.setEndTimeStr("");
				}
				
				
				//审核领导
				String leaderNames = "";
				List<TaskLeader> leaderList = new ArrayList<TaskLeader>();
				leaderList.addAll(superviseInfor.getLeaders());
				for(int i=0;i<leaderList.size();i++){
					leaderNames += leaderList.get(i).getLeader().getPerson().getPersonName();
					if(i<leaderList.size()-1){
						leaderNames += ",";
					}
				}
				vo.setLeaderNames(leaderNames);
				
				//报告
				List<MSuperviseReportVo> reportVos = new ArrayList<MSuperviseReportVo>();
				
				SimpleDateFormat detailSf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				SimpleDateFormat ymdSf = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat ymsf = new SimpleDateFormat("yyyy-MM");
				Set<SuperviseReport> reportSet = superviseInfor.getReports();
				for(SuperviseReport report : reportSet){
					MSuperviseReportVo reportVo = new MSuperviseReportVo();
					
					//复制属性值
					BeanUtils.copyProperties(reportVo, report);
					
					Timestamp operateDate = report.getOperateDate();

					String reportTypeTitle = "";
					
					//报告类型
					//1、进度报告、完成报告
					if(report.getReportType() == SuperviseReport.Report_Type_Ing || report.getReportType() == SuperviseReport.Report_Type_Done){
						if(report.getReportType() == SuperviseReport.Report_Type_Ing){
							String reportMonth = ymsf.format(operateDate);
							reportTypeTitle = reportMonth + "  进度报告"; 
						}else {
							reportTypeTitle = "工作完成报告";
						}
					}else if(report.getReportType() == SuperviseReport.Report_Type_Leader || report.getReportType() == SuperviseReport.Report_Type_CheckAdvice){
						if(report.getReportType() == SuperviseReport.Report_Type_Leader){
							if(report.getSuperviseInfor().getTaskCategory().getCategoryType() == 1){
								reportTypeTitle = "公司领导意见";
							}
							if(report.getSuperviseInfor().getTaskCategory().getCategoryType() == 2){
								reportTypeTitle = "党群领导意见";
							}
						}
						if(report.getReportType() == SuperviseReport.Report_Type_CheckAdvice){
							reportTypeTitle = "工作跟踪评价";
						}
					}
					
					
					//操作时间
					if (operateDate != null) {
						reportVo.setCreateTime(detailSf.format(operateDate));
					}else {
						reportVo.setCreateTime("");
					}
					
					//提交人
					if(report.getOperator() != null){
						reportVo.setOperator(report.getOperator().getPerson().getPersonName());
					}else {
						reportVo.setOperator("");
					}
					
					//部门审核
					if(report.getCheckDate() != null){
						reportVo.setManagerTime(detailSf.format(report.getCheckDate()));
						
						if (report.getIsPassed() == 0) {
							reportVo.setIsPassed("不通过");
						}else if (report.getIsPassed() == 1){
							reportVo.setIsPassed("通过");
						}else {
							reportVo.setIsPassed("");
						}
					}else {
						reportVo.setManagerTime("");
						reportVo.setIsPassed("未审核");
					}
					
					
					
					//预判
					if(report.getJudgeDate() != null){
						reportVo.setJudgeTime(detailSf.format(report.getJudgeDate()));
						
						if(report.getIsJudgePassed() == 0){
							reportVo.setIsJudgePassed("预判不通过");
						}if(report.getIsJudgePassed() == 1){
							reportVo.setIsJudgePassed("预判通过");
						}else {
							reportVo.setIsJudgePassed("");
						}
					}else {
						reportVo.setJudgeTime("");
						
						reportVo.setIsJudgePassed("");
					}
					
					
					
					
					//延迟标签
					if(report.getReportType() == SuperviseReport.Report_Type_Check && report.getDelayDate() != null){
						reportVo.setDelayTag("以下为延期工作报告");
						reportTypeTitle = "延迟提交完成报告";
						//延迟截止时间
						reportVo.setDelayDateStr(ymdSf.format(report.getDelayDate()));
					}
					
					reportVo.setReportTypeTitle(reportTypeTitle);
					
					//是否可以进行各种审核及操作名称 
					if(report.getCheckDate() == null){
						if(canCheckIng){
							reportVo.setOperateName("审核进度报告");
						}
						if(canCheckDone){
							reportVo.setOperateName("审核完成报告");
						}
						
						if(report.getReportType() == SuperviseReport.Report_Type_Ing || report.getReportType() == SuperviseReport.Report_Type_Done){
							reportVo.setCanCheckReport(true);
						}else {
							reportVo.setCanCheckReport(false);
						}
						
						reportVo.setCanJudge(false);
						reportVo.setCanFinalCheck(false);
					}
					
					if(report.getJudgeDate() == null){
						if(superviseInfor.getStatus() == SuperviseInfor.Task_Status_Check && report.getReportType() == SuperviseReport.Report_Type_Done 
								&& superviseInfor.getCreater().getPersonId().intValue() == personId && report.getIsPassed() == 1 
								&& report.getIsDone() == 1 && report.getIsJudged() == 0 && canCheckFinal){
								reportVo.setCanJudge(true);
								reportVo.setOperateName("预判完成情况");
								
								reportVo.setCanCheckReport(false);
								reportVo.setCanFinalCheck(false);
							}
					}
					
//					if(canNameScore){
//						reportVo.setCanFinalCheck(true);
//						reportVo.setOperateName("领导审核");
//						
//						reportVo.setCanCheckReport(false);
//						reportVo.setCanJudge(false);
//					}
							
					
					reportVos.add(reportVo);
				}
				
				if(canNameScore){
					MSuperviseReportVo reportVo = new MSuperviseReportVo();
					
					reportVo.setReportType(SuperviseReport.Report_Type_Leader);
					reportVo.setCanFinalCheck(true);
					reportVo.setOperateName("领导审核");
					
					reportVo.setCanCheckReport(false);
					reportVo.setCanJudge(false);
					
					reportVos.add(reportVo);
				}
				
				vo.setReportVos(reportVos);
				
				jsonObj.put("_Task", vo);
			}
			

			//设置字符编码
			response.setContentType(CoreConstant.CONTENT_TYPE);
			response.getWriter().print(jsonObj);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	
	/**
	 * 保存指定的负责人
	 * @param meetInforVo
	 * @param model
	 * @param request
	 * @param response
	 * @param multipartRequest
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=saveOperator")
	public String saveOperator(@ModelAttribute("superviseInforVo")
			SuperviseInforVo superviseInforVo, Model model, HttpServletRequest request, HttpServletResponse response, DefaultMultipartHttpServletRequest multipartRequest) throws Exception {
		
		int taskId = superviseInforVo.getTaskId();
		SuperviseInfor superviseInfor = (SuperviseInfor)this.superviseInforManager.get(taskId);
		Integer operatorId = superviseInforVo.getOperatorId();

		if (operatorId != null && operatorId.intValue() > 0) {
			SystemUserInfor userInfor = (SystemUserInfor)this.systemUserManager.get(operatorId);
			superviseInfor.setOperator(userInfor);
			if(superviseInfor.getStatus()==1){
				//设置状态
				superviseInfor.setStatus(2);
			}
			this.superviseInforManager.save(superviseInfor);
		}

		return "redirect:/supervise/superviseInfor.do?method=viewTask&rowId="+taskId;
	}
	
	/**
	 * 助理选择领导
	 * @param request
	 * @param response
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=chooseLeader")
	public String chooseLeader(HttpServletRequest request, HttpServletResponse response, TaskLeaderVo vo) throws Exception {
		
		//String taskId = request.getParameter("taskId");
		Integer taskId = vo.getTaskId();
		SuperviseInfor task = new SuperviseInfor(); 
		
		if (taskId != null && taskId.intValue() > 0) {
			
			task = (SuperviseInfor)this.superviseInforManager.get(Integer.valueOf(taskId));
			
			//获取督办实例相关信息
			getTaskBaseInfor(request, response, task);
			
			Set<TaskLeader> leaders = task.getLeaders();
			int[] leaderIds = new int[leaders.size()];
			
			int i=0;
			for(TaskLeader leader : leaders){
				leaderIds[i] = leader.getLeader().getPersonId().intValue();
				i++;
			}
			vo.setPersonIds(leaderIds);
			request.setAttribute("_LeaderIds", leaderIds);
			
		}
		
		//获取部门信息
		List departments = this.organizeManager.getDepartments();
		List actualDeparts = new ArrayList();
		for(int i=0;i<departments.size();i++){
			OrganizeInfor department = (OrganizeInfor)departments.get(i);
			//行政时，显示公司领导 
			//党群时，显示党群工作领导
			if(task.getTaskCategory().getCategoryType() == 1 && department.getOrganizeName().contains("公司领导")){
				actualDeparts.add(department);
			}
			if(task.getTaskCategory().getCategoryType() == 2 && department.getOrganizeName().contains("党群工作领导")){
				actualDeparts.add(department);
			}
			
			//部门建设时，显示公司领导 
			//内控时，显示公司领导
			if(task.getTaskCategory().getCategoryType() == 3 && department.getOrganizeName().contains("公司领导")){
				actualDeparts.add(department);
			}
			if(task.getTaskCategory().getCategoryType() == 4 && department.getOrganizeName().contains("公司领导")){
				actualDeparts.add(department);
			}
		}
		request.setAttribute("_Departments", actualDeparts);
		
		/** 行政-取公司领导角色下的用户 ；党群-取党群工作领导角色下的用户*/
		String roleIds = "";
		if(task.getTaskCategory().getCategoryType() == 1){
			roleIds = String.valueOf(CoreConstant.Role_Checker_Leader);
		}
		if(task.getTaskCategory().getCategoryType() == 2){
			roleIds = String.valueOf(CoreConstant.Role_Leader_Party);
		}
		
		/** 部门建设、内控-取“责任部门审核人”角色下的用户，并再定位到与下发人同部门人领导*/
		if(task.getTaskCategory().getCategoryType() == 3){
			roleIds = String.valueOf(CoreConstant.Role_Leader_Manager);
		}
		if(task.getTaskCategory().getCategoryType() == 4){
			roleIds = String.valueOf(CoreConstant.Role_Leader_Manager);
		}
		
		List roles = this.roleManager.getRoles(roleIds);
		request.setAttribute("_Roles", roles);
		
		//根据职级获取用户
		List users = this.systemUserManager.getUserByPosition(PersonInfor._Position_Tag);
		request.setAttribute("_Users", users);

		//获取职级大于一定值的用户
		List otherUsers = this.systemUserManager.getOtherPositionUser(PersonInfor._Position_Tag);
		request.setAttribute("_OtherUsers", otherUsers);
		
		return "chooseLeader";
	}
	
	/**
	 * 保存选择的领导
	 * @param request
	 * @param response
	 * @param vo
	 * @param multipartRequest
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=saveChoose")
	public String saveChoose(HttpServletRequest request, HttpServletResponse response, 
			TaskLeaderVo vo, DefaultMultipartHttpServletRequest multipartRequest) throws Exception {
		
		Integer taskId = vo.getTaskId();
		SuperviseInfor task = new SuperviseInfor(); 
		

		if (taskId != null && taskId.intValue() > 0) {
			
			task = (SuperviseInfor)this.superviseInforManager.get(Integer.valueOf(taskId));

		}
		
		int[] personIds = vo.getPersonIds();
		Set<TaskLeader> leaders = task.getLeaders();

		// 此次选择中，去掉的删除
		TaskLeader[] arrayLeader = (TaskLeader[]) leaders.toArray(new TaskLeader[leaders.size()]);
		for (int k = arrayLeader.length - 1; k >= 0; k--) {
			TaskLeader leader = (TaskLeader) arrayLeader[k];
			SystemUserInfor tempUser = leader.getLeader();
			int rPersonId = tempUser.getPersonId().intValue();

			boolean hasThisUser = false;
			if (personIds != null && personIds.length != 0) {
				for (int kk = 0; kk < personIds.length; kk++) {
					if (rPersonId == personIds[kk]) {
						hasThisUser = true;
						break;
					}
				}
			}
		}

		// 没有的加上
		if (personIds != null && personIds.length != 0) {
			for (int kk = 0; kk < personIds.length; kk++) {
				boolean hasThisUser = false;
				TaskLeader leader = new TaskLeader();

				for (Iterator it = leaders.iterator(); it.hasNext();) {
					leader = (TaskLeader) it.next();

					SystemUserInfor tempUser = leader.getLeader();
					int rPersonId = tempUser.getPersonId().intValue();
					if (rPersonId == personIds[kk]) {
						hasThisUser = true;
						break;
					}
				}

				if (!hasThisUser) {
					TaskLeader tmpLeader = new TaskLeader();
					// tpExcuter.setIsReaded(0);
					tmpLeader.setSuperviseInfor(task);
					SystemUserInfor tpUser = (SystemUserInfor) this.systemUserManager.get(personIds[kk]);
					tmpLeader.setLeader(tpUser);
					leaders.add(tmpLeader);
				}
			}
		}
		this.taskLeaderManager.save(task);
		
		
		return "redirect:/supervise/superviseInfor.do?method=viewTask&rowId="+task.getTaskId();
	}
	

	/**
	 * 获取需要处理的实例(用于首页显示)
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=getNeedDealTasks")
	public void getNeedDealTasks(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			SimpleDateFormat tmpsf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//当前登录人
			SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
			int personId = systemUser.getPersonId().intValue();
			//System.out.println("执行开始----------" + tmpsf.format(new java.util.Date()));
			/**
			 * 标签，标记是否为app端发出的请求
			 * 若为app请求时，只返回以下三种类型的待办
			 * 1、部门审核   代码为  1
			 * 2、助理预判   代码为  2
			 * 3、最终评判   代码为  3
			 */
			String tag = request.getParameter("tag");
			
			boolean isApp = false;
			if(tag != null && !tag.equals("")){
				if(tag.equals("app")){
					isApp = true;
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
			
			if(isApp && !isAuto && StringUtil.isNotEmpty(SysCommonMethod.getPlatform(request))){
				/************记录app模块使用日志************/ 
				AppModuleLog appModuleLog = new AppModuleLog();
				appModuleLog.setModuleName("工作跟踪审批");
				appModuleLog.setPlatform(SysCommonMethod.getPlatform(request));
				appModuleLog.setLogTime(new Timestamp(System.currentTimeMillis()));
				appModuleLog.setUserName(systemUser.getUserName());
				this.appModuleLogManager.save(appModuleLog);
				/*****************************************/
			}

			//当前时间
			Calendar cal = Calendar.getInstance();
			java.util.Date date = new java.util.Date();
			cal.setTime(date);
			
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sfYM = new SimpleDateFormat("yyyy-MM");
			SimpleDateFormat sfM = new SimpleDateFormat("MM");
			SimpleDateFormat sfD = new SimpleDateFormat("dd");
			
			String thisYearMonth = sfYM.format(date);
			int thisMonth = Integer.valueOf(sfM.format(date));
			int thisDay = Integer.valueOf(sfD.format(date));
			
			// 获取所有未删除未结束的
			String queryHQL = "from SuperviseInfor superviseInfor where isDeleted = 0 and endTime is null order by createDate";
			// and (manager.personId = " + personId + " and operator is null
			List tasks = this.superviseInforManager.getResultByQueryString(queryHQL);
			
			//返回给pc端的list
			List<SuperviseInfor> taskList = new ArrayList<SuperviseInfor>();
			List<String> warningList = new ArrayList<String>();
			
			//返回给pc端的提示list 
			List<SuperviseInfor> changedTaskList = new ArrayList<SuperviseInfor>();
			List<String> changedWarningList = new ArrayList<String>();
			
			//返回给app端的list
			List<SuperviseInfor> appTaskList = new ArrayList<SuperviseInfor>();
			List<Integer> appTypeList = new ArrayList<Integer>();  
			
			for (Iterator it = tasks.iterator();it.hasNext();){
				SuperviseInfor task = (SuperviseInfor)it.next();
				
				if(systemUser.getUserName().contains("xinqb") && task.getCreateDate().before(DateHelper.getDate("2016-01-01"))){
					
				}else {
					int status = task.getStatus();
					int startMonthInt = Integer.valueOf(sfM.format(task.getCreateDate()));
					int startDayInt = Integer.valueOf(sfD.format(task.getCreateDate()));
					
					int secondMonthInt = Integer.valueOf(sfM.format(DateHelper.addMonth(task.getCreateDate(), 1)));
					int secondDayInt = Integer.valueOf(sfD.format(DateHelper.addMonth(task.getCreateDate(), 1)));
					
					/************修改内容时，提醒对应的部门负责人***********/
					if(task.getRemindStatus() == 1 && task.getReadStatus() == 0 && task.getManager().getPersonId().intValue() == personId){
						changedTaskList.add(task);
						changedWarningList.add("[<font color=red>任务内容已改变，请知悉！</font>]");
					}
					if(task.getRemindStatus() == 2 && task.getReadStatus() == 0 && task.getFormerManager().getPersonId().intValue() == personId){
						changedTaskList.add(task);
						changedWarningList.add("[<font color=red>责任部门已改变，请知悉！</font><font color=#08324b>新部门："+task.getOrganizeInfor().getOrganizeName()+"</font>]");
					}
					/****************************************/
					/*********责任经理分配责任人**********/
					if(task.getManager().getPersonId().intValue() == personId && task.getOperator() == null){
						taskList.add(task);
						warningList.add("[<font color=red>请分配责任人</font>]");

						/**********类型为1，返回到app端**********/
						appTaskList.add(task);
						appTypeList.add(1);
					}
					if(task.getCreater().getPersonId().intValue() == personId && task.getOperator() == null){
						taskList.add(task);
						warningList.add("[<font color=red>任务已分配待指定责任人</font>]");
					}

					//其下所有报告
					Set<SuperviseReport> reports = task.getReports();
					List<SuperviseReport> reportList = new ArrayList<SuperviseReport>(reports);
					
					if(status == SuperviseInfor.Task_Status_SetCharger){

//						/*********责任经理分配责任人**********/
//						if(task.getManager().getPersonId().intValue() == personId && task.getOperator() == null){
//							taskList.add(task);
//							warningList.add("[<font color=red>请分配责任人</font>]");
//						}
					}else if(status == SuperviseInfor.Task_Status_Processed || status == SuperviseInfor.Task_Status_ToFinish){
						/*********责任人定时上交工作进度报告*********/
						// 下达任务当月及预期完成月不提醒，其余如果哪月未提交，则都提醒
						String startMonth = sfYM.format(task.getCreateDate());
						String endMonth = sfYM.format(task.getFinishDate());
						if(task.getManager().getPersonId().intValue() == personId || task.getOperator().getPersonId().intValue() == personId || task.getCreater().getPersonId().intValue() == personId){
							//刚下达时，至提交第一次前（任务结束前），提醒责任人
							if(task.getStatus() == SuperviseInfor.Task_Status_Processed && !thisYearMonth.equals(endMonth) && (thisMonth==startMonthInt /*|| (thisMonth==secondMonthInt&&thisDay<25)*/) && (reportList == null || reportList.size() == 0) && task.getOperator().getPersonId().intValue() == personId && (new java.util.Date().before(task.getFinishDate()))){
								taskList.add(task);
								/*
								 * 改为 1、下达日在25日之前，25日之前的时间里提示待执行，25日之后如当月没提交过报告，则提示请在本月底提交进度报告 2、下达日25日之后，下达日到下月1日待执行 下月25日后提示进度报告
								 */
								
								if(task.getOperator().getPersonId().intValue() == personId){
									if(thisMonth == startMonthInt && startDayInt < 25 && thisDay < 25){
										warningList.add("[<font color=red>任务已下达，待执行</font>]");
									}else if(thisMonth == startMonthInt && startDayInt < 25 && thisDay >= 25){
										warningList.add("[<font color=red>请在本月底提交进度报告</font>]");
									}else if(thisMonth == startMonthInt && startDayInt >= 25 && thisDay >= 25){
										warningList.add("[<font color=red>任务已下达，待执行</font>]");
									}else {
										warningList.add("[<font color=red>请在本月底提交进度报告</font>]");
									}
								}
								if(task.getManager().getPersonId().intValue() == personId){
									//warningList.add("[<font color=red>任务已下达，待<font color=#0DE8F5><b>"+task.getOperator().getPerson().getPersonName()+"</b></font>提交报告</font>]");
								}
							}
							//!thisYearMonth.equals(startMonth) && 
							if(!thisYearMonth.equals(endMonth) && task.getStatus() == SuperviseInfor.Task_Status_Processed){
								//开始月与现在间隔的月数（现在需大于等于25日）
								int months = 0;
								if(thisDay<25){
//									months = DateHelper.monthsBetween(task.getCreateDate(), new java.util.Date()) - 1;
									months = DateHelper.monthsBetween(DateHelper.getDate(startMonth+"-25"), new java.util.Date()) - 1;
								}else {
//									months = DateHelper.monthsBetween(task.getCreateDate(), new java.util.Date());
									months = DateHelper.monthsBetween(DateHelper.getDate(startMonth+"-25"), new java.util.Date());
								}

								//如果reportList为空时，则也未提交
								//if(reportList == null || reportList.size() == 0){
								//	taskList.add(task);
								//	warningList.add("[<font color=red>请提交首次进度报告</font>]");
								//}else {
									//提醒周期（1或2）
									int period = task.getTaskCategory().getPeriod();
									for(int i=period;i<=months;i+=period){
										String everyMonth = sfYM.format(DateHelper.addMonth(task.getCreateDate(), i));
										boolean hasSubmited = false;
										
										//把每一次未提交的都放到提醒中（2015-06-15修改为：只在25号至月底前提示当月的，跨月后不再提示）
										for(SuperviseReport report : reportList){
											if(report.getReportType() == SuperviseReport.Report_Type_Ing && report.getIsDeleted() == 0){//类型为进度报告
												//格式化日期
												String reportDateStr = sfYM.format(report.getOperateDate());
												if(report.getParent() != null){
													reportDateStr = sfYM.format(report.getParent().getOperateDate());
												}
												if(reportDateStr.equals(everyMonth)){
													if(report.getParent() == null){
														hasSubmited = true;
													}
													
													/*//已提交，部门经理未处理
													if(report.getCheckDate() == null || report.getCheckDate().toString().equals("")){
														if(task.getManager().getPersonId().intValue() == personId){
															taskList.add(task);
															warningList.add("[<font color=#0DE8F5><b>"+task.getOperator().getPerson().getPersonName()+"</b></font><font color=red>已提交"+everyMonth+"进度报告，请处理</font>]");
														}
													}else {
														//部门经理处理，但未通过的，提示责任人，重新提交
														if(report.getParent() == null && report.getChilds().size() == 0 && report.getIsDone() == 0 && task.getOperator().getPersonId().intValue() == personId){
															taskList.add(task);
															warningList.add("[<font color=red>"+everyMonth+"进度报告未通过，请处理</font>]");
														}
														
														if(report.getParent() != null && report.getIsDone() == 0){
															taskList.add(task);
															warningList.add("[<font color=red>"+everyMonth+"进度报告未通过，请处理</font>]");
														}
													}*/
												}
											}
											
										}
										
										if(!hasSubmited){
											//未提交进度报告
											if((task.getManager().getPersonId().intValue() == personId) && (thisYearMonth.equals(everyMonth)) && (new java.util.Date().before(task.getFinishDate()))){
												taskList.add(task);
												warningList.add("[<font color=red>"+task.getOperator().getPerson().getPersonName()+everyMonth+"进度报告未提交</font>]");
											}
											//（2015-06-15修改为：只在25号至月底前提示当月的，跨月后不再提示）
//											if((task.getOperator().getPersonId().intValue() == personId)){
											if((task.getOperator().getPersonId().intValue() == personId) && (thisYearMonth.equals(everyMonth)) && (new java.util.Date().before(task.getFinishDate()))){
												taskList.add(task);
//												warningList.add("[<font color=red>请提交"+everyMonth+"进度报告</font>]");
												warningList.add("[<font color=red>请在月底前提交本月进度报告</font>]");
											}
										}
									}
									
									
								//}
							}
							
							
							
							/****************进度报告**********************/
							//进度报告数量，以判断是不是最近一次提交的
							int ingCount = 0;
							int doneCount = 0;
							for(SuperviseReport report : reportList){
								if(report.getReportType() == SuperviseReport.Report_Type_Ing && report.getIsDeleted() == 0){//类型为进度报告
									ingCount += 1;
								}
								if(report.getReportType() == SuperviseReport.Report_Type_Done && report.getIsDeleted() == 0){//类型为完成报告
									doneCount += 1;
								}
							}
							//把每一次未提交的都放到提醒中（2015-06-15修改为：只在25号至月底前提示当月的，跨月后不再提示）
							int ingLoopNum = 0;
							for(SuperviseReport report : reportList){
								if(report.getReportType() == SuperviseReport.Report_Type_Ing && report.getIsDeleted() == 0){//类型为进度报告
									ingLoopNum += 1;
									//格式化日期
									String reportDateStr = sfYM.format(report.getOperateDate());
									if(report.getParent() != null){
										reportDateStr = sfYM.format(report.getParent().getOperateDate());
									}
									//if(reportDateStr.equals(everyMonth)){
									//	if(report.getParent() == null){
									//		hasSubmited = true;
									//	}
										
										//已提交，部门经理未处理
										if(report.getCheckDate() == null || report.getCheckDate().toString().equals("")){
											if(task.getManager().getPersonId().intValue() == personId){
												taskList.add(task);
												warningList.add("[<font color=red><b>"+task.getOperator().getPerson().getPersonName()+"</b></font><font color=red>已提交"+reportDateStr+"进度报告，请处理</font>]");
												
												/**********类型为1，返回到app端**********/
												appTaskList.add(task);
												appTypeList.add(1);
											}
										}else {
											//部门经理处理，但未通过的，提示责任人，重新提交
											if(report.getParent() == null && report.getChilds().size() == 0 && report.getIsDone() == 0 && task.getOperator().getPersonId().intValue() == personId){
												taskList.add(task);
												warningList.add("[<font color=red>"+reportDateStr+"进度报告未通过，请处理</font>]");
											}
											
											//部门经理处理，但未通过的，提示经理跟踪，至再次提交
											if(report.getParent() == null && report.getChilds().size() == 0 && report.getIsDone() == 0 && task.getManager().getPersonId().intValue() == personId){
												taskList.add(task);
												warningList.add("[<font color=red>"+reportDateStr+"进度报告未通过，请跟踪</font>]");
											}
											
											if(report.getParent() != null && report.getIsDone() == 0 && ingLoopNum == ingCount && task.getOperator().getPersonId().intValue() == personId){
												taskList.add(task);
												warningList.add("[<font color=red>"+reportDateStr+"进度报告未通过，请处理</font>]");
											}
											if(report.getParent() != null && report.getIsDone() == 0 && ingLoopNum == ingCount && task.getManager().getPersonId().intValue() == personId){
												taskList.add(task);
												warningList.add("[<font color=red>"+reportDateStr+"进度报告未通过，请跟踪</font>]");
											}
										}
									//}
								}
								
							}
							
							
							
							/*********完成报告********/
							if(task.getStatus() == SuperviseInfor.Task_Status_ToFinish){
								int doneLoopNum = 0;
								for(SuperviseReport report : reportList){
									//类型为完成报告
									if(report.getReportType() == SuperviseReport.Report_Type_Done && report.getIsDeleted() == 0){
										doneLoopNum += 1;
										//已提交，部门经理未处理
										if(report.getCheckDate() == null || report.getCheckDate().toString().equals("")){
											if(task.getManager().getPersonId().intValue() == personId){
												taskList.add(task);
												warningList.add("[<font color=#0DE8F5><b>"+task.getOperator().getPerson().getPersonName()+"</b></font><font color=red>已提交完成报告，请处理</font>]");
												
												/**********类型为2，返回到app端**********/
												appTaskList.add(task);
												appTypeList.add(2);
											}
										}else {
											//部门经理处理，但未通过的，提示责任人，重新提交
											if(report.getParent() == null && report.getChilds().size() == 0 && report.getIsDone() == 0 && task.getOperator().getPersonId().intValue() == personId){
												taskList.add(task);
												warningList.add("[<font color=red>完成报告未通过，请处理</font>]");
											}
											if(report.getParent() == null && report.getChilds().size() == 0 && report.getIsDone() == 0 && task.getManager().getPersonId().intValue() == personId){
												taskList.add(task);
												warningList.add("[<font color=red>完成报告未通过，请跟踪</font>]");
											}
											
											if(report.getParent() != null && report.getIsDone() == 0 && task.getOperator().getPersonId().intValue() == personId){
												taskList.add(task);
												warningList.add("[<font color=red>完成报告未通过，请处理</font>]");
											}
											if(report.getParent() != null && report.getIsDone() == 0 && task.getManager().getPersonId().intValue() == personId){
												taskList.add(task);
												warningList.add("[<font color=red>完成报告未通过，请跟踪</font>]");
											}
										}
									}
									
									//延迟时，寻找isDone为0的延迟报告类型，提示责任人提交完成报告
									if(report.getReportType() == SuperviseReport.Report_Type_Check && report.getDelayDate() != null && report.getIsDone() == 0 && report.getIsDeleted() == 0){
										//早于延迟日期的，提醒责任人
										if(task.getOperator().getPersonId().intValue() == personId){//new java.util.Date().before(report.getDelayDate()) && 
											taskList.add(task); 
											warningList.add("[<font color=red>不合格，请在"+report.getDelayDate()+"前再次提交完成报告</font>]");
										}
										
										//早于延迟日期的，提醒部门经理及下达者new java.util.Date().before(report.getDelayDate()) && 
										if((task.getManager().getPersonId().intValue() == personId || task.getCreater().getPersonId().intValue() == personId)){
											taskList.add(task);
											warningList.add("[<font color=red>不合格，<font color=#0DE8F5><b>"+task.getOperator().getPerson().getPersonName()+"</b></font>需在"+report.getDelayDate()+"前再次提交完成报告</font>]");
										}
									}
								}
							}
							
							
							//到期前20天，提醒责任人提交完成报告
							int remainDays = DateHelper.daysBetween(DateHelper.getDate(DateHelper.getString(new java.util.Date())), task.getFinishDate());
							if(remainDays <= 20 ){
								//是否已有完成报告
								boolean hasFinalReport = false;
								SuperviseReport finishReport = null;
								for(SuperviseReport report : reportList){
									if(report.getReportType() == SuperviseReport.Report_Type_Done && report.getIsDeleted() == 0){
										finishReport = report;
										hasFinalReport = true;
										break;
									}
								}
								/*//如果已经有完成报告，但是计划完成时间修改延后 则按照正常流程重新走（不按延期流程）
								if(finishReport!=null && reportList.size()>1){
									int creMon = Integer.valueOf(sfM.format(finishReport.getCreateDate()));
									int creDay = Integer.valueOf(sfD.format(finishReport.getCreateDate()));

									int finMon = Integer.valueOf(sfM.format(task.getFinishDate()));
									int finDay = Integer.valueOf(sfD.format(task.getFinishDate()));

									if(finishReport.getCreateDate().getTime()<task.getFinishDate().getTime() && (finMon>creMon || finMon==creMon && finDay>creDay)){
										//完成报告时间早于计划完成时间
										taskList.add(task);
										warningList.add("[<font color=red>请在"+task.getFinishDate()+"前提交完成报告</font>]");
									}
								}*/


								if(!hasFinalReport && task.getOperator().getPersonId().intValue() == personId ){
									taskList.add(task);
									warningList.add("[<font color=red>请在"+task.getFinishDate()+"前提交完成报告</font>]");
								}
								
								//同时提醒部门经理及下达者(10天)
								if(remainDays <= 10){
									if(!hasFinalReport && (task.getManager().getPersonId().intValue() == personId || task.getCreater().getPersonId().intValue() == personId)){
										taskList.add(task);
										warningList.add("[<font color=red><font color=#0DE8F5><b>"+task.getOperator().getPerson().getPersonName()+"</b></font>需在"+task.getFinishDate()+"前提交完成报告</font>]");
									}
								}
							}
						}
						
					}else if(status == SuperviseInfor.Task_Status_Check){
						//完成报告已通过，行政助理审核时
						
						//先判断是否已有行政助理意见
						boolean hasChecked = false;
						for(SuperviseReport report : reportList){
							if(report.getReportType() == SuperviseReport.Report_Type_Done && report.getJudgeDate() != null && !report.getJudgeDate().toString().equals("") && report.getIsDeleted() == 0){
								hasChecked = true;
								//break;
								//有预判，但未通过时，通知责任人重新提交
								if(report.getParent() == null && report.getChilds().size() == 0 && report.getIsJudged() == 1 && report.getIsJudgePassed() == 0 && task.getOperator().getPersonId().intValue() == personId){
									taskList.add(task);
									warningList.add("[<font color=red>完成报告被预判为不通过，请重新提交</font>]");
								}
								if(report.getParent() == null && report.getChilds().size() == 0 && report.getIsJudged() == 1 && report.getIsJudgePassed() == 0 && task.getManager().getPersonId().intValue() == personId){
									taskList.add(task);
									warningList.add("[<font color=red>完成报告被预判为不通过，请跟踪</font>]");
								}
								if(report.getParent() == null && report.getChilds().size() == 0 && report.getIsJudged() == 1 && report.getIsJudgePassed() == 0 && task.getCreater().getPersonId().intValue() == personId){
									taskList.add(task);
									warningList.add("[<font color=red>完成报告被预判为不通过，请跟踪</font>]");
								}
								
								if(report.getParent() != null && report.getIsJudged() == 1 && report.getIsJudgePassed() == 0 && task.getOperator().getPersonId().intValue() == personId && report.getReportId().intValue() == reportList.get(reportList.size()-1).getReportId().intValue()){
									taskList.add(task);
									warningList.add("[<font color=red>完成报告被预判为不通过，请重新提交</font>]");
								}
								if(report.getParent() != null && report.getIsJudged() == 1 && report.getIsJudgePassed() == 0 && task.getManager().getPersonId().intValue() == personId && report.getReportId().intValue() == reportList.get(reportList.size()-1).getReportId().intValue()){
									taskList.add(task);
									warningList.add("[<font color=red>完成报告被预判为不通过，请跟踪</font>]");
								}
								if(report.getParent() != null && report.getIsJudged() == 1 && report.getIsJudgePassed() == 0 && task.getCreater().getPersonId().intValue() == personId && report.getReportId().intValue() == reportList.get(reportList.size()-1).getReportId().intValue()){
									taskList.add(task);
									warningList.add("[<font color=red>完成报告被预判为不通过，请跟踪</font>]");
								}
							}
							
							//预判不合格后的部门审核
							if(report.getReportType() == SuperviseReport.Report_Type_Done && (report.getCheckDate() == null || report.getCheckDate().toString().equals("")) && report.getIsDeleted() == 0){
								if(task.getManager().getPersonId().intValue() == personId){
									taskList.add(task);
									warningList.add("[<font color=#0DE8F5><b>"+task.getOperator().getPerson().getPersonName()+"</b></font><font color=red>已提交完成报告，请处理</font>]");
									
									/**********类型为1，返回到app端**********/
									appTaskList.add(task);
									appTypeList.add(1);
								}
								
							}else if(report.getReportType() == SuperviseReport.Report_Type_Done && report.getCheckDate() != null && !report.getCheckDate().toString().equals("") && report.getIsDeleted() == 0) {
								//部门经理处理，但未通过的，提示责任人，重新提交
								if(report.getParent() == null && report.getChilds().size() == 0 && report.getIsDone() == 0 && task.getOperator().getPersonId().intValue() == personId){
									taskList.add(task);
									warningList.add("[<font color=red>完成报告未通过，请处理</font>]");
								}
								if(report.getParent() == null && report.getChilds().size() == 0 && report.getIsDone() == 0 && task.getManager().getPersonId().intValue() == personId){
									taskList.add(task);
									warningList.add("[<font color=red>完成报告未通过，请跟踪</font>]");
								}
								
								if(report.getParent() != null && report.getIsDone() == 0 && task.getOperator().getPersonId().intValue() == personId){
									taskList.add(task);
									warningList.add("[<font color=red>完成报告未通过，请处理</font>]");
								}
								if(report.getParent() != null && report.getIsDone() == 0 && task.getManager().getPersonId().intValue() == personId){
									taskList.add(task);
									warningList.add("[<font color=red>完成报告未通过，请跟踪</font>]");
								}
							}
							
							//再次提交的完成报告，助理审核
							if(hasChecked){
								if(report.getReportType() == SuperviseReport.Report_Type_Done && report.getParent() == null && report.getChilds().size() == 0 && report.getIsDone() == 1 && (report.getJudgeDate() == null || report.getJudgeDate().equals(""))){
									if(task.getCreater().getPersonId().intValue() == personId){
										taskList.add(task);
										warningList.add("[<font color=#0DE8F5><b>"+task.getOperator().getPerson().getPersonName()+"</b></font><font color=red>已提交完成报告，请处理</font>]");
										
										/**********类型为2，返回到app端**********/
										appTaskList.add(task);
										appTypeList.add(2);
									}
									
								}else if(report.getReportType() == SuperviseReport.Report_Type_Done && report.getParent() != null && report.getIsPassed() == 1 && (report.getJudgeDate() == null || report.getJudgeDate().equals(""))){
									if(task.getCreater().getPersonId().intValue() == personId){
										taskList.add(task);
										warningList.add("[<font color=#0DE8F5><b>"+task.getOperator().getPerson().getPersonName()+"</b></font><font color=red>已提交完成报告，请处理</font>]");
										
										/**********类型为2，返回到app端**********/
										appTaskList.add(task);
										appTypeList.add(2);
									}
								}
							}
						}
						
						
						
						if(!hasChecked && task.getCreater().getPersonId().intValue() == personId){
							taskList.add(task);
							warningList.add("[<font color=#0DE8F5><b>"+task.getOperator().getPerson().getPersonName()+"</b></font><font color=red>已提交完成报告，请处理</font>]");
							
							/**********类型为2，返回到app端**********/
							appTaskList.add(task);
							appTypeList.add(2);
						}
						
					}else if(status == SuperviseInfor.Task_Status_LeaderCheck){
						//领导审核
						//首先由助理选择需要打分的领导
						if(task.getLeaders() == null || task.getLeaders().size() == 0 && task.getCreater().getPersonId() == personId && task.getIsDeleted() == 0){
							taskList.add(task);
							warningList.add("[<font color=red>请选择需要审核的领导</font>]");
						}else if(task.getLeaders() != null && task.getLeaders().size() > 0){
							//已设定时，提醒对应的领导去审核或者打分
							boolean hasAlChecked = false;
							for(TaskLeader taskLeader : task.getLeaders()){
								if(taskLeader.getOperateDate() != null && !taskLeader.getOperateDate().toString().equals("")){
									hasAlChecked = true;
									break;
								}
							}
							
							//未有操作时间的，即第一次，全都未审，此时提醒所有涉及领导
							if(!hasAlChecked){
								for(TaskLeader taskLeader : task.getLeaders()){
									SystemUserInfor leader = taskLeader.getLeader();
									int leaderId = leader.getPersonId().intValue();
									
									if(leaderId == personId){
										taskList.add(task);
										warningList.add("[<font color=#0DE8F5><b>"+task.getOperator().getPerson().getDepartment().getOrganizeName()+"-"+task.getOperator().getPerson().getPersonName()+"</b></font><font color=red>已提交工作完成报告，请审批</font>]");
										
										/**********类型为3，返回到app端**********/
										appTaskList.add(task);
										appTypeList.add(3);
									}
								}
							}
							
							//第二次及以后时，如果有此状态的、已完成的、未通过的，则表示是第二次或者更多的领导审核层，则可以通知
							boolean canWarn = false;
							for(SuperviseReport report : reportList){
								if(task.getStatus() != SuperviseInfor.Task_Status_Done && report.getReportType() == SuperviseReport.Report_Type_Leader && report.getIsDone() == 1 && report.getIsPassed() == 0 && report.getIsDeleted() == 0){
									canWarn = true;
									break;
								}
							}
							if(canWarn){
								for(TaskLeader taskLeader : task.getLeaders()){
									SystemUserInfor leader = taskLeader.getLeader();
									int leaderId = leader.getPersonId().intValue();
									
									if(leaderId == personId){
										taskList.add(task);
										warningList.add("[<font color=#0DE8F5><b>"+task.getOperator().getPerson().getDepartment().getOrganizeName()+"-"+task.getOperator().getPerson().getPersonName()+"</b></font><font color=red>已提交工作完成报告，请审批</font>]");
										
										/**********类型为3，返回到app端**********/
										appTaskList.add(task);
										appTypeList.add(3);
									}
								}
							}
						}
					}else if(status == SuperviseInfor.Task_Status_Delay){
						//领导审核不通过，助理设置延迟时间
						if(task.getCreater().getPersonId() == personId && (task.getDelayDate() == null || task.getDelayDate2() == null || task.getDelayDate3() == null)){
							taskList.add(task);
							warningList.add("[<font color=red>领导审核未通过，请设置延迟日期</font>]");
						}
						//提示部门负责人，延迟工作设定中，请通知行政秘书已确认的延迟时间
						if(task.getManager().getPersonId().intValue() == personId && (task.getDelayDate() == null || task.getDelayDate2() == null || task.getDelayDate3() == null)){
							taskList.add(task);
							warningList.add("[<font color=red>延迟工作设定中，请通知行政秘书已确认的延迟时间</font>]");
						}
					}
				}
				
				
			}
			
			
			JSONObject jsonObj = new JSONObject();
			JSONConvert convert = new JSONConvert();
			List awareObject = new ArrayList();
			
			if(isApp){
				/**********返回到app端的list**********/
				//转为vo模型
				List<MSuperviseInforVo> vos = new ArrayList<MSuperviseInforVo>();
				for(Iterator it = appTaskList.iterator();it.hasNext();){
					SuperviseInfor infor = (SuperviseInfor)it.next();
					
					MSuperviseInforVo vo = new MSuperviseInforVo();
					vo.setTaskId(infor.getTaskId());
					vo.setTaskName(infor.getTaskName());
					vo.setOperatorName(infor.getOperator().getPerson().getPersonName());
					vo.setOperatorPhoto(infor.getOperator().getPerson().getPhotoAttachment());
					vo.setCreateDate(infor.getCreateDate() != null ? String.valueOf(infor.getCreateDate()) : "");
					
					vos.add(vo);
				}
//				JSONArray appJsonArray = new JSONArray();
//				appJsonArray = convert.modelCollect2JSONArray(appTaskList, awareObject);
				jsonObj.put("_AppTasks", vos);
				jsonObj.put("_AppTypes", appTypeList);
			}else {
				JSONArray jsonArray = new JSONArray();
				//通知Convert，哪些关联对象需要获取
				
				//awareObject.add("flowDefinition.fileRole");
				//awareObject.add("department");
				awareObject.add("taskCategory");
				
				jsonArray = convert.modelCollect2JSONArray(taskList, awareObject);
				jsonObj.put("_Tasks", jsonArray);
				jsonObj.put("_WarningStrs", warningList);
				
				
				
				
				//修改后的提醒
				JSONArray jsonArrayChanged = new JSONArray();
				jsonArrayChanged = convert.modelCollect2JSONArray(changedTaskList, awareObject);
				jsonObj.put("_ChangedTasks", jsonArrayChanged);
				jsonObj.put("_ChangedWarningStrs", changedWarningList);
			}
			
			
			//设置字符编码
			response.setContentType(CoreConstant.CONTENT_TYPE);
			response.getWriter().print(jsonObj);
			
			//System.out.println("执行完成----------" + tmpsf.format(new java.util.Date()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
