package com.kwchina.extend.supervise.vo;

import org.hamcrest.core.Is;


public class SuperviseReportVo {
	private Integer reportId;
    private int reportType;      //报告类型 1-进度报告 2-完成报告 3-延期报告  4-行政/党群意见 5-领导意见
    private String content;      //报告内容
    private String managerAdvice;  //部门负责人意见
    private int isPassed;          //是否通过 0-否，1-是
    private String delayDateStr;        //延迟时间
    private String memo;           //备注
    private int isDeleted;         //是否删除 0-否，1-是
    private String[] attatchmentArray = {}; // 附件路径
    private Integer parentId;			//父分类Id
    private int isJudgePassed;           //判断预判是否通过
    private int score;      //所打分数

	private String attachmentStr; // 附件
    private Integer taskId;
    private Integer operatorId;
    
    
	public Integer getReportId() {
		return reportId;
	}
	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}
	public int getReportType() {
		return reportType;
	}
	public void setReportType(int reportType) {
		this.reportType = reportType;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getManagerAdvice() {
		return managerAdvice;
	}
	public void setManagerAdvice(String managerAdvice) {
		this.managerAdvice = managerAdvice;
	}
	public int getIsPassed() {
		return isPassed;
	}
	public void setIsPassed(int isPassed) {
		this.isPassed = isPassed;
	}
	public String getDelayDateStr() {
		return delayDateStr;
	}
	public void setDelayDateStr(String delayDateStr) {
		this.delayDateStr = delayDateStr;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public int getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}
	public String[] getAttatchmentArray() {
		return attatchmentArray;
	}
	public void setAttatchmentArray(String[] attatchmentArray) {
		this.attatchmentArray = attatchmentArray;
	}
	public String getAttachmentStr() {
		return attachmentStr;
	}
	public void setAttachmentStr(String attachmentStr) {
		this.attachmentStr = attachmentStr;
	}
	public Integer getTaskId() {
		return taskId;
	}
	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}
	public Integer getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(Integer operatorId) {
		this.operatorId = operatorId;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public int getIsJudgePassed() {
		return isJudgePassed;
	}
	public void setIsJudgePassed(int isJudgePassed) {
		this.isJudgePassed = isJudgePassed;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
    
    
}
