<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
<title>查看活动信息</title>
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

	
	function edit(actId){
		//var path;
		var form = document.clubInforForm;
		form.action = "<c:url value='/club/clubInfor.do'/>?method=edit&actId="+actId;
		//form.submit();
		window.name = "__self";
		window.open(form.action, "__self"); //注意是2个下划线 	
		//path = "<c:url value='/club/clubInfor.do'/>?method=saveView&actId="+actId;
		//window.location.href = path;		
	}
	
	function changeStatus(actId,status){
		var form = document.clubInforForm;
		form.action = "<c:url value='/club/clubInfor.do'/>?method=changeStatus&actId="+actId+"&status="+status;
		window.name = "__self";
		window.open(form.action, "__self"); //注意是2个下划线 	
	}
	
</script>
<base target="_self"/>
</head>

<body onload="init();" style="overflow-y: auto;padding: 0 100px" onload="">
<br/>
<form:form commandName="clubInforVo" name="clubInforForm" id="clubInforForm" action="" method="post" enctype="multipart/form-data">

<input type="hidden" name="actId" value="${_ClubInfor.actId}"/>
<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 100%">
<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
  <div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
    <span class="ui-jqgrid-title">查看活动信息</span>
    &nbsp;&nbsp;&nbsp;&nbsp;
    <font color=black></font>
    
  </div>

	<div class="ui-jqgrid-bdiv" style="height: auto; width: 100%">
		<div style="position: relative;">
			<div>
				<table cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable" style="width: 100%">
					<tbody>
								
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td style="width: 15%;" class="ui-state-default jqgrid-rownum">状态：</td>
							<td style="width: 85%;">
								<font color=red><b>
							    	<c:choose>
							    		<c:when test="${_ClubInfor.status == 0}">
							    			暂停
							    		</c:when>
							    		<c:when test="${_ClubInfor.status == 1}">
							    			未开始报名
							    		</c:when>
							    		<c:when test="${_ClubInfor.status == 2}">
							    			报名中
							    		</c:when>
							    		<c:when test="${_ClubInfor.status == 3}">
							    			报名截止，未开始
							    		</c:when>
							    		<c:when test="${_ClubInfor.status == 4}">
							    			开始签到
							    		</c:when>
							    		<c:when test="${_ClubInfor.status == 5}">
							    			活动结束<font color=black>（结束时间：${_ClubInfor.endTime}）</font>
							    		</c:when>
							    	</c:choose>
							    	</b>
							    </font>
							</td>
						</tr>	
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td style="width: 15%;" class="ui-state-default jqgrid-rownum">活动名称：</td>
							<td style="width: 85%;">${_ClubInfor.actTitle}</td>
						</tr>
								
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td style="width: 15%;" class="ui-state-default jqgrid-rownum">活动项目：</td>
							<td style="width: 85%;">${_ClubInfor.actItem}</td>
						</tr>	
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td style="width: 15%;" class="ui-state-default jqgrid-rownum">活动管理员：</td>
							<td style="width: 85%;">${_ClubInfor.manager.person.personName}</td>
						</tr>	
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">报名时间：</td>
							<td>
								<fmt:formatDate value="${_ClubInfor.beginSignDate}" pattern="yyyy-MM-dd"/> 
								<b>至</b>  <fmt:formatDate value="${_ClubInfor.cutDate}" pattern="yyyy-MM-dd"/>
							</td>
						</tr>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td style="width: 15%;" class="ui-state-default jqgrid-rownum">活动时间：</td>
							<td style="width: 85%;">
								<fmt:formatDate value="${_ClubInfor.actTime}" pattern="yyyy-MM-dd HH:mm:00"/>  
								<b>至</b>  <fmt:formatDate value="${_ClubInfor.toTime}" pattern="yyyy-MM-dd HH:mm:00"/> 
							</td>
						</tr>				
						
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td style="width: 15%;" class="ui-state-default jqgrid-rownum">活动地点：</td>
							<td style="width: 85%;">${_ClubInfor.actPlace}</td>
						</tr>			
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 80px;">
							<td class="ui-state-default jqgrid-rownum" >报名方法：</td>
							<td valign="top" >${_ClubInfor.registerWay}</td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 80px;">
							<td class="ui-state-default jqgrid-rownum" >活动规则：</td>
							<td valign="top" >${_ClubInfor.actRule}</td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 80px;">
							<td class="ui-state-default jqgrid-rownum" >备注：</td>
							<td valign="top" >${_ClubInfor.memo}</td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">活动主图：</td>
							<td align="left"><c:forEach var="file" items="${_Attachment_Names}" varStatus="status"><a href="<c:url value="${'/common/'}"/>download.jsp?filepath=${_Attachments[status.index]}"><font color="red">${file}</font></a><br/></c:forEach></td>
						</tr>
						
						<!-- 发起者或管理员可看到二维码，非前两者在开始后可看到 -->
						<c:if test="${(_ClubInfor.creater.personId == _SYSTEM_USER.personId || _ClubInfor.manager.personId == _SYSTEM_USER.personId || _SYSTEM_USER.userType == 1) && (_ClubInfor.status > 1)}">
							<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 80px;">
								<td class="ui-state-default jqgrid-rownum" >签到二维码：</td>
								<td valign="top" >
									<c:if test="${!empty _ClubInfor.twoPic}">
										<img src="<c:url value="/"/>${_ClubInfor.twoPic}" />
										<a href="<c:url value="${'/common/'}"/>download.jsp?filepath=${_ClubInfor.twoPic}"><font color="red">下载二维码</font></a>
									</c:if>
									
								</td>
							</tr>
						</c:if>
						
						<!-- 发起者或管理员可看到参与情况 -->
						<c:if test="${(_ClubInfor.creater.personId == _SYSTEM_USER.personId || _ClubInfor.manager.personId == _SYSTEM_USER.personId || _SYSTEM_USER.userType == 1)}">
							<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 80px;">
								<td class="ui-state-default jqgrid-rownum" >参与情况：</td>
								<td valign="top" align=left >
									<div style="float:left;"><br/>
										<font color=green><b>报名情况：</b></font><br/><br/>
										<table cellpadding="6" cellspacing="1"  style="float:left;">
			                      			<tr style="font-weight:bold;"><!--  bgcolor="#FFFFFF" -->
			                      				<td align=center>&nbsp;&nbsp;&nbsp;&nbsp;序号&nbsp;&nbsp;&nbsp;&nbsp;</td>
			                      				<td align=center>&nbsp;&nbsp;&nbsp;&nbsp;报名人&nbsp;&nbsp;&nbsp;&nbsp;</td>
			                      				<td align=center>&nbsp;&nbsp;&nbsp;&nbsp;报名时间&nbsp;&nbsp;&nbsp;&nbsp;</td>
			                      			</tr>
			                      			<c:forEach items="${_ClubInfor.registers}" var="reg" varStatus="index">
			                      				<tr >
				                      				<td align=center>${index.index+1}</td>
				                      				<td align=center>${reg.reger.person.personName}</td>
				                      				<td align=center>${reg.regTime}</td>
			                      				</tr>
			                      			</c:forEach>
			                      		</table>
									</div>
									
									<div style="float:left;margin-left:30px;"><br/>
										<font color=green><b>签到情况：</b></font><br/><br/>
										<table cellpadding="6" cellspacing="1"  >
			                      			<tr style="font-weight:bold;">
			                      				<td align=center>&nbsp;&nbsp;&nbsp;&nbsp;序号&nbsp;&nbsp;&nbsp;&nbsp;</td>
			                      				<td align=center>&nbsp;&nbsp;&nbsp;&nbsp;签到人&nbsp;&nbsp;&nbsp;&nbsp;</td>
			                      				<td align=center>&nbsp;&nbsp;&nbsp;&nbsp;签到时间&nbsp;&nbsp;&nbsp;&nbsp;</td>
			                      				<td align=center>&nbsp;&nbsp;&nbsp;&nbsp;是否报过名&nbsp;&nbsp;&nbsp;&nbsp;</td>
			                      				<td align=center>&nbsp;&nbsp;&nbsp;&nbsp;签到地点&nbsp;&nbsp;<a target="_blank" href="http://api.map.baidu.com/lbsapi/getpoint/index.html"><font color=white><b>（点此查询坐标）</b></font></a>&nbsp;&nbsp;&nbsp;&nbsp;</td>
			                      			</tr>
			                      			<c:forEach items="${_ClubInfor.attends}" var="att" varStatus="index">
			                      				<tr >
				                      				<td align=center>${index.index+1}</td>
				                      				<td align=center>${att.attender.person.personName}</td>
				                      				<td align=center>${att.attTime}</td>
				                      				<td align=center>
				                      					<c:if test="${att.hasReged == 1}">是</c:if>
				                      					<c:if test="${att.hasReged == 0}"><font color="red">否</font></c:if>
				                      				</td>
				                      				<td align=center>${att.attLocation}</td>
			                      				</tr>
			                      			</c:forEach>
			                      		</table>
									</div>
												
		                      		
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
										<c:set var="canEdit" value="${_ClubInfor.creater.personId ==_SYSTEM_USER.personId || _ClubInfor.manager.personId ==_SYSTEM_USER.personId  || _SYSTEM_USER.userType == 1}" />
										<c:if test="${canEdit}">
											<td><input style="cursor: pointer;" type="button" value="修改" onclick="edit('${_ClubInfor.actId}');"/>&nbsp;&nbsp;&nbsp;&nbsp;</td>
										</c:if>
										
										<%-- <c:if test="${_ClubInfor.status > 0 && _ClubInfor.status < 5 && canEdit}">
											<td><input style="cursor: pointer;" type="button" value="暂停" onclick="changeStatus('${_ClubInfor.actId}',0);"/>&nbsp;&nbsp;&nbsp;&nbsp;</td>
										</c:if> --%>
										
										<c:if test="${_ClubInfor.status == 0 && canEdit}">
											<td><input style="cursor: pointer;" type="button" value="恢复初始状态" onclick="changeStatus('${_ClubInfor.actId}',1);"/>&nbsp;&nbsp;&nbsp;&nbsp;</td>
										</c:if>
										
										<c:if test="${_ClubInfor.status == 1 && canEdit}">
											<td><input style="cursor: pointer;" type="button" value="开始报名" onclick="changeStatus('${_ClubInfor.actId}',2);"/>&nbsp;&nbsp;&nbsp;&nbsp;</td>
										</c:if>
										
										<c:if test="${_ClubInfor.status == 2 && canEdit}">
											<td><input style="cursor: pointer;" type="button" value="报名截止" onclick="changeStatus('${_ClubInfor.actId}',3);"/>&nbsp;&nbsp;&nbsp;&nbsp;</td>
										</c:if>
										
										<c:if test="${_ClubInfor.status == 3 && canEdit}">
											<td><input style="cursor: pointer;" type="button" value="开始签到" onclick="changeStatus('${_ClubInfor.actId}',4);"/>&nbsp;&nbsp;&nbsp;&nbsp;</td>
										</c:if>
										
										<c:if test="${_ClubInfor.status == 4 && canEdit}">
											<td><input style="cursor: pointer;" type="button" value="活动结束" onclick="changeStatus('${_ClubInfor.actId}',5);"/>&nbsp;&nbsp;&nbsp;&nbsp;</td>
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
</form:form><br/><br/>
</body>
</html>
