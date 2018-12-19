<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
<title>查看发送讯息</title>

	<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
<link type="text/css" href="<c:url value="/"/>components/jqgrid/css/jquery.ui.all.css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />

<script src="<c:url value="/"/>js/commonFunction.js" type="text/javascript"></script>

<style type="text/css">
	a:link {color: #FF0000; text-decoration: none} /* 未访问的链接 */
	a:visited {color: #00FF00; text-decoration: none} /* 已访问的链接 */
	a:hover {color: #FF00FF; text-decoration: underline} /* 鼠标在链接上 */
	a:active {color: #0000FF; text-decoration: underline} /* 激活链接 */
</style>
<script>
	//回复
    function replyMessage(messageId){
		var path;
		path = "<c:url value='/personal/receiveMessage.do'/>?method=replyMessage&messageId="+messageId;
		window.name = "__self";
		window.open(path, "__self"); //注意是2个下划线 
	}
	//转发为1
	function changeSend(messageId){
		var path;
		path = "<c:url value='/personal/messageInfor.do'/>?method=sendMessage&messageId=" + messageId+"&handle=transmit";
		window.name = "__self";
		window.open(path, "__self"); //注意是2个下划线 		
	}
	
	//修改为2
	function editSend(messageId){
		var path;
		path = "<c:url value='/personal/messageInfor.do'/>?method=sendMessage&messageId=" + messageId;
		window.name = "__self";
		window.open(path, "__self"); //注意是2个下划线 		
	}
	
</script>
</head>

<body style="border:1px solid #0DE8F5;border-radius: 5px;padding: 0 100px" >
<br/>
<form >

<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 98%">
  <div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
    <span class="ui-jqgrid-title">查看发送讯息</span>
  </div>

	<div class="ui-jqgrid-bdiv" style="height: auto; width: 100%">
		<div style="position: relative;">
			<div>
				<table cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable" style="width: 100%">
					<tbody>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td style="width: 15%;" class="ui-state-default jqgrid-rownum">标题：</td>
							<td style="width: 85%;">${_message.messageTitle}</td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td style="width: 15%;" class="ui-state-default jqgrid-rownum">收件人：</td>	
							<td style="width: 85%;"><c:choose><c:when test="${_message.receiveType==0}">公司全体</c:when>
							<c:when test="${_message.receiveType==2}">${_RoleName}</c:when>
							<c:otherwise><c:forEach items="${_receiverNames}" var="receive" varStatus="status">${receive.receiver.person.personName}&nbsp;&nbsp;</c:forEach></c:otherwise></c:choose></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">发送时间：</td>
							<td><fmt:formatDate value="${_message.sendTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 80px;">
							<td class="ui-state-default jqgrid-rownum" >讯息内容：</td>
							<td valign="top" >${_message.messageContent}</td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum" valign="top">附件信息:</td>
							<td align="left"><c:forEach  var="file"  items="${_Attachment_Names}"  varStatus="status"><a href="<c:url value="${'/common/'}"/>download.jsp?filepath=${_Attachments[status.index]}"><span color="red">${file}</span></a><br/> </c:forEach></td>
						</tr>
						
						<c:if test="${!empty _replyList}">
							<tr>
								<td colspan="2" >
									<table width="98%"  border="0" cellpadding="0" cellspacing="0" >
										 <tr><td>&nbsp;</td></tr>
										 <tr><td align="left" style="font-family:Verdana;font-size:20px;font-weight:bold"> 如下为回复内容:</td></tr>	
										 <tr><td style="background-color: #B2DFEE">&nbsp;</td></tr>					
									</table>		
								</td>
							</tr>
							
							<tr>
								<td colspan="2" >
									<table cellspacing="5" cellpadding="2" >														
									<c:forEach items="${_replyList}" var="receive" varStatus="status">													
											<tr>
												<td style="font-weight:bold">${receive.restorer.person.personName }:</td>
												<td><fmt:formatDate value="${receive.replyTime}" pattern="yyyy-MM-dd HH:mm:ss"/><br/>${receive.replyContent}</td>
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
									<tr><td><input style="cursor: pointer;" type="button" value="回复" onclick="replyMessage('${_message.messageId}');"/></td>
										<td><input style="cursor: pointer;" type="button" value="转发" onclick="changeSend('${_message.messageId}');"/></td>
										<td><input style="cursor: pointer;" type="button" value="修改" onclick="editSend('${_message.messageId}');"/></td>
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
</form>
</body>
</html>
