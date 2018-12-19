<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>
<script>

function changeMonth(obj) {
	document.activityInforForm.action="<c:url value="/customer/activityInfor"/>.do?method=listMonth&tag=changeMonth&dateData=${_LastDate}";
	document.activityInforForm.submit();
}
//下一年
function changeYearDown(obj){
	document.activityInforForm.action="<c:url value="/customer/activityInfor"/>.do?method=listMonth&tag=changeYearDown&dateData=${_LastDate}";
	document.activityInforForm.submit();
}

//上一年
function changeYearUp(obj){
	document.activityInforForm.action="<c:url value="/customer/activityInfor"/>.do?method=listMonth&tag=changeYearUp&dateData=${_LastDate}";
	document.activityInforForm.submit();
}
//下一月
function changeMonthDown(obj){
	document.activityInforForm.action="<c:url value="/customer/activityInfor"/>.do?method=listMonth&tag=changeMonthDown&dateData=${_LastDate}";
	document.activityInforForm.submit();
}

//上一月
function changeMonthUp(obj){
	document.activityInforForm.action="<c:url value="/customer/activityInfor"/>.do?method=listMonth&tag=changeMonthUp&dateData=${_LastDate}";
	document.activityInforForm.submit();
}
function changeYear(obj) {
	document.activityInforForm.action="<c:url value="/customer/activityInfor"/>.do?method=listMonth&tab=changeYear&dateData=${_LastDate}";
	document.activityInforForm.submit();
}

//查看
function doView(rowId){
	window.showModalDialog("/customer/activityInfor.do?method=viewActivity&rowId="+rowId,'',"dialogWidth:1000px;dialogHeight:600px;center:Yes;dialogTop: 100px; dialogLeft: 200px;");
}	
</script>

