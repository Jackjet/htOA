package com.kwchina.extend.supervise.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.common.controller.BasicController;
import com.kwchina.core.util.DateHelper;
import com.kwchina.extend.supervise.entity.SuperviseInfor;
import com.kwchina.extend.supervise.entity.SuperviseReport;
import com.kwchina.extend.supervise.entity.TaskLeader;
import com.kwchina.oa.submit.util.SubmitConstant;
import com.kwchina.oa.util.SysCommonMethod;
import com.kwchina.oa.workflow.entity.FlowDefinition;
import com.kwchina.oa.workflow.entity.FlowInstanceInfor;

public class TaskBaseController extends BasicController {
	
	
	//获取任务督办相关信息
	public void getTaskBaseInfor(HttpServletRequest request, HttpServletResponse response, SuperviseInfor superviseInfor) throws Exception {
		SystemUserInfor userInfor = SysCommonMethod.getSystemUser(request);
		int personId = userInfor.getPersonId().intValue();
//		int taskId = Integer.parseInt(request.getParameter("rowId"));
//		SuperviseInfor superviseInfor = (SuperviseInfor)this.superviseInforManager.get(taskId);
		request.setAttribute("_SuperviseInfor", superviseInfor);
		
		// 附件信息
		String taskAttachment = superviseInfor.getAttachment();
		if (taskAttachment != null && !taskAttachment.equals("")) {
			String[][] attachment = processFile(taskAttachment);
			request.setAttribute("_Attachment_Names", attachment[1]);
			request.setAttribute("_Attachments", attachment[0]);
		}
		
		//当目前所有进度报告已被部门经理审批，且全部为通过，才能再填写
		boolean canSubmitReport = true;
		boolean canSubmitReportfinal = true;
		//当前时间
		Calendar cal = Calendar.getInstance();
		java.util.Date date = new java.util.Date();
		cal.setTime(date);

		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sfYM = new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat sfM = new SimpleDateFormat("MM");
		SimpleDateFormat sfD = new SimpleDateFormat("dd");
		SimpleDateFormat sfY = new SimpleDateFormat("yyyy");
		int startMonthInt = Integer.valueOf(sfM.format(superviseInfor.getCreateDate()));
		int startDayInt = Integer.valueOf(sfD.format(superviseInfor.getCreateDate()));
		int startYearInt = Integer.valueOf(sfY.format(superviseInfor.getCreateDate()));
		int secondMonthInt = Integer.valueOf(sfM.format(DateHelper.addMonth(superviseInfor.getCreateDate(), 1)));
		int secondDayInt = Integer.valueOf(sfD.format(DateHelper.addMonth(superviseInfor.getCreateDate(), 1)));

		int thisMonth = Integer.valueOf(sfM.format(date));
		int thisDay = Integer.valueOf(sfD.format(date));
		int thisYear = Integer.valueOf(sfY.format(date));
//		String thissday = thisDay + "";
//		System.out.println(thissday);
		//判断superviseInfor的时间，1、下达日在25日之前，25日之前的时间里提示待执行，25日之后如当月没提交过报告，则提示请在本月底提交进度报告
		//                         2、下达日25日之后，下达日到下月1日待执行 下月25日后提示进度报告
		superviseInfor.getCreateDate();
		Set<SuperviseReport> reports = superviseInfor.getReports();
		if(reports.isEmpty()){
			//25日前下达的任务，下月 下下月。。。1号-25号任务不可填写进度报告 （25号后提示并可填写进度）
//			25日后下达的任务，下达日-下月1号之前，可填写进度报告。下月1号-25号任务不可填写进度报告 （25号后提示并可填写进度）

			if(startDayInt<25 && thisDay<25 && thisMonth!=startMonthInt || startDayInt>=25 && thisDay<25 && thisMonth!=startMonthInt ){
				canSubmitReport = false;
			}
		}
		if (thisDay<25){
			canSubmitReport = false;
		}
		Timestamp sreportTime = null;
		for(SuperviseReport report : reports){
//			为进度报告时
			if(report.getReportType() == SuperviseReport.Report_Type_Ing && report.getIsDeleted() == 0){
				if(report.getParent() == null && report.getIsDone() == 0){
					canSubmitReportfinal = false;
					canSubmitReport = false;
					break;
				}
//				int checkMonth =report.getCheckDate().getMonth()+1;
//				int checkDay = report.getCheckDate().getDate();
//				if (checkMonth == thisMonth && checkDay >24){
//					canSubmitReport = false;
//					break;
//				}
			}
			sreportTime = report.getCreateDate();
		}

		if(superviseInfor.getTaskCategory().getCategoryId() == 2
				||superviseInfor.getTaskCategory().getCategoryId() == 3
				||superviseInfor.getTaskCategory().getCategoryId() == 28 ) {
			if (sreportTime != null && !sreportTime.equals("")) {

				int lastmonth = Integer.valueOf(sfM.format(sreportTime));
				int lastyear = Integer.valueOf(sfY.format(sreportTime));
				if (thisYear != lastyear) {
					thisMonth = thisMonth + 12;
				}
				if (thisMonth - lastmonth < 2 && superviseInfor.getReportPeriod().equals("每2月")) {
					canSubmitReport = false;
				}
				if (thisMonth - lastmonth < 3 && superviseInfor.getReportPeriod().equals("每季度")) {
					canSubmitReport = false;
				}
				if (thisMonth - lastmonth < 6 && superviseInfor.getReportPeriod().equals("每半年")) {
					canSubmitReport = false;
				}
				if (thisMonth - lastmonth < 12 && superviseInfor.getReportPeriod().equals("每年")) {
					canSubmitReport = false;
				}
				if (superviseInfor.getReportPeriod().equals("单次")) {
					canSubmitReport = false;
				}
			} else {
				if (thisYear != startYearInt) {
					thisMonth = thisMonth + 12;
				}
				if (thisMonth - startMonthInt < 2 && superviseInfor.getReportPeriod().equals("每2月")) {
					canSubmitReport = false;
				}
				if (thisMonth - startMonthInt < 3 && superviseInfor.getReportPeriod().equals("每季度")) {
					canSubmitReport = false;
				}
				if (thisMonth - startMonthInt < 6 && superviseInfor.getReportPeriod().equals("每半年")) {
					canSubmitReport = false;
				}
				if (thisMonth - startMonthInt < 12 && superviseInfor.getReportPeriod().equals("每年")) {
					canSubmitReport = false;
				}
				if (superviseInfor.getReportPeriod().equals("单次")) {
					canSubmitReport = false;
				}
			}
		}
		request.setAttribute("_CanSubmitReport", canSubmitReport);
		request.setAttribute("_CanSubmitReportfinal", canSubmitReportfinal);

		//是否可提交工作完成报告
		boolean canSubmitFinalReport = false;
		int remainDays = DateHelper.daysBetween(new Date(), superviseInfor.getFinishDate());
//		int remainDays = DateHelper.daysBetween(DateHelper.getDate(DateHelper.getString(new java.util.Date())), superviseInfor.getFinishDate());
		if(remainDays <= 19 && superviseInfor.getStatus() == SuperviseInfor.Task_Status_Processed){
			//是否已有完成报告
			boolean hasFinalReport = false;
			Set<SuperviseReport> reportList = superviseInfor.getReports();

			SuperviseReport finishReport = null;
			for(SuperviseReport report : reportList){
				if(report.getReportType() == SuperviseReport.Report_Type_Done && report.getIsDeleted() == 0){
					finishReport = report;
					hasFinalReport = true;
					break;
				}
			}



			/*SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sfYM = new SimpleDateFormat("yyyy-MM");
			SimpleDateFormat sfM = new SimpleDateFormat("MM");
			SimpleDateFormat sfD = new SimpleDateFormat("dd");
			//如果已经有完成报告，但是计划完成时间修改延后 则按照正常流程重新走（不按延期流程）
			if(finishReport!=null){
				int creMon = Integer.valueOf(sfM.format(finishReport.getCreateDate()));
				int creDay = Integer.valueOf(sfD.format(finishReport.getCreateDate()));

				int finMon = Integer.valueOf(sfM.format(superviseInfor.getFinishDate()));
				int finDay = Integer.valueOf(sfD.format(superviseInfor.getFinishDate()));

				if(finishReport.getCreateDate().getTime()<superviseInfor.getFinishDate().getTime() && (finMon>creMon || finMon==creMon && finDay>creDay)){
					canSubmitFinalReport = true;
				}
			}*/


			if(!hasFinalReport && superviseInfor.getOperator().getPersonId().intValue() == personId){
				canSubmitFinalReport = true;
			}
		}
		
		request.setAttribute("_CanSubmitFianlReport", canSubmitFinalReport);

		
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
		
		request.setAttribute("_CanCheckFinal", canCheckFinal);
		
		
		//是否可选择领导（审核打分）
		boolean canChooseLeaders = false;

		//是否可打分（领导）
		boolean canNameScore = false;
		
		if(superviseInfor.getStatus() == SuperviseInfor.Task_Status_LeaderCheck){
			//首先由助理选择需要打分的领导
			if(superviseInfor.getLeaders() == null || superviseInfor.getLeaders().size() == 0 && superviseInfor.getCreater().getPersonId() == personId && superviseInfor.getIsDeleted() == 0){
				canChooseLeaders = true;
			}
			
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
		request.setAttribute("_CanChooseLeaders", canChooseLeaders);
		request.setAttribute("_CanNameScore", canNameScore);
		
		
		//是否可设置延期
		boolean canDelay = false;
		if(superviseInfor.getStatus() == SuperviseInfor.Task_Status_Delay && (superviseInfor.getDelayDate() == null || superviseInfor.getDelayDate2() == null || superviseInfor.getDelayDate3() == null)){
			//领导审核不通过，助理设置延迟时间
			if(superviseInfor.getCreater().getPersonId() == personId){
				canDelay = true;
			}
		}
		request.setAttribute("_CanDelay", canDelay);
		
		//是否可在延期后再提交完成报告
		boolean canSubmitDelay = false;
		for(SuperviseReport report : reports){
			if(report.getReportType() == SuperviseReport.Report_Type_Check && report.getDelayDate() != null && report.getIsDone() == 0 && report.getIsDeleted() == 0){
				//早于延迟日期的，提醒责任人
				if(superviseInfor.getOperator().getPersonId().intValue() == personId){//new java.util.Date().before(report.getDelayDate()) && 
					canSubmitDelay = true;
				}
			}
		}
		request.setAttribute("_CanSubmitDelay", canSubmitDelay);
			
	}
	
}