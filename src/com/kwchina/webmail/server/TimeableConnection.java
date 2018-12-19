package com.kwchina.webmail.server;

/*
 * TimeableConnection.java
 * 
 */

public interface TimeableConnection  {

    public long getLastAccess();

    public void timeoutOccured();
 
    public long getTimeout();

}
