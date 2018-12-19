package com.kwchina.oa.workflow.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.CascadeType;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;


@Entity
@Table(name = "Workflow_Flow_Transition", schema = "dbo")
public class FlowTransition {
	private Integer transId;				//数据Id
	private String transName;				//连接名称
	private int fromIndex;					//from次序	
	
	private FlowNode fromNode;				//from节点
	private FlowNode toNode;				//to节点
	private FlowDefinition flowDefinition; //所属流程
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getTransId() {
		return transId;
	}
	public void setTransId(Integer transId) {
		this.transId = transId;
	}
	
	public int getFromIndex() {
		return fromIndex;
	}
	public void setFromIndex(int fromIndex) {
		this.fromIndex = fromIndex;
	}
	
	@Column(columnDefinition = "nvarchar(200)")
	public String getTransName() {
		return transName;
	}
	public void setTransName(String transName) {
		this.transName = transName;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="flowId",nullable =false)
	public FlowDefinition getFlowDefinition() {
		return flowDefinition;
	}
	public void setFlowDefinition(FlowDefinition flowDefinition) {
		this.flowDefinition = flowDefinition;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="fromNode")
	public FlowNode getFromNode() {
		return fromNode;
	}
	public void setFromNode(FlowNode fromNode) {
		this.fromNode = fromNode;
	}
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	//@Cascade(value = {org.hibernate.annotations.CascadeType.SAVE_UPDATE,org.hibernate.annotations.CascadeType.PERSIST})	
	@JoinColumn(name="toNode")
	public FlowNode getToNode() {
		return toNode;
	}
	public void setToNode(FlowNode toNode) {
		this.toNode = toNode;
	}
}
