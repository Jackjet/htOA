<?xml version="1.0" encoding="gb2312"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">				
				
	<xsl:template name="index">
				<TD valign="top">
							<DIV id="mwel">欢迎光临， <xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/></DIV>
							<DIV class="panelin" id="chkupdate_container"></DIV>
							<SCRIPT type="text/javascript">
								//chkupdate_init(version, software);
							</SCRIPT>
					
							<DIV id="mquota">
							
								<!-- quota usage bar -->
								<TABLE cellSpacing="0" cellPadding="0" border="0"> 
									<TBODY>
										<TR>
											<TD class="quotapc" noWrap="true" width="200" colSpan="3">
												您已使用了<xsl:value-of select="/USERMODEL/MAILHOST_MODEL/quota/resource[@name='STORAGE']/@usagepct"/>%的 <xsl:value-of select="/USERMODEL/MAILHOST_MODEL/quota/resource[@name='STORAGE']/@limitkb"/>M </TD>
										</TR>
										<TR style="PADDING-BOTTOM: 3px">
											<TD class="quotapc" width="5">0%</TD>
											<TD width="150">
												<TABLE class="quotabar" cellSpacing="0" cellPadding="0" width="100%" border="0">
													<TBODY>
														<TR>
															<TD style="BORDER-RIGHT: #c7c8cb 1px solid; PADDING-RIGHT: 1px; BORDER-TOP: #c7c8cb 1px solid; PADDING-LEFT: 1px; PADDING-BOTTOM: 1px; BORDER-LEFT: #c7c8cb 1px solid; PADDING-TOP: 1px; BORDER-BOTTOM: #c7c8cb 1px solid" 
										width="100%">
																<DIV class="quotapc_normal" style="FONT-SIZE: 6px; WIDTH: {$usagequato}%; HEIGHT: 6px"></DIV>
															</TD>
														</TR>
													</TBODY>
												</TABLE>
											</TD>
											<TD class="quotapc" align="left">100%</TD>
										</TR>
										
										<TR>
											<TD class="quotapc" colSpan="3">邮件 <xsl:value-of select="/USERMODEL/MAILHOST_MODEL/quota/resource[@name='MESSAGE']/@usage"/> 封 <xsl:value-of select="/USERMODEL/MAILHOST_MODEL/quota/resource[@name='STORAGE']/@usage"/>K </TD>
										</TR>
									</TBODY>
								</TABLE>
								<!-- end of quota bar -->
								
							</DIV>
							
							<DIV id="minfo">
							<xsl:choose>
									<xsl:when test="/USERMODEL/MAILHOST_MODEL/FOLDER[@name='INBOX']/MESSAGELIST/@new=0">
											您没有未读邮件							          
									</xsl:when>
									<xsl:otherwise>
											<IMG src="{$imgbase}/images/unread.gif"/>
											您有 <SPAN class="newnum"><xsl:value-of select="/USERMODEL/MAILHOST_MODEL/FOLDER[@name='INBOX']/MESSAGELIST/@new"/></SPAN>
											封新邮件	
									</xsl:otherwise>
							</xsl:choose>
							</DIV>
						
							<BR/><BR/><BR/>
			
							<DIV style="PADDING-RIGHT: 5px; PADDING-LEFT: 5px">
								<DIV class="panelout" id="etnews_div">
									<DIV class="paneltit">&#160; ExtMail Project News</DIV>
									<DIV class="panelin" id="etnews_container"></DIV>
								</DIV>
								<SCRIPT type="text/javascript">
									//etnews_init();
								</SCRIPT>
							</DIV>
						</TD>

	</xsl:template>		
	
	
							
</xsl:stylesheet>