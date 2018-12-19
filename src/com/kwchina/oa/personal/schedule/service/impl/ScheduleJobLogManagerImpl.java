package com.kwchina.oa.personal.schedule.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.personal.schedule.dao.ScheduleJobLogDAO;
import com.kwchina.oa.personal.schedule.entity.ScheduleJobLog;
import com.kwchina.oa.personal.schedule.service.ScheduleJobLogManager;
@Service
public class ScheduleJobLogManagerImpl extends BasicManagerImpl<ScheduleJobLog> implements ScheduleJobLogManager{
	
	private ScheduleJobLogDAO scheduleJobLogDAO;
	 
    //注入的方法
	@Autowired
    public void setScheduleJobLogDAO(ScheduleJobLogDAO scheduleJobLogDAO){
		this.scheduleJobLogDAO = scheduleJobLogDAO;
        super.setDao(scheduleJobLogDAO); 
    }
     
 	public List getScheduleJobLog(int scheduleId) {
		List list = this.scheduleJobLogDAO.getScheduleJobLog(scheduleId);
		return list;
	}
     
}