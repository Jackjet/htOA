package com.kwchina.webmail.web.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Element;

import com.kwchina.core.util.string.StringUtil;
import com.kwchina.webmail.server.WebMailSession;
import com.kwchina.webmail.web.bean.CurrentInfor;
import com.kwchina.webmail.web.bean.MailFolder;
import com.kwchina.webmail.web.bean.MailMessageInfor;
import com.kwchina.webmail.web.bean.MailQuotaInfor;
import com.kwchina.webmail.xml.XMLCommon;
import com.kwchina.webmail.xml.XMLUserModel;

public class MailBasicService {
	
	//获得收件夹的Id
	public static String getInboxFloderId(HttpServletRequest request){
		/**		
		WebMailSession mailSession = (WebMailSession)request.getSession().getAttribute("webmail.session");	
		XMLUserModel userModel = mailSession.getUserModel();
		Element mailHost = userModel.getMailhost("Default");
		
		Element inboxFolder = XMLCommon.getElementByAttribute(mailHost,"Folder","name", "INBOX");
		*/
		ArrayList folders = getFolderInfor(request);
		for(Iterator it=folders.iterator();it.hasNext();){
			MailFolder folderBean = (MailFolder)it.next();
			if(folderBean.getFolderName().toUpperCase().equals("INBOX")){
				return folderBean.getFolderId();
			}
		}
		
		return "";		
	}
	
	//获得垃圾箱的Id
	public static String getTrashFloderId(HttpServletRequest request){		
		ArrayList folders = getFolderInfor(request);
		for(Iterator it=folders.iterator();it.hasNext();){
			MailFolder folderBean = (MailFolder)it.next();
			if(folderBean.getFolderName().toUpperCase().equals("TRASH")){
				return folderBean.getFolderId();
			}
		}
		
		return "";		
	}
	
	
	//获取该用户的限额信息
	public static MailQuotaInfor getMailQuota(HttpServletRequest request){
		try{
			Element mailHost = getMailHost(request,"Default");
			
			List quotas = XMLCommon.getElementsByName(mailHost, "quota");
			if(!quotas.isEmpty()){
				Element quota = (Element)quotas.get(0);
				
				List resources = XMLCommon.getElementsByName(quota, "resource");
				if(!resources.isEmpty()){
					Element resource = (Element)resources.get(0);

					MailQuotaInfor mailQuota = new MailQuotaInfor();
					mailQuota.setName(resource.attributeValue("name"));
					mailQuota.setLimit(XMLCommon.getAttributeIntValue(resource,"limit"));
					mailQuota.setLimitKB(XMLCommon.getAttributeIntValue(resource,"limitkb"));
					mailQuota.setUsage(XMLCommon.getAttributeIntValue(resource,"usage"));
					mailQuota.setUsageKB(XMLCommon.getAttributeIntValue(resource,"usagekb"));
					mailQuota.setUsagePct(resource.attributeValue("usagepct"));
					
					return mailQuota;
				}	
			}			
		}catch(Exception ex){
			ex.printStackTrace();			
		}
		
		return null;  
	}
	
	
	
