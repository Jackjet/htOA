package com.kwchina.oa.personal.message.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.kwchina.core.base.entity.SystemUserInfor;
import java.sql.Timestamp;

@Entity
@Table(name = "Personal_Message_Reply", schema = "dbo" )
public class MessageReply {
	private Integer replyId;			//Id
	private Timestamp replyTime;    	//回复时间
	private String  replyContent;		//回复内容
    private MessageInfor message;		//信息
    private SystemUserInfor restorer;	//回复者
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getReplyId() {
		return replyId;
	}
    public void setReplyId(Integer replyId) {
		this.replyId = replyId;
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
	public SystemUserInfor getRestorer() {
		return restorer;
	}
	public void setRestorer(SystemUserInfor restorer) {
		this.restorer = restorer;
	}
	
	@Column(columnDefinition = "ntext",nullable=false)
	public String getReplyContent() {
		return replyContent;
	}
	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}
	
	
    public Timestamp getReplyTime() {
		return replyTime;
	 }
	public void setReplyTime(Timestamp replyTime) {
		this.replyTime = replyTime;
	}
	
    
}
