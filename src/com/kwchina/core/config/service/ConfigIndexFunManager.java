package com.kwchina.core.config.service;

import com.kwchina.core.config.entity.ConfigIndexFun;
import com.kwchina.core.common.service.BasicManager;

public interface ConfigIndexFunManager extends BasicManager{
	
	
	public void save(ConfigIndexFun category);
	
}
