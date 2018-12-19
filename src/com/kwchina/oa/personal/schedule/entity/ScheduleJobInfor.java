package com.kwchina.oa.personal.schedule.entity;

import java.sql.Date;
import java.sql.Timestamp;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.oa.personal.schedule.entity.ScheduleExcuter;
import com.kwchina.oa.personal.schedule.entity.ScheduleJobLog;

@Entity
@Table(name = "Personal_Schedule_JobInfor", schema = "dbo")
public class ScheduleJobInfor {

	public static int Self_Job = 0; // 0- 自我制定

	public static int Excuter_Job = 1; // 1- 安排下属工作

	public static int Type_Secret = 0; // 0- 不公开

	public static int Type_Public = 1; // 1- 全公司公开

	public static int Status_Underway = 0; // 0- 正常进行

	public static int Status_Complete = 1; // 1- 已经完成

	public static int Status_Overdate = 2; // 2- 已经过期

	public static int Status_Pause = 3; // 3- 暂时停止

	private Integer scheduleId;

	private String jobTitle; // 工作标题

	private String categoryName; // 分类名称

	private Timestamp writeTime; // 安排时间

	private Timestamp startDate; // 开始时间

	private Timestamp endDate; // 结束时间

	private String jobContent; // 工作内容

	private String demand; // 要求说明

	private String attachment; // 附件

	private SystemUserInfor assigner; // 安排人员Id

	private int isImportant; // 重要性: 0- 不重要;1- 重要

	private int status; // 任务状态: 0- 正常进行;1- 已经暂停;2- 已经完成

	private int scheduleType; // 日程类型: 0- 自我制定;1- 安排下属工作

	private int openType; // 公开模式：0- 不公开;1- 全公司公开

	private Set<ScheduleExcuter> scheduleExcuters = new HashSet<ScheduleExcuter>(0);

	private Set<ScheduleJobLog> scheduleJobLog = new HashSet<ScheduleJobLog>(0);

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}

	@Column(columnDefinition = "nvarchar(100)", nullable = false)
	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	@Column(columnDefinition = "nvarchar(100)")
	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Timestamp getWriteTime() {
		return writeTime;
	}

	public void setWriteTime(Timestamp writeTime) {
		this.writeTime = writeTime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "personId", nullable = false)
	public SystemUserInfor getAssigner() {
		return assigner;
	}

	public void setAssigner(SystemUserInfor assigner) {
		this.assigner = assigner;
	}

	@Column(columnDefinition = "nvarchar(2000)")
	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	@Column(columnDefinition = "ntext")
	public String getDemand() {
		return demand;
	}

	public void setDemand(String demand) {
		this.demand = demand;
	}

	public int getIsImportant() {
		return isImportant;
	}

	public void setIsImportant(int isImportant) {
		this.isImportant = isImportant;
	}

	@Column(columnDefinition = "ntext")
	public String getJobContent() {
		return jobContent;
	}

	public void setJobContent(String jobContent) {
		this.jobContent = jobContent;
	}

	public int getOpenType() {
		return openType;
	}

	public void setOpenType(int openType) {
		this.openType = openType;
	}

	@OneToMany(mappedBy = "jobInfor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@Cascade(value = { org.hibernate.annotations.CascadeType.DELETE_ORPHAN,
			org.hibernate.annotations.CascadeType.ALL })
	public Set<ScheduleExcuter> getScheduleExcuters() {
		return scheduleExcuters;
	}

	public void setScheduleExcuters(Set<ScheduleExcuter> scheduleExcuters) {
		this.scheduleExcuters = scheduleExcuters;
	}

	public int getScheduleType() {
		return scheduleType;
	}

	public void setScheduleType(int scheduleType) {
		this.scheduleType = scheduleType;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	// @Temporal(TemporalType.TIMESTAMP)
	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@OneToMany(mappedBy = "jobInfor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@Cascade(value = { org.hibernate.annotations.CascadeType.DELETE_ORPHAN,
			org.hibernate.annotations.CascadeType.ALL })
	public Set<ScheduleJobLog> getScheduleJobLog() {
		return scheduleJobLog;
	}

	public void setScheduleJobLog(Set<ScheduleJobLog> scheduleJobLog) {
		this.scheduleJobLog = scheduleJobLog;
	}

}