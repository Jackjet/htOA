<%@ page contentType="text/html; charset=gbk"%>
<%@ include file="/inc/taglibs.jsp"%>


<html>
<LINK media=screen href="<c:url value='/webmailhz'/>/css/style.css" type=text/css rel=stylesheet>
<LINK media=screen href="<c:url value='/webmailhz'/>/css/default.css" type=text/css rel=stylesheet>
<LINK media=screen href="<c:url value='/webmailhz'/>/css/example.css" type=text/css rel=stylesheet>

<LINK media=screen href="<c:url value='/webmailhz'/>/css/newstyle.css" type=text/css rel=stylesheet>
<LINK media=screen href="<c:url value='/webmailhz'/>/images/text-overflow.css" type=text/css rel=stylesheet>
<SCRIPT src="<c:url value='/webmailhz'/>/js/common.js" type=text/javascript></SCRIPT>
<SCRIPT src="<c:url value='/webmailhz'/>/js/moz-text-overflow.js" type=text/javascript></SCRIPT>						

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
							<tiles:insert attribute="header_part"/>
										
							<tiles:insert attribute="systemMenu"/>	
							
							<tiles:insert attribute="subMenu"/>					
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
	        		
	        		<DIV id=Main>
						<TABLE cellSpacing=0 cellPadding=0 width="100%">
						  <TBODY>
						  <TR>
						    	<!-- Left Part -->
								<TD vAlign=top width=170 height="410" bgcolor=#ffffff>
									<tiles:insert attribute="leftMenu"/>	
								</TD>
								<!-- Left Part End -->
								
								<TD class=td1px></TD>		
								
								<!-- Main Content Part -->
						    	<TD class=tdmain vAlign=top bgcolor=#ffffff>
						      		<tiles:insert attribute="contentMain"/>
								</TD>
								<!-- Main Content Part End -->
								
								<TD width=10></TD>
							</TR>
							</TBODY>
						  </TABLE>
						</DIV>
						
	        		</td>
	        		
	        	</tr>
	      	</table>
	  </td>
    </tr>

	
    <tr valign="top">
      	<td>
			<tiles:insert attribute="bottom_part"/>		
      	</td>
    </tr>
    
  </table>
</body>
</html>
