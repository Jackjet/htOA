package com.kwchina.oa.customer.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.kwchina.core.base.entity.SystemUserInfor;

//项目权限
@Entity
@Table(name = "Customer_ProjectRight", schema = "dbo")
public class ProjectRight {

	public static final int _Right_Edit = 1; // 1:项目的修改
	public static final int _Right_Delete = 2; // 2:项目的删除
	public static final int _Right_Create = 3; // 3:添加项目的分类

	private Integer rightId; // 权限Id
	private ProjectInfor projectInfor; // 项目信息
	private SystemUserInfor user; // 用户
	private int operateRight; // 操作权限

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getRightId() {
		return rightId;
	}

	public void setRightId(Integer rightId) {
		this.rightId = rightId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "projectId")
	public ProjectInfor getProjectInfor() {
		return projectInfor;
	}

	public void setProjectInfor(ProjectInfor projectInfor) {
		this.projectInfor = projectInfor;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "personId")
	public SystemUserInfor getUser() {
		return user;
	}

	public void setUser(SystemUserInfor user) {
		this.user = user;
	}

	public int getOperateRight() {
		return operateRight;
	}

	public void setOperateRight(int operateRight) {
		this.operateRight = operateRight;
	}

}
