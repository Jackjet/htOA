package com.kwchina.oa.workflow.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.kwchina.oa.workflow.dao.FlowDefinitionDAO;
import com.kwchina.oa.workflow.dao.FlowNodeDAO;
import com.kwchina.oa.workflow.dao.FlowTransitionDAO;
import com.kwchina.oa.workflow.entity.FlowDefinition;
import com.kwchina.oa.workflow.entity.FlowNode;
import com.kwchina.oa.workflow.entity.FlowTransition;
import com.kwchina.oa.workflow.exception.NodeOperateException;
import com.kwchina.oa.workflow.service.FlowDefinitionManager;
import com.kwchina.oa.workflow.service.FlowNodeManager;
import com.kwchina.oa.workflow.service.FlowTransitionManager;

@ContextConfiguration
public class TestFlowManager extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private FlowDefinitionManager flowDefinitionManager;
	
	@Autowired
	private FlowNodeManager flowNodeManager;
	
	@Autowired
	private FlowDefinitionDAO flowDefinitionDAO;
	
	@Autowired
	private FlowNodeDAO flowNodeDAO;
	
	@Autowired
	private FlowTransitionDAO flowTransitionDAO;
	
	@Autowired
	private FlowTransitionManager flowTransitionManager;
	
	
	@Ignore
	@Test
    public void testCanModifyFlow(){    	
    	Boolean canModify = this.flowDefinitionManager.canModifyFlow(4);
    	System.out.print("---------");
    	System.out.print(canModify);
    	System.out.print("---------");    	
    }
	
	@Ignore
	@Test
	public void testGetFromNodes(){
		List nodes = this.flowNodeManager.getFromNodes(4);
		
		System.out.println("---------");
		for(Iterator it = nodes.iterator();it.hasNext();){
			FlowNode node = (FlowNode)it.next();
			System.out.println(node.getNodeName());
		}    	
    	System.out.println("---------");    	
	}
	
	@Ignore
	@Test
	public void testSaveNodes(){
		FlowNode node = new FlowNode();
		
		/**
		node.setNodeName("Node-1");
		node.setNodeType(1);
		node.setFinishType(0);
		node.setCheckerType(0);		
		node.setSpecial(0);
		
		node.setPrintable(false);
		node.setDownload(false);
		node.setUpload(false);
		*/
		
		node.setNodeName("Node-4");
		node.setNodeType(1);
		node.setFinishType(0);
		node.setCheckerType(0);		
		node.setSpecial(0);
		
		node.setPrintable(false);
		node.setDownload(false);
		node.setUpload(false);	
		
		
		//所属流程
		FlowDefinition flowDefine = this.flowDefinitionDAO.get(4);   
		node.setFlowDefinition(flowDefine);
		
		List fromNodes = new ArrayList();
		FlowNode fromNode = this.flowNodeDAO.get(5);
		fromNodes.add(fromNode);
		//FlowNode fromNode2 = this.flowNodeDAO.get(23);
		//fromNodes.add(fromNode2);
		
		System.out.println("---------");
		try{
			this.flowNodeManager.saveFlowNode(node,fromNodes);
		}catch(NodeOperateException ex){
			System.out.println(ex.toString());
		}
		
		//读取保存的数据
    	List ls = this.flowNodeDAO.getAll();    	
    	System.out.println("NodeSize==" + ls.size());
    	
    	int k = 0;
    	for(Iterator itNode = ls.iterator();itNode.hasNext();){
    		FlowNode tempNode = (FlowNode)itNode.next();
    		k+= 1;
    		
    		System.out.println("Node---:" + k);
    		System.out.println(tempNode.getNodeName());
    		System.out.println(tempNode.getLayer());
    		System.out.println(tempNode.getNodeType());
    		System.out.println(tempNode.isForked());
    	}
    	
    	//读取Transition
    	List lsTransition = this.flowTransitionDAO.getAll();
    	System.out.println("transitionSize==" + lsTransition.size());
    	
    	
    	for(Iterator itTrans = lsTransition.iterator();itTrans.hasNext();){
    		FlowTransition transition = (FlowTransition)itTrans.next();
    		k+= 1;
    		
    		System.out.println("Transition---:" + k);
    		System.out.println(transition.getFromNode().getNodeId());
    		System.out.println(transition.getToNode().getNodeId());
    	}
    	
    	
    	System.out.println("---------");
	}
	
	
	@Test
	public void testDeleteNode(){
		FlowNode node = this.flowNodeDAO.get(29);
		
		try{
			this.flowNodeManager.deleteFlowNode(node);
		}catch(Exception ex){
			System.out.println(ex.toString());
		}

		
		System.out.println("---------");
		int k = 0;
		
		List ls = this.flowNodeDAO.getAll();    	
    	System.out.println("NodeSize==" + ls.size());
    	for(Iterator itNode = ls.iterator();itNode.hasNext();){
    		FlowNode tempNode = (FlowNode)itNode.next();
    		k+= 1;
    		
    		System.out.println("Node---:" + k);
    		System.out.println(tempNode.getNodeName());
    		System.out.println(tempNode.getLayer());
    		System.out.println(tempNode.getNodeType());
    		System.out.println(tempNode.isForked());
    	}
    	
    	//读取Transition
    	List lsTransition = this.flowTransitionDAO.getAll();
    	System.out.println("transitionSize==" + lsTransition.size());
    	
    	
    	for(Iterator itTrans = lsTransition.iterator();itTrans.hasNext();){
    		FlowTransition transition = (FlowTransition)itTrans.next();
    		k+= 1;
    		
    		System.out.println("Transition---:" + k);
    		System.out.println(transition.getFromNode().getNodeId());
    		System.out.println(transition.getToNode().getNodeId());
    	}
    	
    	
    	System.out.println("---------");
		
		
		
	}
	
}
