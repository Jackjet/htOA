package com.kwchina.core.cms.entity;


import javax.persistence.*;

import com.kwchina.core.base.entity.SystemUserInfor;

import java.sql.Timestamp;

@Entity
@Table(name = "Infor_DocumentComment", schema = "dbo")
public class InforComment {


	private int commentId;
    private Timestamp operateDate;
    private String content;
    
    private InforDocument infor;
    private SystemUserInfor operater;
    

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getCommentId() {
		return commentId;
	}

	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}

    
    public Timestamp getOperateDate() {
		return operateDate;
	}

	public void setOperateDate(Timestamp operateDate) {
		this.operateDate = operateDate;
	}

	@Column(columnDefinition = "ntext")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@ManyToOne(cascade =CascadeType.ALL, fetch = FetchType.LAZY)
    @javax.persistence.JoinColumn(name = "inforId")
	public InforDocument getInfor() {
		return infor;
	}

	public void setInfor(InforDocument infor) {
		this.infor = infor;
	}

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @javax.persistence.JoinColumn(name = "personId")
	public SystemUserInfor getOperater() {
		return operater;
	}

	public void setOperater(SystemUserInfor operater) {
		this.operater = operater;
	}

}
