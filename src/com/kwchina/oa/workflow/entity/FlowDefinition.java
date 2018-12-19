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

import com.kwchina.core.base.entity.RoleInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.util.annotation.ObjectId;
import com.kwchina.core.util.json.JSONNotAware;


@Entity
@Table(name = "Workflow_Flow_Definition", schema = "dbo")
@ObjectId(id="flowId")
public class FlowDefinition implements JSONNotAware{	
	private Integer flowId;			//流程
	private String flowName;		//流程名称
	private int status;				//状态 (1-启用 0-停用)
	private String memo;			//流程说明
	private int flowType;			//流程类型（0-固定 1-人工）
	private int transType;			//跳转类型（针对节点）
	private String template;		//模板路径
	private boolean valid;
	private String categoryName;	//流程分类,如：收文,发文,报告.
	
	private SystemUserInfor charger;//主办人
	
	private int filerType;          //归档人类型（0-主办人 1-固定角色）
	private RoleInfor fileRole;     //归档角色（为空时，默认为主办人）
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getFlowId() {
		return flowId;
	}

	public void setFlowId(Integer flowId) {
		this.flowId = flowId;
	}

	
	@Column(columnDefinition = "nvarchar(200)",nullable =false)
	public String getFlowName() {
		return flowName;
	}

	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

	

	public int getFlowType() {
		return flowType;
	}

	public void setFlowType(int flowType) {
		this.flowType = flowType;
	}

	@Column(columnDefinition = "ntext")
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Column(columnDefinition = "nvarchar(500)")
	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}


	public int getTransType() {
		return transType;
	}

	public void setTransType(int transType) {
		this.transType = transType;
	}
	
	@Column(nullable =false)
	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="chargerId",nullable =false)
	public SystemUserInfor getCharger() {
		return charger;
	}

	public void setCharger(SystemUserInfor charger) {
		this.charger = charger;
	}

	@Column(columnDefinition = "nvarchar(80)")
	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="fileRoleId",nullable = true)
	public RoleInfor getFileRole() {
		return fileRole;
	}

	public void setFileRole(RoleInfor fileRole) {
		this.fileRole = fileRole;
	}

	public int getFilerType() {
		return filerType;
	}

	public void setFilerType(int filerType) {
		this.filerType = filerType;
	}

	

}
