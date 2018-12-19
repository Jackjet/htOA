<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>
<%@ page import="com.kwchina.oa.submit.util.SubmitConstant" %>

	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

	<script src="<c:url value="/"/>js/autoRowSpan.js"></script>
	<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
	<link href="<c:url value="/"/>css/submit.css" type="text/css" rel="stylesheet">
	<head>
		<style type="text/css">
			body {
				margin-left: 0px;
				margin-top: 0px;
				margin-right: 0px;
				margin-bottom: 0px;	
			}
			*{font-size:15px}
			table{
				border-collapse:collapse;
				border-color: black;
			}
			td {border:1px solid;}
		</style>
		<title>审批打印</title>
	</head>
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
		
		//根据表格与页面的高度差调整tr高度
		var bodyHeight = document.body.offsetHeight;
		var screenHeight = screen.availHeight;
		//alert(bodyHeight);
		//alert(screenHeight);
		if (screenHeight - bodyHeight > 190) {
			var tb = document.getElementById("tb");
			var varHeight = (screenHeight - bodyHeight - 120)/(tb.rows.length-2);
			//alert(varHeight);
			for(var i=0;i<tb.rows.length;i++) {
	            if (tb.rows[i].id != 'head' && tb.rows[i].id != 'title') {
	            	tb.rows[i].style.height = varHeight + parseInt(tb.rows[i].style.height);
	            	//alert(tb.rows[i].style.height);
	            }
	       }
		}
	});
	
	</script>

	<body onload="autoRowSpan(tb,0,0)">
		<table id="tb" width="90%" border="0" align="center" cellpadding="5" cellspacing="0">

			<tr id="head" style="height: 80px;">
				<th colspan="4" class="TotCatalogHead">
					<script>
						var titleString = "上海海通国际汽车-码头-物流-有限公司";
						if (${_Flow.flowId}==<%=SubmitConstant.SubmitFlow_Report_Publish%>) {
							titleString += "发文处理单";
						}else if (${_Flow.flowId}==<%=SubmitConstant.SubmitFlow_Report_Receive%>) {
							titleString += "收文处理单";
						}else if (${_Flow.flowId}==<%=SubmitConstant.SubmitFlow_Party_Publish%>) {
							titleString += "党委发文处理单";
						}else if (${_Flow.flowId}==<%=SubmitConstant.SubmitFlow_Party_Receive%>) {
							titleString += "党群收文处理单";
						}else if (${_Flow.flowId}==<%=SubmitConstant.SubmitFlow_Report_Inside%>) {
							titleString += "内部报告处理单";
						}else if (${_Flow.flowId}==<%=SubmitConstant.SubmitFlow_Party_Inside%>) {
							titleString += "党群部内部报告处理单";
						}else if (${_Flow.flowId}==<%=SubmitConstant.SubmitFlow_Report_Board%>) {
							titleString += "<br/>董事会文件处理单";
						}else if (${_Flow.flowId}==<%=SubmitConstant.SubmitFlow_HR_ResignApproval%>) {
							titleString += "--员工离职审批表";
						}else if (${_Flow.flowId}==<%=SubmitConstant.SubmitFlow_HR_ResignProcedure%>) {
							titleString += "员工离职手续表";
						}else if (${_Flow.flowId}==<%=SubmitConstant.SubmitFlow_HR_DynamicPersonnel%>) {
							titleString += "人员动态审批表";
						}else if (${_Flow.flowId}==<%=SubmitConstant.SubmitFlow_HR_TrainingApproval%>) {
							titleString += "<br/>培训报批处理单";
						}else if (${_Flow.flowId}==<%=SubmitConstant.SubmitFlow_Report_Contract%>) {
							titleString += "--合同评审表";
						}else if (${_Flow.flowId}==<%=SubmitConstant.SubmitFlow_Report_ContractCheck%>) {
							titleString += "合同验收";
						}else if (${_Flow.flowId}==<%=SubmitConstant.SubmitFlow_Report_Order%>) {
							titleString += "制度评审表";
						}
						document.write(titleString);
					</script>
				</th>
			</tr>	
			<script>
				if (${_Flow.flowId}==<%=SubmitConstant.SubmitFlow_Report_Contract%>) {
				document.write("<tr><th align=left>合同编号：${_ContractNo}</th><th colspan=3 align=right><font style='font-weight:normal'>QR-7.2-02</font></th></tr>");
				}
				if (${_Flow.flowId}==<%=SubmitConstant.SubmitFlow_Report_Publish%>) {
				document.write("<tr><th colspan=4 align=right><font style='font-weight:normal'>QR-4.2.3-02-01</font></th></tr>");
				}
				if (${_Flow.flowId}==<%=SubmitConstant.SubmitFlow_Report_Receive%>) {
				document.write("<tr><th colspan=4 align=right><font style='font-weight:normal'>QR-4.2.3-02-02</font></th></tr>");
				}
			</script>
			<tbody id="instanceHtml">
				<tr style="height: 80px;" id="title">
					<td width="20%">
						文件标题
					</td>
					<td colspan="3">
						${_Instance.instanceTitle}
					</td>
				</tr>
			</tbody>
			
				<c:if test="${!empty _Attachment_Names}">
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 70px;">
						<td class="ui-state-default jqgrid-rownum" style="width: 15%">附件信息：</td>
						<td colspan="3"><c:forEach var="file" items="${_Attachment_Names}" varStatus="status"><a href="<c:url value="${'/common/'}"/>download.jsp?filepath=${_Attachments[status.index]}"><span color="red">${file}</span></a><br/></c:forEach></td>
					</tr>
				</c:if>
				
				<c:if test="${!empty _Instance.manager || !empty _Instance.viceManager}">
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 70px;">
						<td class="ui-state-default jqgrid-rownum" style="width: 15%">部门审核：</td>
						<td colspan="3"><ul id="lis"><c:if test="${_Instance.managerChecked||(!empty _Instance.manager&&empty _Instance.submiterWord&&empty _Instance.startTime)}"><li class="liDetail2">${_Instance.manager.person.personName}：<c:choose><c:when test="${_Instance.managerChecked}">${_Instance.managerWord}&nbsp;&nbsp;(<fmt:formatDate value="${_Instance.checkTime}" pattern="yyyy-MM-dd HH:mm:ss"/>)</c:when><c:otherwise><font color="red">暂未审核</font></c:otherwise></c:choose><br/><c:forEach var="file" items="${_ManagerAttachment_Names}" varStatus="status"><a href="<c:url value="${'/common/'}"/>download.jsp?filepath=${_ManagerAttachments[status.index]}"><span color="red">${file}</span></a><br/></c:forEach></li></c:if><c:if test="${_Instance.viceManagerChecked||(!empty _Instance.viceManager&&empty _Instance.submiterWord&&empty _Instance.startTime)}"><li class="liDetail2">${_Instance.viceManager.person.personName}：<c:choose><c:when test="${_Instance.viceManagerChecked}">${_Instance.viceManagerWord}&nbsp;&nbsp;(<fmt:formatDate value="${_Instance.viceCheckTime}" pattern="yyyy-MM-dd HH:mm:ss"/>)</c:when><c:otherwise><font color="red">暂未审核</font></c:otherwise></c:choose><br/><c:forEach var="file" items="${_ViceManagerAttachment_Names}" varStatus="status"><a href="<c:url value="${'/common/'}"/>download.jsp?filepath=${_ViceManagerAttachments[status.index]}"><span color="red">${file}</span></a><br/></c:forEach></li></c:if><c:if test="${!empty _Instance.submiterWord}"><li class="liDetail2">中止意见：${_Instance.submiterWord}</li></c:if></ul></td>
					</tr>
				</c:if>
					
				<c:if test="${!empty _Instance.layers}">
					<c:forEach items="${_Instance.layers}" var="layer">
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 70px;" valign="top">
							<td class="ui-state-default jqgrid-rownum" valign="middle">${layer.layerName}<br/><c:if test="${_IsCharger}"><c:if test="${layer.status==0||layer.status==2}"><img src="<c:url value='/'/>images/edit.gif" border="0" onclick="editLayer('${layer.layerId}');" title="设定审核人" style="cursor: pointer;"/></c:if>&nbsp;<img src="<c:url value='/'/>images/delete.gif" border="0" style="cursor: pointer;" onclick="deleteLayer('${layer.layerId}');" title="删除"/></c:if></td>
							<td colspan="3"><c:if test="${!empty layer.checkDemand}"><b>审核要求</b>：${layer.checkDemand}<br/></c:if><br/><ul id="lis"><c:forEach items="${layer.checkInfors}" var="checkInfor"><c:if test="${checkInfor.status==1||(checkInfor.status==0&&(layer.status==0||layer.status==3)&&!_Instance.suspended&&empty _Instance.endTime)}"><li class="liDetail2"><strong>${checkInfor.checker.person.personName}：</strong>${checkInfor.checkComment}<c:if test="${!empty checkInfor.endDate}">&nbsp;&nbsp;(<fmt:formatDate value="${checkInfor.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>)</c:if><c:if test="${empty checkInfor.endDate}"><font color="red">暂未审核</font></c:if><c:if test="${_IsCharger}"><img src="<c:url value='/'/>images/edit.gif" border="0" onclick="editCheckInfor('${checkInfor.checkId}');" title="修改审核意见" style="cursor: pointer;"/></c:if><br/><c:if test="${empty layer.flowNode||(!empty layer.flowNode&&layer.flowNode.download)}"><attachment:fileView contextPath="">${checkInfor.attatchment}</attachment:fileView></c:if></li></c:if></c:forEach></ul></td>
						</tr>
					</c:forEach>
				</c:if>
		</table>
	</body>

