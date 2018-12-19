package com.kwchina.oa.workflow.service;

import com.kwchina.core.common.service.BasicManager;

public interface FlowDefinitionManager  extends BasicManager{
	
	/**
	 * 判断是否可以修改流程（主要是指增加或者删除节点),如果该流程下已经有未完成的实例，则不能修改
	 * @param flowId
	 * @return
	 */
	public boolean canModifyFlow(int flowId);
	
	
	/**
	 * 判断是否可以删除流程,如果该流程下已经实例，则不能删除
	 * @param flowId
	 * @return
	 */
	public boolean canDeleteFlow(int flowId);
	
	
}
