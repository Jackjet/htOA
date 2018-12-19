<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml"><head>

<title>登录信息</title>
<meta content="text/html; charset=gb2312" http-equiv="Content-Type">
<link href="css/theme/7/style.css" type="text/css" rel="stylesheet">
<link href="css/theme/7/pheader.css" type="text/css" rel="stylesheet">
<script src="inc/js/utility.js"></script>
<script src="inc/mytable.js"></script>
<script src="inc/js/module.js"></script>

<script language="JavaScript">
//window.setTimeout('this.location.reload();',1200000);

function change_status()
{
	if(document.getElementById("ON_STATUS").style.display=="none")
	   document.getElementById("ON_STATUS").style.display="";
	else
		 document.getElementById("ON_STATUS").style.display="none";
}

function my_on_status()
{
	location="login_info.php?ON_STATUS_SET="+document.getElementById("ON_STATUS").value;
}

var flag=1;
function my_menu_view()
{
	if(flag==1)
	{
     parent.parent.parent.frame1.rows="0,*,20";
     arrow_img.src="images/arrow_down.gif";
  }
	else
	{
		 parent.parent.parent.frame1.rows="68,*,20";
		 arrow_img.src="images/arrow_up.gif";
  }

  flag=1-flag;
}

function CheckSend()
{
  if(event.keyCode==13)
     mysearch();
}

function mysearch()
{
	parent.parent.table_index.main.location='/general/ilook?KWORD='+KWORD.value;
}

var interval=null,key="";
var KWORD;
function CheckSend()
{
	KWORD=$("kword");
	if(KWORD.value=="OA搜索...")
	   KWORD.value="";
  if(KWORD.value=="" && $('search_icon').src.indexOf("/images/quicksearch.gif")==-1)
	{
	   $('search_icon').src="/images/quicksearch.gif";
	}
	if(key!=KWORD.value && KWORD.value!="")
	{
     key=KWORD.value;
	   parent.view_menu(4);
	   parent.ilook.ilook(KWORD.value);
	   if($('search_icon').src.indexOf("/images/quicksearch.gif")>=0)
	   {
	   	   $('search_icon').src="/images/closesearch.gif";
	   	   $('search_icon').title="清除关键字";
	   	   $('search_icon').onclick=function(){KWORD.value='OA搜索...';$('search_icon').src="/images/quicksearch.gif";$('search_icon').title="";$('search_icon').onclick=null;};
	   }
  }
}


var ctroltime,on_status=1;
function set_status(status)
{
	//on_status++;
	if(status>3)
	   status=1;
	on_status=status;

	var obj=$("on_status_desc");
	obj.innerHTML=$("on_status_"+on_status).innerHTML+'&lt;span style="font-family:Webdings"&gt;6&lt;/span&gt;';
	obj.className="on_status_"+on_status;
	hide_status();
	
	var xmlHttpObj=getXMLHttpObj();
	xmlHttpObj.open("GET","login_info.php?ON_STATUS_SET="+on_status,true);
	xmlHttpObj.send(null);
}
function show_status(a)
{
   var pos=fetchOffset(a);
   $("on_status_ul").style.display='block';
   $("on_status_ul").style.width=(a.offsetWidth+5)+"px";
   $("on_status_ul").style.left=(pos['left']-5)+"px";
   $("on_status_ul").style.top=(pos['top']+a.offsetHeight)+"px";
}
function hide_status()
{
   $("on_status_ul").style.display='none';
}
function update_status()
{
	var xmlHttpObj=getXMLHttpObj();
	xmlHttpObj.open("GET","login_info.php?ON_STATUS_SET="+on_status,true);
	xmlHttpObj.send(null)
}
function show_my_status()
{
   $('my_info').style.display='none';
   $('my_status').style.display='';
   $('my_status').focus();
   $('my_status').select();
}
var my_cur_status="dsa";
function update_my_status()
{
   $('my_status').style.display='none';
   $('my_info').style.display='';
   
	var my_status=$('my_status').value;
	if(my_status=="我的留言" || my_cur_status==my_status)
	   return;
	my_cur_status=my_status;
	$('my_info').title=my_cur_status;
	var xmlHttpObj=getXMLHttpObj();
	xmlHttpObj.open("GET","login_info.php?MY_STATUS_SET="+my_status,true);
	xmlHttpObj.send(null);
}
function input_my_status()
{
   if(event.keyCode==13)
      update_my_status();
}
//-------- 打开网址 -------
function openURL(URL)
{
    parent.openURL(URL);
}
</script>
</head>

<body onload="parent.init_menu()">
	<div class="small" id="on_status">
		<span id="my_info">
			<%--<span onclick="show_my_status();">--%>
				<span>
					<%--<img height="18" align="absmiddle" width="18" src="images/wxg.gif">--%>
					您好！${_GLOBAL_PERSON.personName}
				</span>
				
				<%--<span title="上午好！您的部门：销售部 您的登录身份：部门经理 在线时长：66976小时4分 上次登陆IP：183.4.37.135" onmouseout="hide_status();" onmouseover="show_status(this);" id="on_status_span">
		   		<span class="on_status_1 dropdown" id="on_status_desc">联机<span style="font-family: Webdings;">6</span>
		   		</span>
   				--%>
   				
			    <ul id="on_status_ul">
			      <li><a title="" href="javascript:set_status(1);" id="on_status_1">联机</a></li>
			      <li><a title="" href="javascript:set_status(2);" id="on_status_2">忙碌</a></li>
			      <li><a title="" href="javascript:set_status(3);" id="on_status_3">离开</a></li>
			    </ul>
			<%--</span>--%>
		</span>
		<input type="text" style="display: none;" maxlength="100" class="SmallInput" onkeypress="input_my_status()" onblur="update_my_status();" value="dsa" name="my_status" id="my_status">
	</div>
	
	<%--<c:if test="${_SYSTEM_USER.userType != 2 && _SYSTEM_USER.userType != 3}">
		--%>
	<c:if test="${_NORMAL_USER}">
		<div id="tabs">
		  <div id="search">
			<form style="margin:0" name="myform" method="post">
				<input type="text" class="SmallInput" onblur="clearInterval(interval);if(KWORD.value=='')KWORD.value='OA搜索...';" onfocus="interval=setInterval(CheckSend,100);" value="OA搜索..." name="kword" id="kword" size="25">
				<img align="absmiddle" onclick="CheckSend();" style="cursor: pointer;" src="images/quicksearch.gif" id="search_icon">
			</form>
			</div>
		</div>
	</c:if>
  <!--
  <ul id="nav_menu">
    <li><a class="active" title="导航菜单" href="javascript:parent.view_menu(1);" id="menu_1"><span>导航</span></a></li>
    <li><a title="组织机构及人员" href="javascript:parent.view_menu(2);" id="menu_2"><span>组织</span></a></li>
   
    <li><a title="短信箱" href="javascript:parent.view_menu(3);" id="menu_3"><span>短信</span></a></li>
    <li><a title="信息搜索" href="javascript:parent.view_menu(4);" id="menu_4"><span>搜索</span></a></li>
     
  </ul>-->

</body></html>