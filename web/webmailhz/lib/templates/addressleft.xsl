<?xml version="1.0" encoding="gb2312"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
		
	<xsl:template name="addressLeft">		
			
	<!-- Left Part -->
    <TD id="MainLeft" vAlign="top" width="230">
      <TABLE cellSpacing="0" cellPadding="0">
        <TBODY>
        <TR>
          <TD>
           <SCRIPT language="javascript">
							<xsl:comment><![CDATA[
							function GrpAdd(base,sessionId) {
								var url = base + '/address/group?session-id=' + sessionId + '&method=edit';
								document.location.href = url;
							}
							
							function GrpEdit(base,sessionId,id) {
								var url = base + '/address/group?session-id=' + sessionId + '&sid=' + id + '&method=edit';								
								document.location.href = url;
							}
							
							function GrpDelete(base,sessionId,id) {
								if(confirm("组别信息将被永久删除，确定吗?")) {
									var url = base + '/address/group?session-id=' + sessionId + '&sid=' + id + '&method=delete';								
									document.location.href = url;
								}
							}
							
							function GrpBrowse(base,sessionId,grpid) {
								var url = base + '/address/list?session-id=' + sessionId + '&grpId=' + grpid;								
								document.location.href = url;
							}
							]]>		
							</xsl:comment>				
					  </SCRIPT>

           	<DIV class="panelout" style="MARGIN: 0px 5px 5px; WIDTH: 220px">
            	<DIV class="paneltit" style="PADDING-TOP: 5px; TEXT-ALIGN: right">
					<SPAN style="LEFT: 0px; FLOAT: left">&#160;组</SPAN> 
					<SPAN><A href="javascript:GrpAdd('{$base}','{$session-id}');">&gt;&gt;增加</A>&#160;</SPAN> 
				</DIV>
            	
				<DIV class="panelin">
            		<TABLE cellSpacing="0" cellPadding="0">
              			<TBODY>
              				
              				<xsl:apply-templates select="/USERMODEL/USERDATA/ADDRESS/GROUPLIST"/>
              				
											<!--<TR>
												<TD width="100%">
													<A title="浏览 etert" href="javascript:GrpEdit('3');">etert</A> (2)
												</TD>
												<TD style="WHITE-SPACE: nowrap">
													<A title="编辑" href="javascript:GrpEdit('3');">
														<IMG src="{$imgbase}/images/edit.gif"/>
													</A> 
													<A title="删除" href="javascript:GrpDelete('3');">
														<IMG src="{$imgbase}/images/delete.gif"/>
													</A> 
												</TD>
											</TR>
							
											
									  	<TR>
												<TD width="100%">
													<A title="浏览 stu" href="javascript:GrpEdit('1');">stu</A> (3)</TD>
												<TD style="WHITE-SPACE: nowrap"><A title=编辑 
												  href="javascript:GrpEdit('1');"><IMG 
												  src="images/edit.gif"></A> <A title=删除 
												  href="javascript:GrpDelete('1');"><IMG 
												  src="images/delete.gif"></A> </TD>
											</TR>
							
									  	<TR>
												<TD width="100%"><A title="浏览 外国人" 
												  href="javascript:GrpEdit('4');">外国人</A> (0)
												</TD>
												<TD style="WHITE-SPACE: nowrap"><A title=编辑 
												  href="javascript:GrpEdit('4');"><IMG 
												  src="images/edit.gif"></A> <A title=删除 
												  href="javascript:GrpDelete('4');"><IMG 
												  src="images/delete.gif"></A> 
												</TD>
										  </TR>
										  -->
									</TBODY>
								</TABLE>
							</DIV>
						</DIV>
					</TD>
				</TR>
				
				<!-- 
       			<TR>
          			<TD>
            			<FORM name="searchit" action="abook.cgi" method="post">
								<INPUT type="hidden" value="e9ae4a349a786dce207b980606df95f2" name="sid"/> 
								<INPUT type="hidden" value="abook_search" name="__mode"/> 
			          
			          		<DIV class="panelout" style="PADDING-RIGHT: 5px; PADDING-LEFT: 5px; PADDING-BOTTOM: 5px; MARGIN-LEFT: 5px; MARGIN-RIGHT: 5px; PADDING-TOP: 5px">
									<SPAN style="PADDING-RIGHT: 5px; PADDING-LEFT: 5px; PADDING-BOTTOM: 5px; PADDING-TOP: 5px">
										<INPUT size="15" name="keyword"/>&#160;
										<INPUT type="submit" value="搜索" name="search"/> 
			            		</SPAN>
								</DIV>
						</FORM>
					</TD>
				</TR>
				 -->
				</TBODY>
			</TABLE>
    
    		<!-- 
	    	<FORM id="myfrm" name="myfrm" action="abook.cgi" method="post" encType="multipart/form-data">
		  		<INPUT type="hidden" value="e9ae4a349a786dce207b980606df95f2" name="sid"/> 
				<INPUT type="hidden" value="abook_edit" name="__mode"/> 
				<INPUT type="hidden" name="url"/> 
				<INPUT id="delete" type="hidden" name="delete"/> 
			</FORM>
			 -->
		</TD>
		<!-- Left Part End -->	
	  </xsl:template>	
	  
	  <xsl:template match="GROUPLIST">
	  	<xsl:for-each select="GROUP">
	  		 <xsl:sort select="@name" data-type="string" order="ascending"/>
	  		 <TR>
               <TD width="100%" height="22">
					<A title="浏览 {@name}" href="javascript:GrpBrowse('{$base}','{$session-id}','{@id}');"><xsl:value-of select="@name"/></A> (4)
				</TD>								
                
                <TD style="WHITE-SPACE: nowrap">
					<A title="编辑" href="javascript:GrpEdit('{$base}','{$session-id}','{@id}');">
						<IMG src="{$imgbase}/images/edit.gif"/>
					</A> 
					&#160;
					<A title="删除" href="javascript:GrpDelete('{@id}');">
						<IMG src="{$imgbase}/images/delete.gif"/>
					</A> 
				</TD>
			</TR>
	  	</xsl:for-each>
	  	
	  </xsl:template>					
							
</xsl:stylesheet>