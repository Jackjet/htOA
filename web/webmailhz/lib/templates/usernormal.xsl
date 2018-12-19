<?xml version="1.0" encoding="gb2312"?>


<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  	<xsl:output method="html" indent="yes"/>

    <xsl:variable name="imgbase" select="/USERMODEL/STATEDATA/VAR[@name='img base uri']/@value"/>
    <xsl:variable name="base" select="/USERMODEL/STATEDATA/VAR[@name='base uri']/@value"/>
    <xsl:variable name="session-id" select="/USERMODEL/STATEDATA/VAR[@name='session id']/@value"/>
     <!-- ȡ����ΪINBOX���ļ���Id --> 
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
					            style="LEFT: 0px; FLOAT: left"><SPAN class="pl10 b">��������</SPAN> 
					            <SPAN></SPAN></SPAN><SPAN 
					            style="PADDING-RIGHT: 10px; FLOAT: right"></SPAN>
					          </TD>
					        </TR>
					        
					        <TR>
					          <TD class="tdmain_in_con">
					            <DIV id="tabwin" name="tabwin">
					            <UL>
					              <LI class="tab-selected" id="Tab1" onClick="Select('1');">�������� </LI>
					              <LI class="tab" id="Tab2" onClick="Select('2');">д�ʼ����� </LI>
					              <LI class="tab" id="Tab3" onClick="Select('3');">���ʼ����� </LI>
					              <!--<LI class="tab" id="Tab4" onClick="Select('4');">POP3�й����� </LI>
					              <LI class="tab" id="Tab5" onClick="Select('5');">��ʾ���� 
					              <DIV class="cleard"></DIV></LI>
					              -->
					            </UL>
							  
				           
					            <DIV class="content" id="Content1">
						            <TABLE class="content-tab" cellSpacing="5" cellPadding="5">
						              <TBODY>
							              <TR>
							                <TD class="optionkey">����</TD>
							                <TD><INPUT name="FULLNAME" VALUE="{normalize-space(/USERMODEL/USERDATA/FULL_NAME)}" size="40"/> &#160; 
							                <SPAN class="tip">�����ƽ��������ⷢ�ʼ��� 
							                  <B>From</B> ��ͷ��</SPAN></TD>
							              </TR>
							              
							              <TR>
							                <TD class="optionkey">�ʼ���ַ</TD>
							                <TD><INPUT name="EMAIL" VALUE="{normalize-space(/USERMODEL/USERDATA/EMAIL)}" size="40"/> &#160; 
							                </TD>
							              </TR>
							              <!--
							              <TR>
							                <TD class="optionkey">������</TD>
							                <TD><INPUT type="password" size="30" name="oldpw"/></TD></TR>
							              <TR>
							                <TD class="optionkey">������</TD>
							                <TD><INPUT type="password" size="30" name="newpw1"/></TD></TR>
							              <TR>
							                <TD class="optionkey">����һ��</TD>
							                <TD><INPUT type="password" size="30" name="newpw2"/></TD></TR>
							              <TR>
							                <TD></TD>
							                <TD>
							                	<SPAN class="tip">�޸����룬�粻��Ҫ�޸������������
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
						                <TD class="optionkey">�ݸ屣��</TD>
						                <TD>
							                <xsl:choose>
															  <xsl:when test="/USERMODEL/USERDATA/BOOLVAR[@name='save sent messages']/@value = 'yes'">
															    <INPUT TYPE="CHECKBOX" NAME="boolvar%save sent messages" checked="checked"/>
															  </xsl:when>
															  <xsl:otherwise>
															    <INPUT TYPE="CHECKBOX" NAME="boolvar%save sent messages"/>
															  </xsl:otherwise>
															</xsl:choose>
						                  �������ѷ��͵��ʼ���������<B>������</B>���� </TD>
						              </TR>
						              
						              <TR>
						                <TD class="optionkey">RTF�ʼ�</TD>
						                <TD>
						                	<xsl:choose>
															  <xsl:when test="/USERMODEL/USERDATA/BOOLVAR[@name='first show html editor']/@value = 'yes'">
															    <INPUT TYPE="CHECKBOX" NAME="boolvar%first show html editor" checked="checked"/>
															  </xsl:when>
															  <xsl:otherwise>
															    <INPUT TYPE="CHECKBOX" NAME="boolvar%first show html editor"/>
															  </xsl:otherwise>
															</xsl:choose>					       
						                  �Ƿ����ȴ�RTF�����ı���ʽ���༭������ѡ���ڱ�д�ʼ�ʱ�� 
						                </TD>
						              </TR>
						              
						              <TR>
						                <TD class="optionkey">��ַ�Զ�����</TD>
						                <TD>
						                	<xsl:choose>
															  <xsl:when test="/USERMODEL/USERDATA/BOOLVAR[@name='auto save address']/@value = 'yes'">
															    <INPUT TYPE="CHECKBOX" NAME="boolvar%auto save address" checked="checked"/>
															  </xsl:when>
															  <xsl:otherwise>
															    <INPUT TYPE="CHECKBOX" NAME="boolvar%auto save address"/>
															  </xsl:otherwise>
															</xsl:choose>		
						                  ����ʱ�Զ����ʼ���ַ���浽��ַ���� </TD>
						              </TR>
						              
						              <!--
						              <TR>
						                <TD class="optionkey">�ⷢ�ʼ�����:</TD>
						                <TD><INPUT type="radio" value="1" name="trylocal"/>�����ⷢ�ʼ�ʹ��Ĭ���ı�����<BR/>
						                		<INPUT type="radio" CHECKED="true" name="trylocal" vlaue="0"/>�����ⷢ�ʼ�ʹ�� Unicode (UTF-8) ����<BR/>
						                </TD>
						              </TR>
						              -->
						              <TR>
						                <TD class="optionkey" valign="top">����ǩ��</TD>
						                <TD>
							                <TEXTAREA name="SIGNATURE" rows="10" cols="50">
							                	<xsl:value-of select="/USERMODEL/USERDATA/SIGNATURE"/>
							                </TEXTAREA>
							                <BR/>
							                ���Ի�ǩ���� - �������������ⷢ�ʼ���ĩβ 
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
				                <TD class="optionkey">��ʾHTML�ż�</TD>
				                <TD>
				                	<INPUT type="checkbox" CHECKED="true" name="show_html"/> 
				                  ����ѡ��HTML��ʽ��ʾ�ʼ�������ر���������ʾ�ı���ʽ </TD>
				              </TR>
				              
				              <TR>
				                <TD class="optionkey">����ת��</TD>
				                <TD><INPUT type="checkbox" CHECKED="true" name="conv_link"/> 
				                  ���ı���ʽ�ʼ��е�����ת���ɳ��ı����� </TD>
				              </TR>
				              -->
				              
				              <TR>
				                <TD class="optionkey">�Ự����</TD>
				                <TD>				                	
				                	<INPUT TYPE="TEXT" NAME="intvar%max show messages" size="5" class="testoNero" VALUE="{/USERMODEL/USERDATA/INTVAR[@name='max show messages']/@value}"/>
				                   ÿҳ��ʾ���ʼ����� 
				                </TD>
				              </TR>
				              
				              <!--
				              <TR>
				                <TD class="optionkey">��Ļ��С</TD>
				                <TD>
				                		<SELECT name="screen_type"> 
				                			<OPTION value="auto" selected="true">Auto</OPTION> 
				                			<OPTION value="screen1">800x600</OPTION> 
				                			<OPTION value="screen2">1024x768</OPTION> 
				                			<OPTION value="screen3">1280x1024</OPTION>
				                		</SELECT> �⽫Ӱ���б��ʼ�ʱ����ȳ����Զ���ȡ���� 
				                </TD>
				              </TR>
				             
				              
				              <TR>
				                <TD class="optionkey">���з�ʽ</TD>
				                <TD>
				                		<SELECT name="sort"> 
				                				<OPTION value="Sz">�ʼ���С</OPTION> 
				                    		<OPTION value="Dt" selected="true">����</OPTION> 
				                    		<OPTION value="Fs">��/δ��</OPTION> 
				                    		<OPTION value="Sj">����</OPTION> 
				                    		<OPTION value="Fr">������</OPTION> 
				                    		<OPTION value="Ts">�ʼ�ʱ���</OPTION>
				                    </SELECT> 
				                  	Ĭ�ϵ��ʼ���������˳�� 
				                </TD>
				              </TR>
				               -->
				              
				              <TR>
				                <TD class="optionkey">�ʼ�ɾ����ʽ </TD>
				                <TD>
				                		<xsl:choose>
															  <xsl:when test="/USERMODEL/USERDATA/BOOLVAR[@name='direct delete messages']/@value = 'yes'">
															    <INPUT type="radio" name="boolvar%direct delete messages"  CHECKED="true" value="yes"/>
															    ��ɾ��ʱֱ��ɾ�����ʼ�����ת�Ƶ�������<BR/>
															    <INPUT type="radio" name="boolvar%direct delete messages" vlaue="no"/>
															    ��ɾ��ʱ�ʼ����ƶ��������䣨Ĭ�ϣ�
															    <BR/>
															  </xsl:when>
															  <xsl:otherwise>
															    <INPUT type="radio" name="boolvar%direct delete messages" value="yes"/>
															    ��ɾ��ʱֱ��ɾ�����ʼ�����ת�Ƶ�������<BR/>
															    <INPUT type="radio" name="boolvar%direct delete messages"  CHECKED="true"  vlaue="no"/>
															    ��ɾ��ʱ�ʼ����ƶ��������䣨Ĭ�ϣ�
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
				                <TD class=optionkey>����POP3</TD>
				                <TD><INPUT type=checkbox CHECKED name=pop_on> ����ʱ�Զ���ȡPOP3�ʼ� 
				              </TD></TR>
				              <TR>
				                <TD class=optionkey>���ӳ�ʱ</TD>
				                <TD><SELECT name=pop_timeout> &gt; <OPTION 
				                    selected>15</OPTION> &gt; <OPTION>30</OPTION></SELECT> (��) 
				              </TD></TR>
				              <TR>
				                <TD class=optionkey>ÿ���ʺŽ��գ�</TD>
				                <TD><SELECT name=pop_files> &gt; <OPTION 
				                    selected>15</OPTION> &gt; <OPTION>30</OPTION> &gt; 
				                    <OPTION>50</OPTION> &gt; <OPTION>100</OPTION></SELECT> (���ʼ�) 
				                </TD></TR></TBODY></TABLE></DIV>
				                
				                
				            <DIV class=content id=Content5 style="DISPLAY: none">
				            <TABLE class=content-tab cellSpacing=5 cellPadding=5>
				              <TBODY>
				              <TR>
				                <TD class=optionkey>ʱ��/ʱ��</TD>
				                <TD><SELECT name=timezone> <OPTION value=+1300>GMT +13:00 
				                    Сʱ</OPTION> <OPTION value=+1200>GMT +12:00 Сʱ</OPTION> 
				                    <OPTION value=+1100>GMT +11:00 Сʱ</OPTION> <OPTION 
				                    value=+1000>GMT +10:00 Сʱ</OPTION> <OPTION value=+0900>GMT 
				                    +09:00 Сʱ</OPTION> <OPTION value=+0800 selected>GMT +08:00 
				                    Сʱ</OPTION> <OPTION value=+0700>GMT +07:00 Сʱ</OPTION> 
				                    <OPTION value=+0600>GMT +06:00 Сʱ</OPTION> <OPTION 
				                    value=+0500>GMT +05:00 Сʱ</OPTION> <OPTION value=+0400>GMT 
				                    +04:00 Сʱ</OPTION> <OPTION value=+0300>GMT +03:00 
				                    Сʱ</OPTION> <OPTION value=+0200>GMT +02:00 Сʱ</OPTION> 
				                    <OPTION value=+0100>GMT +01:00 Сʱ</OPTION> <OPTION 
				                    value=+0000>GMT 00:00 Сʱ</OPTION> <OPTION value=-0100>GMT 
				                    -01:00 Сʱ</OPTION> <OPTION value=-0200>GMT -02:00 
				                    Сʱ</OPTION> <OPTION value=-0300>GMT -03:00 Сʱ</OPTION> 
				                    <OPTION value=-0400>GMT -04:00 Сʱ</OPTION> <OPTION 
				                    value=-0500>GMT -05:00 Сʱ</OPTION> <OPTION value=-0600>GMT 
				                    -06:00 Сʱ</OPTION> <OPTION value=-0700>GMT -07:00 
				                    Сʱ</OPTION> <OPTION value=-0800>GMT -08:00 Сʱ</OPTION> 
				                    <OPTION value=-0900>GMT -09:00 Сʱ</OPTION> <OPTION 
				                    value=-1000>GMT -10:00 Сʱ</OPTION> <OPTION value=-1100>GMT 
				                    -11:00 Сʱ</OPTION></SELECT> �����ڵص�ʱ����ʱ�����ʱ���ת�� </TD></TR>
				              <TR>
				                <TD class=optionkey>����</TD>
				                <TD><SELECT name=lang> <OPTION value=zh_TW>���ģ����w��</OPTION> 
				                    <OPTION value=zh_CN selected>���ģ����壩</OPTION> <OPTION 
				                    value=en_US>English (US)</OPTION></SELECT> �������漰�����ʼ���Ĭ���ַ����� 
				              </TD></TR>
				              <TR>
				                <TD class=optionkey>ƫ�ý���</TD>
				                <TD><SELECT name=template> <OPTION value=default 
				                    selected>�°���IE5.5+/FF1.0+��</OPTION></SELECT> 
				            </TD></TR></TBODY></TABLE></DIV>
				           -->				        
						        </DIV> 
						        </TD>
						        </TR>
						       
						       <TR><TD height="30"></TD></TR>
						          
						        <TR>
						          <TD style="HEIGHT: 35px" align="middle">
						          		<INPUT type="submit" value="��������" name="dosave"/> &#160; 
						          		<INPUT type="reset" value="������д" name="reset"/> &#160; 
						          		&#160; 
						          		<!--<SPAN class="tip"> ע�⣺��(*)��ѡ���ݲ���ʹ�� </SPAN>-->
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
