<?xml version="1.0" encoding="gbk"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="html" indent="yes"/>
  
  <xsl:variable name="imgbase" select="/GENERICMODEL/STATEDATA/VAR[@name='img base uri']/@value"/>
  <xsl:variable name="base" select="/GENERICMODEL/STATEDATA/VAR[@name='base uri']/@value"/>

 
  <xsl:template match="/">
    
    <HTML>
      <HEAD>
        <TITLE>欢迎使用ExtMail</TITLE>
        <META content="MSHTML 6.00.6000.16640" name="GENERATOR"/>
        <META http-equiv="Content-Type" content="text/html; charset=utf-8"/>        
				<link rel="stylesheet" href="{$imgbase}/css/webmail.css" type="text/css"/>
      </HEAD>

			<SCRIPT language="javascript" type="text/javascript"></SCRIPT>		
				
			<BODY onload="setFocus();">
		<!--
		<DIV id="phd">
			<DIV class="lg">
				<A href="http://www.extmail.org/" target="_blank">
					<IMG alt="ExtMail" src="{$imgbase}/images/logo.gif" border="0">
				</a>
			</div>
			
			<DIV class="donate">
				<A href="http://www.extmail.org/support/zh_CN/index.html" target=_blank><B>¼¼˵֧³Լ/B></A> | 
				<IMG style="VERTICAL-ALIGN: middle" src="{$imgbase}/images/donate.png"> 
				<A href="http://www.extmail.org/donate/" target="_blank"><B style="COLOR: #5678a6">ؔԸ¾闺</B></A> 
			</DIV>
		</DIV>
		-->
	
		
			<!--mantle -->
			
<DIV class="mantle_outer">
<DIV class="ms1" id="mantle">

<!--
<DIV class=tl></DIV>
<DIV class=tr></DIV>
-->
<!--
<DIV class=fix>
	
	<DIV class=fixa>¿팙¿ɿ¿, ¸ࠐՄۉ/O<BR>¿ªԴMAIL¿ªأífõ½�.</DIV>

<BR><SPAN 
class=fixa></SPAN></DIV>
	
<DIV class=intro id=md1 style="DISPLAY: block"></DIV>-->
<DIV class="login" 
style="BORDER-RIGHT: #8a8a8a 1px solid; BORDER-TOP: #8a8a8a 1px solid; BORDER-LEFT: #8a8a8a 1px solid; BORDER-BOTTOM: #8a8a8a 1px solid">
<DIV 
style="PADDING-RIGHT: 15px; PADDING-LEFT: 15px; PADDING-BOTTOM: 15px; PADDING-TOP: 15px">
<!--
<DIV 
style="MARGIN-BOTTOM: 10px; PADDING-BOTTOM: 5px; BORDER-BOTTOM: #8a8a8a 1px solid"><SPAN 
id=my style="COLOR: #255788; HEIGHT: 10px"><B><SPAN id=logtitle>µȂ¼ԊФ</SPAN></B> 
&nbsp;<IMG src="default_files/sl.gif">
<SCRIPT type=text/javascript>menuregister(true, "my")</SCRIPT>
 </SPAN></div>
 -->
 
 <!--
<DIV id=lgextmail style="DISPLAY: block">
<TABLE cellPadding=2>
  <FORM id=login name=login onsubmit=checkType(this) 
  action=/extmail/cgi/index.cgi method=post>
  <TBODY>
  <TR>
    <TD>ԃ»§Ļ</TD>
    <TD><INPUT class=input_n name=username><BR><SPAN class=tip 
      id=tip>ˤɫźԊ¼�@"ǰµŃ�SPAN> </TD></TR>
  <TR>
    <TD>Ĝ ë</TD>
    <TD><INPUT class=input_n type=password name=password></TD></TR>
  <TR>
    <TD>ԲĻ</TD>
    <TD><INPUT class=input_n value=ext.dns0755.net name=domain></TD></TR>
  <TR>
    <TD></TD>
    <TD><INPUT type=checkbox name=bakecookie>¼Ȓ嗡ϒµœû§ĻºΓ󄹼/TD></TR>
  <TR>
    <TD></TD>
    <TD><INPUT type=checkbox CHECKED name=nosameip>IP°²ȫ&nbsp;&nbsp;<INPUT class=input_s type=submit value=µȂ½>&nbsp;&nbsp;</TD></TR></FORM></TBODY></TABLE></DIV>
    -->

