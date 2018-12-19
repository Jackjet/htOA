package com.kwchina.oa.personal.message.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.personal.message.dao.MessageDeletedDAO;
import com.kwchina.oa.personal.message.entity.MessageDeleted;
import com.kwchina.oa.personal.message.service.MessageDeletedManager;

@Service
public class MessageDeletedManagerImpl extends BasicManagerImpl<MessageDeleted> implements MessageDeletedManager {

	private MessageDeletedDAO messageDeletedDAO;

	@Autowired
	public void setMessageDeletedDAO(MessageDeletedDAO messageDeletedDAO) {
		this.messageDeletedDAO = messageDeletedDAO;
		super.setDao(messageDeletedDAO);
	}
}
