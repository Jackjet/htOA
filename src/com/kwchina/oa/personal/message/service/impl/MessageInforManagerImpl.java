package com.kwchina.oa.personal.message.service.impl;

import java.text.SimpleDateFormat;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.base.dao.SystemUserInforDAO;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.personal.message.dao.MessageInforDAO;
import com.kwchina.oa.personal.message.entity.MessageInfor;
import com.kwchina.oa.personal.message.service.MessageInforManager;
import com.kwchina.oa.personal.message.vo.MessageInforVO;
import com.kwchina.oa.sys.SystemConstant;

@Service
public class MessageInforManagerImpl extends BasicManagerImpl<MessageInfor> implements MessageInforManager {
	
	
	private  MessageInforDAO messageInforDAO;
	
	@Autowired
	public void setMessageInforDAO(MessageInforDAO messageInforDAO) {
		this.messageInforDAO = messageInforDAO;
		super.setDao(messageInforDAO);
	}

	// 转化MessageInfor为MessageInforVO
	public MessageInforVO transPOToVO(MessageInfor message) {
		MessageInforVO messageVo = new MessageInforVO();

		try {
			BeanUtils.copyProperties(messageVo, message);
			messageVo.setSenderId(message.getSender().getPersonId());
			messageVo.setAttachmentStr(message.getAttachment());
			messageVo.setSenderName(message.getSender().getPerson().getPersonName());
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String sendTimeStr = dateFormat.format(message.getSendTime());
			messageVo.setSendTimeStr(sendTimeStr);
		} catch (Exception ex) {

		}

		return messageVo;
	}

	/***************************************************************************
	 * 判断用户是否有权限对讯息进行修改或者删除（如果是讯息作者或者是管理员，则有权限对其进行操作）
	 */
	public boolean canDeleteOrEditMessage(SystemUserInfor user, MessageInfor message) {
		boolean flage = false;
		// 用户为发送者或管理员
		int sendId = message.getSender().getPersonId();
		int personId = user.getPersonId();
		if (sendId == personId || user.getUserType() == SystemConstant._User_Type_Admin) {
			flage = true;
		}

		return flage;
	}

}
