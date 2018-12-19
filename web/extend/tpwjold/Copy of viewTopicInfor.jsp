<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<title>操作信息</title>
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
	});
	//提交数据
	function submitData() {
		
		if(check()){
			var form = document.voteInforForm;
			form.submit();
			alert('信息编辑成功！');
			window.returnValue = "refresh";
			window.close();
		}
	}
	
	//验证必填题目是否填写
	function check(){
		//var flag = false;
		<c:forEach items="${_TopicInfor.items}" var="item" varStatus="index">
			//是否必填
			var need = "${item.need}";
			//alert(need+"--"+"${item.itemName}");
			if(need == 'true'){
				//判断题型
				var itemType = "${item.itemType}";
				var itemId = "${item.itemId}";
				
				//题目标题
				var itemName = "${item.itemName}";
				
				//多选时的限制数量
				var checkCount = "${item.checkCount}";
				
				if(itemType == "0"){//单选
					var hasSelected = false;
					$(":radio[name='item_"+itemId+"']").each(function(i){
						if($(this).attr("checked")){
							hasSelected = true;
							//break;
						}
					});
					if(!hasSelected){
						alert("题目："+itemName+"，请选择一个选项！");
						document.getElementById("viewItem"+itemId).scrollIntoView();
						return false;
					}
				}else if(itemType == "1" || itemType == "4"){//多选或者图片
					var hasSelected = false;
					var thisCount = 0;
					$(":checkbox[name='item_"+itemId+"']").each(function(i){
						if($(this).attr("checked")){
							hasSelected = true;
							//break;
							thisCount += 1;
						}
					});
					if(!hasSelected){
						alert("题目："+itemName+"，请至少选择一个选项！");
						document.getElementById("viewItem"+itemId).scrollIntoView();
						return false;
					}
					if(hasSelected && checkCount > 0 && thisCount > checkCount){
						alert("题目："+itemName+"，最多只能选"+checkCount+"项！");
						document.getElementById("viewItem"+itemId).scrollIntoView();
						return false;
					}
				}else if(itemType == "2" || itemType == "3"){  //文本或者段落
				
					var hasInputed = false;
					$(".text[name='item_"+itemId+"']").each(function(i){
						if($(this).val() != null && $(this).val() != ""){
							hasInputed = true;
							//break;
						}
					});
					if(!hasInputed){
						alert("请填写完带*号的题目！");
						document.getElementById("viewItem"+itemId).scrollIntoView();
						return false;
					}
				}else if(itemType == "5"){  //列表
					
				}
			}
		</c:forEach>
		
		return true;
	}	
	
	//查看某个人的投票信息
	function fillData(){
		if("${!empty _VoteInfor}" == 'true'){
			<c:forEach items="${_TopicInfor.items}" var="item" varStatus="index">
				//判断题型
				var itemType = "${item.itemType}";
				var itemId = "${item.itemId}";
				
				if(itemType == "0" || itemType == "1" || itemType == "4"){//单选、多选、文本
					<c:if test="${!empty _VoteInfor.voteItems}">
						<c:forEach items="${_VoteInfor.voteItems}" var="voteItem">
							<c:if test="${voteItem.item.itemId == item.itemId}">
								<c:forEach items="${fn:split(voteItem.voteValue,',')}" var="voteValue">
    								var voteValue = "${voteValue}";
    								
    								if(itemType == "0"){
    									$(":radio[name='item_"+itemId+"'][value='"+voteValue+"']").attr("checked","true");
    								}
    								if(itemType == "1" || itemType == "4"){
    									$(":checkbox[name='item_"+itemId+"'][value='"+voteValue+"']").attr("checked","true");
    								}
	    						</c:forEach>
							</c:if>
						</c:forEach>
					</c:if>
				}else if(itemType == "2" || itemType == "3"){  //文本或者段落
					<c:if test="${!empty _VoteInfor.voteItems}">
						<c:forEach items="${_VoteInfor.voteItems}" var="voteItem">
							<c:if test="${voteItem.item.itemId == item.itemId}">
   								var voteText = "${voteItem.voteText}";
   								$(".text[name='item_"+itemId+"']").val(voteText);
							</c:if>
						</c:forEach>
					</c:if>
				}else if(itemType == "5"){  //列表
					
				}
			</c:forEach>
		}
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
<body onload="fillData();">
<br/>
<form id="voteInforForm" name="voteInforForm" action="/tpwj/voteInfor.do?method=save" method="post">
<input type="hidden" name="topicId" id="topicId" value="${_TopicInfor.topicId}"/>
<table width="80%" cellpadding="6" cellspacing="1" align="center" border="1" bordercolor="#c5dbec">
	<tr>
		<td bgcolor="#dfeffc" align="center">
			<h2><font color="#15A7BC">${_TopicInfor.topicName}</font></h2>
			<div style="text-align:left;padding-left:10px;"><font color=gray>${_TopicInfor.descrip}</font></div>
			<c:if test="${!empty _TopicInfor.rules}">
				<br/>
				<div style="text-align:left;padding-left:10px;"><font color=orange><b>投票规则：</b></font><font color=gray>${_TopicInfor.rules}</font></div>
			</c:if>
			<br/>
			<div style="text-align:left;padding-left:10px;"><font color=red>带 * 号的题目，请您务必填写！</font></div>
		</td>
	</tr>
	
	<!-- 过期或者无效 -->
	<c:if test="${_IsOut || !_TopicInfor.valid}">
		<tr>
			<td align="center">
				<font color=red><h2>当前投票已过期或者无效，您不能进行操作！</h2></font>
			</td>
		</tr>
	</c:if>
	<!-- 已投过 -->
	<c:if test="${!_IsOut && _TopicInfor.valid && _HasDone}">
		<tr>
			<td align="center">
				<font color=red><h2>您已投过票，将不能再进行操作！</h2></font>
			</td>
		</tr>
	</c:if>
	
	<tr>
		<td align=center>
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
											<table width="100%" id="viewItem${item.itemId}">   
	          									<tr> 
											    	<td bgcolor="#dfeffc" align="left" colspan=2>
											    		&nbsp;&nbsp;&nbsp;&nbsp;
											    		<span style="color:#15A7BC;font-size:15px;">
											    			<b>${item.itemName}</b>
											    		</span>
											    		<!--<c:if test="${!empty item.picPath}">
										    				<a title="点击查看原图" href="<c:url value="/"/>${item.picPath}" target="_blank">
										    					<img src="<c:url value="/"/>${item.picPath}"  onload="setImgSize(this,100,100);" />
										    				</a>
										    			</c:if>
										    			--><c:choose>
												       		<c:when test="${item.itemType == 0}">【单选】</c:when>
												       		<c:when test="${item.itemType == 1}">【多选】</c:when>
												       	</c:choose>
										    			<c:if test="${!empty item.tipText}">
										    				<font color=gray>（注：${item.tipText}）</font>
										    			</c:if>
										    			<!--<c:if test="${item.score > 0}">
										    				<font color=green>${item.score}分</font>
										    			</c:if>
										    			--><c:if test="${item.need}">
										    				&nbsp;
										    				<font color=red>*</font>
										    			</c:if>
											    	</td>
												</tr>   
												<tr>
													<td align=left>
														<c:choose>
															<c:when test="${item.itemType == 4}">
																<table border=0 width="98%">
																	<c:set var="_ItemNum" value="0"/>
																	<tr>
																	<c:forEach items="${item.options}" var="option" varStatus="optionIndex">
																		<td style="padding-left:15px;">
																			<input type="checkbox" value="${option.optionId}" id="item_${item.itemId}_${optionIndex.index+1}" name="item_${item.itemId}" />
											    							<a title="点击查看原图" href="<c:url value="/"/>${option.picPath}" target="_blank">
														    					<img src="<c:url value="/"/>${option.picPath}" onload="setImgSize(this,100,100);" />
														    				</a>
													    					<!--<c:if test="${item.score > 0}">
													    						&nbsp;
													    						<font color=green>
													    							${option.score}分
													    						</font>
													    					</c:if>
												    					--></td>
												    					<c:set var="_ItemNum" value="${_ItemNum+1}"/>
												    					<c:if test="${_ItemNum % 4 == 0}">
												    						</tr><tr>
												    					</c:if>
											    					</c:forEach>
											    					</tr>
										    					</table>
															</c:when>
															<c:otherwise>
																<ul>
													    			<c:forEach items="${item.options}" var="option" varStatus="optionIndex">
													    				<li>
													    					<c:choose>
																	       		<c:when test="${item.itemType == 0}"><input type="radio" value="${option.optionId}" id="item_${item.itemId}_${optionIndex.index+1}" name="item_${item.itemId}" /></c:when>
																	       		<c:when test="${item.itemType == 1}"><input type="checkbox" value="${option.optionId}" id="item_${item.itemId}_${optionIndex.index+1}" name="item_${item.itemId}" /></c:when>
																	       		<c:when test="${item.itemType == 2}"><input class="text" name="item_${item.itemId}" type="text" size="50" /></c:when>
																	       		<c:when test="${item.itemType == 3}"><textarea class="text" name="item_${item.itemId}" rows="5" cols="50"></textarea></c:when>
																	       		<c:when test="${item.itemType == 5}">列表型</c:when>
																	       	</c:choose>
													    					<label for="item_${item.itemId}_${optionIndex.index+1}">${option.optionName}</label>
													    					<c:if test="${!empty option.picPath}">
												    							<a title="点击查看原图" href="<c:url value="/"/>${option.picPath}" target="_blank">
															    					<img src="<c:url value="/"/>${option.picPath}" onload="setImgSize(this,100,100);" />
															    				</a>
															    			</c:if>
													    					<!--<c:if test="${item.score > 0}">
													    						&nbsp;
													    						<font color=green>
													    							${option.score}分
													    						</font>
													    					</c:if>
													    				--></li>
													    			</c:forEach>
													    		</ul>
															</c:otherwise>
														</c:choose>
														
													</td>
													<td align="right">
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
								<table style="border:2px dotted #0DE8F5;" width="99%" id="viewItem${item.itemId}">
									<tr>  
								    	<td bgcolor="#dfeffc" align="left" colspan=2>
								    		&nbsp;&nbsp;&nbsp;&nbsp;
								    		<span style="color:#15A7BC;font-size:15px;">
								    			<b>${item.itemName}</b>
								    		</span>
								    		<!--<c:if test="${!empty item.picPath}">
							    				<a title="点击查看原图" href="<c:url value="/"/>${item.picPath}" target="_blank">
							    					<img src="<c:url value="/"/>${item.picPath}"  onload="setImgSize(this,100,100);"" />
							    				</a>
							    			</c:if>
							    			
							    			--><c:choose>
									       		<c:when test="${item.itemType == 0}">【单选】</c:when>
									       		<c:when test="${item.itemType == 1}">【多选】</c:when>
									       	</c:choose>
									       
							    			<c:if test="${!empty item.tipText}">
							    				<font color=gray>（注：${item.tipText}）</font>
							    			</c:if>
							    			<!--<c:if test="${item.score > 0}">
							    				<font color=oragne>${item.score}分</font>
							    			</c:if>
							    			--><c:if test="${item.need}">
							    				&nbsp;
							    				<font color=red>*</font>
							    			</c:if>
								    	</td>
									</tr>   
									<tr>  
								    	<td align=left>
											<c:choose>
												<c:when test="${item.itemType == 4}">
													<table border=0 width="98%">
														<c:set var="_ItemNum" value="0"/>
														<tr>
														<c:forEach items="${item.options}" var="option" varStatus="optionIndex">
															<td style="padding-left:15px;">
																<input type="checkbox" value="${option.optionId}" id="item_${item.itemId}_${optionIndex.index+1}" name="item_${item.itemId}" />
								    							<a title="点击查看原图" href="<c:url value="/"/>${option.picPath}" target="_blank">
											    					<img src="<c:url value="/"/>${option.picPath}" onload="setImgSize(this,100,100);" />
											    				</a>
										    					<!--<c:if test="${item.score > 0}">
										    						&nbsp;
										    						<font color=green>
										    							${option.score}分
										    						</font>
										    					</c:if>
									    					--></td>
									    					<c:set var="_ItemNum" value="${_ItemNum+1}"/>
									    					<c:if test="${_ItemNum % 4 == 0}">
									    						</tr><tr>
									    					</c:if>
								    					</c:forEach>
								    					</tr>
							    					</table>
												</c:when>
												<c:otherwise>
													<ul>
										    			<c:forEach items="${item.options}" var="option" varStatus="optionIndex">
										    				<li>
										    					<c:choose>
														       		<c:when test="${item.itemType == 0}"><input type="radio" value="${option.optionId}" id="item_${item.itemId}_${optionIndex.index+1}" name="item_${item.itemId}" /></c:when>
														       		<c:when test="${item.itemType == 1}"><input type="checkbox" value="${option.optionId}" id="item_${item.itemId}_${optionIndex.index+1}" name="item_${item.itemId}" /></c:when>
														       		<c:when test="${item.itemType == 2}"><input class="text" name="item_${item.itemId}" type="text" size="50" /></c:when>
														       		<c:when test="${item.itemType == 3}"><textarea class="text" name="item_${item.itemId}" rows="5" cols="50"></textarea></c:when>
														       		<c:when test="${item.itemType == 5}">列表型</c:when>
														       	</c:choose>
										    					<label for="item_${item.itemId}_${optionIndex.index+1}">${option.optionName}</label>
										    					<c:if test="${!empty option.picPath}">
									    							<a title="点击查看原图" href="<c:url value="/"/>${option.picPath}" target="_blank">
												    					<img src="<c:url value="/"/>${option.picPath}" onload="setImgSize(this,100,100);" />
												    				</a>
												    			</c:if>
										    					<!--<c:if test="${item.score > 0}">
										    						&nbsp;
										    						<font color=green>
										    							${option.score}分
										    						</font>
										    					</c:if>
										    				--></li>
										    			</c:forEach>
										    		</ul>
												</c:otherwise>
											</c:choose>
										</td>
										<td align=right>
											<c:if test="${!empty item.picPath}">
							    				<a title="点击查看原图" href="<c:url value="/"/>${item.picPath}" target="_blank">
							    					<img src="<c:url value="/"/>${item.picPath}"  onload="setImgSize(this,200,200);"" />
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
	
                    
     <tr> 
        <td bgcolor="#dfeffc" align=left>
        	<c:if test="${!_IsOut && _TopicInfor.valid && !_HasDone}">
          		<input type="button" id="button" style="cursor: pointer;" onclick="submitData();" value="提交"/>
             	&nbsp;
             </c:if>
          <input type="button" value="关闭" style="cursor: pointer;" onclick="window.close();"/>
        </td>
     </tr>
</table>
</form>
</body>
