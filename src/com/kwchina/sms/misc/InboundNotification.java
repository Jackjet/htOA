package com.kwchina.sms.misc;

import static com.kwchina.sms.misc.SysConstants.minBalance;
import static com.kwchina.sms.misc.SysConstants.warnOfBalance_No;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import cn.sendsms.IInboundMessageNotification;
import cn.sendsms.InboundMessage;
import cn.sendsms.Message;
import cn.sendsms.MessageClasses;
import cn.sendsms.MessageTypes;
import cn.sendsms.Service;

import com.kwchina.sms.dao.BaseDAO;
import com.kwchina.sms.entity.SMSMessagesReceived;
import com.kwchina.sms.entity.SMSMessagesToSend;

@SuppressWarnings("unchecked")
public class InboundNotification implements IInboundMessageNotification {
	
	
	private Service service = SMSService.getService();

	public void process(String gatewayId, MessageTypes msgType, String memLoc, int memIndex) {
		BaseDAO<SMSMessagesReceived> baseReceiveDao = new BaseDAO(SMSMessagesReceived.class);
		BaseDAO<SMSMessagesToSend> baseSendDao = new BaseDAO(SMSMessagesToSend.class);
		
		List<Message> msgList;	
		
		if (msgType == MessageTypes.INBOUND) {
			//logger.info(">>> 监测到设备收到新的短信: " + gatewayId + " : " + memLoc + " @ " + memIndex);
			msgList = new ArrayList<Message>();
			
			synchronized (this) {
				try {
					service.readMessages(msgList, MessageClasses.UNREAD, gatewayId);
				} catch (Exception e) {
					//logger.error("读取短信异常..." + e.toString() + "||" + e.getMessage());
				}
			}
			
			for (int i = 0; i < msgList.size(); i++) {
				InboundMessage msg = (InboundMessage) msgList.get(i);
				String text = msg.getText();
				//logger.info(msg.getOriginator() + ":" + text);

				/**
				SMSMessagesReceived receivedMessage = new SMSMessagesReceived();
				receivedMessage.setMobileNo(msg.getOriginator());
				receivedMessage.setMessageText(text);
				receivedMessage.setReceiveDate(new Timestamp(msg.getDate().getTime()));
				this.sMSReceiveMessageDAO.save(receivedMessage);
				*/
				
				String insertSQL = "insert into SMS_MessagesReceived(mobileNo,messageText,receiveDate) values(?,?,?)";
				Object params[] = new Object[] { msg.getOriginator(), text, new Timestamp(msg.getDate().getTime()) };
				baseReceiveDao.update(insertSQL, params, false);				
				try {
					service.deleteMessage(msg);
				} catch (Exception e) {
					//logger.error("删除短信异常..." + e.toString());
				}

				if (msg.getOriginator().equals("+10086") && text.startsWith("余额")) {
					String infor = StringUtils.substringBefore(text, "" + (char) (10));
					String balance = StringUtils.substringBetween(infor, "余额：", "元");
					if (StringUtils.isNotEmpty(balance)) {
						if (Float.valueOf(balance) <= minBalance) {
							/**
							SMSMessagesToSend sendMessage = new SMSMessagesToSend();
							sendMessage.setMobileNos(warnOfBalance_No);
							sendMessage.setMessageText(infor);
							sendMessage.setTransmitStatus(0);
							sendMessage.setScheduleDate(new Date(System.currentTimeMillis()));
							sMSSendMessageDAO.save(sendMessage);
							*/
							
							insertSQL = "insert into SMS_MessagesToSend(mobileNos,messageText,transmitStatus,scheduleDate) values(?,?,?,?)";
							params = new Object[] { warnOfBalance_No, infor, 0, new Date(System.currentTimeMillis()) };
							baseSendDao.update(insertSQL, params, false);
							
							//logger.info("余额不足!");
						}
					} else {
						//logger.info("格式错误：" + text);
					}
				}
			}
		} else if (msgType == MessageTypes.STATUSREPORT) {
			//logger.info(">>> 监测到设备收到短信状态报告: " + gatewayId + " : " + memLoc + " @ " + memIndex);
		}
	}
}
