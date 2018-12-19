package com.kwchina.extend.evaluationvote.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.extend.evaluationvote.dao.PyOptionInforDAO;
import com.kwchina.extend.evaluationvote.entity.PyOptionInfor;
import com.kwchina.extend.evaluationvote.service.PyOptionInforManager;


@Service
public class PyOptionInforManagerImpl extends BasicManagerImpl<PyOptionInfor> implements PyOptionInforManager {
	
	private PyOptionInforDAO pyOptionInforDAO;


	@Autowired
	public void setPyOptionInforDAO(PyOptionInforDAO pyOptionInforDAO) {
		this.pyOptionInforDAO = pyOptionInforDAO;
		super.setDao(pyOptionInforDAO);
	}
}