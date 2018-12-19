<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<title>查看文档信息</title>
<script src="<c:url value="/"/>js/changeclass.js"></script> <!--用于改变页面样式-->
<script src="<c:url value="/"/>js/inc_javascript.js" type="text/javascript"></script>
<script src="<c:url value='/js'/>/addattachment.js"></script>
<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
<script src="<c:url value="/"/>datePicker/WdatePicker.js" type="text/javascript"></script>
<script src="<c:url value="/"/>components/jquery-1.4.2.js" type="text/javascript"></script> <!--jquery包-->
<script src="<c:url value="/"/>components/jqgrid/js/jquery-ui-1.8.2.custom.min.js" type="text/javascript"></script><!--jquery ui-->
<script src="<c:url value="/"/>components/jquery.layout.js" type="text/javascript"></script><!--jquery 布局-->
<script src="<c:url value="/"/>components/jqgrid/js/grid.locale-cn.js" type="text/javascript"></script> <!--jqgrid 中文包-->
<script src="<c:url value="/"/>components/jqgrid/js/jquery.jqGrid.min.js" type="text/javascript"></script> <!--jqgrid 包-->
<script src="<c:url value="/"/>js/changeclass.js"></script>

<script type="text/javaScript">
	/*获取实例的HTML*/
	$().ready(function(){
		if (${_FlowInstanceInfor.contentPath != null && _FlowInstanceInfor.contentPath != ""}) {
			$.ajax({
				url: "${_FlowInstanceInfor.contentPath}",
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
</script>
<link type="text/css" href="<c:url value="/"/>components/jqgrid/css/jquery.ui.all.css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
<link type="text/css" rel="stylesheet" href="/css/noTdBottomBorder.css"></link>
<link type="text/css" rel="stylesheet" href="/css/border.css"></link>
<!-- formValidator -->
<link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css"></link>
<script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript" charset="UTF-8"></script>
<script src="<c:url value="/"/>components/formvalidator/js/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
<base target="_self"/>


<body style="overflow-y: auto;padding: 0 100px">
<br/>
<form id="documentInforForm" name="documentInforForm" action="<c:url value="/document/document.do"/>?method=save" method="post" enctype="multipart/form-data">

<input type="hidden" name="documentId" id="documentId" value="${_DocumentInfor.documentId}"/>

<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 100%">
<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
    <div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
    	<span class="ui-jqgrid-title">查看文档信息</span>
    </div>

	<div class="ui-jqgrid-bdiv" style="height: auto; width: 100%">
		<div style="position: relative;">
			<div>
				<table cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable" style="width: 100%" id="addTable">
					<c:choose>
						<c:when test="${!empty _DocumentInfor.reportId && _DocumentInfor.reportId > 0}">
								<tr>
									<td>
										<table cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable" style="width: 95%" id="addTable">
											
											<tbody id="instanceHtml">
												<tr>
													<td style="width: 15%"></td> 
													<td></td>
													<td style="width: 15%"></td>
													<td></td>
												</tr>
												
												<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
													<td class="ui-state-default jqgrid-rownum" style="width: 15%">文件标题：</td> 
													<td colspan="3">${_FlowInstanceInfor.instanceTitle}</td>
												</tr>
												
												<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
													<td class="ui-state-default jqgrid-rownum" style="width: 15%">主办人：</td> 
													<td>${_FlowInstanceInfor.charger.person.personName}</td>
													<td class="ui-state-default jqgrid-rownum" style="width: 15%">申请人：</td>
													<td>${_FlowInstanceInfor.applier.person.personName}</td>
												</tr>
												
												<%-- 实例对应的html放在这里 --%>
												
											</tbody>
											
											<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
												<td class="ui-state-default jqgrid-rownum">归档时间：</td>
												<td colspan="3">${_DocumentInfor.createTime}</td>
											</tr>
											
											<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
												<td class="ui-state-default jqgrid-rownum">附件：</td>
												<td colspan="3"><c:forEach var="file" items="${_Attachment_Names}" varStatus="status"><a href="<c:url value="${'/common/'}"/>download.jsp?filepath=${_Attachments[status.index]}"><span color="red">${file}</span></a><br/></c:forEach></td>
											</tr>
											
											<c:if test="${!empty _FormalAttach_Names}">
												<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
													<td class="ui-state-default jqgrid-rownum">正式附件：</td>
													<td colspan="3"><c:forEach var="file" items="${_FormalAttach_Names}" varStatus="status"><a href="<c:url value="${'/common/'}"/>download.jsp?filepath=${_FormalAttachs[status.index]}"><span color="red">${file}</span></a><br/></c:forEach></td>
												</tr>
											</c:if>
										</table>
									</td>
								</tr>
						</c:when>
						<c:otherwise>
							<tbody>
								<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
									<td class="ui-state-default jqgrid-rownum" width="20%">标题：</td>
									<td colspan="2">${_DocumentInfor.documentTitle}</td>
								</tr>
								
								<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
									<td class="ui-state-default jqgrid-rownum">编号：</td>
									<td colspan="2">${_DocumentInfor.documentCode}</td>
								</tr>
								
								<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
									<td class="ui-state-default jqgrid-rownum">关键字：</td>
									<td colspan="2">${_DocumentInfor.keyword}</td>
								</tr>
								
								<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
									<td class="ui-state-default jqgrid-rownum">说明(摘要)：</td>
									<td colspan="2">${_DocumentInfor.description}</td>
								</tr>
								
								<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
									<td class="ui-state-default jqgrid-rownum">分类：</td>
									<td colspan="2">${_DocumentInfor.category.categoryName}</td>
								</tr>
								
								<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
									<td class="ui-state-default jqgrid-rownum">所属部门：</td>
									<td colspan="2">${_DocumentInfor.department.organizeName}</td>
								</tr>
								
								<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
									<td class="ui-state-default jqgrid-rownum">作者：</td>
									<td colspan="2">${_DocumentInfor.author.person.personName}</td>
								</tr>
								
								<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
									<td class="ui-state-default jqgrid-rownum">创建时间：</td>
									<td colspan="2">${_DocumentInfor.createTime}</td>
								</tr>
								
								<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
									<td class="ui-state-default jqgrid-rownum">最后更新者：</td>
									<td colspan="2">${_DocumentInfor.editor.person.personName}</td>
								</tr>
								
								<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
									<td class="ui-state-default jqgrid-rownum">更新时间：</td>
									<td colspan="2">${_DocumentInfor.updateTime}</td>
								</tr>
								
								<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
									<td class="ui-state-default jqgrid-rownum">是否推荐：</td>
									<td colspan="2"><c:if test="${_DocumentInfor.commended == 0}">不推荐</c:if><c:if test="${_DocumentInfor.commended == 1}">推荐</c:if></td>
								</tr>
								
								<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
									<td class="ui-state-default jqgrid-rownum">附件：</td>
									<td colspan="2"><c:forEach var="file" items="${_Attachment_Names}" varStatus="status"><a href="<c:url value="${'/common/'}"/>download.jsp?filepath=${_Attachments[status.index]}"><span color="red">${file}</span></a><br/></c:forEach></td>
								</tr>
								
							</tbody>
						</c:otherwise>
					</c:choose>
				</table>
			</div>
		</div>
	</div>
	
	<div style="width: 100%" class="ui-state-default ui-jqgrid-pager ui-corner-bottom" dir="ltr">
		<div role="group" class="ui-pager-control">
			<table cellspacing="0" cellpadding="0" border="0" style="width: 100%; table-layout: fixed;" class="ui-pg-table">
				<tbody>
					<tr>
						<td align="left">
							<table cellspacing="0" cellpadding="0" border="0" style="float: left; table-layout: auto;" class="ui-pg-table navtable">
								<tbody>
									<tr>
										<td><input style="cursor: pointer;" type="button" value="确定" onclick="window.close();"/></td>
										<td><input style="cursor: pointer;" type="button" value="关闭" onclick="window.close();"/></td>
									</tr>
								</tbody>
							</table>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</div>
</div>
</form>
</body>

                  
