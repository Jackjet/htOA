<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
<title>结束审核实例</title>
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
		$.formValidator.initConfig({formid:"purchaseInforForm",onerror:function(msg){alert(msg)},onsuccess:function(){}});
		$("#endTime").formValidator({onshow:"请输入结束时间",onfocus:"结束时间不能为空",oncorrect:"输入正确"}).inputValidator({min:1,onerror: "请输入结束时间"});
	});
	
</script>
<base target="_self"/>
<body style="overflow-y: auto;padding: 0 100px" onload="">
<br/>
<form:form commandName="purchaseInforVo" id="purchaseInforForm" name="purchaseInforForm" action="/gcpurchase/purchaseInfor.do?method=saveEnd" method="post" enctype="multipart/form-data">
<form:hidden path="purchaseId"/>
<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 98%">
	<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
	<div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
		<span class="ui-jqgrid-title">结束审核实例 &nbsp;【${_Flow.flowName} 主办人：${_Purchase.applier.person.personName}】</span>
	</div>

	<%-- 审核实例信息 --%>	
	<%@include file="includePurchase.jsp" %>

				<table cellspacing="0" cellpadding="0" border="0" style="width: 90%">
					<tbody>
						<tr class="ui-widget-content jqgrow ui-row-ltr">
							<td class="ui-state-default jqgrid-rownum" colspan="3"><b>删除操作如下</b>：</td>
						</tr>
						

						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum" style="width: 15.5%">删除时间：</td>
							<td><input name="endTime" id="endTime" size="20" readonly="readonly" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" value="${_SystemTime}"/></td>
							<td><div id="endTimeTip"></div></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum "style="width: 15%">附件：</td>
							<td align="left" colspan="2" id="newstyle"><input type="file" name="attachment" size="50" />&nbsp;<a href="javascript:;" onclick="addtable('newstyle')">更多附件..</a></td>							
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
                  
