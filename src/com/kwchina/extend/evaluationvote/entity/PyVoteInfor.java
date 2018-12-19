package com.kwchina.extend.evaluationvote.entity;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import com.kwchina.core.base.entity.PersonInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.util.annotation.ObjectId;
import com.kwchina.core.util.json.JSONNotAware;

@Entity
@Table(name = "PY_VoteInfor", schema = "dbo")
@ObjectId(id="voteId")
public class PyVoteInfor implements JSONNotAware{
	
	private Integer voteId;
	private Date voteTime;//投票时间
	private PyTopicInfor topicInfor;//评优主题
	private SystemUserInfor person;//投票人
	
	private Set<PyVoteItemInfor> voteItemInfors = new HashSet<PyVoteItemInfor>(0);	//投票信息
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getVoteId() {
		return voteId;
	}
	public void setVoteId(Integer voteId) {
		this.voteId = voteId;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="personId", nullable = false)
	public SystemUserInfor getPerson() {
		return person;
	}
	public void setPerson(SystemUserInfor person) {
		this.person = person;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "topicId")
	public PyTopicInfor getTopicInfor() {
		return topicInfor;
	}
	public void setTopicInfor(PyTopicInfor topicInfor) {
		this.topicInfor = topicInfor;
	}

	public Date getVoteTime() {
		return voteTime;
	}
	public void setVoteTime(Date voteTime) {
		this.voteTime = voteTime;
	}
	
	@OneToMany(mappedBy = "voteInfor",fetch=FetchType.LAZY)	
	@Cascade(value = {org.hibernate.annotations.CascadeType.REMOVE})
	@OrderBy("detailId")
	public Set<PyVoteItemInfor> getVoteItemInfors() {
		return voteItemInfors;
	}
	public void setVoteItemInfors(Set<PyVoteItemInfor> voteItemInfors) {
		this.voteItemInfors = voteItemInfors;
	}
	
	
	
	
	
	
}