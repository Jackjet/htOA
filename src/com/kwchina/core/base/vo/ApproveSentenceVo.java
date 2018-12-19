package com.kwchina.core.base.vo;

public class ApproveSentenceVo{
	 private Integer sentenceId;
     private String sentence;
     private int orderNo;
     
	public int getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}
	public String getSentence() {
		return sentence;
	}
	public void setSentence(String sentence) {
		this.sentence = sentence;
	}
	public Integer getSentenceId() {
		return sentenceId;
	}
	public void setSentenceId(Integer sentenceId) {
		this.sentenceId = sentenceId;
	}
}