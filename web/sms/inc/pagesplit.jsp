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
				tempStr3="<font color=#999999>后页</font>";
				tempStr4="<font color=#999999>尾页</font>";
			}
		}else{
			if (intpage<maxpage){
				tempStr1="<font color=#999999>首页</font>";
				tempStr2="<font color=#999999>前页</font>";
				tempStr3="<a href='" + ActionFile + "&page=" + (intpage+1) + "&oldpage=" + intpage + queryString + "'>后页</a>";
				tempStr4="<a href='" + ActionFile + "&page=" + maxpage + "&oldpage=" + intpage + queryString + "'>尾页</a>";
			}else{
				tempStr1="<font color=#999999>首页</font>";
				tempStr2="<font color=#999999>前页</font>";
				tempStr3="<font color=#999999>后页</font>";
				tempStr4="<font color=#999999>尾页</font>";
			}
		}
		
%>

	<form name="form1" method="post" action="<%=ActionFile%>">
		<tr height=30 class="cword09" bgcolor="#FFFfff">
			<td width="100%" align=right>
				&nbsp;&nbsp;&nbsp;&nbsp;<%=tempStr1%>&nbsp;|&nbsp;<%=tempStr2%>&nbsp;|&nbsp;<%=tempStr3%>&nbsp;|&nbsp;<%=tempStr4%>&nbsp;&nbsp;&nbsp;&nbsp;页次：<b><%=intpage%>/<%=maxpage%></b>页
				到第<input type="text" name="page" size="3" maxlength="3" class="input">页<input type="hidden" name="loadtime" value="1">
			</td>
		</tr>
	</form>
