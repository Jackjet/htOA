<%@ page contentType="text/html; charset=gbk"%>
<%@ include file="/inc/taglibs.jsp"%>

<HTML><HEAD><TITLE>读邮件</TITLE>
<LINK media=screen href="<c:url value='/webmailhz'/>/css/style.css" type=text/css rel=stylesheet>
<LINK media=screen href="<c:url value='/webmailhz'/>/css/default.css" type=text/css rel=stylesheet>
<LINK media=screen href="<c:url value='/webmailhz'/>/css/example.css" type=text/css rel=stylesheet>

<LINK media=screen href="<c:url value='/webmailhz'/>/css/newstyle.css" type=text/css rel=stylesheet>
<SCRIPT src="<c:url value='/webmailhz'/>/js/common.js" type=text/javascript></SCRIPT>
<SCRIPT src="<c:url value='/webmailhz'/>/js/menu.js" type=text/javascript></SCRIPT>

<SCRIPT language=javascript>
	var currentNav = 'nav_mail';
	
	
	function Do(action) {
		switch (action)	{			
			
			
			case "fwdatt":
				window.location.href='compose.cgi?__mode=edit_forward&sid=e9ae4a349a786dce207b980606df95f2&folder=Inbox&pos=14&asattach=1';
				break;
			case "detail":
				window.location.href='readmsg.cgi?__mode=readmsg_sum&sid=e9ae4a349a786dce207b980606df95f2&folder=Inbox&pos=14&detail=1';
				break;
			case "nodetail":
				window.location.href='readmsg.cgi?__mode=readmsg_sum&sid=e9ae4a349a786dce207b980606df95f2&folder=Inbox&pos=14';
				break;
			
			case 'rpl':
				window.location.href='<c:url value='/webmail/mailMessage'/>.mdo?method=reply';
				break;
			case "rplall":
				//window.location.href='compose.cgi?__mode=edit_reply&sid=e9ae4a349a786dce207b980606df95f2&folder=Inbox&pos=14&replyall=1';
				alert("--此功能暂未实现--");
				break;
			case "fwd":
				window.location.href='<c:url value='/webmail/mailMessage'/>.mdo?method=forward';
				break;
				
			case "del":
				if (confirm("将删除您当前阅读的邮件，操作不可恢复，您确定吗？")) {
					//var url = '<c:url value='/webmail/mailList.mdo'/>?method=delete&';
					//window.location.href = url;
					
					$("method").value= "delete";
					$("mailForm").submit();	
				}
				break;
				
			case "delTrash":
				if (confirm("将删除当前邮件到垃圾箱，您确定吗？")) {					
					$("method").value= "move";
					$("mailForm").submit();	
				}
				break;
			
			
			case "print":
				var url = "?__mode=readmsg_sum&sid=e9ae4a349a786dce207b980606df95f2&folder=Inbox&pos=14";
				url += '&screen=print.html';
				var hWnd = window.open(url,"","width=550,height=450,resizable=yes,status=yes,scrollbars=yes");
				if ((document.window != null) && (!hWnd.opener))
					hWnd.opener = document.window;
				break;
			case "rawdata":
				window.open('readmsg.cgi?__mode=readmsg_rawdt&sid=e9ae4a349a786dce207b980606df95f2&folder=Inbox&pos=14');
				break;
			case "header":
				window.open('readmsg.cgi?__mode=readmsg_header&sid=e9ae4a349a786dce207b980606df95f2&folder=Inbox&pos=14');
				break;
			case "top":
				window.location.href='folders.cgi?__mode=messages_list&sid=e9ae4a349a786dce207b980606df95f2&folder=Inbox&page=0';
				break;
			
			case "prev":
				window.location.href='<c:url value='/webmail/mailMessage'/>.mdo?method=list&serialNo=${_Mail_Message.serialNo-1}';
				break;
			case "next":
				window.location.href='<c:url value='/webmail/mailMessage'/>.mdo?method=list&serialNo=${_Mail_Message.serialNo+1}';
				break;						
		}
	}
	
	function ChgEnc(enc) {
		var url ='readmsg.cgi?__mode=readmsg_sum&sid=e9ae4a349a786dce207b980606df95f2&folder=Inbox&pos=14';
		window.location.href= url + '&charset=' + enc;
	}
	
	function eURL(str) {
		var rv = ' '; // not '' for a NS bug!
		for (i=0; i < str.length; i++) {
			aChar=str.substring(i, i+1);
			switch(aChar) {
				case '=': rv += "%3D"; break;
				case '?': rv += "%3F"; break;
				case '&': rv += "%26"; break;
				default: rv += aChar;
			}
		}
		return rv.substring(1, rv.length);
	}
	
	/* AddressBook */
	function html2txt(s) {
			s = s.replace(/&amp;/g, '&').replace(/&qute;/g,'"');
		s = s.replace(/&nbsp;/g, ' '); /* replace all space ? */
			s = s.replace(/&lt;/g, '<').replace(/&gt;/g, '>');
			return s;
	}
	
	function Add_Contact(obj) {
		var txt = html2txt(obj.innerHTML);
		var re = /\s*['\"]*\s*([^\'\"]*)\s*[\'\"]*(\s+|^)<*([a-z0-9A-Z\-_\.]+@[a-z0-9A-Z-\_.]+)>*/;
		if(re.test(txt)) {
			var url ='abook.cgi?sid=e9ae4a349a786dce207b980606df95f2&__mode=abook_show&screen=abook_edit.html';
			var name = RegExp.$1;
			var addr = RegExp.$3;
	
			if(name == "") {
				var re2 = /([^@]+)@(.*)/;
				if(re2.test(addr)) {
				name = RegExp.$1;
				}else {
				name = addr; /* fallback to addr */
				}
			}
			/* convert & to ; to advoid request error, dist programe should do url
			   convertion and recovery the url parameter */
			url += '&name='+escape(name)+'&mail='+addr;
			url += '&url='+eURL('/extmail/cgi/readmsg.cgi?sid=e9ae4a349a786dce207b980606df95f2&folder=Inbox&pos=14');
			document.location.href= url;
		}else {
			alert('Error on parsing sender! Abort');
		}
	}
	
	function DoDelete() {
		if (confirm("将删除您当前阅读的邮件，操作不可恢复，您确定吗？")) {
			var url = '?__mode=delete&sid=e9ae4a349a786dce207b980606df95f2&folder=Inbox&pos=14';
			document.location.href = url;
		}
	}
	
	function DoPrint() {
		var url = "?__mode=readmsg_sum&sid=e9ae4a349a786dce207b980606df95f2&folder=Inbox&pos=14";
		url += '&screen=print.html';
		var hWnd = window.open(url,"","width=550,height=450,resizable=yes,status=yes,scrollbars=yes");
		if ((document.window != null) && (!hWnd.opener))
			hWnd.opener = document.window;
	}
</SCRIPT>

<BODY>
<DIV id=Main>
<TABLE cellSpacing=0 cellPadding=0 width="100%">
  <TBODY>
  <TR>
    	<!-- Left Part -->
		<TD id=MainLeft vAlign=top width=170 height="410">
			<jsp:include page="/webmailhz/left.jsp"/>
		</TD>
		<!-- Left Part End -->
    	
		<TD class=td1px></TD>
    	
		<!-- Right Part -->
		<TD class=tdmain vAlign=top>
      		<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%">
        		<TBODY>
        			<TR>
          				<TD class=tdmain_in_tit height=28>
							<SPAN class=navstl>
								<SPAN class="pl10 b">标题：</SPAN> 
								<SPAN>${_Mail_Message.subject}</SPAN>
							</SPAN> 
							
							<SPAN class=navstr>
								 
								<SPAN class=mopspan onClick="Do('rpl')">回复</SPAN><SPAN id=m_reply><IMG src="<c:url value='/webmailhz/images'/>/sl.gif"><SCRIPT type=text/javascript>menuregister(true, "m_reply")</SCRIPT></SPAN> | 
								
								<SPAN class=mopspan onClick="Do('fwd')">转发</SPAN><SPAN id=m_fwd><IMG src="<c:url value='/webmailhz/images'/>/sl.gif"><SCRIPT type=text/javascript>menuregister(true, "m_fwd")</SCRIPT></SPAN> | 
								<!--
								<SPAN id=m_char>编码<IMG src="<c:url value='/webmailhz/images'/>/sl.gif"><SCRIPT type=text/javascript>menuregister(true, "m_char")</SCRIPT></SPAN> | 
								 -->
								<SPAN class=mopspan onClick=";">删除</SPAN><SPAN id=m_del><IMG src="<c:url value='/webmailhz/images'/>/sl.gif"><SCRIPT type=text/javascript>menuregister(true, "m_del")</SCRIPT></SPAN> |  
								
								&lt;
								<c:if test="${_Mail_Message.serialNo>1}">
									<SPAN class="mopspan" onClick="javascript:Do('prev')">上一封</SPAN>
								</c:if>								
												
								<c:if test="${_Mail_Message.serialNo<_Mail_Folder.totalMessage}">
									<c:if test="${_Mail_Message.serialNo>1}"> | </c:if>
									<SPAN class="mopspan" onClick="javascript:Do('next')">下一封</SPAN>
								</c:if>
								&gt;
								<!-- 
								 |
								<SPAN id=m_more>
									更多操作选项<IMG src="<c:url value='/webmailhz/images'/>/sl.gif">
									<SCRIPT type=text/javascript>menuregister(true, "m_more")</SCRIPT>
								</SPAN> 
								 -->
							</SPAN>
						</TD>
					</TR>
					
					<TR>
					  	<TD class=tdmain_in_con_mt valign="top">
							<DIV class=pl10 style="LINE-HEIGHT: 18px; HEIGHT: 18px">
								<SPAN><B>来自：</B></SPAN> 
								<SPAN id=from>${_Mail_Message.from}</SPAN> 
								<!-- 
								&nbsp;
								<A title=将该来信人加到地址本中 href="javascript:Add_Contact(from)">将该来信人加到地址本中 </A>
								 -->
							</DIV>
							<DIV class=pl10>
								<SPAN><B>发给：</B></SPAN> 
								<SPAN>${_Mail_Message.to}</SPAN> 
							</DIV>
							<DIV class=pl10 style="LINE-HEIGHT: 18px; HEIGHT: 18px">
								<SPAN><B>日期：</B></SPAN> 
								<SPAN>${_Mail_Message.getDate}</SPAN> 
							</DIV>
							
							<c:if test="${_Mail_Message.hasAttachment}">   
								<DIV style="line-height:18px;padding-top:2px;" class="pl10">
									<TABLE border="0" cellspacing="0" cellpadding="0">
										<TR>
											<TD style="vertical-align: top;"><B>附件列表：</B></TD>
											<TD id="attachment_list">
												<TABLE border="0" cellspacing="0" cellpadding="0"> 				
													 <c:forEach items="${requestScope._Message_Parts}" var="part" varStatus="status">											  			
											  			<c:if test="${part.type=='binary' ||  part.type=='image'}">
											  				<TR>
																<TD style="padding-bottom: 2px">		
																	<A title="${part.fileName} (${part.size})" HREF="<c:url value='/webmail/showMime'/>.mdo?method=list&part=${part.partCount}&serialNo=${_Mail_Message.serialNo}">
																		${part.fileName} (${part.size})
																	</A>																		
																</TD>
																
																<TD style="padding-left: 10px; padding-top:2px"></TD> 				
															</TR>
											  			</c:if>
											  		</c:forEach>													
								 				</TABLE>
											</TD>
										</TR>
									</TABLE>								
								</DIV> 
							</c:if>							
						</TD>
					</TR>
					<TR>
					  	<TD class=mailbody style="height:280px;PADDING-RIGHT: 20px; PADDING-LEFT: 20px; PADDING-BOTTOM: 20px; PADDING-TOP: 20px; BORDER-BOTTOM: #d6e0e9 0px solid" valign="top">
					  		<c:forEach items="${requestScope._Message_Parts}" var="part" varStatus="status">					  			
					  			<c:if test="${!part.hidden && part.type!='binary' &&  part.type!='image'}">
					  				<jspView:viewHTML escapeTag="true">${part.content}</jspView:viewHTML>
					  			</c:if>
					  		</c:forEach>
					  	</TD>
					</TR>
				</TBODY>
			</TABLE>
		</TD>
		<!-- Right Part End -->		
		
		<TD width=10></TD>
	</TR>
  </TBODY>
  </TABLE>
</DIV>

	

	<DIV class=headermenu_popup id=m_reply_menu style="DISPLAY: none">
		<TABLE cellSpacing=0 cellPadding=4 border=0>
		  <TBODY>
		  <TR>
			<TD class=popupmenu_option onClick="Do('rpl');">回复</TD></TR>
		  <TR>
			<TD class=popupmenu_option onclick="Do('rplall');">回复全部</TD></TR></TBODY></TABLE>
	</DIV>
	
	<DIV class=headermenu_popup id=m_fwd_menu style="DISPLAY: none">
		<TABLE cellSpacing=0 cellPadding=4 border=0>
		  <TBODY>
		  <TR>
			<TD class=popupmenu_option onClick="Do('fwd');">转发</TD></TR>
		  <!-- <TR>
			<TD class=popupmenu_option onclick="Do('fwdatt');">附件转发</TD></TR>
		 -->
		</TBODY>
		</TABLE>
	</DIV>
	
	<DIV class=headermenu_popup id=m_del_menu style="DISPLAY: none">
		<TABLE cellSpacing=0 cellPadding=4 border=0>
		  <TBODY>
		  <TR>
			<TD class=popupmenu_option onClick="Do('delTrash');">删除</TD></TR>
		  <TR>
			<TD class=popupmenu_option onclick="Do('del');">永久删除</TD></TR>
		</TBODY>
		</TABLE>
	</DIV>
	
	<DIV class=headermenu_popup id=m_char_menu style="DISPLAY: none">
		<TABLE cellSpacing=0 cellPadding=4 border=0>
		  <TBODY>
		  <TR>
			<TD class=popupmenu_option onclick='ChgEnc("auto")'>auto</TD></TR>
		  <TR>
			<TD class=popupmenu_option style="BACKGROUND: #ccc" 
			onclick='ChgEnc("gb2312")'>gb2312</TD></TR>
		  <TR>
			<TD class=popupmenu_option onclick='ChgEnc("gbk")'>gbk</TD></TR>
		  <TR>
			<TD class=popupmenu_option onclick='ChgEnc("gb18030")'>gb18030</TD></TR>
		  <TR>
			<TD class=popupmenu_option onclick='ChgEnc("big5")'>big5</TD></TR>
		  <TR>
			<TD class=popupmenu_option onclick='ChgEnc("utf-8")'>utf-8</TD></TR>
		  <TR>
			<TD class=popupmenu_option 
		  onclick='ChgEnc("iso-2022-jp")'>iso-2022-jp</TD></TR>
		  <TR>
			<TD class=popupmenu_option onclick='ChgEnc("shift-jis")'>shift-jis</TD></TR>
		  <TR>
			<TD class=popupmenu_option onclick='ChgEnc("euc-jp")'>euc-jp</TD></TR>
		  <TR>
			<TD class=popupmenu_option onclick='ChgEnc("euc-kr")'>euc-kr</TD></TR>
		  <TR>
			<TD class=popupmenu_option 
		  onclick='ChgEnc("iso-2022-kr")'>iso-2022-kr</TD></TR></TBODY>
		</TABLE>
	</DIV>
		
	<DIV class=headermenu_popup id=m_more_menu style="DISPLAY: none">
		<TABLE cellSpacing=0 cellPadding=4 border=0>
		 <TBODY>
		  	<TR>
				<TD class=popupmenu_option onClick="Do('detail')">详细结构</TD></TR>
		  	<TR>
				<TD class=popupmenu_option onClick="Do('rawdata')">原始邮件</TD></TR>
		  	<TR>
				<TD class=popupmenu_option onClick="Do('print')">打印</TD></TR>
		  	<TR>
				<TD class=popupmenu_option onClick="Do('top')">返回</TD></TR>
		  	<TR>
				<TD class=popupmenu_option onclick="Do('header')">信头</TD></TR>
		</TBODY>
	</TABLE>
	</DIV>
	
	<html:form action="/webmail/mailList.mdo" method="post">
		<INPUT type="hidden" name="msgnr-${_Mail_Message.serialNo}" value="on"/>
		<INPUT type="hidden" name="method" value="delete"/>
		
		<INPUT id="part" type="hidden" name="part" value="${_Current_Folder.listPart}"/>
		<INPUT id="folderId" type="hidden" name="folderId" value="${_Current_Folder.id}"/>
		<INPUT id="method" type="hidden" name="TO" value="${_Trash_Folder_Id}"/>
	</html:form>

</BODY>
</HTML>
