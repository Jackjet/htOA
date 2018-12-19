package com.kwchina.oa.customer.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kwchina.core.base.entity.PersonInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.OrganizeManager;
import com.kwchina.core.base.service.SystemUserManager;
import com.kwchina.core.common.controller.BasicController;
import com.kwchina.core.common.page.PageList;
import com.kwchina.core.common.page.Pages;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.core.util.json.JSONConvert;
import com.kwchina.oa.customer.entity.ExecutorInfor;
import com.kwchina.oa.customer.entity.ProjectCategory;
import com.kwchina.oa.customer.entity.ProjectCategoryRight;
import com.kwchina.oa.customer.entity.TaskInfor;
import com.kwchina.oa.customer.entity.TaskReport;
import com.kwchina.oa.customer.service.ProjectCategoryManager;
import com.kwchina.oa.customer.service.ProjectInforManager;
import com.kwchina.oa.customer.service.TaskInforManager;
import com.kwchina.oa.customer.service.TaskReportManager;
import com.kwchina.oa.customer.vo.TaskInforVo;
import com.kwchina.oa.document.vo.DocumentCategoryVo;
import com.kwchina.oa.util.SysCommonMethod;

@Controller
@RequestMapping("/customer/taskInfor.do")
public class TaskInforController extends BasicController {
	private static Log log = LogFactory.getLog(ProjectInforController.class);

	@Resource
	private TaskInforManager taskInforManager;

	@Resource
	private SystemUserManager systemUserManager;

	@Resource
	private OrganizeManager organizeManager;

	@Resource
	private ProjectCategoryManager projectCategoryManager;

	@Resource
	private ProjectInforManager projectInforManager;

	@Resource
	private TaskReportManager taskReportManager;

