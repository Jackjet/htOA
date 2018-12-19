package com.kwchina.oa.customer.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import com.kwchina.core.base.entity.SystemUserInfor;

@Entity
@Table(name = "Customer_CustomerInfor", schema = "dbo")
public class CustomerInfor {

	private Integer customerId;

	private Integer customerType; // 客户类型：0- 潜在客户（即在跟踪客户）；1-客户（已经成为公司客户）

	private String companyName; // 公司名称

	private String companyAddress; // 公司地址

	private String website; // 网址

	private String phone; // 电话

	private String companyIntroduction; // 公司介绍

	private String memo; // 备注

	private SystemUserInfor manager; // 客户经理
	
	private Set<ContactInfor> Contacts = new HashSet<ContactInfor>(0);
	private Set<ActivityInfor> Activitys = new HashSet<ActivityInfor>(0);

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	@Column(columnDefinition = "nvarchar(200)", nullable = true)
	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	@Column(columnDefinition = "ntext", nullable = true)
	public String getCompanyIntroduction() {
		return companyIntroduction;
	}

	public void setCompanyIntroduction(String companyIntroduction) {
		this.companyIntroduction = companyIntroduction;
	}

	@Column(columnDefinition = "nvarchar(100)", nullable = false)
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Integer getCustomerType() {
		return customerType;
	}

	public void setCustomerType(Integer customerType) {
		this.customerType = customerType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "managerId")
	public SystemUserInfor getManager() {
		return manager;
	}

	public void setManager(SystemUserInfor manager) {
		this.manager = manager;
	}

	@Column(columnDefinition = "ntext", nullable = true)
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(columnDefinition = "nvarchar(50)", nullable = true)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(columnDefinition = "nvarchar(50)", nullable = true)
	public String getWebsite() {
		return website;
	}
	
	
	public void setWebsite(String website) {
		this.website = website;
	}
	
	@OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
	@Cascade(value = {org.hibernate.annotations.CascadeType.DELETE_ORPHAN, org.hibernate.annotations.CascadeType.ALL})
	public Set<ActivityInfor> getActivitys() {
		return Activitys;
	}

	public void setActivitys(Set<ActivityInfor> activitys) {
		Activitys = activitys;
	}

	@OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
	@Cascade(value = {org.hibernate.annotations.CascadeType.DELETE_ORPHAN, org.hibernate.annotations.CascadeType.ALL})
	public Set<ContactInfor> getContacts() {
		return Contacts;
	}

	public void setContacts(Set<ContactInfor> contacts) {
		Contacts = contacts;
	}

}
