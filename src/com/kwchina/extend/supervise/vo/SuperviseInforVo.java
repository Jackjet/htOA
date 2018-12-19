package com.kwchina.extend.supervise.vo;

public class SuperviseInforVo{

    private Integer taskId;
    private String taskName;		//督办事项
    private String content;         //督办内容
    private String finishDateStr;        //要求完成时间
    private int status;             //状态1-任务下达，待部门负责人指定负责人 2-	已指定，工作进行中 3-已到期，完成报告制作中 4-已提交完成报告，部门审核中 5-行政助理审核中 6-领导审核中 7-延迟工作进行中  8-已结束
    
    private String workType;       //工作类别
    private String dutyDepartment;  //责任部门
    private String contactPerson;   //联系人
    private String reportPeriod;    //汇报周期
    
	private String[] attatchmentArray = {}; // 附件路径

	private String attachmentStr; // 附件
	
	private String memo;

    private int isDeleted;          //是否删除 0-否，1-是
    private Integer taskCategoryId;    //类别
    private Integer createrId;       //创建者
    private Integer leaderId;        //下达任务的领导
    private Integer departmentId;  //责任部门(改为执行部门）
    private Integer managerId;   //责任部门经理
    private Integer operatorId;       //责任人
    
    private String nameScore; //打分

	public String getNameScore() {
		return nameScore;
	}
	public void setNameScore(String nameScore) {
		this.nameScore = nameScore;
	}

	/****批量新增时***/
    private int[] departmentIds = {};
	private int[] managerIds = {};
    
    
    private String statusName;     //状态显示值
    private String createDateStr;  //创建时间
    private String endTimeStr;  //实际完成时间
    private String organizeName;//责任部门
    private String operatorName;//责任人
    private String createrName;  //下发人
    private String createDepartment;//下发人所在部门
    private String categoryName;//类别
    
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getCreateDateStr() {
		return createDateStr;
	}
	public void setCreateDateStr(String createDateStr) {
		this.createDateStr = createDateStr;
	}
	public String getEndTimeStr() {
		return endTimeStr;
	}
	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}
	public String getOrganizeName() {
		return organizeName;
	}
	public void setOrganizeName(String organizeName) {
		this.organizeName = organizeName;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public String getCreaterName() {
		return createrName;
	}
	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public Integer getTaskId() {
		return taskId;
	}
	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
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
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String[] getAttatchmentArray() {
		return attatchmentArray;
	}
	public void setAttatchmentArray(String[] attatchmentArray) {
		this.attatchmentArray = attatchmentArray;
	}
	public int getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}
	public Integer getTaskCategoryId() {
		return taskCategoryId;
	}
	public void setTaskCategoryId(Integer taskCategoryId) {
		this.taskCategoryId = taskCategoryId;
	}
	public Integer getCreaterId() {
		return createrId;
	}
	public void setCreaterId(Integer createrId) {
		this.createrId = createrId;
	}
	public Integer getLeaderId() {
		return leaderId;
	}
	public void setLeaderId(Integer leaderId) {
		this.leaderId = leaderId;
	}
	public Integer getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}
	public Integer getManagerId() {
		return managerId;
	}
	public void setManagerId(Integer managerId) {
		this.managerId = managerId;
	}
	public Integer getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(Integer operatorId) {
		this.operatorId = operatorId;
	}
	public String getAttachmentStr() {
		return attachmentStr;
	}
	public void setAttachmentStr(String attachmentStr) {
		this.attachmentStr = attachmentStr;
	}
	public String getFinishDateStr() {
		return finishDateStr;
	}
	public void setFinishDateStr(String finishDateStr) {
		this.finishDateStr = finishDateStr;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getWorkType() {
		return workType;
	}
	public void setWorkType(String workType) {
		this.workType = workType;
	}
	public String getDutyDepartment() {
		return dutyDepartment;
	}
	public void setDutyDepartment(String dutyDepartment) {
		this.dutyDepartment = dutyDepartment;
	}
	public String getReportPeriod() {
		return reportPeriod;
	}
	public void setReportPeriod(String reportPeriod) {
		this.reportPeriod = reportPeriod;
	}
	public String getCreateDepartment() {
		return createDepartment;
	}
	public void setCreateDepartment(String createDepartment) {
		this.createDepartment = createDepartment;
	}
	public String getContactPerson() {
		return contactPerson;
	}
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	
	public int[] getDepartmentIds() {
		return departmentIds;
	}
	public void setDepartmentIds(int[] departmentIds) {
		this.departmentIds = departmentIds;
	}
	public int[] getManagerIds() {
		return managerIds;
	}
	public void setManagerIds(int[] managerIds) {
		this.managerIds = managerIds;
	}
    
	
}


