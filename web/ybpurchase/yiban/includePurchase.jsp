<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<script>


	<%--//编辑审核信息--%>
	<%--function editCheckInfor(checkId) {--%>
		<%--var path = "<c:url value='/ybpurchase/checkInfor.do'/>?method=edit&chargerEdit=true&instanceId=${_Purchase.instanceId}&checkId="+checkId;--%>
		<%--window.name = "__self";--%>
		<%--window.open(path, "__self");--%>
		<%--//window.location.href = path;--%>
	<%--}--%>

</script>
<style>
	.ui-jqgrid tr.jqgrow td {
		white-space: normal;
	}
</style>
<link href="<c:url value="/"/>css/submit.css" type="text/css" rel="stylesheet">

	<div>
	<c:if test="${Purchase.flowId.flowId == 1 }">
	
		<table cellspacing="0" cellpadding="0" border="0" >
				<tr><td style="width: 70%;">
	</c:if>		
		<table cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable" style="width: 90%">
			<tbody id="tblGrid">	
						<c:if test="${_Purchase.flowId.flowId == 1}">
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
								<td class="ui-state-default jqgrid-rownum" style="width: 20%">标题：</td>
								<td>${_Purchase.purchaseStr1}</td>
						</tr>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum" style="width: 20%">采购物品：</td>
							<td>${_Purchase.purchaseTitle}</td>
						</tr>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum" style="width: 20%">数量：</td>
							<td>${_Purchase.purchaseNumber}</td>
						</tr>
							<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
								<td class="ui-state-default jqgrid-rownum" style="width: 20%">单位：</td>
								<td>${_Purchase.purchaseGoods}</td>
							</tr>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum" style="width: 20%">规格：</td>
							<td>${_Purchase.guige}</td>
						</tr>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum" style="width: 20%">用途：</td>
							<td>${_Purchase.application}</td>
						</tr>
						<c:if test="${_Purchase.ysType != null}">
							<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
								<td class="ui-state-default jqgrid-rownum" style="width: 20%">预算类型：</td>
								<td>${_Purchase.ysType}</td>
							</tr>
						</c:if>
						<c:if test="${_Purchase.purchaseStr2 != null}">
							<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
								<td class="ui-state-default jqgrid-rownum" style="width: 20%">归属公司：</td>
								<td>${_Purchase.purchaseStr2}</td>
							</tr>
						</c:if>
						<c:if test="${_Purchase.supplierName != null}">
							<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
								<td class="ui-state-default jqgrid-rownum" style="width: 20%">供应商：</td>
								<td>${_Purchase.supplierName}</td>
							</tr>
						</c:if>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum" style="width: 20%">使用部门：</td>
							<td>${_Purchase.department.organizeName}</td>
						</tr>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum" style="width: 20%">归口部门：</td>
							<td>${_Purchase.guikouDepartment.organizeName}</td>
						</tr>
							<c:if test="${_Purchase.purchaseStatus > 4}">
							<c:choose>
								<c:when test="${_Purchase.purchaseWay == 1}">
									<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
										<td class="ui-state-default jqgrid-rownum" style="width: 20%">采购方式：</td>
										<td>三方比价</td>
									</tr>
								</c:when>
								<c:when test="${_Purchase.purchaseWay == 2}">
									<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
										<td class="ui-state-default jqgrid-rownum" style="width: 20%">采购方式：</td>
										<td>招投标</td>
									</tr>
								</c:when>
								<c:when test="${_Purchase.purchaseWay == 3}">
									<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
										<td class="ui-state-default jqgrid-rownum" style="width: 20%">采购方式：</td>
										<td>合同变更</td>
									</tr>
								</c:when>
								<c:otherwise>
									<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
										<td class="ui-state-default jqgrid-rownum" style="width: 20%">采购方式：</td>
										<td>直接采购</td>
									</tr>
								</c:otherwise>
							</c:choose>
							</c:if>
						<c:if test="${_Purchase.purchaseMoney != null}">
							<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
								<td class="ui-state-default jqgrid-rownum" style="width: 20%">预计价格：</td>
								<td>${_Purchase.purchaseMoney}元</td>
							</tr>
						</c:if>
						<c:if test="${_Purchase.purchaseFinalMoney != null}">
							<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
								<td class="ui-state-default jqgrid-rownum" style="width: 20%">最终价格：</td>
								<td>${_Purchase.purchaseFinalMoney}元</td>
							</tr>
						</c:if>
						</c:if>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum" style="width: 20%">申请时间：</td>
							<td>${_Purchase.startTime}</td>
						</tr>
						<c:if test="${!empty _Attachment_Names}">
							<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
								<td class="ui-state-default jqgrid-rownum" style="width: 15%">附件信息：</td>
								<td <%--style="background-color: rgb(26,51,73);border-radius: 5px"--%>><c:forEach var="file" items="${_Attachment_Names}" varStatus="status"><a style="text-decoration:underline" href="<c:url value="${'/common/'}"/>download.jsp?filepath=${_Attachments[status.index]}"><span color="white">${file}</span></a><br/></c:forEach></td>
							</tr>
						</c:if>
							<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
								<td class="ui-state-default jqgrid-rownum" style="width:20%">部门领导审核：</td>
								<td >
									<ul id="lis">
									<%--<c:if test="${_Purchase.managerChecked || (!empty _Purchase.manager )}">--%>
										<li class="liDetail2">${_Purchase.manager.person.personName}：
											<c:choose>
												<c:when test="${_Purchase.managerChecked}">
													<font color="aqua">
															<c:if test="${_Purchase.managerCheckStatus == 1}">同意&nbsp;&nbsp;</c:if>
															<c:if test="${_Purchase.managerCheckStatus == 2}">不同意&nbsp;&nbsp;${_Purchase.managerWord}</c:if>
															&nbsp;&nbsp;(<fmt:formatDate value="${_Purchase.checkTime}" pattern="yyyy-MM-dd HH:mm:ss"/>)</font>
												</c:when>
												<c:otherwise><font color="#e20000">暂未审核</font></c:otherwise>
											</c:choose><br/>
											<c:forEach var="file" items="${_ManagerAttachment_Names}" varStatus="status">
												<a href="<c:url value="${'/common/'}"/>download.jsp?filepath=${_ManagerAttachments[status.index]}">
													<span color="white">${file}</span></a><br/>
											</c:forEach>
										</li>
									<%--</c:if>--%>
										<c:if test="${_Purchase.viceManager  != null}">
										<li class="liDetail2">${_Purchase.viceManager.person.personName}：
											<c:choose>
												<c:when test="${_Purchase.viceManagerChecked}">
													<font color="aqua">
														<c:if test="${_Purchase.viceManagerCheckStatus == 1}">同意&nbsp;&nbsp;</c:if>
														<c:if test="${_Purchase.viceManagerCheckStatus == 2}">不同意&nbsp;&nbsp;${_Purchase.viceManagerWord}</c:if>
															&nbsp;&nbsp;(<fmt:formatDate value="${_Purchase.viceManagercheckTime}" pattern="yyyy-MM-dd HH:mm:ss"/>)</font>
												</c:when>
												<c:otherwise><font color="#e20000">暂未审核</font></c:otherwise>
											</c:choose><br/>
											<c:forEach var="file" items="${_ViceManagerAttachment_Names}" varStatus="status">
												<a href="<c:url value="${'/common/'}"/>download.jsp?filepath=${_ViceManagerAttachments[status.index]}">
													<span color="white">${file}</span></a><br/>
											</c:forEach>
										</li>
										</c:if>
										<c:if test="${!empty _Instance.submiterWord}">
											<li class="liDetail2">中止意见：${_Instance.submiterWord}</li>
										</c:if>
									</ul>
								</td>
							</tr>
						<c:if test="${!empty _Purchase.layers}">
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
							<c:forEach items="${_Purchase.layers}" var="layer" varStatus="abc">
								<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;" valign="top">
									<td class="ui-state-default jqgrid-rownum" valign="middle">${layer.layerName}<br/>
									</td>
									<td>
										<ul id="lis">
											<c:forEach items="${layer.checkInfors}" var="checkInfor">
												<c:if test="${layer.status==1}">
													<li class="liDetail2"><strong>${checkInfor.checker.person.personName}&nbsp;&nbsp;</strong><font color="aqua">
														<c:if test="${checkInfor.status == 1}">同意&nbsp;&nbsp;</c:if>
														<c:if test="${checkInfor.status == 2}">不同意&nbsp;&nbsp;${checkInfor.checkComment}</c:if>
														<c:if test="${checkInfor.status == 3}">退回&nbsp;&nbsp;${checkInfor.checkComment}</c:if>
														<c:if test="${!empty checkInfor.endDate}">
															&nbsp;&nbsp;(<fmt:formatDate value="${checkInfor.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>)</font>
														</c:if>
														<c:if test="${empty checkInfor.endDate}">
															<font color="#e20000">暂未审核</font>
														</c:if>
														<%--<c:if test="${_IsAdmin}">--%>
															<%--<img src="<c:url value='/'/>images/edit.gif" border="0" onclick="editCheckInfor('${checkInfor.checkInforId}');" title="修改审核意见" style="cursor: pointer;"/>--%>
														<%--</c:if>--%>
														<br/>
														<c:if test="${empty layer.purchaseNode||(!empty layer.purchaseNode&&layer.purchaseNode.download)}">
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

						<c:if test="${!empty _Purchase.endTime}">
							<tr class="ui-widget-content jqgrow ui-row-ltr">
								<td class="ui-state-default jqgrid-rownum" colspan="2"><b>结束信息如下</b>：</td>
							</tr>
							<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
								<td class="ui-state-default jqgrid-rownum" style="width: 20%">结束时间：</td>
								<td><fmt:formatDate value="${_Purchase.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							</tr>
							<c:if test="${!empty _FormalAttachment_Names}">
								<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
									<td class="ui-state-default jqgrid-rownum" style="width: 20%">正式附件：</td>
									<td><c:forEach var="file" items="${_FormalAttachment_Names}" varStatus="status"><a href="<c:url value="${'/common/'}"/>download.jsp?filepath=${_FormalAttachments[status.index]}"><span color="white">${file}</span></a><br/></c:forEach></td>
								</tr>
							</c:if>
						</c:if>
			</tbody>
		</table>
		<%--<c:if test="${_Purchase.flowId.flowId == 1}">--%>
		<%--</td><td><img src="/images/htsp.png"></td></tr></table>--%>
		<%--</c:if>--%>
	</div>
                  
