package com.kwchina.extend.tpwj.entity;


import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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


import com.kwchina.core.base.entity.SystemUserInfor;
import com.sun.faces.mgbean.ManagedBeanPreProcessingException.Type;

/**
 * 主题信息
 * @author suguan
 *
 */
@Entity
@Table(name = "TP_TopicInfor", schema = "dbo")
public class TopicInfor {

    private Integer topicId;
    private String topicName;		    //投票标题
    private String descrip;		        //描述
    private String rules;                //投票规则
    private Date startTime;			    //开始时间
    private Date endTime;               //结束时间
    private java.util.Date createTime;            //创建时间
    private int openType;				//开放类型:0-自定义;1-全体用户.
    private boolean valid;			        //是否有效:0-无效;1-有效.
    
    private boolean deleted;             //是否删除
    
    private boolean checkCount;         //是否允许投票者查看统计结果
    
    private int type;                   //类型  0-投票，1-问卷
    
    private boolean display;              //是否首页公示  0-否，1-是
    private java.util.Date displayDate;// 公示开始日期
    
    private SystemUserInfor creater;	//创建者
    private Set<VoteInfor> voteInfors = new HashSet<VoteInfor>(0);
    private Set<ItemInfor> items = new HashSet<ItemInfor>(0);
    

    private Set<TopicRight> rights = new HashSet<TopicRight>(0);

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getTopicId() {
        return this.topicId;
    }
    
    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    
    @Column(columnDefinition = "nvarchar(500)",nullable = false)
	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	
	@Column(columnDefinition = "ntext")
	public String getDescrip() {
		return descrip;
	}

	public void setDescrip(String descrip) {
		this.descrip = descrip;
	}

	
	@Column(columnDefinition = "ntext")
	public String getRules() {
		return rules;
	}

	public void setRules(String rules) {
		this.rules = rules;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public java.util.Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}


	
	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="personId", nullable = false)
	public SystemUserInfor getCreater() {
		return creater;
	}

	public void setCreater(SystemUserInfor creater) {
		this.creater = creater;
	}
    
	
//	@ManyToMany(targetEntity = SystemUserInfor.class,cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//	@JoinTable(name = "TP_TopicInfor_Users",schema = "dbo",joinColumns = {@JoinColumn(name = "topicId")},inverseJoinColumns = {@JoinColumn(name = "personId")})
//	@OrderBy("personId")
//	public Set<SystemUserInfor> getUsers() {
//        return this.users;
//    }
//    
//    public void setUsers(Set<SystemUserInfor> users) {
//        this.users = users;
//    }

	public int getOpenType() {
		return openType;
	}

	public void setOpenType(int openType) {
		this.openType = openType;
	}

	
	@OneToMany(mappedBy = "topic",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@OrderBy("displayOrder")
	public Set<ItemInfor> getItems() {
		return items;
	}

	public void setItems(Set<ItemInfor> items) {
		this.items = items;
	}

	@OneToMany(mappedBy = "topic",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@OrderBy("voteTime")
	public Set<VoteInfor> getVoteInfors() {
		return voteInfors;
	}

	public void setVoteInfors(Set<VoteInfor> voteInfors) {
		this.voteInfors = voteInfors;
	}

	
	@OneToMany(mappedBy = "topic",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@Cascade(value = {org.hibernate.annotations.CascadeType.DELETE_ORPHAN, org.hibernate.annotations.CascadeType.ALL})
	public Set<TopicRight> getRights() {
		return rights;
	}

	public void setRights(Set<TopicRight> rights) {
		this.rights = rights;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}


	public java.util.Date getDisplayDate() {
		return displayDate;
	}

	public void setDisplayDate(java.util.Date displayDate) {
		this.displayDate = displayDate;
	}

	public boolean isDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean isCheckCount() {
		return checkCount;
	}

	public void setCheckCount(boolean checkCount) {
		this.checkCount = checkCount;
	}

}


