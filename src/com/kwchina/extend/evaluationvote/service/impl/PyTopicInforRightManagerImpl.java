

package com.kwchina.extend.evaluationvote.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.extend.evaluationvote.dao.PyTopicInforRightDAO;
import com.kwchina.extend.evaluationvote.entity.PyTopicInforRight;
import com.kwchina.extend.evaluationvote.service.PyTopicInforRightManager;

@Service
public class PyTopicInforRightManagerImpl extends BasicManagerImpl<PyTopicInforRight> implements PyTopicInforRightManager {
	private PyTopicInforRightDAO pyTopicInforRightDAO;

	@Autowired

	public void setPyTopicInforRightDAO(PyTopicInforRightDAO pyTopicInforRightDAO) {
		this.pyTopicInforRightDAO = pyTopicInforRightDAO;
		super.setDao(pyTopicInforRightDAO);
	}
	
	


}