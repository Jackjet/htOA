	<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<title>编辑个人通讯录分类</title>
<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
<link type="text/css" href="<c:url value="/"/>components/jqgrid/css/jquery.ui.all.css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />

<!-- formValidator -->
<link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css"></link>
<script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript" charset="UTF-8"></script>
<script src="<c:url value="/"/>components/formvalidator/js/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>

<script>
	//验证
	$(document).ready(function(){
		//alert(${param.rowId});
		$.formValidator.initConfig({formid:"addressCategoryForm",onerror:function(msg){alert(msg)},onsuccess:function(){submitData();}});
		$("#categoryName").formValidator({onshow:"请输入分类名称",onfocus:"分类名称不能为空",oncorrect:"输入正确"}).inputValidator({min:1,onerror: "请输入分类名称"});
		$("#orderNo").formValidator({onshow:"请输入排序序号",onfocus:"排序序号不能为空",oncorrect:"输入正确"}).inputValidator({min:1,onerror:"请输入排序序号"});
	});
	
	//提交数据
	function submitData() {
		/** var form = document.addressCategoryForm;
		form.action = '<c:url value="/personal/address/AddressCategory.do"/>?method=save';
		form.submit(); */
		alert('信息编辑成功！');
		window.returnValue = "Y";
		window.close();
	}

</script>
<base target="_self"/>
<body>
	<form:form commandName="addressCategoryVo" id="addressCategoryForm" name="addressCategoryForm" action="/personal/address/AddressCategory.do?method=save" method="post">
		<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 100%">
			<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
			  <div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
			    <span class="ui-jqgrid-title">编辑个人通讯录分类</span>
			  </div>
			
				<div class="ui-jqgrid-bdiv" style="height: auto; width: 100%">
					<div style="position: relative;">
						<div>
							<table cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable" style="width: 100%">
								<tbody>
									<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
										
										<c:choose>
											<c:when test="${empty _AddressCategory}"></c:when>
											<c:otherwise>
												<input type="hidden" id="categoryId" name="categoryId" size="20" value="${_AddressCategory.categoryId}"/>
											</c:otherwise>
										</c:choose>
										
										<td style="width: 20%;" class="ui-state-default jqgrid-rownum">分类名称：</td>
										<td style="width: 45%;"><form:input path="categoryName" size="20"/></td>
										<td><div id="categoryNameTip"></div></td>
									</tr>
									
									<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
										<td class="ui-state-default jqgrid-rownum">排序序号：</td>
										<td><form:input path="orderNo" size="20"/></td>
										<td><div id="orderNoTip"></div></td>
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

