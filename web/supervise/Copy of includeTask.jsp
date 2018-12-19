<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>
	<script>
		function showLogs(reportId){
			//var html = "<br/>";
			//alert(reportId);			
			$("#viewLog_"+reportId).dialog({
		        autoOpen: false,       
		        modal: true,   
		        resizable: true,       
		        width: 750,   
		        title: "修改记录",   
		        buttons: {   
		            "关闭": function(){$("#viewLog_"+reportId).dialog("close");}
		        }   
		    });
		    $("#viewLog_"+reportId).dialog("open");
			
			//setTimeout(function(){$("#viewUser").html(html);},"500");
		}
		
		function disContent(id){
			$("#"+id).toggle();
		}
		
	</script>
	<style>
		.logContent{
			font-size:12px;
			
		}
	</style>

	<input type="hidden" name="taskId" value="${_SuperviseInfor.taskId}"/>
	<tbody>
		<tr style="height: 0px;">
			<td style="width: 15%;"></td>
			<td style="width: 35%;"></td>
			<td style="width: 15%;"></td>
			<td style="width: 35%;"></td>
		</tr>
		<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
			<td style="width: 15%;" class="ui-state-default jqgrid-rownum">督办事项：</td>
			<td style="width: 85%;" colspan=3 >${_SuperviseInfor.taskName}</td>
		</tr>
				
		<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
			<td style="width:15%;" class="ui-state-default jqgrid-rownum">类别：</td>
			<td style="width:35%;" colspan=3 >${_SuperviseInfor.taskCategory.categoryName}</td>
			<!--<td style="width:15%;" class="ui-state-default jqgrid-rownum">下达任务的领导：</td>
			<td>${_SuperviseInfor.leader.person.personName}</td>
		--></tr>	
		<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
			<td class="ui-state-default jqgrid-rownum">责任部门：</td>
			<td>${_SuperviseInfor.organizeInfor.organizeName}</td>
			<td class="ui-state-default jqgrid-rownum">责任部门负责人：</td>
			<td>${_SuperviseInfor.manager.person.personName}</td>
		</tr>		
		
		<c:if test="${empty _SuperviseInfor.operator}">
			<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
				<td class="ui-state-default jqgrid-rownum">计划完成日期：</td>
				<td colspan=3>${_SuperviseInfor.finishDate}</td>
			</tr>
		</c:if>	
		<c:if test="${!empty _SuperviseInfor.operator}">
			<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
				<td class="ui-state-default jqgrid-rownum">计划完成日期：</td>
				<td>${_SuperviseInfor.finishDate}</td>
				<td class="ui-state-default jqgrid-rownum">责任人：</td>
				<td>${_SuperviseInfor.operator.person.personName}</td>
			</tr>
		</c:if>		
		
		
		<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 50px;">
			<td class="ui-state-default jqgrid-rownum" >督办内容：</td>
			<td valign="top" colspan=3>${_SuperviseInfor.content}</td>
		</tr>
		
		
		<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
			<td class="ui-state-default jqgrid-rownum">附件信息：</td>
			<td align="left" colspan=3><c:forEach var="file" items="${_Attachment_Names}" varStatus="status"><a title="点击下载" href="<c:url value="${'/common/'}"/>download.jsp?filepath=${_Attachments[status.index]}"><font color="red">${file}</font></a><br/></c:forEach></td>
		</tr>
		
		<c:if test="${!empty _SuperviseInfor.endTime}">
			<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
				<td class="ui-state-default jqgrid-rownum">实际完成日期：</td>
				<td colspan=3>${_SuperviseInfor.endTime}</td>
			</tr>
		</c:if>		
		
		<tr class="ui-widget-content jqgrow ui-row-ltr">
			<td class="ui-state-default jqgrid-rownum" colspan="4"><b>进度信息如下</b>：</td>
		</tr>
		
		<c:forEach items="${_SuperviseInfor.reports}" var="report">
			<c:set var="reportType" value=""/>
			<c:choose>
				<c:when test="${(report.reportType == 1 || report.reportType == 2) && report.parent == null}">
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;" valign="middle">
						<td class="ui-state-default jqgrid-rownum" valign="middle" align="center" rowspan="${5 * (fn:length(report.childs)+1)}">
							<c:if test="${report.reportType == 1}">
								<font color=red><b><fmt:formatDate value="${report.operateDate}" pattern="yyyy-MM"/></b></font>&nbsp;
								进度报告
								<c:set var="reportType" value="进度报告"/>
							</c:if>
							<c:if test="${report.reportType == 2}"><b>&nbsp;工作完成报告</b><c:set var="reportType" value="完成报告"/></c:if>
						</td>
						<td colspan=2 class="ui-state-default jqgrid-rownum">
							<font color=black><b>报告时间：</b></font><fmt:formatDate value="${report.operateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
							&nbsp;&nbsp;&nbsp;&nbsp;
							<font color=black><b>提交人：</b></font>${report.operator.person.personName}
							&nbsp;&nbsp;&nbsp;&nbsp;
							<font color=black><b>报告内容：</b></font>${report.content}
						</td>
						<td class="ui-state-default jqgrid-rownum" style="text-align:right">
							<div id="viewLog_${report.reportId}" class="logContent" style="display:none;">
								<table width="85%" cellpadding="5" cellspacing="0" align="center" border="1" >
									<tr>
										<th nowrap>序号</th>
										<th nowrap width="30%">修改内容</th>
										<th nowrap>修改人</th>
										<th nowrap>修改时间</th>
									</tr>
									<c:forEach items="${report.logs}" var="log" varStatus="status">
										<tr>
											<td nowrap>${status.index+1}</td>
											<td>${log.logContent}</td>
											<td nowrap>${log.operator.person.personName}</td>
											<td nowrap><fmt:formatDate value="${log.operateDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
										</tr>
									</c:forEach>
								</table>
							</div>
							<img align="absmiddle" src="<c:url value='/'/>images/edit.gif" border="0" onclick="editReport('','${report.reportId}','${report.superviseInfor.taskId}','${report.reportType}');" title="修改进度报告" style="cursor: pointer;"/>
							&nbsp;&nbsp;
							<img src="<c:url value='/'/>images/calendar.gif" onclick="showLogs(${report.reportId});" border="0" align="absmiddle" title="点击查看修改记录" style="cursor:pointer;"/>
							&nbsp;&nbsp;
							<img src="<c:url value='/'/>images/xpexpand3_s.gif" onclick="disContent('content${report.reportId}');" border="0" align="absmiddle" title="点击展开/收缩" style="cursor:pointer;"/>
							&nbsp;&nbsp;
						</td>
					</tr>
					
					<tbody id="content${report.reportId}" style="display:none;">
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 50px;" valign="top">
							<td colspan=3><font color=black><b>报告内容：</b></font><br/><br/>${report.content}</td>
						</tr>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 50px;" valign="top">
							<td colspan=3><font color=black><b>备注：</b></font><br/><br/>${report.memo}</td>
						</tr>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 50px;" valign="top">
							<td colspan=3><font color=black><b>附件：</b></font><br/><br/><attachment:fileView contextPath="">${report.attachment}</attachment:fileView><br/><br/></td>
						</tr>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 50px;" valign="top">
							<td colspan=3>
								<font color=black><b>部门负责人意见：</b></font>
								<br/><br/>
								<c:if test="${empty report.checkDate}">
									<font color=red>未审核</font>
									<br/><br/>
									<c:if test="${_SuperviseInfor.status == 2 && _SuperviseInfor.manager.personId == _SYSTEM_USER.personId && !_CanSubmitReport}">
										<input style="cursor: pointer;" class="buttonclass" id="setOperatorBtn" type="button" value="审核进度报告" onclick="checkReport('${report.reportId}','${report.reportType}');"/>
									</c:if>
									<c:if test="${_SuperviseInfor.status == 3 && _SuperviseInfor.manager.personId == _SYSTEM_USER.personId && !_CanSubmitFianlReport}">
										<input style="cursor: pointer;" class="buttonclass" id="setOperatorBtn1" type="button" value="审核完成报告" onclick="checkReport('${report.reportId}','${report.reportType}');"/>
									</c:if>
									
								</c:if>
								<c:if test="${!empty report.checkDate}">
									<b>
										<c:if test="${report.isPassed == 0}"><font color=red>不通过</font></c:if>
										<c:if test="${report.isPassed == 1}"><font color=blue>通过</font></c:if>
									</b>
									（<fmt:formatDate value="${report.checkDate}" pattern="yyyy-MM-dd HH:mm:ss"/>）
									<br/><br/>
									${report.managerAdvice}
									<br/><br/>
									<c:if test="${_SuperviseInfor.status == 2 && _SYSTEM_USER.personId == report.operator.personId && report.isDone == 0}">
										<input style="cursor: pointer;" class="buttonclass" id="setOperatorBtn" type="button" value="补充${reportType}" onclick="editReport('${report.reportId}','','${_SuperviseInfor.taskId}','1');"/>
									</c:if>
									<c:if test="${_SuperviseInfor.status == 3 && _SYSTEM_USER.personId == report.operator.personId && report.isDone == 0}">
										<input style="cursor: pointer;" class="buttonclass" id="setOperatorBtn" type="button" value="补充${reportType}" onclick="editReport('${report.reportId}','','${_SuperviseInfor.taskId}','2');"/>
									</c:if>
									
									<c:if test="${_SuperviseInfor.status == 4 && _SYSTEM_USER.personId == report.operator.personId && report.reportType == 2 && report.isJudged == 0}">
										<input style="cursor: pointer;" class="buttonclass" id="setOperatorBtn" type="button" value="补充${reportType}" onclick="editReport('${report.reportId}','','${_SuperviseInfor.taskId}','2');"/>
									</c:if>
									
									
									<!--<c:if test="${_SuperviseInfor.status == 4 && (empty report.childs) && report.reportType == 2 && _SuperviseInfor.creater.personId == _SYSTEM_USER.personId && report.isPassed == 1 && report.isDone == 1 && report.isJudged == 0 && _CanCheckFinal}">
										<input style="cursor: pointer;" class="buttonclass" id="setOperatorBtn" type="button" value="预判完成情况" onclick="judgeReport('${report.reportId}');"/>
									</c:if>-->
									<br/><br/>
								</c:if>
								
								<c:if test="${empty report.judgeDate}">
									<c:if test="${_SuperviseInfor.status == 4 && report.reportType == 2 && _SuperviseInfor.creater.personId == _SYSTEM_USER.personId && report.isPassed == 1 && report.isDone == 1 && report.isJudged == 0 && _CanCheckFinal}">
										<input style="cursor: pointer;" class="buttonclass" id="setOperatorBtn" type="button" value="预判完成情况" onclick="judgeReport('${report.reportId}');"/>
									</c:if>
								</c:if>
								<c:if test="${!empty report.judgeDate}">
									<b>
										<c:if test="${report.isJudgePassed == 0}"><font color=red>预判不通过</font></c:if>
										<c:if test="${report.isJudgePassed == 1}"><font color=blue>预判通过</font></c:if>
									</b>
									（<fmt:formatDate value="${report.judgeDate}" pattern="yyyy-MM-dd HH:mm:ss"/>）
									<br/><br/>
								</c:if>
								
								<c:if test="${_SuperviseInfor.status == 4 && _SYSTEM_USER.personId == report.operator.personId && report.reportType == 2 && report.isJudged == 1 && report.isJudgePassed == 0}">
									<input style="cursor: pointer;" class="buttonclass" id="setOperatorBtn" type="button" value="补充${reportType}" onclick="editReport('${report.reportId}','','${_SuperviseInfor.taskId}','2');"/>
								</c:if>
								
							</td>
						</tr>
					</tbody>
					
					<c:if test="${fn:length(report.childs) > 0}">
						<c:forEach items="${report.childs}" var="child">
							<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;" valign="middle">
								<!--<td class="ui-state-default jqgrid-rownum" valign="middle" align="center" rowspan="${5 * (fn:length(report.childs)+1)}">
									<font color=red><b><fmt:formatDate value="${report.operateDate}" pattern="yyyy-MM"/></b></font>&nbsp;进度报告
									
								</td>-->
								<td colspan=2 class="ui-state-default jqgrid-rownum">
									<font color=black><b>报告时间：</b></font><fmt:formatDate value="${child.operateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
									&nbsp;&nbsp;&nbsp;&nbsp;
									<font color=black><b>提交人：</b></font>${child.operator.person.personName}
									&nbsp;&nbsp;&nbsp;&nbsp;
									<font color=black><b>报告内容：</b></font>${report.content}
								</td>
								<td class="ui-state-default jqgrid-rownum" style="text-align:right">
									<div id="viewLog_${child.reportId}" class="logContent" style="display:none;">
										<table width="85%" cellpadding="5" cellspacing="0" align="center" border="1" >
											<tr>
												<th nowrap>序号</th>
												<th nowrap width="30%">修改内容</th>
												<th nowrap>修改人</th>
												<th nowrap>修改时间</th>
											</tr>
											<c:forEach items="${child.logs}" var="log" varStatus="childStatus">
												<tr>
													<td nowrap>${childStatus.index+1}</td>
													<td>${log.logContent}</td>
													<td nowrap>${log.operator.person.personName}</td>
													<td nowrap><fmt:formatDate value="${log.operateDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
												</tr>
											</c:forEach>
										</table>
									</div>
									<img align="absmiddle" src="<c:url value='/'/>images/edit.gif" border="0" onclick="editReport('${report.reportId}','${child.reportId}','${child.superviseInfor.taskId}','${child.reportType}');" title="修改进度报告" style="cursor: pointer;"/>
									&nbsp;&nbsp;
									<img src="<c:url value='/'/>images/calendar.gif" onclick="showLogs(${child.reportId});" border="0" align="absmiddle" title="点击查看修改记录" style="cursor:pointer;"/>
									&nbsp;&nbsp;
									<img src="<c:url value='/'/>images/xpexpand3_s.gif" onclick="disContent('content${child.reportId}');" border="0" align="absmiddle" title="点击展开/收缩" style="cursor:pointer;"/>
									&nbsp;&nbsp;
								</td>
							</tr>
							
							<tbody id="content${child.reportId}" style="display:none;">
								<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 50px;" valign="top">
									<td colspan=3><font color=black><b>报告内容：</b></font><br/><br/>${child.content}</td>
								</tr>
								<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 50px;" valign="top">
									<td colspan=3><font color=black><b>备注：</b></font><br/><br/>${child.memo}</td>
								</tr>
								<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 50px;" valign="top">
									<td colspan=3><font color=black><b>附件：</b></font><br/><br/><attachment:fileView contextPath="">${child.attachment}</attachment:fileView><br/><br/></td>
								</tr>
								<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 50px;" valign="top">
									<td colspan=3>
										<font color=black><b>部门负责人意见：</b></font>
										<br/><br/>
										<c:if test="${empty child.checkDate}">
											<font color=red>未审核</font>
											<br/><br/>
											<c:if test="${_SuperviseInfor.status == 2 && _SuperviseInfor.manager.personId == _SYSTEM_USER.personId && !_CanSubmitReport}">
												<input style="cursor: pointer;" class="buttonclass" id="setOperatorBtn" type="button" value="审核进度报告" onclick="checkReport('${child.reportId}','${child.reportType}');"/>
											</c:if>
											<c:if test="${(_SuperviseInfor.status == 3 || _SuperviseInfor.status == 4) && _SuperviseInfor.manager.personId == _SYSTEM_USER.personId && !_CanSubmitFianlReport}">
												<input style="cursor: pointer;" class="buttonclass" id="setOperatorBtn" type="button" value="审核完成报告" onclick="checkReport('${child.reportId}','${child.reportType}');"/>
											</c:if>
										</c:if>
										<c:if test="${!empty child.checkDate}">
											<b>
												<c:if test="${child.isPassed == 0}"><font color=red>不通过</font></c:if>
												<c:if test="${child.isPassed == 1}"><font color=blue>通过</font></c:if>
											</b>
											（<fmt:formatDate value="${child.checkDate}" pattern="yyyy-MM-dd HH:mm:ss"/>）
											<br/><br/>
											${child.managerAdvice}
											<br/><br/>
										</c:if>
										<c:if test="${empty child.judgeDate}">
											<c:if test="${_SuperviseInfor.status == 4 && child.reportType == 2 && _SuperviseInfor.creater.personId == _SYSTEM_USER.personId && child.isPassed == 1 && child.isDone == 1 && child.isJudged == 0 && _CanCheckFinal}">
												<input style="cursor: pointer;" class="buttonclass" id="setOperatorBtn" type="button" value="预判完成情况" onclick="judgeReport('${child.reportId}');"/>
											</c:if>
										</c:if>
										<c:if test="${!empty child.judgeDate}">
											<b>
												<c:if test="${child.isJudgePassed == 0}"><font color=red>预判不通过</font></c:if>
												<c:if test="${child.isJudgePassed == 1}"><font color=blue>预判通过</font></c:if>
											</b>
											（<fmt:formatDate value="${child.judgeDate}" pattern="yyyy-MM-dd HH:mm:ss"/>）
											<br/><br/>
											
										</c:if>
									</td>
								</tr>	
							</tbody>
						</c:forEach>
						
					</c:if>
					
					<tr class="ui-widget-content jqgrow ui-row-ltr">
						<td class="ui-state-default jqgrid-rownum" colspan="4" style="height:10px;"></td>
					</tr>
				</c:when>
				
				
				<c:when test="${report.reportType == 5}">
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;" valign="middle">
						<td class="ui-state-default jqgrid-rownum" valign="middle" align="center" rowspan="3">
							<b>&nbsp;公司领导意见</b>
						</td>
						<td colspan=3 class="ui-state-default jqgrid-rownum">
							<font color=black><b>审批时间：</b></font><fmt:formatDate value="${report.checkDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
							&nbsp;&nbsp;&nbsp;&nbsp;
							<font color=black><b>审批人：</b></font>${report.operator.person.personName}
						</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 50px;" valign="top">
						<td colspan=3>
							<font color=black><b>意见：</b></font>
							<br/><br/>
							<b>
								<c:if test="${report.isPassed == 0}"><font color=red>不通过</font></c:if>
								<c:if test="${report.isPassed == 1}"><font color=blue>通过</font></c:if>
							</b>
							<br/><br/>
							${report.managerAdvice}
							<br/><br/>
						</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 50px;" valign="top">
						<td colspan=3>
							<font color=black><b>打分：${report.score}</b></font>
							<font color=gray>（
								<c:if test="${report.score == 1}">非常不满意</c:if>
								<c:if test="${report.score == 2}">不满意</c:if>
								<c:if test="${report.score == 3}">一般</c:if>
								<c:if test="${report.score == 4}">满意</c:if>
								<c:if test="${report.score == 5}">非常满意</c:if>
							）</font>
							<br/><br/>
						</td>
					</tr>
					
					<tr class="ui-widget-content jqgrow ui-row-ltr">
						<td class="ui-state-default jqgrid-rownum" colspan="4" style="height:10px;"></td>
					</tr>
				</c:when>
				
				<c:when test="${report.reportType == 4 && !empty report.delayDate}">
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 50px;" valign="middle">
						<td class="ui-state-default jqgrid-rownum" valign="middle" align="center">
							<b>&nbsp;延迟提交完成报告</b>
						</td>
						<td colspan=3>
							<font color=black><b>延迟截止时间：</b></font><fmt:formatDate value="${report.delayDate}" pattern="yyyy-MM-dd"/>
							&nbsp;&nbsp;&nbsp;&nbsp;
							<font color=black><b>设置人：</b></font>${report.operator.person.personName}
						</td>
					</tr>
					
					<tr class="ui-widget-content jqgrow ui-row-ltr">
						<td class="ui-state-default jqgrid-rownum" colspan="4" style="height:20px;">以下为延期工作报告</td>
					</tr>
				</c:when>
			</c:choose>
			
		</c:forEach>
	</tbody>		
                  
