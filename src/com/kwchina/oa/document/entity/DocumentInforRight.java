package com.kwchina.oa.document.entity;

import javax.persistence.Column;
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
@Table(name = "Document_DocumentInforRight", schema = "dbo")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "rightType", discriminatorType = DiscriminatorType.INTEGER)
public class DocumentInforRight {

	public static final int _Right_Edit = 1;		//1.新增,修改
	public static final int _Right_Delete = 2;		//2:删除
	public static final int _Right_View = 3;		//3:浏览
	
	
    private Integer rightId;
    private int operateRight;		//操作权限数据
    private DocumentInfor document;	//文档信息

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getRightId() {
        return this.rightId;
    }
    
    public void setRightId(Integer rightId) {
        this.rightId = rightId;
    }
    
    @Column(columnDefinition = "int",nullable = false)
    public int getOperateRight() {
        return this.operateRight;
    }
    
    public void setOperateRight(int operateRight) {
        this.operateRight = operateRight;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "documentId")
    public DocumentInfor getDocument() {
        return this.document;
    }
    
    public void setDocument(DocumentInfor document) {
        this.document = document;
    }


}


