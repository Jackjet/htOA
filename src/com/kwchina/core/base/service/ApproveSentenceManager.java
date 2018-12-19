package com.kwchina.core.base.service;

import java.util.List;

import com.kwchina.core.common.service.BasicManager;

public interface ApproveSentenceManager extends BasicManager{
	
//	获取的角色信息通过orderNo排序
	public List getApproveSentenceOrderBy();
	
}