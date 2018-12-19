package com.kwchina.core.cms.entity;


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
@Table(name = "Infor_Base_CategoryInfor", schema = "dbo")
@ObjectId(id="categoryId")
public class InforCategory implements JSONNotAware {

    private Integer categoryId;
    private String categoryName;	//分类名称
    private int orderNo;			//序号
    private String fullPath;		//分类全路径(1,2,3)
    private boolean deleted;		//是否删除(是否显示):0-未删除;1-	删除.
    private boolean leaf;			//是否子分类:0-否;1-是.
    private String attchmentPath;	//附件路径
    private int layer;				//分类层次
    private String pagePath;		//生成的静态页面路径
    private String urlPath;			//访问路径(如party，以区别不同分类)
    private String listTemplate;	//列表模板路径
    private String contentTemplate;	//内容模板路径
    private boolean inherit;		//是否继承父分类信息(继承包括模板\字段\附件路径等):0-否;1-是.
    private int leftIndex;
    private int rightIndex;
    
    private InforCategory parent;	//父分类
    private Set<InforCategory> childs = new HashSet<InforCategory>(0);
    private Set<InforCategoryRight> rights = new HashSet<InforCategoryRight>(0);
    private Set<InforField> fields = new HashSet<InforField>(0);

   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getCategoryId() {
        return this.categoryId;
    }
    
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
    
    @Column(columnDefinition = "nvarchar(100)", nullable = false)
    public String getCategoryName() {
        return this.categoryName;
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public int getOrderNo() {
        return this.orderNo;
    }
    
    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }
    
    @Column(columnDefinition = "nvarchar(100)")
    public String getFullPath() {
        return this.fullPath;
    }
    
    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }
    
    @Column(columnDefinition = "nvarchar(200)")
    public String getAttchmentPath() {
        return this.attchmentPath;
    }
    
    public void setAttchmentPath(String attchmentPath) {
        this.attchmentPath = attchmentPath;
    }
    
    @Column(columnDefinition = "nvarchar(80)")
    public String getUrlPath() {
        return this.urlPath;
    }
    
    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }
    public int getLayer() {
        return this.layer;
    }
    
    public void setLayer(int layer) {
        this.layer = layer;
    }
    
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="parentId")
    public InforCategory getParent() {
        return this.parent;
    }
    
    public void setParent(InforCategory parent) {
        this.parent = parent;
    }
    
	@OneToMany(mappedBy = "parent",fetch=FetchType.LAZY,cascade = CascadeType.ALL)
	@OrderBy("orderNo")
    public Set<InforCategory> getChilds() {
        return this.childs;
    }
    
    public void setChilds(Set<InforCategory> childs) {
        this.childs = childs;
    }
    
	@OneToMany(mappedBy = "category",fetch=FetchType.LAZY)
	@Cascade(value = {org.hibernate.annotations.CascadeType.DELETE_ORPHAN, org.hibernate.annotations.CascadeType.ALL})
    public Set<InforCategoryRight> getRights() {
        return this.rights;
    }
    
    public void setRights(Set<InforCategoryRight> rights) {
        this.rights = rights;
    }

    @Column(columnDefinition = "nvarchar(200)")
	public String getContentTemplate() {
		return contentTemplate;
	}

	public void setContentTemplate(String contentTemplate) {
		this.contentTemplate = contentTemplate;
	}

    public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean isInherit() {
		return inherit;
	}

	public void setInherit(boolean inherit) {
		this.inherit = inherit;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	@Column(columnDefinition = "nvarchar(200)")
	public String getListTemplate() {
		return listTemplate;
	}

	public void setListTemplate(String listTemplate) {
		this.listTemplate = listTemplate;
	}

    @Column(columnDefinition = "nvarchar(200)")
	public String getPagePath() {
		return pagePath;
	}

	public void setPagePath(String pagePath) {
		this.pagePath = pagePath;
	}

    @OneToMany(mappedBy = "category",fetch = FetchType.LAZY)
    @OrderBy("displayOrder")
    @Cascade(value = {org.hibernate.annotations.CascadeType.DELETE_ORPHAN, org.hibernate.annotations.CascadeType.ALL})
	public Set<InforField> getFields() {
		return fields;
	}

	public void setFields(Set<InforField> fields) {
		this.fields = fields;
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

}


