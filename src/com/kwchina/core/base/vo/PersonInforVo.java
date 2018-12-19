package com.kwchina.core.base.vo;



public class PersonInforVo {

    private Integer personId;
    private String personName;			//人员姓名
    private String personNo;			//人员编号
    private String mobile;				//手机
    private String email;				//邮件
    private int gender;					//性别:0-男;1-女.
    private String officeAddress;		//办公室地址
    private String officePhone;			//办公室电话
    private String officeCode;			//办公室邮编
    private String homePhone;			//家庭电话
    private String homeAddress;			//家庭地址
    private String postCode;			//家庭邮政编码
    private String memo;				//备注
    private boolean deleted;			//删除否:0-否;1-是.
    private int positionLayer;			//职级
    //private String photoAttachment;		//个人照片
//    private String[] attatchmentArray = {}; // 附件路径
    private String emailPassword;   	//邮件密码
    private Integer departmentId;		//所属部门
    private Integer groupId;			//所属班组
    private Integer companyId;			//所属公司(为分公司、投资公司等下属公司时，只显示公司，不显示具体部门或班组)
    private Integer structureId;		//岗位
    private Integer userId;				//用户


	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public String getHomeAddress() {
		return homeAddress;
	}
	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}
	public String getHomePhone() {
		return homePhone;
	}
	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getOfficeAddress() {
		return officeAddress;
	}
	public void setOfficeAddress(String officeAddress) {
		this.officeAddress = officeAddress;
	}
	public String getOfficeCode() {
		return officeCode;
	}
	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}
	public String getOfficePhone() {
		return officePhone;
	}
	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}
	public Integer getPersonId() {
		return personId;
	}
	public void setPersonId(Integer personId) {
		this.personId = personId;
	}
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public String getPersonNo() {
		return personNo;
	}
	public void setPersonNo(String personNo) {
		this.personNo = personNo;
	}
//	public String getPhotoAttachment() {
//		return photoAttachment;
//	}
//	public void setPhotoAttachment(String photoAttachment) {
//		this.photoAttachment = photoAttachment;
//	}
	public int getPositionLayer() {
		return positionLayer;
	}
	public void setPositionLayer(int positionLayer) {
		this.positionLayer = positionLayer;
	}
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public Integer getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public Integer getStructureId() {
		return structureId;
	}
	public void setStructureId(Integer structureId) {
		this.structureId = structureId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getEmailPassword() {
		return emailPassword;
	}
	public void setEmailPassword(String emailPassword) {
		this.emailPassword = emailPassword;
	}
//	public String[] getAttatchmentArray() {
//		return attatchmentArray;
//	}
//	public void setAttatchmentArray(String[] attatchmentArray) {
//		this.attatchmentArray = attatchmentArray;
//	}

}


