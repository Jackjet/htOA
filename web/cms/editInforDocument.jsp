<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>编辑资讯信息</title>
<script src="<c:url value="/"/>datePicker/WdatePicker.js" type="text/javascript"></script>
<!-- <script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script> -->
<SCRIPT src="<c:url value='/js/xheditor-1.2.1/jquery/jquery-1.4.4.min.js'/>" type=text/javascript></SCRIPT> <!--jquery包-->
<script src="<c:url value="/"/>js/inc_javascript.js"></script>
<script src="<c:url value="/"/>js/addattachment.js"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/"/>components/jqgrid/css/jquery.ui.all.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />

<!-- formValidator -->
<link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css"></link>
<link type="text/css" rel="stylesheet" href="/css/noTdBottomBorder.css"></link>
<script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript" charset="UTF-8"></script>
<script src="<c:url value="/"/>components/formvalidator/js/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
<!-- ------------- -->
<SCRIPT src="<c:url value='/js/xheditor-1.2.1/xheditor-1.2.1.min.js'/>" type=text/javascript></SCRIPT>
<SCRIPT src="<c:url value='/js/xheditor-1.2.1/xheditor_lang/zh-cn.js'/>" type=text/javascript></SCRIPT>
<SCRIPT src="<c:url value='/js/xheditor-1.2.1/initXheditor.js'/>" type=text/javascript></SCRIPT>

<script>
	$(document).ready(function(){
		//格式化tr,td
		$("#addTable tr").addClass("ui-widget-content jqgrow ui-row-ltr").height(30).find("td:nth-child(1)").addClass("ui-state-default jqgrid-rownum");
		
		initEditor("inforContent");
	});
	
	//提交数据
	function submitData() {
		var form = document.getElementById('inforDocumentForm');
		form.action = '<c:url value="/cms/inforDocument.do"/>?method=save';
		form.submit();
		//var categoryId = document.getElementById("categoryId");
		//var returnArray = ["refresh",categoryId.value];
		//window.returnValue = returnArray;
		//window.close();
	}
</script>
<base target="_self"/>
</head>

<body style="overflow-y: auto;padding: 0 100px">
<br/>
<form id="inforDocumentForm" action="/cms/inforDocument.do?method=save" method="post" enctype="multipart/form-data">
<input type="hidden" name="inforId" value="${_InforDocument.inforId}"/>
<c:choose>
	<c:when test="${!empty _InforDocument}"><input type="hidden" id="categoryId" name="categoryId" value="${_InforDocument.category.categoryId}"/></c:when>
	<c:otherwise><input type="hidden" id="categoryId" name="categoryId" value="${param.categoryId}"/></c:otherwise>
</c:choose>

<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 100%">
<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
  <div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
    <span class="ui-jqgrid-title">编辑资讯信息</span>
  </div>

	<div class="ui-jqgrid-bdiv" style="height: auto; width: 100%">
		<div style="position: relative;">
			<div>
				<table cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable" style="width: 100%" id="addTable">
					<tbody>
					
						<tr>
							<td style="width: 18%">是否首页显示：</td>
							<td><select name="homepage"><option value="false">否</option><option value="true">是</option></select><script language="javaScript">
									var form = document.getElementById('inforDocumentForm');
									getOptsValue(form.homepage,"${_InforDocument.homepage}");
								</script></td>
						</tr>
						<tr>
							<td style="width: 18%">是否置顶：</td>
							<td><select name="topp"><option value="0">否</option><option value="1">是</option></select><script language="javaScript">
                                var form = document.getElementById('inforDocumentForm');
                                getOptsValue(form.homepage,"${_InforDocument.homepage}");
							</script></td>
						</tr>
						<tr>
							<td style="width: 18%">作者：</td>
							<td><input name="zuozhe" size="30"/>
							</td>
						</tr>
						
						<tr>
							<td>首页起止时间：</td>
							<td><input type="text" name="startDate" size="12" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly="true" value="${_InforDocument.startDate}"/> 至 <input type="text" name="endDate" size="12" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly="true" value="${_InforDocument.endDate}"/></td>
						</tr>
						
						<tr>
							<td>是否与总/分公司互通：</td>
							<td><select name="handOut"><option value="false">否</option><option value="true">是</option></select><script language="javaScript">
									var form = document.getElementById('inforDocumentForm');
									getOptsValue(form.handOut,"${_InforDocument.handOut}");
								</script></td>
						</tr>
						
						<!--<tr>
							<td>是否为接收到的：</td>
							<td><select name="received"><option value="false">否</option><option value="true">是</option></select><script language="javaScript">
									var form = document.getElementById('inforDocumentForm');
									getOptsValue(form.received,"${_InforDocument.received}");
								</script></td>
						</tr>
						
						--><!-- 自定义字段信息 -->
						${_AddFields}
						<c:if test="${!empty _CategoryAlias && _CategoryAlias == 'honorroom' }">
							<tr>
								<td>荣誉类别：</td>
								<td>
									<select name="kind">
										<option value="">--选择类别--</option>
										<option value="上海市">上海市</option>
										<option value="集团">集团</option>
										<option value="安吉物流">安吉物流</option>
										<option value="客户">客户</option>
										<option value="行业协会">行业协会</option>
										<option value="其他">其他</option>
									</select>
										<script language="javaScript">
											var form = document.getElementById('inforDocumentForm');
											getOptsValue(form.kind,"${_InforDocument.kind}");
										</script>
								</td>
							</tr>
							<tr>
								<td>荣誉类型：</td>
								<td>
									<select name="honorKind">
										<option value="">--选择荣誉类别--</option>
										<option value="集团荣誉">集团荣誉</option>
										<option value="个人荣誉（合同工）">个人荣誉（合同工）</option>
										<option value="个人荣誉（中介工）">个人荣誉（中介工）</option>
										<option value="其他">其他</option>
									</select>
										<script language="javaScript">
											var form = document.getElementById('inforDocumentForm');
											getOptsValue(form.honorKind,"${_InforDocument.honorKind}");
										</script>
								</td>
							</tr>
						</c:if>
						
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
										<td><input style="cursor: pointer;" type="submit" value="提交"/></td><!--  onclick="submitData();" -->
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
                  
