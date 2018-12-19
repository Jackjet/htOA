package com.kwchina.core.base.entity;


import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.kwchina.core.util.json.JSONNotAware;

@Entity
@Table(name = "Core_RoleInfor", schema = "dbo")
public class RoleInfor implements JSONNotAware{

    private Integer roleId;
    private String roleName;			//角色名称
    private int orderNo;				//排序编号
    private boolean deleted;			//是否删除:0-否;1-是
    private boolean fixed;				//是否固定:0-否(可删);1-是(不可删).
    private int roleType;				//角色类型:0-自定义;1-全体用户.
    private Set<SystemUserInfor> users = new HashSet<SystemUserInfor>(0);

   
   	@Column(columnDefinition = "nvarchar(100)",nullable = false)
    public String getRoleName() {
        return this.roleName;
    }
    
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    public int getOrderNo() {
        return this.orderNo;
    }
    
    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }
    public boolean isDeleted() {
        return this.deleted;
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


	public int getRoleType() {
		return roleType;
	}


	public void setRoleType(int roleType) {
		this.roleType = roleType;
	}

	@ManyToMany(targetEntity = SystemUserInfor.class,cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name = "Core_RoleUser_Link",schema = "dbo",joinColumns = {@JoinColumn(name = "roleId")},inverseJoinColumns = {@JoinColumn(name = "personId")})
	@OrderBy("personId")
	public Set<SystemUserInfor> getUsers() {
        return this.users;
    }
    
    public void setUsers(Set<SystemUserInfor> users) {
        this.users = users;
    }

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

}


