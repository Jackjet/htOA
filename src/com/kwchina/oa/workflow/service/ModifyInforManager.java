package com.kwchina.oa.workflow.service;

import java.util.List;

import com.kwchina.core.common.service.BasicManager;

public interface ModifyInforManager extends BasicManager{
	
	public List getModifyInfor(int instanceId);
	
}