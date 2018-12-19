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
	

	<body>


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
						<td width="80" align="center">
							时间
						</td>
						<td align="center">会议名称
							<bean:message bundle="meet" key="meet.meetName" />
						</td>

						<td width="30%" align="center">参加对象
							<bean:message bundle="meet" key="meet.attend" />
						</td>
						<td width="15%" align="center">
							召集部门

						</td>
						<td width="20%" align="center">
							会议地点
						</td>

					</tr>



					<c:forEach items="${requestScope._Meets}" var="meet"
						varStatus="status">
						<c:set var="cIndex" value="${cIndex+1}" />
						<tr height="50">

							<td align="center">
								${_DateList[status.index]}<br>${_WeekList[status.index]}
							</td>

							<td align="center" nowrap>
								<c:if test="${meet.startHour <12}">上午&nbsp;${meet.startHour}:${meet.startMinutes}<c:if test="${meet.startMinutes ==0}">0</c:if>
									
								</c:if>
								<c:if test="${meet.startHour >=12}">下午&nbsp;${meet.startHour-12}:${meet.startMinutes}<c:if test="${meet.startMinutes ==0}">0</c:if>
									
								</c:if>
							</td>

							<td align="center">
								${meet.meetName}
							</td>
							<td align="center">
								${meet.attendInfor}
							</td>

							<td align="center">
								${meet.organize.organizeName}
							</td>

							<td align="center">
								${meet.meetRoom}
							</td>


						</tr>
					</c:forEach>


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
