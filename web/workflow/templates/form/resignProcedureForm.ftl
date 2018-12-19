[#ftl][#-- 注:此标签用来指定ftl标签使用方括号,不能删 --]
[#include "customizeTag.ftl"/]
[#-- 注:使用自定义标签时,controlName要放在最后 --]

					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum" style="width: 15%">姓名：</td>
						<td>[@input size="25" controlName="personName"/]</td>
						<td class="ui-state-default jqgrid-rownum" style="width: 15%">离职日期：</td>
						<td>[@dateInput size="15" controlName="resignDate"/]</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum">部门及岗位：</td>
						<td>[@input size="15" controlName="depAndStr"/]</td>
						<td class="ui-state-default jqgrid-rownum">入职日期：</td> 
						<td>[@dateInput size="15" controlName="entryDate"/]</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum">工号：</td>
						<td>[@input size="15" controlName="personNo"/]</td>
						<td class="ui-state-default jqgrid-rownum">合同到期日：</td> 
						<td>[@dateInput size="15" controlName="conExpiresDate"/]</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum">离职理由：</td>
						<td colspan="3">[@textarea controlName="resignReason"/]</td>
					</tr>
