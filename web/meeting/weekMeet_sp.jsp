<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>
<head>
	<style type="text/css">
		body {
			padding-left: 10px;
			/*margin-top: 20px;*/
			padding-right: 10px;
			/*padding-bottom: 0px;	*/

		}
		::-webkit-scrollbar-track
		{
			background-color: #22394b;
			-webkit-box-shadow: inset 0 0 6px rgba(255, 123, 215, 0.22);
		}
		/*定义滚动条高宽及背景*/
		::-webkit-scrollbar
		{
			width: 5px;
			height: 200px;
			background-color: rgba(246, 68, 58, 0.34);
		}
		/*定义滚动条*/
		::-webkit-scrollbar-thumb
		{
			background-color: #8b8b8b;
			border-radius: 10px;
		}
		*{font-size:13px}

		table{
			border-collapse:collapse;

		}
		td {border:1px solid #0DE8F5}
		.TotCatalogHead {font-family: "黑体"; font-size:35px; font-weight: bold;}
		@media print{
			.pageBreak {page-break-after:always;}
			.Noprn {display:none;}
		}
		.TotCatalog {font-family: "黑体"; font-size:25px; font-weight: lighter;}
		@media print{
			.pageBreak {page-break-after:always;}
			.Noprn {display:none;}
		}
	</style>

	<title>一周会议</title>
	<script type="text/javascript" src="/js/jquery-1.8.3.js"></script>

</head>




<body style="background-image:url('/img/bgIn.png');background-size: cover;border: 0 #0DE8F5 solid;color: white;overflow-y: auto" text="#000000" topmargin=0 leftmargin=0>


<table width="600" 0 border="0" align="center" cellpadding="0"
	   cellspacing="0">
	<tr>
		<table align="center"  height="50">
			<tr>
				<th height="50">
					&nbsp;
				</th>
			</tr>
			<tr>
				<th>
					<a href="<c:url value="/meeting"/>/meetInfor.do?method=viewIndex&sp=sp&n=${_N-1}" title="上个月"><img width="23" height="18" border="0" src="<c:url value="/images"/>/arrow_left.gif">
					</a>
					<span class="TotCatalogHead">一周会议安排</span>
					<a href="<c:url value="/meeting"/>/meetInfor.do?method=viewIndex&sp=sp&n=${_N+1}" title="下个月"><img width="25" height="20" border="0" src="<c:url value="/images"/>/arrow_right.gif">
					</a>
					<br>
					<span class="TotCatalog">(${_Start_Date}---${_End_Date})</span>
				</th>
			</tr>
		</table>
	</tr>
	<tr>
		<table width="100%" align="center" border="1"  cellpadding="4" cellspacing="1">

			<tr align="center" height="35">
				<td width="5%" align="center">

				</td>


				<td width="15%" align="center">星期一

				</td>

				<td width="15%" align="center">星期二

				</td>
				<td width="15%" align="center">星期三

				</td>
				<td width="15%" align="center">星期四

				</td>
				<td width="15%" align="center">星期五

				</td>
				<td width="15%" align="center">星期六

				</td>
				<td width="15%" align="center">星期日

				</td>



			</tr>




			<tr height="50">


				<td align="center">
					上午
				</td>

				<td align="left" >
					<c:forEach items="${requestScope._Meets}" var="meet" varStatus="status">


						<c:if test='${meet.startHour <12 && _WeekList[status.index]=="星期一"}'>会议时间：<font color=RED>${meet.meetDate} 上午&nbsp;${meet.startHour}:${meet.startMinutes}<c:if test="${meet.startMinutes ==0}">0</c:if></font>
							</br>会议名称：${meet.meetName}</br>
							参加对象：<font color=#0DE8F5>${meet.attendInfor}</font></br>
							召集部门：${meet.organize.organizeName}</br>
							会议地点：${meet.meetRoom}</br></br>

						</c:if>

					</c:forEach>
				</td>

				<td align="left" >
					<c:forEach items="${requestScope._Meets}" var="meet" varStatus="status">
						<c:if test="${meet.startHour <12 && _WeekList[status.index]=='星期二'}">会议时间：<font color=RED>${meet.meetDate} 上午&nbsp;${meet.startHour}:${meet.startMinutes}<c:if test="${meet.startMinutes ==0}">0</c:if></font>
							</br>会议名称：${meet.meetName}</br>
							参加对象：<font color=#0DE8F5>${meet.attendInfor}</font></br>
							召集部门：${meet.organize.organizeName}</br>
							会议地点：${meet.meetRoom}</br></br>

						</c:if>
					</c:forEach>
				</td>

				<td align="left" >
					<c:forEach items="${requestScope._Meets}" var="meet" varStatus="status">
						<c:if test="${meet.startHour <12 && _WeekList[status.index]=='星期三' }">会议时间：<font color=RED>${meet.meetDate} 上午&nbsp;${meet.startHour}:${meet.startMinutes}<c:if test="${meet.startMinutes ==0}">0</c:if></font>
							</br>会议名称：${meet.meetName}</br>
							参加对象：<font color=0DE8F5>${meet.attendInfor}</font></br>
							召集部门：${meet.organize.organizeName}</br>
							会议地点：${meet.meetRoom}</br></br>

						</c:if>
					</c:forEach>
				</td>

				<td align="left" >
					<c:forEach items="${requestScope._Meets}" var="meet" varStatus="status">
						<c:if test="${meet.startHour <12 && _WeekList[status.index]=='星期四' }">会议时间：<font color=RED>${meet.meetDate} 上午&nbsp;${meet.startHour}:${meet.startMinutes}<c:if test="${meet.startMinutes ==0}">0</c:if></font>
							</br>会议名称：${meet.meetName}</br>
							参加对象：<font color=0DE8F5>${meet.attendInfor}</font></br>
							召集部门：${meet.organize.organizeName}</br>
							会议地点：${meet.meetRoom}</br></br>

						</c:if>
					</c:forEach>
				</td>

				<td align="left" >
					<c:forEach items="${requestScope._Meets}" var="meet" varStatus="status">
						<c:if test="${meet.startHour <12 && _WeekList[status.index]=='星期五'}">会议时间：<font color=RED>${meet.meetDate} 上午&nbsp;${meet.startHour}:${meet.startMinutes}<c:if test="${meet.startMinutes ==0}">0</c:if></font>
							</br>会议名称：${meet.meetName}</br>
							参加对象：<font color=0DE8F5>${meet.attendInfor}</font></br>
							召集部门：${meet.organize.organizeName}</br>
							会议地点：${meet.meetRoom}</br></br>

						</c:if>
					</c:forEach>
				</td>

				<td align="left" >
					<c:forEach items="${requestScope._Meets}" var="meet" varStatus="status">
						<c:if test="${meet.startHour <12 && _WeekList[status.index]=='星期六'}">会议时间：<font color=RED>${meet.meetDate} 上午&nbsp;${meet.startHour}:${meet.startMinutes}<c:if test="${meet.startMinutes ==0}">0</c:if></font>
							</br>会议名称：${meet.meetName}</br>
							参加对象：<font color=0DE8F5>${meet.attendInfor}</font></br>
							召集部门：${meet.organize.organizeName}</br>
							会议地点：${meet.meetRoom}</br></br>

						</c:if>
					</c:forEach>
				</td>

				<td align="left" >
					<c:forEach items="${requestScope._Meets}" var="meet" varStatus="status">
						<c:if test="${meet.startHour <12 && _WeekList[status.index]=='星期日'}">会议时间: <font color=RED>${meet.meetDate} ：上午&nbsp;${meet.startHour}:${meet.startMinutes}<c:if test="${meet.startMinutes ==0}">0</c:if></font>
							</br>会议名称：${meet.meetName}</br>
							参加对象：<font color=0DE8F5>${meet.attendInfor}</font></br>
							召集部门：${meet.organize.organizeName}</br>
							会议地点：${meet.meetRoom}</br></br>

						</c:if>
					</c:forEach>
				</td>



			</tr>
			<tr height="50">
				<td align="center">
					下午
				</td>

				<td align="left">
					<c:forEach items="${requestScope._Meets}" var="meet" varStatus="status">
						<c:if test='${meet.startHour >=12 && _WeekList[status.index]=="星期一"}'>会议时间：<font color=RED>${meet.meetDate} 下午&nbsp;${meet.startHour}:${meet.startMinutes}<c:if test="${meet.startMinutes ==0}">0</c:if></font>
							</br>会议名称：${meet.meetName}</br>
							参加对象：<font color=0DE8F5>${meet.attendInfor}</font></br>
							召集部门：${meet.organize.organizeName}</br>
							会议地点：${meet.meetRoom}</br></br>
						</c:if>

					</c:forEach>
				</td>

				<td align="left">
					<c:forEach items="${requestScope._Meets}" var="meet" varStatus="status">
						<c:if test="${meet.startHour >=12 && _WeekList[status.index]=='星期二'}">会议时间：<font color=RED>${meet.meetDate} 下午&nbsp;${meet.startHour}:${meet.startMinutes}<c:if test="${meet.startMinutes ==0}">0</c:if></font>
							</br>会议名称：${meet.meetName}</br>
							参加对象：<font color=0DE8F5>${meet.attendInfor}</font></br>
							召集部门：${meet.organize.organizeName}</br>
							会议地点：${meet.meetRoom}</br></br>
						</c:if>
					</c:forEach>
				</td>

				<td align="left">
					<c:forEach items="${requestScope._Meets}" var="meet" varStatus="status">
						<c:if test="${meet.startHour >=12 && _WeekList[status.index]=='星期三'}">会议时间：<font color=RED>${meet.meetDate} 下午&nbsp;${meet.startHour}:${meet.startMinutes}<c:if test="${meet.startMinutes ==0}">0</c:if></font>
							</br>会议名称：${meet.meetName}</br>
							参加对象：<font color=0DE8F5>${meet.attendInfor}</font></br>
							召集部门：${meet.organize.organizeName}</br>
							会议地点：${meet.meetRoom}</br></br>
						</c:if>
					</c:forEach>
				</td>

				<td align="left">
					<c:forEach items="${requestScope._Meets}" var="meet" varStatus="status">
						<c:if test="${meet.startHour >=12 && _WeekList[status.index]=='星期四'}">会议时间：<font color=RED>${meet.meetDate} 下午&nbsp;${meet.startHour}:${meet.startMinutes}<c:if test="${meet.startMinutes ==0}">0</c:if></font>
							</br>会议名称：${meet.meetName}</br>
							参加对象：<font color=0DE8F5>${meet.attendInfor}</font></br>
							召集部门：${meet.organize.organizeName}</br>
							会议地点：${meet.meetRoom}</br></br>
						</c:if>
					</c:forEach>
				</td>

				<td align="left">
					<c:forEach items="${requestScope._Meets}" var="meet" varStatus="status">
						<c:if test="${meet.startHour >=12 && _WeekList[status.index]=='星期五'}">会议时间：<font color=RED>${meet.meetDate} 下午&nbsp;${meet.startHour}:${meet.startMinutes}<c:if test="${meet.startMinutes ==0}">0</c:if></font>
							</br>会议名称：${meet.meetName}</br>
							参加对象：<font color=0DE8F5>${meet.attendInfor}</font></br>
							召集部门：${meet.organize.organizeName}</br>
							会议地点：${meet.meetRoom}</br></br>
						</c:if>
					</c:forEach>
				</td>

				<td align="left">
					<c:forEach items="${requestScope._Meets}" var="meet" varStatus="status">
						<c:if test="${meet.startHour >=12 && _WeekList[status.index]=='星期六'}">会议时间：<font color=RED>${meet.meetDate} 下午&nbsp;${meet.startHour}:${meet.startMinutes}<c:if test="${meet.startMinutes ==0}">0</c:if></font>
							</br>会议名称：${meet.meetName}</br>
							参加对象：<font color=0DE8F5>${meet.attendInfor}</font></br>
							召集部门：${meet.organize.organizeName}</br>
							会议地点：${meet.meetRoom}</br></br>
						</c:if>
					</c:forEach>
				</td>


				<td align="left">
					<c:forEach items="${requestScope._Meets}" var="meet" varStatus="status">
						<c:if test="${meet.startHour >=12 && _WeekList[status.index]=='星期日'}">会议时间：<font color=RED>${meet.meetDate} 下午&nbsp;${meet.startHour}:${meet.startMinutes}<c:if test="${meet.startMinutes ==0}">0</c:if></font>
							</br>会议名称：${meet.meetName}</br>
							参加对象：<font color=0DE8F5>${meet.attendInfor}</font></br>
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
