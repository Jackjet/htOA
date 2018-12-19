package com.kwchina.webmail.server;

import java.util.*;

/*
 * WebMailVirtualDomain.java 
 */
/**
 * Represents a virtual domain in WebMail.
 * 表示Webmail中一个虚拟的Domain.
 * 
 *  
 * A virtual domain in WebMail allows the following things
 * - users can belong to a certain domain
 * - each domain has it's own default host, authentication host, and default email suffix
 * - each domain can have specific security features, i.e. IMAP/POP hosts users of that domain
 *   are allowed to connect to.
  */

public interface WebMailVirtualDomain  {

    /**
     * Return the name of this domain. This will be appended to a new users email address
     * and will be used in the login screen
     */
    public String getDomainName();

    public void setDomainName(String name) throws Exception;

    /**
     * This returns the name of the default server that will be used.
     * The default server is where a user gets his first folder (the one named "Default").
     */
    public String getDefaultServer();

    public void setDefaultServer(String name);

    /**
     * If the authentication type for this domain is IMAP or POP, this host will be used
     * to authenticate users.
     */
    public String getAuthenticationHost();

    public void setAuthenticationHost(String name);

    /**
     * Check if a hostname a user tried to connect to is within the allowed range of
     * hosts. Depending on implementation, this could simply check the name or do an
     * DNS lookup to check for IP ranges.
     * The default behaviour should be to only allow connections to the default host and
     * reject all others. This behaviour should be configurable by the administrator, however.
     */
    public boolean isAllowedHost(String host);

    /**
     * Set the hosts a user may connect to if host restriction is enabled.
     * Excpects a comma-separated list of hostnames.
     * The default host will be added to this list in any case
     */
    public void setAllowedHosts(String hosts);

    public Enumeration getAllowedHosts();

    /**
     * Enable/Disable restriction on the hosts that a user may connect to.
     * If "disabled", a user may connect to any host on the internet
     * If "enabled", a user may only connect to hosts in the configured list
     * @see isAllowedHost
     */
    public void setHostsRestricted(boolean b);

    public boolean getHostsRestricted();

}
