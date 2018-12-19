<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
	<head>
		<title>查看详细信息</title>
		<link type="text/css" href="<c:url value="/"/>components/jqgrid/css/jquery.ui.all.css" rel="stylesheet" />
		<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />
		<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
		
	</head>
	<body>
		<form:form commandName="companyAddressVo" id="companyAddressForm" name="companyAddressForm" action="/personal/companyAddressInfor.do?method=save" method="post">
			<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 100%">
				<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
				  <div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
				    <span class="ui-jqgrid-title">公司通讯录详细信息</span>
				  </div>
				
					<div class="ui-jqgrid-bdiv" style="height: auto; width: 100%">
						<div style="position: relative;">
							<div>
								<table cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable" style="width: 100%">
									<tbody>
										
										<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
											<td class="ui-state-default jqgrid-rownum">姓名：</td>
											<td><c:choose><c:when test="${empty _message}">&nbsp;</c:when><c:otherwise>${_message.personName}</c:otherwise></c:choose></td>
										</tr>
										
										<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
											<td class="ui-state-default jqgrid-rownum">职位：</td>
											<td><c:choose><c:when test="${empty _message}">&nbsp;</c:when><c:otherwise>${_message.position}</c:otherwise></c:choose></td>
										</tr>
										<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
											<td style="width: 15%;" class="ui-state-default jqgrid-rownum">部门：</td>
											<td><c:choose><c:when test="${empty _message}">&nbsp;</c:when><c:otherwise>${_message.department}</c:otherwise></c:choose></td>
										</tr>
										
										<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
											<td class="ui-state-default jqgrid-rownum">手机：</td>
											<td><c:choose><c:when test="${empty _message}">&nbsp;</c:when><c:otherwise>${_message.mobile}</c:otherwise></c:choose></td>
										</tr>
										
										<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
											<td class="ui-state-default jqgrid-rownum">邮箱：</td>
											<td><c:choose><c:when test="${empty _message}">&nbsp;</c:when><c:otherwise>${_message.email}</c:otherwise></c:choose></td>
										</tr>
										
										<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
											<td class="ui-state-default jqgrid-rownum">性别：</td>
											<td><c:choose><c:when test="${empty _message}">&nbsp;</c:when><c:otherwise><c:if test="${_message.gender==0}">男</c:if><c:if test="${_message.gender==1}">女</c:if></c:otherwise></c:choose></td>
										</tr>
										
										<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
											<td class="ui-state-default jqgrid-rownum">出生日期：</td>
											<td><c:choose><c:when test="${empty _message}">&nbsp;</c:when><c:otherwise>${_message.birthday}</c:otherwise></c:choose></td>
										</tr>
										
										<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
											<td class="ui-state-default jqgrid-rownum">备注说明：</td>
											<td><c:choose><c:when test="${empty _message}">&nbsp;</c:when><c:otherwise>${_message.memo}</c:otherwise></c:choose></td>
										</tr>
										
										<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
											<td class="ui-state-default jqgrid-rownum">办公室地址：</td>
											<td><c:choose><c:when test="${empty _message}">&nbsp;</c:when><c:otherwise>${_message.officeAddress}</c:otherwise></c:choose></td>
										</tr>
										
										<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
											<td class="ui-state-default jqgrid-rownum">办公室电话：</td>
											<td><c:choose><c:when test="${empty _message}">&nbsp;</c:when><c:otherwise>${_message.officePhone}</c:otherwise></c:choose></td>
										</tr>
										
										<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
											<td class="ui-state-default jqgrid-rownum">办公室邮编：</td>
											<td><c:choose><c:when test="${empty _message}"></c:when><c:otherwise>${_message.officeCode}</c:otherwise></c:choose></td>
										</tr>
										
										<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
											<td class="ui-state-default jqgrid-rownum">家庭地址：</td>
											<td><c:choose><c:when test="${empty _message}">&nbsp;</c:when><c:otherwise>${_message.homeAddress}</c:otherwise></c:choose></td>
										</tr>
										
										<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
											<td class="ui-state-default jqgrid-rownum">家庭电话：</td>
											<td><c:choose><c:when test="${empty _message}"></c:when><c:otherwise>${_message.homePhone}</c:otherwise></c:choose></td>
										</tr>
										
										<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
											<td class="ui-state-default jqgrid-rownum">家庭邮编：</td>
											<td><c:choose><c:when test="${empty _message}"></c:when><c:otherwise>${_message.postCode}</c:otherwise></c:choose></td>
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
													<tr>
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
