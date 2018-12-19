<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<title>编辑系统用户</title>
<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
<script src="<c:url value="/"/>js/commonFunction.js"></script>
<link type="text/css" href="<c:url value="/"/>components/jqgrid/css/jquery.ui.all.css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
<link rel="stylesheet" type="text/css" media="screen" href="/css/noTdBottomBorder.css" />

<!-- formValidator -->
<link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css"></link>
<script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript" charset="UTF-8"></script>
<script src="<c:url value="/"/>components/formvalidator/js/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
<!-- ------------- -->


<script>
	//部门,班组,用户下拉联动 
		//下拉数据初始化
		$.fn.selectInit = function(){return $(this).html("<option value=''>请选择</option>");};
		
		//加载部门及联动信息
		$.loadDepartments_Persons = function(departmentId, groupId, personId) {
			//加载部门数据
//			$.getJSON("/core/organizeInfor.do?method=getDepartments",function(data) {
//				if (data != null) {
//				    $.each(data._Departments, function(i, n) {
//					    $("#"+departmentId).append("<option value='"+ n.organizeId + "'>" + n.organizeName + "</option>");
//					});
//				}
//			});
			
			if ($("#"+groupId).html() != null) {
				//含下拉班组信息时,改变部门信息,联动班组信息
				$("#"+departmentId).bind("change",function(){
		           $("#"+groupId).selectInit();
		           var depSelectId = $("#"+departmentId+" option:selected").val();
		           var groupUrl = "/core/organizeInfor.do?method=getGroups&departmentId=" + depSelectId;
		           $.loadGroups(groupUrl, groupId);
				});
					
				//含下拉班组信息时,改变班组信息,联动用户信息
				$("#"+groupId).bind("change",function(){
			       $("#"+personId).selectInit();
			       var groSelectId = $("#"+groupId+" option:selected").val(); 
			       var userUrl = "/core/personInfor.do?method=getPersons&groupId=" + groSelectId;
			       $.loadPersons(userUrl, personId);
				});
			}else {
				//不含下拉班组信息时,改变部门信息,联动人员信息
				$("#"+departmentId).bind("change",function(){
		            $("#"+personId).selectInit();
		            var depSelectId = $("#"+departmentId+" option:selected").val();
		            var url = "/core/personInfor.do?method=getPersons&departmentId=" + depSelectId;
		            $.loadPersons(url, personId);                    
				});
			}
		}
			 
		//获取班组信息
		$.loadGroups = function(url, groupId) {
			$.getJSON(url,function(data) {
				if (data != null) {
			 		$.each(data._Groups, function(i, n) {
				 		$("#"+groupId).append("<option value='"+ n.organizeId +"'>" + n.organizeName + "</option>");
				 	});
			 	}
			});
		}
			 
		//获取人员信息
		$.loadPersons = function(url, personId) {
			$.getJSON(url,function(data) {
				if (data != null) {
					$.each(data._Persons, function(i, n) {
						$("#"+personId).append("<option value='"+ n.personId + "'>" + n.personName + "</option>");
					});
				}
			});
		}
</script>


<script>
	//验证
	$(document).ready(function(){
		//alert(${param.rowId});
		$.formValidator.initConfig({formid:"systemUserForm",onerror:function(msg){alert(msg)},onsuccess:function(){submitData();}});
		if (${empty param.rowId}) {
			//新增信息时的验证
			$("#personId").formValidator({onshow:"请选择人员",onfocus:"人员必须选择",oncorrect:"选择正确",defaultvalue:""}).inputValidator({min:1,onerror: "请选择人员"});//.defaultPassed();
		}
		$("#userName").formValidator({onshow:"请输入用户名",onfocus:"用户名至少3个字符",oncorrect:"该用户名可以注册"}).inputValidator({min:3,onerror:"输入的用户名非法,请确认"}).regexValidator({regexp:"username",datatype:"enum",onerror:"用户名格式不正确"})
		/*.ajaxValidator({
			type : "get",
			url : "/core/systemUserInfor.do?method=validate&personId=${param.rowId}",
			datatype : "json",
			success : function(returnMap){
		       if (returnMap.isDuplicate){
		        	return false;
		       }else {
		        	return true;
		       }
			},
			buttons: $("#button"),
			error: function(){alert("服务器没有返回数据，可能服务器忙，请重试");},
			onerror : "该用户名已存在，请更换用户名",
			onwait : "正在对用户名进行合法性校验，请稍候..."
		});*/
		
		.functionValidator({
		    fun:function(val,elem){
		        var isDuplicate = false;
		        $.ajax({
					url: "/core/systemUserInfor.do?method=validate&personId=${param.rowId}&userName="+val,
					type: "post",
					dataType: "json",
					async: false,	//设置为同步
					beforeSend: function (xhr) {
					},
					complete : function (req, err) {
						var returnValues = eval("("+req.responseText+")");
						if (returnValues["isDuplicate"]){
					        alert("该用户名已存在，请更换用户名");
					        isDuplicate = true;
					    }else {
					    	isDuplicate = false;
					    }
					}
				});
				if (isDuplicate) {
					return false;
				}else {
					return true;
				}
			}
		});
		
		$("#password").formValidator({onshow:"请输入密码",onfocus:"密码不能为空",oncorrect:"密码合法"}).inputValidator({min:1,empty:{leftempty:false,rightempty:false,emptyerror:"密码两边不能有空符号"},onerror:"密码不能为空,请确认"});
	
		//加载部门及联动信息
		$.loadDepartments_Persons("departmentId", null, "personId");
		
		
		/** ******** */
			
	});
	
	//提交数据
	function submitData() {
//		alert('信息编辑成功！');
//		window.returnValue = "refresh";
//		window.close();
		var form = document.systemUserForm;
        form.submit();
        window.opener.reload();
	}
	/**
	//添加用户
	function addUser() {
    	$.getJSON("/core/systemUserInfor.do?method=validate", {userName: $('#userName').val()}, function(returnMap) {  
        	//alert(returnMap.isDuplicate);
        	//验证用户名重复
        	if (returnMap.isDuplicate) {
        		alert("该用户名已存在,请另取他名!");
        	}else {
        		submitData();
        	}
        });
    }
    */
