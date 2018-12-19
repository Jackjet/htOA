package com.kwchina.oa.workflow.vo;



public class FlowInstanceInforVo {

	private Integer instanceId; 				// 审核实例Id
	private Integer managerId;					// 部门审核人一
	private Integer viceManagerId;				// 部门审核人二
	private String instanceTitle; 				// 实例名称
	private String[] attatchArray = {}; 		// 附件路径
	private int[] categoryIds; 					// 归档分类
	private Integer chargerId;					// 主办人Id
	private int[] personIds; 					// 讯息通知人员(归档时使用)
	private String contractNo; 				    // 合同编号
	
	private Integer oldInstanceId; 				// 旧审核实例Id
	
	public String getContractNo() {
		return contractNo;
	}
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}
	public String[] getAttatchArray() {
		return attatchArray;
	}
	public void setAttatchArray(String[] attatchArray) {
		this.attatchArray = attatchArray;
	}
	public Integer getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(Integer instanceId) {
		this.instanceId = instanceId;
	}
	public String getInstanceTitle() {
		return instanceTitle;
	}
	public void setInstanceTitle(String instanceTitle) {
		this.instanceTitle = instanceTitle;
	}
	public Integer getManagerId() {
		return managerId;
	}
	public void setManagerId(Integer managerId) {
		this.managerId = managerId;
	}
	public Integer getViceManagerId() {
		return viceManagerId;
	}
	public void setViceManagerId(Integer viceManagerId) {
		this.viceManagerId = viceManagerId;
	}
	public int[] getCategoryIds() {
		return categoryIds;
	}
	public void setCategoryIds(int[] categoryIds) {
		this.categoryIds = categoryIds;
	}
	public Integer getChargerId() {
		return chargerId;
	}
	public void setChargerId(Integer chargerId) {
		this.chargerId = chargerId;
	}
	public int[] getPersonIds() {
		return personIds;
	}
	public void setPersonIds(int[] personIds) {
		this.personIds = personIds;
	}
	public Integer getOldInstanceId() {
		return oldInstanceId;
	}
	public void setOldInstanceId(Integer oldInstanceId) {
		this.oldInstanceId = oldInstanceId;
	}

	
}
