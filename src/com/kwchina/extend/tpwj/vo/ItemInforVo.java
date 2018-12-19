package com.kwchina.extend.tpwj.vo;


public class ItemInforVo {
	private Integer itemId;
    private String itemName;         //条目标题
    private String categoryName;     //分类名称
    private boolean need;                //是否必须
    private String tipText;          //提示文本
    private int itemType;            //类型  0-文本型,1-段落,2-单选,3-多选,4-列表,5-图片
    private int displayOrder;        //条目排序
    private float score;            //分值 （0或其它）
    private float ration;           //权重
    private String picAttach;          //图片路径
    private int topicId;        //所属主题
    
    private int checkCount;         //多选数量限制
    

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	

	public boolean isNeed() {
		return need;
	}

	public void setNeed(boolean need) {
		this.need = need;
	}

	public String getTipText() {
		return tipText;
	}

	public void setTipText(String tipText) {
		this.tipText = tipText;
	}

	public int getItemType() {
		return itemType;
	}

	public void setItemType(int itemType) {
		this.itemType = itemType;
	}


	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}


	public int getTopicId() {
		return topicId;
	}

	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getPicAttach() {
		return picAttach;
	}

	public void setPicAttach(String picAttach) {
		this.picAttach = picAttach;
	}

	public float getRation() {
		return ration;
	}

	public void setRation(float ration) {
		this.ration = ration;
	}

	public int getCheckCount() {
		return checkCount;
	}

	public void setCheckCount(int checkCount) {
		this.checkCount = checkCount;
	}


    
    
}
