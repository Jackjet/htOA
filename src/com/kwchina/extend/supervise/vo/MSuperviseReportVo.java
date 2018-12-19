package com.kwchina.extend.supervise.vo;



public class MSuperviseReportVo {
	private Integer reportId;
	private int reportType;			//报告类型  
									//（原代码释义： 1-进度报告 2-完成报告
									//3-延期报告  4-行政/党群意见 5-领导意见）

	private String reportTypeTitle; //层次名称

    private String createTime;      //报告时间
    private String operator;        //提交人
    private String content;         //报告内容
    private String memo;            //备注
	private String attachment;      //附件
    
    private String managerAdvice;   //部门负责人意见
    private String managerTime;     //部门审核时间
    private String isPassed;        //是否通过 （原代码释义：0-否，1-是）
    
    private String isJudgePassed;   //预判是否通过（原代码释义：0-否，1-是）
    private String judgeTime;       //预判时间 
    
    private String delayDateStr;    //延迟截止时间
    
    private String delayTag;        //标示“以下为延期工作报告”
    
    private boolean canCheckReport; //是否可以部门审核（包含进度报告、完成报告）
    private boolean canJudge;       //是否可以预判
    private boolean canFinalCheck;  //是否可以最终评判
    
    private String operateName;     //操作名称 
    

	public boolean isCanCheckReport() {
		return canCheckReport;
	}

	public void setCanCheckReport(boolean canCheckReport) {
		this.canCheckReport = canCheckReport;
	}

	public boolean isCanJudge() {
		return canJudge;
	}

	public void setCanJudge(boolean canJudge) {
		this.canJudge = canJudge;
	}

	public boolean isCanFinalCheck() {
		return canFinalCheck;
	}

	public void setCanFinalCheck(boolean canFinalCheck) {
		this.canFinalCheck = canFinalCheck;
	}

	public String getOperateName() {
		return operateName;
	}

	public void setOperateName(String operateName) {
		this.operateName = operateName;
	}

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
	
	public String getReportTypeTitle() {
		return reportTypeTitle;
	}

	public void setReportTypeTitle(String reportTypeTitle) {
		this.reportTypeTitle = reportTypeTitle;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public String getManagerAdvice() {
		return managerAdvice;
	}

	public void setManagerAdvice(String managerAdvice) {
		this.managerAdvice = managerAdvice;
	}

	public String getManagerTime() {
		return managerTime;
	}

	public void setManagerTime(String managerTime) {
		this.managerTime = managerTime;
	}

	public String getIsPassed() {
		return isPassed;
	}

	public void setIsPassed(String isPassed) {
		this.isPassed = isPassed;
	}

	public String getIsJudgePassed() {
		return isJudgePassed;
	}

	public void setIsJudgePassed(String isJudgePassed) {
		this.isJudgePassed = isJudgePassed;
	}

	public String getJudgeTime() {
		return judgeTime;
	}

	public void setJudgeTime(String judgeTime) {
		this.judgeTime = judgeTime;
	}

	public String getDelayDateStr() {
		return delayDateStr;
	}

	public void setDelayDateStr(String delayDateStr) {
		this.delayDateStr = delayDateStr;
	}

	public String getDelayTag() {
		return delayTag;
	}

	public void setDelayTag(String delayTag) {
		this.delayTag = delayTag;
	}
    
    
}
