package com.kwchina.extend.evaluationvote.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.extend.evaluationvote.dao.PyVoteItemInforDAO;
import com.kwchina.extend.evaluationvote.entity.PyVoteItemInfor;
import com.kwchina.extend.evaluationvote.service.PyVoteItemInforManager;

@Service
public class PyVoteItemInforManagerImpl extends BasicManagerImpl<PyVoteItemInfor> implements PyVoteItemInforManager {
	
	private PyVoteItemInforDAO pyVoteItemInforDAO;
	
	@Autowired
	public void setPyVoteItemInforDAO(PyVoteItemInforDAO pyVoteItemInforDAO) {
		this.pyVoteItemInforDAO = pyVoteItemInforDAO;
		super.setDao(pyVoteItemInforDAO);
		
	}
	
	

}