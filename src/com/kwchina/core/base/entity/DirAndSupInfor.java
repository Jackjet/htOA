package com.kwchina.core.base.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Core_DirAndSupInfor", schema = "dbo")
public class DirAndSupInfor {
	
	private Integer dirAndSupId;
	private String directors;		//董事
	private String supervisors;		//监事
	private OrganizeInfor organize;	//组织结构信息
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getDirAndSupId() {
		return dirAndSupId;
	}
	public void setDirAndSupId(Integer dirAndSupId) {
		this.dirAndSupId = dirAndSupId;
	}
	
 	@Column(columnDefinition = "nvarchar(2000)")
	public String getDirectors() {
		return directors;
	}
	public void setDirectors(String directors) {
		this.directors = directors;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="organizeId")
	public OrganizeInfor getOrganize() {
		return organize;
	}
	public void setOrganize(OrganizeInfor organize) {
		this.organize = organize;
	}
	
 	@Column(columnDefinition = "nvarchar(2000)")
	public String getSupervisors() {
		return supervisors;
	}
	public void setSupervisors(String supervisors) {
		this.supervisors = supervisors;
	}
	
}