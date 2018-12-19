<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<title>编辑惯用语信息</title>
<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
<script src="<c:url value="/"/>js/inc_javascript.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/css/form.css"/>"/>
<link rel="stylesheet" type="text/css" href="/css/noTdBottomBorder.css"/>
<link rel="stylesheet" type="text/css" href="/css/myTable.css"/>

<!-- formValidator -->
<link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css"></link>
<script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript" charset="UTF-8"></script>
<script src="<c:url value="/"/>components/formvalidator/js/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
<!-- ------------- -->

<script>
	
	$(document).ready(function(){
		//验证
		$.formValidator.initConfig({formid:"approveSentenceForm",onerror:function(msg){alert(msg)},onsuccess:function(){submitData();}});
		$("#sentence").formValidator({onshow:"请输入惯用语",onfocus:"惯用语不能为空",oncorrect:"输入正确"}).inputValidator({min:1,onerror:"惯用语不能为空,请确认"});
		
	});
	
	//提交数据
	function submitData() {
		alert('信息编辑成功！');
		window.returnValue = "refresh";
		window.close();
	}
	



</script>
<base target="_self"/>
<body style="padding: 0 100px">
<br/>
<form:form commandName="approveSentenceVo" id="approveSentenceForm" name="approveSentenceForm" action="/core/approveSentence.do?method=save" method="post">
<form:hidden path="sentenceId"/>
<table width="98%" cellpadding="6" cellspacing="1" align="center" border="0" bordercolor="#c5dbec">
	<%--<tr>
		<td colspan="3" bgcolor="#dfeffc"></td>
	</tr>--%>
	
	<tr> 
       <td width="20%">惯用语：</td>
       <td><form:input path="sentence" size="20"/></td>
       <td><div id="sentenceTip" style="width:250px"></div></td>
    </tr>
                    
    <tr> 
       <td>排序号：</td>
      	<td><form:input path="orderNo" size="20"/></td>
		<td><div id="orderNoTip" style="width:250px"></div></td>
    </tr>
                    
          
    
                    
     <tr> 
        <td colspan="3">
          <input type="submit" id="button" style="cursor: pointer;" value="提交"/>
             &nbsp;
          <input type="button" value="关闭" style="cursor: pointer;" onclick="window.close();"/>
        </td>
     </tr>
</table>
</form:form>
</body>

