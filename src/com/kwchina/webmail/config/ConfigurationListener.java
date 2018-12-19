package com.kwchina.webmail.config;

/*
 * ConfigurationListener.java 
 */
/**
 * Objects that register configuration parameters should implement this.
 * They will then be notified whenever their configuration changes.
 */
public interface ConfigurationListener {

    public void notifyConfigurationChange(String key);

}
