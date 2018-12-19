package com.kwchina.oa.workflow.vo;


public class InstanceCheckInforVo {
	
	private Integer checkId;					//信息Id
	private String checkComment; 				//审核内容
	private String[] attatchArray = {}; 		//附件路径
	
	
	
	public String[] getAttatchArray() {
		return attatchArray;
	}
	public void setAttatchArray(String[] attatchArray) {
		this.attatchArray = attatchArray;
	}
	public String getCheckComment() {
		return checkComment;
	}
	public void setCheckComment(String checkComment) {
		this.checkComment = checkComment;
	}
	public Integer getCheckId() {
		return checkId;
	}
	public void setCheckId(Integer checkId) {
		this.checkId = checkId;
	}
	
}
