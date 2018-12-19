package com.kwchina.core.base.vo;



public class DirAndSupInforVo {
	
	private Integer dirAndSupId;
	private String directors;		//董事
	private String supervisors;		//监事
	private Integer organizeId;		//组织结构信息
	
	
	public Integer getDirAndSupId() {
		return dirAndSupId;
	}
	public void setDirAndSupId(Integer dirAndSupId) {
		this.dirAndSupId = dirAndSupId;
	}
	public String getDirectors() {
		return directors;
	}
	public void setDirectors(String directors) {
		this.directors = directors;
	}
	public Integer getOrganizeId() {
		return organizeId;
	}
	public void setOrganizeId(Integer organizeId) {
		this.organizeId = organizeId;
	}
	public String getSupervisors() {
		return supervisors;
	}
	public void setSupervisors(String supervisors) {
		this.supervisors = supervisors;
	}
	
}