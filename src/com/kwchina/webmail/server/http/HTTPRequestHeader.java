package com.kwchina.webmail.server.http;

import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Hashtable;

import com.kwchina.webmail.misc.ByteStore;

/**
 * HTTPHeader.java
  */

public class HTTPRequestHeader  {

    private Hashtable content;
    private Hashtable headers;

    //用于收集页面集合类参数�������
    private Hashtable arrays;

    public HTTPRequestHeader() {
    	headers=new Hashtable(10,.9f);
    	content=new Hashtable(5,.9f);
    	arrays = new Hashtable();
    }

    public String getMethod() {
    	return getHeader("Method");
    }

    public String getPath() {
    	return getHeader("Path");
    }

    public String getVersion() {
    	return getHeader("Version");
    }

    public void setPath(String s) {
    	setHeader("PATH",URLDecoder.decode(s));
    }

    public void setMethod(String s) {
    	setHeader("METHOD",s);
    }

    public void setVersion(String s) {
    	setHeader("VERSION",s);
    }


    public void setHeader(String key, String value) {
		if(headers == null) {
		    headers=new Hashtable();
		}
		headers.put(key.toUpperCase(), value);
    }

    public String getHeader(String t) {
    	return (String)headers.get(t.toUpperCase());
    }

    public Hashtable getContent() {
    	return content;
    }
    
    public Hashtable getArrays(){
    	return arrays;
    }

    public Object getObjContent(String key) {
		if(content!=null) {
		    return content.get(key.toUpperCase());
		} else {
		    return null;
		}
    }
    
    
    public String getContent(String key) {
		if(content!=null) {
		    Object o=content.get(key.toUpperCase());
		    if(o == null) {
		    	return null;
		    } else if(o instanceof String) {
		    	
		    	String keyValue = (String)o;
		    	/**
		    	try{
		    		keyValue = new String(keyValue.getBytes("iso8859-1"),"gbk");
		    	}catch(Exception ex){		    		
		    	}    */
		    	return keyValue;
		    	
		    	
		    	//return (String)o;
		    } else if(o instanceof ByteStore) {
		    	return new String(((ByteStore)o).getBytes());
		    } else {
		    	return "";
		    }
		} else {
		    return "";
		}
    }
    
    public String getArrayContent(String key) {
		if(arrays!=null) {
		    Object o=arrays.get(key.toUpperCase());
		    if(o == null) {
		    	return null;
		    } else if(o instanceof String) {
		    	return (String)o;
		    } else if(o instanceof ByteStore) {
		    	return new String(((ByteStore)o).getBytes());
		    } else {
		    	return "";
		    }
		} else {
		    return "";
		}
    }

    public boolean isContentSet(String key) {
		Object s=content.get(key.toUpperCase());
		return s != null && !(s instanceof String && ((String)s).trim().equals(""));
    }

    public void setContent(String key, Object value) {
    	//����Ѿ����ã�����ȥ��
    	if(isContentSet(key))
    		content.remove(key);
    	content.put(key.toUpperCase(),value);
    }
    
    public void setArrayContent(String key, Object value) {
    	arrays.put(key.toUpperCase(),value);
    }

    public Enumeration getHeaderKeys() {
    	return headers.keys();
    }

    public Enumeration getContentKeys() {
    	return content.keys();
    }
    
    public Enumeration getArrayKeys() {
    	return arrays.keys();
    }

    public String toString() {
		String s="Method: "+headers.get("METHOD")+", Path="+headers.get("PATH")+", HTTP-version: "+headers.get("VERSION")+"\n";
		s+="HTTP Headers:\n";
		Enumeration e=headers.keys();
		while(e.hasMoreElements()) {
		    String h=(String)e.nextElement();
		    s+="Header name: "+h+", value: "+headers.get(h)+"\n";
		}
		if(content != null) {
		    s+="Form Content:\n";
		    e=content.keys();
		    while(e.hasMoreElements()) {
			String h=(String)e.nextElement();
			s+="Header name: "+h+", value: "+content.get(h)+"\n";
		    }
		}
		return s;
    }
    
    
} 
