<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<title>查看节点信息</title>
<link type="text/css" href="<c:url value="/"/>components/jqgrid/css/jquery.ui.all.css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />

<script>
	//修改
	function editInfor(){
		var path = "<c:url value='/workFlowDefinition.do'/>?method=editNode&nodeId="+${_FlowNode.nodeId}+"&flowId="+${_FlowNode.flowDefinition.flowId};
		window.name = "__self";
		window.open(path, "__self");
	}
</script>
<base target="_self"/>

<body>
<form id="nodeForm" name="nodeForm" action="<c:url value="/workFlowDefinition.do"/>?method=saveNode" method="post" enctype="multipart/form-data">
<input type="hidden" name="nodeId" id="nodeId" value="${_FlowNode.nodeId}"/>

<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 100%">
<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
    <div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
    	<span class="ui-jqgrid-title">查看节点信息</span>
    </div>

	<div class="ui-jqgrid-bdiv" style="height: auto; width: 100%">
		<div style="position: relative;">
			<div>
				<table cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable" style="width: 100%" id="addTable">
					<tbody>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum" width="15%">节点名称：</td>
							<td>${_FlowNode.nodeName}</td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">所属流程：</td>
							<td>${_FlowNode.flowDefinition.flowName}</td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">节点类型：</td>
							<td><c:if test="${_FlowNode.nodeType == 1}">普通节点</c:if><c:if test="${_FlowNode.nodeType == 2}">分叉节点</c:if><c:if test="${_FlowNode.nodeType == 3}">聚合节点</c:if><c:if test="${_FlowNode.nodeType == 4}">状态节点</c:if></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">审核结束类型：</td>
							<td><c:if test="${_FlowNode.finishType == 0}">只要有一人通过审批即向下流转</c:if><c:if test="${_FlowNode.finishType == 1}">只有全部人员通过审批才向下流转</c:if></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">层次：</td>
							<td>${_FlowNode.layer}</td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">是否分叉内节点：</td>
							<td><c:if test="${_FlowNode.forked == false}">不是</c:if><c:if test="${_FlowNode.forked == true}">是</c:if></td>
						</tr>
						
						<%--<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">来自分叉的节点：</td>
							<td><c:forEach items="${_ForkedNodes}" var="dep"><c:if test="${_FlowNode.forkedNode.nodeId == dep.nodeId}">${dep.nodeName}</c:if></c:forEach></td>
						</tr>--%>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">来自分叉的节点：</td>
							<td>${_FlowNode.forkedNode.nodeName}</td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">是否可打印：</td>
							<td><c:if test="${_FlowNode.printable == false}">不可以</c:if><c:if test="${_FlowNode.printable == true}">可以</c:if></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">是否可下载：</td>
							<td><c:if test="${_FlowNode.download == false}">不可以</c:if><c:if test="${_FlowNode.download == true}">可以</c:if></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">是否可上传：</td>
							<td><c:if test="${_FlowNode.upload == false}">不可以</c:if><c:if test="${_FlowNode.upload == true}">可以</c:if></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">节点说明：</td>
							<td>${_FlowNode.memo}</td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">审核者类型：</td>
							<td><c:if test="${_FlowNode.checkerType == 0}">手工设定</c:if><c:if test="${_FlowNode.checkerType == 1}">自定义</c:if><c:if test="${_FlowNode.checkerType == 2}">岗位</c:if><c:if test="${_FlowNode.checkerType == 3}">部门</c:if><c:if test="${_FlowNode.checkerType == 4}">人员</c:if><c:if test="${_FlowNode.checkerType == 11}">特殊审核人</c:if></td>
						</tr>
						
						<c:if test="${_FlowNode.checkerType != 0}">
							<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
								<td class="ui-state-default jqgrid-rownum">审核者：</td>
								<td id="checkerTypeCon"><c:choose><c:when test="${_FlowNode.checkerType == 1}"><c:forEach items="${_FlowNode.checkerPersons}" var="checkerPerson">${checkerPerson.user.person.personName} </c:forEach><c:forEach items="${_FlowNode.checkerRoles}" var="checkerRole">${checkerRole.role.roleName} </c:forEach></c:when><c:when test="${_FlowNode.checkerType == 2}">${_FlowNode.structure.structureName}</c:when><c:when test="${_FlowNode.checkerType == 3}">${_FlowNode.department.organizeName}</c:when><c:when test="${_FlowNode.checkerType == 4}">${_FlowNode.user.person.personName}</c:when><c:when test="${_FlowNode.checkerType == 11}"><c:if test="${_FlowNode.special==1}">部门领导</c:if><c:if test="${_FlowNode.special==2}">申请者</c:if></c:when></c:choose></td>
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
										<td><input style="cursor: pointer;" type="button" value="修改" onclick="editInfor();"/></td>
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

                  
