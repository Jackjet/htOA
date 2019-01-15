<%@ page import="java.util.Date" %>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
	<%--<meta http-equiv="X-UA-Compatible" content="IE=9" />--%>
	<title>审批流转</title>
	<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
	<script type="text/javascript" src="<c:url value="/"/>components/jqgrid/js/jquery.ui.core.js"></script>
	<script type="text/javascript" src="<c:url value="/"/>components/jqgrid/js/jquery.ui.widget.js"></script>
	<script type="text/javascript" src="<c:url value="/"/>components/jqgrid/js/grid.locale-cn.js"></script>
	<script type="text/javascript" src="<c:url value="/"/>components/jqgrid/js/jquery.ui.datepicker.js"></script>
	
	<link rel="stylesheet" type="text/css" href="<c:url value="/"/>components/jqgrid/css/jquery.ui.all.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />

	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="/css/noTdBottomBorder.css" />

	<script src="/js/inc_javascript.js"></script>
	<script src="/js/addattachment.js"></script>
	<script src="/js/commonFunction.js"></script>

	<style type="text/css">




			.buttonclass {
				font-weight:bold;
			}				
			/**input {color:expression(this.type=="button"?"red":"blue") } */		
			
	</style>
	
	<!-- formValidator -->
	<link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css"></link>
	<script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript" charset="UTF-8"></script>
	<script src="<c:url value="/"/>components/formvalidator/js/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
	<!-- ------------- -->
	
	<script>


	
		$(document).ready(function(){


			//格式化tr,td
			//$("#addTable tr").addClass("ui-widget-content jqgrow ui-row-ltr").height(30).find("td:nth-child(1)").addClass("ui-state-default jqgrid-rownum");
			
			//验证
			$.formValidator.initConfig({formid:"purchaseInforForm",onerror:function(msg){alert(msg)},onsuccess:function(){}});
			$("#purchaseTitle").formValidator({onshow:"请输入项目名称",onfocus:"项目名称不能为空"}).inputValidator({min:1,onerror: "请输入项目名称"});
			$("#purchaseGoods").formValidator({onshow:"请输入需求原因及内容",onfocus:"需求原因及内容不能为空"}).inputValidator({min:1,onerror: "请输入需求原因及内容"});
//			$("#purchaseNumber").formValidator({onshow:"请输入预计工期",onfocus:"预计工期不能为空"}).inputValidator({type:"number",min:0,onerror: "请输入正确的预计工期"});
			$("#guige").formValidator({onshow:"请选择工程类型",onfocus:"工程类型不能为空"}).inputValidator({min:1,onerror: "请选择工程类型"});
			$("#application").formValidator({onshow:"请输入用途",onfocus:"用途不能为空"}).inputValidator({min:1,onerror: "请输入用途"});
//			$("#ysType").formValidator({onshow:"请输入预算类型",onfocus:"标题不能为空"}).inputValidator({min:1,onerror: "请输入标题"});
			$("#guikouDepartment").formValidator({onshow:"请输入归口部门",onfocus:"归口部门不能为空"}).inputValidator({min:1,onerror: "请输入归口部门"});


		});
		
			
	</script>
	<base target="_self"/>
</head>

