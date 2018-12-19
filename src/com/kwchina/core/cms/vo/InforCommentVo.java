package com.kwchina.core.cms.vo;



public class InforCommentVo {
  	
    private int commentId;
    private String content;  //内容

    private int inforId;
    private int operaterId;  //评论人
    private String operaterName; //评论人
    private String operateTime;  //评论时间
    private String operaterPic;  //评论人头像
    
	public String getOperaterName() {
		return operaterName;
	}
	public void setOperaterName(String operaterName) {
		this.operaterName = operaterName;
	}
	public String getOperateTime() {
		return operateTime;
	}
	public void setOperateTime(String operateTime) {
		this.operateTime = operateTime;
	}
	public int getCommentId() {
		return commentId;
	}
	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getInforId() {
		return inforId;
	}
	public void setInforId(int inforId) {
		this.inforId = inforId;
	}
	public int getOperaterId() {
		return operaterId;
	}
	public void setOperaterId(int operaterId) {
		this.operaterId = operaterId;
	}
	public String getOperaterPic() {
		return operaterPic;
	}
	public void setOperaterPic(String operaterPic) {
		this.operaterPic = operaterPic;
	}
    

}
