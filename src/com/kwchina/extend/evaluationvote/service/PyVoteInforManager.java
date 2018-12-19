package com.kwchina.extend.evaluationvote.service;

import java.util.List;

import com.kwchina.core.common.service.BasicManager;

public interface PyVoteInforManager extends BasicManager {
	
	public List getVoteByPerson(int topicId,int personId);
	
	
}