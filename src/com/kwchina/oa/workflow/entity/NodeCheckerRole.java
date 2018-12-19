package com.kwchina.oa.workflow.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.kwchina.core.base.entity.RoleInfor;


@Entity
@Table(name = "Workflow_Node_Checker_Role", schema = "dbo")
public class NodeCheckerRole {
	private Integer dataId;	//数据Id
	
	private FlowNode flowNode;		//所属节点
	private RoleInfor role;			//角色
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getDataId() {
		return dataId;
	}
	public void setDataId(Integer dataId) {
		this.dataId = dataId;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="nodeId",nullable=true)
	public FlowNode getFlowNode() {
		return flowNode;
	}
	public void setFlowNode(FlowNode flowNode) {
		this.flowNode = flowNode;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="roleId")
	public RoleInfor getRole() {
		return role;
	}
	public void setRole(RoleInfor role) {
		this.role = role;
	}
	
}
