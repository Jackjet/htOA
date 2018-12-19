<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>
<!doctype html>
<html>
<head>
	<meta charset="utf-8">
		<TITLE>首页</TITLE>
		<META content="text/html; charset=utf-8" http-equiv=Content-Type>
		<SCRIPT type=text/javascript src="js/jquery-1.9.1.js"></SCRIPT>
		<link href="/css/all.css" rel="stylesheet" type="text/css" />

		<script type="text/javascript" src="/js/echarts/echarts.min.js"></script>
		<script type="text/javascript" src="/js/echarts/macarons.js"></script>


		<script type="text/javascript">
			function init() {
                window.location.href="/FrameCaigouDaiban.jsp";
				<%--var url = ${_URL};--%>
                <%--window.location.href=url;--%>
            }
		</script>

	</HEAD>
	<%--<BODY style="background-color:#0B0C15;">--%>
	<BODY onload='init()' style="background-color:#0B0C15;">

	</BODY>
</HTML>
