package com.kwchina.webmail.config;

import java.util.*;

/*
 * ConfigStore.java 
 */

public abstract class ConfigStore {

	protected ConfigScheme scheme;

	public ConfigStore(ConfigScheme cs) {
		scheme = cs;
	}

	public ConfigStore() {
		this(new ConfigScheme());
	}

	public ConfigScheme getConfigScheme() {
		return scheme;
	}

	/**
	 * ��ConfigSchme�л�ȡ����key
	 */
	public Enumeration getConfigKeys() {
		return scheme.getPossibleKeys();
	}

	/**
	 * ��ConifgScheme�л�ȡָ��keyֵ����Ϣ
	 * 
	 * @param key
	 *            Identifier for the configuration
	 */	
	public String getConfig(String key) {
		String value = getConfigRaw(key.toUpperCase());
		if (value == null || value.equals("")) {
			value = (String) scheme.getDefaultValue(key.toUpperCase());
		}
		if (value == null) {
			value = "";
		}
		return value;
	}

	/**
	 * Access a configuration on a low level, e.g. access a file, make a SQL
	 * query, ... Will be called by getConfig. return null if undefined
	 */	
	protected abstract String getConfigRaw(String key);
	

	
	public boolean isConfigSet(String key) {
		return getConfigRaw(key) != null;
	}

	
	public void setConfig(String key, String value)
			throws IllegalArgumentException {
		setConfig(key, value, true, true);
	}

	/**
	 * Set a configuration "key" to the specified value.
	 * 
	 * @param key
	 *            Identifier for the configuration
	 * @paran value value to set
	 */	
	public void setConfig(String key, String value, boolean filter,
			boolean notify) throws IllegalArgumentException {
		if (!scheme.isValid(key, value))
			throw new IllegalArgumentException();
		
		//���û�У��������õ�ֵ�����˱仯
		//if (!(isConfigSet(key) && getConfigRaw(key).equals(value))) {
		if (!isConfigSet(key) || !getConfigRaw(key).equals(value)) {
			// System.err.println("Key: "+key);
			// System.err.println("Value old: |"+getConfigRaw(key)+"|");
			// System.err.println("Value new: |"+value+"|");

			setConfigRaw(scheme.getConfigParameterGroup(key), key,	filter ? scheme.filter(key, value) : value, scheme
							.getConfigParameterType(key));
			if (notify)
				scheme.notifyConfigurationChange(key);
		}
	}

	/**
	 * Access a configuration on a low level, e.g. access a file, make a SQL
	 * query, ... Will be called by setConfig. return null if undefined
	 */
	
	public abstract void setConfigRaw(String group, String key, String value,
			String type);
	
	

	public void addConfigurationListener(String key, ConfigurationListener l) {
		scheme.addConfigurationListener(key, l);
	}

}
