package com.kwchina.webmail.server;

import java.util.*;
/**
 * MailHostData.java
 * 
 */

public interface MailHostData {	
	
    /**
     * The password for this mailbox
     * @return Value of password.
     */
    public String getPassword();
    
    /**
     * Set the value of password.
     * @param v  Value to assign to password.
     */
    public void setPassword(String  v);
    
    /**
     * The name of this mailbox
     * @return Value of name.
     */
    public String getName();
    
    /**
     * Set the value of name.
     * @param v  Value to assign to name.
     */
    public void setName(String  v);
    
    /**
     * The login for this mailbox
     */
    public String getLogin();

    public void setLogin(String s);

    /**
     * The Hostname for this mailbox
     * @return Value of host.
     */
    public String getHostURL();

    /**
     * Set the value of host.
     * @param v  Value to assign to host.
     */
    public void setHostURL(String  v); 

    /**
     * The unique ID of this mailbox
     */
    public String getID();
}
