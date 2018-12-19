package com.kwchina.extend.supervise.controller;

import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.ApproveSentenceManager;
import com.kwchina.core.util.DateHelper;
import com.kwchina.core.util.string.StringUtil;
import com.kwchina.extend.supervise.entity.SuperviseInfor;
import com.kwchina.extend.supervise.entity.SuperviseReport;
import com.kwchina.extend.supervise.entity.TaskLeader;
import com.kwchina.extend.supervise.entity.TaskLog;
import com.kwchina.extend.supervise.service.SuperviseInforManager;
import com.kwchina.extend.supervise.service.SuperviseReportManager;
import com.kwchina.extend.supervise.service.TaskLeaderManager;
import com.kwchina.extend.supervise.service.TaskLogManager;
import com.kwchina.extend.supervise.vo.SuperviseReportVo;
import com.kwchina.extend.supervise.vo.TaskLeaderVo;
import com.kwchina.oa.customer.service.TaskReportManager;
import com.kwchina.oa.util.SysCommonMethod;
import com.kwchina.oa.workflow.entity.FlowDefinition;
import com.kwchina.oa.workflow.entity.FlowInstanceInfor;
import com.kwchina.oa.workflow.entity.InstanceCheckInfor;
import com.kwchina.oa.workflow.entity.InstanceLayerInfor;
import com.kwchina.oa.workflow.service.FlowCheckInforManager;
import com.kwchina.oa.workflow.service.FlowInstanceManager;
import com.kwchina.oa.workflow.service.ModifyInforManager;
import com.kwchina.oa.workflow.vo.InstanceCheckInforVo;
import com.kwchina.sms.entity.SMSMessagesToSend;
//import com.kwchina.sms.service.SmsManager;

@Controller
@RequestMapping("/supervise/superviseReport.do")
public class SuperviseReportController extends TaskBaseController {
	
	@Resource
	private SuperviseInforManager superviseInforManager;
	
	@Resource
	private SuperviseReportManager superviseReportManager;
	
	@Resource
	private TaskLogManager taskLogManager;
	
	@Resource
	private TaskLeaderManager taskLeaderManager;
	
	
	/**
	 * 编辑进度报告
	 * @param request
	 * @param response
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=edit")
	public String edit(HttpServletRequest request, HttpServletResponse response, SuperviseReportVo vo) throws Exception {
		
		String taskId = request.getParameter("taskId");
		Integer reportId = vo.getReportId();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		if (taskId != null && !taskId.equals("")) {
			
			SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
			int userId = systemUser.getPersonId().intValue();
			
			SuperviseInfor superviseInfor = (SuperviseInfor)this.superviseInforManager.get(Integer.valueOf(taskId));
			
			//获取督办实例相关信息
			getTaskBaseInfor(request, response, superviseInfor);
			
			//操作时间（新增时，默认当前时间，修改时，实际当时时间）
			Date operateDate = new Date();
			
			if (reportId != null && reportId.intValue() > 0) {
				/** 修改时 */
				SuperviseReport report = (SuperviseReport)this.superviseReportManager.get(reportId);
				
				BeanUtils.copyProperties(vo, report);
				
				//延迟时间
				//request.setAttribute("_DelayDate", report.getDelayDate());
				//进度时间
				operateDate = report.getOperateDate();
				
				//附件
				if (report.getAttachment() != null && report.getAttachment().length() > 0) {
					String attachmentFile = report.getAttachment();
					if (attachmentFile != null && !attachmentFile.equals("")) {
						String[][] attachment = processFile(attachmentFile);
						request.setAttribute("_ReportAttachment_Names", attachment[1]);
						request.setAttribute("_ReportAttachments", attachment[0]);
					}
				}

			}else {
				
				operateDate = Timestamp.valueOf(sf.format(new Date()));
			}
			
			request.setAttribute("_OperateDate", operateDate);
			
		}
		
