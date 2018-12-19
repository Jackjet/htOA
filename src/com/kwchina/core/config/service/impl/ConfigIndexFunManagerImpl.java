package com.kwchina.core.config.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.core.config.dao.ConfigIndexFunDAO;
import com.kwchina.core.config.entity.ConfigIndexFun;
import com.kwchina.core.config.service.ConfigIndexFunManager;

@Service
public class ConfigIndexFunManagerImpl extends BasicManagerImpl<ConfigIndexFun> implements ConfigIndexFunManager{
	 
	private ConfigIndexFunDAO configIndexFunDAO;
	
	
    //注入的方法
	@Autowired
    public void setConfigIndexFunDAO(ConfigIndexFunDAO configIndexFunDAO){
   	 	this.configIndexFunDAO = configIndexFunDAO;
        super.setDao(configIndexFunDAO);
    }
    
	
	public void save(ConfigIndexFun configIndexFun){
			
		//保存
		this.configIndexFunDAO.save(configIndexFun);
	}
	
}
