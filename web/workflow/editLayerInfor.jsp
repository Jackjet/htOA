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
		//提交数据
		function submitData() {
			$.ajax({
				url: "/workflow/layerInfor.do?method=validate",
				type: "post",
				dataType: "json",
				data: $('#instanceLayerInforForm').serialize(), // 从表单中获取数据
				async: false,	//设置为同步
				beforeSend: function (xhr) {
				},
				complete : function (req, err) {
					var returnValues = eval("("+req.responseText+")");
					if (!returnValues["canChoose"]){
				        //验证父审核层
				        alert(returnValues["warningStr"]);
				    }else {
				        //验证层次名称
						var layerName = document.getElementById("layerName");
						var layerNamevalue = layerName.value;
						if (layerName.value == null || layerName.value == "") {
							alert("请输入层次名称！");
						}else if(layerNamevalue == "公司领导" ||layerNamevalue =="公司领导审批" ||layerNamevalue =="公司领导审核"
                            ||layerNamevalue =="公司领导意见"||layerNamevalue =="公司总经理"){
                            alert("请输入其他层次名称！");
						}else {
                            //获取表单提交按钮
                            var btnSubmit = document.getElementById("submitbutton");
							//提交数据
							var form = document.instanceLayerInforForm;
							form.action = "<c:url value='/'/>workflow/layerInfor.do?method=save";
							form.submit();
							//禁止重复提交
                            btnSubmit.disabled= "disabled";
						}
				    }
				}
			});
		}

		var i=0,number;
		//全选操作
		/** 
		function selectUserId(checkbox,organizeId){
			var isChecked = false;
			if(checkbox.checked){
				isChecked = true;
			}
			var obj;
			obj = document.instanceLayerInforForm.personIds;		
			if(obj!=null){
				if(obj.length==null){
					//只有一个,则只需要判断该用户是不是这个分类
					<c:forEach var="user" items="${_Users}" varStatus="index">
						var tempOrganizeId = '${user.person.department.organizeId}';
						if(organizeId==tempOrganizeId){
							obj.checked = isChecked;
						}
					</c:forEach>
					
					<c:forEach var="user" items="${_OtherUsers}" varStatus="index">
						var tempOrganizeId = '${user.person.department.organizeId}';
							
						if(organizeId==tempOrganizeId){
							obj.checked = isChecked;
						}
					</c:forEach>
				}else{
					//多个用户
					var personNum;
					personNum = 0;
					var personNum = obj.length;			
					for(var k = 0; k<personNum;k++){
						var userId;
						personId = obj[k].value;
						<c:forEach var="user" items="${_Users}" varStatus="index">
							var tempOrganizeId = '${user.person.department.organizeId}';
							var tempPersonId = '${user.person.personId}';
								
							if(organizeId==tempOrganizeId && tempPersonId==personId){
								obj[k].checked = isChecked;
							}
						</c:forEach>
							
						<c:forEach var="user" items="${_OtherUsers}" varStatus="index">
							var tempOrganizeId = '${user.person.department.organizeId}';
							var tempPersonId = '${user.person.personId}';
								
							if(organizeId==tempOrganizeId && tempPersonId==personId){
								obj[k].checked = isChecked;
							}
						</c:forEach>
					}
				}				
			}
		} */
		
		/** 全选/反选用户信息 */
		$.fn.checkbox = function(){
			var hand = this;
			/**
			* 切换全选/反选
			* @example $("#checkAll").checkbox().toggle($("input[name='contactIds']"));
			*/
			this.toggle = function(ele){
				$(ele).click(function(){
					$(hand).attr('checked', false);
				});
				$(this).click(function(){
					$(ele).attr('checked', $(this).attr('checked') == true ? true : false);
				});
			};
			//全选
			this.checked = function(ele){
				$(ele).attr('checked', true);
			};
			//反选
			this.unchecked = function(ele){
				$(ele).attr('checked', false);
			};
			return this;
		};
		function selAll(checkbox,roleId){
			$('#'+checkbox.id).checkbox().toggle($("input[id='checker"+roleId+"']"));
		}
		/** *** */
		
		//显示部分用户
		function displayPartUser() {
			$("#partUsers").css("display","block");
			if($("#allUsers").css("display")=="block"){
				$("#allUsers").css("display","none");	
			}
						
			//把allUsers表格下checkbox的勾去掉,勾上partUsers表格下的personIds
			//alert(11);
			$("#allUsers").find("input[name='personIds']").attr("checked",false);
			<c:forEach var="checkerId" items="${_CheckerIds}">
				$("#partUsers").find("input[name='personIds']").each(function() {
					//alert(${checkerId});alert($(this).val());
					if(${checkerId} == $(this).val()){
						$(this).attr("checked", true);
					}
				});
			</c:forEach>
		}
		
		//显示所有用户
		function displayAllUser() {
			$("#allUsers").css("display","block");
			if($("#partUsers").css("display")=="block"){
				$("#partUsers").css("display","none");	
			}
			
			//把partUsers表格下checkbox的勾去掉,勾上allUsers表格下的personIds
			//alert(22);
			$("#partUsers").find("input[name='personIds']").attr("checked",false);
			<c:forEach var="checkerId" items="${_CheckerIds}">
				$("#allUsers").find("input[name='personIds']").each(function() {
					//alert(${checkerId});alert($(this).val());
					if(${checkerId} == $(this).val()) {
						$(this).attr("checked", true);
					}
				});
			</c:forEach>
		}
		
		$(document).ready(function(){
			//button字体变粗
			for(i=0;i<document.getElementsByTagName("INPUT").length;i++){ 
				if(document.getElementsByTagName("INPUT")[i].type=="button" || document.getElementsByTagName("INPUT")[i].type=="submit") 
				document.getElementsByTagName("INPUT")[i].className="buttonclass" ;
			}
			
			if (${_PartUser}) {
				$("input[name='checkerType'][value=0]").attr("checked",true);
				displayPartUser();
			}
			else {
				$("input[name='checkerType'][value=1]").attr("checked",true);
				displayAllUser();
			}
			
			//显示全部或部分
			$("input[name='checkerType']").click(function(){
				var checked = $("input[name='checkerType']:checked").val();
				if(checked == 0){
					displayPartUser();
				}
				else{
					displayAllUser();
				}
			});
		});		
		
		//定义一个数组，记录各个数据点击的次数
		var clickTimes =new Array();
	</script>
	<base target="_self"/>
