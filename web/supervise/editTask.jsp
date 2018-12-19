<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>
<%@ page language="java" 
		import="org.springframework.web.context.WebApplicationContext,
				org.springframework.web.context.support.*,
				java.util.*,
				com.kwchina.core.base.service.PersonInforManager,
				com.kwchina.core.base.entity.PersonInfor" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<title>编辑工作跟踪内容</title>
<script src="<c:url value="/"/>datePicker/WdatePicker.js" charset="UTF-8" language="JavaScript"></script>
<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
<script src="<c:url value="/"/>js/inc_javascript.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/css/form.css"/>" />
<link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css"></link>
<link type="text/css" rel="stylesheet" href="/css/noTdBottomBorder.css"></link>
<link type="text/css" rel="stylesheet" href="/css/myTable.css?version=20180321"></link>
<script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript" charset="UTF-8"></script>
<script src="<c:url value="/"/>components/formvalidator/js/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
<script src="<c:url value='/js'/>/addattachment.js"></script>
<script src="<c:url value='/fckeditor'/>/fckeditor.js"></script>
<script>	
	
	//验证输入数据
	function validateSuperviseInfor(){
		var taskName;		
		var finishDateStr;
		//var meetRoom;
		taskName = superviseInforForm.taskName.value;
		finishDateStr = superviseInforForm.finishDateStr.value;
		//meetRoom = superviseInforForm.meetRoom.value;
		if(taskName==""||taskName==null){
			alert("请输入工作内容和要求!");
			superviseInforForm.taskName.focus();
			return false;
		}
		
		if(finishDateStr==""||finishDateStr==null){
			alert("请输入完成时间!");
			superviseInforForm.finishDateStr.focus();
			return false;
		}
		
		//if(meetRoom==""||meetRoom==null){
		//	alert("请输入会议地点!");
		//	superviseInforForm.meetRoom.focus();
		//	return false;
		//}
		return true;
	}
	//提交数据
	function submitData() {
		var form = document.superviseInforForm;
		form.action = '<c:url value="/supervise/superviseInfor"/>.do?method=save';
		form.submit();
		//window.opener.location.reload();
		//window.returnValue = "Y";
		//window.close();
	}
		
		
	//不含下拉班组信息时,改变部门信息,联动用户信息
	function loadUsers(departmentId,userSelId){
           
           var url = "/core/systemUserInfor.do?method=getTaskUsers&departmentId=" + departmentId;
           //alert(url);
           $.myLoadUsers(url,userSelId);     
	}
	
	//获取用户信息
	$.myLoadUsers = function(url,userSelId) {
		$.getJSON(url,function(data) {
			if (data != null) {
				var returnHtml = "";
				$.each(data._Users, function(i, n) {
					//$("#"+userSelId).append("<option value='"+ n.person.personId + "'>" + n.person.personName + "</option>");
					returnHtml += "<option value='"+ n.person.personId + "'";
					//alert(n.person.department.director.personId);
					//if(n.person.department.director.personId == n.personId){
						//returnHtml += " selected";
					//}
					returnHtml += ">" + n.person.personName + "</option>";
				});
				$("#"+userSelId).html(returnHtml);
			}
		});
	}
	
	
	//不含下拉班组信息时,改变部门信息,联动用户信息----根据部门名称
	function loadNormalUsers(departmentName,userSelId){
           
           var url = "/core/systemUserInfor.do?method=getUsersByDepartName&departName=" + encodeURI(departmentName);
           //alert(url);
           $.normalLoadUsers(url,userSelId);     
	}
	//获取用户信息
	$.normalLoadUsers = function(url,userSelId) {
		$.getJSON(url,function(data) {
			if (data != null) {
				var returnHtml = "";
				$.each(data._Users, function(i, n) {
					//$("#"+userSelId).append("<option value='"+ n.person.personId + "'>" + n.person.personName + "</option>");
					returnHtml += "<option value='"+ n.person.personName + "'";
					//alert(n.person.department.director.personId);
					//if(n.person.department.director.personId == n.personId){
						//returnHtml += " selected";
					//}
					returnHtml += ">" + n.person.personName + "</option>";
				});
				$("#"+userSelId).html(returnHtml);
			}
		});
	}
	
	
	//不含下拉班组信息时,改变部门信息,联动用户信息 --------根据部门获取角色中的人员
	function loadRoleUsers(departmentId,userSelId){
           
           var url = "/core/systemUserInfor.do?method=getRoleUsers&departmentId=" + departmentId;
           //alert(url);
           $.roleLoadUsers(url,userSelId);     
	}
	
	//获取用户信息
	$.roleLoadUsers = function(url,userSelId) {
		$.getJSON(url,function(data) {
			if (data != null) {
				var returnHtml = "";
				$.each(data._Users, function(i, n) {
					//$("#"+userSelId).append("<option value='"+ n.person.personId + "'>" + n.person.personName + "</option>");
					returnHtml += "<option value='"+ n.person.personId + "'";
					//alert(n.person.department.director.personId);
					//if(n.person.department.director.personId == n.personId){
						//returnHtml += " selected";
					//}
					returnHtml += ">" + n.person.personName + "</option>";
				});
				$("#"+userSelId).html(returnHtml);
			}
		});
	}
	 
