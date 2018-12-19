package com.kwchina.core.base.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Core_DataRightInfor", schema = "dbo")
public class DataRightInfor {

    private Integer dataRightId;
    private int dataId;					//数据Id
    private long rightData;				//权限数据,如:1(001),2(010),4(100),通过在二进制位数上置1的方式形成权限的组合,具体数位代表的权限信息参考OperationDefinition表
    private VirtualResource resource;	//资源信息
    private RoleInfor role;				//角色信息
    private SystemUserInfor user;		//用户信息
    
    
	public long getRightData() {
		return rightData;
	}
	public void setRightData(long rightData) {
		this.rightData = rightData;
	}
	public int getDataId() {
		return dataId;
	}
	public void setDataId(int dataId) {
		this.dataId = dataId;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getDataRightId() {
		return dataRightId;
	}
	public void setDataRightId(Integer dataRightId) {
		this.dataRightId = dataRightId;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="resourceId",nullable = false)
	public VirtualResource getResource() {
		return resource;
	}
	public void setResource(VirtualResource resource) {
		this.resource = resource;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="roleId")
	public RoleInfor getRole() {
		return role;
	}
	public void setRole(RoleInfor role) {
		this.role = role;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="userId")
	public SystemUserInfor getUser() {
		return user;
	}
	public void setUser(SystemUserInfor user) {
		this.user = user;
	}
     
}