package com.kwchina.core.base.vo;


public class AppImgVo{
	
    private Integer imgId;
    private String imgTitle;			//图片标题
    private int imgOrder;			    //图片排序
    private String[] attatchmentArray = {}; // 附件路径
    private String memo;                //备注
	public Integer getImgId() {
		return imgId;
	}
	public void setImgId(Integer imgId) {
		this.imgId = imgId;
	}
	public String getImgTitle() {
		return imgTitle;
	}
	public void setImgTitle(String imgTitle) {
		this.imgTitle = imgTitle;
	}
	public int getImgOrder() {
		return imgOrder;
	}
	public void setImgOrder(int imgOrder) {
		this.imgOrder = imgOrder;
	}
	public String[] getAttatchmentArray() {
		return attatchmentArray;
	}
	public void setAttatchmentArray(String[] attatchmentArray) {
		this.attatchmentArray = attatchmentArray;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}

    

}


