package com.kwchina.core.cms.service;

import com.kwchina.core.cms.entity.InforField;
import com.kwchina.core.common.service.BasicManager;

public interface InforFieldManager extends BasicManager{
	
	//根据category及fieldName获取InforField
	public InforField getFieldByName(String fieldName,int categoryId);
	
}
