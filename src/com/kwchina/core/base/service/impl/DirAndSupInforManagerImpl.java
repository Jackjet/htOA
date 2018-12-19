package com.kwchina.core.base.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.base.dao.DirAndSupInforDAO;
import com.kwchina.core.base.entity.DirAndSupInfor;
import com.kwchina.core.base.service.DirAndSupInforManager;
import com.kwchina.core.common.service.BasicManagerImpl;

@Service
public class DirAndSupInforManagerImpl extends BasicManagerImpl<DirAndSupInfor> implements DirAndSupInforManager {
	
	private DirAndSupInforDAO dirAndSupInforDAO;

	@Autowired
	public void setDirAndSupInforDAO(DirAndSupInforDAO dirAndSupInforDAO) {
		this.dirAndSupInforDAO = dirAndSupInforDAO;
		super.setDao(dirAndSupInforDAO);
	}

}
