package com.kwchina.oa.workflow.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kwchina.core.common.dao.BasicDaoImpl;
import com.kwchina.oa.workflow.dao.FlowNodeDAO;
import com.kwchina.oa.workflow.entity.FlowNode;

@Repository
public class FlowNodeDAOImpl extends BasicDaoImpl<FlowNode> implements FlowNodeDAO{
	
	//查找某个流程的审核节点信息
	public List findFlowNodes(int flowId){
		String sql = "from FlowNode  node where  node.flowDefinition.flowId = " + flowId + " order by nodeId";
		return getResultByQueryString(sql);
	}
	
	//获取某个流程某个层次的审核节点
	public List findeFlowNodes(int flowId,int layer){
		String sql = "from FlowNode  node where  node.flowDefinition.flowId = " + flowId + " and node.layer = " + layer + " order by nodeId";
		return getResultByQueryString(sql);
	}
	
	
	//查找某个节点的审核人信息
	public List findNodeCheckers(int nodeId){
		String sql = "from NodeCheckerPerson  checkPerson where  checkPerson.flowNode.nodeId = " + nodeId;
		return getResultByQueryString(sql);
	}
	
	//查找某个节点的审核角色信息
	public List findeNodeCheckerRoles(int nodeId){
		String sql = "from NodeCheckerRole  checkRole where  checkRole.flowNode.nodeId = " + nodeId;
		return getResultByQueryString(sql);
		
	}
}
