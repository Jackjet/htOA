package com.kwchina.oa.personal.schedule.entity;

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
@Table(name = "Personal_Schedule_Excuter", schema = "dbo")
public class ScheduleExcuter implements java.io.Serializable {

	private Integer excuterId; // 信息Id

	private SystemUserInfor executor; // 执行人员

	private ScheduleJobInfor jobInfor; // 工作Id

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getExcuterId() {
		return excuterId;
	}
	public void setExcuterId(Integer excuterId) {
		this.excuterId = excuterId;
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

}
