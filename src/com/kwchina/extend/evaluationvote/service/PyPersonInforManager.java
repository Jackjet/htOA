package com.kwchina.extend.evaluationvote.service;

import java.util.List;

import com.kwchina.core.common.service.BasicManager;

public interface PyPersonInforManager extends BasicManager {
	
	//获取的人员信息通过displayOrder排序
	public List getPersonOrderBy();
	
}