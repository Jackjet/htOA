<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
<title>编辑部门[班组]信息</title>
<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
<script src="<c:url value="/"/>js/inc_javascript.js"></script>
<link type="text/css" href="<c:url value="/"/>components/jqgrid/css/jquery.ui.all.css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
<link rel="stylesheet" type="text/css" media="screen" href="/css/noTdBottomBorder.css" />

<!-- formValidator -->
<link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css"></link>
<script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript" charset="UTF-8"></script>
<script src="<c:url value="/"/>components/formvalidator/js/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
<!-- ------------- -->

<script>
	//验证
	$(document).ready(function(){
		$.formValidator.initConfig({formid:"organizeForm",onerror:function(msg){alert(msg)},onsuccess:function(){submitData();}});
		$("#organizeName").formValidator({onshow:"请输入部门[班组]名称",onfocus:"部门[班组]名称不能为空",oncorrect:"输入正确"}).inputValidator({min:1,onerror:"部门[班组]名称不能为空,请确认"});
		$("#shortName").formValidator({empty:true,onshow:"请输入部门简称,可以为空",onfocus:"格式例如：办,财",oncorrect:"输入正确"});
		$("#organizeNo").formValidator({empty:true,onshow:"请输入编号,可以为空",onfocus:"格式例如：YY,59",oncorrect:"输入正确"});
		$("#orderNo").formValidator({onshow:"请输入排序编号",onfocus:"只能输入阿拉伯数字",oncorrect:"输入正确"}).inputValidator({min:1,max:99,type:"value",onerrormin:"输入的值必须大于等于1",onerror:"只能输入阿拉伯数字,请确认"});
	
		showDirAndSupInfor();
	});
	
	//提交数据
	var form = document.getElementById('organizeForm');
	/*function submitData() {
		alert('信息编辑成功！');
		window.returnValue = "refresh";
		window.close();
	}*/
    function submitData() {
        var form = document.getElementById('organizeForm');
        form.submit();
        window.opener.reloadOrg();

    }
	
	//显示或隐藏董事、监事信息
	function showDirAndSupInfor() {
		var levelId = document.getElementById('levelId');
		var directors = document.getElementById('directorsTr');
		var supervisors = document.getElementById('supervisorsTr');
		if (levelId.value == '4') {
			directors.style.display = '';
			supervisors.style.display = '';
		}else {
			directors.style.display = 'none';
			supervisors.style.display = 'none';
		}
	}
</script>
</head>
<base target="_self"/>
<body style="padding: 0 100px">
<br/>
<form id="organizeForm" action="/core/organizeInfor.do?method=save" method="post">
<input type="hidden" name="organizeId" value="${_Organize.organizeId}"/>
<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 100%">
<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
  <div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
    <span class="ui-jqgrid-title">编辑部门[班组]信息</span>
  </div>

	<div class="ui-jqgrid-bdiv" style="height: auto; width: 100%">
		<div style="position: relative;">
			<div>
				<table cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable" style="width: 100%">
					<tbody>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td style="width: 20%;" class="ui-state-default jqgrid-rownum">部门[班组]名称：</td>
							<td style="width: 45%;"><input type="text" id="organizeName" name="organizeName" size="20" value="${_Organize.organizeName}"/></td>
							<td style="width: 35%;"><div id="organizeNameTip" style="width:250px"></div></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">部门简称：</td>
							<td><input type="text" id="shortName" name="shortName" size="20" value="${_Organize.shortName}"/></td>
							<td></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">编号：</td>
							<td><input type="text" id="organizeNo" name="organizeNo" size="20" value="${_Organize.organizeNo}"/></td>
							<td><div id="organizeNoTip" style="width:250px"></div></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">组织层级：</td>
							<td><select name="levelId" id="levelId" onchange="showDirAndSupInfor();">
									<option value="1">部门</option>
									<option value="2">班组</option>
									<option value="3">分公司</option>
									<option value="4">投资公司</option>
								</select><script language="javaScript">
									var form = document.getElementById('organizeForm');
									getOptsValue(form.levelId,"${_Organize.levelId}");
								</script></td>
							<td></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">排序编号：</td>
							<td><input type="text" id="orderNo" name="orderNo" size="20" value="${_Organize.orderNo}"/></td>
							<td><div id="orderNoTip" style="width:250px"></div></td>
						</tr>

						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">是否归口部门：</td>
							<td><select name="guikou">
								<option value="否">--请选择--</option>
								<option value="是">是</option>
								<option value="否">否</option>
							<td></td>
						</tr>

						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">所属部门[公司]：</td>
							<td><select name="parentId">
									<c:forEach items="${requestScope._TreeOrganizes}" var="organize"> 
										<c:choose>
											<c:when test="${_Organize.parent.organizeId == organize.organizeId}">
												<option value="${organize.organizeId}" selected="selected">
											</c:when>
											<c:otherwise>
												<option value="${organize.organizeId}">
											</c:otherwise>
										</c:choose>
											<c:forEach begin="0" end="${organize.layer}">&nbsp;</c:forEach>
											<c:if test="${organize.layer==1}"><b>+</b></c:if><c:if test="${organize.layer==2}"><b>-</b></c:if>${organize.organizeName}				
										</option>
									</c:forEach>
						        </select></td>
							<td></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">部门经理[主管]：</td>
							<td><select name="directorId">
									<option value="">--请选择人员--</option>
									<c:forEach items="${requestScope._Persons}" var="person"> 
										<c:choose>
											<c:when test="${_Organize.director.personId == person.personId}">
												<option value="${person.personId}" selected="selected">
											</c:when>
											<c:otherwise>
												<option value="${person.personId}">
											</c:otherwise>
										</c:choose>
											${person.personName}		
										</option>
									</c:forEach>
						        </select></td>
							<td></td>
						</tr>
						
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;display: none;" id="directorsTr">
							<td class="ui-state-default jqgrid-rownum" valign="top">董事信息：</td>
							<td><textarea id="directors" name="directors" cols="25" rows="2">${_DirAndSup.directors}</textarea></td>
							<td></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;display: none;" id="supervisorsTr">
							<td class="ui-state-default jqgrid-rownum" valign="top">监事信息：</td>
							<td><textarea id="supervisors" name="supervisors" cols="25" rows="2">${_DirAndSup.supervisors}</textarea></td>
							<td></td>
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
										<%--<td><input style="cursor: pointer;" type="submit" id="button" value="提交"/></td>--%>
										<td><input style="cursor: pointer;" type="button"  onclick="submitData()" value="提交"/></td>
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
</form>
</body>
</html>
                  
