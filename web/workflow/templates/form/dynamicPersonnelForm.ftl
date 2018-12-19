[#ftl][#-- 注:此标签用来指定ftl标签使用方括号,不能删 --]
[#include "customizeTag.ftl"/]
[#-- 注:使用自定义标签时,controlName要放在最后 --]

					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum" style="width: 15%">姓名：</td>
						<td>[@input size="25" controlName="personName"/]</td>
						<td class="ui-state-default jqgrid-rownum" style="width: 15%">工号：</td>
						<td>[@input size="15" controlName="personNo"/]</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum">发生日期：</td>
						<td>[@dateInput size="15" controlName="occurDate"/]</td>
						<td class="ui-state-default jqgrid-rownum">变更类型：</td>
						<td>[@select selectId="changeType" optionValueArray=["公司调动","部门内调动","部门间调动","职务变动","职级变动","临时借调","新进","其他"] controlName="changeType"/]</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr">
						<td class="ui-state-default jqgrid-rownum" colspan="4">变更前情况：</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum">投资公司/部门：</td>
						<td colspan="3">[@input size="40" controlName="department"/]</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum">职位：</td>
						<td>[@input size="25" controlName="position"/]</td>
						<td class="ui-state-default jqgrid-rownum">岗级：</td>
						<td>[@input size="25" controlName="structureLayer"/]</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum">其他：</td>
						<td colspan="3">[@textarea controlName="other"/]</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr">
						<td class="ui-state-default jqgrid-rownum" colspan="4">变更后情况：</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum">投资公司/部门：</td>
						<td colspan="3">[@input size="40" controlName="afterDep"/]</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum">职位：</td>
						<td>[@input size="25" controlName="afterPosition"/]</td>
						<td class="ui-state-default jqgrid-rownum">岗级：</td>
						<td>[@input size="25" controlName="afterStructureLayer"/]</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum">其他：</td>
						<td colspan="3">[@textarea controlName="afterOther"/]</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum">变更理由：</td>
						<td colspan="3">[@textarea controlName="reason"/]</td>
					</tr>
