package com.kwchina.oa.personal.message.service;

import java.util.List;

import com.kwchina.core.common.service.BasicManager;

public interface MessageReplyManager extends BasicManager {
	// 查看回复的信息
	public List getReplyMessage(int messageId);
}
