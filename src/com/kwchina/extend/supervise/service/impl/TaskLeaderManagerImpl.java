package com.kwchina.extend.supervise.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.extend.supervise.dao.TaskLeaderDAO;
import com.kwchina.extend.supervise.entity.TaskLeader;
import com.kwchina.extend.supervise.service.TaskLeaderManager;

@Service
public class TaskLeaderManagerImpl extends BasicManagerImpl<TaskLeader> implements TaskLeaderManager {

	private TaskLeaderDAO taskLeaderDAO;


	//注入的方法
	@Autowired
	public void setTaskLeaderDAO(TaskLeaderDAO taskLeaderDAO) {
		this.taskLeaderDAO = taskLeaderDAO;
		super.setDao(taskLeaderDAO);
	}
	


}