//		String earliest = sf.format(DateHelper.addDay(new Date(), -30));
//		request.setAttribute("_LastTime", earliest);
		
		//关键词
		String flag = "";
		if(vo.getReportType() == 1){
			flag = "进度报告";
		}
		if(vo.getReportType() == 2){
			flag = "工作完成报告";
		}
		request.setAttribute("_ReportType", flag);
		request.setAttribute("_ReportTypeCode", vo.getReportType());
		
		//判断是否是延迟后提交的完成报告
		String tag = request.getParameter("tag");
		request.setAttribute("_Tag", tag);
		
		return "editReport";
	}
	
	/**
	 * 保存进度报告
	 * @param request
	 * @param response
	 * @param vo
	 * @param multipartRequest
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=save")
	public String save(HttpServletRequest request, HttpServletResponse response, 
			SuperviseReportVo vo, DefaultMultipartHttpServletRequest multipartRequest) throws Exception {
		
		Integer reportId = vo.getReportId();
		Integer taskId = vo.getTaskId();
		SystemUserInfor userInfor = SysCommonMethod.getSystemUser(request);
		
		SuperviseInfor taskInfor = new SuperviseInfor();
		SuperviseReport report = new SuperviseReport();
		
		if (taskId != null && taskId.intValue() > 0) {
			taskInfor = (SuperviseInfor)this.superviseInforManager.get(taskId);
		}
		
		//是否是修改，如果是，则保留修改记录
		boolean isEdit = false;
		
		String oldFiles = "";
		if (reportId != null && reportId.intValue() > 0) {
			report = (SuperviseReport)this.superviseReportManager.get(reportId);
			
			//对原附件进行处理
			String filePaths = report.getAttachment();
			oldFiles = deleteOldFile(request, filePaths, "attatchmentArray");
			
			isEdit = true;
		}else {
			//新增时保存创建时间
			report.setCreateDate(new Timestamp(System.currentTimeMillis()));
			
			//保存父report
			Integer parentId = vo.getParentId();
			if(parentId != null && parentId.intValue() > 0){
				SuperviseReport parentReport = (SuperviseReport)this.superviseReportManager.get(parentId);
				report.setParent(parentReport);
			}
			
			//新增时保存isDone,isPass
			report.setIsDone(0);
			report.setIsPassed(0);
			
			//新增完成报告时，改变状态
			if(vo.getReportType() == SuperviseReport.Report_Type_Done){
				taskInfor.setStatus(SuperviseInfor.Task_Status_ToFinish);
			}
		}
		
		//新提交的附件
		String attachment = this.uploadAttachment(multipartRequest, "supervise");
		if (attachment == null || attachment.equals("")) {
			attachment = oldFiles;
		} else {
			if (oldFiles == null || oldFiles.equals("")) {
				// attachment = attachment;
			} else {
				attachment = attachment + "|" + oldFiles;
			}
		}
		
		/***********保存前，判断是否是修改，如果是，则保留修改记录*************/
		if(isEdit){
			//内容
			boolean contentTag = false;
			String oldContent = report.getContent();
			String newContent = vo.getContent();
			if(!oldContent.equals(newContent)){
				contentTag = true;
			}
			
			//操作时间
			boolean operateDateTag = false;
			String oldOperateDate = report.getOperateDate().toString();
			String newOperateDate = request.getParameter("operateDate");
			if(!oldOperateDate.equals(newOperateDate)){
				operateDateTag = true;
			}
			
			//备注
			boolean memoTag = false;
			String oldMemo = report.getMemo();
			String newMemo = vo.getMemo();
			if(!oldMemo.equals(newMemo)){
				memoTag = true;
			}
			
			//附件
			boolean attachmentTag = false;
			String oldAttachment = report.getAttachment();
			String newAttachment = attachment;
			if(!oldAttachment.equals(newAttachment)){
				attachmentTag = true;
			}
			
			if(contentTag || operateDateTag || memoTag || attachmentTag){
				String logContent = "";
				if(contentTag){
					logContent += "报告内容由“"+oldContent+"”改为："+"“"+newContent+"”|";
				}
				if(operateDateTag){
					logContent += "报告时间由“"+oldOperateDate+"”改为："+"“"+newOperateDate+"”|";
				}
				if(memoTag){
					logContent += "备注由“"+oldMemo+"”改为："+"“"+newMemo+"”|";
				}
				if(attachmentTag){
					logContent += "附件由“"+oldAttachment+"”改为："+"“"+newAttachment+"”|";
				}
				
				TaskLog log = new TaskLog(logContent,new Timestamp(System.currentTimeMillis()),report,userInfor);
				this.taskLogManager.save(log);
			}
		}
		/****************************************************/
		
		BeanUtils.copyProperties(report, vo);
		
		
		report.setAttachment(attachment);
		
		//操作时间
		String operateDateStr = request.getParameter("operateDate");
		Timestamp operateDate = null;
		if (operateDateStr != null && operateDateStr.length() > 0) {
			operateDate = Timestamp.valueOf(operateDateStr);
		}
		report.setOperateDate(operateDate);
		
		//提交人
		report.setOperator(userInfor);
		
		//所属督办
		report.setSuperviseInfor(taskInfor);
		
		this.superviseInforManager.save(taskInfor);
		this.superviseReportManager.save(report);
		
		//如果是延迟后提交的完成报告 ，则把之前助理设置的delay意见 的isDone设为1
		String tag = request.getParameter("tag");
		if(tag != null && !tag.equals("")){
			if(tag.equals("re")){
				Set<SuperviseReport> reports = taskInfor.getReports();
				for(SuperviseReport tmpRreport : reports){
					if(tmpRreport.getReportType() == SuperviseReport.Report_Type_Check && tmpRreport.getDelayDate() != null && tmpRreport.getIsDone() == 0 && tmpRreport.getIsDeleted() == 0){
						tmpRreport.setIsDone(1);
						this.superviseReportManager.save(tmpRreport);
					}
				}
			}
		}
		
		
		return "redirect:/supervise/superviseInfor.do?method=viewTask&rowId="+taskId;
			
	}
	
	
	
	/**
	 * 审核进度报告
	 * @param request
	 * @param response
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=checkReport")
	public String checkReport(HttpServletRequest request, HttpServletResponse response, SuperviseReportVo vo) throws Exception {
		
		//String taskId = request.getParameter("taskId");
		Integer reportId = vo.getReportId();
		SuperviseReport superviseReport = new SuperviseReport(); 
		
		if (reportId != null && !reportId.equals("")) {
			
			SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
			int userId = systemUser.getPersonId().intValue();
			
			superviseReport = (SuperviseReport)this.superviseReportManager.get(Integer.valueOf(reportId));
			
			//获取督办实例相关信息
			getTaskBaseInfor(request, response, superviseReport.getSuperviseInfor());
			
			//操作时间
			Date operateDate = new Date();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			operateDate = Timestamp.valueOf(sf.format(new Date()));
			request.setAttribute("_CheckDate", operateDate);
			
			//默认通过
			vo.setIsPassed(1);
			
		}
		
		request.setAttribute("_Report", superviseReport);
		
		return "checkReport";
	}
	
	
	/**
	 * 保存进度审核
	 * @param request
	 * @param response
	 * @param vo
	 * @param multipartRequest
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=saveCheck")
	public String saveCheck(HttpServletRequest request, HttpServletResponse response, 
			SuperviseReportVo vo) throws Exception {//, DefaultMultipartHttpServletRequest multipartRequest
		
		Integer reportId = vo.getReportId();
		SuperviseReport report = new SuperviseReport();
		
		if (reportId != null && reportId.intValue() > 0) {
			report = (SuperviseReport)this.superviseReportManager.get(reportId);
			
			report.setIsPassed(vo.getIsPassed());
			report.setManagerAdvice(vo.getManagerAdvice());
			
			String checkDateStr = request.getParameter("operateDate");
			Timestamp operateDate = null;
			if (checkDateStr != null && checkDateStr.length() > 0) {
				operateDate = Timestamp.valueOf(checkDateStr);
			}else {
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				operateDate = Timestamp.valueOf(sf.format(new Date()));
			}
			report.setCheckDate(operateDate);
			
			//当通过时，不论是第几次，均保存(父report)为“已结束”，即不再有往复
			if(vo.getIsPassed() == 1){
				report.setIsDone(1);
				if(report.getParent() != null){
					SuperviseReport parentReport = report.getParent();
					parentReport.setIsDone(1);

					//其下其他子节点也应该全部已结束
					Set<SuperviseReport> childs = parentReport.getChilds();
					if(childs!=null && !childs.isEmpty()){
						for(SuperviseReport report1:childs){
							report1.setIsDone(1);
						}
					}

					this.superviseReportManager.save(parentReport);
				}
				
				//当完成报告审核通过时，改变状态
				if(vo.getReportType() == SuperviseReport.Report_Type_Done){
					SuperviseInfor task = report.getSuperviseInfor();
					task.setStatus(SuperviseInfor.Task_Status_Check);
					this.superviseInforManager.save(task);
				}
			}
			
			
			
			this.superviseReportManager.save(report);
		}
		
		return "redirect:/supervise/superviseInfor.do?method=viewTask&rowId="+report.getSuperviseInfor().getTaskId();
	}
	
	
	/**
	 * 助理预判完成报告
	 * @param request
	 * @param response
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=judgeReport")
	public String judgeReport(HttpServletRequest request, HttpServletResponse response, SuperviseReportVo vo) throws Exception {
		
		//String taskId = request.getParameter("taskId");
		Integer reportId = vo.getReportId();
		SuperviseReport superviseReport = new SuperviseReport(); 
		
		if (reportId != null && !reportId.equals("")) {
			
			superviseReport = (SuperviseReport)this.superviseReportManager.get(Integer.valueOf(reportId));
			
			//获取督办实例相关信息
			getTaskBaseInfor(request, response, superviseReport.getSuperviseInfor());
			
			//操作时间
			Date operateDate = new Date();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			operateDate = Timestamp.valueOf(sf.format(new Date()));
			request.setAttribute("_JudgeDate", operateDate);
			
		}
		
		request.setAttribute("_Report", superviseReport);
		
		return "judgeReport";
	}
	
	/**
	 * 保存预判审核
	 * @param request
	 * @param response
	 * @param vo
	 * @param multipartRequest
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=saveJudge")
	public String saveJudge(HttpServletRequest request, HttpServletResponse response, 
			SuperviseReportVo vo) throws Exception {//, DefaultMultipartHttpServletRequest multipartRequest
		
		Integer reportId = vo.getReportId();
		SuperviseReport report = new SuperviseReport();
		
		if (reportId != null && reportId.intValue() > 0) {
			report = (SuperviseReport)this.superviseReportManager.get(reportId);
			
			report.setIsJudgePassed(vo.getIsJudgePassed());
			
			String judgeDateStr = request.getParameter("operateDate");
			Timestamp operateDate = null;
			if (judgeDateStr != null && judgeDateStr.length() > 0) {
				operateDate = Timestamp.valueOf(judgeDateStr);
			}else {
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				operateDate = Timestamp.valueOf(sf.format(new Date()));
			}
			report.setJudgeDate(operateDate);
			
			if(report.getCheckDate() == null){
				report.setCheckDate(operateDate);
			}
			
			//无论结果是否通过，都设置isJudge为1
			report.setIsJudged(1);
			
			//当通过时，不论是第几次，均保存为“已判断完成”，即不再有往复
			if(vo.getIsJudgePassed() == 1){
//				report.setIsJudged(1);
//				if(report.getParent() != null){
//					SuperviseReport parentReport = report.getParent();
//					parentReport.setIsJudged(1);
//					this.superviseReportManager.save(parentReport);
//				}
				
				//当通过时，改变状态为领导审核中
				//if(vo.getReportType() == SuperviseReport.Report_Type_Check){
					SuperviseInfor task = report.getSuperviseInfor();
					task.setStatus(SuperviseInfor.Task_Status_LeaderCheck);
					this.superviseInforManager.save(task);
				//}
			}
			
			
			this.superviseReportManager.save(report);
		}
		
		return "redirect:/supervise/superviseInfor.do?method=viewTask&rowId="+report.getSuperviseInfor().getTaskId();
	}
	
	/**
	 * 领导审批打分
	 * @param request
	 * @param response
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=nameScore")
	public String nameScore(HttpServletRequest request, HttpServletResponse response, SuperviseReportVo vo) throws Exception {
		
		//String taskId = request.getParameter("taskId");
		Integer taskId = vo.getTaskId();
		SuperviseInfor task = new SuperviseInfor(); 
		
		if (taskId != null && taskId.intValue() > 0) {
			
			task = (SuperviseInfor)this.superviseInforManager.get(Integer.valueOf(taskId));
			
			//获取督办实例相关信息
			getTaskBaseInfor(request, response, task);
			
			//操作时间
			java.util.Date operateDate = new java.util.Date();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			operateDate = Timestamp.valueOf(sf.format(new java.util.Date()));
			request.setAttribute("_CheckDate", operateDate);
			
		}
		
		
		return "nameScore";
	}
	
	/**
	 * 保存审批打分
	 * @param request
	 * @param response
	 * @param vo
	 * @param multipartRequest
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=saveNameScore")
	public String saveNameScore(HttpServletRequest request, HttpServletResponse response, 
			SuperviseReportVo vo) throws Exception {//, DefaultMultipartHttpServletRequest multipartRequest
		
		Integer taskId = vo.getTaskId();
		SuperviseInfor task = new SuperviseInfor();
		SuperviseReport report = new SuperviseReport();
		
		if (taskId != null && taskId.intValue() > 0) {
			task = (SuperviseInfor)this.superviseInforManager.get(taskId);
			
			report.setIsPassed(vo.getIsPassed());
			report.setManagerAdvice(vo.getManagerAdvice());
			
			String checkDateStr = request.getParameter("operateDate");
			Timestamp operateDate = null;
			if (checkDateStr != null && checkDateStr.length() > 0) {
				operateDate = Timestamp.valueOf(checkDateStr);
			}else {
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				operateDate = Timestamp.valueOf(sf.format(new Date()));
			}
			report.setCheckDate(operateDate);
			report.setOperateDate(operateDate);
			report.setCreateDate(operateDate);
			
			//操作人
			SystemUserInfor userInfor = SysCommonMethod.getSystemUser(request);
			report.setOperator(userInfor);
			
			report.setSuperviseInfor(task);
			
			report.setReportType(vo.getReportType());
			String nameScore ="";
			//保存打分
			String scoreStr = request.getParameter("nameScore");
			if (Integer.valueOf(scoreStr) == 1){
				 nameScore = "符合";
			}else{
				 nameScore = "基本符合";
			}
			if(vo.getIsPassed() == 0){
				nameScore = "不符合";
			}
			if(StringUtil.isNotEmpty(scoreStr)){
				report.setScore(Integer.valueOf(scoreStr));
				task.setNameScore(nameScore);
			}else {
				report.setScore(0);
			}
			
			
			//当通过时，不论是第几次，均保存为“已结束”，即不再有往复
			report.setIsDone(1);
			
			if(vo.getIsPassed() == 1){
				//当完成报告审核通过时，改变状态
				//if(vo.getReportType() == SuperviseReport.Report_Type_Done){
					task.setStatus(SuperviseInfor.Task_Status_Done);
					task.setEndTime(new java.sql.Date(System.currentTimeMillis()));
					//this.superviseInforManager.save(task);
				//}
			}else if(vo.getIsPassed() == 0){

				//不通过时，返回到助理，设置延迟时间
				task.setStatus(SuperviseInfor.Task_Status_Delay);
			}
			
			
			//保存TaskLeader
			Set<TaskLeader> leaders = task.getLeaders();
			for(TaskLeader leader : leaders){
				if(leader.getLeader().getPersonId().intValue() == userInfor.getPersonId().intValue()){
					leader.setOperateDate(operateDate);
					this.taskLeaderManager.save(leader);
				}
			}
			
			this.superviseInforManager.save(task);
			this.superviseReportManager.save(report);
		}
		
		return "redirect:/supervise/superviseInfor.do?method=viewTask&rowId="+report.getSuperviseInfor().getTaskId();
	}
	
	
	
	/**
	 * 行政/党群评价
	 * @param request
	 * @param response
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=checkAdvice")
	public String checkAdvice(HttpServletRequest request, HttpServletResponse response, SuperviseReportVo vo) throws Exception {
		
		//String taskId = request.getParameter("taskId");
		Integer taskId = vo.getTaskId();
		SuperviseInfor task = new SuperviseInfor(); 
		
		if (taskId != null && taskId.intValue() > 0) {
			
			task = (SuperviseInfor)this.superviseInforManager.get(Integer.valueOf(taskId));
			
			//获取督办实例相关信息
			getTaskBaseInfor(request, response, task);
			
			//操作时间
			java.util.Date operateDate = new java.util.Date();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			operateDate = Timestamp.valueOf(sf.format(new java.util.Date()));
			request.setAttribute("_CheckDate", operateDate);
			
		}
		
		
		return "checkAdvice";
	}
	
	/**
	 * 保存督办评价
	 * @param request
	 * @param response
	 * @param vo
	 * @param multipartRequest
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=saveCheckAdvice")
	public String saveCheckAdvice(HttpServletRequest request, HttpServletResponse response, 
			SuperviseReportVo vo) throws Exception {//, DefaultMultipartHttpServletRequest multipartRequest
		
		Integer taskId = vo.getTaskId();
		SuperviseInfor task = new SuperviseInfor();
		SuperviseReport report = new SuperviseReport();
		
		if (taskId != null && taskId.intValue() > 0) {
			task = (SuperviseInfor)this.superviseInforManager.get(taskId);
			
			report.setIsPassed(vo.getIsPassed());
			report.setManagerAdvice(vo.getManagerAdvice());
			
			String checkDateStr = request.getParameter("operateDate");
			Timestamp operateDate = null;
			if (checkDateStr != null && checkDateStr.length() > 0) {
				operateDate = Timestamp.valueOf(checkDateStr);
			}else {
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				operateDate = Timestamp.valueOf(sf.format(new Date()));
			}
			report.setCheckDate(operateDate);
			report.setOperateDate(operateDate);
			report.setCreateDate(operateDate);
			
			//操作人
			SystemUserInfor userInfor = SysCommonMethod.getSystemUser(request);
			report.setOperator(userInfor);
			
			report.setSuperviseInfor(task);
			
			report.setReportType(vo.getReportType());
			
			//保存打分
			String scoreStr = request.getParameter("nameScore");
			report.setScore(Integer.valueOf(scoreStr));
			
			//当通过时，不论是第几次，均保存为“已结束”，即不再有往复
			report.setIsDone(1);
			
			if(vo.getIsPassed() == 1){
				//当完成报告审核通过时，改变状态
				//if(vo.getReportType() == SuperviseReport.Report_Type_Done){
					task.setStatus(SuperviseInfor.Task_Status_Done);
					task.setEndTime(new java.sql.Date(System.currentTimeMillis()));
					//this.superviseInforManager.save(task);
				//}
			}else if(vo.getIsPassed() == 0){
				//不通过时，返回到助理，设置延迟时间
				task.setStatus(SuperviseInfor.Task_Status_Delay);
			}
			
			
			/*//保存TaskLeader
			Set<TaskLeader> leaders = task.getLeaders();
			for(TaskLeader leader : leaders){
				if(leader.getLeader().getPersonId().intValue() == userInfor.getPersonId().intValue()){
					leader.setOperateDate(operateDate);
					this.taskLeaderManager.save(leader);
				}
			}*/
			
			this.superviseInforManager.save(task);
			this.superviseReportManager.save(report);
		}
		
		return "redirect:/supervise/superviseInfor.do?method=viewTask&rowId="+report.getSuperviseInfor().getTaskId();
	}
	
	
	/**
	 * 设置延迟日期
	 * @param request
	 * @param response
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=setDelay")
	public String setDelay(HttpServletRequest request, HttpServletResponse response, SuperviseReportVo vo) throws Exception {
		
		//String taskId = request.getParameter("taskId");
		Integer taskId = vo.getTaskId();
		SuperviseInfor task = new SuperviseInfor(); 
		
		if (taskId != null && taskId.intValue() > 0) {
			
			task = (SuperviseInfor)this.superviseInforManager.get(Integer.valueOf(taskId));
			
			//获取督办实例相关信息
			getTaskBaseInfor(request, response, task);
			
			//操作时间
			java.sql.Date operateDate = null;
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			operateDate = java.sql.Date.valueOf(sf.format(new java.util.Date()));
			request.setAttribute("_DelayDate", operateDate);
			
		}
		
		
		return "setDelay";
	}
	
	/**
	 * 保存延迟日期
	 * @param request
	 * @param response
	 * @param vo
	 * @param multipartRequest
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=saveDelay")
	public String saveDelay(HttpServletRequest request, HttpServletResponse response, 
			SuperviseReportVo vo) throws Exception {//, DefaultMultipartHttpServletRequest multipartRequest
		
		Integer taskId = vo.getTaskId();
		SuperviseInfor task = new SuperviseInfor();
		SuperviseReport report = new SuperviseReport();
		
		if (taskId != null && taskId.intValue() > 0) {
			task = (SuperviseInfor)this.superviseInforManager.get(taskId);
			
			
			String delayDateStr = request.getParameter("delayDate");
			java.sql.Date delayDate = null;
			Timestamp operateDate = null;
			if (delayDateStr != null && delayDateStr.length() > 0) {
				delayDate = java.sql.Date.valueOf(delayDateStr);
			}
			
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			operateDate = Timestamp.valueOf(sf.format(new Date()));
			
			report.setDelayDate(delayDate);
			report.setCreateDate(operateDate);
			
			//操作人
			SystemUserInfor userInfor = SysCommonMethod.getSystemUser(request);
			report.setOperator(userInfor);
			
			report.setSuperviseInfor(task);
			
			report.setReportType(vo.getReportType());
			
			
			//保存时，设置 isDone为0，当责任人接收住，再提交完成报告后，将isDone设为1
			report.setIsDone(0);
			
			//改变状态至提交完成报告
			task.setStatus(SuperviseInfor.Task_Status_ToFinish);
			
			//添加多两次延迟，此处判断
			if(task.getDelayDate() == null){
				task.setDelayDate(delayDate);
			}else if(task.getDelayDate2() == null){
				task.setDelayDate2(delayDate);
			}else if(task.getDelayDate3() == null){
				task.setDelayDate3(delayDate);
			}
			
			
			
			this.superviseInforManager.save(task);
			this.superviseReportManager.save(report);
		}
		
		return "redirect:/supervise/superviseInfor.do?method=viewTask&rowId="+report.getSuperviseInfor().getTaskId();
	}
}