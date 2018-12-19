<%@ page import="java.util.UUID" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,Firefox=1" />
	<title>合同变更</title>
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
            for(var i=0;i < document.contractInfo.elements.length-1;i++)
            {
                if(document.contractInfo.elements[i].value==""&&document.contractInfo.elements[i].type!="file"&&document.contractInfo.elements[i].type!="hidden")
                {
                    document.contractInfo.elements[i].focus();
                  	alert("表单未填写完整！")
                    return false;
                }
            }
            return true;
        }

        function selectSup(name) {
            $.ajax({
                type : 'POST',
                url : "${pageContext.request.contextPath}/review.do?method=contact",
                //需要页面传入值
                data :{supplierName:name},
                dataType : "JSON",
                success : function(data) {
                    $("#contact").val(data);
                },
                error:function (data) {

                }
            });
        }
        $(function(){
            $("#ca").change(function(){
                alert($(this).val())
               $("#cav").val($(this).val());
            });
            $("#sa").change(function(){
                alert($(this).val())
               $("#sav").val($(this).val())
            });
        });

	</script>
	<base target="_self"/>
</head>

<body style="overflow-y: auto;padding: 0 100px" onload="">
<br/>
<form id="contractInfo"  name="contractInfo" action="${pageContext.request.contextPath}/contract.do?method=save" onsubmit="return myCheck()" method="post" enctype="multipart/form-data" >
	<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 100%">
		<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
			<div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
		    	<span class="ui-jqgrid-title">编辑供方信息 &nbsp;
						【合同变更 主办人:${_GLOBAL_PERSON.personName}】
		    	</span>
			</div>
		<div>
			<input type="hidden" name="purchaseId" value="${purchaseInfor.purchaseId}"/>
			<table cellspacing="0" cellpadding="0" border="0" style="width: 110%">
				<tbody id="tblGrid">
				<tr class="ui-widget-content jqgrow ui-row-ltr">
					<td class="ui-state-default jqgrid-rownum" style="width: 15%">合同标题：</td>
					<td>
						<input name="contractName"/>
					</td>
				</tr>
				<tr class="ui-widget-content jqgrow ui-row-ltr">
					<td class="ui-state-default jqgrid-rownum" style="width: 15%">采购类型：</td>
					<td>
						<input name="purchaseTypeName" value="${purchaseInfor.flowId.flowName}"/>
					</td>
				</tr>
				<tr class="ui-widget-content jqgrow ui-row-ltr">
					<td class="ui-state-default jqgrid-rownum" style="width: 15%">供应商名称：</td>
					<td style="border: none">
						<select id="supplier" name="supplierName" style="background-color: inherit;" onchange="selectSup(this.value);">
							<option value="">--请选择--</option>
							<c:forEach var="item" items="${suppliers }">
								<option value="${item.supplierName }">${item.supplierName }</option>
							</c:forEach>
						</select>
						&nbsp;&nbsp;<span class="ui-state-default jqgrid-rownum">联系方式：</span><input name="supplierContact" id="contact"/>
					</td>
				</tr>
				<tr class="ui-widget-content jqgrow ui-row-ltr" >
					<td class="ui-state-default jqgrid-rownum" style="width: 15%">变更内容：</td>
					<td><textarea cols="100" name="contractDesc"></textarea></td>
				</tr>
				<tr class="ui-widget-content jqgrow ui-row-ltr" >
					<td class="ui-state-default jqgrid-rownum" style="width: 15%">变更附件：</td>
					<td><input id="ca" name="contractAttach" type="file"><input type="hidden" name="cav" id="cav"/></td>
				</tr>
				<tr></tr>

				<tr class="ui-widget-content jqgrow ui-row-ltr">
					<td class="ui-state-default jqgrid-rownum" style="width: 15%">申请部门：</td>
					<td >
						<input value="${purchaseInfor.department.organizeName}" name="applyDept" readonly/>
					</td>
				</tr>
				<tr class="ui-widget-content jqgrow ui-row-ltr">
					<td class="ui-state-default jqgrid-rownum" style="width: 15%">申请人：</td>
					<td >
						<input value="${purchaseInfor.applier.person.personName}" name="applierName" readonly />
					</td>
				</tr>
				<tr class="ui-widget-content jqgrow ui-row-ltr">
					<td class="ui-state-default jqgrid-rownum" style="width: 15%">申请日期：</td>
					<td >
						<fmt:formatDate var="reTime" value='${purchaseInfor.startTime}' pattern='yyyy-MM-dd' />
						<input value="${reTime}" name="applyDate" readonly/>
					</td>
				</tr>
				<tr class="ui-widget-content jqgrow ui-row-ltr" >
					<td class="ui-state-default jqgrid-rownum" style="width: 15%">方案评估：</td>
					<td><textarea  cols="100" name="techSolution"></textarea></td>
				</tr>
				<tr class="ui-widget-content jqgrow ui-row-ltr" >
					<td class="ui-state-default jqgrid-rownum" style="width: 15%">方案附件：</td>
					<td><input id="sa" name="solutionAttach" type="file"><input type="hidden" name="sav" id="sav"/></td>
				</tr>

				<tr class="ui-widget-content jqgrow ui-row-ltr">
					<td class="ui-state-default jqgrid-rownum" style="width: 15%">价格评估：</td>
					<td>
						<table>
							<thead >
							<tr>
								<th class="ui-state-default jqgrid-rownum" >采购项目</th>
								<th class="ui-state-default jqgrid-rownum" >规格型号</th>
								<th class="ui-state-default jqgrid-rownum" >数量</th>
								<th class="ui-state-default jqgrid-rownum" >去年价格</th>
								<th class="ui-state-default jqgrid-rownum" >今年价格</th>
								<th class="ui-state-default jqgrid-rownum" >备注</th>
							</tr>
							</thead>
							<tbody>
								<tr>
									<td style="border: none"><input name="projectName"/></td>
									<td style="border: none"><input name="projectModel"/></td>
									<td style="border: none"><input name="projectCount"/></td>
									<td style="border: none"><input class="price" name="lastYearPrice" placeholder="请输入正确价格" /></td>
									<td style="border: none"><input class="price" name="thisYearPrice" placeholder="请输入正确价格" /></td>
									<td style="border: none"><textarea rows="1" cols="20" name="projectMemo"></textarea></td>
								</tr>
							</tbody>
						</table>
					</td>
				</tr>
				<tr class="ui-widget-content jqgrow ui-row-ltr">
					<td class="ui-state-default jqgrid-rownum" style="width: 15%">结论:</td>
					<td>
						<textarea cols="100" name="conclusion"></textarea>
					</td>
				</tr>
				<tr class="ui-widget-content jqgrow ui-row-ltr">
					<td class="ui-state-default jqgrid-rownum" style="width: 15%">经办人:</td>
					<td >
						<input name="executorName" value="${_GLOBAL_PERSON.personName}"/>
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
						<input  id="cbdate" name="contractDate" value="<%=format%>"/>
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

