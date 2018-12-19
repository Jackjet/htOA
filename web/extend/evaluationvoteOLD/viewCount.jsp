<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<title>投票统计信息</title>
<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
<script src="<c:url value="/"/>datePicker/WdatePicker.js" type="text/javascript"></script>
<script src="<c:url value="/"/>js/inc_javascript.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/css/form.css"/>"/>

<script type="text/javascript" src="<c:url value="/"/>js/setimgsize.js"></script>

<!-- formValidator -->
<link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css"></link>
<script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript" charset="UTF-8"></script>
<!--<script src="<c:url value="/"/>components/formvalidator/js/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>

--><script>
	
	$(document).ready(function(){
		//验证
		//$.formValidator.initConfig({formid:"itemInforForm",onerror:function(msg){alert(msg)},onsuccess:function(){submitData();}});
		//$("#itemName").formValidator({onshow:"请输入投票标题",onfocus:"投票标题不能为空",oncorrect:"输入正确"}).inputValidator({min:1,onerror:"投票标题不能为空,请确认"});
		
		//window.scrollTo(0,99999);
		//document.getElementById("gototest").scrollIntoView();
	});
</script>
<style>
	img{
		border:0;
	}
	li{
		list-style:none;
	}
	input,label { vertical-align:middle;}
		 
	 .xstooltip 
		{
		visibility: hidden; 
		position: absolute; 
		top: 0;  
		left: 0; 
		z-index: 2; 
		 font: normal 8pt sans-serif; 
		padding: 3px; 
		border: solid 1px;
		}
																   
