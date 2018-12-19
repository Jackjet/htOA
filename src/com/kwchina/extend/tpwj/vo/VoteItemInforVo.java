package com.kwchina.extend.tpwj.vo;


public class VoteItemInforVo {

	private Integer detailId;
    private String voteText;        //内容
    private String voteValue;       //option选择值 （单选/多选/列表） “2,3,4”
    
    private int voteInforId;
    private int itemId;
	public Integer getDetailId() {
		return detailId;
	}
	public void setDetailId(Integer detailId) {
		this.detailId = detailId;
	}
	public String getVoteText() {
		return voteText;
	}
	public void setVoteText(String voteText) {
		this.voteText = voteText;
	}
	public String getVoteValue() {
		return voteValue;
	}
	public void setVoteValue(String voteValue) {
		this.voteValue = voteValue;
	}
	public int getVoteInforId() {
		return voteInforId;
	}
	public void setVoteInforId(int voteInforId) {
		this.voteInforId = voteInforId;
	}
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
    
    
}
