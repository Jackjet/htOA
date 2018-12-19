<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
<title>编辑任务报告</title>
<script src="<c:url value='/js'/>/addattachment.js"></script>
<script src="<c:url value="/"/>js/inc_javascript.js" type="text/javascript"></script>
<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/css/form.css"/>"/>

<!-- formValidator -->
<link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css"></link>
<script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript" charset="UTF-8"></script>
<script src="<c:url value="/"/>components/formvalidator/js/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
<!-- ------------- -->

<script language="javaScript">
	var i=0,number;
	$(document).ready(function(){
		
	});
	
	//提交数据
	function submitData() {
		alert('信息编辑成功');
		window.returnValue = "Y";
		window.close();
	}
	
		
	//进入添加页面时自动选上"添加"和"浏览"权限
	function init(){
		<%--
		if (${empty param.categoryId}) {
			var params = new Array();
			params[0] = "viewIds";
			params[1] = "createIds";
			params[2] = "editCheckBox";
			params[3] = "addCheckBox";
			initRight(params);
		}
		--%>
	}
	
</script>
<base target="_self"/>
</head>
	 
<body onload="init();">
<br/>
<form:form commandName="taskReportVo" id="taskReportForm" name="taskReportForm" action="/customer/taskReport.do?method=saveReport" method="post" enctype="multipart/form-data">
<form:hidden path="taskId"/>  
<table width="98%" cellpadding="6" cellspacing="1" align="center" border="1" bordercolor="#c5dbec">
   <tr>
	 <td colspan="3" bgcolor="#dfeffc">${_TaskName}</td>
   </tr>

   <tr> 
     <td width="20%">报告内容：</td>
     <td width="60%"><form:textarea rows="5" cols="50" path="content"></form:textarea></td>
     <td><div id="contentTip" style="width:250px"></div></td>
   </tr>
   
      <tr>
		<td>
			附件：
		</td>
		<td width="60%">
			<table cellpadding="0" cellspacing="0"
				style="margin-bottom:0;margin-top:0">
				<tr>
					<td>
						<input type="file" name="attachment" size="50" />
						<input type="button" value="更多附件..." onClick="addtable('newstyle')" class="bt" />
					</td>
				</tr>
			</table>
			<span id="newstyle" ></span>
		</td>
		<td>&nbsp;</td>
	</tr>
	<c:if test="${!empty(taskReportVo.attachmentStr)}">
		<tr>
			<td colspan="3" valign="top">
				原附件信息(
				<font color=red>如果要删除某个附件，请选择该附件前面的选择框</font>)：
			</td>
		</tr>
		<tr>
			<td colspan="3">
				<c:forEach var="file" items="${_Attachment_Names}" varStatus="index">
					<form:checkbox path="attatchmentArray" value="${index.index}" />
					<a href="<%=request.getRealPath("/")%>${file}">${_Attachment_Names[index.index]}</a>
					<br>
				</c:forEach>
			</td>
		</tr>
	</c:if>        
	
	<tr> 
        <td colspan="3" bgcolor="#dfeffc">
          <input type="submit" id="button" style="cursor: pointer;" value="提交"/>
             &nbsp;
          <input type="button" value="关闭" style="cursor: pointer;" onclick="window.close();"/>
        </td>
    </tr>
    
</table>
</form:form>
</body>