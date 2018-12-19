<?xml version="1.0" encoding="gb2312"?>


<!-- This template is part of the German translation -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="html" indent="yes"/>
  
    <xsl:template match="/">
    
    <HTML>
      <HEAD>
        <TITLE>Session Time Out</TITLE>
		<META CONTENT="AUTHOR" VALUE="Sebastian Schaffert"/>
		<META CONTENT="GENERATOR" VALUE="JWebMail 0.7 XSL"/>
		<link rel="stylesheet" href="../../webmail.css"/>
      </HEAD>
      
      <BODY bgcolor="#B5C1CF" topmargin="5" leftmargin="0" marginwidth="0" marginheight="5">
		<TABLE WIDTH="100%">
	  		<TR bgcolor="#A6B1C0">
	    		<TD COLSPAN="2" align="center" class="testoGrande">登录超时</TD>
	  		</TR>
	  		<TR>
	    		<TD class="testoNero"><SPAN class="testoGrande">Error message</SPAN></TD>
	    		<TD class="testoNero">
	      			<P class="testoMesg"></P>
	    		</TD>
	  		</TR>
	  		<TR>
	    		<TD COLSPAN="2" ALIGN="center" class="testoNero">
	      			<TABLE bgcolor="#E2E6F0" WIDTH="80%" BORDER="1">
						<TR>
		  					<TD>
		  						<SPAN class="testoGrande">Stack Trace</SPAN><BR/>
		    					<PRE>
		      					<P class="testoMesg">
										登录超时,请<a href="/webmailhz/WebMail">重新登录</a>
								</P>
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
