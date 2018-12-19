package com.kwchina.oa.personal.schedule.dao.impl;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.kwchina.core.common.dao.BasicDaoImpl;
import com.kwchina.oa.personal.schedule.dao.ScheduleJobLogDAO;
import com.kwchina.oa.personal.schedule.entity.ScheduleJobLog;

@Repository
public class ScheduleJobLogDAOImpl extends BasicDaoImpl<ScheduleJobLog> implements ScheduleJobLogDAO {
	/*****
	 * 查看日程工作日志
	 * @param scheduleId:被查看日程的主键
	 */
	public List getScheduleJobLog(int scheduleId) {
		String hql = " from ScheduleJobLog scheduleJobLog where scheduleJobLog.jobInfor.scheduleId='"+scheduleId+"'";
		List list = getResultByQueryString(hql);
		return list;
	}	

}