package com.kwchina.oa.personal.address.service;

import java.util.List;

import com.kwchina.core.common.service.BasicManager;

public interface AddressCategoryManager extends BasicManager{

	//获取分类名称
	public List getCategoryName(int personId);

	public List getCategoryOrderById();
	
}

