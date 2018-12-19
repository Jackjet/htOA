package com.kwchina.oa.customer.vo;

import java.util.Date;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.oa.customer.entity.ProjectCategory;
import com.kwchina.oa.customer.entity.TaskInfor;

public class TaskInforVo {
	private Integer taskId; // 任务Id
	private String taskCode; // 任务编号
	private Integer categoryId; // 项目分类
	private String categoryName; // 项目分类名称
	private String taskName; // 任务名称
	private String content; // 内容简介及要求
	private String updateDateStr; // 提交日期
	private Integer checkerId; // 审核人
	private String checkComment; // 审核意见
	private String checkTimeStr; // 审核时间
	private String startDateStr; // 开始日期
	private String endDateStr; // 结束日期
	private int status; // 状态：0- 进行；1- 通过；2- 暂停；3- 过期。
	private Integer signerId; // 签核人
	private String memo; // 备注
	
	private Integer flag;	//用于判断是修改任务还是增加执行人 0-修改任务 1-增加执行人

	private int[] executorIds; // 执行人主键

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public String getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getCheckerId() {
		return checkerId;
	}

	public void setCheckerId(Integer checkerId) {
		this.checkerId = checkerId;
	}

	public String getCheckComment() {
		return checkComment;
	}

	public void setCheckComment(String checkComment) {
		this.checkComment = checkComment;
	}

	public String getUpdateDateStr() {
		return updateDateStr;
	}

	public void setUpdateDateStr(String updateDateStr) {
		this.updateDateStr = updateDateStr;
	}

	public String getCheckTimeStr() {
		return checkTimeStr;
	}

	public void setCheckTimeStr(String checkTimeStr) {
		this.checkTimeStr = checkTimeStr;
	}

	public String getStartDateStr() {
		return startDateStr;
	}

	public void setStartDateStr(String startDateStr) {
		this.startDateStr = startDateStr;
	}

	public String getEndDateStr() {
		return endDateStr;
	}

	public void setEndDateStr(String endDateStr) {
		this.endDateStr = endDateStr;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Integer getSignerId() {
		return signerId;
	}

	public void setSignerId(Integer signerId) {
		this.signerId = signerId;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public int[] getExecutorIds() {
		return executorIds;
	}

	public void setExecutorIds(int[] executorIds) {
		this.executorIds = executorIds;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

}
