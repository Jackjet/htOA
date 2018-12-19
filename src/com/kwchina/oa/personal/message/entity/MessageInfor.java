package com.kwchina.oa.personal.message.entity;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import com.kwchina.core.base.entity.SystemUserInfor;

@Entity
@Table(name = "Personal_MessageInfor", schema = "dbo")
public class MessageInfor {

	private Integer messageId;
	private Timestamp sendTime; // 发送时间
	private String messageTitle; // 信息标题
	private String messageContent; // 信息内容
	private String attachment; // 附件
	private Integer isImportant; // 重要 0-否, 1-是
	private Integer isDelete; // 删除 0-否, 1-是
	private Integer receiveType; // 接收者0-公司全体, 1-自行定义
	private SystemUserInfor sender; // 发送者
	private Set<MessageReply> Replys = new HashSet<MessageReply>(0);
	private Set<ReceiveMessage> Receives = new HashSet<ReceiveMessage>(0);
	private Set<MessageDeleted> Deletes = new HashSet<MessageDeleted>(0);

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getMessageId() {
		return this.messageId;
	}
	public void setMessageId(Integer messageId) {
		this.messageId = messageId;
	}

	public Timestamp getSendTime() {
		return this.sendTime;
	}
	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}

	@Column(columnDefinition = "nvarchar(200)", nullable = false)
	public String getMessageTitle() {
		return this.messageTitle;
	}

	public void setMessageTitle(String messageTitle) {
		this.messageTitle = messageTitle;
	}
	@Column(columnDefinition = "ntext", nullable = false)
	public String getMessageContent() {
		return this.messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}
	@Column(columnDefinition = "nvarchar(2000)")
	public String getAttachment() {
		return this.attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public Integer getIsImportant() {
		return this.isImportant;
	}
	public void setIsImportant(Integer isImportant) {
		this.isImportant = isImportant;
	}

	public Integer getReceiveType() {
		return this.receiveType;
	}

	public void setReceiveType(Integer receiveType) {
		this.receiveType = receiveType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "senderId")
	public SystemUserInfor getSender() {
		return this.sender;
	}
	public void setSender(SystemUserInfor sender) {
		this.sender = sender;
	}
	
	@OneToMany(mappedBy = "message", fetch = FetchType.LAZY)
	@Cascade(value = {org.hibernate.annotations.CascadeType.DELETE_ORPHAN, org.hibernate.annotations.CascadeType.ALL})
	public Set<MessageDeleted> getDeletes() {
		return Deletes;
	}
	public void setDeletes(Set<MessageDeleted> deletes) {
		Deletes = deletes;
	}
	
	@OneToMany(mappedBy = "message", fetch = FetchType.LAZY)
	@Cascade(value = {org.hibernate.annotations.CascadeType.DELETE_ORPHAN, org.hibernate.annotations.CascadeType.ALL})
	public Set<ReceiveMessage> getReceives() {
		return Receives;
	}
	public void setReceives(Set<ReceiveMessage> receives) {
		Receives = receives;
	}
	
	@OneToMany(mappedBy = "message", fetch = FetchType.LAZY)
	@Cascade(value = {org.hibernate.annotations.CascadeType.DELETE_ORPHAN, org.hibernate.annotations.CascadeType.ALL})
	public Set<MessageReply> getReplys() {
		return Replys;
	}
	public void setReplys(Set<MessageReply> replys) {
		Replys = replys;
	}
	public Integer getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

}
