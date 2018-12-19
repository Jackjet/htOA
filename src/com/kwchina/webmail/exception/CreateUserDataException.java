
package com.kwchina.webmail.exception;

/*
 * WebMailException.java
 * 
 */
/**
 * An error occured while creating the userdata for a user. 
 */

public class CreateUserDataException extends UserDataException {
    
    public CreateUserDataException() {
    	super();
    }

    public CreateUserDataException(String s, String user, String domain) {
    	super(s,user,domain);
    }
    
} 
