package com.kwchina.oa.workflow.vo;



public class MCheckInforVo {
	
	private Integer checkId;					//信息Id
	private String checkComment; 				//审核内容
	private String[] attatchArray = {}; 		//附件路径
	
	private String startDateStr; 					//开始时间
	private String endDateStr; 						//审核时间（结束时间）
	private String attatchment; 					//附件路径
	private int status;								//状态(0-尚未审核 1-已审核 2-暂停 3-取消/中止)
	
	private int checkerId;
	private String checkerName; 
	
	public int getCheckerId() {
		return checkerId;
	}
	public void setCheckerId(int checkerId) {
		this.checkerId = checkerId;
	}
	public String getCheckerName() {
		return checkerName;
	}
	public void setCheckerName(String checkerName) {
		this.checkerName = checkerName;
	}
	public Integer getCheckId() {
		return checkId;
	}
	public void setCheckId(Integer checkId) {
		this.checkId = checkId;
	}
	public String getCheckComment() {
		return checkComment;
	}
	public void setCheckComment(String checkComment) {
		this.checkComment = checkComment;
	}
	public String[] getAttatchArray() {
		return attatchArray;
	}
	public void setAttatchArray(String[] attatchArray) {
		this.attatchArray = attatchArray;
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
	public String getAttatchment() {
		return attatchment;
	}
	public void setAttatchment(String attatchment) {
		this.attatchment = attatchment;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	
	
}
