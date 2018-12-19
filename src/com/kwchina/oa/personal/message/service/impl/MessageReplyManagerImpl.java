package com.kwchina.oa.personal.message.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.personal.message.dao.ReplyMessageDAO;
import com.kwchina.oa.personal.message.entity.MessageReply;
import com.kwchina.oa.personal.message.service.MessageReplyManager;

@Service
public class MessageReplyManagerImpl extends BasicManagerImpl<MessageReply> implements MessageReplyManager {

	
	private ReplyMessageDAO replyMessageDAO;

	@Autowired
	public void setReplyMessageDAO(ReplyMessageDAO replyMessageDAO) {
		this.replyMessageDAO = replyMessageDAO;
		super.setDao(replyMessageDAO);
	}

	/**
	 * 查看回复
	 * 
	 * @param messageId:被回复讯息的Id
	 */
	public List getReplyMessage(int messageId) {
		List list = this.replyMessageDAO.getReplyMessage(messageId);
		return list;
	}

}
