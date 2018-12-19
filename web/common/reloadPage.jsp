<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

		<style>
			li {
				list-style-type:square;
				list-style-position:inside;
			}
		</style>
		
		<table width=100% style="padding-left:5px">
			<tr><td>
				${_ErrorMessage!""}
			</td></tr>
		</table>
		<br><br>
		
		<input type="button" class="btn29" value=" 返回 " onclick="window.history.go(-1)">