	//当前Folder
	public static CurrentInfor getCurrentFolder(HttpServletRequest request){		
		WebMailSession mailSession = (WebMailSession)request.getSession().getAttribute("webmail.session");	
		XMLUserModel userModel = mailSession.getUserModel();

		Element currentFolder = userModel.getCurrentFolder("");
		
		
		CurrentInfor current = new CurrentInfor();
		current.setId(currentFolder.attributeValue("id"));
		current.setFirstMessage(XMLCommon.getAttributeIntValue(currentFolder,"first_msg"));
		current.setLastMessage(XMLCommon.getAttributeIntValue(currentFolder,"last_msg"));
		current.setListPart(XMLCommon.getAttributeIntValue(currentFolder,"list_part"));
		current.setAllMessage(XMLCommon.getAttributeIntValue(currentFolder,"all_msg"));
		current.setAllPart(XMLCommon.getAttributeIntValue(currentFolder,"all_part"));
		
		return current;
		
	}
	
	
	//当前Message	
	public static CurrentInfor getCurrentMessage(HttpServletRequest request){		
		WebMailSession mailSession = (WebMailSession)request.getSession().getAttribute("webmail.session");	
		XMLUserModel userModel = mailSession.getUserModel();

		Element currentMessage = userModel.getCurrentMessage("");
		
		
		CurrentInfor current = new CurrentInfor();
		current.setId(currentMessage.attributeValue("id"));	
		
		return current;
	}
	
	
	public static Element getMailHost(HttpServletRequest request,String hostName){		
		//Mail Session
		WebMailSession mailSession = (WebMailSession)request.getSession().getAttribute("webmail.session");
		
		//从XMLUserModel中获得名称为"Default"的MAILHOST_MODEL，从而得到Floder的信息
		XMLUserModel userModel = mailSession.getUserModel();
		
		Element mailHost = userModel.getMailhost("Default");
		
		/**把XML输出查看*/
		/**
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
			
		}	*/
		
		return mailHost;
	}
	
	
	//获取单个邮件夹的信息
	public static MailFolder getFolderInfor(HttpServletRequest request,String folderId){
		try{
			Element mailHost = getMailHost(request,"Default");
			
			Element folder = XMLCommon.getElementByAttribute(mailHost,"FOLDER","id",folderId);
			if(folder!=null){
				 MailFolder folderBean = getSingleMailFolder(folder);
				 
				 return folderBean;     
			}
			
			//String xpath = "./FOLDER/FOLDER[@id='" + folderId + "']";   
			//List list = mailHost.selectNodes(xpath);
			//Iterator it = list.iterator();   
            //if (it.hasNext()) {   
                //Element folder = (Element) it.next();   
                
                //MailFolder folderBean = getSingleMailFolder(folder);
                
                //return folderBean;                
            //}
		}catch(Exception ex){
			ex.printStackTrace();			
		}
		
		return null;            
	}
	
	
	public static MailFolder getSingleMailFolder(Element folder){
		MailFolder folderBean = new MailFolder();
		
		folderBean.setFolderId(folder.attributeValue("id"));
		folderBean.setFloderName(folder.attributeValue("name"));
		
		//String holds_folders = folder.attributeValue("holds_folders");
		//boolean hasFolders = Boolean.valueOf(holds_folders);
		folderBean.setHasFolders(XMLCommon.getAttributeBooleanValue(folder,"holds_folders"));
		
		//String holds_messages = folder.attributeValue("holds_messages");
		//boolean holdMessages = Boolean.valueOf(holds_messages);
		folderBean.setHoldMessage(XMLCommon.getAttributeBooleanValue(folder,"holdMessages"));	
		
		
		Element messageList = XMLCommon.getDirectElementByTag(folder,"MESSAGELIST");
		if(messageList !=null ){
			folderBean.setTotalMessage(XMLCommon.getAttributeIntValue(messageList,"total"));	
			folderBean.setNewMessage(XMLCommon.getAttributeIntValue(messageList,"new"));
		}	
		
		
		return folderBean;
	}
	
	
	//获取MessageInfor
	public static MailMessageInfor getMailMessage(Element eleMessage,boolean transCode){
		MailMessageInfor message = new MailMessageInfor();
        message.setMessageId(eleMessage.attributeValue("msgid"));
        message.setSerialNo(XMLCommon.getAttributeIntValue(eleMessage, "msgnr"));
        message.setRecent(XMLCommon.getAttributeBooleanValue(eleMessage,"recent"));
        message.setHasAttachment(XMLCommon.getAttributeBooleanValue(eleMessage,"attachment"));
        message.setSize(eleMessage.attributeValue("size"));
        
        //获取Header部分信息
        Element header = XMLCommon.getElementByTag(eleMessage, "HEADER");
        if(header!=null){
	        String from = XMLCommon.getTagValue(header,"FROM");
	        
	        from  = dealFrom(from);
	        //from = StringUtil.replace(from, "<", "&lt;");	  
	        //from = StringUtil.replace(from, ">", "&gt;");	
	        if(transCode)
	        	message.setFrom(transTagToCode(from));
	        else
	        	message.setFrom(from);
	        
	        message.setSubject(XMLCommon.getTagValue(header,"SUBJECT"));
	        
	        String to = XMLCommon.getTagValue(header,"TO");
	        if(transCode)
	        	 message.setTo(transTagToCode(to));
	        else
	        	 message.setTo(to);       
	        
	        message.setCc(XMLCommon.getTagValue(header,"CC"));
	        message.setBcc(XMLCommon.getTagValue(header,"BCC"));
	        message.setReply(XMLCommon.getTagValue(header,"REPLY-TO"));
	        message.setGetDate(XMLCommon.getTagValue(header,"DATE"));
        }
        
        message.setSeen(XMLCommon.getAttributeBooleanValue(eleMessage,"seen"));
        
        return message;
	}
	
	
	//转换><"等符号
	public static String transTagToCode(String content){
		content = StringUtil.replace(content, "&", "&amp;");
		
		content = StringUtil.replace(content, "<", "&lt;");	  
		content = StringUtil.replace(content, ">", "&gt;");	
		content = StringUtil.replace(content, "\"", "&quot;");		
		
		return content;
	}
	
	
	//对发件人做处理,显示别名或者地址
	public static String dealFrom(String from){
		String dealedFrom = "";
		
		//"xugl@sipgl-transport.com" <xugl@sipgl-transport.com>
		
		if(from!=null && !from.equals("")) {
			int pos1 = from.indexOf("<");
			int pos2 = from .indexOf(">");
			
			if(pos1>=0){
				dealedFrom = from.substring(0, pos1);
				if(!dealedFrom.equals("")){
					dealedFrom.trim();
				}
				
				if(dealedFrom.length()==0){
					dealedFrom = from.substring(pos1+1, pos2);
				}				
			}else{
				dealedFrom = from;
				dealedFrom = dealedFrom.trim();
			}
		}
		
		
		//如果有双引号，去掉
		String returnFrom = dealedFrom.replaceAll("\"", "");
		returnFrom = returnFrom.replaceAll(" ", "");
		
		return returnFrom;
		 
	}
	
	
	public static String transCodeToTag(String content){
		content = StringUtil.replace(content, "&lt;", "<");	  
		content = StringUtil.replace(content, "&gt;", ">");	
		content = StringUtil.replace(content, "&quot;", "\"");
		
		content = StringUtil.replace(content, "&amp;", "&");		
		
		return content;
	}
	
	
	//获取邮件夹的信息
	public static ArrayList getFolderInfor(HttpServletRequest request) {
		ArrayList folderBeans = new ArrayList();
		
	
		Element mailHost = getMailHost(request,"Default");
		
		ArrayList folders = XMLCommon.getElementsByName(mailHost,"FOLDER");
		
		for(Iterator it = folders.iterator();it.hasNext();){
			Element folder = (Element)it.next();
			
			String folderName = folder.attributeValue("name");
			if(folderName!=null && !folderName.equals("")){			
				MailFolder folderBean = getSingleMailFolder(folder);			
				folderBeans.add(folderBean);
			}
		}
		
		return folderBeans;
		
		//request.setAttribute("_Folders", folderBeans);		
	}
	
}

