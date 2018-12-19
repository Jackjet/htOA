<%@ page contentType="text/html; charset=gbk"%>
<%@ include file="/inc/taglibs.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>登陆</title>
<LINK media=screen href="<c:url value='/webmailhz'/>/css/style.css" type=text/css rel=stylesheet>
<LINK media=screen href="<c:url value='/webmailhz'/>/css/default.css" type=text/css rel=stylesheet>
<LINK media=screen href="<c:url value='/webmailhz'/>/css/example.css" type=text/css rel=stylesheet>

<link rel="stylesheet" href="/wmail/standard/default.css" type="text/css">
<style> 
body {
	font-family: arial;
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
table td {font-family: arial;}
</style> <script type="text/javascript" src="/wmail/extlib.js"> 
</script>
<script type="text/javascript" language="javascript"> 
<!--
	function submit()
	{
	document.login.submit();
	return true;
	}
	function gotonmc()
	{
	window.open("/nmc","","");
	return false;
	}
 
 
 
function fixDate (date) {
    var base = new Date(0);
    var skew = base.getTime();
    if (skew > 0)
        date.setTime(date.getTime() - skew);
}
 
function genNowTime() {
    var now = new Date();
    fixDate(now);
    now.setTime(now.getTime() + 365 * 24 * 60 * 60 * 1000);
    now = now.toGMTString();
    return now;
}  

function setFocus() {
    var f = document.forms['login'];
    if (f) {
	if (f.username.value == null || f.username.value == "") {
	    f.username.focus();
	} else {
	    f.password.focus();
	}
    }
}
//-->
</script> 
</head>
<body onload="setFocus()"> 
<table width="100%" height="400"  border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td align="center" valign="middle"><table width="499" height="304" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td valign="top" background="<c:url value='/webmailhz/images'/>/load2.jpg">
        <table width="499" height="200" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td width="200" height="60" colspan="2">&nbsp;</td>
            <td width="299" rowspan="6">&nbsp;</td>
          </tr>
          
          
         <form name="login" method="post" action="<c:url value='/webmail'/>/mailLogin.mdo"><tr>
            <td width="60" height="30" align="right"><p>用户名&nbsp;&nbsp;</p></td>
            <td width="120" align="left">
              <input type=text name="username" style="height: 18; width: 140; border: 1px groove #C0C0C0; " size="30">
            </td>
          </tr>
          <tr>
            <td width="60" height="25" align="right"><p>@&nbsp;&nbsp;</p></td>
            <td height="25" align="left">
            	<input type=text name=domain style="height: 18; width: 140; border: 1px groove #C0C0C0; " size="30" value="${_Domain_Name}" readonly>
            </td>
          </tr>
          <tr>
            <td width="60" valign="center" height="25" align="right">密  码&nbsp;&nbsp;</td>
            <td height="25" align="left">
            	<input name=password type=password style="height: 18; width: 140; border: 1px groove #C0C0C0; " size="30">
            </td>
          </tr>
          <tr>
            <td height="30" colspan="2" valign="bottom" align="left">
            	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            	<input type=image width="70" height="26" src="<c:url value='/webmailhz/images'/>/BT1.jpg" border="0" name=imageField  style="cursor:hand;">
            	&nbsp;&nbsp;&nbsp;
            </td>
          </tr>
          </form>
        </table></td>
      </tr>
    </table></td>
  </tr>
</table>
 
</body>
</html>
 

