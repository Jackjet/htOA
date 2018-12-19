<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>
<%@ page import="org.jasig.cas.client.authentication.AttributePrincipal"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<HTML>
	<HEAD><TITLE>海通OA</TITLE>
		<META content="text/html; charset=gbk" http-equiv="Content-Type">
		<META content="text/css" http-equiv="Content-Style-Type">
		<META content="no-cache" http-equiv="Pragma">
		<!--<LINK rel="stylesheet" type="text/css" href="/images/login/login.css">
		--><SCRIPT language="javascript" src="/images/login/pageCommon.js"></SCRIPT>		
		<SCRIPT language="javascript" src="/images/login/login.js"></SCRIPT>		
		<SCRIPT language="javascript" src="/images/login/stm31.js"></SCRIPT>
		<script src="<c:url value="/"/>components/jquery-1.4.2.js" type="text/javascript"></script> <!--jquery包-->
		<!-- <script type="text/javascript">
			var url = window.location.href;
	        if (url.indexOf("https") < 0) {
	            url = url.replace("http:", "https:");
	            window.location.replace(url);
	        }
		</script> -->
		<%
			/*String auth = request.getHeader("Authorization");
			if (auth == null)
			{
			  response.setStatus(response.SC_UNAUTHORIZED);
			  response.setHeader("WWW-Authenticate", "NTLM");
			  response.flushBuffer();
			  return;
			}
			if (auth.startsWith("NTLM "))
			{
			  byte[] msg = new sun.misc.BASE64Decoder().decodeBuffer(auth.substring(5));
			  int off = 0, length, offset;
			  if (msg[8] == 1)
			  {
			    byte z = 0;
			    byte[] msg1 = {(byte)'N', (byte)'T', (byte)'L', (byte)'M', (byte)'S', (byte)'S', (byte)'P', 
			      z,(byte)2, z, z, z, z, z, z, z,(byte)40, z, z, z, 
			      (byte)1, (byte)130, z, z,z, (byte)2, (byte)2,
			      (byte)2, z, z, z, z, z, z, z, z, z, z, z, z};
			    response.setHeader("WWW-Authenticate", "NTLM " + 
			       new sun.misc.BASE64Encoder().encodeBuffer(msg1));
			    response.sendError(response.SC_UNAUTHORIZED);
			    return;
			  }
			  else if (msg[8] == 3)
			  {
			    off = 30;
	
			    length = msg[off+17]*256 + msg[off+16];
			    offset = msg[off+19]*256 + msg[off+18];
			    String remoteHost = new String(msg, offset, length);
	
			    length = msg[off+1]*256 + msg[off];
			    offset = msg[off+3]*256 + msg[off+2];
			    String domain = new String(msg, offset, length);
	
			    length = msg[off+9]*256 + msg[off+8];
			    offset = msg[off+11]*256 + msg[off+10];
			    String username = new String(msg, offset, length);
	
			    out.println("Username:"+username+"<BR>");
			    out.println("RemoteHost:"+remoteHost+"<BR>");
			    out.println("Domain:"+domain+"<BR>");
			  }
			}*/
			
			//out.println(request.getRemoteUser());
		%>
		<%--<% String path=request.getContextPath();--%>
			<%--String scheme=request.getScheme();--%>
			<%--String server=request.getServerName();--%>
		<%--%>--%>

		<script type="text/javascript">
            <%--var path="<%=path%>";--%>
            <%--var scheme="<%=scheme%>";--%>
            <%--var server="<%=server%>";--%>
            <%--var url=server;--%>
            <%--if( 1== 1){--%>
                <%--alert(url);--%>
            <%--}--%>
            <%--if (server == "localhost"){--%>
                <%--alert(111);--%>
			<%--}--%>
			
			function rstForm(){
				$(".TextField").val("");
				$("#userName").focus();
			}
		</script>
		<style type="text/css">
			body{
				align:center;
			}
			#container{
				margin:0 auto;
				width:1024px;
				height:768px;
				background: url(/images/login/login.jpg) no-repeat;
				border:0px solid red;
			}
			#maincontent {
				position: relative;
				top: 120px;
				left: 600px;
				width: 405px;
				height:345px;
				border:0px solid red;
				BACKGROUND:url(/images/login/form_bg.png) no-repeat;
			}
			#LoginInfo{
				border:0px solid red;
				width:100%;
				margin-top:0px;
				position: relative;
				top:90px;
			}
			#loginTab{
				margin-left:85px;
			}
			#btnTab{
				margin-left:26px;
				height:110px;
				border:0px solid red;
				width:370px;
			}
			.TextField{
				height:46px;
				line-height:48px;
				font-size:18px;
				width:290px;
				border-left:0px solid red;
				border-right:0px solid red;
				border-top:0px solid red;
				border-bottom:0px solid red;
			}
		</style>
		<script type="text/javascript">
			$(window).load(function() {
				//$("#background").fullBg();
				
			});
			jQuery().ready(function (){
				//getUserInfor();
			});



            function logout(){
                //请求CAS服务器登出
                window.location.href="http://localhost:8088/logout.do";
                //清除应用服务器session
                <%request.getSession().invalidate();%>
            }
			
			function error() {	
			}
						
		</script>
		
		<%
			AttributePrincipal principal = (AttributePrincipal) request.getUserPrincipal();
			String username = "";
			//username = principal.getName();
		%>
		<%
			if (username != null && !username.equals("")) {
				//if(!username.equals("admin")){
		%>
		
			<script>
				window.location.href="/autoLogin.do";
				//alert($("#userName").val());
				//form = document.actForm;
				//form.action="/autoLogin.do";
				//form.submit();
			</script>
		<!--<input type="button" value="退出" id="logout" onclick="logout();" />-->
		<%
			//	}
			}
		%>
	</HEAD>

	<BODY class="PageBody" onload="onLoad();error();" topMargin="0" MARGINHEIGHT="0" MARGINWIDTH="0">
		<!--<img src="/images/login/login.gif" alt="" id="background" />-->
		<!-- <div style="align:center;background:white;position:absolute;width:120px;height:168px;border:1px solid silver;right:10px;top:150px;">
			<a href="<c:url value="${'/common/'}"/>download.jsp?filepath=/upload/apps/htOA_150911_real.ipa" title="本地下载ios版">
				<img src="/images/apple.jpg" border=0/>
			</a>
			<img src="/images/app-apple.png" width="120" height="120" />
		</div> -->
		
		
		
		<div id="container">
			<div id="maincontent">
				<FORM method="post" name="homeForm">
					<INPUT type="hidden" name="alertMsg">
					<INPUT type="hidden" name="wosid">
				</FORM>
		
				<FORM method="post" name="actForm" action="/adLogin.do">
					<INPUT type="hidden" name="functionName">
					<INPUT type="hidden" name="screenWidth">
					<INPUT type="hidden" name="screenHeight">
					
					<DIV id="LoginInfo">			
						<TABLE border="0" cellSpacing="0"  width="60%" id="loginTab">
							<TR>
								<!--<TD class="Subject" width="80"><IMG border="0" src="/images/login/userId.gif"></TD>-->
								<TD><INPUT class="TextField" name="userName" id="userName"></TD>
								<!--<td rowspan="2"><IMG style="CURSOR: pointer;" onclick="submitButton('LOGIN');" border="0" src="/images/login/userLogin_button.gif"></td>-->
							</TR>
							<tr>
								<td style="height:47px;"></td>
							</tr>
							<TR>
								<!--<TD class="Subject"><IMG border="0" src="/images/login/password.gif" title="初始密码为6个1，建议尽快修改密码！"></TD>-->
								<TD><INPUT class="TextField" type="password" name="password"></TD>	
							</TR>
						</table>
						<table id="btnTab">
							<tr>
								<td>
									<img src="/images/login/btn_sb.gif" style="CURSOR: pointer;" onclick="submitButton('LOGIN');" border="0" />
								</td>
								<td>
									<img src="/images/login/btn_cn.gif" style="CURSOR: pointer;" onclick="rstForm();" border="0" />
								</td>
							</tr>
						</TABLE>
					</DIV>
				</FORM>
			</div>
			
			<style>
				#appBack{
					align:center;
					vertical-align:middle;
					width:403px;
					height:170px;
					border:1px solid silver;
					position:relative;
					left:600px;
					top:136px; 
					z-index:10;
					
					/**背景半透明**/
					background:white;
					filter:alpha(opacity=70);
					-moz-opacity:0.7;
					-khtml-opacity: 0.7;
					opacity: 0.7;
					
					/**圆角**/
					-moz-border-radius: 13px;      /* Gecko browsers */
				    -webkit-border-radius: 13px;   /* Webkit browsers */
				    border-radius:13px;            /* W3C syntax */
									
				}
				#appContent{
					align:center;
					vertical-align:middle;
					width:403px;
					height:170px;
					border:1px solid silver;
					position:relative;
					left:600px;
					top:-36px; 
					z-index:11;
					
					/**圆角**/
					-moz-border-radius: 13px;      /* Gecko browsers */
				    -webkit-border-radius: 13px;   /* Webkit browsers */
				    border-radius:13px; 
				}
			</style>
			<div style="" id="appBack">
			</div>
			<div id="appContent">
				<table border=0 width="100%" height="100%" style="">
					<tr style="height:15px;line-height:13px;">
						<td></td>
						<td align=center valign="bottom">
							<!-- <img src="/images/title-android.png" width="65" height="15" title="Android版扫描下载安装" />	 -->
						</td>
					</tr>
					<tr style="height:100px;">
						<td align=center valign=top>
							<!-- <a href="<c:url value="${'/common/'}"/>download.jsp?filepath=/upload/apps/htOA_151012_real.ipa" title="本地下载ios版"> -->
							<a href="https://itunes.apple.com/cn/app/hai-tongoa/id1050553275?mt=8" target="_blank" title="到app store下载">
								<img src="/images/down-ios.png" width="150" height="42" border=0/>
							</a>
							<br/><br/>
							<!-- <a href="<c:url value="${'/common/'}"/>download.jsp?filepath=/upload/apps/htOA_151030_real.apk" title="本地下载android版"> -->
							<a href="http://shouji.baidu.com/software/11080180.html" target="_blank" title="本地下载android版">
							
								<img src="/images/down-android.png" width="150" height="42" border=0/>
							</a>
							<!-- <br/>
							<font color=black style="font-family:微软雅黑;text-align:left;font-size:11px;line-height:9px;">
								<a href="<c:url value="${'/common/'}"/>download.jsp?filepath=/upload/apps/htOA_150925_real_noBaidu.apk">Android版若无法安装，<br/>点此下载特别版。</a>
							</font> -->
						</td>
						<td align=center valign=top>
							<img src="/images/appDown.png" width="105" height="105" title="扫描下载安装" />
							<br/>
							<font color=black style="font-family:微软雅黑;text-align:left;font-size:11px;line-height:9px;">扫描后若无跳转，请选<br/>择“在浏览器中打开”</font>
						</td>
					</tr>
					<tr>
						<td></td>
						<td align=center valign=top>
							
						</td>
					</tr>
				</table>
			</div>
		</div>
		
	</BODY>
</HTML>
