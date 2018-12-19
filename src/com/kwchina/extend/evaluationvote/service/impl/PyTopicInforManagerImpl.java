package com.kwchina.extend.evaluationvote.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.base.dao.RoleInforDAO;
import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.extend.evaluationvote.dao.PyTopicInforDAO;
import com.kwchina.extend.evaluationvote.entity.PyTopicInfor;
import com.kwchina.extend.evaluationvote.service.PyTopicInforManager;

@Service
public class PyTopicInforManagerImpl extends BasicManagerImpl<PyTopicInfor> implements PyTopicInforManager {
	private PyTopicInforDAO pyTopicInforDAO;

	@Autowired
	public void setPyTopicInforDAO(PyTopicInforDAO pyTopicInforDAO) {
		this.pyTopicInforDAO = pyTopicInforDAO;
		super.setDao(pyTopicInforDAO);
		//此句重要！否则DAO在超类里就不会注入，返回null的错误。
	}


}