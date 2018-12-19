<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<title>编辑讯息</title>

<link rel="stylesheet" type="text/css" href="<c:url value="/css/form.css"/>" />
<link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css"></link>

<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
<script src="<c:url value="/"/>js/commonFunction.js" type="text/javascript"></script>
<script src="<c:url value="/"/>js/inc_javascript.js" type="text/javascript"></script>
<script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript" charset="UTF-8"></script>
<script src="<c:url value="/"/>components/formvalidator/js/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
<script src="<c:url value='/js'/>/addattachment.js"></script>
<link type="text/css" rel="stylesheet" href="/css/myTable.css"></link>
<script>	
	
	//验证输入数据
	function validateMessageInfor(){
		var messageTitle;		
		var messageContent;
		messageTitle = messageInforForm.messageTitle.value;
		messageContent =  messageInforForm.messageContent.value;
		if(messageTitle==""||messageTitle==null){
			alert("请输入讯息主题！");
			messageInforForm.messageTitle.focus();
			return false;
		}
		
		if(messageContent==""||messageContent==null){
			alert("请输入讯息内容！");
			return false;
		}
			
		return true;
	}
	
	//提交数据
	function submitData() {
		var form = document.messageInforForm;
		form.action = '<c:url value="/personal/messageInfor"/>.do?method=saveSendMessage';
		form.submit();
		alert('讯息发送成功！');
		window.returnValue = "Y";
		//window.opener.location.reload();
		window.close();
	}
	
	var i=0,number;
	//全选操作
	function selectUserId(checkbox,organizeId){
		var isChecked = false;
		if(checkbox.checked){
			isChecked = true;
		}
		var obj;
		obj = document.messageInforForm.personIds;		
		if(obj!=null){
			if(obj.length==null){
					//只有一个,则只需要判断该用户是不是这个分类
				<c:forEach var="user" items="${_Users}" varStatus="index">
					var tempOrganizeId = '${user.person.department.organizeId}';
					if(organizeId==tempOrganizeId){
						obj.checked = isChecked;
					}
				</c:forEach>
				
				<c:forEach var="user" items="${_OtherUsers}" varStatus="index">
					var tempOrganizeId = '${user.person.department.organizeId}';
						
					if(organizeId==tempOrganizeId){
						obj.checked = isChecked;
					}
				</c:forEach>
			}else{
				//多个用户
				var personNum;
				personNum = 0;
				var personNum = obj.length;			
				for(var k = 0; k<personNum;k++){
					var userId;
					personId = obj[k].value;
					<c:forEach var="user" items="${_Users}" varStatus="index">
						var tempOrganizeId = '${user.person.department.organizeId}';
						var tempPersonId = '${user.person.personId}';
							
						if(organizeId==tempOrganizeId && tempPersonId==personId){
							obj[k].checked = isChecked;
						}
					</c:forEach>
						
					<c:forEach var="user" items="${_OtherUsers}" varStatus="index">
						var tempOrganizeId = '${user.person.department.organizeId}';
						var tempPersonId = '${user.person.personId}';
							
						if(organizeId==tempOrganizeId && tempPersonId==personId){
							obj[k].checked = isChecked;
						}
					</c:forEach>
				}
			}				
		}
	}
	
	//显示或隐藏用户信息
	function hdUsers() {
		var form = document.messageInforForm;
		var receiveType = form.receiveType.value;
		var users = document.getElementById('users');
		var roles = document.getElementById('mailRole');
		
		//alert(receiveType);
		if (receiveType == 1) {
			users.style.display = '';
			roles.style.display= 'none';
		}else if(receiveType == 2){
			users.style.display = 'none';
			roles.style.display = '';
		}else {
			users.style.display = 'none';
			roles.style.display= 'none';
		}
	}
		
	
	//定义一个数组，记录各个数据点击的次数
	var clickTimes =new Array();	
</script>

