package com.kwchina.oa.customer.entity;

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

//项目分类权限
@Entity
@Table(name = "Customer_ProjectCategoryRight", schema = "dbo")
public class ProjectCategoryRight {

	public static final int _Right_Edit = 1; // 1:分类的修改
	public static final int _Right_Delete = 2; // 2:分类的删除
	public static final int _Right_Create = 3; // 3:添加分类下的任务

	private Integer rightId; // 权限Id
	private ProjectCategory projectCategory; // 项目分类
	private SystemUserInfor user; // 系统用户
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
	@JoinColumn(name = "categoryId")
	public ProjectCategory getProjectCategory() {
		return projectCategory;
	}

	public void setProjectCategory(ProjectCategory projectCategory) {
		this.projectCategory = projectCategory;
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
