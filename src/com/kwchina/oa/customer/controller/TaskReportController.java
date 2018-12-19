package com.kwchina.oa.customer.controller;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.sql.Date;
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
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

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
import com.kwchina.oa.customer.entity.TaskInfor;
import com.kwchina.oa.customer.entity.TaskReport;
import com.kwchina.oa.customer.service.ProjectCategoryManager;
import com.kwchina.oa.customer.service.ProjectInforManager;
import com.kwchina.oa.customer.service.TaskInforManager;
import com.kwchina.oa.customer.service.TaskReportManager;
import com.kwchina.oa.customer.vo.ActivityInforVo;
import com.kwchina.oa.customer.vo.TaskInforVo;
import com.kwchina.oa.customer.vo.TaskReportVo;
import com.kwchina.oa.document.vo.DocumentCategoryVo;
import com.kwchina.oa.document.vo.DocumentInforVo;
import com.kwchina.oa.util.SysCommonMethod;

@Controller
@RequestMapping("/customer/taskReport.do")
public class TaskReportController extends BasicController {
	private static Log log = LogFactory.getLog(ProjectInforController.class);

	@Resource
	private TaskInforManager taskInforManager;

	@Resource
	private SystemUserManager systemUserManager;

	@Resource
	private OrganizeManager organizeManager;

	@Resource
	private ProjectInforManager projectInforManager;

	@Resource
	private TaskReportManager taskReportManager;

	/***************************************************************************
	 * 增加任务报告
	 * 
	 * @param taskInforVo
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=editReport")
	public String editReport(@ModelAttribute("taskReportVo")
	TaskReportVo taskReportVo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering 'ProjectInforAction.editReport' method...");
		}
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);

		Integer taskId = taskReportVo.getTaskId();

		// 判断用户是否为本任务的执行人
		int isExecutor = 0; // 0-不是 1-是
		TaskInfor taskInfor = (TaskInfor) this.taskInforManager.get(taskId);
		request.setAttribute("_TaskName", taskInfor.getTaskName());
		Set executors = taskInfor.getExecutorInfors();
		Iterator itr = executors.iterator();
		while (itr.hasNext()) {
			ExecutorInfor executor = (ExecutorInfor) itr.next();
			if (systemUser.getPersonId().intValue() == executor.getExecutor().getPersonId().intValue()) {
				isExecutor = 1;
			}
		}
		if (systemUser.getUserType() == 1)
			isExecutor = 1;
		if (isExecutor == 0) {
			request.setAttribute("_ErrorMessage", "对不起，您不是本任务的执行人，无权添加任务报告!");
			return "/common/error";
		}

		Integer reportId = taskReportVo.getReportId();
		if (reportId != null && reportId != 0) {
			TaskReport taskReport = new TaskReport();
			taskReport = (TaskReport) this.taskReportManager.get(reportId);

			taskReportVo.setContent(taskReport.getContent());

			// 对附件信息进行处理
			String attachmentFile = taskReport.getAttachment();
			if (attachmentFile != null && !attachmentFile.equals("")) {
				String[][] attachment = processFile(attachmentFile);
				request.setAttribute("_Attachment_Names", attachment[1]);
				request.setAttribute("_Attachments", attachment[0]);
			}
		}

		return "editTaskReport";
	}

	/***************************************************************************
	 * 保存任务报告信息
	 * 
	 * @param taskInforVo
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=saveReport")
	public String save(@ModelAttribute("taskReportVo")
	TaskReportVo taskReportVo, Model model, HttpServletRequest request, HttpServletResponse response, DefaultMultipartHttpServletRequest multipartRequest) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering 'ProjectInforAction.saveReport' method...");
		}

		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);

		TaskReport taskReport = new TaskReport();
		Integer reportId = taskReportVo.getReportId();
		String oldFiles = "";
		if (reportId != null && reportId != 0) {
			this.taskReportManager.get(reportId);

			// 修改信息时,对附件进行修改
			String filePaths = taskReport.getAttachment();
			oldFiles = deleteOldFile(request, filePaths, "attatchmentArray");
		} else {
			// 新添加报告，则加入提交时间
			Date updateDate = new Date(System.currentTimeMillis());
			taskReport.setUpdateDate(updateDate);
		}

		// 上传附件
		String attachment = this.uploadAttachment(multipartRequest, "taskReport");

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
		taskReport.setAttachment(attachment);

		taskReport.setContent(taskReportVo.getContent());

		// 添加所属任务
		Integer taskId = taskReportVo.getTaskId();
		TaskInfor taskInfor = (TaskInfor) this.taskInforManager.get(taskId);
		taskReport.setTaskInfor(taskInfor);

		// 添加执行人
		taskReport.setPerson(systemUser);

		this.taskReportManager.save(taskReport);

		/**
		 * 该语句不放在编辑页面的原因: 若在编辑页面提交数据后立即执行window.close()操作, 则后台无法取到编辑页面的数据.
		 * (此情况仅在页面包含附件操作时存在)
		 */
		PrintWriter out = response.getWriter();
		out.print("<script language='javascript'>");
		out.print("window.returnValue = 'Y';");
		out.print("window.close();");
		out.print("</script>");
		
		return null;
	}
	
	
	@RequestMapping(params = "method=deleteReport")
	public String deleteReport(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering 'ProjectInforAction.deleteReport' method...");
		}
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		Integer reportId = Integer.parseInt(request.getParameter("reportId"));
		Integer taskId = 0;
		TaskReport taskReport = new TaskReport();
		if(reportId != null&&reportId != 0){
			taskReport = (TaskReport) this.taskReportManager.get(reportId);
			taskId = taskReport.getTaskInfor().getTaskId();
			//首先判断用户是否为报告提交人
			if(systemUser.getPersonId().intValue()==taskReport.getPerson().getPersonId().intValue()){
				//删除附件
				String filePaths = taskReport.getAttachment();
				deleteFiles(filePaths);
				
				this.taskReportManager.remove(taskReport);
			}
		}
		return "redirect:taskInfor.do?method=view&taskId="+taskId;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
