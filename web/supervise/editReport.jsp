<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
<title>工作跟踪</title>
<script src="<c:url value="/"/>datePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
	<script src="<c:url value="/"/>js/inc_javascript.js"></script>
	<script src="<c:url value="/"/>js/addattachment.js"></script>
	
	<link rel="stylesheet" type="text/css" href="<c:url value="/"/>components/jqgrid/css/jquery.ui.all.css" />
	<%--<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />--%>
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>css/base/jquery-ui-1.9.2.custom.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
	
	<!-- formValidator -->
	<link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css"></link>
	<link type="text/css" rel="stylesheet" href="/css/noTdBottomBorder.css"></link>
	<link type="text/css" rel="stylesheet" href="/css/border.css"></link>
	<link type="text/css" rel="stylesheet" href="/css/myTable.css"></link>
	<script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript" charset="UTF-8"></script>
	<script src="<c:url value="/"/>components/formvalidator/js/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
	<style type="text/css">
		.buttonclass {
			font-weight:bold;
		}
		/*html,
		body {
			overflow: hidden;!important;
			height: 100%;!important;
			margin: auto;!important;
			padding: auto;!important;
			font: 14px Georgia, Arial, Simsun;!important;
			background-image: url(/img/bgIn.png);!important;
			background-size: cover;!important;
			background-color: #e3e3e3;!important;
			overflow-y: visible;!important;
			font-family:  "黑体" ;!important;
		}*/

	</style>
<script>
    window.location ="#zhidao";//自动跳转到锚点处
	$(document).ready(function(){
		//验证
		$.formValidator.initConfig({formid:"superviseReportForm",onerror:function(msg){alert(msg)},onsuccess:function(){}});
		$("#content").formValidator({onshow:"请输入报告内容",onfocus:"报告内容不能为空",oncorrect:"输入正确"}).inputValidator({min:1,onerror: "请输入报告内容"});
		$("#operateDate").formValidator({onshow:"请输入日期",onfocus:"日期不能为空",oncorrect:"输入正确"}).inputValidator({min:1,onerror: "请输入日期"});
		if("${_ReportTypeCode}" == "2"){
			//alert(1);
			$("#attachment").formValidator({onshow:"请上传附件",onfocus:"附件不能为空",oncorrect:"输入正确"}).inputValidator({min:1,onerror: "请上传附件"});
		}
	});
	
	//保存审核
	/** function save(){
		var form = document.instanceCheckInforForm;
		form.action = "<c:url value='/workflow/checkInfor.do'/>?method=save";
		form.submit();
	} */
	
	
	//去掉初始化的提示信息
	$("#contentTip").html("");
	$("#operateDateTip").html("");




	
</script>
<base target="_self"/>
</head>
<body >
	<form:form commandName="superviseReportVo" name="superviseReportForm" id="superviseReportForm" action="/supervise/superviseReport.do?method=save" method="post" enctype="multipart/form-data">
		<form:hidden path="reportType"/>
		<form:hidden path="reportId"/>
		<form:hidden path="parentId"/>
		<form:hidden path="isPassed"/>
		<form:hidden path="isJudgePassed"/>
		<form:hidden path="managerAdvice"/>
		<input type="hidden" name="tag" value="${_Tag}"/>
		<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 85%;margin:0 auto;">
			<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%;">
  				<div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
    				<span class="ui-jqgrid-title">工作跟踪</span>
  				</div>

				<div class="ui-jqgrid-bdiv" style="height: auto; width: 100%">
					<div style="position: relative;">
						<div>
							<table cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable" style="width: 100%">
								<%-- 工作跟踪实例信息 --%>			
								<%@include file="includeTask.jsp" %>
					
								<tbody id="setOperatorBody">
									<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 20px;">
										<td class="ui-state-default jqgrid-rownum" colspan=4>
											<b><font color=red>编辑${_ReportType}</font></b>
										</td>
									</tr>
									<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
										<td class="ui-state-default jqgrid-rownum" >日期：</td>
										<td colspan=3>
											<c:choose>
												<c:when test="${_ReportTypeCode == 2}">
													<input name="operateDate" id="operateDate" size="20" value="${_OperateDate}" readonly="readonly" />
												</c:when>
												<c:otherwise>
													<input name="operateDate" id="operateDate" size="20" value="${_OperateDate}" readonly="readonly" />
													<!--  onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'%y-%M-%d'})" -->
												</c:otherwise>
											</c:choose>
											
											<div id="operateDateTip"></div>
										</td>
									</tr>
									<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
										<td class="ui-state-default jqgrid-rownum" >报告内容：</td>
										<td valign="top" colspan=3>
											<form:textarea path="content" cols="80" rows="5"/><div id="contentTip"></div>
										</td>
									</tr>
									<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
										<td class="ui-state-default jqgrid-rownum" >备注：</td>
										<td valign="top" colspan=3>
											<form:textarea path="memo" cols="80" rows="5"/>
										</td>
									</tr>
									
									<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
										<td class="ui-state-default jqgrid-rownum">附件：</td>
										<td align="left" id="newstyle" colspan=3>
											<input type="file" id="attachment" name="attachment" size="50" />&nbsp;<a href="javascript:;" onclick="addtable('newstyle')" >更多附件..</a>
											
											<div id="attachmentTip"></div>
										</td>								
									</tr>
											
									<c:if test="${!empty _ReportAttachment_Names}">
										<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
											<td colspan="2" class="ui-state-default jqgrid-rownum">原附件信息(<font color="white">如果要删除某个附件，请选择该附件前面的选择框</font>)：</td>
										</tr>
										<tr>
											<td colspan="2"><c:forEach var="file" items="${_ReportAttachments}" varStatus="index">
													<input type="checkbox" name="attatchmentArray" value="${index.index}" />
													<a href="<%=request.getRealPath("/")%>${file}">${_ReportAttachment_Names[index.index]}</a><br/>
											</c:forEach></td>
										</tr>
									</c:if>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<a name="zhidao"></a><!--锚点处-->
				<div style="width: 100%" class="ui-state-default ui-jqgrid-pager ui-corner-bottom" dir="ltr">
					<div role="group" class="ui-pager-control">
						<table cellspacing="0" cellpadding="0" border="0" style="width: 100%; table-layout: fixed;" class="ui-pg-table">
							<tbody>
								<tr>
									<td align="left">
										<table cellspacing="0" cellpadding="0" border="0" style="float: left; table-layout: auto;" class="ui-pg-table navtable">
											<tbody>
												<tr>
													<td><input style="cursor: pointer;" type="submit" value="保存"/></td>
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
