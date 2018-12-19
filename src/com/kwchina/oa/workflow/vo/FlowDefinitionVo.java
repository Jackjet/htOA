package com.kwchina.oa.workflow.vo;


public class FlowDefinitionVo {
	
	private Integer flowId;			//流程ID
	private String flowName;		//流程名称
	private int status=1;			//状态 (1-启用 0-停用)
	private String memo;			//流程说明
	private int flowType;			//流程类型（0-固定 1-人工）
	private int transType;			//跳转类型（针对节点）
	private String[] attatchmentArray = {}; 	//附件路径
	private int valid;              //是否有效
	private Integer chargerId;	    //主办人
	private String categoryName;	//流程分类,如：收文,发文,报告.
	private Integer fileRoleId;     //归档角色
	private int filerType;          //归档人类型(0-主办人 1-固定角色)
	
	
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getFlowName() {
		return flowName;
	}
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public int getFlowType() {
		return flowType;
	}
	public void setFlowType(int flowType) {
		this.flowType = flowType;
	}
	public int getTransType() {
		return transType;
	}
	public void setTransType(int transType) {
		this.transType = transType;
	}
	public String[] getAttatchmentArray() {
		return attatchmentArray;
	}
	public void setAttatchmentArray(String[] attatchmentArray) {
		this.attatchmentArray = attatchmentArray;
	}
	public int getValid() {
		return valid;
	}
	public void setValid(int valid) {
		this.valid = valid;
	}
	public Integer getChargerId() {
		return chargerId;
	}
	public void setChargerId(Integer chargerId) {
		this.chargerId = chargerId;
	}
	public Integer getFlowId() {
		return flowId;
	}
	public void setFlowId(Integer flowId) {
		this.flowId = flowId;
	}
	public Integer getFileRoleId() {
		return fileRoleId;
	}
	public void setFileRoleId(Integer fileRoleId) {
		this.fileRoleId = fileRoleId;
	}
	public int getFilerType() {
		return filerType;
	}
	public void setFilerType(int filerType) {
		this.filerType = filerType;
	}

}
