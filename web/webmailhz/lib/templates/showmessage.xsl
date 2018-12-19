<?xml version="1.0" encoding="gb2312"?>


<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:w="urn:www.microsoft.com/word" xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:v="urn:schemas-microsoft-com:vml"  xmlns:x="http://www.ibm.com" version="1.0">
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
      </HEAD>
			
			<SCRIPT src="{$imgbase}/js/common.js" type="text/javascript"></SCRIPT>
			<SCRIPT src="{$imgbase}/js/menu.js" type="text/javascript"></SCRIPT>			
			
			
			<SCRIPT language="javascript">
				<xsl:comment><![CDATA[
				var currentNav = "nav_mail";
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
								<xsl:variable name="current_msg" select="/USERMODEL/CURRENT[@type='message']/@id"/>
								<xsl:variable name="current_folder" select="/USERMODEL/CURRENT[@type='folder']/@id"/>
								<xsl:apply-templates select="/USERMODEL/MAILHOST_MODEL//FOLDER[@id=$current_folder]//MESSAGE[@msgid=$current_msg]"/>
								
								
								<INPUT type="hidden" name="msgId" id="msgId">   
								    <xsl:attribute name="value"><xsl:value-of select="$current_msg"/></xsl:attribute>   
								</INPUT> 
					    		<INPUT type="hidden" name="folderId" id="folderId">   
								    <xsl:attribute name="value"><xsl:value-of select="$current_folder"/></xsl:attribute>   
								</INPUT> 
								
								<INPUT type="hidden" name="base" id="base">   
								    <xsl:attribute name="value"><xsl:value-of select="$base"/></xsl:attribute>   
								</INPUT> 
								<INPUT type="hidden" name="sessionId" id="sessionId">   
								    <xsl:attribute name="value"><xsl:value-of select="$session-id"/></xsl:attribute>   
								</INPUT>
								
								<INPUT type="hidden" name="msgNr" id="msgNr">   
								    <xsl:attribute name="value"><xsl:value-of select="/USERMODEL/MAILHOST_MODEL//FOLDER[@id=$current_folder]//MESSAGE[@msgid=$current_msg]/@msgnr"/></xsl:attribute>   
								</INPUT>
						</TD>
						<!--Main Content Part End -->
				
						<TD width="10"></TD>
					</TR>
				</TBODY>
				</TABLE>
			</DIV>
 
    		<xsl:call-template name="Bottom"/>
    	
    		<DIV class="headermenu_popup" id="m_reply_menu" style="DISPLAY: none">
				<TABLE cellSpacing="0" cellPadding="4" border="0">
				  <TBODY>
					  <TR>
							<TD class="popupmenu_option" onClick="javascript:Do('rpl');">回复</TD></TR>
					  <TR>
							<TD class="popupmenu_option" onclick="javascript:Do('rplall');">回复全部</TD>
						</TR>
					</TBODY>
				</TABLE>
			</DIV>
			
			<DIV class="headermenu_popup" id="m_fwd_menu" style="DISPLAY: none">
				<TABLE cellSpacing="0" cellPadding="4" border="0">
				  <TBODY>
					  <TR>
							<TD class="popupmenu_option" onClick="Do('fwd');">转发</TD></TR>
					  <!-- 
					  <TR>
							<TD class="popupmenu_option" onclick="Do('fwdatt');">附件转发</TD>
						</TR>
						 -->
					</TBODY>
				</TABLE>
			</DIV>
				
			<DIV class="headermenu_popup" id="m_char_menu" style="DISPLAY: none">
				<TABLE cellSpacing="0" cellPadding="4" border="0">
				  <TBODY>
					  <TR>
							<TD class="popupmenu_option" onclick="">auto</TD>
						</TR>
					  <!-- 
					  <TR>
							<TD class="popupmenu_option" style="BACKGROUND: #ccc" onclick="#">gb2312</TD>
						</TR>
					  <TR>
							<TD class="popupmenu_option" onclick="ChgEnc('gbk')">gbk</TD>
						</TR>
					  <TR>
							<TD class="popupmenu_option" onclick="ChgEnc('gb18030')">gb18030</TD></TR>
					  <TR>
							<TD class="popupmenu_option" onclick="ChgEnc('big5')">big5</TD></TR>
					  <TR>
							<TD class="popupmenu_option" onclick="ChgEnc('utf-8')">utf-8</TD></TR>
					  <TR>
							<TD class="popupmenu_option" onclick="ChgEnc('iso-2022-jp')">iso-2022-jp</TD>
						</TR>
					  <TR>
							<TD class="popupmenu_option" onclick="ChgEnc('shift-jis')">shift-jis</TD>
						</TR>
					  <TR>
							<TD class="popupmenu_option" onclick="ChgEnc('euc-jp')">euc-jp</TD>
						</TR>
					  <TR>
							<TD class="popupmenu_option" onclick="ChgEnc('euc-kr')">euc-kr</TD>
						</TR>
					  <TR>
							<TD class="popupmenu_option" onclick="ChgEnc('iso-2022-kr')">iso-2022-kr</TD>
						</TR>
						 -->
					</TBODY>
				</TABLE>
			</DIV>
			<!--
			<DIV class=headermenu_popup id=m_more_menu style="DISPLAY: none">
			<TABLE cellSpacing=0 cellPadding=4 border=0>
				 <TBODY>
				  	<TR>
						<TD class=popupmenu_option onClick="Do('detail')">详细结构</TD></TR>
				  	<TR>
						<TD class=popupmenu_option onClick="Do('rawdata')">原始邮件</TD></TR>
				  	<TR>
						<TD class=popupmenu_option onClick="Do('print')">打印</TD></TR>
				  	<TR>
						<TD class=popupmenu_option onClick="Do('top')">返回</TD></TR>
				  	<TR>
						<TD class=popupmenu_option onclick="Do('header')">信头</TD></TR>
				</TBODY>
			</TABLE>
			</DIV>
			-->
    		
    		
    		<SCRIPT language="javascript">
				<xsl:comment><![CDATA[
					var base = document.getElementById("base").value;
					var sessionId = document.getElementById("sessionId").value;
					var folderId = document.getElementById("folderId").value;
					var messageNr = document.getElementById("msgNr").value;
									
					function Do(action){
						//alert(action);
						//alert(base);
						var url;
						switch (action)	{
							case 'rpl':								
								url =  base   + '/compose?session-id=' + sessionId + '&folder-id=' + folderId + '&message-nr=' + messageNr + '&reply=1'								
								//alert(url);
								window.location.href = url;
								break;
							case 'fwd':
								url =  base   + '/compose?session-id=' + sessionId + '&folder-id=' + folderId + '&message-nr=' + messageNr + '&forward=1'								
								//alert(url);
								window.location.href = url;
								break;
							case 'del':
								if (confirm("将删除您当前阅读的邮件，操作不可恢复，您确定吗？")) {								
								}
								break;
							case 'prev':
								url =  base   + '/folder/showmsg?session-id=' + sessionId + '&folder-id=' + folderId + '&message-nr=' + String(parseInt(messageNr)+1) + '&reply=1'								
								//alert(url);
								window.location.href = url;
								break;
							case 'next':
								url =  base   + '/folder/showmsg?session-id=' + sessionId + '&folder-id=' + folderId + '&message-nr=' + String(parseInt(messageNr)-1) + '&reply=1'								
								//alert(url);
								window.location.href = url;
								break;
						}
					}
					
					function ChgEnc(enc) {
						var url ='readmsg.cgi?__mode=readmsg_sum&sid=e9ae4a349a786dce207b980606df95f2&folder=Inbox&pos=14';
						window.location.href= url + '&charset=' + enc;
					}
					
					function eURL(str) {
						var rv = ' '; // not '' for a NS bug!
						for (i=0; i<str.length; i++) {
							aChar=str.substring(i, i+1);
							switch(aChar) {
								case '=': rv += "%3D"; break;
								case '?': rv += "%3F"; break;
								case '&': rv += "%26"; break;
								default: rv += aChar;
							}
						}
						return rv.substring(1, rv.length);
					}
					
					/* AddressBook */
					function html2txt(s) {
							s = s.replace(/&amp;/g, '&').replace(/&qute;/g,'"');
						s = s.replace(/&nbsp;/g, ' '); /* replace all space ? */
							s = s.replace(/&lt;/g, '<').replace(/&gt;/g, '>');
							return s;
					}
					
					function Add_Contact(obj) {
						var txt = html2txt(obj.innerHTML);
						var re = /\s*['\"]*\s*([^\'\"]*)\s*[\'\"]*(\s+|^)<*([a-z0-9A-Z\-_\.]+@[a-z0-9A-Z-\_.]+)>*/;
						if(re.test(txt)) {
							var url ='abook.cgi?sid=e9ae4a349a786dce207b980606df95f2&__mode=abook_show&screen=abook_edit.html';
							var name = RegExp.$1;
							var addr = RegExp.$3;
					
							if(name == "") {
								var re2 = /([^@]+)@(.*)/;
								if(re2.test(addr)) {
								name = RegExp.$1;
								}else {
								name = addr; /* fallback to addr */
								}
							}
							/* convert & to ; to advoid request error, dist programe should do url
							   convertion and recovery the url parameter */
							url += '&name='+escape(name)+'&mail='+addr;
							url += '&url='+eURL('/extmail/cgi/readmsg.cgi?sid=e9ae4a349a786dce207b980606df95f2&folder=Inbox&pos=14');
							document.location.href= url;
						}else {
							alert('Error on parsing sender! Abort');
						}
					}
					
					function DoDelete() {
						if (confirm("将删除您当前阅读的邮件，操作不可恢复，您确定吗？")) {
							var url = '?__mode=delete&sid=e9ae4a349a786dce207b980606df95f2&folder=Inbox&pos=14';
							document.location.href = url;
						}
					}
					
					function DoPrint() {
						var url = "?__mode=readmsg_sum&sid=e9ae4a349a786dce207b980606df95f2&folder=Inbox&pos=14";
						url += '&screen=print.html';
						var hWnd = window.open(url,"","width=550,height=450,resizable=yes,status=yes,scrollbars=yes");
						if ((document.window != null) && (!hWnd.opener))
							hWnd.opener = document.window;
					}
				]]>		
				</xsl:comment>				
			</SCRIPT>
			
		 </BODY>
    </HTML>
  </xsl:template>
  
  
  <xsl:template match="MESSAGE">
  			
  
  		<TABLE height="100%" cellSpacing="0" cellPadding="0" width="100%">
        		<TBODY>
        			<TR>
          				<TD height="28" class="tdmain_in_tit">
										<SPAN class="navstl">
											<SPAN class="pl10 b">标题：</SPAN> 
											<SPAN><xsl:value-of select="HEADER/SUBJECT"/></SPAN>
										</SPAN> 
										
										<SPAN class="navstr">
											<SPAN class="mopspan" onClick="javascript:Do('rpl')">回复</SPAN><SPAN id="m_reply"><IMG src="{$imgbase}/images/sl.gif"/>
												<SCRIPT type="text/javascript"><xsl:comment>
													<![CDATA[
													menuregister(true, "m_reply")
													]]>
													</xsl:comment>	
												</SCRIPT>
											</SPAN> | 
											<SPAN class="mopspan" onClick="javascript:Do('fwd')">转发</SPAN><SPAN id="m_fwd"><IMG src="{$imgbase}/images/sl.gif"/>
												<SCRIPT type="text/javascript"><xsl:comment>
													<![CDATA[
													menuregister(true, "m_fwd")
													]]>
													</xsl:comment>	
												</SCRIPT>
											</SPAN> | 
											<SPAN id="m_char">编码<IMG src="{$imgbase}/images/sl.gif"/>
												<SCRIPT type="text/javascript"><xsl:comment>
													<![CDATA[
													menuregister(true, "m_char")
													]]>
													</xsl:comment>	
												</SCRIPT>
											</SPAN> | 
											<SPAN class="mopspan" onClick="">删除</SPAN> | 
											<xsl:if test="@msgnr &lt; ../../MESSAGELIST/@total">
												<SPAN class="mopspan" onClick="javascript:Do('prev')">&lt;上一封</SPAN> |
											</xsl:if>
											<xsl:if test="@msgnr > 1">
												<SPAN class="mopspan" onClick="javascript:Do('next')">下一封&gt;</SPAN> |
											</xsl:if>
											
											<SPAN id="m_more">更多操作选项<IMG src="{$imgbase}/images/sl.gif"/></SPAN> 
										</SPAN>
								</TD>
						</TR>
						
						<TR>
					  	<TD class="tdmain_in_con_mt">
								<DIV class="pl10" style="LINE-HEIGHT: 18px; HEIGHT: 18px">
									<SPAN><B>来自：</B></SPAN> 
									<SPAN id="from"><xsl:value-of select="HEADER/FROM"/></SPAN>&#160; 
									<!--<A title="将该来信人加到地址本中" href="javascript:Add_Contact(from)">将该来信人加到地址本中</A>-->
								</DIV>
								<DIV class="pl10">
									<SPAN><B>发给：</B></SPAN> 
									<SPAN><xsl:value-of select="HEADER/TO"/></SPAN> 
								</DIV>
								<DIV class="pl10" style="LINE-HEIGHT: 18px; HEIGHT: 18px">
									<SPAN><B>日期：</B></SPAN> 
									<SPAN><xsl:value-of select="HEADER/DATE"/></SPAN> 
								</DIV>
								
								<xsl:if test="@attachment">   
								<DIV style="line-height:18px;padding-top:2px;" class="pl10">
									<TABLE border="0" cellspacing="0" cellpadding="0">
										<TR>
											<TD style="vertical-align: top;"><B>附件列表：</B></TD>
											<TD id="attachment_list">
												<TABLE border="0" cellspacing="0" cellpadding="0"> 				
													 <xsl:if test="PART/@type='multi'">
													 	<xsl:for-each select="PART/PART">
													 		<xsl:if test="@type='binary' or @type='image'">
													 			<TR>
																	<TD style="padding-bottom: 2px">
																		<A title="{@filename} ({@size}K)" HREF="{$base}/showmime?session-id={$session-id}&amp;folder-id={ancestor::FOLDER/@id}&amp;message-nr={ancestor::MESSAGE/@msgnr}&amp;m-part={@part-count}">
																		 	<xsl:value-of select="@filename"/> (<xsl:value-of select="@size"/>K)
																		</A>																		
																	</TD>
																
																	<TD style="padding-left: 10px; padding-top:2px">
																	</TD> 				
																</TR>
													 		</xsl:if>
													 	</xsl:for-each>
													 </xsl:if>
								 				</TABLE>
											</TD>
										</TR>
									</TABLE>								
								</DIV> 
								</xsl:if>
								
							</TD>
						</TR>
						
						
						<TR>
					  	<TD class="mailbody" style="PADDING-RIGHT: 20px; PADDING-LEFT: 20px; PADDING-BOTTOM: 20px; PADDING-TOP: 20px; BORDER-BOTTOM: #d6e0e9 0px solid">
					  		<xsl:for-each select="PART">     
									<xsl:apply-templates select="."/>	
    						</xsl:for-each>
					  	</TD>
						</TR>
					
					</TBODY>
			</TABLE>
	</xsl:template>
	
	
	
	<xsl:template match="PART">
    <xsl:choose>
      <xsl:when test="@type='text' and @hidden='true'" />
      <xsl:when test="@type='text' and not(@hidden='true')">
			  <xsl:for-each select="*">
			    <pre><xsl:apply-templates select="."/></pre>
			    <!--<xsl:copy-of select="."/> -->
			  </xsl:for-each>
      </xsl:when>
      <xsl:when test="@type='html'">
				<xsl:for-each select="*">
				  <xsl:apply-templates select="."/>
				</xsl:for-each>
				 
      </xsl:when>
      <xsl:when test="@type='multi'">
				<xsl:for-each select="PART">
				  <!--<CENTER><P class="testoGrande">MIME part</P></CENTER><BR/>-->
				  <xsl:apply-templates select="."/>
				</xsl:for-each>		
      </xsl:when>
      <!--
      <xsl:otherwise>
				<P>Other Part</P>
      </xsl:otherwise>
      -->
    </xsl:choose>
  </xsl:template>
	
	<xsl:template match="CONTENT">
    <xsl:choose>
      <xsl:when test="../@type = 'html'">
      	   <xsl:copy-of select="."/> 
					<!--<xsl:copy-of select="*" /><xsl:apply-templates select="*"/>-->
		  </xsl:when>
      <xsl:otherwise>
					<xsl:value-of select="." disable-output-escaping="yes" />
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  
  
</xsl:stylesheet>
