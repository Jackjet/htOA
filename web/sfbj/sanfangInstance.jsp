<%@ page import="java.util.UUID" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<title>审批流转</title>
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
	<script>
        $(document).ready(function(){
            $(".price").val(0.0)
            $(".price").focus(function(){
                this.value='';
            });
            $(".price").blur(function(){
                if (this.value==''){
                    this.value=0.0;
                }
                this.val(this.value);
            });

        });
        $(function () {
            $(".price").keyup(function(){
                $(this).val($(this).val().replace(/[^0-9.]/g,''));
            }).bind("paste",function(){  //CTR+V事件处理
                $(this).val($(this).val().replace(/[^0-9.]/g,''));
            }).css("ime-mode", "disabled"); //CSS设置输入法不可用
        });

        function myCheck()
        {
            for(var i=0;i < document.supplierInfo.elements.length-1;i++)
            {
                if(document.supplierInfo.elements[i].value==""&&document.supplierInfo.elements[i].type!="file"&&document.supplierInfo.elements[i].type!="hidden")
                {
                    document.supplierInfo.elements[i].focus();
                   	alert("表单未填写完整!")
                    return false;
                }
            }
            return true;
        }
        function first(name){
            $("#sup1").val(name);
            $("#supl1").text(name);
            var tel="";
            $.ajax({
                type : 'POST',
                url : "${pageContext.request.contextPath}/review.do?method=contact",
                //需要页面传入值
                data :{supplierName:name},
                dataType : "JSON",
                success : function(data) {
                    $("#cont1").val(data);
                },
                error:function (data) {

                }
            });
        }
        function sec(name){
            $("#sup2").val(name);
            $("#supl2").text(name);
            var tel="";
            $.ajax({
                type : 'POST',
                url : "${pageContext.request.contextPath}/review.do?method=contact",
                //需要页面传入值
                data :{supplierName:name},
                dataType : "JSON",
                success : function(data) {
                    $("#cont2").val(data);
                },
                error:function (data) {

                }
            });
        }
        function third(name){
            $("#sup3").val(name);
            $("#supl3").text(name);
            var tel="";
            $.ajax({
                type : 'POST',
                url : "${pageContext.request.contextPath}/review.do?method=contact",
                //需要页面传入值
                data :{supplierName:name},
                dataType : "JSON",
                success : function(data) {
                    $("#cont3").val(data);
                },
                error:function (data) {

                }
            });
        }

        $(function(){
            $("#finalSupplier option").click(function(){
                for (var i = 1; i < 4; i++) {
                    if ($(this).text()==$("#sup"+i).val()) {
                        $("#finaMoney").val(parseInt($("#mtprice"+i).val())+parseInt($("#wlprice"+i).val()));
                    }
                }
            });
            $("#sa").change(function(){
                $("#sav").val($(this).val());
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
        });

	</script>
	<base target="_self"/>
</head>

<body style="overflow-y: auto;padding: 0 100px" onload="">
<br/>
<form id="supplierInfo"  name="supplierInfo" action="${pageContext.request.contextPath}/review.do?method=save" onsubmit="return myCheck()" method="post" enctype="multipart/form-data" >
	<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 100%">
		<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
			<div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
		    	<span class="ui-jqgrid-title">编辑供方信息 &nbsp;
						【三方比价 主办人:${_GLOBAL_PERSON.personName}】
		    	</span>
			</div>
			<div>
				<input type="hidden" name="purchaseId" value="${purchaseId}"/>

				<table cellspacing="0" cellpadding="0" border="0" style="width: 110%">
					<tbody id="tblGrid">
					<tr class="ui-widget-content jqgrow ui-row-ltr">
						<td class="ui-state-default jqgrid-rownum" style="width: 15%">三方标题：</td>
						<td>
							<input name="sanfangTitle"/>
						</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" >
						<td class="ui-state-default jqgrid-rownum" style="width: 15%">需求说明：</td>
						<td><textarea rows="2" cols="100" name="sanfangDesc"></textarea></td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" >
						<td class="ui-state-default jqgrid-rownum" style="width: 15%">三方附件：</td>
						<td><input id="sa" name="sanfangAttach" type="file"><input type="hidden" name="sav" id="sav"/></td>
					</tr>
					<tr></tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr">
						<td class="ui-state-default jqgrid-rownum" style="width: 15%">采购类型：</td>
						<td>
							<input name="purchaseType" value="${purchaseType}" readonly/>
						</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr">
						<td class="ui-state-default jqgrid-rownum" style="width: 15%">申请部门：</td>
						<td >
							<input value="${applyDept.organizeName}" name="sanfangDepartment" readonly/>
						</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr">
						<td class="ui-state-default jqgrid-rownum" style="width: 15%">申请人：</td>
						<td >
							<input value="${applier.person.personName}" name="sanfangApplier" readonly />
						</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr">
						<td class="ui-state-default jqgrid-rownum" style="width: 15%">申请日期：</td>
						<td >
							<input value="<fmt:formatDate value='${applyDate}' pattern='yyyy-MM-dd' />" name="applyDate" readonly/>
						</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr">
						<td class="ui-state-default jqgrid-rownum" style="width: 15%">供方信息：</td>
						<td>
							<table>
								<thead >
								<tr>
									<th class="ui-state-default jqgrid-rownum" >供应商名称</th>
									<th class="ui-state-default jqgrid-rownum" >联系电话</th>
									<th class="ui-state-default jqgrid-rownum" >方案描述</th>
									<th class="ui-state-default jqgrid-rownum" >方案附件</th>
									<th class="ui-state-default jqgrid-rownum" >总报价</th>
								</tr>
								</thead>
								<tbody>
								<tr>
									<td style="border: none">
										<select id="supplier1" name="supplier1" style="background-color: inherit;" onchange="first(this.value);">
											<option value="">--请选择--</option>
											<c:forEach var="item" items="${suppliers }">
												<option value="${item.supplierName }">${item.supplierName }</option>
											</c:forEach>p
										</select>
									</td>
									<td style="border: none"><input name="contact1" id="cont1"/></td>
									<td style="border: none"><textarea rows="1" cols="40" name="supplierMemo1"></textarea></td>
									<td style="border: none"><input id="sa1" name="supplierAttach1" type="file"><input type="hidden" id="sav1" name="sav1"/></td>
									<td style="border: none"><input id="price1" type="text" class="price" name="price1" placeholder="请输入正确价格" /></td>
								</tr>
								<tr>
									<td style="border: none">
										<select id="supplier2" name="supplier2" style="background-color: inherit;" onchange="sec(this.value);">
											<option value="">--请选择--</option>
											<c:forEach var="item" items="${suppliers }">
												<option value="${item.supplierName }">${item.supplierName }</option>
											</c:forEach>
										</select>
									</td>
									<td style="border: none"><input name="contact2" id="cont2"/></td>
									<td style="border: none"><textarea rows="1" cols="40" name="supplierMemo2"></textarea></td>
									<td style="border: none"><input id="sa2" name="supplierAttach2" type="file"><input type="hidden" id="sav2" name="sav2"/></td>
									<td style="border: none"><input id="price2" type="text" name="price2" class="price"  placeholder="请输入正确价格" /></td>
								</tr>
								<tr>
									<td style="border: none">
										<select id="supplier3" name="supplier3" style="background-color: inherit;" onchange="third(this.value);">
											<option style="display: none" value="">--请选择--</option>
											<c:forEach var="item" items="${suppliers }">
												<option value="${item.supplierName }">${item.supplierName }</option>
											</c:forEach>
										</select>
									</td>
									<td style="border: none"><input name="contact3" type="text" id="cont3"/></td>
									<td style="border: none"><textarea rows="1" cols="40" name="supplierMemo3"></textarea></td>
									<td style="border: none"><input id="sa3" name="supplierAttach3" type="file"><input type="hidden" id="sav3" name="sav3"/></td>
									<td style="border: none"><input id="price3" type="text" name="price3" class="price"  placeholder="请输入正确价格" /></td>
								</tr>
								</tbody>
							</table>
						</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr">
						<td class="ui-state-default jqgrid-rownum" style="width: 15%">结论:</td>
						<td>
							<font color="red">所选供应商：</font>
							<select id="finalSupplier" name="sanfangFinalSupplier">
								<option value="">---请选择---</option>
								<option id="supl1"></option>
								<option id="supl2"></option>
								<option id="supl3"></option>
							</select><br/>
							<textarea rows="1" cols="80" name="sanfangConclusion"></textarea>
						</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr">
						<td class="ui-state-default jqgrid-rownum" style="width: 15%">采购经办人:</td>
						<td >
							<input name="purchaseCharge" value="${_GLOBAL_PERSON.personName}"/>
						</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr">
						<td class="ui-state-default jqgrid-rownum" style="width: 15%">日期:</td>
						<td>
							<%
								Date date = new Date();
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								String format = sdf.format(date);
							%>
							<input  id="cbdate" name="startDate" value="<%=format%>"/>
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

