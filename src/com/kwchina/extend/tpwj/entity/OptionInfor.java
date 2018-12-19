package com.kwchina.extend.tpwj.entity;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 选项信息
 * @author suguan
 *
 */
@Entity
@Table(name = "TP_OptionInfor", schema = "dbo")
public class OptionInfor {

    private Integer optionId;
    private String optionName;         //选项名称
    private int optionValue;           //选项值
    private int displayOrder;          //选项排序
//    private float ration;             //权重
    private float score;             //权重
    private String picPath;            //图片路径
    
    private ItemInfor item;            //所属条目
    
    

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getOptionId() {
        return this.optionId;
    }
    
    public void setOptionId(Integer optionId) {
        this.optionId = optionId;
    }

    
    @Column(columnDefinition = "nvarchar(500)",nullable = true)
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

	
	@Column(columnDefinition = "nvarchar(500)",nullable = true)
	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="itemId", nullable = false)
	public ItemInfor getItem() {
		return item;
	}

	public void setItem(ItemInfor item) {
		this.item = item;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

    

}


