package com.kwchina.oa.workflow.vo;



public class InstanceLayerInforVo {
	
	private Integer layerId; 		//层次Id
	private String layerName;		//层次名称
	private String checkDemand; 	//审核说明(审核要求)
	private int[] personIds;		//审核人
	private int[] fromLayerIds;		//来源层Id
	
	
	public int[] getFromLayerIds() {
		return fromLayerIds;
	}
	public void setFromLayerIds(int[] fromLayerIds) {
		this.fromLayerIds = fromLayerIds;
	}
	public int[] getPersonIds() {
		return personIds;
	}
	public void setPersonIds(int[] personIds) {
		this.personIds = personIds;
	}
	public String getCheckDemand() {
		return checkDemand;
	}
	public void setCheckDemand(String checkDemand) {
		this.checkDemand = checkDemand;
	}
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
	
}
