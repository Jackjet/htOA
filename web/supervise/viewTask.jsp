<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
<title>查看工作跟踪</title>
<link type="text/css" href="<c:url value="/"/>components/jqgrid/css/jquery.ui.all.css" rel="stylesheet" />
<%--<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />--%>

	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>css/base/jquery-ui-1.9.2.custom.css" />

<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
<link rel="stylesheet" type="text/css" media="screen" href="/css/noTdBottomBorder.css" />
<link rel="stylesheet" type="text/css" media="screen" href="/css/border.css" />

 <!--<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>jquery包-->
<script src="<c:url value='/js'/>/addattachment.js"></script>
<script src="<c:url value='/fckeditor'/>/fckeditor.js"></script>

<script src="<c:url value="/"/>datePicker/WdatePicker.js" type="text/javascript"></script>
<script src="<c:url value="/"/>components/jquery-1.4.2.js" type="text/javascript"></script>  <!--jquery包-->
<script src="<c:url value="/"/>components/jqgrid/js/jquery-ui-1.8.2.custom.min.js" type="text/javascript"></script><!--jquery ui-->
<script src="<c:url value="/"/>components/jquery.layout.js" type="text/javascript"></script> <!--jquery 布局-->

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
	.buttonclass {
		font-weight:bold;
	}
	input[type="button"],input[type="submit"],button{
		padding: 5px 5px 5px 5px;
		border: none;
		background-color: rgba(255, 255, 255, 0.2);
		color: white;
		box-shadow: none;
		border-radius: 5px;
		font-family: "微软雅黑";
		/*height: auto;!important;*/
	}
	html,
	body {

		overflow: hidden;!important;
		height: 100%;!important;
		margin: auto;!important;
		padding: auto;!important;
		font: 14px Georgia, Arial, Simsun;!important;
		background-image: url(/img/bgIn.png);!important;
		background-size: cover;!important;
		background-color: #e3e3e3;!important;
		overflow-y: visible;!important;

		font-family:  "黑体" ;!important;



	}
</style>
<script>

	//修改会议纪要
	function edit(taskId,categoryId){
		//var path;
		var form = document.superviseInforForm;
		form.action = "<c:url value='/supervise/superviseInfor.do'/>?method=edit&taskId="+taskId+"&categoryId="+categoryId;
		//form.submit();
		window.name = "__self";
		window.open(form.action, "__self"); //注意是2个下划线 	
		//path = "<c:url value='/supervise/superviseInfor.do'/>?method=saveView&taskId="+taskId;
		//window.location.href = path;		
	}
	
	//初始化
	function init() {		
		
		$("#setOperatorBody").css("display","none");
		$("#setOperatorBtn").css("display","");
		$("#saveOperatorBtn").css("display","none");
	}
	
	//指定负责人
	function display() {
		$("#setOperatorBody").css("display","");
		$("#setOperatorBtn1").css("display","none");
		$("#saveOperatorBtn").css("display","");
	}
	
	function unDisplay() {
		init();
	}
	
	//保存指定的负责人
	function saveOperator(){
		//var path;
		var form = document.superviseInforForm;
		form.action = "<c:url value='/supervise/superviseInfor.do'/>?method=saveOperator";
		form.submit();
		
		//window.name = "__self";
		//window.open(form.action, "__self"); //注意是2个下划线 	
		
		//window.returnValue = "Y";
		//window.close();		
	}
	
	
	function commonFun(path) {
		window.name = "__self";
		window.open(path, "__self");
	}
	
	//进度报告
	function editReport(parentId,reportId,taskId,reportType){
		//alert(parentId+"--"+reportId+"--"+taskId+"--"+reportType);
		var path = "<c:url value='/supervise/superviseReport.do'/>?method=edit&reportId="+reportId+"&taskId=" + taskId+"&reportType="+reportType+"&parentId="+parentId;
		commonFun(path);
	}
	
	//再次提交完成报告
	function reEditReport(taskId,reportType){
		//alert(parentId+"--"+reportId+"--"+taskId+"--"+reportType);
		var path = "<c:url value='/supervise/superviseReport.do'/>?method=edit&reportId=&taskId=" + taskId+"&reportType="+reportType+"&tag=re";
		commonFun(path);
	}
	
	//审核进度报告
	function checkReport(reportId,reportType){
		var path = "<c:url value='/supervise/superviseReport.do'/>?method=checkReport&reportId="+reportId+"&reportType="+reportType;
		commonFun(path);
	}
	
	//预判完成报告
	function judgeReport(reportId){
		var path = "<c:url value='/supervise/superviseReport.do'/>?method=judgeReport&reportId="+reportId;
		commonFun(path);
	}
	
	//选择领导
	function chooseLeader(taskId){
		var path = "<c:url value='/supervise/superviseInfor.do'/>?method=chooseLeader&taskId="+taskId;
		commonFun(path);
	}
	
	//审批打分
	function nameScore(taskId,reportType){
		var path = "<c:url value='/supervise/superviseReport.do'/>?method=nameScore&taskId="+taskId+"&reportType="+reportType;
		commonFun(path);
	}
	
	//行政/党群评价工作跟踪
	function checkAdvice(taskId,reportType){
		var path = "<c:url value='/supervise/superviseReport.do'/>?method=checkAdvice&taskId="+taskId+"&reportType="+reportType;
		commonFun(path);
	}
	
	//设置延迟
	function setDelay(taskId,reportType){
		var path = "<c:url value='/supervise/superviseReport.do'/>?method=setDelay&taskId="+taskId+"&reportType="+reportType;
		commonFun(path);
	}
