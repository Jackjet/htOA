package com.kwchina.oa.purchase.yiban.entity;

import com.kwchina.core.base.entity.OrganizeInfor;
import com.kwchina.core.base.entity.RoleInfor;
import com.kwchina.core.base.entity.StructureInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.util.annotation.ObjectId;
import com.kwchina.core.util.json.JSONNotAware;
import com.kwchina.oa.workflow.entity.FlowDefinition;
import com.kwchina.oa.workflow.entity.NodeCheckerPerson;
import com.kwchina.oa.workflow.entity.NodeCheckerRole;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "Purchase_Node", schema = "dbo")
@ObjectId(id="nodeId")
public class PurchaseNode implements JSONNotAware {

	
		
	private Integer nodeId; 		//节点Id	
	private String nodeName; 		//节点名称
	private String memo; 			//节点说明



	private int checkerType; 		//该节点审核者类型 0 人员 1 角色

	private int layer;				//层次

	private boolean printable; 		//允许打印(0-不能打印 1-可打印)
	private boolean download; 		//可下载附件(0-不能下载 1-可下载)
	private boolean upload; 		//可上传附件(0-不可上传 1-可上传)

	private PurchaseFlowDefinition purchaseFlowDefinition;

	
	private OrganizeInfor department; // 所属部门
	private RoleInfor roleId;         //审批角色
	private SystemUserInfor user; 		// 审批人员
	
//	private Set<NodeCheckerPerson> checkerPersons = new HashSet<NodeCheckerPerson>(0);
//    private Set<NodeCheckerRole> checkerRoles = new HashSet<NodeCheckerRole>(0);
    //private Set<FlowTransition> fromTransitions = new HashSet<FlowTransition>(0);
    //private Set<FlowTransition> toTransitions = new HashSet<FlowTransition>(0);
	
	@Id
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
	@JoinColumn(name="user_")
	public SystemUserInfor getUser() {
		return user;
	}
	public void setUser(SystemUserInfor user) {
		this.user = user;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="roleId")
	public RoleInfor getRoleId() {
		return roleId;
	}
	public void setRoleId(RoleInfor roleId) {
		this.roleId = roleId;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="flowId",nullable =false)
	public PurchaseFlowDefinition getPurchaseFlowDefinition() {
		return purchaseFlowDefinition;
	}
	public void setPurchaseFlowDefinition(PurchaseFlowDefinition purchaseFlowDefinition) {
		this.purchaseFlowDefinition = purchaseFlowDefinition;
	}

	public int getLayer() {
		return layer;
	}
	public void setLayer(int layer) {
		this.layer = layer;
	}
}
