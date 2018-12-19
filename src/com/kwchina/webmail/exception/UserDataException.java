package com.kwchina.webmail.exception;

/*
 * WebMailException.java
 * 
 */

public class UserDataException extends WebMailException {

	String user;

	String domain;

	public UserDataException() {
		super();
	}

	public UserDataException(String s, String user, String domain) {
		super(s);
		this.user = user;
		this.domain = domain;
	}

	public String getUser() {
		return user;
	}

	public String getDomain() {
		return domain;
	}

} 
