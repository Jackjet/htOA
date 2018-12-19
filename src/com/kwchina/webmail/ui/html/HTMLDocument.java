package com.kwchina.webmail.ui.html;

import java.util.*;

/*
 * HTMLDocument.java
 * 
 */

/**
 * WebMail's class for representing HTML Documents.
 * ÃèÊöÒ»¸öHTMLÎÄµµ
 */

public class HTMLDocument {
	
    protected String content;
    protected HTMLHeader header;
	
    protected Hashtable httpheaders;
	
    protected int returncode=200;
    protected String returnstatus="OK";
	
    public HTMLDocument() {
    }
	
    public HTMLDocument(String title, String content) {
		header=new HTMLHeader(title);
		this.content=content;
    }
	
    public HTMLDocument(String title, String cont, String basepath) {
		this(title,cont);
		
// 		try {
// 			RE regexp2=new RE("<<BASE>>",RE.REG_ICASE & RE.REG_MULTILINE);
// 			content=regexp2.substituteAll(content,basepath);
// 		} catch(Exception e) {
// 			e.printStackTrace();
// 		}
    }
    
//     public HTMLDocument(String title,Storage loader, String docname) {
// 		this(title,loader.getTextFile(loader.getStringResource(docname,Locale.getDefault()),Locale.getDefault()));
//     }
	
//     public HTMLDocument(String title,Storage loader, String docname, String basepath) {
// 		this(title,loader.getTextFile(loader.getStringResource(docname,Locale.getDefault()),Locale.getDefault()),basepath);
//     }
	
    public void addHTTPHeader(String key, String value) {
		if(httpheaders==null) {
			httpheaders=new Hashtable(5);
		}
		httpheaders.put(key,value);
    }
	
    public Enumeration getHTTPHeaderKeys() {
		return httpheaders.keys();
    }
	
    public String getHTTPHeader(String key) {
		return (String)httpheaders.get(key);
    }
	
    public boolean hasHTTPHeader() {
		return (httpheaders!=null) && !httpheaders.isEmpty();
    }
	
    public int getReturnCode() {
		return returncode;
    }
	
    public String getReturnStatus() {
		return returnstatus;
    }
	
    public void setStatus(int code, String status) {
		returncode=code;
		returnstatus=status;
    }
	
    public String toString() {
    	if (header!=null){
    		return header.toString()+"\r\n"+content;
    	}else{
    		return content;
    	}
		
    }
	
    public int length() {
		return header.toString().length()+1+content.length();
    }
} 
