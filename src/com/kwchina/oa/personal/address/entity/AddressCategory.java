package com.kwchina.oa.personal.address.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.util.annotation.ObjectId;
import com.kwchina.core.util.json.JSONNotAware;

@Entity
@Table(name = "Personal_Address_Category", schema = "dbo")
@ObjectId(id="categoryId")
public class AddressCategory implements JSONNotAware {
	private Integer categoryId;      //分类Id
	private String categoryName;     //分类名称
	private Integer orderNo;         //排序序号
	private SystemUserInfor person;  //所属用户
	
	@Id
   	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	
	@Column(columnDefinition = "nvarchar(80)",nullable = false)
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	@Column(nullable = false)
	public Integer getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="personId")
	public SystemUserInfor getPerson() {
		return person;
	}
	public void setPerson(SystemUserInfor person) {
		this.person = person;
	}
}
