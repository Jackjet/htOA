package com.kwchina.oa.bbs.entity;


import javax.persistence.*;

import com.kwchina.core.base.entity.SystemUserInfor;

import java.sql.Timestamp;

@Entity
@Table(name = "BBS_CommentInfor", schema = "dbo")
public class CommentInfor {


	private int commentId;
    private Timestamp replyDate;
    private String replyContent;
    private String nickName;
    private String imgAttachment;	//图片附件
    
    private ThesisInfor thesisInfor;
    private SystemUserInfor replyMan;
    

    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getCommentId() {
		return commentId;
	}

	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}
	

    public Timestamp getReplyDate() {
        return replyDate;
    }

    public void setReplyDate(Timestamp replyDate) {
        this.replyDate = replyDate;
    }


    @Column(columnDefinition = "ntext")
    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    @Column(columnDefinition = "nvarchar(100)", nullable = false)
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }



    @ManyToOne(cascade =CascadeType.ALL, fetch = FetchType.LAZY)
    @javax.persistence.JoinColumn(name = "thesisId")
    public ThesisInfor getThesisInfor() {
        return thesisInfor;
    }

    public void setThesisInfor(ThesisInfor thesisInfor) {
        this.thesisInfor = thesisInfor;
    }



	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @javax.persistence.JoinColumn(name = "replyMan")
    public SystemUserInfor getReplyMan() {
        return replyMan;
    }

    public void setReplyMan(SystemUserInfor replyMan) {
        this.replyMan = replyMan;
    }

    @Column(columnDefinition = "nvarchar(1000)", nullable = true)
	public String getImgAttachment() {
		return imgAttachment;
	}

	public void setImgAttachment(String imgAttachment) {
		this.imgAttachment = imgAttachment;
	}
}
