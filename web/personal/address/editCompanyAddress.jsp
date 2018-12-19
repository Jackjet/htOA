<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<title>编辑公司通讯录</title>
<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
<script src="<c:url value="/"/>datePicker/WdatePicker.js" type="text/javascript"></script>
<script src="<c:url value="/"/>js/changeclass.js"></script>
<link type="text/css" href="<c:url value="/"/>components/jqgrid/css/jquery.ui.all.css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
<link rel="stylesheet" type="text/css" media="screen" href="/css/noTdBottomBorder.css" />

<!-- formValidator -->
<link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css"></link>
<script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript" charset="UTF-8"></script>
<script src="<c:url value="/"/>components/formvalidator/js/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>

<script>
	//验证
	$(document).ready(function(){
		//alert(${param.rowId});
		$.formValidator.initConfig({formid:"companyAddressForm",onerror:function(msg){alert(msg)},onsuccess:function(){submitData();}});
		$("#department").formValidator({onshow:"请输入部门",onfocus:"部门为必选项",oncorrect:"输入正确"}).inputValidator({min:1,onerror: "请输入部门"});
		$("#personName").formValidator({onshow:"请输入姓名",onfocus:"如：张某",oncorrect:"输入正确"}).inputValidator({min:1,onerror:"请输入姓名"});
		//$("#mobile").formValidator({empty:true,onshow:"请输入手机号码,可以为空",onfocus:"输入时要按手机格式",oncorrect:"输入正确"}).inputValidator({min:11,max:11,onerror:"手机号码必须是11位的,请确认"}).regexValidator({regexp:"mobile",datatype:"enum",onerror:"输入的手机号码格式不正确"});
		$("#email").formValidator({empty:true,onshow:"请输入邮箱,可以为空",onfocus:"邮箱6-100个字符",oncorrect:"输入正确"}).inputValidator({min:6,max:100,onerror:"输入的邮箱长度非法,请确认"}).regexValidator({regexp:"^([\\w-.]+)@(([[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.)|(([\\w-]+.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(]?)$",onerror:"输入的邮箱格式不正确"});
		//$("#officePhone").formValidator({empty:true,onshow:"请输入办公室电话,可以为空",onfocus:"格式例如：021-888888",oncorrect:"输入正确"}).regexValidator({regexp:"^[[0-9]{3}-|\[0-9]{4}-]?([0-9]{8}|[0-9]{7})?$",onerror:"输入的电话格式不正确"});
		//$("#homePhone").formValidator({empty:true,onshow:"请输入家庭电话,可以为空",onfocus:"格式例如：021-888888",oncorrect:"输入正确"}).regexValidator({regexp:"^[[0-9]{3}-|\[0-9]{4}-]?([0-9]{8}|[0-9]{7})?$",onerror:"输入的电话格式不正确"});
	});
	
	//提交数据
	function submitData() {
		/** var form = document.companyAddressForm;
		form.action = '<c:url value="/personal/address/companyAddressInfor.do"/>?method=save';
		form.submit(); */
		alert('信息编辑成功！');
		//var categoryId = document.getElementById("categoryId");
		var returnArray = "refresh";
		window.returnValue = returnArray;
		window.close();
	}

</script>
<base target="_self"/>


<body style="overflow-y: auto;padding: 0 100px">
<br/>
	<form:form commandName="companyAddressVo" id="companyAddressForm" name="companyAddressForm" action="/personal/address/companyAddressInfor.do?method=save" method="post">
	<input type="hidden" name="rowId" value="${param.rowId}"/>
		<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 100%">
			<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
			  <div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
			    <span class="ui-jqgrid-title">编辑公司通讯录</span>
			  </div>
			
				<div class="ui-jqgrid-bdiv" style="height: auto; width: 100%">
					<div style="position: relative;">
						<div>
							<table cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable" style="width: 100%">
								<tbody>
									
									<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
										<td class="ui-state-default jqgrid-rownum" style="width: 13%">姓名：</td>
										<td style="width: 30%"><form:input path="personName" size="20" /></td>
									    <td><div id="personNameTip" style="width:250px"></div></td>
									</tr>
									
									<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
										<td class="ui-state-default jqgrid-rownum">职位：</td>
										<td><form:input path="position" size="20" /></td>
									    <td><div id="positionTip" style="width:250px"></div></td>
									</tr>
									
									<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
										<td class="ui-state-default jqgrid-rownum">部门：</td>
										<td><form:input path="department" size="20" /></td>
									    <td><div id="departmentTip" style="width:250px"></div></td>
									</tr>
									
									<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
										<td class="ui-state-default jqgrid-rownum">手机：</td>
										<td><form:input path="mobile" size="20" /></td>
									    <td><div id="mobileTip" style="width:250px"></div></td>
									</tr>
									
									<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
										<td class="ui-state-default jqgrid-rownum">邮箱：</td>
										<td><form:input path="email" size="20" /></td>
									    <td><div id="emailTip" style="width:250px"></div></td>
									</tr>
									
									<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
										<td class="ui-state-default jqgrid-rownum">性别：</td>
										<td><form:select path="gender">
								              <form:option value="0">男</form:option>
								              <form:option value="1">女</form:option>
								          </form:select></td>
									    <td><div id="genderTip" style="width:250px"></div></td>
									</tr>
									
									<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
										<td class="ui-state-default jqgrid-rownum">出生日期：</td>
										<td><input type="text" name="birthday" id="birthday" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly="true" size="20" value="${_Birthday}"/></td>
									    <td><div id="birthdayTip" style="width:250px"></div></td>
									</tr>
									
									<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
										<td class="ui-state-default jqgrid-rownum">备注说明：</td>
										<td><form:input path="memo" size="20" /></td>
									    <td><div id="memoTip" style="width:250px"></div></td>
									</tr>
									
									<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
										<td class="ui-state-default jqgrid-rownum">办公室地址：</td>
										<td><form:input path="officeAddress" size="35" /></td>
									    <td><div id="officeAddressTip" style="width:250px"></div></td>
									</tr>
									
									<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
										<td class="ui-state-default jqgrid-rownum">办公室电话：</td>
										<td><form:input path="officePhone" size="20" /></td>
									    <td><div id="officePhoneTip" style="width:250px"></div></td>
									</tr>
									
									<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
										<td class="ui-state-default jqgrid-rownum">办公室邮编：</td>
										<td><form:input path="officeCode" size="20" /></td>
									    <td><div id="officeCodeTip" style="width:250px"></div></td>
									</tr>
									
									<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
										<td class="ui-state-default jqgrid-rownum">家庭地址：</td>
										<td><form:input path="homeAddress" size="35" /></td>
									    <td><div id="homeAddressTip" style="width:250px"></div></td>
									</tr>
									
									<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
										<td class="ui-state-default jqgrid-rownum">家庭电话：</td>
										<td><form:input path="homePhone" size="20" /></td>
									    <td><div id="homePhoneTip" style="width:250px"></div></td>
									</tr>
									
									<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
										<td class="ui-state-default jqgrid-rownum">家庭邮编：</td>
										<td><form:input path="postCode" size="20" /></td>
									    <td><div id="postCodeTip" style="width:250px"></div></td>
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
													<td><input style="cursor: pointer;" type="submit" id="button" value="提交"/></td>
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

