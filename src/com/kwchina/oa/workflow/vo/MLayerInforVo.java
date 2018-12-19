package com.kwchina.oa.workflow.vo;

import java.util.List;



public class MLayerInforVo {
	
	private Integer layerId; 		//层次Id
	private String layerName;		//层次名称
	private String checkDemand; 	//审核说明(审核要求)
	
	private String startTimeStr;    //开始时间
	private String endTimeStr;      //结束时间
	private int layerType; 						//层次类型（1-主办人员设置 2-预设的层次）
	private int layer; 							//层次（即第几个审核层次）
	private int status; 						//状态（0-正常 1-中止 2-暂停 3-结束）
	private int forkedType;						//分叉类型（0-普通 1-分叉 2-分叉内层次 3-聚合）
	
	private int flowNodeId;
	private String flowNodeName;
	
	private List<MCheckInforVo> mCheckInfors;    //审核意见

	public Integer getLayerId() {
		return layerId;
	}

	public void setLayerId(Integer layerId) {
		this.layerId = layerId;
	}

	public String getLayerName() {
		return layerName;
	}

	public void setLayerName(String layerName) {
		this.layerName = layerName;
	}

	public String getCheckDemand() {
		return checkDemand;
	}

	public void setCheckDemand(String checkDemand) {
		this.checkDemand = checkDemand;
	}

	public String getStartTimeStr() {
		return startTimeStr;
	}

	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}

	public String getEndTimeStr() {
		return endTimeStr;
	}

	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}

	public int getLayerType() {
		return layerType;
	}

	public void setLayerType(int layerType) {
		this.layerType = layerType;
	}

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getForkedType() {
		return forkedType;
	}

	public void setForkedType(int forkedType) {
		this.forkedType = forkedType;
	}

	public int getFlowNodeId() {
		return flowNodeId;
	}

	public void setFlowNodeId(int flowNodeId) {
		this.flowNodeId = flowNodeId;
	}

	public String getFlowNodeName() {
		return flowNodeName;
	}

	public void setFlowNodeName(String flowNodeName) {
		this.flowNodeName = flowNodeName;
	}

	public List<MCheckInforVo> getMCheckInfors() {
		return mCheckInfors;
	}

	public void setMCheckInfors(List<MCheckInforVo> checkInfors) {
		mCheckInfors = checkInfors;
	}

	
}
