<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
<title>部门审核</title>
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
        $("html,body").animate({scrollTop: $("#bottomFlag").offset().top}, 500);
	});
	
</script>
<base target="_self"/>
<body style="overflow-y: auto;padding: 0 100px;bottom: 0">
<form id="instanceInforForm" name="instanceInforForm" action="/contract.do?method=check" method="post" enctype="multipart/form-data">
	<input type="hidden" name="sanfangID" value="${sanfangInfor.sanfangID}">
	<input type="hidden" name="token" value="${Session.SESSION_ORDER_TOKEN}" >
	<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 98%">
		<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
			<div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix" style="color: #22FBFF">
				<span class="ui-jqgrid-title">查看审批信息 &nbsp;【合同变更 主办人:${contractInfo.executor.person.personName}  发起时间:${contractInfo.contractTime}】</span>
			</div>
			<div style="width: 90%"><hr style="border:0.5px solid #22FBFF;" /></div>
			<input type="hidden" name="contractID" value="${contractInfo.contractID}"/>
			<%-- 审核实例信息 --%>
			<%@include file="includeInstance.jsp" %>
			<table cellspacing="0" cellpadding="0" border="0" style="width: 90%">
				<tbody>
					<tr  class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum" style="width: 15%">审批意见：</td>
						<td>
							<select name="checkResult">
								<option value="1">同意</option>
								<option value="0">不同意</option>
							</select>
							<%
								Date date = new Date();
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								String format = sdf.format(date);
							%>
							日期：<input id="checkDate" name="checkDate" value="<%=format%>"/><br/>
							<textarea name="checkOpinion" id="checkOpinion" cols="70" rows="5"></textarea>
						</td>
					</tr>
					<tr  class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum" style="width: 15%">附件：</td>
						<td>
							<input type="file" name="checkAttach"/>
						</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr">
						<td class="ui-state-default jqgrid-rownum" colspan="3"><input style="cursor: pointer;" type="submit" value="保存" />&nbsp;<input style="cursor: pointer;" type="button" value="关闭" onclick="window.close();"/></td>
					</tr>
				</tbody>
			</table>
			<div id="bottomFlag"></div>
		</div>
	</div>
</form>
</body>
</html>
                  
