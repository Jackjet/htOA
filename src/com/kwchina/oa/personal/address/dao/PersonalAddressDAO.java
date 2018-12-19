package com.kwchina.oa.personal.address.dao;

import com.kwchina.core.common.dao.BasicDao;
import com.kwchina.oa.personal.address.entity.PersonalAddress;

public interface PersonalAddressDAO  extends BasicDao<PersonalAddress> {
	//通过主键查询指定讯息
	public PersonalAddress showById(int personId);
}

