[#ftl][#-- 注:此标签用来指定ftl标签使用方括号,不能删 --]
[#include "customizeTag.ftl"/]
[#-- 注:使用自定义标签时,controlName要放在最后 --]

				<tr style="height: 30px" class="ui-widget-content jqgrow ui-row-ltr">
                    <td class="ui-state-default jqgrid-rownum" style="width: 10%">收文日期：</td>
                    <td>[@dateInput size="15" controlName="receiveDate"/]</td>

                </tr>
				<tr style="height: 30px" class="ui-widget-content jqgrow ui-row-ltr">
					<td class="ui-state-default jqgrid-rownum" style="width: 10%">收文号：</td>
					<td>[@input size="30" controlName="documentNo"/]</td>
				</tr>

                <tr style="display: none;">
					<td>[@hidden controlName="reportYear"/]</td>
					<td>[@hidden controlName="serialNo"/]</td>

				</tr>
				<tr style="display: none;">
					<td colspan="2"><script>
						var sysDate = new Date();
						var sysYear = sysDate.getFullYear();
						function createDocumentNo() {
							var selUnitNameIndex = $("#selUnitName").get(0).selectedIndex;
							var documentNos = document.getElementsByName("documentNo");
							if (selUnitNameIndex > 0) {
								//获取最大流水号
								var serialNo = "";
								$.ajax({
									url: "/workflow/submit.do?method=getSerialNo&tableName=Customize_Dangqunshouwen&fieldName=serialNo&fieldYear=reportYear&reportYear="+sysYear,
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
								documentNos[0].value = "沪港物流党群收"+sysYear+"-"+selUnitNameIndex+"第"+serialNo+"号";
								var reportYears = document.getElementsByName("reportYear");
								var serialNos = document.getElementsByName("serialNo");
								reportYears[0].value = sysYear;
								serialNos[0].value = serialNo;
							}else {
								documentNos[0].value = "";
							}
						}
						$("#selUnitName").change(function(){createDocumentNo();});
						if ('${_InstanceId}'==null||'${_InstanceId}'=='') {
							//初始化收文日期
							var receiveDates = document.getElementsByName("receiveDate");
							var month = sysDate.getMonth()+1;
							var sysDateFormat = sysYear+"-"+month+"-"+sysDate.getDate();
							receiveDates[0].value = sysDateFormat;
						}
					</script></td>
				</tr>
                <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
					<td class="ui-state-default jqgrid-rownum">收件人：</td>
					<td>[@usrSelect selectId="receiverId" personName="receiverName" controlName="receiverId"/]</td>

				</tr>
				<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
					<td class="ui-state-default jqgrid-rownum">文件字号：</td>
					<td>[@input size="30" controlName="reportNo"/]</td>
				</tr>
				<tr style="height: 30px" class="ui-widget-content jqgrow ui-row-ltr">
                    <td class="ui-state-default jqgrid-rownum">文件日期：</td>
                    <td>[@dateInput size="15" controlName="fileDate"/]</td>

                </tr>
				<tr style="height: 30px" class="ui-widget-content jqgrow ui-row-ltr">
					<td class="ui-state-default jqgrid-rownum">份数：</td>
					<td>[@input size="10" controlName="fileNum"/]</td>
				</tr>
                <tr style="height: 30px" class="ui-widget-content jqgrow ui-row-ltr">
                    <td class="ui-state-default jqgrid-rownum">密级：</td>
                    <td>[@select selectId="secretName" optionValueArray=["绝密","机密","秘密","非密"] controlName="secretName"/]</td>

                </tr>
				<tr style="height: 30px" class="ui-widget-content jqgrow ui-row-ltr">
					<td class="ui-state-default jqgrid-rownum">催办等级：</td>
					<td>[@select selectId="urgencyName" optionValueArray=["一般","普通","紧急"] controlName="urgencyName"/]</td>
				</tr>
                
                <tr style="height: 30px" class="ui-widget-content jqgrow ui-row-ltr">
                    <td style="width: 15%" class="ui-state-default jqgrid-rownum">来文单位：</td>
                    <td>[@select selectId="selUnitName" optionValueArray=["中共中央办公厅","中共上海市委办公厅","中共上海国际港务(集团)股份有限公司委员会","中共上海国际港务(集团)股份有限公司纪律检查委员会","中共上海国际港务(集团)股份有限公司党委工作部","其他"] controlName="selUnitName"/]</td>
                </tr>
                <tr style="display: none;">
					<td colspan="2">[@searchControl searchType="input" searchField="documentNo" searchOper="cn" searchName="收文号"/]</td>
				</tr>
                