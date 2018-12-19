<%@ page contentType="text/html; charset=gbk"%>
<%@ include file="/inc/taglibs.jsp"%>

<html>
<head>
<title>综合办公系统</title>
</head>

<body leftmargin="0" topmargin="0" >
  <table border="0" width="100%" cellspacing="0" cellpadding="0">
    <tr valign="top">
		<TD width="100%">
			<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0>
				<TBODY>
					<TR>
						<TD vAlign=top style="padding-bottom:8px">
							<!-- TOP PART -->
							<jsp:include flush="true" page="../templates/top.jsp"></jsp:include>
													
							<jsp:include flush="true" page="mailMenu.jsp"></jsp:include>				
										
							<jsp:include flush="true" page="mailSubMenu.jsp"></jsp:include>
							<!-- TOP PART end -->		
						</TD>
					</TR>
				</TBODY>
			</TABLE>
		</TD>
    </tr>
	
	<tr valign="top">
		<td>
	      	<table width="100%" height="440" border="0" cellpadding="0" cellspacing="0">
	        	<tr>
	        		<td width="100%" rowspan="2" align=middle valign=top bgcolor=#bfd2e3>
	        			<%
	        				String serialNo = request.getParameter("serialNo");
	        				if(serialNo!=null && !serialNo.equals("")){
	        			%>
	        			<iframe src="<c:url value='/webmail/mailMessage'/>.mdo?method=list&serialNo=<%=serialNo%>" name="left" id="left" align="MIDDLE" width="100%" height="300" frameborder="0" scrolling="no" id="mailleft" name="mailleft" onload="Javascript:SetCwinHeight(this)"> </iframe>
	        			<% } %>
	        		</td>
	        		
	        	</tr>
	      	</table>
	  </td>
    </tr>
    
    <script>
		function SetCwinHeight(obj){
	  		var cwin=obj;
	  		if (document.getElementById) {
	    		if (cwin && !window.opera) {
			      	if (cwin.contentDocument && cwin.contentDocument.body.offsetHeight)
			        	cwin.height = cwin.contentDocument.body.offsetHeight; 
			      	else if(cwin.Document && cwin.Document.body.scrollHeight)
			        	cwin.height = cwin.Document.body.scrollHeight;
		   		}
	  		}
		}
	</script>

	
    <tr valign="top">
      	<td>
			<jsp:include flush="true" page="../templates/bottom.jsp"></jsp:include>		
      	</td>
    </tr>
    
  </table>
</body>
</html>
