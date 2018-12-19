package com.kwchina.webmail.server;

/*
 * Plugin.java
 *
 */

public interface Plugin {

    /**
     * Register this plugin with a WebMailServer
     * The plugin thus has access to most WebMail objects.
     */
    public void register(WebMailServer parent);

    /**
     * Return the name for this plugin.
     */
    public String getName();

    /**
     * Return a short description for this plugin to be shown in the
     * plugin list and perhaps in configuration
     */
    public String getDescription();

    /**
     * Get a version information for this plugin.
     * This is used for informational purposes only.
     */
    public String getVersion();

    /**
     * Return a stringlist (comma seperated) of features this plugin provides.
     * @see requires
     */
    public String provides();

    /**
     * Return a stringlist (comma seperated) of features this plugin requires.
     * @see provides
     */
    public String requires(); 

}
