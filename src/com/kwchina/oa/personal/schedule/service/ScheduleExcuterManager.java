package com.kwchina.oa.personal.schedule.service;

import java.util.List;

import com.kwchina.core.common.service.BasicManager;
import com.kwchina.oa.personal.schedule.entity.ScheduleJobInfor;

public interface ScheduleExcuterManager extends BasicManager{
	
	// 按工作Id获得工作执行人员
	public List getExcuterByScheduleId (int scheduleId);
	
	//按工作Id获得工作
	public ScheduleJobInfor seeScheduleExcuter(int scheduleId);
	
	// 按执行人Id获得工作执行人员
	public List getExcuterByPersonId (int personId);
	
	
}