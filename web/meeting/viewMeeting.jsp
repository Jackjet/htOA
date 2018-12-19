<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
<title>查看会议信息</title>
<link type="text/css" href="<c:url value="/"/>components/jqgrid/css/jquery.ui.all.css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
<link rel="stylesheet" type="text/css" media="screen" href="/css/noTdBottomBorder.css" />
<link rel="stylesheet" type="text/css" media="screen" href="/css/border.css" />

<script src="<c:url value='/js'/>/addattachment.js"></script>
<script src="<c:url value='/fckeditor'/>/fckeditor.js"></script>
<style type="text/css">
	#lis {
		padding:0;
		margin:5px;
	}
	#lis .liDetail {
		list-style:none;
		margin-left:20px;
	}
	#lis .liDetail2 {
		list-style:none;
		margin-left:20px;
		height: 30px;
	}
	#lis .liDetail3 {
		list-style:none;
		margin-left:18px;
	}
</style>
<script>

	function init() {		
		/**
		var summary = document.getElementById("summary");
		var summaryId = document.getElementById("summaryId");
		var submitButton = document.getElementById("submit");
		*/
		var summary = document.getElementById("summary");
		var summaryId = document.getElementById("summaryId");
		var add = document.getElementById("add");
		var savebutton = document.getElementById("savebutton");
		//alert (savebutton);
		var att = document.getElementById("att");
		var attId = document.getElementById("attId");
		var attView = document.getElementById("attView");
		var attViewId = document.getElementById("attViewId");
		
		summary.style.display="none";
		summaryId.style.display="none";
		if(savebutton == null){
		}else{
		savebutton.style.display="none"
		}
		att.style.display="none";
		attId.style.display="none";
		if(attView == null){
		}else{
		attView.style.display="none";
		}
		if(attViewId == null){
		}else{
		attViewId.style.display="none";
		}	
	}
	
	function display() {
		
		//var add2 = document.getElementById("add2");
		var summary = document.getElementById("summary");
		var summaryId = document.getElementById("summaryId");
		var add = document.getElementById("add");
		 var savebutton = document.getElementById("savebutton");
		var att = document.getElementById("att");
		var attId = document.getElementById("attId");
		var attView = document.getElementById("attView");
		var attViewId = document.getElementById("attViewId");
		
		summary.style.display="";
		summaryId.style.display="";
		savebutton.style.display="";
		att.style.display="";
		attId.style.display="";
		add.style.display="none";
		if(attView == null){
		}else{
		attView.style.display="";
		}
		if(attViewId == null){
		}else{
		attViewId.style.display="";
		}
	}
	
	function unDisplay() {
		init();
	}
	
	//保存会议纪要
	function save(){
		//var path;
		var form = document.meetInforForm;
		form.action = "<c:url value='/meeting/meetInfor.do'/>?method=saveView";
		form.submit();
		//path = "<c:url value='/meeting/meetInfor.do'/>?method=saveView&meetId="+meetId;
		//window.location.href = path;	
		//window.name = "__self";
		//window.open(form.action, "__self"); //注意是2个下划线 	
		window.returnValue = "Y";
		window.close();	
	}
	//修改会议纪要
	
	function edit(meetId){
		//var path;
		var form = document.meetInforForm;
		form.action = "<c:url value='/meeting/meetInfor.do'/>?method=edit&meetId="+meetId;
		//form.submit();
		window.name = "__self";
		window.open(form.action, "__self"); //注意是2个下划线 	
		//path = "<c:url value='/meeting/meetInfor.do'/>?method=saveView&meetId="+meetId;
		//window.location.href = path;		
	}
	
</script>
<base target="_self"/>
</head>
<body style="overflow-y: auto;padding: 0 100px"  onload="init();">
<br/>
<form:form commandName="meetInforVo" name="meetInforForm" id="meetInforForm" action="" method="post" enctype="multipart/form-data">

