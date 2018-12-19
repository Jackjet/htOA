package com.kwchina.oa.personal.address.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.base.dao.PersonInforDAO;
import com.kwchina.core.base.entity.PersonInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.PersonInforManager;
import com.kwchina.core.base.service.SystemUserManager;
import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.personal.address.dao.AddressDAO;
import com.kwchina.oa.personal.address.service.AddressManager;

@Service
public class AddressManagerImpl extends BasicManagerImpl<PersonInfor> implements AddressManager{
	private AddressDAO addressDAO;

	@Autowired
	public void setAddressDAO(AddressDAO addressDAO) {
		this.addressDAO = addressDAO;
		super.setDao(addressDAO);
	}
}
