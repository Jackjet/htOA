<html>
	<head>
		<title>个人邮件</title>
	</head>
	<body>
		<form name="frm" action="${_NotesUrl}" method="post">
			<input type="hidden" name="User" value="${_MailUserName}">
			<input type="hidden" name="Password" value="${_MailPasswork}">
		</form>
		<script language="javaScript">
			frm.submit();
		</script>
	<body>
</html>