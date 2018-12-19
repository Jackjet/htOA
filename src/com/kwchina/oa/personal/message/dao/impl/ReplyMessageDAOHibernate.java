package com.kwchina.oa.personal.message.dao.impl;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.kwchina.core.common.dao.BasicDaoImpl;
import com.kwchina.oa.personal.message.dao.ReplyMessageDAO;
import com.kwchina.oa.personal.message.entity.MessageReply;


@Repository
public class ReplyMessageDAOHibernate extends BasicDaoImpl<MessageReply> implements ReplyMessageDAO {

	/*****
	 * 查看回复的信息
	 * @param messageId:被查看讯息的主键
	 */
	public List getReplyMessage(int messageId) {
		String hql = " from MessageReply messageReply where messageReply.message.messageId='"+messageId+"' order by messageReply.replyId desc";
		List list = getResultByQueryString(hql);
		return list;
	}

}
