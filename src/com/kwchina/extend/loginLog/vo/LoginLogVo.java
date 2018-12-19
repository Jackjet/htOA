package com.kwchina.extend.loginLog.vo;


public class LoginLogVo {
	
	private Integer logId;
    
	private String userName;         //登录用户名
    private String logFrom;             //登录途径  pc  app
    private String logTimeStr;          //登录时间
    private String sucTagStr;                 //是否登录成功标志  0- 未成功  1-成功
    private int logCount;               //登录次数
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
	public String getLogTimeStr() {
		return logTimeStr;
	}
	public void setLogTimeStr(String logTimeStr) {
		this.logTimeStr = logTimeStr;
	}
	public String getSucTagStr() {
		return sucTagStr;
	}
	public void setSucTagStr(String sucTagStr) {
		this.sucTagStr = sucTagStr;
	}
	public int getLogCount() {
		return logCount;
	}
	public void setLogCount(int logCount) {
		this.logCount = logCount;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
    
    
}
