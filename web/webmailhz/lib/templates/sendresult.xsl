<?xml version="1.0" encoding="gb2312"?>


<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  	<xsl:output method="html" indent="yes"/>

    <xsl:variable name="imgbase" select="/USERMODEL/STATEDATA/VAR[@name='img base uri']/@value"/>
    <xsl:variable name="base" select="/USERMODEL/STATEDATA/VAR[@name='base uri']/@value"/>
    <xsl:variable name="session-id" select="/USERMODEL/STATEDATA/VAR[@name='session id']/@value"/>
     <!-- 取名称为INBOX的文件夹Id --> 
    <xsl:variable name="inboxId" select="/USERMODEL/MAILHOST_MODEL/FOLDER[@name='INBOX']/@id"/>
    <!-- 当前-->      
		<xsl:variable name="work" select="/USERMODEL/WORK/MESSAGE[position()=1]"/>   
		         
    <xsl:include href="head.xsl"/>
    <xsl:include href="left.xsl"/>   
   	<xsl:include href="bottom.xsl"/>
   	
   	
   
  <xsl:template match="/">		
    <HTML>
      <HEAD>
        <TITLE>WebMail Mailbox for <xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/></TITLE>
				<META CONTENT="AUTHOR" VALUE="kwchina"/>			
				<LINK media="screen" href="{$imgbase}/css/newstyle.css" type="text/css" rel="stylesheet"/>
				<LINK media="screen" href="{$imgbase}/css/text-overflow.css" type="text/css" rel="stylesheet"/>			
      </HEAD>
			
			<SCRIPT src="{$imgbase}/js/common.js" type="text/javascript"></SCRIPT>
			<SCRIPT src="{$imgbase}/js/moz-text-overflow.js" type="text/javascript"></SCRIPT>
			<SCRIPT language="javascript">
				<xsl:comment><![CDATA[
				var currentNav = "nav_mail";
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
						<TD class="tdmain" valign="top">
						
								<TABLE width="100%" border="0" cellspacing="2" cellpadding="4">
							  <TR>
							    <TD height="22" class="testoNero">
							      <IMG SRC="{$imgbase}/images/icona_composer.gif" BORDER="0" align="absmiddle"/>Mail delivery results
							    </TD>
							  </TR>
							  <TR>
							    <TD bgcolor="#697791" height="22" class="testoBianco">Date: <xsl:value-of select="/USERMODEL/STATEDATA/VAR[@name='date']/@value"/></TD>
							  </TR>
							  <TR>
							    <TD class="testoGrande" bgcolor="#A6B1C0">Sending message<BR/>
							      Subject: <SPAN class="testoNero"><xsl:value-of select="$work/HEADER/SUBJECT"/></SPAN><BR/>
							      To: <SPAN class="testoNero"><xsl:value-of select="$work/HEADER/TO"/></SPAN><BR/>
							      Date: <SPAN class="testoNero"><xsl:value-of select="/USERMODEL/STATEDATA/VAR[@name='date']/@value"/></SPAN>
							    </TD>
							  </TR>
							  <TR>
							    <TD class="testoGrande" bgcolor="#D1D7E7">Send status:
								<SPAN class="testoNero"><xsl:value-of select="/USERMODEL/STATEDATA/VAR[@name='send status']/@value"/>
								</SPAN>
							    </TD>
							</TR>
							<TR>
						         <TD class="testoGrande" bgcolor="#E2E6F0">
							  <!-- Only show the section for valid addresses if there actually were any -->
							  <xsl:if test="/USERMODEL/STATEDATA/VAR[@name='valid sent addresses']/@value != ''">
							       Delivered to addresses: <SPAN class="testoNero">valid&#160;&#160;<xsl:value-of select="/USERMODEL/STATEDATA/VAR[@name='valid sent addresses']/@value"/></SPAN>
							  </xsl:if>
							 </TD>
							</TR>
							<TR>
							  <TD class="testoGrande" bgcolor="#E2E6F0">  
							  <!-- Only show the section for invalid addresses if there actually were any -->
							  <xsl:if test="/USERMODEL/STATEDATA/VAR[@name='valid unsent addresses']/@value != '' or /USERMODEL/STATEDATA/VAR[@name='invalid unsent addresses']/@value != ''">
							    Not delivered to addresses: 
							      <SPAN class="testoNero">
								<xsl:if test="/USERMODEL/STATEDATA/VAR[@name='valid unsent addresses']/@value != ''">valid&#160;&#160;
						<xsl:value-of select="/USERMODEL/STATEDATA/VAR[@name='valid unsent addresses']/@value"/>
							        </xsl:if></SPAN>
							    <SPAN class="testoNero"><xsl:if test="/USERMODEL/STATEDATA/VAR[@name='invalid unsent addresses']/@value != ''">
							      invalid&#160;&#160;<xsl:value-of select="/USERMODEL/STATEDATA/VAR[@name='invalid unsent addresses']/@value"/> 
							    </xsl:if></SPAN> 
							  </xsl:if>
							  </TD>
							</TR>
							<TR>
							  <TD bgcolor="#697791">   
							<TABLE border="0" cellpadding="0" cellspacing="0"><TR><TD>
							<A HREF="{$base}/compose?session-id={$session-id}&amp;continue=1"><IMG SRC="{$imgbase}/images/back.gif" BORDER="0"/></A></TD><TD><A HREF="{$base}/compose?session-id={$session-id}&amp;continue=1"><SPAN class="testoBianco"> Back to compose dialog ...</SPAN></A></TD></TR></TABLE>
							  </TD>
							</TR>
						 </TABLE>
							
						 <BR/>
						 <BR/>
						 <BR/>
						 <BR/>
								
						</TD>
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
