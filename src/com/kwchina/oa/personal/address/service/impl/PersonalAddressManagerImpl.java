package com.kwchina.oa.personal.address.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.personal.address.dao.PersonalAddressDAO;
import com.kwchina.oa.personal.address.entity.PersonalAddress;
import com.kwchina.oa.personal.address.service.PersonalAddressManager;

@Service
public class PersonalAddressManagerImpl extends BasicManagerImpl<PersonalAddress> implements PersonalAddressManager{
	private PersonalAddressDAO addressDAO;

	@Autowired
	public void setAddressDAO(PersonalAddressDAO addressDAO) {
		this.addressDAO = addressDAO;
		super.setDao(addressDAO);
	}
	
	/***
	 * 查看指定接收讯息
	 * @param messageId:被查看的讯息主键
	 * @return message:返回查看讯息
	 */
	public PersonalAddress seeAddress(int personId) {
		PersonalAddress address = this.addressDAO.showById(personId);
		return address;
	}
	
	//按照id排序
	public List getAddressOrderById(){
		
		String sql = "from PersonalAddress address order by address.personId";
		List list = this.addressDAO.getResultByQueryString(sql);
		
		return list;
	}
}
