package com.kwchina.core.cms.entity;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "Infor_Category_Right", schema = "dbo")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "rightType", discriminatorType = DiscriminatorType.INTEGER)
public class InforCategoryRight {

	public static final int _Right_Edit = 1;		//1.新增,修改
	public static final int _Right_Delete = 2;		//2:删除
	public static final int _Right_View = 3;		//3:浏览

    private Integer rightId;
    private int operateRight;		//操作权限(所占位):1-新增,修改;2-删除;3-浏览.
    private InforCategory category;	//分类信息

   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getRightId() {
        return this.rightId;
    }
    
    public void setRightId(Integer rightId) {
        this.rightId = rightId;
    }
    
    public int getOperateRight() {
        return this.operateRight;
    }
    
    public void setOperateRight(int operateRight) {
        this.operateRight = operateRight;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId")
    public InforCategory getCategory() {
        return this.category;
    }
    
    public void setCategory(InforCategory category) {
        this.category = category;
    }

}


