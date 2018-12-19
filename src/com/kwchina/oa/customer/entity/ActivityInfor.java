package com.kwchina.oa.customer.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Customer_ActivityInfor", schema = "dbo")
public class ActivityInfor {
	
	private Integer activityId;
	private Date planDate;	//计划时间
	private String content;	//活动内容
	private String activityPlace;	//活动地点
	private String memo;	//备注
	private Date activityDate;	//实际时间
	private String feedback;	//后续反馈
	private String attachment;	//附件
	private CustomerInfor customer;	//客户
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getActivityId() {
		return activityId;
	}
	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}
	
	@Column(columnDefinition = "nvarchar(100)", nullable = false)
	public String getActivityPlace() {
		return activityPlace;
	}
	public void setActivityPlace(String activityPlace) {
		this.activityPlace = activityPlace;
	}
	
	@Column(columnDefinition = "nvarchar(2000)", nullable = true)
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	
	@Column(columnDefinition = "ntext", nullable = true)
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customerId")
	public CustomerInfor getCustomer() {
		return customer;
	}
	public void setCustomer(CustomerInfor customer) {
		this.customer = customer;
	}
	
	@Column(columnDefinition = "ntext", nullable = true)
	public String getFeedback() {
		return feedback;
	}
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}
	
	@Column(columnDefinition = "ntext", nullable = true)
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	public Date getActivityDate() {
		return activityDate;
	}
	public void setActivityDate(Date activityDate) {
		this.activityDate = activityDate;
	}
	
	public Date getPlanDate() {
		return planDate;
	}
	public void setPlanDate(Date planDate) {
		this.planDate = planDate;
	}
	
	
}
