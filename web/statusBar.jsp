<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>
<html><head>
<link href="css/theme/7/style.css" type="text/css" rel="stylesheet">
<link href="css/theme/7/status_bar.css" type="text/css" rel="stylesheet">

<title>状态</title>
<style type="text/css">
	.mytrans { filter:revealTrans(Transition=12,Duration=2)}
</style>

<script src="inc/ccorrect_btn.js"></script>
<script src="inc/mytable.js"></script>
<script src="inc/marquee.js"></script>
<script language="JavaScript">
function killErrors()
{
  return true;
}
window.onerror = killErrors;

var ctroltime;

/**
function MyLoad()
{
  setTimeout("email_mon()",11000);
  ctroltime=setTimeout("sms_mon()",3000);
}

var xmlHttpObj=getXMLHttpObj();
function email_mon()
{
  var theURL="email_mon.php";
  xmlHttpObj.open("GET",theURL,true);
  var responseText="";
  xmlHttpObj.onreadystatechange=function()
  {
    if(xmlHttpObj.readyState==4)
    {
      responseText=xmlHttpObj.responseText;
      if(responseText=="1")
         document.getElementById("new_letter").innerHTML="&lt;a href='#' onclick='javascript:show_email();' style='color:#0d4cab; WIDTH: 80%;' title='点击查看邮件'&gt;新邮件&lt;/a&gt;";
      else
      	 document.getElementById("new_letter").innerHTML="";
    }
  }
  xmlHttpObj.send(null)
  setTimeout("email_mon()",900000);
}

function sms_mon()
{
  var theURL="sms_mon.php";
  xmlHttpObj.open("GET",theURL,true);
  var responseText="";
  xmlHttpObj.onreadystatechange=function()
  {
    if(xmlHttpObj.readyState==4)
    {
      responseText=xmlHttpObj.responseText;
      if(responseText=="S")
      {
         document.getElementById("new_sms").innerHTML="&lt;a href='#' onclick='javascript:show_sms();' style='color:#0d4cab; width:80%;' title='点击查看短信'&gt;&lt;img src='/images/sms1.gif'border=0 height=10&gt; 短信&lt;/a&gt;";
      }
      else
      	 document.getElementById("new_sms").innerHTML="";
    }
  }
  xmlHttpObj.send(null)
  ctroltime=setTimeout("sms_mon()",30000);
}

function show_sms()
{
   clearTimeout(ctroltime);
   ctroltime=window.setTimeout('sms_mon()',40000);

   mytop=screen.availHeight-190;
   myleft=0;
   d = new Date();
   window.open("sms_show.php","SMS_1","height=170,width=400,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,top="+mytop+",left="+myleft+",resizable=yes");
}

function show_email()
{
   document.getElementById("new_letter").innerHTML="";
   parent.table_index.main.location="/general/email/inbox?BOX_ID=0";
}

function show_online()
{
	
   if(parent.table_index.login_info.view_flag2==0)
   	parent.table_index.login_info.my_menu_view2('arrow2');
   parent.leftmenu.view_menu(2);
}

function main_refresh()
{
   parent.table_index.main.location.reload();
}

menu_flag=0;
var STATUS_BAR_MENU;

function show_menu()
{
   mytop=screen.availHeight-480;
   myleft=screen.availWidth-215;
   if(menu_flag==0)
       STATUS_BAR_MENU=window.open("/general/ipanel/menu.php?OA_SUB_WINDOW=1","STATUS_BAR_MENUwxg","height=400,width=200,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,top="+mytop+",left="+myleft+",resizable=no");

   STATUS_BAR_MENU.focus();
}

function MyUnload()
{
   if(menu_flag==1)
   {
     STATUS_BAR_MENU.focus();
     STATUS_BAR_MENU.MAIN_CLOSE=1;
     STATUS_BAR_MENU.close();
   }
}

function HelpArray(len)
{
	this.length=len;
}
　　
*/
var HelpText=new HelpArray(3);
HelpText[0]="Office Automation 2010";
HelpText[1]="努力打造中国OA第一品牌";
HelpText[2]="体验科技创新   共享美好未来";
ScriptText=new HelpArray(3);
var i= -1;

function playHelp()
{
	if (i==2)
	{ i=0;}
	else
	{ i++; }
	message1.filters[0].apply();
	message1.innerText=HelpText[i];
	message1.filters[0].play();
　　　　
	mytimeout=setTimeout("playHelp()",60000);
}

function MyLoad()
{
  setTimeout("email_mon()",11000);
  ctroltime=setTimeout("sms_mon()",3000);
   if(0==1)
	{
	   playHelp();
	}
}
</script>
</head>

<body marginwidth="0" marginheight="0" bgcolor="#f6f6f6" onunload="MyUnload();" onload="MyLoad();" leftmargin="0" topmargin="0" class="statusbar">

<table cellspacing="0" cellpadding="0" border="0" width="100%" bordercolordark="#264989" bordercolorlight="#264989" class="small">
  	<tbody>
	  <tr>
	    <td align="center" width="90">
	    	<!-- 
	       <a class="mytrans" style="width: 100%;" onclick="javascript:show_online();" href="#">
	        	共<input id="user_count1" value="12">人在线
	       </a>
	        -->
	    </td>
	    <td align="center" width="80">&nbsp;
	       <!-- 
	       <span id="new_sms">
	       		<a title="点击查看短信" style="color: rgb(13, 76, 171); width: 80%;" onclick="javascript:show_sms();" href="#">
	       		<img height="10" border="0" src="images/sms1.gif"> 短信</a>
	       	</span>
	       	 -->
	    </td>
	    <td align="center" class="mytrans" onclick="javascript:main_refresh();" style="font-weight: bold;font-size:12px" title="">
	      上海海通国际汽车码头有限公司
	      <!-- 
	      <script language="JavaScript">
	        new marquee('status_text');
	        status_text.init(new Array("上海慧智计算机技术有限公司 2010","Office Automation 2010","体验科技创新  共享美好未来"),60000);
	      </script>
	      <div style="overflow: hidden;" id="status_text">
	      	<div style="overflow: hidden; "></div>
	      </div> -->
		</td>
	    
	    <td align="center" width="80">&nbsp;
	       <span id="new_letter"></span>
	    </td>
	    <td align="center" width="75">&nbsp;
	       <!-- <a class="mytrans" style="width: 100%;" href="javascript:show_menu();">菜单</a> -->
	    </td>
	  </tr>
	</tbody>
</table>
<script>
window.setTimeout('this.location.reload();',3600000);
parent.leftmenu.online_count();
</script>


<div id="livemargins_control" style="position: absolute; display: none; z-index: 9999;">
	<img height="5" width="77" style="position: absolute; left: -77px; top: -5px;" src="chrome://livemargins/skin/monitor-background-horizontal.png">	
	<img style="position: absolute; left: 0pt; top: -5px;" src="chrome://livemargins/skin/monitor-background-vertical.png">	
	<img style="position: absolute; left: 1px; top: 0pt; opacity: 0.5; cursor: pointer;" onmouseout="this.style.opacity=0.5" onmouseover="this.style.opacity=1" src="chrome://livemargins/skin/monitor-play-button.png" id="monitor-play-button">
</div>

</body></html>