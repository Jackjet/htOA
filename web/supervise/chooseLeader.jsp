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
	<style type="text/css">
		.buttonclass {
			font-weight:bold;
		}	
	</style>
	<script>
		
		var i=0,number;
		//全选操作
		function selectUserId(checkbox,organizeId){
			var isChecked = false;
			if(checkbox.checked){
				isChecked = true;
			}
			var obj;
			obj = document.taskLeaderForm.personIds;		
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
		}
		
		function selAll(checkbox,roleId){
			$('#'+checkbox.id).checkbox().toggle($("input[id='checker"+roleId+"']"));
		}
		
		
		//定义一个数组，记录各个数据点击的次数
		var clickTimes =new Array();
	</script>
	<style>
		#tdContent td{
			
		}
	</style>
	<base target="_self"/>
</head>
<body onload="window.scrollTo(0,document.body.scrollHeight); ">
	<form:form commandName="taskLeaderVo" name="taskLeaderForm" id="taskLeaderForm" action="/supervise/superviseInfor.do?method=saveChoose" method="post" enctype="multipart/form-data">
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
											<b><font color=red>选择审核领导</font></b>
										</td>
									</tr>
									<tr style="height: 30px;"><!--  class="ui-widget-content jqgrow ui-row-ltr" -->
										<td class="ui-state-default jqgrid-rownum" >审核领导：</td>
										<td colspan=3 id="idContent">
											<table width="100%" id="partUsers">
												<c:forEach var="role" items="${_Roles}">
													<c:if test="${!empty role && !empty role.users}">
														<tr>
															<td colspan="2"><strong>${role.roleName}：</strong></td>
														</tr>
														
														<tr id="checker${role.roleId}">
															<td width="9%" align="right" valign="top">
																<input type="checkbox" id="selAllBox${role.roleId}" onclick="selAll(this,'${role.roleId}')" />全选
															</td>
															<td style="padding-left:10px;" width="92%">
																<table>
																	<tr>
																		<!-- 部门建设、内控类只显示与下发人同部门的人员 -->
																		<c:choose>
																			<c:when test="${_SuperviseInfor.taskCategory.categoryType == 3 || _SuperviseInfor.taskCategory.categoryType == 4}">
																				
																				<c:set var="_TypeNum" value="0"/>
															      				<c:forEach var="user" items="${role.users}" varStatus="index">
															      					<c:if test="${_SuperviseInfor.creater.person.department.organizeId == user.person.department.organizeId}">
															      						<c:if test="${_TypeNum!=0 && _TypeNum%6==0}"></tr><tr></c:if>
																						<td width="16%" valign="top">
																							<form:checkbox path="personIds" value="${user.personId}" id="checker${role.roleId}"/> ${user.person.personName}
																						</td>				
																						<c:set var="_TypeNum" value="${_TypeNum+1}"/>
															      					</c:if>
																				</c:forEach>
																				<c:forEach begin="${_TypeNum%6}" end="5">
																					<td width="16%">&nbsp;</td>
																				</c:forEach>	
																			</c:when>
																			<c:otherwise>
																				<c:set var="_TypeNum" value="0"/>
															      				<c:forEach var="user" items="${role.users}" varStatus="index">
																					<c:if test="${_TypeNum!=0 && _TypeNum%6==0}"></tr><tr></c:if>
																					<td width="16%" valign="top">
																						<form:checkbox path="personIds" value="${user.personId}" id="checker${role.roleId}"/> ${user.person.personName}
																					</td>				
																					<c:set var="_TypeNum" value="${_TypeNum+1}"/>
																				</c:forEach>
																				<c:forEach begin="${_TypeNum%6}" end="5">
																					<td width="16%">&nbsp;</td>
																				</c:forEach>	
																			</c:otherwise>
																		</c:choose>
																	
													      					      											
																	</tr>
																</table>
															</td>
														</tr>
													</c:if>
												</c:forEach>
											</table>
										
										
											<table width="100%" id="allUsers" style="display: none;">
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
															<table border=0>
																<tr>
												      				<c:set var="_TypeNum" value="0"/>
												      				<c:forEach var="user" items="${_Users}" varStatus="index">
												      					<c:if test="${user.person.department.organizeId==department.organizeId}">
																			<c:if test="${_TypeNum!=0 && _TypeNum%6==0}"></tr><tr></c:if>
																			<td width="16%" valign="top" style="border:0px;">
																				<form:checkbox path="personIds" value="${user.personId}"/> ${user.person.personName}
																			</td>				
																			<c:set var="_TypeNum" value="${_TypeNum+1}"/>
																		</c:if>																			
																	</c:forEach>
																	<c:forEach begin="${_TypeNum%6}" end="5">
																		<td width="16%" style="border:0px;">&nbsp;</td>
																	</c:forEach>		      											
																</tr>
																	
																<tr>
												      				<c:set var="_TypeNum" value="0"/>
												      				<c:forEach var="user" items="${_OtherUsers}" varStatus="index">
												      					<c:if test="${user.person.department.organizeId==department.organizeId}">
																			<c:if test="${_TypeNum!=0 && _TypeNum%6==0}"></tr><tr></c:if>
																			<td width="16%" valign="top" style="border:0px;">
																				<form:checkbox path="personIds" value="${user.personId}"/> ${user.person.personName}
																			</td>				
																			<c:set var="_TypeNum" value="${_TypeNum+1}"/>
																		</c:if>																			
																	</c:forEach>
																	<c:forEach begin="${_TypeNum%6}" end="5">
																		<td width="16%" style="border:0px;">&nbsp;</td>
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
