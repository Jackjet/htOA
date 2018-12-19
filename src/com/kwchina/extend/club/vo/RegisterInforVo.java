package com.kwchina.extend.club.vo;

/**
 * 报名情况
 * @author suguan
 *
 */
public class RegisterInforVo {

    private Integer regId;
    private String regTimeStr;      //报名时间
    
    
    private int actId;  //所属活动
    private int regeId;       //报名人
	public Integer getRegId() {
		return regId;
	}
	public void setRegId(Integer regId) {
		this.regId = regId;
	}
	public String getRegTimeStr() {
		return regTimeStr;
	}
	public void setRegTimeStr(String regTimeStr) {
		this.regTimeStr = regTimeStr;
	}
	public int getActId() {
		return actId;
	}
	public void setActId(int actId) {
		this.actId = actId;
	}
	public int getRegeId() {
		return regeId;
	}
	public void setRegeId(int regeId) {
		this.regeId = regeId;
	}
    

}


