<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<title>编辑日程</title>
<script src="<c:url value="/"/>datePicker/WdatePicker.js" language="JavaScript"></script>
<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
<script src="<c:url value="/"/>js/inc_javascript.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/css/form.css"/>"/>
<!-- formValidator -->
<link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css"></link>
<link type="text/css" rel="stylesheet" href="/css/myTable.css"></link>
<script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript" charset="UTF-8"></script>
<script src="<c:url value="/"/>components/formvalidator/js/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
<script src="<c:url value='/js'/>/addattachment.js"></script>

<script>	
	$(document).ready(function(){
		//验证
		$.formValidator.initConfig({formid:"mscheduleJobInforForm",onerror:function(msg){alert(msg)},onsuccess:function(){alert('信息发送成功');submitData();}});
		$("#jobTitle").formValidator({onshow:"请输入标题",onfocus:"标题不能为空",oncorrect:"输入正确"}).inputValidator({min:1,onerror:"标题不能为空,请确认"});
		$("#jobContent").formValidator({onshow:"请输入内容",onfocus:"内容不能为空",oncorrect:"输入正确"}).inputValidator({min:1,onerror:"内容不能为空,请确认"});
	});
	
	//提交数据
	function submitData() {
		var form = document.scheduleJobInforForm;
		form.action = '<c:url value="/personal/personalJobInfor"/>.do?method=save';
		form.submit();
		//window.returnValue = "Y";
		//window.close();
	}
		
	//验证输入数据
	function validateCalendarInfor(){
		var jobTitle;	
		var startTimeStr;
		var endTimeStr;	
		
		jobTitle = scheduleJobInforForm.jobTitle.value;
		startTimeStr = scheduleJobInforForm.startTimeStr.value;
		endTimeStr = scheduleJobInforForm.endTimeStr.value;
		if(jobTitle==""||jobTitle==null){
			alert("请输入日程主题!");
			scheduleJobInforForm.jobTitle.focus();
			return false;
		}
		
		if(startTimeStr==""||startTimeStr==null){
			alert("请输入开始时间!");
			scheduleJobInforForm.startTimeStr.focus();
			return false;
		}
		if(endTimeStr==""||endTimeStr==null){
			alert("请输入结束时间!");
			scheduleJobInforForm.endTimeStr.focus();
			return false;
		}
			
		return true;
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
		var form = document.scheduleJobInforForm;
		//var scheduleType = '${scheduleJobForm.scheduleType}';
		var scheduleType = form.scheduleType.value;
		var users = document.getElementById('users');
	
		if (scheduleType == 1) {
			users.style.display = '';
		}else {
			users.style.display = 'none';
		}
	}

	//定义一个数组，记录各个数据点击的次数
	var clickTimes =new Array();
</script>
<base target="_self"/>
<body onload="hdUsers();">
<br/>
<form:form commandName="scheduleJobInforVo" name="scheduleJobInforForm" id="scheduleJobInforForm" action="" method="post" enctype="multipart/form-data">
	<form:hidden path="scheduleId"/>			
	<input type="hidden" name="handle" value="<%=request.getParameter("handle") %>"/>	
	<input type="hidden" name="attachment" value="${_Schedule.attachment}"/>
	<input type="hidden" name="scheduleType" value="<%=request.getParameter("scheduleType") %>"/>
	<table width="98%" cellpadding="6" cellspacing="1" align="center" border="0" bordercolor="#c5dbec">
		<%--<tr>
			<td colspan="3">编辑日程</td>
		</tr>
		--%>
		<tr>
			<td style="width: 100px;" class="ui-state-default jqgrid-rownum">标题：</td>
			<td>
				<form:input path="jobTitle" size="50"/>	&nbsp;<font color="red">*</font>						
			</td>			
		</tr>
		
		<tr>
			<td style="width: 100px;" class="ui-state-default jqgrid-rownum">开始时间：</td>
			<td>
				<form:input path="startTimeStr" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="true" />	&nbsp;<font color="red">*</font>						
			</td>			
		</tr>
		
		<tr>
			<td style="width: 100px;" class="ui-state-default jqgrid-rownum">结束时间：</td>
			<td>
				<form:input path="endTimeStr" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="true" />	&nbsp;<font color="red">*</font>						
			</td>			
		</tr>
		
		<tr>
			<td >工作内容：</td>
			<td>
				<form:textarea rows="5" cols="50" path="jobContent"></form:textarea>
			</td>		
		</tr>
		
		<tr>
			<td >要求说明：</td>
			<td>
				<form:textarea rows="5" cols="50" path="demand"></form:textarea>
			</td>		
		</tr>
		
		<tr>
			<td style="width: 100px;" class="ui-state-default jqgrid-rownum">分类名称：</td>
			<td>
				<form:input path="categoryName" size="30"/>				
			</td>			
		</tr>
	
		<tr>
			<td >是否重要：</td>
			<td>
				<form:select path="isImportant">
                	<form:option value="0">否</form:option>
                    <form:option value="1">是</form:option>
                </form:select>
				<script language="javaScript">
					var form = document.getElementById('scheduleJobInforForm');
					getOptsValue(form.isImportant,"${scheduleJobInforVo.isImportant}");
				</script>					
			</td>
		</tr>
	
		<tr>
			<td>是否公开：</td>
			<td>
				<form:select path="openType">
                     <form:option value="0">不公开</form:option>
                     <form:option value="1">全公司公开</form:option>
                </form:select>
			</td>				   
		</tr>
		
		<tr>
			<td>是否公开：</td>
			<td>
				<form:select path="status">
                     <form:option value="0">正常进行</form:option>
                     <form:option value="1">暂时停止</form:option>
                     <form:option value="2">已经完成</form:option>
                </form:select>
			</td>				   
		</tr>
				             
        <tr>
			<td>附件：</td>
			<td>
				
				<table cellpadding="0" cellspacing="0" style="margin-bottom:0;margin-top:0">
					<tr>
						<td>
							<input type="file" name="attachment" size="50" />
							&nbsp;
							<input type="button" value="更多附件.." onClick="addtable('newstyle')" class="bt"/> 			
						</td>
					</tr>
				</table>
				<span id="newstyle"></span> 
			</td>
		</tr>
		
			<c:if test="${!empty scheduleJobInforVo.attatchmentArray}">
				<tr>
					<td colspan="2" valign="top">
						原附件信息(
						<font color=red>如果要删除某个附件，请选择该附件前面的选择框</font>)：
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<c:forEach var="file" items="${scheduleJobInforVo.attatchmentArray}"
							varStatus="index">
							<form:checkbox path="attatchmentArray" value="${index.index}" />
							<a href="<%=request.getRealPath("/")%>${file}">${_Attachment_Names[index.index]}
							</a>
							<br>
						</c:forEach>
					</td>
				</tr>
			</c:if>
			
		<tr id="users" valign="top">
			<td>
			执行人员：
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
		
     	<tr> 
        	<td colspan="2" >
          		<input type="button" id="button" style="cursor: pointer;" value="提交" onclick="javaScript:if(validateCalendarInfor()){submitData();}"/>
            	&nbsp;
          		<input type="button" value="取消" style="cursor: pointer;" onclick="window.close();"/>
        	</td>
     	</tr>
	</table>

</form:form>
</body>

