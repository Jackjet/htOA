package com.kwchina.core.cms.entity;

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
@Table(name = "Infor_Document_Field", schema = "dbo")
public class InforField {


    private Integer fieldId;
    private String fieldName;		//字段名
    private String displayTitle;	//显示标题
    private boolean displayed;		//是否显示:0-	不显示;1-	显示.(添加信息时)
    private int displayOrder;		//显示顺序(添加信息时)
    private boolean listDisplayed;	//是否显示:0-	不显示;1-	显示.(浏览信息时)
    private int listOrder;			//显示顺序(浏览信息时)
    private InforCategory category;	//所属资讯分类

   
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getFieldId() {
        return this.fieldId;
    }
    
    public void setFieldId(Integer fieldId) {
        this.fieldId = fieldId;
    }
    
    @Column(columnDefinition = "nvarchar(100)", nullable = false)
    public String getFieldName() {
        return this.fieldName;
    }
    
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    
    @Column(columnDefinition = "nvarchar(100)", nullable = false)
    public String getDisplayTitle() {
        return this.displayTitle;
    }
    
    public void setDisplayTitle(String displayTitle) {
        this.displayTitle = displayTitle;
    }
    public boolean isDisplayed() {
        return this.displayed;
    }
    
    public void setDisplayed(boolean displayed) {
        this.displayed = displayed;
    }
    
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="categoryId")
    public InforCategory getCategory() {
        return this.category;
    }
    
    public void setCategory(InforCategory category) {
        this.category = category;
    }

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}
	
	public boolean isListDisplayed() {
		return listDisplayed;
	}

	public void setListDisplayed(boolean listDisplayed) {
		this.listDisplayed = listDisplayed;
	}

	public int getListOrder() {
		return listOrder;
	}

	public void setListOrder(int listOrder) {
		this.listOrder = listOrder;
	}

}