	/***************************************************************************
	 * 查询任务信息
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=list")
	public void list(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering 'TaskInforAction.list' method...");
		}

		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		int userId = systemUser.getPersonId().intValue();
		Integer categoryId = Integer.parseInt(request.getParameter("categoryId"));

		// 构造查询语句
		String[] queryString = new String[2];
		queryString[0] = " from TaskInfor taskInfor where 1=1";
		queryString[1] = " select count(taskInfor.taskId) from TaskInfor taskInfor where 1=1";
		String condition = "";

		// 判断权限
		if (systemUser.getUserType() != 1) {

			// 判断项目下的权限
			condition += " and (taskInfor.taskId in (select taskInfora.taskId from TaskInfor taskInfora where taskInfora.projectCategory.projectInfor.creator.personId='"
					+ userId
					+ "') "
					+ "or taskInfor.taskId in(select taskInforb.taskId from TaskInfor taskInforb where taskInforb.projectCategory.projectInfor.manager.personId='"
					+ userId
					+ "')"
					+ " or taskInfor.taskId in (select taskInfora.taskId from TaskInfor taskInfora where taskInfora.projectCategory.projectInfor.creator.personId='"
					+ userId
					+ "' )"
					+ "or taskInfor.taskId in (select taskInforc.taskId from TaskInfor taskInforc where taskInforc.projectCategory.categoryId in (select projectCategoryc.categoryId from ProjectCategory projectCategoryc where projectCategoryc.projectInfor.projectId in (select projectRighta.projectInfor.projectId from ProjectRight projectRighta where projectRighta.user.personId = '"
					+ userId + "')))";

			// 判断分类下的权限
			condition += " or taskInfor.taskId in (select taskInford.taskId from TaskInfor taskInford where taskInford.projectCategory.categoryId in(select projectCategoryRightd.projectCategory.categoryId from ProjectCategoryRight projectCategoryRightd where projectCategoryRightd.user.personId = '"
					+ userId + "'))";

			// 判断任务下的权限
			condition += "or taskInfor.taskId in (select executorInfor.taskInfor.taskId from ExecutorInfor executorInfor where executorInfor.executor.personId = '" + userId + "') or taskInfor.checker.personId = '" + userId
					+ "' or taskInfor.signer.personId = '" + userId + "')";

		}
		// 判断是全部任务还是某个分类下的任务
		if (categoryId != null && categoryId != 0) {

			// 由于防止树形图中项目和分类的主键冲突，树形结构中的分类的Id都是加上项目信息的最大Id的，所以作如下处理
			String hql = "select max(infor.projectId) from ProjectInfor infor";
			List list = this.projectInforManager.getResultByQueryString(hql);
			int maxProjectInforId = 0;
			if (list.size() != 0) {
				maxProjectInforId = Integer.parseInt(list.get(0).toString());
			}
			categoryId -= maxProjectInforId;
			condition += "and taskInfor.projectCategory.categoryId=" + categoryId;
		}

		// 添加查询条件
		queryString[0] += condition;
		queryString[1] += condition;

		// 构造查询条件
		queryString = this.taskInforManager.generateQueryString(queryString, getSearchParams(request));
		String page = request.getParameter("page"); // 当前页
		String rowsNum = request.getParameter("rows"); // 每页显示的行数
		Pages pages = new Pages(request);
		pages.setPage(Integer.valueOf(page));
		pages.setPerPageNum(Integer.valueOf(rowsNum));

		// 获取该用户有权限的所有任务信息
		PageList pl = this.taskInforManager.getResultByQueryString(queryString[0], queryString[1], true, pages);
		List taskInforList = pl.getObjectList();
		List taskInforVos = new ArrayList();
		Iterator itr = taskInforList.iterator();
		while (itr.hasNext()) {
			TaskInfor taskInfor = (TaskInfor) itr.next();
			if (taskInfor.getEndDate() != null) {

				// 判断签核人没有设置过状态的任务是否过期
				Calendar cala = Calendar.getInstance();
				Calendar calb = Calendar.getInstance();
				SimpleDateFormat sim = new SimpleDateFormat("yyyy-mm-dd");
				Date nowDate = new Date(System.currentTimeMillis());
				String nowDateStr = sim.format(nowDate);
				nowDate = sim.parse(nowDateStr);
				cala.setTime(nowDate);
				calb.setTime(taskInfor.getEndDate());
				if (cala.before(calb) && taskInfor.getStatus() == 0) {
					taskInfor.setStatus(3);
					this.taskInforManager.save(taskInfor);
				}
			}
			taskInforVos.add(this.taskInforManager.transPOToVO(taskInfor));
		}

		// 定义返回的数据类型：json，使用了json-lib
		JSONObject jsonObj = new JSONObject();

		// 定义rows
		JSONArray rows = new JSONArray();

		// 存放数据
		JSONConvert convert = new JSONConvert();
		rows = convert.modelCollect2JSONArray(taskInforVos, new ArrayList());
		jsonObj.put("rows", rows); // 返回到前台每页显示的数据(名称必须为rows)

		// 设置字符编码
		response.setContentType(CoreConstant.CONTENT_TYPE);
		response.getWriter().print(jsonObj);
	}

	/***************************************************************************
	 * 批量删除任务
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=delete")
	public void deleteTaskInfor(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering 'TaskInforAction.deleteTaskInfor' method...");
		}

		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		String rowIds = request.getParameter("rowIds");
		if (rowIds != null && rowIds.length() > 0) {
			String[] detleteIds = rowIds.split(",");
			if (detleteIds.length > 0) {
				for (int i = 0; i < detleteIds.length; i++) {
					Integer taskId = Integer.valueOf(detleteIds[i]);
					TaskInfor taskInfor = (TaskInfor) this.taskInforManager.get(taskId);

					// 判断是否有删除任务的权限
					Integer categoryId = taskInfor.getProjectCategory().getCategoryId();
					ProjectCategory categoryInfor = new ProjectCategory();
					categoryInfor = (ProjectCategory) this.projectCategoryManager.get(categoryId);
					// 判断当前用户是否有权限修改任务信息
					if (this.projectCategoryManager.hasRight(categoryInfor, systemUser, ProjectCategoryRight._Right_Create) || systemUser.getUserType() == SystemUserInfor._Type_Admin) {
						this.taskInforManager.remove(taskInfor);
					}
				}
			}
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
	public String edit(@ModelAttribute("taskInforVo")
	TaskInforVo taskInforVo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering 'ProjectInforAction.edit' method...");
		}

		// 获取用户信息
		List users = this.systemUserManager.getAll();
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		Integer taskId = taskInforVo.getTaskId();
		TaskInfor taskInfor = new TaskInfor();
		Integer categoryId = taskInforVo.getCategoryId();
		if (taskId != null && taskId != 0) {
			// taskId不为空，则为修改信息
			taskInfor = (TaskInfor) this.taskInforManager.get(taskId);
			categoryId = taskInfor.getProjectCategory().getCategoryId();
			BeanUtils.copyProperties(taskInforVo, taskInfor);
			taskInforVo.setCheckerId(taskInfor.getChecker().getPersonId());
			taskInforVo.setSignerId(taskInfor.getSigner().getPersonId());
		} else {
			// 如果是新增，则categoryId是从树形结构中传过来的
			// 由于防止树形图中项目和分类的主键冲突，树形结构中的分类的Id都是加上项目信息的最大Id的，所以作如下处理
			String hql = "select max(infor.projectId) from ProjectInfor infor";
			List list = this.projectInforManager.getResultByQueryString(hql);
			int maxProjectInforId = 0;
			if (list.size() != 0) {
				maxProjectInforId = Integer.parseInt(list.get(0).toString());
			}
			categoryId -= maxProjectInforId;
		}

		taskInforVo.setCategoryId(categoryId);
		ProjectCategory categoryInfor = new ProjectCategory();
		categoryInfor = (ProjectCategory) this.projectCategoryManager.get(categoryId);
		// 判断当前用户是否有权限修改任务信息
		if (!(this.projectCategoryManager.hasRight(categoryInfor, systemUser, ProjectCategoryRight._Right_Create) || systemUser.getUserType() == SystemUserInfor._Type_Admin)) {
			request.setAttribute("_ErrorMessage", "对不起,您无权对任务进行编辑,请联系管理员!");
			return "/common/error";
		}

		// 当前时间
		String nowDate = new Date(System.currentTimeMillis()).toString();

		// 执行人
		int[] executorIds = new int[taskInfor.getExecutorInfors().size()]; // 创建项目分类权限

		if (taskInfor != null) {
			int k = 0;
			Set executors = taskInfor.getExecutorInfors();
			for (Iterator it = executors.iterator(); it.hasNext();) {
				ExecutorInfor executor = (ExecutorInfor) it.next();
				int executorId = executor.getExecutor().getPersonId().intValue();
				executorIds[k] = executorId;
				k += 1;
			}
		}
		request.setAttribute("_ExecutorIds", executorIds);

		// 所有项目分类
		List allProjectCategoryInfor = this.projectCategoryManager.getAll();
		request.setAttribute("_AllProjectCategoryInfor", allProjectCategoryInfor);

		// 所有用户
		List allSystemUsers = systemUserManager.findUserOrderByPersonName();
		request.setAttribute("_AllSystemUsers", allSystemUsers);

		// 根据职级获取用户
		List persons = this.systemUserManager.getUserByPosition(PersonInfor._Position_Tag);
		request.setAttribute("_Persons", persons);

		// 获取职级大于一定值的用户
		List otherPersons = this.systemUserManager.getOtherPositionUser(PersonInfor._Position_Tag);
		request.setAttribute("_OtherPersons", otherPersons);

		// 处理日期
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-mm-dd");
		if (taskInfor.getStartDate() != null) {
			taskInforVo.setStartDateStr(sim.format(taskInfor.getStartDate()));
		}
		if (taskInfor.getEndDate() != null) {
			taskInforVo.setEndDateStr(sim.format(taskInfor.getEndDate()));
		}

		// 获取部门信息
		List organizes = this.organizeManager.getDepartments();
		request.setAttribute("_Organizes", organizes);

		request.setAttribute("_NowDate", nowDate);
		request.setAttribute("_Users", users);
		String returnStr = "editTaskInfor";
		if (taskInforVo.getFlag() == 1) {
			returnStr = "editExecutor";
			request.setAttribute("_TaskId", taskId);
		}
		return returnStr;

	}

	/***************************************************************************
	 * 保存任务信息
	 * 
	 * @param request
	 * @param response
	 * @param projectInforInforVo
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=save")
	public void save(HttpServletRequest request, HttpServletResponse response, TaskInforVo taskInforVo, Model model) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering 'ProjectInforAction.save' method...");
		}

		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		TaskInfor taskInfor = new TaskInfor();
		Integer taskId = taskInforVo.getTaskId();
		if (taskId != null && taskId.intValue() != 0) {
			taskInfor = (TaskInfor) this.taskInforManager.get(taskId);
		} else {
			Date updateDate = new Date(System.currentTimeMillis());
			taskInfor.setUpdateDate(updateDate);
		}
		BeanUtils.copyProperties(taskInfor, taskInforVo);

		// 当前时间
		Calendar cal = Calendar.getInstance();
		// 自动获取任务编号
		if (taskId == null || taskId.intValue() == 0) {
			taskInfor.setTaskCode(this.taskInforManager.getAutoTaskCode(cal.get(Calendar.YEAR)));
		}

		// 状态默认为0
		if (taskId == null || taskId.intValue() == 0) {
			taskInfor.setStatus(0);
		}

		// 保存所属项目分类
		if (taskId == null || taskId.intValue() == 0) {
			ProjectCategory categoryInfor = new ProjectCategory();
			categoryInfor = (ProjectCategory) this.projectCategoryManager.get(taskInforVo.getCategoryId());
			taskInfor.setProjectCategory(categoryInfor);
		}

		// 日期转换
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		if (taskInforVo.getUpdateDateStr() != null && !taskInforVo.getUpdateDateStr().equals("")) {
			java.util.Date updateDateUtil = dateFormat.parse(taskInforVo.getUpdateDateStr());
			Date updateDate = new Date(updateDateUtil.getTime());
			taskInfor.setUpdateDate(updateDate);
		}
		if (taskInforVo.getStartDateStr() != null && !taskInforVo.getStartDateStr().equals("")) {
			java.util.Date updateDateUtil = dateFormat.parse(taskInforVo.getStartDateStr());
			Date startDate = new Date(updateDateUtil.getTime());
			taskInfor.setStartDate(startDate);
		}
		if (taskInforVo.getEndDateStr() != null && !taskInforVo.getEndDateStr().equals("")) {
			java.util.Date updateDateUtil = dateFormat.parse(taskInforVo.getEndDateStr());
			Date endDate = new Date(updateDateUtil.getTime());
			taskInfor.setEndDate(endDate);
		}

		// 添加签核人
		if (taskInforVo.getSignerId() != null && taskInforVo.getSignerId() != 0) {
			SystemUserInfor signer = (SystemUserInfor) this.systemUserManager.get(taskInforVo.getSignerId().intValue());
			taskInfor.setSigner(signer);
		}

		// 添加审核人
		if (taskInforVo.getCheckerId() != null && taskInforVo.getCheckerId() != 0) {
			SystemUserInfor checker = (SystemUserInfor) this.systemUserManager.get(taskInforVo.getCheckerId().intValue());
			taskInfor.setChecker(checker);
		}

		// 添加执行人
		Set oldExecutors = taskInfor.getExecutorInfors();
		taskInfor.getExecutorInfors().removeAll(oldExecutors);
		int[] executorIds = taskInforVo.getExecutorIds();
		if (executorIds != null) {
			for (int k = 0; k < executorIds.length; k++) {
				int userId = executorIds[k];
				SystemUserInfor user = (SystemUserInfor) this.systemUserManager.get(userId);
				ExecutorInfor executer = new ExecutorInfor();
				executer.setTaskInfor(taskInfor);
				executer.setExecutor(user);
				taskInfor.getExecutorInfors().add(executer);
			}
		}

		this.taskInforManager.save(taskInfor);
	}

	/***************************************************************************
	 * 保存执行人
	 * 
	 * @param request
	 * @param response
	 * @param taskInforVo
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping(params = "method=saveExecutor")
	public void saveExecutor(HttpServletRequest request, HttpServletResponse response, TaskInforVo taskInforVo, Model model) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering 'ProjectInforAction.saveExecutor' method...");
		}

		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		TaskInfor taskInfor = new TaskInfor();
		Integer taskId = taskInforVo.getTaskId();
		if (taskId != null && taskId.intValue() != 0) {
			taskInfor = (TaskInfor) this.taskInforManager.get(taskId);
		}

		// 添加执行人
		Set oldExecutors = taskInfor.getExecutorInfors();
		taskInfor.getExecutorInfors().removeAll(oldExecutors);
		int[] executorIds = taskInforVo.getExecutorIds();
		if (executorIds != null) {
			for (int k = 0; k < executorIds.length; k++) {
				int userId = executorIds[k];
				SystemUserInfor user = (SystemUserInfor) this.systemUserManager.get(userId);
				ExecutorInfor executer = new ExecutorInfor();
				executer.setTaskInfor(taskInfor);
				executer.setExecutor(user);
				taskInfor.getExecutorInfors().add(executer);
			}
		}

		this.taskInforManager.save(taskInfor);

	}

	/***************************************************************************
	 * 查看任务
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
		if (log.isDebugEnabled()) {
			log.debug("entering 'ProjectInforAction.view' method...");
		}

		// 查看任务
		int taskId = Integer.parseInt(request.getParameter("taskId"));
		TaskInfor taskInfor = (TaskInfor) this.taskInforManager.get(taskId);
		request.setAttribute("_TaskInfor", taskInfor);

		// 查看任务报告
		Set taskReports = taskInfor.getTaskReports();
		request.setAttribute("_TaskReports", taskReports);
		request.setAttribute("_SumReport", taskReports.size());

		// 对附件信息进行处理
		Iterator iter = taskReports.iterator();
		String[][][] attach = new String[taskReports.size()][2][10];
		int i = 0;
		while (iter.hasNext()) {
			TaskReport taskReport = (TaskReport) iter.next();
			String attachmentFile = taskReport.getAttachment();
			if (attachmentFile != null && !attachmentFile.equals("")) {
				String[][] attachment = processFile(attachmentFile);
				attach[i][0] = attachment[1];
				attach[i][1] = attachment[0];
			}
			i++;
		}
		request.setAttribute("_Attachment", attach);
		return "viewTaskInfor";
	}

	/**
	 * 审核人审核
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=checkEdit")
	public String checkEdit(@ModelAttribute("taskInforVo")
	TaskInforVo taskInforVo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering 'ProjectInforAction.checkEdit' method...");
		}

		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		Integer taskId = taskInforVo.getTaskId();
		TaskInfor taskInfor = new TaskInfor();
		if (taskId != null && taskId != 0) {
			// taskId不为空，则为修改信息
			taskInfor = (TaskInfor) this.taskInforManager.get(taskId);
			taskInforVo.setCheckComment(taskInfor.getCheckComment());

			// 判断当前用户是否为审核人
			if (!(taskInfor.getChecker() != null && taskInfor.getChecker().getPersonId().intValue() == systemUser.getPersonId().intValue())) {
				request.setAttribute("_ErrorMessage", "对不起,您不是本任务的审核人!");
				return "/common/error";
			}
		}

		request.setAttribute("_TaskName", taskInfor.getTaskName());

		return "editTaskChecker";
	}

	/***************************************************************************
	 * 保存审核意见
	 * 
	 * @param taskInforVo
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=checkSave")
	public void checkSave(@ModelAttribute("taskInforVo")
	TaskInforVo taskInforVo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering 'ProjectInforAction.checkSave' method...");
		}

		Integer taskId = taskInforVo.getTaskId();
		TaskInfor taskInfor = new TaskInfor();
		if (taskId != null && taskId != 0) {

			// taskId不为空，则为修改信息
			taskInfor = (TaskInfor) this.taskInforManager.get(taskId);
			taskInfor.setCheckComment(taskInforVo.getCheckComment());
			Date checkTime = new Date(System.currentTimeMillis());
			taskInfor.setCheckTime(checkTime);
			this.taskInforManager.save(taskInfor);
		}

	}

	/**
	 * 更改任务状态
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=statuEdit")
	public String statuEdit(@ModelAttribute("taskInforVo")
	TaskInforVo taskInforVo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering 'ProjectInforAction.statuEdit' method...");
		}

		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		Integer taskId = taskInforVo.getTaskId();
		TaskInfor taskInfor = new TaskInfor();
		if (taskId != null && taskId != 0) {
			// taskId不为空，则为修改信息
			taskInfor = (TaskInfor) this.taskInforManager.get(taskId);
			taskInforVo.setCheckComment(taskInfor.getCheckComment());

			// 判断当前用户是否为签核人
			if (taskInfor.getSigner().getPersonId().intValue() != systemUser.getPersonId().intValue()) {
				request.setAttribute("_ErrorMessage", "对不起,您不是本任务的签核人!");
				return "/common/error";
			}
		}

		request.setAttribute("_TaskName", taskInfor.getTaskName());

		return "editTaskStatu";
	}

	/***************************************************************************
	 * 保存任务状态
	 * 
	 * @param taskInforVo
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=statuSave")
	public void statuSave(@ModelAttribute("taskInforVo")
	TaskInforVo taskInforVo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering 'ProjectInforAction.statuSave' method...");
		}

		Integer taskId = taskInforVo.getTaskId();
		TaskInfor taskInfor = new TaskInfor();
		if (taskId != null && taskId != 0) {

			// taskId不为空，则为修改信息
			taskInfor = (TaskInfor) this.taskInforManager.get(taskId);
			taskInfor.setStatus(taskInforVo.getStatus());
			this.taskInforManager.save(taskInfor);
		}

	}
}
