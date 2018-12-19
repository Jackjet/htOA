package com.kwchina.extend.tpwj.vo;


public class OptionInforVo {

	private Integer optionId;
    private String optionName;         //选项名称
    private int optionValue;           //选项值
    private int displayOrder;          //选项排序
    //private float ration;             //权重
    private float score;
    
    private int itemId;            //所属条目

	public Integer getOptionId() {
		return optionId;
	}

	public void setOptionId(Integer optionId) {
		this.optionId = optionId;
	}

	public String getOptionName() {
		return optionName;
	}

	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}

	public int getOptionValue() {
		return optionValue;
	}

	public void setOptionValue(int optionValue) {
		this.optionValue = optionValue;
	}

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

//	public float getRation() {
//		return ration;
//	}
//
//	public void setRation(float ration) {
//		this.ration = ration;
//	}


	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}
    
    
}
