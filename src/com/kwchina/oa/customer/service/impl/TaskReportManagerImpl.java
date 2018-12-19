package com.kwchina.oa.customer.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.customer.dao.TaskReportDAO;
import com.kwchina.oa.customer.entity.TaskReport;
import com.kwchina.oa.customer.service.TaskReportManager;

@Service
public class TaskReportManagerImpl extends BasicManagerImpl<TaskReport> implements TaskReportManager{
	
	private TaskReportDAO taskReportDAO;

	@Autowired
	public void setTaskReportDAO(TaskReportDAO taskReportDAO) {
		this.taskReportDAO = taskReportDAO;
		super.setDao(taskReportDAO);
	}

	
}