</style>

	  
<base target="_self"/>
<body>
<br/>
<form id="voteInforForm" name="voteInforForm" action="/tpwj/voteInfor.do?method=save" method="post">
<input type="hidden" name="topicId" id="topicId" value="${_TopicInfor.topicId}"/>
<table width="98%" cellpadding="6" cellspacing="1" align="center" border="1" bordercolor="#c5dbec">
	<tr>
		<td bgcolor="#dfeffc" align="center">
			<h2><font color="#15A7BC">${_TopicInfor.topicName}</font></h2>
			<div style="text-align:left;padding-left:10px;"><font color=gray>${_TopicInfor.descrip}</font></div>
			<c:if test="${!empty _TopicInfor.ruler}">
				<br/>
				<div style="text-align:left;padding-left:10px;"><font color=orange><b>投票规则：</b></font><font color=gray>${_TopicInfor.descrip}</font></div>
			</c:if>
			<br/>
			
		</td>
	</tr>
		<tr>
		<td valign=top style="padding-top:10px;">
		
			<table style="border:2px dotted #0DE8F5;" width="98%">
        		<tr>  
					<td bgcolor="#dfeffc" align="center" style="font-size:14px;font-family:黑体;">
						<c:set var="_AllNum" value="0"/>
						<b>应投票人数：
							<font color=blue>
								<c:if test="${_TopicInfor.openType == '0'}">${fn:length(_TopicInfor.rights)}<c:set var="_AllNum" value="${fn:length(_TopicInfor.rights)}"/></c:if>
								<c:if test="${_TopicInfor.openType == '1'}">${_AllUserCount}（全体用户）<c:set var="_AllNum" value="${_AllUserCount}"/></c:if>
							</font>
						</b>
						<b style="margin-left:50px;">已投票人数：<font color=blue>${fn:length(_TopicInfor.voteInfors)}</font></b>
						<b style="margin-left:50px;">未投票人数：<font color=red>${_AllNum - fn:length(_TopicInfor.voteInfors)}</font></b>
						<b style="margin-left:50px;">投票率：<font color=blue><fmt:formatNumber value="${fn:length(_TopicInfor.voteInfors) / _AllNum * 100}" pattern="0.0"/>%</font></b>
					</td>
				</tr>
			</table>
			<br/>
			<table id="optionTab" class="optionTab" width="98%" border="1">
				<tr>
					<td width="5%" nowrap align="center">序号</td>
					<td width="10%"align="center">部门</td>
					<td width="8%" align="center">姓名</td>
					<td width="12%" align="center">项目名称</td>
					<c:forEach items="${_TopicInfor.itemInfors}" var="item" varStatus="index">
						<c:forEach items="${item.optionInfors}" var="option" varStatus="index">
						<td align="center">${option.optionName}</td>
						</c:forEach>	
					</c:forEach>
				</tr>
					<c:forEach items="${_TopicInfor.personInfors}" var="person" varStatus="index">
					<tr>
					<td nowrap align=center>${index.index+1}</td>
					<td align="center">${person.department }</td>
					<td align="center">${person.personName }</td>
					<td align="center">${person.descrip }</td>
					<c:forEach items="${_TopicInfor.itemInfors}" var="item" varStatus="index">
						<c:forEach items="${item.optionInfors}" var="option" varStatus="index">
						<td align="left">
										    					<c:set var="count" value="0"/>
										    					<c:set var="people" value=""/>
										    					<c:forEach items="${_VoteItemList}" var="voteItem">
										    						<c:forEach items="${fn:split(voteItem.voteValue,',')}" var="voteValue">
										    							<c:if test="${voteValue == option.optionId && voteItem.personInfor.PId == person.PId }">
											    							<c:set var="count" value="${count+1}"/> 
											    							<c:set var="people" value="${people}${ voteItem.voteInfor.person.person.personName};"/>
											    							
											    						</c:if>
										    						</c:forEach>
										    					</c:forEach>										    			
											    					<c:set var="scale" value="${count/fn:length(_TopicInfor.voteInfors)}" />
											    					<div style="float:left;border:1px solid silver;width:100px;height:10px;text-align:left;"  title="${people}">
											    						<div style="height:10px;width:${100*scale}px;background-color:#ee335f;"></div><!-- #dd30ae -->
											    					</div>
											    					
											    					<div title="${people}">
											    						&nbsp;&nbsp;
											    						<font color=green><fmt:formatNumber value="${scale*100}" pattern="0.0"/>%&nbsp;（${count}票）</font>
											    					</div>
											    				
						</c:forEach>	
					</c:forEach>
					</tr>
				</c:forEach>
				
			</table>
		</td>
	</tr>
        
        		<script>
 function xstooltip_findPosX(obj) 
 {
   var curleft = 0;
   if (obj.offsetParent) 
   {
  while (obj.offsetParent) 
   {
    curleft += obj.offsetLeft
    obj = obj.offsetParent;
   }
  }
  else if (obj.x)
   curleft += obj.x;
  return curleft;
 }

 function xstooltip_findPosY(obj) 
 {
  var curtop = 0;
  if (obj.offsetParent) 
  {
   while (obj.offsetParent) 
   {
    curtop += obj.offsetTop
    obj = obj.offsetParent;
   }
  }
  else if (obj.y)
   curtop += obj.y;
  return curtop;
 }

 function xstooltip_show(tooltipId, parentId, posX, posY)
 {
  it = document.getElementById(tooltipId);
  
  if ((it.style.top == '' || it.style.top == 0) 
   && (it.style.left == '' || it.style.left == 0))
  {
   // need to fixate default size (MSIE problem)
   it.style.width = it.offsetWidth + 'px';
   it.style.height = it.offsetHeight + 'px';
   
   img = document.getElementById(parentId); 
  
   // if tooltip is too wide, shift left to be within parent 
   if (posX + it.offsetWidth > img.offsetWidth) posX = img.offsetWidth - it.offsetWidth;
   if (posX < 0 ) posX = 0; 
   
   x = xstooltip_findPosX(img) + posX;
   y = xstooltip_findPosY(img) + posY;
   
   it.style.top = y + 'px';
   it.style.left = x + 'px';
  }
  
  it.style.visibility = 'visible'; 
 }

 function xstooltip_hide(id)
 {
  it = document.getElementById(id); 
  it.style.visibility = 'hidden'; 
 }
</script>
                    
     <tr> 
        <td bgcolor="#dfeffc" align=left>
          <!--<input type="button" id="button" style="cursor: pointer;" onclick="submitData();" value="提交"/>
             &nbsp;
          <input type="button" value="关闭" style="cursor: pointer;" onclick="window.close();"/>
        --></td>
     </tr>
</table>
</form>
</body>
