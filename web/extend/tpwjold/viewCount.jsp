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
</style>
<base target="_self"/>
<body>
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
	
	<tr>
		<td align=center>
			<table style="border:2px dotted #0DE8F5;" width="99%">
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
			</table>
			<br/>
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
											    	<td bgcolor="#dfeffc" align="left">
											    		&nbsp;&nbsp;&nbsp;&nbsp;
											    		<span style="color:#15A7BC;font-size:15px;">
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
										    				<font color=gray>（注：${item.tipText}）</font>
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
																			<!--<input type="checkbox" value="${option.optionId}" id="item_${item.itemId}_${optionIndex.index+1}" name="item_${item.itemId}" />
											    							--><a title="点击查看原图" href="<c:url value="/"/>${option.picPath}" target="_blank">
														    					<img src="<c:url value="/"/>${option.picPath}" onload="setImgSize(this,100,100);" />
														    				</a>
													    					<c:if test="${item.score > 0}">
													    						&nbsp;
													    						<font color=green>
													    							${option.score}分
													    						</font>
													    					</c:if>
													    					
													    					<c:set var="count" value="0"/>
													    					<c:forEach items="${_VoteItemList}" var="voteItem">
													    						<c:forEach items="${fn:split(voteItem.voteValue,',')}" var="voteValue">
													    							<c:if test="${voteValue == option.optionId}">
														    							<c:set var="count" value="${count+1}"/>
														    							<c:set var="allScore" value="${allScore+option.score}"/>
														    						</c:if>
													    						</c:forEach>
													    					</c:forEach>
													    					
													    					<c:if test="${item.score<=0}">
														    					<!-- 计次统计 -->
														    					<br/><br/>
														    					<c:set var="scale" value="${count/fn:length(_TopicInfor.voteInfors)}" />
														    					<div style="float:left;border:1px solid silver;width:90px;height:10px;text-align:left;">
														    						<div style="height:10px;width:${90*scale}px;background-color:#ee335f;"></div><!-- #dd30ae -->
														    					</div>
														    					
														    					<div>
														    						&nbsp;&nbsp;
														    						<font color=green><fmt:formatNumber value="${scale*100}" pattern="0.0"/>%&nbsp;（${count}票）</font>
														    					</div>
														    				</c:if>
												    					</td>
												    					<c:set var="_ItemNum" value="${_ItemNum+1}"/>
												    					<c:if test="${_ItemNum % 4 == 0}">
												    						</tr><tr>
												    					</c:if>
											    					</c:forEach>
											    					</tr>
										    					</table>
										    					
										    					<!-- 计分 -->
												    			<c:if test="${item.score > 0}">
												    				<br/>
												    				<div style="border:5px double silver;width:200px;padding-10px;text-align:center;">
												    					<font color=red>
												    						<h2>平均分：<fmt:formatNumber value="${allScore/fn:length(_TopicInfor.voteInfors)}" pattern="0.0"/></h2>
												    					</font>
												    				</div>
												    			</c:if>
															</c:when>
															<c:otherwise>
																<ul>
																	<c:set var="allScore" value="0"/>
													    			<c:forEach items="${item.options}" var="option" varStatus="optionIndex">
													    				<li>
													    					<c:choose>
																	       		<c:when test="${item.itemType == 0}"><!--<input type="radio" value="${option.optionId}" id="item_${item.itemId}_${optionIndex.index+1}" name="item_${item.itemId}" />--></c:when>
																	       		<c:when test="${item.itemType == 1}"><!--<input type="checkbox" value="${option.optionId}" id="item_${item.itemId}_${optionIndex.index+1}" name="item_${item.itemId}" />--></c:when>
																	       		<c:when test="${item.itemType == 2 || item.itemType == 3}">
																	       			
																	       		</c:when>
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
													    				
													    				<c:set var="count" value="0"/>
												    					<c:forEach items="${_VoteItemList}" var="voteItem">
												    						<c:forEach items="${fn:split(voteItem.voteValue,',')}" var="voteValue">
												    							<c:if test="${voteValue == option.optionId}">
													    							<c:set var="count" value="${count+1}"/>
													    							<c:set var="allScore" value="${allScore+option.score}"/>
													    						</c:if>
												    						</c:forEach>
												    					</c:forEach>
												    					
													    				<!-- 单选、多选计次 -->
													    				<c:if test="${(item.itemType == 0 || item.itemType == 1) && item.score<=0}">
														    				<li>
														    					<c:set var="scale" value="${count/fn:length(_TopicInfor.voteInfors)}" />
														    					<div style="float:left;border:1px solid silver;width:200px;height:10px;text-align:left;">
														    						<div style="height:10px;width:${200*scale}px;background-color:#ee335f;"></div><!-- #dd30ae -->
														    					</div>
														    					
														    					<div>
														    						&nbsp;&nbsp;
														    						<font color=green><fmt:formatNumber value="${scale*100}" pattern="0.0"/>%&nbsp;（${count}票）</font>
														    					</div>
														    				</li><br/>
													    				</c:if>
													    				
													    				<!-- 文本 -->
													    				<c:if test="${(item.itemType == 2 || item.itemType == 3)}">
														    				<c:forEach items="${_VoteItemList}" var="voteItem">
													    						<c:if test="${item.itemId == voteItem.item.itemId && !empty voteItem.voteText}">
													    							<p><font color=blue><b>${voteItem.voteInfor.voter.person.personName}</b></font>：${voteItem.voteText}；<p/>
													    						</c:if>
													    					</c:forEach>
													    				</c:if>
													    			</c:forEach>
													    			
													    			<!-- 单选、多选计分 -->
													    			<c:if test="${(item.itemType == 0 || item.itemType == 1) && item.score > 0}">
													    				<br/>
													    				<div style="border:5px double silver;width:200px;padding-10px;text-align:center;">
													    					<font color=red>
													    						<h2>平均分：<fmt:formatNumber value="${allScore/fn:length(_TopicInfor.voteInfors)}" pattern="0.0"/></h2>
													    					</font>
													    				</div>
													    			</c:if>
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
								    	<td bgcolor="#dfeffc" align="left">
								    		&nbsp;&nbsp;&nbsp;&nbsp;
								    		<span style="color:#15A7BC;font-size:15px;">
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
							    				<font color=gray>（注：${item.tipText}）</font>
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
															<c:set var="allScore" value="0"/>
															<c:forEach items="${item.options}" var="option" varStatus="optionIndex">
																<td style="padding-left:15px;">
																	<!--<input type="checkbox" value="${option.optionId}" id="item_${item.itemId}_${optionIndex.index+1}" name="item_${item.itemId}" /> -->
									    							<a title="点击查看原图" href="<c:url value="/"/>${option.picPath}" target="_blank">
												    					<img src="<c:url value="/"/>${option.picPath}" onload="setImgSize(this,100,100);" />
												    				</a>
											    					<c:if test="${item.score > 0}">
											    						&nbsp;
											    						<font color=green>
											    							${option.score}分
											    						</font>
											    					</c:if>
											    					
											    					<c:set var="count" value="0"/>
											    					<c:forEach items="${_VoteItemList}" var="voteItem">
											    						<c:forEach items="${fn:split(voteItem.voteValue,',')}" var="voteValue">
											    							<c:if test="${voteValue == option.optionId}">
												    							<c:set var="count" value="${count+1}"/>
												    							<c:set var="allScore" value="${allScore+option.score}"/>
												    						</c:if>
											    						</c:forEach>
											    					</c:forEach>
											    					
											    					<c:if test="${item.score<=0}">
												    					<!-- 计次统计 -->
												    					<br/><br/>
												    					<c:set var="scale" value="${count/fn:length(_TopicInfor.voteInfors)}" />
												    					<div style="float:left;border:1px solid silver;width:100px;height:10px;text-align:left;">
												    						<div style="height:10px;width:${100*scale}px;background-color:#ee335f;"></div><!-- #dd30ae -->
												    					</div>
												    					
												    					<div>
												    						&nbsp;&nbsp;
												    						<font color=green><fmt:formatNumber value="${scale*100}" pattern="0.0"/>%&nbsp;（${count}票）</font>
												    					</div>
												    				</c:if>
										    					</td>
										    					<c:set var="_ItemNum" value="${_ItemNum+1}"/>
										    					<c:if test="${_ItemNum % 4 == 0}">
										    						</tr><tr>
										    					</c:if>
									    					</c:forEach>
								    					</tr>
							    					</table>
							    					<!-- 计分 -->
									    			<c:if test="${item.score > 0}">
									    				<br/>
									    				<div style="border:5px double silver;width:200px;padding-10px;text-align:center;">
									    					<font color=red>
									    						<h2>平均分：<fmt:formatNumber value="${allScore/fn:length(_TopicInfor.voteInfors)}" pattern="0.0"/></h2>
									    					</font>
									    				</div>
									    			</c:if>
												</c:when>
												<c:otherwise>
													<ul>
										    			<c:set var="allScore" value="0"/>
										    			<c:forEach items="${item.options}" var="option" varStatus="optionIndex">
										    				<li>
										    					<c:choose>
														       		<c:when test="${item.itemType == 0}"><!--<input type="radio" value="${option.optionId}" id="item_${item.itemId}_${optionIndex.index+1}" name="item_${item.itemId}" />--></c:when>
														       		<c:when test="${item.itemType == 1}"><!--<input type="checkbox" value="${option.optionId}" id="item_${item.itemId}_${optionIndex.index+1}" name="item_${item.itemId}" />--></c:when>
														       		<c:when test="${item.itemType == 2 || item.itemType == 3}"></c:when>
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
										    				
										    				<c:set var="count" value="0"/>
									    					<c:forEach items="${_VoteItemList}" var="voteItem">
									    						<c:forEach items="${fn:split(voteItem.voteValue,',')}" var="voteValue">
									    							<c:if test="${voteValue == option.optionId}">
										    							<c:set var="count" value="${count+1}"/>
										    							<c:set var="allScore" value="${allScore+option.score}"/>
										    						</c:if>
									    						</c:forEach>
									    					</c:forEach>
									    					
										    				<!-- 单选、多选计次 -->
										    				<c:if test="${(item.itemType == 0 || item.itemType == 1) && item.score<=0}">
											    				<li>
											    					<c:set var="scale" value="${count/fn:length(_TopicInfor.voteInfors)}" />
											    					<div style="float:left;border:1px solid silver;width:200px;height:10px;text-align:left;">
											    						<div style="height:10px;width:${200*scale}px;background-color:#ee335f;"></div><!-- #dd30ae -->
											    					</div>
											    					
											    					<div>
											    						&nbsp;&nbsp;
											    						<font color=green><fmt:formatNumber value="${scale*100}" pattern="0.0"/>%&nbsp;（${count}票）</font>
											    					</div>
											    				</li><br/>
										    				</c:if>
										    				<!-- 文本 -->
										    				<c:if test="${(item.itemType == 2 || item.itemType == 3)}">
											    				<c:forEach items="${_VoteItemList}" var="voteItem">
										    						<c:if test="${item.itemId == voteItem.item.itemId && !empty voteItem.voteText}">
										    							<p><font color=blue><b>${voteItem.voteInfor.voter.person.personName}</b></font>：${voteItem.voteText}；<p/>
										    						</c:if>
										    					</c:forEach>
										    				</c:if>
										    			</c:forEach>
										    			
										    			<!-- 单选、多选计分 -->
										    			<c:if test="${(item.itemType == 0 || item.itemType == 1) && item.score > 0}">
										    				<br/>
										    				<div style="border:5px double silver;width:200px;padding-10px;text-align:center;">
										    					<font color=red>
										    						<h2>平均分：<fmt:formatNumber value="${allScore/fn:length(_TopicInfor.voteInfors)}" pattern="0.0"/></h2>
										    					</font>
										    				</div>
										    			</c:if>
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
        <td bgcolor="#dfeffc" align=left>
          <!--<input type="button" id="button" style="cursor: pointer;" onclick="submitData();" value="提交"/>
             &nbsp;-->
          <input type="button" value="关闭" style="cursor: pointer;" onclick="window.close();"/>
        </td>
     </tr>
</table>
</form>
</body>
