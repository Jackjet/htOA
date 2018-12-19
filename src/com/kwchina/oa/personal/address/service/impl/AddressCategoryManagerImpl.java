package com.kwchina.oa.personal.address.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.base.entity.OrganizeInfor;
import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.oa.personal.address.dao.AddressCategoryDAO;
import com.kwchina.oa.personal.address.dao.PersonalAddressDAO;
import com.kwchina.oa.personal.address.entity.AddressCategory;
import com.kwchina.oa.personal.address.entity.PersonalAddress;
import com.kwchina.oa.personal.address.service.AddressCategoryManager;
import com.kwchina.oa.personal.address.service.PersonalAddressManager;

@Service 
public class AddressCategoryManagerImpl extends BasicManagerImpl<AddressCategory> implements AddressCategoryManager{
	private AddressCategoryDAO addressDAO;

	@Autowired
	public void setAddressDAO(AddressCategoryDAO addressDAO) {
		this.addressDAO = addressDAO;
		super.setDao(addressDAO);
	}

	//获取分类名称
	public List<AddressCategory> getCategoryName(int personId) {
		List<AddressCategory> returnLs = new ArrayList<AddressCategory>();
 		String hql = "from AddressCategory where personId = "+personId;
		returnLs = this.addressDAO.getResultByQueryString(hql); 		

 		return returnLs;
	}
	
	//按照id排序
	public List getCategoryOrderById(){
		
		String sql = "from AddressCategory category order by category.categoryId";
		List list = this.addressDAO.getResultByQueryString(sql);
		
		return list;
	}
}
