package com.kwchina.extend.loginLog.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.kwchina.core.util.json.JSONNotAware;

/**
 * app 各模块的使用频率（精确到人）
 * @author suguan
 *
 */
@Entity
@Table(name = "Core_AppModuleLog", schema = "dbo")
public class AppModuleLog implements JSONNotAware {
	
    private Integer logId;
    
    private String platform;            //登录平台  ios / android
    private Timestamp logTime;          //进入时间
    private String moduleName;          //模块名称
    private String userName;            //用户名
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getLogId() {
		return logId;
	}
	public void setLogId(Integer logId) {
		this.logId = logId;
	}
	

	@Column(columnDefinition = "nvarchar(50)",nullable = true)
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	
	
	public Timestamp getLogTime() {
		return logTime;
	}
	public void setLogTime(Timestamp logTime) {
		this.logTime = logTime;
	}
	

	@Column(columnDefinition = "nvarchar(500)",nullable = true)
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	

	@Column(columnDefinition = "nvarchar(100)",nullable = true)
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

}


