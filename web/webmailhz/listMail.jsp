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


<SCRIPT language=Javascript>
	var currentNav = 'nav_mail'; function CA(form) {
		for (var i=0;i<form.elements.length;i++)	{
			var e = form.elements[i];
			if ((e.name != 'SELECTALL') && (e.type=='checkbox'))
			{
				e.checked = form.SELECTALL.checked;
				/*if(e.checked)
					hL(e);
				else 
					dL(e);*/
				//alert(e.parentElement.parentElement.className);
			}
		}
	}
	
	function deletemsg() {
		var msg = '将删除您所选择的邮件，操作不可恢复，您确定吗？'; 
		if(confirm(msg)){
			$("method").value= "delete";
			$("mailForm").submit();					
			return true;
		} else {
			return false;
		}
	}
	
	function movemsg() {
		var msg = '将把选择的邮件删除到垃圾箱，您确定吗？'; 
		if(confirm(msg)){
			$("method").value= "move";
			$("mailForm").submit();					
			return true;
		} else {
			return false;
		}
		
		/**
		$("move").value=1;
		$("delete").value="";
		$("msgslist").submit();
		return true;
		*/
	}
	
	function readmail(pos) {
		//document.location.href="readmsg.cgi?__mode=readmsg_sum&sid=e9ae4a349a786dce207b980606df95f2&folder=Inbox&pos="+pos;
		document.location.href="readmail.htm";
	}
	
	function editmail(file) {
		//document.location.href="compose.cgi?__mode=edit_drafts&sid=e9ae4a349a786dce207b980606df95f2&folder=Inbox&draft="+file;
	}
	
	
	function do_mkdir() {
		$("mkdir").value="1";
		$("rmdir").value="";
		$("rename").value="";
		$("foldername").value=$("fname").value;
		$("mgrform").submit();
	}
	
	function do_delete(old) {
		if (confirm("")) {
			$("mkdir").value="";
			$("rmdir").value="1";
			$("oldfolder").value=old;
			$("rename").value="";
			$("mgrform").submit();
		}
	}
	
	function do_rename(old) {
		var fname = prompt("将此邮件夹重新命名为:",old);
		if ((fname==null)||(fname==old))	{
		}	else	{
			$("mkdir").value="";
			$("rmdir").value="";
			$("rename").value="1";
			$("oldfolder").value=old;
			$("foldername").value=fname;
			$("mgrform").submit();
		}
	}
	
	function do_purge(old) {
		if (confirm("")) {
			$("mkdir").value="";
			$("rmdir").value="";
			$("rename").value="";
			$("purge").value="1";
			$("foldername").value=old;
			$("mgrform").submit();
		}
	}
	
	function showerror(err) {
		$("spanresult").innerHTML = "<font color=#ff0000>"+err+"</font>";
	}
	
	function changePart(obj){
		var vPart = obj.value;
		$("part").value= vPart;
		$("mailForm").submit();			
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
		
		<!-- Main Content Part -->
    	<TD class=tdmain vAlign=top>
      		<html:form action="/webmail/mailList.mdo" method="post">
      		
      		<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%">
        		<TBODY>
        			<TR>
          				<TD class=tdmain_in_tit height=28>
							<SPAN class=navstl>
								<SPAN class="pl10 b">收件箱</SPAN> | 
								<SPAN>当前索引排列方式: 降序标题</SPAN> 
							</SPAN>
							
							<SPAN class=navstr>
								<c:if test="${_Current_Folder.listPart>1}">
									<a href="<c:url value='/webmail/mailList'/>.mdo?method=list&part=1">
									<IMG alt=第一页 src="<c:url value='/webmailhz/images'/>/firstpg.d.gif"> &nbsp; 
									</a>
									
									<a href="<c:url value='/webmail/mailList'/>.mdo?method=list&part=${_Current_Folder.listPart-1}">
            							<IMG alt=上一页 src="<c:url value='/webmailhz/images'/>/prevpg.d.gif"> &nbsp; 
            						</a>
            					</c:if>
            					
            					
            					<c:if test="${_Current_Folder.listPart< _Current_Folder.allPart}">
									<a href="<c:url value='/webmail/mailList'/>.mdo?method=list&part=${_Current_Folder.listPart+1}">
										<IMG alt=下一页 src="<c:url value='/webmailhz/images'/>/nextpg.d.gif"> &nbsp; 
									</a>
									
									<a href="<c:url value='/webmail/mailList'/>.mdo?method=list&part=${_Current_Folder.allPart}">
										<IMG alt=最后一页 src="<c:url value='/webmailhz/images'/>/lastpg.d.gif"> 
									</a>
								</c:if>
								页数： 
								<SELECT name="pageindex" onChange="javascript:changePart(this)"> 
									<c:forEach var="part" begin="1" end="${_Current_Folder.allPart}" step="1">
										<c:choose>
											<c:when test="${part==_Current_Folder.listPart}">
												<option value="${part}" selected>${part}&nbsp;/&nbsp;${_Current_Folder.allPart}</option>
											</c:when>											
											<c:otherwise>
												<option value="${part}">${part}&nbsp;/&nbsp;${_Current_Folder.allPart}</option>
											</c:otherwise>
										</c:choose>										
									</c:forEach>	
								</SELECT> 
								
            				</SPAN>
						</TD>
					</TR>
					
					<TR>
          				<TD valign="top">
            				<TABLE class=text-overflow style="FONT-SIZE: 12px; BACKGROUND: #fff;width:100%" cellSpacing=0 cellPadding=0>
								  <COLGROUP>
								  <COL style="WIDTH: 24px">
								  <COL style="WIDTH: 20px">
								  <COL style="WIDTH: 27ex">
								  <COL style="WIDTH: 2ex">
								  <COL>
								  <COL style="WIDTH: 14ex">
								  <COL style="WIDTH: 10px">
								  <COL style="WIDTH: 14ex">
								  <TBODY>
              						<TR class=MLTR_HEAD>
										<TD align=right><INPUT onclick=CA(this.form) type=checkbox name=SELECTALL></TD>
										<TD>&nbsp;</TD>
										<TD width="200"><A href="#">来信人</A></TD>
										<TD>&nbsp; </TD>
										<TD><IMG src="<c:url value='/webmailhz/images'/>/sort_asc.gif" border=0> <A href="#">主题</A> </TD>
										<TD width="50"><A  href="#">大小</A></TD>
                						<TD>&nbsp; </TD>
                						<TD width="130"><A href="#">日期</A></TD>
									</TR>
									
									<c:forEach items="${requestScope._Mail_Messages}" var="message" varStatus="status">									
	              						<TR class=MLTR bgColor=#ffffff>
	                						<TD style="PADDING-LEFT: 3px" align=right>
												<INPUT type="checkbox" name="msgnr-${message.serialNo}"/>												
											</TD>
	                						<TD class="sc t">
	                							&nbsp;<c:if test="${message.hasAttachment}"><IMG src="<c:url value='/webmailhz/images'/>/attach.gif"></c:if>
	                						</TD>
	                						<TD> 
	                							<DIV>&nbsp;
	                								<c:choose>
	                									<c:when test="${message.seen}"><SPAN title="${message.subject}">${message.from}</SPAN></c:when>
	                									<c:otherwise><SPAN class="boldA" title="">${message.from}</SPAN></c:otherwise>
	                								</c:choose>
	                							</DIV>
	                						</TD>
	                						<TD>&nbsp; </TD>
	                						<TD style="CURSOR: pointer">
	                  							<DIV>
	                  								<A HREF="<c:url value='/webmail/mailMessage'/>.mdo?method=list&serialNo=${message.serialNo}" class="seen">
		                  								<c:choose>
		                									<c:when test="${message.seen}"><SPAN class=f12 title="${message.subject}">${message.subject}</SPAN></c:when>
		                									<c:otherwise><SPAN class="boldA" title="${message.subject}">${message.subject}</SPAN></c:otherwise>
		                								</c:choose>													
													</A>
												</DIV>
											</TD>
											<TD>${message.size}</TD>
											<TD>&nbsp; </TD>
											<TD noWrap>
												<SPAN title="Date: ${message.getDate}  Size:${message.size}" style="CURSOR: default">${message.getDate}</SPAN>
											</TD>
										</TR>
									</c:forEach>									  
									</TBODY>
							</TABLE>
						</TD>
					</TR>
					
        			<TR>
          				<TD height=28>
							<SPAN class=navsbr>
								<c:if test="${_Current_Folder.listPart>1}">
									<a href="<c:url value='/webmail/mailList'/>.mdo?method=list&part=1">
									<IMG alt=第一页 src="<c:url value='/webmailhz/images'/>/firstpg.d.gif"> &nbsp; 
									</a>
									
									<a href="<c:url value='/webmail/mailList'/>.mdo?method=list&part=${_Current_Folder.listPart-1}">
            							<IMG alt=上一页 src="<c:url value='/webmailhz/images'/>/prevpg.d.gif"> &nbsp; 
            						</a>
            					</c:if>
            					
            					
            					<c:if test="${_Current_Folder.listPart< _Current_Folder.allPart}">
									<a href="<c:url value='/webmail/mailList'/>.mdo?method=list&part=${_Current_Folder.listPart+1}">
										<IMG alt=下一页 src="<c:url value='/webmailhz/images'/>/nextpg.d.gif"> &nbsp; 
									</a>
									
									<a href="<c:url value='/webmail/mailList'/>.mdo?method=list&part=${_Current_Folder.allPart}">
										<IMG alt=最后一页 src="<c:url value='/webmailhz/images'/>/lastpg.d.gif"> 
									</a>
								</c:if>
								页数： 
								<SELECT name="pageindex" onChange="javascript:changePart(this)"> 
									<c:forEach var="part" begin="1" end="${_Current_Folder.allPart}" step="1">
										<c:choose>
											<c:when test="${part==_Current_Folder.listPart}">
												<option value="${part}" selected>${part}&nbsp;/&nbsp;${_Current_Folder.allPart}</option>
											</c:when>											
											<c:otherwise>
												<option value="${part}">${part}&nbsp;/&nbsp;${_Current_Folder.allPart}</option>
											</c:otherwise>
										</c:choose>										
									</c:forEach>	
								</SELECT>  
            				</SPAN>
							
							<SPAN class=navsbl>
								<c:if test="${_Current_Folder.id!=_Trash_Folder_Id}">
									<INPUT onClick="return movemsg()" type="button" value="删 除" name="btn_delete"> 
								</c:if>
								
								<INPUT onClick="return deletemsg()" type="button" value="永久删除" name="btn_delete"> 
								<!-- 
								<INPUT onclick=movemsg(); type=button value=移动到 name=btn_move> 
            					<SELECT name=distfolder> 
									<OPTION value=Sent selected>发件箱</OPTION> 
              						<OPTION value=Drafts>草稿箱</OPTION> 
									<OPTION value=Trash>垃圾箱</OPTION> 
              						<OPTION value=Junk>垃圾邮件</OPTION> 
									<OPTION value=filter>filter</OPTION>
								</SELECT>
								 -->
								 
								<INPUT id="part" type="hidden" name="part"/>
								<INPUT id="folderId" type="hidden" name="folderId" value="${_Current_Folder.id}"/>
								<INPUT id="method" type="hidden" name="method" value="list"/>
								<INPUT id="method" type="hidden" name="TO" value="${_Trash_Folder_Id}"/>
								
        					</SPAN>
						</TD>
					</TR>
				</TBODY>
			</TABLE>
			</html:form>
		</TD>
		<!-- Main Content Part End -->
		
		<TD width=10></TD>
	</TR>
	</TBODY>
  </TABLE>
</DIV>


</BODY>
</HTML>

