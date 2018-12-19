<%@ page contentType="text/html; charset=utf-8"%>
<link rel="stylesheet" type="text/css" media="screen" href="/css/myTabll.css"/>
<style>
	.table_dashed{
		border:1px dashed #3db2b6;
	}
	.table_dashed td{
		border:1px dashed #3db2b6;
		text-align: center;
	}
</style>
<%@ include file="/inc/taglibs.jsp"%>

					<div  style="background-image: url('/img/bgIn.png')"><!-- 流程信息 -->
						<form:form commandName="flowDefinitionVo" name="definitionForm" id="definitionForm" action="/workFlowDefinition.do?method=save" method="post">
							<form:hidden path="flowId"/>
							<table class="table_dashed"  width="98%" cellpadding="6" cellspacing="0" align="center" border="0">

								<tr>
									<td colspan="10"  align="left">流程信息</td>
								</tr>
								<tr>
									<td style="width: 8%;"  class="right_td">流程名称：</td>
									<td style="width: 12%;" ><form:input path="flowName" size="23"/></td>
									
									<td style="width: 8%;" class="right_td" >&nbsp;状态：</td>
									<td style="width: 12%;" ><form:select path="status">
										<form:option value="1">启用</form:option>
										<form:option value="0">停用</form:option>
									</form:select></td>
									
									<td style="width: 10%;"  class="right_td">流程类型：</td>
									<td style="width: 10%;" ><form:select path="flowType">
										<form:option value="0">固定</form:option>
										<form:option value="1">人工</form:option>
									</form:select></td>
									
									<td style="width: 8%;"  >主办人：</td>
									<td style="width: 12%;" ><form:select path="chargerId">
										<form:option value="0">--选择主办人--</form:option>
										<c:forEach items="${_AllSystemUsers}" var="user">
											<form:option value="${user.personId}">${user.person.personName}</form:option>
										</c:forEach>
									</form:select></td>
										
									<td style="width: 10%;"  class="right_td">流程模板：</td>
									<td >
										<select name="flowTemplate" id="flowTemplate"></select>
										<script>
											//获取流程模板信息
											$.getJSON("/workFlowDefinition.do?method=getFlowTemplates",function(data) {
												if (data != null) {
													var content = "<option value=''>--选择模板--</option>";
													$.each(data.templates, function(i, n) {
														if ('${_TemplateName}'==n.templateName) {
															content += "<option value='"+ n.templateName + "' selected='selected'>" + n.templateName + "</option>";
														}else {
															content += "<option value='"+ n.templateName + "'>" + n.templateName + "</option>";
														}
													});
													$("#flowTemplate").html(content);
												}
											});
										</script>
									</td>
								</tr>
								<tr>
									<td style="width: 8%;" class="right_td" >流程分类：</td>
									<td style="width: 12%;" ><form:input path="categoryName" size="23"/><br/><font color="red">如：收文，发文，报告</font></td>
									<td style="width: 8%;" class="right_td" >&nbsp;归档人：</td>
									<td style="width: 12%;"  colspan=2>
										<form:radiobutton path="filerType" value="0" onclick="selectFileType();" />主办人<br/>
										<form:radiobutton path="filerType" value="1" onclick="selectFileType();"/>固定角色
										<div id="fileRoleDiv" style="display:none;">
											<form:select path="fileRoleId">
												<c:forEach items="${_AllRoles}" var="role">
													<form:option value="${role.roleId}">${role.roleName}</form:option>
												</c:forEach>
											</form:select>
										</div>
									</td>
									<td style="width: 8%;" >流程说明：</td>
									<td  colspan="4"><form:textarea path="memo" cols="50" rows="3"></form:textarea></td>
								</tr>
								
								<tr>
									<td  colspan="10">
										<input style="cursor: pointer;" type="submit" value="提交"/>&nbsp;&nbsp;&nbsp;&nbsp;
										<input style="cursor: pointer;" type="reset" value="重写"/>
									</td>
								</tr>
							</table>
						</form:form>
					</div>
