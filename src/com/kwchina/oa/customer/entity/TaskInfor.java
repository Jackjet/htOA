package com.kwchina.oa.customer.entity;

import java.util.Date;
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

import org.hibernate.annotations.Cascade;

import com.kwchina.core.base.entity.SystemUserInfor;

//工作任务
@Entity
@Table(name = "Customer_TaskInfor", schema = "dbo")
public class TaskInfor {

	private Integer taskId; // 任务Id
	private String taskCode; // 任务编号
	private ProjectCategory projectCategory; // 项目分类
	private String taskName; // 任务名称
	private String content; // 内容简介及要求
	private Date updateDate; // 提交日期
	private SystemUserInfor checker; // 审核人
	private String checkComment; // 审核意见
	private Date checkTime; // 审核时间
	private Date startDate; // 开始日期
	private Date endDate; // 结束日期
	private int status; // 状态：0- 进行；1- 通过；2- 暂停；3- 过期。
	private SystemUserInfor signer; // 签核人
	private String memo; // 备注
	
	private Set<ExecutorInfor> executorInfors = new HashSet<ExecutorInfor>(0);
	private Set<TaskReport> taskReports = new HashSet<TaskReport>(0);
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	@Column(columnDefinition = "nvarchar(80)", nullable = false)
	public String getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "categoryId")
	public ProjectCategory getProjectCategory() {
		return projectCategory;
	}

	public void setProjectCategory(ProjectCategory projectCategory) {
		this.projectCategory = projectCategory;
	}

	@Column(columnDefinition = "nvarchar(100)", nullable = false)
	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	@Column(columnDefinition = "ntext", nullable = false)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "checkerId")
	public SystemUserInfor getChecker() {
		return checker;
	}

	public void setChecker(SystemUserInfor checker) {
		this.checker = checker;
	}

	@Column(columnDefinition = "ntext", nullable = true)
	public String getCheckComment() {
		return checkComment;
	}

	public void setCheckComment(String checkComment) {
		this.checkComment = checkComment;
	}

	public Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "signerId")
	public SystemUserInfor getSigner() {
		return signer;
	}

	public void setSigner(SystemUserInfor signer) {
		this.signer = signer;
	}

	@Column(columnDefinition = "ntext", nullable = true)
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
	@OneToMany(mappedBy = "taskInfor",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@Cascade(value = {org.hibernate.annotations.CascadeType.DELETE_ORPHAN, org.hibernate.annotations.CascadeType.ALL})
	public Set<ExecutorInfor> getExecutorInfors() {
		return executorInfors;
	}

	public void setExecutorInfors(Set<ExecutorInfor> executorInfors) {
		this.executorInfors = executorInfors;
	}

	@OneToMany(mappedBy = "taskInfor",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	public Set<TaskReport> getTaskReports() {
		return taskReports;
	}

	public void setTaskReports(Set<TaskReport> taskReports) {
		this.taskReports = taskReports;
	}
	
	

}
