package com.kwchina.extend.supervise.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.kwchina.core.base.entity.SystemUserInfor;

@Entity
@Table(name = "Supervise_TaskCategoryRight", schema = "dbo")
public class TaskCategoryRight {

//	public static final int _Right_Create = 1;		//1.创建
//	public static final int _Right_Delete = 2;		//2:删除
//	public static final int _Right_Edit = 3;		//3:修改
//	public static final int _Right_View = 4;		//4:浏览
//	public static final int _Right_Transfer = 5;	//5.转移
//	public static final int _Right_Commend = 6;		//6.推荐
	public static final int _Right_Edit = 1;		//1.新增,修改
	public static final int _Right_Delete = 2;		//2:删除
	public static final int _Right_View = 3;		//3:浏览
	
    private Integer rightId;
    private int operateRight;		//操作权限数据
    private TaskCategory category;//文档分类
    private SystemUserInfor user;	//用户

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
    public TaskCategory getCategory() {
        return this.category;
    }
    
    public void setCategory(TaskCategory category) {
        this.category = category;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personId")
    public SystemUserInfor getUser() {
        return this.user;
    }
    
    public void setUser(SystemUserInfor user) {
        this.user = user;
    }

}


