<%@ page contentType="text/html; charset=gbk"%>
<%@ include file="/inc/taglibs.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>通讯录</title>
<LINK media=screen href="<c:url value='/webmailhz'/>/css/style.css" type=text/css rel=stylesheet>
<LINK media=screen href="<c:url value='/webmailhz'/>/css/default.css" type=text/css rel=stylesheet>
<LINK media=screen href="<c:url value='/webmailhz'/>/css/example.css" type=text/css rel=stylesheet>

<script type="text/javascript" src="<c:url value='/webmailhz'/>/js/utils.js"></script>
<script type="text/javascript" src="<c:url value='/webmailhz'/>/js/jquery.js"></script>
</head>

<style> 
	body {margin-top: 0px;font-size:12px;}
	select {margin:0;width:170px;overflow:hidden;font:12px;}
	ul{
	        margin:0;
	        padding:0;
	        list-style:none;
	}
	li {
	        display:inline;
	}
	.close {
	        display:none;
	        list-style:none;
	        margin: 0px;
	}
	.open {
	        display:block;
	        list-style:none;
	        margin: 0px;
	}
	.open li{
	        margin:0px;
	}
	.folder {
	        font-family: Verdana, Geneva, Arial, Helvetica, sans-serif;
	        font-size: 12px;
	        color: #666;
	        white-space: nowrap;
	        border:1px ;
	        border-color:red;
	}
	.folder img {
	        border: 0px;
	        vertical-align: middle;
	}
	.unSelected{
		cursor:pointer;
	}
	 
	.Selected{
	        cursor:hand;
	        background-color:#99ccff;
		border:double 1px #6699cc
	}
	.childs{
	        margin: 0px;
	        padding: 0px;
	        border: 0px solid #cccccc;
	        border-color:red;
		a:hover{cursor:hand;};
	}
</style>

<script language="javaScript">
		function refreshData(organizeId,type){
			//alert('1234');
			//var location;
			//location = "<%=request.getContextPath()%>/base/listPerson.jsp?organizeId=" + organizeId;
			//parent.location.href=location;
			len = addressform.FromList.length 
			for (i=0;i<len;i++) {
				addressform.FromList.options[0]=null;
			}	
			
			//alert(type);
			var i = 0;
			if(parseInt(type)==1){
				<c:forEach items="${requestScope._Company_Persons}" var="cPerson" varStatus="status">		
					var belong = false;
						
					<c:choose>
						<c:when test="${!empty(cPerson.group) && !empty(cPerson.department)}">
							if(parseInt(${cPerson.department.organizeId}) == organizeId || parseInt(${cPerson.group.organizeId}) == organizeId)
								belong = true;
						</c:when>
						<c:when test="${!empty(cPerson.department)}">
							if(parseInt(${cPerson.department.organizeId}) == organizeId)
								belong = true;
						</c:when>
					</c:choose>
					
					if(belong){
						var oOption = document.createElement ("OPTION");
						oOption.value = '${cPerson.email}';
 						oOption.text= '${cPerson.personName}' + ' <' + '${cPerson.email}' + '>';
						addressform.FromList.options[i]=oOption
						
						i=i+1						
					}
				</c:forEach>
			}else{
				i = 0;
				if(organizeId==null || organizeId==""){
					<c:forEach items="${requestScope._Self_Persons}" var="sPerson" varStatus="status">
						var oOption = document.createElement ("OPTION");
						oOption.value = '${sPerson.email}';
 						oOption.text= '${sPerson.personName}' + ' <' + '${sPerson.email}' + '>';
						addressform.FromList.options[i]=oOption
												
						i = i + 1;
					</c:forEach>
				} else {
					<c:forEach items="${requestScope._Self_Persons}" var="sPerson" varStatus="status">
						var belong = false;
						if(parseInt(${sPerson.category.categoryId}) == organizeId)
							belong = true;
						
						if(belong){		
							var oOption = document.createElement ("OPTION");
							oOption.value = '${sPerson.email}';
	 						oOption.text= '${sPerson.personName}' + ' <' + '${sPerson.email}' + '>';
							addressform.FromList.options[i]=oOption
							
							i = i + 1;						
						}		
					</c:forEach>
				}
			}
		}
		
		function handleOK(){
			var emails = "";
			var to = addressform.to;
			var cc = addressform.cc;
			var bcc = addressform.bcc;
			
			if(to.length>0){
				 for(var i = 0; i < to.length; i++) {
				 	if(emails==""){
				 		emails = to.options[i].value;
				 	}else{
				 		emails = emails + ", " + to.options[i].value;
				 	}      				 
                }
            }	
            emails = emails + "|";
            
            if(cc.length>0){
				 for(var i = 0; i < cc.length; i++) {
				 	if(i==0){
				 		emails = emails + cc.options[i].value;
				 	}else{
				 		emails = emails + ", " + cc.options[i].value;
				 	}      				 
                }
            }	
             emails = emails + "|";            
            
            if(bcc.length>0){
				 for(var i = 0; i < bcc.length; i++) {
				 	if(i==0){
				 		emails = emails + bcc.options[i].value;
				 	}else{
				 		emails = emails + ", " + bcc.options[i].value;
				 	}      				 
                }
            }
            
            window.returnValue = emails;
            self.close();
            //alert(emails);			
		}
		
	</script>
	
