<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
	<title>审批流转</title>
	<script src="<c:url value="/"/>datePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
	<script src="<c:url value="/"/>js/inc_javascript.js"></script>
	<script src="<c:url value="/"/>js/addattachment.js"></script>
	
	<link rel="stylesheet" type="text/css" href="<c:url value="/"/>components/jqgrid/css/jquery.ui.all.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
	
	<!-- formValidator -->
	<link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css"></link>

	<link rel="stylesheet" type="text/css" href="/css/noTdBottomBorder.css"/>
	<script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript" charset="UTF-8"></script>
	<script src="<c:url value="/"/>components/formvalidator/js/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
	<!-- ------------- -->
	<style type="text/css">
			.buttonclass {
				font-weight:bold;
			}				
			/**input {color:expression(this.type=="button"?"red":"blue") } */		
	</style>
	<script>
		$(document).ready(function(){
			//验证
			$.formValidator.initConfig({formid:"instanceCheckInforForm",onerror:function(msg){alert(msg)},onsuccess:function(){}});
			$("#checkComment").formValidator({onshow:"请输入审核内容",onfocus:"审核内容不能为空",oncorrect:"输入正确"}).inputValidator({min:1,onerror: "请输入审核内容"});
//            $("[name=caigou]").formValidator({onshow:"请输入审核内容",onfocus:"审核内容不能为空",oncorrect:"输入正确"}).inputValidator({min:1,onerror: "请输入审核内容"});
		});
		//保存审核
		/** function save(){
			var form = document.instanceCheckInforForm;
			form.action = "<c:url value='/workflow/checkInfor.do'/>?method=save";
			form.submit();
		} */
		//去掉初始化的提示信息
		$("#checkCommentTip").html("");
			
		$(document).ready(function(){
            $("html,body").animate({scrollTop: $("#bottomFlag").offset().top}, 500);
			//button字体变粗
			for(i=0;i<document.getElementsByTagName("INPUT").length;i++){ 
						if(document.getElementsByTagName("INPUT")[i].type=="button" || document.getElementsByTagName("INPUT")[i].type=="submit") 
						document.getElementsByTagName("INPUT")[i].className="buttonclass" ;
				}		
		});	
	</script>
	<base target="_self"/>
