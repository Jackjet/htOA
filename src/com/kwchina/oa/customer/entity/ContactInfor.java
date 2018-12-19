package com.kwchina.oa.customer.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Customer_ContactInfor", schema = "dbo")
public class ContactInfor {
	private Integer contactId;
	private String contactName;	//联系人姓名
	private String duty;	//职务
	private String phone;	//固定电话
	private String mobile;	//手机
	private String fax;	//传真
	private String email;	//电子邮箱
	private Date birthday;	//生日
	private CustomerInfor customer;	//客户
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getContactId() {
		return contactId;
	}
	public void setContactId(Integer contactId) {
		this.contactId = contactId;
	}
	
	
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
	@Column(columnDefinition = "nvarchar(80)", nullable = false)
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customerId")
	public CustomerInfor getCustomer() {
		return customer;
	}
	public void setCustomer(CustomerInfor customer) {
		this.customer = customer;
	}
	
	@Column(columnDefinition = "nvarchar(40)", nullable = true)
	public String getDuty() {
		return duty;
	}
	public void setDuty(String duty) {
		this.duty = duty;
	}
	
	@Column(columnDefinition = "nvarchar(30)", nullable = true)
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Column(columnDefinition = "nvarchar(30)", nullable = true)
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	
	@Column(columnDefinition = "nvarchar(30)", nullable = true)
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	@Column(columnDefinition = "nvarchar(30)", nullable = true)
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
}
