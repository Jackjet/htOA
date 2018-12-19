package com.kwchina.oa.customer.vo;


public class ActivityInforVo {

	private Integer activityId = 0;
	private String planDateStr;	//计划时间
	private String content;	//活动内容
	private String activityPlace;	//活动地点
	private String memo;	//备注
	private String activityDateStr;	//实际时间
	private String feedback;	//后续反馈
	private String attachmentStr;	//附件
	private String companyName;	//公司名称
	private String activityType="0"; //活动状态 0-未完成 1-完成
	private Integer customerId;	//客户
	private String[] attatchmentArray = {}; 	//附件路径
	private String searchYear="";	//搜索年代
	private String searchMonth="";	//搜索月份
	private String type;	//选择查询范围 1-所有活动 2-计划未实施活动 3-实施完成活动
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getActivityPlace() {
		return activityPlace;
	}
	public void setActivityPlace(String activityPlace) {
		this.activityPlace = activityPlace;
	}

	

	public String getAttachmentStr() {
		return attachmentStr;
	}
	public void setAttachmentStr(String attachmentStr) {
		this.attachmentStr = attachmentStr;
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public String getFeedback() {
		return feedback;
	}
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Integer getActivityId() {
		return activityId;
	}
	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}
	public String getPlanDateStr() {
		return planDateStr;
	}
	public void setPlanDateStr(String planDateStr) {
		this.planDateStr = planDateStr;
	}
	public String getActivityDateStr() {
		return activityDateStr;
	}
	public void setActivityDateStr(String activityDateStr) {
		this.activityDateStr = activityDateStr;
	}
	public String getActivityType() {
		return activityType;
	}
	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}
	
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public String[] getAttatchmentArray() {
		return attatchmentArray;
	}
	public void setAttatchmentArray(String[] attatchmentArray) {
		this.attatchmentArray = attatchmentArray;
	}
	public String getSearchMonth() {
		return searchMonth;
	}
	public void setSearchMonth(String searchMonth) {
		this.searchMonth = searchMonth;
	}
	public String getSearchYear() {
		return searchYear;
	}
	public void setSearchYear(String searchYear) {
		this.searchYear = searchYear;
	}
	
	
}