</head>
<body style="overflow-y: auto;padding: 0 100px">
<br/>
<form:form commandName="purchaseCheckInforVo" id="purchaseCheckInforForm" name="purchaseCheckInforForm" action="/gcpurchase/checkInfor.do?method=editSave" method="post" enctype="multipart/form-data">
	<%--<form:hidden path="checkInforId"/>--%>
	<%--<input type="hidden" name="chargerEdit" value="${param.chargerEdit}"/>--%>
	<input type="hidden" name="checkInforId" value="${_CheckId}"/>

	<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 98%">
	<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
	  	<div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
	    	<span class="ui-jqgrid-title">审批文件 &nbsp;【${_Flow.flowName} 申请人:${_Purchase.applier.person.personName}】</span>
	  	</div>
		<%-- 审核实例信息 --%>
		<%@include file="includePurchase.jsp" %>
		<div>
			<table cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable" style="width: 90%" id="addTable">
				<tbody>
					<tr>
						<td class="ui-state-default jqgrid-rownum" style="width: 15%"></td>
						<td></td>
				    </tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr">
						<td colspan="2" height="20"></td>
					</tr>
					<c:if test="${_Purchase.purchaseStatus == 4 || _Purchase.purchaseStatus == 5}">
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum" style="width: 15%">审核内容：</td>
							<td>
								<input type="hidden" name="status" value="1" />
								<input type="radio" name="caigou" value="0" id="ty" checked /><label for="ty">直接采购</label>&nbsp;&nbsp;&nbsp;
								<input type="radio" name="caigou" value="1" id="ty"  /><label for="ty">三方比价</label>&nbsp;&nbsp;&nbsp;
								<input type="radio" name="caigou" value="2" id="ty"  /><label for="ty">招投标</label>&nbsp;&nbsp;&nbsp;
								<%--<input type="radio" name="caigou" value="3" id="ty"  /><label for="ty">合同变更</label><br/><br/>--%>
								<br/><br/>
								<form:textarea path="checkComment" cols="80" rows="5" /><div id="checkCommentTip"></div>
							<br/>
							</td>
						</tr>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum" style="width: 15%">选择供应商：</td>
							<td>
								<form:select path="supplierName">
									<form:option value="">--选择供应商--</form:option>
									<c:forEach items="${_Suppliers}" var="supplier">
										<form:option value="${supplier.supplierName}">${supplier.supplierName}</form:option>
									</c:forEach>
								</form:select>
								<br/>
							</td>
						</tr>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum" style="width: 15%">预计价格：</td>
							<td>
								<form:input path="purchaseMoney" size="25" />
								<br/>
							</td>
						</tr>
					</c:if>

					<c:if test="${_Purchase.purchaseStatus == 1 || _Purchase.purchaseStatus == 2}">
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum" style="width: 15%">审核内容：</td>
							<td>
								<form:textarea path="checkComment" cols="80" rows="4" /><div id="checkCommentTip"></div>
								<br/>
							</td>
						</tr>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum" style="width: 15%">预算类型：</td>
							<td>
								<form:input path="ysType" size="25" />
								<br/>
							</td>
						</tr>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum" style="width: 15%">归属公司：</td>
							<td>
									<%--<form:input path="purchaseStr2" size="25" />--%>
								<input type="radio" name="purchaseStr2"  value="码头" />&nbsp;&nbsp;码头&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<input type="radio" name="purchaseStr2"  value="物流" />&nbsp;&nbsp;物流&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<br/>
							</td>
						</tr>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum" style="width: 15%">预计工期：</td>
							<td>
								<form:input path="purchaseNumber" size="25" />天
								&nbsp;
								<font color="red">*</font>
								<br/>
							</td>
						</tr><tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum" style="width: 15%">工程方案：</td>
						<td>
							<form:textarea path="gcfa" cols="35" rows="5" />
							&nbsp;
							<font color="red">*</font>
							<br/>
						</td>
						</tr>
						<c:if test="${_Purchase.guikouDepartment.organizeId == 68}">
							<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
								<td class="ui-state-default jqgrid-rownum" style="width: 15%">选择技术规划部领导：</td>
								<td>
									<c:forEach var="user" items="${_users}" varStatus="index">
										<input type="checkbox" name="check[]"  value="${user.personId}" />&nbsp;&nbsp;${user.person.personName}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									</c:forEach>
								</td>
							</tr>

						</c:if>

					</c:if>
					<%--<c:if test="${_Purchase.purchaseStatus == 9}">--%>
						<%--<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">--%>
							<%--<td class="ui-state-default jqgrid-rownum" style="width: 15%">最终价格：</td>--%>
							<%--<td>--%>
								<%--<form:input path="purchaseFinalMoney" size="25" />--%>
								<%--<br/>--%>
							<%--</td>--%>
						<%--</tr>--%>
					<%--</c:if>--%>
					<c:if test="${_CanUpload}">
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">附件：</td>
							<td align="left" id="newstyle"><input type="file" name="attachment" size="50" />&nbsp;<a href="javascript:;" onclick="addtable('newstyle')" >更多附件..</a></td>								
						</tr>
						<c:if test="${!empty _CheckAttachment_Names}">
							<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
								<td colspan="2" class="ui-state-default jqgrid-rownum">原附件信息(<font color="white">如果要删除某个附件，请选择该附件前面的选择框</font>)：</td>
							</tr>
							<tr>
								<td colspan="2">
								<c:forEach var="file" items="${_CheckAttachments}" varStatus="index">
										<input type="checkbox" name="attatchmentArray" value="${index.index}" />
										<a href="<%=request.getRealPath("/")%>${file}">${_CheckAttachment_Names[index.index]}</a><br/>
								</c:forEach></td>
							</tr>
						</c:if>
					</c:if>
				</tbody>
			</table>			
		</div>
		<div style="width: 100%" class="ui-state-default ui-jqgrid-pager ui-corner-bottom" dir="ltr">
			<td><input style="cursor: pointer;" type="submit" value="保存"/></td>
			<td><input style="cursor: pointer;" type="button" value="关闭" onclick="window.close();"/></td>
		</div>
	</div>
	</div>
	<div id="bottomFlag"></div>
</form:form>
</body>
</html>
                  
