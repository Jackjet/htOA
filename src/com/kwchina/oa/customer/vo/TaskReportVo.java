package com.kwchina.oa.customer.vo;

import java.util.Date;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.oa.customer.entity.ProjectCategory;
import com.kwchina.oa.customer.entity.TaskInfor;

public class TaskReportVo {
	private Integer taskId; // 任务Id
	private Integer reportId; // 报告Id
	private Integer personRepId; // 报告提交人员
	private Date updateDateRep; // 报告提交时间
	private String content; // 报告内容
	private String[] attatchmentArray = {}; 	//附件路径
	private String attachmentStr;	//附件

	public String getAttachmentStr() {
		return attachmentStr;
	}

	public void setAttachmentStr(String attachmentStr) {
		this.attachmentStr = attachmentStr;
	}

	public String[] getAttatchmentArray() {
		return attatchmentArray;
	}

	public void setAttatchmentArray(String[] attatchmentArray) {
		this.attatchmentArray = attatchmentArray;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public Integer getReportId() {
		return reportId;
	}

	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}

	public Integer getPersonRepId() {
		return personRepId;
	}

	public void setPersonRepId(Integer personRepId) {
		this.personRepId = personRepId;
	}

	public Date getUpdateDateRep() {
		return updateDateRep;
	}

	public void setUpdateDateRep(Date updateDateRep) {
		this.updateDateRep = updateDateRep;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}


}
