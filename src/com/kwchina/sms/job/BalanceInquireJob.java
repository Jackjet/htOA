package com.kwchina.sms.job;

import java.sql.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.kwchina.sms.dao.BaseDAO;
import com.kwchina.sms.entity.SMSMessagesToSend;

@SuppressWarnings("unchecked")
public class BalanceInquireJob implements Job {
	
	private static final String insertSQL = "insert into MessagesToSend(mobileNos,messageText,transmitStatus,scheduleDate) values(?,?,?,?)";
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		//logger.info("balance inquring ...");
		
		//BaseDAO baseDao = new BaseDAO();
		BaseDAO<SMSMessagesToSend> baseDao = new BaseDAO(SMSMessagesToSend.class);
		Object params[] = new Object[]{"10086","YECX",0,new Date(System.currentTimeMillis())};
		baseDao.update(insertSQL, params, false);
		
		/**
		SMSMessagesToSend sendMessage = new SMSMessagesToSend();
		sendMessage.setMobileNos("10086");
		sendMessage.setMessageText("YECX");
		sendMessage.setTransmitStatus(0);
		sendMessage.setScheduleDate(new Date(System.currentTimeMillis()));
		sMSSendMessageDAO.save(sendMessage);
		*/
		
	}

}
