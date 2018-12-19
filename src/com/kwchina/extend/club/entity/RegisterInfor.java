package com.kwchina.extend.club.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.kwchina.core.base.entity.SystemUserInfor;
/**
 * 报名情况
 * @author suguan
 *
 */
@Entity
@Table(name = "Club_RegisterInfor", schema = "dbo")
public class RegisterInfor {

    private Integer regId;
    private Timestamp regTime;      //报名时间
    
    
    private ClubInfor clubInfor;  //所属活动
    private SystemUserInfor reger;       //报名人
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getRegId() {
		return regId;
	}
	public void setRegId(Integer regId) {
		this.regId = regId;
	}
	
	
	public Timestamp getRegTime() {
		return regTime;
	}
	public void setRegTime(Timestamp regTime) {
		this.regTime = regTime;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="actId")
	public ClubInfor getClubInfor() {
		return clubInfor;
	}
	public void setClubInfor(ClubInfor clubInfor) {
		this.clubInfor = clubInfor;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="personId")
	public SystemUserInfor getReger() {
		return reger;
	}
	public void setReger(SystemUserInfor reger) {
		this.reger = reger;
	}
    
    

}


