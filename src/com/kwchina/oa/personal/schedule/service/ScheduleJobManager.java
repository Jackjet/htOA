package com.kwchina.oa.personal.schedule.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.common.page.Pages;
import com.kwchina.core.common.service.BasicManager;
import com.kwchina.oa.personal.message.entity.MessageInfor;
import com.kwchina.oa.personal.schedule.entity.ScheduleJobInfor;
import com.kwchina.oa.personal.schedule.vo.ScheduleJobInforVo;



public interface ScheduleJobManager extends BasicManager{
	
	// 转化ScheduleJobInfor为ScheduleJobInforVO
	public ScheduleJobInforVo transPOToVO(ScheduleJobInfor Schedule);
		
	// 判断权限
	public boolean judgeRight(ScheduleJobInfor scheduleJobInfor,SystemUserInfor user,int rightType);
	
	// 编辑日程
	public ScheduleJobInfor getEditSchedule(int scheduleId);
	
	// 获取任务督办的语句
	public String taskCondition(HttpServletRequest request, String method, Pages pages);
	
	// 删除日程
	public void deleteSchedule(int scheduleId, SystemUserInfor user);
	
	// 判断是否能删除或修改消息
	public boolean canDeleteOrEditSchedule(SystemUserInfor user,ScheduleJobInfor schedule);
	
}