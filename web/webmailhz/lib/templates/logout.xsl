<?xml version="1.0" encoding="gb2312"?>


<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="html" indent="yes"/>

   <xsl:variable name="imgbase" select="/USERMODEL/STATEDATA/VAR[@name='img base uri']/@value"/>
    <xsl:variable name="base" select="/USERMODEL/STATEDATA/VAR[@name='base uri']/@value"/>

  
    <xsl:template match="/">

    <HTML>
      <HEAD>
        <TITLE>WebMail Mailbox for <xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/></TITLE>
				<META CONTENT="AUTHOR" VALUE="Kwchina.com"/>
				<META HTTP-EQUIV="REFRESH" CONTENT="5;URL={/USERMODEL/STATEDATA/VAR[@name='base uri']/@value}/"/>
				<link rel="stylesheet" href="{$imgbase}/css/webmail.css"/> 
      </HEAD>

      <BODY bgcolor="#ffffff">
			<P align="center">&#160;</P>
			<P align="center">&#160;</P>
			<CENTER>
			<TABLE width="402" border="0" cellspacing="0" cellpadding="1" bgcolor="#000000" height="252" align="center">
			  <TR>
			    <TD align="center" bgcolor="#000000" valign="middle">
			      <TABLE width="400" border="0" cellspacing="0" cellpadding="0" height="250">
				<TR>
				  <TD colspan="2" width="400" height="50" bgcolor="#7B889F">&#160;
				  </TD>
				</TR>
				<TR>
				  <TD rowspan="2" width="85" align="center" bgcolor="#D3D8DE" height="110">
				    <!--<IMG SRC="{$imgbase}/images/logobibop.gif" ALT="Logo BiBop"/>-->
				  </TD>
				  <TD align="center" height="40" bgcolor="#FFFFFF" width="315">
				    <IMG SRC="{$imgbase}/images/logo.gif"/>
				  </TD>
				</TR>
				<TR>
				  <TD align="center" bgcolor="#FFFFFF" width="315" class="testo">
				    <BR/><SPAN class="testoGrande">感谢使用慧智 WebMail!</SPAN><BR/><BR/>
				    系统正在登出 <xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/>...<BR/><BR/>
				  </TD>
				</TR>
				<TR>
				  <TD colspan="2" align="center" height="50" bgcolor="#7B889F" width="400" class="testo">
				    正关闭您的登录,并把您的配置写入到文件,请等待.<BR/>
				    如果几秒钟之后您没有看到 <SPAN class="bold">登录页面</SPAN>, 请点击
				    <A HREF="{/USERMODEL/STATEDATA/VAR[@name='base uri']/@value}/"><SPAN class="testoScuro">这里</SPAN></A>.
				  </TD>
				</TR>
				<TR>
				  <TD colspan="2" width="400" class="testoBianco" bgcolor="#394864" height="35" align="center">
				    <SPAN class="bold"> WebMail </SPAN>is based on<BR/>
		                    WebMail is &#169; 2008/2009 by HuiZhi<BR/>
				  </TD>
				</TR>
	       </TABLE>
	     </TD>
	   </TR>
	 </TABLE>
	 </CENTER>
      </BODY>

    </HTML>
  </xsl:template>

  
</xsl:stylesheet>
