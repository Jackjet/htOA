[#ftl][#-- 注:此标签用来指定ftl标签使用方括号,不能删 --]
[#include "customizeTag.ftl"/]
[#-- 注:使用自定义标签时,controlName要放在最后 --]
				
			
				<tr style="height: 30px" class="ui-widget-content jqgrow ui-row-ltr">
                    <td class="ui-state-default jqgrid-rownum" style="width: 15%">收文日期：</td>
                    <td>[@dateInput size="15" controlName="receiveDate"/]</td>
                    <td class="ui-state-default jqgrid-rownum" style="width: 15%">收文号：</td>
                    <td>[@input size="30" controlName="documentNo"/]</td>
                </tr>
                <tr style="display: none;">
					<td>[@hidden controlName="reportYear"/]</td>
					<td colspan="2">[@hidden controlName="serialNo"/]</td>
					<td><script>

						if ('${_InstanceId}'==null||'${_InstanceId}'=='') {
							var documentNos = document.getElementsByName("documentNo");
							var sysDate = new Date();
							var sysYear = sysDate.getFullYear();
							//获取最大流水号
							var serialNo = "";
							$.ajax({
								url: "/workflow/submit.do?method=getSerialNo&tableName=Customize_shouwenguanli&fieldName=serialNo&fieldYear=reportYear&reportYear="+sysYear,
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
							documentNos[0].value = "HT-SW-"+sysYear+"-"+serialNo;
							var reportYears = document.getElementsByName("reportYear");
							var serialNos = document.getElementsByName("serialNo");
							reportYears[0].value = sysYear;
							serialNos[0].value = serialNo;
							
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
					<td class="ui-state-default jqgrid-rownum">文件字号：</td>
                    <td>[@input size="30" controlName="reportNo"/]</td>
				</tr>
				<tr style="height: 30px" class="ui-widget-content jqgrow ui-row-ltr">
                    <td class="ui-state-default jqgrid-rownum">文件日期：</td>
                    <td>[@dateInput size="15" controlName="fileDate"/]</td>
                    <td class="ui-state-default jqgrid-rownum">份数：</td>
                    <td>[@input size="10" controlName="fileNum"/]</td>
                </tr>
                <tr style="height: 30px" class="ui-widget-content jqgrow ui-row-ltr">
                    <td class="ui-state-default jqgrid-rownum">密级：</td>
                    <td>[@select selectId="secretName" optionValueArray=["绝密","机密","秘密","非密"] controlName="secretName"/]</td>
                    <td class="ui-state-default jqgrid-rownum">催办等级：</td>
                    <td>[@select selectId="urgencyName" optionValueArray=["一般","普通","紧急"] controlName="urgencyName"/]</td>
                </tr>
                
                <tr style="height: 30px" class="ui-widget-content jqgrow ui-row-ltr">
                    <td style="width: 15%" class="ui-state-default jqgrid-rownum">来文单位：</td>
                    <td colspan="3">[@input size="40" controlName="unitName"/][@select selectId="selUnitName" optionValueArray=["安全监督部","工程技术部","总裁事务部","审计事务部","董事会办公室","投资发展部","生产业务部","人事组织部","资产财务部","公司所属企业","集团所属企业","市委办局","行业协会","其他"] controlName="selUnitName"/]</td>
                </tr>
                <tr style="display: none;">
					<td colspan="3">[@searchControl searchType="input" searchField="documentNo" searchOper="cn" searchName="收文号"/]</td>
				</tr>
                
                