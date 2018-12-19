<?xml version="1.0" encoding="gb2312"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
		
		<xsl:template name="mailLeft">		
			
			<TD id="MainLeft" valign="top" width="170">
					<SCRIPT language="javascript">
							<xsl:comment><![CDATA[
								function CheckMail(base,inboxId,sessionId) {
									//var url = '/extmail/cgi/folders.cgi?__mode=folders_list';
									//url += '&chkpop=true&sid=e9ae4a349a786dce207b980606df95f2&screen=welcome.html';
									//window.location = url;
									
									var url = base + '/folder/list?session-id=' + sessionId + '&folder-id=' + inboxId + '&part=1';
									alert(url);
									window.location = url;
								}
						
								function Compose() {
									window.location.href="{$base}/compose?session-id={$session-id}";
								}
						
								function LM_OCTRL(CLSNAME)	{
									var obj = document.getElementById(CLSNAME);
									if (obj.style.display=="none")
									{
										obj.style.display = "block";
										document.getElementById(CLSNAME + "_CTRLIMG").src = "/extmail/default/images/lo.gif";
									}
									else
									{
										obj.style.display = "none";
										document.getElementById(CLSNAME + "_CTRLIMG").src = "/extmail/default/images/lc.gif";
									}
								}
								]]>		
								</xsl:comment>				
							</SCRIPT>
					
							<DIV style="TEXT-ALIGN: center">
								<INPUT class="lnbtn" id="ckbtn" onclick="CheckMail('{$base}','{$inboxId}','{$session-id}')" type="button" value="查信"/>&#160;  
								<INPUT class="lnbtn" id="cmbtn" onclick="Compose()" type="button" value="写邮件"/> 
							</DIV>
							
							<DIV id="DivSysFolder" style="CLEAR: both; MARGIN-TOP: 0px">
								<DIV style="MARGIN-TOP: 5px; DISPLAY: block; HEIGHT: 20px">
									<DIV style="PADDING-LEFT: 5px; FONT-WEIGHT: bold; FLOAT: left; CURSOR: pointer; COLOR: #3b107b; PADDING-TOP: 2px" 
								  onclick="LM_OCTRL('LM_SYSFD');">
										<SPAN><IMG id="LM_SYSFD_CTRLIMG" src="{$imgbase}/images/lo.gif"/></SPAN> 系统邮件夹
									</DIV>
									<DIV class="btnlm"></DIV>
								</DIV>
								<DIV id="LM_SYSFD">
									<UL id="LM_SYSFD_UL">
										
										<xsl:if test="/USERMODEL/MAILHOST_MODEL/FOLDER/@name='Inbox' or /USERMODEL/MAILHOST_MODEL/FOLDER/@name='INBOX'">
											<xsl:apply-templates select="/USERMODEL/MAILHOST_MODEL/FOLDER"/>
										</xsl:if>
											
									
										<xsl:for-each select="/USERMODEL/MAILHOST_MODEL/FOLDER/FOLDER">
											<xsl:apply-templates select="."/>
										</xsl:for-each>
										<!-- 
										<xsl:apply-templates select="/USERMODEL/MAILHOST_MODEL/FOLDER[@name='Inbox' or @name='INBOX']"/>
										<xsl:apply-templates select="/USERMODEL/MAILHOST_MODEL/FOLDER/FOLDER[@name='Sent']"/>
										<xsl:apply-templates select="/USERMODEL/MAILHOST_MODEL/FOLDER/FOLDER[@name='Drafts']"/>
										<xsl:apply-templates select="/USERMODEL/MAILHOST_MODEL/FOLDER/FOLDER[@name='Trash']"/>
										<xsl:apply-templates select="/USERMODEL/MAILHOST_MODEL/FOLDER/FOLDER[@name='Junk']"/>
										 -->
									</UL>
								</DIV>
							</DIV>
									
							<DIV id="DivSysFolder" style="CLEAR: both; MARGIN-TOP: 0px">
								<DIV style="MARGIN-TOP: 5px; DISPLAY: block; HEIGHT: 20px">
									<DIV style="PADDING-LEFT: 5px; FONT-WEIGHT: bold; FONT-SIZE: 12px; FLOAT: left; CURSOR: pointer; COLOR: #3b107b; PADDING-TOP: 2px" onClick="LM_OCTRL('LM_USRFD');">
										<SPAN><IMG id="LM_USRFD_CTRLIMG" src="{$imgbase}/images/lo.gif"/></SPAN> 我的文件夹 
									</DIV>
									<DIV class="btnlm" style="PADDING-RIGHT: 10px; FONT-WEIGHT: normal; FONT-SIZE: 12px; FLOAT: right; COLOR: #3b107b; PADDING-TOP: 2px">
										<!--<A href="#">[管理]</A>-->
									</DIV>
								</DIV>
								
								<DIV id="LM_USRFD">
									<UL id="LM_SYSFD_UL"></UL>
								</DIV>
							</DIV>
			</TD>
		</xsl:template>
		
		
		<xsl:template match="FOLDER">
			<LI class="fdnav"><A HREF="{$base}/folder/list?session-id={$session-id}&amp;folder-id={@id}&amp;part=1">&#160; 
			<SPAN class="lfld">
			<xsl:choose>
		        <xsl:when test="@name='Inbox' or @name='INBOX'">
		          收件箱
		        </xsl:when>
		        <xsl:when test="@name='Drafts'">
		          草稿箱
		        </xsl:when>
		        <xsl:when test="@name='Junk'">
		          垃圾邮件
		        </xsl:when>
		        <xsl:when test="@name='Sent'">
		          发件箱
		        </xsl:when>
		        <xsl:when test="@name='Trash'">
		          垃圾箱
		        </xsl:when>
		        <xsl:otherwise>
		          <!-- 过滤器 -->
		          <xsl:value-of select="@name"/>
		        </xsl:otherwise>        
		      </xsl:choose>
			&#160;
			
			<xsl:choose>
				<xsl:when test="MESSAGELIST/@new=0">
					<xsl:value-of select="MESSAGELIST/@new"/>
				</xsl:when>
				<xsl:otherwise>
          			<FONT color="red"><xsl:value-of select="MESSAGELIST/@new"/></FONT>
        		</xsl:otherwise>
       			</xsl:choose>
			 		/<xsl:value-of select="MESSAGELIST/@total"/>			
				</SPAN></A> 
			</LI>
		</xsl:template>			
						
							
</xsl:stylesheet>