</script>
<base target="_self"/>
</head>
<body onload="init();">	 
<form:form commandName="superviseInforVo" name="superviseInforForm" id="superviseInforForm" action="" method="post" enctype="multipart/form-data">


<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 80%;margin:0 auto;">
<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%;">
  <div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
    <span class="ui-jqgrid-title">任务工作跟踪详情</span>
  </div>

	<div class="ui-jqgrid-bdiv" style="height: auto; width: 100%">
		<div style="position: relative;">
			<div>
				<table cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable" style="width: 100%">
					<%-- 工作跟踪实例信息 --%>			
					<%@include file="includeTask.jsp" %>
					
					<tbody id="setOperatorBody">
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 20px;">
							<td class="ui-state-default jqgrid-rownum" colspan=4><b>指定责任人</b></td>
						</tr>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum" >责任人：</td>
							<td valign="top" colspan=3>
								<form:select path="operatorId">
								<c:forEach items="${_OwnUsers}" var="user">
									<form:option value="${user.person.personId}">${user.person.personName}</form:option>
								</c:forEach>
								</form:select>
							</td>
						</tr>
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
										<c:if test="${_SuperviseInfor.creater.personId ==_SYSTEM_USER.personId || _SYSTEM_USER.userType == 1}">
											<td><input style="cursor: pointer;" class="buttonclass" type="button" value="修改工作跟踪内容" onclick="edit('${_SuperviseInfor.taskId}','${_SuperviseInfor.taskCategory.categoryId}');"/>&nbsp;&nbsp;</td>
										</c:if>
										<!-- _SuperviseInfor.status == 1 &&  -->
										<%--<c:if test="${_SuperviseInfor.manager.personId == _SYSTEM_USER.personId && _SuperviseInfor.status == null}">
											--%>
										<c:if test="${_SuperviseInfor.manager.personId == _SYSTEM_USER.personId && _SuperviseInfor.status < 7}">
											<c:choose>
												<c:when test="${!empty _SuperviseInfor.operator}">
													<td><input style="cursor: pointer;"  class="buttonclass" id="setOperatorBtn1" type="button" value="重新指定责任人" onclick="display();"/>&nbsp;&nbsp;</td>
												</c:when>
												<c:otherwise>
													<td><input style="cursor: pointer;"  class="buttonclass" id="setOperatorBtn1" type="button" value="指定责任人" onclick="display();"/>&nbsp;&nbsp;</td>
												</c:otherwise>
											</c:choose>
											<td><input style="cursor: pointer;display: none" class="buttonclass" id="saveOperatorBtn" type="button" value="保存责任人" onclick="saveOperator();"/>&nbsp;&nbsp;</td>
										</c:if>
										
										<c:if test="${_SuperviseInfor.status == 2 && _SuperviseInfor.operator.personId == _SYSTEM_USER.personId && _CanSubmitReport && !_CanSubmitFianlReport && !_CanSubmitDelay}">
											<td><input style="cursor: pointer;" class="buttonclass" id="setOperatorBtn" type="button" value="填写月进度报告" onclick="editReport('','','${_SuperviseInfor.taskId}',1);"/>&nbsp;&nbsp;</td>
										</c:if>
										
										<c:if test="${_SuperviseInfor.status == 2 && _SuperviseInfor.operator.personId == _SYSTEM_USER.personId && _CanSubmitFianlReport && _CanSubmitReportfinal}">
											<td><input style="cursor: pointer;" class="buttonclass" id="setOperatorBtn" type="button" value="填写工作完成报告" onclick="editReport('','','${_SuperviseInfor.taskId}',2);"/>&nbsp;&nbsp;</td>
										</c:if>
										
										<c:if test="${_CanChooseLeaders}">
											<td><input style="cursor: pointer;" class="buttonclass" id="setOperatorBtn" type="button" value="选择领导评价" onclick="chooseLeader('${_SuperviseInfor.taskId}');"/>&nbsp;&nbsp;</td>
											<td><input style="cursor: pointer;" class="buttonclass" id="setOperatorBtn" type="button" value="工作跟踪自我评价" onclick="checkAdvice('${_SuperviseInfor.taskId}',6);"/>&nbsp;&nbsp;</td>
										</c:if>
										
										<c:if test="${_CanNameScore}">
											<td><input style="cursor: pointer;" class="buttonclass" id="setOperatorBtn" type="button" value="领导审批" onclick="nameScore('${_SuperviseInfor.taskId}',5);"/>&nbsp;&nbsp;</td>
										</c:if>
										
										<c:if test="${_CanDelay}">
											<td><input style="cursor: pointer;" class="buttonclass" id="setOperatorBtn" type="button" value="设置延迟时间" onclick="setDelay('${_SuperviseInfor.taskId}',4);"/>&nbsp;&nbsp;</td>
										</c:if>
										
										<c:if test="${_CanSubmitDelay}">
											<td><input style="cursor: pointer;" class="buttonclass" id="setOperatorBtn" type="button" value="再次提交完成报告" onclick="reEditReport('${_SuperviseInfor.taskId}',2);"/>&nbsp;&nbsp;</td>
										</c:if>
										
										<td><input style="cursor: pointer;" class="buttonclass" type="button" value="关闭" onclick="window.close();"/></td>
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
