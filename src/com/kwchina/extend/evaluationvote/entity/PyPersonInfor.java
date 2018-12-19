package com.kwchina.extend.evaluationvote.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.kwchina.core.util.annotation.ObjectId;
import com.kwchina.core.util.json.JSONNotAware;

@Entity
@Table(name = "PY_PersonInfor", schema = "dbo")
@ObjectId(id="pId")
public class PyPersonInfor implements JSONNotAware{
	
	private Integer pId;
	private String department; //部门
	private String descrip; //描述
	private String personName; //人员姓名
	private int displayOrder; //排序
	private PyTopicInfor topicInfor; //所属主题
	
	

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getPId() {
		return pId;
	}
	public void setPId(Integer id) {
		pId = id;
	}
	@Column(columnDefinition = "nvarchar(200)",nullable = false)
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getDescrip() {
		return descrip;
	}
	@Column(columnDefinition = "ntext",nullable = true)
	public void setDescrip(String descrip) {
		this.descrip = descrip;
	}
	
	public int getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}
	@Column(columnDefinition = "nvarchar(200)",nullable = false)
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="topicId", nullable = false)
	public PyTopicInfor getTopicInfor() {
		return topicInfor;
	}
	public void setTopicInfor(PyTopicInfor topicInfor) {
		this.topicInfor = topicInfor;
	}
	
	
	
	
}