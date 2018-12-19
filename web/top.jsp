<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>
<!doctype html>
<html>
<head>
	<meta charset="utf-8">
	<script type="text/javascript" src="<c:url value="/"/>js/poshytip-1.2/demo/includes/jquery-1.4.2.min.js"></script>
	<SCRIPT type=text/javascript src="js/js.js"></SCRIPT>
	<script src="<c:url value='/js'/>/colortip-1.0/colortip-1.0-jquery.js"></script>
	<link href="/css/all.css" rel="stylesheet" type="text/css" />
	<SCRIPT>
		function re_login()
		{
			msg="确认要注销么？";
		  	if(window.confirm(msg))
		    	parent.parent.location="/logout.htm";
		}
		
		function home(){
			//window.location.href="main"
		}

	</SCRIPT>
	<style>
		body{
			margin:0 auto;
			font-family:微软雅黑;
		}
		a{
			text-decoration:none;
			color:black;
		}
		a:hover,a:visited {
			outline:none;
		}
	</style>
<META name=GENERATOR content="MSHTML 9.00.8112.16476">
<title>首页</title>
</HEAD>
<BODY style="background-color:#0B0C15;">
	<div style="border:0px solid red;margin:0 auto;width:100%;height:65px;">
		<table border=0 style="height:100%;width:100%;" cellspacing="0">
			<tr>
				<td style="border:0px solid red;width:198px;background:#0B0C15;" align="center">
					<span style="color:#0FD7FD;font-size:18px;">
						您好，<span>${_GLOBAL_PERSON.personName}</span>
					</span>
				</td>
				<td valign="middle"><span style="color:#0FD7FD;font-size:25px;">&nbsp;&nbsp;上海海通国际汽车码头采购管理平台</span></td>
				<td style="width:220px;" align=center>
					<!--  <span style="cursor:pointer;"><a href="http://192.168.1.112:8080/exam" target="_blank">考试系统</a></span>
					&nbsp;&nbsp;&nbsp;&nbsp;-->
					<span style="cursor:pointer;" onclick="javascript:;"><a style="color:#0FD7FD" href="/cggl.jsp" target="frame1">首页</a></span>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<%--<span style="color:#0FD7FD;cursor:pointer;" onclick="javascript:re_login();">退出</span>--%>
					<span style="cursor:pointer;" onclick="javascript:;"><a style="color:#0FD7FD" href="/ybpurchase/purchaseInfor.do?method=open&type=14" target="frame1">待办</a></span>
				</td>
			</tr>
		</table>
	</div>
</BODY>
</HTML>
