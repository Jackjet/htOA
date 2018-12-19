package com.kwchina.oa.personal.address.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.kwchina.core.util.annotation.ObjectId;
import com.kwchina.core.util.json.JSONNotAware;

@Entity
@Table(name = "Personal_Address_Company", schema = "dbo")
@ObjectId(id="personId")
public class CompanyAddress implements JSONNotAware {
	private Integer personId;             //人员编号
	private String personName;            //人员姓名
	private String department;            //部门
	private String position;              //职位
	private String mobile;                //手机 
	private String email;                 //邮件
	private Integer gender;               //性别（0-男，1-女）
	private Date birthday;                //出生日期
	private String memo;                  //备注说明
	private String officeAddress;         //办公室地址
	private String officePhone;           //办公室电话 
	private String officeCode;            //办公室邮编
	private String homePhone;             //家庭电话 
	private String homeAddress;           //家庭地址
	private String postCode;              //家庭邮编 
	
	@Id
   	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getPersonId() {
		return personId;
	}
	public void setPersonId(Integer personId) {
		this.personId = personId;
	}
	
	
	@Column(columnDefinition = "nvarchar(80)",nullable = false)
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	
	@Column(columnDefinition = "nvarchar(100)")
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	
	@Column(columnDefinition = "nvarchar(100)")
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	@Column(columnDefinition = "nvarchar(100)")
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Column(nullable = false)
	public Integer getGender() {
		return gender;
	}
	public void setGender(Integer gender) {
		this.gender = gender;
	}
	
	
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
	@Column(columnDefinition = "ntext")
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	@Column(columnDefinition = "nvarchar(200)")
	public String getOfficeAddress() {
		return officeAddress;
	}
	public void setOfficeAddress(String officeAddress) {
		this.officeAddress = officeAddress;
	}
	
	@Column(columnDefinition = "nvarchar(80)")
	public String getOfficePhone() {
		return officePhone;
	}
	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}
	
	@Column(columnDefinition = "nvarchar(20)")
	public String getOfficeCode() {
		return officeCode;
	}
	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}
	
	@Column(columnDefinition = "nvarchar(80)")
	public String getHomePhone() {
		return homePhone;
	}
	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}
	
	@Column(columnDefinition = "nvarchar(200)")
	public String getHomeAddress() {
		return homeAddress;
	}
	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}
	
	@Column(columnDefinition = "nvarchar(20)")
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	
	@Column(columnDefinition = "nvarchar(100)")
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	
}
