package com.kwchina.oa.workflow.dao;

import java.util.List;

import com.kwchina.core.common.dao.BasicDao;
import com.kwchina.oa.workflow.entity.FlowTransition;

public interface FlowTransitionDAO extends BasicDao<FlowTransition>{

	/**
	 * 查找某个流程的全部连接信息
	 * @param flowId
	 * @return
	 */
	public List findFlowTransition(int flowId);
	

	/**	
	 * 获取链接到某个节点的所有Transition信息,即toNode为该节点的连接信息	
	 * @param nodeId
	 * @return
	 */
	public List findBeforeNodeTransition(int nodeId);
	
	
	/**
	 * 获取某个节点链接到其他节点的所有Transition信息,即fromNode为该节点的连接信息	
	 * @param nodeId
	 * @return
	 */
	public List findAfterNodeTransition(int nodeId);
	
	
	/**
	 * 获取两个节点之间的链接信息
	 * @param fromNodeId
	 * @param toNodeId
	 * @return
	 */
	public FlowTransition findNodeTransition(int fromNodeId,int toNodeId);
}
