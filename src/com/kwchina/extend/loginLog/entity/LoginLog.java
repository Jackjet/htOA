package com.kwchina.extend.loginLog.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.kwchina.core.util.json.JSONNotAware;

@Entity
@Table(name = "Core_LoginLog", schema = "dbo")
public class LoginLog implements JSONNotAware {
	
    private Integer logId;
    
    private String logFrom;             //登录途径  pc  app
    private Timestamp logTime;          //登录时间
    private int sucTag;                 //是否登录成功标志  0- 未成功  1-成功
    
//    private SystemUserInfor loginUser;	//登录人
    private String userName;            //登录用户名
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getLogId() {
		return logId;
	}
	public void setLogId(Integer logId) {
		this.logId = logId;
	}
	

  	@Column(columnDefinition = "nvarchar(100)",nullable = true)
  	public String getLogFrom() {
		return logFrom;
	}
	public void setLogFrom(String logFrom) {
		this.logFrom = logFrom;
	}
	
	public Timestamp getLogTime() {
		return logTime;
	}
	public void setLogTime(Timestamp logTime) {
		this.logTime = logTime;
	}
	
//	@ManyToOne(fetch=FetchType.LAZY)
//	@JoinColumn(name="userId", nullable = false)
//	public SystemUserInfor getLoginUser() {
//		return loginUser;
//	}
//	
//	public void setLoginUser(SystemUserInfor loginUser) {
//		this.loginUser = loginUser;
//	}
	public int getSucTag() {
		return sucTag;
	}
	public void setSucTag(int sucTag) {
		this.sucTag = sucTag;
	}
	
	@Column(columnDefinition = "nvarchar(500)",nullable = true)
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}


}


