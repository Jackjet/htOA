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
			
			<STYLE>
				#tabwin UL {
					PADDING-RIGHT: 0px; PADDING-LEFT: 0px; PADDING-BOTTOM: 0px; MARGIN: 0px; VERTICAL-ALIGN: top; COLOR: #000; PADDING-TOP: 0px; LIST-STYLE-TYPE: none
				}
				#tabwin LI {
					BORDER-RIGHT: #fff 1px solid; PADDING-RIGHT: 8px; PADDING-LEFT: 8px; BACKGROUND: #fff; FLOAT: left; PADDING-BOTTOM: 3px; CURSOR: pointer; PADDING-TOP: 3px
				}
				#tabwin .tab-selected {
					PADDING-RIGHT: 8px; PADDING-LEFT: 8px; FONT-WEIGHT: bold; BACKGROUND: #d9e6f4; PADDING-BOTTOM: 3px; MARGIN: 0px; PADDING-TOP: 3px
				}
				#tabwin .cleard {
					CLEAR: both
				}
				#tabwin .content-tab {
					BORDER-RIGHT: #ccc 1px solid; BORDER-TOP: #ccc 1px solid; MARGIN: 0px; BORDER-LEFT: #ccc 1px solid; WIDTH: 98%; BORDER-BOTTOM: #ccc 1px solid
				}
				#tabwin INPUT {
					WIDTH: 200px
				}
				#tabwin .abkey {
					WIDTH: 100px
				}
			</STYLE>
			
			<SCRIPT language="javascript">
				<xsl:comment><![CDATA[
					function chk(frm) {
						if (!frm.ab_name.value || !frm.ab_email.value) {
							alert('邮件或姓名为空');
							return false;
						}
						if (frm.ab_email.value.replace(/^ +/, "").replace(/ $/, "").replace(/(,|^)(.|)+<([\w._]+@\w+\.(\w+\.){0,3}\w{2,4})>|(\,|^)([\w+._]+@\w+\.(\w+\.){0,3}\w{2,4})/g,"")!="") {
							alert('邮件格式不正确！');
							return false;
						}
						// we permit space, for en-us or europe people name has white space
						if (!new RegExp("^[_a-zA-Z0-9 \\u0081-\\uffff]*$","g").test(frm.ab_name.value)) {
							alert('姓名 只能是8bit、英文、数字和下划线的组合！');
							return false;
						}
						
						return true;
					}
					
					function Select(tab){
						for(i=1; i <3; i++){
							if (i==tab) { 
								document.getElementById("Tab"+i).className="tab-selected";
								document.getElementById("Content"+i).style.display="block";
							}else{
								document.getElementById("Tab"+i).className="";
								document.getElementById("Content"+i).style.display="none";
							}
						}
					}
					
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
					
					function DoCompose() {
						var form = document.getElementById("myfrm");
						var mail = form.ab_email.value;
						var name = form.ab_name.value;
						if (mail) {
							var to = '<'+mail+'>';
							if (name) {
								to = '"'+name+'" '+to;
							}
							to = escape(to);
							document.location.href='compose.cgi?sid=e9ae4a349a786dce207b980606df95f2&to='+to + '&atab=1';
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
						
						  <FORM method="post" ACTION="{$base}/address/person?session-id={$session-id}" id="myfrm" name="myfrm" onSubmit="return chk(this);">
						      <TABLE height="100%" cellSpacing="0" cellPadding="0" width="100%">
						        <TBODY>
						        <TR>
						          <TD class="tdmain_in_tit" height="28">
						          	<SPAN style="LEFT: 0px; FLOAT: left">
						            	<SPAN class="pl10 b">增加 联系人信息</SPAN>
						            	<SPAN class="pl10 b"></SPAN>
						           	</SPAN>
						           	
						           	<SPAN style="PADDING-RIGHT: 10px; FLOAT: right">
						           		<A href="#">&lt;&lt;返回通讯录 </A>
						            </SPAN>
						           </TD>
						        </TR>
						        
						        <xsl:variable name="current" select="/USERMODEL/CURRENT[@type='person']/@id"/>
						        
						        <TR>
						          <TD class="tdmain_in_con">
						          	<TABLE style="FONT-SIZE: 12px; BACKGROUND: #fff; WIDTH: 80%" cellSpacing="0" cellPadding="0">
							              <TBODY>
							              <TR>
							                <TD colSpan="4">
							                  <DIV id="tabwin" style="PADDING-RIGHT: 10px; PADDING-LEFT: 10px; FONT-SIZE: 14px; 
											  			PADDING-BOTTOM: 10px; PADDING-TOP:10px" name="tabwin">
							                  	<UL>
													<LI class="tab-selected" id="Tab1" onClick="Select('1');">常用信息 
													</LI>
													<LI class="tab" id="Tab2" onClick="Select('2');">家庭信息 </LI>
													<!-- 
													<LI class="tab" id="Tab3" onClick="Select('3');">商务信息 </LI>
													<LI class="tab" id="Tab4" onClick="Select('4');">其他信息 
													
														<DIV class="cleard"></DIV>
													</LI>
													 -->
												</UL>
												
							                  	<DIV class="content" id="Content1">
												  <TABLE class="content-tab" cellSpacing="5" cellPadding="5">
													<TBODY>
													<TR>
													  <TD class="abkey">姓名</TD>
													  <TD><INPUT name="ab_name" value="{normalize-space(/USERMODEL/USERDATA/ADDRESS/PERSONLIST/PERSON[@id=$current]/AB_NAME)}"/></TD></TR>
													<TR>
													  <TD class="abkey">电邮</TD>
													  <TD><INPUT name="ab_email" value="{normalize-space(/USERMODEL/USERDATA/ADDRESS/PERSONLIST/PERSON[@id=$current]/AB_EMAIL)}"/></TD></TR>
													<TR>
													  <TD class="abkey">昵称</TD>
													  <TD><INPUT name="ab_nickname" value="{normalize-space(/USERMODEL/USERDATA/ADDRESS/PERSONLIST/PERSON[@id=$current]/AB_NICKNAME)}"/></TD></TR>
													<TR>
													  <TD class="abkey">移动电话</TD>
													  <TD><INPUT name="ab_mobile" value="{normalize-space(/USERMODEL/USERDATA/ADDRESS/PERSONLIST/PERSON[@id=$current]/AB_MOBILE)}"/></TD></TR>
													<TR>
													  <TD class="abkey">工作/职位</TD>
													  <TD><INPUT name="ab_job" value="{normalize-space(/USERMODEL/USERDATA/ADDRESS/PERSONLIST/PERSON[@id=$current]/AB_JOB)}"/></TD></TR>
													<TR>
													  <TD class="abkey">Gtalk号</TD>
													  <TD><INPUT name="ab_imgoogle" value="{normalize-space(/USERMODEL/USERDATA/ADDRESS/PERSONLIST/PERSON[@id=$current]/AB_IMGOOGLE)}"/></TD></TR>
													<TR>
													  <TD class="abkey">MSN号</TD>
													  <TD><INPUT name="ab_immsn" value="{normalize-space(/USERMODEL/USERDATA/ADDRESS/PERSONLIST/PERSON[@id=$current]/AB_IMMSN)}"/></TD></TR>
													<TR>
													  <TD class="abkey">QQ号码</TD>
													  <TD><INPUT name="ab_imqq" value="{normalize-space(/USERMODEL/USERDATA/ADDRESS/PERSONLIST/PERSON[@id=$current]/AB_IMQQ)}"/></TD></TR>
													<TR>
													  <TD class="abkey">Skype号</TD>
													  <TD><INPUT name="ab_imskype" value="{normalize-space(/USERMODEL/USERDATA/ADDRESS/PERSONLIST/PERSON[@id=$current]/AB_IMSKYPE)}"/></TD>
													 </TR></TBODY>
													</TABLE>
											  </DIV>
							                  <DIV class="content" id="Content2" style="DISPLAY: none">
												  <TABLE class="content-tab" cellSpacing="5" cellPadding="5">
													<TBODY>
													<TR>
													  <TD class="abkey">家庭电话</TD>
													  <TD><INPUT name="ab_hometel" value="{normalize-space(/USERMODEL/USERDATA/ADDRESS/PERSONLIST/PERSON[@id=$current]/AB_HOMETEL)}"/></TD></TR>
													<TR>
													  <TD class="abkey">家庭地址</TD>
													  <TD><INPUT name="ab_homeaddress" value="{normalize-space(/USERMODEL/USERDATA/ADDRESS/PERSONLIST/PERSON[@id=$current]/AB_HOMEADDRESS)}"/></TD></TR>
													<TR>
													  <TD class="abkey">城市</TD>
													  <TD><INPUT name="ab_homecity" value="{normalize-space(/USERMODEL/USERDATA/ADDRESS/PERSONLIST/PERSON[@id=$current]/AB_HOMECITY)}"/></TD></TR>
													<TR>
													  <TD class="abkey">省份</TD>
													  <TD><INPUT name="ab_homestate" value="{normalize-space(/USERMODEL/USERDATA/ADDRESS/PERSONLIST/PERSON[@id=$current]/AB_HOMESTATE)}"/></TD></TR>
													<TR>
													  <TD class="abkey">邮政编码</TD>
													  <TD><INPUT name="ab_homezip" value="{normalize-space(/USERMODEL/USERDATA/ADDRESS/PERSONLIST/PERSON[@id=$current]/AB_HOMEZIP)}"/></TD></TR>
													<TR>
													  <TD class="abkey">国家</TD>
													  <TD><INPUT name="ab_homecountry" value="{normalize-space(/USERMODEL/USERDATA/ADDRESS/PERSONLIST/PERSON[@id=$current]/AB_HOMECOUNTRY)}"/></TD></TR></TBODY>
													</TABLE>
											  </DIV>
											  											  
											</DIV></TD></TR></TBODY>
										</TABLE>
						            </TD>
						        </TR>
						        <TR>
						          <TD height="28">
						          	<SPAN class="navsbl">
						          		<INPUT type="hidden" name="method" value="save"/>
						          		<INPUT type="hidden" name="sid" value="{$current}"/>
						          		
						          		<INPUT type="submit" value=" 保存信息 " name="editsave"/> 
						          		&#160;
						          		 <INPUT onClick="javaScript:window.history.go(-1)" type="button" value=" 取消 "/> 
						          		<!-- <INPUT onClick="DoCompose('dst');" type="button" value=" 写邮件 "/>  -->
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
