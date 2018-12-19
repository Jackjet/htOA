package com.kwchina.oa.workflow.entity;

import java.sql.Timestamp;

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
@Table(name = "Workflow_Instance_Token", schema = "dbo")
public class InstanceToken {
	private Integer tokenId;					//信息Id	
	
	private FlowInstanceInfor instance;			//审核实例Id
	private InstanceToken parent;				//所属父Token	
	private FlowNode currentNode;				//当前Node
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getTokenId() {
		return tokenId;
	}
	public void setTokenId(Integer tokenId) {
		this.tokenId = tokenId;
	}
	
			
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="currentNode")
	public FlowNode getCurrentNode() {
		return currentNode;
	}
	public void setCurrentNode(FlowNode currentNode) {
		this.currentNode = currentNode;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="instanceId",nullable=false)
	public FlowInstanceInfor getInstance() {
		return instance;
	}
	public void setInstance(FlowInstanceInfor instance) {
		this.instance = instance;
	}	
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="parent")
	public InstanceToken getParent() {
		return parent;
	}
	public void setParent(InstanceToken parent) {
		this.parent = parent;
	}
	

}
