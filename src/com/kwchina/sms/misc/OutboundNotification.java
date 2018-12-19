package com.kwchina.sms.misc;

import cn.sendsms.IOutboundMessageNotification;
import cn.sendsms.OutboundMessage;



@SuppressWarnings("unchecked")
public class OutboundNotification implements IOutboundMessageNotification {

	public void process(String gatewayId, OutboundMessage msg) {
		
		//logger.info("已发送短信：" + msg.getRecipient() + "---" + msg.getText());
		
		/**
		BaseDAO baseDAO = new BaseDAO();
		String updateSQL = "update MessagesToSend set transmitStatus = 1 where messageId = ?";
		Object params[] = new Object[] {msg.getId()};
		baseDAO.update(updateSQL, params, false);
		*/
	}
}
