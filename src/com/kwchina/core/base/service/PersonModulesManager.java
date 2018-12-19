package com.kwchina.core.base.service;

import com.kwchina.core.base.entity.PersonModules;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.common.service.BasicManager;

import java.util.List;

public interface PersonModulesManager extends BasicManager{
	//根据人员获取modules
    public PersonModules selectByPerson(int personId);
    public void saveModule(PersonModules personModules);
	
}