<input type="hidden" name="meetId" value="${_Meet.meetId}"/>
<input type="hidden" name="summaryAttach" value="${_Meet.summaryAttach}"/>
<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 100%">
<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
  <div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
    <span class="ui-jqgrid-title">查看会议信息</span>
  </div>

	<div class="ui-jqgrid-bdiv" style="height: auto; width: 100%">
		<div style="position: relative;">
			<div >

				<table cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable" style="width: 100%">
					<tbody>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td style="width: 15%;" class="ui-state-default jqgrid-rownum">会议名称：</td>
							<td style="width: 85%;">${_Meet.meetName}</td>
						</tr>
								
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td style="width: 15%;" class="ui-state-default jqgrid-rownum">主持人：</td>
							<td style="width: 85%;">${_Meet.compere}</td>
						</tr>	
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td style="width: 15%;" class="ui-state-default jqgrid-rownum">所属部门：</td>
							<td style="width: 85%;">${_Meet.organize.organizeName}</td>
						</tr>	
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td style="width: 15%;" class="ui-state-default jqgrid-rownum">会议地点：</td>
							<td style="width: 85%;">${_Meet.meetRoom}</td>
						</tr>				
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">会议时间：</td>
							<td><c:choose><c:when test="${empty _Meet.endMeetDate}">${_Meet.meetDate} <c:choose><c:when test="${_Meet.endHour!=0||_Meet.startHour!=0||_Meet.endHour!=0||_Meet.endMinutes!=0}"><c:choose><c:when test="${_Meet.startHour==0}">00</c:when><c:otherwise>${_Meet.startHour}</c:otherwise></c:choose>:<c:choose><c:when test="${_Meet.startMinutes==0}">00</c:when><c:otherwise>${_Meet.startMinutes}</c:otherwise></c:choose> - <c:choose><c:when test="${_Meet.endHour==0}">00</c:when><c:otherwise>${_Meet.endHour}</c:otherwise></c:choose>:<c:choose><c:when test="${_Meet.endMinutes==0}">00</c:when><c:otherwise>${_Meet.endMinutes}</c:otherwise></c:choose></c:when></c:choose></c:when><c:otherwise>${_Meet.meetDate}<c:choose><c:when test="${_Meet.endHour!=0||_Meet.startHour!=0||_Meet.endHour!=0||_Meet.endMinutes!=0}">&nbsp;<c:choose><c:when test="${_Meet.startHour==0}">00</c:when><c:otherwise>${_Meet.startHour}</c:otherwise></c:choose>:<c:choose><c:when test="${_Meet.startMinutes==0}">00</c:when><c:otherwise>${_Meet.startMinutes}</c:otherwise></c:choose></c:when></c:choose> 至 ${_Meet.endMeetDate}<c:choose><c:when test="${_Meet.endHour!=0||_Meet.startHour!=0||_Meet.endHour!=0||_Meet.endMinutes!=0}">&nbsp;<c:choose><c:when test="${_Meet.endHour==0}">00</c:when><c:otherwise>${_Meet.endHour}</c:otherwise></c:choose>:<c:choose><c:when test="${_Meet.endMinutes==0}">00</c:when><c:otherwise>${_Meet.endMinutes}</c:otherwise></c:choose></c:when></c:choose></c:otherwise></c:choose></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td style="width: 15%;" class="ui-state-default jqgrid-rownum">与会人员：</td>
							<td style="width: 85%;">${_Meet.attendInfor}</td>
						</tr>			
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 80px;">
							<td class="ui-state-default jqgrid-rownum" >会议内容：</td>
							<td valign="top" >${_Meet.content}</td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 80px;">
							<td class="ui-state-default jqgrid-rownum" >会议要求：</td>
							<td valign="top" >${_Meet.demand}</td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">附件信息：</td>
							<td align="left"><c:forEach var="file" items="${_Attachment_Names}" varStatus="status"><a href="<c:url value="${'/common/'}"/>download.jsp?filepath=${_Attachments[status.index]}"><font color="red">${file}</font></a><br/></c:forEach></td>
						</tr>
						<c:if test="${_Meet.recordUser.personId != null}">
							<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
								<td style="width: 15%;" class="ui-state-default jqgrid-rownum">会议记录者：</td>
								<td style="width: 85%;">${_Meet.recordUser.person.personName}</td>
							</tr>	
						</c:if>
						
						<c:if test="${_Meet.summary != null}">
							<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
								<td style="width: 15%;" class="ui-state-default jqgrid-rownum">会议纪要：</td>
								<td style="width: 85%;">${_Meet.summary}</td>
							</tr>	
						</c:if>
						
						<c:if test="${!empty _Meet.summaryAttach}">
							<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
								<td style="width: 15%;" class="ui-state-default jqgrid-rownum">会议纪要附件：</td>
								<td style="width: 85%;"><c:forEach  var="file" items="${_Attachment_SummaryNames}"  varStatus="status"><a href="<c:url value="${'/common/'}"/>download.jsp?filepath=${_AttachmentsSummary[status.index]}"><font color="red">${file}</font></a><br/></c:forEach></td>
							</tr>	
						</c:if>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr">
								<td id="summaryId" style="width: 15%;" class="ui-state-default jqgrid-rownum">会议纪要：</td>
								<td id="summary" style="width: 85%;"><form:textarea path="summary" cols="50" rows="5"/>
								<%--<FCK:editor id="summary" basePath="/fckeditor/" width="550" height="400" skinPath="/fckeditor/editor/skins/default/" toolbarSet="Default">
									${_Meet.summary}		
								</FCK:editor>	
								--%></td>
						</tr>	
						
						<tr>
							<td id="att" style="width: 15%;" class="ui-state-default jqgrid-rownum">
								附件：
							</td>
							<td id="attId">
								<table cellpadding="0" cellspacing="0" style="margin-bottom:0;margin-top:0">
									<tr>
										<td>
											<input type="file" name="attatchment" size="50" />
											&nbsp;
											<input type="button" value="更多附件.." onclick="addtable('newstyle')"
												class="bt" />
										</td>
									</tr>
								</table>
								<span id="newstyle"></span>
							</td>
						</tr>
						<c:if test="!empty${meetInforVo.summaryAttachStr}">
							<tr>
								<td id="attView" colspan="2" valign="top">
									原附件信息(
									<font color=red>如果要删除某个附件，请选择该附件前面的选择框</font>)：
								</td>
							</tr>
							<tr>
								<td id="attViewId" colspan="2">
									<c:forEach var="file" items="${_Attachment_SummaryNames}"
										varStatus="index">
										<form:checkbox path="summaryAttachArray" value="${index.index}" />
										<a href="<%=request.getRealPath("/")%>${file}">${_Attachment_SummaryNames[index.index]}
										</a>
										<br/>
									</c:forEach>
								</td>
							</tr>
						</c:if>
					</tbody>
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
										<c:if test="${_Meet.recordUser.personId ==_SYSTEM_USER.personId}">
											<td><input id="add" type="button" style="cursor: pointer;" value='会议纪要' class="bt" onclick="display()"/></td>
											
											<td><input id="savebutton" style="cursor: pointer;" type="button" value="保存纪要" onclick="save();"/></td>
											
										</c:if>
										<c:if test="${_Meet.author.personId ==_SYSTEM_USER.personId || _SYSTEM_USER.userType == 1}">
											<td><input style="cursor: pointer;" type="button" value="修改" onclick="edit('${_Meet.meetId}');"/></td>
										</c:if>
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
</form:form>
</body>
</html>
