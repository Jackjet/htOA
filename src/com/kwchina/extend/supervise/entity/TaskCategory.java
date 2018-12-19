package com.kwchina.extend.supervise.entity;

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
import com.kwchina.oa.document.entity.DocumentInfor;
/**
 * 任务分类
 * @author suguan
 *
 */
@Entity
@Table(name = "Supervise_TaskCategory", schema = "dbo")
@ObjectId(id="categoryId")
public class TaskCategory implements JSONNotAware{

    private Integer categoryId;
    private String categoryName;		//分类名称
    private int layer;					//层级
    private String fullPath;			//全路径(1,2,3)
    private int categoryType;		//分类类别 1-行政，2-党群 3-部门建设 4-内控
    private boolean leaf;				//是否页分类:0-否;1-是.
    private int period;                 //提醒周期，1-每月25日，2-每两月25日
    private int displayNo;				//显示次序
    private int leftIndex;
    private int rightIndex;
    
    private TaskCategory parent;	//父分类
    private Set<TaskCategory> childs = new HashSet<TaskCategory>(0);
    private Set<TaskCategoryRight> rights = new HashSet<TaskCategoryRight>(0);
    private Set<SuperviseInfor> tasks = new HashSet<SuperviseInfor>(0);

    
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
    public TaskCategory getParent() {
        return this.parent;
    }
    
    public void setParent(TaskCategory parent) {
        this.parent = parent;
    }
    
    @OneToMany(mappedBy = "parent",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    @OrderBy("displayNo")
    public Set<TaskCategory> getChilds() {
        return this.childs;
    }
    
    public void setChilds(Set<TaskCategory> childs) {
        this.childs = childs;
    }
    
    @OneToMany(mappedBy = "category",fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.DELETE_ORPHAN, org.hibernate.annotations.CascadeType.ALL})
    public Set<TaskCategoryRight> getRights() {
        return this.rights;
    }
    
    public void setRights(Set<TaskCategoryRight> rights) {
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


	public int getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(int categoryType) {
		this.categoryType = categoryType;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	
	@OneToMany(mappedBy = "taskCategory",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	public Set<SuperviseInfor> getTasks() {
		return tasks;
	}

	public void setTasks(Set<SuperviseInfor> tasks) {
		this.tasks = tasks;
	}
	
	
}


