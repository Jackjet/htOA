package com.kwchina.oa.personal.schedule.service;

import java.util.List;

import com.kwchina.core.common.service.BasicManager;

public interface ScheduleJobLogManager extends BasicManager{
	
	//按scheduleId查找日志
	public List getScheduleJobLog(int scheduleId);
	
}