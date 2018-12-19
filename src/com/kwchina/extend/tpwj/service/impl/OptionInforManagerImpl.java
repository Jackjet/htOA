package com.kwchina.extend.tpwj.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.extend.tpwj.dao.OptionInforDAO;
import com.kwchina.extend.tpwj.entity.OptionInfor;
import com.kwchina.extend.tpwj.service.OptionInforManager;

@Service
public class OptionInforManagerImpl extends BasicManagerImpl<OptionInfor> implements OptionInforManager {

	private OptionInforDAO optionInforDAO;

	@Autowired
	public void setOptionInforDAO(OptionInforDAO optionInforDAO) {
		this.optionInforDAO = optionInforDAO;
		super.setDao(optionInforDAO);
	}
	
	
	
}
