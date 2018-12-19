package com.kwchina.oa.document.vo;


public class DocumentCategoryVo {
	
	private Integer categoryId;
    private String categoryName;		//分类名称
    private int layer;					//层级
    private String fullPath;			//全路径(1,2,3)
    private String categoryCode;		//分类编号
    private boolean leaf;				//是否页分类:0-否;1-是.
    private int displayNo;				//显示次序
    private Integer parentId;			//父分类Id
	private int fromReport;			//是否为归档类:0-否;1-是.
    private int[] createIds; 			//创建
	private int[] deleteIds; 			//删除
	private int[] editIds; 				//修改
	private int[] viewIds; 				//浏览

    
    
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
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
	public int getDisplayNo() {
		return displayNo;
	}
	public void setDisplayNo(int displayNo) {
		this.displayNo = displayNo;
	}
	public String getFullPath() {
		return fullPath;
	}
	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
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
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public int[] getCreateIds() {
		return createIds;
	}
	public void setCreateIds(int[] createIds) {
		this.createIds = createIds;
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
	public int[] getViewIds() {
		return viewIds;
	}
	public void setViewIds(int[] viewIds) {
		this.viewIds = viewIds;
	}
	public int getFromReport() {
		return fromReport;
	}
	public void setFromReport(int fromReport) {
		this.fromReport = fromReport;
	}
}