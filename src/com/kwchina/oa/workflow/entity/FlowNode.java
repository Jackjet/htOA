package com.kwchina.oa.workflow.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
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

import com.kwchina.core.base.entity.OrganizeInfor;
import com.kwchina.core.base.entity.StructureInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.util.annotation.ObjectId;
import com.kwchina.core.util.json.JSONNotAware;


@Entity
@Table(name = "Workflow_Flow_Node", schema = "dbo")
@ObjectId(id="nodeId")
public class FlowNode implements JSONNotAware {
	public static int Node_Type_Normal = 1;
	public static int Node_Type_Fork = 2;
	public static int Node_Type_Join = 3;
	public static int Node_Type_Status = 4;
	
	public static int Node_CheckerType_Set = 0;
	public static int Node_CheckerType_PreSet = 1;
	public static int Node_CheckerType_Structrue = 2;
	public static int Node_CheckerType_Department = 3;
	public static int Node_CheckerType_Person = 4;
	public static int Node_CheckerType_Special = 11;
	
		
	private Integer nodeId; 		//节点Id	
	private String nodeName; 		//节点名称
	private String memo; 			//节点说明
	private int nodeType; 			//节点类型

	private int finishType; 		//审核结束类型（并和或的关系）
	private int checkerType; 		//该节点审核者类型
	private int special;			//特殊审核人
	private int layer;				//位于层次

	private boolean forked;			//是否分叉内的节点
	private boolean printable; 		//允许打印(0-不能打印 1-可打印)
	private boolean download; 		//可下载附件(0-不能下载 1-可下载)
	private boolean upload; 		//可上传附件(0-不可上传 1-可上传)

	private FlowDefinition flowDefinition;
	private FlowNode forkedNode;
	
	private OrganizeInfor department; // 所属部门
	private StructureInfor structure; // 岗位
	private SystemUserInfor user; // 人员
	
	private Set<NodeCheckerPerson> checkerPersons = new HashSet<NodeCheckerPerson>(0);
    private Set<NodeCheckerRole> checkerRoles = new HashSet<NodeCheckerRole>(0);
    //private Set<FlowTransition> fromTransitions = new HashSet<FlowTransition>(0);
    //private Set<FlowTransition> toTransitions = new HashSet<FlowTransition>(0);
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getNodeId() {
		return nodeId;
	}
	public void setNodeId(Integer nodeId) {
		this.nodeId = nodeId;
	}
	
	
	public int getCheckerType() {
		return checkerType;
	}
	public void setCheckerType(int checkerType) {
		this.checkerType = checkerType;
	}
	
	
	public int getFinishType() {
		return finishType;
	}
	public void setFinishType(int finishType) {
		this.finishType = finishType;
	}
	
	@Column(columnDefinition = "ntext")
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	@Column(columnDefinition = "nvarchar(200)",nullable =false)
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
		
	public int getNodeType() {
		return nodeType;
	}
	public void setNodeType(int nodeType) {
		this.nodeType = nodeType;
	}
	
	public boolean isDownload() {
		return download;
	}
	public void setDownload(boolean download) {
		this.download = download;
	}
	public boolean isPrintable() {
		return printable;
	}
	public void setPrintable(boolean printable) {
		this.printable = printable;
	}
	public boolean isUpload() {
		return upload;
	}
	public void setUpload(boolean upload) {
		this.upload = upload;
	}
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="department_")
	public OrganizeInfor getDepartment() {
		return department;
	}
	public void setDepartment(OrganizeInfor department) {
		this.department = department;
	}
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="structure_")
	public StructureInfor getStructure() {
		return structure;
	}
	public void setStructure(StructureInfor structure) {
		this.structure = structure;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_")
	public SystemUserInfor getUser() {
		return user;
	}
	public void setUser(SystemUserInfor user) {
		this.user = user;
	}
	
		
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="forkedNode")
	public FlowNode getForkedNode() {
		return forkedNode;
	}
	public void setForkedNode(FlowNode forkedNode) {
		this.forkedNode = forkedNode;
	}
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="flowId",nullable =false)
	public FlowDefinition getFlowDefinition() {
		return flowDefinition;
	}
	public void setFlowDefinition(FlowDefinition flowDefinition) {
		this.flowDefinition = flowDefinition;
	}
	
	@OneToMany(mappedBy = "flowNode",fetch=FetchType.LAZY,cascade = CascadeType.REMOVE)
	@OrderBy("dataId")
	//@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.ALL)
	public Set<NodeCheckerPerson> getCheckerPersons() {
		return checkerPersons;
	}
	public void setCheckerPersons(Set<NodeCheckerPerson> checkerPersons) {
		this.checkerPersons = checkerPersons;
	}
	
	@OneToMany(mappedBy = "flowNode",fetch=FetchType.LAZY)//,cascade = CascadeType.REMOVE
	@Cascade(value = {org.hibernate.annotations.CascadeType.DELETE_ORPHAN, org.hibernate.annotations.CascadeType.ALL})
	@OrderBy("dataId")
	public Set<NodeCheckerRole> getCheckerRoles() {
		return checkerRoles;
	}
	public void setCheckerRoles(Set<NodeCheckerRole> checkerRoles) {
		this.checkerRoles = checkerRoles;
	}
	
	/**
	@OneToMany(fetch=FetchType.LAZY)	
	@Cascade(value = {org.hibernate.annotations.CascadeType.ALL})
	public Set<FlowTransition> getFromTransitions() {
		return fromTransitions;
	}
	public void setFromTransitions(Set<FlowTransition> fromTransitions) {
		this.fromTransitions = fromTransitions;
	}
	
	
	@OneToMany(fetch=FetchType.LAZY)
	@Cascade(value = {org.hibernate.annotations.CascadeType.ALL})
	public Set<FlowTransition> getToTransitions() {
		return toTransitions;
	}
	public void setToTransitions(Set<FlowTransition> toTransitions) {
		this.toTransitions = toTransitions;
	}
	*/
	
	
	public int getSpecial() {
		return special;
	}
	public void setSpecial(int special) {
		this.special = special;
	}
	public int getLayer() {
		return layer;
	}
	public void setLayer(int layer) {
		this.layer = layer;
	}
	public boolean isForked() {
		return forked;
	}
	public void setForked(boolean forked) {
		this.forked = forked;
	}
	
	
	
}
