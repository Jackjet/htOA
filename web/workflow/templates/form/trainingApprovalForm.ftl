[#ftl][#-- 注:此标签用来指定ftl标签使用方括号,不能删 --]
[#include "customizeTag.ftl"/]
[#-- 注:使用自定义标签时,controlName要放在最后 --]

					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum" style="width: 22%">培训报批档案号：</td>
						<td>[@input size="25" controlName="fileNo"/]</td>
						<td class="ui-state-default jqgrid-rownum" style="width: 22%">申请培训对象：</td>
						<td>[@input size="25" controlName="trainingObj"/]</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum">申请培训内容：</td>
						<td colspan="3">[@textarea controlName="trainingContent"/]</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum">培训起讫时间：</td>
						<td>[@input size="25" controlName="trainingTime"/]</td>
						<td class="ui-state-default jqgrid-rownum">每周占用工作时间：</td>
						<td>[@input size="25" controlName="occupiedHours"/]</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum">申请项目：</td>
						<td colspan="3">[@select selectId="project" optionValueArray=["费用报销","占用工作时间","其他"] controlName="project"/][@input size="30" controlName="detail"/]</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum" style="width: 15%">申请费用报销金额：</td>
						<td>[@input size="25" controlName="applicationAmount"/]</td>
						<td class="ui-state-default jqgrid-rownum">培训金额：</td>
						<td>[@input size="25" controlName="trainingAmount"/]</td>
					</tr>
					<tr style="display: none;">
						<td colspan="4">[@hidden controlName="reportYear"/]<script>
									if ('${_InstanceId}'==null||'${_InstanceId}'=='') {
										var fileNos = document.getElementsByName("fileNo");
										var sysDate = new Date();
										var sysYear = sysDate.getFullYear();
										//获取最大流水号
										var serialNo = "";
										$.ajax({
											url: "/workflow/submit.do?method=getSerialNo&tableName=Customize_Peixunbaopichulidan&fieldName=fileNo&fieldYear=reportYear&reportYear="+sysYear,
											type: "post",
											dataType: "json",
											async: false,	//设置为同步
											beforeSend: function (xhr) {
											},
											complete : function (req, err) {
												var returnValues = eval("("+req.responseText+")");
												serialNo = returnValues["serialNo"];
											}
										});
										fileNos[0].value = sysYear+serialNo;
										var reportYears = document.getElementsByName("reportYear");
										reportYears[0].value = sysYear;
									}
						</script></td>
					</tr>
