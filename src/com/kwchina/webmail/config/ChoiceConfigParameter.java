package com.kwchina.webmail.config;

import java.util.*;

/*
 * ChoiceConfigParameter.java 
 */

/**
 * 选择项配置信息
 */
public class ChoiceConfigParameter extends ConfigParameter {

	Hashtable possible_values;

	public ChoiceConfigParameter(String name, String desc) {
		super(name, null, desc);
		possible_values = new Hashtable();
	}

	public void addChoice(Object choice, String desc) {
		/* First is default */
		if (possible_values.isEmpty()) {
			def_value = choice;
		}
		possible_values.put(choice, desc);
	}

	public void removeChoice(Object choice) {
		possible_values.remove(choice);
	}

	public Enumeration choices() {
		return possible_values.keys();
	}

	public String getDescription(String choice) {
		return (String) possible_values.get(choice);
	}

	public boolean isPossibleValue(Object value) {
		Enumeration e = possible_values.keys();
		boolean flag = false;
		while (e.hasMoreElements()) {
			Object o = e.nextElement();
			if (value.equals(o)) {
				flag = true;
				break;
			}
			// System.err.println((String)value + " <> " + (String)o);
		}
		return flag;
	}

	public String getType() {
		return "choice";
	}
}
