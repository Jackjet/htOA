package com.kwchina.webmail.web.service;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.kwchina.webmail.exception.NoSuchFolderException;
import com.kwchina.webmail.server.WebMailSession;
import com.kwchina.webmail.server.http.HTTPRequestHeader;
import com.kwchina.webmail.web.bean.MailMessageInfor;
import com.kwchina.webmail.xml.XMLCommon;

public class MailListService extends MailBasicService {
	
	//获取邮件夹的邮件信息
	public static void getFolderMailInfor(HttpServletRequest request,String folderId,HTTPRequestHeader http_header) {
	
		WebMailSession mailSession = (WebMailSession)request.getSession().getAttribute("webmail.session");		
		
		//设置邮件标志，并更新FOLDER及MESSAGELIST的信息		
		/**
		try{
			mailSession.setFlags(folderId, http_header);
		}catch(MessagingException ex){			
		}
		*/
		//更新该Folder的信息
		if(folderId==null || folderId.equals("")){
			folderId = getInboxFloderId(request);			
		}
		mailSession.refreshFolderInformation(folderId);
		
		//更新所有Folder的总信息
		//mailSession.refreshFolderInformation();		
		
		//取得当前查看页面的邮件信息
		int nr = 1;
		try {
			nr = Integer.parseInt(http_header.getContent("PART"));
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		try {	
			mailSession.createMessageList(folderId, nr);
			
			getMailInfor(request,folderId);
			
		}catch (NoSuchFolderException e) {
			//throw new DocumentNotFoundException("Could not find folder " + folderId + "!");
			e.printStackTrace();
		}	
		
		/**把XML输出查看*/		
		try{
			Writer out = new OutputStreamWriter(new FileOutputStream("c:\\test_1.xml"),"GBK");
			OutputFormat format = OutputFormat.createPrettyPrint();
			//指定XML编码 
	        format.setEncoding("GBK");           

			XMLWriter writer = new XMLWriter(out, format);
			writer.write(mailSession.getModel());
			out.flush();
			out.close();
		}catch(Exception ex){
			
		}	
		
	}
	
	
	//获取某个邮件夹某页的邮件信息
	public static void getMailInfor(HttpServletRequest request,String folderId){
		ArrayList messageBeans = new ArrayList();
		
		try{
			Element mailHost = getMailHost(request,"Default");			
			Element folder = XMLCommon.getElementByAttribute(mailHost,"FOLDER","id",folderId);
			
			if(folder!=null){
				String xpath = "./MESSAGELIST/MESSAGE";
				List list = folder.selectNodes(xpath);
				Iterator it = list.iterator();   
	            while (it.hasNext()) {   
	                Element elt = (Element) it.next();
	        		
	                MailMessageInfor message = getMailMessage(elt,true);
	                
	                messageBeans.add(message);            
	            } 
				
				//MailMessageInfor message = getMailMessage(folder);
                
                //messageBeans.add(message);    
			}
			
			/**把XML输出查看*/	
            /**
    		try{
    			Writer out = new OutputStreamWriter(new FileOutputStream("c:\\test_1.xml"),"GBK");
    			OutputFormat format = OutputFormat.createPrettyPrint();
    			//指定XML编码 
    	        format.setEncoding("GBK");           

    			XMLWriter writer = new XMLWriter(out, format);
    			writer.write(elt);
    			out.flush();
    			out.close();
    		}catch(Exception ex){
    			
    		}*/	
			
			//String xpath = "./FOLDER/FOLDER[@id='" + folderId + "']/MESSAGELIST/MESSAGE";   
			/**
			String xpath = "./FOLDER/FOLDER[@id='" + folderId + "']/MESSAGELIST/MESSAGE";
			List list = mailHost.selectNodes(xpath);
			Iterator it = list.iterator();   
            while (it.hasNext()) {   
                Element elt = (Element) it.next();
        		
                MailMessageInfor message = getMailMessage(elt);
                
                messageBeans.add(message);            
            }   */
		
            request.setAttribute("_Mail_Messages", messageBeans);	
		}catch(Exception ex){		
			ex.printStackTrace();
		}
		
	}
	
	
	
			
	
	
	//
}
