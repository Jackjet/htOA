package com.kwchina.extend.evaluationvote.vo;

import com.kwchina.extend.evaluationvote.entity.PyTopicInfor;

public class PyItemInforVo {
	
	private Integer itemId=0;
	private String itemName; //条目标题
	private int  itemType;//类型
	private int  displayOrder;//条目排序
	private int topicId; //所属主题
	
	
	public int getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}
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
	public int getItemType() {
		return itemType;
	}
	public void setItemType(int itemType) {
		this.itemType = itemType;
	}
	public int getTopicId() {
		return topicId;
	}
	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}
	
	
	
	
}