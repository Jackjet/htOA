package com.kwchina.oa.customer.service;

import com.kwchina.core.common.service.BasicManager;
import com.kwchina.oa.customer.entity.CustomerInfor;
import com.kwchina.oa.customer.vo.CustomerInforVo;

public interface CustomerInforManager extends BasicManager {

	//转化MessageInfor为MessageInforVO
	public CustomerInforVo transPOToVO(CustomerInfor customer) ;
}
