<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<title>编辑活动信息</title>
<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
<script src="<c:url value="/"/>js/inc_javascript.js" type="text/javascript"></script>
<script src="<c:url value="/"/>datePicker/WdatePicker.js" language="JavaScript"></script>
<script src="<c:url value='/js'/>/addattachment.js"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/css/form.css"/>"/>

<!-- formValidator -->
<link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css"></link>
<script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript" charset="UTF-8"></script>
<script src="<c:url value="/"/>components/formvalidator/js/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
<!-- ------------- -->

<script>
	//验证
	$(document).ready(function(){
		//alert(${param.rowId});
		$.formValidator.initConfig({formid:"activityInforForm",onerror:function(msg){alert(msg)},onsuccess:function(){}});
			$("#content").formValidator({onshow:"活动内容不能为空",onfocus:"请输入活动内容",oncorrect:"输入正确"}).inputValidator({min:1,onerror:"活动内容不能为空，请确认！"})
			$("#customerId").formValidator({onshow:"请选择客户",onfocus:"客户必须选择",oncorrect:"选择正确"}).inputValidator({min:1,onerror: "你是不是忘记选择客户了!"});
			$("#planDateStr").formValidator({onshow:"计划时间必须输入",onfocus:"请选择",oncorrect:"选择正确"}).inputValidator({min:1,onerror:"请输入计划时间！"});
	});
	
</script>
<base target="_self"/>
<body >
<form:form commandName="activityInforVo" id="activityInforForm" name="activityInforForm" action="/customer/activityInfor.do?method=save" method="post" enctype="multipart/form-data">
<form:hidden path="activityId" />
<table width="98%" cellpadding="6" cellspacing="1" align="center" border="1" bordercolor="#c5dbec">
	<tr>
		<td colspan="3" bgcolor="#dfeffc"></td>
	</tr>
	
	<tr> 
       <td width="20%">活动内容：</td>
       <td width="60%"><form:textarea path="content" cols="41" rows="2"/></td>
       <td><div id="contentTip" style="width:250px"></div></td>
   	</tr>
                    
                    
   	<tr> 
       <td width="20%">	活动地点：</td>
       <td width="60%"><form:input path="activityPlace" size="50"/></td>
       <td>&nbsp;</td>
   	</tr>
   	
    
    <tr>
       <td width="20%">客户：</td>
	   <td width="60%"><form:select path="customerId">
	     <form:option value="" label="--请选择--"/>
	     <form:options  items="${customerInforList}" itemValue="customerId" itemLabel="companyName"/>
 	     </form:select>
 	   </td>
       <td><div id="customerIdTip" style="width:250px"></div></td>
    </tr>

    <tr> 
       <td width="20%">	计划时间：</td>
       <td width="60%"><form:input  path="planDateStr" class="searchString"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly="true" size="20"/></td>
       <td><div id="planDateStrTip" style="width:250px"></div></td>
   	</tr>
    
    <tr> 
       <td width="20%"> 实际时间：</td>
       <td width="60%"><form:input  path="activityDateStr" class="searchString"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly="true" size="20"/></td>
       <td>&nbsp;</td>
    </tr>
    
    <tr> 
       <td width="20%"> 备注：</td>
       <td width="60%"><form:textarea path="memo" cols="41" rows="2"/></td>
       <td>&nbsp;</td>
    </tr>
    
    <tr> 
       <td width="20%"> 后续反馈：</td>
       <td width="60%"><form:textarea path="feedback" cols="41" rows="2"/></td>
       <td>&nbsp;</td>
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
	<c:if test="${!empty(activityInforVo.attachmentStr)}">
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
          <input type="submit" value="保存" id="button" style="cursor: pointer;"/>
             &nbsp;
          <input type="button" value="关闭" style="cursor: pointer;" onclick="window.close();"/>
        </td>
     </tr>
</table>
</form:form>
</body>

