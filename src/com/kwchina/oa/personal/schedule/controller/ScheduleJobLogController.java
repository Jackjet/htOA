package com.kwchina.oa.personal.schedule.controller;

import java.sql.Timestamp;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.common.controller.BasicController;
import com.kwchina.core.util.File;
import com.kwchina.oa.personal.schedule.entity.ScheduleJobInfor;
import com.kwchina.oa.personal.schedule.entity.ScheduleJobLog;
import com.kwchina.oa.personal.schedule.service.ScheduleJobLogManager;
import com.kwchina.oa.personal.schedule.service.ScheduleJobManager;
import com.kwchina.oa.personal.schedule.vo.ScheduleJobLogVo;
import com.kwchina.oa.util.SysCommonMethod;

@Controller
@RequestMapping(value = "/personal/scheduleJobLog.do")
public class ScheduleJobLogController extends BasicController {

	@Resource
	private ScheduleJobLogManager scheduleJobLogManager;

	@Resource
	private ScheduleJobManager scheduleJobManager;

	/**
	 * 写工作日志
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=edit")
	public String edit(@ModelAttribute("scheduleJobLogVo")
			ScheduleJobLogVo scheduleJobLogVo, Model model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		//int scheduleId = Integer.parseInt(request.getParameter("scheduleId"));
		int scheduleId = scheduleJobLogVo.getScheduleId();
		// 取出要写日志的日程
		ScheduleJobInfor scheduleJobInfor = (ScheduleJobInfor) this.scheduleJobManager
				.get(scheduleId);
		model.addAttribute("_Schedule", scheduleJobInfor);
		
		String documentAttachment = scheduleJobInfor.getAttachment();
		if (documentAttachment != null && !documentAttachment.equals("")) {
			String[][] attachment = processFile(documentAttachment);
			request.setAttribute("_Attachment_Names", attachment[1]);
			request.setAttribute("_Attachments", attachment[0]);
		}
		
		int logId = scheduleJobLogVo.getLogId();
		ScheduleJobLog scheduleJobLog = new ScheduleJobLog();
		// 附件信息
		if (logId !=0) {
			scheduleJobLog = (ScheduleJobLog)this.scheduleJobLogManager.get(logId);
			
			BeanUtils.copyProperties(scheduleJobLogVo, scheduleJobLog);
		
			String attachmentFile = scheduleJobLog.getAttachment();
			if (attachmentFile != null && (!attachmentFile.equals(""))) {
				String[] arrayFiles = attachmentFile.split("\\|");
				scheduleJobLogVo.setAttatchmentArray(arrayFiles);

				String[] attachmentNames = new String[arrayFiles.length];
				for (int k = 0; k < arrayFiles.length; k++) {
					attachmentNames[k] = File.getFileName(arrayFiles[k]);
				}
				request.setAttribute("_AttachmentLog_Names", attachmentNames);
			}
		}
		// 查看回复的信息
		//List list = this.scheduleJobLogManager.getScheduleJobLog(scheduleId);
		//request.setAttribute("_LogList", list);

		return "schedule/addJobLog";
	}

	/**
	 * 保存日志信息
	 * @param messageInforVo
	 * @param model
	 * @param request
	 * @param response
	 * @param multipartRequest
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=save")
	public String save(@ModelAttribute("scheduleJobLogVo")
	ScheduleJobLogVo scheduleJobLogVo, Model model, HttpServletRequest request,
			HttpServletResponse response,
			DefaultMultipartHttpServletRequest multipartRequest)
			throws Exception {
		SystemUserInfor user = SysCommonMethod.getSystemUser(request);
		
		int logId = scheduleJobLogVo.getLogId();
		int scheduleType = Integer.parseInt(request.getParameter("scheduleType"));
		int scheduleId = scheduleJobLogVo.getScheduleId();
		String logContent = scheduleJobLogVo.getLogContent();
		// 如果日志内容不为空，则添加日志内容
		if (logContent != null && !logContent.equals("")) {
			ScheduleJobLog jobLog = new ScheduleJobLog();
			ScheduleJobInfor schsdule = new ScheduleJobInfor();
			schsdule = (ScheduleJobInfor) this.scheduleJobManager.get(scheduleId);

			
			String oldFiles = "";
			if (logId != 0) {
				jobLog = (ScheduleJobLog) this.scheduleJobLogManager
						.get(logId);

				// 修改文档信息时，对文档附件信息进行修改
				String filePaths = jobLog.getAttachment();
				oldFiles = deleteOldFile(request, filePaths, "attatchmentArray");

			} else {
				SystemUserInfor preUser = (SystemUserInfor) SysCommonMethod
						.getSystemUser(request);
				jobLog.setExecutor(preUser);

				long time = System.currentTimeMillis();
				Timestamp timeNow = new Timestamp(time);
				jobLog.setWriteTime(timeNow);

			}

			try {
				// 上传附件
				String attachment = this.uploadAttachment(multipartRequest,
						"jobLog");

				BeanUtils.copyProperties(jobLog, scheduleJobLogVo);
				if (attachment == null || attachment.equals("")) {
					attachment = oldFiles;
				} else {
					if (oldFiles == null || oldFiles.equals("")) {

					} else {
						attachment = attachment + "|" + oldFiles;
					}
				}
				jobLog.setAttachment(attachment);
				if (scheduleJobLogVo.getLogId() == null
						|| scheduleJobLogVo.getLogId() == 0) {
					// 设置发送时间
					jobLog.setWriteTime(new Timestamp(System
							.currentTimeMillis()));
				}
			} catch (Exception ex) {
				model.addAttribute("_ErrorMessage","添加或修改该信息发生错误！<br>错误信息如下:<br>" + ex.toString());
				return "/common/error";
			}

			// 回复时间
			long time = System.currentTimeMillis();
			Timestamp writeTime = new Timestamp(time);

			// scheduleJobLog.setAttachment(attachment);
			//jobLog.setJobInfor(schsdule);
			jobLog.setWriteTime(writeTime);
			jobLog.setJobInfor(schsdule);
			jobLog.setLogContent(logContent);
			jobLog.setExecutor(user);
			this.scheduleJobLogManager.save(jobLog);

		}

		return "redirect:personalJobInfor.do?method=viewCalendar&rowId="+scheduleId+"&scheduleType="+scheduleType;
	}

}
