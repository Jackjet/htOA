<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
<title>活动信息查看</title>
<link type="text/css" href="<c:url value="/"/>components/jqgrid/css/jquery.ui.all.css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
<style type="text/css">
	a:link {color: #FF0000; text-decoration: none} /* 未访问的链接 */
	a:visited {color: #00FF00; text-decoration: none} /* 已访问的链接 */
	a:hover {color: #FF00FF; text-decoration: underline} /* 鼠标在链接上 */
	a:active {color: #0000FF; text-decoration: underline} /* 激活链接 */
</style>
<script>

	//修改
	function changeInfor(activityId){
		var path;
		path = "<c:url value='/customer/activityInfor.do'/>?method=edit&activityId="+activityId;
		window.name = "__self";
		window.open(path, "__self"); //注意是2个下划线 
	}
</script>
</head>
<body>	 
<form>
<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 100%">
<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
  <div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
    <span class="ui-jqgrid-title">活动信息查看</span>
  </div>

	<div class="ui-jqgrid-bdiv" style="height: auto; width: 100%">
		<div style="position: relative;">
			<div>
				<table cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable " style="width: 100%" >
					<tbody>
						<tr class="ui-widget-content jqgrow ui-row-ltr " style="height: 50px;"  >
							<td style="width: 15%;word-wrap: break-word;word-break:break-all;" class="ui-state-default jqgrid-rownum">活动内容：</td>
							<td style="width: 85%;">${_activityInfor.content}</td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td style="width: 15%;" class="ui-state-default jqgrid-rownum">活动地点：</td>
							<td valign="top">${_activityInfor.activityPlace}</td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td style="width: 15%;" class="ui-state-default jqgrid-rownum">计划时间：</td>
							<td>${_activityInfor.planDate}</td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td style="width: 15%;" class="ui-state-default jqgrid-rownum">实际时间：</td>
							<td>${_activityInfor.activityDate}</td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td style="width: 15%;" class="ui-state-default jqgrid-rownum">后续反馈：</td>
							<td>${_activityInfor.feedback}</td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 70px;">
							<td style="width: 15%;" class="ui-state-default jqgrid-rownum">备注：</td>
							<td>${_activityInfor.memo}</td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td  style="width: 15%;" class="ui-state-default jqgrid-rownum" >附件信息：</td>
							<td align="left"> <c:forEach  var="file"  items="${_Attachment_Names}"  varStatus="status"><a href="<c:url value="${'/common/'}"/>download.jsp?filepath=${_Attachments[status.index]}"><span color="red">${file}</span></a><br/> </c:forEach></td>
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
										<td><input style="cursor: pointer;" type="button" value="修改" onclick="changeInfor('${_activityInfor.activityId}');"/></td>
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
</form>
</body>
</html>
