package com.kwchina.oa.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kwchina.core.base.entity.PersonInfor;
import com.kwchina.core.base.entity.SystemUserInfor;

public class NotesMailLogin extends HttpServlet {
	 	public void init() throws ServletException {
	    }
	    
	    //Process the HTTP Get request
	    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        doPost(request,response);
	    }
	    
	    //Process the HTTP Post request
	    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    	//邮件服务器IP及访问端口
	    	ServletContext context = request.getSession().getServletContext();
			String ip = context.getInitParameter("mailserver");
	    	
	    	StringBuffer requestURL = request.getRequestURL();
	    	if (requestURL != null && requestURL.length() > 0) {
	    		String mailserverIP = context.getInitParameter("mailserverIP");
	    		if (requestURL.indexOf(mailserverIP) > -1) {
	    			ip = mailserverIP;
	    		}
	    	}

			String port = context.getInitParameter("mailport");
			//String ip= this.getInitParameter("mailserver");
	    	//String port = this.getInitParameter("mailport");
	    	
	    	//个人邮件信息（包括用户名密码）	    		    	
	    	//String wlsRedirect="/test.jsp";
	    	
	    	String username= "";
	    	String password = "";
	    	String email = "";
	    	String loginname = "";
	    	
	    	try{
		    	HttpSession session = request.getSession();
		    	if(session.getAttribute("_SYSTEM_USER")==null){
		    		
		    	}else{
		    		SystemUserInfor systemUser = (SystemUserInfor)session.getAttribute("_SYSTEM_USER");
		    		PersonInfor person = systemUser.getPerson();
		    		
		    		loginname = systemUser.getUserName();
		    		
		    		email = person.getEmail();
		    		if(email!=null && !email.equals("")){
		    			//int pos = email.indexOf("@");
		    			//if(pos>0)
		    			//	username = email.substring(0, pos);
		    			username = email;
		    		}
		    		
		    		password = person.getEmailPassword()==null?"":person.getEmailPassword(); 
		    	}
	    	}catch(Exception ex){	    		
	    	}
	    	
	    		    	
	    	/**
	    	Properties config = new Properties();
	        try {
	            config.load(new FileInputStream("dominoplugin.properties"));
	        } catch (Exception ex) {
	            log("Warning : WebLogic-Domino plugin cannot find file dominoplugin.properties!");
	            ex.printStackTrace();
	            config = null;
	        }
	        ip = config.getProperty("domino_ip");
	        port = config.getProperty("domino_port");
	        redirect =config.getProperty("domino_redirect");
	        wlsRedirect= config.getProperty("weblogic_redirect");
	        
	        final String forwardPath = this.getInitParameter("forwardPath")+"?entryType="+entryType;
             response.sendRedirect(forwardPath);
	        */
	    		    	
	    	
	    	//String notesUrl ="http://" + ip + ":" + port + "/names.nsf?Login";
	    	//if(username!=null && !username.equals("") && password!=null && !password.equals("")){
	    	//	notesUrl = notesUrl + "&Username=" + username;
	    	//	notesUrl = notesUrl +  "&Password=" + password;
	    	//}
	    	//notesUrl = notesUrl + "&RedirectTo=/"+redirect;	
		
	    	//String s= "<" +
	    	//		"<frameset rows='100%,0%'><frame src=" + notesUrl + ">";
	    	//s= s + "<frame src=" + wlsRedirect + "></frameset>";
	    	
	    	//String s="<script language=\"javaScript\">";
	    	//s+= "window.open('" + notesUrl + "');";
	    	//s+= "</script>";
	    	
	    	/**
	    	String s = "<script>";
	    	s += "function SetCwinHeight(obj){";
	    		s += "var cwin=obj;alert(cwin.contentDocument);cwin.height = 10000;alert(123);";
	    			//s += "if (document.getElementById) {";
	    				//s += "if (cwin && !window.opera) {";
	    					//s += "if (cwin.contentDocument && cwin.contentDocument.body.offsetHeight)";
	    						//s += "cwin.height = cwin.contentDocument.body.offsetHeight; ";
	    							//s += "else if(cwin.Document && cwin.Document.body.scrollHeight)";
	    								//s += " cwin.height = cwin.Document.body.scrollHeight;";
	    									//s += "}";
	    										//s += "}";
	    											s += "}";
	    											s += "</script>";
	    	
	    	s += "<html><head><title>个人邮件</title></head>";
	    	s += "<body>";
	    	s += "<iframe src=\"" + notesUrl  + "\" width=\"100%\" height=\"900\" frameborder=\"0\" scrolling=\"no\" id=cwin name=\"cwin\"></iframe>";
	    	s += "<body></html>";
	    	*/
	        //log("\nframe=" + s);	
	    	
	    	/**
	    	String s="<script language=\"javaScript\">";
	    	s+= "window.location.href='" + notesUrl + "';";
	    	s+= "</script>";
	    	*/
	    	String redirect="mail/" + username + ".nsf";
	    	
	    	String notesUrl ="http://" + ip + ":" + port + "/names.nsf?Login";
//	    	String notesUrl ="http://" + ip + ":" + port + "/mail/"+loginname+".nsf";
	    	String s = "<html><head><title>个人邮件</title></head>";
	    	s += "<body>";
	    	s += "<form name=\"frm\" action=\"" + notesUrl +"\" method=\"post\">";
	    	s += "<input type=\"hidden\" name=\"Username\" value=\"" + username + "\">";
	    	s += "<input type=\"hidden\" name=\"Password\" value=\"" + password + "\">";
	    	//s += "<input name=\"RedirectTo\" value=\"" + redirect + ">";
	    	s += "</form>";
	    	s += "<script language=\"javaScript\">";
		    s+= "frm.submit();";
		    s+= "</script>";
	    	s += "<body></html>";
	        
	        PrintWriter myout = response.getWriter();
	        myout.println(s);
	        
	        //log("login wls and domino finish\n\n");
	    }

	    
	    //Clean up resources
	    public void destroy() {
	    }

}
