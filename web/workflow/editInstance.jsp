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
			$.formValidator.initConfig({formid:"instanceInforForm",onerror:function(msg){alert(msg)},onsuccess:function(){}});
			$("#instanceTitle").formValidator({onshow:"请输入正文标题",onfocus:"正文标题不能为空"}).inputValidator({min:1,onerror: "请输入正文标题"});
            if (document.getElementById("chargerId") != null) {
                $("#chargerId").formValidator({onshow:"请选择主办人",onfocus:"主办人不能为空"}).inputValidator({min:1,onerror: "请选择主办人"});
            }
			//获取流程模板信息
			var instanceId = "${param.instanceId}";
			if(instanceId == ""){
			
			instanceId = "${param.oldInstanceId}";
			}
			
			//alert(instanceId);
			$.ajax({
				url: "/workflow/customizeForm.do?method=getCustomizeForm&flowId="+${_Flow.flowId}+"&instanceId="+instanceId,
				type: "get",
				dataType: "html",
				//async: false,	//设置为同步
				beforeSend: function (xhr) {
				},
				complete : function (req, err) {
					//alert(req.responseText);
					$("#flowTemplate").append(req.responseText);
				}
			});
			
			//去掉初始化的提示信息
			$("#instanceTitleTip").html("");
			
			//button字体变粗
			for(i=0;i<document.getElementsByTagName("INPUT").length;i++){ 
					if(document.getElementsByTagName("INPUT")[i].type=="button" || document.getElementsByTagName("INPUT")[i].type=="submit") 
					document.getElementsByTagName("INPUT")[i].className="buttonclass" ;
			}		
		});
		
			
	</script>
	<base target="_self"/>
</head>

<body style="overflow-y: auto;padding: 0 100px" onload="">
<br/>
	<form:form commandName="flowInstanceInforVo" id="instanceInforForm" name="instanceInforForm" action="/workflow/instanceInfor.do?method=save" method="post" enctype="multipart/form-data">
		<form:hidden path="instanceId"/>
		<form:hidden path="oldInstanceId"/>
		<input type="hidden" name="flowId" id="flowId" value="${_Flow.flowId}"/>

		<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 98%">
			<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
				<div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
		    	<span class="ui-jqgrid-title">编辑审批信息 &nbsp;
		    	<c:choose>
					<c:when test="${!empty flowInstanceInforVo.instanceId}">
						【${_Flow.flowName} 主办人:${_Instance.charger.person.personName}】
					</c:when>
					<c:otherwise>
						【${_Flow.flowName} 主办人:${_User.person.personName}<%--${_Flow.charger.person.personName}--%>】
					</c:otherwise>
				</c:choose>
		    	</span>
				</div>

				<div>
					<c:if test="${ _Flow.flowId == 86 || _Flow.flowId == 87 ||_Flow.flowId == 88 }">

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
									<td class="ui-state-default jqgrid-rownum" style="width: 15%">正文标题：</td>
									<td colspan="3" style="padding-top: 5px;padding-bottom: 5px;"><form:textarea path="instanceTitle" cols="75" rows="2"/><div id="instanceTitleTip"></div></td>
								</tr>

								</tbody>
							</table>


							<table cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable" style="width: 90%">
								<tbody>

								<c:if test="${_Flow.flowId == 87 || _Flow.flowId == 86 || _Flow.flowId == 88 || (_Flow.flowId > 89 && _Flow.flowId < 101)}">
									<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
										<td class="ui-state-default jqgrid-rownum" style="width: 18%">主办人：</td>
										<td colspan="3">${_User.person.personName}<input type="hidden" name="chargerId" id="chargerId" value="${_User.personId}"/></td>
									</tr>
								</c:if>
								<c:if test="${_Flow.flowId == 86 && _User.person.department.organizeId !=72}">
									<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
										<td class="ui-state-default jqgrid-rownum" style="width: 18%">部门审核人：</td>
										<td colspan="3"><form:select path="managerId">
											<form:option value="0">--选择审核人一--</form:option>
											<c:choose>
												<c:when test="${!empty flowInstanceInforVo.instanceId}">
													<form:option value="${_Instance.manager.person.personId}">${_Instance.manager.person.personName}</form:option>
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

										</form:select>&nbsp;<form:select path="viceManagerId">
											<form:option value="0">--选择审核人二--</form:option>
											<c:choose>
												<c:when test="${!empty flowInstanceInforVo.instanceId && _Instance.viceManager != null}">
													<form:option value="${_Instance.viceManager.person.personId}">${_Instance.viceManager.person.personName}</form:option>
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
										</form:select></td>
									</tr>
								</c:if>
								<c:if test="${_Flow.flowId != 86 || _User.person.department.organizeId ==72}">
									<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
										<c:if test="${_Flow.flowId == 85}">
											<td class="ui-state-default jqgrid-rownum" style="width: 18%">办公室送审意见：</td>
										</c:if>
										<c:if test="${_Flow.flowId != 85}">
											<td class="ui-state-default jqgrid-rownum" style="width: 18%">部门审核人：</td>
										</c:if>
										<td colspan="3"><form:select path="managerId">
											<form:option value="0">--选择审核人一--</form:option>
											<c:choose>
												<c:when test="${!empty flowInstanceInforVo.instanceId}">
													<form:option value="${_Instance.manager.person.personId}">${_Instance.manager.person.personName}</form:option>
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
										</form:select>&nbsp;<form:select path="viceManagerId">
											<form:option value="0">--选择审核人二--</form:option>
											<c:choose>
												<c:when test="${!empty flowInstanceInforVo.instanceId && _Instance.viceManager != null}">
													<form:option value="${_Instance.viceManager.person.personId}">${_Instance.viceManager.person.personName}</form:option>
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
										</form:select></td>
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
							<c:if test="${ _Flow.flowId == 86 }">
						</td><td><img src="/images/htsp.png"></td></tr></table>
					</c:if>
					<c:if test="${ _Flow.flowId == 87 }">
						</td><td><img src="/images/nbbg.png"></td></tr></table>
					</c:if>
					<c:if test="${ _Flow.flowId == 88 }">
						</td><td><img src="/images/zdps.png"></td></tr></table>
					</c:if>
				</div>

				<div class="ui-state-default ui-jqgrid-pager ui-corner-bottom" dir="ltr" style="width: 100%;margin-top:20px;overflow:visible" >
					<input style="cursor: pointer;" type="submit" value="保存草稿"/>
						<%--<td><input style="cursor: pointer;" type="button" value="返回" onclick="history.back();"/></td>--%>
					<input style="cursor: pointer;" type="button" value="关闭" onclick="window.close();"/>
				</div>
			</div>
		</div>
	</form:form>


</body>

</html>
                  
