package com.kwchina.oa.personal.message.vo;


public class MessageInforVO {
	private Integer messageId=0;
	private String sendTimeStr; 	// 发送时间
	private String messageTitle; 	// 信息标题
	private String messageContent; 	// 信息内容
	private String attachmentStr; 	// 附件
	private int isImportant; 		// 重要 0-否, 1-是
	private int receiveType; 		// 接收者0-公司全体, 1-自行定义,  2-分组接收者
	private int senderId;			//发送者
	private String senderName;			
	private int[] personIds;		//接收者
	private int[] roles;            //分组接收者
	private String[] attatchmentArray = {}; 	//附件路径
	private int isReaded;			//是否阅读
	private String 	replyContent;   //回复内容
	private int isReply;			//是否有回复 0-没有,1-有
	private String personNames;		//接收者姓名

	
	
	public String getPersonNames() {
		return personNames;
	}
	public void setPersonNames(String personNames) {
		this.personNames = personNames;
	}
	public int getIsReply() {
		return isReply;
	}
	public void setIsReply(int isReply) {
		this.isReply = isReply;
	}
	public String getReplyContent() {
		return replyContent;
	}
	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}
	public int getIsReaded() {
		return isReaded;
	}
	public void setIsReaded(int isReaded) {
		this.isReaded = isReaded;
	}
	public String getSendTimeStr() {
		return sendTimeStr;
	}
	public void setSendTimeStr(String sendTimeStr) {
		this.sendTimeStr = sendTimeStr;
	}
	public String getAttachmentStr() {
		return attachmentStr;
	}
	public void setAttachmentStr(String attachmentStr) {
		this.attachmentStr = attachmentStr;
	}
	
	public String getMessageContent() {
		return messageContent;
	}
	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}
	public Integer getMessageId() {
		return messageId;
	}
	public void setMessageId(Integer messageId) {
		this.messageId = messageId;
	}
	public String getMessageTitle() {
		return messageTitle;
	}
	public void setMessageTitle(String messageTitle) {
		this.messageTitle = messageTitle;
	}
	
	public int getSenderId() {
		return senderId;
	}
	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}
	public void setIsImportant(int isImportant) {
		this.isImportant = isImportant;
	}
	public void setReceiveType(int receiveType) {
		this.receiveType = receiveType;
	}
	public int[] getPersonIds() {
		return personIds;
	}
	public void setPersonIds(int[] personIds) {
		this.personIds = personIds;
	}
	public int getIsImportant() {
		return isImportant;
	}
	public int getReceiveType() {
		return receiveType;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String[] getAttatchmentArray() {
		return attatchmentArray;
	}
	public void setAttatchmentArray(String[] attatchmentArray) {
		this.attatchmentArray = attatchmentArray;
	}
	public int[] getRoles() {
		return roles;
	}
	public void setRoles(int[] roles) {
		this.roles = roles;
	}
	

}
