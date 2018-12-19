package com.kwchina.extend.evaluationvote.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import com.kwchina.core.util.annotation.ObjectId;
import com.kwchina.core.util.json.JSONNotAware;

@Entity
@Table(name = "PY_ItemInfor", schema = "dbo")
@ObjectId(id="itemId")
public class PyItemInfor implements JSONNotAware{
	
	private Integer itemId;
	private String itemName; //条目标题
	private int  itemType;//类型
	private int  displayOrder;//条目排序
	private PyTopicInfor topicInfor; //所属主题
	
	private Set<PyOptionInfor> optionInfors = new HashSet<PyOptionInfor>(0);	//选项信息
	private Set<PyVoteItemInfor> voteItemInfors = new HashSet<PyVoteItemInfor>(0);	//投票信息
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getItemId() {
		return itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	
	public int getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}
	@Column(columnDefinition = "nvarchar(200)",nullable = false)
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
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="topicId", nullable = false)
	public PyTopicInfor getTopicInfor() {
		return topicInfor;
	}
	public void setTopicInfor(PyTopicInfor topicInfor) {
		this.topicInfor = topicInfor;
	}
	
	@OneToMany(mappedBy = "itemInfor",fetch=FetchType.LAZY)	
	@Cascade(value = {org.hibernate.annotations.CascadeType.REMOVE})
	@OrderBy("displayOrder")
	public Set<PyOptionInfor> getOptionInfors() {
		return optionInfors;
	}
	public void setOptionInfors(Set<PyOptionInfor> optionInfors) {
		this.optionInfors = optionInfors;
	}
	
	@OneToMany(mappedBy = "itemInfor",fetch=FetchType.LAZY)	
	@Cascade(value = {org.hibernate.annotations.CascadeType.REMOVE})
	@OrderBy("detailId")
	public Set<PyVoteItemInfor> getVoteItemInfors() {
		return voteItemInfors;
	}
	public void setVoteItemInfors(Set<PyVoteItemInfor> voteItemInfors) {
		this.voteItemInfors = voteItemInfors;
	}
	
}