package com.kwchina.sms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.meeting.dao.MeetDAO;
import com.kwchina.sms.dao.SMSSendMessageDAO;
import com.kwchina.sms.entity.SMSMessagesToSend;
import com.kwchina.sms.service.SmsManager;

@Service
public class SmsManagerImpl extends BasicManagerImpl<SMSMessagesToSend> implements SmsManager{
	
	private SMSSendMessageDAO sMSSendMessageDAO;

	@Autowired
	public void setMeetDAO(SMSSendMessageDAO sMSSendMessageDAO) {
		this.sMSSendMessageDAO = sMSSendMessageDAO;
		super.setDao(sMSSendMessageDAO);
	}

	
}