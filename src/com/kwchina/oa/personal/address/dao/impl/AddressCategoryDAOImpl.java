package com.kwchina.oa.personal.address.dao.impl;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.kwchina.core.common.dao.BasicDaoImpl;
import com.kwchina.oa.personal.address.dao.AddressCategoryDAO;
import com.kwchina.oa.personal.address.dao.PersonalAddressDAO;
import com.kwchina.oa.personal.address.entity.PersonalAddress;
import com.kwchina.oa.personal.address.entity.AddressCategory;

@Repository
public class AddressCategoryDAOImpl extends BasicDaoImpl<AddressCategory> implements AddressCategoryDAO{
	
	
//	/***
//	 * 通过主键来查询指定讯息
//	 * @param messageId:要查询讯息的主键
//	 * @return message:查询出来的讯息
//	 */
//	public AddressCategory showById(int personId) {
//		PersonalAddress address = null;
//		String hql = " select a.personId,categoryName,position,mobile,email,gender,birthday,memo,"
//						+"officeAddress,officePhone,officeCode,homePhone,homeAddress,postCode from " 
//						+"PersonalAddress a inner join " 
//						+"AddressCategory b " 
//						+"on a.categoryId = b.categoryId "
//						+"where a.personId="+personId+"";
//		List list = getResultByQueryString(hql);
//		address = (PersonalAddress)list.get(0);
//		return address;
//	}
}

