<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<title>设计条目信息</title>
<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
<script src="<c:url value="/"/>datePicker/WdatePicker.js" type="text/javascript"></script>
<script src="<c:url value="/"/>js/inc_javascript.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/css/form.css"/>"/>

<script type="text/javascript" src="<c:url value="/"/>js/setimgsize.js"></script>

<!-- formValidator -->
<link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css"></link>
<script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript" charset="UTF-8"></script>
<!--<script src="<c:url value="/"/>components/formvalidator/js/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>

--><script>
	
	$(document).ready(function(){
		//验证
		//$.formValidator.initConfig({formid:"itemInforForm",onerror:function(msg){alert(msg)},onsuccess:function(){submitData();}});
		//$("#itemName").formValidator({onshow:"请输入投票标题",onfocus:"投票标题不能为空",oncorrect:"输入正确"}).inputValidator({min:1,onerror:"投票标题不能为空,请确认"});
		
		window.scrollTo(0,99999);
	});
	var isArray = false;
	//提交数据
	function submitData() {
		
		if(check()){
			var form = document.itemInforForm;
			form.submit();
			alert('信息编辑成功！');
			//window.returnValue = "refresh";
			//window.close();
			//window.location.reload();
		}
	}
	
	
	function check(){
		var flag = true;
		var form = document.itemInforForm;
		//标题
		var itemName = form.itemName;
		//图片
		var itemPicPath = form.itemPicPath;
		//排序号
		var displayOrder = form.displayOrder;
		
		if(itemName.value == ''){
			flag = false;
			alert("条目标题不能为空！");
			itemName.focus();
		}
		
		if(itemPicPath.value != ''){
			var point = itemPicPath.value.lastIndexOf(".");
			var filetype = itemPicPath.value.substr(point).toLowerCase();
			
			if(filetype!=".jpg" && filetype!=".jpeg" && filetype!=".bmp" && filetype!=".png" && filetype!=".gif" && filetype!=".ico"){ 
				itemPicPath.focus(); 
				alert("上传的附件必须为图片文件！"); 
				flag = false; 
			}
		}
		
		if(displayOrder.value == '' || displayOrder.value < 1){
			flag = false;
			alert("请正确填写排序号！");
			displayOrder.focus();
		}
		
		return flag;
	}
	
	
	function hdScore(){
		var form = document.itemInforForm;
		
		//统计方式
		var countType = form.countType.value;
		var scoreTR = document.getElementById('scoreTR');
		
		var scoreInput = form.score;
		
		//计分时
		if (countType == '1') {
			scoreTR.style.display = '';
			
		}else if (countType == '2'){//计次数时
			scoreTR.style.display = 'none';
			scoreInput.value = '0';
		}
	}
	
	//新增选项信息
	function addOptionInfor(itemId){
		var refresh = window.showModalDialog("/tpwj/itemInfor.do?method=editOption&itemId="+itemId,null,"dialogWidth:1000px;dialogHeight:600px;center:Yes;dialogTop: 100px; dialogLeft: 200px;");
		if(refresh == "refresh") {
			self.location.reload();
		}
	}
	//修改选项信息
	function editOptionInfor(itemId,optionId){
		var refresh = window.showModalDialog("/tpwj/itemInfor.do?method=editOption&itemId="+itemId+"&optionId="+optionId,null,"dialogWidth:1000px;dialogHeight:600px;center:Yes;dialogTop: 100px; dialogLeft: 200px;");
		if(refresh == "refresh") {
			self.location.reload();
		}
	}
	
	//删除条目信息
	function deleteInfor(url){
		var yes = window.confirm("确定要删除吗？");
		if (yes) {
			/*$.ajax({
				url: url,	
				cache: false,
				type: "POST",
				dataType: "html",
				beforeSend: function (xhr) {						
				},
					
				complete : function (req, err) {
					alert("数据已经删除！");
					//self.location.reload();
				}
			});	*/
			window.location.href = url;
		}
	}
	
	//选择选项源
	function selOptionSource(targetId,itemType){
		$(".radio"+itemType+"[value!='"+targetId+"']").css("display","");
		$(":radio[name='optionSource'][class!='radio"+itemType+"']").css("display","none");
		$(":radio[name='optionSource'][class='radio"+itemType+"'][value='"+targetId+"']").css("display","none");
		
		$("#copyOpTr").css("display","");
		
		$("#targetId").val(targetId);
		
		/*.each(function(i,n){
		alert($(this).css("display"));
			$(this).css("display","none");
		});*/
	}
	
	//提交选择的选项源
	function submitOptionSource(){
		var sourceId=$(":radio[name='optionSource']:checked").val();
		var targetId=$("#targetId").val();
		//alert(sourceId+"--"+targetId);
		$.ajax({
			url: '/tpwj/itemInfor.do?method=saveCopy&sourceId='+sourceId+"&targetId="+targetId,
			cache: false,
			type: "GET",
			//dataType : "json",
			async: true,
            cache: false,
			beforeSend: function (xhr) {
				
			},
			complete : function(req, msg) {
				//var msg = eval("(" + req.responseText + ")");
			},
			success : function (msg) {
				//alert(msg);
				if(msg == 'success'){
					alert("复制成功！");
					//window.location.reload();
				}
				if(msg == 'fail'){
					alert("复制失败，请重试！");
				}
				cancleAllRadio();
				window.location.reload();
			}
		});
	}
	
	//取消所有可选复制源
	function cancleAllRadio(){
		$(":radio[name='optionSource']").css("display","none");
		$("#copyOpTr").css("display","none");
		$("#targetId").val("");
	}
