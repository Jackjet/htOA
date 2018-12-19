[#ftl][#-- 注:此标签用来指定ftl标签使用方括号,不能删 --]
[#include "customizeTag.ftl"/]
[#-- 注:使用自定义标签时,controlName要放在最后 --]
 
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum" style="width: 15%">归属公司：</td>
						<td colspan="3" id="guishu">[@select selectId="category" optionValueArray=["码头","物流"] controlName="category"/]</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum" style="width: 15%">流水号：</td>
						<td colspan="3">[@input size="35"  controlName="indexNo"/]</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum" style="width: 15%">合同类型：</td>
						<td id="typeTd" colspan="3">[@select selectId="contractType" optionValueArray=["销售合同","一般采购合同","物流采购合同","其他合同"] controlName="contractType"/]</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum" style="width: 15%" nowrap>是否使用了公司合同模板：</td>
						<td colspan="3" id="shiyong">[@select selectId="useModel" optionValueArray=["是","否"] controlName="useModel"/]</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum">我方对外签署名称：</td>
						<td colspan="3">[@input size="40" controlName="foreignName"/]</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum">对方单位：</td>
						<td colspan="3">[@input size="40" controlName="oppositeUnit"/]</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum">履约期限：<br>（例：20130101-20140101）</td>
						<td colspan="3">[@input size="40" controlName="contractPeriod"/]</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum">合同价金：<br>（例：100.00元）</td>
						<td colspan="3">[@input size="15" controlName="contractPrice"/]</td>
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
						<td class="ui-state-default jqgrid-rownum">合同说明：</td>
						<td colspan="3">[@textarea controlName="memo"/]</td>
					</tr>
					<tr style="display: none;">
						<td>[@hidden controlName="reportYear"/]</td>
						<td colspan="2">[@hidden controlName="serialNo"/]</td>
						<td><script>
							$(document).ready(function(){

							document.getElementsByName("indexNo")[0].readOnly = "true";
//								alert($("#fff").html())
                                var contractType = $('#contractType option:selected').val();
								var typeTd = $('#typeTd').html();
                                typeTd = typeTd.replace(/[\'\"\\\/\b\f\n\r\t]/g, '');
                                if(contractType!="销售合同" && typeTd!="销售合同" ){
                                    $('#saleTypeTr').css("display","none");
                                }else{
                                    $('#saleTypeTr').css("display","");
								}

							});
							function createContractNo() {
								var category = $("#category option:selected").val();
								var contractNos = document.getElementsByName("indexNo");
								
								
								 //$("#indexNo").attr("readonly":"readonly"); 
								//alert(document.getElementsByName("indexNo").readOnly);
								var sysDate = new Date();
								var sysYear = sysDate.getFullYear();
								var sysMonth = sysDate.getMonth()+1;
								var sysDate = sysDate.getDate();
								if (category != '' && category.length != null) {
									//获取最大流水号
									var serialNo = "";
									$.ajax({
										url: "/workflow/submit.do?method=getHTSerialNo&tableName=Customize_Hetongshenpi&fieldName=serialNo&fieldYear=reportYear&reportYear="+sysYear+"&categoryName="+encodeURI(category),
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
									if(category=="物流"){
										category="WL";
									
									}else{
										category="MT";
									
									}
									contractNos[0].value = category+sysYear+serialNo;
									var reportYears = document.getElementsByName("reportYear");
									var serialNos = document.getElementsByName("serialNo");
									reportYears[0].value = sysYear;
									serialNos[0].value = serialNo;
								}else {
									contractNos[0].value = "";
								}
							}
							
							function setCompanyName() {
								var category = $("#category option:selected").val();
								
								var foreignNames = document.getElementsByName("foreignName");
								
								if (category != '' && category.length != null) {
									if(category=="物流"){
										foreignNames[0].value = "上海海通国际汽车物流有限公司";
									}else if(category=="码头"){
										foreignNames[0].value = "上海海通国际汽车码头有限公司";
									}else{
										foreignNames[0].value = "";
									}
								}else{
									foreignNames[0].value = "";
								}
							}
							function contract_sale() {
								var contractType = $('#contractType option:selected').val();
//								var typeTd = $('#typeTd').html();
//								alert(!contractType.eq('销售合同')  || !typeTd.eq('销售合同'))
                                if(contractType != '销售合同' ){
                                    $('#saleTypeTr').css("display","none");
                                    $('#saleType option:selected').val("");
								}else {
                                    $('#saleTypeTr').css("display","");

                                    $("#saleType option[value='现有业务']").attr("selected","selected");



//
								}
                            }
							
							if('${_InstanceId}'==null||'${_InstanceId}'=='') {
								$("#category111").change(function(){createContractNo();});
                            }
							//根据合同所属公司自动生成对应的“对外签署名称”
							$("#category").change(function(){setCompanyName();});

                            //$("#contractType").change(function(){contract_sale();});

						</script></td>

					</tr>
						<tr style="display: none;">
						<td colspan="4">[@hidden controlName="contractNo"/]</td>
					</tr>
					<tr style="display: none;">
						<td colspan="4">[@searchControl searchType="input" searchField="contractType" searchOper="cn" searchName="合同类型"/]</td>
					</tr>
					<tr style="display: none;">
						<td colspan="4">[@searchControl searchType="input" searchField="contractNo" searchOper="cn" searchName="合同编号"/]</td>
					</tr>
					<tr style="display: none;">
						<td colspan="4">[@searchControl searchType="input" searchField="indexNo" searchOper="cn" searchName="合同流水号"/]</td>
					</tr>
					<tr style="display: none;">
						<td colspan="4">[@searchControl searchType="input" searchField="contractPrice" searchOper="cn" searchName="合同金额"/]</td>
					</tr>
					<tr style="display: none;">
						<td colspan="4">[@searchControl searchType="input" searchField="oppositeUnit" searchOper="cn" searchName="对方单位"/]</td>
					</tr>
					