<!-- SECTION 1 -->
	<style>
		.font_left_03 {
			FONT-WEIGHT: bold; FONT-SIZE: 12px; COLOR: #000000; LINE-HEIGHT: 22px; FONT-FAMILY: "宋体"; LETTER-SPACING: 1px
		}
	   /* styles for the tree */
	   SPAN.TreeviewSpanArea A {
	        font-size: 12pt; 
	        font-family: verdana,helvetica; 
	        text-decoration: none;
	        color: black
	   }
	   SPAN.TreeviewSpanArea A:hover {
	        color: '#820082';
	   }
	   /* rest of the document 
	   BODY {background-color: white}
	   TD {
	        font-size: 10pt; 
	        font-family: verdana,helvetica; 
	   }*/
	</style>
	
	<!-- Code for browser detection -->
	<script src="<c:url value='/core/js'/>/ua.js"></script>

	<!-- Infrastructure code for the tree -->
	<script src="<c:url value='/core/js'/>/ftiens4.js"></script>

	<script>
	
	// Decide if the names are links or just the icons
	USETEXTLINKS = 1  //replace 0 with 1 for hyperlinks	
	// Decide if the tree is to start all open or just showing the root folders
	STARTALLOPEN = 0 //replace 0 with 1 to show the whole tree	
	ICONPATH = '<c:url value="${'/images'}"/>/tree/' //change if the gif's folder is a subfolder, for example: 'images/'	
	HIGHLIGHT = 1
	USETEXTLINKS = 1
	STARTALLOPEN = 1
	USEFRAMES = 0
	USEICONS = 1
	WRAPTEXT = 1
	PERSERVESTATE = 1
	
	foldersTree = gFld("<i><b><font size='-1'>公司及个人通讯录</b></i>", "")	
	<logic:iterate id="organize" name="_TREE" indexId="id">		           		
		<c:choose>
			<c:when test="${id ==0}">
				aux0${organize.organizeId} = insFld(foldersTree, gFld("<font size='-1'><b>公司通讯录</b>", "")) 				
			</c:when>
			<c:otherwise>				
				aux0${organize.organizeId} =insFld(aux0${organize.parent.organizeId}, gFld("<font size='-1'>${organize.organizeName}", "javaScript:refreshData('${organize.organizeId}','1')")) 
			</c:otherwise>
		</c:choose>	
	</logic:iterate>
	
	aux10 = insFld(foldersTree, gFld("<font size='-1'><b>个人通讯录</b>", "javaScript:refreshData('${category.categoryId}','2')"))	
	<logic:iterate id="category" name="_Address_Categorys" indexId="id">
		aux10${category.categoryId} =insFld(aux10, gFld("<font size='-1'>${category.categoryName}", "javaScript:refreshData('${category.categoryId}','2')")) 
	</logic:iterate>		
</script>