<BODY>
		<form:form commandName="activityInforVo" id="activityInforForm"   name="activityInforForm" onsubmit="post" >
		<table width="100%">
			<TBODY>
				<TR>
					<!-- Main Content Part -->
					<TD vAlign=top>
						<TABLE border=0 cellSpacing=0 cellPadding=0 width="100%">
							<TR>
								<TD align="left" background= <c:url value='/images'/>/index_110.gif>
								&nbsp;&nbsp;&nbsp;
								<span><a title="上一年" onclick="changeYearDown(this);" href="javaScript:;"><img src="<c:url value="/images/"/>go-down.gif" border="0"/></a></span>
									<form:select path="searchYear" onchange="changeYear(this);" >
									<script language="javascript">
										var year = '${_Year}';
										if(year!=null){
											var topYear = '${_NowYear}';
											if('${_Year}'>'${_NowYear}'){
												topYear = '${_Year}';
											}else if('${_Year}'<'${_NowYear}'-10){
												topYear = (topYear -('${_NowYear}'-10-'${_Year}'));
											}
											for(var nowYear = topYear;nowYear>topYear-11 ;nowYear--){
												if(year == nowYear){
													document.write("<option value=\"" + nowYear + "\" selected>" + nowYear + "</option>");
												}else{
													document.write("<option value=\"" + nowYear + "\" >" + nowYear + "</option>");
												}
											}
										}else{
											for(var nowYear = '${_NowYear}';nowYear>'${_NowYear}'-11 ;nowYear--){
												document.write("<option value=\"" + nowYear + "\" >" + nowYear + "</option>");
											}
										}
										
								    </SCRIPT>
									</form:select>
									<span><a title="下一年" onclick="changeYearUp(this);" href="javaScript:;"><img src="<c:url value="/images/"/>go-up.gif" border="0"/></a></span>
									&nbsp;&nbsp;&nbsp;&nbsp;
									<span><a title="上一月" onclick="changeMonthDown(this);" href="javaScript:;"><img src="<c:url value="/images/"/>go-down.gif" border="0"/></a></span>
									<form:select path="searchMonth" onchange="changeMonth(this);" >
                                    <script language="javascript">
										var mon = '${_Month}';
										if(mon!=null){
											for(var i=1;i<13;i++){
											if(i == mon){
												document.write("<option value=\"" + i + "\" selected>" + i + "</option>");
												}else{
													document.write("<option value=\"" + i + "\" >" + i + "</option>");
												}
											}
										}else{
											for(var i=1;i<13;i++){
												document.write("<option value=\"" + i + "\" >" + i + "</option>");
											}
										}
										
																						
									</script>		
									</form:select>
									<span><a title="下一月" onclick="changeMonthUp(this);" href="javaScript:;"><img src="<c:url value="/images/"/>go-up.gif" border="0"/></a></span>
								</TD>
							</TR>
							<TR>
								<TD>
									<table width="100%" border="0" bgcolor="#7D7D7D" cellspacing="1" cellpadding="2" align="center">
										<tr bgcolor="#BFD2E3" height="40">
											<td align="center" width="14%"><strong>日</strong></td>
											<td align="center" width="14%"><strong>一</strong></td>
											<td align="center" width="14%"><strong>二</strong></td>
											<td align="center" width="14%"><strong>三</strong></td>
											<td align="center" width="14%"><strong>四</strong></td>
											<td align="center" width="14%"><strong>五</strong></td>
											<td align="center" width="16%"><strong>六</strong></td>
										</tr>
										<tr height="55">
											<c:set var="_TypeNum" value="0" />
											<c:forEach var="arrayList" items="${_ScheduleList}" varStatus="status">
												
												<c:if test="${_TypeNum != 0 && _TypeNum%7 == 0}"></tr><tr height="55"></c:if>
													<c:choose>
														<c:when test="${status.index == _CurrentDay}">
															<td height="55" style=" cursor:hand;" onMouseOver="this.style.backgroundColor='#BFD2E3';" onMouseOut="this.style.backgroundColor=''" align="center"bgcolor='#C0C0C0'>
														</c:when>
														<c:otherwise>
															<c:choose>
	                        									<c:when test="${status.index%7 == 0 || status.index%7 == 6}">
	                     											<td height="55" style=" cursor:hand;" onMouseOver="this.style.backgroundColor='#BFD2E3';" onMouseOut="this.style.backgroundColor=''" align="center"bgcolor='#FFE8E9'>                     	
	                     										</c:when>
	                        									<c:otherwise>
	                        										<td height="55" style=" cursor:hand;" onMouseOver="this.style.backgroundColor='#BFD2E3'" onMouseOut="this.style.backgroundColor=''" align="center" bgcolor='#FFFFFF'>
	                        									</c:otherwise>
	                        								</c:choose>
	                        							</c:otherwise>
	                        						</c:choose>
													<c:choose>
														<c:when test="${!empty arrayList}">
															<table width="98%">
																<tr>
																	<td>&nbsp;</td>
																	<td align="right">${_Date[status.index - _WeekDay]}</td>
																</tr>
																<tr>
																	<td colspan="2">
																		<c:forEach var="activity" items="${arrayList}" varStatus="stu">
																			<c:choose>
																				<c:when test="${activity.activityDate!=null}">
																					<a title="点击查看详情" onclick="doView(${activity.activityId});" href="javaScript:;">
																					<img src="<c:url value="/images/"/>complete.gif" border="0"/></a>
																				</c:when>
																				<c:otherwise>
																					<a title="点击查看详情" onclick="doView(${activity.activityId});" href="javaScript:;">
																					<img src="<c:url value="/images/"/>plan.gif" border="0"/></a>
																				</c:otherwise>
																			</c:choose>
																		</c:forEach>
																	</td>
																</tr>
															</table>
															</td>
														</c:when>
														<c:otherwise>
															<table width="98%">
																<tr>
																	<td>&nbsp;</td>
																	<td align="right">${_Date[status.index - _WeekDay]}</td>
																</tr>
																<tr>
																	<td colspan="2">&nbsp;</td>
																</tr>
															</table>
														</td>			
														</c:otherwise>
													</c:choose>			
												<c:set var="_TypeNum" value="${_TypeNum + 1}"/>
											</c:forEach>
										</tr>
									</table>
								</TD>
							</tr>
						</table>
					</TD>
				</TR>
			</TBODY>
			<tr>
				<td>
					<span>计划活动：<img src="/images/plan.gif" alt="计划活动"/></span>
					<span>已完成活动： <img src="/images/complete.gif" alt="已经完成活动"/></span>
				</td>
			</tr>
		</TABLE>
	</form:form>
</BODY>

