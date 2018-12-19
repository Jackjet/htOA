package com.kwchina.oa.customer.vo;

import java.sql.Date;

import com.kwchina.core.base.entity.SystemUserInfor;

//项目信息
public class ProjectInforVo {
	private Integer projectId; // 项目Id
	private String projectName; // 项目名称
	private String updateDateStr; // 创建时间
	private String description; // 项目描述
	private Integer creatorId; // 创建者
	private String createorName;//创建者姓名
	private Integer managerId; // 项目经理
	private String managerName;	//项目经理姓名
	private int layer; // 层级
	private boolean leaf; // 是否页分类:0-否;1-是.
	private int leftIndex;
	private int rightIndex;
	private int setRight;	//判断用户是否有权限设置项目权限 0-有 1-没有

	private int[] deleteIds; // 删除
	private int[] editIds; // 修改
	private int[] createIds; // 添加项目分类

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getUpdateDateStr() {
		return updateDateStr;
	}

	public void setUpdateDateStr(String updateDateStr) {
		this.updateDateStr = updateDateStr;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
	}

	public Integer getManagerId() {
		return managerId;
	}

	public void setManagerId(Integer managerId) {
		this.managerId = managerId;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public int getLeftIndex() {
		return leftIndex;
	}

	public void setLeftIndex(int leftIndex) {
		this.leftIndex = leftIndex;
	}

	public int getRightIndex() {
		return rightIndex;
	}

	public void setRightIndex(int rightIndex) {
		this.rightIndex = rightIndex;
	}

	public int[] getDeleteIds() {
		return deleteIds;
	}

	public void setDeleteIds(int[] deleteIds) {
		this.deleteIds = deleteIds;
	}

	public int[] getEditIds() {
		return editIds;
	}

	public void setEditIds(int[] editIds) {
		this.editIds = editIds;
	}

	public int[] getCreateIds() {
		return createIds;
	}

	public void setCreateIds(int[] createIds) {
		this.createIds = createIds;
	}

	public String getCreateorName() {
		return createorName;
	}

	public void setCreateorName(String createorName) {
		this.createorName = createorName;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public int getSetRight() {
		return setRight;
	}

	public void setSetRight(int setRight) {
		this.setRight = setRight;
	}


}
