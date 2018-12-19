<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<script>
	
	//获取实例的html
	$().ready(function(){
		if (${_Instance.contentPath != null && _Instance.contentPath != ""}) {
			$.ajax({
				url: "${_Instance.contentPath}",
				type: "get",
				dataType: "html",
				//async: false,	//设置为同步
				beforeSend: function (xhr) {
				},
				complete : function (req, err) {
					//alert(req.responseText);
					$("#instanceHtml").append(req.responseText);
				}
			});
		}
	});
	
	//编辑审核层
	function editLayer(layerId) {
		var path = "<c:url value='/workflow/layerInfor.do'/>?method=edit&layerId="+layerId;
		window.name = "__self";
		window.open(path, "__self");
		//window.location.href = path;
	}
	
	//编辑审核信息
	function editCheckInfor(checkId) {
		var path = "<c:url value='/workflow/checkInfor.do'/>?method=edit&chargerEdit=true&instanceId=${_Instance.instanceId}&checkId="+checkId;
		window.name = "__self";
		window.open(path, "__self");
		//window.location.href = path;
	}
	
	//删除审核层
	function deleteLayer(layerId) {
		var yes = window.confirm("确定要删除该审核层吗？");
		if (yes) {
			$.ajax({
				url: "/workflow/layerInfor.do?method=delete&layerId="+layerId,
				type: "post",
				dataType: "json",
				async: false,	//设置为同步
				beforeSend: function (xhr) {
				},
				complete : function (req, err) {
					var returnValues = eval("("+req.responseText+")");
				    alert(returnValues["message"]);
				    reloadPage('${_Instance.instanceId}');
				}
			});
		}
	}
	
</script>
<style>
	.ui-jqgrid tr.jqgrow td {
		white-space: normal;
	}
