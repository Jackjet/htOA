<%@ page contentType="text/html; charset=GBK"%>
<%@ include file="/inc/taglibs.jsp"%>
<%@ page language="java"
	import="com.kwchina.sms.dao.*,
			com.kwchina.sms.entity.*,
			org.springframework.web.context.WebApplicationContext,
			org.springframework.web.context.support.*,
			java.util.*" %>
	
<script src="js/cal.js"></script>
<script language="javaScript">
	//function doDelete(form){					
			//form.action='<c:url value="${'/attendance/personVacation.do'}"/>?method=batchDelete&inpages=${personVacationForm.inpages}';
			//form.submit();			
		//}
	//}	
</script>

<%
 	ServletContext context = request.getSession().getServletContext();
 	WebApplicationContext webContext = WebApplicationContextUtils.getWebApplicationContext(context);
 
 	//SystemUserManager userManager = (SystemUserManager) webContext.getBean("systemUserManager");  
 	SMSSendMessageDAO sMSSendMessageDAO = (SMSSendMessageDAO) webContext.getBean("SMSSendMessageDAOImpl");
%>

<html>
	<head>
		<title>Message List</title>
	</head>


	<link type="text/css" rel="stylesheet" href="<c:url value="/"/>sms/css/default_new.css">
	<link type="text/css" rel="stylesheet" href="/css/gundongtiao.css">
	<%--<link type="text/css" rel="stylesheet" href="/css/myTable.css">--%>

	<%
		//String strPersonId =(String)request.getParameter("personId");	
		String strIsSend = (String)request.getParameter("isSend");
	 %>
