package com.kwchina.core.base.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.base.dao.AppVersionDAO;
import com.kwchina.core.base.entity.AppVersion;
import com.kwchina.core.base.service.AppVersionManager;
import com.kwchina.core.common.service.BasicManagerImpl;

@Service
public class AppVersionManagerImpl extends BasicManagerImpl<AppVersion> implements AppVersionManager{
	 
	private AppVersionDAO appVersionDAO;
	 
	@Autowired
    public void setAppVersionDAO(AppVersionDAO appVersionDAO){
    	this.appVersionDAO = appVersionDAO;
        super.setDao(appVersionDAO);    
    }
     
     

}
