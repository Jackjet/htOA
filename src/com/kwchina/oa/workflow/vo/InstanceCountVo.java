package com.kwchina.oa.workflow.vo;

import java.util.List;

public class InstanceCountVo {
	private Integer instanceId;
	private String instanceTitle;  //标题
	private Integer categoryId;     //类别ID
	private String categoryName;   //类别
	private List<InstanceCountLayerVo> layers;  //审核层的信息
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
	public List<InstanceCountLayerVo> getLayers() {
		return layers;
	}
	public void setLayers(List<InstanceCountLayerVo> layers) {
		this.layers = layers;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	
	
}
