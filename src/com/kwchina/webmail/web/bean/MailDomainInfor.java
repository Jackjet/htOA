package com.kwchina.webmail.web.bean;

public class MailDomainInfor {
	private String name;

	private String host;

	private String authentication;

	private String allowedHost;
	

	public String getAllowedHost() {
		return allowedHost;
	}

	public void setAllowedHost(String allowedHost) {
		this.allowedHost = allowedHost;
	} 

	public String getAuthentication() {
		return authentication;
	}

	public void setAuthentication(String authentication) {
		this.authentication = authentication;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
