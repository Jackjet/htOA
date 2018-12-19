package com.kwchina.oa.workflow.entity;

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
@Table(name = "Workflow_Instance_TransitionInfor", schema = "dbo")
public class InstanceTransitionInfor {
	private Integer transId;				//数据Id
	private String transName;				//连接名称
	private int fromIndex;					//from次序	
	
	private InstanceLayerInfor fromLayer;				//from节点
	private InstanceLayerInfor toLayer;				//to节点
	private FlowInstanceInfor instance; 	//所属实例
	
	
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
	@JoinColumn(name="instanceId",nullable =false)
	public FlowInstanceInfor getInstance() {
		return instance;
	}
	public void setInstance(FlowInstanceInfor instance) {
		this.instance = instance;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="fromLayer")
	public InstanceLayerInfor getFromLayer() {
		return fromLayer;
	}
	public void setFromLayer(InstanceLayerInfor fromLayer) {
		this.fromLayer = fromLayer;
	}
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	//@Cascade(value = {org.hibernate.annotations.CascadeType.SAVE_UPDATE,org.hibernate.annotations.CascadeType.PERSIST})	
	@JoinColumn(name="toLayer")
	public InstanceLayerInfor getToLayer() {
		return toLayer;
	}
	public void setToLayer(InstanceLayerInfor toLayer) {
		this.toLayer = toLayer;
	}

}
