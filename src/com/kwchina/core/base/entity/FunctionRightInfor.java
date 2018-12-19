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
@Table(name = "Core_FunctionRightInfor", schema = "dbo")
public class FunctionRightInfor {

    private Integer functionRightId;
    private VirtualResource resource;	//资源信息
    private RoleInfor role;				//角色信息
    private long rightData;				//权限数据,如:1(0001),2(0010),4(0100),8(1000),通过在二进制位数上置1的方式形成权限的组合,具体数位代表的权限信息参考OperationDefinition表�OperationDefinition��
    
    
	public long getRightData() {
		return rightData;
	}
	public void setRightData(long rightData) {
		this.rightData = rightData;
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
	@JoinColumn(name="roleId",nullable = false)
	public RoleInfor getRole() {
		return role;
	}
	public void setRole(RoleInfor role) {
		this.role = role;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getFunctionRightId() {
		return functionRightId;
	}
	public void setFunctionRightId(Integer functionRightId) {
		this.functionRightId = functionRightId;
	}
     
}