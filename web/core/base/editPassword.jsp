<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<title>修改密码</title>
<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
<link type="text/css" href="<c:url value="/"/>components/jqgrid/css/jquery.ui.all.css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />

<!-- formValidator -->
<link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css"></link>
<link type="text/css" rel="stylesheet" href="/css/noTdBottomBorder.css"></link>
<script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript" charset="UTF-8"></script>
<script src="<c:url value="/"/>components/formvalidator/js/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
<!-- ------------- -->

<script>
	//验证
	$(document).ready(function(){
		$.formValidator.initConfig({formid:"systemUserForm",onerror:function(msg){alert(msg)},onsuccess:function(){alert('密码修改成功！');}});
		//验证输入的原始密码是否正确
		$("#oldPassword").formValidator({onshow:"请输入原始密码",onfocus:"原始密码至少6位",oncorrect:"原始密码输入正确"}).inputValidator({min:6,onerror:"原始密码输入错误，请重新输入"})
			    .ajaxValidator({
			    type : "get",
				url : "/core/systemUserInfor.do?method=validateOldPas",
				datatype : "json",
				success : function(returnMap){
		            if (!returnMap.isRight){
		        		alert("原始密码输入错误，请重新输入！");
		        		return false;
		        	}else {
		        		return true;
		        	}
				},
				buttons: $("#button"),
				error: function(){alert("服务器没有返回数据，可能服务器忙，请重试");},
				onerror : "原始密码输入错误，请重新输入！",
				onwait : "正在对原始密码进行合法性校验，请稍候..."
			});
		$("#password").formValidator({onshow:"请输入密码",onfocus:"密码不能为空",oncorrect:"密码合法"}).inputValidator({min:1,empty:{leftempty:false,rightempty:false,emptyerror:"密码两边不能有空符号"},onerror:"密码不能为空,请确认"});
		$("#rePassword").formValidator({onshow:"请再次输入密码",onfocus:"密码不能为空",oncorrect:"密码合法"}).inputValidator({min:1,empty:{leftempty:false,rightempty:false,emptyerror:"密码两边不能有空符号"},onerror:"请再次输入密码"}).functionValidator({
		    fun:function(val,elem){
		        var password = document.getElementById("password").value;
		        if(password != val){
					alert("密码及确认密码不一致,请更改!");
					return false;
				}else {
					return true;
				}
			}
		});
	});
	
	
	
</script>

<body style="overflow-y: auto;padding: 0 100px">
<br/>
<form:form commandName="systemUserInforVo" id="systemUserForm" name="systemUserForm" action="/core/systemUserInfor.do?method=savePassword" method="post">
<form:hidden path="personId"/>
<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 100%">
<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
  <div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
    <span class="ui-jqgrid-title">修改密码</span>
  </div>

	<div class="ui-jqgrid-bdiv" style="height: auto; width: 100%">
		<div style="position: relative;">
			<div>
				<table cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable" style="width: 100%">
					<tbody>
						
						<c:if test="${! empty _User.person.department}">
							<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
								<td style="width: 15%;" class="ui-state-default jqgrid-rownum">所属部门：</td>
								<td colspan="2">${_User.person.department.organizeName}</td>
							</tr>
						</c:if>
						
						<c:if test="${! empty _User.person.group}">
							<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
								<td class="ui-state-default jqgrid-rownum">所属班组：</td>
								<td colspan="2">${_User.person.group.organizeName}</td>
							</tr>
						</c:if>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">人员：</td>
							<td colspan="2">${_User.person.personName}</td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">用户名：</td>
							<td colspan="2">${_User.userName}</td>
						</tr>
						
					    <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
					        <td class="ui-state-default jqgrid-rownum">原始密码：</td>
					        <td style="width: 200px;"><form:input path="oldPassword" size="20"/></td>
					        <td><div id="oldPasswordTip" ></div></td>
					    </tr>
					    
					    <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
					        <td class="ui-state-default jqgrid-rownum">新密码：</td>
					        <td><form:password path="password" size="20"/></td>
					        <td><div id="passwordTip" ></div></td>
					    </tr>
					    
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
					        <td class="ui-state-default jqgrid-rownum">确认新密码：</td>
					        <td><form:password path="rePassword" size="20"/></td>
					        <td><div id="rePasswordTip"></div></td>
					    </tr>
						
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
										<td><input style="cursor: pointer;" type="submit" id="button" value="提交"/></td>&nbsp;
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

