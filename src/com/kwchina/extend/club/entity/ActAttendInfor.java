package com.kwchina.extend.club.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
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
 * 签到情况
 * @author suguan
 *
 */
@Entity
@Table(name = "Club_AttendInfor", schema = "dbo")
public class ActAttendInfor {

    private Integer attId;
    private Timestamp attTime;      //签到时间
    private int hasReged;           //是否是报过名的
    private String attLocation;     //签到时的位置（经纬度）
    
    
    private ClubInfor clubInfor;  //所属活动
    private SystemUserInfor attender;       //签到人
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getAttId() {
		return attId;
	}
	public void setAttId(Integer attId) {
		this.attId = attId;
	}
	
	
	public Timestamp getAttTime() {
		return attTime;
	}
	public void setAttTime(Timestamp attTime) {
		this.attTime = attTime;
	}
	
	
	public int getHasReged() {
		return hasReged;
	}
	public void setHasReged(int hasReged) {
		this.hasReged = hasReged;
	}
	
	
	@Column(columnDefinition = "ntext", nullable = true)
	public String getAttLocation() {
		return attLocation;
	}
	public void setAttLocation(String attLocation) {
		this.attLocation = attLocation;
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
	public SystemUserInfor getAttender() {
		return attender;
	}
	public void setAttender(SystemUserInfor attender) {
		this.attender = attender;
	}
    
    
    

}


