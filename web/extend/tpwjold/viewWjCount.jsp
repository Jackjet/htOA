<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<title>查看统计信息</title>
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
		
		//window.scrollTo(0,99999);
		//document.getElementById("gototest").scrollIntoView();
	});
</script>
<style>
	img{
		border:0;
	}
	li{
		list-style:none;
		line-height:30px;
	}
	input,label { vertical-align:middle;} 
	*{
		font-family:微软雅黑;
	}
</style>
<base target="_self"/>
<body>
<br/>
<form id="voteInforForm" name="voteInforForm" action="/tpwj/voteInfor.do?method=save" method="post">
<input type="hidden" name="topicId" id="topicId" value="${_TopicInfor.topicId}"/>
<table width="80%" cellpadding="6" cellspacing="1" align="center" border="1" bordercolor="#c5dbec">
	<tr>
		<td bgcolor="#dfeffc" align="center">
			<h2><font color="#15A7BC">${_TopicInfor.topicName} 问卷汇总表</font></h2>
			<div style="text-align:left;padding-left:10px;"><font color=gray>${_TopicInfor.descrip}</font></div>
			<c:if test="${!empty _TopicInfor.rules}">
				<br/>
				<div style="text-align:left;padding-left:10px;"><font color=orange><b>投票规则：</b></font><font color=gray>${_TopicInfor.rules}</font></div>
			</c:if>
			<br/>
			<!--<div style="text-align:left;padding-left:10px;"><font color=red>带 * 号的题目，请您务必填写！</font></div>-->
		</td>
	</tr>
	
	<tr>
		<td bgcolor="#dfeffc" align="center" style="font-size:14px;font-family:黑体;">
			<c:set var="_AllNum" value="0"/>
			<b>应投票人数：
				<font color=blue>
					<c:if test="${_TopicInfor.openType == '0'}">${fn:length(_TopicInfor.rights)}<c:set var="_AllNum" value="${fn:length(_TopicInfor.rights)}"/></c:if>
					<c:if test="${_TopicInfor.openType == '1'}">${_AllUserCount}（全体用户）<c:set var="_AllNum" value="${_AllUserCount}"/></c:if>
				</font>
			</b>
			<b style="margin-left:50px;">已投票人数：<font color=blue>${fn:length(_TopicInfor.voteInfors)}</font></b>
			<b style="margin-left:50px;">未投票人数：<font color=red>${_AllNum - fn:length(_TopicInfor.voteInfors)}</font></b>
			<b style="margin-left:50px;">投票率：<font color=blue><fmt:formatNumber value="${fn:length(_TopicInfor.voteInfors) / _AllNum * 100}" pattern="0.0"/>%</font></b>
		</td>
	</tr>
	
	<tr>
		<td align=center>
			<table style="border:1px solid black;font-size:15px;" width="100%">   
      			<tr>  
					<td width="15%" align="center" style="border-right:1px solid black;font-size:16px;">
						<b>类别</b>
					</td>
					<td width="35%" style="border-right:1px solid black;font-size:16px;"><b>项目</b></td>
					<!--<c:forEach items="${_Options}" var="option">
						<td style="border-right:1px solid black;" width="${50/fn:length(_Options)}%"><b>${option.optionName}</b></td>
					</c:forEach>-->
					<td style="font-size:16px;"><b>选项统计</b></td>
				</tr>
			</table>
			
			
			<c:forEach items="${_CategoryList}" var="category">
          		<c:choose>
          			<c:when test="${!empty category}">
          				<c:set var="rows" value="0"/>
          				<c:forEach items="${_TopicInfor.items}" var="item" varStatus="index">
				        	<c:if test="${category == item.categoryName}">
				        		<c:set var="rows" value="${rows+1}"/>
				        	</c:if>
				        </c:forEach>
				        
				        <table style="border:1px solid black;" cellpadding="0" width="100%">  
				        	<tr>
		        				<td width="15%" bgcolor="#dfeffc" rowspan="${rows}" align="center" style="border:1px solid black;">
									<b>${category}</b>
								</td>
			        			
					        	<c:forEach items="${_TopicInfor.items}" var="item" varStatus="index">
						        	<c:if test="${category == item.categoryName}">
						        			<td width="35%" align=left style="padding-left:5px;border:1px solid black;">${item.itemName}</td>
						        			
						        			<td style="border:1px solid black;padding:0px;">
						        				<!-- 单选、多选 -->
						        				<c:if test="${(item.itemType == 0 || item.itemType == 1)}">
						        					<table style="border:0px solid black;" cellpadding="0" width="100%"> 
							        					<tr>
										        			<c:forEach items="${item.options}" var="itemOption" varStatus="optionIndex">
										        				<c:if test="${optionIndex.index < (fn:length(item.options)-1)}">
										        					<td style="border-right:1px solid black;border-bottom:1px solid black;" width="${100/fn:length(item.options)}%">
										        				</c:if>
										        				<c:if test="${optionIndex.index == (fn:length(item.options)-1)}">
										        					<td style="border-bottom:1px solid black;" width="${100/fn:length(item.options)}%">
										        				</c:if>
										        				
										        					<b>${itemOption.optionName}</b></td>
										        			</c:forEach>
										        		</tr>
										        		<tr>	
										        			<c:forEach items="${item.options}" var="option" varStatus="optionIndex">
										        				<c:if test="${optionIndex.index < (fn:length(item.options)-1)}">
										        					<td style="border-right:1px solid black;" width="${100/fn:length(item.options)}%">
										        				</c:if>
										        				<c:if test="${optionIndex.index == (fn:length(item.options)-1)}">
										        					<td style="border:0px solid black;" width="${100/fn:length(item.options)}%">
										        				</c:if>
																
																	<c:set var="count" value="0"/>
																	<c:forEach items="${_VoteItemList}" var="voteItem">
											    						<c:forEach items="${fn:split(voteItem.voteValue,',')}" var="voteValue">
											    							<c:if test="${voteValue == option.optionId && voteItem.item.itemId == item.itemId}">
												    							<c:set var="count" value="${count+1}"/>
												    						</c:if>
											    						</c:forEach>
											    					</c:forEach>
											    					${count}
																</td>
															</c:forEach>
														</tr>
													</table>
						        				</c:if>
						        				
						        				<!-- 文本 -->
							    				<c:if test="${(item.itemType == 2 || item.itemType == 3)}">
							    					<table style="border:0px solid black;" cellpadding="0" width="100%"> 
							        					<tr>
							        						<td align=left style="padding-left:10px;">
							        							<c:forEach items="${_VoteItemList}" var="voteItem">
										    						<c:if test="${item.itemId == voteItem.item.itemId && !empty voteItem.voteText}">
										    							<!-- <font color=blue><b>${voteItem.voteInfor.voter.person.personName}</b></font>： -->
										    							<p><font color=blue>▶</font>&nbsp;${voteItem.voteText}；<p/>
										    						</c:if>
										    					</c:forEach>
							        						</td>
							        					</tr>
							        				</table>
							    				</c:if>
											</td>
						        		</tr><tr>
						        	</c:if>
						        </c:forEach>
				        	</tr>
				        </table>
	          		</c:when>
	          		<c:when test="${empty category}">
	          			
	          			<table style="border:1px solid black;" cellpadding="0" width="100%">  
				        	<c:forEach items="${_TopicInfor.items}" var="item" varStatus="index">
					        	<c:if test="${empty item.categoryName}">
					        		<tr>
					        			<td width="50%" align=left style="padding-left:5px;border:1px solid black;">${item.itemName}</td>
					        			
					        			<td style="border:1px solid black;padding:0px;">
					        				<!-- 单选、多选 -->
					        				<c:if test="${(item.itemType == 0 || item.itemType == 1)}">
					        					<table style="border:0px solid black;" cellpadding="0" width="100%"> 
						        					<tr>
									        			<c:forEach items="${item.options}" var="itemOption" varStatus="optionIndex">
									        				<c:if test="${optionIndex.index < (fn:length(item.options)-1)}">
									        					<td style="border-right:1px solid black;border-bottom:1px solid black;" width="${100/fn:length(item.options)}%">
									        				</c:if>
									        				<c:if test="${optionIndex.index == (fn:length(item.options)-1)}">
									        					<td style="border-bottom:1px solid black;" width="${100/fn:length(item.options)}%">
									        				</c:if>
									        				
									        					<b>${itemOption.optionName}</b></td>
									        			</c:forEach>
									        		</tr>
									        		<tr>	
									        			<c:forEach items="${item.options}" var="option" varStatus="optionIndex">
									        				<c:if test="${optionIndex.index < (fn:length(item.options)-1)}">
									        					<td style="border-right:1px solid black;" width="${100/fn:length(item.options)}%">
									        				</c:if>
									        				<c:if test="${optionIndex.index == (fn:length(item.options)-1)}">
									        					<td style="border:0px solid black;" width="${100/fn:length(item.options)}%">
									        				</c:if>
															
																<%--<c:set var="count" value="0"/>
																<c:forEach items="${_VoteItemList}" var="voteItem">
										    						<c:forEach items="${fn:split(voteItem.voteValue,',')}" var="voteValue">
										    							<c:if test="${voteValue == option.optionId && voteItem.item.itemId == item.itemId}">
											    							<c:set var="count" value="${count+1}"/>
											    						</c:if>
										    						</c:forEach>
										    					</c:forEach>
										    					${count}--%>
										    					
										    					<c:set var="count" value="0"/>
																<c:forEach items="${_VoteItemList}" var="voteItem">
										    						<c:forEach items="${fn:split(voteItem.voteValue,',')}" var="voteValue">
										    							<c:if test="${voteValue == option.optionId && voteItem.item.itemId == item.itemId}">
											    							<c:set var="count" value="${count+1}"/>
											    						</c:if>
										    						</c:forEach>
										    					</c:forEach>
										    					${count}
															</td>
														</c:forEach>
													</tr>
												</table>
					        				</c:if>
					        				
					        				<!-- 文本 -->
						    				<c:if test="${(item.itemType == 2 || item.itemType == 3)}">
						    					<table style="border:0px solid black;" cellpadding="0" width="100%"> 
						        					<tr>
						        						<td align=left style="padding-left:10px;">
						        							<c:forEach items="${_VoteItemList}" var="voteItem">
									    						<c:if test="${item.itemId == voteItem.item.itemId && !empty voteItem.voteText}">
									    							<!-- <font color=blue><b>${voteItem.voteInfor.voter.person.personName}</b></font>： -->
										    						<p><font color=blue>▶</font>&nbsp;${voteItem.voteText}；<p/>
									    						</c:if>
									    					</c:forEach>
						        						</td>
						        					</tr>
						        				</table>
						    				</c:if>
										</td>
					        		</tr>
					        	</c:if>
					        </c:forEach>
				        </table>
	          		</c:when>
          		</c:choose>
          		
          	</c:forEach>
			
			
		</td>
	</tr>
	
                    
     <tr> 
        <td bgcolor="#dfeffc" align=left>
          <!--<input type="button" id="button" style="cursor: pointer;" onclick="submitData();" value="提交"/>
             &nbsp;-->
          <input type="button" value="关闭" style="cursor: pointer;" onclick="window.close();"/>
        </td>
     </tr>
</table>
</form>
</body>
