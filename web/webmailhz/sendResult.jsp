<%@ page contentType="text/html; charset=gbk"%>
<%@ include file="/inc/taglibs.jsp"%>


<HTML>
	<HEAD><TITLE>Send Result</TITLE>
	<LINK media=screen href="<c:url value='/webmailhz'/>/css/style.css" type=text/css rel=stylesheet>
	<LINK media=screen href="<c:url value='/webmailhz'/>/css/default.css" type=text/css rel=stylesheet>
	<LINK media=screen href="<c:url value='/webmailhz'/>/css/example.css" type=text/css rel=stylesheet>

	<LINK media=screen href="<c:url value='/webmailhz'/>/css/newstyle.css" type=text/css rel=stylesheet>
	<SCRIPT src="<c:url value='/webmailhz'/>/js/common.js" type=text/javascript></SCRIPT>
	<SCRIPT src="<c:url value='/webmailhz'/>/js/menu.js" type=text/javascript></SCRIPT>   	
	   
    <HEAD>
        <TITLE>WebMail Mailbox</TITLE>
		<META CONTENT="AUTHOR" VALUE="kwchina"/>			
		<LINK media="screen" href="<c:url value='/webmailhz'/>/css/newstyle.css" type="text/css" rel="stylesheet"/>
		<LINK media="screen" href="<c:url value='/webmailhz'/>/css/text-overflow.css" type="text/css" rel="stylesheet"/>	
		
		<SCRIPT src="<c:url value='/webmailhz'/>/js/common.js" type=text/javascript></SCRIPT>
		<SCRIPT src="<c:url value='/webmailhz'/>/js/menu.js" type=text/javascript></SCRIPT>		
     </HEAD>
		
     <BODY>         
      <DIV id="Main">
				<TABLE cellspacing="0" cellpadding="0" width="100%">
				  <TBODY>
				  	<TR>
				  
						<!-- Left Part -->
			    		<TD id=MainLeft vAlign=top width=170>
						 <jsp:include page="/webmailhz/left.jsp"/>
						</TD>
				
					<!-- Left Part End -->
			    	
					<TD class=td1px></TD>
						
						
					<!-- Main Content Part -->
					<TD class="tdmain" valign="top" height="380">
						<TABLE width="100%" border="0" cellspacing="2" cellpadding="4">
							  <TR>
							    <TD height="22" class="testoNero">
							      <IMG SRC="<c:url value='/webmailhz'/>/images/icona_composer.gif" BORDER="0" align="absmiddle"/>�ʼ����ͳɹ�
							    </TD>
							  </TR>
							  
							  <!-- 
							  <TR>
							    <TD bgcolor="#697791" height="22" class="testoBianco">ʱ��: 2009��10��30�� 15:50:55</TD>
							  </TR>
							  <TR>
							    <TD class="testoGrande" bgcolor="#A6B1C0">������Ϣ<BR/>
							      	����: <SPAN class="testoNero"></SPAN><BR/>
							      	������: <SPAN class="testoNero"></SPAN><BR/>
							      	����ʱ��: <SPAN class="testoNero"></SPAN>
							    </TD>
							  </TR>
							  <TR>
							    <TD class="testoGrande" bgcolor="#D1D7E7">����״̬:
								<SPAN class="testoNero">�ɹ�
								</SPAN>
							    </TD>
							</TR>
							<TR>
						         <TD class="testoGrande" bgcolor="#E2E6F0">	
							       �ɹ�����: <SPAN class="testoNero"></SPAN>
							 </TD>
							</TR>
							<TR>
							  <TD class="testoGrande" bgcolor="#E2E6F0"> 							  
									δ�ɹ�����: 
							      	<SPAN class="testoNero">
									</SPAN>
							    	<SPAN class="testoNero"></SPAN> 
							  </xsl:if>
							  </TD>
							</TR>
							 -->
							<TR>
							  <TD>   
								<TABLE border="0" cellpadding="0" cellspacing="0">
									<TR>
										<td width="65"></td>
										<TD>
											<A HREF="<c:url value='/webmail/mailMessage'/>.mdo?method=edit">
												<IMG SRC="<c:url value='/webmailhz'/>/images/back.gif" BORDER="0"/>
											</A>
										</TD>
										<TD>
											<A HREF="<c:url value='/webmail/mailMessage'/>.mdo?method=edit">
												<SPAN class="testoBianco"> ���ؼ��������ʼ� ...</SPAN>
											</A>
										</TD>
									</TR>
								</TABLE>
							</TD>
							</TR>
						 </TABLE>
							
						 <BR/>
						 <BR/>
						 <BR/>
						 <BR/>
								
						</TD>
						<!--Main Content Part End -->
						
						
						<!-- Right Part -->
						<!--
						<TD vAlign=top width=230>
							<DIV class=panelout id=coolweather_div>
								<DIV class=paneltit>&nbsp;������Ϣ</DIV>
								<DIV class=panelin id=coolweather_container></DIV>
								<SCRIPT type=text/javascript>
									//coolweather_iconspath = '/extmail/plugins/coolweather/icons/';
									coolweather_init();
								</SCRIPT>
							</DIV>
						</TD>
						-->
						<TD width="10"></TD>
					</TR>
				</TBODY>
				</TABLE>
			</DIV>
		 </BODY>
    </HTML>
