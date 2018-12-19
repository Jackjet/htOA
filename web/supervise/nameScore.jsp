<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
<title>工作跟踪</title>
<script src="<c:url value="/"/>datePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
	<script src="<c:url value="/"/>js/inc_javascript.js"></script>
	<script src="<c:url value="/"/>js/addattachment.js"></script>
	
	<link rel="stylesheet" type="text/css" href="<c:url value="/"/>components/jqgrid/css/jquery.ui.all.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
	
	<!-- formValidator -->
	<link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css"></link>
	<script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript" charset="UTF-8"></script>
	<script src="<c:url value="/"/>components/formvalidator/js/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
	
	<!-- 打分
	<link type="text/css" rel="stylesheet" href="<c:url value="/"/>js/rate/rate-style.css"></link>
	<script src="<c:url value="/"/>js/rate/rate.js" type="text/javascript" charset="UTF-8"></script> -->
	
	<style type="text/css">
		.buttonclass {
			font-weight:bold;
		}	
	</style>
<script>
	$(document).ready(function(){
		//验证
		$.formValidator.initConfig({formid:"superviseReportForm",onerror:function(msg){alert(msg)},onsuccess:function(){}});
		//$("#managerAdvice").formValidator({onshow:"请输入审核意见",onfocus:"审核意见不能为空",oncorrect:"输入正确"}).inputValidator({min:1,onerror: "请输入审核意见"});
		$("#operateDate").formValidator({onshow:"请输入审核时间",onfocus:"审核时间不能为空",oncorrect:"输入正确"}).inputValidator({min:1,onerror: "请输入审核时间"});
//		$("#nameScore").formValidator({onshow:"请打分",onfocus:"请打分",oncorrect:"操作正确"}).inputValidator({min:1,onerror: "请打分"});
	});
	
	//保存审核
	/** function save(){
		var form = document.instanceCheckInforForm;
		form.action = "<c:url value='/workflow/checkInfor.do'/>?method=save";
		form.submit();
	} */
	
	
	//去掉初始化的提示信息
	//$("#managerAdviceTip").html("");
	$("#operateDateTip").html("");
    function display1() {

        $("#pingjia").css("display","");
    }
    function display2() {

        $("#pingjia").css("display","none");
    }

	
</script>
<base target="_self"/>
</head>
<body onload="window.scrollTo(0,document.body.scrollHeight); ">
	<form:form commandName="superviseReportVo" name="superviseReportForm" id="superviseReportForm" action="/supervise/superviseReport.do?method=saveNameScore" method="post" enctype="multipart/form-data">
		<form:hidden path="reportId"/>
		<form:hidden path="reportType"/>
		<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 85%;margin:0 auto;">
			<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%;">
  				<div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
    				<span class="ui-jqgrid-title">工作跟踪</span>
  				</div>

				<div class="ui-jqgrid-bdiv" style="height: auto; width: 100%">
					<div style="position: relative;">
						<div>
							<table cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable" style="width: 100%">
								<%-- 工作跟踪实例信息 --%>			
								<%@include file="includeTask.jsp" %>
					
								<tbody id="setOperatorBody">
									<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 20px;">
										<td class="ui-state-default jqgrid-rownum" colspan=4>
											<b><font color=red>领导审核</font></b><!-- 打分 -->
										</td>
									</tr>
									<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
										<td class="ui-state-default jqgrid-rownum" >审核日期：</td>
										<td colspan=3>
											<input name="operateDate" id="operateDate" size="20" value="${_CheckDate}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d'})" readonly="readonly" /><!--  onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" -->
											<div id="operateDateTip"></div>
										</td>
									</tr>
									<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
										<td class="ui-state-default jqgrid-rownum" >是否通过：</td>
										<td valign="top" colspan=3>
											<form:radiobutton path="isPassed" value="1" onchange="display1()"/>通过&nbsp;&nbsp;
											<form:radiobutton path="isPassed" value="0" onchange="display2()" checked="checked" />不符合
										</td>
									</tr>
									<tr id="pingjia" class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;display:none;">
										<td class="ui-state-default jqgrid-rownum" >完成效果：</td>
										<td valign="middle" colspan=3>
											<input type="radio" name="nameScore" value="1" checked/>符合&nbsp;&nbsp;
											<input type="radio" name="nameScore" value="0"/>基本符合
										</td>
									</tr>
									<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
										<td class="ui-state-default jqgrid-rownum" >审核意见：</td>
										<td valign="top" colspan=3>
											<form:textarea path="managerAdvice" cols="80" rows="5"/>
											<!--<div id="managerAdviceTip"></div>-->
										</td>
										<!-- <td class="ui-state-default jqgrid-rownum" >评价：</td>
										<td valign="middle">
											<div id="xzw_starSys">
												<div id="xzw_starBox">
													<ul class="star" id="star">
														<li><a href="javascript:void(0)" title="1" class="one-star">1</a></li>
														<li><a href="javascript:void(0)" title="2" class="two-stars">2</a></li>
														<li><a href="javascript:void(0)" title="3" class="three-stars">3</a></li>
														<li><a href="javascript:void(0)" title="4" class="four-stars">4</a></li>
														<li><a href="javascript:void(0)" title="5" class="five-stars">5</a></li>
													</ul>
													<div class="current-rating" id="showb"></div>
												</div>
												
												<div class="description"></div>
											</div>
											<input type="hidden" name="nameScore" id="nameScore" value="" />
											<div id="nameScoreTip"></div>									
										
										</td>-->
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
													<td><input style="cursor: pointer;" type="submit" value="保存"/></td>
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
