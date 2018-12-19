package com.kwchina.extend.tpwj.entity;



import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
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


import com.kwchina.core.base.entity.SystemUserInfor;

/**
 * 用户投票信息
 * @author suguan
 *
 */
@Entity
@Table(name = "TP_VoteInfor", schema = "dbo")
public class VoteInfor {

    private Integer voteId;
    private java.util.Date voteTime;         //投票时间
    private float voteScore;      //投票分数
    
    private TopicInfor topic;      //所属主题
    private SystemUserInfor voter; //投票人
    
    private Set<VoteItemInfor> voteItems = new HashSet<VoteItemInfor>(0);

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getVoteId() {
        return this.voteId;
    }
    
    public void setVoteId(Integer voteId) {
        this.voteId = voteId;
    }

    
	public java.util.Date getVoteTime() {
		return voteTime;
	}

	public void setVoteTime(java.util.Date voteTime) {
		this.voteTime = voteTime;
	}

	
	public float getVoteScore() {
		return voteScore;
	}

	public void setVoteScore(float voteScore) {
		this.voteScore = voteScore;
	}

	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="topicId", nullable = false)
	public TopicInfor getTopic() {
		return topic;
	}

	public void setTopic(TopicInfor topic) {
		this.topic = topic;
	}

	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="personId", nullable = false)
	public SystemUserInfor getVoter() {
		return voter;
	}

	public void setVoter(SystemUserInfor voter) {
		this.voter = voter;
	}

	
	@OneToMany(mappedBy = "voteInfor",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	public Set<VoteItemInfor> getVoteItems() {
		return voteItems;
	}

	public void setVoteItems(Set<VoteItemInfor> voteItems) {
		this.voteItems = voteItems;
	}

    

}


