package com.kwchina.extend.club.entity;

import java.sql.Date;
import java.sql.Timestamp;
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

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.util.annotation.ObjectId;
import com.kwchina.core.util.json.JSONNotAware;
/**
 * 俱乐部活动内容
 * @author suguan
 *
 */
@Entity
@Table(name = "Club_BaseInfor", schema = "dbo")
@ObjectId(id="actId")
public class ClubInfor implements JSONNotAware{
	public static int Club_Status_Pause = 0;      //暂停
	public static int Club_Status_ToReg = 1;      //未开始报名
	public static int Club_Status_Reging = 2;     //报名中
	public static int Club_Status_ToGegin = 3;    //报名截止，未开始
	public static int Club_Status_Acting = 4;     //活动进行中（开始签到）
	public static int Club_Status_Over = 5;       //活动结束

    private Integer actId;
    private String actTitle;		//活动名称
    private String actItem;         //活动项目
    private Timestamp actTime;        //活动开始时间
    private Timestamp toTime;        //活动结束时间
    private Timestamp cutDate;        //报名截止时间
    private String actPlace;     //活动地点
    private String registerWay;  //报名方法
    private String actRule;      //活动办法
    private String memo;         //备注
    
    private String league;       //活动社团（“海之声”合唱团、博雅读书社、光影流年、酷跑团、轻“羽”飞扬、勇“网”直前、“乒”博社、奔跑吧，足球！、力挽狂“篮”）
//    private int loopPeriod;      //循环周期（间隔天数）
    private Timestamp beginSignDate;  //报名开始日期
    
    
    private int status;             //状态0-暂停  1-未开始报名 2-开始报名，未截止 3-截止报名，未开始 4-活动进行中 5-活动结束

    private int isDeleted;          //是否删除 0-否，1-是
    private String mainPic;      //附件 主图片
    private String twoPic;       //签到二维码
    
    private Date endTime;           //实际结束时间
    
    private Timestamp createTime;           //创建时间
    
    private SystemUserInfor creater; //创建者
    private SystemUserInfor manager;  //管理员
    

    private Set<RegisterInfor> registers = new HashSet<RegisterInfor>(0);    //报名
    private Set<ActAttendInfor> attends = new HashSet<ActAttendInfor>(0);    //签到
    
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getActId() {
		return actId;
	}

	public void setActId(Integer actId) {
		this.actId = actId;
	}

	@Column(columnDefinition = "nvarchar(500)", nullable = false)
	public String getActTitle() {
		return actTitle;
	}

	public void setActTitle(String actTitle) {
		this.actTitle = actTitle;
	}
	
	@Column(columnDefinition = "nvarchar(500)", nullable = true)
	public String getActItem() {
		return actItem;
	}
	public void setActItem(String actItem) {
		this.actItem = actItem;
	}
	
	
	public Timestamp getActTime() {
		return actTime;
	}
	public void setActTime(Timestamp actTime) {
		this.actTime = actTime;
	}
	
	
	public Timestamp getCutDate() {
		return cutDate;
	}
	public void setCutDate(Timestamp cutDate) {
		this.cutDate = cutDate;
	}
	
	@Column(columnDefinition = "nvarchar(1000)", nullable = true)
	public String getActPlace() {
		return actPlace;
	}
	public void setActPlace(String actPlace) {
		this.actPlace = actPlace;
	}
	
	@Column(columnDefinition = "nvarchar(1000)", nullable = true)
	public String getRegisterWay() {
		return registerWay;
	}
	public void setRegisterWay(String registerWay) {
		this.registerWay = registerWay;
	}
	
	@Column(columnDefinition = "nvarchar(1000)", nullable = true)
	public String getActRule() {
		return actRule;
	}
	public void setActRule(String actRule) {
		this.actRule = actRule;
	}
	
	@Column(columnDefinition = "ntext", nullable = true)
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
	
	@Column(columnDefinition = "ntext", nullable = true)
	public String getMainPic() {
		return mainPic;
	}
	public void setMainPic(String mainPic) {
		this.mainPic = mainPic;
	}
	
	@Column(columnDefinition = "ntext", nullable = true)
	public String getTwoPic() {
		return twoPic;
	}
	public void setTwoPic(String twoPic) {
		this.twoPic = twoPic;
	}
	
	
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="createrId")
	public SystemUserInfor getCreater() {
		return creater;
	}
	public void setCreater(SystemUserInfor creater) {
		this.creater = creater;
	}
	
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "managerId")
	public SystemUserInfor getManager() {
		return manager;
	}
	public void setManager(SystemUserInfor manager) {
		this.manager = manager;
	}

    
	@OneToMany(mappedBy = "clubInfor",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@OrderBy("regId")
	public Set<RegisterInfor> getRegisters() {
		return registers;
	}
	public void setRegisters(Set<RegisterInfor> registers) {
		this.registers = registers;
	}
	
	@OneToMany(mappedBy = "clubInfor",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@OrderBy("attId")
	public Set<ActAttendInfor> getAttends() {
		return attends;
	}
	public void setAttends(Set<ActAttendInfor> attends) {
		this.attends = attends;
	}
	public Timestamp getToTime() {
		return toTime;
	}
	public void setToTime(Timestamp toTime) {
		this.toTime = toTime;
	}
	
	
	
	@Column(columnDefinition = "nvarchar(1000)", nullable = true)
	public String getLeague() {
		return league;
	}
	public void setLeague(String league) {
		this.league = league;
	}
	
	
//	public int getLoopPeriod() {
//		return loopPeriod;
//	}
//	public void setLoopPeriod(int loopPeriod) {
//		this.loopPeriod = loopPeriod;
//	}
	
	
	public Timestamp getBeginSignDate() {
		return beginSignDate;
	}
	public void setBeginSignDate(Timestamp beginSignDate) {
		this.beginSignDate = beginSignDate;
	}
	
	

}


