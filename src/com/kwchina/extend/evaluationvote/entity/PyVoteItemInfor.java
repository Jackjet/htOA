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
@Table(name = "PY_VoteItemInfor", schema = "dbo")
@ObjectId(id="detailId")
public class PyVoteItemInfor implements JSONNotAware{
	
	private Integer detailId;
	private String voteValue;
	private int selected;
	private PyVoteInfor voteInfor;
	private PyPersonInfor personInfor;
	private PyItemInfor itemInfor;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getDetailId() {
		return detailId;
	}
	public void setDetailId(Integer detailId) {
		this.detailId = detailId;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="itemId", nullable = false)
	public PyItemInfor getItemInfor() {
		return itemInfor;
	}
	public void setItemInfor(PyItemInfor itemInfor) {
		this.itemInfor = itemInfor;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="pId", nullable = false)
	public PyPersonInfor getPersonInfor() {
		return personInfor;
	}
	public void setPersonInfor(PyPersonInfor personInfor) {
		this.personInfor = personInfor;
	}
	
	public int getSelected() {
		return selected;
	}
	public void setSelected(int selected) {
		this.selected = selected;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="voteId", nullable = false)
	public PyVoteInfor getVoteInfor() {
		return voteInfor;
	}
	public void setVoteInfor(PyVoteInfor voteInfor) {
		this.voteInfor = voteInfor;
	}
	
	@Column(columnDefinition = "nvarchar(200)",nullable = true)
	public String getVoteValue() {
		return voteValue;
	}
	public void setVoteValue(String voteValue) {
		this.voteValue = voteValue;
	}
	

}