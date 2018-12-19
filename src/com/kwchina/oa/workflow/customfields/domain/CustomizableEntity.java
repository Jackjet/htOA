package com.kwchina.oa.workflow.customfields.domain;

import java.util.HashMap;
import java.util.Map;

public class CustomizableEntity {

	private int id;
	private int instanceId;			//审核实例Id
	private Map customProperties;	//自定义属性

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(int instanceId) {
		this.instanceId = instanceId;
	}

	public Map getCustomProperties() {
		if (customProperties == null) customProperties = new HashMap();
		return customProperties;
	}

	public void setCustomProperties(Map customProperties) {
		this.customProperties = customProperties;
	}

	/** 获取自定义字段值
	 * @param name 字段名 
	 *  */
	public Object getValueOfCustomField(String name) {
		return getCustomProperties().get(name);
	}

	/** 设置自定义字段值
	 * @param name 字段名
	 * @param value 字段值
	 *  */
	public void setValueOfCustomField(String name, Object value) {
		getCustomProperties().put(name, value);
	}

}
