package com.kwchina.oa.personal.message.service;

import java.util.List;

import com.kwchina.core.common.service.BasicManager;

public interface ReceiveMessageManager extends BasicManager {

	//根据角色和messageId查出receiveMessage
	public List getReceiveMessage(int roleId,int messageId);
}
