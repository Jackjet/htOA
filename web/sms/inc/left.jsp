<%@ page contentType="text/html; charset=GBK"%>
<%@ include file="../inc/taglibs.jsp"%>


<html>
<head>
<title></title>
<link rel="stylesheet" href="../css/default.css" type="text/css">
</head>
<style type="text/css">
  .ttl { color: #FFFFFF; padding-top: 4px; cursor: hand; }
</style>
<script language="javascript">
  function showHide(obj){
    var oStyle = obj.parentElement.parentElement.parentElement.rows[1].style;
    oStyle.display == "none" ? oStyle.display = "block" : oStyle.display = "none";
  }
</script>

<BODY 
style="BACKGROUND-REPEAT: repeat-x; BACKGROUND-COLOR: #4296ce" 
leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<SCRIPT language=JavaScript type=text/javascript>
<!--
    var r=Math.random();
    document.write("<img alt=\"\" width=\"1\" height=\"1\" style=\"display:none\" ");
    document.write("src=\"http://dmtracking.alibaba.com/b.jpg?cD0yJnU9ey9jaGluYS5hbGliYWJhLmNvbS9iaXpleHByZXNzL215X3NlYXJjaGVyLmh0bX0mbT17R0VUfSZzPXsyMDB9JnI9ey19JmE9e2NfbWlkPWNyZWF0aW1jaGluYXxjX2xpZD1jcmVhdGltY2hpbmF8Y19tcz0xfSZiPXtjX3dfc2lnbmVkPVl9JmM9ey19&rand="+r+"\">");
-->
</SCRIPT>
<NOSCRIPT><IMG style="DISPLAY: none" height=1 alt="" src="images/b.jpg" 
width=1> </NOSCRIPT>

<br />	

 
	<table width="159" border="0" cellspacing="0" cellpadding="0" align="center">
    <tr>
      <td width="23"><img src="../images/box_topleft.gif" width="23" height="25" /></td>
      <td width="129" background='../images/box_topbg.gif' class="ttl" onclick="showHide(this)">��ز���</td>
      <td width="7"><img src="../images/box_topright.gif" width="7" height="25" /></td>
    </tr>
    <tr>
      <td style="padding: 3px; " colspan="3" background="../images/box_bg.gif">
        <table width="100%" >        
		  	
		    <tr><td>
				<img src="../images/arrow.gif" align="absmiddle" width="5" height="7" hspace="5">
		      	<a href='../personList.jsp' target='mainFrame'>Ա���б�</a>
		      	</td>
		    </tr>
		    <tr>
		    	<td height="20"></td>
		    </tr>
		    
		    <!-- 
		    <tr><td>
				<img src="../images/arrow.gif" align="absmiddle" width="5" height="7" hspace="5">
		      	<a href='../importSalary.jsp?salaryType=0' target='mainFrame'>�¶ȹ��ʵ���</a>
		      	</td>
		    </tr> 
		    <tr><td>
				<img src="../images/arrow.gif" align="absmiddle" width="5" height="7" hspace="5">
		      	<a href='../importSalary.jsp?salaryType=1' target='mainFrame'>һ���Խ�����</a>
		      	</td>
		    </tr> 
		     -->
		     
		     <tr><td>
				<img src="../images/arrow.gif" align="absmiddle" width="5" height="7" hspace="5">
		      	<a href='../addMessage.jsp' target='mainFrame'>��д����Ϣ</a>
		      	</td>
		    </tr> 
		    <tr>
		    	<td height="20"></td>
		    </tr>
		    
		    <!--  <tr><td>
				<img src="../images/arrow.gif" align="absmiddle" width="5" height="7" hspace="5">
		      	<a href='../messageQuery.jsp' target='mainFrame'>����Ϣ��ѯ</a>
		      	</td>
		    </tr>	 -->
		   	   
    	</table>
      </td>
    </tr>
    </tr>
    <tr>
      <td colspan="3"><img src="../images/box_bottom.gif" width="159" height="10" /></td>
    </tr>
  </table> 
 
 <!--
	<table width="159" border="0" cellspacing="0" cellpadding="0" align="center">
    <tr>
      <td width="23"><img src="../images/box_topleft.gif" width="23" height="25" /></td>
      <td width="129" background='../images/box_topbg.gif' class="ttl" onclick="showHide(this)">������Ա����</td>
      <td width="7"><img src="../images/box_topright.gif" width="7" height="25" /></td>
    </tr>
    <tr style="display:none">
      <td style="padding: 3px; " colspan="3" background="../images/box_bg.gif">
        <table width="100%" >
         	<tr><td>
				<img src="../images/arrow.gif" align="absmiddle" width="5" height="7" hspace="5">
		      	<a href='<c:url value="${'/yearcheck/yearCheckDefine.do'}"/>?method=edit' target='mainFrame'>��ӿ�����Ա����</a></td></tr>		
		    
		     <tr><td>
				<img src="../images/arrow.gif" align="absmiddle" width="5" height="7" hspace="5">
		      	<a href='<c:url value="${'/yearcheck/yearCheckDefine.do'}"/>?method=list&defineType=4' target='mainFrame'>һ�������Ա</a></td></tr>  
		      	
		  	<tr><td>
				<img src="../images/arrow.gif" align="absmiddle" width="5" height="7" hspace="5">
		      	<a href='<c:url value="${'/yearcheck/yearCheckDefine.do'}"/>?method=list&defineType=3' target='mainFrame'>���μ�������Ա</a></td>
		   	</tr>   
		   	
		   	 <tr><td>
				<img src="../images/arrow.gif" align="absmiddle" width="5" height="7" hspace="5">
		      	<a href='<c:url value="${'/yearcheck/yearCheckDefine.do'}"/>?method=list&defineType=2' target='mainFrame'>���ܼ�������Ա</a></td></tr>		   
		      	
    		<tr><td>
				<img src="../images/arrow.gif" align="absmiddle" width="5" height="7" hspace="5">				
		      	<a href='<c:url value="${'/yearcheck/yearCheckDefine.do'}"/>?method=list&defineType=1' target='mainFrame'>�߼�������Ա</a>		      	
		      	</td>		      
		    </tr>			  
		      	
		  <tr><td>
				<img src="../images/arrow.gif" align="absmiddle" width="5" height="7" hspace="5">
		      	<a href='<c:url value="${'/yearcheck/yearCheckDefine.do'}"/>?method=checklist' target='mainFrame'>
		      	<font color=red>��������Ա��ѯ</font>
		      	</a>
		  </td></tr>		
        </table>
      </td>
    </tr>
    </tr>
    <tr>
      <td colspan="3"><img src="../images/box_bottom.gif" width="159" height="10" /></td>
    </tr>
  </table>
  -->
  
  
 
</body>
</html>