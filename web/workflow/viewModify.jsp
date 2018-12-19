<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>



<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
	<title>修改记录</title>
	<script src="<c:url value="/"/>datePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
	<script src="<c:url value="/"/>js/inc_javascript.js"></script>
	<script src="<c:url value="/"/>js/addattachment.js"></script>
	
	<link rel="stylesheet" type="text/css" href="<c:url value="/"/>components/jqgrid/css/jquery.ui.all.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
	
	<!-- formValidator -->


	<!-- ------------- -->
	

		<script >
			function commonFun(path) {
			window.name = "__self";
			window.open(path, "__self");
		}
		
		function goback(instanceId){
			var path = "<c:url value='/workflow/instanceInfor.do'/>?method=view&instanceId=" + instanceId;
			commonFun(path);
		}
		
		</script>
		
			


	


	<base target="_self"/>
</head>
<body style="overflow-y: auto;padding: 0 100px">
<br/>
<form:form commandName="instanceCheckInforVo" id="instanceCheckInforForm" name="instanceCheckInforForm" action="" method="post" enctype="multipart/form-data">
	<form:hidden path="checkId"/>
	<input type="hidden" name="chargerEdit" value="${param.chargerEdit}"/>
	
	<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 98%">
	<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
	  	<div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
	    	<span class="ui-jqgrid-title">审批文件 &nbsp;【${_Flow.flowName} 主办人:${_Instance.charger.person.personName}】</span>
	  	</div>
	
		
		<%-- 审核实例信息 --%>	
		<%@include file="includeInstance.jsp" %>
	
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
							
					
				
					
			<c:if test="${!empty _replyList}">
							<tr >
								<td colspan="2" >
									<table width="98%"  border="0" cellpadding="0" cellspacing="0" >
										 <tr><td>&nbsp;</td></tr>
										 <tr><td align="left" style="font-family:Verdana;font-size:20px;font-weight:bold"> 如下为修改记录:</td></tr>	
										 <tr><td style="background-color: #B2DFEE">&nbsp;</td></tr>					
									</table>		
								</td>
							</tr>
							
							<tr  >
								<td colspan="2" >
									<table cellspacing="5" cellpadding="2" >														
									<c:forEach items="${_replyList}" var="modify" varStatus="status">													
											<tr>
												<td style="font-weight:bold">${modify.modifyer.person.personName }:</td>
												<td><fmt:formatDate value="${modify.modifyTime}" pattern="yyyy-MM-dd HH:mm:ss"/><br/>${modify.content}</td>
											</tr>
									</c:forEach>
									</table>
								</td>
							</tr>
						</c:if>
					

					
					
					

				</tbody>
			</table>			
		</div>
		
		<div style="width: 100%" class="ui-state-default ui-jqgrid-pager ui-corner-bottom" dir="ltr">
		<td><input style="cursor: pointer;" type="button" value="返回" onclick="goback('${_Instance.instanceId}');"/></td>
			<td><input style="cursor: pointer;" type="button" value="关闭" onclick="window.close();"/></td>
		</div>
	</div>
	</div>
</form:form>
</body>
</html>
                  
