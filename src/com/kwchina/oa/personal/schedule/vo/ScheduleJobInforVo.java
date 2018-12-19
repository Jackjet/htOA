package com.kwchina.oa.personal.schedule.vo;
public class ScheduleJobInforVo {
	private Integer scheduleId=0;
	private String writeTimeStr; 	// 安排时间
	private String jobTitle; 		// 信息标题
	private String categoryName; 	// 分类名称
	private String startTimeStr; 	// 开始时间
	private String endTimeStr; 		// 结束时间
	private String jobContent; 	    // 工作内容
	private String demand; 	    	// 要求说明
	private String attachmentStr; 	// 附件
	private int status; 			// 状态：0- 正常进行;1- 已经暂停;2- 已经完成
	private int isImportant; 		// 重要：0-不重要, 1-重要
	private int scheduleType; 		// 日程类型：0-自我制定, 1-安排下属
	private int openType; 			// 是否公开：0-不公开, 1-公开
	private int personId;			//安排人员
	private String personName;      //安排人员名
	private int[] personIds;		//工作执行人员
	private String[] attatchmentArray = {}; 	//附件路径
	private String logContent; 	    //日志内容
	
	public String[] getAttatchmentArray() {
		return attatchmentArray;
	}
	public void setAttatchmentArray(String[] attatchmentArray) {
		this.attatchmentArray = attatchmentArray;
	}
	public int[] getPersonIds() {
		return personIds;
	}
	public void setPersonIds(int[] personIds) {
		this.personIds = personIds;
	}
	public String getAttachmentStr() {
		return attachmentStr;
	}
	public void setAttachmentStr(String attachmentStr) {
		this.attachmentStr = attachmentStr;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getDemand() {
		return demand;
	}
	public void setDemand(String demand) {
		this.demand = demand;
	}
	public int getIsImportant() {
		return isImportant;
	}
	public void setIsImportant(int isImportant) {
		this.isImportant = isImportant;
	}
	public String getJobContent() {
		return jobContent;
	}
	public void setJobContent(String jobContent) {
		this.jobContent = jobContent;
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	public int getOpenType() {
		return openType;
	}
	public void setOpenType(int openType) {
		this.openType = openType;
	}
	public int getPersonId() {
		return personId;
	}
	public void setPersonId(int personId) {
		this.personId = personId;
	}
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public Integer getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}
	public int getScheduleType() {
		return scheduleType;
	}
	public void setScheduleType(int scheduleType) {
		this.scheduleType = scheduleType;
	}

	public String getEndTimeStr() {
		return endTimeStr;
	}
	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}
	public String getStartTimeStr() {
		return startTimeStr;
	}
	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}
	public String getWriteTimeStr() {
		return writeTimeStr;
	}
	public void setWriteTimeStr(String writeTimeStr) {
		this.writeTimeStr = writeTimeStr;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getLogContent() {
		return logContent;
	}
	public void setLogContent(String logContent) {
		this.logContent = logContent;
	}
	
	
	
	
	

	
	

}