</head>
<body style="overflow-y: auto;padding: 0 100px">
<br/>
<form:form commandName="instanceLayerInforVo" id="instanceLayerInforForm" name="instanceLayerInforForm" action="/workflow/layerInfor.do?method=save" method="post">
	<form:hidden path="layerId"/>
	<input type="hidden" name="instanceId" id="instanceId" value="${param.instanceId}"/>
	
	<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 98%">
	<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
		<div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
	    	<span class="ui-jqgrid-title">设定审批层次 &nbsp;【${_Flow.flowName} 主办人:${_Instance.charger.person.personName}】</span>
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
							
					<c:choose>
						<c:when test="${!empty instanceLayerInforVo.layerId}">
							<c:if test="${!empty _ForkedLayer}">
								<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
									<td class="ui-state-default jqgrid-rownum" style="width: 15%">所属父审核层：</td>
									<td>${_ForkedLayer.layerName}</td>
								</tr>
							</c:if>
						</c:when>
						
						<c:otherwise>
							<!-- 
							<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
								<td class="ui-state-default jqgrid-rownum" style="width: 15%">可选父审核层：</td>
								<td><c:forEach items="${_FromLayers}" var="layer"><form:checkbox path="fromLayerIds" value="${layer.layerId}"/>${layer.layerName}<br/></c:forEach></td>
							</tr> -->
						</c:otherwise>
					</c:choose>
							
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum" style="width: 15%">层次名称：</td>
						<td><form:input path="layerName"/></td>
					</tr>
							
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum" style="width: 15%">审核说明：</td>
						<td><form:textarea path="checkDemand" cols="80" rows="3"/></td>
					</tr>
					
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum" rowspan="2">审核人员：</td>
						<td><input type="radio" name="checkerType" value="0"/>部分 <input type="radio" name="checkerType" value="1"/>全部</td>
					</tr>

					<tr>
						<td>
							<table width="100%" id="partUsers">
								<c:choose>

									<c:when test="${_Flow.flowId==86}">
										<c:forEach var="role" items="${_Roles}">
											<c:if test="${!empty role && !empty role.users}">
												<c:choose>
													<c:when test="${_LayerName=='法务意见'}">
														<c:if test="${fn:contains(role.roleName, '法务')}">
															<tr>
																<td colspan="2"><strong>${role.roleName}：</strong></td>
															</tr>

															<tr id="checker${role.roleId}">
																<td width="9%" align="right" valign="top">
																	<input type="checkbox" id="selAllBox${role.roleId}" onclick="selAll(this,'${role.roleId}')" />全选
																</td>
																<td style="padding-left:10px;" width="91%">
																	<table>
																		<tr>
																			<c:set var="_TypeNum" value="0"/>
																			<c:forEach var="user" items="${role.users}" varStatus="index">
																			<c:if test="${_TypeNum!=0 && _TypeNum%6==0}"></tr><tr></c:if>
																		<td width="15%" valign="top">
																			<form:checkbox path="personIds" value="${user.personId}" id="checker${role.roleId}"/> ${user.person.personName}
																		</td>
																		<c:set var="_TypeNum" value="${_TypeNum+1}"/>
																		</c:forEach>
																			<%--	<c:forEach begin="${_TypeNum%6}" end="5">
																					<td width="16%">&nbsp;</td>
																				</c:forEach>	--%>
																	</tr>
																	</table>
																</td>
															</tr>
														</c:if>
													</c:when>
													<c:when test="${_LayerName=='部门会签意见'}">
														<c:if test="${fn:contains(role.roleName, '部门经理')}">
															<tr>
																<td colspan="2"><strong>${role.roleName}：</strong></td>
															</tr>

															<tr id="checker${role.roleId}">
																<td width="9%" align="right" valign="top">
																	<input type="checkbox" id="selAllBox${role.roleId}" onclick="selAll(this,'${role.roleId}')" />全选
																</td>
																<td style="padding-left:10px;" width="91%">
																	<table>
																		<tr>
																			<c:set var="_TypeNum" value="0"/>
																			<c:forEach var="user" items="${role.users}" varStatus="index">
																			<c:if test="${_TypeNum!=0 && _TypeNum%6==0}"></tr><tr></c:if>
																		<td width="15%" valign="top">
																			<form:checkbox path="personIds" value="${user.personId}" id="checker${role.roleId}"/> ${user.person.personName}
																		</td>
																		<c:set var="_TypeNum" value="${_TypeNum+1}"/>
																		</c:forEach>
																			<%--	<c:forEach begin="${_TypeNum%6}" end="5">
																					<td width="16%">&nbsp;</td>
																				</c:forEach>	--%>
																	</tr>
																	</table>
																</td>
															</tr>
														</c:if>
													</c:when>
													<c:when test="${_LayerName=='分管领导意见'}">
														<c:if test="${fn:contains(role.roleName, '分管领导')}">
															<tr>
																<td colspan="2"><strong>${role.roleName}：</strong></td>
															</tr>

															<tr id="checker${role.roleId}">
																<td width="9%" align="right" valign="top">
																	<input type="checkbox" id="selAllBox${role.roleId}" onclick="selAll(this,'${role.roleId}')" />全选
																</td>
																<td style="padding-left:10px;" width="91%">
																	<table>
																		<tr>
																			<c:set var="_TypeNum" value="0"/>
																			<c:forEach var="user" items="${role.users}" varStatus="index">
																			<c:if test="${_TypeNum!=0 && _TypeNum%6==0}"></tr><tr></c:if>
																		<td width="15%" valign="top">
																			<form:checkbox path="personIds" value="${user.personId}" id="checker${role.roleId}"/> ${user.person.personName}
																		</td>
																		<c:set var="_TypeNum" value="${_TypeNum+1}"/>
																		</c:forEach>
																			<%--	<c:forEach begin="${_TypeNum%6}" end="5">
																					<td width="16%">&nbsp;</td>
																				</c:forEach>	--%>
																	</tr>
																	</table>
																</td>
															</tr>
														</c:if>
													</c:when>
													<c:otherwise>
														<tr>
															<td colspan="2"><strong>${role.roleName}：</strong></td>
														</tr>

														<tr id="checker${role.roleId}">
															<td width="9%" align="right" valign="top">
																<input type="checkbox" id="selAllBox${role.roleId}" onclick="selAll(this,'${role.roleId}')" />全选
															</td>
															<td style="padding-left:10px;" width="91%">
																<table>
																	<tr>
																		<c:set var="_TypeNum" value="0"/>
																		<c:forEach var="user" items="${role.users}" varStatus="index">
																		<c:if test="${_TypeNum!=0 && _TypeNum%6==0}"></tr><tr></c:if>
																	<td width="15%" valign="top">
																		<form:checkbox path="personIds" value="${user.personId}" id="checker${role.roleId}"/> ${user.person.personName}
																	</td>
																	<c:set var="_TypeNum" value="${_TypeNum+1}"/>
																	</c:forEach>
																		<%--	<c:forEach begin="${_TypeNum%6}" end="5">
                                                                                <td width="16%">&nbsp;</td>
                                                                            </c:forEach>	--%>
																</tr>
																</table>
															</td>
														</tr>
													</c:otherwise>
												</c:choose>
											</c:if>
										</c:forEach>
									</c:when>

									<c:otherwise>
										<c:forEach var="role" items="${_Roles}">
											<c:if test="${!empty role && !empty role.users}">
												<tr>
													<td colspan="2"><strong>${role.roleName}：</strong></td>
												</tr>

												<tr id="checker${role.roleId}">
													<td width="9%" align="right" valign="top">
														<input type="checkbox" id="selAllBox${role.roleId}" onclick="selAll(this,'${role.roleId}')" />全选
													</td>
													<td style="padding-left:10px;" width="91%">
														<table>
															<tr>
																<c:set var="_TypeNum" value="0"/>
																<c:forEach var="user" items="${role.users}" varStatus="index">
																	<c:if test="${_TypeNum!=0 && _TypeNum%6==0}"></tr><tr></c:if>
																	<td width="15%" valign="top">
																		<form:checkbox path="personIds" value="${user.personId}" id="checker${role.roleId}"/> ${user.person.personName}
																	</td>
																	<c:set var="_TypeNum" value="${_TypeNum+1}"/>
																</c:forEach>
															<%--	<c:forEach begin="${_TypeNum%6}" end="5">
																	<td width="16%">&nbsp;</td>
																</c:forEach>	--%>
															</tr>
														</table>
													</td>
												</tr>
											</c:if>
										</c:forEach>
									</c:otherwise>
								</c:choose>
							</table>

							<table width="100%" style="display: none;" id="allUsers">
								<c:set var="_Num" value="0" />
								<c:forEach var="department" items="${_Departments}">
									<script language="javaScript">
										clickTimes[${_Num}] = 0;							
									</script>
				
									<tr height="33">
										<td valign="bottom" width="100%" style="border-bottom:1px dotted #888888;font-size:10pt" colspan="2">
											${department.organizeName}&nbsp;
											<span onclick="show_list('${_Num}')"> <img name="img"
													src="<c:url value="${'/images'}"/>/xpexpand3_s.gif"
													style="margin-top:0px;margin-bottom:0px;" /> </span>
										</td>
									</tr>
				
									<tr name="tr" style="display:none;padding-top:10px;">
										<td width="9%" align="right" valign="top">
											<input type="checkbox" onclick="selectUserId(this,'${department.organizeId}')" />全选
										</td>
										<td style="padding-left:10px;" width="92%">
											<table>
												<tr>
								      				<c:set var="_TypeNum" value="0"/>
								      				<c:forEach var="user" items="${_Users}" varStatus="index">
								      					<c:if test="${user.person.department.organizeId==department.organizeId}">
															<c:if test="${_TypeNum!=0 && _TypeNum%6==0}"></tr><tr></c:if>
															<td width="16%" valign="top">
																<form:checkbox path="personIds" value="${user.personId}"/> ${user.person.personName}
															</td>				
															<c:set var="_TypeNum" value="${_TypeNum+1}"/>
														</c:if>																			
													</c:forEach>
													<c:forEach begin="${_TypeNum%6}" end="5">
														<td width="16%">&nbsp;</td>
													</c:forEach>		      											
												</tr>
													
												<tr>
								      				<c:set var="_TypeNum" value="0"/>
								      				<c:forEach var="user" items="${_OtherUsers}" varStatus="index">
								      					<c:if test="${user.person.department.organizeId==department.organizeId}">
															<c:if test="${_TypeNum!=0 && _TypeNum%6==0}"></tr><tr></c:if>
															<td width="16%" valign="top">
																<form:checkbox path="personIds" value="${user.personId}"/> ${user.person.personName}
															</td>				
															<c:set var="_TypeNum" value="${_TypeNum+1}"/>
														</c:if>																			
													</c:forEach>
													<c:forEach begin="${_TypeNum%6}" end="5">
														<td width="16%">&nbsp;</td>
													</c:forEach>		      											
												</tr>
											</table>
										</td>
									</tr>
									
									<c:set var="_Num" value="${_Num+1}" />
								</c:forEach>
							</table>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		
		<div style="width: 90%" class="ui-state-default ui-jqgrid-pager ui-corner-bottom" dir="ltr">
			<input style="cursor: pointer;" type="button" value="保存" onclick="submitData();" id="submitbutton"/>
			<input style="cursor: pointer;" type="button" value="关闭" onclick="window.close();"/>
		</div>
	</div>
	</div>
</form:form>
</body>
</html>
                  
