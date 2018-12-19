package com.kwchina.oa.personal.message.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.kwchina.core.base.entity.SystemUserInfor;

@Entity
@Table(name = "Personal_Message_Deleted", schema = "dbo")
public class MessageDeleted {

	private Integer deleteId; // Id
	private MessageInfor message; // 信息
	private SystemUserInfor user; // 用户

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getDeleteId() {
		return deleteId;
	}
	public void setDeleteId(Integer deleteId) {
		this.deleteId = deleteId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "messageId")
	public MessageInfor getMessage() {
		return message;
	}
	public void setMessage(MessageInfor message) {
		this.message = message;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "personId")
	public SystemUserInfor getUser() {
		return user;
	}
	public void setUser(SystemUserInfor user) {
		this.user = user;
	}

}
