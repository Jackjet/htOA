<?xml version="1.0" encoding="utf-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="html" indent="yes" omit-xml-declaration="yes"/>
    
  <xsl:variable name="imgbase" select="/GENERICMODEL/STATEDATA/VAR[@name='img base uri']/@value"/>
  <xsl:variable name="base" select="/GENERICMODEL/STATEDATA/VAR[@name='base uri']/@value"/>

 
  <xsl:template match="/">
    
    <HTML>
      <HEAD>
        <TITLE>欢迎使用慧智 WebMail</TITLE>
        <META content="MSHTML 6.00.6000.16640" name="GENERATOR"/>
        <META http-equiv="Content-Type" content="text/html; charset=GBK"/>        
				<link rel="stylesheet" href="{$imgbase}/css/login.css" type="text/css"/>
      </HEAD>

			<SCRIPT src="{$imgbase}/js/common.js" type="text/javascript"></SCRIPT>

			<SCRIPT src="{$imgbase}/js/menu.js" type="text/javascript"></SCRIPT>
			
			<SCRIPT language="javascript">
				<xsl:comment><![CDATA[
					function setCookie (name, value, expires, path, domain, secure) {
					    var curCookie = name + "=" + escape(value) + (expires ? "; expires=" + expires : "") + (path ? "; path=" + path : "") + (domain ? "; domain=" + domain : "") + (secure ? "secure" : "");
					    document.cookie = curCookie;
					}
					
					function getCookie (name) {
					    var prefix = name + '=';
					    var c = document.cookie;
					    var nullstring = '';
					    var cookieStartIndex = c.indexOf(prefix);
					    if (cookieStartIndex == -1)
					        return nullstring;
					    var cookieEndIndex = c.indexOf(";", cookieStartIndex + prefix.length);
					    if (cookieEndIndex == -1)
					        cookieEndIndex = c.length;
					    return unescape(c.substring(cookieStartIndex + prefix.length, cookieEndIndex));
					}
					
					function deleteCookie (name, path, domain) {
					    if (getCookie(name))
					        document.cookie = name + "=" + ((path) ? "; path=" + path : "") + ((domain) ? "; domain=" + domain : "") + "; expires=Thu, 01-Jan-70 00:00:01 GMT";
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
					
					function rememberMe (f) {
					    var now = genNowTime();
					    if (f.username != null)
					       setCookie('extmail_username', f.username.value, now, '/', '', '');
					    if (f.domain != null)
					       setCookie('extmail_domain', f.domain.value, now, '/', '', '');
					}
					
					
					function forgetMe (f) {
					    deleteCookie('extmail_username', '/', '');
					    deleteCookie('extmail_domain', '/', '');
					    deleteCookie('extmail_passwd', '/', '');
					    //f.username.value = '';
					   //f.domain.value = '';
					    //f.password.value = '';
					}
					
					function checkType (f) {
					    if (f.bakecookie.checked) rememberMe(f)
						else forgetMe (f);
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
					
					function showerror(err) {alert(err);}
			]]>		
					</xsl:comment>				
			</SCRIPT>		
				
			<BODY onload="setFocus();">
			
			<!-- 
			<DIV id="phd">
				<DIV class="lg">
					<A href="http://www.extmail.org/" target="_blank">
						<IMG alt="ExtMail" src="{$imgbase}/images/logo.gif" border="0"/>
					</A>
				</DIV>
				<DIV class="donate">
					<A href="http://www.extmail.org/support/zh_CN/index.html" target="_blank"><B>技术支持</B></A>
					<IMG style="VERTICAL-ALIGN: middle" src="{$imgbase}/images/donate.png"/> 
					<A href="http://www.extmail.org/donate/" target="_blank"><B style="COLOR: #5678a6">自愿捐助</B></A> 
				</DIV>
			</DIV>
		
			<DIV class="hack"></DIV>
			
			 -->	
			 
			<DIV class="mantle_outer">
				<DIV class="ms1" id="mantle">
					<!-- 
					<DIV class="tl"></DIV>
					<DIV class="tr"></DIV>
				
				 	
					<DIV class="fix">	
						<DIV class="fixa">快速可靠, 高性能I/O<BR/>开源MAIL开足马力迈进中...</DIV>				
						<BR/>
						<SPAN class="fixa"></SPAN>
					</DIV>
					
					<DIV class="intro" id="md1" style="DISPLAY: block"></DIV> 
					 -->

					<DIV class="login" 
							style="BORDER-RIGHT: #8a8a8a 1px solid; BORDER-TOP: #8a8a8a 1px solid; BORDER-LEFT: #8a8a8a 1px solid; BORDER-BOTTOM: #8a8a8a 1px solid">
						<DIV style="PADDING-RIGHT: 15px; PADDING-LEFT: 15px; PADDING-BOTTOM: 15px; PADDING-TOP: 15px">
							
							<DIV style="MARGIN-BOTTOM: 10px; PADDING-BOTTOM: 5px; BORDER-BOTTOM: #8a8a8a 1px solid">
								<SPAN id="my" style="COLOR: #255788; HEIGHT: 10px">
									<B><SPAN id="logtitle">登录邮箱</SPAN></B> &#160;
													
									<IMG src="{$imgbase}/images/sl.gif"/>
										<!--
										<SCRIPT language="javascript">
											<xsl:comment><![CDATA[
											menuregister(true, "my")
											]]>		
											</xsl:comment>				
										</SCRIPT>									
										-->
	 							</SPAN>
	 						</DIV>
					
					
					
					
							<DIV id="lgextmail" style="DISPLAY: block">
								<TABLE cellpadding="2">
								  <FORM id="login" name="login" action="{$base}/login" method="post">
								  <TBODY>
									  <TR>
									    <TD>用户名</TD>
									    <TD>
									    		<INPUT class="input_n" name="username"/>
									     		<!--<BR/>
									    		<SPAN id="tip">输入您邮件地址"@"前的名称</SPAN>-->
									    </TD>
									  </TR>
									  <TR>
									    <TD>密 码</TD>
									    <TD><INPUT class="input_n" type="password" name="password"/></TD>
									    </TR>
									    
									  <TR>
									    <TD>域名</TD>
									    <TD>
									     <SELECT name="domain" class="testo">
			      							<xsl:for-each select="/GENERICMODEL/SYSDATA/DOMAIN">
												<OPTION value="{./NAME}"><xsl:apply-templates select="NAME"/></OPTION>
			      							</xsl:for-each>
			    						</SELECT>
									    <!--									    
									    <INPUT class="input_n" value="sctport.com.cn" name="domain"/>
									    -->
									    </TD></TR>
									  <TR>
									    <TD></TD>
									    <TD><!--<INPUT type="checkbox" name="bakecookie"/>记忆住我的用户名和域名--></TD></TR>
									     
									   
									  	<TR>
									    <TD></TD>
									    <TD>
										    <!--<INPUT type="checkbox CHECKED" name="nosameip"/>IP安全 -->
										    &#160;&#160;				
										    <INPUT class="input_s" type="submit" value="登陆"/>
										    &#160;
										     <INPUT class="input_s" type="submit" value="重填"/>
										    &#160;&#160;
									    </TD>
									    </TR> 
									    
										</TBODY>
										</FORM>
									</TABLE>
								</DIV>
						
								<!--
								<DIV id="lgextman" style="DISPLAY: none">
									<TABLE cellPadding="2">
										 <FORM action="{$base}/admin/login" method="post" name="loginForm">
										 	<INPUT type="hidden" value="valid_login" name="action"/> 
										  <TBODY>
											  <TR>
											    <TD>用户名</TD>
											    <TD><INPUT class="input_n" name="username"/></TD>
											  </TR>
											  <TR>
											    <TD>密 码</TD>
											    <TD><INPUT class="input_n" type="password" name="password"/></TD></TR>
											  <TR>
											    <TD></TD>
											    <TD><INPUT type="checkbox" name=""/>记忆住我的用户名和域名</TD></TR>
											  <TR>
											    <TD></TD>
											    <TD><INPUT class="input_s" type="submit" value="登陆"/>&#160;&#160;&#160;&#160;</TD>
											  </TR>
											</TBODY>
										</FORM>
									</TABLE>
								</DIV>
								-->
						
								<P/>
								<DIV style="PADDING-RIGHT: 10px; PADDING-LEFT: 10px; PADDING-BOTTOM: 10px; PADDING-TOP: 0px; TEXT-ALIGN: center">						
									<!-- <BUTTON style="PADDING-TOP: 2px">免费注册邮箱</BUTTON> -->
								</DIV>
						
								<DIV style="MARGIN-BOTTOM: 5px; PADDING-BOTTOM: 5px; BORDER-BOTTOM: #8a8a8a 1px solid">
									<SPAN id="dmy" style="COLOR: #255788"><B>欢迎使用慧智Web-Mail!</B></SPAN> 
								</DIV>
						
								<DIV style="TEXT-ALIGN: center">
									<SPAN id="dmy" style="COLOR: #666; LINE-HEIGHT: 20px">
										POP3与SMTP服务器设置信息
										<BR/>POP3 <B>:</B>&#160;&#160;pop3.yourdomain.com
										<BR/>SMTP <B>:</B>&#160;&#160;smtp.yourdomain.com
									</SPAN>
								</DIV>
							</DIV>
						</DIV> 
						
						<!-- 
						<DIV class="plus">
							<UL class="ul2">
							  <LI class="ico1"><B>快速而可靠</B><BR/>引入索引缓存(Cache)技术和高效核心，WebMail操作疾步如飞</LI> 
							  <LI class="ico1"><B>多语言同屏读写</B><BR/>全面支持UTF8，实现同屏读写多国语言，真正做到国际邮、无乱码</LI> 
							  <LI class="ico1"><B>真正模板化设计</B><BR/>MVC设计+高速模板引擎，实现了内容数据完全分离，轻松修改模板</LI> 
							  <LI class="ico1"><B>高性能I/O</B><BR/>轻松应付&gt;1GB邮箱/200M附件，远强于流行的各式php webmail</LI>							
							</UL>
						</DIV>
																	
						<DIV class="bl"></DIV>
						<DIV class="br"></DIV>
						 -->
												
				</DIV>
			</DIV>
			
			
			<P></P>
			<DIV id="foot" style="CLEAR: both; BORDER-TOP: #dadada 1px solid; FONT-SIZE: 12px; MARGIN: 30px auto; WIDTH: 750px; COLOR: #8a8a8a; PADDING-TOP: 5px; FONT-FAMILY: Arial; TEXT-ALIGN: center">
				<!--
				<DIV class="pb">
					Powered by 
					<FONT style="FONT-WEIGHT: bold; COLOR: #000">ExtMail 1.0.4</FONT> ? 2004-2006 ExtMail.Org Runtime: 0 wsecs ( 0.00 usr + 0.00 sys)
				</DIV>
				  -->
				<DIV class="headermenu_popup" id="my_menu" style="DISPLAY: none">
					<TABLE cellSpacing="0" cellPadding="4" border="0">
					  <TBODY>
					  <TR>
					    <TD class="popupmenu_option" onclick="logmail();">登录邮箱</TD></TR>
					  <TR>
					    <TD class="popupmenu_option" onclick="logman();">登录邮箱管理</TD>
					  </TR>
					  </TBODY>
					</TABLE>
				</DIV>
			</DIV>
			
			<SCRIPT language="javascript">
					<xsl:comment><![CDATA[
			
					/**
					function logmail()
					{
						$("lgextmail").style.display="block";
						$("lgextman").style.display="none";
						$("logtitle").innerHTML = "登录邮箱";
					}
					
					
					function logman()
					{
						$("lgextmail").style.display="none";
						$("lgextman").style.display="block";
						$("logtitle").innerHTML = "登录邮箱管理";
					}*/
					
					function domain() {
						var url = document.location.href;
						url = url.replace(/^(http:\/\/|https:\/\/)*([a-zA-Z0-9-_\.=]+):*.*/, "$2");
						var res = url.match(/^(mail|webmail|www|freemail|)\.(.*)/);
						if (res) { url = res[2] }
						return url;
					}
					
					var f = document.login;
					if (f.username != null && getCookie("extmail_username"))
					    f.username.value = getCookie("extmail_username");
					if (f.domain != null && getCookie("extmail_domain"))
					    f.domain.value = getCookie("extmail_domain");
					if (f.password != null && getCookie("extmail_passwd"))
					    f.password.value = getCookie("extmail_passwd");
					if(getCookie("extmail_username") && getCookie("extmail_domain")) {
					    f.bakecookie.checked = true;
					}else {
					    /* forget me completely */
					    //f.bakecookie.checked = false;
					}
					
					if (f.domain != null && !getCookie("extmail_domain") && f.domain.value =='') {
					    f.domain.value = domain();
					}
					
					]]>		
					</xsl:comment>				
				</SCRIPT>									
			
		
      </BODY>
    </HTML>
  </xsl:template>
</xsl:stylesheet>

