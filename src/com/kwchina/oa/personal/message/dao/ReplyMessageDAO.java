package com.kwchina.oa.personal.message.dao;

import java.util.List;

import com.kwchina.core.common.dao.BasicDao;
import com.kwchina.oa.personal.message.entity.MessageReply;

public interface ReplyMessageDAO extends BasicDao<MessageReply>{
    //查看回复的信息
	public List getReplyMessage(int messageId);
	
}
