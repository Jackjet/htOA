package com.kwchina.webmail.config;

import java.util.Enumeration;
import java.util.Hashtable;

/*
 * ConfigScheme.java 
 */

public class ConfigScheme {

	protected Hashtable config_scheme;

	public ConfigScheme() {
		System.err.print("- Configuration Scheme Handler ... ");
		config_scheme = new Hashtable();
		System.err.println("done!");
	}
	
	
	
	/**
	 * Check whether a key/value pair is valid in this configuration scheme
	 * 
	 * @param key
	 *            Name of the parameter
	 * @param value
	 *            value to check for
	 */
	public boolean isValid(String key, Object value) {
		ConfigParameter scheme = getConfigParameter(key);
		if (scheme == null) {
			return false;
		} else {
			return scheme.isPossibleValue(value);
		}
	}
	
	
	public String filter(String key, String value) {
		ConfigParameter c = (ConfigParameter) config_scheme.get(key);
		if (c != null) {
			return c.filter(value);
		} else {
			return value;
		}
	}

	/**
	 * Register a configuration key that can take Integer values
	 * 
	 * @param key
	 *            Name of the configuration key
	 * @param default
	 *            Default value for this key
	 * @param desc
	 *            Description for this key
	 */
	public void configRegisterIntegerKey(String key, String def, String desc) {
		IntegerConfigParameter parm = new IntegerConfigParameter(key, def, desc);
		registerConfig(parm);
	}

	public void configRegisterIntegerKey(ConfigurationListener l, String key,
			String def, String desc) {
		IntegerConfigParameter parm = new IntegerConfigParameter(key, def, desc);
		registerConfig(parm);
		parm.addConfigurationListener(l);
	}

	/**
	 * Register a configuration key that can take String values and crypts them
	 * 
	 * @param key
	 *            Name of the configuration key
	 * @param default
	 *            Default value for this key
	 * @param desc
	 *            Description for this key
	 */
	/**
	public void configRegisterCryptedStringKey(String key, String def,
			String desc) {
		CryptedStringConfigParameter parm = new CryptedStringConfigParameter(key, def, desc);
		registerConfig(parm);
	}

	public void configRegisterCryptedStringKey(ConfigurationListener l,
			String key, String def, String desc) {
		CryptedStringConfigParameter parm = new CryptedStringConfigParameter(key, def, desc);
		registerConfig(parm);
		parm.addConfigurationListener(l);
	}
	*/
	
	
	/**
	 * Register a configuration key that can take String values
	 * 
	 * @param key
	 *            Name of the configuration key
	 * @param default
	 *            Default value for this key
	 * @param desc
	 *            Description for this key
	 */
	public void configRegisterStringKey(String key, String def, String desc) {
		StringConfigParameter parm = new StringConfigParameter(key, def, desc);
		registerConfig(parm);
	}

	public void configRegisterStringKey(ConfigurationListener l, String key,
			String def, String desc) {
		StringConfigParameter parm = new StringConfigParameter(key, def, desc);
		registerConfig(parm);
		parm.addConfigurationListener(l);
	}
	
	
	
	/**
	 * Register a configuration key that can take one of a choice of possible
	 * values
	 * 
	 * @param key
	 *            Name of the configuration key
	 * @param desc
	 *            Description for this key
	 * @see configAddChoice
	 */
	public void configRegisterChoiceKey(String key, String desc) {
		ChoiceConfigParameter parm = new ChoiceConfigParameter(key, desc);
		registerConfig(parm);
	}

	public void configRegisterChoiceKey(ConfigurationListener l, String key,
			String desc) {
		ChoiceConfigParameter parm = new ChoiceConfigParameter(key, desc);
		registerConfig(parm);
		parm.addConfigurationListener(l);
	}
	
	
	public void configRegisterYesNoKey(String key, String desc) {
		ChoiceConfigParameter parm = new ConfigYesNoParameter(key, desc);
		registerConfig(parm);
	}

	public void configRegisterYesNoKey(ConfigurationListener l, String key,
			String desc) {
		ChoiceConfigParameter parm = new ConfigYesNoParameter(key, desc);
		registerConfig(parm);
		parm.addConfigurationListener(l);
	}

	
	/**
	 * ���ѡ���ChoiceConfigParameter
	 * 
	 * @param key
	 *            Name of the configuration key where a choice is to be added
	 * @param choice
	 *            Name of the new choice
	 * @param desc
	 *            Description for this choice
	 */
	public void configAddChoice(String key, String choice, String desc) {
		if (config_scheme != null) {
			ConfigParameter parm = (ConfigParameter) config_scheme.get(key);
			if (parm instanceof ChoiceConfigParameter) {
				((ChoiceConfigParameter) parm).addChoice(choice, desc);
			}
		}
	}

	//���õ����Է����ı䣬����Ҫ֪ͨConfigurationListeners	
	public void notifyConfigurationChange(String key) {
		// System.err.println("NOTIFY: "+key);
		ConfigParameter parm = getConfigParameter(key);
		if (parm != null) {
			Enumeration enum1 = parm.getConfigurationListeners();

			while (enum1.hasMoreElements()) {
				ConfigurationListener l = (ConfigurationListener) enum1.nextElement();
				// System.err.println(l);
				try {
					l.notifyConfigurationChange(key);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	//��ȡ����keys
	public Enumeration getPossibleKeys() {
		return config_scheme.keys();
	}
	
	
	//�������key��Ĭ��ֵ
	public void setDefaultValue(String key, Object default_value) {
		ConfigParameter cp = (ConfigParameter) config_scheme.get(key);
		if (cp != null) {
			cp.setDefault(default_value);
		}
	}
	
	
	//ͨ��key��ȡ��description
	public String getDescription(String key) {
		ConfigParameter cp = (ConfigParameter) config_scheme.get(key);
		if (cp != null) {
			return cp.getDescription();
		} else {
			return null;
		}
	}

	
	//register ���������Ϣ
	public void registerConfig(ConfigParameter parm) {
		if (config_scheme == null) {
			config_scheme = new Hashtable();
		}
		
		Enumeration e = config_scheme.keys();
		boolean flag = false;
		while (e.hasMoreElements()) {
			if (e.nextElement().equals(parm.getKey())) {
				flag = true;
				break;
			}
		}
		if (!flag) {
			config_scheme.put(parm.getKey(), parm);
		}
	}
	
	/**
	 * ���ض���������Ϣ���Listener. �Ա���ز��������ı�
	 */
	public void addConfigurationListener(String key, ConfigurationListener l) {
		ConfigParameter parm = getConfigParameter(key);
		parm.addConfigurationListener(l);
	}
	
	//����key��ȡ���ConfigParameter
	public ConfigParameter getConfigParameter(String key) {
		return (ConfigParameter) config_scheme.get(key);
	}
	
	//��ȡ���ConfigParameter������
	public String getConfigParameterType(String key) {
		return getConfigParameter(key).getType();
	}

	//��ȡConfigParameter��Ӧ��Group
	public String getConfigParameterGroup(String key) {
		return getConfigParameter(key).getGroup();
	}

	//��ȡ��ӦConfigParamter��Ĭ��ֵ
	public Object getDefaultValue(String key) {
		ConfigParameter cp = (ConfigParameter) config_scheme.get(key);
		if (cp != null) {
			return cp.getDefault();
		} else {
			return null;
		}
	}
	
}
