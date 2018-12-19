package com.kwchina.extend.evaluationvote.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.extend.evaluationvote.dao.PyVoteInforDAO;
import com.kwchina.extend.evaluationvote.entity.PyVoteInfor;
import com.kwchina.extend.evaluationvote.service.PyVoteInforManager;

@Service
public class PyVoteInforManagerImpl extends BasicManagerImpl<PyVoteInfor> implements PyVoteInforManager {
	 
	
	private PyVoteInforDAO pyVoteInforDAO;
	@Autowired
	public void setPyVoteInforDAO(PyVoteInforDAO pyVoteInforDAO) {
		this.pyVoteInforDAO = pyVoteInforDAO;
		super.setDao(pyVoteInforDAO);
	}
	
	
	public List getVoteByPerson(int topicId,int personId) {
		String sql ="from PyVoteInfor vote where 1=1 and vote.topicInfor.topicId="+topicId+" and vote.person.personId="+personId;
		
		List ls = this.pyVoteInforDAO.getResultByQueryString(sql);
	
	
		return ls;
	}

}