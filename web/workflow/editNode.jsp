<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
<title>编辑节点信息</title>
<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
<script src="<c:url value="/"/>js/inc_javascript.js" type="text/javascript"></script>
<script src="<c:url value="/"/>js/addattachment.js"></script>
<link type="text/css" href="<c:url value="/"/>components/jqgrid/css/jquery.ui.all.css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />

<!-- formValidator -->
<link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css"></link>
<script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript" charset="UTF-8"></script>
<script src="<c:url value="/"/>components/formvalidator/js/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>

<script>
	/**
	$(document).ready(function(){
		//流程信息验证
		$.formValidator.initConfig({formid:"nodeInforForm",onerror:function(msg){alert(msg)},onsuccess:function(){submitData();}});
		$("#flowNodeName").formValidator({onshow:"请输入节点名称",onfocus:"节点名称不能为空",oncorrect:"输入正确"}).inputValidator({min:1,onerror: "请输入节点名称"});
	}); 
	*/
	
	//提交数据
	function submitData(){
		if (validate()) {
			$.ajax({
				url: "/workFlowDefinition.do?method=saveNode",
				type: "post",
				dataType: "json",
				data: $('#nodeInforForm').serialize(), // 从表单中获取数据
				async: false,	//设置为同步
				beforeSend: function (xhr) {
				},
				complete : function (req, err) {
					var returnValues = eval("("+req.responseText+")");
					if (returnValues["warningStr"] != null) {
						alert(returnValues["warningStr"]);
					}else {
						alert('信息编辑成功！');
						window.returnValue = "refresh";
						window.close();
					}
				}
			});
		}
	}
	
	//验证
	function validate() {
		var flowNodeName = document.getElementById('flowNodeName');
		//验证节点名称
		if (flowNodeName.value != null && flowNodeName.value != '') {
			//验证来源节点
			/** var checkedNum = 0;
			var fromNodeIds = document.getElementsByName('fromNodeIds');
			if (fromNodeIds != null && fromNodeIds.length > 0) {
				for (var i=0;i<fromNodeIds.length;i++) {
					if (fromNodeIds[i].checked) {
						checkedNum++;
					}
				}
			}
			if (checkedNum > 0) {
				
			}else {
				alert('请选择来源节点！');
			} */
			return true;
		}else {
			alert('请输入节点名称！');
			return false;
		}
	}
	
	//全选操作
	function selectUserId(checkbox,obj,organizeId){
		var isChecked = false;
		if(checkbox.checked){
			isChecked = true;
		}
		var userIds = document.getElementsByName('userIds');
		var addCheckBox = document.getElementsByName('addCheckBox');
			
		if(obj!=null){
			if(obj.length==null){
				//只有一个,则只需要判断该用户是不是这个分类
				<c:forEach var="user" items="${_Users}" varStatus="index">
					var tempDepartmentId = '${user.person.department.organizeId}';
					var tempGroupId = '${user.person.group.organizeId}';
					if(organizeId==tempDepartmentId || organizeId==tempGroupId){
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
					userId = obj[k].value;
					<c:forEach var="user" items="${_Users}" varStatus="index">
						var tempDepartmentId = '${user.person.department.organizeId}';
						var tempGroupId = '${user.person.group.organizeId}';
						var tempUserId = '${user.personId}';
						if((organizeId==tempDepartmentId && tempUserId==userId) || (organizeId==tempGroupId && tempUserId==userId)){
							obj[k].checked = isChecked;
						}
					</c:forEach>
				}
			}
		}
	}
	
