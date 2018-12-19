
package com.kwchina.webmail.exception;

/**
 * ErrorHandler.java
 */


public class ErrorHandler  {
    
    public ErrorHandler(Exception ex) {
    	//System.err.println(ex.getMessage());;
    	ex.printStackTrace();
    }
    
} 
