package com.kwchina.oa.personal.schedule.dao;

import java.util.List;

import com.kwchina.core.common.dao.BasicDao;
import com.kwchina.oa.personal.schedule.entity.ScheduleJobLog;

public interface ScheduleJobLogDAO extends BasicDao<ScheduleJobLog> {
	
	//查看回复的信息
	public List getScheduleJobLog(int scheduleId);
		
}