package com.kwchina.oa.customer.entity;

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

//项目分类
@Entity
@Table(name = "Customer_ProjectCategory", schema = "dbo")
public class ProjectCategory {

	private Integer categoryId; // 分类Id
	private String categoryName; // 分类名称
	private Integer displayNo; // 分类显示次序
	private ProjectInfor projectInfor; // 项目信息
	private SystemUserInfor creator; // 创建者

	private Set<TaskInfor> taskInfors = new HashSet<TaskInfor>(0);
	private Set<ProjectCategoryRight> categoryRights = new HashSet<ProjectCategoryRight>(0);

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	@Column(columnDefinition = "nvarchar(80)", nullable = false)
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
	public SystemUserInfor getCreator() {
		return creator;
	}

	public void setCreator(SystemUserInfor creator) {
		this.creator = creator;
	}

	@OneToMany(mappedBy = "projectCategory", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public Set<TaskInfor> getTaskInfors() {
		return taskInfors;
	}

	public void setTaskInfors(Set<TaskInfor> taskInfors) {
		this.taskInfors = taskInfors;
	}

	@OneToMany(mappedBy = "projectCategory", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@Cascade(value = { org.hibernate.annotations.CascadeType.DELETE_ORPHAN, org.hibernate.annotations.CascadeType.ALL })
	public Set<ProjectCategoryRight> getCategoryRights() {
		return categoryRights;
	}

	public void setCategoryRights(Set<ProjectCategoryRight> categoryRights) {
		this.categoryRights = categoryRights;
	}

}
