<%@ page contentType="text/html; charset=GBK"%>
<%@ include file="/inc/taglibs.jsp"%>

<TABLE cellSpacing=0 cellPadding=0 width=100% border=0>
		<TBODY>
		<TR>
		<TD width="100%" height=28 vAlign=bottom background="../images/default_navbg1.gif">
		<TABLE cellSpacing=0 cellPadding=0 border=0>
			<TBODY>
				<TR>
							<TD width=30>&nbsp;</TD>
							<TD vAlign=bottom align=middle>
							<IMG height=23 src="<c:url value='/images'/>/nav_icon_01.gif" width=23 align=absMiddle border=0> 
							<A class=link_top_01 href="<c:url value='/'/>login.mdo" noWrap>个人办公</A>&nbsp;&nbsp;&nbsp;&nbsp; 
							</TD>
							
							<function:viewIf alias="common">
							<TD vAlign=bottom align=middle width=2>&nbsp;</TD>
							<TD vAlign=bottom align=middle>
							<IMG height=23 src="<c:url value='/images'/>/nav_icon_02.gif" width=23 align=absMiddle border=0> 
							<A class=link_top_01 href="<c:url value='/cms'/>/commonwork" noWrap>日常办公</A>&nbsp;&nbsp;&nbsp; </TD>
							</function:viewIf>
							
							
							<function:viewIf alias="submit">
							<TD vAlign=bottom align=middle width=2>&nbsp;</TD>
							<TD vAlign=bottom align=middle><IMG height=23 src="<c:url value='/images'/>/nav_icon_03a.gif" width=23 align=absMiddle border=0> 
							<A class=link_top_01 href="<c:url value='/submit'/>/reportDocument" noWrap>公文流转</A>&nbsp;&nbsp; </TD>
							</function:viewIf>
							
							<function:viewIf alias="document">
							<TD vAlign=bottom align=middle width=2>&nbsp;</TD>							
							<TD vAlign=bottom align=middle><IMG height=23 src="<c:url value='/images'/>/nav_icon_05a.gif" width=23 align=absMiddle border=0> 
							<A class=link_top_01 href="<c:url value='/document'/>/list" noWrap>文档大全</A>&nbsp;&nbsp;&nbsp; </TD>
							</function:viewIf>
							
							<TD vAlign=bottom align=middle width=2>&nbsp;</TD>							
							<TD vAlign=bottom align=middle bgColor=#cbeafc><IMG height=23 src="<c:url value='/images'/>/mail.gif" width=23 bgcolor="white" align=absMiddle border=0>
								<A class=link_top_02 href="<c:url value='/webmailhz'/>/mailFrame.jsp" noWrap>电子邮件</A>&nbsp;&nbsp;&nbsp; 
							</TD>							
							
							<function:viewIf alias="config">
							<TD vAlign=bottom align=middle width=2>&nbsp;</TD>							
							<TD vAlign=bottom align=middle><IMG height=23 src="<c:url value='/images'/>/nav_icon_04.gif" width=23 align=absMiddle border=0> 
							<A class=link_top_01 href="<c:url value='/base'/>/config" noWrap>系统维护</A>&nbsp;&nbsp;&nbsp;&nbsp; </TD>
							</function:viewIf>
							
							<TD vAlign=bottom align=middle width=2>&nbsp;</TD>
						</TR>
</TBODY></TABLE></TD></TR></TBODY></TABLE>

<!-- menu part end-->


