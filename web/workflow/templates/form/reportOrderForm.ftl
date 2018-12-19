[#ftl][#-- 注:此标签用来指定ftl标签使用方括号,不能删 --]
[#include "customizeTag.ftl"/]
[#-- 注:使用自定义标签时,controlName要放在最后 --]
 
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum" style="width: 15%">制度类别：</td>
						<td colspan="3">[@select selectId="leibie" optionValueArray=["规定","办法"] controlName="leibie"/]</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum" style="width: 15%">公司类别：</td>
						<td colspan="3">[@mSelect selectId="category" mOptionValueArray=["码头","物流","码头和物流"] controlName="category"/]</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum" style="width: 15%">评审字号：</td>
						<td colspan="3">[@input size="35" controlName="orderNo"/]</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum">评审日期：</td>
						<td colspan="3">[@dateInput size="15" controlName="orderDate"/]</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum">催办等级：</td>
						<td colspan="3">[@select selectId="urgencyName" optionValueArray=["一般","普通","紧急"] controlName="urgencyName"/]</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum">涉及部门：</td>
						<td colspan="3">[@input size="40" controlName="involvedDeps"/]</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum">经办人：</td>
						<td colspan="3">[@input size="15" controlName="attner"/]</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum">制度说明：</td>
						<td colspan="3">[@textarea controlName="memo"/]</td>
					</tr>
					<tr style="display: none;">
						<td>[@hidden controlName="reportYear"/]</td>
						<td colspan="2">[@hidden controlName="serialNo"/]</td>
						<td><script>
						$(document).ready(function(){
						//document.getElementsByName("orderNo")[0].readOnly = "true";
						});
						function createContractNo(category) {
									//var category = $("#category option:selected").val();
									var orderNos = document.getElementsByName("orderNo");
									var sysDate = new Date();
									var sysYear = sysDate.getFullYear();
									var sysMonth = sysDate.getMonth()+1;
									var sysDate = sysDate.getDate();
									if (category != '' && category.length != null) {
									//获取最大流水号
									var serialNo = "";
									$.ajax({
										url: "/workflow/submit.do?method=getSerialNo&tableName=Customize_ZhiDuPingShen&fieldName=serialNo&fieldYear=reportYear&reportYear="+sysYear+"&categoryName="+encodeURI(category),
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
									//获取部门名称
									var organizeName = "";
									$.ajax({
										url: "/core/organizeInfor.do?method=getOrganizeName&departmentId="+${_GLOBAL_PERSON.department.organizeId},
										type: "post",
										dataType: "json",
										async: false,	//设置为同步
										beforeSend: function (xhr) {
										},
										complete : function (req, err) {
											var returnValues = eval("("+req.responseText+")");
											organizeName = returnValues["_OrganizeName"];
										}
									});
									orderNos[0].value = category+"-"+sysYear+"-"+serialNo;
									var reportYears = document.getElementsByName("reportYear");
									var serialNos = document.getElementsByName("serialNo");
									reportYears[0].value = sysYear;
									serialNos[0].value = serialNo;
								}else {
										orderNos[0].value = "";
								}
							}
								//if ('${_InstanceId}'==null||'${_InstanceId}'=='') {
									$("#category").change(function(){createContractNo($("#category option:selected").val());});
								//}
								
								if ('${_InstanceId}'==null||'${_InstanceId}'=='') {
									createContractNo($("#category option:first").val());
								}
						</script></td>
					</tr>
					<tr style="display: none;">
						<td colspan="4">[@searchControl searchType="input" searchField="orderNo" searchOper="cn" searchName="制度编号"/]</td>
					</tr>
					

					
