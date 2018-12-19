package com.kwchina.core.base.service;

import com.kwchina.core.base.entity.Modules;
import com.kwchina.core.base.entity.PersonModules;
import com.kwchina.core.common.service.BasicManager;

public interface ModulesManager extends BasicManager{
	//根据名称获取modules
    public Modules getByName(String name);
	
}
