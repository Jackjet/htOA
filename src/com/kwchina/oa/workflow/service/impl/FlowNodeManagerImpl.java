package com.kwchina.oa.workflow.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.workflow.dao.FlowNodeDAO;
import com.kwchina.oa.workflow.dao.FlowTransitionDAO;
import com.kwchina.oa.workflow.dao.InstanceInforDAO;
import com.kwchina.oa.workflow.entity.FlowInstanceInfor;
import com.kwchina.oa.workflow.entity.FlowNode;
import com.kwchina.oa.workflow.entity.FlowTransition;
import com.kwchina.oa.workflow.entity.InstanceLayerInfor;
import com.kwchina.oa.workflow.exception.NodeNotFoundException;
import com.kwchina.oa.workflow.exception.NodeOperateException;
import com.kwchina.oa.workflow.service.FlowDefinitionManager;
import com.kwchina.oa.workflow.service.FlowNodeManager;

@Service
public class FlowNodeManagerImpl extends BasicManagerImpl<FlowNode> implements FlowNodeManager {
	@Autowired
	private FlowDefinitionManager flowDefinitionManager;

	@Autowired
	private InstanceInforDAO instanceInforDAO;

	@Autowired
	private FlowTransitionDAO flowTransitionDAO;

	
	private FlowNodeDAO flowNodeDAO;
	
	@Autowired
	public void setFlowNodeDAO(FlowNodeDAO flowNodeDAO) {
		this.flowNodeDAO = flowNodeDAO;
		super.setDao(flowNodeDAO);
	}

	/**
	 * 增加某个流程的节点时，获取可以作为fromNode的节点信息 
	 * 1. 如果已有分叉，则分叉节点前节点，不能作为fromNode 
	 * 2. 聚合节点前的所有节点，均不能作为fromNode 
	 * 3. 分叉节点，在分叉内，其后有后续节点，不能作为fromNode
	 * 
	 * @param flowId
	 * @return
	 */
	public List getFromNodes(int flowId) {
		List rNodes = new ArrayList();

		List nodes = this.flowNodeDAO.findFlowNodes(flowId);
		List transitions = this.flowTransitionDAO.findFlowTransition(flowId);

		// 得到层次最大的分叉节点,聚合节点
		int maxForkLayer = 0;
		int maxJoinLayer = 0;
		for (Iterator it = nodes.iterator(); it.hasNext();) {
			FlowNode node = (FlowNode) it.next();
			if (node.getNodeType() == FlowNode.Node_Type_Fork && node.getLayer() > maxForkLayer) {
				maxForkLayer = node.getLayer();
			}

			if (node.getNodeType() == FlowNode.Node_Type_Join && node.getLayer() > maxJoinLayer) {
				maxJoinLayer = node.getLayer();
			}
		}

		for (Iterator it = nodes.iterator(); it.hasNext();) {
			FlowNode node = (FlowNode) it.next();

			// 最大聚合节点前的节点不能作为fromNode，最大分叉节点前的节点不能作为fromNode
			if (node.getLayer() < maxJoinLayer || node.getLayer() < maxForkLayer)
				continue;

			boolean canAdd = true;
			if (node.isForked()) {
				// 分叉内的节点,其后有后续节点，不能作为fromNode
				for (Iterator itTransition = transitions.iterator(); itTransition.hasNext();) {
					FlowTransition transition = (FlowTransition) itTransition.next();
					if (transition.getFromNode() != null && transition.getFromNode().getNodeId() == node.getNodeId()){
						canAdd = false;
						break;
					}	
				}
			}

			if(canAdd)
				rNodes.add(node);
		}

		return rNodes;
	}

