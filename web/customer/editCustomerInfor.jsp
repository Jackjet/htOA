<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<title>编辑客户信息</title>
<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
<script src="<c:url value="/"/>js/inc_javascript.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/css/form.css"/>"/>

<!-- formValidator -->
<link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css"></link>
<script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript" charset="UTF-8"></script>
<script src="<c:url value="/"/>components/formvalidator/js/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
<!-- ------------- -->

<script>
	
	//验证
	$(document).ready(function(){
		$.formValidator.initConfig({formid:"customerInforForm",onerror:function(msg){alert(msg)},onsuccess:function(){submitData();}});
		$("#companyName").formValidator({onshow:"公司名称不能为空",onfocus:"公司名称不能为空",oncorrect:"输入正确"}).inputValidator({min:1,onerror:"公司名称不能为空，请确认"});
		$("#phone").formValidator({empty:true,onshow:"请输入电话，可以为空",onfocus:"格式例如：021-888888",oncorrect:"输入正确"}).regexValidator({regexp:"^[[0-9]{3}-|\[0-9]{4}-]?([0-9]{8}|[0-9]{7})?$",onerror:"输入的电话格式不正确"});
	});
	
	//提交数据
	function submitData() {
		/** var form = document.customerInforForm;
		form.action = '<c:url value="/customer/customerInfor"/>.do?method=save';
		form.submit(); */
		alert('信息编辑成功！');
		window.returnValue = "Y";
		window.close();
	}
</script>
<base target="_self"/>
<body >
<form:form commandName="customerInforVo" id="customerInforForm" name="customerInforForm" action="/customer/customerInfor.do?method=save" method="post">
<form:hidden path="customerId" />
<table width="98%" cellpadding="6" cellspacing="1" align="center" border="1" bordercolor="#c5dbec">
	<tr>
		<td colspan="3" bgcolor="#dfeffc"></td>
	</tr>
	
	<tr> 
       <td style="width: 15%">公司名称：</td>
       <td><form:input path="companyName" size="50"/></td>
       <td style="width: 15%"><div id="companyNameTip" style="width:250px"></div></td>
   	</tr>
                    
                    
   	<tr> 
       <td>	客户类型：</td>
       <td>潜在客户<form:radiobutton path="customerType" value="0"></form:radiobutton>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;客户<form:radiobutton path="customerType" value="1"></form:radiobutton></td>
       <td>&nbsp;</td>
   	</tr>
    
    <tr> 
       <td>	公司介绍：</td>
       <td><form:textarea path="companyIntroduction" rows="5" cols="50"></form:textarea></td>
       <td>&nbsp;</td>
   	</tr>
    
    <tr> 
       <td> 电话：</td>
       <td><form:input path="phone" size="20"/></td>
       <td><div id="phoneTip" style="width:250px"></div></td>
    </tr>
    
    <tr> 
       <td> 网址：</td>
       <td><form:input path="website" size="50"/></td>
       <td>&nbsp;</td>
    </tr>
    
    <tr> 
       <td> 公司地址：</td>
       <td><form:input path="companyAddress" size="50"/></td>
       <td>&nbsp;</td>
    </tr>

    <tr> 
       <td> 备注：</td>
       <td width="40%"><form:textarea path="memo" rows="5" cols="50"/></td>
       <td>&nbsp;</td>
    </tr>
             
     <tr> 
        <td colspan="3" bgcolor="#dfeffc">
          <input type="submit" id="button" style="cursor: pointer;" value="保存" />
             &nbsp;
          <input type="button" value="关闭" style="cursor: pointer;" onclick="window.close();"/>
        </td>
     </tr>
</table>
</form:form>
</body>

