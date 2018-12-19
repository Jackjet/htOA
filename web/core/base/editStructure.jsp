<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
<title>编辑岗位信息</title>
<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
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
		$.formValidator.initConfig({formid:"structureForm",onerror:function(msg){alert(msg)},onsuccess:function(){submitData();}});
		$("#structureName").formValidator({onshow:"请输入岗位名称",onfocus:"岗位名称不能为空",oncorrect:"输入正确"}).inputValidator({min:1,onerror:"岗位名称不能为空,请确认"});
		$("#orderNo").formValidator({onshow:"请输入排序编号",onfocus:"只能输入阿拉伯数字",oncorrect:"输入正确"}).inputValidator({min:1,max:99,type:"value",onerrormin:"输入的值必须大于等于1",onerror:"只能输入阿拉伯数字,请确认"});
	});
	
	//提交数据
	function submitData() {
		alert('信息编辑成功！');
		window.returnValue = "refresh";
		window.close();
	}
</script>
</head>
<base target="_self"/>
<body style="padding: 0 100px">
<br/>
<form id="structureForm" action="/core/structureInfor.do?method=save" method="post">
<input type="hidden" name="structureId" value="${_Structure.structureId}"/>
<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 100%">
<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
  <div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
    <span class="ui-jqgrid-title">编辑岗位信息</span>
  </div>

	<div class="ui-jqgrid-bdiv" style="height: auto; width: 100%">
		<div style="position: relative;">
			<div>
				<table cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable" style="width: 100%">
					<tbody>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td style="width: 20%;" class="ui-state-default jqgrid-rownum">岗位名称：</td>
							<td style="width: 45%;"><input type="text" id="structureName" name="structureName" size="20" value="${_Structure.structureName}"/></td>
							<td style="width: 35%;"><div id="structureNameTip" style="width:250px"></div></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">排序编号：</td>
							<td><input type="text" id="orderNo" name="orderNo" size="20" value="${_Structure.orderNo}"/></td>
							<td><div id="orderNoTip" style="width:250px"></div></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">所属岗位：</td>
							<td><select name="parentId">
									<c:forEach items="${requestScope._TreeStructures}" var="structure" varStatus="status"> 
										<c:choose>
											<c:when test="${_Structure.parent.structureId == structure.structureId}">
												<option value="${structure.structureId}" selected="selected">
											</c:when>
											<c:otherwise>
												<option value="${structure.structureId}">
											</c:otherwise>
										</c:choose>
											<c:forEach begin="0" end="${structure.layer}">&nbsp;</c:forEach>
											<c:if test="${structure.layer==1}"><b>+</b></c:if><c:if test="${structure.layer==2}"><b>-</b></c:if>${structure.structureName}				
										</option>
									</c:forEach>
						        </select></td>
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
</form>
</body>
</html>
                  