<!--
<P>
<DIV 
style="PADDING-RIGHT: 10px; PADDING-LEFT: 10px; PADDING-BOTTOM: 10px; PADDING-TOP: 0px; TEXT-ALIGN: center">
<SCRIPT language=javascript>
		var signup_domain = '';
		function DoSignup() {
			var url = '/extman/cgi/signup.cgi?domain=';
			if (signup_domain) {
				url += signup_domain;
			} else {
				url += document.login.domain.value;
			}
			document.location.href = url;
		}
	</SCRIPT>
<BUTTON style="PADDING-TOP: 2px" onclick=DoSignup()>Ģ·җ¢²┊Ф</BUTTON> </DIV>
-->

<!--
<DIV 
style="MARGIN-BOTTOM: 5px; PADDING-BOTTOM: 5px; BORDER-BOTTOM: #8a8a8a 1px solid"><SPAN 
id=dmy style="COLOR: #255788"><B>欢迎使用ExtMail!</B></SPAN> </DIV>
<DIV style="TEXT-ALIGN: center"><SPAN id=dmy 
style="COLOR: #666; LINE-HEIGHT: 20px">POP3与SMTP服务器设置信息<BR>POP3 
<B>:</B>&nbsp;&nbsp;pop3.yourdomain.com<BR>SMTP 
<B>:</B>&nbsp;&nbsp;smtp.yourdomain.com<BR></SPAN></div>
-->

</DIV></DIV>

<!--
<DIV class=plus>
<UL class=ul2>
  <LI class=ico1><B>快速而可靠</B><BR>引入索引缓存(Cache)技术和高效核心，WebMail操作疾步如飞 
  <LI class=ico1><B>多语言同屏读写</B><BR>全面支持UTF8，实现同屏读写多国语言，真正做到国际邮、无乱码 
  <LI class=ico1><B>真正模板化设计</B><BR>MVC设计+高速模板引擎，实现了内容数据完全分离，轻松修改模板 
  <LI class=ico1><B>高性能I/O</B><BR>轻松应付&gt;1GB邮箱/200M附件，远强于流行的各式php webmail 
</LI></UL></DIV>
-->

<!--
<DIV class=bl></DIV>
<DIV class=br></div>
-->
</DIV></DIV><!--//mantle -->
			
		
		<!--
			<P></P>
			<DIV id=foot 
			style="CLEAR: both; BORDER-TOP: #dadada 1px solid; FONT-SIZE: 12px; MARGIN: 30px auto; WIDTH: 750px; COLOR: #8a8a8a; PADDING-TOP: 5px; FONT-FAMILY: Arial; TEXT-ALIGN: center">
				<DIV class=pb>Powered by <FONT style="FONT-WEIGHT: bold; COLOR: #000">ExtMail 
				1.0.4</FONT> ? 2004-2006 ExtMail.Org Runtime: 0 wsecs ( 0.00 usr + 0.00 
				sys)</DIV>
				<DIV class=headermenu_popup id=my_menu style="DISPLAY: none">
				<TABLE cellSpacing=0 cellPadding=4 border=0>
				  <TBODY>
				  <TR>
				    <TD class=popupmenu_option onclick=logmail();>µȂ¼ԊФ</TD></TR>
				  <TR>
				    <TD class=popupmenu_option 
				onclick=logman();>µȂ¼ԊФ¹݀뺯TD></TR></TBODY></TABLE></div>
			</DIV>
			-->
      </BODY>
    </HTML>
  </xsl:template>
</xsl:stylesheet>

