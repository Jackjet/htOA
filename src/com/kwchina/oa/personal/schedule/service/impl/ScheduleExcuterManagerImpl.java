package com.kwchina.oa.personal.schedule.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.personal.schedule.dao.ScheduleExcuterDAO;
import com.kwchina.oa.personal.schedule.dao.ScheduleJobDAO;
import com.kwchina.oa.personal.schedule.entity.ScheduleExcuter;
import com.kwchina.oa.personal.schedule.entity.ScheduleJobInfor;
import com.kwchina.oa.personal.schedule.service.ScheduleExcuterManager;


@Service
public class ScheduleExcuterManagerImpl extends BasicManagerImpl<ScheduleExcuter> implements ScheduleExcuterManager{
	
	private ScheduleExcuterDAO scheduleExcuterDAO;
	private ScheduleJobDAO scheduleJobDAO;
	 
     //注入的方法
	
     public void setScheduleExcuterDAO(ScheduleExcuterDAO scheduleExcuterDAO){
    	 this.scheduleExcuterDAO = scheduleExcuterDAO;
         super.setDao(scheduleExcuterDAO); 
     }
     
     
 	public void setScheduleJobDAO(ScheduleJobDAO scheduleJobDAO) {
 		this.scheduleJobDAO = scheduleJobDAO;
 		super.setDao(scheduleJobDAO);
 	}
     
     // 按工作Id获得工作执行人员
 	public List getExcuterByScheduleId (int scheduleId) {
 		return this.scheduleExcuterDAO.getExcuterByScheduleId(scheduleId);
 	}
    
 	// 按执行人Id获得工作执行人员
	public List getExcuterByPersonId (int personId) {
		return this.scheduleExcuterDAO.getExcuterByPersonId(personId);
	}
 	
	public ScheduleJobInfor seeScheduleExcuter(int scheduleId) {
		ScheduleJobInfor schedule = this.scheduleJobDAO.get(scheduleId);
		return schedule;
	}
}