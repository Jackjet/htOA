<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<title>设计投票条目信息</title>
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
			var form = document.itemInforForm;
			form.submit;
			alert('信息编辑成功！');
			window.returnValue = "refresh";
			window.close();
		}
	}
	
	//判断表单内容是否为空
	function check(){
		var flag = true;
		var form = document.itemInforForm;
		var itemName = form.itemName;
		
		//选项序号
		var optionOrder = form.optionOrder;
		//选项文本
		var optionName = form.optionName;
		//选项值
		var optionValue = form.optionValue;
		//权重
		var ration = form.ration;
		//图片
		var optionPicPath = form.optionPicPath;
		
		if(itemName.value == ''){
			flag = false;
			alert("条目标题不能为空！");
			itemName.focus();
		}
		
		
		//统计方式及条目类型
		var countType = form.countType.value;
		var itemType = form.itemType.value;
		
		var count = 0;
		$(".optionTab").each(function (i){
			if($(this).html() != '' && $(this).html() != null){
				count += 1;
			}
		});
		//$(".optionTab").each(function (i){
		for(var i=0;i<count;i++){
			var orderValue = '';
			var nameValue = '';
			var valueValue = '';
			var rationValue = '';
			var picValue = '';
			
			if(count > 1){
				orderValue = optionOrder[i].value;
				nameValue = optionName[i].value;
				valueValue = optionValue[i].value;
				rationValue = ration[i].value;
				picValue = optionPicPath[i].value;
			}else if(count == 1){
				orderValue = optionOrder.value;
				nameValue = optionName.value;
				valueValue = optionValue.value;
				rationValue = ration.value;
				picValue = optionPicPath.value;
			}
			
		
			if(orderValue == '' || orderValue == 'undefined'){
				flag = false;
				alert("序号不能为空！");
				optionOrder[i].focus();	
			}
			
			//计分时
			if (countType == '1') {
				if(itemType == '0' || itemType == '1'){
					//单选或者多选时，文本、值、权重均不能为空
					if(nameValue == '' || nameValue == 'undefined'){
						flag = false;
						alert("选项文本不能为空！");
						optionName[i].focus();	
					}
					if(valueValue == '' || valueValue == 'undefined'){
						flag = false;
						alert("选项值不能为空！");
						optionValue[i].focus();	
					}
					if(rationValue == '' || rationValue == 'undefined'){
						flag = false;
						alert("权重不能为空！");
						ration[i].focus();	
					}
				}else if(itemType == '2' || itemType == '3'){
					//文本时，只序号不为空即可
				}else if(itemType == '4'){
					//图片型时，选项值、权重、图片上传不可为空
					if(valueValue == '' || valueValue == 'undefined'){
						flag = false;
						alert("选项值不能为空！");
						optionValue[i].focus();	
					}
					
					if(rationValue == '' || rationValue == 'undefined'){
						flag = false;
						alert("权重不能为空！");
						ration[i].focus();	
					}
					if(picValue == '' || picValue == 'undefined'){
						flag = false;
						alert("选项图片不能为空！");
						optionPicPath[i].focus();	
					}
				}
			}
			//计次时
			if (countType == '2') {
				if(itemType == '0' || itemType == '1'){
					//单选或者多选时，文本、值不能为空
					if(nameValue == '' || nameValue == 'undefined'){
						flag = false;
						alert("选项文本不能为空！");
						optionName[i].focus();	
					}
					if(valueValue == '' || valueValue == 'undefined'){
						flag = false;
						alert("选项值不能为空！");
						optionValue[i].focus();	
					}
				}else if(itemType == '2' || itemType == '3'){
					//文本时，只序号不为空即可
				}else if(itemType == '4'){
					//图片型时，选项值、权重、图片上传不可为空
					if(valueValue == '' || valueValue == 'undefined'){
						flag = false;
						alert("选项值不能为空！");
						optionValue[i].focus();	
					}
					if(picValue == '' || picValue == 'undefined'){
						flag = false;
						alert("选项图片不能为空！");
						optionPicPath[i].focus();	
					}
				}
			}
		}
		//});
		
		return flag;
	}
	
	
	function hdDs(){
		var form = document.itemInforForm;
		
		//统计方式及条目类型
		var countType = form.countType.value;
		var itemType = form.itemType.value;
		
		var scoreTR = document.getElementById('scoreTR');
		//var picTR = document.getElementById('picTR');
		
		//文本
		var textTH = document.getElementById('textTH');
		var textTD = document.getElementById('textTD');
		
		//值
		var optionValueTH = document.getElementById('optionValueTH');
		var optionValueTD = document.getElementById('optionValueTD');
		
		//权重
		var rationTH = document.getElementById('rationTH');
		var rationTD = document.getElementById('rationTD');
		
		//选项图片
		var picPathTH = document.getElementById('picPathTH');
		var picPathTD = document.getElementById('picPathTD');
		
		//计分时
		if (countType == '1') {
			scoreTR.style.display = '';
			
			if(itemType == '0' || itemType == '1'){
				//picTR.style.display = 'none';
				//picTR.value = '';
			
				textTH.style.display = '';
				textTD.style.display = '';
				
				optionValueTH.style.display = '';
				optionValueTD.style.display = '';
				
				rationTH.style.display = '';
				rationTD.style.display = '';
				
				picPathTH.style.display = '';
				picPathTD.style.display = '';
			}else if(itemType == '2' || itemType == '3'){
				//picTR.style.display = 'none';
				//picTR.value = '';
				
				textTH.style.display = 'none';
				textTD.style.display = 'none';
				
				optionValueTH.style.display = 'none';
				optionValueTD.style.display = 'none';
				
				rationTH.style.display = 'none';
				rationTD.style.display = 'none';
				
				picPathTH.style.display = 'none';
				picPathTD.style.display = 'none';
			}else if(itemType == '4'){
				//picTR.style.display = '';
				
				textTH.style.display = 'none';
				textTD.style.display = 'none';
				
				optionValueTH.style.display = '';
				optionValueTD.style.display = '';
				
				rationTH.style.display = '';
				rationTD.style.display = '';
				
				picPathTH.style.display = '';
				picPathTD.style.display = '';
			}
			
		}else if (countType == '2'){//计次数时
			scoreTR.style.display = 'none';
			scoreTR.value = '0';
			
			if(itemType == '0' || itemType == '1'){
				//picTR.style.display = 'none';
				//picTR.value = '';
				
				textTH.style.display = '';
				textTD.style.display = '';
				
				optionValueTH.style.display = '';
				optionValueTD.style.display = '';
				
				rationTH.style.display = 'none';
				rationTD.style.display = 'none';
				
				picPathTH.style.display = '';
				picPathTD.style.display = '';
			}else if(itemType == '2' || itemType == '3'){
				//picTR.style.display = 'none';
				//picTR.value = '';
				
				textTH.style.display = 'none';
				textTD.style.display = 'none';
				
				optionValueTH.style.display = 'none';
				optionValueTD.style.display = 'none';
				
				rationTH.style.display = 'none';
				rationTD.style.display = 'none';
				
				picPathTH.style.display = 'none';
				picPathTD.style.display = 'none';
			}else if(itemType == '4'){
				//picTR.style.display = '';
				
				textTH.style.display = 'none';
				textTD.style.display = 'none';
				
				optionValueTH.style.display = '';
				optionValueTD.style.display = '';
				
				rationTH.style.display = 'none';
				rationTD.style.display = 'none';
				
				picPathTH.style.display = '';
				picPathTD.style.display = '';
			}
			
		}
		
		//将增加的条目去除
		$(".appendOptionTAB").remove();
	}
	
