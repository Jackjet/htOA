package com.kwchina.oa.bbs.vo;


public class ThesisInforVo {
	
	
	private Integer thesisId;           //id
	private String title;		    //标题
    private String content;		    //内容
    private String updateDate;   //发布时间 
    private String nickName;	    //昵称
    private String attachment;	    //附件
    private String attachmentArray;
    private int viewsCount;		    //浏览次数统计
    private boolean topThesis;	    //是否置顶：0-否；1-是.
    private boolean essence;	    //是否精品：0-否；1-是.
    private String imgAttachment;//图片附件
    private String[] imgAttachmentArray = {};   //图片附件路径
    private int author; //作者
    private int comments;//跟帖数
	
    
    public int getComments() {
		return comments;
	}
	public void setComments(int comments) {
		this.comments = comments;
	}
	public Integer getThesisId() {
		return thesisId;
	}
	public void setThesisId(Integer thesisId) {
		this.thesisId = thesisId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public int getViewsCount() {
		return viewsCount;
	}
	public void setViewsCount(int viewsCount) {
		this.viewsCount = viewsCount;
	}
	public boolean getTopThesis() {
		return topThesis;
	}
	public void setTopThesis(boolean topThesis) {
		this.topThesis = topThesis;
	}
	public boolean getEssence() {
		return essence;
	}
	public void setEssence(boolean essence) {
		this.essence = essence;
	}
	public String[] getImgAttachmentArray() {
		return imgAttachmentArray;
	}
	public void setImgAttachmentArray(String[] imgAttachmentArray) {
		this.imgAttachmentArray = imgAttachmentArray;
	}
	public int getAuthor() {
		return author;
	}
	public void setAuthor(int author) {
		this.author = author;
	}
	public String getImgAttachment() {
		return imgAttachment;
	}
	public void setImgAttachment(String imgAttachment) {
		this.imgAttachment = imgAttachment;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	public String getAttachmentArray() {
		return attachmentArray;
	}
	public void setAttachmentArray(String attachmentArray) {
		this.attachmentArray = attachmentArray;
	}
    
    
}