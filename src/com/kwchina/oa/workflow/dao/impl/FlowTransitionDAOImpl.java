package com.kwchina.oa.workflow.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kwchina.core.common.dao.BasicDaoImpl;
import com.kwchina.oa.workflow.dao.FlowTransitionDAO;
import com.kwchina.oa.workflow.entity.FlowTransition;

@Repository
public class FlowTransitionDAOImpl extends BasicDaoImpl<FlowTransition> implements FlowTransitionDAO{
	//查找某个流程的节点连接信息
	public List findFlowTransition(int flowId){
		String sql = "from FlowTransition  transition where  transition.flowDefinition.flowId = " + flowId + " order by transId";
		return getResultByQueryString(sql);
	}
	

	/**	
	 * 获取链接到某个节点的所有Transition信息,即toNode为该节点的连接信息	
	 * @param nodeId
	 * @return
	 */
	public List findBeforeNodeTransition(int nodeId){
		String sql = "from FlowTransition  transition where  transition.toNode.nodeId = " + nodeId;
		return getResultByQueryString(sql);
	}
	
	
	/**
	 * 获取某个节点链接到其他节点的所有Transition信息,即fromNode为该节点的连接信息	
	 * @param nodeId
	 * @return
	 */
	public List findAfterNodeTransition(int nodeId){
		String sql = "from FlowTransition  transition where  transition.fromNode.nodeId = " + nodeId;
		return getResultByQueryString(sql);
	}
	
	
	/**
	 * 获取两个节点之间的链接信息
	 * @param fromNodeId
	 * @param toNodeId
	 * @return
	 */
	public FlowTransition findNodeTransition(int fromNodeId,int toNodeId){
		String sql = "from FlowTransition  transition where  transition.fromNode.nodeId = " + fromNodeId + " and transition.toNode.nodeId=" + toNodeId;
		List ls = getResultByQueryString(sql);
		if(!ls.isEmpty()){
			return (FlowTransition)ls.get(0);
		}
		
		return null;
	}
}
