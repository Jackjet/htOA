package com.kwchina.oa.workflow.dao;

import java.util.List;

import com.kwchina.core.common.dao.BasicDao;
import com.kwchina.oa.workflow.entity.FlowNode;

public interface FlowNodeDAO extends BasicDao<FlowNode>{
	//查找某个流程的审核节点信息
	public List findFlowNodes(int flowId);
	
	//获取某个流程某个层次的审核节点
	public List findeFlowNodes(int flowId,int layer);
	
	//查找某个节点的审核人信息
	public List findNodeCheckers(int nodeId);
	
	//查找某个节点的审核角色信息
	public List findeNodeCheckerRoles(int nodeId);
}
