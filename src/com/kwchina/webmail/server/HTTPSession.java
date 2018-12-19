package com.kwchina.webmail.server;

import java.net.InetAddress;
import java.util.Locale;

import org.dom4j.Document;

import com.kwchina.webmail.exception.InvalidPasswordException;
import com.kwchina.webmail.server.http.HTTPRequestHeader;

/*
 * HTTPSession.java
 * 
 */

public interface HTTPSession extends TimeableConnection {
    
    public void login(HTTPRequestHeader h) throws InvalidPasswordException;

    public void login();

    public void logout();

    public String getSessionCode();

    public Locale getLocale();

    public long getLastAccess();

    public void setLastAccess();

    public String getEnv(String key);

    public void setEnv(String key, String value);

    public void setEnv();

    public InetAddress getRemoteAddress();

    public void saveData();

    public Document getModel();

    public boolean isLoggedOut();

    public void setException(Exception ex);
   
} 
