[#ftl][#-- 注:此标签用来指定ftl标签使用方括号,不能删 --]
[#include "customizeTag.ftl"/]
[#-- 注:使用自定义标签时,controlName要放在最后 --]

					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum" style="width: 15%">工号：</td>
						<td>[@input size="15" controlName="personNo"/]</td>
						<td class="ui-state-default jqgrid-rownum" style="width: 15%">姓名：</td>
						<td>[@input size="25" controlName="personName"/]</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum">性别：</td>
						<td>[@select selectId="gender" optionValueArray=["男","女"] controlName="gender"/]</td>
						<td class="ui-state-default jqgrid-rownum">年龄：</td>
						<td>[@input size="15" controlName="age"/]</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum">最高学历：</td>
						<td>[@input size="25" controlName="highestEdu"/]</td>
						<td class="ui-state-default jqgrid-rownum">政治面貌：</td> 
						<td>[@input size="25" controlName="political"/]</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum">参加工作时间：</td>
						<td>[@input size="15" controlName="workTime"/]</td>
						<td class="ui-state-default jqgrid-rownum">本企业时间：</td> 
						<td>[@input size="15" controlName="companyTime"/]</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum">原部门/岗位：</td>
						<td colspan="3">[@input size="15" controlName="depAndStr"/]</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum">合同终止日期：</td>
						<td>[@dateInput size="15" controlName="endContractDate"/]</td>
						<td class="ui-state-default jqgrid-rownum">离职日期：</td> 
						<td>[@dateInput size="15" controlName="resignDate"/]</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum">离职原因：</td>
						<td colspan="3">[@textarea controlName="resignReason"/]</td>
					</tr>
