<%@ page contentType="text/html; charset=gbk"%>
<%@ include file="/inc/taglibs.jsp"%>

<html>				
							
									<SCRIPT language=javascript>
				function CheckMail() {
					//var url = '/extmail/cgi/folders.cgi?__mode=folders_list';
					//url += '&chkpop=true&sid=e9ae4a349a786dce207b980606df95f2&screen=welcome.html';
					//window.location = url;
				}
		
				function Compose() {
					window.location.href="<c:url value='/webmail/mailMessage'/>.mdo?method=edit";
				}
		
				function LM_OCTRL(CLSNAME)	{
					var obj = document.getElementById(CLSNAME);
					if (obj.style.display=="none"){
						obj.style.display = "block";
						document.getElementById(CLSNAME + "_CTRLIMG").src = "images/lo.gif";
					}else{
						obj.style.display = "none";
						document.getElementById(CLSNAME + "_CTRLIMG").src = "images/lc.gif";
					}
				}
			</SCRIPT>
	
			<DIV style="TEXT-ALIGN: center">
				<INPUT class=lnbtn id=ckbtn onclick="" type=button value=列表>&nbsp; 
				<INPUT class=lnbtn id=cmbtn onclick=Compose() type=button value=写邮件> 
			</DIV>
			
			<DIV id=DivSysFolder style="CLEAR: both; MARGIN-TOP: 0px">
				<DIV style="MARGIN-TOP: 5px; DISPLAY: block; HEIGHT: 20px">
					<DIV style="PADDING-LEFT: 5px; FONT-WEIGHT: bold; FLOAT: left; CURSOR: pointer; COLOR: #3b107b; PADDING-TOP: 2px">
						<SPAN><IMG id=LM_SYSFD_CTRLIMG src="<c:url value='/webmailhz/images'/>/lo.gif"></SPAN> 系统邮件夹
					</DIV>
					<DIV class=btnlm></DIV>
				</DIV>
				<DIV id=LM_SYSFD>
					<UL id=LM_SYSFD_UL>
						<c:forEach items="${requestScope._Folders}" var="folder" varStatus="status">
							<LI class=fdnav><A HREF="<c:url value='/webmail/mailList'/>.mdo?method=list&folderId=${folder.folderId}&part=1">
								<c:choose>
									<c:when test="${folder.folderName=='Inbox' || folder.folderName=='INBOX'}">收件箱</c:when>
									<c:when test="${folder.folderName=='Trash' || folder.folderName=='TRASH'}">垃圾箱</c:when>									
									<c:otherwise>${folder.folderName}</c:otherwise>						
								</c:choose>
								
								 
								<c:choose>
									<c:when test="${folder.newMessage==0}">${folder.newMessage}/${folder.totalMessage}</c:when>
									<c:otherwise><FONT color="red">${folder.newMessage}</FONT>/${folder.totalMessage}</c:otherwise>
								</c:choose>								
							</A></LI>
						</c:forEach>
					</UL>
				</DIV>
			</DIV>
			
			<br><br>
			<DIV id=DivSysFolder style="CLEAR: both; MARGIN-TOP: 0px">
				<DIV id=mquota>
					<!-- quota usage bar -->
					<TABLE cellSpacing=0 cellPadding=0 border=0>
						<TBODY>
							<c:choose>
								<c:when test="${_Mail_Quota.limit==0}">
									<TR>
										<TD class=quotapc noWrap colSpan=3>邮箱无限额</TD>
									</TR>
								</c:when>
							
								<c:otherwise>
									<TR>
										<TD class=quotapc noWrap colSpan=3>
											您已使用了<fmt:formatNumber value="${_Mail_Quota.usage/_Mail_Quota.limit}" type="percent"/>的${_Mail_Quota.limitKB}M
										</TD>
									</TR>
									<TR style="PADDING-BOTTOM: 3px">
										<TD class=quotapc width=5>0%</TD>
										<TD width=100>
												 <TABLE class=quotabar cellSpacing=0 cellPadding=0 width="100%" border=0>
												    <TBODY>
												       <TR>
												         <TD style="BORDER-RIGHT: #c7c8cb 1px solid; PADDING-RIGHT: 1px; BORDER-TOP: #c7c8cb 1px solid; PADDING-LEFT: 1px; PADDING-BOTTOM: 1px; BORDER-LEFT: #c7c8cb 1px solid; PADDING-TOP: 1px; BORDER-BOTTOM: #c7c8cb 1px solid" 
												                width="100%">
												             <DIV class=quotapc_normal 
												                  style="FONT-SIZE: 6px; WIDTH: ${(_Mail_Quota.usage*100)/_Mail_Quota.limit}%; HEIGHT: 6px"></DIV>
												          </TD>
												         </TR>
												       </TBODY>
												   </TABLE>
										</TD>
										<TD class=quotapc align=left>100%</TD>
									</TR>
								</c:otherwise>
							</c:choose>
														
							<TR>
								<TD class=quotapc colSpan=3>								
									已使用 ${_Mail_Quota.usage}K
								</TD>
							</TR>
						</TBODY>
					</TABLE>
										          <!-- end of quota bar -->
										       </DIV>
			</DIV>
									 
							