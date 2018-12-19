<%@ page contentType="text/html; charset=gbk"%>
<%@ include file="/inc/taglibs.jsp"%>


<html>
<LINK media=screen href="<c:url value='/webmailhz'/>/css/style.css" type=text/css rel=stylesheet>
<LINK media=screen href="<c:url value='/webmailhz'/>/css/default.css" type=text/css rel=stylesheet>
<LINK media=screen href="<c:url value='/webmailhz'/>/css/example.css" type=text/css rel=stylesheet>

<LINK media=screen href="<c:url value='/webmailhz'/>/css/newstyle.css" type=text/css rel=stylesheet/>
<LINK media=screen href="<c:url value='/webmailhz'/>/css/text-overflow.css" type=text/css rel=stylesheet/>

<SCRIPT src="<c:url value='/webmailhz'/>/js/common.js" type=text/javascript></SCRIPT>
<SCRIPT src="<c:url value='/webmailhz'/>/js/menu.js" type=text/javascript></SCRIPT>
<SCRIPT src="<c:url value='/js'/>/inc_javascript.js" type=text/javascript></SCRIPT>

<LINK media=screen href="<c:url value='/webmailhz'/>/css/newstyle.css" type=text/css rel=stylesheet>
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

<SCRIPT language=javascript>
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
		
		var obj;
		obj = window.parent.document.getElementById("left");
		obj.height = document.body.scrollHeight				
	}	
	
	String.prototype.inc=function(k1,k2){return k2==null?this.indexOf(k1)>-1?true:false:(k2+this+k2).indexOf(k2+k1+k2)>-1?true:false}
						String.prototype.trim=function(){return this.replace(/(^[\s]*)|([\s]*$)/g, "")}
						String.prototype._slice="".slice
						String.prototype.slice=function(n1,n2){var v,b1=typeof(n1)=="number",b2=typeof(n2)=="number";if(!b1||typeof(n2)=="string"){v=eval("this._slice("+(b1?n1:this.indexOf(n1)+(n2==null?1:0)+(this.indexOf(n1)==-1?this.length:0))+(n2==null?"":(b2?n2:(this.indexOf(n2)==-1?"":","+this.indexOf(n2))))+")")}else{v=isIE5&&n1<0&&n2==null?this._slice(this.length-1):eval("this._slice(n1"+(n2==null?"":","+n2)+")")}return v}
						String.prototype.css=function(key,def){var n1,n2,l=this;if(key=="")return "";if((";"+l+";").indexOf(";"+key+";")>-1)return true;n1=(";"+l+":").indexOf(";"+key+":");if(n1==-1)return def==null?"":def;n1+=(key+":").length;n2=(";"+l+";").indexOf(";",n1+1);return l.slice(n1,n2-1)}
						String.prototype.qv=function(key){var l=this.replace("?","&")+"&",n1,n2;n1=l.indexOf("&"+key+"=");if(n1==-1)return "";n1=l.indexOf("=",n1)+1;n2=l.indexOf("&",n1);return l.slice(n1,n2)}
						String.prototype.toArray=function(key){var l=this,v;if(key==null)key="|";v=key;if(key=="n"){l=l.replace(/\r/g,"");v="\n"};l=l.replace(new RegExp("(\\"+key+")+","g"),v).replace(new RegExp("^[\\"+key+"]*|[\\"+key+"]+$","g"),"");return l==""?new Array():l.split(v)}
						Array.prototype.add=function(key){this[this.length]=key}
						
						//检验填写信息
						function fm_chk(fm){
								var a,i,l,name,rule;
								for(i=0;i<fm.length;i++) {
									name=fm[i].getAttribute("chkName");
									rule=fm[i].getAttribute("chkRule");
									
									if(name==null)
										continue;
									
									//alert(name);
									
									l="";
									if(rule.inc("cpwd","/")==true && fm[i].value!=fm[i-1].value)
										l=name+"不一致！";
									if(rule.inc(">=4","/")==true && fm[i].value.length<4)
										l=name+"不得少于4个字符！";
									if(rule.inc("<=255","/")==true && fm[i].value.length>255)
										l=name+"不得多于255个字符！";
									if(rule.inc("中a1_","/")==true && !new RegExp("^[_a-zA-Z0-9\\u4E00-\\u9FA5\\uF900-\\uFA2D]*$","g").test(fm[i].value))
										l=name+"只能是8bit、英文、数字和下划线的组合！"
									if(rule.inc("a1","/")==true && !new RegExp("^[a-zA-Z0-9]*$","g").test(fm[i].value))
										l=name+"只能为英文字母和数字的组合！";
									if(rule.inc("mobile","/")==true && !new RegExp("^13(\\d{9})+$").test(fm[i].value))
										l="手机号码格式不正确！";
									if(rule.inc("mobiles","/")==true && fm[i].value.replace(/，/g,",").replace(/(,|^)13\d{9}/g,"").replace(/\,/g,"")!="")
										l="手机号码格式不正确！";
									if(rule.inc("eml","/")==true && !new RegExp("^[\\w._]+@\\w+\.(\\w+\.){0,3}\\w{2,4}$","g").test(fm[i].value.replace(/-|\//g,"")) && fm[i].value!="")
										l=name+"邮件格式不正确！";
									if(rule.inc("emls","/")==true && fm[i].value!="")
										if(fm[i].value.replace(/ |-|\//g,"").replace(/;|；|，/g,",").replace(/(,|^)(.|)+<([\w._]+@\w+\.(\w+\.){0,3}\w{2,4})>|(\,|^)([\w+._]+@\w+\.(\w+\.){0,3}\w{2,4})/g,"").replace(/\,/,"")!="")
											l=name+"邮件格式不正确！";
									if(rule.inc("eml_nickname","/")==true && new RegExp("[,|;|<|>]","g").test(fm[i].value))
										l=name+"不能含有特殊字符！";
									if(rule.inc("notnull","/")==true && fm[i].value=="")
										l=name+"不能为空！";
										
									if(l!=""){
										//msg(l,"parent.document."+fm.name+"["+i+"].focus()")
										alert(l);
										return false;
									}
								}
								
								//alert(123435);								
								return true;
						}
												
						function chk() {		
							//alert(123);												
							var FR = document.forms['mailForm'];
							return fm_chk(FR);
						}
</SCRIPT>
  
 <body bgcolor="#ffffff">    
      <DIV id="Main">
				<TABLE cellspacing="0" cellpadding="0" width="100%">
				  <TBODY>
				  	<TR>				  
						<TD id="MainLeft" vAlign="top" width="170" height="300">
					      <DIV id="DivSysFolder" style="CLEAR: both; MARGIN-TOP: 0px">
						      <UL id="">
						        <LI class="fdnav"><A href="<c:url value='/webmail/userConfig'/>.mdo?method=list"><b>常规设置</b></A></LI>
						        <LI class="fdnav"><A href="<c:url value="/webmail/folderSetup"/>.mdo?method=list">邮件夹</A></LI>
						        <!--
						        <LI class="fdnav"><A href="#">过滤器设置</A></LI>
						        <LI class="fdnav"><A href="#">自动回复设置</A> </LI>
						        <LI class="fdnav"><A href="#">POP3取信帐号</A> </LI>
						        <LI class="fdnav"><A href="#">白名单</A> </LI>
						        <LI class="fdnav"><A href="#">黑名单</A> </LI>
						         -->
						      </UL>
					      </DIV>
						</TD>

						
						<TD class="td1px"></TD>
						
						
						<!-- Main Content Part -->
						<TD class="tdmain" valign="top">
						
						<html:form method="post" action="/webmail/userConfig.mdo" onsubmit="return chk();">
							<html:hidden property="method" value="save"/>
							
					      <TABLE height="100%" cellSpacing="0" cellPadding="0" width="100%">
					        <TBODY>
					        <TR>
					          <TD class="tdmain_in_tit" height="28">
					          	<SPAN style="LEFT: 0px; FLOAT: left">
					          		<SPAN class="pl10 b">常规设置</SPAN> 
					            	<SPAN></SPAN>
					            </SPAN>
					            <SPAN style="PADDING-RIGHT: 10px; FLOAT: right"></SPAN>
					          </TD>
					        </TR>
					        
					        <TR>
					          <TD class="tdmain_in_con" valign="top">
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
							                <TD><INPUT name="FULLNAME" size="40" chkName="姓名" chkRule="notnull" value="${_User_Config.fullName}"/> &#160; 
							                <SPAN class="tip">该名称将出现在外发邮件的 
							                  <B>From</B> 信头中</SPAN></TD>
							              </TR>
							              
							              <TR>
							                <TD class="optionkey">邮件地址</TD>
							                <TD><INPUT name="EMAIL" size="40"  chkName="邮件地址" chkRule="notnull/emls" value="${_User_Config.email}"/> &#160; 
							                </TD>
							              </TR>
							              <!--
							              <TR>
							                <TD class="optionkey">旧密码</TD>
							                <TD><INPUT type="password" size="30" name="oldpw"/></TD></TR>
							               <TR>
							                <TD></TD>
							                <TD>
							                	<SPAN class="tip">修改密码，如不需要修改则请务必留空
							                  <DIV></DIV>
							                  </SPAN>
							                </TD>
							              </TR>
							             
							              <TR>
							                <TD class="optionkey">密码</TD>
							                <TD><INPUT type="password" size="30" name="PASSWORD" chkName="密码" chkRule="notnull" value="${_User_Config.password}"/></TD></TR>
							              <TR>
							                <TD class="optionkey">重输一次</TD>
							                <TD><INPUT type="password" size="30" name="VERIFY" chkName="密码" chkRule="cpwd" value="${_User_Config.password}"/></TD></TR>
							               -->
						              </TBODY>
						            </TABLE>
						       		</DIV>
								 
								 		
					            <DIV class="content" id="Content2" style="DISPLAY: none">
						            <TABLE class="content-tab" cellSpacing="5" cellPadding="5">
						              <TBODY>
						              <TR>
						                <TD class="optionkey">邮件保存</TD>
						                <TD>
						                	<c:choose>
							                	<c:when test="${_User_Config.saveSent}">
						                			<INPUT TYPE="CHECKBOX" NAME="boolvar%save sent messages" checked/>
						                		</c:when>
						                		<c:otherwise>
						                			<INPUT TYPE="CHECKBOX" NAME="boolvar%save sent messages"/>
						                		</c:otherwise>
						                	</c:choose>
							                  将所有已发送的邮件保存至
											<SELECT NAME="SENTFOLDER" class="testoNero">
											  	<c:forEach items="${requestScope._Folders}" var="folder" varStatus="status">
											  		<option value="${folder.folderId}">${folder.folderName}</option>
											  	</c:forEach>
											</SELECT>
											文件夹 
											
											<script language="javaScript">
												getOptsValue(mailForm.SENTFOLDER,"${_User_Config.sendFolder}");
											</script>	
											
										</TD>
						              </TR>
						              
						              <!--
						              <TR>
						                <TD class="optionkey">邮址自动保存</TD>
						                <TD>
						                	 <c:choose>
							                	<c:when test="${_User_Config.saveAddress}">
						                			<INPUT TYPE="CHECKBOX" NAME="boolvar%auto save address" checked/>
						                		</c:when>
						                		<c:otherwise>
						                			<INPUT TYPE="CHECKBOX" NAME="boolvar%auto save address"/>
						                		</c:otherwise>
						                	</c:choose>																
						                  	发信时自动将邮件地址保存到地址本中 
						                 </TD>
						              </TR>
						              
						              
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
							                	<TEXTAREA name="SIGNATURE" rows="10" cols="50">${_User_Config.signature}</TEXTAREA>
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
				                  	优先选择按HTML格式显示邮件，如果关闭则优先显示文本格式 
				              	</TD>
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
				                	<SELECT name="intvar%max show messages" id="showMax"> 				
					                	<option>10</option>
										<option>20</option>
										<option>50</option>
									</SELECT>           
				                   	每页显示的邮件数量
				                   	<script language="javaScript">
					                   	var sel;
					                   	sel = document.getElementById("showMax")
					                   	for (var opts=0; opts<sel.length; ++opts) {
								           if (sel.options[opts].value==${_User_Config.maxShow}) {								
								               sel.options[opts].selected=true;							           
								            }
								        }
										//getOptsValue(mailForm.intvar%max show messages,"${_User_Config.maxShow}");
									</script>				                   	
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
				                	<INPUT type="radio" name="boolvar%direct delete messages"  CHECKED="true" value="yes"/>
									点删除时直接删除
				                	
				                	<!-- 
				                	<c:choose>
							        	<c:when test="${_User_Config.directDelete}">	                		
											<INPUT type="radio" name="boolvar%direct delete messages"  CHECKED="true" value="yes"/>
											点删除时直接删除，邮件不会转移到垃圾箱<BR/>
									
											<INPUT type="radio" name="boolvar%direct delete messages" vlaue="no"/>
											点删除时邮件被移动到垃圾箱（默认）
										</c:when>
										<c:otherwise>
											<INPUT type="radio" name="boolvar%direct delete messages"  value="yes"/>
											点删除时直接删除，邮件不会转移到垃圾箱<BR/>
									
											<INPUT type="radio" name="boolvar%direct delete messages" CHECKED="true" vlaue="no"/>
											点删除时邮件被移动到垃圾箱（默认）
										</c:otherwise>
									</c:choose>
									 -->
									<BR/>
				                </TD>
				              </TR>
				             
				              </TBODY>
				              </TABLE>
				            </DIV>
				          		        
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
				      
				      </html:form>
				      
				      
				      
						</TD>
						
						<TD width="10"></TD>
					</TR>
				</TBODY>
				</TABLE>
				</DIV> 
		 </BODY>
    </HTML>

