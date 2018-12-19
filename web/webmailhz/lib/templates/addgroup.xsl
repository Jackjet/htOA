<?xml version="1.0" encoding="gb2312"?>


<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  	<xsl:output method="html" indent="yes"/>

    <xsl:variable name="imgbase" select="/USERMODEL/STATEDATA/VAR[@name='img base uri']/@value"/>
    <xsl:variable name="base" select="/USERMODEL/STATEDATA/VAR[@name='base uri']/@value"/>
    <xsl:variable name="session-id" select="/USERMODEL/STATEDATA/VAR[@name='session id']/@value"/>
     <!-- 取名称为INBOX的文件夹Id --> 
    <xsl:variable name="inboxId" select="/USERMODEL/MAILHOST_MODEL/FOLDER[@name='INBOX']/@id"/>
    
            
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
					function unSelect(s) {
						var lst = document.getElementById(s);
						for(var i = 0; i<lst.options.length; i++) {
						   if(lst.options[i].selected){
						      lst.options[i].selected = false;
						   }
						}
					}
					
					function Del(src) {
						var lst2 = document.getElementById(src);
						var lstindex = lst2.selectedIndex;
						if(lstindex>=0){
						   var length = lst2.options.length;
						   //must delete reverse or delete will fail
						   var ii = length -1;
						   while (ii>=0){
						   		if(lst2.options[ii].selected)
						   			lst2.options[ii].parentNode.removeChild(lst2.options[ii]);
						   			
						   		ii = ii - 1;
						   }						  
						}
					}
					
						
					function Add(src, dst) {
						//alert('111111111111');
						var lst1 = document.getElementById(src);
						var lstindex = lst1.selectedIndex;
						        
						var lst2 = document.getElementById(dst);
						
						//alert(lstindex);
						if(lstindex>=0) {
						   for(var ii = 0; ii<lst1.options.length; ii++) {
						        var canAdd = 1;
						         if(lst1.options[ii].selected){
						            var value = lst1.options[ii].value;
						            var text = lst1.options[ii].text;
						            for(var i = 0; i<lst2.options.length; i++) {
						                if(value == lst2.options[i].value) {
						                    canAdd = 0;
						                    break;
						                }
						            }
						            
									if(canAdd)
						            	lst2.options.add(new Option(text,value));
						         }
						   }
						}
					}
				
					//
					function chk(frm) {
						var dList = frm.dst;
						var grpmember = '';
						var len = dList.options.length;
						var lName = frm.grpname.value.replace(/^ +/, "").replace(/ +$/, "");
					
						// we permit space, for en-us or europe people groupname has white space
						if (!new RegExp("^[_a-zA-Z0-9 \\u0081-\\uffff]+$","g").test(lName)) {
							alert('组名 只能是8bit、英文、数字和下划线的组合！');
							return false;
						}
					
						//alert(lName);
						frm.grpname.value = lName; // after cleanup
					
						for (var i=0; i < len; i++) {
							if (i< len-1) {
								grpmember += dList.options[i].value + '|';
							} else {
								grpmember += dList.options[i].value;
							}
						}
						frm.grpmember.value = grpmember;
						alert(grpmember);
						
						return true;
					}
					
					//
					function cfm_delete() {
						if(confirm("通讯录信息将被永久删除，确定吗?"))
						{
							document.getElementById("delete").value=1;
							document.getElementById("myfrm").submit();
							return true;
						}
						else
						{
							return false;
						}
					}
					
					function DoCompose(lst) {
						var List = document.getElementById(lst);
						var mail = '';
						for(var i=0;i<List.options.length;i++) {
							var e = List.options[i];
							if (mail) {
								mail += ', ' + e.value;
							} else {
								mail = e.value;
							}
						}
						if (mail) {
							// must call escape() to encode UTF8 string to ucs4
							mail = escape(mail);
							document.location.href='compose.cgi?sid=e6d4b745710e2f0b9239bdf60ecde383&to='+mail + '&atag=1';
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
						<TD class="tdmain" vAlign="top">
						
						  <FORM method="post" ACTION="{$base}/address/group?session-id={$session-id}" id="myfrm" name="myfrm" onSubmit="return chk(this);">
						      <TABLE height="100%" cellSpacing="0" cellPadding="0" width="100%">
						        <TBODY>
						        <TR>
						          <TD class="tdmain_in_tit" height="28">
						          	<SPAN style="LEFT: 0px; FLOAT: left">
						            	<SPAN class="pl10 b">编辑组 (请填写组名,选择组中人员)</SPAN>
						            	<SPAN class="pl10 b"></SPAN>
						           	</SPAN>
						           	
						           	<SPAN style="PADDING-RIGHT: 10px; FLOAT: right">
						           		<A href="#">&lt;&lt;返回通讯录 </A>
						            </SPAN>
						           </TD>
						        </TR>
						        
						        <xsl:variable name="current" select="/USERMODEL/CURRENT[@type='group']/@id"/>
						        
						        <TR>
						          <TD class="tdmain_in_con">
						          	<!-- <xsl:value-of select="$current"/> -->
						            <DIV style="PADDING-RIGHT: 5px; PADDING-LEFT: 10px; PADDING-BOTTOM: 5px; PADDING-TOP: 5px"> 
						            	<INPUT size="34" value="{normalize-space(/USERMODEL/USERDATA/ADDRESS/GROUPLIST/GROUP[@id=$current]/@name)}" style="WIDTH: 200px;" name="grpname"/> 
						            </DIV>
						            
						            <TABLE style="FONT-SIZE: 12px; BACKGROUND: #fff; MARGIN-LEFT: 10px; WIDTH: 90%" cellSpacing="0" cellPadding="0">
						              <TBODY>
						              <TR>
						                <TD>
						                	<SELECT id="src" style="WIDTH: 200px; HEIGHT: 250px" multiple="true" size="10" name="src"> 
						                    	<xsl:for-each select="/USERMODEL/USERDATA/ADDRESS/PERSONLIST/PERSON">
						                    		<option value="{normalize-space(@id)}"><xsl:value-of select="AB_NAME"/> (<xsl:value-of select="AB_EMAIL"/>)</option>
						                    	</xsl:for-each>
						                    </SELECT> 
						                </TD>
						                <TD style="PADDING-RIGHT: 10px; PADDING-LEFT: 10px; PADDING-BOTTOM: 10px; PADDING-TOP: 10px">
						                	<INPUT onClick="Add('src', 'dst');" type="button" value="》增加"/> 
						                	<BR/><BR/>
											<INPUT onClick="Del('dst');" type="button" value="《删除"/> 
										</TD>
						                <TD width="100%">
						                	<SELECT id="dst" style="WIDTH: 200px; HEIGHT: 250px" multiple="true" size="10" name="dst"> 
						                    	 <xsl:for-each select="/USERMODEL/USERDATA/ADDRESS/GROUPLIST/GROUP[@id=$current]/PERSON">
						                    		<xsl:variable name="abccc" select="./@id"> </xsl:variable>
						                    		<option value="{normalize-space(@id)}">
						                    			<xsl:value-of select="/USERMODEL/USERDATA/ADDRESS/PERSONLIST/PERSON[@id=$abccc]/AB_NAME"/>  (<xsl:value-of select="/USERMODEL/USERDATA/ADDRESS/PERSONLIST/PERSON[@id=$abccc]/AB_EMAIL"/>)
						                    		</option>
						                    	</xsl:for-each>
						                    </SELECT>
						                </TD>
						               </TR>
						               </TBODY>
						             </TABLE>
						            </TD>
						        </TR>
						        <TR>
						          <TD height="28">
						          	<SPAN class="navsbl">
						          		<INPUT type="hidden" name="method" value="save"/>
						          		<INPUT type="hidden" name="sid" value="{$current}"/>
						          		<INPUT type="hidden" name="grpmember"/>
						          		
						          		<INPUT type="submit" value=" 保存修改 "/> 
						          		&#160;
						          		<INPUT onclick="cfm_delete();" type="button" value=" 删除 "/> 
						          		
						          	</SPAN>
						         </TD>
						       </TR>
						       </TBODY>
						       </TABLE>
						    </FORM>
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
	
	
</xsl:stylesheet>
