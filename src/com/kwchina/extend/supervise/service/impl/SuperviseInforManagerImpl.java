package com.kwchina.extend.supervise.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.extend.supervise.dao.SuperviseInforDAO;
import com.kwchina.extend.supervise.entity.SuperviseInfor;
import com.kwchina.extend.supervise.service.SuperviseInforManager;

@Service
public class SuperviseInforManagerImpl extends BasicManagerImpl<SuperviseInfor> implements SuperviseInforManager {

	private SuperviseInforDAO superviseInforDAO;


	//注入的方法
	@Autowired
	public void setSuperviseInforDAO(SuperviseInforDAO superviseInforDAO) {
		this.superviseInforDAO = superviseInforDAO;
		super.setDao(superviseInforDAO);
	}
	


}
