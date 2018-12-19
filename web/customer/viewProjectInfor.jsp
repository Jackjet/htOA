<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
<title>项目信息查看</title>
<link type="text/css" href="<c:url value="/"/>components/jqgrid/css/jquery.ui.all.css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
<script>

	//修改
	function changeInfor(projectId){
		var path;
		path = "<c:url value='/customer/projectInfor.do'/>?method=edit&projectId="+projectId;
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
    <span class="ui-jqgrid-title">项目信息查看</span>
  </div>

	<div class="ui-jqgrid-bdiv" style="height: auto; width: 100%">
		<div style="position: relative;">
			<div>
				<table cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable " style="width: 100%" >
					<tbody>
						<tr class="ui-widget-content jqgrow ui-row-ltr " style="height: 30px;"  >
							<td style="width: 15%;word-wrap: break-word;word-break:break-all;" class="ui-state-default jqgrid-rownum">项目名称：</td>
							<td style="width: 85%;" colspan="3">${_ProjectInfor.projectName}</td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td style="width: 15%;" class="ui-state-default jqgrid-rownum">项目经理：</td>
							<td valign="top">${_ProjectInfor.manager.person.personName}</td>
							<td style="width: 15%;" class="ui-state-default jqgrid-rownum">创建时间：</td>
							<td>${_ProjectInfor.updateDate}</td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 70px;">
							<td style="width: 15%;" class="ui-state-default jqgrid-rownum">项目描述：</td>
							<td colspan="3">${_ProjectInfor.description}</td>
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
										<td><input style="cursor: pointer;" type="button" value="修改" onclick="changeInfor('${_ProjectInfor.projectId}');"/></td>
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
