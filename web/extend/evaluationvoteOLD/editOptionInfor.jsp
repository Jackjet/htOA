<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<title>编辑选项信息</title>
<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
<script src="<c:url value="/"/>datePicker/WdatePicker.js" type="text/javascript"></script>
<script src="<c:url value="/"/>js/inc_javascript.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/css/form.css"/>"/>

<!-- formValidator -->
<link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css"></link>
<script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript" charset="UTF-8"></script>
<!--<script src="<c:url value="/"/>components/formvalidator/js/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>

--><script>
	
	$(document).ready(function(){
		//验证
		//$.formValidator.initConfig({formid:"itemInforForm",onerror:function(msg){alert(msg)},onsuccess:function(){submitData();}});
		//$("#itemName").formValidator({onshow:"请输入投票标题",onfocus:"投票标题不能为空",oncorrect:"输入正确"}).inputValidator({min:1,onerror:"投票标题不能为空,请确认"});
		
	});
	var isArray = false;
	//提交数据
	function submitData() {
		
		if(check()){
			var form = document.pyOptionInforForm;
			form.submit();
			alert('信息编辑成功！');
			//window.returnValue = "refresh";
			//window.close();
		}
	}
	
	//判断表单内容是否为空
	function check(){
		var flag = true;
		var form = document.pyOptionInforForm;
		
		//选项序号
		var optionOrder = form.displayOrder;
		//选项文本
		var optionName = form.optionName;
		//选项值
		var selectNum = form.selectNum;

		
		
		var orderValue = optionOrder.value;
		var nameValue = optionName.value;
		//var selectNum = selectNum.value;
	
		
		if(orderValue == '' || orderValue == 'undefined'){
			flag = false;
			alert("序号不能为空！");
			optionOrder.focus();	
		}


		
		
		return flag;
	}
	
	function is_float(js_value) {
	   var re = /^\s*$/;
	   var re1 = /^[0-9]{1,}\.{0,1}[0-9]{0,2}0*$/;
	   if(js_value.match(re)) {
	      return true;
	   }
	   if(js_value.match(re1))
	      return true;
	   return false;
	}
</script>
<base target="_self"/>
<body>
<br/>
<form:form commandName="pyOptionInforVo" id="pyOptionInforForm" name="pyOptionInforForm" action="/extend/pyOptionInfor.do?method=save" method="post" enctype="multipart/form-data">
<form:hidden path="optionId"/>
<input type="hidden" name="rowId" value="<%=request.getParameter("rowId") %>"/>
<table width="98%" cellpadding="6" cellspacing="1" align="center" border="1" bordercolor="#c5dbec">
	<tr>
		<td colspan="2" bgcolor="#dfeffc" align="left" style="padding-left:5px;">
			<form:hidden path="itemId"/>题目：<b><font color="#15A7BC">${_Item.itemName}</font></b>
		</td>
	</tr>
	
	<tr>
		<td valign=top style="padding-top:10px;">
			<table id="optionTab" class="optionTab" width="50%">
				<tr>
					<td width="20%" nowrap align=right>选项序号：</td>
					<td><form:input path="displayOrder" size="5" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/></td>
				</tr>

					<tr>
						<td nowrap align=right>选项名称：</td>
						<td><form:input path="optionName" size="40"/></td>
					</tr>
				
					<tr>
						<td nowrap align=right>选择次数：</td>
						<td><form:input path="selectNum"  size="5" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/></td>
					</tr>
		
			</table>
		</td>
	</tr>
	
     <tr> 
        <td colspan="2" bgcolor="#dfeffc" align=left>
          <input type="button" id="button" style="cursor: pointer;" onclick="submitData();" value="提交"/>
             &nbsp;
          <input type="button" value="关闭" style="cursor: pointer;" onclick="window.close();"/>
        </td>
     </tr>
</table>
</form:form>
</body>

