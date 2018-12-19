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
						
	function do_mkdir(){
		var fname;
		fname = $("mailForm").fname.value;
		if(fname==""){
			alert("请输入需要创建的文件夹名称！");
			return;
		}
		
		$("mailForm").submit();
	}
	
	function do_delete(folderId){
		if (confirm("是否确定要删除该文件夹?")) {
			$("method").value="remove";
			$("folderId").value = folderId;
			
			$("mailForm").submit();
		}
	}
	
	function do_rename(old){
		var fname = prompt("将此邮件夹重新命名为:",old);
		if ((fname==null)||(fname==old))
		{
		} else {			
			$("oldname").value=old;
			$("fname").value=fname;
			
			$("mailForm").submit();
		}
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
						        <LI class="fdnav"><A href="<c:url value='/webmail/userConfig'/>.mdo?method=list">常规设置</A></LI>
						        <LI class="fdnav"><A href="<c:url value="/webmailhz"/>/userFolder.jsp"><b>邮件夹</b></A></LI>
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
						
						<html:form method="post" action="/webmail/folderSetup.mdo" onsubmit="return chk();">
							<input type="hidden" name="method" id="method" value="save"/>
							
					     <TABLE cellSpacing=0 cellPadding=0 width="100%" valign="top">
					        <TBODY>
					        <TR>
					          <TD class=tdmain_in_tit height=28>
					          		<SPAN style="LEFT: 0px; FLOAT: left">
					          			<SPAN class="pl10 b">邮箱管理</SPAN>
					          		</SPAN> 
					          		<SPAN id=spanresult style="PADDING-RIGHT: 10px; FLOAT: right"></SPAN>
					          	</TD>
					        </TR>
					        <TR>
					          <TD class=tdmain_in_con>
					            <TABLE style="FONT-SIZE: 12px; BACKGROUND: #fff; WIDTH: 100%" cellSpacing=0 cellPadding=0>
					              <COLGROUP>
					              <COL style="WIDTH: 10px">
					              <COL>
					              <COL>
					              <COL style="WIDTH: 50px">
					              <COL style="WIDTH: 50px">
					              <COL style="WIDTH: 50px">
					              <COL style="WIDTH: 80px">
					              <TBODY>
					              
					              <TR class=MLTR_HEAD>
					                <TD>&nbsp;</TD>
					                <TD>邮箱</TD>
					                <TD>&nbsp;</TD>
					                <TD>未读</TD>
					                <TD>已读</TD>
					                <TD>大小</TD>
					                <TD>操作</TD>
								  </TR>
								  
																  
								  <c:forEach items="${requestScope._Folders}" var="folder" varStatus="status">								  
						             
						              	<c:if test="${folder.folderName=='INBOX' || folder.folderName=='Inbox' || folder.folderName=='Trash'  || folder.folderName=='Junk'  || folder.folderName=='Drafts'  || folder.folderName=='Sent' }">						              
								              <TR class=MLTR>
								                <TD>&nbsp;</TD>
								                <TD style="OVERFLOW: hidden">
													<A href="#"><IMG src="<c:url value='/webmailhz/images'/>/sent.gif" align=absMiddle border=0></A> 
													<A href="#">${folder.folderName}</A>
												</TD>
								                <TD>&nbsp;</TD>
								                <TD>&nbsp;</TD>
								                <TD>&nbsp;</TD>
								                <TD>&nbsp;</TD>
								                <TD>&nbsp;</TD>
								              </TR>
						              </c:if>			              
					              </c:forEach>
					              
					               <c:forEach items="${requestScope._Folders}" var="folder" varStatus="status">
						              	<c:if test="${!(folder.folderName=='INBOX' || folder.folderName=='Inbox' || folder.folderName=='Trash'  || folder.folderName=='Junk'  || folder.folderName=='Drafts'  || folder.folderName=='Sent')}">						              
								              <TR class=MLTR>
								                <TD>&nbsp;</TD>
								                <TD style="OVERFLOW: hidden">
													<A href="#"><IMG src="<c:url value='/webmailhz/images'/>/folders.gif" align=absMiddle border=0></A> 
													<A href="#">${folder.folderName}</A>
												</TD>
								                <TD>&nbsp;</TD>
								                <TD>&nbsp;</TD>
								                <TD>&nbsp;</TD>
								                <TD>&nbsp;</TD>
								                <TD>
													<a href="javascript:do_rename('${folder.folderName}');">
													<img alt="改名" src="<c:url value='/webmailhz/images'/>/edit.gif"></a>&nbsp;
													<a href="javascript:do_delete('${folder.folderId}');">
													<img alt="删除" src="<c:url value='/webmailhz/images'/>/delete.gif"></a>&nbsp; 	  
													<!-- 
													<a href="javascript:do_purge('ce');"> 		  
													<img alt="清空" src="<c:url value='/webmailhz/images'/>/purge.gif"></a> 	 	
													 -->
												</TD>
								              </TR>
						              </c:if>						              					              
					              </c:forEach>
					              
					              <!-- 
								  <tr class="MLTR">
									<td>&nbsp;</td>
						  			<td style="overflow:hidden;">
						  				<a href=""><img src="<c:url value='/webmailhz/images'/>/folders.gif" border=0 align=absmiddle></a>
						  				<a href="">ce</a>
									</td>
						  			<td>&nbsp;</td>
									<td>0</td>
									<td>0</td>
									<td>0</td>
									<td>&nbsp; 	  
										<a href="javascript:do_rename('ce');">
										<img alt="改名" src="<c:url value='/webmailhz/images'/>/edit.gif"></a>&nbsp;
										<a href="javascript:do_delete('ce');">
										<img alt="删除" src="<c:url value='/webmailhz/images'/>/delete.gif"></a>&nbsp; 	  
										
										<a href="javascript:do_purge('ce');"> 		  
										<img alt="清空" src="<c:url value='/webmailhz/images'/>/purge.gif"></a> 	  
										 
									</td>
								</tr>
								
								
								<TR class=MLTR_N>
					                <TD>&nbsp;</TD>
					                <TD><B>总计</B></TD>
					                <TD>&nbsp;</TD>
					                <TD>0</TD>
					                <TD>25</TD>
					                <TD>506.6K</TD>
					                <TD>&nbsp;</TD>
					            </TR>
					             -->
					            <TR>
					            	<TD>&nbsp;</TD>
						            <TD style="PADDING-RIGHT: 10px; PADDING-LEFT: 10px; PADDING-BOTTOM: 10px; PADDING-TOP: 10px">创建新邮件夹&nbsp;&nbsp;							
									<INPUT id="fname" maxLength="30" name="fname" width="130px">&nbsp;&nbsp;
									
									<INPUT id="oldname" type="hidden" maxLength="30" name="oldname" width="130px">&nbsp;&nbsp;
									<INPUT id="folderId" type="hidden" maxLength="30" name="folderId" width="130px">&nbsp;&nbsp;
									
									<INPUT style="WIDTH: 60px" onclick="do_mkdir();" type="button" value="创建"></TD>
					                <TD>&nbsp;</TD>
					                <TD>&nbsp;</TD>
					                <TD>&nbsp;</TD>
					                <TD>&nbsp;</TD>
					                <TD>&nbsp;</TD>
					            </TR>
                
								</TABLE></TD></TR></TBODY></TABLE>
				      
				      </html:form>
				      
						</TD>
						
						<TD width="10"></TD>
					</TR>
				</TBODY>
				</TABLE>
				</DIV> 
		 </BODY>
    </HTML>

