<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<title>编辑文档信息</title>
<script src="<c:url value="/"/>js/changeclass.js"></script> <!--用于改变页面样式-->
<script src="<c:url value="/"/>js/addattachment.js"></script>
<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
<script src="<c:url value="/"/>datePicker/WdatePicker.js" type="text/javascript"></script>
<script src="<c:url value="/"/>js/changeclass.js"></script>
<link type="text/css" href="<c:url value="/"/>components/jqgrid/css/jquery.ui.all.css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />

<!-- formValidator -->
<link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css"></link>
<script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript" charset="UTF-8"></script>
<script src="<c:url value="/"/>components/formvalidator/js/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>

<script>
	$(document).ready(function(){
		//格式化tr,td
		//$("#addTable tr").addClass("ui-widget-content jqgrow ui-row-ltr").height(30).find("td:nth-child(1)").addClass("ui-state-default jqgrid-rownum");
		
		//验证
		$.formValidator.initConfig({formid:"documentInforForm",onerror:function(msg){alert(msg)},onsuccess:function(){}});
		$("#documentTitle").formValidator({onshow:"请输入标题",onfocus:"标题不能为空",oncorrect:"输入正确"}).inputValidator({min:1,onerror: "请输入标题"});
		$("#categoryId").formValidator({onshow:"请选择文档分类",onfocus:"分类为必选项",oncorrect:"选择正确"}).inputValidator({min:1,onerror: "请选择文档分类"});
		$("#authorId").formValidator({onshow:"请选择作者",onfocus:"作者为必选项",oncorrect:"选择正确"}).inputValidator({min:1,onerror:"请选择作者"});
		$("#departmentId").formValidator({onshow:"请选择部门",onfocus:"部门为必选项",oncorrect:"选择正确"}).inputValidator({min:1,onerror:"请选择部门"});
		
	});

</script>
<base target="_self"/>


<body style="overflow-y: auto;padding: 0 100px" >
<br/>
<form:form commandName="documentInforVo" id="documentInforForm" name="documentInforForm" action="/document/document.do?method=save" method="post" enctype="multipart/form-data">
<form:hidden path="documentId"/>

<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 100%">
<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
    <div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
    	<span class="ui-jqgrid-title">编辑文档信息</span>
    </div>

	<div class="ui-jqgrid-bdiv" style="height: auto; width: 100%">
		<div style="position: relative;">
			<div>
				<table cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable" style="width: 100%" id="addTable">
					<tbody>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum" style="width: 20%">标题：</td>
							<td style="width: 60%"><form:input path="documentTitle" size="70" /></td>
						    <td><div id="documentTitleTip" style="width:200px"></div></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">编号：</td>
							<td><form:input path="documentCode" size="20" /></td>
						    <td><div id="documentCodeTip" style="width:200px"></div></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">关键字：</td>
							<td><form:input path="keyword" size="20" /></td>
						    <td><div id="keywordTip" style="width:200px"></div></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum" valign="top">说明(摘要)：</td>
							<td><form:textarea rows="5" cols="50" path="description"></form:textarea></td>
						    <td><div id="descriptionTip" style="width:200px"></div></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">分类：</td>
							<td><form:select path="categoryId">
								       <form:option value="">--选择分类--</form:option>
								       <c:forEach items="${_TREE}" var="category">
								           <form:option value="${category.categoryId}"><c:forEach begin="0" end="${category.layer}">&nbsp;</c:forEach><c:if test="${category.layer==1}"><b>+</b></c:if><c:if test="${category.layer==2}"><b>-</b></c:if>${category.categoryName}</form:option>
								       </c:forEach>
								    </form:select></td>
						    <td><div id="categoryIdTip" style="width:200px"></div></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">所属部门：</td>
							<td><form:select path="departmentId">
								       <form:option value="0">--选择部门--</form:option>
								       <c:forEach items="${_Departments}" var="department">
								           <form:option value="${department.organizeId}">${department.organizeName}</form:option>
								       </c:forEach>									
								    </form:select></td>
						    <td><div id="departmentIdTip" style="width:200px"></div></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">作者：</td>
							<td><form:select path="authorId">
								       <form:option value="0">--选择作者--</form:option>
								       <c:forEach items="${_AllSystemUsers}" var="user">
								           <form:option value="${user.person.personId}">${user.person.personName}</form:option>
								       </c:forEach>									
								    </form:select></td>
						    <td><div id="authorIdTip" style="width:200px"></div></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">创建时间：</td>
							<td><input name="createTime" id="createTime" value="${_CreateTime}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly="readonly" size="20" /></td>
						    <td><div id="createTimeTip" style="width:200px"></div></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">是否推荐：</td>
							<td><form:select path="commended">
								              <form:option value="0">不推荐</form:option>
								              <form:option value="1">推荐</form:option>
								          </form:select></td>
						    <td><div id="commendedTip" style="width:200px"></div></td>
						</tr>
						
						
						<%--<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">对应公文ID：</td>
							<td><input name="reportId" id="reportId" size="20" value="${_DocumentInfor.reportId}" /></td>
						    <td><div id="reportIdTip" style="width:200px"></div></td>
						</tr>--%>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">附件：</td>
							<td colspan="2"><table cellpadding="0" cellspacing="0" style="margin-bottom:0;margin-top:0">
									<tr>
										<td><input type="file" name="attachment" size="50" />&nbsp;<input type="button" value="更多附件.." onclick="addtable('newstyle')" /></td>
									</tr>
								</table><span id="newstyle"></span></td>
						</tr>
						
						<c:if test="${!empty _Attachment_Names}">
							<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
								<td colspan="3" class="ui-state-default jqgrid-rownum">原附件信息(<font color=red>如果要删除某个附件，请选择该附件前面的选择框</font>)：</td>
							</tr>
							<tr>
								<td colspan="3"><c:forEach var="file" items="${_Attachment_Names}" varStatus="index">
											<input type="checkbox" name="attatchmentArray" value="${index.index}" />
												<a href="<%=request.getRealPath("/")%>${file}">${_Attachment_Names[index.index]}</a><br/>
										</c:forEach></td>
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
										<td><input style="cursor: pointer;" type="submit" value="提交"/></td>
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

                  
