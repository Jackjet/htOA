package com.kwchina.oa.workflow.entity;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;


@Entity
@Table(name = "Workflow_Instance_LayerInfor", schema = "dbo")
public class InstanceLayerInfor {
	public static int Layer_Type_Set = 1;
	public static int Layer_Type_PreSet = 2;
	
	public static int Layer_Status_Normal = 0;		//0-正常
	public static int Layer_Status_End = 1;			//1-中止
	public static int Layer_Status_Suspended = 2;	//2-暂停
	public static int Layer_Status_Finished = 3;	//3-结束
	
	public static int Layer_Forked_Normal = 0;		//0-正常
	public static int Layer_Forked_Fork = 1;		//1-中止
	public static int Layer_Forked_ForkInner = 2;	//2-暂停
	public static int Layer_Forked_Join = 3;		//3-结束
	
	
	private Integer layerId; 					//层次Id
	
	private String layerName;					//层次名称
	private String checkDemand; 				//审核说明（审核要求）
	private Timestamp startTime; 				//开始时间
	private Timestamp endTime; 					//结束时间
	private int layerType; 						//层次类型（1-主办人员设置 2-预设的层次）
	private int layer; 							//层次（即第几个审核层次）
	
	//private int fromIndex; 					//from层次
	private int status; 						//状态（0-正常 1-中止 2-暂停 3-结束）
	private int forkedType;						//分叉类型（0-普通 1-分叉 2-分叉内层次 3-聚合）
	
	
	//private InstanceLayerInfor fromLayer;		//from层
	//private InstanceLayerInfor toLayer;		//to层
	private FlowNode flowNode;					//对应的Node
	private FlowInstanceInfor instance; 		//审核实例
	private InstanceLayerInfor forkedLayer;		//分叉的父层
	
	//private int fromLayer; 					//from层
	//private int toLayer; 						//to层
	
	private Set<InstanceCheckInfor> checkInfors = new HashSet<InstanceCheckInfor>(0);	//审核信息
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getLayerId() {
		return layerId;
	}

	public void setLayerId(Integer layerId) {
		this.layerId = layerId;
	}
	
	@Column(columnDefinition = "nvarchar(200)",nullable =false)
	public String getLayerName() {
		return layerName;
	}

	public void setLayerName(String layerName) {
		this.layerName = layerName;
	}
	
	@Column(columnDefinition = "ntext")
	public String getCheckDemand() {
		return checkDemand;
	}

	public void setCheckDemand(String checkDemand) {
		this.checkDemand = checkDemand;
	}

	
	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

		

	

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	

	public int getLayerType() {
		return layerType;
	}

	public void setLayerType(int layerType) {
		this.layerType = layerType;
	}

	@Column(nullable =false)
	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	
	
	public int getForkedType() {
		return forkedType;
	}

	public void setForkedType(int forkedType) {
		this.forkedType = forkedType;
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
	@JoinColumn(name="nodeId")
	public FlowNode getFlowNode() {
		return flowNode;
	}

	public void setFlowNode(FlowNode flowNode) {
		this.flowNode = flowNode;
	}

	
	@OneToMany(mappedBy = "layerInfor",fetch=FetchType.EAGER)	
	@Cascade(value = {org.hibernate.annotations.CascadeType.REMOVE})
	@OrderBy("checkId")
	public Set<InstanceCheckInfor> getCheckInfors() {
		return checkInfors;
	}
	public void setCheckInfors(Set<InstanceCheckInfor> checkInfors) {
		this.checkInfors = checkInfors;
	}

	/**
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="fromLayer")
	public InstanceLayerInfor getFromLayer() {
		return fromLayer;
	}

	public void setFromLayer(InstanceLayerInfor fromLayer) {
		this.fromLayer = fromLayer;
	}
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="toLayer")
	public InstanceLayerInfor getToLayer() {
		return toLayer;
	}

	public void setToLayer(InstanceLayerInfor toLayer) {
		this.toLayer = toLayer;
	}
	*/
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="forkedLayer")
	public InstanceLayerInfor getForkedLayer() {
		return forkedLayer;
	}

	public void setForkedLayer(InstanceLayerInfor forkedLayer) {
		this.forkedLayer = forkedLayer;
	}
	
	
	
	
}
