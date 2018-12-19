
package com.kwchina.webmail.config;

/*
 * ConfigIntegerParameter.java 
 */

public class IntegerConfigParameter extends ConfigParameter {

	public IntegerConfigParameter(String name, String def, String desc) {
		super(name, def, desc);
	}

	//判断是否是Integer类型的value
	public boolean isPossibleValue(Object value) {
		try {
			int i = Integer.parseInt((String) value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public String getType() {
		return "integer";
	}
}