</script>
<style>
	img{
		border:0;
	}
	li{
		list-style:none;
	}
	input,label { vertical-align:middle;} 
</style>
<base target="_self"/>
<body onload="hdScore();">
<br/>
<form:form commandName="itemInforVo" id="itemInforForm" name="itemInforForm" action="/tpwj/itemInfor.do?method=save" method="post" enctype="multipart/form-data">
<form:hidden path="itemId"/>
<table width="80%" cellpadding="6" cellspacing="1" align="center" border="1" bordercolor="#c5dbec">
	<tr>
		<td colspan="2" bgcolor="#dfeffc" align="center"><form:hidden path="topicId"/><h2><font color="#15A7BC">${_TopicInfor.topicName}</font></h2></td>
	</tr>
	
	<tr>
		<td colspan=2 align=center>
			<c:forEach items="${_CategoryList}" var="category">
          		<c:choose>
          			<c:when test="${!empty category}">
	          			<table style="border:2px dotted #0DE8F5;" width="99%">
	          				<tr>  
								<td width="20%" bgcolor="#dfeffc" align="center" style="border-right:1px solid black;">
									<b>${category}</b>
								</td>
								<td>
									<c:forEach items="${_TopicInfor.items}" var="item" varStatus="index">
				          				<c:if test="${category == item.categoryName}">
											<table width="100%">   
	          									<tr> 
											    	<td bgcolor="#dfeffc" align="left" colspan=2>
											    		&nbsp;&nbsp;
											    		<input type="radio" name="optionSource" class="radio${item.itemType}" value="${item.itemId}" style="display:none;border:4px solid red;"/>
											    		&nbsp;&nbsp;
											    		<span style="color:#15A7BC;font-size:15px;">
											    			<b>${item.itemName}</b>
											    		</span>
											    		
										    			【
										    			<c:choose>
												       		<c:when test="${item.itemType == 0}">单选型</c:when>
												       		<c:when test="${item.itemType == 1}">多选型</c:when>
												       		<c:when test="${item.itemType == 2}">文本型</c:when>
												       		<c:when test="${item.itemType == 3}">段落型</c:when>
												       		<c:when test="${item.itemType == 4}">图片型</c:when>
												       		<c:when test="${item.itemType == 5}">列表型</c:when>
												       	</c:choose>
												       	】
												       	【权重：<font color=green>${item.ration}</font>】
										    			<c:if test="${!empty item.tipText}">
										    				<font color=gray>（注：${item.tipText}）</font>
										    			</c:if>
										    			<c:if test="${item.score > 0}">
										    				<font color=green>${item.score}分</font>
										    			</c:if>
										    			<c:if test="${item.need}">
										    				&nbsp;
										    				<font color=red>*</font>
										    			</c:if>
										    			&nbsp;&nbsp;
										    			<a href="/tpwj/itemInfor.do?method=edit&topicId=${_TopicInfor.topicId}&rowId=${item.itemId}"><font color=orange>【编辑题目】</font></a>
										    			<a href="javascript:deleteInfor('/tpwj/itemInfor.do?method=delete&topicId=${_TopicInfor.topicId}&itemId=${item.itemId}');"><font color=red>【删除题目】</font></a>
										    			<!-- 文本或者段落型时，只能添加一个选项 -->
										    			<c:if test="${item.itemType != 2 && item.itemType != 3}">
										    				<a href="javascript:addOptionInfor(${item.itemId});"><font color=orange>【增加选项】</font></a>
										    				<a href="javascript:selOptionSource(${item.itemId},${item.itemType});"><font color=blue>【选择选项源】</font></a>
										    			</c:if>
										    			
										    			<a href="/tpwj/itemInfor.do?method=edit&topicId=${_TopicInfor.topicId}&orderNo=${item.displayOrder}&category=${item.categoryName}"><font color=green>【在此题前插入】</font></a>
											    	</td>
												</tr>   
												<tr>
													<td align=left>
														<ul>
											    			<c:forEach items="${item.options}" var="option" varStatus="optionIndex">
											    				<li>
											    					<c:choose>
															       		<c:when test="${item.itemType == 0}"><input type="radio" id="item_${item.itemId}_${optionIndex.index+1}" name="item_${item.itemId}" /></c:when>
															       		<c:when test="${item.itemType == 1}"><input type="checkbox" id="item_${item.itemId}_${optionIndex.index+1}" name="item_${item.itemId}" /></c:when>
															       		<c:when test="${item.itemType == 2}"><input type="text" size="50" /></c:when>
															       		<c:when test="${item.itemType == 3}"><textarea rows="3" cols="50"></textarea></c:when>
															       		<c:when test="${item.itemType == 4}"><input type="checkbox" id="item_${item.itemId}_${optionIndex.index+1}" name="item_${item.itemId}" /></c:when>
															       		<c:when test="${item.itemType == 5}">列表型</c:when>
															       	</c:choose>
											    					<label for="item_${item.itemId}_${optionIndex.index+1}">${option.optionName}</label>
											    					<c:if test="${!empty option.picPath}">
													    				<a title="点击查看原图" href="<c:url value="/"/>${option.picPath}" target="_blank">
													    					<img src="<c:url value="/"/>${option.picPath}" onload="setImgSize(this,100,100);" />
													    				</a>
													    			</c:if>
											    					<c:if test="${item.score > 0}">
											    						&nbsp;
											    						<font color=green>
											    							<!--fmt:formatNumber value="(item.score) * (option.ration)" pattern="0.0"/-->
											    							${option.score}分
											    						</font>
											    					</c:if>
											    					<c:if test="${item.itemType != 2 && item.itemType != 3}">
												    					&nbsp;
												    					<a href="javascript:editOptionInfor(${item.itemId},${option.optionId});">【修改】</a>
												    					&nbsp;
												    					<a href="javascript:deleteInfor('/tpwj/itemInfor.do?method=deleteOption&topicId=${_TopicInfor.topicId}&optionId=${option.optionId}');"><font color=red>【删除】</font></a>
											    					</c:if>
											    				</li>
											    			</c:forEach>
											    		</ul>
													</td>
													<td align=right>
														<c:if test="${!empty item.picPath}">
										    				<a title="点击查看原图" href="<c:url value="/"/>${item.picPath}" target="_blank">
										    					<img src="<c:url value="/"/>${item.picPath}"  onload="setImgSize(this,200,200);" />
										    				</a>
										    			</c:if>
													</td>
												</tr>
											</table> 
											<br/>
				          				</c:if>
									</c:forEach>
								</td>
		          			
						</table> 
	          		</c:when>
	          		<c:when test="${empty category}">
	          			<c:forEach items="${_TopicInfor.items}" var="item" varStatus="index">
	          				<c:if test="${empty item.categoryName}">
								<table style="border:2px dotted #0DE8F5;" width="99%">
									<tr>  
								    	<td bgcolor="#dfeffc" align="left" colspan=2>
								    		&nbsp;&nbsp;
								    		<input type="radio" name="optionSource" class="radio${item.itemType}" value="${item.itemId}" style="display:none;border:4px solid red;"/>
								    		&nbsp;&nbsp;
								    		<span style="color:#15A7BC;font-size:15px;">
								    			<b>${item.itemName}</b>
								    		</span>
							    			【
							    			<c:choose>
									       		<c:when test="${item.itemType == 0}">单选型</c:when>
									       		<c:when test="${item.itemType == 1}">多选型</c:when>
									       		<c:when test="${item.itemType == 2}">文本型</c:when>
									       		<c:when test="${item.itemType == 3}">段落型</c:when>
									       		<c:when test="${item.itemType == 4}">图片型</c:when>
									       		<c:when test="${item.itemType == 5}">列表型</c:when>
									       	</c:choose>
									       	】
									       	【权重：<font color=green>${item.ration}</font>】
							    			<c:if test="${!empty item.tipText}">
							    				<font color=gray>（注：${item.tipText}）</font>
							    			</c:if>
							    			<c:if test="${item.score > 0}">
							    				<font color=oragne>${item.score}分</font>
							    			</c:if>
							    			<c:if test="${item.need}">
							    				&nbsp;
							    				<font color=red>*</font>
							    			</c:if>
							    			&nbsp;&nbsp;
							    			<a href="/tpwj/itemInfor.do?method=edit&topicId=${_TopicInfor.topicId}&rowId=${item.itemId}"><font color=orange>【编辑题目】</font></a>
							    			<a href="javascript:deleteInfor('/tpwj/itemInfor.do?method=delete&topicId=${_TopicInfor.topicId}&itemId=${item.itemId}');"><font color=red>【删除题目】</font></a>
							    			<!-- 文本或者段落型时，只能添加一个选项 -->
							    			<c:if test="${item.itemType != 2 && item.itemType != 3}">
							    				<a href="javascript:addOptionInfor(${item.itemId});"><font color=orange>【增加选项】</font></a>
							    				<a href="javascript:selOptionSource(${item.itemId},${item.itemType});"><font color=blue>【选择选项源】</font></a>
							    			</c:if>
							    			
							    			<a href="/tpwj/itemInfor.do?method=edit&topicId=${_TopicInfor.topicId}&orderNo=${item.displayOrder}&category=${item.categoryName}"><font color=green>【在此题前插入】</font></a>
								    	</td>
									</tr>   
									<tr>  
								    	<td align=left>
											<ul>
								    			<c:forEach items="${item.options}" var="option" varStatus="optionIndex">
								    				<li>
								    					<c:choose>
												       		<c:when test="${item.itemType == 0}"><input type="radio" id="item_${item.itemId}_${optionIndex.index+1}" name="item_${item.itemId}" /></c:when>
												       		<c:when test="${item.itemType == 1}"><input type="checkbox" id="item_${item.itemId}_${optionIndex.index+1}" name="item_${item.itemId}" /></c:when>
												       		<c:when test="${item.itemType == 2}"><input type="text" size="50" /></c:when>
												       		<c:when test="${item.itemType == 3}"><textarea rows="3" cols="50"></textarea></c:when>
												       		<c:when test="${item.itemType == 4}"><input type="checkbox" id="item_${item.itemId}_${optionIndex.index+1}" name="item_${item.itemId}" /></c:when>
												       		<c:when test="${item.itemType == 5}">列表型</c:when>
												       	</c:choose>
								    					<label for="item_${item.itemId}_${optionIndex.index+1}">${option.optionName}</label>
								    					<c:if test="${!empty option.picPath}">
										    				<a title="点击查看原图" href="<c:url value="/"/>${option.picPath}" target="_blank">
										    					<img src="<c:url value="/"/>${option.picPath}" onload="setImgSize(this,100,100);" />
										    				</a>
										    			</c:if>
								    					<c:if test="${item.score > 0}">
								    						&nbsp;
								    						<font color=green>
								    							<!--fmt:formatNumber value="(item.score) * (option.ration)" pattern="0.0"-->
								    							${option.score}分
								    						</font>
								    					</c:if>
								    					<c:if test="${item.itemType != 2 && item.itemType != 3}">
									    					&nbsp;
									    					<a href="javascript:editOptionInfor(${item.itemId},${option.optionId});">【修改】</a>
									    					&nbsp;
									    					<a href="javascript:deleteInfor('/tpwj/itemInfor.do?method=deleteOption&topicId=${_TopicInfor.topicId}&optionId=${option.optionId}');"><font color=red>【删除】</font></a>
								    					</c:if>
								    				</li>
								    			</c:forEach>
								    		</ul>
										</td>
										<td align=right>
											<c:if test="${!empty item.picPath}">
							    				<a title="点击查看原图" href="<c:url value="/"/>${item.picPath}" target="_blank">
							    					<img src="<c:url value="/"/>${item.picPath}"  onload="setImgSize(this,200,200);" />
							    				</a>
							    			</c:if>
										</td>
									</tr>  
									                                        
								</table>  
								<br/>
							</c:if>
						</c:forEach>
	          		</c:when>
          		</c:choose>
          		
          	</c:forEach>
			
			
		</td>
	</tr>
	
	<tr id="copyOpTr" style="display:none;">
		<td colspan=2 width="40%" bgcolor="#dfeffc" align="center">
			<input type="hidden" name="targetId" id="targetId" />
			<input type="button" value="确认复制" onclick="submitOptionSource();" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="button" value="取消复制" onclick="cancleAllRadio();" />
		</td>
	</tr>
	
	<tr>
		<td colspan=2 width="40%" bgcolor="#dfeffc" align="left">&nbsp;&nbsp;&nbsp;&nbsp;<font color="#15A7BC"><b>编辑条目信息</b></font></td>
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
				    
				    <!--<tr> 
				       <td nowrap align=right>统计类型：</td>
				       <td colspan="2">
				           <select id="countType" name="countType" onchange="hdScore();">
							  <option value="1">计分</option>
							  <option value="2">计次</option>
						   </select>
				       </td>
				    </tr>-->
				    <tr id="scoreTR"> 
				       <td nowrap align=right>权重：</td>
				       <td colspan="2"><form:input path="ration" size="20"/></td>
				    </tr>
					
					<tr id="scoreTR"> 
				       <td nowrap align=right>分值：</td>
				       <td colspan="2"><form:input path="score" size="20"/><font color=red>分值设为0时，则此题目按计次处理</font></td>
				    </tr>
					
					<tr> 
				       <td nowrap align=right>提示文本：</td>
				       <td colspan="2"><form:textarea rows="2" cols="60" path="tipText"></form:textarea></td>
				    </tr>
					
					<tr> 
				       <td nowrap align=right>分类名称：</td>
				       <td colspan="2">
				       	<div style="position:relative;">  
			            	<span id="spanfindvalue1" style="margin-left:200px;width:18px;overflow:hidden;"> <!-- this.parentNode.nextSibling.value=this.value --> 
			                	<select id="selectfindvalue1" name="selectfind" style="height:26px;width:222px;margin-left:-200px" onchange="javascript:itemInforForm.categoryName.value=itemInforForm.selectfind.value;">  
			                    	<option value="">--选择分类--</option>
			                    	<c:forEach items="${_CategoryList}" var="category">
			                    		<c:if test="${!empty category}">
			                    			<option value="${category}">${category}</option>
			                    		</c:if>
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
				           <form:select path="itemType">
							  <form:option value="0">单选型</form:option>
							  <form:option value="1">多选型</form:option>
							  <form:option value="2">文本型</form:option>
							  <form:option value="3">段落型</form:option>
							  <form:option value="4">图片型</form:option>
							  <!--<form:option value="5">列表型</form:option>
						   --></form:select>
				       </td>
				    </tr>
				    
				    <tr> 
				       <td nowrap align=right>最多可选：</td>
				       <td colspan="2">
				           <form:select path="checkCount">
							  <form:option value="0">不限</form:option>
							  <form:option value="1">1个</form:option>
							  <form:option value="2">2个</form:option>
							  <form:option value="3">3个</form:option>
							  <form:option value="4">4个</form:option>
							  <form:option value="5">5个</form:option>
							  <form:option value="6">6个</form:option>
							  <form:option value="7">7个</form:option>
							  <form:option value="8">8个</form:option>
							  <form:option value="9">9个</form:option>
							  <form:option value="10">10个</form:option>
						   </form:select>
						   <font color=red>多选型时选择</font>
				       </td>
				    </tr>
				    
				    <tr id="picTR"> 
				       <td nowrap align=right>图片：</td>
				       <td colspan="2"><input type="file" name="itemPicPath" size="50" /></td>
				    </tr>
				    
				    <c:if test="${!empty _Attachment_Names}">
						<tr>
							<td colspan="3">原附件信息(<font color=red>如果要删除某个附件，请选择该附件前面的选择框；如果上传了新的文件，原文件将自动被替换！</font>)：</td>
						</tr>
						<tr>
							<td></td>
							<td colspan="2">
								<c:forEach var="file" items="${_Attachment_Names}" varStatus="index">
									<input type="checkbox" name="picAttach" value="${index.index}" />
									<a href="<%=request.getRealPath("/")%>${file}">${_Attachment_Names[index.index]}</a><br/>
								</c:forEach>
							</td>
						</tr>
					</c:if>
					
					<tr> 
				       <td nowrap align=right>排序号：</td>
				       <td colspan="2">
				       	<c:if test="${_Insert}">
				       		<form:input path="displayOrder" size="20" readonly="true" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/>
				       		<font color=red>当前为插入状态，排序号不可更改，否则将插入失败</font>
				       	</c:if>
				       	<c:if test="${!_Insert}">
				       		<form:input path="displayOrder" size="20" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/>
				       	</c:if>
				       	
				       </td>
				    </tr>
				</table>
			<!--</fieldset>-->
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
<!-- <table width="98%">
											<tr> 
										       <td nowrap align=right width="20%">条目标题：</td>
										       <td align=left>${item.itemName}</td>
										    </tr>
										    
										    <tr> 
										       <td nowrap align=right>统计类型：</td>
										       <td align=left>
										       	<c:choose>
										       		<c:when test="${item.score > 0}">计分</c:when>
										       		<c:when test="${item.score == 0}">计次</c:when>
										       	</c:choose>
										       </td>
										    </tr>
											
											<tr id="scoreTR"> 
										       <td nowrap align=right>分值：</td>
										       <td align=left>${item.score}</td>
										    </tr>
											
											<tr> 
										       <td nowrap align=right>提示文本：</td>
										       <td align=left>${item.tipText}</td>
										    </tr>
											
											<tr> 
										       <td nowrap align=right>分类名称：</td>
										       <td align=left>${item.categoryName}</td>
										    </tr>
										                    
										    <tr> 
										       <td nowrap align=right>是否必须：</td>
										       <td align=left>
										           <c:choose>
											       		<c:when test="${item.need}">是</c:when>
											       		<c:when test="${!item.need}">否</c:when>
											       	</c:choose>
										       </td>
										    </tr>
										                    
										    <tr> 
										       <td nowrap align=right>条目类型：</td>
										       <td align=left>
										       	<c:choose>
										       		<c:when test="${item.itemType == 0}">单选型</c:when>
										       		<c:when test="${item.itemType == 1}">多选型</c:when>
										       		<c:when test="${item.itemType == 2}">文本型</c:when>
										       		<c:when test="${item.itemType == 3}">段落型</c:when>
										       		<c:when test="${item.itemType == 4}">图片型</c:when>
										       		<c:when test="${item.itemType == 5}">列表型</c:when>
										       	</c:choose>
										       </td>
										    </tr>
										    
									    	 <tr id="picTR"> 
										       <td nowrap align=right>图片：</td>
										       <td align=left></td>
										    </tr>
											
											<tr> 
										       <td nowrap align=right>排序号：</td>
										       <td align=left>${item.displayOrder}</td>
										    </tr>
										</table> -->
