package com.kwchina.extend.supervise.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.extend.supervise.dao.TaskLogDAO;
import com.kwchina.extend.supervise.entity.TaskLog;
import com.kwchina.extend.supervise.service.TaskLogManager;

@Service
public class TaskLogManagerImpl extends BasicManagerImpl<TaskLog> implements TaskLogManager {

	private TaskLogDAO taskLogDAO;


	//注入的方法
	@Autowired
	public void setTaskLogDAO(TaskLogDAO taskLogDAO) {
		this.taskLogDAO = taskLogDAO;
		super.setDao(taskLogDAO);
	}
	


}
