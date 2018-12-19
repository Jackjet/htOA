<?xml version="1.0" encoding="gb2312"?>


<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  	<xsl:output method="html" indent="yes"/>

    <xsl:variable name="imgbase" select="/USERMODEL/STATEDATA/VAR[@name='img base uri']/@value"/>
    <xsl:variable name="base" select="/USERMODEL/STATEDATA/VAR[@name='base uri']/@value"/>
    <xsl:variable name="session-id" select="/USERMODEL/STATEDATA/VAR[@name='session id']/@value"/>
     <!-- 取名称为INBOX的文件夹Id --> 
    <xsl:variable name="inboxId" select="/USERMODEL/MAILHOST_MODEL/FOLDER[@name='INBOX']/@id"/>
            
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
				
				function deletemsg() {
					//var msg = '确认将所选邮件移至垃圾箱吗?'; 
					var msg = '确认彻底删除将所选邮件吗?'; 
					if(confirm(msg)) {
						$("move").value="";
						$("delete").value=1;
						$("msgslist").submit();
						return true;
					} else {
						return false;
					}
				}
				
				function movemsg() {
					$("move").value=1;
					$("delete").value="";
					$("msgslist").submit();
					return true;
				}
				
				function readmail(pos) {
					//document.location.href="readmsg.cgi?__mode=readmsg_sum&sid=e9ae4a349a786dce207b980606df95f2&folder=Inbox&pos="+pos;
					document.location.href="readmail.htm";
				}
				
				function editmail(file) {
					//document.location.href="compose.cgi?__mode=edit_drafts&sid=e9ae4a349a786dce207b980606df95f2&folder=Inbox&draft="+file;
				}
				
				
				function do_mkdir() {
					$("mkdir").value="1";
					$("rmdir").value="";
					$("rename").value="";
					$("foldername").value=$("fname").value;
					$("mgrform").submit();
				}
				
				function do_delete(old) {
					if (confirm("")) {
						$("mkdir").value="";
						$("rmdir").value="1";
						$("oldfolder").value=old;
						$("rename").value="";
						$("mgrform").submit();
					}
				}
				
				function do_rename(old) {
					var fname = prompt("将此邮件夹重新命名为:",old);
					if ((fname==null)||(fname==old))	{
					}	else	{
						$("mkdir").value="";
						$("rmdir").value="";
						$("rename").value="1";
						$("oldfolder").value=old;
						$("foldername").value=fname;
						$("mgrform").submit();
					}
				}
				
				function do_purge(old) {
					if (confirm("")) {
						$("mkdir").value="";
						$("rmdir").value="";
						$("rename").value="";
						$("purge").value="1";
						$("foldername").value=old;
						$("mgrform").submit();
					}
				}
				
				function showerror(err) {
					$("spanresult").innerHTML = "<font color=#ff0000>"+err+"</font>";
				}
				
				function changePart(obj){
					var vPart = obj.value;
					$("delete").value= vPart;
					$("msgslist").submit();			
				}
				
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
						
							<xsl:variable name="current" select="/USERMODEL/CURRENT[@type='folder']/@id"/>
								
							<FORM id="msgslist" name="msgslist" action="{$base}/folder/list?flag=1&amp;session-id={$session-id}&amp;folder-id={$current}" method="post">
					      		<TABLE height="100%" cellSpacing="0" cellPadding="0" width="100%">
					        		<TBODY>
					        			<TR>
					          				<TD class="tdmain_in_tit" height="28">
												<!--
													<SPAN class="navstl">
													<SPAN class="pl10 b">收件箱</SPAN> | 
													<SPAN>当前索引排列方式: 降序标题
														Showing messages <xsl:value-of select="/USERMODEL/CURRENT[@type='folder']/@first_msg"/> 
													      to <xsl:value-of select="/USERMODEL/CURRENT[@type='folder']/@last_msg"/> 
													      in folder <xsl:value-of select="@name"/> (<A HREF="{$base}/help?session-id={$session-id}&amp;helptopic=messagelist">Help</A>).
																</SPAN> 
															</SPAN>
															-->
															<xsl:call-template name="navigation"/>
															
															
														</TD>
												</TR>
												
												
												<TR>
					          				<TD>
					            				<xsl:apply-templates select="/USERMODEL/MAILHOST_MODEL//FOLDER[@id=$current]/MESSAGELIST"/>
														</TD>
												</TR>
										
										
										
					        			<TR>
					          				<TD height="36">
															<!--
															<SPAN class=navsbr>
																<IMG alt=第一页 src="images/firstpg.d.gif"> &nbsp; 
																<IMG alt=上一页 src="images/prevpg.d.gif"> &nbsp; 
																<IMG alt=下一页 src="images/nextpg.d.gif"> &nbsp; 
																<IMG alt=最后一页 src="images/lastpg.d.gif"> 
																页数：
																<SELECT onChange="#" name=pageindex> 
																	<OPTION value=0 selected>1 / 1</OPTION>
																</SELECT> 
					            				</SPAN>
					            				-->
					            				<xsl:call-template name="navigation"/>
												
															<SPAN class="navsbl">																
																<INPUT onClick="return deletemsg()" type="button" value="删除选择邮件" name="btn_delete"/> 
																<!-- 
																<INPUT onclick="movemsg();" type="button" value="移动到" name="btn_move"/> 
								            					<SELECT name="distfolder"> 
																	<OPTION value="Sent" selected="true">发件箱</OPTION> 
								              						<OPTION value="Drafts">草稿箱</OPTION> 
																	<OPTION value="Trash">垃圾箱</OPTION> 
								              						<OPTION value="Junk">垃圾邮件</OPTION> 
																	<OPTION value="filter">filter</OPTION>
																</SELECT>
																 -->
																 
																<INPUT id="move" type="hidden" name="move"/> 
																<INPUT id="delete" type="hidden" name="delete"/> 
																<INPUT id="part" type="hidden" name="part" value="{/USERMODEL/CURRENT[@type='folder']/@list_part}"/>																
																<INPUT type="hidden" value="" name="sid"/> 																
					        					</SPAN>
											</TD>
										</TR>
										
									</TBODY>
								</TABLE>
								</FORM>
								<!--<xsl:apply-templates select="/USERMODEL/MAILHOST_MODEL//FOLDER[@id=$current]"/>	-->
								
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
  
  <!--
  <xsl:template match="FOLDER">						
			<FORM id="msgslist" name="msgslist" action="folders.cgi" method="post">
				      		<TABLE height="100%" cellSpacing="0" cellPadding="0" width="100%">
				        		<TBODY>
				        			<TR>
				          				<TD class="tdmain_in_tit" height="28">
														<SPAN class="navstl">
															<SPAN class="pl10 b">收件箱</SPAN> | 
															<SPAN>
															Showing messages <xsl:value-of select="/USERMODEL/CURRENT[@type='folder']/@first_msg"/> 
												      to <xsl:value-of select="/USERMODEL/CURRENT[@type='folder']/@last_msg"/> 
												      in folder <xsl:value-of select="@name"/> (<A HREF="{$base}/help?session-id={$session-id}&amp;helptopic=messagelist">Help</A>).
															</SPAN> 
														</SPAN>
														
														<SPAN class="navstr">
															<IMG alt="第一页" src="{$imgbase}/images/firstpg.d.gif"/> &#160; 
							            					<IMG alt="上一页" src="{$imgbase}/images/prevpg.d.gif"/> &#160;
															<IMG alt="下一页" src="{$imgbase}/images/nextpg.d.gif"/> &#160; 
															<IMG alt="最后一页" src="{$imgbase}/images/lastpg.d.gif"/> 页数： 
															<SELECT name="pageindex"> 
																<OPTION value="0" selected="true">1 / 1</OPTION>
															</SELECT> 
							            	</SPAN>
													</TD>
											</TR>
											
											
											<TR>
				          				<TD>
				            				<xsl:apply-templates select="MESSAGELIST"/>
													</TD>
											</TR>
									
									
									
								</TBODY>
							</TABLE>
			</FORM>
	</xsl:template>
	-->
	
	<xsl:template name="navigation">
		<SPAN class="navstr">
				<xsl:if test="number(/USERMODEL/CURRENT[@type='folder']/@list_part) > number(1)">
				<A HREF="{$base}/folder/list?session-id={$session-id}&amp;folder-id={/USERMODEL/CURRENT[@type='folder']/@id}&amp;part=1">
				<IMG alt="第一页" src="{$imgbase}/images/firstpg.d.gif"/>
				</A>
				&#160; 		
				<A HREF="{$base}/folder/list?session-id={$session-id}&amp;folder-id={/USERMODEL/CURRENT[@type='folder']/@id}&amp;part={/USERMODEL/CURRENT[@type='folder']/@list_part - 1}">		
				<IMG alt="上一页" src="{$imgbase}/images/prevpg.d.gif"/> 
				</A>
				&#160;
				</xsl:if>
				
				<xsl:if test="number(/USERMODEL/CURRENT[@type='folder']/@all_part) > number(/USERMODEL/CURRENT[@type='folder']/@list_part)">
					<A HREF="{$base}/folder/list?session-id={$session-id}&amp;folder-id={/USERMODEL/CURRENT[@type='folder']/@id}&amp;part={/USERMODEL/CURRENT[@type='folder']/@list_part + 1}">	
					<IMG alt="下一页" src="{$imgbase}/images/nextpg.d.gif"/> 
					</A>
					&#160;
					<A HREF="{$base}/folder/list?session-id={$session-id}&amp;folder-id={/USERMODEL/CURRENT[@type='folder']/@id}&amp;part={/USERMODEL/CURRENT[@type='folder']/@all_part}"> 					
					<IMG alt="最后一页" src="{$imgbase}/images/lastpg.d.gif"/> 
					</A>
					&#160;
				</xsl:if>	
				
				页数： 
				<SELECT name="pageindex" onChange="javascript:changePart(this)"> 
					<xsl:call-template name="loop">
						<xsl:with-param name="Count"><xsl:value-of select="/USERMODEL/CURRENT[@type='folder']/@all_part"/></xsl:with-param>
					</xsl:call-template>
					<!-- 
					<OPTION value="0" selected="true">1 / 1</OPTION>
					 -->
				</SELECT> 
		</SPAN>
	</xsl:template>
	
	<xsl:template name="loop">
		<xsl:param name="Count"/>
		<!--  <xsl:if test="$Count&lt;1"><xsl:value-of select="'finish'"/></xsl:if>-->
		<xsl:if test="$Count&gt;=1">
			<!--  <xsl:value-of select="$Count"/>-->
			<xsl:choose>
				<xsl:when test="number(/USERMODEL/CURRENT[@type='folder']/@list_part) = $Count">
					<OPTION value="{$Count}" selected="true"><xsl:value-of select="$Count"/> / <xsl:value-of select="/USERMODEL/CURRENT[@type='folder']/@all_part"/></OPTION>	
				</xsl:when>
				<xsl:otherwise>
					<OPTION value="{$Count}"><xsl:value-of select="$Count"/> / <xsl:value-of select="/USERMODEL/CURRENT[@type='folder']/@all_part"/></OPTION>	
				</xsl:otherwise>
			</xsl:choose>
			
			<xsl:call-template name="loop">
			<xsl:with-param name="Count"><xsl:value-of select="number($Count)-1"/></xsl:with-param>
			</xsl:call-template>
			
		</xsl:if>
	</xsl:template>

	<xsl:template match="MESSAGELIST">
			<TABLE class="text-overflow" style="FONT-SIZE: 12px; BACKGROUND: #fff" cellSpacing="0" cellPadding="0">
														  <!--
														  <COLGROUP/>
														  <COL style="WIDTH: 24px"/>
														  <COL style="WIDTH: 20px"/>
														  <COL style="WIDTH: 27ex"/>
														  <COL style="WIDTH: 2ex"/>
														  <COL/>
														  <COL style="WIDTH: 14ex"/>
														  <COL style="WIDTH: 10px"/>
														  <COL style="WIDTH: 14ex"/>
														  -->
														  
												  		<TBODY>
				              									<TR class="MLTR_HEAD">
																		<TD align="right" style="WIDTH: 24px">
																			<INPUT onclick="CA(this.form)" type="checkbox" name="SELECTALL"/>
																		</TD>
																		<TD style="WIDTH: 20px">&#160;</TD>
																		<TD style="WIDTH: 50px"><A href="#"><B>附件</B></A></TD>
																		<TD style="WIDTH: 20px">&#160;</TD>
																		<TD style="WIDTH: 16%"><A href="#"><B>来信人</B></A></TD>
																		<TD style="WIDTH: 20px">&#160;</TD>
																		<TD><IMG src="{$imgbase}/images/sort_asc.gif" border="0"/> <A href="#"><B>主题</B></A> </TD>
																		<TD style="WIDTH: 80px"><A  href="#"><B>大小</B></A></TD>
								                						<TD style="WIDTH: 20px">&#160;</TD>
								                						<TD style="WIDTH: 150px"><A href="#"><B>日期</B></A></TD>
																	</TR>
																	
																	<xsl:for-each select="MESSAGE[number(@msgnr) >= number(/USERMODEL/CURRENT[@type='folder']/@first_msg) and number(@msgnr) &lt;= number(/USERMODEL/CURRENT[@type='folder']/@last_msg)]">
																	  <xsl:sort select="@msgnr" data-type="number" order="descending"/>
																    
																    	<TR class="MLTR" bgColor="#ffffff">
																			<TD style="PADDING-LEFT: 5px" align="right" height="16">
																				<INPUT type="checkbox" name="msgnr-{@msgnr}"/> 
																			</TD>
																			<TD class="sct">&#160;</TD>
																			
																			<TD>&#160;																			
																			  	<xsl:if test="@attachment">
																			  		<IMG src="{$imgbase}/images/i_attach.gif"/>
																			  	</xsl:if>
																			  	</TD>
																			<TD>&#160;</TD>
																			
																			<TD>
																			  <DIV>
																			  
																			  	<xsl:choose>
																		        <xsl:when test="contains(HEADER/FROM,'&lt;')">
																		          	<xsl:choose>
																			  			<xsl:when test="@seen">
																			  				<xsl:value-of select="substring-before(HEADER/FROM,'&lt;')"/>
																			  			</xsl:when>
																			  			<xsl:otherwise>
																			  				<SPAN class="boldA" title=""><xsl:value-of select="substring-before(HEADER/FROM,'&lt;')"/></SPAN>
																			  			</xsl:otherwise>
																			  		</xsl:choose>
																		          <BR/>
																		          
																		          
																		          <!--<SPAN class="bold">EMail:</SPAN> <xsl:value-of select="substring-before(substring-after(HEADER/FROM,'&lt;'),'>')"/>-->
																		        </xsl:when>
																		        <xsl:otherwise>
																		          <!--<BR/>
																		          <SPAN class="bold">EMail:</SPAN>--> 
																		          	<xsl:choose>
																			  			<xsl:when test="@seen">
																			  				<xsl:value-of select="HEADER/FROM"/>
																			  			</xsl:when>
																			  			<xsl:otherwise>
																			  				<SPAN class="boldA" title=""><xsl:value-of select="HEADER/FROM"/></SPAN>
																			  			</xsl:otherwise>
																			  		</xsl:choose>																		          
																		        </xsl:otherwise>
																		      </xsl:choose>
																			 
																			  </DIV>
																			</TD>
																			
																			<TD>&#160;</TD>
																			<TD style="CURSOR: pointer">
																			  <DIV>
																			  <A HREF="{$base}/folder/showmsg?session-id={$session-id}&amp;folder-id={/USERMODEL/CURRENT[@type='folder']/@id}&amp;message-nr={@msgnr}" class="seen">
																			  <xsl:choose>
																			  	<xsl:when test="@seen">
																			  		<xsl:value-of select="HEADER/SUBJECT"/>
																			  	</xsl:when>
																			  	<xsl:otherwise>
																			  		<SPAN class="boldA" title=""><xsl:value-of select="HEADER/SUBJECT"/></SPAN>
																			  	</xsl:otherwise>
																			  </xsl:choose>
																			  </A>
																			  </DIV>
																			</TD>
																			<TD>
																			<xsl:choose>
																			  	<xsl:when test="@seen">
																			  		<xsl:value-of select="@size"/>
																			  	</xsl:when>
																			  	<xsl:otherwise>
																			  		<SPAN class="boldA" title=""><xsl:value-of select="@size"/></SPAN>
																			  	</xsl:otherwise>
																			  </xsl:choose>
																			</TD>
																			<TD>&#160;</TD>
																			<TD noWrap="true">
																			<xsl:choose>
																				<xsl:when test="@seen">
																			  		<xsl:value-of select="HEADER/DATE"/>
																			  	</xsl:when>
																			  	<xsl:otherwise>
																			  		<SPAN class="boldA" title=""><xsl:value-of select="HEADER/DATE"/></SPAN>
																			  	</xsl:otherwise>
																			  </xsl:choose>
																			
																			</TD>
																		</TR>     
																	 
																	</xsl:for-each>
																	
																	
																	<input name="itemNum" value="{count(*)}" type="hidden"/>
													
															
												</TBODY>
			</TABLE>
		</xsl:template>
	
</xsl:stylesheet>
