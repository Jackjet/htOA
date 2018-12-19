<%@ page contentType="text/html; charset=gbk"%>
<%@ include file="/inc/taglibs.jsp"%>
<%@ include file="/inc/css.jsp"%>

<HTML>
<HEAD>
<TITLE></TITLE>
<link rel="stylesheet" href="<c:url value='/webmailhz'/>/css/autocomplete.css" type="text/css">
<LINK media=screen href="<c:url value='/webmailhz'/>/css/newstyle.css" type=text/css rel=stylesheet>

<script type="text/javascript" src="<c:url value='/webmailhz'/>/js/common.js"></script>
<script type="text/javascript" src="<c:url value='/webmailhz'/>/js/menu.js"></script>
<script src="<c:url value='/webmailhz'/>/js/MultiSelector.js" type="text/javaScript"></script>


<script type="text/javascript" src="<c:url value='/webmailhz'/>/js/jquery.js"></script>
<script type="text/javascript" src="<c:url value='/webmailhz'/>/js/jquery.autocomplete.js"></script>
<link rel="stylesheet" type="text/css" href="<c:url value='/webmailhz'/>/css/jquery.autocomplete.css" />

<script language=javascript>
						var allowsubmit = false;
						var issending = false;
						var issaving = false;
						var currentNav = 'nav_compose';
						
						var postminchars = parseInt('8');
						var postmaxchars = parseInt('50000');
						var disablepostctrl = parseInt('0');
						var typerequired = parseInt('');
						var bbinsert = parseInt('1');
						function escapeHTML(str)
						{
							str = str.replace(/&/g,'&amp;');
							str = str.replace(/'/g, '&#039;');
							str = str.replace(/"/g, '&quot;');
							str = str.replace(/>/g, '&gt;');
							str = str.replace(/</g, '&lt;');
							str = str.replace(/ /g, '&nbsp;');
							str = str.replace(/\r*\n/g, "<br>\n");
							
							return str;
						}
						
						function showerror(err) {
							var tab = $('warntab');
							tab.innerHTML = err;
							tab.style.display = 'block';
						
							issending = false;
							issaving = false;
							$('LoadingStatus').style.display = "none";
							allowsubmit = true;
							$('btnsend1').disabled = false;
							$('btnsend2').disabled = false;
							$('btnsave1').disabled = false;
							$('btnsave2').disabled = false;
						}
						
						function unescapeHTML(str)
						{
							//var div = document.createElement('div');
							str = str.replace(/\r*\n\s*/g, "");
							str = str.replace(/<\/div>/gi, "#nl#");
							str = str.replace(/<\/li>/gi,"#nl#");
							str = str.replace(/<\/P>/gi,"#nl#");
							str = str.replace(/<\s*\/?\s*BR\s*\/?\s*>/gi,"#nl#");
							str = str.replace(/<script[^>]*?>.*?<\/script>/gi,"");
							str = str.replace(/<[^!]*?[^<>]*?>/gi,"");
							//str = str.replace(/([\r\n])\s+/g, "$1");
							str = str.replace(/&(quot|#34);/gi, '"');
							str = str.replace(/&(amp|#38);/gi, '&');
							str = str.replace(/&(lt|#60);/gi, '<');
							str = str.replace(/&(gt|#62);/gi, '>');
							str = str.replace(/&(nbsp|#160);/gi, " ");
							str = str.replace(/&(iexcl|#161);/gi, String.fromCharCode(161));
							str = str.replace(/&(cent|#162);/gi, String.fromCharCode(162));
							str = str.replace(/&(pound|#163);/gi, String.fromCharCode(163));
							str = str.replace(/&(copy|#169);/gi, String.fromCharCode(169));
							str = str.replace(/&#(\d+);/gi, String.fromCharCode("$1"));
							str = str.replace(/#nl#/gi, "\r\n");
							return str;
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


						
						//检查收件人信息
						function chkMailTo() {
							var l,a
							l=document.mailForm.to.value+","+document.mailForm.cc.value+","+document.mailForm.bcc.value
							l=l.replace(/;/g,",").replace(/[\,]+/g,",")
							a=l.split(",")
							if(a.length>100){
								alert("收件人(包括抄送和密送)必须少于100人！")
								return false
							} else {
								return true
							}
						}
						
						function chk() {
							if (!allowsubmit){
								allowsubmit = false;
								return false;
							}
							
							var FR = document.forms['mailForm'];
							return fm_chk(FR);
						}
						
						function showWaiting() {
							var str;
							str = '<img src="/webmailhz/lib/templates/images/toploading.gif" align="absmiddle">&nbsp;';
							str = str + (document.mailForm.issend.value==1 ? "正在发送邮件, 请稍候" : "正在保存邮件, 请稍候")+ '...';
							$('LoadingStatus').innerHTML = str
							$('LoadingStatus').style.display = "block";
						}
						
						function DoSave() {
							if (issending)
							{
								alert('正在保存邮件, 请稍候');
								return false;
							}
							if (issaving)
							{
								alert('正在发送邮件, 请稍候');
								return false;
							}
							allowsubmit = true;
							var FR = document.forms['mailForm'];
							FR.issend.value = "";
							showWaiting();
							FR.dosave.value = '保存草稿';
							FR.dosend.value = '';
							if (wysiwyg)
							{
								FR.html.value=true;
								document.mailForm.body.value = getEditorContents();
							}
							else
							{
								FR.html.value="";
								document.mailForm.body.value = getEditorContents();
							}
							FR.subchk.value = "1";
							
							//FR.target = "proframe";
							/* FR.target = '_blank'; */
							//attchk();
							FR.submit();
							FR.subchk.value = "";
							issaving = true;
							allowsubmit = false;
						}
						
						//----------------------发送邮件--------------------------
						function DoSend() {
							if (issending) {
								alert('正在发送邮件, 请稍候');
								return false;
							}
							
							if (issaving) {
								alert('正在保存邮件, 请稍候');
								return false;
							}
						
							allowsubmit = true;
							var FR = document.forms['mailForm'];
							FR.dosave.value = '';
							FR.issend.value = "1";
							
							var isPass=fm_chk(FR);
							if(isPass && chkMailTo()){
								showWaiting()
							}else{
								allowsubmit = false;
								return false
							}
							
							//v=(isIE5&&!isIE5_5)||!isIE?glb.hte_win.document.body.innerHTML:glb.hte_win.innerHTML
							//document.fmRun.body.value="<style>p{margin:0}img{border:0}</style>\n<font style='font-size:12px'>"+v+"</font>"
														
							if (wysiwyg) {
								FR.html.value=true;
								document.mailForm.body.value = getEditorContents();
							}	else {
								FR.html.value="";
								document.mailForm.body.value = getEditorContents();
							}
						
							//alert(12345);
							//alert(document.mailForm.body.value);
														
							//FR.target = "proframe";
							//attchk();
							//$('btnsend1').disabled = true;
							//$('btnsend2').disabled = true;
							//$('btnsave1').disabled = true;
							//$('btnsave2').disabled = true;
							postSubmited = true;
							FR.dosend.value = '发送邮件';
							FR.subchk.value = "1";
							
							//发送
							FR.submit();
							
							issending = true;
							FR.subchk.value = "";
							allowsubmit = false;
						}
						//--------------------------------------------------------------
						
						
						function DoWarn(str)
						{
							var tab = $('warntab');
							tab.innerHTML = str;
							tab.style.display = 'block';
						
							issending = false;
							issaving = false;
							$('LoadingStatus').style.display = "none";
							allowsubmit = true;
							$('btnsend1').disabled = false;
							$('btnsend2').disabled = false;
							$('btnsave1').disabled = false;
							$('btnsave2').disabled = false;
						}
						
						function attachok(arrATT) {
						
							var FR = document.forms['mailForm'];
							var str = "";
							for (var i=1; i<=arrATT.length-1; i++)
							{
									str += '<li><input type=checkbox checked name="REMOVE-'+arrATT[i][0]+'"> '+ arrATT[i][1] + ' (' + arrATT[i][2] + ')</li>';
							}
							$('divAttach').innerHTML = '<ul id=ulAttach>' + str + '</ul>';
							FR.draft.value = arrATT[0][2];
							$('tdtAttach').innerHTML = '<input id="my_file_element" type="file" name="file_1"><div id="files_list" style="font-size: 12px;padding-top: 10px; line-height: 140%"></div>';
							var multi_selector = new MultiSelector( document.getElementById( 'files_list' ), 10 );
							multi_selector.addElement( document.getElementById( 'my_file_element' ) );
						}
						
						function attchk() {
							var FR = document.forms['mailForm'];
							for (var i=0;i<FR.elements.length;i++)	{
								var e = FR.elements[i];
								if  ((e.type=='checkbox')
									&& (e.name!='ccsent')
									&& (e.name!='priority')
									&& (e.name!='notification'))
								{
									e.checked = !e.checked;
									//e.style.display = "none";
									//e.disabled = true;
								}
							}
						}
						
						function sendok(str){
							$('warntab').style.display = 'none';
							//alert(str); // this str will be alert() by sendok/saveok html
							if(document.mailForm.issend.value==1)
							{
								$('LoadingStatus').innerHTML = "邮件已发送";
								window.location.href='folders.cgi?__mode=messages_list&sid=5802ce1b15554872f4e8f03bfcc9e16a&folder=Inbox';
							}
							else{
								$('LoadingStatus').innerHTML = "草稿已保存";
								issaving = false;
							}
						}
						
						function showInput(key){
							if(key=="cc"){
								$("acc").style.display="none"
								$("trcc").style.display=""
							}
							else{
								$("abcc").style.display="none"
								$("trbcc").style.display=""
							}
							if($("acc").style.display=="none"&&$("abcc").style.display=="none")
								$("tbMain").deleteRow(1)
								
							changePageHeight(22);
						}

	
						function changePageHeight(change){
							var obj;
							obj = window.parent.document.getElementById("left");
							var ht;
							ht = obj.height;
							//parent.SetCwinHeight(obj);
							//alert(ht);
							//alert(change);
							//alert(ht+change);
							obj.height = parseFloat(ht) + parseFloat(change);	
						}

	</script>
	
	<script type="text/javascript">
		var emails = [
			{ name: "Peter Pan", to: "peter@pan.de" },
			{ name: "Molly", to: "molly@yahoo.com" },
			{ name: "Forneria Marconi", to: "live@japan.jp" },
			{ name: "Master <em>Sync</em>", to: "205bw@samsung.com" },
			{ name: "Dr. <strong>Tech</strong> de Log", to: "g15@logitech.com" },
			{ name: "Don Corleone", to: "don@vegas.com" },
			{ name: "Mc Chick", to: "info@donalds.org" },
			{ name: "Donnie Darko", to: "dd@timeshift.info" },
			{ name: "Quake The Net", to: "webmaster@quakenet.org" },
			{ name: "Dr. Write", to: "write@writable.com" }
		];

		jq().ready(function() {			
			jq("to").autocomplete(emails, {
				multiple: true,
				matchContains: true,
				minChars: 0,
				width: 310,
				matchContains: "word",
				autoFill: false,
				formatItem: function(row, i, max) {
					return i + "/" + max + ": \"" + row.name + "\" [" + row.to + "]";
				},
				formatMatch: function(row, i, max) {
					return row.name + " " + row.to;
				},
				formatResult: function(row) {
					return row.to;
				}
			});				
		});
	</script>
</HEAD>

<BODY onkeydown="if(event.keyCode==27) return false;">
 <DIV id="LoadingStatus" style="position:absolute; right:0px; top:0px; background:#CC0000; height:18px; color:#fff;padding:2px 10px 1px 10px;display:none;">
     		<IMG src=<c:url value='/webmailhz'/>/images/toploading.gif" align="absmiddle"/>&#160;正在加载...
 </DIV>


<DIV id="Main">
	<html:form method="post" action="/webmail/mailMessage.do" enctype="multipart/form-data" onsubmit="return chk();">
		<html:hidden property="method" value="send"/>
		
		<INPUT type="hidden" name="issend" value="1"/>
		<INPUT type="hidden" name="dosave" value=""/>
		<INPUT type="hidden" name="dosend" value=""/>
		<INPUT type="hidden" name="subchk" value=""/>
		<INPUT type="hidden" name="html" id="html" value="true"/>
		
		<TABLE width=100% cellpadding=0 cellspacing=0>
			<TR>
				<!-- Left Part -->
				<TD id=MainLeft vAlign=top width=170 height="410">
					<SCRIPT language=javascript>				
						function Compose() {
							window.location.href="<c:url value='/webmail/mailMessage'/>.do?method=edit";
						}
				
						function LM_OCTRL(CLSNAME)	{
							var obj = document.getElementById(CLSNAME);
							if (obj.style.display=="none"){
								obj.style.display = "block";
								document.getElementById(CLSNAME + "_CTRLIMG").src = "images/lo.gif";
							}else{
								obj.style.display = "none";
								document.getElementById(CLSNAME + "_CTRLIMG").src = "images/lc.gif";
							}
						}
					</SCRIPT>
			
					<DIV style="TEXT-ALIGN: center">
						<INPUT class=lnbtn id=ckbtn onclick="" type=button value=列表>&nbsp; 
						<INPUT class=lnbtn id=cmbtn onclick=Compose() type=button value=写邮件>
					</DIV>
					
					<DIV id=DivSysFolder style="CLEAR: both; MARGIN-TOP: 0px">
						<DIV style="MARGIN-TOP: 5px; DISPLAY: block; HEIGHT: 20px">
							<DIV style="PADDING-LEFT: 5px; FONT-WEIGHT: bold; FLOAT: left; CURSOR: pointer; COLOR: #3b107b; PADDING-TOP: 2px" 
						  onclick="LM_OCTRL('LM_SYSFD');">
								<SPAN><IMG id=LM_SYSFD_CTRLIMG src="<c:url value='/webmailhz/images'/>/lo.gif"></SPAN> 系统邮件夹
							</DIV>
							<DIV class=btnlm></DIV>
						</DIV>
						<DIV id=LM_SYSFD>
							<UL id=LM_SYSFD_UL>
								<c:forEach items="${requestScope._Folders}" var="folder" varStatus="status">
									<LI class=fdnav><A HREF="<c:url value='/webmail/mailList'/>.do?method=list&folderId=${folder.folderId}&part=1">
										<c:choose>
											<c:when test="${folder.folderName=='Inbox' || folder.folderName=='INBOX'}">收件箱</c:when>
											<c:when test="${folder.folderName=='Drafts'}"> 草稿箱</c:when>
											<c:when test="${folder.folderName=='Junk'}">垃圾邮件</c:when>
											<c:when test="${folder.folderName=='Sent'}">发件箱</c:when>
											<c:when test="${folder.folderName=='Trash'}">垃圾箱</c:when>	
											<c:otherwise>其它</c:otherwise>						
										</c:choose>
										
										<c:choose>
											<c:when test="${folder.newMessage==0}">${folder.newMessage}/${folder.totalMessage}</c:when>
											<c:otherwise><FONT color="red">${folder.newMessage}</FONT>/${folder.totalMessage}</c:otherwise>
										</c:choose>								
									</A></LI>
								</c:forEach>
							</UL>
						</DIV>
					</DIV>
					
					<!-- 
					<DIV id=DivSysFolder style="CLEAR: both; MARGIN-TOP: 0px">
						<DIV style="MARGIN-TOP: 5px; DISPLAY: block; HEIGHT: 20px">
							<DIV style="PADDING-LEFT: 5px; FONT-WEIGHT: bold; FONT-SIZE: 12px; FLOAT: left; CURSOR: pointer; COLOR: #3b107b; PADDING-TOP: 2px" onClick="LM_OCTRL('LM_USRFD');">
								<SPAN><IMG id=LM_USRFD_CTRLIMG src="<c:url value='/webmailhz/images'/>/lo.gif"></SPAN> 我的文件夹 
							</DIV>
							<DIV class=btnlm style="PADDING-RIGHT: 10px; FONT-WEIGHT: normal; FONT-SIZE: 12px; FLOAT: right; COLOR: #3b107b; PADDING-TOP: 2px">
								<A href="#">[管理]</A>
							</DIV>
						</DIV>
						
						<DIV id=LM_USRFD>
							<UL id=LM_SYSFD_UL></UL>
						</DIV>
					</DIV>
					 -->
				</TD>
				
				<TD class=td1px width=1px></TD>
	
				<!-- Main Content Part -->
				<TD class="tdmain" valign="top">								
									<TABLE width="100%" cellpadding="0" cellspacing="0">
										<TR>
											<TD height="28px" class="TDmain_in_tit">
												<SPAN style="float:left;left:0px;">
													<SPAN class="pl10 b">写邮件</SPAN> 
													<SPAN></SPAN>
												</SPAN>										
											</TD>
										</TR>
										
										<!-- 
										<TR>
											<TD class="TDmain_in_topopt">
												<TABLE>
													<TR>
														<TD>
															<SPAN  class="navtbl">
																<INPUT id="btnsend1" type="button" value="发送邮件" onClick="DoSend();"/>
																<INPUT id="btnsave1" type="button" value="保存草稿" onClick="DoSave();"/>
															</SPAN>
														</TD>
														<TD>
															<SPAN id="warntab" style="display: none; background: #FFD363;padding: 2px; padding-left: 5px; padding-right: 5px; margin-left: 30px">
															</SPAN>
														</TD>
													</TR>
												</TABLE>
											</TD>
										</TR>
										 -->
										
										<TEXTAREA name="body" style="width:100%;height:200;overflow:auto;display:none"></TEXTAREA>
										
										
										<TR>
											<TD style="padding-top:5px;">
												<TABLE width="95%" cellpadding="0" cellspacing="2" id="tbMain">
													<TR id="TRto">
														<TD align="right" style="width:80"><SPAN class="bold">
														收件人:
														</SPAN></TD>
														<TD class="altbg2"> 
															<input type="text" id="to" name="to" chkName="收件人" chkRule="notnull/emls" style="width:100%;height:25" class="ACtype" tabindex="1" onKeyDown="ctlent(event);">													
														
														</TD>
													</TR>
													
													<TR id="TRBtcc" bgcolor="#F6F6F6" style="height:22">
														<TD style="width:80">&#160;</TD>
														<TD>
															<A id="acc" class="br" tabindex="2" title="" href="javaScript:showInput('cc')">抄送</A> 
															&#160;<A id="abcc" class="br" tabindex="3" title="" href="javaScript:showInput('bcc')">暗送</A>　　　　
															<!-- &#160;<FONT color="#808080">提示：您可以尝试邮件地址自动补齐特性，简化输入</FONT> -->
														</TD>
													</TR>
													<TR id="TRcc" bgcolor="#F6F6F6" style="display:none">
														<TD align="right" style="width:80">抄送:</TD>
														<TD>
															<input type="text" name="cc" chkName="抄送" chkRule="emls" style="width:100%;height:25" class="ACtype" tabindex="4" onKeyDown="ctlent(event);">
															
														</TD>
													</TR>
													<TR id="TRbcc" bgcolor="#F6F6F6" style="display:none">
														<TD align="right" style="width:80">暗送:</TD>
														<TD>
															<input type="text" name="bcc" chkName="暗送" chkRule="emls" style="width:100%;height:25" class="ACtype" tabindex="5" onKeyDown="ctlent(event);">
															
														</TD>
													</TR>
													<TR style="height:28">
														<TD align="right" style="width:80"><SPAN class="bold">
														主题:
														</SPAN></TD>
														<TD class="altbg2">
															<INPUT type="text" name="subject" id="subject" style="width:100%" onKeyDown="ctlent(event);" tabindex="6" VALUE=""/>
														</TD>
													</TR>
													<!-- 
													<TR bgcolor="#F6F6F6" style="height:27">
															<TD align="right" style="width:80">&#160;</TD>
															<TD style="padding-right:10">
															
															<INPUT tabindex="7" type="checkbox" name="ccsent" checked="true"/> 发送同时保存草稿到发件箱
															<INPUT tabindex="8" type="checkbox" name="priority" /> 紧急邮件
															<INPUT tabindex="9" type="checkbox" name="notification" />需要对方读信时发回执
														
															</TD>
													</TR>
														 -->
												</TABLE>
											</TD>
										</TR>
										
										<TR>
												<TD bgcolor="#EFEFEF" style="padding:5px 0px 5px 0px;border-top:1px solid #E1E1E1;">
													<TABLE>
													<TR>
														<TD align="right" valign="top" style="width:80;padding-top:3px;">
															附件:&#160;<IMG src="<c:url value='/webmailhz'/>/images/paperclip.gif" align="absmiddle"/>&#160;
														</TD>
														<TD id="TDAttach">
															<DIV width="200" style="border-bottom: 0px solid #aaa" id="DIVAttach">
																<UL id="ulAttach">&nbsp;</UL>
														 	</DIV>
															<DIV id="TDtAttach">
																<INPUT id="my_file_element" type="file" name="file_1" size="30"/>
																
																<!-- Multifiles upload secion -->
																<DIV id="files_list" style="font-size: 11px;padding-top: 10px; line-height: 140%"></DIV>
																<SCRIPT language="javaScript">																	
																	var multi_selector = new MultiSelector( document.getElementById( 'files_list' ), 10 );
																	multi_selector.addElement( document.getElementById( 'my_file_element' ) );																				
																</SCRIPT>																
																<!-- end of Multiflies upload -->
															</DIV>
														</TD>
													</TR>
													</TABLE>
											</TD>
										</TR>
										
										<TR>
										<TD class="TDmain_in_editor">
											
											<STYLE type="text/css">
												.swINPUT {
													BORDER-RIGHT: #7ac4ea 1px solid; BORDER-TOP: #7ac4ea 1px solid; FONT: 12px Tahoma, Verdana; BORDER-LEFT: #7ac4ea 1px solid; COLOR: #333333; BORDER-BOTTOM: #7ac4ea 1px solid; BACKGROUND-COLOR: #f5fbff
												}
												.altbg1 {height:26px;}
												.altbg2 {height:26px;}
												.wysiwyg {
													BORDER-RIGHT: #dddddd 1px solid; PADDING-RIGHT: 4px; BORDER-TOP: #dddddd 1px solid; PADDING-LEFT: 4px; PADDING-BOTTOM: 4px; FONT: 12px Tahoma, Verdana; BORDER-LEFT: #dddddd 1px solid; WORD-BREAK: break-all; PADDING-TOP: 4px; BORDER-BOTTOM: #dddddd 1px solid;
													font-size:12px;font-family:Arial,"宋体"
												}
												
												.wysiwyg A {
													COLOR: #154ba0; TEXT-DECORATION: underline
												}
												
												.editor {
													BORDER-RIGHT: #bbe9ff 0px solid; BORDER-TOP: #bbe9ff 0px solid; BORDER-LEFT: #bbe9ff 0px solid; BORDER-BOTTOM: #ccc 1px solid
												}
												.editor_line {
													BORDER-TOP: #ccc 1px solid
												}
												.editor_switcher {
													BACKGROUND: #f5fbff; MARGIN-LEFT: 10px; VERTICAL-ALIGN: middle; BORDER-BOTTOM: #ccc 1px solid; POSITION: relative; TOP: 1px;
													BORDER-RIGHT: #ccc 1px solid; BORDER-TOP: #ccc 1px solid; FONT: 12px Tahoma, Verdana; BORDER-LEFT: #ccc 1px solid; COLOR: #333333; BACKGROUND-COLOR: #efefef
												}
												.editor_switcher_highlight {
													FONT-WEIGHT: bold; BACKGROUND: #ffffff; MARGIN-LEFT: 10px; VERTICAL-ALIGN: middle; BORDER-BOTTOM: #ffffff 1px solid; POSITION: relative; TOP: 1px;
													BORDER-RIGHT: #ccc 1px solid; BORDER-TOP: #ccc 1px solid; FONT: 12px Tahoma, Verdana; BORDER-LEFT: #ccc 1px solid; COLOR: #333333; BACKGROUND-COLOR: #fff
												}
												.editor_text {
													BORDER-RIGHT: #ccc 1px solid; BORDER-TOP: 0px; BACKGROUND: #ffffff; FONT: 12px Tahoma, Verdana; BORDER-LEFT: #ccc 1px solid; BORDER-BOTTOM: #ccc 1px solid
												}
												.editor_text TEXTAREA {
													BORDER-RIGHT: 0px; PADDING-RIGHT: 4px; BORDER-TOP: 0px; PADDING-LEFT: 4px; BACKGROUND: #ffffff; PADDING-BOTTOM: 4px; OVERFLOW: auto; BORDER-LEFT: 0px; WORD-BREAK: break-all; PADDING-TOP: 4px; BORDER-BOTTOM: 0px
												}
												.editor_button {
													BORDER-RIGHT: #bbe9ff 0px solid; BORDER-TOP: #ccc 0px solid;  MARGIN-BOTTOM: 6px; BORDER-LEFT: #bbe9ff 0px solid; BORDER-BOTTOM: #bbe9ff 0px solid; POSITION: relative; TOP: 0px
												}
												.editor_textexpand {
													BORDER-TOP: 0px; FLOAT: RIGHT; POSITION: relative; TOP: 2px;RIGHT:5px;
												}
												.editor_buttonnormal {
													BORDER-RIGHT: medium none; PADDING-RIGHT: 1px; BORDER-TOP: medium none; PADDING-LEFT: 1px; BACKGROUND: #f5fbff; PADDING-BOTTOM: 1px; BORDER-LEFT: medium none; COLOR: #000000; PADDING-TOP: 1px; BORDER-BOTTOM: medium none
												}
												.editor_buttondown {
													BORDER-RIGHT: #ccc 1px solid; PADDING-RIGHT: 0px; BORDER-TOP: #ccc 1px solid; PADDING-LEFT: 0px; BACKGROUND: #efefef; PADDING-BOTTOM: 0px; BORDER-LEFT: #ccc 1px solid; COLOR: #000000; PADDING-TOP: 0px; BORDER-BOTTOM: #ccc 1px solid
												}
												.editor_buttonhover {
													BORDER-RIGHT: #ccc 1px solid; PADDING-RIGHT: 0px; BORDER-TOP: #ccc 1px solid; PADDING-LEFT: 0px; BACKGROUND: #efefef; PADDING-BOTTOM: 0px; BORDER-LEFT: #ccc 1px solid; COLOR: #000000; PADDING-TOP: 0px; BORDER-BOTTOM: #ccc 1px solid
												}
												.editor_buttonselected {
													BORDER-RIGHT: #ccc 1px solid; PADDING-RIGHT: 0px; BORDER-TOP: #ccc 1px solid; PADDING-LEFT: 0px; BACKGROUND: #efefef; PADDING-BOTTOM: 0px; BORDER-LEFT: #ccc 1px solid; COLOR: #000000; PADDING-TOP: 0px; BORDER-BOTTOM: #ccc 1px solid
												}
												.editor_menunormal {
													PADDING-RIGHT: 3px; PADDING-LEFT: 3px; BACKGROUND: #ffffff; PADDING-BOTTOM: 0px; FONT: 11px tahoma; OVERFLOW: hidden; COLOR: #000000; PADDING-TOP: 0px; WHITE-SPACE: nowrap; HEIGHT: 18px
												}
												.editor_menuhover {
													PADDING-RIGHT: 3px; PADDING-LEFT: 3px; BACKGROUND: #ffffff; PADDING-BOTTOM: 0px; FONT: 11px tahoma; OVERFLOW: hidden; COLOR: #000000; PADDING-TOP: 0px; WHITE-SPACE: nowrap; HEIGHT: 18px
												}
												.editor_menunormal {
													BORDER-RIGHT: #ffffff 1px solid
												}
												.editor_menuhover {
													BORDER-RIGHT: #ccc 1px solid
												}
												.editor_menuhover DIV {
													BACKGROUND: #ffffff; COLOR: #000000
												}
												.editor_menunormal DIV {
													BACKGROUND: #ffffff; COLOR: #000000
												}
												.editor_colormenunormal {
													BORDER-RIGHT: #ffffff 1px solid
												}
												.editor_colormenuhover {
													BORDER-RIGHT: #ccc 1px solid;
												}
												.editor_colornormal {
													PADDING-RIGHT: 2px; PADDING-LEFT: 2px; FONT-SIZE: 1px; PADDING-BOTTOM: 2px; PADDING-TOP: 2px;
												}
												.editor_colorhover {
													PADDING-RIGHT: 2px; PADDING-LEFT: 2px; FONT-SIZE: 1px; BACKGROUND: #ccc; PADDING-BOTTOM: 2px; PADDING-TOP: 2px; WHITE-SPACE: nowrap
												}
												.editor_colornormal DIV {
													BORDER-RIGHT: #92a05a 1px solid; BORDER-TOP: #92a05a 1px solid; BORDER-LEFT: #92a05a 1px solid; WIDTH: 10px; BORDER-BOTTOM: #92a05a 1px solid; HEIGHT: 10px;font-size:2px
												}
												.editor_colorhover DIV {
													BORDER-RIGHT: #ccc 1px solid; BORDER-TOP: #ccc 1px solid; BORDER-LEFT: #ccc 1px solid; WIDTH: 10px; BORDER-BOTTOM: #ccc 1px solid; HEIGHT: 10px;font-size:2px
												}
												.popupmenu_popup {
													BORDER-RIGHT: #ccc 1px solid; BORDER-TOP: #ccc 1px solid; BORDER-LEFT: #ccc 1px solid; COLOR: #154ba0; BORDER-BOTTOM: #ccc 1px solid;background-color:#fff;
												}
												.popupmenu_option {
													PADDING-RIGHT: 8px; PADDING-LEFT: 8px; BACKGROUND: #d9eef9; PADDING-BOTTOM: 3px; FONT: 12px Tahoma, Verdana; CURSOR: pointer; COLOR: #154ba0; PADDING-TOP: 3px; WHITE-SPACE: nowrap;background-color:#fff;
												}
												.popupmenu_option A {
													PADDING-RIGHT: 8px; PADDING-LEFT: 8px; PADDING-BOTTOM: 3px; COLOR: #154ba0; PADDING-TOP: 3px; TEXT-DECORATION: none
												}
												.headermenu_popup {
													BORDER-RIGHT: #ccc 1px solid; BORDER-TOP: 0px; MARGIN-TOP: 8px; BORDER-LEFT: #ccc 1px solid; COLOR: #154ba0; BORDER-BOTTOM: #ccc 1px solid
												}
												.headermenu_popup A {
													COLOR: #154ba0; TEXT-DECORATION: none;
												}
												.popupmenu_highlight {
													PADDING-RIGHT: 8px; PADDING-LEFT: 8px; BACKGROUND: #e1e1e1; PADDING-BOTTOM: 3px; FONT: 12px Tahoma, Verdana; CURSOR: pointer; COLOR: #ffffff; PADDING-TOP: 3px; WHITE-SPACE: nowrap
												}
												.popupmenu_highlight A {
													PADDING-RIGHT: 8px; PADDING-LEFT: 8px; PADDING-BOTTOM: 3px; COLOR: #ffffff; PADDING-TOP: 3px; TEXT-DECORATION: none
												}
											</STYLE>
										
										
											<DIV class="spaceborder" style="width: 100%;border:0px solid #000;">
											<TABLE cellspacing="0" cellpadding="5" width="100%">										
												<TR class="bottom">										
													<TD align="left" class="altbg2" valign="top">										
														<DIV id="posteditor">
															
															<SCRIPT language="javaScript">																
																var editorid = 'posteditor';
																var wysiwyg = (is_ie || is_moz || (is_opera && opera.version() >= 9)) && parseInt('0') && bbinsert == 1 ? 1 : 0;
																var wysiwyg = 1;
																var allowswitcheditor = parseInt('1');
																//var allowhtml = parseInt('1');
																//var forumallowhtml = parseInt('0');
																//var allowsmilies = parseInt('1');
																//var allowbbcode = parseInt('1');
																//var allowimgcode = parseInt('1');
																//var smilies = new Array();
																
																var BORDERCOLOR = "#ccc";
																var ALTBG2 = "#FFFFFF";
																
																function previewpost(){
																if(!validate($('mailForm'), true)) {
																$('subject').focus();
																return;
																}
																$("previewmessage").innerHTML = bbcode2html($('mailForm').message.value);
																//$("previewmessage").innerHTML = $('mailForm').message.value;
																
																$("previewTABLE").style.display = '';
																window.scroll(0, 0);
																}
																
																function clearcontent() {
																	if(wysiwyg && bbinsert) {
																		editdoc.body.innerHTML = is_moz ? '<br />' : '';
																	} else {
																		textobj.value = '';
																	}
																}
																
																function resizeEditor(change) {
																	var editorbox = bbinsert ? editbox : textobj;
																	var newheight = parseInt(editorbox.style.height, 10) + change;
																	if(newheight >= 100) {
																		editorbox.style.height = newheight + 'px';
																	}
																	
																	changePageHeight(change);																																
																}																			
															</SCRIPT>
															
															<SCRIPT type="text/javaScript" src="<c:url value='/webmailhz'/>/js/editor.js"></SCRIPT>
															
															<SCRIPT language="javaScript">																
																	var lang = new Array();
																	lang["enter_email_link"]		= "请输入此链接的邮箱地址:";
																	lang['enter_table_rows']		= "请输入行数，最多 30 行:";
																	lang['enter_table_columns']		= "请输入列数，最多 30 列:";
																	lang['fontname']			= "字体";
																	lang['fontsize']			= "大小";
																	lang['cfm_fmtext']			= "若切换至纯文本，所有页面效果都将消失,是否继续?";
																	var custombbcodes = new Array();
																	custombbcodes["fly"] = "[fly]This is sample text[/fly]";
																	custombbcodes["qq"] = "[qq]688888[/qq]";
																	var fontoptions = new Array( "黑体", "宋体", "Tahoma", "Arial", "Impact", "Verdana");															
															</SCRIPT>
												
															<TABLE width="100%" cellpadding="0" cellspacing="0" border="0" class="editor">
																<TR>
																	<TD id="posteditor_conTRols_" class="editor_conTRolbar" colSPAN="2">
															
																		<DIV id="posteditor_switcher">
																		<INPUT type="button" id="bbcodemode" value="纯文本格式" onClick="switchEditor(0);" class="editor_switcher_highlight" tabindex="8"/>
																		<INPUT type="button" id="wysiwygmode" value="HTML格式" onClick="switchEditor(1)" class="editor_switcher" tabindex="9"/>
																		</DIV>		
															
																	</TD>
																</TR>
															</TABLE>
												
															<DIV id="posteditor_conTRols" style="border-left:1px solid #ccc;border-right:1px solid #ccc;border-bottom:1px solid #ccc;padding:1px;">
																<TABLE cellpadding="0" cellspacing="0" border="0">
																		<TR>
																
																			<TD><DIV class="editor_buttonnormal" id="posteditor_cmd_removeformat" onClick="discuzcode('removeformat')" onMouseOver="buttonContext(this, 'mouseover')" onMouseOut="buttonContext(this, 'mouseout')">
																				<IMG src="<c:url value='/webmailhz'/>/images/bb_removeformat.gif" width="21" height="20" title="清除文本格式" alt="清除文本格式" /></DIV>
																			</TD>
																			<TD><DIV class="editor_buttonnormal" id="posteditor_cmd_undo" onClick="discuzcode('undo')" onMouseOver="buttonContext(this, 'mouseover')" onMouseOut="buttonContext(this, 'mouseout')"><IMG src="<c:url value='/webmailhz'/>/images/bb_undo.gif" width="21" height="20" title="撤消" alt="撤消" /></DIV></TD>
																			<TD><DIV class="editor_buttonnormal" id="posteditor_cmd_redo" onClick="discuzcode('redo')" onMouseOver="buttonContext(this, 'mouseover')" onMouseOut="buttonContext(this, 'mouseout')"><IMG src="<c:url value='/webmailhz'/>/images/bb_redo.gif" width="21" height="20" title="重做" alt="重做" /></DIV></TD>
																			
																			<TD><IMG src="<c:url value='/webmailhz'/>/images/bb_separator.gif" width="6" height="20" alt=""/></TD>
																			
																			<TD><DIV class="editor_buttonnormal" id="posteditor_cmd_bold" onClick="discuzcode('bold')" onMouseOver="buttonContext(this, 'mouseover')" onMouseOut="buttonContext(this, 'mouseout')"><IMG src="<c:url value='/webmailhz'/>/images/bb_bold.gif" width="21" height="20" title="粗体" alt="粗体" /></DIV></TD>
																			<TD><DIV class="editor_buttonnormal" id="posteditor_cmd_italic" onClick="discuzcode('italic')" onMouseOver="buttonContext(this, 'mouseover')" onMouseOut="buttonContext(this, 'mouseout')"><IMG src="<c:url value='/webmailhz'/>/images/bb_italic.gif" width="21" height="20" title="斜体" alt="斜体" /></DIV></TD>
																			<TD><DIV class="editor_buttonnormal" id="posteditor_cmd_underline" onClick="discuzcode('underline')" onMouseOver="buttonContext(this, 'mouseover')" onMouseOut="buttonContext(this, 'mouseout')"><IMG src="<c:url value='/webmailhz'/>/images/bb_underline.gif" width="21" height="20" title="下划线" alt="下划线" /></DIV></TD>
																			
																			<TD><IMG src="<c:url value='/webmailhz'/>/images/bb_separator.gif" width="6" height="20" alt="" /></TD>
																			
																			<TD id="posteditor_popup_fontname" title="字体">
																				<DIV class="editor_buttonnormal" onMouseOver="menuContext(this, 'mouseover')" onMouseOut="menuContext(this, 'mouseout')">
																					<TABLE cellpadding="0" cellspacing="0" border="0" unselecTABLE="on">
																						<TR>
																						<TD class="editor_menunormal" unselecTABLE="on" id="posteditor_menu"><DIV id="posteditor_font_out" style="width:40px;overflow:hidden;" unselecTABLE="on">字体</DIV></TD>
																						<TD unselecTABLE="on"><IMG src="<c:url value='/webmailhz'/>/images/bb_menupop.gif" width="7" height="4" alt="" /></TD>
																						</TR>
																					</TABLE>
																					<SCRIPT language="javaScript">
																						menuregister(true, "posteditor_popup_fontname")																										
																					</SCRIPT>
																				</DIV>
																			</TD>
																			
																			<TD id="posteditor_popup_fontsize" title="大小">
																				<DIV class="editor_buttonnormal" onMouseOver="menuContext(this, 'mouseover')" onMouseOut="menuContext(this, 'mouseout')">
																					<TABLE cellpadding="0" cellspacing="0" border="0" unselecTABLE="on">
																					<TR>
																					<TD class="editor_menunormal" unselecTABLE="on" id="posteditor_menu"><DIV id="posteditor_size_out" style="width:25px" unselecTABLE="on">大小</DIV></TD>
																					<TD unselecTABLE="on"><IMG src="<c:url value='/webmailhz'/>/images/bb_menupop.gif" width="7" height="4" alt="" /></TD>
																					</TR>
																					</TABLE>
																					<SCRIPT language="javaScript">
																						menuregister(true, "posteditor_popup_fontsize")																								
																					</SCRIPT>
																				</DIV>
																			</TD>
																			
																			<TD id="posteditor_popup_forecolor" title="颜色">
																				<DIV class="editor_buttonnormal" onMouseOver="menuContext(this, 'mouseover')" onMouseOut="menuContext(this, 'mouseout')">
																					<TABLE cellpadding="0" cellspacing="0" border="0" unselecTABLE="on">
																					<TR>																						
																						<TD class="editor_colormenunormal" unselecTABLE="on" id="posteditor_colormenu">
																							<IMG src="<c:url value='/webmailhz'/>/images/bb_color.gif" width="21" height="16"/><BR/>
																							<IMG src="<c:url value='/webmailhz'/>/images/bb_clear.gif" id="posteditor_color_bar" style="background-color:black" width="21" height="4"/>
																						</TD>																					
																						<TD unselecTABLE="on"><IMG src="<c:url value='/webmailhz'/>/images/bb_menupop.gif" width="7" height="4" alt="" /></TD>
																					</TR>
																					</TABLE>
																					<SCRIPT language="javaScript">
																						menuregister(true, "posteditor_popup_forecolor")																									
																					</SCRIPT>																					
																				</DIV>
																			</TD>
																			
																			<TD><IMG src="<c:url value='/webmailhz'/>/images/bb_separator.gif" width="6" height="20" alt="" /></TD>
																			
																			<TD><DIV class="editor_buttonnormal" id="posteditor_cmd_justifyleft" onClick="discuzcode('justifyleft')" onMouseOver="buttonContext(this, 'mouseover')" onMouseOut="buttonContext(this, 'mouseout')"><IMG src="<c:url value='/webmailhz'/>/images/bb_left.gif" width="21" height="20" title="居左" alt="居左" /></DIV></TD>
																			<TD><DIV class="editor_buttonnormal" id="posteditor_cmd_justifycenter" onClick="discuzcode('justifycenter')" onMouseOver="buttonContext(this, 'mouseover')" onMouseOut="buttonContext(this, 'mouseout')"><IMG src="<c:url value='/webmailhz'/>/images/bb_center.gif" width="21" height="20" title="居中" alt="居中" /></DIV></TD>
																			<TD><DIV class="editor_buttonnormal" id="posteditor_cmd_justifyright" onClick="discuzcode('justifyright')" onMouseOver="buttonContext(this, 'mouseover')" onMouseOut="buttonContext(this, 'mouseout')"><IMG src="<c:url value='/webmailhz'/>/images/bb_right.gif" width="21" height="20" title="居右" alt="居右" /></DIV></TD>
																			
																			<TD><IMG src="<c:url value='/webmailhz'/>/images/bb_separator.gif" width="6" height="20" alt="" /></TD>
																			
																			<TD><DIV class="editor_buttonnormal" id="posteditor_cmd_insertorderedlist" onClick="discuzcode('insertorderedlist')" onMouseOver="buttonContext(this, 'mouseover')" onMouseOut="buttonContext(this, 'mouseout')"><IMG src="<c:url value='/webmailhz'/>/images/bb_orderedlist.gif" width="21" height="20" title="排序的列表" alt="排序的列表" /></DIV></TD>
																			<TD><DIV class="editor_buttonnormal" id="posteditor_cmd_insertunorderedlist" onClick="discuzcode('insertunorderedlist')" onMouseOver="buttonContext(this, 'mouseover')" onMouseOut="buttonContext(this, 'mouseout')"><IMG src="<c:url value='/webmailhz'/>/images/bb_unorderedlist.gif" width="21" height="20" title="未排序列表" alt="未排序列表" /></DIV></TD>
																			<TD><DIV class="editor_buttonnormal" id="posteditor_cmd_ouTDent" onClick="discuzcode('ouTDent')" onMouseOver="buttonContext(this, 'mouseover')" onMouseOut="buttonContext(this, 'mouseout')"><IMG src="<c:url value='/webmailhz'/>/images/bb_outdent.gif" width="21" height="20" title="减少缩进" alt="减少缩进" /></DIV></TD>
																			<TD><DIV class="editor_buttonnormal" id="posteditor_cmd_indent" onClick="discuzcode('indent')" onMouseOver="buttonContext(this, 'mouseover')" onMouseOut="buttonContext(this, 'mouseout')"><IMG src="<c:url value='/webmailhz'/>/images/bb_indent.gif" width="21" height="20" title="增加缩进" alt="增加缩进" /></DIV></TD>
																			<TD><IMG src="<c:url value='/webmailhz'/>/images/bb_separator.gif" width="6" height="20" alt="" /></TD>
																			<TD><DIV class="editor_buttonnormal" id="posteditor_cmd_createlink" onClick="discuzcode('createlink')" onMouseOver="buttonContext(this, 'mouseover')" onMouseOut="buttonContext(this, 'mouseout')"><IMG src="<c:url value='/webmailhz'/>/images/bb_url.gif" width="21" height="20" title="插入链接" alt="插入链接" /></DIV></TD>
																			<TD><DIV class="editor_buttonnormal" id="posteditor_cmd_unlink" onClick="discuzcode('unlink')" onMouseOver="buttonContext(this, 'mouseover')" onMouseOut="buttonContext(this, 'mouseout')"><IMG src="<c:url value='/webmailhz'/>/images/bb_unlink.gif" width="21" height="20" title="移除链接" alt="移除链接" /></DIV></TD>
																			<TD><DIV class="editor_buttonnormal" id="posteditor_cmd_email" onClick="discuzcode('email')" onMouseOver="buttonContext(this, 'mouseover')" onMouseOut="buttonContext(this, 'mouseout')"><IMG src="<c:url value='/webmailhz'/>/images/bb_email.gif" width="21" height="20" title="插入邮箱链接" alt="插入邮箱链接" /></DIV></TD>
																			<TD><DIV class="editor_buttonnormal" id="posteditor_cmd_TABLE" onClick="discuzcode('table')" onMouseOver="buttonContext(this, 'mouseover')" onMouseOut="buttonContext(this, 'mouseout')"><IMG src="<c:url value='/webmailhz'/>/images/bb_table.gif" width="21" height="20" title="插入表格" alt="插入表格" /></DIV></TD>
																		</TR>
																	</TABLE>
														
																	<DIV class="popupmenu_popup" id="posteditor_popup_fontname_menu" style="display: none">
																		<TABLE cellpadding="4" cellspacing="0" border="0" unselecTABLE="on">
																		<TR><TD class="popupmenu_option" onClick="discuzcode('fontname', '黑体')" unselecTABLE="on"><font face="黑体" unselecTABLE="on">黑体</font></TD></TR>
																		<TR><TD class="popupmenu_option" onClick="discuzcode('fontname', '宋体')" unselecTABLE="on"><font face="宋体" unselecTABLE="on">宋体</font></TD></TR>
																		<TR><TD class="popupmenu_option" onClick="discuzcode('fontname', 'Tahoma')" unselecTABLE="on"><font face="Tahoma" unselecTABLE="on">Tahoma</font></TD></TR>
																		<TR><TD class="popupmenu_option" onClick="discuzcode('fontname', 'Arial')" unselecTABLE="on"><font face="Arial" unselecTABLE="on">Arial</font></TD></TR>
																		<TR><TD class="popupmenu_option" onClick="discuzcode('fontname', 'Impact')" unselecTABLE="on"><font face="Impact" unselecTABLE="on">Impact</font></TD></TR>
																		<TR><TD class="popupmenu_option" onClick="discuzcode('fontname', 'Verdana')" unselecTABLE="on"><font face="Verdana" unselecTABLE="on">Verdana</font></TD></TR>
																		</TABLE>
																	</DIV>
														
																	<DIV class="popupmenu_popup" id="posteditor_popup_fontsize_menu" style="display: none">
																		<TABLE cellpadding="4" cellspacing="0" border="0" unselecTABLE="on">
																		<TR align="center"><TD class="popupmenu_option" onClick="discuzcode('fontsize', 1)" unselecTABLE="on"><font size="1" unselecTABLE="on">1</font></TD></TR>
																		<TR align="center"><TD class="popupmenu_option" onClick="discuzcode('fontsize', 2)" unselecTABLE="on"><font size="2" unselecTABLE="on">2</font></TD></TR>
																		<TR align="center"><TD class="popupmenu_option" onClick="discuzcode('fontsize', 3)" unselecTABLE="on"><font size="3" unselecTABLE="on">3</font></TD></TR>
																		<TR align="center"><TD class="popupmenu_option" onClick="discuzcode('fontsize', 4)" unselecTABLE="on"><font size="4" unselecTABLE="on">4</font></TD></TR>
																		<TR align="center"><TD class="popupmenu_option" onClick="discuzcode('fontsize', 5)" unselecTABLE="on"><font size="5" unselecTABLE="on">5</font></TD></TR>
																		<TR align="center"><TD class="popupmenu_option" onClick="discuzcode('fontsize', 6)" unselecTABLE="on"><font size="6" unselecTABLE="on">6</font></TD></TR>
																		<TR align="center"><TD class="popupmenu_option" onClick="discuzcode('fontsize', 7)" unselecTABLE="on"><font size="7" unselecTABLE="on">7</font></TD></TR>
																		</TABLE>
																	</DIV>
														
																	<DIV class="popupmenu_popup" id="posteditor_popup_forecolor_menu" style="display: none">
																		<TABLE cellpadding="4" cellspacing="0" border="0" unselecTABLE="on">
																		<TR>
																		<TD class="editor_colornormal" onClick="discuzcode('forecolor', 'Black')" unselecTABLE="on" onMouseOver="colorContext(this, 'mouseover')" onMouseOut="colorContext(this, 'mouseout')"><DIV style="background-color: Black" unselecTABLE="on"></DIV></TD>
																		<TD class="editor_colornormal" onClick="discuzcode('forecolor', 'Sienna')" unselecTABLE="on" onMouseOver="colorContext(this, 'mouseover')" onMouseOut="colorContext(this, 'mouseout')"><DIV style="background-color: Sienna" unselecTABLE="on"></DIV></TD>
																		<TD class="editor_colornormal" onClick="discuzcode('forecolor', 'DarkOliveGreen')" unselecTABLE="on" onMouseOver="colorContext(this, 'mouseover')" onMouseOut="colorContext(this, 'mouseout')"><DIV style="background-color: DarkOliveGreen" unselecTABLE="on"></DIV></TD>
																		<TD class="editor_colornormal" onClick="discuzcode('forecolor', 'DarkGreen')" unselecTABLE="on" onMouseOver="colorContext(this, 'mouseover')" onMouseOut="colorContext(this, 'mouseout')"><DIV style="background-color: DarkGreen" unselecTABLE="on"></DIV></TD>
																		<TD class="editor_colornormal" onClick="discuzcode('forecolor', 'DarkSlateBlue')" unselecTABLE="on" onMouseOver="colorContext(this, 'mouseover')" onMouseOut="colorContext(this, 'mouseout')"><DIV style="background-color: DarkSlateBlue" unselecTABLE="on"></DIV></TD>
																		<TD class="editor_colornormal" onClick="discuzcode('forecolor', 'Navy')" unselecTABLE="on" onMouseOver="colorContext(this, 'mouseover')" onMouseOut="colorContext(this, 'mouseout')"><DIV style="background-color: Navy" unselecTABLE="on"></DIV></TD>
																		<TD class="editor_colornormal" onClick="discuzcode('forecolor', 'Indigo')" unselecTABLE="on" onMouseOver="colorContext(this, 'mouseover')" onMouseOut="colorContext(this, 'mouseout')"><DIV style="background-color: Indigo" unselecTABLE="on"></DIV></TD>
																		<TD class="editor_colornormal" onClick="discuzcode('forecolor', 'DarkSlateGray')" unselecTABLE="on" onMouseOver="colorContext(this, 'mouseover')" onMouseOut="colorContext(this, 'mouseout')"><DIV style="background-color: DarkSlateGray" unselecTABLE="on"></DIV></TD>
																		</TR>
																		<TR>
																		<TD class="editor_colornormal" onClick="discuzcode('forecolor', 'DarkRed')" unselecTABLE="on" onMouseOver="colorContext(this, 'mouseover')" onMouseOut="colorContext(this, 'mouseout')"><DIV style="background-color: DarkRed" unselecTABLE="on"></DIV></TD>
																		<TD class="editor_colornormal" onClick="discuzcode('forecolor', 'DarkOrange')" unselecTABLE="on" onMouseOver="colorContext(this, 'mouseover')" onMouseOut="colorContext(this, 'mouseout')"><DIV style="background-color: DarkOrange" unselecTABLE="on"></DIV></TD>
																		<TD class="editor_colornormal" onClick="discuzcode('forecolor', 'Olive')" unselecTABLE="on" onMouseOver="colorContext(this, 'mouseover')" onMouseOut="colorContext(this, 'mouseout')"><DIV style="background-color: Olive" unselecTABLE="on"></DIV></TD>
																		<TD class="editor_colornormal" onClick="discuzcode('forecolor', 'Green')" unselecTABLE="on" onMouseOver="colorContext(this, 'mouseover')" onMouseOut="colorContext(this, 'mouseout')"><DIV style="background-color: Green" unselecTABLE="on"></DIV></TD>
																		<TD class="editor_colornormal" onClick="discuzcode('forecolor', 'Teal')" unselecTABLE="on" onMouseOver="colorContext(this, 'mouseover')" onMouseOut="colorContext(this, 'mouseout')"><DIV style="background-color: Teal" unselecTABLE="on"></DIV></TD>
																		<TD class="editor_colornormal" onClick="discuzcode('forecolor', 'Blue')" unselecTABLE="on" onMouseOver="colorContext(this, 'mouseover')" onMouseOut="colorContext(this, 'mouseout')"><DIV style="background-color: Blue" unselecTABLE="on"></DIV></TD>
																		<TD class="editor_colornormal" onClick="discuzcode('forecolor', 'SlateGray')" unselecTABLE="on" onMouseOver="colorContext(this, 'mouseover')" onMouseOut="colorContext(this, 'mouseout')"><DIV style="background-color: SlateGray" unselecTABLE="on"></DIV></TD>
																		<TD class="editor_colornormal" onClick="discuzcode('forecolor', 'DimGray')" unselecTABLE="on" onMouseOver="colorContext(this, 'mouseover')" onMouseOut="colorContext(this, 'mouseout')"><DIV style="background-color: DimGray" unselecTABLE="on"></DIV></TD>
																		</TR><TR>
																		<TD class="editor_colornormal" onClick="discuzcode('forecolor', 'Red')" unselecTABLE="on" onMouseOver="colorContext(this, 'mouseover')" onMouseOut="colorContext(this, 'mouseout')"><DIV style="background-color: Red" unselecTABLE="on"></DIV></TD>
																		<TD class="editor_colornormal" onClick="discuzcode('forecolor', 'SandyBrown')" unselecTABLE="on" onMouseOver="colorContext(this, 'mouseover')" onMouseOut="colorContext(this, 'mouseout')"><DIV style="background-color: SandyBrown" unselecTABLE="on"></DIV></TD>
																		<TD class="editor_colornormal" onClick="discuzcode('forecolor', 'YellowGreen')" unselecTABLE="on" onMouseOver="colorContext(this, 'mouseover')" onMouseOut="colorContext(this, 'mouseout')"><DIV style="background-color: YellowGreen" unselecTABLE="on"></DIV></TD>
																		<TD class="editor_colornormal" onClick="discuzcode('forecolor', 'SeaGreen')" unselecTABLE="on" onMouseOver="colorContext(this, 'mouseover')" onMouseOut="colorContext(this, 'mouseout')"><DIV style="background-color: SeaGreen" unselecTABLE="on"></DIV></TD>
																		<TD class="editor_colornormal" onClick="discuzcode('forecolor', 'MediumTurquoise')" unselecTABLE="on" onMouseOver="colorContext(this, 'mouseover')" onMouseOut="colorContext(this, 'mouseout')"><DIV style="background-color: MediumTurquoise" unselecTABLE="on"></DIV></TD>
																		<TD class="editor_colornormal" onClick="discuzcode('forecolor', 'RoyalBlue')" unselecTABLE="on" onMouseOver="colorContext(this, 'mouseover')" onMouseOut="colorContext(this, 'mouseout')"><DIV style="background-color: RoyalBlue" unselecTABLE="on"></DIV></TD>
																		<TD class="editor_colornormal" onClick="discuzcode('forecolor', 'Purple')" unselecTABLE="on" onMouseOver="colorContext(this, 'mouseover')" onMouseOut="colorContext(this, 'mouseout')"><DIV style="background-color: Purple" unselecTABLE="on"></DIV></TD>
																		<TD class="editor_colornormal" onClick="discuzcode('forecolor', 'Gray')" unselecTABLE="on" onMouseOver="colorContext(this, 'mouseover')" onMouseOut="colorContext(this, 'mouseout')"><DIV style="background-color: Gray" unselecTABLE="on"></DIV></TD>
																		</TR><TR>
																		<TD class="editor_colornormal" onClick="discuzcode('forecolor', 'Magenta')" unselecTABLE="on" onMouseOver="colorContext(this, 'mouseover')" onMouseOut="colorContext(this, 'mouseout')"><DIV style="background-color: Magenta" unselecTABLE="on"></DIV></TD>
																		<TD class="editor_colornormal" onClick="discuzcode('forecolor', 'Orange')" unselecTABLE="on" onMouseOver="colorContext(this, 'mouseover')" onMouseOut="colorContext(this, 'mouseout')"><DIV style="background-color: Orange" unselecTABLE="on"></DIV></TD>
																		<TD class="editor_colornormal" onClick="discuzcode('forecolor', 'Yellow')" unselecTABLE="on" onMouseOver="colorContext(this, 'mouseover')" onMouseOut="colorContext(this, 'mouseout')"><DIV style="background-color: Yellow" unselecTABLE="on"></DIV></TD>
																		<TD class="editor_colornormal" onClick="discuzcode('forecolor', 'Lime')" unselecTABLE="on" onMouseOver="colorContext(this, 'mouseover')" onMouseOut="colorContext(this, 'mouseout')"><DIV style="background-color: Lime" unselecTABLE="on"></DIV></TD>
																		<TD class="editor_colornormal" onClick="discuzcode('forecolor', 'Cyan')" unselecTABLE="on" onMouseOver="colorContext(this, 'mouseover')" onMouseOut="colorContext(this, 'mouseout')"><DIV style="background-color: Cyan" unselecTABLE="on"></DIV></TD>
																		<TD class="editor_colornormal" onClick="discuzcode('forecolor', 'DeepSkyBlue')" unselecTABLE="on" onMouseOver="colorContext(this, 'mouseover')" onMouseOut="colorContext(this, 'mouseout')"><DIV style="background-color: DeepSkyBlue" unselecTABLE="on"></DIV></TD>
																		<TD class="editor_colornormal" onClick="discuzcode('forecolor', 'DarkOrchid')" unselecTABLE="on" onMouseOver="colorContext(this, 'mouseover')" onMouseOut="colorContext(this, 'mouseout')"><DIV style="background-color: DarkOrchid" unselecTABLE="on"></DIV></TD>
																		<TD class="editor_colornormal" onClick="discuzcode('forecolor', 'Silver')" unselecTABLE="on" onMouseOver="colorContext(this, 'mouseover')" onMouseOut="colorContext(this, 'mouseout')"><DIV style="background-color: Silver" unselecTABLE="on"></DIV></TD>
																		</TR><TR>
																		<TD class="editor_colornormal" onClick="discuzcode('forecolor', 'Pink')" unselecTABLE="on" onMouseOver="colorContext(this, 'mouseover')" onMouseOut="colorContext(this, 'mouseout')"><DIV style="background-color: Pink" unselecTABLE="on"></DIV></TD>
																		<TD class="editor_colornormal" onClick="discuzcode('forecolor', 'Wheat')" unselecTABLE="on" onMouseOver="colorContext(this, 'mouseover')" onMouseOut="colorContext(this, 'mouseout')"><DIV style="background-color: Wheat" unselecTABLE="on"></DIV></TD>
																		<TD class="editor_colornormal" onClick="discuzcode('forecolor', 'LemonChiffon')" unselecTABLE="on" onMouseOver="colorContext(this, 'mouseover')" onMouseOut="colorContext(this, 'mouseout')"><DIV style="background-color: LemonChiffon" unselecTABLE="on"></DIV></TD>
																		<TD class="editor_colornormal" onClick="discuzcode('forecolor', 'PaleGreen')" unselecTABLE="on" onMouseOver="colorContext(this, 'mouseover')" onMouseOut="colorContext(this, 'mouseout')"><DIV style="background-color: PaleGreen" unselecTABLE="on"></DIV></TD>
																		<TD class="editor_colornormal" onClick="discuzcode('forecolor', 'PaleTurquoise')" unselecTABLE="on" onMouseOver="colorContext(this, 'mouseover')" onMouseOut="colorContext(this, 'mouseout')"><DIV style="background-color: PaleTurquoise" unselecTABLE="on"></DIV></TD>
																		<TD class="editor_colornormal" onClick="discuzcode('forecolor', 'LightBlue')" unselecTABLE="on" onMouseOver="colorContext(this, 'mouseover')" onMouseOut="colorContext(this, 'mouseout')"><DIV style="background-color: LightBlue" unselecTABLE="on"></DIV></TD>
																		<TD class="editor_colornormal" onClick="discuzcode('forecolor', 'Plum')" unselecTABLE="on" onMouseOver="colorContext(this, 'mouseover')" onMouseOut="colorContext(this, 'mouseout')"><DIV style="background-color: Plum" unselecTABLE="on"></DIV></TD>
																		<TD class="editor_colornormal" onClick="discuzcode('forecolor', 'White')" unselecTABLE="on" onMouseOver="colorContext(this, 'mouseover')" onMouseOut="colorContext(this, 'mouseout')"><DIV style="background-color: White" unselecTABLE="on"></DIV></TD>
																		</TR><TR>
																		</TR>
																		</TABLE>
																	</DIV>
													
															</DIV>
													
															<DIV class="editor_text" style="width:100%;">
																<TEXTAREA name="message" rows="10" cols="60" style="width:99%; height:250px;font-size:14px;font-family:Arial;padding:5px;" id="posteditor_TEXTAREA" onSelect="javaScript: storeCaret(this);" onClick="javaScript: storeCaret(this);" onKeyUp="javaScript:storeCaret(this);" onKeyDown="ctlent(event);" tabindex="100">													
																
																</TEXTAREA>
															</DIV>
													
													
															<TABLE width="100%" cellpadding="4" cellspacing="0" class="editor_button">
																<TR><TD>
																	<DIV class="editor_textexpand">
																	<!-- 按键[CTRl+Enter]立刻发送邮件&#160; &#160;  -->
																	<IMG src="<c:url value='/webmailhz'/>/images/zoomout.gif" width="14" height="13" title="收缩编辑框" alt="收缩编辑框" onClick="resizeEditor(-100)" />&#160; 
																	<IMG src="<c:url value='/webmailhz'/>/images/zoomin.gif" width="14" height="13" title="扩展编辑框" alt="扩展编辑框" onClick="resizeEditor(100)" /></DIV>
																</TD></TR>
															</TABLE>
													
														</DIV>
														
														<DIV class="postsubmit">
															<input type="hidden" name="wysiwyg" id="posteditor_mode" value="0"/>
														</DIV>							
													
													</TD>
												</TR>
											</TABLE>												
											</DIV>
												
												
											<SCRIPT language="javaScript">
												var textobj = $(editorid + '_TEXTAREA');
												$('to').focus();
												newEditor(wysiwyg);																
											</SCRIPT>
								
										</TD>
									</TR>
									
									<TR>
										<TD>
											<span class="navsbl">
											<input id="btnsend2" type="button" value="发送邮件" onClick="DoSend();" tabindex="110"/>
											<!-- <input id="btnsave2" type="button" value="保存草稿" onClick="DoSave();" tabindex="111"/> -->
											</span>
										</TD>
									</TR>										
								</TABLE>						
						</TD>
						<!--Main Content Part End -->

						<TD width=10px></TD>
					</TR>
				</TABLE>
			</html:form>
		</DIV>

		<iframe id="proframe" name="proframe" style="border:0px;width:0px;height:0px;"></iframe>
	</BODY>
</HTML>

