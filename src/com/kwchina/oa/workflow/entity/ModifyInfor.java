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

import com.kwchina.core.base.entity.SystemUserInfor;
@Entity
@Table(name = "Workflow_Instance_ModifyInfor", schema = "dbo")
public class ModifyInfor {
	
	private Integer modifyId;
	
	private Timestamp modifyTime;
	
	private String content;
	
	private SystemUserInfor modifyer; 
	private FlowInstanceInfor instance; 
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getModifyId() {
		return modifyId;
	}

	public void setModifyId(Integer modifyId) {
		this.modifyId = modifyId;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="instanceId",nullable=false)
	public FlowInstanceInfor getInstance() {
		return instance;
	}
	public void setInstance(FlowInstanceInfor instance) {
		this.instance = instance;
	}
	
	@Column(columnDefinition = "ntext")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="personId",nullable=false)
	public SystemUserInfor getModifyer() {
		return modifyer;
	}

	public void setModifyer(SystemUserInfor modifyer) {
		this.modifyer = modifyer;
	}

	@Column(nullable =false)
	public Timestamp getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Timestamp modifyTime) {
		this.modifyTime = modifyTime;
	}
}