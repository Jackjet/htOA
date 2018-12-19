package com.kwchina.extend.evaluationvote.vo;

import com.kwchina.extend.evaluationvote.entity.PyTopicInfor;

public class PyPersonInforVo {
	
	private Integer pId=0;
	private String department; //部门
	private String descrip; //描述
	private String personName; //人员姓名
	private int displayOrder; //排序
	private int topicId; //所属主题
	
	
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getDescrip() {
		return descrip;
	}
	public void setDescrip(String descrip) {
		this.descrip = descrip;
	}
	public int getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public Integer getPId() {
		return pId;
	}
	public void setPId(Integer id) {
		pId = id;
	}
	public int getTopicId() {
		return topicId;
	}
	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}
	
	
	
}