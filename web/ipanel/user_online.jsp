<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>
<html><head>
<link href="<c:url value="/css/theme/7/style.css"/>" type="text/css" rel="stylesheet">
<link href="<c:url value="/css/theme/7/menu_left.css"/>" type="text/css" rel="stylesheet">
<link href="<c:url value="/css/theme/7/treeview.css"/>" type="text/css" rel="stylesheet">
<script src="<c:url value="/inc/ccorrect_btn.js"/>"></script>
<script src="<c:url value="/inc/js/treeview.js"/>" language="javascript"></script>
<script language="JavaScript">
function setCookie(name,value) {
   var today = new Date();
   var expires = new Date();
   expires.setTime(today.getTime() + 1000*60*60*24*1000);
   document.cookie = name + "=" + escape(value) + "; expires=" + expires.toGMTString();
}
</script>
</head><body leftmargin="0" topmargin="0" class="bodycolor">
<div id="body">
<ul>
   <li><a id="link_2" onclick="setCookie('ONLINE_UI','1')" href="user_online.php?VIEW_ALL=0"><span>显示在线人员(树型列表)</span></a></li>
   <li><a href="user_all.php?VIEW_ALL=1&amp;UI="><span>显示全部</span></a></li>
</ul>
</div>
<div id="bottom"></div>
<script language="JavaScript">
function clickMenu(ID)
{
    targetelement=document.all(ID);
    if (targetelement.style.display=="none")
        targetelement.style.display='';
    else
        targetelement.style.display="none";
}

window.setTimeout('this.location.reload();',120000);

function killErrors()
{
  return true;
}
window.onerror = killErrors;

var D = new Array(new Array());

D[0]=["1","销售部","销售部",[]];
D[0][3][0]=["wxg","王希国","U01","","部门经理","","",""];
D[1]=["3","财务部","财务部",[]];
D[1][3][0]=["cw","徐财务","U01","","财务主管","010-5166610000","",""];

InitUserList();

function InitUserList()
{
  if(2==0)
  {
     document.write("&lt;center&gt;尚未定义部门，&lt;br&gt;无法显示人员列表&lt;/center&gt;");
     return;
  }

  var STR="";
  /**
  for(i=0;i<2;i++)
  {
     STR+="&lt;table class=TableBlock width=99% cellpadding=3&gt;";
	 if(i==0)
	  {
	 STR+="&lt;tr class=TableHeader onclick='list_expand()' style=cursor:hand&gt;";
	 STR+="&lt;td align=center id='expand_text'&gt;全部收缩&lt;/td&gt;&lt;/tr&gt;";
	  }
     STR+="&lt;tr class=TableHeader onclick=clickMenu('"+ D[i][0] +"') style=cursor:hand&gt;";
     STR+="&lt;td align=center title='"+ D[i][2] +"'&gt;&lt;b&gt;"+ D[i][1]+"&lt;/b&gt;&lt;/td&gt;&lt;/tr&gt;&lt;/table&gt;";
     STR+="&lt;table cellpadding=3 width=99% id="+ D[i][0] +" class=TableBlock&gt;";

     for(j=0;j<D[i][3].length;j++)
     {
       STR+="&lt;tr class=TableData align=center title=\"部门:"+ D[i][1]+"\n角色:"+D[i][3][j][4]+"\n电话:"+D[i][3][j][5]+"\nEMAIL:"+D[i][3][j][6]+"\nQQ:"+D[i][3][j][7]+"\n最后登陆IP:"+D[i][3][j][3]+"\"&gt;";
       STATUS_STR="&lt;img src=/images/user_list/"+D[i][3][j][2]+".gif align=absmiddle&gt;";
       STR+="&lt;td width=25&gt;"+ STATUS_STR +"&lt;/td&gt;";
       STR+="&lt;td&gt;&lt;a href=javascript:view_user(\""+ D[i][3][j][0]+"\")&gt;"+ D[i][3][j][1] +"&lt;/a&gt;&lt;/td&gt;";
       STR+="&lt;td&gt;&lt;a href=javascript:parent.send_sms('"+ D[i][3][j][0] +"','"+ D[i][3][j][1] +"')&gt;短信&lt;/a&gt;&amp;nbsp;";
       STR+="&lt;a href=javascript:parent.send_email('"+ D[i][3][j][0] +"','"+ D[i][3][j][1] +"')&gt;邮件&lt;/a&gt;&lt;/td&gt;&lt;/tr&gt;";
     }
     STR+="&lt;/table&gt;";
  }
	*/
	
  document.write(STR);
}

var menu_flag=0;
function list_expand()
{
   for(i=0;i<D.length;i++)
   {
      var online_user=document.getElementById(D[i][0]);
      var expand_text=document.getElementById("expand_text");
      if(!online_user||!expand_text)
         continue;
      if (menu_flag==1)
      {
         online_user.style.display='';
         expand_text.innerHTML="全部收缩";
      }
      else
      {
         online_user.style.display='none';
         expand_text.innerHTML="全部展开";
      }
   }
   menu_flag=1-menu_flag;
}
parent.parent.status_bar.document.getElementById("user_count1").value='2';
//window.onerror = killErrors;
function view_user(USER_ID)
{
   parent.openURL("/general/info/user/user.php?USER_ID="+USER_ID+"&amp;WINDOW=1",1);
}
</script>


<table cellpadding="3" width="99%" class="TableBlock">
	<tbody>
		<tr style="" onclick="list_expand()" class="TableHeader">
			<td align="center" id="expand_text">全部收缩</td>
		</tr>
		
		<tr style="" onclick="clickMenu('1')" class="TableHeader">
			<td align="center" title="销售部"><b>销售部</b></td>
		</tr>
	</tbody>
</table>

<table cellpadding="3" width="99%" class="TableBlock" id="1">
	<tbody>
		<tr align="center" title="部门:销售部
			角色:部门经理
			电话:
			EMAIL:
			QQ:
			最后登陆IP:" class="TableData">
			
			<td width="25">
					<img align="absmiddle" src="../images/user_list/U01.gif">
			</td>
			
			<td><a href="javascript:view_user(&quot;wxg&quot;)">王希国</a></td>
			
			<td><a href="javascript:parent.send_sms('wxg','王希国')">短信</a>&nbsp;<a href="javascript:parent.send_email('wxg','王希国')">邮件</a></td>
		</tr>
	</tbody>
</table>

<table cellpadding="3" width="99%" class="TableBlock">
	<tbody>
		<tr style="" onclick="clickMenu('3')" class="TableHeader">
			<td align="center" title="财务部"><b>财务部</b></td>
		</tr>
	</tbody>
</table>

<table cellpadding="3" width="99%" class="TableBlock" id="3">
	<tbody>
		<tr align="center" title="部门:财务部
			角色:财务主管
			电话:010-5166610000
			EMAIL:
			QQ:
			最后登陆IP:" class="TableData">
			<td width="25"><img align="absmiddle" src="/images/user_list/U01.gif"></td>
			<td><a href="javascript:view_user(&quot;cw&quot;)">徐财务</a></td>
			<td><a href="javascript:parent.send_sms('cw','徐财务')">短信</a>&nbsp;<a href="javascript:parent.send_email('cw','徐财务')">邮件</a></td>
		</tr>
	</tbody>
</table>


</body></html>