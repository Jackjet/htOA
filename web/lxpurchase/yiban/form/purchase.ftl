[#ftl][#-- 注:此标签用来指定ftl标签使用方括号,不能删 --]
[#include "customizeTag.ftl"/]
[#-- 注:使用自定义标签时,controlName要放在最后 --]

					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum" style="width: 15%">发文日期：</td>
						<td>[@dateInput size="15" controlName="sendDate"/]</td>
						<td class="ui-state-default jqgrid-rownum">发文号：</td>
						<td>[@input size="25" controlName="documentNo"/]</td>
					</tr>
					<tr style="display: none;">
						<td>[@hidden controlName="reportYear"/]</td>
						<td colspan="2">[@hidden controlName="serialNo"/]</td>
						<td><script>
								function createDocumentNo() {
									var organizeId = $("#organizeId option:selected").val();
									var documentNos = document.getElementsByName("documentNo");
									var sysDate = new Date();
									var sysYear = sysDate.getFullYear();
									if (organizeId != '' && organizeId.length != null) {
										//获取最大流水号
										var serialNo = "";
										$.ajax({
											url: "/workflow/submit.do?method=getSerialNo&tableName=Customize_Dangqunfawen&fieldName=serialNo&fieldYear=reportYear&reportYear="+sysYear,
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
										documentNos[0].value = "沪港物流委发["+sysYear+"]第"+serialNo+"号";
										var reportYears = document.getElementsByName("reportYear");
										var serialNos = document.getElementsByName("serialNo");
										reportYears[0].value = sysYear;
										serialNos[0].value = serialNo;
									}else {
										documentNos[0].value = "";
									}
								}
								if ('${_InstanceId}'==null||'${_InstanceId}'=='') {
									$("#organizeId").change(function(){createDocumentNo();});
								}
						</script></td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum">主办科室：</td>
						<td>[@depSelect selectId="organizeId" organizeName="organizeName" controlName="organizeId"/]</td>
						<td class="ui-state-default jqgrid-rownum">拟稿人：</td>
						<td>[@usrSelect selectId="writerId" personName="writerName" controlName="writerId"/]</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum">催办等级：</td>
						<td>[@select selectId="urgencyName" optionValueArray=["一般","普通","紧急"] controlName="urgencyName"/]</td>
						<td class="ui-state-default jqgrid-rownum">校对：</td>
						<td>[@usrSelect selectId="reviewerId" personName="reviewerName" controlName="reviewerId"/]</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum">主题词：</td>
						<td colspan="3">[@input size="30" controlName="keywords"/]</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum">主送：</td>
						<td colspan="3">[@input size="40" controlName="sendPersons"/]</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum">抄送：</td>
						<td colspan="3">[@input size="40" controlName="copyPersons"/]</td>
					</tr>
					<tr style="display: none;">
						<td>[@searchControl searchType="input" searchField="documentNo" searchOper="cn" searchName="发文号"/]</td>
						<td>[@searchControl searchType="orgSelect" searchField="organizeId" searchOper="eq" searchName="主办科室"/]</td>
						<td>[@searchControl searchType="usrSelect" searchField="writerId" searchOper="eq" searchName="拟稿人"/]</td>
						<td>[@searchControl searchType="input" searchField="keywords" searchOper="cn" searchName="主题词"/]</td>
					</tr>
					