<body style="border:0px solid #0DE8F5;border-radius: 5px">
	<TABLE cellSpacing=0 cellPadding=2 width="100%" border=0>
	<tr>
		<td>
			<table width="100%" style="border:solid 0px rgba(113,255,214,0)" cellpadding="0" cellspacing="4"  <%--bordercolor="#004A80"--%>>
				<tr>
					<td>
						<form name="frm" action="messageQuery.jsp" method="post" style="margin-top:0px;margin-bottom:0px;">
							<select name="isSend">
								<option value="">-选择发送状态-</option>
								<option value="0" <%if (strIsSend!=null && strIsSend.equals("0")) {out.println("selected");} %>>等待发送</option>
								<option value="1" <%if (strIsSend!=null && strIsSend.equals("1")) {out.println("selected");} %>>已经发送</option>
								<option value="2" <%if (strIsSend!=null && strIsSend.equals("2")) {out.println("selected");} %>>发送失败</option>
							</select>
							<!--
							<input type="text" name="start" onclick="showcal(this)" readonly="true">
							<input type="text" name="end" onclick="showcal(this)" readonly="true">
							-->
							<input type="submit" value="查 询">
							<input type="button" value="删 除" onclick="listFrm.submit();return false;">
						</form>
					</td>
				</tr>
				<%	try { %>
				<tr>
					<td>
						<!---------     -------------->
						<form name="listFrm" action="deleteMessage.jsp" style="margin-top:0px;margin-bottom:0px;">
							<table class="main_table"  width="100%" cellpadding="4" cellspacing="0" border="1"  style="border: #00688f" <%--bgcolor="#666666"--%>>
								<tr align="center" valign="middle" style="border: solid 1px #bcff9e" <%--bgcolor="#cccccc"--%>>
									<td width="50" align="center">
										<input type="checkbox" name="checkall"
											   onclick="var checked=this.checked;for(i=0;i<this.form.elements.length;i++)
								{var e=this.form.elements[i];if (e.name=='toBeDeletedId'){if(e.disabled==true)continue;if(e.checked!=checked){e.click();}}} "/>
										序号
									</td>
									<td width="80" align="center" >
										发送人
									</td>
									<td width="80" align="center" >
										接收手机号
									</td>
									<td width="80">
										&nbsp;发送状态
									</td>
									<td align="center">
										发送时间
									</td>
									<td align="center">
										&nbsp;短消息内容
									</td>
								</tr>

								<%
									//获取条件
									String condition = " where 1=1";
									String queryString = " from SMSMessagesToSend messageToSend";
									//String strStart = (String)request.getParameter("start");
									//String strEnd = (String)request.getParameter("end");
									String ActionFile = "messageQuery.jsp?a=a";

									/**
									 if (strPersonId!=null && !strPersonId.equals("")){
									 ActionFile = ActionFile + "&personId=" + strPersonId;
									 condition = condition + " and personId=" + strPersonId;
									 }*/

									if (strIsSend!=null && !strIsSend.equals("")){
										ActionFile = ActionFile + "&isSend=" + strIsSend;
										condition = condition + " and messageToSend.transmitStatus=" + strIsSend;
									}
									/**
									 if (strStart!=null && !strStart.equals("")){
									 ActionFile = ActionFile + "&start=" + strStart;
									 condition = condition + " and messageTime>=" + strStart;
									 }
									 if (strEnd!=null && !strEnd.equals("")){
									 ActionFile = ActionFile + "&end=" + strEnd;
									 queryString = queryString + "&end=" + strEnd;
									 //加一天
									 condition = condition + " and  messageTime<=" + strEnd;
									 }*/
									queryString += condition;
									queryString += " order by messageToSend.messageId desc ";
									//out.print(condition);

									String newpage = request.getParameter("page");
									if (newpage == null) {
										newpage = "1";
									}
									int npage = Integer.parseInt(newpage);
									int pagesize = 20;
									int maxpage;


									List messagesLs = sMSSendMessageDAO.getResultByQueryString(queryString);
									//out.print(messagesLs);

									/**
									 for(Iterator it = messagesLs.iterator();it.hasNext();){
									 //Object obj = (Object)it.next();
									 //out.print(obj);

									 //SMSMessagesToSend toSend = (SMSMessagesToSend)obj;
									 //out.print("-----------------");
									 //out.println(toSend.getMessageText());
									 }*/


									Object[] messages = (Object[])messagesLs.toArray();
									//messageDAO.getMessageInfors(condition);

									if (messages.length % pagesize == 0) {
										maxpage = messages.length / pagesize;
									} else {
										maxpage = messages.length / pagesize + 1;
									}

									if (npage < 0) {
										npage = 1;
									} else if (npage > maxpage) {
										npage = maxpage;
									}
									int intpage = npage;

									int start = pagesize * (npage - 1);
									//System.out.println("start="+start);
									if (start < 0) {
										start = 0;
									}
									int end;
									if (messages.length > (start + pagesize)) {
										end = start + pagesize;
									} else {
										end = messages.length;
									}

									SMSMessagesToSend[] returnMessages = new SMSMessagesToSend[end - start];
									for (int i = start; i < end; i++) {
										returnMessages[i - start] = (SMSMessagesToSend)messages[i];
									}

									//out.println(persons.length);
									for (int k = 0; k < returnMessages.length; k++) {

										int isSend = returnMessages[k].getTransmitStatus();
								%>
								<tr <%--bgcolor="#cccccc"--%>>
									<td align="center">
										<input type="checkbox" name="toBeDeletedId"	value="<%=returnMessages[k].getMessageId()%>">
										<%=pagesize * (npage - 1) + k +1%>
									</td>
									<td align="center" style="word-break:break-all;word-wrap:break-word;">
										<%=returnMessages[k].getApplier()%>
									</td>
									<td align="center" style="word-break:break-all;word-wrap:break-word;">
										<%=returnMessages[k].getMobileNos()%>
									</td>
									<td align="center">
										<% if (isSend==0) {
											out.println("等待发送");
										}else if(isSend==1){
											out.println("发送成功");
										}else{
											out.println("发送失败");
										}
										%>
									</td>
									<td align="left">
										<%=returnMessages[k].getScheduleDate()%>
									</td>
									<td>
										<%=returnMessages[k].getMessageText()%>
									</td>
								</tr>
								<%
									}
								%>
							</table>
						</form>
						<!---------     -------------->
					</td>
				</tr>
				<% if (maxpage>1){
				%>
				<%
					String tempStr1,tempStr2,tempStr3,tempStr4,tempStr;
					if (intpage>1){
						if (intpage<maxpage){
							tempStr1="<a href='" + ActionFile + "&page=1" + "&oldpage=" + intpage  + queryString + "'>首页</a>";
							tempStr2="<a href='" + ActionFile + "&page=" + (intpage-1) + "&oldpage=" + intpage + queryString + "'>前页</a>";
							tempStr3="<a href='" + ActionFile + "&page=" + (intpage+1) + "&oldpage=" + intpage +  queryString + "'>后页</a>";
							tempStr4="<a href='" + ActionFile + "&page=" + maxpage + "&oldpage=" + intpage + queryString + "'>尾页</a>";
						}else{
							tempStr1="<a href='" + ActionFile + "&page=1" + "&oldpage=" + intpage + queryString + "'>首页</a>";
							tempStr2="<a href='" + ActionFile + "&page=" + (intpage-1) + "&oldpage=" + intpage +  queryString + "'>前页</a>";
							tempStr3="<font >后页</font>";
							tempStr4="<font >尾页</font>";
						}
					}else{
						if (intpage<maxpage){
							tempStr1="<font >首页</font>";
							tempStr2="<font >前页</font>";
							tempStr3="<a href='" + ActionFile + "&page=" + (intpage+1) + "&oldpage=" + intpage + queryString + "'>后页</a>";
							tempStr4="<a href='" + ActionFile + "&page=" + maxpage + "&oldpage=" + intpage + queryString + "'>尾页</a>";
						}else{
							tempStr1="<font >首页</font>";
							tempStr2="<font >前页</font>";
							tempStr3="<font >后页</font>";
							tempStr4="<font >尾页</font>";
						}
					}

				%>

				<form name="form1" method="post" action="<%=ActionFile%>">
					<tr height=30 class="cword09" <%--bgcolor="#FFFfff"--%>>
						<td width="100%" align=right>
							&nbsp;&nbsp;&nbsp;&nbsp;<%=tempStr1%>&nbsp;|&nbsp;<%=tempStr2%>&nbsp;|&nbsp;<%=tempStr3%>&nbsp;|&nbsp;<%=tempStr4%>&nbsp;&nbsp;&nbsp;&nbsp;页次：<b><%=intpage%>/<%=maxpage%></b>页
							到第<input type="text" name="page" size="3" maxlength="3" class="input">页<input type="hidden" name="loadtime" value="1">
						</td>
					</tr>
				</form>


				<%}
				} catch (Exception ex) {
					out.println(ex.toString());
				}
				%>
				<tr>
					<td class="SpaceDotLIne"></td>
				</tr>
			</table>

		</td>
	</tr>
</TABLE>
</body>
