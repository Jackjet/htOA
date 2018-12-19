package com.kwchina.oa.customer.service.impl;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.customer.dao.CustomerInforDAO;
import com.kwchina.oa.customer.entity.CustomerInfor;
import com.kwchina.oa.customer.service.CustomerInforManager;
import com.kwchina.oa.customer.vo.CustomerInforVo;

@Service
public class CustomerInforManagerImpl extends BasicManagerImpl<CustomerInfor> implements CustomerInforManager {

	private CustomerInforDAO customerInforDAO;

	@Autowired
	public void setCustomerInforDAO(CustomerInforDAO customerInforDAO) {
		this.customerInforDAO = customerInforDAO;
		super.setDao(customerInforDAO);
	}

	//转化CustomerInfor为CustomerInforVo
	public CustomerInforVo transPOToVO(CustomerInfor customer) {
		CustomerInforVo customerInforVo = new CustomerInforVo();

		try {
			BeanUtils.copyProperties(customerInforVo, customer);
			customerInforVo.setCustomerId(customer.getCustomerId());
			customerInforVo.setManagerName(customer.getManager().getPerson().getPersonName());
		} catch (Exception ex) {

		}

		return customerInforVo;
	}
}
