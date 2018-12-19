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
@Table(name = "Core_StructureInfor", schema = "dbo")
@ObjectId(id="structureId")
public class StructureInfor implements JSONNotAware {

    private Integer structureId;
    private String structureName;	//岗位名称
    private String fullPath;		//全路径
    private int orderNo;			//排序编号
    private int layer;				//层次
    private StructureInfor parent;	//父岗位
    private Set<StructureInfor> childs = new HashSet<StructureInfor>(0);

     
    @Column(columnDefinition = "nvarchar(80)",nullable = false)
    public String getStructureName() {
        return this.structureName;
    }
    
    public void setStructureName(String structureName) {
        this.structureName = structureName;
    }
    
   	@Column(columnDefinition = "nvarchar(100)")
    public String getFullPath() {
        return this.fullPath;
    }
    
    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }
    public int getOrderNo() {
        return this.orderNo;
    }
    
    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }
    public int getLayer() {
        return this.layer;
    }
    
    public void setLayer(int layer) {
        this.layer = layer;
    }
    
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="parentId")
    public StructureInfor getParent() {
        return this.parent;
    }
    
    public void setParent(StructureInfor parent) {
        this.parent = parent;
    }
    
	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@OrderBy("orderNo")
    public Set<StructureInfor> getChilds() {
        return this.childs;
    }
    
    public void setChilds(Set<StructureInfor> childs) {
        this.childs = childs;
    }

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getStructureId() {
		return structureId;
	}

	public void setStructureId(Integer structureId) {
		this.structureId = structureId;
	}


}


