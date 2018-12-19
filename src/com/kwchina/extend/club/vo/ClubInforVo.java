package com.kwchina.extend.club.vo;

import java.sql.Date;

/**
 * 俱乐部活动内容
 * @author suguan
 *
 */
public class ClubInforVo{

    private Integer actId;
    private String actTitle;		//活动名称
    private String actItem;         //活动项目
    private String actTimeStr;        //活动开始时间
    private String toTimeStr;        //活动结束时间
    private String cutDateStr;        //报名截止时间
    private String actPlace;     //活动地点
    private String registerWay;  //报名方法
    private String actRule;      //活动办法
    private String memo;         //备注
    
    private String league;      //活动社团
//    private int loopPeriod;       //循环周期（间隔天数）
    private String beginSignDateStr;  //报名开始日期
    
    private int status;             //状态0-暂停  1-未开始报名 2-开始报名，未截止 3-截止报名，未开始 4-活动进行中 5-活动结束

    private int isDeleted;          //是否删除 0-否，1-是
    private String mainPicStr;      //附件 主图片
    private String twoPic;       //签到二维码
    
    private String endTimeStr;           //实际结束时间
    
    private String createTimeStr;           //创建时间
    
    private int createrId; //创建者
    private int managerId;  //管理员
    private String managerName; //管理员姓名
    private String createrName; //创建者姓名
    
    private String[] attatchmentArray = {}; 	//附件路径
    
    private int registerCount;      //报名人数
    private int signerCount;        //签到人数
    
	public int getRegisterCount() {
		return registerCount;
	}
	public void setRegisterCount(int registerCount) {
		this.registerCount = registerCount;
	}
	public int getSignerCount() {
		return signerCount;
	}
	public void setSignerCount(int signerCount) {
		this.signerCount = signerCount;
	}
	public String[] getAttatchmentArray() {
		return attatchmentArray;
	}
	public void setAttatchmentArray(String[] attatchmentArray) {
		this.attatchmentArray = attatchmentArray;
	}
	public Integer getActId() {
		return actId;
	}
	public void setActId(Integer actId) {
		this.actId = actId;
	}
	public String getActTitle() {
		return actTitle;
	}
	public void setActTitle(String actTitle) {
		this.actTitle = actTitle;
	}
	public String getActItem() {
		return actItem;
	}
	public void setActItem(String actItem) {
		this.actItem = actItem;
	}
	public String getActTimeStr() {
		return actTimeStr;
	}
	public void setActTimeStr(String actTimeStr) {
		this.actTimeStr = actTimeStr;
	}
	public String getCutDateStr() {
		return cutDateStr;
	}
	public void setCutDateStr(String cutDateStr) {
		this.cutDateStr = cutDateStr;
	}
	public String getActPlace() {
		return actPlace;
	}
	public void setActPlace(String actPlace) {
		this.actPlace = actPlace;
	}
	public String getRegisterWay() {
		return registerWay;
	}
	public void setRegisterWay(String registerWay) {
		this.registerWay = registerWay;
	}
	public String getActRule() {
		return actRule;
	}
	public void setActRule(String actRule) {
		this.actRule = actRule;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}
	public String getMainPicStr() {
		return mainPicStr;
	}
	public void setMainPicStr(String mainPicStr) {
		this.mainPicStr = mainPicStr;
	}
	public String getTwoPic() {
		return twoPic;
	}
	public void setTwoPic(String twoPic) {
		this.twoPic = twoPic;
	}
	public String getEndTimeStr() {
		return endTimeStr;
	}
	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}
	public String getCreateTimeStr() {
		return createTimeStr;
	}
	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}
	public int getCreaterId() {
		return createrId;
	}
	public void setCreaterId(int createrId) {
		this.createrId = createrId;
	}
	public int getManagerId() {
		return managerId;
	}
	public void setManagerId(int managerId) {
		this.managerId = managerId;
	}
	public String getManagerName() {
		return managerName;
	}
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}
	public String getCreaterName() {
		return createrName;
	}
	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}
	public String getToTimeStr() {
		return toTimeStr;
	}
	public void setToTimeStr(String toTimeStr) {
		this.toTimeStr = toTimeStr;
	}
	public String getLeague() {
		return league;
	}
	public void setLeague(String league) {
		this.league = league;
	}
	public String getBeginSignDateStr() {
		return beginSignDateStr;
	}
	public void setBeginSignDateStr(String beginSignDateStr) {
		this.beginSignDateStr = beginSignDateStr;
	}
    

	
}


