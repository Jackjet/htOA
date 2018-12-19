package com.kwchina.sms.entity;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.kwchina.core.util.annotation.ObjectId;
import com.kwchina.core.util.json.JSONNotAware;

@Entity
@Table(name = "SMS_MessagesToSend", schema = "dbo")
@ObjectId(id="messageId")
public class SMSMessagesToSend implements JSONNotAware {
	
	private Integer messageId;		//信息Id
	private String mobileNos;		//发送手机号
	private String messageText;		//消息文本
	private int transmitStatus;		//发送状态;0:待发送1:成功发送2:发送失败
	private Timestamp scheduleDate;	//计划发送日期
	private int status;		        //信息类型;0:马上发送1:定时发送
	private String applier;		//消息文本

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getMessageId() {
		return messageId;
	}

	public void setMessageId(Integer messageId) {
		this.messageId = messageId;
	}

	@Column(columnDefinition = "nvarchar(200)",nullable =true)
	public String getMobileNos() {
		return mobileNos;
	}

	public void setMobileNos(String mobileNos) {
		this.mobileNos = mobileNos;
	}

	@Column(columnDefinition = "nvarchar(500)",nullable =true)
	public String getMessageText() {
		return messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	
	public int getTransmitStatus() {
		return transmitStatus;
	}

	public void setTransmitStatus(int transmitStatus) {
		this.transmitStatus = transmitStatus;
	}

	public Timestamp getScheduleDate() {
		return scheduleDate;
	}

	public void setScheduleDate(Timestamp scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

	public String getApplier() {
		return applier;
	}

	public void setApplier(String applier) {
		this.applier = applier;
	}
}
