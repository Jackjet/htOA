package com.kwchina.extend.supervise.vo;

import java.util.List;

public class MSuperviseInforVo{

    private Integer taskId;         //ID
    private String workType;        //工作类别
    private String taskName;		//工作内容和要求
    private String categoryName;    //类别
    
    private String dutyDepartment;  //下达部门
    private String contactPerson;   //下达人

    private Integer departmentId;   //执行部门ID
    private String departmentName;  //执行部门
    private Integer managerId;      //执行部门负责人ID
    private String managerName;     //执行部门负责人

    private String finishDateStr;   //计划完成时间
    private Integer operatorId;     //执行人ID
    private String operatorName;    //执行人
    private String operatorPhoto;   //执行人头像

    private String reportPeriod;    //汇报周期
	private String memo;            //备注
	private String attachment;   //附件

    private String endTimeStr;      //实际完成日期
    private String leaderNames;     //审核领导
    
    private String createDate;       //创建时间
    
    


	private List<MSuperviseReportVo> reportVos;    //进度报告、完成报告


    
	public Integer getTaskId() {
		return taskId;
	}


	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}


	public String getWorkType() {
		return workType;
	}


	public void setWorkType(String workType) {
		this.workType = workType;
	}


	public String getTaskName() {
		return taskName;
	}


	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}


	public String getCategoryName() {
		return categoryName;
	}


	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}


	public String getDutyDepartment() {
		return dutyDepartment;
	}


	public void setDutyDepartment(String dutyDepartment) {
		this.dutyDepartment = dutyDepartment;
	}


	public String getContactPerson() {
		return contactPerson;
	}


	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}


	public Integer getDepartmentId() {
		return departmentId;
	}


	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}


	public String getDepartmentName() {
		return departmentName;
	}


	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}


	public Integer getManagerId() {
		return managerId;
	}


	public void setManagerId(Integer managerId) {
		this.managerId = managerId;
	}


	public String getManagerName() {
		return managerName;
	}


	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}


	public String getFinishDateStr() {
		return finishDateStr;
	}


	public void setFinishDateStr(String finishDateStr) {
		this.finishDateStr = finishDateStr;
	}


	public Integer getOperatorId() {
		return operatorId;
	}


	public void setOperatorId(Integer operatorId) {
		this.operatorId = operatorId;
	}


	public String getOperatorName() {
		return operatorName;
	}


	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}


	public String getReportPeriod() {
		return reportPeriod;
	}


	public void setReportPeriod(String reportPeriod) {
		this.reportPeriod = reportPeriod;
	}


	public String getMemo() {
		return memo;
	}


	public void setMemo(String memo) {
		this.memo = memo;
	}


	public String getAttachment() {
		return attachment;
	}


	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}


	public String getEndTimeStr() {
		return endTimeStr;
	}


	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}


	public String getLeaderNames() {
		return leaderNames;
	}


	public void setLeaderNames(String leaderNames) {
		this.leaderNames = leaderNames;
	}


	public List<MSuperviseReportVo> getReportVos() {
		return reportVos;
	}


	public void setReportVos(List<MSuperviseReportVo> reportVos) {
		this.reportVos = reportVos;
	}

	public String getCreateDate() {
		return createDate;
	}


	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}


	public String getOperatorPhoto() {
		return operatorPhoto;
	}


	public void setOperatorPhoto(String operatorPhoto) {
		this.operatorPhoto = operatorPhoto;
	}
	
	
}


