<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
<title>任务信息查看</title>
<link type="text/css" href="<c:url value="/"/>components/jqgrid/css/jquery.ui.all.css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
<script src="<c:url value="/"/>components/jquery-1.4.2.js" type="text/javascript"></script> <!--jquery包-->
<script src="<c:url value="/"/>components/jqgrid/js/jquery-ui-1.8.2.custom.min.js" type="text/javascript"></script><!--jquery ui-->
<script src="<c:url value="/"/>components/jquery.layout.js" type="text/javascript"></script><!--jquery 布局-->
<script src="<c:url value="/"/>components/jqgrid/js/grid.locale-cn.js" type="text/javascript"></script> <!--jqgrid 中文包-->
<script src="<c:url value="/"/>components/jqgrid/js/jquery.jqGrid.min.js" type="text/javascript"></script> <!--jqgrid 包-->
<script>
	$(document).ready(function(){
		$("#panel").slideToggle("slow");
	});
	function show(num){
		 $(".panel"+num).slideToggle("slow");
	}
	  
	//添加报告
    function addReport(taskId){
		var path;
		path = "<c:url value='/customer/taskReport.do'/>?method=editReport&taskId="+taskId;
		window.name = "__self";
		window.open(path, "__self");  //注意是2个下划线 	
	}
	//修改任务
	function editTask(taskId){
		var path;
		path = "<c:url value='/customer/taskInfor.do'/>?method=edit&flag=0&taskId="+taskId;
		window.name = "__self";
		window.open(path, "__self");  //注意是2个下划线 
	}
	
	//删除报告
	function deleteReport(reportId){
		var path;
		path = "<c:url value='/customer/taskReport.do'/>?method=deleteReport&reportId="+reportId;
		window.name = "__self";
		window.open(path, "__self");  //注意是2个下划线 
		alert('删除成功！');
	
		<%--
		$.ajax({
				url: "/customer/taskReport.do?method=deleteReport&reportId="+reportId,
				type: "GET",					
				dataType: "html",
				beforeSend: function (xhr) {
				},
				complete : function (req, err) {
					alert('删除成功！');
					self.location.reload();
				}
			});
		--%>
	}
	
</script>
<style type="text/css"> 
	div#panel,div#flip
	{
	margin:0px;
	padding:5px;
	text-align:center;
	background:#e5eecc;
	border:solid 1px #c3c3c3;
	}
	div#panel
	{
	display:none;
	}
</style>
</head>
<body>	 
<form>
<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 100%">
<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
  <div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
    <span class="ui-jqgrid-title">任务信息查看</span>
  </div>

	<div class="ui-jqgrid-bdiv" style="height: auto; width: 100%">
		<div style="position: relative;">
			<div>
				<table cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable" style="width: 100%">
					<tbody>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td style="width: 15%;" class="ui-state-default jqgrid-rownum">任务编号：</td>
							<td style="width: 35%;">${_TaskInfor.taskCode}</td>
							<td style="width: 15%;" class="ui-state-default jqgrid-rownum" >任务名称：</td>
							<td style="width: 35%;">${_TaskInfor.taskName}</td>
						</tr>
						
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">所属分类：</td>
							<td valign="top">${_TaskInfor.projectCategory.categoryName}</td>
							<td class="ui-state-default jqgrid-rownum">状态：</td>
							<td><c:choose><c:when test="${_TaskInfor.status==0}">进行</c:when><c:when test="${_TaskInfor.status==1}">通过</c:when><c:when test="${_TaskInfor.status==2}">暂停</c:when><c:otherwise>过期</c:otherwise></c:choose></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">开始日期：</td>
							<td>${_TaskInfor.startDate}</td>
							<td class="ui-state-default jqgrid-rownum">结束日期：</td>
							<td>${_TaskInfor.endDate}</td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">提交日期：</td>
							<td colspan="3">${_TaskInfor.updateDate}</td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">审核人：</td>
							<td>${_TaskInfor.checker.person.personName}</td>
							<td class="ui-state-default jqgrid-rownum">审核日期：</td>
							<td>${_TaskInfor.checkTime}</td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 70px;">
							<td class="ui-state-default jqgrid-rownum">审核意见：</td>
							<td valign="top" colspan="3">${_TaskInfor.checkComment}</td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 70px;">
							<td class="ui-state-default jqgrid-rownum">内容简介：</td>
							<td valign="top" colspan="3">${_TaskInfor.content}</td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 70px;">
							<td class="ui-state-default jqgrid-rownum">备注：</td>
							<td valign="top" colspan="3">${_TaskInfor.memo}</td>
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
									<tr><td><input style="cursor: pointer;" type="button" value="关闭" onclick="window.close();"/></td>
										<td><input style="cursor: pointer;" type="button" value="添加报告" onclick="addReport(${_TaskInfor.taskId});"/></td>
										<td><input style="cursor: pointer;" type="button" value="修改" onclick="editTask(${_TaskInfor.taskId});"/></td>
									</tr>
								</tbody>
							</table>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	<div>
		<table width="100%">
			<tr>
				<td valign="left">
					<c:set var="stu" value="0"/>
					<c:forEach items="${_TaskReports}" var="taskReport" >
						<div class="flip${stu}" style="height: 20px;width: 100%;" onclick="show(${stu})" id="flip">
							<p style="width=100%;">${taskReport.person.person.personName}的报告:</p>
						</div>
						<div class="panel${stu}" id="panel">
							<table width="100%">
								<tr>
									<td align="left" style="width:10%">提交时间：</td><td align="left" style="width:90%">${taskReport.updateDate}</td>
								</tr>
								<tr>
									<td align="left" style="width:10%;">报告内容：</td><td align="left" style="width:90%"><p style="width:90%">${taskReport.content}</p></td>
								</tr>
								<tr>
									<td align="left" style="width:10%">附件内容：</td><td align="left" style="width:90%"><p style="width:90%"><c:choose><c:when test="${empty _Attachment[stu][0][0]}">附件为空</c:when><c:otherwise><c:forEach  var="file"  items="${_Attachment[stu][0]}"  varStatus="status"><a href="<c:url value="${'/common/'}"/>download.jsp?filepath=${_Attachment[stu][1][status.index]}"><span style="color: red">${file}</span></a><br/> </c:forEach></c:otherwise></c:choose></p></td>
								</tr>
								<tr style="height: 20px;">
									<td colspan="2" align="left"><input type="button" value="删除" onclick="deleteReport(${taskReport.reportId});"/></td>
								</tr>
							</table>
						</div>
						<br/>
						<c:set var="stu" value="${stu+1}"/>
					</c:forEach>
				</td>
			</tr>
		</table>
	</div>
</div>
</div>
</form>
</body>
</html>
