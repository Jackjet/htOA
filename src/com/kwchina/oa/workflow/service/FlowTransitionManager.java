package com.kwchina.oa.workflow.service;

import java.util.List;

import com.kwchina.core.common.service.BasicManager;

public interface FlowTransitionManager extends BasicManager {
	
	/**
	 * 获取某个节点链接到其他节点的所有Transition信息,即fromNode为该节点的连接信息	
	 * @param nodeId
	 * @return
	 */
	public List findAfterNodeTransition(int nodeId);
}
