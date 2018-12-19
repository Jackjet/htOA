package com.kwchina.oa.personal.address.dao.impl;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.kwchina.core.common.dao.BasicDaoImpl;
import com.kwchina.oa.personal.address.dao.CompanyAddressDAO;
import com.kwchina.oa.personal.address.entity.CompanyAddress;

@Repository
public class CompanyAddressDAOImpl extends BasicDaoImpl<CompanyAddress> implements CompanyAddressDAO{
	
	
	/***
	 * 通过主键来查询指定讯息
	 * @param personId:要查询讯息的主键
	 * @return address:查询出来的讯息
	 */
	public CompanyAddress showById(int personId) {
		CompanyAddress address = new CompanyAddress();
		String hql = " select b.categoryName,a.position,a.department,a.mobile,a.email,a.gender,a.birthday,a.memo,"
						+" a.officeAddress,a.officePhone,a.officeCode,a.homePhone,a.homeAddress,a.postCode from " 
						+" CompanyAddress as a " 
						//+"a.category as b "
						+" where a.personId="+personId;
		List<CompanyAddress> list = getResultByQueryString(hql);
		
		address = (CompanyAddress)list;
		return address;
	}
}

