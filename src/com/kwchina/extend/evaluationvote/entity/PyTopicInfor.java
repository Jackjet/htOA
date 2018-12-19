package com.kwchina.extend.evaluationvote.entity;

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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@Table(name = "PY_TopicInfor", schema = "dbo")
@ObjectId(id="topicId")
public class PyTopicInfor implements JSONNotAware{
	
	private Integer topicId;
	private String topicName; //投票标题
	private String descrip;//描述
	private Date startTime;//开始时间
	private Date endTime;//结束时间
	private Date createTime;//创建时间
	private  SystemUserInfor person;//创建者
	private boolean isValid;//有效性 0-无效 1-有效
	private String ruler;//投票规则
	private int openType;				//角色类型:0-自定义;1-全体用户.

    private boolean homepage;              //是否首页公示  0-否，1-是
    
    private boolean  sameDept;
    private java.util.Date publicTime;// 公示开始日期
	private Set<PyTopicInforRight> rights = new HashSet<PyTopicInforRight>(0);
	
	private Set<PyItemInfor> itemInfors = new HashSet<PyItemInfor>(0);	//条目信息
	
	private Set<PyPersonInfor> personInfors = new HashSet<PyPersonInfor>(0);	//人员信息
	
	private Set<PyVoteInfor> voteInfors = new HashSet<PyVoteInfor>(0);	//投票信息
    
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getTopicId() {
		return topicId;
	}
	public void setTopicId(Integer topicId) {
		this.topicId = topicId;
	}
	
	@Column(columnDefinition = "nvarchar(200)",nullable = false)
	public String getTopicName() {
		return topicName;
	}
	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Column(columnDefinition = "ntext",nullable = true)
	public String getDescrip() {
		return descrip;
	}
	public void setDescrip(String descrip) {
		this.descrip = descrip;
	}
	
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public boolean isValid() {
		return isValid;
	}
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="personId", nullable = false)
	public SystemUserInfor getPerson() {
		return person;
	}
	public void setPerson(SystemUserInfor person) {
		this.person = person;
	}
	
	@OneToMany(mappedBy = "topicInfor",fetch = FetchType.LAZY)
	@Cascade(value = {org.hibernate.annotations.CascadeType.DELETE_ORPHAN, org.hibernate.annotations.CascadeType.ALL})
	public Set<PyTopicInforRight> getRights() {
		return rights;
	}
	public void setRights(Set<PyTopicInforRight> rights) {
		this.rights = rights;
	}
	
	@Column(columnDefinition = "ntext",nullable = true)
	public String getRuler() {
		return ruler;
	}
	public void setRuler(String ruler) {
		this.ruler = ruler;
	}
	
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public int getOpenType() {
		return openType;
	}
	public void setOpenType(int openType) {
		this.openType = openType;
	}
	
	@OneToMany(mappedBy = "topicInfor",fetch=FetchType.LAZY)	
	@Cascade(value = {org.hibernate.annotations.CascadeType.REMOVE})
	@OrderBy("displayOrder")
	public Set<PyItemInfor> getItemInfors() {
		return itemInfors;
	}
	public void setItemInfors(Set<PyItemInfor> itemInfors) {
		this.itemInfors = itemInfors;
	}
	
	@OneToMany(mappedBy = "topicInfor",fetch=FetchType.LAZY)	
	@Cascade(value = {org.hibernate.annotations.CascadeType.REMOVE})
	@OrderBy("displayOrder")
	public Set<PyPersonInfor> getPersonInfors() {
		return personInfors;
	}
	public void setPersonInfors(Set<PyPersonInfor> personInfors) {
		this.personInfors = personInfors;
	}
	
	@OneToMany(mappedBy = "topicInfor",fetch=FetchType.LAZY)	
	@Cascade(value = {org.hibernate.annotations.CascadeType.REMOVE})
	@OrderBy("voteId")
	public Set<PyVoteInfor> getVoteInfors() {
		return voteInfors;
	}
	public void setVoteInfors(Set<PyVoteInfor> voteInfors) {
		this.voteInfors = voteInfors;
	}

	
	public boolean isHomepage() {
		return homepage;
	}
	public void setHomepage(boolean homepage) {
		this.homepage = homepage;
	}
	public java.util.Date getPublicTime() {
		return publicTime;
	}
	public void setPublicTime(java.util.Date publicTime) {
		this.publicTime = publicTime;
	}
	public boolean isSameDept() {
		return sameDept;
	}
	public void setSameDept(boolean sameDept) {
		this.sameDept = sameDept;
	}

    
    
	
	
	
	
}