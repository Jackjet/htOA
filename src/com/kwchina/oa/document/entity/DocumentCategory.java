package com.kwchina.oa.document.entity;


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

import org.hibernate.annotations.Cascade;

import com.kwchina.core.util.annotation.ObjectId;
import com.kwchina.core.util.json.JSONNotAware;
@Entity
@Table(name = "Document_DocumentCategory", schema = "dbo")
@ObjectId(id="categoryId")
public class DocumentCategory implements JSONNotAware{

    private Integer categoryId;
    private String categoryName;		//分类名称
    private int layer;					//层级
    private String fullPath;			//全路径(1,2,3)
    private String categoryCode;		//分类编号
    private boolean leaf;				//是否页分类:0-否;1-是.
    private int displayNo;				//显示次序
    private int leftIndex;
    private int rightIndex;
    private int fromReport;			//判断是否为归档类;0-否;1-是.
    
    private DocumentCategory parent;	//父分类
    private Set<DocumentCategory> childs = new HashSet<DocumentCategory>(0);
    private Set<DocumentInfor> documents = new HashSet<DocumentInfor>(0);
    private Set<DocumentCategoryRight> rights = new HashSet<DocumentCategoryRight>(0);

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getCategoryId() {
        return this.categoryId;
    }
    
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
    
    
    @Column(columnDefinition = "nvarchar(80)", nullable = false)
    public String getCategoryName() {
        return this.categoryName;
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public int getLayer() {
        return this.layer;
    }
    
    public void setLayer(int layer) {
        this.layer = layer;
    }
    
    @Column(columnDefinition = "nvarchar(80)")
    public String getFullPath() {
        return this.fullPath;
    }
    
    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }
    
    @Column(columnDefinition = "nvarchar(60)")
    public String getCategoryCode() {
        return this.categoryCode;
    }
    
    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }
    public boolean isLeaf() {
        return this.leaf;
    }
    
    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }
    public int getDisplayNo() {
        return this.displayNo;
    }
    
    public void setDisplayNo(int displayNo) {
        this.displayNo = displayNo;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId")
    public DocumentCategory getParent() {
        return this.parent;
    }
    
    public void setParent(DocumentCategory parent) {
        this.parent = parent;
    }
    
    @OneToMany(mappedBy = "parent",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    @OrderBy("displayNo")
    public Set<DocumentCategory> getChilds() {
        return this.childs;
    }
    
    public void setChilds(Set<DocumentCategory> childs) {
        this.childs = childs;
    }
    
    @OneToMany(mappedBy = "category",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    public Set<DocumentInfor> getDocuments() {
        return this.documents;
    }
    
    public void setDocuments(Set<DocumentInfor> documents) {
        this.documents = documents;
    }
    
    @OneToMany(mappedBy = "category",fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.DELETE_ORPHAN, org.hibernate.annotations.CascadeType.ALL})
    public Set<DocumentCategoryRight> getRights() {
        return this.rights;
    }
    
    public void setRights(Set<DocumentCategoryRight> rights) {
        this.rights = rights;
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

	public int getFromReport() {
		return fromReport;
	}

	public void setFromReport(int fromReport) {
		this.fromReport = fromReport;
	}
	
	
}


