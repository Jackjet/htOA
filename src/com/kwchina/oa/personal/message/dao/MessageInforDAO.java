package com.kwchina.oa.personal.message.dao;

import com.kwchina.core.common.dao.BasicDao;
import com.kwchina.oa.personal.message.entity.MessageInfor;

public interface MessageInforDAO extends BasicDao<MessageInfor> {

	// 判断接受的消息是否阅读
	public boolean checkIsRead(int messageId, int personId);

}
