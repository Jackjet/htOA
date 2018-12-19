<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

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
		
		var tblGrid = document.getElementById("tblGrid");
		
		//审核信息"经办部门意见","领导批示"或"信息反馈","相关董监事意见"所在行的下标
		var rowIndexOne = 0;
		var rowIndexTwo = 0;
		var rowIndexThree = 0;
		
		//遍历tblGrid下的所有row,以确定各个rowspan的值
		var hasDepCheck = false;
		var hasSubmitDepCheck = false;
		for (var i=0;i<tblGrid.rows.length;i++) {
			if (tblGrid.rows[i].cells[0].innerHTML.indexOf('经办部门意见') > -1) {
				rowIndexOne = i;
				hasSubmitDepCheck = true;
			}else if ((tblGrid.rows[i].cells[0].innerHTML.indexOf('相关董监事和部门意见') > -1||tblGrid.rows[i].cells[0].innerHTML.indexOf('相关部门和董监事意见') > -1) && !hasSubmitDepCheck) {
				//审核阶段中不包含"经办部门意见"时才执行该操作
				rowIndexOne = i;
			}
			if (tblGrid.rows[i].cells[0].innerHTML.indexOf('信息反馈') > -1) {
				rowIndexTwo = i;
			}else if (tblGrid.rows[i].cells[0].innerHTML.indexOf('领导批示') > -1) {
				rowIndexTwo = i;
			}
			if (tblGrid.rows[i].cells[0].innerHTML.indexOf('相关董监事意见') > -1) {
				rowIndexThree = i;
			}
			if (tblGrid.rows[i].cells[0].innerHTML.indexOf('部门审核') > -1) {
				hasDepCheck = true;
			}
		}
		//alert(rowIndexOne);alert(rowIndexTwo);
		//插入左边列"议案初审","议案流转"
		for (var i=0;i<tblGrid.rows.length;i++) {
			if (tblGrid.rows[i].cells[0].innerHTML.indexOf('议案') > -1) {
				var oCell = tblGrid.rows[i].insertCell(0);
				oCell.innerHTML = "议案初审";
				oCell.style.width = "7%";
				if (rowIndexOne == 0) {
					oCell.setAttribute("rowspan",tblGrid.rows.length,"0");
				}else {
					if (hasDepCheck) {
						oCell.setAttribute("rowspan",rowIndexOne - 1,"0");
					}else {
						oCell.setAttribute("rowspan",rowIndexOne,"0");
					}
				}
			}else if (tblGrid.rows[i].cells[0].innerHTML.indexOf('经办部门意见') > -1) {
				var oCell = tblGrid.rows[i].insertCell(0);
				oCell.innerHTML = "议案流转";
				oCell.style.width = "7%";
				oCell.setAttribute("valign","middle","0");
				if (rowIndexThree == 0) {
					oCell.setAttribute("rowspan",tblGrid.rows.length-rowIndexOne,"0");
				}else {
					oCell.setAttribute("rowspan",rowIndexThree-rowIndexOne,"0");
				}
			}else if ((tblGrid.rows[i].cells[0].innerHTML.indexOf('相关董监事和部门意见') > -1||tblGrid.rows[i].cells[0].innerHTML.indexOf('相关部门和董监事意见') > -1) && !hasSubmitDepCheck) {
				//审核阶段中不包含"经办部门意见"时才执行该操作
				var oCell = tblGrid.rows[i].insertCell(0);
				oCell.innerHTML = "议案流转";
				oCell.style.width = "7%";
				oCell.setAttribute("valign","middle","0");
				if (rowIndexThree == 0) {
					oCell.setAttribute("rowspan",tblGrid.rows.length-rowIndexOne,"0");
				}else {
					oCell.setAttribute("rowspan",rowIndexThree-rowIndexOne,"0");
				}
			}
		}
		
		//插入左边列"决议流转",及董事会决议相关信息
		if (rowIndexTwo > 0) {
			var insertRow = tblGrid.insertRow(rowIndexTwo+1);//插入到"领导批示"或"信息反馈"后
			insertRow.style.height = "30px";
				
			var oCell = insertRow.insertCell(0);
			oCell.innerHTML = "决议流转";
			oCell.setAttribute("rowspan",3,"0");
				
			oCell = insertRow.insertCell(1);
			oCell.innerHTML = "董事会决议";
				
			oCell = insertRow.insertCell(2);
			var attchmentStr = "";
			<c:forEach var="file" items="${_ResAttachment_Names}" varStatus="status">
				attchmentStr += "<a href='../common/download.jsp?filepath=${_ResAttachments[status.index]}'><span color='red'>${file}</span></a><br/>";
			</c:forEach>
			oCell.innerHTML = attchmentStr;
			oCell.setAttribute("colspan",2,"0");
				
			insertRow = tblGrid.insertRow(rowIndexTwo+2);//插入到"领导批示"或"信息反馈"后第二行
			insertRow.style.height = "30px";
					
			oCell = insertRow.insertCell(0);
			oCell.innerHTML = "备注";
				
			oCell = insertRow.insertCell(1);
			oCell.innerHTML = "${_Instance.attachMemo}";
			oCell.setAttribute("colspan",2,"0");
		}
		
		/** 插入分隔行 */
		//分隔行一
		insertRow = tblGrid.insertRow(0);
		insertRow.style.height = "10px";
		oCell = insertRow.insertCell(0);
		oCell.setAttribute("colspan",4,"0");
		if (rowIndexOne > 0) {
			//分隔行二
			insertRow = tblGrid.insertRow(rowIndexOne+1);
			insertRow.style.height = "10px";
			oCell = insertRow.insertCell(0);
			oCell.setAttribute("colspan",4,"0");
		}
		//分隔行三
		if (rowIndexTwo > 0) {
			insertRow = tblGrid.insertRow(rowIndexTwo+2+1);
			insertRow.style.height = "10px";
			oCell = insertRow.insertCell(0);
			oCell.setAttribute("colspan",4,"0");
		}
		/****/
	});
	
	</script>

	<body>
		<table width="90%" border="0" align="center" cellpadding="5" cellspacing="0">

			<tr id="head" style="height: 80px;">
				<th colspan="4" class="TotCatalogHead">
					<script>
						var titleString = "上港集团物流有限公司";
						titleString += "<br/>董事会文件处理单";
						document.write(titleString);
					</script>
				</th>
			</tr>	
			
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
			
			<tbody id="tblGrid">
			
				<c:if test="${!empty _Instance.manager || !empty _Instance.viceManager}">
					<tr style="height: 30px;">
						<td style="width: 15%;">部门审核：</td>
						<td colspan="3"><ul id="lis"><c:if test="${_Instance.managerChecked||(!empty _Instance.manager&&empty _Instance.submiterWord&&empty _Instance.startTime)}"><li class="liDetail2">${_Instance.manager.person.personName}：<c:choose><c:when test="${_Instance.managerChecked}">${_Instance.managerWord}&nbsp;&nbsp;(<fmt:formatDate value="${_Instance.checkTime}" pattern="yyyy-MM-dd HH:mm:ss"/>)</c:when><c:otherwise><font color="red">暂未审核</font></c:otherwise></c:choose><br/><c:forEach var="file" items="${_ManagerAttachment_Names}" varStatus="status"><a href="<c:url value="${'/common/'}"/>download.jsp?filepath=${_ManagerAttachments[status.index]}"><span color="red">${file}</span></a><br/></c:forEach></li></c:if><c:if test="${_Instance.viceManagerChecked||(!empty _Instance.viceManager&&empty _Instance.submiterWord&&empty _Instance.startTime)}"><li class="liDetail2">${_Instance.viceManager.person.personName}：<c:choose><c:when test="${_Instance.viceManagerChecked}">${_Instance.viceManagerWord}&nbsp;&nbsp;(<fmt:formatDate value="${_Instance.viceCheckTime}" pattern="yyyy-MM-dd HH:mm:ss"/>)</c:when><c:otherwise><font color="red">暂未审核</font></c:otherwise></c:choose><br/><c:forEach var="file" items="${_ViceManagerAttachment_Names}" varStatus="status"><a href="<c:url value="${'/common/'}"/>download.jsp?filepath=${_ViceManagerAttachments[status.index]}"><span color="red">${file}</span></a><br/></c:forEach></li></c:if><c:if test="${!empty _Instance.submiterWord}"><li class="liDetail2">中止意见：${_Instance.submiterWord}</li></c:if></ul></td>
					</tr>
				</c:if>
				
				<c:if test="${!empty _Attachment_Names}">
					<tr style="height: 30px;">
						<td style="width: 15%;">议案</td>
						<td colspan="2"><c:forEach var="file" items="${_Attachment_Names}" varStatus="status"><a href="<c:url value="${'/common/'}"/>download.jsp?filepath=${_Attachments[status.index]}"><span color="red">${file}</span></a><br/></c:forEach></td>
					</tr>
				</c:if>
				
				<c:if test="${!empty _Instance.layers}">
					<c:forEach items="${_Instance.layers}" var="layer">
						<tr style="height: 30px;" valign="top">
							<td valign="middle">${layer.layerName}<br/><c:if test="${_IsCharger}"><c:if test="${layer.status==0||layer.status==2}"><img src="<c:url value='/'/>images/edit.gif" border="0" onclick="editLayer('${layer.layerId}');" title="设定审核人" style="cursor: pointer;"/></c:if>&nbsp;<img src="<c:url value='/'/>images/delete.gif" border="0" style="cursor: pointer;" onclick="deleteLayer('${layer.layerId}');" title="删除"/></c:if></td>
							<td colspan="2"><c:if test="${!empty layer.checkDemand}"><b>审核要求</b>：${layer.checkDemand}<br/></c:if><br/><ul id="lis"><c:forEach items="${layer.checkInfors}" var="checkInfor"><c:if test="${checkInfor.status==1||(checkInfor.status==0&&(layer.status==0||layer.status==3)&&!_Instance.suspended&&empty _Instance.endTime)}"><li class="liDetail2"><strong>${checkInfor.checker.person.personName}：</strong>${checkInfor.checkComment}<c:if test="${!empty checkInfor.endDate}">&nbsp;&nbsp;(<fmt:formatDate value="${checkInfor.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>)</c:if><c:if test="${empty checkInfor.endDate}"><font color="red">暂未审核</font></c:if><c:if test="${_IsCharger}"><img src="<c:url value='/'/>images/edit.gif" border="0" onclick="editCheckInfor('${checkInfor.checkId}');" title="修改审核意见" style="cursor: pointer;"/></c:if><br/><c:if test="${empty layer.flowNode||(!empty layer.flowNode&&layer.flowNode.download)}"><attachment:fileView contextPath="">${checkInfor.attatchment}</attachment:fileView></c:if></li></c:if></c:forEach></ul></td>
						</tr>
					</c:forEach>
				</c:if>
				
				<c:if test="${!empty _Instance.endTime}">
					<c:if test="${!empty _FormalAttachment_Names}">
						<tr height="10px;">
							<td colspan="4"></td>
						</tr>
						
						<tr style="height: 30px;">
							<td style="width: 15%">决议归档：</td>
							<td colspan="3"><c:forEach var="file" items="${_FormalAttachment_Names}" varStatus="status"><a href="<c:url value="${'/common/'}"/>download.jsp?filepath=${_FormalAttachments[status.index]}"><span color="red">${file}</span></a><br/></c:forEach></td>
						</tr>
					</c:if>
				</c:if>
				
			</tbody>
			
		</table>
	</body>

