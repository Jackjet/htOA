package com.kwchina.core.base.service;


import java.util.ArrayList;

import com.kwchina.core.base.entity.OperationDefinition;
import com.kwchina.core.common.service.BasicManager;


public interface OperationDefinitionManager extends BasicManager{
	
	//通过方法名获取操作定义
	public OperationDefinition getOperationByMethod(String method);
	
	//按照树状结构组织操作信息
	public ArrayList getOperationAsTree(Integer operationId);
}
