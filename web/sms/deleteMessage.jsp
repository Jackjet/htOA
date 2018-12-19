<%@ page contentType="text/html; charset=GBK"%>
<%@ include file="/inc/taglibs.jsp"%>
<%@ page language="java"
		 import="com.kwchina.sms.dao.*,
				 com.kwchina.sms.entity.*,
				 org.springframework.web.context.WebApplicationContext,
				 org.springframework.web.context.support.*"%>


<%
	ServletContext context = request.getSession().getServletContext();
 	WebApplicationContext webContext = WebApplicationContextUtils.getWebApplicationContext(context);
 
 	//SystemUserManager userManager = (SystemUserManager) webContext.getBean("systemUserManager");  
 	SMSSendMessageDAO sMSSendMessageDAO = (SMSSendMessageDAO) webContext.getBean("SMSSendMessageDAOImpl");
	
	String[] deleteIds = request.getParameterValues("toBeDeletedId");
	if (deleteIds != null && deleteIds.length > 0) {
		for (int i = 0; i < deleteIds.length; i++) {
			// int documentId = Integer.parseInt(deleteIds[i]);
			// delete person
			//if (log.isDebugEnabled()) {
			//	log.debug(deleteIds[i]);
			//}
			
			int messageId = Integer.parseInt(deleteIds[i]);
			sMSSendMessageDAO.remove(messageId);			
		}
	}
%>

<script language="javaScript">
	window.location.href="messageQuery.jsp";
</script>