</script>
<base target="_self"/>
<body style="padding:0 100px">
<br/>
<form:form commandName="systemUserInforVo" id="systemUserForm" name="systemUserForm" action="/core/systemUserInfor.do?method=save" method="post">
<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 100%">
<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
  <div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
    <span class="ui-jqgrid-title">编辑系统用户</span>
  </div>

	<div class="ui-jqgrid-bdiv" style="height: auto; width: 100%">
		<div style="position: relative;">
			<div>
				<table cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable" style="width: 100%">
					<tbody>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td style="width: 20%;" class="ui-state-default jqgrid-rownum">所属部门：</td>
							<td style="width: 45%;">
								<c:choose>
									<c:when test="${empty _User}">
										<form:select path="departmentId">
											<form:option value="0">--选择部门--</form:option>
											<c:forEach items="${_Departments}" var="department">

												<form:option value="${department.organizeId}">${department.organizeName}</form:option>
											</c:forEach>
							         </form:select>
								</c:when>
									<c:otherwise>${_User.person.department.organizeName}
									</c:otherwise>
								</c:choose></td>
					         <td style="width: 35%;"></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">人员：</td>
							<td><c:choose><c:when test="${empty _User}"><form:select path="personId">
								       <form:option value="">--选择人员--</form:option>
								       <c:forEach items="${_Persons}" var="person" varStatus="status">
								           <form:option value="${person.personId}">${person.personNo}-${person.personName}</form:option>
								       </c:forEach>										
								    </form:select></c:when><c:otherwise>${_User.person.personName}<form:hidden path="personId"/></c:otherwise></c:choose></td>
							 <td><div id="personIdTip" style="width:250px"></div></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">登录用户名：</td>
							<td><form:input path="userName" size="20"/></td>
							<td><div id="userNameTip" style="width:250px"></div></td>
						</tr>
						
					    <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
					        <td class="ui-state-default jqgrid-rownum">登录密码：</td>
					        <td><form:input path="password" size="20"/></td>
					        <td><div id="passwordTip" style="width:250px"></div></td>
					    </tr>
					    
					    <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
					        <td class="ui-state-default jqgrid-rownum">用户类型：</td>
					        <td colspan="2"><form:select path="userType">
					              <form:option value="0">普通用户</form:option>
					              <form:option value="1">系统管理员</form:option>
					              <%--<form:option value="2">档案员</form:option>--%>
					              <form:option value="2">投票用户</form:option>
					              <form:option value="3">非合同制用工</form:option>
					          </form:select></td>
					    </tr>
					    
					    <c:if test="${!empty _Roles}">                    
					       <tr style="height: 30px;">
					          <td class="ui-state-default jqgrid-rownum">用户角色：</td>
					          <td colspan="2">
						          <table>
						          	<tr>
						          	<c:set var="_TypeNum" value="0"/>
					      			<c:forEach var="role" items="${_Roles}" varStatus="index">
										<c:if test="${_TypeNum!=0 && _TypeNum%5==0}">
											</tr><tr>
										</c:if>
										<td width="16%">
											<form:checkbox path="roleIds" value="${role.roleId}"/> ${role.roleName}
										</td>
										<c:set var="_TypeNum" value="${_TypeNum+1}"/>
									</c:forEach>
									<c:forEach begin="${_TypeNum%5}" end="4">
										<td width="15%">&nbsp;</td>
									</c:forEach>
									</tr>
								  </table>
							  </td>
					       </tr>
					    </c:if>
						
					</tbody>
				</table>
			</div>
		</div>
	</div>
	
	<div style="width: 100%" class="ui-state-default ui-jqgrid-pager ui-corner-bottom" dir="ltr">
		<div role="group" class="ui-pager-control">
			<table cellspacing="0" cellpadding="0" border="0" style="width: 100%; table-layout: fixed;" class="ui-pg-table">
				<tbody>
					<tr>
						<td align="left">
							<table cellspacing="0" cellpadding="0" border="0" style="float: left; table-layout: auto;" class="ui-pg-table navtable">
								<tbody>
									<tr>
										<%--<td><input style="cursor: pointer;" type="submit" id="button" value="提交"/></td>--%>
										<td><input style="cursor: pointer;" type="button" id="button" onclick="submitData()" value="提交"/></td>
										<td><input style="cursor: pointer;" type="button" value="关闭" onclick="window.close();"/></td>
									</tr>
								</tbody>
							</table>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</div>
</div>
</form:form>
</body>