<base target="_self"/>
<body onload="hdUsers();resetDialogHeight();">
	<br />

	<form:form commandName="messageInforVo" name="messageInforForm" id="messageInforForm" action="" method="post" enctype="multipart/form-data">
		<form:hidden path="messageId" />
		<input type="hidden" name="handle" value="<%=request.getParameter("handle") %>"/>	
		<input type="hidden" name="attachment" value="${_Message.attachment}"/>
		<table width="100%" cellpadding="6" cellspacing="0" align="center"
			border="0" bordercolor="#c5dbec">
			<%--<tr>
				<td colspan="2" bgcolor="#dfeffc"></td>
			</tr>--%>

			<tr>
				<td style="width: 10%;color: #22fbff" class="ui-state-default jqgrid-rownum">
					标题：
				</td>
				<td>
					<form:input path="messageTitle" size="50" />
					&nbsp;
					<font color="red">*</font>

				</td>

			</tr>

			<tr>
				<td style="color: #22fbff">
					内容：
				</td>
				<td>
					<form:textarea rows="5" cols="50" path="messageContent"></form:textarea>
					&nbsp;
					<font color="red">*</font>
				</td>
			</tr>



			<tr>
				<td style="color: #22fbff">
					是否重要：
				</td>
				<td>
					<form:select path="isImportant">
						<form:option value="0">否</form:option>
						<form:option value="1">是</form:option>
					</form:select>
					<script language="javaScript">
					var form = document.getElementById('messageInforForm');
					getOptsValue(form.isImportant,"${messageInforVo.isImportant}");
				</script>
				</td>
			</tr>

			<tr>
				<td style="color: #22fbff">
					接收者：
				</td>
				<td>
					<form:select path="receiveType" onchange="hdUsers()">
						<form:option value="0">全公司</form:option>
						<form:option value="1">自定义</form:option>
						<form:option value="2">分组</form:option>
					</form:select>
				</td>
			</tr>

			<tr id="users" valign="top">
				<td>
					发送用户：
				</td>
				<td>
					<table width="100%">
						<c:set var="_Num" value="0" />
						<c:forEach var="department" items="${_Departments}">
							<script language="javaScript">
								clickTimes[${_Num}] = 0;							
							</script>

							<tr height="33">
								<td valign="bottom" width="100%"
									style="border-bottom:1px dotted #888888;font-size:10pt"
									colspan="2">
									${department.organizeName}&nbsp;
									<span onclick="show_list('${_Num}')"> <img name="img"
											src="<c:url value="${'/images'}"/>/xpexpand3_s.gif"
											style="margin-top:0px;margin-bottom:0px;" /> </span>
								</td>
							</tr>

							<tr name="tr" style="display:none;padding-top:10px;">
								<td width="9%" align="right" valign="top">
									<input type="checkbox" onclick="selectUserId(this,'${department.organizeId}')" />全选
								</td>
									<td style="padding-left:10px;" width="92%">
									<table>
										<tr>
				      						<c:set var="_TypeNum" value="0"/>
				      						<c:forEach var="user" items="${_Persons}" varStatus="index">
				      							<c:if test="${user.person.department.organizeId==department.organizeId}">
													<c:if test="${_TypeNum!=0 && _TypeNum%6==0}">
														</tr><tr>
													</c:if>
													<td width="16%" valign="top">
														<form:checkbox  path="personIds" value="${user.personId}"/> ${user.person.personName}
													</td>				
													<c:set var="_TypeNum" value="${_TypeNum+1}"/>
												</c:if>																			
											</c:forEach>
											<c:forEach begin="${_TypeNum%6}" end="5">
												<td width="16%">&nbsp;</td>
											</c:forEach>		      											
										</tr>	
										<tr>
				      						<c:set var="_TypeNum" value="0"/>
				      						<c:forEach var="user" items="${_OtherPersons}" varStatus="index">
				      							<c:if test="${user.person.department.organizeId==department.organizeId}">
													<c:if test="${_TypeNum!=0 && _TypeNum%6==0}">
														</tr><tr>
													</c:if>
													<td width="16%" valign="top">
														<form:checkbox path="personIds" value="${user.personId}"/> ${user.person.personName}
													</td>				
													<c:set var="_TypeNum" value="${_TypeNum+1}"/>
												</c:if>																			
											</c:forEach>
											<c:forEach begin="${_TypeNum%6}" end="5">
												<td width="16%">&nbsp;</td>
											</c:forEach>		      											
										</tr>				
									</table>
								</td>
							</tr>
							<c:set var="_Num" value="${_Num+1}" />
						</c:forEach>
					</table>
				</td>
			</tr>
			
			
			<tr id="mailRole">
				<td>分组收件人：</td>
				<td>
					<c:forEach items="${_MailRoles}" var="mailRole">
						<form:checkbox path="roles" value="${mailRole.roleId}"/> ${mailRole.roleName}
						<br/>
					</c:forEach>
				</td>
			</tr>


			<tr>
				<td style="color: #22fbff">
					附件：
				</td>
				<td>

					<table cellpadding="0" cellspacing="0"
						style="margin-bottom:0;margin-top:0">
						<tr>
							<td>
								<input type="file" name="attachment" size="50" />
								&nbsp;
								<input type="button" value="更多附件..." onClick="addtable('newstyle')"
									class="bt" />

							</td>
						</tr>
					</table>
					<span id="newstyle"></span>
				</td>
			</tr>
			<c:if test="${!empty messageInforVo.attachmentStr}">
				<tr>
					<td colspan="2" valign="top">
						原附件信息(
						<font color=red>如果要删除某个附件，请选择该附件前面的选择框</font>)：
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<c:forEach var="file" items="${_Attachment_Names}"
							varStatus="index">
							<form:checkbox path="attatchmentArray" value="${index.index}" />
							<a href="<%=request.getRealPath("/")%>${file}">${_Attachment_Names[index.index]}
							</a>
							<br>
						</c:forEach>
					</td>
				</tr>
			</c:if>

			<tr>
				<td colspan="2" >
					<input type="button" id="button" style="cursor: pointer;"
						value="发送"
						onclick="javaScript:if(validateMessageInfor()){submitData();}" />
					&nbsp;
					<input type="button" value="取消" style="cursor: pointer;"
						onclick="window.close();" />
				</td>
			</tr>
		</table>
	</form:form>
</body>

