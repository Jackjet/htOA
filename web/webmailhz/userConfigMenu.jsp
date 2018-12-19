<%@ page contentType="text/html; charset=GBK"%>
<%@ include file="/inc/taglibs.jsp"%>
<%@ page import="java.net.*"%>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="26" background="<c:url value='/images'/>/nav_bg_2.gif" class="STYLE1">
		<table  border="0" cellpadding="3" cellspacing="0">
			<tr>
		        <td width="20">&nbsp;</td>		          
		   				        
		         <td align="center" nowrap>          
	           	 	<a href="<c:url value='/webmailhz'/>/mailFrame.jsp" class="link_top_02">我的邮件</a>&nbsp;&nbsp;&nbsp;
				 </td>
				 
				 <logic:present name="webmail.session" scope="session">
					 <td align="center" nowrap>          
		           	 	<a href="<c:url value='/webmailhz'/>/mailUserConfig.jsp" class="link_top_03">邮件设置</a>&nbsp;&nbsp;&nbsp;
					 </td>
					 <%
						 //获取服务器IP
						 InetAddress localhost = InetAddress.getLocalHost();   
					  	 String ip=localhost.getHostAddress();   
				  	 %>
					 
				</logic:present>
				 
				 <!-- 
				 <td align="center" nowrap>          
	           	 	<a href="<c:url value='/core'/>/annouce.mdo?method=list" class="link_top_02">服务器设置</a>&nbsp;&nbsp;&nbsp;
				 </td>
				  -->
			</TR>
		</TABLE>
	</TD>
	</TR>
</TABLE>
<!-- head end -->