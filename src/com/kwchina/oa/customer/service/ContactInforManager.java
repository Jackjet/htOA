package com.kwchina.oa.customer.service;

import com.kwchina.core.common.service.BasicManager;
import com.kwchina.oa.customer.entity.ContactInfor;
import com.kwchina.oa.customer.vo.ContactInforVo;

public interface ContactInforManager extends BasicManager {
	
	//转化MessageInforVO为MessageInfor
	public ContactInfor transVOToPO(ContactInforVo contactInforVo);
}
