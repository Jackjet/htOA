<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>
<html><head>
<title></title>
<meta content="text/html; charset=gb2312" http-equiv="Content-Type">
<link href="css/theme/7/style.css" type="text/css" rel="stylesheet">
<link href="css/theme/7/ilook.css" type="text/css" rel="stylesheet">

<script src="inc/ccorrect_btn.js"></script>
<script src="inc/mytable.js"></script>
<script>
var area="1",keyword="";
function c(id)
{
  var module=$('module_'+id);
  if(module.style.display=="none" || module.style.display=="")
  {
     $('module_'+area).style.display='none';
     $('link_'+area).className="";
     area=id;
     module.style.display='block';
     $('link_'+id).className="active";
     if(module.innerHTML=="")
        ilook();
  }
  else
  {
     module.style.display="none";
     $('link_'+id).className="";
  }
}
function ilook(key)
{
	$('module_'+area).style.display='block';
	$('link_'+area).className="active";
	$('module_'+area).innerHTML="<img src='/images/loading.gif' height='20' width='20' align='absMiddle'> 加载中，请稍候……";

	if(key) keyword=key;
	if(!keyword)
	{
	   $('module_'+area).innerHTML="<div class=\"blank\">请输入关键词进行搜索</div>";
	   return;
	}
	var req=getXMLHttpObj();
	req.open("GET","search.php?AREA="+area+"&amp;KWORD="+keyword,true);
	req.onreadystatechange=function(){if(req.readyState==4)$("module_"+area).innerHTML=req.responseText;};
	req.send(null)
}
function open_news(USER_ID)
{
 URL="/general/ipanel/user_info.php?USER_ID="+USER_ID + "&amp;SEARCH_CENTER_FLAG=" + 1;
 myleft=(screen.availWidth-650)/2;
 mytop=100
 mywidth=650;
 myheight=500;
 window.open(URL,"user","height="+myheight+",width="+mywidth+",status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top="+mytop+",left="+myleft+",resizable=yes");
}

function open_email(EMAIL_ID,BOX_ID)
{
 URL="/general/email/inbox/read_email/read_email.php?BOX_ID="+BOX_ID + "&amp;EMAIL_ID="+EMAIL_ID + "&amp;SEARCH_CENTER_FLAG=" + 1;
 myleft=(screen.availWidth-650)/2;
 mytop=100
 mywidth=650;
 myheight=500;
 window.open(URL,"email","height="+myheight+",width="+mywidth+",status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top="+mytop+",left="+myleft+",resizable=yes");
}

function open_notify(NOTIFY_ID)
{
 URL="/general/notify/show/read_notify.php?NOTIFY_ID="+NOTIFY_ID + "&amp;SEARCH_CENTER_FLAG=" + 1;
 myleft=(screen.availWidth-650)/2;
 mytop=100
 mywidth=650;
 myheight=500;
 window.open(URL,"open_notify","height="+myheight+",width="+mywidth+",status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top="+mytop+",left="+myleft+",resizable=yes");
}

function open_address(ADD_ID)
{
 URL="/general/address/private/address/add_detail.php?ADD_ID="+ADD_ID + "&amp;SEARCH_CENTER_FLAG=" + 1;
 myleft=(screen.availWidth-650)/2;
 mytop=100
 mywidth=650;
 myheight=500;
 window.open(URL,"open_notify","height="+myheight+",width="+mywidth+",status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top="+mytop+",left="+myleft+",resizable=yes");
}

function open_file(CONTENT_ID,SORT_ID)
{
 URL="/general/file_folder/read.php?CONTENT_ID=" + CONTENT_ID + "&amp;SORT_ID=" + SORT_ID + "&amp;SEARCH_CENTER_FLAG=" + 1;
 myleft=(screen.availWidth-650)/2;
 mytop=100
 mywidth=650;
 myheight=500;
 window.open(URL,"open_file","height="+myheight+",width="+mywidth+",status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top="+mytop+",left="+myleft+",resizable=yes");
}

function form_view(RUN_ID,FLOW_ID,FLOW_PRCS)
{
  myleft=(screen.availWidth-800)/2;
  window.open("/general/workflow/list/print?RUN_ID="+RUN_ID+"&amp;FLOW_ID="+FLOW_ID+"&amp;FLOW_PRCS="+FLOW_PRCS + "&amp;SEARCH_CENTER_FLAG=" + 1,"","status=0,toolbar=no,menubar=no,width=800,height=600,location=no,scrollbars=yes,resizable=yes,left="+myleft+",top=50");
}

function open_customer(CUSTOMER_ID)
{
 URL="/general/sale_manage/crm/query/customer/showdetail.php?CUSTOMER_ID=" + CUSTOMER_ID + "&amp;SEARCH_CENTER_FLAG=" + 1;
 myleft=(screen.availWidth-650)/2;
 mytop=100
 mywidth=650;
 myheight=500;
 window.open(URL,"open_customer","height="+myheight+",width="+mywidth+",status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top="+mytop+",left="+myleft+",resizable=yes");
}

function open_online(ONLINE_ID)
{
 URL="/general/training/train/zhidao/question/display.php?ONLINE_ID=" + ONLINE_ID + "&amp;SEARCH_CENTER_FLAG=" + 1;
 myleft=(screen.availWidth-650)/2;
 mytop=100
 mywidth=650;
 myheight=500;
 window.open(URL,"open_online","height="+myheight+",width="+mywidth+",status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top="+mytop+",left="+myleft+",resizable=yes");
}
</script>
</head><body>
<div id="body">
<ul>
  <li class="L1"><a id="link_1" href="javascript:c('1');"><span> 用户</span></a></li>
  <div class="moduleContainer" id="module_1"></div>
  <li class="L1"><a id="link_2" href="javascript:c('2');"><span> 内部邮件(收件箱)</span></a></li>
  <div class="moduleContainer" id="module_2"></div>
  <li class="L1"><a id="link_3" href="javascript:c('3');"><span> 公告通知</span></a></li>
  <div class="moduleContainer" id="module_3"></div>
  <li class="L1"><a id="link_4" href="javascript:c('4');"><span> 通讯簿</span></a></li>
  <div class="moduleContainer" id="module_4"></div>
  <li class="L1"><a id="link_5" href="javascript:c('5');"><span> 文件柜</span></a></li>
  <div class="moduleContainer" id="module_5"></div>
  <li class="L1"><a id="link_6" href="javascript:c('6');"><span> 工作流</span></a></li>
  <div class="moduleContainer" id="module_6"></div>
  <li class="L1"><a id="link_7" href="javascript:c('7');"><span> 客户信息</span></a></li>
  <div class="moduleContainer" id="module_7"></div>
  <li class="L1"><a id="link_8" href="javascript:c('8');"><span> 知识问答</span></a></li>
  <div class="moduleContainer" id="module_8"></div>
</ul>
</div>
<div id="bottom"></div>
<div id="livemargins_control" style="position: absolute; display: none; z-index: 9999;">
	<img height="5" width="77" style="position: absolute; left: -77px; top: -5px;" src="chrome://livemargins/skin/monitor-background-horizontal.png">	
	<img style="position: absolute; left: 0pt; top: -5px;" src="chrome://livemargins/skin/monitor-background-vertical.png">	
	<img style="position: absolute; left: 1px; top: 0pt; opacity: 0.5; cursor: pointer;" onmouseout="this.style.opacity=0.5" onmouseover="this.style.opacity=1" src="chrome://livemargins/skin/monitor-play-button.png" id="monitor-play-button">
</div>

</body></html>