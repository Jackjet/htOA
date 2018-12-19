<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
<title>重新流转</title>
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

<script>
	
	$(document).ready(function(){
		//验证
		$.formValidator.initConfig({formid:"instanceInforForm",onerror:function(msg){alert(msg)},onsuccess:function(){}});
		$("#suspendedReason").formValidator({onshow:"请输入理由",onfocus:"理由不能为空",oncorrect:"输入正确"}).inputValidator({min:4,onerror: "请输入至少4个字的理由"});
	});
	
</script>
<base target="_self"/>
<body>

<form:form commandName="flowInstanceInforVo" id="instanceInforForm" name="instanceInforForm" action="/workflow/instanceInfor.do?method=newEdit" method="post" enctype="multipart/form-data">
<form:hidden path="instanceId"/>
<form:hidden path="oldInstanceId"/>
<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 98%">
	<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
	<div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
		<span class="ui-jqgrid-title">重新流转 &nbsp;【${_Flow.flowName} 主办人：${_Instance.charger.person.personName}】</span>
	</div>

	<%-- 审核实例信息 --%>	
	<%@include file="includeInstance.jsp" %>

				<table cellspacing="0" cellpadding="0" border="0" style="width: 90%">
					<tbody>
						<tr class="ui-widget-content jqgrow ui-row-ltr">
							<td class="ui-state-default jqgrid-rownum" colspan="3"><b>重新流转操作如下</b>：</td>
						</tr>
						
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum" style="width: 15.5%">重新流转理由：</td>
							<td>
								<textarea name="suspendedReason" id="suspendedReason"  rows="5" cols="80"></textarea>
							</td>
							<td><div id="endTimeTip"></div></td>
						</tr>
						
								
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
                  
