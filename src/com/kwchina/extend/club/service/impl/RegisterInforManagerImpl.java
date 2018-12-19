package com.kwchina.extend.club.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.extend.club.dao.RegisterInforDAO;
import com.kwchina.extend.club.entity.RegisterInfor;
import com.kwchina.extend.club.service.RegisterInforManager;

@Service
public class RegisterInforManagerImpl extends BasicManagerImpl<RegisterInfor> implements RegisterInforManager {

	private RegisterInforDAO registerInforDAO;


	//注入的方法
	@Autowired
	public void setRegisterInforDAO(RegisterInforDAO registerInforDAO) {
		this.registerInforDAO = registerInforDAO;
		super.setDao(registerInforDAO);
	}
	


}
