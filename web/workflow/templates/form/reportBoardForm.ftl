[#ftl][#-- 注:此标签用来指定ftl标签使用方括号,不能删 --]
[#include "customizeTag.ftl"/]
[#-- 注:使用自定义标签时,controlName要放在最后 --]

					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum" style="width: 15%">来文单位：</td>
						<td>[@select selectId="selUnitName" optionValueArray=["上港集团物流有限公司",
						"上海集发物流有限公司",
						"上海深水港国际物流有限公司",
						"上海东方海外集装箱货运有限公司",
						"上海海辉国际集装箱修理有限公司",
						"上海海富国际集装箱货运有限公司",
						"上海外高桥物流中心有限公司",
						"上海港城危险品物流有限公司",
						"上海港口化工物流有限公司",
						"上海港船务代理有限公司",
						"上海路港集装箱多式联运有限公司",
						"上海宝罗危险品仓储有限公司",
						"上海海铁联运服务有限公司",
						"上海英雪纳国际货运有限公司",
						"上海金港驾驶培训有限公司",
						"上海集盛劳务有限公司",
						"上海联合国际船舶代理有限公司",
						"上海航华国际船务代理有限公司",
						"湖北航华国际船务代理有限公司",
						"江西航华国际船务代理有限公司",
						"扬州航华国际船务有限公司",
						"宁波航华国际船务有限公司",
						"温州航华国际船务有限公司",
						"深圳航华国际船务代理有限公司",
						"江苏航华国际船务代理有限公司",
						"上海海华国际货运有限公司",
						"宁波大榭开发区集信物流有限公司",
						"芜湖申芜港联国际物流有限公司",
						"重庆久久物流有限责任公司",
						"上海外红伊势达国际物流有限公司",
						"上海江海国际集装箱货运有限公司",
						"上海华英仓储有限公司",
						"大丰集丰物流有限公司",
						"安庆申安物流有限公司",
						"上海联东地中海国际船舶代理有限公司",
						"上港物流(江西)有限公司",
						"上海新港集装箱物流有限公司",
						"上港物流（宁波）有限公司",
						"上港物流金属仓储（上海）有限公司",
						"上港物流信息技术（上海）有限公司",
						"上港物流（天津）有限公司",
						"上港物流（厦门）有限公司",
						"上海海通国际汽车物流有限公司",
						"上海新高桥物流有限公司",
						"上港物流拼箱服务（上海）有限公司",
						"上港船舶服务（上海）有限公司"] controlName="selUnitName"/]</td>
						<td class="ui-state-default jqgrid-rownum" style="width: 15%">催办等级：</td>
						<td>[@select selectId="urgencyName" optionValueArray=["一般","普通","紧急"] controlName="urgencyName"/]</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum">会议性质：</td>
						<td>[@select selectId="meetNature" optionValueArray=["年度","临时"] controlName="meetNature"/]</td>
						<td class="ui-state-default jqgrid-rownum">召开时间：</td>
                    	<td>[@dateInput size="15" controlName="holdTime"/]</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum">年代：</td>
						<td colspan="3">[@input size="15" controlName="boardYear"/]</td>
					</tr>
					<tr style="display: none;">
						<td colspan="4">
							<script>
								if ('${_InstanceId}'==null||'${_InstanceId}'=='') {
									var sysDate = new Date();
									var sysYear = sysDate.getFullYear();
									//初始化年代
									var boardYears = document.getElementsByName("boardYear");
									boardYears[0].value = sysYear;
									
									//初始化经办时间
									var handlingTimes = document.getElementsByName("handlingTime");
									var month = sysDate.getMonth()+1;
									var sysDateFormat = sysYear+"-"+month+"-"+sysDate.getDate();
									handlingTimes[0].value = sysDateFormat;
									
									//初始化经办人
									if (document.all.chargerId != null) {
										document.all.attner.value = document.all.chargerId.options[document.all.chargerId.selectedIndex].text;
									}
								}
							</script>
						</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum">我方董事：</td>
						<td colspan="3">[@input size="40" controlName="director"/]</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum">我方监事：</td>
						<td colspan="3">[@input size="40" controlName="supervisors"/]</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum">经办人：</td>
						<td>[@input size="15" controlName="attner"/]</td>
						<td class="ui-state-default jqgrid-rownum">经办时间：</td>
                    	<td>[@dateInput size="15" controlName="handlingTime"/]</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum">备注：</td>
						<td colspan="3">[@textarea controlName="memo"/]</td>
					</tr>
					<tr style="display: none;">
						<td>[@searchControl searchType="input" searchField="selUnitName" searchOper="cn" searchName="来文单位"/]</td>
						<td colspan="3">[@searchControl searchType="dateInput" searchField="holdTime" searchOper="eq" searchName="召开时间"/]</td>
					</tr>
