package com.kwchina.core.base.vo;


import java.sql.Timestamp;

public class SystemUserInforVo {

    private Integer personId;
    private String userName;		//登录用户名
    private String password;		//登录密码
    private boolean invalidate;		//注销与否:0-未注销;1-已注销.
    private int userType;			//用户类型:0-普通用户;1-系统管理员.
    //private Timestamp lastLoginTime;//最近登录时间
    private int loginTimes;			//登录次数
    private boolean firstLogin;		//是否首次登陆:0-否;1-是.
	private int[] roleIds; 			//角色Ids
	private int departmentId;		//所属部门
	private String oldPassword;		//原始密码
	private String rePassword;		//确认新密码
    
	
	
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	public boolean isFirstLogin() {
		return firstLogin;
	}
	public void setFirstLogin(boolean firstLogin) {
		this.firstLogin = firstLogin;
	}
	public boolean isInvalidate() {
		return invalidate;
	}
	public void setInvalidate(boolean invalidate) {
		this.invalidate = invalidate;
	}
	/*public Timestamp getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(Timestamp lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}*/
	public int getLoginTimes() {
		return loginTimes;
	}
	public void setLoginTimes(int loginTimes) {
		this.loginTimes = loginTimes;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getPersonId() {
		return personId;
	}
	public void setPersonId(Integer personId) {
		this.personId = personId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getUserType() {
		return userType;
	}
	public void setUserType(int userType) {
		this.userType = userType;
	}
	public int getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}
	public int[] getRoleIds() {
		return roleIds;
	}
	public void setRoleIds(int[] roleIds) {
		this.roleIds = roleIds;
	}
	public String getRePassword() {
		return rePassword;
	}
	public void setRePassword(String rePassword) {
		this.rePassword = rePassword;
	}

}


