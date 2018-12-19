package com.kwchina.core.base.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.kwchina.core.util.json.JSONNotAware;

@Entity
@Table(name = "Core_AppImg", schema = "dbo")
public class AppImg implements JSONNotAware {
	
    private Integer imgId;
    private String imgTitle;			//图片标题
    private Date createDate;			//创建日期
    private Date updateDate;            //更新日期
    private int imgOrder;			    //图片排序
    private String attachment;		    //图片
    private String memo;                //备注
    
    private SystemUserInfor author;	//作者(提交人)
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getImgId() {
		return imgId;
	}
	public void setImgId(Integer imgId) {
		this.imgId = imgId;
	}
	

  	@Column(columnDefinition = "nvarchar(100)",nullable = true)
	public String getImgTitle() {
		return imgTitle;
	}
	public void setImgTitle(String imgTitle) {
		this.imgTitle = imgTitle;
	}
	
	
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
	
	public int getImgOrder() {
		return imgOrder;
	}
	public void setImgOrder(int imgOrder) {
		this.imgOrder = imgOrder;
	}
	

	@Column(columnDefinition = "ntext")
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	
	@Column(columnDefinition = "ntext")
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="authorId", nullable = false)
	public SystemUserInfor getAuthor() {
		return author;
	}
	public void setAuthor(SystemUserInfor author) {
		this.author = author;
	}				 


}


