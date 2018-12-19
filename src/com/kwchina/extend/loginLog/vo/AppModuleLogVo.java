package com.kwchina.extend.loginLog.vo;


public class AppModuleLogVo {
	
	private Integer logId;
    
	private String userName;         //登录用户名
    private String platform;         //平台
    private String logTimeStr;          //登录时间
    private String moduleName;          //模块名称
    private int logCount;               //登录次数
    
    
	public Integer getLogId() {
		return logId;
	}
	public void setLogId(Integer logId) {
		this.logId = logId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getLogTimeStr() {
		return logTimeStr;
	}
	public void setLogTimeStr(String logTimeStr) {
		this.logTimeStr = logTimeStr;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public int getLogCount() {
		return logCount;
	}
	public void setLogCount(int logCount) {
		this.logCount = logCount;
	}
	
    
    
}
