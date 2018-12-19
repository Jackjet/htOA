<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<title>按人查看信息</title>
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
	
	//查看某个人的投票信息
	function fillData(){
		if("${!empty _VoteInfor}" == 'true'){
			<c:forEach items="${_TopicInfor.items}" var="item" varStatus="index">
				//判断题型
				var itemType = "${item.itemType}";
				var itemId = "${item.itemId}";
				
				if(itemType == "0" || itemType == "1" || itemType == "4"){//单选
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
	input[type="button"],input[type="submit"]{
		padding: 6px 9px 6px 9px;
		border: none;
		border-radius: 5px;
		background-color: #3e4652;
		color: #bcbcbc;
		box-shadow: none;
		/*height: auto;!important;*/
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
<body onload="fillData();">
<br/>
<form id="voteInforForm" name="voteInforForm" action="/tpwj/voteInfor.do?method=save" method="post">
<input type="hidden" name="topicId" id="topicId" value="${_TopicInfor.topicId}"/>

<table style="float:left;" border=1 bordercolor="#0DE8F5" width="15%">
	<tr onclick="disUsers('votedUsers');" style="cursor:pointer;">
		<th <%--bgcolor="#dfeffc"--%>style="background-color: #0e1827" height="40" colspan=3>
			<font color="white" style="font-size:15px;font-family:微软雅黑;"><b>已投票人员</b></font>
			<br/>
			<font color=#f5f5f5 style="font-family:微软雅黑;">（点击显示/隐藏）</font>
		</th>
	</tr>
	
	<tbody id="votedUsers">
	<c:forEach items="${_TopicInfor.voteInfors}" var="voteInfor" varStatus="index">
		<tr>
			<td bgcolor="#0e1827" align="center" colspan=3 style="font-family:微软雅黑;">
				<c:if test="${voteInfor.voter.personId == _VoteInfor.voter.personId}">
					<font color=white style="font-size:14px;"><b>
						${index.index+1}、${voteInfor.voter.person.personName}
						<br/><font color=#f5f5f5>（${fn:substring(voteInfor.voteTime,0,19)}）</font>
					</b></font>
				</c:if>
				<c:if test="${voteInfor.voter.personId != _VoteInfor.voter.personId}">
					<a style="color:#0DE8F5" title="点击查看此人投票信息" href="/tpwj/topicInfor.do?method=viewVoters&topicId=${_TopicInfor.topicId}&voterId=${voteInfor.voter.personId}">${index.index+1}、${voteInfor.voter.person.personName}</a>
					<br/><font color=#f5f5f5>（${fn:substring(voteInfor.voteTime,0,19)}）</font>
				</c:if>
			</td>
		</tr>
	</c:forEach>
	</tbody>
	
	<script>
		function disUsers(id){
			$("#"+id).toggle();
		}
	</script>
	
	<tr onclick="disUsers('notVoteUsers');" style="cursor:pointer;">
		<th style="background-color: #0e1827" height="40" colspan=3><font color="red" style="font-size:15px;font-family:微软雅黑;"><b>未投票人员</b></font>
			<br/>
			<font color=#f5f5f5 style="font-family:微软雅黑;">（点击显示/隐藏）</font>
		</th>
	</tr>
	<tbody id="notVoteUsers" style="display:none;font-family:微软雅黑;">
	<tr>
		<c:set var="notVoteIndex" value="0" />
		<c:forEach items="${_NotVoteUsers}" var="user" varStatus="index">
			<td bgcolor="#0f1829" align="center">
				${user.person.personName}
			</td>
			<c:if test="${(notVoteIndex+1) % 3 == 0}">
				</tr><tr>
			</c:if>
			<c:set var="notVoteIndex" value="${notVoteIndex+1}" />
		</c:forEach>
	</tr>
	</tbody>
</table>
<table style="float:left;" width="80%" cellpadding="6" cellspacing="1" align="center" border="1" bordercolor="#0DE8F5">
	<tr>
		<td style="background-color: #0e1827" align="center">
			<h2><font color="#ffffff">${_TopicInfor.topicName}</font></h2>
			<div style="text-align:left;padding-left:10px;"><font color=#f5f5f5>${_TopicInfor.descrip}</font></div>
			<c:if test="${!empty _TopicInfor.rules}">
				<br/>
				<div style="text-align:left;padding-left:10px;"><font color=orange><b>投票规则：</b></font><font color=#f5f5f5>${_TopicInfor.rules}</font></div>
			</c:if>
			<br/>
			<div style="text-align:left;padding-left:10px;"><font color=orange><b>投票人：</b></font><font><b>${_VoteInfor.voter.person.personName}</b></font></div>
		</td>
	</tr>
	
	<tr>
		<td align=center>
			<c:forEach items="${_CategoryList}" var="category">
          		<c:choose>
          			<c:when test="${!empty category}">
	          			<table style="border:2px dotted #0DE8F5;" width="99%">
	          				<tr>  
								<td width="20%" style="background-color: #0e1827" align="center" style="border-right:1px solid #0e1827;">
									<b>${category}</b>
								</td>
								<td>
									<c:forEach items="${_TopicInfor.items}" var="item" varStatus="index">
				          				<c:if test="${category == item.categoryName}">
											<table width="100%" id="viewItem${item.itemId}">   
	          									<tr> 
											    	<td style="background-color: #0e1827" align="left">
											    		&nbsp;&nbsp;&nbsp;&nbsp;
											    		<span style="color:#ffffff;font-size:15px;">
											    			<b>${item.itemName}</b>
											    		</span>
											    		<c:if test="${!empty item.picPath}">
										    				<a title="点击查看原图" href="<c:url value="/"/>${item.picPath}" target="_blank">
										    					<img src="<c:url value="/"/>${item.picPath}"  onload="setImgSize(this,100,100);" />
										    				</a>
										    			</c:if>
										    			<c:choose>
												       		<c:when test="${item.itemType == 0}">【单选】</c:when>
												       		<c:when test="${item.itemType == 1}">【多选】</c:when>
												       	</c:choose>
												       	【权重：<font color=green>${item.ration}</font>】
										    			<c:if test="${!empty item.tipText}">
										    				<font color=#f5f5f5>（注：${item.tipText}）</font>
										    			</c:if>
										    			<c:if test="${item.score > 0}">
										    				<font color=green>${item.score}分</font>
										    			</c:if>
										    			<c:if test="${item.need}">
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
													    					<c:if test="${item.score > 0}">
													    						&nbsp;
													    						<font color=green>
													    							${option.score}分
													    						</font>
													    					</c:if>
												    					</td>
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
																	       		<c:when test="${item.itemType == 0}">
																					<input type="radio" value="${option.optionId}" id="item_${item.itemId}_${optionIndex.index+1}" name="item_${item.itemId}" />
																	       		</c:when>
																	       		
																	       		<c:when test="${item.itemType == 1}">
																					<input type="checkbox" value="${option.optionId}" id="item_${item.itemId}_${optionIndex.index+1}" name="item_${item.itemId}" />
																	       		</c:when>
																	       		
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
													    					<c:if test="${item.score > 0}">
													    						&nbsp;
													    						<font color=green>
													    							${option.score}分
													    						</font>
													    					</c:if>
													    				</li>
													    			</c:forEach>
													    		</ul>
															</c:otherwise>
														</c:choose>
														
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
								    	<td style="background-color: #0e1827" align="left">
								    		&nbsp;&nbsp;&nbsp;&nbsp;
								    		<span style="color:#ffffff;font-size:15px;">
								    			<b>${item.itemName}</b>
								    		</span>
								    		<c:if test="${!empty item.picPath}">
							    				<a title="点击查看原图" href="<c:url value="/"/>${item.picPath}" target="_blank">
							    					<img src="<c:url value="/"/>${item.picPath}"  onload="setImgSize(this,100,100);"" />
							    				</a>
							    			</c:if>
							    			
							    			<c:choose>
									       		<c:when test="${item.itemType == 0}">【单选】</c:when>
									       		<c:when test="${item.itemType == 1}">【多选】</c:when>
									       	</c:choose>
									       
									       【权重：<font color=green>${item.ration}</font>】
									       
							    			<c:if test="${!empty item.tipText}">
							    				<font color=#f5f5f5>（注：${item.tipText}）</font>
							    			</c:if>
							    			<c:if test="${item.score > 0}">
							    				<font color=oragne>${item.score}分</font>
							    			</c:if>
							    			<c:if test="${item.need}">
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
										    					<c:if test="${item.score > 0}">
										    						&nbsp;
										    						<font color=green>
										    							${option.score}分
										    						</font>
										    					</c:if>
									    					</td>
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
										    					<c:if test="${item.score > 0}">
										    						&nbsp;
										    						<font color=green>
										    							${option.score}分
										    						</font>
										    					</c:if>
										    				</li>
										    			</c:forEach>
										    		</ul>
												</c:otherwise>
											</c:choose>
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
        <td style="background-color: #0e1827" align=left>
          <!--<input type="button" id="button" style="cursor: pointer;" onclick="submitData();" value="提交"/>
             &nbsp;
          --><input type="button" value="关闭" style="cursor: pointer;" onclick="window.close();"/>
        </td>
     </tr>
</table>
</form>
</body>
