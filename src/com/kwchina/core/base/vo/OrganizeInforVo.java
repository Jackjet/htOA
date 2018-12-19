package com.kwchina.core.base.vo;



public class OrganizeInforVo {

    private Integer organizeId;
    private String organizeName;	//名称
    private String shortName;		//简称
    private String organizeNo;		//编号
    private int layer;				//层级
    private int levelId;			//组织层级:0-公司(集团公司);1-部门;2-班组;3-分公司;4-投资公司.
    private boolean deleted;		//是否删除:0-否;1-是.
    private int orderNo;			//排序编号
    private Integer parentId;		//父部门或公司
    private Integer directorId;		//经理/主管
    
    
    
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	public Integer getDirectorId() {
		return directorId;
	}
	public void setDirectorId(Integer directorId) {
		this.directorId = directorId;
	}
	public int getLayer() {
		return layer;
	}
	public void setLayer(int layer) {
		this.layer = layer;
	}
	public int getLevelId() {
		return levelId;
	}
	public void setLevelId(int levelId) {
		this.levelId = levelId;
	}
	public int getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}
	public Integer getOrganizeId() {
		return organizeId;
	}
	public void setOrganizeId(Integer organizeId) {
		this.organizeId = organizeId;
	}
	public String getOrganizeName() {
		return organizeName;
	}
	public void setOrganizeName(String organizeName) {
		this.organizeName = organizeName;
	}
	public String getOrganizeNo() {
		return organizeNo;
	}
	public void setOrganizeNo(String organizeNo) {
		this.organizeNo = organizeNo;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

}


