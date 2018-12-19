<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
<title>任务审核</title>
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
	
	//提交数据
	function submitData() {
		alert('信息编辑成功');
		window.returnValue = "Y";
		window.close();
	}
	
</script>
<base target="_self"/>
</head>
	 
<body>
<br/>
<form:form commandName="taskInforVo" id="taskInforForm" name="taskInforForm" action="/customer/taskInfor.do?method=statuSave" method="post" >
<form:hidden path="taskId"/>  
<table width="98%" cellpadding="6" cellspacing="1" align="center" border="1" bordercolor="#c5dbec">
   <tr>
	 <td colspan="2" bgcolor="#dfeffc">${_TaskName}</td>
   </tr>

   <tr> 
     <td width="40%">任务状态：</td>
     <td width="60%"><form:select path="status" >
     	<form:option value="0">---进行---</form:option>
     	<form:option value="1">---通过---</form:option>
     	<form:option value="2">---暂停---</form:option>
     	<form:option value="3">---过期---</form:option>
     	</form:select>
     </td>
   </tr>
   
	<tr> 
        <td colspan="2" bgcolor="#dfeffc">
          <input type="submit" id="button" style="cursor: pointer;" value="提交" onclick="submitData();"/>
             &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
             &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
             &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <input type="button" value="关闭" style="cursor: pointer;" onclick="window.close();"/>
        </td>
    </tr>
    
</table>
</form:form>
</body>