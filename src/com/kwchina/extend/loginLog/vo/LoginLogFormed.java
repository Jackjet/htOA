package com.kwchina.extend.loginLog.vo;


public class LoginLogFormed {
private Integer logId;
    
    private String logFrom;             //登录途径  pc  app
    private String logTime;          //登录时间
    private int sucTag;                 //是否登录成功标志  0- 未成功  1-成功
    private String userName;            //登录用户名
	public Integer getLogId() {
		return logId;
	}
	public void setLogId(Integer logId) {
		this.logId = logId;
	}
	public String getLogFrom() {
		return logFrom;
	}
	public void setLogFrom(String logFrom) {
		this.logFrom = logFrom;
	}
	public String getLogTime() {
		return logTime;
	}
	public void setLogTime(String logTime) {
		this.logTime = logTime;
	}
	public int getSucTag() {
		return sucTag;
	}
	public void setSucTag(int sucTag) {
		this.sucTag = sucTag;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
    
    
    
}
