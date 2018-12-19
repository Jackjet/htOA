package com.kwchina.oa.personal.address.service;

import java.util.List;

import com.kwchina.core.common.service.BasicManager;
import com.kwchina.oa.personal.address.entity.CompanyAddress;

public interface CompanyAddressManager extends BasicManager{

	public CompanyAddress seeAddress(int personId);
	
	public List getAddressOrderById();
	//获取所有包含手机号信息的人员
	public List getMobilePerson();
}

