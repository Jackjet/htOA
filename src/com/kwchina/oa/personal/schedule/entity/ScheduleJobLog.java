package com.kwchina.oa.personal.schedule.entity;

import java.sql.Timestamp;

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
import com.kwchina.oa.personal.schedule.entity.ScheduleJobInfor;

@Entity
@Table(name = "Personal_Schedule_JobLog", schema = "dbo")
public class ScheduleJobLog implements java.io.Serializable {

	private Integer logId; // 日志Id

	private SystemUserInfor executor; // 执行人员

	private String logContent; // 日志内容

	private String attachment; // 附件

	private Timestamp writeTime; // 填写时间

	private ScheduleJobInfor jobInfor; // 工作Id

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getLogId() {
		return logId;
	}

	public void setLogId(Integer logId) {
		this.logId = logId;
	}

	@Column(columnDefinition = "nvarchar(2000)", nullable = false)
	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "personId", nullable = false)
	public SystemUserInfor getExecutor() {
		return executor;
	}

	public void setExecutor(SystemUserInfor executor) {
		this.executor = executor;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "scheduleId", nullable = false)
	public ScheduleJobInfor getJobInfor() {
		return jobInfor;
	}

	public void setJobInfor(ScheduleJobInfor jobInfor) {
		this.jobInfor = jobInfor;
	}

	@Column(columnDefinition = "ntext")
	public String getLogContent() {
		return logContent;
	}

	public void setLogContent(String logContent) {
		this.logContent = logContent;
	}

	@Column(nullable = false)
	public Timestamp getWriteTime() {
		return writeTime;
	}

	public void setWriteTime(Timestamp writeTime) {
		this.writeTime = writeTime;
	}
}