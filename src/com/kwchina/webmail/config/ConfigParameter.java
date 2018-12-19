package com.kwchina.webmail.config;

import java.util.*;

/*
 * ConfigParameter.java 
 */
/**
 * An abstraction for a configuration parameter. Subclasses must implement a
 * method that checks whether a specific value is correct for this parameter.
 * 
 * ConfigParameters may have ConfigurationListeners that work much like the
 * Listeners in the Java AWT. All listeners get informed if the value of the
 * parameter has changed.
 * 
 * Each ConfigParameter has a corresponding (unique) key, a default value (if
 * not yet changed by the user) and a short description for the administrator
 * about what the parameter means.
 * 
 * This is a scheme only, however, ConfigParameters just describe the behaviour
 * of certain keys in the WebMail configuration, they don't actually store the
 * value itself.
 * (仅为一种策略，描述了相关配置的内容，但是并不存储真正的值)
 */


public abstract class ConfigParameter {
	protected String key;

	protected Object def_value;

	protected String desc;

	protected Vector listeners;

	protected String group;

	/**
	 * Create a new parameter.
	 * 
	 * @param name
	 *            Unique key of this parameter
	 * @param def
	 *            Default value for this parameter
	 * @param desc
	 *            Description for this parameter
	 */
	public ConfigParameter(String name, Object def, String desc) {
		key = name;
		this.def_value = def;
		this.desc = desc;
		group = "default";
		listeners = new Vector();
	}

	public void setGroup(String g) {
		group = g;
	}
	
	public String getGroup() {
		return group;
	}

	/**
	 * Return the key of this parameter.
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Return the default value of this parameter.
	 */
	public Object getDefault() {
		return def_value;
	}

	public void setDefault(Object value) {
		def_value = value;
	}

	/**
	 * Return the description for this parameter.
	 */
	public String getDescription() {
		return desc;
	}

	/**
	 * Add a ConfigurationListener for this object that will be informed if the
	 * parameter's value changes.
	 */
	public void addConfigurationListener(ConfigurationListener l) {
		listeners.addElement(l);
	}

	/**
	 * Get a list of all configuration listeners.
	 */
	public Enumeration getConfigurationListeners() {
		return listeners.elements();
	}

	/**
	 * Put through some sort of filter. This method is called when a String
	 * value for this parameter is set. Subclasses should implement this, if
	 * they want to change the behaviour
	 * 
	 * @see CryptedStringConfigParameter
	 */
	public String filter(String s) {
		return s;
	}

	/**
	 * Check whether the value that is passed as the parameter is a valid value
	 * for this ConfigParameter
	 * 
	 * @see ChoiceConfigParameter
	 * @see IntegerConfigParameter 
	 */
	public abstract boolean isPossibleValue(Object value);
	
	
	//------类型-----------
	public String getType() {
		return "undefined";
	}

	
}
