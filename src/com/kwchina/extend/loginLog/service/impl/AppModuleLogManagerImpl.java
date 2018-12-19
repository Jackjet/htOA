package com.kwchina.extend.loginLog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.extend.loginLog.dao.AppModuleLogDAO;
import com.kwchina.extend.loginLog.entity.AppModuleLog;
import com.kwchina.extend.loginLog.service.AppModuleLogManager;

@Service
public class AppModuleLogManagerImpl extends BasicManagerImpl<AppModuleLog> implements AppModuleLogManager {
	
	private AppModuleLogDAO appModuleLogDAO;
	

	@Autowired
	public void setAppModuleLogDAO(AppModuleLogDAO appModuleLogDAO) {
		this.appModuleLogDAO = appModuleLogDAO;
		super.setDao(appModuleLogDAO);
	}

	
}
