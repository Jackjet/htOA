package com.kwchina.oa.customer.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.customer.dao.ExecutorInforDAO;
import com.kwchina.oa.customer.entity.ExecutorInfor;
import com.kwchina.oa.customer.service.ExecutorInforManager;

@Service
public class ExecutorInforManagerImpl extends BasicManagerImpl<ExecutorInfor> implements ExecutorInforManager{
	
	private ExecutorInforDAO executorInforDAO;

	@Autowired
	public void setExecutorInforDAO(ExecutorInforDAO executorInforDAO) {
		this.executorInforDAO = executorInforDAO;
		super.setDao(executorInforDAO);
	}
	
	
}
