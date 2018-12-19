<%@ page contentType="text/html; charset=GBK"%>
<%@ include file="/inc/taglibs.jsp"%>
<%@ page language="java" import="org.springframework.web.context.WebApplicationContext,
								org.springframework.web.context.support.*,
								java.util.*,
								com.huizhi.investment.base.service.*,
								com.huizhi.investment.base.model.*" %>

<html>
<head>
<title>Person List</title>
</head>	

<link type="text/css" rel="stylesheet" href="css/yearcheck.css">

<%
	ServletContext context = request.getSession().getServletContext();
 WebApplicationContext webContext = WebApplicationContextUtils.getWebApplicationContext(context);
 ApplyInforManager userManager = (ApplyInforManager) webContext.getBean("systemUserManager");
%>


<TABLE cellSpacing=0 cellPadding=2 width="100%" border=0> 
  <tr> 
    <td> 
    	<table width="100%"  border="0" cellpadding="0" cellspacing="4" bordercolor="#004A80"> 
	    	<tr>
	    		<td> 
	    		</td>
	    	</tr>
	        <tr> 
	          <td> 
	            <!---------     -------------->              
	             <table border="0" width="100%" cellpadding="4" cellspacing="1" bgcolor="#666666">              
	               <tr align="center" valign="middle">
	               	<td width="50" align="center">序号</td>
	               	<td width="80" align="center">姓名</td>
	               	<td width="100" align="center">员工编号</td>
	               	<td width="150" align="center">&nbsp;手机号码</td>
	               	<td align="left">&nbsp;操作</td>	               	
	               </tr>
	               <%
	               	try{	               	
	               	               	               	List persons = userManager.getAllUser();
	               	               	               	//out.println(persons.length);
	               	               	               	int k = 0;
	               	               	               	for(Iterator it = persons.iterator();it.hasNext();){     
	               	               	               		ApplyInfor user = (ApplyInfor)it.next();
	               	               	               		k += 1;
	               %>
	               <tr>
	               		<td align="center"><%=k%></td>
	               		<td align="center">
	               			<%=user.getPersonName()%>
	               		</td>
	               		<td align="center"></td>
	               		<td><%=user.getMobile()%></td>
	               		<td>	               			
	               		</td>
	               </tr>               
	               <%}
	               }catch (Exception ex){
	               		out.println(ex.toString());
	               }
	               %>
	             </table> 
	           
	            <!---------     --------------> 
	          </td> 
	        </tr> 
	        <tr> 
	          <td class="SpaceDotLIne" ></td> 
	        </tr> 
      	</table>
   		
      </td> 
  </tr> 
</TABLE> 