</script>
<base target="_self"/>
<body onload="hdDs();">
<br/>
<form:form commandName="itemInforVo" id="itemInforForm" name="itemInforForm" action="/tpwj/itemInfor.do?method=save" method="post" enctype="multipart/form-data">
<form:hidden path="itemId"/>
<table width="98%" cellpadding="6" cellspacing="1" align="center" border="1" bordercolor="#c5dbec">
	<tr>
		<td colspan="2" bgcolor="#dfeffc" align="center"><form:hidden path="topicId"/><h3><font color="#15A7BC">${_TopicInfor.topicName}</font></h3></td>
	</tr>
	
	<tr>
		<td colspan=2>
			<table border="0" style="border-collapse:collapse ">   
				<tr>  
			    	<td>  
			        	<!--<div style="position:relative;">  
			            	<span id="spanfindvalue1" style="margin-left:200px;width:18px;overflow:hidden;">  this.parentNode.nextSibling.value=this.value  
			                	<select id="selectfindvalue1" name="selectfind" class="text" style="height:26px;width:222px;margin-left:-200px" onchange="javascript:itemInforForm.findValue.value=itemInforForm.selectfind.value;">  
			                    	<option value="可编辑下拉框" selected>可编辑下拉框</option>  
			                    	<option value="456">456</option>  
			                    	<option value="123">123</option>  
			                    	<option value="456">456</option>  
			                	</select>  
			            	</span><input name="findValue" id="findValue1"  class="text" style="width:200px;position:absolute;left:0px;" onclick="">  
			       		</div>  -->
			    	</td>  
				</tr>                                           
			</table>  
		</td>
	</tr>
	
	<tr>
		<td width="40%" bgcolor="#dfeffc" align="left">&nbsp;&nbsp;&nbsp;&nbsp;<font color="#15A7BC"><b>编辑条目信息</b></font></td>
		<td bgcolor="#dfeffc" align="left">
			&nbsp;&nbsp;&nbsp;&nbsp;<font color="#15A7BC"><b>条目下的选项信息</b></font>
			&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="#" style="color:red" onclick="addOneOption()">增加一条</a>	
		</td>
	</tr>
	
	<tr>
		<td style="padding-left:20px;padding-top:10px;" valign=top>
			<!--<fieldset style="padding-left:20px;border:2px solid #15A7BC">
				<legend><font color=red><b>编辑单个条目信息</b></font></legend>-->
				<table>
					<tr> 
				       <td nowrap align=right>条目标题：</td>
				       <td><form:textarea rows="2" cols="40" path="itemName"></form:textarea></td>
				       <td><div id="itemNameTip" style="width:0px"></div></td>
				    </tr>
				    
				    <tr> 
				       <td nowrap align=right>统计类型：</td>
				       <td colspan="2">
				           <select id="countType" name="countType" onchange="hdDs();">
							  <option value="1">计分</option>
							  <option value="2">计次</option>
						   </select>
				       </td>
				    </tr>
					
					<tr id="scoreTR"> 
				       <td nowrap align=right>分值：</td>
				       <td colspan="2"><form:input path="score" size="20"/></td>
				    </tr>
					
					<tr> 
				       <td nowrap align=right>提示文本：</td>
				       <td colspan="2"><form:textarea rows="2" cols="40" path="tipText"></form:textarea></td>
				    </tr>
					
					<tr> 
				       <td nowrap align=right>分类名称：</td>
				       <td colspan="2">
				       	<div style="position:relative;">  
			            	<span id="spanfindvalue1" style="margin-left:200px;width:18px;overflow:hidden;"> <!-- this.parentNode.nextSibling.value=this.value --> 
			                	<select id="selectfindvalue1" name="selectfind" style="height:26px;width:222px;margin-left:-200px" onchange="javascript:itemInforForm.categoryName.value=itemInforForm.selectfind.value;">  
			                    	<c:forEach items="${_CategoryList}" var="category">
			                    		<option value="${category}">${category}</option>
			                    	</c:forEach>
			                	</select>  
			            	</span>
			            	<form:input path="categoryName" style="width:200px;position:absolute;left:0px;" size="20"/>
			            	<font color=red>可填写可选择</font>
			            	<!--<input name="findValue" id="findValue1" style="width:200px;position:absolute;left:0px;" onclick=""> --> 
			       		</div>
				       
				      </td>
				    </tr>
				                    
				    <tr> 
				       <td nowrap align=right>是否必须：</td>
				       <td colspan="2">
				           <form:select path="need">
							  <form:option value="true">是</form:option>
							  <form:option value="false">否</form:option>
						   </form:select>
				       </td>
				    </tr>
				                    
				    <tr> 
				       <td nowrap align=right>条目类型：</td>
				       <td colspan="2">
				           <form:select path="itemType" onchange="hdDs();">
							  <form:option value="0">单选型</form:option>
							  <form:option value="1">多选型</form:option>
							  <form:option value="2">文本型</form:option>
							  <form:option value="3">段落型</form:option>
							  <form:option value="4">图片型</form:option>
							  <!--<form:option value="5">列表型</form:option>
						   --></form:select>
				       </td>
				    </tr>
				    
				    <tr id="picTR"> 
				       <td nowrap align=right>图片：</td>
				       <td colspan="2"><input type="file" name="itemPicPath" size="50" /></td>
				    </tr>
				    
					
					<tr> 
				       <td nowrap align=right>排序号：</td>
				       <td colspan="2"><form:input path="itemOrder" size="20" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/></td>
				    </tr>
				</table>
			<!--</fieldset>-->
		</td>
		<td valign=top style="padding-top:5px;">
			<table id="optionTab" class="optionTab">
				<tr>
					<th>选项序号</th>
					<th id="textTH">选项文本</th>
					<th id="optionValueTH">选项值</th>
					<th id="rationTH">权重</th>
					<th id="picPathTH" style="display:none;">图片</th>
				</tr>
				<tr id="optionModelTR">
					<td><form:input path="optionOrder" size="3" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/></td>
					<td id="textTD"><form:input path="optionName" size="40"/></td>
					<td id="optionValueTD"><form:input path="optionValue" size="3"/></td>
					<td id="rationTD"><form:input path="ration" size="3"/></td>
					<td id="picPathTD" style="display:none;"><input type="file" name="optionPicPath" size="20"/></td>
				</tr>
			</table>
		</td>
	</tr>
	
	<script>
		function addOneOption(){
			var modelOptions = $("#optionModelTR").html();
			
			var appendHtml = "<table class='appendOptionTAB optionTab'><tr class='appendOptionTR'>"+modelOptions;
			appendHtml += "<td><a href=# onclick='delAppendTab(this);'>删除</a></td>";
			appendHtml += "</tr></table>";
			
			$("#optionTab").after(appendHtml);
			
			isArray = true;
		}
		
		function delAppendTab(obj){
			//alert($(obj).parent().parent().parent().attr("class"));
			$(obj).parent().parent().parent().remove();
		}
		
	</script>
                    
                    
     <tr> 
        <td colspan="2" bgcolor="#dfeffc" align=center>
          <input type="button" id="button" style="cursor: pointer;" onclick="submitData();" value="提交"/>
             &nbsp;
          <input type="button" value="关闭" style="cursor: pointer;" onclick="window.close();"/>
        </td>
     </tr>
</table>
</form:form>
</body>

