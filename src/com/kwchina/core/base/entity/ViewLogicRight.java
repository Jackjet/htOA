package com.kwchina.core.base.entity;

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
@Table(name = "Core_ViewLogicRight", schema = "dbo")
public class ViewLogicRight {

    private Integer viewRightId;
    private OperationDefinition operation;	//操作定义
    private String sql;						//sql语句(该权限sql语句添加在OperationDefinition表的父操作数据上)
    private RoleInfor role;					//角色信息
    private SystemUserInfor user;			//用户信息
    
    
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="operationId",nullable = false)
	public OperationDefinition getOperation() {
		return operation;
	}
	public void setOperation(OperationDefinition operation) {
		this.operation = operation;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="roleId")
	public RoleInfor getRole() {
		return role;
	}
	public void setRole(RoleInfor role) {
		this.role = role;
	}
	
    @Column(columnDefinition = "nvarchar(2000)",nullable = false)
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="userId")
	public SystemUserInfor getUser() {
		return user;
	}
	public void setUser(SystemUserInfor user) {
		this.user = user;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getViewRightId() {
		return viewRightId;
	}
	public void setViewRightId(Integer viewRightId) {
		this.viewRightId = viewRightId;
	}
     
}