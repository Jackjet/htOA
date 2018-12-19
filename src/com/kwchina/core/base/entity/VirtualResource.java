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

@Entity
@Table(name = "Core_VirtualResource", schema = "dbo")
public class VirtualResource {

    private Integer resourceId;
    private String resourceName;	//资源名称
    private String aliasName;		//别名
    private String resourcePath;	//资源路径（URI）
    private String description;		//功能描述
    private int orderNo;			//排序编号
    private boolean hided;			//是否隐藏:0-	显示;1-隐藏.
    private int layer;				//层级
    private int resourceType;		//判断权限:1-需要判断子权限;2-需要判断本身的权限.
    private int virResourceType;	//系统资源类型,用于区分属于OA还是档案系统:0-OA;1-档案.
    private String primarykeyName;	//主键名称,用于对数据权限判断的主键进行描述
    private String imgPath;			//图片路径,用于控制界面显示不同功能模块的不同图标
    private VirtualResource parent;
    private Set<VirtualResource> childs = new HashSet<VirtualResource>(0);
    private Set<FunctionRightInfor> functionRights = new HashSet<FunctionRightInfor>(0);
    private Set<DataRightInfor> dataRights = new HashSet<DataRightInfor>(0);

   
    @Column(columnDefinition = "nvarchar(100)",nullable = false)
    public String getResourceName() {
        return this.resourceName;
    }
    
	@OneToMany(mappedBy = "resource",cascade = CascadeType.ALL)
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.ALL)
    public Set<FunctionRightInfor> getFunctionRights() {
		return functionRights;
	}

	public void setFunctionRights(Set<FunctionRightInfor> functionRights) {
		this.functionRights = functionRights;
	}

	public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }
    
   	@Column(columnDefinition = "nvarchar(200)",nullable = false)
    public String getResourcePath() {
        return this.resourcePath;
    }
    
    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }
    
	@Column(columnDefinition = "ntext")
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    public int getOrderNo() {
        return this.orderNo;
    }
    
    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }
    public boolean isHided() {
        return this.hided;
    }
    
    public void setHided(boolean hided) {
        this.hided = hided;
    }
    public int getLayer() {
        return this.layer;
    }
    
    public void setLayer(int layer) {
        this.layer = layer;
    }
    
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="parentId")
    public VirtualResource getParent() {
        return this.parent;
    }
    
    public void setParent(VirtualResource parent) {
        this.parent = parent;
    }
    
	@OneToMany(mappedBy = "parent",cascade = CascadeType.ALL)
	@OrderBy("orderNo")
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.ALL)
    public Set<VirtualResource> getChilds() {
        return this.childs;
    }
    
    public void setChilds(Set<VirtualResource> childs) {
        this.childs = childs;
    }
    
   	@Column(columnDefinition = "nvarchar(100)")
	public String getAliasName() {
		return aliasName;
	}


	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}
	
	public int getVirResourceType() {
		return virResourceType;
	}


	public void setVirResourceType(int virResourceType) {
		this.virResourceType = virResourceType;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getResourceId() {
		return resourceId;
	}

	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}

    @Column(columnDefinition = "nvarchar(200)")
	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

    @Column(columnDefinition = "nvarchar(80)")
	public String getPrimarykeyName() {
		return primarykeyName;
	}

	public void setPrimarykeyName(String primarykeyName) {
		this.primarykeyName = primarykeyName;
	}

	public int getResourceType() {
		return resourceType;
	}

	public void setResourceType(int resourceType) {
		this.resourceType = resourceType;
	}

	@OneToMany(mappedBy = "resource",cascade = CascadeType.ALL)
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.ALL)
	public Set<DataRightInfor> getDataRights() {
		return dataRights;
	}

	public void setDataRights(Set<DataRightInfor> dataRights) {
		this.dataRights = dataRights;
	}
}


