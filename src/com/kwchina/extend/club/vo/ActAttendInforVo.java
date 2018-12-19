package com.kwchina.extend.club.vo;

/**
 * 签到情况
 * @author suguan
 *
 */
public class ActAttendInforVo {

    private Integer attId;
    private String attTimeStr;      //签到时间
    private int hasReged;           //是否是报过名的
    private String attLocation;     //签到时的位置（经纬度）
    
    
    private int actId;  //所属活动
    private int attenderId;       //签到人
	public Integer getAttId() {
		return attId;
	}
	public void setAttId(Integer attId) {
		this.attId = attId;
	}
	public String getAttTimeStr() {
		return attTimeStr;
	}
	public void setAttTimeStr(String attTimeStr) {
		this.attTimeStr = attTimeStr;
	}
	public int getHasReged() {
		return hasReged;
	}
	public void setHasReged(int hasReged) {
		this.hasReged = hasReged;
	}
	public String getAttLocation() {
		return attLocation;
	}
	public void setAttLocation(String attLocation) {
		this.attLocation = attLocation;
	}
	public int getActId() {
		return actId;
	}
	public void setActId(int actId) {
		this.actId = actId;
	}
	public int getAttenderId() {
		return attenderId;
	}
	public void setAttenderId(int attenderId) {
		this.attenderId = attenderId;
	}
    
    
    
    

}


