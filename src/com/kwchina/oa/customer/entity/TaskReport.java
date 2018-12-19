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

import com.kwchina.core.base.entity.SystemUserInfor;

//任务报告
@Entity
@Table(name = "Customer_TaskReport", schema = "dbo")
public class TaskReport {

	private Integer reportId; // Id
	private SystemUserInfor person; // 人员
	private TaskInfor taskInfor; // 任务
	private Date updateDate; // 提交时间
	private String content; // 报告内容
	private String attachment; // 附件

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getReportId() {
		return reportId;
	}

	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "personId")
	public SystemUserInfor getPerson() {
		return person;
	}

	public void setPerson(SystemUserInfor person) {
		this.person = person;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "taskId")
	public TaskInfor getTaskInfor() {
		return taskInfor;
	}

	public void setTaskInfor(TaskInfor taskInfor) {
		this.taskInfor = taskInfor;
	}

	@Column(columnDefinition = "nvarchar(2000)", nullable = true)
	public String getAttachment() {
		return attachment;
	}

	
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Column(columnDefinition = "ntext", nullable = true)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
