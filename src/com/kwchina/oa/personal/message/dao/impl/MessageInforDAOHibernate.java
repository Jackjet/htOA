package com.kwchina.oa.personal.message.dao.impl;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.kwchina.core.common.dao.BasicDaoImpl;
import com.kwchina.oa.personal.message.dao.MessageInforDAO;
import com.kwchina.oa.personal.message.entity.MessageInfor;
import com.kwchina.oa.personal.message.entity.ReceiveMessage;

@Repository
public class MessageInforDAOHibernate extends BasicDaoImpl<MessageInfor> implements MessageInforDAO {
	
	/***************************************************************************
	 * 判断消息是否阅读
	 * 
	 * @param messageId:消息主键
	 * @return flage:boolean-阅读了 false-没有阅读
	 */
	public boolean checkIsRead(int messageId, int personId) {
		boolean flage = false;
		int isRead;
		String hql = "from ReceiveMessage receiveMessage where receiveMessage.message.messageId='" + messageId + "' and receiveMessage.receiver.personId='" + personId + "'";
		List list = getResultByQueryString(hql);
		if (list.size() == 0) {
			isRead = 0;
		} else {
			ReceiveMessage receiveMessage = (ReceiveMessage) list.get(0);
			isRead = receiveMessage.getIsReaded();
		}
		// 如果取出数据为1，则表示该信息已经阅读过了
		if (isRead == 1) {
			flage = true;
		}
		return flage;
	}

}
