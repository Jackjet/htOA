package com.kwchina.oa.personal.address.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.personal.address.dao.CompanyAddressDAO;
import com.kwchina.oa.personal.address.entity.CompanyAddress;
import com.kwchina.oa.personal.address.service.CompanyAddressManager;

@Service("companyAddressManager")
public class CompanyAddressManagerImpl extends BasicManagerImpl<CompanyAddress> implements CompanyAddressManager{
	private CompanyAddressDAO addressDAO;

	@Autowired
	public void setAddressDAO(CompanyAddressDAO addressDAO) {
		this.addressDAO = addressDAO;
		super.setDao(addressDAO);
	}
	
	/***
	 * 查看指定接收讯息

	 * @return message:返回查看讯息
	 */
	public CompanyAddress seeAddress(int personId) {
		CompanyAddress address = this.addressDAO.showById(personId);
		return address;
	}
	
	//按照id排序
	public List getAddressOrderById(){
		
		String sql = "from CompanyAddress address order by address.personId";
		List list = this.addressDAO.getResultByQueryString(sql);
		
		return list;
	}
	//获取所有包含手机号信息的人员
	public List getMobilePerson() {
		String querySQL = "from CompanyAddress address where address.mobile is not null order by personName";
		List<CompanyAddress> persons = this.addressDAO.getResultByQueryString(querySQL);
		return persons;
	}
}
