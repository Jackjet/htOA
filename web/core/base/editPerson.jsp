<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<title>编辑用户</title>
<script src="/js/jquery-1.8.3.js" type="text/javascript"></script>
<script src="/js/commonFunction.js" type="text/javascript"></script>
<script src="<c:url value="/"/>js/multisearch.js"></script> <!--加载模态多条件查询相关js-->


<script src="<c:url value="/"/>datePicker/WdatePicker.js" type="text/javascript"></script>
<%--<script src="<c:url value="/"/>components/jquery-1.4.2.js" type="text/javascript"></script> <!--jquery包-->--%>

<script src="<c:url value="/"/>components/jqgrid/js/jquery-ui-1.8.2.custom.min.js" type="text/javascript"></script><!--jquery ui-->
<script src="<c:url value="/"/>components/jquery.layout.js" type="text/javascript"></script><!--jquery 布局-->
<script src="<c:url value="/"/>components/jqgrid/js/grid.locale-cn.js" type="text/javascript"></script> <!--jqgrid 中文包-->
<script src="<c:url value="/"/>components/jqgrid/js/jquery.jqGrid.min.js" type="text/javascript"></script> <!--jqgrid 包-->
<script src="<c:url value="/"/>js/changeclass.js"></script>
<link type="text/css" href="<c:url value="/"/>components/jqgrid/css/jquery.ui.all.css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
<link rel="stylesheet" type="text/css" media="screen" href="/css/noTdBottomBorder.css" />

<!-- formValidator -->
<link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css"></link>
<script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript" charset="UTF-8"></script>
<script src="<c:url value="/"/>components/formvalidator/js/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
<!-- ------------- -->

