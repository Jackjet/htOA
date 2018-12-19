package com.kwchina.oa.workflow.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.workflow.dao.FlowDefinitionDAO;
import com.kwchina.oa.workflow.dao.InstanceInforDAO;
import com.kwchina.oa.workflow.entity.FlowDefinition;
import com.kwchina.oa.workflow.service.FlowDefinitionManager;

@Service("flowManager")
public class FlowDefinitionManagerImpl extends BasicManagerImpl<FlowDefinition> implements FlowDefinitionManager {
	
	@Autowired
	private InstanceInforDAO instanceInforDAO;
	
	private FlowDefinitionDAO flowDefinitionDAO;

	@Autowired
	public void setFlowDefinitionDAO(FlowDefinitionDAO flowDefinitionDAO) {
		this.flowDefinitionDAO = flowDefinitionDAO;
		super.setDao(flowDefinitionDAO);
	}
	
	/**
	 * 判断是否可以修改流程（主要是指增加或者删除节点),如果该流程下已经有未完成的实例，则不能修改
	 * @param flowId
	 * @return
	 */
	public boolean canModifyFlow(int flowId){
		List ls = this.instanceInforDAO.getUnfinishInstances(flowId);
		if(!ls.isEmpty()){
			return false;
		}
		
		return true;
	}
	
	
	/**
	 * 判断是否可以删除流程,如果该流程下已经实例，则不能删除
	 * @param flowId
	 * @return
	 */
	public boolean canDeleteFlow(int flowId){
		List ls = this.instanceInforDAO.getInstancesByFlow(flowId);
		if(!ls.isEmpty()){
			return false;
		}
		
		return true;
	}

	
}
