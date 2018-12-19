package com.kwchina.oa.customer.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.kwchina.core.base.entity.SystemUserInfor;

//任务执行人
@Entity
@Table(name = "Customer_ExecutorInfor", schema = "dbo")
public class ExecutorInfor {

	private Integer executorId; // 任务执行人Id
	private TaskInfor taskInfor;// 任务
	private SystemUserInfor executor;// 执行人

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getExecutorId() {
		return executorId;
	}

	public void setExecutorId(Integer executorId) {
		this.executorId = executorId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "taskId")
	public TaskInfor getTaskInfor() {
		return taskInfor;
	}

	public void setTaskInfor(TaskInfor taskInfor) {
		this.taskInfor = taskInfor;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "personId")
	public SystemUserInfor getExecutor() {
		return executor;
	}

	public void setExecutor(SystemUserInfor executor) {
		this.executor = executor;
	}

}
