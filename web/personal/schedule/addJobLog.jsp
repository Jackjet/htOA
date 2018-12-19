<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
<title>填写日志</title>
<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
<script src="<c:url value="/"/>js/inc_javascript.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/css/form.css"/>" />
<link type="text/css" href="<c:url value="/"/>components/jqgrid/css/jquery.ui.all.css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
<!-- formValidator -->
<link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css"></link>
<script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript" charset="UTF-8"></script>
<script src="<c:url value="/"/>components/formvalidator/js/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
<script src="<c:url value='/js'/>/addattachment.js"></script>

<style type="text/css">
	#lis {
		padding:0;
		margin:5;
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
	//保存回复
    function saveJobLog(){
   		var form = document.scheduleJobLogForm;
		form.action = "<c:url value='/personal/scheduleJobLog.do'/>?method=save";
		form.submit(); 
		//window.opener.location.reload();
	}
</script>
</head>
<base target="_self"/>
<body>	 
<form:form commandName="scheduleJobLogVo" name="scheduleJobLogForm" id="scheduleJobLogForm" action="/personal/scheduleJobLog.do?method=save" method="post" enctype="multipart/form-data">
<form:hidden path="scheduleId" />
<form:hidden path="logId"/>	
<input type="hidden" name="scheduleType" value="<%=request.getParameter("scheduleType") %>"/>
<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 100%">
<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
  <div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
    <span class="ui-jqgrid-title">填写日志</span>
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
			
						
						<tr>
							<td>日志内容：</td>
							<td>
							<form:textarea  rows="3" cols ="80" path="logContent"></form:textarea>
							<%--<textarea rows="3" cols ="80" name ="replyContent"></textarea>--%>
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
											<input type="button" value="更多附件.." onclick="addtable('newstyle')" class="bt"/> 
										</td>
									</tr>
								</table>
								<span id="newstyle"></span> 
							</td>
						</tr>
						
						<c:if test="${!empty(scheduleJobLogVo.attatchmentArray)}">
						
							<tr>
								<td colspan="2" valign="top">
									原附件信息(<font color="red">如果要删除某个附件，请选择该附件前面的选择框 </font>)：
								</td>
							</tr>
							
							<tr>
								<td colspan="2">
									<c:forEach var="file" items="${scheduleJobLogVo.attatchmentArray}"
										varStatus="index">
										<form:checkbox path="attatchmentArray" value="${index.index}" />
										<a href="<%=request.getRealPath("/")%>${file}">${_AttachmentLog_Names[index.index]}
										</a>
										<br/>
									</c:forEach>
								</td>
							</tr>
							
						</c:if>
						<tr>
							<td colspan="2">
								<input style="cursor: pointer;" type="submit" value="保存日志" onclick="saveJobLog();"/>
								<input style="cursor: pointer;" type="button" value="关闭" onclick="window.close();"/>
							</td>
						</tr>
						
					</tbody>
				</table>
			</div>
		</div>
	</div>
	
</div>
</div>
</form:form>
</body>
</html>
