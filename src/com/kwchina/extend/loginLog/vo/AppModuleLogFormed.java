package com.kwchina.extend.loginLog.vo;


/**
 * app 各模块的使用频率（精确到人）
 * @author suguan
 *
 */
public class AppModuleLogFormed {
	
    private Integer logId;
    
    private String platform;            //登录平台  ios / android
    private String logTime;          //进入时间
    private String moduleName;          //模块名称
    private String userName;            //用户名
    
    public Integer getLogId() {
		return logId;
	}
	public void setLogId(Integer logId) {
		this.logId = logId;
	}
	

	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	
	
	public String getLogTime() {
		return logTime;
	}
	public void setLogTime(String logTime) {
		this.logTime = logTime;
	}
	

	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

}


