package com.kwchina.extend.loginLog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.extend.loginLog.dao.LoginLogDAO;
import com.kwchina.extend.loginLog.entity.LoginLog;
import com.kwchina.extend.loginLog.service.LoginLogManager;

@Service
public class LoginLogManagerImpl extends BasicManagerImpl<LoginLog> implements LoginLogManager {
	
	private LoginLogDAO loginLogDAO;
	

	@Autowired
	public void setLoginLogDAO(LoginLogDAO loginLogDAO) {
		this.loginLogDAO = loginLogDAO;
		super.setDao(loginLogDAO);
	}

	
}
