package com.kwchina.oa.workflow.vo;

public class FlowNodeVo {
	
	private Integer nodeId;
	private Integer flowId;       //所属流程ID
	private String flowNodeName;  //节点名称(不能命名为nodeName,此名称在页面上使用时与jquery中的变量名冲突)
	private String memo;          //节点说明
	private int nodeType;         //节点类型（1-普通，2-分叉，3-聚合，4-状态）
	private int finishType;    	  //审核结束类型（并和或的关系）
	private int checkerType;      //节点审核者类型
	private int structure_;       //审核岗位
	private int user_;            //审核人
	private int department_;      //审核部门（部门领导）
	private int special;          //特殊审核人
	private int layer;            //层次
	private int forked;           //是否分叉内节点
	private int forkedNodeId;     //来自分叉的节点
	private int printable=1;      //允许打印（0-不允许，1-允许）
	private int download=1;       //可下载附件（0-不可下载，1-可下载）
	private int upload=1;         //可上传附件（0-不可上传，1-可上传）
	private int[] fromNodeIds;	  //来源节点Id
	private int[] userIds;	  	  //审核人
	private int[] roleIds;	  	  //审核角色
	
	
	
	public String getFlowNodeName() {
		return flowNodeName;
	}
	public void setFlowNodeName(String flowNodeName) {
		this.flowNodeName = flowNodeName;
	}
	public Integer getNodeId() {
		return nodeId;
	}
	public void setNodeId(Integer nodeId) {
		this.nodeId = nodeId;
	}
	public Integer getFlowId() {
		return flowId;
	}
	public void setFlowId(Integer flowId) {
		this.flowId = flowId;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public int getNodeType() {
		return nodeType;
	}
	public void setNodeType(int nodeType) {
		this.nodeType = nodeType;
	}
	public int getFinishType() {
		return finishType;
	}
	public void setFinishType(int finishType) {
		this.finishType = finishType;
	}
	public int getCheckerType() {
		return checkerType;
	}
	public void setCheckerType(int checkerType) {
		this.checkerType = checkerType;
	}
	public int getStructure_() {
		return structure_;
	}
	public void setStructure_(int structure_) {
		this.structure_ = structure_;
	}
	public int getUser_() {
		return user_;
	}
	public void setUser_(int user_) {
		this.user_ = user_;
	}
	public int getDepartment_() {
		return department_;
	}
	public void setDepartment_(int department_) {
		this.department_ = department_;
	}
	public int getSpecial() {
		return special;
	}
	public void setSpecial(int special) {
		this.special = special;
	}
	public int getLayer() {
		return layer;
	}
	public void setLayer(int layer) {
		this.layer = layer;
	}
	public int getForkedNodeId() {
		return forkedNodeId;
	}
	public void setForkedNodeId(int forkedNodeId) {
		this.forkedNodeId = forkedNodeId;
	}
	public int getForked() {
		return forked;
	}
	public void setForked(int forked) {
		this.forked = forked;
	}
	public int getPrintable() {
		return printable;
	}
	public void setPrintable(int printable) {
		this.printable = printable;
	}
	public int getDownload() {
		return download;
	}
	public void setDownload(int download) {
		this.download = download;
	}
	public int getUpload() {
		return upload;
	}
	public void setUpload(int upload) {
		this.upload = upload;
	}
	public int[] getFromNodeIds() {
		return fromNodeIds;
	}
	public void setFromNodeIds(int[] fromNodeIds) {
		this.fromNodeIds = fromNodeIds;
	}
	public int[] getRoleIds() {
		return roleIds;
	}
	public void setRoleIds(int[] roleIds) {
		this.roleIds = roleIds;
	}
	public int[] getUserIds() {
		return userIds;
	}
	public void setUserIds(int[] userIds) {
		this.userIds = userIds;
	}
}