<body style="overflow-y: auto;padding: 0 100px" onload="">
<br/>
	<form:form commandName="purchaseInforVo" id="purchaseInforForm" name="purchaseInforForm" action="/gcpurchase/purchaseInfor.do?method=save" method="post" enctype="multipart/form-data">
		<%--<form:hidden path="purchaseId"/>--%>
		<%--<form:hidden path="oldInstanceId"/>--%>
		<input type="hidden" name="flowId" id="flowId" value="${_Flow.flowId}"/>
		<input type="hidden" name="purchaseId" id="purchaseId" value="${_Purchase.purchaseId}"/>


		<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 98%">
			<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
				<div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
		    	<span class="ui-jqgrid-title">编辑采购信息 &nbsp;${_Flow.flowName}</span>
				</div>

				<div>
					<c:if test="${ _Flow.flowId == 1 }">

					<table cellspacing="0" cellpadding="0" border="0" >
						<tr><td style="width: 65%;">
							</c:if>
							<table cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable" style="width: 90%;">
								<tbody id="flowTemplate">
								<tr>
									<td style="width: 18%"></td>
									<td style="width: 40%"></td>
									<td style="width: 15%"></td>
									<td></td>
								</tr>
								<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
									<td class="ui-state-default jqgrid-rownum" style="width: 15%">项目名称：</td>
									<td>
										<form:input path="purchaseTitle" size="35" />
										&nbsp;
										<font color="red">*</font>
									</td>
								</tr>

								</tbody>
							</table>


							<table cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable" style="width: 90%">
								<tbody>
									<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
										<td class="ui-state-default jqgrid-rownum" style="width: 18%">需求原因及内容：</td>
										<td>
											<%--<form:input path="purchaseGoods" size="50" />--%>
												<form:textarea path="purchaseGoods" cols="28" rows="5" />
											&nbsp;
											<font color="red">*</font>
										</td>
									</tr>
									${0==''}
									<c:if test="${_Purchase.purchaseNumber != 0}">
									<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
										<td class="ui-state-default jqgrid-rownum" style="width: 18%">预计工期：</td>
										<td>
											<form:input path="purchaseNumber" size="25" />天
											&nbsp;
											<font color="red">*</font>
										</td>
									</tr>
									</c:if>


									<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
										<%--<td class="ui-state-default jqgrid-rownum" style="width: 18%">工程类型：</td>--%>
										<%--<td>--%>
											<%--<form:input path="guige" size="25" />--%>
											<%--&nbsp;--%>
											<%--<font color="red"></font>--%>
										<%--</td>--%>
										<td class="ui-state-default jqgrid-rownum" style="width: 18%">工程类型：</td>
										<td colspan="3"><form:select path="guige">
											<form:option value="维修保养">--选择工程类型--</form:option>
											<form:option value="新建">新建</form:option>
											<form:option value="改扩建">改扩建</form:option>
											<form:option value="维修保养">维修保养</form:option>

											</form:select>
											&nbsp;
											<font color="red">*</font>
									</tr>

									<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
										<%--<td class="ui-state-default jqgrid-rownum" style="width: 18%">用途：</td>--%>
										<%--<td>--%>
											<%--<form:input path="application" size="25" />--%>
											<%--&nbsp;--%>
											<%--<font color="red"></font>--%>
										<%--</td>--%>
										<td class="ui-state-default jqgrid-rownum" style="width: 18%">用途：</td>
										<td colspan="3"><form:select path="application">
											<form:option value="非生产设施">--选择用途--</form:option>
											<form:option value="生产设施">生产设施</form:option>
											<form:option value="非生产设施">非生产设施</form:option>

										</form:select>
											&nbsp;
											<font color="red">*</font>
										</td>
									</tr>
									<c:if test="${_Purchase.gcfa != null  && _Purchase.gcfa != ''}">
									<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
										<td class="ui-state-default jqgrid-rownum" style="width: 18%">工程方案：</td>
										<td>
												<%--<form:input path="purchaseGoods" size="50" />--%>
											<form:textarea path="gcfa" cols="28" rows="5" />
											&nbsp;
											<font color="red">*</font>
										</td>
									</tr>
									</c:if>

									<%--<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">--%>
										<%--<td class="ui-state-default jqgrid-rownum" style="width: 18%">预算类型：</td>--%>
										<%--<td>--%>
											<%--<form:input path="ysType" size="25" />--%>
											<%--&nbsp;--%>
											<%--<font color="red"></font>--%>
										<%--</td>--%>
									<%--</tr>--%>

									<c:if test="${_Purchase.purchaseId != null  && _Purchase.purchaseId != ''}">
										<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
											<td class="ui-state-default jqgrid-rownum" style="width: 18%">使用部门：</td>
											<td>
												<input   value="${_Purchase.department.organizeName}" />
											</td>
										</tr>
									</c:if>



									<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
										<td class="ui-state-default jqgrid-rownum" style="width: 18%">归口部门：</td>
										<td colspan="3"><form:select path="guikouDepartment">
											<form:option value="0">--选择归口部门--</form:option>
											<c:forEach items="${_guikouDepartment}" var="gk">
												<form:option value="${gk.organizeId}">${gk.organizeName}</form:option>
											</c:forEach>
											</form:select>
											&nbsp;
											<font color="red">*</font>
										</td>

									</tr>
									<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
										<td class="ui-state-default jqgrid-rownum" style="width: 18%">部门审核人：</td>
										<td colspan="3">
												<%--<form:select path="managerId">--%>
											<input  readonly="readonly"  value="${_director.person.personName}" />
											<form:input path="manager" hidden="hidden"  value="${_director.person.personId}" />
												<%--</form:select>&nbsp;--%>
											<form:select path="viceManagerId">
												<form:option value="0">--选择审核人二--</form:option>
												<c:choose>
													<c:when test="${!empty _Purchase.purchaseId && _Purchase.viceManager != null}">
														<form:option value="${_Purchase.viceManager.person.personId}">${_Purchase.viceManager.person.personName}</form:option>
														<c:forEach items="${_Userss}" var="user">
															<form:option value="${user.personId}">${user.person.personName}</form:option>
														</c:forEach>
													</c:when>
													<c:otherwise>
														<c:forEach items="${_Userss}" var="user">
															<form:option value="${user.personId}">${user.person.personName}</form:option>
														</c:forEach>
													</c:otherwise>
												</c:choose>
											</form:select>
										</td>
									</tr>
									<c:if test="${_Purchase.purchaseId != null  && _Purchase.purchaseId != ''}">
										<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
											<td class="ui-state-default jqgrid-rownum" style="width: 18%">申请时间：</td>
											<td>
												<form:input path="startTime"  value="${_Purchase.startTime}" />
											</td>
										</tr>
									</c:if>


								<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
									<td class="ui-state-default jqgrid-rownum "style="width: 18%">附件：</td>
									<td align="left" id="newstyle" colspan="3"><input type="file" name="attachment" size="50" />&nbsp;<a href="#" onclick="addtable('newstyle')" >更多附件..</a></td>
								</tr>

								<c:if test="${!empty _Attachment_Names}">
									<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
										<td colspan="4" class="ui-state-default jqgrid-rownum">原附件信息(<font color="white">如果要删除某个附件，请选择该附件前面的选择框</font>)：</td>
									</tr>
									<tr>
										<td colspan="4"><c:forEach var="file" items="${_Attachments}" varStatus="index">
											<input type="checkbox" name="attatchmentArray" value="${index.index}" />
											<a href="<%=request.getRealPath("/")%>${file}">${_Attachment_Names[index.index]}</a><br/>
										</c:forEach></td>
									</tr>
								</c:if>
								</tbody>
							</table>


					<%--<c:if test="${ _Flow.flowId == 1 }">--%>
						<%--</td><td><img src="/images/zdps.png"></td></tr></table>--%>
					<%--</c:if>--%>
				</div>

				<div class="ui-state-default ui-jqgrid-pager ui-corner-bottom" dir="ltr" style="width: 100%;margin-top:20px;overflow:visible" >
					<input style="cursor: pointer;" type="submit" value="保存提交"/>
						<%--<td><input style="cursor: pointer;" type="button" value="返回" onclick="history.back();"/></td>--%>
					<input style="cursor: pointer;" type="button" value="关闭" onclick="window.close();"/>
				</div>
			</div>
		</div>
	</form:form>


</body>

</html>
                  
