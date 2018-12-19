package com.kwchina.oa.workflow.customfields.service;

import org.hibernate.mapping.Component;

public interface CustomizableEntityManager {
	
	//自定义组件名称
	public static String CUSTOM_COMPONENT_NAME = "customProperties";
	
	//获取实体类
	public Class getEntityClass();
	
	//获取自定义属性
	public Component getCustomProperties();

	/** 添加自定义字段 
	 * @param name 字段名
	 * @param typeClass 字段类型
	 * @param hbmName hbm文件名
	 * */
	public void addCustomField(String name, Class typeClass, String hbmName);

	/** 删除指定的自定义字段 
	 * @param name 字段名
	 * @param hbmName hbm文件名
	 * */
	public void removeCustomField(String name, String hbmName);

}
