<?xml version="1.0" encoding="UTF-8"?>


<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  	<xsl:output method="html" indent="yes"/>

    <xsl:variable name="imgbase" select="/USERMODEL/STATEDATA/VAR[@name='img base uri']/@value"/>
    <xsl:variable name="base" select="/USERMODEL/STATEDATA/VAR[@name='base uri']/@value"/>
    <xsl:variable name="session-id" select="/USERMODEL/STATEDATA/VAR[@name='session id']/@value"/>
    
    <!-- 取名称为INBOX的文件夹Id  --> 
     	<xsl:variable name="smallcase" select="'abcdefghijklmnopqrstuvwxyz'"/>
   		<xsl:variable name="uppercase" select="'ABCDEFGHIJKLMNOPQRSTUVWXYZ'"/>
    	<xsl:variable name="inboxId" select="/USERMODEL/MAILHOST_MODEL/FOLDER[translate(@name, $smallcase, $uppercase)='INBOX']/@id"/>
    <!-- 使用百分比  -->
    <xsl:variable name="usagequato" select="/USERMODEL/MAILHOST_MODEL/quota/resource[@name='STORAGE']/@usagepct"/>
         
    <xsl:include href="head.xsl"/>
    <xsl:include href="left.xsl"/>
    <xsl:include href="index.xsl"/>
   	<xsl:include href="bottom.xsl"/>
   	
   	
   
    <xsl:template match="/">
	
    <HTML>
      <HEAD>
        <TITLE>WebMail Mailbox for <xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/></TITLE>
				<META CONTENT="AUTHOR" VALUE="kwchina"/>			
				<LINK media="screen" href="{$imgbase}/css/newstyle.css" type="text/css" rel="stylesheet"/>
				<LINK href="{$imgbase}/css/style.css" rel="stylesheet"/>
				<LINK href="{$imgbase}/css/style(1).css" rel="stylesheet"/>
				<LINK href="{$imgbase}/css/style(2).css" rel="stylesheet"/>
      </HEAD>
       
      <SCRIPT src="{$imgbase}/js/common.js" type="text/javascript"></SCRIPT>
      
      <SCRIPT language="javascript">
				<xsl:comment><![CDATA[
				var currentNav = "nav_home";
				]]>		
				</xsl:comment>				
			</SCRIPT>
       
			<!--
      <FRAMESET ROWS="65,*" border="0">
				<FRAME NAME="Title" SRC="{$base}/title?session-id={$session-id}" scrolling="no"/>
				<FRAMESET COLS="{/USERMODEL/USERDATA/INTVAR[@name='icon size']/@value + 46},*" border="0">
				  <FRAME NAME="Content" SRC="{$base}/content?session-id={$session-id}" scrolling="auto"/>
				  <FRAME NAME="Main" SRC="{$base}/mailbox?session-id={$session-id}&amp;force-refresh=1" scrolling="auto"/>
				</FRAMESET>
      </FRAMESET>
    </HTML>
     -->
     <BODY>    
      <xsl:call-template name="topHeader"/> 
      <xsl:call-template name="normalHeader"/>
      
      <DIV id="Main">
				<TABLE cellspacing="0" cellpadding="0" width="100%">
				  <TBODY>
				  	<TR>
				  
						<!-- Left Part -->
						<xsl:call-template name="mailLeft"/>
						<!-- Left Part End -->
						
						<TD class="td1px"></TD>
						
						<!-- Main Content Part -->
						<xsl:call-template name="index"/>
						<!--Main Content Part End -->
						
						<!-- Right Part -->
						<!--
						<TD vAlign=top width=230>
							<DIV class=panelout id=coolweather_div>
								<DIV class=paneltit>&nbsp;天气信息</DIV>
								<DIV class=panelin id=coolweather_container></DIV>
								<SCRIPT type=text/javascript>
									//coolweather_iconspath = '/extmail/plugins/coolweather/icons/';
									coolweather_init();
								</SCRIPT>
							</DIV>
						</TD>
						-->
						<TD width="10"></TD>
					</TR>
				</TBODY>
				</TABLE>
				</DIV>
 
    	<xsl:call-template name="Bottom"/>
    	
		 </BODY>
    </HTML>

  </xsl:template>
</xsl:stylesheet>
