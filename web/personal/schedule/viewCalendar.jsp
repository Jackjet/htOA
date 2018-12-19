<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
<title>查看日程信息</title>
<link type="text/css" href="<c:url value="/"/>components/jqgrid/css/jquery.ui.all.css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
<link rel="stylesheet" type="text/css" media="screen" href="/css/noTdBottomBorder.css" />
<link rel="stylesheet" type="text/css" media="screen" href="/css/border.css" />
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
	//写日志
    function jobLog(logId,scheduleType){
		var form = document.scheduleJobLogForm;
		form.action = "<c:url value='/personal/scheduleJobLog.do'/>?method=edit&logId="+logId+"&scheduleType="+scheduleType;
		form.submit();
		//window.name = "__self";
		//window.open(form.action, "__self"); //注意是2个下划线 		
	}
	
	//修改为2
	function edit(scheduleId,scheduleType){
		//var path;
		//path = "<c:url value='/personal/personalJobInfor.do'/>?method=edit&scheduleId="+scheduleId+"&scheduleType="+scheduleType;
		//window.location.href = path;	
		
		//window.name = "__self";
		//window.open(path, "__self"); //注意是2个下划线
		var form = document.scheduleJobLogForm;
		form.action = "<c:url value='/personal/personalJobInfor.do'/>?method=edit&scheduleId="+scheduleId+"&scheduleType="+scheduleType;
		form.submit();
	}
	
</script>
</head>
<base target="_self"/>
<body style="overflow-y: auto;padding: 0 100px">
<br/>
<form:form commandName="scheduleJobLogVo" name="scheduleJobLogForm" id="scheduleJobLogForm" action="" method="post" enctype="multipart/form-data">
<input type="hidden" name="scheduleId" value="${_Schedule.scheduleId}"/>	
<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 100%">
<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
  <div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
    <span class="ui-jqgrid-title">查看日程信息</span>
  </div>

	<div class="ui-jqgrid-bdiv" style="height: auto; width: 100%">
		<div style="position: relative;">
			<div>
				<table cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable" style="width: 100%">
					<tbody>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td style="width: 15%;" class="ui-state-default jqgrid-rownum">标&nbsp;&nbsp;&nbsp;&nbsp;题：</td>
							<td style="width: 85%;">${_Schedule.jobTitle}</td>
						</tr>
												
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">安排时间：</td>
							<td><fmt:formatDate value="${_Schedule.writeTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">重要性：</td>	
							<td><c:if test="${_Schedule.isImportant==1}"><font color="red">重要</font></c:if><c:if test="${_Schedule.isImportant==0}">不重要</c:if></td>	
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">公开模式：</td>
							<td><c:if test="${_Schedule.openType==1}"><font color="red">公开</font></c:if><c:if test="${_Schedule.openType==0}">不公开</c:if></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">开始时间：</td>
							<td><fmt:formatDate value="${_Schedule.startDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">结束时间：</td>
							<td><fmt:formatDate value="${_Schedule.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 80px;">
							<td class="ui-state-default jqgrid-rownum" >工作内容：</td>
							<td valign="top" >${_Schedule.jobContent}</td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">附件信息：</td>
							<td align="left"><c:forEach  var="file"  items="${_Attachment_Names}"  varStatus="status"><a href="<c:url value="${'/common/'}"/>download.jsp?filepath=${_Attachments[status.index]}"><font color="red">${file}</font></a><br/> </c:forEach></td>
						</tr>
						
						<c:if test="${!empty _LogList}">
							<tr class="ui-widget-content jqgrow ui-row-ltr">
								<td class="ui-state-default jqgrid-rownum" colspan="2"><strong>如下为工作日志内容：</strong></td>
							</tr>						
							
							<tr>
								<td colspan="2">
									<table cellspacing="1" cellpadding="1" border="0" width="100%">														
									<c:forEach items="${_LogList}" var="jobLog" varStatus="status">													
											<tr>
												<td style="width: 15%;">${jobLog.executor.person.personName }：</td>
												<td><fmt:formatDate value="${jobLog.writeTime}" pattern="yyyy-MM-dd HH:mm:ss"/><br/>${jobLog.logContent}
												<c:if test="${jobLog.executor.person.personId == _SYSTEM_USER.personId}">
											  		<img title="点击修改" onclick="jobLog('${jobLog.logId}','${_ScheduleType}');" style="cursor:hand;" src="<c:url value="/images/"/>edit.gif" border="0"/>
											  	</c:if>
												
												</td>
											</tr>
											
											
											<c:if test="${!empty jobLog.attachment}">
											<tr>
												<td valign="top"></td>
												<td>
												<font color="red">
												<attachment:fileView contextPath="">
													${jobLog.attachment}
												</attachment:fileView>
												</font>
											  	</td>
										  	</tr>
										  	</c:if>
										  	
										  	<tr>
						                     	<td style='border-bottom:1px dotted #888888;font-size:10pt' valign="top" colspan="2">&nbsp;</td>
											</tr>
											
									</c:forEach>
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
									 <c:if test="${_Status == 0 && _Is_Excuter}">  
										<td><input style="cursor: pointer;" type="button" value="写日志" onclick="jobLog(0,'${_ScheduleType}');"/></td>
									 </c:if>
									 <c:if test="${_Is_Assigner}">
										<td><input style="cursor: pointer;" type="button" value="修改" onclick="edit('${_Schedule.scheduleId}','${_ScheduleType}');"/></td>
									</c:if>
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
</html>
