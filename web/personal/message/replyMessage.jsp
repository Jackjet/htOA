<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
<title>回复讯息</title>
<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
<script src="<c:url value="/"/>js/inc_javascript.js" type="text/javascript"></script>
<script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript" charset="UTF-8"></script>
<script src="<c:url value="/"/>components/formvalidator/js/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/css/form.css"/>" />
<link type="text/css" href="<c:url value="/"/>components/jqgrid/css/jquery.ui.all.css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
<link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css"></link>

<script src="<c:url value='/js'/>/addattachment.js"></script>
<style type="text/css">
	a:link {color: #FF0000; text-decoration: none} /* 未访问的链接 */
	a:visited {color: #00FF00; text-decoration: none} /* 已访问的链接 */
	a:hover {color: #FF00FF; text-decoration: underline} /* 鼠标在链接上 */
	a:active {color: #0000FF; text-decoration: underline} /* 激活链接 */
</style>
<script>
	//保存回复
    function saveReplyMessage(messageId){
		var path;
		path = "<c:url value='/personal/receiveMessage.do'/>?method=saveReplyMessage";
		window.name = "__self";
		window.open(path, "__self"); //注意是2个下划线 
		
	}
</script>
	<title>回复讯息</title>

	<script src="<c:url value='/js'/>/addattachment.js"></script>


	<!-- formValidator -->

	<link type="text/css" rel="stylesheet" href="/css/noTdBottomBorder.css	"></link>
	
	<style type="text/css">
		a:link {color: #FF0000; text-decoration: none} /* 未访问的链接 */
		a:visited {color: #00FF00; text-decoration: none} /* 已访问的链接 */
		a:hover {color: #FF00FF; text-decoration: underline} /* 鼠标在链接上 */
		a:active {color: #0000FF; text-decoration: underline} /* 激活链接 */
	</style>
</head>
<base target="_self"/>
<body style="padding: 0 100px" >
<br/>
<form:form commandName="messageInforVo" name="messageReplyForm" id="messageReplyForm" action="/personal/receiveMessage.do?method=saveReplyMessage" method="post">
<form:hidden path="messageId" />
<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 100%">
<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
  <div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
    <span class="ui-jqgrid-title">回复讯息</span>
  </div>

	<div class="ui-jqgrid-bdiv" style="height: auto; width: 100%">
		<div style="position: relative;">
			<div>
				<table cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable" style="width: 100%">
					<tbody>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 10px;">
							<td style="width: 10%;" class="ui-state-default jqgrid-rownum">标题：</td>
							<td style="width: 85%;">${_message.messageTitle}</td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 50px;">
							<td class="ui-state-default jqgrid-rownum" >信息内容：</td>
							<td valign="top">${_message.messageContent}</td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 10px;">
							<td class="ui-state-default jqgrid-rownum">是否重要：</td>
							<td><c:if test="${_message.isImportant==0}">否</c:if><c:if test="${_message.isImportant==1 }">是</c:if></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 10px;">
							<td class="ui-state-default jqgrid-rownum">发送者：</td>
							<td>${_message.sender.person.personName}</td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 10px;">
							<td class="ui-state-default jqgrid-rownum">发送时间：</td>
							<td>${_message.sendTime}</td>
						</tr>
						
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" >
							<td class="ui-state-default jqgrid-rownum" valign="top">附件信息:</td>
							<td align="left"><c:forEach  var="file"  items="${_Attachment_Names}"  varStatus="status"><a href="<c:url value="${'/common/'}"/>download.jsp?filepath=${_Attachments[status.index]}"><span color="red">${file}</span></a><br/> </c:forEach></td>
						</tr>
						
						
						<c:if test="${!empty _replyList}">
							<tr>
								<td colspan="2">
									<table width="100%"  border="0" cellpadding="0" cellspacing="0">
										 <tr><td>&nbsp;</td></tr>
										 <tr><td align="left" style="font-family:Verdana;font-size:20px;font-weight:bold"> 如下为回复内容:</td></tr>							
										 <tr><td><div style="width: 90%"><hr style="border:0.5px solid #22FBFF;" /></div></td></tr>
									</table>		
								</td>
							</tr>
							
							<tr>
								<td colspan="2">
									<table cellspacing="5" cellpadding="2" >														
									<c:forEach items="${_replyList}" var="receive" varStatus="status">													
											<tr>
												<td style="font-weight:bold">${receive.restorer.person.personName }:</td>
												<td>${receive.replyTime}<br/>${receive.replyContent}</td>
											</tr>
									</c:forEach>
									</table>
								</td>
							</tr>
						</c:if>
						<tr>
							<td>
								添加回复：
							</td>
							<td>
							<form:textarea  rows="3" cols ="80" path="replyContent"></form:textarea>
							</td>
						</tr>
						<tr><td colspan="2">&nbsp;</td></tr>
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
						<td align="left" style="height: 30px">
							<table cellspacing="0" cellpadding="0" border="0" style="float: left; table-layout: auto;" class="ui-pg-table navtable"><tbody> 
									<tr><input style="height:100%;cursor: pointer;" type="submit" value="发送" onclick="saveReplyMessage(${_message.messageId});"/>
										&nbsp;<input style="height:100%;cursor: pointer;" type="button" value="关闭" onclick="window.close();"/>
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