<script>
	//验证
	$(document).ready(function(){
		//$("#personName").val($("#personName").val()+" ");
		//$("#personName").val($("#personName").val().trim());
		$.formValidator.initConfig({formid:"personForm",onerror:function(msg){alert(msg)},onsuccess:function(){submitData();}});
		$("#personName").formValidator({onshow:"请输入姓名",onfocus:"员工姓名不能为空",oncorrect:"输入正确"}).inputValidator({min:1,onerror:"员工姓名不能为空,请确认"})
		/*.ajaxValidator({
			type : "get",
			url : "/core/personInfor.do?method=valPersonName&personId=${param.personId}",
			datatype : "json",
			success : function(returnMap){
		       if (returnMap.isDuplicate){
		        	return false;
		       }else {
		        	return true;
		       }
			},
			buttons: $("#button"),
			error: function(){alert("服务器没有返回数据，可能服务器忙，请重试");},
			onerror : "该姓名已存在，请更换姓名",
			onwait : "正在对姓名进行合法性校验，请稍候..."
		});*/
		
		.functionValidator({
		    fun:function(val,elem){
		        var isDuplicate = false;
		        $.ajax({
					url: "/core/personInfor.do?method=valPersonName&personId=${param.personId}&personName="+encodeURI(val),
					type: "post",
					dataType: "json",
					async: false,	//设置为同步
					beforeSend: function (xhr) {
					},
					complete : function (req, err) {
						var returnValues = eval("("+req.responseText+")");
						if (returnValues["isDuplicate"]){
					        alert("该姓名已存在，请更换姓名");
					        isDuplicate = true;
					    }else {
					    	isDuplicate = false;
					    }
					}
				});
				if (isDuplicate) {
					return false;
				}else {
					return true;
				}
			}
		});
		
		$("#departmentId").formValidator({onshow:"请选择部门",onfocus:"部门必须选择",oncorrect:"选择正确"}).inputValidator({min:1,onerror: "请选择部门"});
		$("#positionLayer").formValidator({onshow:"请输入职级",onfocus:"只能输入阿拉伯数字,数值越小职级越高",oncorrect:"输入正确"}).inputValidator({min:1,max:99,type:"value",onerrormin:"输入的值必须大于等于1",onerror:"只能输入阿拉伯数字,请确认"});//.defaultPassed();
		$("#mobile").formValidator({empty:true,onshow:"请输入手机号码,可以为空",onfocus:"输入时要按手机格式",oncorrect:"输入正确"}).inputValidator({min:11,max:11,onerror:"手机号码必须是11位的,请确认"}).regexValidator({regexp:"mobile",datatype:"enum",onerror:"输入的手机号码格式不正确"});
		$("#email").formValidator({empty:true,onshow:"请输入邮箱,可以为空",onfocus:"邮箱6-100个字符",oncorrect:"输入正确"}).inputValidator({min:6,max:100,onerror:"输入的邮箱长度非法,请确认"}).regexValidator({regexp:"^([\\w-.]+)@(([[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.)|(([\\w-]+.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(]?)$",onerror:"输入的邮箱格式不正确"})
		.functionValidator({
		    fun:function(val,elem){
		        var isDuplicate = false;
		        $.ajax({
					url: "/core/personInfor.do?method=valPersonEmail&personId=${param.personId}&email="+val,
					type: "post",
					dataType: "json",
					async: false,	//设置为同步
					beforeSend: function (xhr) {
					},
					complete : function (req, err) {
						var returnValues = eval("("+req.responseText+")");
						if (returnValues["isDuplicate"]){
					        alert("该邮箱已存在，请更换邮箱名");
					        isDuplicate = true;
					    }else {
					    	isDuplicate = false;
					    }
					}
				});
				if (isDuplicate) {
					return false;
				}else {
					return true;
				}
			}
		});
		$("#emailPassword").formValidator({empty:true,onshow:"请输入邮箱密码,可以为空",onfocus:"邮箱密码不得小于6位",oncorrect:"输入正确"}).inputValidator({min:6,max:100,onerror:"输入的密码长度不够,请重新输入"});
		$("#officePhone").formValidator({empty:true,onshow:"请输入办公室电话,可以为空",onfocus:"格式例如：021-888888",oncorrect:"输入正确"}).regexValidator({regexp:"^[[0-9]{3}-|\[0-9]{4}-]?([0-9]{8}|[0-9]{7})?$",onerror:"输入的电话格式不正确"});
		$("#homePhone").formValidator({empty:true,onshow:"请输入家庭电话,可以为空",onfocus:"格式例如：021-888888",oncorrect:"输入正确"}).regexValidator({regexp:"^[[0-9]{3}-|\[0-9]{4}-]?([0-9]{8}|[0-9]{7})?$",onerror:"输入的电话格式不正确"});
	});

	//提交数据
    function submitData() {
        var form = document.personForm;
        <%--var browserName=navigator.userAgent.toLowerCase();--%>
        <%--if(/chrome/i.test(browserName)&&/webkit/i.test(browserName)&&/mozilla/i.test(browserName)){--%>
			<%--//如果是chrome浏览器--%>
			<%--$.ajax({--%>
				<%--type: "POST",--%>
				<%--url:'<c:url value="/core/personInfor.do"/>?method=save',--%>
				<%--data:$('#personForm').serialize(),--%>
				<%--async: false--%>

			<%--});--%>
		<%--}else{--%>
			form.submit();
//		}
        alert('信息编辑成功！');
//        window.close();
		window.opener.reload();
//        alert('信息编辑成功！');
//        window.close();

//            window.returnValue = "refresh";
//    	    window.opener.reload();
//            window.close();

    }

</script>
<base target="_self"/>
<body style="padding:0 100px">
<br/>
<form:form commandName="personInforVo" id="personForm" name="personForm" action="/core/personInfor.do?method=save" method="post" enctype="multipart/form-data">
<form:hidden path="personId"/>
<form:hidden path="deleted"/>
<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 100%">
<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
  <div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
    <span class="ui-jqgrid-title">编辑用户</span>
  </div>

	<div class="ui-jqgrid-bdiv" style="height: auto; width: 100%">
		<div style="position: relative;">
			<div>
				<table cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable" style="width: 100%">
					<tbody>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum" style="width: 15%">姓名：</td>
							<td><form:input path="personName" size="20"/></td>
						    <td style="width: 25%"><div id="personNameTip" style="width:250px"></div></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">人员编号：</td>
							<td><form:input path="personNo" size="20"/></td>
							<td><div id="personNoTip" style="width:250px"></div></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">性别：</td>
							<td><form:select path="gender">
	                      		<form:option value="0">男</form:option>
	                      		<form:option value="1">女</form:option>
	                      	</form:select></td>
						    <td><div id="genderTip" style="width:250px"></div></td>
						</tr>
						
						<%--<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">头像：</td>
							<td>
								<table cellpadding="0" cellspacing="0" style="margin-bottom:0;margin-top:0">
									<tr>
										<td><input type="file" name="attachment" size="50" />&nbsp;</td>
									</tr>
								</table>
							</td>
							<td><div id="attachmentTip" style="width:250px"></div></td>
						</tr>
						<c:if test="${!empty _Attachment_Names}">
							<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
								<td class="ui-state-default jqgrid-rownum">原头像信息(<font color=red>如果要删除原头像，请选择该头像前面的选择框</font>)：</td>
								<td><c:forEach var="file" items="${_Attachments}" varStatus="index">
											<input type="checkbox" name="attatchmentArray" value="${index.index}" />
												<a href="<c:url value="${'/common/'}"/>download.jsp?filepath=${file}">${_Attachment_Names[index.index]}</a><br/>
										</c:forEach></td>
							</tr>
						</c:if>--%>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td style="width: 20%;" class="ui-state-default jqgrid-rownum">所属部门：</td>
							<td style="width: 45%;"><form:select path="departmentId">
	                      		<form:option value="0">--选择部门--</form:option>
	                      		<c:forEach items="${_Departments}" var="department">
									<form:option value="${department.organizeId}">${department.organizeName}</form:option>
								</c:forEach>
	                      	</form:select></td>
					         <td style="width: 35%;"><div id="departmentIdTip" style="width:250px"></div></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td style="width: 20%;" class="ui-state-default jqgrid-rownum">所属班组：</td>
							<td style="width: 45%;"><form:select path="groupId">
	                      		<form:option value="0">--选择班组--</form:option>
	                      		<c:forEach items="${_Groups}" var="group">
									<form:option value="${group.organizeId}">${group.organizeName}</form:option>
								</c:forEach>
	                      	</form:select></td>
					         <td style="width: 35%;"><div id="groupIdTip" style="width:250px"></div></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td style="width: 20%;" class="ui-state-default jqgrid-rownum">岗位：</td>
							<td style="width: 45%;"><form:select path="structureId">
	                      		<c:forEach items="${_Structures}" var="structure">
									<form:option value="${structure.structureId}">
										<c:forEach begin="0" end="${structure.layer}">&nbsp;&nbsp;&nbsp;</c:forEach>
										<c:if test="${structure.layer==1}"><b>+</b></c:if><c:if test="${structure.layer==2}"><b>-</b></c:if>${structure.structureName}				
									</form:option>
								</c:forEach>
	                      	</form:select></td>
					         <td style="width: 35%;"><div id="structureIdTip" style="width:250px"></div></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">职级：</td>
							<td><form:input path="positionLayer" size="20"/></td>
							<td><div id="positionLayerTip" style="width:250px"></div></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">手机：</td>
							<td><form:input path="mobile" size="40"/></td>
						    <td><div id="mobileTip" style="width:250px"></div></td>
						</tr>

						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">邮箱：</td>
							<td><form:input path="email" size="40"/></td>
						    <td><div id="emailTip" style="width:250px"></div></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">邮箱密码：</td>
							<td><input type="password" name="emailPassword" id="emailPassword" value="${_EmailPassword}"/></td>
						    <td><div id="emailPasswordTip" style="width:250px"></div></td>
						</tr>
						
						<%--<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">出生日期：</td>
							<td><input name="birthday" id="birthday" size="20" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" value="${_Birthday}" readonly="readonly"/></td>
						    <td><div id="birthdayTip" style="width:250px"></div></td>
						</tr>
						--%>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">办公室地址：</td>
							<td><form:input path="officeAddress" size="60"/></td>
						    <td><div id="officeAddressTip" style="width:250px"></div></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">办公室电话：</td>
							<td><form:input path="officePhone" size="60"/></td>
						    <td><div id="officePhoneTip" style="width:250px"></div></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">办公室邮编：</td>
							<td><form:input path="officeCode" size="60"/></td>
						    <td><div id="officeCodeTip" style="width:250px"></div></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">家庭地址：</td>
							<td><form:input path="homeAddress" size="60"/></td>
						    <td><div id="homeAddressTip" style="width:250px"></div></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">家庭电话：</td>
							<td><form:input path="homePhone" size="60"/></td>
						    <td><div id="homePhoneTip" style="width:250px"></div></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">家庭邮编：</td>
							<td><form:input path="postCode" size="60"/></td>
						    <td><div id="postCodeTip" style="width:250px"></div></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">备注：</td>
							<td><form:textarea path="memo" cols="50" rows="5"/></td>
						    <td><div id="memoTip" style="width:250px"></div></td>
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
										<td><input style="cursor: pointer;" type="button" id="aa" onclick="submitData()" value="提交"/></td>
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
