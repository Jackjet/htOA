<?xml version="1.0" encoding="gb2312"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
		
		<xsl:template name="userLeft">		
			
			<TD id="MainLeft" vAlign="top" width="170">
	      <DIV id="DivSysFolder" style="CLEAR: both; MARGIN-TOP: 0px">
		      <UL id="">
		        <LI class="fdnav"><A href="{$base}/usernormal?session-id={$session-id}">常规设置</A></LI>
		        <LI class="fdnav"><A href="#">过滤器设置</A></LI>
		        <LI class="fdnav"><A href="#">自动回复设置</A> </LI>
		        <LI class="fdnav"><A href="#">POP3取信帐号</A> </LI>
		        <LI class="fdnav"><A href="#">白名单</A> </LI>
		        <LI class="fdnav"><A href="#">黑名单</A> </LI>
		      </UL>
	      </DIV>
		</TD>
		</xsl:template>
						
							
</xsl:stylesheet>