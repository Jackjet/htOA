<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>
<html><head>
<link href="<c:url value="/"/>css/theme/7/style.css" type="text/css" rel="stylesheet">
<link href="<c:url value="/"/>css/theme/7/mytable.css" type="text/css" rel="stylesheet">
<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script> <!--jquery包-->

<style type="text/css"> 
<!--
a:link {
	font-size: 12px;
	color: #000000;
	text-decoration: none;
	letter-spacing: 1px;

}
a:visited {
	font-size: 12px;
	color: #000000;
	text-decoration: none;
	letter-spacing: 1px;

}
a:hover {
	font-size: 12px;
	color: #1C4867;
	text-decoration: underline;
	letter-spacing: 1px;

}
.STYLE1 {font-size: 5px}
.STYLE2 {color: #848284}
.STYLE5 {color: #FF0000}
-->
</style>
<script language="JavaScript">
	window.setTimeout('this.location.reload();',120000);
</script>
</head>

<body>
		<table width="100%" border="0" cellspacing="0" cellpadding="0" height="25">
                <tr>
                    <td height="27" bgcolor="#F9F9F9">
                    	<table width="100%"  border="0" cellspacing="0" cellpadding="0">
                            <tr>
                                <td width="4%" height="27">&nbsp;</td>
                                <td width="5%" align="center" valign="middle"><strong>
                                	<!-- <img src="<c:url value="/"/>images/pic.gif" width="23" height="19" />--></strong></td>
                                <td align="left">
	                                <%--<c:if test="${_SYSTEM_USER.userType != 2 && _SYSTEM_USER.userType != 3}">
	                                	 --%>
	                                	<c:if test="${_NORMAL_USER}">
	                                	 <!--<a href="<c:url value='/'/>noteslogin" target="blank">
	                                		 -->
	                                	 <a href="<c:url value='/'/>maillogin.jsp" target="blank">
	                                		<!--<strong>您有<span id="emailtotal" class="style5">0</span>封邮件 &nbsp;<span id="emailnew" class="style5">0</span>封未读邮件</strong>
	                                		<strong><span class="style5">点击</span>登入邮箱</strong>-->
	                                	</a>	
	                                </c:if>						
								</td>
								<td>&nbsp;</td>
								<!-- 
                                <td width="4%" align="center"><img src="images/sipgl/pic1.gif" width="23" height="19" /></td>
                                <td align="left"><a href="/flowtree.php"><strong>您有<span id="docs" class="style5"></span>件待办公文</strong></a></td>
                                <td width="4%" align="center"><img src="images/sipgl/002.gif" width="23" height="19" /></td>
                                <td align="left"><a href="schedules/new_schedule/week/kv_c_schedule.jsp?displayPage=0"><strong>您有<span id="meetings" class="style5"></span>个会议需要出席</strong></a></td>
                                 -->
                            </tr>
                    	</table>
                    </td>
                </tr>
                <tr> </tr>
            </table>
    
     	<script type="text/javascript">
				jQuery().ready(function (){
				    //$.getJSON("/mail/getMailInfor.do?method=getMail",function(data) {
					//	$("#emailtotal").html(data._Mail_All);
					//	$("#emailnew").html(data._Mail_New);
					//});
				});
				
				
		</script>
     		
</body>
</html>