<%@ page contentType="text/html; charset=GBK"%>
<%@ include file="/inc/taglibs.jsp"%>
<%@ page language="java" 
		 import="java.util.*,
		 		 java.sql.Timestamp,
				 com.kwchina.sms.dao.*,
				 com.kwchina.sms.entity.*,
				 org.springframework.web.context.WebApplicationContext,
				 org.springframework.web.context.support.*" %>
<%@ page import="com.kwchina.core.base.entity.SystemUserInfor" %>
<%@ page import="com.kwchina.oa.util.SysCommonMethod" %>
<%@ page import="com.kwchina.sms.tencentcloud.SMSSend" %>


<html>
<head>
<title>Save Message</title>
</head>	

<body oncontextmenu="return false" onselectstart="return false">
<%request.setCharacterEncoding("GBK");%>

<% 	
 	ServletContext context = request.getSession().getServletContext();
 	WebApplicationContext webContext = WebApplicationContextUtils.getWebApplicationContext(context);
 
 	//SystemUserManager userManager = (SystemUserManager) webContext.getBean("systemUserManager");  
 	SMSSendMessageDAO sMSSendMessageDAO = (SMSSendMessageDAO) webContext.getBean("SMSSendMessageDAOImpl");

	
	String mobiles = request.getParameter("mobiles").trim();	
	String[] mobiless = mobiles.split(",");
	String content = request.getParameter("content").trim();
	SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
	String[] contentt = new String[]{content};

	//out.println("----");
	try{
		SMSMessagesToSend sendMessage = new SMSMessagesToSend();
		sendMessage.setMobileNos(mobiles);
		sendMessage.setMessageText(content);
		sendMessage.setTransmitStatus(0);
		sendMessage.setScheduleDate(new Timestamp(System.currentTimeMillis()));
		sendMessage.setStatus(0);
		sendMessage.setApplier(systemUser.getPerson().getPersonName());
		sMSSendMessageDAO.save(sendMessage);
//		SMSSend a = new SMSSend();
//		a.SMSSendMessageALL(mobiless,SMSSend.TEMPLATEID1,contentt);


		out.println("&nbsp;&nbsp;短消息添加成功！");
		out.println("<br>&nbsp;&nbsp;<a href=\"addMessage.jsp\">返回继续添加</a>");
			
	}catch(Exception ex){
		out.println(ex.toString());
		//out.println((personIdStr!=null && !personIdStr.equals("")));
	}
%>

	
	