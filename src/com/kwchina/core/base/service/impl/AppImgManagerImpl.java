package com.kwchina.core.base.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.base.dao.AppImgDAO;
import com.kwchina.core.base.entity.AppImg;
import com.kwchina.core.base.service.AppImgManager;
import com.kwchina.core.common.service.BasicManagerImpl;

@Service
public class AppImgManagerImpl extends BasicManagerImpl<AppImg> implements AppImgManager {
	
	private AppImgDAO appImgDAO;
	

	@Autowired
	public void setAppImgDAO(AppImgDAO appImgDAO) {
		this.appImgDAO = appImgDAO;
		super.setDao(appImgDAO);
	}

	
}
