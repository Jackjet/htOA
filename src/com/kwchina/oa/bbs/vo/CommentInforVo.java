package com.kwchina.oa.bbs.vo;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public class CommentInforVo {
	private int commentId;
    private Timestamp replyDate;
    private String replyContent;
    private String nickName;
    private int thesisId;
    private int Author;
    private String thesisTitle;
    private int  replyDateAsc;
    private String imgAttachment;				//图片附件
    private String[] imgAttachmentArray = {};  	//图片附件路径
    
    private int page;
  	private int inpages;

     public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest httpServletRequest) {
 		ActionErrors errors = new ActionErrors();
 		if (getInpages() == 0) {
 			setInpages(1);
 		}
 		if (getPage() == 0) {
 			setPage(1);
 		}
 		return errors;
 	}
	
    
    public int getPage() {
		return page;
	}


	public void setPage(int page) {
		this.page = page;
	}


	public int getInpages() {
		return inpages;
	}


	public void setInpages(int inpages) {
		this.inpages = inpages;
	}


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
	public String getReplyContent() {
		return replyContent;
	}
	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public int getThesisId() {
		return thesisId;
	}
	public void setThesisId(int thesisId) {
		this.thesisId = thesisId;
	}
	public int getAuthor() {
		return Author;
	}
	public void setAuthor(int author) {
		Author = author;
	}
	public String getThesisTitle() {
		return thesisTitle;
	}
	public void setThesisTitle(String thesisTitle) {
		this.thesisTitle = thesisTitle;
	}
	public int getReplyDateAsc() {
		return replyDateAsc;
	}
	public void setReplyDateAsc(int replyDateAsc) {
		this.replyDateAsc = replyDateAsc;
	}
	public String getImgAttachment() {
		return imgAttachment;
	}
	public void setImgAttachment(String imgAttachment) {
		this.imgAttachment = imgAttachment;
	}
	public String[] getImgAttachmentArray() {
		return imgAttachmentArray;
	}
	public void setImgAttachmentArray(String[] imgAttachmentArray) {
		this.imgAttachmentArray = imgAttachmentArray;
	}
    
    
}
