package com.kwchina.oa.workflow.service;

import java.util.List;

import com.kwchina.core.common.service.BasicManager;
import com.kwchina.oa.workflow.entity.FlowNode;
import com.kwchina.oa.workflow.exception.NodeNotFoundException;
import com.kwchina.oa.workflow.exception.NodeOperateException;

public interface FlowNodeManager extends BasicManager {

	/**
	 * 增加某个流程的节点时，获取可以作为fromNode的节点信息
	 * @param flowId
	 * @return
	 */
	public List getFromNodes(int flowId);
	
	
	/**	
	 * 保存节点信息	 
	 * @param node:当前操作节点
	 * @param fromNode：当前节点的前节点	 
	 * @return
	 */
	public FlowNode saveFlowNode(FlowNode node,List fromNodes) throws NodeOperateException;
	
	
	/**
	 * 删除流程节点
	 * @param node
	 * @return
	 */
	public void deleteFlowNode(FlowNode node) throws NodeNotFoundException,NodeOperateException;
	
	
	//查找某个流程的审核节点信息
	public List findFlowNodes(int flowId);
}
