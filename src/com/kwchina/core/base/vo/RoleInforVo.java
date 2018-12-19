package com.kwchina.core.base.vo;



public class RoleInforVo {

    private Integer roleId;
    private String roleName;			//角色名称
    private int orderNo;				//排序编号
    private boolean deleted;			//是否删除:0-否;1-是
    private boolean fixed;				//是否固定:0-否(可删);1-是(不可删).
    private int roleType;				//角色类型:0-自定义;1-全体用户.
    private int[] personIds;				
    
    
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	public boolean isFixed() {
		return fixed;
	}
	public void setFixed(boolean fixed) {
		this.fixed = fixed;
	}
	public int getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public int getRoleType() {
		return roleType;
	}
	public void setRoleType(int roleType) {
		this.roleType = roleType;
	}
	public int[] getPersonIds() {
		return personIds;
	}
	public void setPersonIds(int[] personIds) {
		this.personIds = personIds;
	}

}


