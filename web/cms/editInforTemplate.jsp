<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
<title>编辑模板信息</title>
<script>
	$(document).ready(function(){
		//格式化tr,td
		$("#addTable tr").addClass("ui-widget-content jqgrow ui-row-ltr").height(30).find("td:nth-child(1)").addClass("ui-state-default jqgrid-rownum");
	});
	
	//提交数据
	function submitData() {
		var form = document.getElementById('inforTemplateForm');
		form.action = '<c:url value="/cms/inforTemplate.do"/>?method=save&templateStyle=${param.templateStyle}';
		form.submit();
		alert("模板已保存！");
	}
	
	//FCKeditor默认显示源代码
	function FCKeditor_OnComplete(editorInstance) {
		editorInstance.SwitchEditMode(); 
	}
</script>
</head>

<body>
<form id="inforTemplateForm" action="/cms/inforTemplate.do?method=save" method="post">
<input type="hidden" name="path" id="path" value="${param.path}"/>
<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 100%">
<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
	<div class="ui-jqgrid-bdiv" style="height: auto; width: 100%">
		<div style="position: relative;">
			<div>
				<table cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable" style="width: 100%" id="addTable">
					<tbody>
					
						<tr>
							<td><FCK:editor id="fileContent" basePath="/fckeditor/" width="100%" height="450" skinPath="/fckeditor/editor/skins/default/" toolbarSet="Default">
                       			${_FileContent}
							</FCK:editor></td>
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
										<td><input style="cursor: pointer;" type="button" value="提交" onclick="submitData();"/></td>
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
                  
