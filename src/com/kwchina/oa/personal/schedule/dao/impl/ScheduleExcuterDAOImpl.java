package com.kwchina.oa.personal.schedule.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kwchina.core.common.dao.BasicDaoImpl;
import com.kwchina.oa.personal.schedule.dao.ScheduleExcuterDAO;
import com.kwchina.oa.personal.schedule.entity.ScheduleExcuter;
@Repository
public class ScheduleExcuterDAOImpl extends BasicDaoImpl<ScheduleExcuter> implements ScheduleExcuterDAO {
	
	// 按工作Id获得工作执行人员
	public List getExcuterByScheduleId (int scheduleId) {
		String hql = "from ScheduleExcuter scheduleExcuter where scheduleExcuter.jobInfor.scheduleId = ?";
		List list = getResultByQueryString(hql);
		
		return list;
	}
	
	// 按执行人Id获得工作执行人员
	public List getExcuterByPersonId (int personId) {
		String hql = "from ScheduleExcuter scheduleExcuter where scheduleExcuter.executor.personId = ?";
		List list = getResultByQueryString(hql);
		
		return list;
	}
	
}