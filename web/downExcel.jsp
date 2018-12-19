<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

<title>附件下载</title>
<style>
	body{
		font-family:微软雅黑;
	}
</style>

<script>
	//window.location.href = "/common/download_excel.jsp?filepath=${filePath}";
	window.open("/common/download_excel.jsp?filepath=${filePath}",  "_blank");
	//window.close();
</script>
</head>

<body>
<br/><br/>
	<center>
		若无自动下载，请点击链接：<br/><br/><br/> <a href="/common/download_excel.jsp?filepath=${filePath}"><span
			color="black">${fileName}</span>
		</a>
		<br/><br/><br/>
		<a href="javascript:;" onclick="window.close();">关闭</a>
	</center>
</body>
</html>
