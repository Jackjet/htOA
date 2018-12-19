package com.kwchina.oa.customer.vo;


public class ProjectCategoryVo {
	private Integer categoryId; // 分类Id
	private String categoryName; // 分类名称
	private Integer displayNo; // 分类显示次序
	private Integer projectId; // 项目信息
	private Integer creatorId; // 创建者
	private int setRight; // 分类权限设置的控制 0-可以设置 1-不可以

	private int[] deleteIds; // 删除
	private int[] editIds; // 修改
	private int[] createIds; // 添加项目分类

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Integer getDisplayNo() {
		return displayNo;
	}

	public void setDisplayNo(Integer displayNo) {
		this.displayNo = displayNo;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
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

	public int getSetRight() {
		return setRight;
	}

	public void setSetRight(int setRight) {
		this.setRight = setRight;
	}

}
