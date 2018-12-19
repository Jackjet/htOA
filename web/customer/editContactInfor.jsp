<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<title>编辑联系人信息</title>
<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
<script src="<c:url value="/"/>js/inc_javascript.js" type="text/javascript"></script>
<script src="<c:url value="/"/>datePicker/WdatePicker.js" language="JavaScript"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/css/form.css"/>"/>
<link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css"></link>
<script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript" charset="UTF-8"></script>
<script src="<c:url value="/"/>components/formvalidator/js/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
<link type="text/css" href="<c:url value="/"/>components/jqgrid/css/jquery.ui.all.css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
<!-- ------------- -->

<script>
	
	//验证
	$(document).ready(function(){
		//alert(${param.rowId});
		$.formValidator.initConfig({formid:"contactInforForm",onerror:function(msg){alert(msg)},onsuccess:function(){submitData();}});
			$("#contactName").formValidator({onshow:"联系人姓名不能为空",onfocus:"请输入联系人姓名",oncorrect:"输入正确"}).inputValidator({min:1,onerror:"联系人姓名不能为空，请确认"})
			$("#phone").formValidator({onshow:"电话可以为空",onfocus:"请输入13位以内的数字",oncorrect:"输入正确"}).inputValidator({max:9999999999999,type:"number",onerror:"对不起，您输入的电话长度或格式不正确"})
			$("#mobile").formValidator({onshow:"手机可以为空",onfocus:"请输入13位以内的数字",oncorrect:"输入正确"}).inputValidator({max:9999999999999,type:"number",onerror:"对不起，您输入的手机长度或格式不正确"})
			$("#email").formValidator({empty:true,onshow:"请输入邮箱，可以为空",onfocus:"邮箱至少6个字符,最多100个字符",oncorrect:"输入正确"}).inputValidator({min:6,max:100,onerror:"你输入的邮箱长度非法，请确认"})
			.regexValidator({regexp:"^([\\w-.]+)@(([[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.)|(([\\w-]+.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(]?)$",onerror:"你输入的邮箱格式不正确"});
			
	});
	
	//提交数据
	function submitData() {
		var path;
		path = "<c:url value='/customer/customerInfor.do'/>?method=saveContact";
		window.name = "__self";
		window.open(path, "__self");  //注意是2个下划线 
	}
	
</script>
<base target="_self"/>
<body>
<form:form commandName="contactInforVo" id="contactInforForm" name="contactInforForm" action="/customer/customerInfor.do?method=saveContact" method="post">
<input type="hidden" name="customerId" value="<%=request.getParameter("customerId")%>"/>
<form:hidden path="contactId" />
<table width="98%" cellpadding="6" cellspacing="1" align="center" border="1" bordercolor="#c5dbec">
	<tr>
		<td colspan="3" bgcolor="#dfeffc" style="font-size: 18;font-weight: bold;font-family:Verdana; ">客户信息</td>
	</tr>
	
	<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 10px;">
		<td style="width: 15%;" class="ui-state-default jqgrid-rownum">公司名称：</td>
		<td style="width: 85%;" colspan="2">${_CustomerInfor.companyName}</td>
	</tr>
	
	<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 10px;">
		<td class="ui-state-default jqgrid-rownum" >客户类型：</td>
		<td valign="top" colspan="2"><c:if test="${_CustomerInfor.customerType==0}">潜在客户</c:if><c:if test="${_customerinfor.customerType==1}">客户</c:if></td>
	</tr>
	
	<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 50px;">
		<td class="ui-state-default jqgrid-rownum">公司介绍：</td>
		<td valign="top" colspan="2">${_CustomerInfor.companyIntroduction}</td>
	</tr>
	
	<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 10px;">
		<td class="ui-state-default jqgrid-rownum">公司网址：</td>
		<td colspan="2">${_CustomerInfor.website}</td>
	</tr>
	
	<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 10px;">
		<td class="ui-state-default jqgrid-rownum">公司电话：</td>
		<td colspan="2">${_CustomerInfor.phone}</td>
	</tr>
	
	<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 20px;">
		<td class="ui-state-default jqgrid-rownum">公司地址：</td>
		<td colspan="2">${_CustomerInfor.companyAddress}</td>
	</tr>
	
	<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 70px;">
		<td class="ui-state-default jqgrid-rownum">备注：</td>
		<td valign="top" colspan="2">${_CustomerInfor.memo}</td>
	</tr>
	
	<tr>
		<td colspan="3" bgcolor="#dfeffc" style="font-size: 18;font-weight: bold;font-family:Verdana; ">添加联系人信息</td>
	</tr>
	
	<tr> 
       <td width="20%">联系人姓名：</td>
       <td><form:input path="contactName" size="20"/></td>
       <td><div id="contactNameTip" style="width:250px"></div></td>
   	</tr>
                    
                    
   	<tr> 
       <td width="20%">	职务：</td>
       <td><form:input path="duty" size="20"/></td>
       <td><div id="roleNameTip" style="width:250px"></div></td>
   	</tr>
    
    <tr> 
       <td width="20%">	固定电话：</td>
       <td><form:input path="phone" size="20"/></td>
       <td><div id="phoneTip" style="width:250px"></div></td>
   	</tr>
    
    <tr> 
       <td width="20%"> 手机：</td>
       <td><form:input path="mobile" size="20"/></td>
       <td><div id="mobileTip" style="width:250px"></div></td>
    </tr>
    
    <tr> 
       <td width="20%"> 传真：</td>
       <td><form:input path="fax" size="20"/></td>
       <td><div id="roleNameTip" style="width:250px"></div></td>
    </tr>
    
    <tr> 
       <td width="20%"> 电子邮箱：</td>
       <td><form:input path="email" size="20"/></td>
       <td><div id="emailTip" style="width:250px"></div></td>
    </tr>
    
     <tr> 
       <td width="20%"> 生日：</td>
       <td><form:input path="birthdayStr" class="searchString"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly="true" size="20"/></td>
       <td><div id="roleNameTip" style="width:250px"></div></td>
    </tr>
                    
     <tr> 
        <td colspan="3" bgcolor="#dfeffc">
          <input type="submit" id="button" style="cursor: pointer;" value="保存"  />
             &nbsp;
          <input type="button" value="关闭" style="cursor: pointer;" onclick="window.close();"/>
        </td>
     </tr>
</table>
</form:form>
</body>

