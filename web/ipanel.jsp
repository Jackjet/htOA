<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<html><head>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<link href="css/theme/7/style.css" type="text/css" rel="stylesheet">

<script src="inc/ccorrect_btn.js"></script>
<script src="inc/mytable.js"></script>
<script>
var menu_id=1;

function setPointer(element,over_flag,menu_id_over)
{
  if(menu_id!=menu_id_over)
  {
     if(over_flag==1)
        element.bgColor="#B3D1FF";
     else
        element.bgColor="#EEEEEE";
  }
}

var init_flag=0;
function init_menu()
{
  init_flag++;
  if(init_flag==1)
     view_menu(1);
}


function view_menu(id) {
	var frame1 = document.getElementById("frame1");

  if(id==1)
  {
     frame1.rows="50,*,0,0"; 
     load_page.location="about:blank";
  }
  else if(id==2)
  {
     load_page.location="ipanel/user_online.jsp";
     frame1.rows="83,0,*,0";
  }
  else if(id==3)
  {
     load_page.location="ipanel/smsbox.htm";
     frame1.rows="83,0,*,0";
  }
  else if(id==4)
  {
     frame1.rows="83,0,0,*";
     load_page.location="about:blank";
  }
  
  pheader.$('menu_'+menu_id).className="";
  pheader.$('menu_'+id).className="active";
  menu_id=id;

}


/**
  var xmlHttpObj=getXMLHttpObj();
  var theURL="user_count.php";

function online_count()
{
  xmlHttpObj.open("GET",theURL,true);
  var responseText="";
  xmlHttpObj.onreadystatechange=function()
	{
		if(xmlHttpObj.readyState==4)
		{
			responseText=xmlHttpObj.responseText;
			if(parent.parent.status_bar.document.getElementById("user_count1"))
			   parent.parent.status_bar.document.getElementById("user_count1").value=responseText;
		}
	}
  xmlHttpObj.send(null)
  //setTimeout("online_count()",120000);
}
//setTimeout("online_count()",1000);
*/
function openURL(URL,OP)
{
	  if(OP==1)
	  {
	     mytop=(screen.availHeight-500)/2-30;
		 myleft=(screen.availWidth-780)/2;
		 window.open(URL,"wy","height=500,width=780,status=0,toolbar=no,menubar=yes,location=no,scrollbars=yes,top="+mytop+",left="+myleft+",resizable=yes");
	  } else {			
		//alert(URL);
        parent.table_index.main.location=URL;
	  }
}

/**
function send_sms(TO_ID,TO_NAME)
{
   mytop=screen.availHeight-190;
   myleft=0;
   window.open("/general/status_bar/sms_back.php?TO_ID="+TO_ID+"&amp;TO_NAME="+TO_NAME,"send_sms","height=180,width=380,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,top="+mytop+",left="+myleft+",resizable=yes");
}

function send_email(TO_ID,TO_NAME)
{
   parent.table_index.main.location="/general/email/new?TO_ID="+TO_ID+"&amp;TO_NAME="+TO_NAME;
}
*/

</script>
</head>

<frameset id="frame1" framespacing="0" border="0" frameborder="no" cols="*" rows="83,*,0,0">
	<frame scrolling="no" noresize="" frameborder="0" src="panel.jsp" name="pheader"></frame>
    <frame scrolling="auto" frameborder="0" src="/leftMenuInfor.do" noresize="" name="menu_main"></frame>
    <frame scrolling="auto" frameborder="0" src="#" noresize="" name="load_page"></frame>
    <frame scrolling="auto" frameborder="0" src="shortcut.jsp" noresize="" name="ilook"></frame>
	<div id="livemargins_control" style="position: absolute; display: none; z-index: 9999;"></div>
</frameset>
<noframes></noframes>
</html>