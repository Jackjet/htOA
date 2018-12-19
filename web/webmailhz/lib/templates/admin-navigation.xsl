<?xml version="1.0" encoding="UTF-8"?>


<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="html" indent="yes"/>

    <xsl:variable name="imgbase" select="/GENERICMODEL/STATEDATA/VAR[@name='img base uri']/@value"/>
    <xsl:variable name="base" select="/GENERICMODEL/STATEDATA/VAR[@name='base uri']/@value"/>
    <xsl:variable name="session-id" select="/GENERICMODEL/STATEDATA/VAR[@name='session id']/@value"/>
  
    <xsl:template match="/">

    <HTML>
      <HEAD>
        <TITLE>WebMail Administration Interface: Navigation</TITLE>
		<META CONTENT="AUTHOR" VALUE="Sebastian Schaffert"/>
      </HEAD>
	
      <BODY BGCOLOR="lightblue">
		<TABLE WIDTH="100%">
	  	<TR>
	    	<TD height="30">
	      		<A HREF="{$base}/admin/system?session-id={$session-id}" TARGET="Main"><FONT SIZE="-1" COLOR="red"><B>System Configuration</B></FONT></A>
	    	</TD>
	 	 </TR>
	  
	  <TR>
	    <TD height="30">
	      <A HREF="{$base}/admin/control?session-id={$session-id}" TARGET="Main"><FONT SIZE="-1" COLOR="red"><B>System Control</B></FONT></A>
	    </TD>
	  </TR>
	  
	  <TR>
	    <TD height="30">
	      <A HREF="{$base}/admin/domain?session-id={$session-id}" TARGET="Main"><FONT SIZE="-1" COLOR="red"><B>Virtual Domain Configuration</B></FONT></A>
	    </TD>
	  </TR>
	  
	  <!--
	  <TR>
	    <TD height="30">
	      <A HREF="{$base}/admin/user?session-id={$session-id}" TARGET="Main"><FONT SIZE="-1" COLOR="red"><B>User Configuration</B></FONT></A>
	    </TD>
	  </TR>
	  -->
	  
	  <!--
	  <TR>
	    <TD height="30">
	      <A HREF="{$base}/admin/help?session-id={$session-id}" TARGET="Main"><FONT SIZE="-1" COLOR="red"><B>Help</B></FONT></A>
	      <FONT SIZE="-1" COLOR="red">Help (not yet implemented)</FONT>
	    </TD>
	  </TR>
	  -->
	  
	  <TR>
	    <TD height="30">
	      <A HREF="{$base}/admin/logout?session-id={$session-id}" TARGET="_top"><FONT SIZE="-1" COLOR="red"><B>Logout</B></FONT></A>
	    </TD>
	  </TR>
	</TABLE>
      </BODY>
	
    </HTML>

  </xsl:template>
</xsl:stylesheet>
