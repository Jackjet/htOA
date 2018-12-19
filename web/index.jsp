<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>
<html>
	<head>
		<title></title>
		<script src="<c:url value="/"/>components/jquery-1.4.2.js" type="text/javascript"></script> <!--jquery包-->
		<script>
			jQuery().ready(function (){
				getUserInfor();
			});
			
			var getHost = function(url) {            
				var host = "null";            
				if (typeof url == "undefined" || null == url){
					url = window.location.href;            
					var regex = /.*\:\/\/([^\/|:]*).*/;            
					var match = url.match(regex);            
				}
				if (typeof match != "undefined" && null != match) {
					host = match[1];            
				}            
				if (typeof host != "undefined" && null != host) {
					var strAry = host.split(".");
					if (strAry.length > 1) {
						host = strAry[strAry.length - 2] + "." + strAry[strAry.length - 1];
					}
				}         
				alert(host);   
				return host;        
			}        
			//document.domain=getHost();
			
			function getUserInfor(){
				/*
				工具>Internet选项>安全>Internet>自定义级别>ActiveX控件和插件>对没有标记为安全的ActiveX控件进行初始化和脚本运行,设置为“启动”或“提示”。
				*/
				try{
					//alert(0);
					var WshNetwork = new ActiveXObject("WScript.Network");
					//alert("Domain = " + (WshNetwork.UserDomain).toLowerCase());
					//alert("Computer Name = " + WshNetwork.ComputerName);
					//alert("User Name = " + WshNetwork.UserName);
					var domain = WshNetwork.UserDomain.toLowerCase();
					var computerName = WshNetwork.ComputerName;
					var userName = WshNetwork.UserName.toLowerCase();
					//alert(domain);
					//如果域名包含haitongauto.com，则自动用此域用户名登录
					
					
					if(domain.indexOf("haitongauto") > -1){
						/*$.ajax({
							url: '/autoLogin.do?userName='+userName,
							cache: false,
							type: "GET",
							dataType : "json",
							async: true,
			               	cache: false,
							beforeSend: function (xhr) {
							},
							success : function (data) {
								var personList = new Array();
								personList = data._Infors;
								
							}
						});*/
						
						//window.location.href = '/autoLogin.do?userName='+userName;
						$("#userName").val(userName);
						frm.submit();
					}else{
						window.location.href = 'login.jsp';
					}
				}catch(e){
					if(confirm("您的IE浏览器设置导致无法自动登录，请按以下设置：\n\n工具>Internet选项>安全>Internet>自定义级别>ActiveX控件和插件>对没有标记为安全的ActiveX控件进行初始化和脚本运行,设置为“启动”或“提示”\n\n或者点确定转到普通登录")){
						window.location.href = 'login.jsp';
					}
				}
			}
		</script>
	</head>
	<body>
		<!-- 
		<iframe id="iFrame1" src="homepage.jsp" style="position:absolute; left:0; top:0;" width="100%" frameborder="0" onload="this.height=iFrame1.document.body.scrollHeight" />
		 -->
		 <form name="frm" action="/autoLogin.do" method="post">
		 	<input type="hidden" name="userName" id="userName">
		 </form>
	</body>

</html>