package com.kwchina.oa.personal.address.dao.impl;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.kwchina.core.common.dao.BasicDaoImpl;
import com.kwchina.oa.personal.address.dao.PersonalAddressDAO;
import com.kwchina.oa.personal.address.entity.PersonalAddress;
import com.kwchina.oa.personal.address.entity.AddressCategory;

@Repository
public class PersonalAddressDAOImpl extends BasicDaoImpl<PersonalAddress> implements PersonalAddressDAO{
	
	
	/***
	 * 通过主键来查询指定讯息
	 * @param personId:要查询讯息的主键
	 * @return address:查询出来的讯息
	 */
	public PersonalAddress showById(int personId) {
		PersonalAddress address = new PersonalAddress();
		String hql = " select b.categoryName,a.position,a.mobile,a.email,a.gender,a.birthday,a.memo,"
						+"a.officeAddress,a.officePhone,a.officeCode,a.homePhone,a.homeAddress,a.postCode from " 
						+"PersonalAddress as a left join " 
						+"a.category as b "
						+"where a.personId="+personId;
		List<PersonalAddress> list = getResultByQueryString(hql);
		
		address = (PersonalAddress)list;
		return address;
	}
}

