<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
<title>添加决议附件</title>
<script src="<c:url value="/"/>datePicker/WdatePicker.js" type="text/javascript"></script>
<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
<script src="<c:url value="/"/>js/inc_javascript.js"></script>
<script src="<c:url value="/"/>js/addattachment.js"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/"/>components/jqgrid/css/jquery.ui.all.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />

<!-- formValidator -->
<link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css"></link>
<script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript" charset="UTF-8"></script>
<script src="<c:url value="/"/>components/formvalidator/js/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
<!-- ------------- -->

</head>
<base target="_self"/>
<body>

<form:form commandName="flowInstanceInforVo" id="instanceInforForm" name="instanceInforForm" action="/workflow/instanceInfor.do?method=saveResAttach" method="post" enctype="multipart/form-data">
<form:hidden path="instanceId"/>
<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 98%">
	<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
	<div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
		<span class="ui-jqgrid-title">添加决议附件 &nbsp;【${_Flow.flowName} 主办人：${_Instance.charger.person.personName}】</span>
	</div>

	<%-- 审核实例信息 --%>	
	<%@include file="includeInstance.jsp" %>

				<table cellspacing="0" cellpadding="0" border="0" style="width: 90%">
					<tbody>
						<tr class="ui-widget-content jqgrow ui-row-ltr">
							<td class="ui-state-default jqgrid-rownum" colspan="3"><b>添加附件操作如下</b>：</td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum" style="width: 15%">备注：</td>
							<td><textarea name="attachMemo" id="attachMemo" cols="60" rows="5">${_Instance.attachMemo}</textarea></td>						
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum "style="width: 15%">决议附件：</td>
							<td align="left" colspan="2" id="newstyle"><input type="file" name="attachment" id="attachment" size="50" />&nbsp;<a href="javascript:;" onclick="addtable('newstyle')">更多附件..</a></td>							
						</tr>
						
						<c:if test="${!empty _ResAttachment_Names}">
							<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
								<td colspan="3" class="ui-state-default jqgrid-rownum">原附件信息(<font color="white">如果要删除某个附件，请选择该附件前面的选择框</font>)：</td>
							</tr>
							<tr>
								<td colspan="3"><c:forEach var="file" items="${_ResAttachments}" varStatus="index">
									<input type="checkbox" name="attatchmentArray" value="${index.index}" />
									<a href="<%=request.getRealPath("/")%>${file}">${_ResAttachment_Names[index.index]}</a><br/>
								</c:forEach></td>
							</tr>
						</c:if>
								
						<tr class="ui-widget-content jqgrow ui-row-ltr">
							<td class="ui-state-default jqgrid-rownum" colspan="3"><input style="cursor: pointer;" type="submit" value="保存"/>&nbsp;<input style="cursor: pointer;" type="button" value="关闭" onclick="window.close();"/></td>
						</tr>
						
					</tbody>
				</table>
	
</div>
</div>
</form:form>
</body>
</html>
                  
