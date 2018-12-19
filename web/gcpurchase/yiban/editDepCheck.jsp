<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
<title>部门领导审核</title>
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
<!-- ------------- -->

</head>

<script>
	
	$(document).ready(function(){
		//验证
		$.formValidator.initConfig({formid:"purchaseInforForm",onerror:function(msg){alert(msg)},onsuccess:function(){}});
		$("#checkWord").formValidator({onshow:"请填写审核意见",onfocus:"审核意见不能为空",oncorrect:"输入正确"}).inputValidator({min:1,onerror: "请输入审核意见"});
        $("html,body").animate({scrollTop: $("#bottomFlag").offset().top}, 500);
	});
	
</script>
<base target="_self"/>
<body style="overflow-y: auto;padding: 0 100px;bottom: 0">
<br/>
<form:form commandName="purchaseInforVo" id="purchaseInforForm" name="purchaseInforForm" action="/gcpurchase/purchaseInfor.do?method=saveDepCheck" method="post" enctype="multipart/form-data">
<form:hidden path="purchaseId"/>

<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 98%">
	<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
	<div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
		<span class="ui-jqgrid-title" >部门审核 &nbsp;【${_Flow.flowName} 申请人:${_Purchase.applier.person.personName}】</span>
	</div>

	<%-- 审核实例信息 --%>	
	<%@include file="includePurchase.jsp" %>

				<table cellspacing="0" cellpadding="0" border="0" style="width: 90%">
					<tbody>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum" style="width: 15%">审核意见：</td>
							<td>
								<script>
                                    function setCheckWord(value){
                                        if(value == 1){
                                            $("#checkWord").attr("readonly","readonly");
                                            $("#checkWord").val("同意");
                                        }else{
                                            $("#checkWord").removeAttr("readonly");
                                            $("#checkWord").val("");
                                        }
                                    }
								</script>
								<input type="radio" name="checkWordR" value="1" id="ty" onclick="setCheckWord(this.value);" /><label for="ty">同意</label><br/>
								<input type="radio" name="checkWordR" value="2" id="bty" onclick="setCheckWord(this.value);" /><label for="ty">不同意</label><br/>
								<textarea name="checkWord" id="checkWord" cols="70" rows="5" readonly="readonly">${_CheckWord}</textarea>
								<br>


							</td>
							<td><div id="checkWordTip"></div></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum "style="width: 15%">附件：</td>
							<td align="left" colspan="2" id="newstyle"><input type="file" name="attachment" size="50" />&nbsp;<a href="javascript:;" onclick="addtable('newstyle')">更多附件..</a></td>							
						</tr>
						
						<c:if test="${!empty _DepAttachment_Names}">
							<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
								<td colspan="3" class="ui-state-default jqgrid-rownum">原附件信息(<font color="white">如果要删除某个附件，请选择该附件前面的选择框</font>)：</td>
							</tr>
							<tr>
								<td colspan="3"><c:forEach var="file" items="${_DepAttachment_Names}" varStatus="index">
													<input type="checkbox" name="attatchmentArray" value="${index.index}" />
														<a href="<%=request.getRealPath("/")%>${file}">${_DepAttachment_Names[index.index]}</a><br/>
												</c:forEach></td>
							</tr>
						</c:if>
								
						<tr class="ui-widget-content jqgrow ui-row-ltr">
							<td class="ui-state-default jqgrid-rownum" colspan="3"><input style="cursor: pointer;" type="submit" value="保存"/>&nbsp;<input style="cursor: pointer;" type="button" value="关闭" onclick="window.close();"/></td>
						</tr>
						
					</tbody>
				</table>
	<div id="bottomFlag"></div>
</div>
</div>
</form:form>
</body>
</html>
                  
