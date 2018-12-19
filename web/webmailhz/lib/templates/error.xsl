<?xml version="1.0" encoding="gb2312"?>


<!-- This template is part of the German translation -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="html" indent="yes"/>

    <xsl:variable name="imgbase" select="/USERMODEL/STATEDATA/VAR[@name='img base uri']/@value"/>
    <xsl:variable name="base" select="/USERMODEL/STATEDATA/VAR[@name='base uri']/@value"/>
    <xsl:variable name="session-id" select="/USERMODEL/STATEDATA/VAR[@name='session id']/@value"/>
  
    <xsl:template match="/">
    
    <HTML>
      <HEAD>
        <TITLE>WebMail Error Reporting</TITLE>
		<META CONTENT="AUTHOR" VALUE="Sebastian Schaffert"/>
		<META CONTENT="GENERATOR" VALUE="JWebMail 0.7 XSL"/>
		<LINK media="screen" href="{$imgbase}/css/newstyle.css" type="text/css" rel="stylesheet"/>
		<LINK media="screen" href="{$imgbase}/css/text-overflow.css" type="text/css" rel="stylesheet"/>	
      </HEAD>
      
      <BODY bgcolor="#B5C1CF" topmargin="5" leftmargin="0" marginwidth="0" marginheight="5">
		<TABLE WIDTH="100%">
	  		<TR bgcolor="#A6B1C0">
	    		<TD COLSPAN="2" align="center" class="testoGrande">An error ocurred</TD>
	  		</TR>
	  		<TR>
	    		<TD class="testoNero"><SPAN class="testoGrande">Error message</SPAN></TD>
	    		<TD class="testoNero">
	      			<P class="testoMesg"><xsl:apply-templates select="//STATEDATA/EXCEPTION/EX_MESSAGE"/></P>
	    		</TD>
	  		</TR>
	  		<TR>
	    		<TD COLSPAN="2" ALIGN="center" class="testoNero">
	      			<TABLE bgcolor="#E2E6F0" WIDTH="80%" BORDER="1">
						<TR>
		  					<TD>
		  						<SPAN class="testoGrande">Stack Trace</SPAN><BR/>
		    					<PRE>
		      					<P class="testoMesg"><xsl:apply-templates select="//STATEDATA/EXCEPTION/EX_STACKTRACE"/></P>
		    					</PRE>
		  					</TD>
						</TR>
	      			</TABLE>
	    		</TD>
	  		</TR>
	  		<TR bgcolor="#A6B1C0">
	   			<TD COLSPAN="2" align="center" class="testoGrande">!</TD>
	  		</TR>
		</TABLE>
      </BODY>
    </HTML>
  </xsl:template>
  

</xsl:stylesheet>
