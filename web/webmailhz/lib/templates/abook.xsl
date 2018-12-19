<?xml version="1.0" encoding="gb2312"?>


<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  	<xsl:output method="html" indent="yes"/>

    <xsl:variable name="imgbase" select="/USERMODEL/STATEDATA/VAR[@name='img base uri']/@value"/>
    <xsl:variable name="base" select="/USERMODEL/STATEDATA/VAR[@name='base uri']/@value"/>
    <xsl:variable name="session-id" select="/USERMODEL/STATEDATA/VAR[@name='session id']/@value"/>
     <!-- 取名称为INBOX的文件夹Id --> 
    <xsl:variable name="inboxId" select="/USERMODEL/MAILHOST_MODEL/FOLDER[@name='INBOX']/@id"/>
         
    <xsl:variable name="currentgrp" select="/USERMODEL/CURRENT[@type='groupbrowse']/@id"/>     
            
    <xsl:include href="head.xsl"/>
    <xsl:include href="addressleft.xsl"/>   
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
				var currentNav = "nav_abook";
				]]>		
				</xsl:comment>				
			</SCRIPT>
			
			<SCRIPT language="javascript">
				<xsl:comment><![CDATA[
					function CA() {
						var form = document.getElementById("myfrm");
						for (var i=0;i<form.elements.length;i++)
						{
							var e = form.elements[i];
							if ((e.name != 'SELECTALL') && (e.type=='checkbox'))
							{
								e.checked = form.SELECTALL.checked;
							}
						}
					}
					
					function cfm_delete() {
						if(confirm("通讯录信息将被永久删除，确定吗?"))	{
							//document.getElementById("delete").value=1;
							document.getElementById("myfrm").submit();
							return true;
						} else	{
							return false;
						}
					}
					
					function DoEdit(base,sessionId,id) {
						var url = base + '/address/person?session-id=' + sessionId + '&sid=' + id + '&method=edit';								
						document.location.href = url;
					}
				
									
					function DoCompose() {
						var form = document.getElementById("myfrm");
						var mail = '';
						for(var i=0;i<form.elements.length;i++) {
							var e = form.elements[i];
							if (e.name == 'SELECTALL')
								continue;
							if (e.type =='checkbox' && e.checked) {
								if (mail) {
									mail += ', ' + e.value;
								} else {
									mail = e.value;
								}
							}
						}
						if (mail) {
							document.location.href='compose.cgi?sid=e9ae4a349a786dce207b980606df95f2&to='+mail + '&atag=1';
						} else {
							alert('选择为空，请重新选择');
						}
					}
				]]>		
				</xsl:comment>				
			</SCRIPT>
		
     <BODY>    
      <xsl:call-template name="topHeader"/> 
      <xsl:call-template name="normalHeader"/>
      
      <DIV id="Main">
				<TABLE cellspacing="0" cellpadding="0" width="100%">
				  <TBODY>
				  	<TR>
				  
						<!-- Left Part -->
						<xsl:call-template name="addressLeft"/>
						<!-- Left Part End -->
						
						<TD class="td1px"></TD>
						
						
						<!-- Main Content Part -->
						<TD class="tdmain" valign="top">
								<TABLE height="100%" cellSpacing="0" cellPadding="0" width="100%">
				        	<TBODY>
				        		<TR>
				          			<TD class="tdmain_in_tit" height="28">
							 						<SPAN style="LEFT: 0px; FLOAT: left">
														<SPAN class="pl10 b">个人通讯录 </SPAN> 
														<!-- 
														|
														<SPAN><A href="#">&#160;显示全部</A></SPAN> |  
														<SPAN><A href="#"><B>增加</B></A></SPAN> 
														 -->
							            </SPAN>
													
													<SPAN style="PADDING-RIGHT: 10px; FLOAT: right">
														<A href="#" target="_blank">通讯录导出</A>
														<!--  | <A href="#">全局通讯录</A>  -->
							            </SPAN>
												</TD>
										</TR>
										<TR>
										  	<TD class="tdmain_in_con">
										  		<xsl:choose>
										  			<xsl:when test="/USERMODEL/CURRENT[@type='groupbrowse']">										  				
										  				<xsl:apply-templates select="/USERMODEL/USERDATA/ADDRESS/GROUPLIST/GROUP[@id=$currentgrp]"/>
										  			</xsl:when>
										  			<xsl:otherwise>
										  				<xsl:apply-templates select="/USERMODEL/USERDATA/ADDRESS/PERSONLIST"/>
										  			</xsl:otherwise>
										  		</xsl:choose>
											</TD>
										</TR>
								
										<TR>
										  	<TD height="28">
										  		<SPAN class="navsbr"></SPAN>
										  		<SPAN class="navsbl">
														<INPUT onclick="cfm_delete();" type="button" value=" 删除选择人员 "/> 
														&#160;
														<INPUT onclick="DoEdit('{$base}','{$session-id}','')" type="button" value=" 增加人员 "/> 
														<!-- <INPUT onclick="DoCompose();" type="button" value=" 写邮件 "/> -->
													</SPAN>
													<INPUT type="hidden" value="0" name="page"/> 
													<INPUT type="hidden" name="keyword"/> 
												</TD>
										</TR>
									</TBODY>
								</TABLE>
						</TD>
						<!--Main Content Part End -->
												
						<TD width="10"></TD>
					</TR>
				</TBODY>
				</TABLE>
				</DIV>
 
    	<xsl:call-template name="Bottom"/>
    	
		 </BODY>
    </HTML>
  </xsl:template>
  
 
  <xsl:template match="PERSONLIST">
  	<form name="myfrm" action="{$base}/address/person?session-id={$session-id}&amp;method=delete" method="post">
	<TABLE style="FONT-SIZE: 12px; BACKGROUND: #fff; WIDTH: 100%" cellSpacing="0" cellPadding="0">
		<TBODY>
			<TR class="MLTR_HEAD">
				<TD>
				<INPUT onclick="CA()" type="checkbox" name="SELECTALL"/>
				</TD> 
				<TD>姓名</TD>
				<TD>电邮</TD>
				<TD>移动电话</TD>
				<TD>工作职位</TD>
			</TR>
			
			<xsl:for-each select="PERSON">
				<xsl:sort select="AB_NAME" data-type="string" order="ascending"/>											
				<TR class="MLTR">
					<TD><INPUT type="checkbox" value="{@id}" name="deleteId-{position()}"/></TD>
					<TD><A title="编辑 {normalize-space(AB_NAME)}" href="javascript:DoEdit('{$base}','{$session-id}','{@id}');"><xsl:value-of select="AB_NAME"/></A></TD>
					<TD><A href="#"><xsl:value-of select="AB_EMAIL"/></A></TD>
					<TD>&#160;<xsl:value-of select="AB_MOBILE"/></TD>
					<TD>&#160;<xsl:value-of select="AB_JOB"/></TD>
				</TR>
			</xsl:for-each>
		</TBODY>
	</TABLE>
	
	<input name="itemNum" value="{count(*)}" type="hidden"/>
	</form>
  </xsl:template>	
  
  
  <xsl:template match="GROUP">
  	<form name="myfrm" action="{$base}/address/person?session-id={$session-id}&amp;method=delete" method="post">
	<TABLE style="FONT-SIZE: 12px; BACKGROUND: #fff; WIDTH: 100%" cellSpacing="0" cellPadding="0">
		<TBODY>
			<TR class="MLTR_HEAD">
				<TD>
				<INPUT onclick="CA()" type="checkbox" name="SELECTALL"/>
				</TD> 
				<TD>姓名</TD>
				<TD>电邮</TD>
				<TD>移动电话</TD>
				<TD>工作职位</TD>
			</TR>
			
			<xsl:for-each select="./PERSON">
				<xsl:variable name="abccc" select="./@id"> </xsl:variable>				
				<xsl:apply-templates select="/USERMODEL/USERDATA/ADDRESS/PERSONLIST/PERSON[@id=$abccc]"/>
			</xsl:for-each>
		</TBODY>
	</TABLE>
	
	<input name="itemNum" value="{count(*)}" type="hidden"/>
	</form>
  </xsl:template>	
  
  <xsl:template match="PERSON">	
  	<TR class="MLTR">
					<TD><INPUT type="checkbox" value="{@id}" name="deleteId-{position()}"/></TD>
					<TD>
						<A title="编辑 {normalize-space(AB_NAME)}" href="javascript:DoEdit('{$base}','{$session-id}','{@id}');">
							<xsl:value-of select="AB_NAME"/>
						</A>
					</TD>
					<TD><xsl:value-of select="AB_EMAIL"/></TD>
					<TD>&#160;<xsl:value-of select="AB_MOBILE"/></TD>
					<TD>&#160;<xsl:value-of select="AB_JOB"/></TD>
				</TR>
</xsl:template>
  
  
  
	
</xsl:stylesheet>
