package com.kwchina.oa.customer.entity;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import com.kwchina.core.base.entity.SystemUserInfor;

//项目信息
@Entity
@Table(name = "Customer_ProjectInfor", schema = "dbo")
public class ProjectInfor {

	private Integer projectId; // 项目Id
	private String projectName; // 项目名称
	private Date updateDate; // 创建时间
	private String description; // 项目描述
	private SystemUserInfor creator; // 创建者
	private SystemUserInfor manager; // 项目经理
	
	private Set<ProjectCategory> projectCategorys = new HashSet<ProjectCategory>(0);
	private Set<ProjectRight>	projectRights = new HashSet<ProjectRight>(0);


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	@Column(columnDefinition = "nvarchar(200)", nullable = false)
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Column(columnDefinition = "ntext", nullable = true)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "personId")
	public SystemUserInfor getCreator() {
		return creator;
	}

	public void setCreator(SystemUserInfor creator) {
		this.creator = creator;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "managerId")
	public SystemUserInfor getManager() {
		return manager;
	}

	public void setManager(SystemUserInfor manager) {
		this.manager = manager;
	}

	@OneToMany(mappedBy = "projectInfor",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	public Set<ProjectCategory> getProjectCategorys() {
		return projectCategorys;
	}

	public void setProjectCategorys(Set<ProjectCategory> projectCategorys) {
		this.projectCategorys = projectCategorys;
	}
	
	@OneToMany(mappedBy = "projectInfor",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	 @Cascade(value = {org.hibernate.annotations.CascadeType.DELETE_ORPHAN, org.hibernate.annotations.CascadeType.ALL})
	public Set<ProjectRight> getProjectRights() {
		return projectRights;
	}

	public void setProjectRights(Set<ProjectRight> projectRights) {
		this.projectRights = projectRights;
	}

}
