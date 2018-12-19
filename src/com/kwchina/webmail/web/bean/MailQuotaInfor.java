package com.kwchina.webmail.web.bean;

public class MailQuotaInfor {

	 private String name;
	 private int limit;
	 private int usage;
	 private int limitKB;
	 private int usageKB;
	 private String usagePct;
	 
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public int getLimitKB() {
		return limitKB;
	}
	public void setLimitKB(int limitKB) {
		this.limitKB = limitKB;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getUsage() {
		return usage;
	}
	public void setUsage(int usage) {
		this.usage = usage;
	}
	public int getUsageKB() {
		return usageKB;
	}
	public void setUsageKB(int usageKB) {
		this.usageKB = usageKB;
	}
	public String getUsagePct() {
		return usagePct;
	}
	public void setUsagePct(String usagePct) {
		this.usagePct = usagePct;
	}
	 
}
