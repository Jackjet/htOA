<?xml version="1.0" encoding="gb2312"?>


<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  	<xsl:output method="html" indent="yes"/>

    <xsl:variable name="imgbase" select="/USERMODEL/STATEDATA/VAR[@name='img base uri']/@value"/>
    <xsl:variable name="base" select="/USERMODEL/STATEDATA/VAR[@name='base uri']/@value"/>
    <xsl:variable name="session-id" select="/USERMODEL/STATEDATA/VAR[@name='session id']/@value"/>
     <!-- 取名称为INBOX的文件夹Id --> 
    <xsl:variable name="inboxId" select="/USERMODEL/MAILHOST_MODEL/FOLDER[@name='INBOX']/@id"/>
            
    <xsl:include href="head.xsl"/>
    <xsl:include href="userleft.xsl"/>   
   	<xsl:include href="bottom.xsl"/>
   	
   	
   
  <xsl:template match="/">		
    <HTML>
      <HEAD>
        <TITLE>WebMail Mailbox for <xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/></TITLE>
				<META CONTENT="AUTHOR" VALUE="kwchina"/>			
				<LINK media="screen" href="{$imgbase}/css/newstyle.css" type="text/css" rel="stylesheet"/>
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
						BORDER-RIGHT: #ccc 1px solid; BORDER-TOP: #ccc 1px solid; MARGIN: 20px 10px 0px; BORDER-LEFT: #ccc 1px solid; WIDTH: 98%; BORDER-BOTTOM: #ccc 1px solid
					}
				</STYLE>				
      </HEAD>
			
			<SCRIPT src="{$imgbase}/js/common.js" type="text/javascript"></SCRIPT>
			<SCRIPT src="{$imgbase}/js/menu.js" type="text/javascript"></SCRIPT>
			<SCRIPT language="javascript">
				<xsl:comment><![CDATA[
				function Select(tab){
					for(i=1; i <4; i++){
						if (i==tab) { 
							document.getElementById("Tab"+i).className="tab-selected";
							document.getElementById("Content"+i).style.display="block";
						}else{
							document.getElementById("Tab"+i).className="";
							document.getElementById("Content"+i).style.display="none";
						}
					}
				}
			
			    function getOptsValue(sel,str)
				    {
							alert(sel+str);    
				     for (var opts=0; opts<sel.length; ++opts) {
				          if (sel.options[opts].value==str)
				            {
				            sel.options[opts].selected=true;
				            return true;
				            }
				        }
				     return false;
				   }
   
				var currentNav = "nav_option";
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
						<xsl:call-template name="userLeft"/>
						<!-- Left Part End -->
						
						<TD class="td1px"></TD>
						
						
						<!-- Main Content Part -->
						<TD class="tdmain" valign="top">
						
							 <FORM name="preForm" action="{$base}/usernormal/submit?session-id={$session-id}" method="post">
					      <TABLE height="100%" cellSpacing="0" cellPadding="0" width="100%">
					        <TBODY>
					        <TR>
					          <TD class="tdmain_in_tit" height="28"><SPAN 
					            style="LEFT: 0px; FLOAT: left"><SPAN class="pl10 b">常规设置</SPAN> 
					            <SPAN></SPAN></SPAN><SPAN 
					            style="PADDING-RIGHT: 10px; FLOAT: right"></SPAN>
					          </TD>
					        </TR>
					        
					        <TR>
					          <TD class="tdmain_in_con">
					            <DIV id="tabwin" name="tabwin">
					            <UL>
					              <LI class="tab-selected" id="Tab1" onClick="Select('1');">个人设置 </LI>
					              <LI class="tab" id="Tab2" onClick="Select('2');">写邮件设置 </LI>
					              <LI class="tab" id="Tab3" onClick="Select('3');">读邮件设置 </LI>
					              <!--<LI class="tab" id="Tab4" onClick="Select('4');">POP3有关设置 </LI>
					              <LI class="tab" id="Tab5" onClick="Select('5');">显示设置 
					              <DIV class="cleard"></DIV></LI>
					              -->
					            </UL>
							  
				           
					            <DIV class="content" id="Content1">
						            <TABLE class="content-tab" cellSpacing="5" cellPadding="5">
						              <TBODY>
							              <TR>
							                <TD class="optionkey">姓名</TD>
							                <TD><INPUT name="FULLNAME" VALUE="{normalize-space(/USERMODEL/USERDATA/FULL_NAME)}" size="40"/> &#160; 
							                <SPAN class="tip">该名称将出现在外发邮件的 
							                  <B>From</B> 信头中</SPAN></TD>
							              </TR>
							              
							              <TR>
							                <TD class="optionkey">邮件地址</TD>
							                <TD><INPUT name="EMAIL" VALUE="{normalize-space(/USERMODEL/USERDATA/EMAIL)}" size="40"/> &#160; 
							                </TD>
							              </TR>
							              <!--
							              <TR>
							                <TD class="optionkey">旧密码</TD>
							                <TD><INPUT type="password" size="30" name="oldpw"/></TD></TR>
							              <TR>
							                <TD class="optionkey">新密码</TD>
							                <TD><INPUT type="password" size="30" name="newpw1"/></TD></TR>
							              <TR>
							                <TD class="optionkey">重输一次</TD>
							                <TD><INPUT type="password" size="30" name="newpw2"/></TD></TR>
							              <TR>
							                <TD></TD>
							                <TD>
							                	<SPAN class="tip">修改密码，如不需要修改则请务必留空
							                  <DIV></DIV>
							                  </SPAN>
							                </TD>
							              </TR>
							              -->
						              </TBODY>
						            </TABLE>
						       		</DIV>
								 
								 		
					            <DIV class="content" id="Content2" style="DISPLAY: none">
						            <TABLE class="content-tab" cellSpacing="5" cellPadding="5">
						              <TBODY>
						              <TR>
						                <TD class="optionkey">草稿保存</TD>
						                <TD>
							                <xsl:choose>
															  <xsl:when test="/USERMODEL/USERDATA/BOOLVAR[@name='save sent messages']/@value = 'yes'">
															    <INPUT TYPE="CHECKBOX" NAME="boolvar%save sent messages" checked="checked"/>
															  </xsl:when>
															  <xsl:otherwise>
															    <INPUT TYPE="CHECKBOX" NAME="boolvar%save sent messages"/>
															  </xsl:otherwise>
															</xsl:choose>
						                  将所有已发送的邮件保存至“<B>发件箱</B>”中 </TD>
						              </TR>
						              
						              <TR>
						                <TD class="optionkey">RTF邮件</TD>
						                <TD>
						                	<xsl:choose>
															  <xsl:when test="/USERMODEL/USERDATA/BOOLVAR[@name='first show html editor']/@value = 'yes'">
															    <INPUT TYPE="CHECKBOX" NAME="boolvar%first show html editor" checked="checked"/>
															  </xsl:when>
															  <xsl:otherwise>
															    <INPUT TYPE="CHECKBOX" NAME="boolvar%first show html editor"/>
															  </xsl:otherwise>
															</xsl:choose>					       
						                  是否首先打开RTF（富文本格式）编辑器？勾选将在编写邮件时打开 
						                </TD>
						              </TR>
						              
						              <TR>
						                <TD class="optionkey">邮址自动保存</TD>
						                <TD>
						                	<xsl:choose>
															  <xsl:when test="/USERMODEL/USERDATA/BOOLVAR[@name='auto save address']/@value = 'yes'">
															    <INPUT TYPE="CHECKBOX" NAME="boolvar%auto save address" checked="checked"/>
															  </xsl:when>
															  <xsl:otherwise>
															    <INPUT TYPE="CHECKBOX" NAME="boolvar%auto save address"/>
															  </xsl:otherwise>
															</xsl:choose>		
						                  发信时自动将邮件地址保存到地址本中 </TD>
						              </TR>
						              
						              <!--
						              <TR>
						                <TD class="optionkey">外发邮件编码:</TD>
						                <TD><INPUT type="radio" value="1" name="trylocal"/>对于外发邮件使用默认文本编码<BR/>
						                		<INPUT type="radio" CHECKED="true" name="trylocal" vlaue="0"/>对于外发邮件使用 Unicode (UTF-8) 编码<BR/>
						                </TD>
						              </TR>
						              -->
						              <TR>
						                <TD class="optionkey" valign="top">个性签名</TD>
						                <TD>
							                <TEXTAREA name="SIGNATURE" rows="10" cols="50">
							                	<xsl:value-of select="/USERMODEL/USERDATA/SIGNATURE"/>
							                </TEXTAREA>
							                <BR/>
							                个性化签名档 - 将附加在所有外发邮件的末尾 
						            		</TD>
						            	</TR>
						            	</TBODY>
						            </TABLE>
					            </DIV>
				            
				            
				            <DIV class="content" id="Content3" style="DISPLAY: none">
				            <TABLE class="content-tab" cellSpacing="5" cellPadding="5">
				              <TBODY>
				              <!--
				              <TR>
				                <TD class="optionkey">显示HTML信件</TD>
				                <TD>
				                	<INPUT type="checkbox" CHECKED="true" name="show_html"/> 
				                  优先选择按HTML格式显示邮件，如果关闭则优先显示文本格式 </TD>
				              </TR>
				              
				              <TR>
				                <TD class="optionkey">链接转换</TD>
				                <TD><INPUT type="checkbox" CHECKED="true" name="conv_link"/> 
				                  将文本格式邮件中的链接转换成超文本链接 </TD>
				              </TR>
				              -->
				              
				              <TR>
				                <TD class="optionkey">会话数量</TD>
				                <TD>				                	
				                	<INPUT TYPE="TEXT" NAME="intvar%max show messages" size="5" class="testoNero" VALUE="{/USERMODEL/USERDATA/INTVAR[@name='max show messages']/@value}"/>
				                   每页显示的邮件数量 
				                </TD>
				              </TR>
				              
				              <!--
				              <TR>
				                <TD class="optionkey">屏幕大小</TD>
				                <TD>
				                		<SELECT name="screen_type"> 
				                			<OPTION value="auto" selected="true">Auto</OPTION> 
				                			<OPTION value="screen1">800x600</OPTION> 
				                			<OPTION value="screen2">1024x768</OPTION> 
				                			<OPTION value="screen3">1280x1024</OPTION>
				                		</SELECT> 这将影响列表邮件时标题等长度自动截取特性 
				                </TD>
				              </TR>
				             
				              
				              <TR>
				                <TD class="optionkey">排列方式</TD>
				                <TD>
				                		<SELECT name="sort"> 
				                				<OPTION value="Sz">邮件大小</OPTION> 
				                    		<OPTION value="Dt" selected="true">日期</OPTION> 
				                    		<OPTION value="Fs">已/未读</OPTION> 
				                    		<OPTION value="Sj">标题</OPTION> 
				                    		<OPTION value="Fr">来信人</OPTION> 
				                    		<OPTION value="Ts">邮件时间戳</OPTION>
				                    </SELECT> 
				                  	默认的邮件索引排列顺序 
				                </TD>
				              </TR>
				               -->
				              
				              <TR>
				                <TD class="optionkey">邮件删除方式 </TD>
				                <TD>
				                		<xsl:choose>
															  <xsl:when test="/USERMODEL/USERDATA/BOOLVAR[@name='direct delete messages']/@value = 'yes'">
															    <INPUT type="radio" name="boolvar%direct delete messages"  CHECKED="true" value="yes"/>
															    点删除时直接删除，邮件不会转移到垃圾箱<BR/>
															    <INPUT type="radio" name="boolvar%direct delete messages" vlaue="no"/>
															    点删除时邮件被移动到垃圾箱（默认）
															    <BR/>
															  </xsl:when>
															  <xsl:otherwise>
															    <INPUT type="radio" name="boolvar%direct delete messages" value="yes"/>
															    点删除时直接删除，邮件不会转移到垃圾箱<BR/>
															    <INPUT type="radio" name="boolvar%direct delete messages"  CHECKED="true"  vlaue="no"/>
															    点删除时邮件被移动到垃圾箱（默认）
															    <BR/>
															  </xsl:otherwise>
															</xsl:choose>		
				                </TD>
				              </TR>
				             
				              </TBODY>
				              </TABLE>
				            </DIV>
				           
				            
				           <!--
				            <DIV class=content id=Content4 style="DISPLAY: none">
				            <TABLE class=content-tab cellSpacing=5 cellPadding=5>
				              <TBODY>
				              <TR>
				                <TD class=optionkey>激活POP3</TD>
				                <TD><INPUT type=checkbox CHECKED name=pop_on> 查信时自动收取POP3邮件 
				              </TD></TR>
				              <TR>
				                <TD class=optionkey>连接超时</TD>
				                <TD><SELECT name=pop_timeout> &gt; <OPTION 
				                    selected>15</OPTION> &gt; <OPTION>30</OPTION></SELECT> (秒) 
				              </TD></TR>
				              <TR>
				                <TD class=optionkey>每次帐号接收：</TD>
				                <TD><SELECT name=pop_files> &gt; <OPTION 
				                    selected>15</OPTION> &gt; <OPTION>30</OPTION> &gt; 
				                    <OPTION>50</OPTION> &gt; <OPTION>100</OPTION></SELECT> (封邮件) 
				                </TD></TR></TBODY></TABLE></DIV>
				                
				                
				            <DIV class=content id=Content5 style="DISPLAY: none">
				            <TABLE class=content-tab cellSpacing=5 cellPadding=5>
				              <TBODY>
				              <TR>
				                <TD class=optionkey>时区/时差</TD>
				                <TD><SELECT name=timezone> <OPTION value=+1300>GMT +13:00 
				                    小时</OPTION> <OPTION value=+1200>GMT +12:00 小时</OPTION> 
				                    <OPTION value=+1100>GMT +11:00 小时</OPTION> <OPTION 
				                    value=+1000>GMT +10:00 小时</OPTION> <OPTION value=+0900>GMT 
				                    +09:00 小时</OPTION> <OPTION value=+0800 selected>GMT +08:00 
				                    小时</OPTION> <OPTION value=+0700>GMT +07:00 小时</OPTION> 
				                    <OPTION value=+0600>GMT +06:00 小时</OPTION> <OPTION 
				                    value=+0500>GMT +05:00 小时</OPTION> <OPTION value=+0400>GMT 
				                    +04:00 小时</OPTION> <OPTION value=+0300>GMT +03:00 
				                    小时</OPTION> <OPTION value=+0200>GMT +02:00 小时</OPTION> 
				                    <OPTION value=+0100>GMT +01:00 小时</OPTION> <OPTION 
				                    value=+0000>GMT 00:00 小时</OPTION> <OPTION value=-0100>GMT 
				                    -01:00 小时</OPTION> <OPTION value=-0200>GMT -02:00 
				                    小时</OPTION> <OPTION value=-0300>GMT -03:00 小时</OPTION> 
				                    <OPTION value=-0400>GMT -04:00 小时</OPTION> <OPTION 
				                    value=-0500>GMT -05:00 小时</OPTION> <OPTION value=-0600>GMT 
				                    -06:00 小时</OPTION> <OPTION value=-0700>GMT -07:00 
				                    小时</OPTION> <OPTION value=-0800>GMT -08:00 小时</OPTION> 
				                    <OPTION value=-0900>GMT -09:00 小时</OPTION> <OPTION 
				                    value=-1000>GMT -10:00 小时</OPTION> <OPTION value=-1100>GMT 
				                    -11:00 小时</OPTION></SELECT> 您所在地的时区与时差，用于时间差转换 </TD></TR>
				              <TR>
				                <TD class=optionkey>语言</TD>
				                <TD><SELECT name=lang> <OPTION value=zh_TW>中文（繁體）</OPTION> 
				                    <OPTION value=zh_CN selected>中文（简体）</OPTION> <OPTION 
				                    value=en_US>English (US)</OPTION></SELECT> 决定界面及发送邮件的默认字符编码 
				              </TD></TR>
				              <TR>
				                <TD class=optionkey>偏好界面</TD>
				                <TD><SELECT name=template> <OPTION value=default 
				                    selected>新版风格（IE5.5+/FF1.0+）</OPTION></SELECT> 
				            </TD></TR></TBODY></TABLE></DIV>
				           -->				        
						        </DIV> 
						        </TD>
						        </TR>
						       
						       <TR><TD height="30"></TD></TR>
						          
						        <TR>
						          <TD style="HEIGHT: 35px" align="middle">
						          		<INPUT type="submit" value="保存设置" name="dosave"/> &#160; 
						          		<INPUT type="reset" value="重新填写" name="reset"/> &#160; 
						          		&#160; 
						          		<!--<SPAN class="tip"> 注意：带(*)的选项暂不能使用 </SPAN>-->
						            </TD>
						        </TR>
				            
				            </TBODY>
				         </TABLE>
				      
				      </FORM>
						</TD>
						
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
