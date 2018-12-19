package com.kwchina.oa.workflow.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.workflow.dao.FlowTransitionDAO;
import com.kwchina.oa.workflow.entity.FlowTransition;
import com.kwchina.oa.workflow.service.FlowTransitionManager;


@Service
public class FlowTransitionManagerImpl extends BasicManagerImpl<FlowTransition> implements FlowTransitionManager {

	@Autowired
	private FlowTransitionDAO flowTransitionDAO;
	
	
	/**
	 * 获取某个节点链接到其他节点的所有Transition信息,即fromNode为该节点的连接信息	
	 * @param nodeId
	 * @return
	 */
	public List findAfterNodeTransition(int nodeId) {
		return this.flowTransitionDAO.findAfterNodeTransition(nodeId);
	}
}