</script>
<base target="_self"/>
<body onload="">
	<br/>
	<form:form commandName="superviseInforVo" name="superviseInforForm" id="superviseInforForm" action="" method="post" enctype="multipart/form-data">
		<form:hidden path="taskId" />
		<form:hidden path="status" />
		<table width="85%" cellpadding="6" cellspacing="1" align="center"
			border="0" bordercolor="#c5dbec">
			<tr style="height: 0">
				<td colspan="3" >
					<c:if test="${!empty _Batch}">
						<c:if test="${_Batch == 1}"><font color=red><b>*当前为批量新增模式</b></font></c:if>
						<input type="hidden" name="batch" value="1" />
					</c:if>
				</td>
			</tr>
			
			<tr>
				<td style="width: 15%;" class="ui-state-default jqgrid-rownum">
					工作类别：
				</td>
				<td>
					<form:input path="workType" size="30" />
					
				</td>
			</tr>
			
			<tr>
				<td class="ui-state-default jqgrid-rownum">
					工作内容和要求：
				</td>
				<td>
					<form:input path="taskName" size="50" />
					&nbsp;
					<font color="red">*</font>
				</td>
			</tr>
			<tr>
				<td class="ui-state-default jqgrid-rownum">
					类别：
				</td>
				<td>
					${_Category.categoryName}
					<form:hidden path="taskCategoryId"  />
				</td>
			</tr>
			
			<%--<tr>
				<td class="ui-state-default jqgrid-rownum">
					下达部门：
				</td>
				<td>
					<form:input path="dutyDepartment" size="30" />
					
				</td>
			</tr>
			
			<tr>
				<td class="ui-state-default jqgrid-rownum">
					下达人：
				</td>
				<td>
					<form:input path="contactPerson" size="20" />
				</td>
			</tr>
			
			--%>
			
			<tr>
				<td class="ui-state-default jqgrid-rownum">
					下达部门：
				</td>
				<td>
					<%--<form:input path="dutyDepartment" size="30" />--%>
					
					<form:select path="dutyDepartment" onchange="loadNormalUsers(this.value,'contactPerson');">
						<!--<form:option value="0">--选择部门--</form:option>-->
						<c:forEach items="${_Departments}" var="department">
							<form:option value="${department.organizeName}">${department.organizeName}</form:option>
						</c:forEach>
					</form:select>
					
				</td>
			</tr>
			
			<tr>
				<td class="ui-state-default jqgrid-rownum">
					下达人：
				</td>
				<td>
					<%--<form:input path="contactPerson" size="20" />--%>
					
					<form:select path="contactPerson">
						<!--<form:option value="0">--选择人员--</form:option>-->
						<c:forEach items="${_DutyUsers}" var="user">
							<form:option value="${user.person.personName}">${user.person.personName}</form:option>
						</c:forEach>
					</form:select>
				</td>
			</tr>
			
			<!--<tr>
				<td class="ui-state-default jqgrid-rownum">
					下达任务的领导：
				</td>
				<td>
					<form:select path="leaderId">
					<form:option value="0">--选择人员--</form:option>
					<c:forEach items="${_Users}" var="user">
						<form:option value="${user.person.personId}">${user.person.personName}</form:option>
					</c:forEach>
					</form:select>
				</td>
			</tr>
			--><tr>
				<td class="ui-state-default jqgrid-rownum">
					执行部门：
				</td>
				<td>
					<form:select path="departmentId" onchange="loadRoleUsers(this.value,'managerId');">
						<!--<form:option value="0">--选择部门--</form:option>-->
						<c:forEach items="${_Departments_ZX}" var="department">
							<form:option value="${department.organizeId}">${department.organizeName}</form:option>
						</c:forEach>
					</form:select>
					
					&nbsp;&nbsp;&nbsp;&nbsp;
					<c:if test="${!empty _Batch}">
						<c:if test="${_Batch == 1}">
							<input type="button" id="batchBtn" value="添加执行部门/负责人" onclick="addBatchDepart();" />
						</c:if>
					</c:if>
					<script>
						var clickNum = 1;
						function addBatchDepart(){
							//$("#batchTd").append(clickNum+"<br/>");
							
							var content = "<div id=batchDiv"+clickNum+">";
							
							//执行部门
							content += "<select name=\"departmentIds\" id=\"departmentId_\""+clickNum+" onchange=\"loadRoleUsers(this.value,'managerId_"+clickNum+"');\">";
							<c:forEach items="${_Departments_ZX}" var="department">		    	
						    	var name = "${department.organizeName}";
						    	var value= "${department.organizeId}";
								
						    	content += "<option value='"+value+"'>"+name+"</option>";
										
							</c:forEach>	
							content += "</select>";
							
							content += "&nbsp;&nbsp;&nbsp;&nbsp;";
							
							//执行部门负责人
							content += "<select name=\"managerIds\" id=\"managerId_"+clickNum+"\" >";
							<c:forEach items="${_Users}" var="user">		    	
						    	var name = "${user.person.personName}";
						    	var value= "${user.person.personId}";
								
						    	content += "<option value='"+value+"'>"+name+"</option>";
										
							</c:forEach>
							content += "</select>"
							
							//删除按钮
							content += "&nbsp;&nbsp;&nbsp;&nbsp;";
							content += "<a href=\"javascript:;\" onclick=\"delBatchOne("+clickNum+");\"><font color=red>删除</font></a>";
							
							content += "<br/><br/></div>";
							
							$("#batchTd").append(content);
							
							clickNum ++;
							
							//alert(clickNum);
						}
						
						function delBatchOne(idNum){
							$("#batchDiv"+idNum).remove();
						}
					</script>
				</td>
			</tr>
			<tr>
				<td class="ui-state-default jqgrid-rownum">
					执行部门负责人：
				</td>
				<td>
					<form:select path="managerId">
					<!--<form:option value="0">--选择人员--</form:option>-->
					<c:forEach items="${_Users}" var="user">
						<form:option value="${user.person.personId}">${user.person.personName}</form:option>
					</c:forEach>
					</form:select>
				</td>
			</tr>
			
			<c:if test="${!empty _Batch}">
				<c:if test="${_Batch == 1}">
					<tbody id="batchBody" >
						<tr>
							<td class="ui-state-default jqgrid-rownum">
								<font color=blue><b>批量选择执行部门/负责人：</b></font>
							</td>
							<td id="batchTd">
								
							</td>
						</tr>
					</tbody>
				</c:if>
			</c:if>
			
			<tr>
				<td style="width: 100px;" class="ui-state-default jqgrid-rownum">
					计划完成时间：
				</td>
				<td>
					<form:input path="finishDateStr" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly="true"/>
					&nbsp;
					<font color="red">*</font>
				</td>
			</tr>
			
			<c:if test="${!empty _SuperviseInfor.operator}">
				<tr>
					<td class="ui-state-default jqgrid-rownum">
						执行人：
					</td>
					<td>
						<form:select path="operatorId">
						<c:forEach items="${_ThisUsers}" var="user">
							<form:option value="${user.person.personId}">${user.person.personName}</form:option>
						</c:forEach>
						</form:select>
					</td>
				</tr>
			</c:if>		
			
			<tr>
				<td class="ui-state-default jqgrid-rownum">
					汇报周期：
				</td>
				<td>
					<form:select path="reportPeriod">
						<form:option value="">--选择周期--</form:option>
						<form:option value="单次">单次</form:option>
						<form:option value="每月">每月</form:option>
						<form:option value="每2月">每2月</form:option>
						<form:option value="每季度">每季度</form:option>
						<form:option value="每半年">每半年</form:option>
						<form:option value="每年">每年</form:option>
					</form:select>
				</td>
			</tr>
			
			<!-- <tr>
				<td valign="top">
					督办内容：
				</td>
				<td>
					<table cellspacing=0 cellpadding=0>
						<tr>
							<td>
								<form:textarea path="content" cols="65" rows="8"/>
							</td>

						</tr>
					</table>
				</td>
			</tr> -->
			
			<tr>
				<td valign="top">
					备注：
				</td>
				<td>
					<table cellspacing=0 cellpadding=0>
						<tr>
							<td>
								<form:textarea path="memo" cols="65" rows="5"/>
							</td>

						</tr>
					</table>
				</td>
			</tr>
			
			<tr>
				<td>
					附件：
				</td>
				<td>

					<table cellpadding="0" cellspacing="0"
						style="margin-bottom:0;margin-top:0">
						<tr>
							<td>
								<input type="file" name="attachment" size="50" />
								&nbsp;
								<input type="button" value="更多附件.." onclick="addtable('newstyle')"
									class="bt" />
							</td>
						</tr>
					</table>
					<span id="newstyle"></span>
				</td>
			</tr>
			<c:if test="${!empty superviseInforVo.attachmentStr}">
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
							<br/>
						</c:forEach>
					</td>
				</tr>
			</c:if>
			<tr>
				<td colspan="2">
					<input type="button" id="button" style="cursor: pointer;"
						value="提交"
						onclick="javaScript:if(validateSuperviseInfor()){submitData();}" />
					&nbsp;
					<input type="button" value="取消" style="cursor: pointer;"
						onclick="window.close();" />
				</td>
			</tr>
		</table>
	</form:form>
</body>

