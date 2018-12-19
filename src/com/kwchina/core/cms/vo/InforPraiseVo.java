package com.kwchina.core.cms.vo;


public class InforPraiseVo {
  	
  	
  	private int praiseId;
    private int praised;          //是否赞（0-未赞（即赞过后取消）  1-已赞）
    
    private int inforId;
    private int praiserId;  //点赞人
	public int getPraiseId() {
		return praiseId;
	}
	public void setPraiseId(int praiseId) {
		this.praiseId = praiseId;
	}
	public int getPraised() {
		return praised;
	}
	public void setPraised(int praised) {
		this.praised = praised;
	}
	public int getInforId() {
		return inforId;
	}
	public void setInforId(int inforId) {
		this.inforId = inforId;
	}
	public int getPraiserId() {
		return praiserId;
	}
	public void setPraiserId(int praiserId) {
		this.praiserId = praiserId;
	}
  	

}
