<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
<title>编辑功能权限</title>
<link type="text/css" href="<c:url value="/"/>components/jqgrid/css/jquery.ui.all.css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
<link rel="stylesheet" type="text/css" media="screen" href="/css/noTdBottomBorder.css" />
	<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
<style type="text/css">
	#lis {
		padding:0;
		margin:5px;
	}
	#lis .liDetail {
		list-style:none;
		margin-left:20px;
	}
	#lis .liDetail2 {
		list-style:none;
		margin-left:20px;
		height: 30px;
	}
	#lis .liDetail3 {
		list-style:none;
		margin-left:18px;
	}
</style>
<script>
	window.onload = function() {
		//勾选选中的角色
		var roleIds = document.getElementsByName('roleIds');
		<c:forEach var="roleId" items="${_RoleIds}">
			var tmpRoleId = '${roleId}';
			if (roleIds != null && roleIds.length > 0) {
				for (var i=0;i<roleIds.length;i++) {
					var roleId = roleIds[i];
					if (tmpRoleId == roleId.value) {
						roleId.checked = true;
					}
				}
			}
		</c:forEach>
	}
	//提交数据
	function submitData() {
		var form = document.virtualResourceForm;
		form.action = '<c:url value="/core/virtualResource.do"/>?method=save';
        var browserName=navigator.userAgent.toLowerCase();
        if(/chrome/i.test(browserName)&&/webkit/i.test(browserName)&&/mozilla/i.test(browserName)){
            //如果是chrome浏览器
            $.ajax({
                type: "POST",
                url:'<c:url value="/core/virtualResource.do"/>?method=save',
                data:$('#virtualResourceForm').serialize(),
                async: false
            });
        }else{
            form.submit();
		}

		alert('信息编辑成功！');
		window.returnValue = "refresh";
		window.close();
	}
</script>
</head>
<base target="_self"/>
<body style="padding: 0 100px">
<br/>
<form id="virtualResourceForm" name="virtualResourceForm" action="/core/virtualResource.do?method=save" method="post">
<input type="hidden" name="resourceId" value="${_Resource.resourceId}"/>
<input type="hidden" name="operationId" value="${_Operation.operationId}"/>
<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 100%">
<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
  <div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
    <span class="ui-jqgrid-title">编辑功能权限</span>
  </div>

	<div class="ui-jqgrid-bdiv" style="height: auto; width: 100%">
		<div style="position: relative;">
			<div>
				<table cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable" style="width: 100%">
					<tbody>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td style="width: 15%;" class="ui-state-default jqgrid-rownum">模块名称：</td>
							<td style="width: 85%;">${_Resource.resourceName}</td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">功能描述：</td>
							<td>${_Resource.description}</td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">相关操作：</td>
							<td>${_Operation.operationName}</td>
						</tr>
						
						<c:if test="${!empty _Roles}">                    
					       <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
					          <td class="ui-state-default jqgrid-rownum" valign="top">操作角色：</td>
					          <td><ul id="lis"><c:forEach var="role" items="${_Roles}"><li><input type="checkbox" name="roleIds" value="${role.roleId}"/> ${role.roleName}</li></c:forEach></ul></td>
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
										<td><input style="cursor: pointer;" type="button" value="提交" onclick="submitData();"/></td>
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
</form>
</body>
</html>
