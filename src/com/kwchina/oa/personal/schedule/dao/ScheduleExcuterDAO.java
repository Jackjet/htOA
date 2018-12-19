package com.kwchina.oa.personal.schedule.dao;

import java.util.List;

import com.kwchina.core.common.dao.BasicDao;
import com.kwchina.oa.personal.schedule.entity.ScheduleExcuter;

public interface ScheduleExcuterDAO extends BasicDao<ScheduleExcuter> {
	
	//按工作Id获得工作执行人员
	public List getExcuterByScheduleId (int scheduleId);
	
	//按执行人Id获得工作执行人员
	public List getExcuterByPersonId (int personId); 
	
}