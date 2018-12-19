<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<title>编辑选项信息</title>
<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
<script src="<c:url value="/"/>datePicker/WdatePicker.js" type="text/javascript"></script>
<script src="<c:url value="/"/>js/inc_javascript.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/css/form.css"/>"/>
<link rel="stylesheet" type="text/css" href="<c:url value="/css/myTable.css"/>"/>

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
			var form = document.optionInforForm;
			form.submit();
			alert('信息编辑成功！');
			window.returnValue = "refresh";
			window.close();
		}
	}
	
	//判断表单内容是否为空
	function check(){
		var flag = true;
		var form = document.optionInforForm;
		
		//选项序号
		var optionOrder = form.displayOrder;
		//选项文本
		var optionName = form.optionName;
		//选项值
		var optionValue = form.optionValue;
		//权重
		//var ration = form.ration;
		//分数
		var score = form.score;
		//图片
		var optionPicPath = form.picPath;
		
		var optionId = form.optionId.value;
		
		var orderValue = optionOrder.value;
		
		var nameValue = '';
		if(optionName != null){
			nameValue = optionName.value;
		}
		
		var valueValue = '';
		if(optionValue != null){
			valueValue = optionValue.value;
		}
		
		
		//var rationValue = '';
		//if(ration != null){
		//	rationValue = ration.value;
		//}
		
		var scoreValue = '';
		if(score != null){
			scoreValue = score.value;
		}
		
		
		var picValue = '';
		if(optionPicPath != null){
			picValue = optionPicPath.value;
		}
		
		if(orderValue == '' || orderValue == 'undefined' ||orderValue < 1){
			flag = false;
			alert("请正确填写排序号！");
			optionOrder.focus();	
		}
		
		//计分时
		if (${_ItemInfor.score > 0}) {
			if(${_ItemInfor.itemType == 0 || _ItemInfor.itemType == 1}){
				//单选或者多选时，文本、值、权重均不能为空
				if(nameValue == '' || nameValue == 'undefined'){
					flag = false;
					alert("选项文本不能为空！");
					optionName.focus();	
				}
				if(valueValue == '' || valueValue == 'undefined'){
					flag = false;
					alert("选项值不能为空！");
					optionValue.focus();	
				}
				//if(rationValue == '' || rationValue == 'undefined'){
				//	flag = false;
				//	alert("权重不能为空！");
				//	ration.focus();	
				//}
				if(scoreValue == '' || scoreValue == 'undefined'){
					flag = false;
					alert("分数不能为空！");
					score.focus();	
				}
			}else if(${_ItemInfor.itemType == 2 || _ItemInfor.itemType == 3}){
				//文本时，只序号不为空即可
			}else if(${_ItemInfor.itemType == 4}){
				//图片型时，选项值、权重、图片上传不可为空
				if(valueValue == '' || valueValue == 'undefined'){
					flag = false;
					alert("选项值不能为空！");
					optionValue.focus();	
				}
				
				//if(rationValue == '' || rationValue == 'undefined'){
				//	flag = false;
				//	alert("权重不能为空！");
				//	ration.focus();	
				//}
				if(scoreValue == '' || scoreValue == 'undefined'){
					flag = false;
					alert("分数不能为空！");
					score.focus();	
				}
				if((picValue == '' || picValue == 'undefined') && (optionId == 0 || optionId == null)){
					flag = false;
					alert("选项图片不能为空！");
					optionPicPath.focus();	
				}
			}
		}
		//计次时
		if (${_ItemInfor.score == 0}) {
			if(${_ItemInfor.itemType == 0 || _ItemInfor.itemType == 1}){
				//单选或者多选时，文本、值不能为空
				if(nameValue == '' || nameValue == 'undefined'){
					flag = false;
					alert("选项文本不能为空！");
					optionName.focus();	
				}
				if(valueValue == '' || valueValue == 'undefined'){
					flag = false;
					alert("选项值不能为空！");
					optionValue.focus();	
				}
			}else if(${_ItemInfor.itemType == 2 || _ItemInfor.itemType == 3}){
				//文本时，只序号不为空即可
			}else if(${_ItemInfor.itemType == 4}){
				//图片型时，选项值、权重、图片上传不可为空
				if(valueValue == '' || valueValue == 'undefined'){
					flag = false;
					alert("选项值不能为空！");
					optionValue.focus();	
				}
				if((picValue == '' || picValue == 'undefined') && (optionId == 0 || optionId == null)){
					flag = false;
					alert("选项图片不能为空！");
					optionPicPath.focus();	
				}
			}
		}
		
		if(picValue != ''){
			var point = picValue.lastIndexOf(".");
			var filetype = picValue.substr(point).toLowerCase();
			
			if(filetype!=".jpg" && filetype!=".jpeg" && filetype!=".bmp" && filetype!=".png" && filetype!=".gif" && filetype!=".ico"){ 
				optionPicPath.focus(); 
				alert("上传的附件必须为图片文件！"); 
				flag = false; 
			}
		}
		
		//if(rationValue != ''){
		//	if(!is_float(rationValue) || rationValue<0 || rationValue>1){
		//		flag = false;
		//		alert("权重字段请输入0-1之间的数值！");
		//		ration.focus();
		//	}
		//}
		
		
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
<form:form commandName="optionInforVo" id="optionInforForm" name="optionInforForm" action="/tpwj/itemInfor.do?method=saveOption" method="post" enctype="multipart/form-data">
<form:hidden path="optionId"/>
<table width="98%" cellpadding="6" cellspacing="1" align="center" border="0" bordercolor="#c5dbec">
	<tr>
		<td colspan="2"  align="left" style="padding-left:5px;">
			<form:hidden path="itemId"/>题目：<b><font color="#15A7BC">${_ItemInfor.itemName}</font></b>
		</td>
	</tr>
	
	<tr>
		<td valign=top style="padding-top:10px;">
			<table id="optionTab" class="optionTab" width="80%">
				<tr>
					<td width="20%" nowrap align=right>选项序号：</td>
					<td><form:input path="displayOrder" size="5" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/></td>
				</tr>
				
				<c:if test="${_ItemInfor.itemType == 0 || _ItemInfor.itemType == 1}">
					<tr>
						<td nowrap align=right>选项文本：</td>
						<td><form:input path="optionName" size="40"/></td>
					</tr>
				</c:if>
				<c:if test="${_ItemInfor.itemType == 0 || _ItemInfor.itemType == 1 || _ItemInfor.itemType == 4}">
					<tr>
						<td nowrap align=right>选项值：</td>
						<td><form:input path="optionValue" size="5"/></td>
					</tr>
				</c:if>
				<c:if test="${(_ItemInfor.score > 0) && (_ItemInfor.itemType == 0 || _ItemInfor.itemType == 1 || _ItemInfor.itemType == 4)}">
					<tr>
						<td nowrap align=right>分数：</td>
						<td><form:input path="score" size="5"/></td>
					</tr>
				</c:if>
				<c:if test="${_ItemInfor.itemType == 0 || _ItemInfor.itemType == 1 || _ItemInfor.itemType == 4}">
					<tr>
						<td nowrap align=right>图片：</td>
						<td><input type="file" name="picPath" size="50"/></td>
					</tr>
				</c:if>
				<c:if test="${!empty _Attachment_Names}">
					<tr>
						<td colspan="2">原附件信息(<font color=red>如果要删除某个附件，请选择该附件前面的选择框；如果上传了新的文件，原文件将自动被替换！</font>)：</td>
					</tr>
					<tr>
						<td></td>
						<td>
							<c:forEach var="file" items="${_Attachment_Names}" varStatus="index">
								<input type="checkbox" name="picAttach" value="${index.index}" />
								<a href="<%=request.getRealPath("/")%>${file}">${_Attachment_Names[index.index]}</a><br/>
							</c:forEach>
						</td>
					</tr>
				</c:if>
			</table>
		</td>
	</tr>
	
     <tr> 
        <td colspan="2"  align=left>
          <input type="button" id="button" style="cursor: pointer;" onclick="submitData();" value="提交"/>
             &nbsp;
          <input type="button" value="关闭" style="cursor: pointer;" onclick="window.close();"/>
        </td>
     </tr>
</table>
</form:form>
</body>

