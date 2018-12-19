<?xml version="1.0" encoding="UTF-8"?>


<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="html" indent="yes"/>

    <xsl:variable name="imgbase" select="/GENERIC/MODEL/STATEDATA/VAR[@name='img base uri']/@value"/>
    <xsl:variable name="base" select="/GENERICMODEL/STATEDATA/VAR[@name='base uri']/@value"/>
    <xsl:variable name="session-id" select="/GENERICMODEL/STATEDATA/VAR[@name='session id']/@value"/>
  
    <xsl:template match="/">

    <HTML>
      <HEAD>
        <TITLE>WebMail Administration Interface</TITLE>
	<META CONTENT="AUTHOR" VALUE="Sebastian Schaffert"/>
      </HEAD>

      <FRAMESET COLS="150,*" border="0">
	<FRAME NAME="Content"  SRC="{$base}/admin/navigation?session-id={$session-id}" scrolling="auto"/>
	<FRAME NAME="Main"   SRC="{$base}/admin/system?session-id={$session-id}" scrolling="auto"/>
      </FRAMESET>    
    </HTML>

  </xsl:template>
</xsl:stylesheet>
