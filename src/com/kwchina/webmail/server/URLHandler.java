package com.kwchina.webmail.server;

import javax.servlet.ServletException;

import com.kwchina.webmail.exception.WebMailException;
import com.kwchina.webmail.server.http.HTTPRequestHeader;
import com.kwchina.webmail.ui.html.HTMLDocument;


/**
 * URLHandler.java
 * 
 */
/**
 * ���ڴ���URL��Class����ʵ�ֱ��ӿ�
 *  
*/
public interface URLHandler extends URLHandlerTreeNode {

    public String getName();
    
    public String getDescription();

    public HTMLDocument handleURL(String suburl, HTTPSession session, HTTPRequestHeader header) throws WebMailException, ServletException;

}
