<%
		String tempStr1,tempStr2,tempStr3,tempStr4,tempStr;	
		if (intpage>1){
			if (intpage<maxpage){
				tempStr1="<a href='" + ActionFile + "&page=1" + "&oldpage=" + intpage  + queryString + "'>��ҳ</a>";
				tempStr2="<a href='" + ActionFile + "&page=" + (intpage-1) + "&oldpage=" + intpage + queryString + "'>ǰҳ</a>";
				tempStr3="<a href='" + ActionFile + "&page=" + (intpage+1) + "&oldpage=" + intpage +  queryString + "'>��ҳ</a>";
				tempStr4="<a href='" + ActionFile + "&page=" + maxpage + "&oldpage=" + intpage + queryString + "'>βҳ</a>";
			}else{
				tempStr1="<a href='" + ActionFile + "&page=1" + "&oldpage=" + intpage + queryString + "'>��ҳ</a>";
				tempStr2="<a href='" + ActionFile + "&page=" + (intpage-1) + "&oldpage=" + intpage +  queryString + "'>ǰҳ</a>";
				tempStr3="<font color=#999999>��ҳ</font>";
				tempStr4="<font color=#999999>βҳ</font>";
			}
		}else{
			if (intpage<maxpage){
				tempStr1="<font color=#999999>��ҳ</font>";
				tempStr2="<font color=#999999>ǰҳ</font>";
				tempStr3="<a href='" + ActionFile + "&page=" + (intpage+1) + "&oldpage=" + intpage + queryString + "'>��ҳ</a>";
				tempStr4="<a href='" + ActionFile + "&page=" + maxpage + "&oldpage=" + intpage + queryString + "'>βҳ</a>";
			}else{
				tempStr1="<font color=#999999>��ҳ</font>";
				tempStr2="<font color=#999999>ǰҳ</font>";
				tempStr3="<font color=#999999>��ҳ</font>";
				tempStr4="<font color=#999999>βҳ</font>";
			}
		}
		
%>

	<form name="form1" method="post" action="<%=ActionFile%>">
		<tr height=30 class="cword09" bgcolor="#FFFfff">
			<td width="100%" align=right>
				&nbsp;&nbsp;&nbsp;&nbsp;<%=tempStr1%>&nbsp;|&nbsp;<%=tempStr2%>&nbsp;|&nbsp;<%=tempStr3%>&nbsp;|&nbsp;<%=tempStr4%>&nbsp;&nbsp;&nbsp;&nbsp;ҳ�Σ�<b><%=intpage%>/<%=maxpage%></b>ҳ
				����<input type="text" name="page" size="3" maxlength="3" class="input">ҳ<input type="hidden" name="loadtime" value="1">
			</td>
		</tr>
	</form>
