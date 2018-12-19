package com.kwchina.core.config.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.kwchina.core.cms.entity.InforCategory;
import com.kwchina.core.util.json.JSONNotAware;

@Entity
@Table(name = "config_indexfunc", schema = "dbo")
public class ConfigIndexFun implements JSONNotAware {

    private Integer id;
    private String displayName;			//实际显示名称
    private int displayCount;           //显示条数
    private boolean displayTime;        //是否显示时间 0-否，1-是
    private boolean roll;               //是否滚动     0-否，1-是
    
    private InforCategory category;     //类别名称
    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
   
  	@Column(columnDefinition = "nvarchar(80)",nullable = false)
    public String getDisplayName() {
        return this.displayName;
    }
    
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    
    
  	public int getDisplayCount() {
		return displayCount;
	}

	public void setDisplayCount(int displayCount) {
		this.displayCount = displayCount;
	}

	
	public boolean isDisplayTime() {
		return displayTime;
	}

	public void setDisplayTime(boolean displayTime) {
		this.displayTime = displayTime;
	}

	
	public boolean isRoll() {
		return roll;
	}

	public void setRoll(boolean roll) {
		this.roll = roll;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="categoryId")
	public InforCategory getCategory() {
		return category;
	}

	public void setCategory(InforCategory category) {
		this.category = category;
	}
	
}


