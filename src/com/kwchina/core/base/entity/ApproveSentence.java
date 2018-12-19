package com.kwchina.core.base.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "Core_ApproveSentence", schema = "dbo")
public class ApproveSentence{
	 private Integer sentenceId;
     private String sentence;
     private int orderNo;
     
    @Id
 	@GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getSentenceId() {
 		return sentenceId;
 	}
 	public void setSentenceId(Integer sentenceId) {
 		this.sentenceId = sentenceId;
 	} 
 	
	public int getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}
	
	@Column(columnDefinition = "nvarchar(500)",nullable = false)
	public String getSentence() {
		return sentence;
	}
	public void setSentence(String sentence) {
		this.sentence = sentence;
	}
	
	
     
     
	
}