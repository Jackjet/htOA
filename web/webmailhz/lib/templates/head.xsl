<?xml version="1.0" encoding="gb2312"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  
     
  <xsl:template name="topHeader">
		<DIV id="TopBan">
			<TABLE height="50" cellSpacing="0" cellPadding="0" width="100%" border="0">
				<TBODY>
					<TR>
						<TD style="PADDING-LEFT: 10px"><IMG src="{$imgbase}/images/logo.gif" border="0"/></TD>
						<TD vAlign="top" align="right">
							<SPAN id="tlogout"><xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/>:&#160; 
							<A href="{$base}/logout?session-id={$session-id}">�˳�</A> <!--| <A href="#" target="_blank">����Extmail</A>--> | <A href="#" target="_blank">����</A>
							</SPAN> 
						</TD>
					</TR>
				</TBODY>
			</TABLE>
		</DIV>		
	</xsl:template>  
	
		
	<xsl:template name="normalHeader">
		<DIV id="TopNav">
			<DIV class="h5px"></DIV>
			<DIV id="Tmenu">
				<UL id="navmenu">
			  	<LI class="tl2" id="nav_home">
						<A href="{$base}/login">��ҳ</A>  
			  	</LI>  			  
			  	<LI class="tl2" id="nav_mail">
			  		<A HREF="{$base}/folder/list?session-id={$session-id}&amp;folder-id={$inboxId}&amp;part=1">�ռ���</A>						
				  </LI>
				  <LI class="tl2" id="nav_compose">
						<A HREF="{$base}/compose?session-id={$session-id}">д�ʼ�</A> 
				  </LI>
				  <LI class="tl2" id="nav_abook">
						<A href="{$base}/address/list?session-id={$session-id}">ͨѶ¼</A> 
				  </LI>
				  <LI class="tl2" id="nav_mbox">
						<A class="topnav" href="#">�������</A> 
				  </LI>
				  <!--
				  <LI class="tl2" id="nav_ndisk">
						<A href="netdisk.htm">�������</A> 
				  </LI>
				  -->
				  <LI class="tl2" id="nav_option">
						<A class="topnav" href="{$base}/usernormal?session-id={$session-id}">ƫ������</A> 
				  </LI>
				</UL>
			</DIV>
			
			<SCRIPT language="javascript">
				<xsl:comment><![CDATA[     
				function HightLightNav(id)	{
					//alert(id);
					$(id).className = "tl1";
				}				    
				]]>
				
				HightLightNav(currentNav);
				</xsl:comment>				
			</SCRIPT>
	
		</DIV>
		<DIV></DIV>
	</xsl:template>
	
	
</xsl:stylesheet>