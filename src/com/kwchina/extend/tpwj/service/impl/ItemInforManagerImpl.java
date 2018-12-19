package com.kwchina.extend.tpwj.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.extend.tpwj.dao.ItemInforDAO;
import com.kwchina.extend.tpwj.entity.ItemInfor;
import com.kwchina.extend.tpwj.service.ItemInforManager;

@Service
public class ItemInforManagerImpl extends BasicManagerImpl<ItemInfor> implements ItemInforManager {

	private ItemInforDAO itemInforDAO;

	@Autowired
	public void setItemInforDAO(ItemInforDAO itemInforDAO) {
		this.itemInforDAO = itemInforDAO;
		super.setDao(itemInforDAO);
	}
	
	
	
}
