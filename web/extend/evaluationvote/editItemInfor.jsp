<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<title>设计投票条目信息</title>
<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
<script src="<c:url value="/"/>datePicker/WdatePicker.js" type="text/javascript"></script>
<script src="<c:url value="/"/>js/inc_javascript.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/css/form.css"/>"/>
<link rel="stylesheet" type="text/css" href="/css/myTable.css"/>
<!-- formValidator -->
<link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css"></link>
<script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript" charset="UTF-8"></script>
<!--<script src="<c:url value="/"/>components/formvalidator/js/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>

-->
<script>
	
	$(document).ready(function(){
		//验证
		//$.formValidator.initConfig({formid:"itemInforForm",onerror:function(msg){alert(msg)},onsuccess:function(){submitData();}});
		//$("#itemName").formValidator({onshow:"请输入投票标题",onfocus:"投票标题不能为空",oncorrect:"输入正确"}).inputValidator({min:1,onerror:"投票标题不能为空,请确认"});
		
		//window.scrollTo(0,99999);
	});
	var isArray = false;
	//提交数据
	function submitData() {
		
		if(check()){
			var form = document.pyItemInforForm;
			form.submit();
			alert('信息编辑成功！');
			//window.returnValue = "refresh";
			//window.close();
			//window.location.reload();
		}
	}
	
	
	function check(){
		var flag = true;
		var form = document.pyItemInforForm;
		//标题
		var itemName = form.itemName;
	
		//排序号
		var displayOrder = form.displayOrder;
		
		if(itemName.value == ''){
			flag = false;
			alert("条目标题不能为空！");
			itemName.focus();
		}
		
	
		
		if(displayOrder.value == '' || displayOrder.value < 1){
			flag = false;
			alert("请正确填写排序号！");
			displayOrder.focus();
		}
		
		return flag;
	}
	
	

	
	//新增选项信息
	function addOptionInfor(itemId){
		var refresh = window.showModalDialog("/tpwj/itemInfor.do?method=editOption&itemId="+itemId,null,"dialogWidth:1000px;dialogHeight:600px;center:Yes;dialogTop: 100px; dialogLeft: 200px;");
		if(refresh == "refresh") {
			self.location.reload();
		}
	}
</script>
<style>
	html,
	body {

		margin: auto;
		padding: auto;
		font: 14px/1.8 Georgia, Arial, Simsun;
		background-image: url(/img/bgIn.png);
		background-size: cover;
		background-color: #bcbcbc;
		font-family:  "黑体" ;
	}
	img{
		border:0;
	}
	li{
		list-style:none;
	}
	input,label { vertical-align:middle;}
</style>
<base target="_self"/>
<body>
<br/>
<form:form commandName="pyItemInforVo" id="pyItemInforForm" name="pyItemInforForm" action="/extend/pyItemInfor.do?method=save" method="post" >
<form:hidden path="itemId"/>
<table width="80%" cellpadding="6" cellspacing="1" align="center" border="0" bordercolor="#0DE8F5">
	<tr>
		<td colspan="2"  align="center"><form:hidden path="topicId"/><h2><font color="#15A7BC">${_Topic.topicName}</font></h2></td>
	</tr>
	
	<tr>
		<td colspan=2 align=center>
			
          
          	
          			<c:forEach items="${_Topic.itemInfors}" var="item" varStatus="index">
          				
							<table style="border:2px dotted #0DE8F5;" width="99%">
								<tr>  
							    	<td  align="left" style="color: #bcbcbc">
							    		&nbsp;&nbsp;&nbsp;&nbsp;
							    		<span style="color:white;font-size:15px;">
							    			<b>${index.index+1}、${item.itemName}</b>
							    		</span>
							    	
						    			【
						    			<c:choose>
								       		<c:when test="${item.itemType == 0}">单选型</c:when>
								       		<c:when test="${item.itemType == 1}">多选型</c:when>
								       	
								       	</c:choose>
								       	】<a style="color: #bcbcbc" href="/extend/pyItemInfor.do?method=edit&rowId=${item.topicInfor.topicId}&itemId=${item.itemId}">编辑</a>&nbsp;    <a style="color: #bcbcbc" href="/extend/pyOptionInfor.do?method=edit&rowId=${item.topicInfor.topicId}&itemId=${item.itemId}">新增选项</a>&nbsp;    <a style="color: #bcbcbc" href="/extend/pyItemInfor.do?method=delete&rowId=${item.topicInfor.topicId}&itemId=${item.itemId}">删除</a>
						    			
							    	</td>
								</tr>
								<c:forEach items="${item.optionInfors}" var="option" varStatus="index">
								<tr><td align="left">
									<c:if test="${item.itemType==0}">
									<input type="radio" name="type" value="${option.optionId}" />${option.optionName} &nbsp;<a style="color: #bcbcbc" href="/extend/pyOptionInfor.do?method=edit&rowId=${item.topicInfor.topicId}&optionId=${option.optionId}">编辑</a>
									</c:if>
									<c:if test="${item.itemType==1}">
									<input type="checkbox" name="type" value="${option.optionId}"/>${option.optionName}&nbsp;<a style="color: #bcbcbc" href="/extend/pyOptionInfor.do?method=edit&rowId=${item.topicInfor.topicId}&optionId=${option.optionId}">编辑</a>
									</c:if>
								</td> </tr>  
								</c:forEach>
							<!--</table> 
							<table style="border-left:2px dotted #0DE8F5;border-bottom:2px dotted #0DE8F5;border-right:2px dotted #0DE8F5;" width="99%">   -->
								<tr>  
							    	<td>
							    		
							    	</td>
								</tr>  
								                                        
							</table>  
							<br/>
					
					</c:forEach>
          	
          
			
			
		</td>
	</tr>
	
	<tr>
		<td colspan=2 width="40%"  align="left">&nbsp;&nbsp;&nbsp;&nbsp;<font color="white"><b>编辑条目信息</b></font></td>
	</tr>
	
	<tr>
		<td colspan=2 style="padding-left:20px;padding-top:10px;" valign=top>
			<!--<fieldset style="padding-left:20px;border:2px solid #15A7BC">
				<legend><font color=red><b>编辑单个条目信息</b></font></legend>-->
				<table>
					<tr> 
				       <td nowrap align=right width="20%">条目标题：</td>
				       <td><form:textarea rows="2" cols="60" path="itemName"></form:textarea></td>
				       <td><div id="itemNameTip" style="width:0px"></div></td>
				    </tr>
				
        
				                    
				    <tr> 
				       <td nowrap align=right>条目类型：</td>
				       <td colspan="2">
				           <form:select path="itemType">
							  <form:option value="0">单选型</form:option>
							  <form:option value="1">多选型</form:option>
							
						   --></form:select>
				       </td>
				    </tr>

					<tr> 
				       <td nowrap align=right>排序号：</td>
				       <td colspan="2"><form:input path="displayOrder" size="20" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/></td>
				    </tr>
				</table>
			<!--</fieldset>-->
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
