[#ftl][#-- 注:此标签用来指定ftl标签使用方括号,不能删 --]
<html>
<head>
<meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
[#include "customizeTag.ftl"/]
</head>
<body>
[#-- 注:使用自定义标签时,controlName要放在最后 --]
				<table cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable" style="width: 100%">
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum" style="width: 15%">标题：</td>
						<td>[@input controlName="title"/]</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum">内容：</td>
						<td>[@textarea controlName="content"/]</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum">责任部室：</td>
						<td>[@depSelect selectId="departmentId" controlName="departmentId"/]</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum">负责人：</td>
						<td>[@usrSelect selectId="personId" controlName="personId"/]</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum">是否重要：</td>
						<td>[@select selectId="important" optionValueArray=["否", "是"] controlName="important"/]</td>
					</tr>
				</table>
</body>
</html>
