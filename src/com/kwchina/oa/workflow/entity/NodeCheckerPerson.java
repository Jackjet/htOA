package com.kwchina.oa.workflow.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.kwchina.core.base.entity.SystemUserInfor;

@Entity
@Table(name = "Workflow_Node_Checker_Person", schema = "dbo")
public class NodeCheckerPerson {
	private Integer dataId;			//数据Id
	
	private SystemUserInfor user; 	//审核人
	private FlowNode flowNode;		//所属节点
	
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
	@JoinColumn(name="personId")
	public SystemUserInfor getUser() {
		return user;
	}
	public void setUser(SystemUserInfor user) {
		this.user = user;
	}
	
	
	
}
