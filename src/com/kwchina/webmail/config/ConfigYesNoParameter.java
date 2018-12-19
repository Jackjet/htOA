package com.kwchina.webmail.config;

/**
 * ConfigYesNoParameter.java 
 */

public class ConfigYesNoParameter extends ChoiceConfigParameter {
    
    public ConfigYesNoParameter(String name, String desc) {
		super(name,desc);
		addChoice("YES","Enabled.");
		addChoice("NO","Disabled.");	
    }
    
    public String getType() {
    	return "bool";
    }
} 
