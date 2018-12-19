<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>
			
			<div>	
				<table cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable" style="width: 90%">
					<tbody>	
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum" style="width: 15%" rowspan="3">流程信息</td>
							<td colspan="2">【名称】${_Flow.flowName}</td>
						</tr>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td colspan="2">【主办人】${_Flow.charger.person.personName}</td>
						</tr>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td colspan="2">【说明】${_Flow.memo}</td>
						</tr>
					</tbody>
				</table>
			</div>
	
