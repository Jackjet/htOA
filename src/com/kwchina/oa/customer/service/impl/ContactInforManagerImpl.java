package com.kwchina.oa.customer.service.impl;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.customer.dao.ContactInforDAO;
import com.kwchina.oa.customer.entity.ContactInfor;
import com.kwchina.oa.customer.service.ContactInforManager;
import com.kwchina.oa.customer.vo.ContactInforVo;

@Service
public class ContactInforManagerImpl extends BasicManagerImpl<ContactInfor> implements ContactInforManager {

	private ContactInforDAO contactInforDAO;

	@Autowired
	public void setContactInforDAO(ContactInforDAO contactInforDAO) {
		this.contactInforDAO = contactInforDAO;
		super.setDao(contactInforDAO);
	}
	
	//转化contactInforVo为ContactInfor
	public ContactInfor transVOToPO(ContactInforVo contactInforVo) {
		ContactInfor contactInfor = new ContactInfor();
		try {
			BeanUtils.copyProperties(contactInfor, contactInforVo);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date birthdayUtil = dateFormat.parse(contactInforVo.getBirthdayStr());
			Date birthday = new Date(birthdayUtil.getTime());
			contactInfor.setBirthday(birthday);
		} catch (Exception ex) {

		}

		return contactInfor;
	}
}
