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
@Table(name = "Workflow_Instance_CheckInfor", schema = "dbo")
public class InstanceCheckInfor {
	public static int Check_Status_Unckeck = 0;		//0-尚未审核
	public static int Check_Status_Checked = 1;		//1-已审核
	public static int Check_Status_Suspend = 2;		//2-暂停
	public static int Check_Status_Canceled = 3;	//3-取消/中止
	
	private Integer checkId;						//信息Id
	
	private Timestamp startDate; 					//开始时间
	private Timestamp endDate; 						//审核时间（结束时间）
	//private boolean canceled; 					//是否取消（是否被中止）（0-否 1-是）
	//private boolean suspended; 					//是否暂停
	private String checkComment; 					//审核内容
	//private boolean checked; 						//是否审核 (0-尚未审核	1-已经审核)
	private String attatchment; 					//附件路径
	private int status;								//状态(0-尚未审核 1-已审核 2-暂停 3-取消/中止)
	
	
	private InstanceLayerInfor layerInfor; 			//审核层次
	private SystemUserInfor checker; 				//审核人
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getCheckId() {
		return checkId;
	}
	public void setCheckId(Integer checkId) {
		this.checkId = checkId;
	}
	
	@Column(columnDefinition = "nvarchar(500)")
	public String getAttatchment() {
		return attatchment;
	}
	public void setAttatchment(String attatchment) {
		this.attatchment = attatchment;
	}
	
	/**
	public boolean isCanceled() {
		return canceled;
	}
	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}
	*/
	
	@Column(columnDefinition = "ntext")
	public String getCheckComment() {
		return checkComment;
	}	
	
	public void setCheckComment(String checkComment) {
		this.checkComment = checkComment;
	}
	
	/**
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	*/
	
	
	public Timestamp getEndDate() {
		return endDate;
	}
	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}
	
	@Column(nullable =false)
	public Timestamp getStartDate() {
		return startDate;
	}
	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}
	
	/**
	public boolean isSuspended() {
		return suspended;
	}
	public void setSuspended(boolean suspended) {
		this.suspended = suspended;
	}
	*/
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="layerId",nullable=false)
	public InstanceLayerInfor getLayerInfor() {
		return layerInfor;
	}
	public void setLayerInfor(InstanceLayerInfor layerInfor) {
		this.layerInfor = layerInfor;
	}	
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="personId",nullable=false)
	public SystemUserInfor getChecker() {
		return checker;
	}
	public void setChecker(SystemUserInfor checker) {
		this.checker = checker;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
}
