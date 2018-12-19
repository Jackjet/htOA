package com.kwchina.core.base.entity;


import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.kwchina.core.util.json.JSONNotAware;
/**
 * app版本
 * @author suguan
 *
 */
@Entity
@Table(name = "Core_Version", schema = "dbo")
public class AppVersion implements JSONNotAware {
	
	private Integer id;
	private String version;        //当前版本
	private String platform;       //平台（ios  android）
	
	private Timestamp updateTime;  //更新时间
	private String appPath;        //app存放路径
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	

	@Column(columnDefinition = "nvarchar(255)")
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	

	@Column(columnDefinition = "nvarchar(2000)")
	public String getAppPath() {
		return appPath;
	}
	public void setAppPath(String appPath) {
		this.appPath = appPath;
	}
	
	
	@Column(columnDefinition = "nvarchar(255)")
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	

}


