package com.kwchina.oa.personal.message.entity;
// Generated 2008-7-31 12:53:07 by Hibernate Tools 3.2.1.GA

import java.sql.Timestamp;

import javax.management.relation.Role;
import javax.management.relation.RoleInfo;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.kwchina.core.base.entity.RoleInfor;
import com.kwchina.core.base.entity.SystemUserInfor;

@Entity
@Table(name = "Personal_Message_Receive", schema = "dbo")
public class ReceiveMessage {

	private Integer receiveId; 			// Id
	private Integer isReaded; 			// 是否阅读 0-未阅读, 1-已阅读
	private Timestamp readTime; 		// 阅读时间
	private MessageInfor message; 		// 信息
	private SystemUserInfor receiver; 	// 接收者
	private RoleInfor role;              //接收角色

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getReceiveId() {
		return this.receiveId;
	}
	public void setReceiveId(Integer receiveId) {
		this.receiveId = receiveId;
	}

	public Integer getIsReaded() {
		return this.isReaded;
	}
	public void setIsReaded(Integer isReaded) {
		this.isReaded = isReaded;
	}

	public Timestamp getReadTime() {
		return this.readTime;
	}
	public void setReadTime(Timestamp readTime) {
		this.readTime = readTime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "messageId")
	public MessageInfor getMessage() {
		return this.message;
	}
	public void setMessage(MessageInfor message) {
		this.message = message;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "personId")
	public SystemUserInfor getReceiver() {
		return this.receiver;
	}
	public void setReceiver(SystemUserInfor receiver) {
		this.receiver = receiver;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "roleId")
	public RoleInfor getRole() {
		return this.role;
	}
	public void setRole(RoleInfor role) {
		this.role = role;
	}

}
