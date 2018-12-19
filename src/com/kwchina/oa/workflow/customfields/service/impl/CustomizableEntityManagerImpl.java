package com.kwchina.oa.workflow.customfields.service.impl;

import java.util.Iterator;

import org.hibernate.mapping.Column;
import org.hibernate.mapping.Component;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.SimpleValue;

import com.kwchina.oa.workflow.customfields.service.CustomizableEntityManager;
import com.kwchina.oa.workflow.customfields.service.MappingManager;
import com.kwchina.oa.workflow.customfields.util.HibernateUtil;

public class CustomizableEntityManagerImpl implements CustomizableEntityManager {
	
	private Component customProperties;		//自定义属性
	private Class entityClass;				//实体类

	
	public CustomizableEntityManagerImpl(Class entityClass) {
		this.entityClass = entityClass;
	}

	//获取实体类
	public Class getEntityClass() {
		return entityClass;
	}

	//获取自定义属性
	public Component getCustomProperties() {
		if (customProperties == null) {
			Property property = getPersistentClass().getProperty(CUSTOM_COMPONENT_NAME);
			customProperties = (Component) property.getValue();
		}
		return customProperties;
	}

	/** 添加自定义字段 
	 * @param name 字段名
	 * @param typeClass 字段类型
	 * @param hbmName hbm文件名
	 * */
	public void addCustomField(String name, Class typeClass, String hbmName) {
		SimpleValue simpleValue = new SimpleValue();
		simpleValue.addColumn(new Column(name));
		simpleValue.setTypeName(typeClass.getName());

		PersistentClass persistentClass = getPersistentClass();
		simpleValue.setTable(persistentClass.getTable());

		Property property = new Property();
		property.setName(name);
		property.setValue(simpleValue);
		
		//判断是否已存在该字段(已存在则不加入)
		Iterator propertyIterator = getCustomProperties().getPropertyIterator();
		boolean have = false;
		while (propertyIterator.hasNext()) {
			Property tmpProperty = (Property) propertyIterator.next();
			if (tmpProperty.getName().equals(name)) {
				have = true;
				break;
			}
		}
		if (!have) {
			customProperties.addProperty(property);
		}

		updateMapping(hbmName);
	}

	/** 删除指定的自定义字段 
	 * @param name 字段名
	 * @param hbmName hbm文件名
	 * */
	public void removeCustomField(String name, String hbmName) {
		Iterator propertyIterator = customProperties.getPropertyIterator();

		while (propertyIterator.hasNext()) {
			Property property = (Property) propertyIterator.next();
			if (property.getName().equals(name)) {
				propertyIterator.remove();
				updateMapping(hbmName);
				return;
			}
		}
	}
	
	/** 更新映射 
	 * @param hbmName hbm文件名
	 * */
	private synchronized void updateMapping(String hbmName) {
		MappingManager.updateClassMapping(this, hbmName);
		HibernateUtil.getInstance().reset();
	}

	//获取持久化类
	private PersistentClass getPersistentClass() {
		return HibernateUtil.getInstance().getClassMapping(this.entityClass);
	}
}
