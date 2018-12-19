<%@ page contentType="text/html; charset=utf-8"%>

<link rel="stylesheet" type="text/css" media="screen" href="/css/myTabll.css"/>
<style>
	.table_dashed td{
		border:1px dashed #3db2b6;
		text-align: center;
	}
</style>
<%@ include file="/inc/taglibs.jsp"%>
	  				
					<div><!-- 流程信息 -->
						<form name="definitionForm" id="definitionForm" action="<c:url value="/workFlowDefinition.do"/>?method=save" method="post" enctype="multipart/form-data">
							<input type="hidden" name="flowId" id="flowId" value="${_FlowDefinition.flowId}"/>
							<table class="table_dashed" width="98%" cellpadding="6" cellspacing="0" align="center" border="0"
								   style="border:1px dashed #3db2b6" >

								<tr>
									<td colspan="10"  align="left">流程信息</td>
								</tr>
								<tr >
									<td style="width: 10%;"  >流程名称：</td>
									<td style="width: 12%;">${_FlowDefinition.flowName}</td>
									
									<td style="width: 8%;"  >状态：</td>
									<td style="width: 8%;" >
		                    			<c:if test="${_FlowDefinition.status == 0}">
		                    				停用
		                    			</c:if>
		                    			<c:if test="${_FlowDefinition.status == 1}">
		                    				启用
		                    			</c:if>
				                    </td>
									
									<td style="width: 10%;" >流程类型：</td>
									<td style="width: 8%;" >
		                    			<c:if test="${_FlowDefinition.flowType == 0}">
		                    				固定
		                    			</c:if>
		                    			<c:if test="${_FlowDefinition.flowType == 1}">
		                    				人工
		                    			</c:if>
									</td>
									
									<td style="width: 8%;"  >主办人：</td>
									<td style="width: 8%;" >
										${_FlowDefinition.charger.person.personName}
									</td>
									
									<td style="width: 8%;"  >流程模板：</td>
									<td >${_FlowDefinition.template}</td>
								</tr>
								<tr>
									<td style="width: 8%;"  >流程分类：</td>
									<td style="width: 12%;" >
										${_FlowDefinition.categoryName}
									</td>
									<td style="width: 8%;"  >归档人：</td>
									<td style="width: 12%;"  colspan=2>
										<c:if test="${_FlowDefinition.filerType == '0'}">
											主办人
										</c:if>
										<c:if test="${_FlowDefinition.filerType == '1'}">
											${_FlowDefinition.fileRole.roleName}
										</c:if>
									</td>
									<td style="width: 8%;" >流程说明：</td>
									<td  colspan="5">${_FlowDefinition.memo}</td>
								</tr>
								
							</table>
						</form>
					</div>
