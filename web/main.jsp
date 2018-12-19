<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>
<html>
	<head>
		<link href="<c:url value="/"/>css/theme/7/style.css" type="text/css" rel="stylesheet">
		<script src="<c:url value="/"/>inc/ccorrect_btn.js"></script>
	</head>


	<frameset framespacing="0" border="0" frameborder="NO" cols="*" rows="28,28,*">
    <frame scrolling="no" frameborder="0" src="loginInfo.jsp" noresize="" name="login_info"></frame>
    <frame scrolling="no" frameborder="0" src="selfInfo.jsp" noresize="" name="self_info"></frame>
    <frame scrolling="auto" frameborder="0" src="/mainInfor.do" noresize="" name="main"></frame>
		<!--<div id="livemargins_control" style="position: absolute; display: none; z-index: 9999;"></div>-->
	</frameset>
</html>