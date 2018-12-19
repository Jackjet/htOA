package com.kwchina.core.cms.entity;


import javax.persistence.*;

import com.kwchina.core.base.entity.SystemUserInfor;

import java.sql.Timestamp;

@Entity
@Table(name = "Infor_DocumentPraise", schema = "dbo")
public class InforPraise {


	private int praiseId;
    private Timestamp updateTime; //操作时间
    private int praised;          //是否赞（0-未赞（即赞过后取消）  1-已赞）
    
    private InforDocument infor;
    private SystemUserInfor praiser;  //点赞人
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getPraiseId() {
		return praiseId;
	}
	public void setPraiseId(int praiseId) {
		this.praiseId = praiseId;
	}
	
	
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	
	
	public int getPraised() {
		return praised;
	}
	public void setPraised(int praised) {
		this.praised = praised;
	}
	
	@ManyToOne(cascade =CascadeType.ALL, fetch = FetchType.LAZY)
    @javax.persistence.JoinColumn(name = "inforId")
	public InforDocument getInfor() {
		return infor;
	}
	public void setInfor(InforDocument infor) {
		this.infor = infor;
	}
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @javax.persistence.JoinColumn(name = "personId")
	public SystemUserInfor getPraiser() {
		return praiser;
	}
	public void setPraiser(SystemUserInfor praiser) {
		this.praiser = praiser;
	}
    
}
