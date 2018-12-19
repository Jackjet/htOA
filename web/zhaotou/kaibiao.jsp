<%@ page import="java.util.UUID" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,Firefox=1" />
	<title>开标一览表</title>
	<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
	<script type="text/javascript" src="<c:url value="/"/>components/jqgrid/js/jquery.ui.core.js"></script>
	<script type="text/javascript" src="<c:url value="/"/>components/jqgrid/js/jquery.ui.widget.js"></script>
	<script type="text/javascript" src="<c:url value="/"/>components/jqgrid/js/grid.locale-cn.js"></script>
	<script type="text/javascript" src="<c:url value="/"/>components/jqgrid/js/jquery.ui.datepicker.js"></script>
	
	<link rel="stylesheet" type="text/css" href="<c:url value="/"/>components/jqgrid/css/jquery.ui.all.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />

	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="/css/noTdBottomBorder.css" />
	<script src="/js/inc_javascript.js"></script>
	<script src="/js/addattachment.js"></script>
	<script src="/js/commonFunction.js"></script>
	<script src="../js/jquery1.9.1.min.js"></script>
	<%--表单提交提示框--%>
	<script src="../components/sweetalert/dist/sweetalert2.min.js"></script>
	<link rel="stylesheet" href="../components/sweetalert/dist/sweetalert2.min.css">
	<style type="text/css">
			.buttonclass {
				font-weight:bold;
			}
	</style>
	<!-- formValidator -->
	<link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css"></link>
	<script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript" charset="UTF-8"></script>
	<script src="<c:url value="/"/>components/formvalidator/js/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
	<!-- ------------- -->
	<script type="text/javascript">
		function myCheck() {
            for(var i=1;i < document.bidInfo.elements.length;i++)
            {
                if(document.bidInfo.elements[i].value==""&&document.bidInfo.elements[i].type!="file"&&(i<8||i>46)document.bidInfo.elements[i].type!="hidden")
                {
                    document.bidInfo.elements[i].focus();
                   alert("表单未填写完整")
        }
        $(function () {
            $("#ba").change(function(){
                $("#bav").val($(this).val());
            });
            $("#sa1").change(function(){
                $("#sav1").val($(this).val())
            });
            $("#sa2").change(function(){
                $("#sav2").val($(this).val())
            });
            $("#sa3").change(function(){
                $("#sav3").val($(this).val())
            });
            $("#sa4").change(function(){
                $("#sav4").val($(this).val())
            });
            $("#sa5").change(function(){
                $("#sav5").val($(this).val())
            });
        });
	</script>

	<base target="_self"/>
</head>

<body style="overflow-y: auto;padding: 0 100px" onload="">
<br/>
<form name="bidInfo" id="bidInfo"  action="${pageContext.request.contextPath}/bid.do?method=save" method="post" onsubmit="return myCheck()" enctype="multipart/form-data" >
	<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 100%">
		<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
			<div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
		    	<span class="ui-jqgrid-title">编辑供方信息 &nbsp;
						【招投标 主办人:${_GLOBAL_PERSON.personName}】
		    	</span>
			</div>
		<div>
			<input type="hidden" name="purchaseId" value="${purchase.purchaseId}"/>
			<table cellspacing="0" cellpadding="0" border="0" style="width: 110%">
				<tbody id="tblGrid">
				<tr class="ui-widget-content jqgrow ui-row-ltr" >
					<td class="ui-state-default jqgrid-rownum" style="width: 15%">采购类型：</td>
					<td>
						<input name="purchaseTypeMsg" value="${purchase.flowId.flowName}"/>
					</td>
				</tr>
				<tr class="ui-widget-content jqgrow ui-row-ltr" >
					<td class="ui-state-default jqgrid-rownum" style="width: 15%">开标时间：</td>
					<td>
						<%
							Date date=new Date();
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							String format = sdf.format(date);
						%>
						<input name="startDate" value="<%=format%>"/>
					</td>
				</tr>
				<tr class="ui-widget-content jqgrow ui-row-ltr">
					<td class="ui-state-default jqgrid-rownum" style="width: 15%">项目名称：</td>
					<td>
						<input name="projectName"/>
					</td>
				</tr>
				<tr class="ui-widget-content jqgrow ui-row-ltr">
					<td class="ui-state-default jqgrid-rownum" style="width: 15%">唱标人：</td>
					<td >
						<input  name="readerName" value="${_GLOBAL_PERSON.personName}"/>
					</td>
				</tr>
				<tr class="ui-widget-content jqgrow ui-row-ltr">
					<td class="ui-state-default jqgrid-rownum" style="width: 15%">开标监督人：</td>
					<td >
						<input  name="supervisorName" value="${_GLOBAL_PERSON.personName}"/>
					</td>
				</tr>
				<tr class="ui-widget-content jqgrow ui-row-ltr">
					<td class="ui-state-default jqgrid-rownum" style="width: 15%">招标编号：</td>
					<td >
						<input name="zbCode" />
					</td>
				</tr>
				<tr class="ui-widget-content jqgrow ui-row-ltr">
					<td class="ui-state-default jqgrid-rownum" style="width: 15%">供方信息：</td>
					<td>
						<table>
							<thead >
							<tr>
								<th class="ui-state-default jqgrid-rownum" >投标单位</th>
								<th class="ui-state-default jqgrid-rownum" >管理费率</th>
								<th class="ui-state-default jqgrid-rownum" >施工费率</th>
								<th class="ui-state-default jqgrid-rownum" >投标单位资质</th>
								<th class="ui-state-default jqgrid-rownum" >维修响应时间</th>
								<th class="ui-state-default jqgrid-rownum" >质保期</th>
								<th class="ui-state-default jqgrid-rownum" >附件</th>
							</tr>
							</thead>
							<tbody>
								<tr>
									<td style="border: none">
										<select  name="supplierName1" style="background-color: inherit;">
											<option value="">--请选择--</option>
											<c:forEach var="item" items="${suppliers }">
												<option value="${item.supplierName}">${item.supplierName}</option>
											</c:forEach>
										</select>
									</td>
									<td style="border: none"><input name="managerRate1"/></td>
									<td style="border: none"><input name="constructRate1" /></td>
									<td style="border: none"><input name="qualification1" /></td>
									<td style="border: none"><input name="responseTime1"/></td>
									<td style="border: none"><input name="shelflife1" /></td>
									<td style="border: none"><input type="file" id="sa1" name="supplierAttach1" /><input type="hidden" id="sav1" name="sav1" /></td>
								</tr>
								<tr>
									<td style="border: none">
										<select  name="supplierName2" style="background-color: inherit;">
											<option value="">--请选择--</option>
											<c:forEach var="item" items="${suppliers }">
												<option value="${item.supplierName}">${item.supplierName}</option>
											</c:forEach>
										</select>
									</td>
									<td style="border: none"><input name="managerRate2"/></td>
									<td style="border: none"><input name="constructRate2" /></td>
									<td style="border: none"><input name="qualification2" /></td>
									<td style="border: none"><input name="responseTime2"/></td>
									<td style="border: none"><input name="shelflife2" /></td>
									<td style="border: none"><input type="file" id="sa2" name="supplierAttach2" /><input type="hidden" id="sav2" name="sav2" /></td>
								</tr>
								<tr>
									<td style="border: none">
										<select  name="supplierName3" style="background-color: inherit;">
											<option value="">--请选择--</option>
											<c:forEach var="item" items="${suppliers }">
												<option value="${item.supplierName}">${item.supplierName}</option>
											</c:forEach>
										</select>
									</td>
									<td style="border: none"><input name="managerRate3"/></td>
									<td style="border: none"><input name="constructRate3" /></td>
									<td style="border: none"><input name="qualification3" /></td>
									<td style="border: none"><input name="responseTime3"/></td>
									<td style="border: none"><input name="shelflife3" /></td>
									<td style="border: none"><input type="file" id="sa3" name="supplierAttach3" /><input type="hidden" id="sav3" name="sav3" /></td>
								</tr>
								<tr>
									<td style="border: none">
										<select  name="supplierName4" style="background-color: inherit;">
											<option value="">--请选择--</option>
											<c:forEach var="item" items="${suppliers }">
												<option value="${item.supplierName}">${item.supplierName}</option>
											</c:forEach>
										</select>
									</td>
									<td style="border: none"><input name="managerRate4"/></td>
									<td style="border: none"><input name="constructRate4" /></td>
									<td style="border: none"><input name="qualification4" /></td>
									<td style="border: none"><input name="responseTime4"/></td>
									<td style="border: none"><input name="shelflife4" /></td>
									<td style="border: none"><input type="file" id="sa4" name="supplierAttach4" /><input type="hidden" id="sav4" name="sav4" /></td>
								</tr>
								<tr>
									<td style="border: none">
										<select  name="supplierName5" style="background-color: inherit;">
											<option value="">--请选择--</option>
											<c:forEach var="item" items="${suppliers }">
												<option value="${item.supplierName}">${item.supplierName}</option>
											</c:forEach>
										</select>
									</td>
									<td style="border: none"><input name="managerRate5"/></td>
									<td style="border: none"><input name="constructRate5" /></td>
									<td style="border: none"><input name="qualification5" /></td>
									<td style="border: none"><input name="responseTime5"/></td>
									<td style="border: none"><input name="shelflife5" /></td>
									<td style="border: none"><input type="file" id="sa5" name="supplierAttach5" /><input type="hidden" id="sav5" name="sav5" /></td>
								</tr>
							</tbody>
						</table>
					</td>
				</tr>
				<tr class="ui-widget-content jqgrow ui-row-ltr">
					<td class="ui-state-default jqgrid-rownum" style="width: 15%">选择模板:</td>
					<td>
						<select name="templateName">
							<option value="">---请选择---</option>
							<c:forEach var="item" items="${templates }">
								<option value="${item.templateName }">${item.templateName }</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr class="ui-widget-content jqgrow ui-row-ltr">
					<td class="ui-state-default jqgrid-rownum" style="width: 15%">附件:</td>
					<td >
						<input type="file" id="ba" name="bidAttach" /><input type="hidden" id="bav" name="bav" />
					</td>
				</tr>
				</tbody>
			</table>
		</div>
		<div class="ui-state-default ui-jqgrid-pager ui-corner-bottom" dir="ltr" style="width: 100%;margin-top:20px;overflow:visible" >
			<input class="buttonclass" style="cursor: pointer;" type="submit" value="提交"/>
			<input class="buttonclass" style="cursor: pointer;" type="button" value="关闭" onclick="window.close();"/>
		</div>
		</div>
		</div>
	</form>
</body>
</html>

