package com.kwchina.webmail.config;

/*
 * StringConfigParameter.java
 * 
 */

public class StringConfigParameter extends ConfigParameter {

	public StringConfigParameter(String name, String def, String desc) {
		super(name, def, desc);
	}

	public boolean isPossibleValue(Object value) {
		if (value instanceof String) {
			return true;
		} else {
			return false;
		}
	}

	public String getType() {
		return "string";
	}
}
