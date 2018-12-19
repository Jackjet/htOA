<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>	

<head><title>Doh!</title></head>

<script language="javaScript">
		
	//alert('${_Parent_Location}');	
		
	//window.opener.location.href = '${_Parent_Location}';
	//alert(window.opener.location);
	window.opener.reloadTab2();
	    
    //关闭本窗口
	window.close();
</script>