</script>
<base target="_self"/>
</head>
<body style="padding-left: 100px">
<form:form commandName="flowNodeVo" id="nodeInforForm" name="nodeInforForm" action="/workFlowDefinition.do?method=saveNode" method="post">
<form:hidden path="nodeId" />
<form:hidden path="flowId" />
<form:hidden path="layer" />
<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 100%">
<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
    <div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
    	<span class="ui-jqgrid-title">编辑节点信息</span>
    </div>

	<div class="ui-jqgrid-bdiv" style="height: auto; width: 100%">
		<div style="position: relative;">
			<div>
				<table cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable" style="width: 100%" id="addTable">
					<tbody>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td width="15%" class="ui-state-default jqgrid-rownum">节点名称：</td>
							<td width="70%" colspan="2"><form:input path="flowNodeName" size="30"/></td>
						    <%--<td width="15%"><div id="flowNodeNameTip" style="width:250px"></div></td>--%>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">所属流程：</td>
							<td colspan="2">${_Flow.flowName}</td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">节点类型：</td>
							<td colspan="2"><form:select path="nodeType">
								<form:option value="1">普通节点</form:option>
								<form:option value="2">分叉节点</form:option>
								<form:option value="3">聚合节点</form:option>
								<%--<form:option value="4">状态节点</form:option>--%>
							</form:select></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">是否分叉内节点：</td>
							<td colspan="2"><form:select path="forked">
								<form:option value="0">否</form:option>
								<form:option value="1">是</form:option>
							</form:select></td>
						</tr>
						
						<%--<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">来自分叉的节点：</td>
							<td colspan="2"><form:select path="forkedNodeId"><form:option value="0">--请选择--</form:option>
							            <c:forEach items="${_ForkedNodes}" var="node">
							            	<option value="${node.nodeId}">${node.nodeName}</option>
										</c:forEach>
							         </form:select></td>
						</tr>--%>
						
						<c:if test="${empty param.nodeId}">
							<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
								<td class="ui-state-default jqgrid-rownum">可选来源节点：</td>
								<td colspan="2"><c:forEach items="${_FromNodes}" var="node"><form:checkbox path="fromNodeIds" value="${node.nodeId}"/>${node.nodeName}<br/></c:forEach></td>
							</tr>
						</c:if>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">审核结束类型：</td>
							<td colspan="2"><form:select path="finishType">
								<form:option value="1">只要有一人通过审批即向下流转</form:option>
								<form:option value="2">只有全部人员通过审批才向下流转</form:option>
							</form:select></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">可否打印：</td>
							<td colspan="2"><form:select path="printable">
								<form:option value="1">可以</form:option>
								<form:option value="0">不可以</form:option>
							</form:select></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">可否下载附件：</td>
							<td colspan="2"><form:select path="download">
								<form:option value="1">可以</form:option>
								<form:option value="0">不可以</form:option>
							</form:select></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">可否上传附件：</td>
							<td colspan="2"><form:select path="upload">
								<form:option value="1">可以</form:option>
								<form:option value="0">不可以</form:option>
							</form:select></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">节点说明：</td>
							<td colspan="2"><form:textarea path="memo" cols="65" rows="5"></form:textarea></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">审核者类型：</td>
							<td colspan="2"><form:select path="checkerType" id="checkerType">
								<form:option value="0">手工设定</form:option>
								<form:option value="1">自定义节点</form:option>
								<form:option value="2">岗位</form:option>
								<form:option value="3">部门</form:option>
								<form:option value="4">人员</form:option>
								<form:option value="11">特殊审核人</form:option>
							</form:select></td>
						</tr>
						
						<tr style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">审核者：</td>
							<td id="checkerTypeCon" colspan="2">
								<form:select id='structure_' path="structure_" style="display: none;">
									<form:option value="0">--选择岗位--</form:option>
									<c:forEach items="${_Structures}" var="str">
										<form:option value="${str.structureId}">${str.structureName}</form:option>
									</c:forEach>
								</form:select>
								<form:select id='department_' path='department_' style="display: none;">
									<form:option value="0">--选择部门--</form:option>
									<c:forEach items="${_Departments}" var='dep'>
										<form:option value="${dep.organizeId}">${dep.organizeName}</form:option>
									</c:forEach>
								</form:select>
								<form:select id='user_' path='user_' style="display: none;">
									<form:option value="0">--选择人员--</form:option>
									<c:forEach items="${_Users}" var="user">
										<form:option value="${user.personId}">${user.person.personName}</form:option>
									</c:forEach>
								</form:select>
								<form:select path="special" id="special" style="display:none;">
									<form:option value="0">--选择特殊审核人--</form:option>
									<form:option value="1">部门领导</form:option>
									<form:option value="2">申请者</form:option>
								</form:select>
								<div id="typeDiv" style="display:none;">
									<input type="radio" class="rightType" name="rightType" value="0"/>角色 
									<input type="radio" class="rightType" name="rightType" value="1"/>用户
								</div>
									<!-- 角色 -->
									<table width="100%" id="roleTab" style="display:none;">
										<tr>
											<c:set var="_TypeNum" value="0"/>
												<c:forEach var="role" items="${_Roles}">
													<c:if test="${_TypeNum!=0 && _TypeNum%6==0}"></tr><tr></c:if>
														<td width="16%" valign="top"><form:checkbox path="roleIds" value="${role.roleId}"/>${role.roleName}</td>
													<c:set var="_TypeNum" value="${_TypeNum+1}"/>
												</c:forEach>
												<c:forEach begin="${_TypeNum%6}" end="5">
													<td width="16%">&nbsp;</td>
												</c:forEach>
										</tr>
									</table>
												
								<script language="javaScript">
								   	//定义一个数组，记录各个数据点击的次数
									var clickTimes =new Array();	
								</script>
								
								<!-- 用户 -->
								<table width="100%" id="userTab" style="display:none;">
					            	<tr>
					            		<td colspan="2" width="100%">
					            			<input type="checkbox" onclick="selectAll(this,nodeInforForm.addCheckBox,nodeInforForm.userIds)"/>选择全部
					            		</td>
					            	</tr>
					            	<c:set var="_Num" value="0"/>
					        		<c:forEach var="organize" items="${_Departments}" varStatus="index">
					          			<tr height="33">
					             			<td valign="bottom" width="100%" style="border-bottom:1px dotted #888888;font-size:10pt" colspan="2">
								               <script language="javaScript">
												clickTimes[${_Num}] = 0;							
											   </script>	
					                 			${organize.organizeName}&nbsp;<span onclick="show_list('${_Num}')">
					                 			<img name="img" src="<c:url value="${'/images'}"/>/xpexpand3_s.gif" style="margin-top:0px;margin-bottom:0px;"/></span>
							     			</td>
						      			</tr>
						      			<tr name="tr" style="display:none;padding-top:10px;">
						      				<td align="right" valign="top" width="15%">
						      					<input type="checkbox" onclick="selectUserId(this,nodeInforForm.userIds,'${organize.organizeId}')" name="addCheckBox"/>全选
						      				</td>
						      				<td style="padding-left:1px;" width="85%" >
						      					<table width="100%" id="userTable">
									      			<tr>
									      				<c:set var="_TypeNum" value="0"/>
									      				<c:forEach var="user" items="${_Users}" varStatus="index">
									      					<c:if test="${user.person.department.organizeId==organize.organizeId || user.person.group.organizeId==organize.organizeId}">
																<c:if test="${_TypeNum!=0 && _TypeNum%6==0}">
																	</tr><tr>
																</c:if>
																<td width="14%" valign="top">
																	<form:checkbox path="userIds" value="${user.personId}"/>
																	 ${user.person.personNo}-${user.person.personName}
																</td>					
																<c:set var="_TypeNum" value="${_TypeNum + 1}"/>
															</c:if>																			
														</c:forEach>
																					
														<c:forEach begin="${_TypeNum%6}" end="5">
															<td width="14%">&nbsp;</td>
														</c:forEach>		      											
													</tr>
												</table>
						      				</td>
						      		    </tr>
					          			<c:set var="_Num" value="${_Num+1}"/>
									</c:forEach>
								</table>
						  	</td>
						</tr>   
						   
						<script>
							changeType();
							
							//更改审核者类型
							$("#checkerType").change(function(){changeType();});
							
							function changeType() {
								//审核者类型选中的选项值
							    var selectId = $("#checkerType option:selected").val();
							    //alert(selectId);
							    
							    //手工设定审核人员
							    if(selectId == 0){
							    	$("#checkerTypeCon select").each(function() {
										if($(this).css("display")=="block"){
											$(this).css("display","none");
										}
										//$(this).val("0");
									});
										
								    $("#typeDiv").css("display","none");
								    $(".rightType").each(function(){
								    	$(this).removeAttr("checked");
								    });
								    $("#checkerTypeCon table").each(function(){
								    	$(this).css("display","none");
								    });
								    //$("#handle").remove();
								    //$("#checkerTypeCon").prepend("<input id='handle' name='handle' />");
							    }
							    
							    //自定义 
								if(selectId == 1){
							    	$("#checkerTypeCon select").each(function() {
										if($(this).css("display")=="block"){
											$(this).css("display","none");
										}
										//$(this).val("0");
									});
							    	
									$("#typeDiv").css("display","none");
									$(".rightType").each(function(){
							    		$(this).removeAttr("checked");
							    	});
							    	$("#checkerTypeCon table").each(function(){
							    		$(this).css("display","none");
							    	});
							    	//$("#handle").remove();
							    	
							    	$("#typeDiv").css("display","block");
							    	
							    	 //显示审核人(修改时)
								    if('${_CheckerPerson}'=='true'){
								    	$("input[name='rightType'][value=1]").attr("checked",true);
								    	displayUser();
								    }
								    //显示审核角色(修改时)
								   	if('${_CheckerRole}'=='true'){
								    	$("input[name='rightType'][value=0]").attr("checked",true);
								    	displayRole();
								   	}
							    	
							    	//选择人员或角色
							    	$(".rightType").click(function(){
							    		var checked = $("input[name='rightType']:checked").val();
								    	if(checked == 0){
								    		displayRole();
								    	}
								    	if(checked == 1){
								    		displayUser();
								    	}
							    	});
							    	
							    }
							    
							    //岗位
							    if(selectId == 2){
							    	$("#checkerTypeCon select").each(function() {
										if($(this).css("display")=="block"){
											$(this).css("display","none");
										}
										//$(this).val("0");
									});
							    	
							    	$("#typeDiv").css("display","none");
									$(".rightType").each(function(){
							    		$(this).removeAttr("checked");
							    	});
							    	$("#checkerTypeCon table").each(function(){
							    		$(this).css("display","none");
							    	});
							    	$("#structure_").css("display","block");
							    	//$("#handle").remove();
							    }
							    
							    //部门
							    if(selectId == 3){
							    	$("#checkerTypeCon select").each(function() {
										if($(this).css("display")=="block"){
											$(this).css("display","none");
										}
										//$(this).val("0");
									});
							    	
							    	$("#typeDiv").css("display","none");
									$(".rightType").each(function(){
							    		$(this).removeAttr("checked");
							    	});
							    	$("#checkerTypeCon table").each(function(){
							    		$(this).css("display","none");
							    	});
							    	$("#department_").css("display","block");
							    	//$("#handle").remove();
							    	
							    }
							    
							    //人员
							    if(selectId == 4){
							    	$("#checkerTypeCon select").each(function() {
										if($(this).css("display")=="block"){
											$(this).css("display","none");
										}
										//$(this).val("0");
									});
							    	
							    	$("#typeDiv").css("display","none");
									$(".rightType").each(function(){
							    		$(this).removeAttr("checked");
							    	});
							    	$("#checkerTypeCon table").each(function(){
							    		$(this).css("display","none");
							    	});
							    	$("#user_").css("display","block");
							    	//$("#handle").remove();
							    }
							    
							    //特殊审核人
							    if(selectId == 11){
							    	$("#checkerTypeCon select").each(function() {
										if($(this).css("display")=="block"){
											$(this).css("display","none");
										}
										//$(this).val("0");
									});
									
							    	$("#typeDiv").css("display","none");
									$(".rightType").each(function(){
							    		$(this).removeAttr("checked");
							    	});
							    	$("#checkerTypeCon table").each(function(){
							    		$(this).css("display","none");
							    	});
									$("#special").css("display","block");
							    	//$("#handle").remove();
							    }
							}
							
							function displayUser() {
								$("#userTab").css("display","block");
								$("table[id=userTable]").css("display","block");
								if($("#roleTab").css("display")=="block"){
								    $("#roleTab").css("display","none");	
								}
							}
							
							function displayRole() {
								$("#roleTab").css("display","block");
								if($("#userTab").css("display")=="block"){
								    $("#userTab").css("display","none");	
								}
							}
							
						</script>
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
										<td><input style="cursor: pointer;" type="button" value="提交" onclick="submitData();"/></td>
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
