<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>
<html><head>
<link href="css/theme/7/style.css" type="text/css" rel="stylesheet">
<link href="css/theme/7/topbar.css" type="text/css" rel="stylesheet">

<script src="inc/ccorrect_btn.js"></script>
<script src="inc/js/sterm.js"></script>
<script>
	//window.setTimeout('this.location.reload();',1200000);
</script>
<script src="inc/mytable.js" language="JavaScript"></script>
<script src="inc/weather.js" language="JavaScript"></script>

<script language="JavaScript">
function menu_click(menu_id)
{
	parent.leftmenu.menu_main.location="/general/ipanel/menu.php?MENU_ID="+menu_id;
	parent.leftmenu.view_menu(1);
	parent.callleftmenu.leftmenu_open();
}

function mdate()
{
   var solarTerm=sTerm(2010,08,10);
   if(solarTerm != "")
      $('mdate').innerHTML = solarTerm;
}

var OA_TIME = new Date(2010,7,10,08,41,55);

function timeview()
{
  timestr=OA_TIME.toLocaleString();
  timestr=timestr.substr(timestr.indexOf(":")-2);
  document.getElementById("time_area").innerHTML = timestr;
  OA_TIME.setSeconds(OA_TIME.getSeconds()+1);
  window.setTimeout( "timeview()", 1000 );
}

function startmarquee()
{
   var t;
   var pl=pr=false;
   var tb=document.getElementById("Nav_tb");
   var o=document.getElementById("Nav");
   o.scrollLeft = 0;
   var l=document.getElementById("NavLeft");
   var r=document.getElementById("NavRight");
   tb.onmouseover=function(){l.style.display="";r.style.display="";}
   tb.onmouseout =function(){l.style.display="none";r.style.display="none";}
   l.onmouseover=function(){if(l.src.indexOf("/images/nav_l2.gif")>-1){l.src="/images/nav_l3.gif";l.style.cursor="hand";} else pl=true;}
   r.onmouseover=function(){if(r.src.indexOf("/images/nav_r2.gif")>-1){r.src="/images/nav_r3.gif";r.style.cursor="hand";} else pr=true;}
   l.onclick=function(){if(pl) return; t=setInterval(scroll_right,10);pl=true;}
   r.onclick=function(){if(pr) return; t=setInterval(scroll_left,10); pr=true;}
   l.onmouseout=function(){pl=false;clearInterval(t);if(l.src.indexOf("/images/nav_l3.gif")>-1)l.src="/images/nav_l2.gif"; l.style.cursor="default";}
   r.onmouseout=function(){pr=false;clearInterval(t);if(r.src.indexOf("/images/nav_r3.gif")>-1)r.src="/images/nav_r2.gif"; r.style.cursor="default";}

   function scroll_left()
   {
      if(o.scrollLeft<o.scrollWidth-o.clientWidth)
      {
         o.scrollLeft += 2;
         if(l.src.indexOf("/images/nav_l2.gif")==-1) l.src="/images/nav_l2.gif";
      }
      else
      {
         if(r.src.indexOf("/images/nav_r1.gif")==-1) r.src="/images/nav_r1.gif";
      }
   }

   function scroll_right()
   {
      if(o.scrollLeft>0)
      {
         o.scrollLeft -= 2;
         if(r.src.indexOf("/images/nav_r2.gif")==-1) r.src="/images/nav_r2.gif";
      }
      else
    	{
         if(l.src.indexOf("/images/nav_l1.gif")==-1) l.src="/images/nav_l1.gif";
      }
   }

   function show_arrow()
   {
      if(tb.scrollWidth<o.scrollWidth)
         r.src="/images/nav_r2.gif";
      else
         r.src="/images/nav_r1.gif";

      if(o.scrollLeft<0)
         l.src="/images/nav_l2.gif";
      else
         l.src="/images/nav_l1.gif";
   }

   window.onresize=show_arrow;
   show_arrow();
}
</script>
</head>

<body onload="" style="margin: 0pt; padding: 0pt;" leftmargin="0" topmargin="0">

<table height="100%" cellspacing="0" cellpadding="0" border="0" width="100%" class="topbar">
  <tbody><tr height="55">
    <td nowrap="">
		<%--<img align="absmiddle" src="<c:url value="/"/>css/theme/7/product.png" width="310px">--%>
    </td>
    <td align="right" valign="top">
      <div id="time">
      	<span class="time_left">
      		<span class="time_right">
		 		<span id="date">
					<script>
						setInterval("document.getElementById('time').innerHTML=new Date().toLocaleString()+' 星期'+'日一二三四五六'.charAt(new Date().getDay());",1000);
					</script>
  				</span>
  			</span>
  		</span>
      </div>
	</td>
	</tr>
</tbody>
</table>
</body>
</html>