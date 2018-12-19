package com.kwchina.extend.tpwj.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.extend.tpwj.dao.TopicRightDAO;
import com.kwchina.extend.tpwj.entity.TopicRight;
import com.kwchina.extend.tpwj.service.TopicRightManager;

@Service
public class TopicRightManagerImpl extends BasicManagerImpl<TopicRight> implements TopicRightManager {

	private TopicRightDAO topicRightDAO;

	@Autowired
	public void setTopicRightDAO(TopicRightDAO topicRightDAO) {
		this.topicRightDAO = topicRightDAO;
		super.setDao(topicRightDAO);
	}
	
	
	
}
