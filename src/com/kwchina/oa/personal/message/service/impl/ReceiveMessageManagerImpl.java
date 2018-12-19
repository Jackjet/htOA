package com.kwchina.oa.personal.message.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.personal.message.dao.ReceiveMessageDAO;
import com.kwchina.oa.personal.message.entity.ReceiveMessage;
import com.kwchina.oa.personal.message.service.ReceiveMessageManager;

@Service
public class ReceiveMessageManagerImpl extends BasicManagerImpl<ReceiveMessage> implements ReceiveMessageManager {

	private ReceiveMessageDAO receiveMessageDAO;

	@Autowired
	public void setReceiveMessageDAO(ReceiveMessageDAO receiveMessageDAO) {
		this.receiveMessageDAO = receiveMessageDAO;
		super.setDao(receiveMessageDAO);
	}

	public List getReceiveMessage(int roleId, int messageId) {
		List list = new ArrayList();
		String queryString = "from ReceiveMessage receive where receive.role.roleId="+roleId+" and receive.message.messageId="+messageId;
		list = this.receiveMessageDAO.getResultByQueryString(queryString);
		
		return list;
	}
}
