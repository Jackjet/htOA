<?xml version="1.0" encoding="gb2312"?>


<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="html" indent="yes"/>
  
  <xsl:variable name="imgbase" select="/GENERICMODEL/STATEDATA/VAR[@name='img base uri']/@value"/>
  <xsl:variable name="base" select="/GENERICMODEL/STATEDATA/VAR[@name='base uri']/@value"/>

  <xsl:template match="/">
    
    <HTML>
      <HEAD>
        <TITLE>WebMail Administrator Login Screen</TITLE>
	<META CONTENT="AUTHOR" VALUE="Sebastian Schaffert"/>
      </HEAD>
      
      <BODY bgcolor="#ffffff">
	<TABLE WIDTH="100%">
	  <TR>
	    <TD COLSPAN="3" ALIGN="CENTER" HEIGHT="70">
	    </TD>
	  </TR>
	  <TR>
	    <TD WIDTH="20%" VALIGN="TOP"><IMG SRC="{$imgbase}/images/java_powered.png" ALT="Java powered"/></TD>
	    <TD WIDTH="60%" ALIGN="CENTER">
	      <FORM ACTION="{$base}/admin/login" METHOD="POST" NAME="loginForm">
		<TABLE CELLSPACING="0" CELLPADDING="20" BORDER="4" bgcolor="#ff0000">
		  <TR>
		    <TD ALIGN="CENTER">
		      <TABLE CELLSPACING="0" CELLPADDING="10" BORDER="0" bgcolor="#ff0000">
			<TR>
			  <TD COLSPAN="2" ALIGN="CENTER">
			    <IMG SRC="{$imgbase}/images/login_title.png" ALT="WebMail login"/></TD>
			</TR>
			<TR>
			  <TD WIDTH="50%" ALIGN="RIGHT"><STRONG>Login:</STRONG></TD>
			  <TD WIDTH="50%">Administrator</TD>
			</TR>
			<TR>
			  <TD WIDTH="50%" ALIGN="RIGHT"><STRONG>Password:</STRONG></TD>
			  <TD WIDTH="50%"><INPUT TYPE="password" NAME="password" SIZE="15"/></TD>
			</TR>
			<TR>
			  <TD ALIGN="CENTER"><INPUT TYPE="submit" value="Login"/></TD>
			  <TD ALIGN="CENTER"><INPUT TYPE="reset" value="Reset"/></TD>
			</TR>
		      </TABLE>
		    </TD>
		  </TR>
		</TABLE>
	      </FORM>
	    </TD>
	    <TD WIDTH="20%">
	    </TD>
	  </TR>
	  <xsl:if test="/GENERICMODEL/STATEDATA/VAR[@name='invalid password']/@value = 'yes'">
	    <!-- START invalid pass -->
	    <TR>
	      <TD COLSPAN="3" ALIGN="CENTER">
		<FONT COLOR="red" SIZE="+1">
		  Login incorrect. The passwords did not match or the name/password field was empty! Attempt will be logged.
		</FONT>
	      </TD>
	    </TR>
	    <!-- END invalid pass -->
	  </xsl:if>
	</TABLE>
      </BODY>
    </HTML>
  </xsl:template>
</xsl:stylesheet>
