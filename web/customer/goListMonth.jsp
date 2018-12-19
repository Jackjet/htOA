<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>
<title>编辑活动信息信息</title>
<body >
	<form:form >
	<table width="100%">
		<tr height="460" valign="top"><td><iframe src="<c:url value="/customer/activityInfor"/>.do?method=listMonth" width="100%" height="460" frameborder="0" scrolling="no"></iframe></td></tr>
	</table>
	</form:form>
</body>