	/**
	 * 保存节点信息,默认修改时候不能进行节点位置的改变
	 * 
	 * @param node:当前操作节点
	 * @param fromNode：当前节点的前节点
	 * @return
	 */
	public FlowNode saveFlowNode(FlowNode node, List fromNodes) throws NodeOperateException {
		Integer nodeId = node.getNodeId();
		/**
		 * if(fromNodes==null || fromNodes.isEmpty()){ //没有fromNode,抛出异常 }
		 */
		
		/*
		 * @todo:
		 * 考虑一开始就分叉的情况 
		 */

		/**
		 * 如果fromNode有多个，说明是聚合节点 聚合节点必须来自于同一个分叉，并且要完整
		 */
		if (fromNodes != null && fromNodes.size() > 1) {
			int k = 0;
			int forkedNodeId = 0;
			int forkSize = 0;
			
			for (Iterator it = fromNodes.iterator(); it.hasNext();) {
				FlowNode tempNode = (FlowNode) it.next();
				if (!tempNode.isForked())
					// 抛出异常
					throw new NodeOperateException("Join Node,Selected from Nodes error!");
				
				forkSize+=1;
				k += 1;
				if (k > 1 && forkedNodeId != tempNode.getForkedNode().getNodeId())
					throw new NodeOperateException("Join Node,Selected from Nodes error!");
				else
					forkedNodeId = tempNode.getForkedNode().getNodeId();
			}
			
			//完整性判断,获取有几个分叉，然后与forkSize比较
			/**@todo 未完成 */
			
		}

		
		int layer = 0;		
		if (nodeId==null || nodeId == 0) {
			
			//预设节点为普通节点
			//node.setNodeType(FlowNode.Node_Type_Normal);
			node.setLayer(1);	
			//设定其聚合的相关属性		
			node.setForked(false);
			node.setForkedNode(null);	
			//保存Node信息
			node = (FlowNode)this.flowNodeDAO.save(node);
			
			//新增节点			
			if(fromNodes != null && !fromNodes.isEmpty()) {
				// 有起始节点,且起始只有一个
				if (fromNodes.size() == 1) {
					FlowNode tempNode = (FlowNode) fromNodes.get(0);
					if (tempNode.isForked()) {
						//如果fromNode就是分叉内节点，设定本节点也为分叉内节点
						node.setForked(true);
						node.setForkedNode(tempNode.getForkedNode());
					}
					if (tempNode.getNodeType() == FlowNode.Node_Type_Fork) {
						//如果fromNode是分叉节点，设定本节点为分叉内节点
						node.setForked(true);
						node.setForkedNode(tempNode);
					}
					
					int fromNodeId = tempNode.getNodeId();
					List aTransitions = this.flowTransitionDAO.findAfterNodeTransition(fromNodeId);
					if (aTransitions.size() == 1) {
						/**
						 * 如果fromNode后有且仅有一个节点，则需要
						 * 1.更改该节点为分叉节点 
						 * 2.把另外一个分支更改为分叉内节点
						 */
						FlowTransition theOtherTrans = (FlowTransition) aTransitions.get(0);
						FlowNode otherNode = theOtherTrans.getToNode();
						otherNode.setForked(true);
						otherNode.setForkedNode(tempNode);
						this.flowNodeDAO.save(otherNode);
	
						tempNode.setNodeType(FlowNode.Node_Type_Fork);
						this.flowNodeDAO.save(tempNode);
					}
					
					//如果fromNode后已经有节点，则本节点也需要设置为分叉内节点
					if(!aTransitions.isEmpty()){
						node.setForked(true);
						node.setForkedNode(tempNode);
					}
				} else {
					// 设定其聚合的相关属性
					node.setNodeType(FlowNode.Node_Type_Join);					
				}
	
				// 新增Transition
				int fromIndex = 0;
				for (Iterator it = fromNodes.iterator(); it.hasNext();) {
					FlowNode tempNode = (FlowNode) it.next();
					layer = tempNode.getLayer();
					
					//设置Node的层次
					node.setLayer(layer+1);
					
					fromIndex += 1;
	
					FlowTransition transition = new FlowTransition();
					transition.setFlowDefinition(node.getFlowDefinition());
					transition.setFromIndex(fromIndex);
					transition.setFromNode(tempNode);
					transition.setToNode(node);
					transition.setTransName("");
				
					this.flowTransitionDAO.save(transition);
				}
			}
		}


		/**
		 * //新增或者更改fromNode,如果fromNode其后已经有节点,则需要更改该节点为分叉节点 if(fromNode!=null){
		 * int fromNodeId = fromNode.getNodeId(); List aTransitions =
		 * this.flowTransitionDAO.findAfterNodeTransition(fromNodeId);
		 * 
		 * if((nodeId==0 && !aTransitions.isEmpty() && aTransitions.size()>0) ||
		 * !fromNode.equals(oldFromNode)){
		 * fromNode.setNodeType(FlowNode.Node_Type_Fork);
		 * this.flowNodeDAO.save(fromNode); } }
		 * 
		 * //新增或者更改toNode,对于该节点的toNode,如果其前面已经有节点，则需要更改该节点为聚合节点
		 * if(toNode!=null){ int toNodeId = toNode.getNodeId(); List
		 * bTransitions =
		 * this.flowTransitionDAO.findBeforeNodeTransition(toNodeId);
		 * if((nodeId==0 && !bTransitions.isEmpty() && bTransitions.size()>0) ||
		 * !toNode.equals(oldToNode)){
		 * toNode.setNodeType(FlowNode.Node_Type_Join);
		 * this.flowNodeDAO.save(toNode); } }
		 * 
		 * 
		 * if(nodeId!=0){ //如果是修改节点
		 * 
		 * //起始fromNode更改,需要更改Transition的信息,并且更改fromNode的类型
		 * if(!fromNode.equals(oldFromNode)){ int oldFromNodeId =
		 * oldFromNode.getNodeId(); FlowTransition transition =
		 * this.flowTransitionDAO.findNodeTransition(oldFromNodeId, nodeId);
		 * if(transition!=null){ transition.setFromNode(fromNode); }
		 * this.flowTransitionDAO.save(transition);
		 * 
		 * //如果原fromNode后只有一个节点，则需要从Fork类型改为Normal类型 List aTransitions =
		 * this.flowTransitionDAO.findAfterNodeTransition(oldFromNodeId);
		 * if(aTransitions.size()==1){
		 * fromNode.setNodeType(FlowNode.Node_Type_Normal);
		 * this.flowNodeDAO.save(fromNode); } }
		 * 
		 * //toNode更改,,需要更改Transition的信息,并且更改toNode的类型
		 * if(!toNode.equals(oldToNode)){ int oldToNodeId =
		 * oldToNode.getNodeId(); FlowTransition transition =
		 * this.flowTransitionDAO.findNodeTransition(nodeId, oldToNodeId);
		 * if(transition!=null){ transition.setToNode(toNode); }
		 * this.flowTransitionDAO.save(transition);
		 * 
		 * //如果原toNode前只有一个节点，则需要从Join类型改为Normal类型 List bTransitions =
		 * this.flowTransitionDAO.findAfterNodeTransition(oldToNodeId);
		 * if(bTransitions.size()==1){
		 * toNode.setNodeType(FlowNode.Node_Type_Normal);
		 * this.flowNodeDAO.save(toNode); } } }else{ int fromIndex = 0;
		 * //fromNode其后已有的节点 if(fromNode!=null){ List aTransitions =
		 * this.flowTransitionDAO.findAfterNodeTransition(fromNode.getNodeId());
		 * fromIndex = aTransitions.size()+1; }
		 * 
		 * //新增节点,添加Transition信息 FlowTransition transition = new
		 * FlowTransition();
		 * transition.setFlowDefinition(node.getFlowDefinition());
		 * transition.setFromIndex(fromIndex); transition.setFromNode(fromNode);
		 * transition.setToNode(toNode); transition.setTransName(""); }
		 */

		//保存Node		
		this.flowNodeDAO.save(node);

		return node;
	}

