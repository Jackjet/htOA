<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>
	<head>
		<style type="text/css">
		body {
			margin-left: 0px;
			margin-top: 0px;
			margin-right: 0px;
			margin-bottom: 0px;	
			
		}
		*{font-size:15px}

		table{
			border-collapse:collapse;
			border-color: black;
		}
		td {border:1px solid;}
		.TotCatalogHead {font-family: "新宋体"; font-size:35px; color: black; font-weight: bold;}
			@media print{ 
			.pageBreak {page-break-after:always;}
			.Noprn {display:none;} 
			} 
		.TotCatalog {font-family: "新宋体"; font-size:25px; color: black; font-weight: lighter;}
			@media print{ 
			.pageBreak {page-break-after:always;}
			.Noprn {display:none;} 
			} 
		</style>
		
		<title>一周会议</title>
	</head>
	

	<body text="#000000" bgcolor="#FFFFFF" topmargin=0 leftmargin=0>


		<table width="600" 0 border="0" align="center" cellpadding="0"
			cellspacing="0">

			<tr>
				<table align="center" height="50">
					<tr>
						<th height="50">
							&nbsp;
						</th>
					</tr>
					<tr>
						<th>
							<span class="TotCatalogHead">一周会议安排</span>
							<br>
							<span class="TotCatalog">(${_Start_Date}---${_End_Date})</span>
						</th>
					</tr>
				</table>
			</tr>
			<tr>
				<table width="90%" align="center" border="1" cellpadding="4" cellspacing="1">

					<tr align="center" height="35">
						<td width="10%" align="center">
							日期
						</td>
						

						<td width="45%" align="center">上午
							
						</td>

						<td width="45%" align="center">下午
							
						</td>
						


					</tr>




						<tr height="50">
						
						
							<td align="center">
								星期一
							</td>
							
							<td align="left" >
							<c:forEach items="${requestScope._Meets}" var="meet" varStatus="status">
							
							
								<c:if test='${meet.startHour <12 && _WeekList[status.index]=="星期一"}'>会议时间：<font color=RED>${meet.meetDate} 上午&nbsp;${meet.startHour}:${meet.startMinutes}<c:if test="${meet.startMinutes ==0}">0</c:if></font>
									</br>会议名称：${meet.meetName}</br>
									参加对象：<font color=blue>${meet.attendInfor}</font></br>
									召集部门：${meet.organize.organizeName}</br>
									会议地点：${meet.meetRoom}</br></br>
									
								</c:if>
							</c:forEach>
							</td>

							<td align="left">
							<c:forEach items="${requestScope._Meets}" var="meet" varStatus="status">
							<c:if test='${meet.startHour >=12 && _WeekList[status.index]=="星期一"}'>会议时间：<font color=RED>${meet.meetDate} 下午&nbsp;${meet.startHour}:${meet.startMinutes}<c:if test="${meet.startMinutes ==0}">0</c:if></font>
									</br>会议名称：${meet.meetName}</br>
									参加对象：<font color=blue>${meet.attendInfor}</font></br>
									召集部门：${meet.organize.organizeName}</br>
									会议地点：${meet.meetRoom}</br></br>
								</c:if>
								
							</c:forEach>
							</td>
							</tr>
							<tr height="50">
							<td align="center">
								星期二
							</td>
							
							<td align="left" >
							<c:forEach items="${requestScope._Meets}" var="meet" varStatus="status">
								<c:if test="${meet.startHour <12 && _WeekList[status.index]=='星期二'}">会议时间：<font color=RED>${meet.meetDate} 上午&nbsp;${meet.startHour}:${meet.startMinutes}<c:if test="${meet.startMinutes ==0}">0</c:if></font>
									</br>会议名称：${meet.meetName}</br>
									参加对象：<font color=blue>${meet.attendInfor}</font></br>
									召集部门：${meet.organize.organizeName}</br>
									会议地点：${meet.meetRoom}</br></br>
									
								</c:if>
							</c:forEach>
							</td>

							<td align="left">
							<c:forEach items="${requestScope._Meets}" var="meet" varStatus="status">
							<c:if test="${meet.startHour >=12 && _WeekList[status.index]=='星期二'}">会议时间：<font color=RED>${meet.meetDate} 下午&nbsp;${meet.startHour}:${meet.startMinutes}<c:if test="${meet.startMinutes ==0}">0</c:if></font>
									</br>会议名称：${meet.meetName}</br>
									参加对象：<font color=blue>${meet.attendInfor}</font></br>
									召集部门：${meet.organize.organizeName}</br>
									会议地点：${meet.meetRoom}</br></br>
								</c:if>
							</c:forEach>
							</td>
							</tr>
							<tr height="50">
							<td align="center">
								星期三
							</td>
							
							<td align="left" >
							<c:forEach items="${requestScope._Meets}" var="meet" varStatus="status">
								<c:if test="${meet.startHour <12 && _WeekList[status.index]=='星期三' }">会议时间：<font color=RED>${meet.meetDate} 上午&nbsp;${meet.startHour}:${meet.startMinutes}<c:if test="${meet.startMinutes ==0}">0</c:if></font>
									</br>会议名称：${meet.meetName}</br>
									参加对象：<font color=blue>${meet.attendInfor}</font></br>
									召集部门：${meet.organize.organizeName}</br>
									会议地点：${meet.meetRoom}</br></br>
									
								</c:if>
							</c:forEach>
							</td>

							<td align="left">
							<c:forEach items="${requestScope._Meets}" var="meet" varStatus="status">
							<c:if test="${meet.startHour >=12 && _WeekList[status.index]=='星期三'}">会议时间：<font color=RED>${meet.meetDate} 下午&nbsp;${meet.startHour}:${meet.startMinutes}<c:if test="${meet.startMinutes ==0}">0</c:if></font>
									</br>会议名称：${meet.meetName}</br>
									参加对象：<font color=blue>${meet.attendInfor}</font></br>
									召集部门：${meet.organize.organizeName}</br>
									会议地点：${meet.meetRoom}</br></br>
								</c:if>
							</c:forEach>
							</td>
							</tr>
							<tr height="50">
							<td align="center">
								星期四
							</td>
							
							<td align="left" >
							<c:forEach items="${requestScope._Meets}" var="meet" varStatus="status">
								<c:if test="${meet.startHour <12 && _WeekList[status.index]=='星期四' }">会议时间：<font color=RED>${meet.meetDate} 上午&nbsp;${meet.startHour}:${meet.startMinutes}<c:if test="${meet.startMinutes ==0}">0</c:if></font>
									</br>会议名称：${meet.meetName}</br>
									参加对象：<font color=blue>${meet.attendInfor}</font></br>
									召集部门：${meet.organize.organizeName}</br>
									会议地点：${meet.meetRoom}</br></br>
									
								</c:if>
							</c:forEach>
							</td>

							<td align="left">
							<c:forEach items="${requestScope._Meets}" var="meet" varStatus="status">
							<c:if test="${meet.startHour >=12 && _WeekList[status.index]=='星期四'}">会议时间：<font color=RED>${meet.meetDate} 下午&nbsp;${meet.startHour}:${meet.startMinutes}<c:if test="${meet.startMinutes ==0}">0</c:if></font>
									</br>会议名称：${meet.meetName}</br>
									参加对象：<font color=blue>${meet.attendInfor}</font></br>
									召集部门：${meet.organize.organizeName}</br>
									会议地点：${meet.meetRoom}</br></br>
								</c:if>
							</c:forEach>
							</td>
							</tr>
							<tr height="50">
							<td align="center">
								星期五
							</td>
							
							<td align="left" >
							<c:forEach items="${requestScope._Meets}" var="meet" varStatus="status">
								<c:if test="${meet.startHour <12 && _WeekList[status.index]=='星期五'}">会议时间：<font color=RED>${meet.meetDate} 上午&nbsp;${meet.startHour}:${meet.startMinutes}<c:if test="${meet.startMinutes ==0}">0</c:if></font>
									</br>会议名称：${meet.meetName}</br>
									参加对象：<font color=blue>${meet.attendInfor}</font></br>
									召集部门：${meet.organize.organizeName}</br>
									会议地点：${meet.meetRoom}</br></br>
									
								</c:if>
							</c:forEach>
							</td>

							<td align="left">
							<c:forEach items="${requestScope._Meets}" var="meet" varStatus="status">
							<c:if test="${meet.startHour >=12 && _WeekList[status.index]=='星期五'}">会议时间：<font color=RED>${meet.meetDate} 下午&nbsp;${meet.startHour}:${meet.startMinutes}<c:if test="${meet.startMinutes ==0}">0</c:if></font>
									</br>会议名称：${meet.meetName}</br>
									参加对象：<font color=blue>${meet.attendInfor}</font></br>
									召集部门：${meet.organize.organizeName}</br>
									会议地点：${meet.meetRoom}</br></br>
								</c:if>
							</c:forEach>
							</td>
							</tr>
							<tr height="50">
							<td align="center">
								星期六
							</td>
							
							<td align="left" >
							<c:forEach items="${requestScope._Meets}" var="meet" varStatus="status">
								<c:if test="${meet.startHour <12 && _WeekList[status.index]=='星期六'}">会议时间：<font color=RED>${meet.meetDate} 上午&nbsp;${meet.startHour}:${meet.startMinutes}<c:if test="${meet.startMinutes ==0}">0</c:if></font>
									</br>会议名称：${meet.meetName}</br>
									参加对象：<font color=blue>${meet.attendInfor}</font></br>
									召集部门：${meet.organize.organizeName}</br>
									会议地点：${meet.meetRoom}</br></br>
									
								</c:if>
							</c:forEach>
							</td>

							<td align="left">
							<c:forEach items="${requestScope._Meets}" var="meet" varStatus="status">
							<c:if test="${meet.startHour >=12 && _WeekList[status.index]=='星期六'}">会议时间：<font color=RED>${meet.meetDate} 下午&nbsp;${meet.startHour}:${meet.startMinutes}<c:if test="${meet.startMinutes ==0}">0</c:if></font>
									</br>会议名称：${meet.meetName}</br>
									参加对象：<font color=blue>${meet.attendInfor}</font></br>
									召集部门：${meet.organize.organizeName}</br>
									会议地点：${meet.meetRoom}</br></br>
								</c:if>
							</c:forEach>
							</td>
							</tr>
							<tr height="50">
							
							<td align="center">
								星期日
							</td>
							
							<td align="left" >
							<c:forEach items="${requestScope._Meets}" var="meet" varStatus="status">
								<c:if test="${meet.startHour <12 && _WeekList[status.index]=='星期日'}">会议时间: <font color=RED>${meet.meetDate} ：上午&nbsp;${meet.startHour}:${meet.startMinutes}<c:if test="${meet.startMinutes ==0}">0</c:if></font>
									</br>会议名称：${meet.meetName}</br>
									参加对象：<font color=blue>${meet.attendInfor}</font></br>
									召集部门：${meet.organize.organizeName}</br>
									会议地点：${meet.meetRoom}</br></br>
									
								</c:if>
							</c:forEach>
							</td>

							<td align="left">
							<c:forEach items="${requestScope._Meets}" var="meet" varStatus="status">
							<c:if test="${meet.startHour >=12 && _WeekList[status.index]=='星期日'}">会议时间：<font color=RED>${meet.meetDate} 下午&nbsp;${meet.startHour}:${meet.startMinutes}<c:if test="${meet.startMinutes ==0}">0</c:if></font>
									</br>会议名称：${meet.meetName}</br>
									参加对象：<font color=blue>${meet.attendInfor}</font></br>
									召集部门：${meet.organize.organizeName}</br>
									会议地点：${meet.meetRoom}</br></br>
								</c:if>
							</c:forEach>
							</td>
							
					


						</tr>
					



				</table>
			</tr>

			<tr>

				<td width="10" height="40">
					&nbsp;
				</td>

			</tr>

			<tr>
				<table width="90%" align="center" height="35" border="1"
					cellpadding="0" cellspacing="0">
					<tr>
						<td>
							&nbsp;备注：因有事不能到会者，请事先向会议主持人请假。
						</td>
					</tr>
				</table>
			</tr>
			<tr>

				<td width="10" height="40">
					&nbsp;
				</td>

			</tr>
			<tr>

				<table width="90%" align="center" height="50">

					<tr>
						<th align="right">
							${_Now_Date}
						</th>
					</tr>

				</table>
			</tr>
			<tr>

				<%--<table width="50%" align="center" height="50">

					<tr>
						<OBJECT id=WebBrowser
							classid=CLSID:8856F961-340A-11D0-A96B-00C04FD705A2 height=0
							width=0></OBJECT>
						<th  align="right">
							<a  class="TableBtn2" onclick="document.all.WebBrowser.ExecWB(7,1)"
								>打印</a>&nbsp;&nbsp;
							<a 
								onclick="javaScript:window.close();">关闭</a> 
						</th>

						
					</tr>

				</table>





			--%>
			</tr>
		</table>
	</body>
