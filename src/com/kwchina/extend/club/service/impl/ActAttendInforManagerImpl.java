package com.kwchina.extend.club.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.extend.club.dao.ActAttendInforDAO;
import com.kwchina.extend.club.entity.ActAttendInfor;
import com.kwchina.extend.club.service.ActAttendInforManager;

@Service
public class ActAttendInforManagerImpl extends BasicManagerImpl<ActAttendInfor> implements ActAttendInforManager {

	private ActAttendInforDAO attendInforDAO;


	//注入的方法
	@Autowired
	public void setAttendInforDAO(ActAttendInforDAO attendInforDAO) {
		this.attendInforDAO = attendInforDAO;
		super.setDao(attendInforDAO);
	}
	


}
