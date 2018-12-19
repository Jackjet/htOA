package com.kwchina.oa.personal.address.service;

import java.util.List;

import com.kwchina.core.common.service.BasicManager;
import com.kwchina.oa.personal.address.entity.PersonalAddress;

public interface PersonalAddressManager extends BasicManager{

	public PersonalAddress seeAddress(int personId);
	
	public List getAddressOrderById();
	
}

