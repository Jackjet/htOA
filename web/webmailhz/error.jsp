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
		var msg = '��ɾ������ѡ����ʼ����������ɻָ�����ȷ����'; 
		if(confirm(msg)){
			$("method").value= "delete";
			$("mailForm").submit();					
			return true;
		} else {
			return false;
		}
	}
	
	function movemsg() {
		var msg = '����ѡ����ʼ�ɾ���������䣬��ȷ����'; 
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
		var fname = prompt("�����ʼ�����������Ϊ:",old);
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
      		
      		<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%">
        		<TBODY>
        			<TR>
          				<TD style="padding-left:30px" height=28 valign="top">
          					<br>
							<c:if test="${_Error_Message != null}">
								<font color="red"><b>��������</b></font>:<br>
								${_Error_Message}
							</c:if>
							<br><br>
							����&nbsp;<a href="<c:url value='/webmail/mailList'/>.mdo?method=list"><b>����</b></a>&nbsp;�����ʼ��б�
							
						</TD>
					</TR>					
				</TBODY>
			</TABLE>
			
		</TD>
		<!-- Main Content Part End -->
		
		<TD width=10></TD>
	</TR>
	</TBODY>
  </TABLE>
</DIV>

</BODY>
</HTML>

