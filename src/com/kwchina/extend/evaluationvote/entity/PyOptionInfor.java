package com.kwchina.extend.evaluationvote.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.kwchina.core.util.annotation.ObjectId;
import com.kwchina.core.util.json.JSONNotAware;

@Entity
@Table(name = "PY_OptionInfor", schema = "dbo")
@ObjectId(id="optionId")
public class PyOptionInfor implements JSONNotAware{
	private Integer optionId;
	private String optionName;//选项名称
	private int displayOrder;//选项排序
	private PyItemInfor itemInfor;//选项信息
	private int selectNum;//选择次数
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getOptionId() {
		return optionId;
	}
	public void setOptionId(Integer optionId) {
		this.optionId = optionId;
	}
	
	public int getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="itemId", nullable = false)
	public PyItemInfor getItemInfor() {
		return itemInfor;
	}
	public void setItemInfor(PyItemInfor itemInfor) {
		this.itemInfor = itemInfor;
	}

	@Column(columnDefinition = "nvarchar(200)",nullable = false)
	public String getOptionName() {
		return optionName;
	}
	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}
	
	public int getSelectNum() {
		return selectNum;
	}
	public void setSelectNum(int selectNum) {
		this.selectNum = selectNum;
	}
	
	
}