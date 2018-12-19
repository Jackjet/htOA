package com.kwchina.extend.tpwj.entity;



import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
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


/**
 * 条目信息
 * @author suguan
 *
 */
@Entity
@Table(name = "TP_ItemInfor", schema = "dbo")
public class ItemInfor {

    private Integer itemId;
    private String itemName;         //条目标题
    private String categoryName;     //分类名称
    private boolean need;                //是否必须
    private String tipText;          //提示文本
    private int itemType;            //类型  0-单选,1-多选,2-文本型,3-段落,4-图片,5-列表
    private int displayOrder;        //条目排序
    private float score;            //分值 （0或其它）
    private String picPath;          //图片路径
    
    private int checkCount;          //多选数据数量限制 
    
    private float ration;            //权重
      
    private TopicInfor topic;        //所属主题
    private Set<OptionInfor> options = new HashSet<OptionInfor>(0);
    

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getItemId() {
        return this.itemId;
    }
    
    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    
    @Column(columnDefinition = "nvarchar(500)",nullable = false)
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	
	@Column(columnDefinition = "nvarchar(500)",nullable = true)
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

	@Column(columnDefinition = "ntext")
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

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	
	@Column(columnDefinition = "nvarchar(500)",nullable = true)
	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="topicId", nullable = false)
	public TopicInfor getTopic() {
		return topic;
	}

	public void setTopic(TopicInfor topic) {
		this.topic = topic;
	}

	
	@OneToMany(mappedBy = "item",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@OrderBy("displayOrder")
	public Set<OptionInfor> getOptions() {
		return options;
	}

	public void setOptions(Set<OptionInfor> options) {
		this.options = options;
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