</style>
<link href="<c:url value="/"/>css/submit.css" type="text/css" rel="stylesheet">

	<div>
	<c:if test="${_Instance.flowDefinition.flowId == 86 || _Instance.flowDefinition.flowId == 87  || _Instance.flowDefinition.flowId == 88 }">
	
		<table cellspacing="0" cellpadding="0" border="0" >
				<tr><td style="width: 70%;">
	</c:if>		
		<table cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable" style="width: 90%" id="addTable">
			<tbody  id="instanceHtml">
					<tr>
						<td style="width: 20%"></td>
						<td></td>
						<td style="width: 20%"></td>
						<td></td>
					</tr>
					
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<!-- <td class="ui-state-default jqgrid-rownum" style="width: 15%">文件标题：</td> -->
						<td colspan="4"><b>${_Instance.instanceTitle}</b></td>
					</tr>
					
					<%-- 实例对应的html放在这里 --%>
			</tbody>
		</table>		
		
		<table cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable" style="width: 90%">
			<tbody id="tblGrid">	
				
				<c:choose>
					<c:when test="${_Instance.flowDefinition.flowId == 77}">
						<!-- 显示董事会文件 -->
						<%@include file="../submit/reportBoardContent.jsp" %>
					</c:when>
					<c:otherwise>
						
						<!-- 显示其他工作流内容 -->
						
						<c:if test="${_Instance.flowDefinition.flowId == 86}">
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
								<td class="ui-state-default jqgrid-rownum" style="width: 20%">合同编号：</td>
								<td>${_ContractNo}</td>
						</tr>
						</c:if>
						<c:if test="${!empty _Attachment_Names}">
							<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
								<td class="ui-state-default jqgrid-rownum" style="width: 15%">附件信息：</td>
								<td <%--style="background-color: rgb(26,51,73);border-radius: 5px"--%>><c:forEach var="file" items="${_Attachment_Names}" varStatus="status"><a style="text-decoration:underline" href="<c:url value="${'/common/'}"/>download.jsp?filepath=${_Attachments[status.index]}"><span color="white">${file}</span></a><br/></c:forEach></td>
							</tr>
						</c:if>
						
						<c:if test="${!empty _Instance.manager || !empty _Instance.viceManager}">
							<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
								<c:if test="${_Instance.flowDefinition.flowId == 85}" >
									<td class="ui-state-default jqgrid-rownum" style="width:20%">办公室送审意见：</td>
								</c:if>
								<c:if test="${_Instance.flowDefinition.flowId != 85}" >
									<td class="ui-state-default jqgrid-rownum" style="width:20%">部门审核：</td>
								</c:if>
								<td <%--style="background-color: rgb(26,51,73);border-radius: 8px"--%>><ul id="lis">
									<c:if test="${_Instance.managerChecked||(!empty _Instance.manager&&empty _Instance.submiterWord&&empty _Instance.startTime)}">
										<li class="liDetail2">${_Instance.manager.person.personName}：
											<c:choose>
												<c:when test="${_Instance.managerChecked}"><font color="aqua">${_Instance.managerWord}&nbsp;&nbsp;(<fmt:formatDate value="${_Instance.checkTime}" pattern="yyyy-MM-dd HH:mm:ss"/>)</font>
												</c:when>
												<c:otherwise><font color="#e20000">暂未审核</font></c:otherwise>
											</c:choose><br/>
											<c:forEach var="file" items="${_ManagerAttachment_Names}" varStatus="status"><a href="<c:url value="${'/common/'}"/>download.jsp?filepath=${_ManagerAttachments[status.index]}"><span color="white">${file}</span></a><br/></c:forEach></li>
									</c:if>
									<c:if test="${_Instance.viceManagerChecked||(!empty _Instance.viceManager&&empty _Instance.submiterWord&&empty _Instance.startTime)}">
										<li class="liDetail2">${_Instance.viceManager.person.personName}：
											<c:choose><c:when test="${_Instance.viceManagerChecked}"><font color="aqua">${_Instance.viceManagerWord}&nbsp;&nbsp;(<fmt:formatDate value="${_Instance.viceCheckTime}" pattern="yyyy-MM-dd HH:mm:ss"/>)</font>
											</c:when><c:otherwise><font color="#e20000">暂未审核</font></c:otherwise></c:choose><br/><c:forEach var="file" items="${_ViceManagerAttachment_Names}" varStatus="status"><a href="<c:url value="${'/common/'}"/>download.jsp?filepath=${_ViceManagerAttachments[status.index]}"><span color="white">${file}</span></a><br/></c:forEach></li></c:if><c:if test="${!empty _Instance.submiterWord}"><li class="liDetail2">中止意见：${_Instance.submiterWord}</li></c:if></ul></td>
							</tr>
						</c:if>
						
						<c:if test="${!empty _Instance.oldInstanceId && _Instance.flowDefinition.flowId == 86}">
							<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
								<td class="ui-state-default jqgrid-rownum" style="width: 15%">原终止合同：</td>
								<td><ul id="lis"><a href="/workflow/instanceInfor.do?method=view&instanceId=${_Instance.oldInstanceId }" target="_blank">${_IndexNo }</a></ul></td>
							</tr>
							
							<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
								<td class="ui-state-default jqgrid-rownum" style="width: 15%">中止理由：</td>
								<td>${_Instance.suspendedReason}</td>
							</tr>
						</c:if>
						<c:if test="${!empty _Instance.oldInstanceId && (_Instance.flowDefinition.flowId > 83 && _Instance.flowDefinition.flowId <86) || (_Instance.flowDefinition.flowId >86 && _Instance.flowDefinition.flowId <101) }">

							<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
								<td class="ui-state-default jqgrid-rownum" style="width: 15%">中止理由：</td>
								<td>${_Instance.suspendedReason}</td>
							</tr>
						</c:if>
						
						<c:if test="${empty _Instance.oldInstanceId && _Instance.suspended && _Instance.flowDefinition.flowId > 83 && _Instance.flowDefinition.flowId < 101}">
							<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
								<td class="ui-state-default jqgrid-rownum" style="width: 15%">中止理由：</td>
								<td>${_Instance.suspendedReason}</td>
							</tr>
						</c:if>
							
						<c:if test="${!empty _Instance.layers}">
							<tr>
								<td class="ui-state-default jqgrid-rownum" style="width: 20%"></td>
								<td></td>
							</tr>
							
							<tr class="ui-widget-content jqgrow ui-row-ltr">
								<td class="ui-state-default jqgrid-rownum" colspan="2" style="border-bottom: none"><b>审核信息如下</b>：</td>
							</tr>

							<tr class="ui-widget-content jqgrow ui-row-ltr" style="width: 100%;border-bottom: #e20000">
								<td class="ui-state-default jqgrid-rownum" style="border-bottom: none" colspan="2"><hr style="border:0.5px solid #22FBFF;" /></td>
							</tr>

							<%--<hr style="height:1px;border:none;border-top:3px ridge #22FBFF;" />--%>
							<c:forEach items="${_Instance.layers}" var="layer" varStatus="abc">
								<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;" valign="top">
									<td class="ui-state-default jqgrid-rownum" valign="middle">${layer.layerName}<br/>

										<c:if test="${_IsCharger && _Instance.stamped < 1}">
											<c:if test="${layer.status==0||layer.status==2}">
												<c:if test="${fn:length(_Instance.layers) == abc.count }">
													<img src="<c:url value='/'/>images/edit.gif" border="0" onclick="editLayer('${layer.layerId}');" title="设定审核人" style="cursor: pointer;"/>
												</c:if>
											</c:if>
											&nbsp;
											<c:if test="${empty layer.checkInfors}">
												<c:choose>
													<c:when test="${layer.layerName == '文件流转审核人' && abc.count == 1 }">

													</c:when>
													<c:otherwise>
														<img src="<c:url value='/'/>images/delete.gif" border="0" style="cursor: pointer;" onclick="deleteLayer('${layer.layerId}');" title="删除"/>
													</c:otherwise>
												</c:choose>
											</c:if>
										</c:if>
									</td>
									<td><c:if test="${!empty layer.checkDemand}"><b>审核要求</b>：${layer.checkDemand}<br></c:if><br/>
										<ul id="lis">
											<c:forEach items="${layer.checkInfors}" var="checkInfor">
												<c:if test="${checkInfor.status==1||(checkInfor.status==0&&(layer.status==0||layer.status==3)&&!_Instance.suspended&&empty _Instance.endTime)}">
													<li class="liDetail2"><strong>${checkInfor.checker.person.personName}：</strong><font color="aqua">${checkInfor.checkComment}
														<c:if test="${!empty checkInfor.endDate}">
															&nbsp;&nbsp;(<fmt:formatDate value="${checkInfor.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>)</font>
														</c:if>
														<c:if test="${empty checkInfor.endDate}">
															<font color="#e20000">暂未审核</font>
														</c:if>
														<c:if test="${_IsAdmin}">
															<img src="<c:url value='/'/>images/edit.gif" border="0" onclick="editCheckInfor('${checkInfor.checkId}');" title="修改审核意见" style="cursor: pointer;"/>
														</c:if>
														<br/>
														<c:if test="${empty layer.flowNode||(!empty layer.flowNode&&layer.flowNode.download)}">
															<attachment:fileView contextPath="">${checkInfor.attatchment}</attachment:fileView>
														</c:if>
													</li>
												</c:if>
											</c:forEach>
										</ul>
									</td>
								</tr>
							</c:forEach>
						</c:if>

						<c:if test="${_Instance.flowDefinition.flowId == 82 && _Instance.stamped}">
							<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
								<td class="ui-state-default jqgrid-rownum" style="width: 20%">是否盖章：</td>
								<td><font color="white">已盖章</font></td>
							</tr>
						</c:if>
						
						<c:if test="${!empty _Instance.endTime}">
							<tr class="ui-widget-content jqgrow ui-row-ltr">
								<td class="ui-state-default jqgrid-rownum" colspan="2"><b>结束信息如下</b>：</td>
							</tr>
							
							<c:if test="${_Instance.handOut}">
								<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
									<td class="ui-state-default jqgrid-rownum" style="width: 20%">是否下发：</td>
									<td><font color=white><b>已下发到分公司收文</b></font></td>
								</tr>
							</c:if>
							
							<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
								<td class="ui-state-default jqgrid-rownum" style="width: 20%">结束时间：</td>
								<td><fmt:formatDate value="${_Instance.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							</tr>
							
							<c:if test="${!empty _FormalAttachment_Names}">
								<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
									<td class="ui-state-default jqgrid-rownum" style="width: 20%">正式附件：</td>
									<td><c:forEach var="file" items="${_FormalAttachment_Names}" varStatus="status"><a href="<c:url value="${'/common/'}"/>download.jsp?filepath=${_FormalAttachments[status.index]}"><span color="white">${file}</span></a><br/></c:forEach></td>
								</tr>
							</c:if>
						</c:if>
						
						<c:if test="${_Instance.filed}">
							<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
								<td class="ui-state-default jqgrid-rownum" style="width: 20%">归档状态：</td>
								<td><font color="white">已归档</font></td>
							</tr>
						</c:if>
						
					</c:otherwise>
				</c:choose>
				
			</tbody>
		</table>
		<c:if test="${_Instance.flowDefinition.flowId == 86}">
		</td><td><img src="/images/htsp.png"></td></tr></table>
		</c:if>
		<c:if test="${_Instance.flowDefinition.flowId == 87}">
		</td><td><img src="/images/nbbg.png"></td></tr></table>
		</c:if>
		<c:if test="${_Instance.flowDefinition.flowId == 88}">
		</td><td><img src="/images/zdps.png"></td></tr></table>
		</c:if>
	</div>		
                  
