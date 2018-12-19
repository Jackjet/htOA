package com.kwchina.extend.evaluationvote.vo;

import java.sql.Date;

import com.kwchina.core.base.entity.PersonInfor;
import com.kwchina.extend.evaluationvote.entity.PyTopicInfor;

public class PyVoteInforVo {
	private Integer voteId=0;
	private String voteTime;//投票时间
	private int topicId;//评优主题
	private int personId;//投票人
	
	public int getPersonId() {
		return personId;
	}
	public void setPersonId(int personId) {
		this.personId = personId;
	}
	public int getTopicId() {
		return topicId;
	}
	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}
	public Integer getVoteId() {
		return voteId;
	}
	public void setVoteId(Integer voteId) {
		this.voteId = voteId;
	}
	public String getVoteTime() {
		return voteTime;
	}
	public void setVoteTime(String voteTime) {
		this.voteTime = voteTime;
	}
	
	
}