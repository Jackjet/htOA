<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<title>编辑APP IMG</title>
<script src="<c:url value="/"/>js/multisearch.js"></script> <!--加载模态多条件查询相关js-->
<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
<script src="<c:url value="/"/>datePicker/WdatePicker.js" type="text/javascript"></script>
<script src="<c:url value="/"/>components/jquery-1.4.2.js" type="text/javascript"></script> <!--jquery包-->
<script src="<c:url value="/"/>components/jqgrid/js/jquery-ui-1.8.2.custom.min.js" type="text/javascript"></script><!--jquery ui-->
<script src="<c:url value="/"/>components/jquery.layout.js" type="text/javascript"></script><!--jquery 布局-->
<script src="<c:url value="/"/>components/jqgrid/js/grid.locale-cn.js" type="text/javascript"></script> <!--jqgrid 中文包-->
<script src="<c:url value="/"/>components/jqgrid/js/jquery.jqGrid.min.js" type="text/javascript"></script> <!--jqgrid 包-->
<script src="<c:url value="/"/>js/changeclass.js"></script>
<link type="text/css" href="<c:url value="/"/>components/jqgrid/css/jquery.ui.all.css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />

<!-- formValidator -->
<link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css"></link>
<script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript" charset="UTF-8"></script>
<script src="<c:url value="/"/>components/formvalidator/js/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
<!-- ------------- -->

<script>
	//验证
	$(document).ready(function(){
		//$.formValidator.initConfig({formid:"appImgForm",onerror:function(msg){alert(msg)},onsuccess:function(){submitData();}});
		//$("#imgTitle").formValidator({onshow:"请输入标题",onfocus:"标题不能为空",oncorrect:"输入正确"}).inputValidator({min:1,onerror: "请输入标题"});
		////$("#attachment").formValidator({onshow:"请上传图片",onfocus:"图片附件不能为空",oncorrect:"选择正确"}).inputValidator({min:1,onerror: "请上传图片附件"});
	});
	
	//验证输入数据
	function validateImgInfor(){
		var imgId;
		var imgTitle;		
		var attachment;
		imgId = appImgForm.imgId.value;
		imgTitle = appImgForm.imgTitle.value;
		attachment = appImgForm.attachment.value;
		if(imgTitle==""||imgTitle==null){
			alert("请输入标题!");
			appImgForm.imgTitle.focus();
			return false;
		}
		if(imgId == "" || imgId == null || imgId == "0"){
			if(attachment==""||attachment==null){
				alert("请上传图片!");
				appImgForm.attachment.focus();
				return false;
			}
		}
		return true;
	}
	
	//提交数据
	function submitData() {
		var form = document.appImgForm;
		form.action = '<c:url value="/core/app"/>.do?method=save';
		form.submit();
	
		alert('信息编辑成功！');
		window.opener.location.reload();
		window.returnValue = "refresh";
		window.close();
	}
	
</script>
<base target="_self"/>
<body>
<form:form commandName="appImgVo" id="appImgForm" name="appImgForm" action="/core/app.do?method=save" method="post" enctype="multipart/form-data">
<form:hidden path="imgId"/>
<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 100%">
<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
  <div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
    <span class="ui-jqgrid-title">编辑APP IMG</span>
  </div>

	<div class="ui-jqgrid-bdiv" style="height: auto; width: 100%">
		<div style="position: relative;">
			<div>
				<table cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable" style="width: 100%">
					<tbody>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum" style="width: 15%">图片标题：</td>
							<td><form:input path="imgTitle" size="50"/></td>
						    <td style="width: 25%"><div id="imgTitleTip" style="width:250px"></div></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">排序号：</td>
							<td><form:input path="imgOrder" size="20"/></td>
							<td><div id="imgOrderTip" style="width:250px"></div></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 20px;">
							<td colspan=3 class="ui-state-default jqgrid-rownum"><font color=red>建议图片容量不要太大，分辨率为16：9</font></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">图片附件：</td>
							<td>
								<table cellpadding="0" cellspacing="0" style="margin-bottom:0;margin-top:0">
									<tr>
										<td><input type="file" name="attachment" id="attachment" size="50" />&nbsp;</td>
									</tr>
								</table>
							</td>
							<td><div id="attachmentTip" style="width:250px"></div></td>
						</tr>
						<c:if test="${!empty _Attachment_Names}">
							<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
								<td class="ui-state-default jqgrid-rownum">原图片信息(<font color=red>如果要删除原图片，请选择该图片前面的选择框</font>)：</td>
								<td><c:forEach var="file" items="${_Attachments}" varStatus="index">
											<input type="checkbox" name="attatchmentArray" value="${index.index}" />
												<a href="<c:url value="${'/common/'}"/>download.jsp?filepath=${file}">${_Attachment_Names[index.index]}</a><br/>
										</c:forEach></td>
							</tr>
						</c:if>
						
						
						<!-- <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">备注：</td>
							<td><form:textarea path="memo" cols="50" rows="5"/></td>
						    <td><div id="memoTip" style="width:250px"></div></td>
						</tr> -->
						
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
										<td><!-- <input style="cursor: pointer;" type="submit" id="button" value="提交"/> -->
											<input type="button" id="button" style="cursor: pointer;" value="提交" onclick="javaScript:if(validateImgInfor()){submitData();}" />
										</td>
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