	/**
	 * 删除流程节点
	 * 
	 * @param node
	 * @return 0-删除成功 1-不能删除 2-删除失败
	 */
	public void deleteFlowNode(FlowNode node) throws NodeNotFoundException,NodeOperateException {

		if (node == null)
			throw new NodeNotFoundException("");

		Integer nodeId = node.getNodeId();

		// 该节点存在关联的layer信息,不能删除
		Integer flowId = node.getFlowDefinition().getFlowId();
		List ls = this.instanceInforDAO.getInstancesByFlow(flowId);
		if (!ls.isEmpty()) {
			for (Iterator it=ls.iterator();it.hasNext();) {
				FlowInstanceInfor instance = (FlowInstanceInfor)it.next();
				Set layers = instance.getLayers();
				for (Iterator itLayer=layers.iterator();itLayer.hasNext();) {
					InstanceLayerInfor layer = (InstanceLayerInfor)itLayer.next();
					if (layer.getFlowNode() != null && layer.getFlowNode().getNodeId().intValue() == nodeId) {
						throw new NodeOperateException("Delete Node exception:This Flow has layers,please check the Workflow_Instance_LayerInfor");
					}
				}
			}
		}

		/**
		 * 先删除该节点的Transition信息
		 * 第一步：删除fromNode为该节点的信息，如果该节点不是第一个，则把其前面的节点链接到该节点toNode的节点上
		 */
		/**
		 * List bTransitions =
		 * this.flowTransitionDAO.findBeforeNodeTransition(nodeId); List
		 * aTransitions =
		 * this.flowTransitionDAO.findAfterNodeTransition(nodeId); if
		 * (!bTransitions.isEmpty()) { // 前面有节点，如果该节点为分叉节点的话，则不能删除 if
		 * (aTransitions.size() > 1) return 1;
		 * 
		 * if (aTransitions.isEmpty()) { // 后面无节点，删除前面的Transition for (Iterator
		 * it = bTransitions.iterator(); it.hasNext();) { FlowTransition
		 * bTransition = (FlowTransition) it.next();
		 * this.flowTransitionDAO.remove(bTransition); } } else { //
		 * 后面有节点，则把前面节点的toNode换为该节点的toNode FlowTransition transition =
		 * (FlowTransition) aTransitions.get(0); FlowNode afterNode =
		 * transition.getToNode();
		 * 
		 * for (Iterator it = bTransitions.iterator(); it.hasNext();) {
		 * FlowTransition bTransition = (FlowTransition) it.next();
		 * bTransition.setToNode(afterNode);
		 * this.flowTransitionDAO.save(bTransition); } } }
		 */

		List aTransitions = this.flowTransitionDAO.findAfterNodeTransition(nodeId);
		// 前面该节点后面有节点，则不能删除
		if (aTransitions.size() > 1)
			throw new NodeOperateException("Can't delete this node,after it nodes exsits!");

		// 删除链接关系,即toNode为该节点的Transition
		List bTransitions = this.flowTransitionDAO.findBeforeNodeTransition(nodeId);
		for (Iterator it = bTransitions.iterator(); it.hasNext();) {
			FlowTransition transition = (FlowTransition) it.next();

			/**
			 * 如果前面一个节点有且只有2个作为fromNode的Transition(即删除节点的前面一个为分叉节点)
			 * 则需要把前面一个节点从Fork类型改为Normal类型,并且分叉的另外一个节点的forked属性需要更改
			 */
			FlowNode fromNode = transition.getFromNode();
			int fromNodeId = fromNode.getNodeId();
			List nodeTransitions = this.flowTransitionDAO.findAfterNodeTransition(fromNodeId);
			if (!nodeTransitions.isEmpty() && nodeTransitions.size() == 2) {
				// 更改另外一个节点的forked属性
				for (Iterator itTansition = nodeTransitions.iterator(); itTansition.hasNext();) {
					FlowTransition theOtherTrans = (FlowTransition) itTansition.next();
					FlowNode otherNode = theOtherTrans.getToNode();
					if (otherNode.getNodeId() != nodeId) {
						otherNode.setForked(false);
						otherNode.setForkedNode(null);
						this.flowNodeDAO.save(otherNode);

						break;
					}
				}

				fromNode.setNodeType(FlowNode.Node_Type_Normal);
				this.flowNodeDAO.save(fromNode);
			}

			// 删除该Transition
			this.flowTransitionDAO.remove(transition);
		}

		// 删除节点信息
		this.flowNodeDAO.remove(node);

	}
	
	
	//查找某个流程的审核节点信息
	public List findFlowNodes(int flowId) {
		return this.flowNodeDAO.findFlowNodes(flowId);
	}

}