<body> 
	<form onsubmit="return false;" id="addressform" name="addressform">
			 
	<div style="background: rgb(204, 0, 0) none repeat scroll 0%; position: absolute; left: 10px; top: 10px; -moz-background-clip: -moz-initial; -moz-background-origin: -moz-initial; -moz-background-inline-policy: -moz-initial; height: 18px; color: rgb(255, 255, 255); display: none;font-size:12px;" id="LoadingStatus">loading data.......</div>
	<div>		
		<div style="float: right; margin-top: 20px;">
	        <table width="650" cellspacing="0" cellpadding="10" border="0">
	          <tbody>				
				<tr>
					<td width="280" valign="top">
						<table border=0>
							<tr><td>
								<font size=-2>
									<a href="http://www.treemenu.net"></a></font>
							</td>
						</table>

						<span class=TreeviewSpanArea>
						<script>initializeDocument()</script>
						<noscript>
						A tree for site navigation will open here if you enable JavaScript in your browser.
						</noscript>
						</span>					
					</td>
		            <td width="200" align="right">
		              <table width="100%" cellspacing="0" cellpadding="0" border="0">
		                <tbody><tr>
		                  <td>
		                    <select size="25" multiple="" id="FromList" name="FromList">
		                    	<c:forEach items="${requestScope._Company_Persons}" var="cPerson" varStatus="status">			
									<option value="${cPerson.email}">${cPerson.personName}&nbsp;<"${cPerson.email}"></option>
								</c:forEach>
		                    </select>
		                  </td>
		                </tr>
		              </tbody></table>
		            </td>
			    	<td align="left">
			    		<table width="100%" height="100%" cellspacing="0" cellpadding="0" border="0">
			                <tbody><tr>
			                  <td width="100" align="left">
			                      	<br/>
			                      	<input type="button" class="add2" value="添加&gt;&gt;" onclick="javascript:ListAdd('FromList','to')" name="addto"/>
			                      	<br/>			                      
			                      	<input type="button" class="add3" value="去除&lt;&lt;" onclick="javascript:ListRemove('to')" name="removeto"/>			                  		
			                  </td>
			                  <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
			                  <td>
			                    	收件人
			                    	<select size="6" multiple="" id="to" name="to"></select>
			                  </td>
			                </tr>
			              </tbody>
			           </table>
		               <table width="100%" height="100%" cellspacing="0" cellpadding="0" border="0">
			                <tbody><tr>
			                  <td width="60">
			                      <br/>
			                      <input type="button" class="add2" value="添加&gt;&gt;" onclick="javascript:ListAdd('FromList','cc')" name="addcc"/>
			                      <br/>			                      
			                      <input type="button" class="add3" value="去除&lt;&lt;" onclick="javascript:ListRemove('cc')" name="removecc"/>
			                  </td>
			                  <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
			                  <td>
			                     抄送
			                    <select size="6" multiple="" id="cc" name="cc">
			                    </select>
			                  </td>
			                </tr>
			              </tbody>
			          </table>
		              <table width="100%" height="100%" cellspacing="0" cellpadding="0" border="0">
			                <tbody><tr>
			                  <td width="60">
			                      <br/>
			                      <input type="button" class="add2" value="添加&gt;&gt;" onclick="javascript:ListAdd('FromList','bcc')" name="addbcc"/>			                     
			                      <br/>
			                      <input type="button" class="add3" value="去除&lt;&lt;" onclick="javascript:ListRemove('bcc')" name="removebcc"/>
			                  </td>
			                  <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
			                  <td>
								暗送
			                    <select size="6" multiple="" id="bcc" name="bcc"></select>
			                  </td>
			                </tr>
			              </tbody>
		             </table>
		            </td>
		          </tr>
	        	</tbody>
	        </table>
	        
			<div>
				<br>
				&nbsp;&nbsp;
		        <input type="button" class="button2" value=" 确 定 " onclick="javascript:handleOK();" name="sure"/>      
		        <input type="button" class="button2" value=" 取 消 " onclick="javascript:window.close();" name="back"/>
			</div>
		</div>
	</div>
	</form>
	
</body>
</html>

