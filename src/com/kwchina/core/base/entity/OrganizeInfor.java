package com.kwchina.core.base.entity;


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
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.kwchina.core.util.annotation.ObjectId;
import com.kwchina.core.util.json.JSONNotAware;

@Entity
@Table(name = "Core_OrganizeInfor", schema = "dbo")
@ObjectId(id="organizeId")
public class OrganizeInfor implements JSONNotAware {

    private Integer organizeId;
    private String organizeName;	//名称
    private String shortName;		//简称
    private String organizeNo;		//编号
    private int layer;				//层级
    private int levelId;			//组织层级:0-公司(集团公司);1-部门;2-班组;3-分公司;4-投资公司.
    private boolean deleted;		//是否删除:0-否;1-是.
    private int orderNo;			//排序编号
    private OrganizeInfor parent;	//父部门或公司
    private PersonInfor director;	//经理/主管
    private Set<OrganizeInfor> childs = new HashSet<OrganizeInfor>(0);
    private Set<DirAndSupInfor> dirAndSups = new HashSet<DirAndSupInfor>(0);

    private String guikou;  // 是否是归口部门

 	@Column(columnDefinition = "nvarchar(100)",nullable = false)
    public String getOrganizeName() {
        return this.organizeName;
    }
    
    public void setOrganizeName(String organizeName) {
        this.organizeName = organizeName;
    }
    
 	@Column(columnDefinition = "nvarchar(80)")
    public String getShortName() {
        return this.shortName;
    }
    
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
    
 	@Column(columnDefinition = "nvarchar(80)")
    public String getOrganizeNo() {
        return this.organizeNo;
    }
    
    public void setOrganizeNo(String organizeNo) {
        this.organizeNo = organizeNo;
    }
    public int getLayer() {
        return this.layer;
    }
    
    public void setLayer(int layer) {
        this.layer = layer;
    }
    public int getLevelId() {
        return this.levelId;
    }
    
    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }
    public boolean isDeleted() {
        return this.deleted;
    }
    
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    public int getOrderNo() {
        return this.orderNo;
    }
    
    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }
    
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="parentId")
    public OrganizeInfor getParent() {
        return this.parent;
    }
    
    public void setParent(OrganizeInfor parent) {
        this.parent = parent;
    }
    
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="personId")
    public PersonInfor getDirector() {
        return this.director;
    }
    
    public void setDirector(PersonInfor director) {
        this.director = director;
    }
    
	@OneToMany(mappedBy = "parent",fetch=FetchType.LAZY,cascade = CascadeType.ALL)
	@OrderBy("orderNo")
    public Set<OrganizeInfor> getChilds() {
        return this.childs;
    }
    
    public void setChilds(Set<OrganizeInfor> childs) {
        this.childs = childs;
    }

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getOrganizeId() {
		return organizeId;
	}

	public void setOrganizeId(Integer organizeId) {
		this.organizeId = organizeId;
	}

	@OneToMany(mappedBy = "organize",fetch=FetchType.LAZY,cascade = CascadeType.ALL)
	public Set<DirAndSupInfor> getDirAndSups() {
		return dirAndSups;
	}

	public void setDirAndSups(Set<DirAndSupInfor> dirAndSups) {
		this.dirAndSups = dirAndSups;
	}


    public String getGuikou() {
        return guikou;
    }

    public void setGuikou(String guikou) {
        this.guikou = guikou;
    }
}


