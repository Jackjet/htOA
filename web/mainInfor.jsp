<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>
<%@ page import="com.kwchina.core.cms.util.InforConstant" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">

<html><head>


<title>我的办公桌</title>

<link href="<c:url value="/"/>css/theme/7/style.css" type="text/css" rel="stylesheet">
<link href="<c:url value="/"/>css/theme/7/mytable.css" type="text/css" rel="stylesheet">

<script src="<c:url value="/"/>css/theme/7/mytable.js"></script>
<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script> <!--jquery包-->
<script src="<c:url value="/"/>js/commonFunction.js"></script>

<script language="JavaScript">
	window.setTimeout('this.location.reload();',3000);//1200000
	var my_pos= true;
	var my_expand= true;
	var lines_per_page=5;
	var my_scroll=false;
	var my_width=75;
	
	function _resize(module_id)	{
		 var module_i=document.getElementById("module_"+module_id);
		 var head_i=document.getElementById("module_"+module_id+"_head");
		 var body_i=document.getElementById("module_"+module_id+"_body");
		 var img_i=document.getElementById("img_resize_"+module_id);
		 
		 //alert("module_"+module_id+"_body");		 
		 
		 if(body_i.style.display=="none") {
		    module_i.className=module_i.className.substr(0,module_i.className.lastIndexOf(" "));
		    head_i.className=head_i.className.substr(0,head_i.className.lastIndexOf(" "));
		    body_i.style.display="block";
		    img_i.src=img_i.src.substr(0,img_i.src.lastIndexOf("/")+1)+"mytable_ad.png";
		    img_i.title="折叠";
		    setCookie("my_expand_1_"+module_id, "");
		 } else	 {
		 	//alert(121212212);
		    module_i.className=module_i.className+" listColorCollapsed";
		    head_i.className=head_i.className+" moduleHeaderCollapsed";
		    body_i.style.display="none";
		    img_i.src=img_i.src.substr(0,img_i.src.lastIndexOf("/")+1)+"mytable_ar.png";
		    img_i.title="展开";
		    setCookie("my_expand_1_"+module_id, "0");
		 }
	}
	
	function _edit()
	{
		 show_msg("optionBlock");
		 $('display_lines').focus();
		 $('display_lines').value=lines_per_page;
		 $('col_width').value=my_width;
	}
	function SetNums()
	{
		 var today_lines=$('display_lines').value;
		 var col_width=$('col_width').value;
	   if(today_lines=="" || checkNum(today_lines) || col_width=="" || checkNum(col_width))
	   {
	      alert("显示条数和栏目宽度必须是数字");
	      return;
	   }
	
	   if(parseInt(today_lines)<=0 || parseInt(today_lines)>=1000)
	   {
	      alert("显示条数必须在1-1000之间");
	      return;
	   }
	   if(parseInt(col_width)<=0 || parseInt(col_width)>100)
	   {
	      alert("栏目宽度必须在1-100之间");
	      return;
	   }
	   setCookie("my_nums_1", today_lines);
	   setCookie("my_width_1", col_width);
	
	   lines_per_page=today_lines;
	   my_width=col_width;
	
	   $("msgBody").style.display = "none";
	   $("msgCommand").style.display = "none";
	   $("msgSuccess").style.display = "block";
	   
	   window.location.reload();
	}
	
	function setCookie(name,value) {
	   var today = new Date();
	   var expires = new Date();
	   expires.setTime(today.getTime() + 1000*60*60*24*2000);
	   parent.document.cookie = name + "=" + escape(value) + "; expires=" + expires.toGMTString();
	}
	function checkNum(str)
	{
	   var re=/\D/;
	   return str.match(re);
	}
	function SetEmail(module,read_flag)
	{
	    setCookie("wxg_my_"+module+"_read_flag", read_flag);
	   window.location.reload();
	}
	
	function SetNumsMo(module,CookieField,infoArea)
	{
	   if(!CookieField || CookieField=="")
	   {
	      CookieName="wxg_my_"+module+"_nums";
	      CookieField="MODULE_NUM_"+module;
	      infoArea="module_"+module+"_info";
	   }
	   else
	   {
	      CookieName=module;
	   }
	
	   var CookieValue=document.all(CookieField).value;
	   if(CookieValue=="")
	   {
	      alert("请输入数值");
	      return;
	   }
	   if(checkNum(CookieValue))
	   {
	      alert("显示条数必须是数字");
	      return;
	   }
	
	   if(parseInt(CookieValue)<=0 || parseInt(CookieValue)>=1000)
	   {
	      alert("显示封数必须在1-1000之间");
	      return;
	   }
	   setCookie(CookieName, CookieValue);
	   if(document.all("MODULE_SCROLL_"+module))
	      setCookie("wxg_my_"+module+"_scroll", document.all("MODULE_SCROLL_"+module).checked);
	   if(document.all(infoArea))
	      document.all(infoArea).innerHTML="&lt;font color=red&gt;设置成功！&lt;/font&gt;";
	}
	
	
	function SetDateMo(module)
	{
	    CookieName="wxg_my_"+module+"_date";
	    CookieField="MODULE_DATE_"+module;
	    infoArea="module_"+module+"_info";
	
	   var CookieValue=document.all(CookieField).value;
	   if(CookieValue=="")
	   {
	      alert("请输入日期");
	      return;
	   }
	   setCookie(CookieName, CookieValue);
	   if(document.all(infoArea))
	      document.all(infoArea).innerHTML="&lt;font color=red&gt;设置成功！&lt;/font&gt;";
	}
	function _show(module)
	{
	   var module_i=document.getElementById("module_"+module+"_edit");
	   if(module_i && module_i.style.display=="none")
	      module_i.style.display="";
	   else
	   {
	      module_i.style.display="none";
	      var infoArea=document.getElementById("module_"+module+"_info");
	      if(infoArea)
	         infoArea.innerHTML="";
	   }
	}
	function resize_all()
	{
	   var img_all_resize=document.getElementById("img_all_resize");
	   
	   var imgs=document.getElementsByTagName("IMG");
	   var module_id_str="";
	   for(var i=0;i<imgs.length;i++)
	   {
	      if(imgs[i].id.substr(0,11)!="img_resize_")
	         continue;
	      
	      var module_id=imgs[i].id.substr(11,imgs[i].id.length);
	      module_id_str+=module_id+",";
	      if(my_expand && document.getElementById("module_"+module_id+"_body").style.display!="none" || !my_expand && document.getElementById("module_"+module_id+"_body").style.display=="none")
	         _resize(module_id);
	   }
	   
	   if(my_expand)
	   {
	      img_all_resize.src=img_all_resize.src.substr(0,img_all_resize.src.lastIndexOf("/")+1)+"mytable_ar.png";
	      setCookie("my_expand_1",module_id_str);
	   }
	   else
	   {
	      img_all_resize.src=img_all_resize.src.substr(0,img_all_resize.src.lastIndexOf("/")+1)+"mytable_ad.png";
	      setCookie("my_expand_1","");
	   }
	
	   my_expand=!my_expand;
	   setCookie("my_expand_all_1", my_expand ? "" : "0");
	}
	
	function td_calendar(fieldname)
	{
	  myleft=document.body.scrollLeft+event.clientX-event.offsetX-80;
	  mytop=document.body.scrollTop+event.clientY-event.offsetY+140;
	  window.showModalDialog("/inc/calendar.php?FIELDNAME="+fieldname,self,"edge:raised;scroll:0;status:0;help:0;resizable:1;dialogWidth:280px;dialogHeight:215px;dialogTop:"+mytop+"px;dialogLeft:"+myleft+"px");
	}
</script>
</head>

<body style="background: url("file://") repeat scroll 0% 0% transparent;">
<div id="desktop_config">
  <img title="全部 展开/折叠" onclick="resize_all()" src="images/mytable_ad.png" id="img_all_resize" style="cursor: pointer;">
</div>

<table height="100%" cellspacing="0" cellpadding="0" border="0" width="100%">
 <tbody><tr>
  <td width="65%" valign="top" id="col_l">
  
  
  <!-------------------- 待办事宜---------------------->
	<div class="module listColor" id="module_41">
		  <div class="head">
			  <h4 class="moduleHeader" id="module_41_head">
			    <a class="expand" href="javascript:_resize(41);"><img title="折叠" src="images/mytable_ad.png" id="img_resize_41" class="icon"></a>
			    <span onclick="_resize(41);" class="text" id="module_41_text">待办事宜</span>
			    <span style="cursor: move; width: 586px;" class="title" id="module_41_title"> </span>
			    <span class="close" id="module_41_op">
			    <a alt="设置显示条数" href="javascript:_show('41');"><img border="0" alt="设置显示条数" src="images/edit.gif"></a>&nbsp;
			  	<a href="/general/url.php">全部</a>&nbsp;
			    <a href="javascript:_edit();">设置</a>&nbsp;<a href="javascript:_del(41);"><img src="images/close.png"></a></span>
			  </h4>
		  </div>
		  <div style="font-size: 9pt; display: none;" id="module_41_edit">
		      <div style="float: left;" id="module_41_info"></div>
		     显示条数：<input type="input" name="MODULE_NUM_41" value="" class="SmallInput" size="3">&nbsp;
		     <a title="保存设置" href="javascript:SetNumsMo('41');">设置</a>
		     <a title="关闭" href="javascript:_show('41');">关闭</a>
		  </div>
		  <div class="module_body" id="module_41_body">
		      <div class="module_div" id="module_41_ul">
		      	<%--<c:if test="${_SYSTEM_USER.userType != 2 && _SYSTEM_USER.userType != 3}">
		      		--%>
		      	<c:if test="${_NORMAL_USER}">
		      		<!-- 工作工作跟踪 -->
			      	<ul id="taskUl"><li></li>
						<script>
							function disTaskTip(taskId){
								if(confirm("请确认已知悉本提醒，之后将不再显示！")){
									$.ajax({
										url: '/supervise/superviseInfor.do?method=saveReadStatus&taskId='+taskId,
										cache: false,
										type: "GET",
										//dataType : "json",
										async: true,
							            cache: false,
										beforeSend: function (xhr) {
											
										},
										complete : function(req, msg) {
											//var msg = eval("(" + req.responseText + ")");
											//alert(msg+"--");
											window.location.reload();
											
										},
										success : function (msg) {
											window.location.reload();
										}
									});
								}
							}
					    	jQuery().ready(function (){
					    		$.getJSON("/supervise/superviseInfor.do?method=getNeedDealTasks",function(data) {
									//需要处理的审核实例
									var content = "";
									$.each(data._Tasks, function(i, n) {
										var workType = n.taskCategory.categoryType;
										var typeStr = '';
										if(workType == '1'){
											typeStr = '方针目标';
										}else if(workType == '2'){
											typeStr = '党群';
										}else if(workType == '3'){
											typeStr = '部门建设';
										}else if(workType == '4'){
											typeStr = '内控';
										}
										content += "<li><a href='/supervise/superviseInfor.do?method=viewTask&rowId="+n.taskId+"' target='_blank'> [工作跟踪-"+typeStr+"]"+data._WarningStrs[i]+"："+n.taskName+"</a></li>";
									});
									//修改内容后的提醒
									$.each(data._ChangedTasks, function(i, n) {
										var workType = n.taskCategory.categoryType;
										var typeStr = '';
										if(workType == '1'){
											typeStr = '方针目标';
										}else if(workType == '2'){
											typeStr = '党群';
										}else if(workType == '3'){
											typeStr = '部门建设';
										}else if(workType == '4'){
											typeStr = '内控';
										}
										content += "<li><a href='#' onclick='disTaskTip("+n.taskId+");'> [工作跟踪-"+typeStr+"]"+data._ChangedWarningStrs[i]+"："+n.taskName+"</a></li>";
									});
									$('#taskUl').html(content);
								});
					    	});
					    </script>
				    </ul>
				    
			      	<ul id="summaryUl"><li></li>
						<script>
					    	jQuery().ready(function (){
					    		$.getJSON("/meeting/meetInfor.do?method=getSummaryMeet",function(data) {
									//需要填写会议纪要的会议信息
									var content = "";
									$.each(data._Meets, function(i, n) {
										content += "<li><a href='/meeting/meetInfor.do?method=viewMeeting&rowId="+n.meetId+"' target='_blank'>[会议纪要]： "+n.meetName+" ("+n.author.person.personName+" "+n.meetDate+")</a></li>";
									});	
									$('#summaryUl').html(content);
								});
					    	});
					    </script>
				    </ul>
				    
				    <ul id="instanceUl"><li></li>
						<script>
					    	jQuery().ready(function (){
					    		$.getJSON("/workflow/instanceInfor.do?method=getNeedDealInstances",function(data) {
									//需要处理的审核实例
									var content = "";
									$.each(data._Instances, function(i, n) {
										content += "<li><a href='/workflow/instanceInfor.do?method=view&instanceId="+n.instanceId+"' target='_blank'> ["+n.flowDefinition.flowName+"]["+data._WarningStrs[i]+"]："+n.instanceTitle+" ("+n.department.organizeName+" "+n.updateTime+")</a></li>";
									});
									$('#instanceUl').html(content);
								});
					    	});
					    </script>
				    </ul>
				  </c:if>
			    
			   <ul id="topicUl"><li></li>
					<script>
				    	jQuery().ready(function (){
				    		$.getJSON("/tpwj/topicInfor.do?method=getTopics",function(data) {
								//需要处理的审核实例
								var content = "";
								$.each(data._Topics, function(i, n) {
									var typeName = "";
									if(n.type == 0){
										typeName = "投票"
									}
									if(n.type == 1){
										typeName = "问卷"
									}
									content += "<li><a href='/tpwj/topicInfor.do?method=viewTopicInfor&type="+n.type+"&topicId="+n.topicId+"' target='_blank'> [<font color=orange>"+typeName+"</font>]："+n.topicName+"</a></li>";
								});
								$('#topicUl').html(content);
							});
				    	});
				    </script>
			    </ul>
			    
			   <ul id="pyTopicUl"><li></li>
					<script>
				    	jQuery().ready(function (){
				    		$.getJSON("/extend/pyTopicInfor.do?method=getPyTopics",function(data) {
								//需要处理的审核实例
								var content = "";
								$.each(data._Topics, function(i, n) {
									var typeName = "评优投票";
							
									content += "<li><a href='/extend/pyVoteInfor.do?method=viewpy&rowId="+n.topicId+"' target='_blank'> [<font color=orange>"+typeName+"</font>]："+n.topicName+"</a></li>";
								});
								$('#pyTopicUl').html(content);
							});
				    	});
				    </script>
			    </ul>
			    
		      </div>
		  </div>
	</div>
  
  
  
  
  
  <!-------------------- 可配置显示出的模块   左边较宽位置显示5条及5条以上的信息 ----------------------->
  
  <%--<c:if test="${_SYSTEM_USER.userType != 2 && _SYSTEM_USER.userType != 3}">
	  --%>
	 <c:if test="${_NORMAL_USER}">
	  <c:forEach items="${_IndexFunc}" var="func" varStatus="idc">
	  	<c:if test="${func.displayCount >= 5}" > 
	  	<div id="module_100${idc.count}" class="module listColor">
		  <div class="head">
		  	<input type="hidden" id="hiddenPath${idc.count}" value="${func.category.pagePath}" />
			  <h4 id="module_100${idc.count}_head" class="moduleHeader">
			      <a href="javascript:_resize(100${idc.count});" class="expand"><img class="icon" id="img_resize_100${idc.count}" src="images/mytable_ad.png" title="折叠" /></a>
			      <span id="module_100${idc.count}_text" class="text"  onclick="_resize(100${idc.count});">${func.displayName}</span>
			      <span id="module_100${idc.count}_title" class="title"  style="cursor:move;"> </span>
			      <span id="module_100${idc.count}_op" class="close">
			      <a href="javascript:_show('100${idc.count}');" alt="设置显示条数"><img border="0" src="images/edit.gif" alt="设置显示条数"></a>&nbsp;
			      <a href="/general/notify/show">全部</a>&nbsp;
			   	  <a href="javascript:_edit();">设置</a>&nbsp;</span>
			   	  <span class="text" style="float:right;"><a href="/cms/${func.category.pagePath}.do?method=list">More...</a></span>
			  </h4>
		  </div>
		  
		  <div id="module_100${idc.count}_body" class="module_body"  >
		    <div id="module_100${idc.count}_ul" class="module_div">
		   		<ul id="funcUl${idc.count}"><li></li>
					<script>
					    //查看静态页面
						function viewInfor(htmlFilePath){
							window.open(<%=request.getContextPath()%>htmlFilePath,'','');
						}
					    jQuery().ready(function (){
					    	//alert(11);
					    	$.getJSON("/cms/inforDocument.do?method=getInforDocument&categoryId=${func.category.categoryId}&sidx=inforId&sord=desc&_search=false&page=1&rows=${func.displayCount}",function(data) {
								//需要首页显示的发布信息
								//alert(222);
								var content = "";
								$.each(data.rows, function(i, n) {
									if(${func.displayTime==true}){
									
										if(n.issueUnit!="" && n.issueUnit!=null){
										content += "<li><a href='javascript:;' onclick='viewInfor(\""+n.htmlFilePath+"?inforId="+n.inforId+"\");'> "+n.inforTitle+" ("+ n.issueUnit + " " +n.createTime+")</a>";
										}else{
										content += "<li><a href='javascript:;' onclick='viewInfor(\""+n.htmlFilePath+"?inforId="+n.inforId+"\");'> "+n.inforTitle+" ("+ n.author.person.department.organizeName + " " +n.createTime+")</a>";
										}
										
									}else if(${func.displayTime==false}){
										content += "<li><a href='javascript:;' onclick='viewInfor(\""+n.htmlFilePath+"?inforId="+n.inforId+"\");'> "+n.inforTitle+" </a>";
									}
									
									if (n.important) {
										content += " <img src='<c:url value='/'/>images/flag_red.gif' border='0'/>";
									}
									content += "</li>";
								});	
								//alert(content);
								$('#funcUl${idc.count}').html(content);
							});
					    });
					</script>
				</ul>
			</div>
		  </div>
		</div>
		</c:if>
	  </c:forEach>
	  
		 
		
		
		
		
	
	
	
	
	
	<!----------------------------------------- 海通简报 ---------------------------------------
	<div id="module_2" class="module listColor">
		  <div class="head">
			  <h4 id="module_2_head" class="moduleHeader">
			      <a href="javascript:_resize(2);" class="expand"><img class="icon" id="img_resize_2" src="images/mytable_ad.png" title="折叠" /></a>
			      <span id="module_2_text" class="text"  onclick="_resize(2);">海通简报</span>
			      <span id="module_2_title" class="title"  style="cursor:move;"> </span>
			      <span id="module_2_op" class="close">
			      <a href="javascript:_show('2');" alt="设置显示条数"><img border="0" src="images/edit.gif" alt="设置显示条数"></a>&nbsp;
			      <a href="/general/notify/show">全部</a>&nbsp;
			   	  <a href="javascript:_edit();">设置</a>&nbsp;</span>
			   	  <span class="text" style="float:right;"><a href="/cms/htreports.do?method=list">More...</a></span>
			  </h4>
			  
		  </div>
		  <div id="module_2_edit" style="font-size: 9pt;display:none;">
		      <div id="module_2_info" style="float:left;"></div>
		       显示条数：<input type="input" size="3" class="SmallInput" value="" name="MODULE_NUM_2">&nbsp;
		      <a href="javascript:SetNumsMo('2');" title="保存设置">设置</a>
		      <a href="javascript:_show('2');" title="关闭">关闭</a>
		  </div>
		  <div id="module_2_body" class="module_body"  >
		    <div id="module_2_ul" class="module_div">
		   		<ul id="htreportsUl"><li></li>
					<script>
					    //查看静态页面
						function viewInfor(htmlFilePath){
							window.open(<%=request.getContextPath()%>htmlFilePath,'','');
						}
					    jQuery().ready(function (){
					    	$.getJSON("/cms/inforDocument.do?method=getInforDocument&categoryId="+<%=InforConstant.Cms_Category_Htreports %>+"&sidx=inforId&sord=asc&_search=false&page=1&rows=10",function(data) {
								//需要首页显示的发布信息
								var content = "";
								$.each(data.rows, function(i, n) {
									content += "<li><a href='javascript:;' onclick='viewInfor(\""+n.htmlFilePath+"\");'> "+n.inforTitle+" ("+ n.author.person.personName + " " +n.createTime+")</a>";
									if (n.important) {
										content += " <img src='<c:url value='/'/>images/flag_red.gif' border='0'/>";
									}
									content += "</li>";
								});	
								$('#htreportsUl').html(content);
							});
					    });
					</script>
				</ul>
			</div>
		  </div>
		  
		</div>-->
		
	
	<!-------------------- 管理工作 ---------------------
	
	<div id="module_44" class="module listColor">
	  <div class="head">
		  <h4 id="module_44_head" class="moduleHeader">
		      <a href="javascript:_resize(44);" class="expand"><img class="icon" id="img_resize_44" src="/images/mytable_ad.png" title="折叠" /></a>
		      <span id="module_44_text" class="text"  onclick="_resize(44);">管理工作</span>
		      <span id="module_44_title" class="title"  style="cursor:move;"> </span>
		      <span id="module_44_op" class="close">
		      <a href="javascript:_show('44');" alt="设置显示条数"><img border="0" src="/images/edit.gif" alt="设置显示条数"></a>&nbsp;
		      <a href="/general/file_folder/index1.php">全部</a>&nbsp;
		      <a href="javascript:_edit();">设置</a>&nbsp;<a href="javascript:_del(44);"><img src="/images/close.png" /></a></span>
		      <span class="text" style="float:right;"><a href="/cms/managework.do?method=list">More...</a></span>
		  </h4>
	  </div>
	  <div id="module_44_edit" style="font-size: 9pt;display:none;">
	      <div id="module_44_info" style="float:left;"></div>
	  	  显示条数：<input type="input" size="3" class="SmallInput" value="" name="MODULE_NUM_44">&nbsp;
	      <a href="javascript:SetNumsMo('44');" title="保存设置">设置</a>
	      <a href="javascript:_show('44');" title="关闭">关闭</a>
	  </div>
	  <div id="module_44_body" class="module_body">
	    <div class="module_div" id="module_44_ul">
		    <ul id="manageworkUl"><li></li>
				<script type="text/javascript">
					 //查看静态页面
						function viewInfor(htmlFilePath){
							window.open(<%=request.getContextPath()%>htmlFilePath,'','');
						}
					    jQuery().ready(function (){
					    	$.getJSON("/cms/inforDocument.do?method=getInforDocument&categoryId="+<%=InforConstant.Cms_Category_Managework %>+"&sidx=inforId&sord=desc&_search=false&page=1&rows=10",function(data) {
								//需要首页显示的发布信息
								var content = "";
								$.each(data.rows, function(i, n) {
									content += "<li><a href='javascript:;' onclick='viewInfor(\""+n.htmlFilePath+"\");'> "+n.inforTitle+" ("+ n.author.person.personName + " " +n.createTime+")</a>";
									if (n.important) {
										content += " <img src='<c:url value='/'/>images/flag_red.gif' border='0'/>";
									}
									content += "</li>";
								});	
								$('#manageworkUl').html(content);
							});
					    });
				</script>
			</ul>
		</div>
	  </div>
	</div>-->
	
	
	<!----------------------------------------- 市场信息及研究 ---------------------------------------
	<div id="module_3" class="module listColor">
		  <div class="head">
			  <h4 id="module_3_head" class="moduleHeader">
			      <a href="javascript:_resize(3);" class="expand"><img class="icon" id="img_resize_3" src="images/mytable_ad.png" title="折叠" /></a>
			      <span id="module_3_text" class="text"  onclick="_resize(3);">市场信息及研究</span>
			      <span id="module_3_title" class="title"  style="cursor:move;"> </span>
			      <span id="module_3_op" class="close">
			      <a href="javascript:_show('3');" alt="设置显示条数"><img border="0" src="images/edit.gif" alt="设置显示条数"></a>&nbsp;
			      <a href="/general/notify/show">全部</a>&nbsp;
			   	  <a href="javascript:_edit();">设置</a>&nbsp;</span>
			   	  <span class="text" style="float:right;"><a href="/cms/marketinfo.do?method=list">More...</a></span>
			  </h4>
		  </div>
		  <div id="module_3_edit" style="font-size: 9pt;display:none;">
		      <div id="module_3_info" style="float:left;"></div>
		       显示条数：<input type="input" size="3" class="SmallInput" value="" name="MODULE_NUM_3">&nbsp;
		      <a href="javascript:SetNumsMo('3');" title="保存设置">设置</a>
		      <a href="javascript:_show('3');" title="关闭">关闭</a>
		  </div>
		  <div id="module_3_body" class="module_body"  >
		    <div id="module_3_ul" class="module_div">
		   		<ul id="marketinfoUl"><li></li>
					<script>
					    //查看静态页面
						function viewInfor(htmlFilePath){
							window.open(<%=request.getContextPath()%>htmlFilePath,'','');
						}
					    jQuery().ready(function (){
					    	$.getJSON("/cms/inforDocument.do?method=getInforDocument&categoryId="+<%=InforConstant.Cms_Category_Marketinfo %>+"&sidx=inforId&sord=desc&_search=false&page=1&rows=10",function(data) {
								//需要首页显示的发布信息
								var content = "";
								$.each(data.rows, function(i, n) {
									content += "<li><a href='javascript:;' onclick='viewInfor(\""+n.htmlFilePath+"\");'> "+n.inforTitle+" ("+ n.author.person.personName + " " +n.createTime+")</a>";
									if (n.important) {
										content += " <img src='<c:url value='/'/>images/flag_red.gif' border='0'/>";
									}
									content += "</li>";
								});	
								$('#marketinfoUl').html(content);
							});
					    });
					</script>
				</ul>
			</div>
		  </div>
		  
		</div>-->
		
		
		<!----------------------------------------- 党群园地 ---------------------------------------
	<div id="module_4" class="module listColor">
		  <div class="head">
			  <h4 id="module_4_head" class="moduleHeader">
			      <a href="javascript:_resize(4);" class="expand"><img class="icon" id="img_resize_4" src="images/mytable_ad.png" title="折叠" /></a>
			      <span id="module_4_text" class="text"  onclick="_resize(4);">党群园地</span>
			      <span id="module_4_title" class="title"  style="cursor:move;"> </span>
			      <span id="module_4_op" class="close">
			      <a href="javascript:_show('4');" alt="设置显示条数"><img border="0" src="images/edit.gif" alt="设置显示条数"></a>&nbsp;
			      <a href="/general/notify/show">全部</a>&nbsp;
			   	  <a href="javascript:_edit();">设置</a>&nbsp;</span>
			   	  <span class="text" style="float:right;"><a href="/cms/partygarden.do?method=list">More...</a></span>
			  </h4>
		  </div>
		  <div id="module_4_edit" style="font-size: 9pt;display:none;">
		      <div id="module_4_info" style="float:left;"></div>
		       显示条数：<input type="input" size="3" class="SmallInput" value="" name="MODULE_NUM_4">&nbsp;
		      <a href="javascript:SetNumsMo('4');" title="保存设置">设置</a>
		      <a href="javascript:_show('4');" title="关闭">关闭</a>
		  </div>
		  <div id="module_4_body" class="module_body"  >
		    <div id="module_4_ul" class="module_div">
		   		<ul id="partygardenUl"><li></li>
					<script>
					    //查看静态页面
						function viewInfor(htmlFilePath){
							window.open(<%=request.getContextPath()%>htmlFilePath,'','');
						}
					    jQuery().ready(function (){
					    	$.getJSON("/cms/inforDocument.do?method=getInforDocument&categoryId="+<%=InforConstant.Cms_Category_Partygarden %>+"&sidx=inforId&sord=desc&_search=false&page=1&rows=10",function(data) {
								//需要首页显示的发布信息
								var content = "";
								$.each(data.rows, function(i, n) {
									content += "<li><a href='javascript:;' onclick='viewInfor(\""+n.htmlFilePath+"\");'> "+n.inforTitle+" ("+ n.author.person.personName + " " +n.createTime+")</a>";
									if (n.important) {
										content += " <img src='<c:url value='/'/>images/flag_red.gif' border='0'/>";
									}
									content += "</li>";
								});	
								$('#partygardenUl').html(content);
							});
					    });
					</script>
				</ul>
			</div>
		  </div>
		  
		</div>-->

	</c:if>

	
	<div class="shadow"></div>
	  </td>
	  <td width="10"></td>
	  <td width="35%" valign="top" style="padding-right: 10px;" id="col_r">
	  
	  <!-------------------- 工作督办 ---------------------
	  <c:if test="${_SYSTEM_USER.userType != 2 && _SYSTEM_USER.userType != 3}">
		<div id="module_305" class="module listColor">
			  
			  <div class="head">
				  <h4 class="moduleHeader" id="module_305_head">
					  <a class="expand" href="javascript:_resize(305);"><img title="折叠" src="images/mytable_ad.png" id="img_resize_305" class="icon"></a>
					  <span onclick="_resize(305);" class="text" id="module_305_text">工作督办</span>
					  <span style="cursor: move; width: 190px;" class="title" id="module_305_title"> </span>
					  <span class="close" id="module_305_op">
					  <a alt="设置显示条数" href="javascript:_show('305');"><img border="0" alt="设置显示条数" src="images/edit.gif"></a>&nbsp;
					  <a href="/general/calendar/">全部</a>&nbsp;
					  <a href="javascript:_edit();">设置</a>&nbsp;<a href="javascript:_del(305);"><img src="images/close.png"></a></span>
				  </h4>
			  </div>
			   
			  
		  <div id="module_305_body" class="module_body">
		    <div id="module_305_ul" class="module_div" style="height:auto;">
			    
			     <ul id="taskUl"><li></li>
					<script>
						function disTaskTip(taskId){
							if(confirm("请确认已知悉本提醒，之后将不再显示！")){
								$.ajax({
									url: '/supervise/superviseInfor.do?method=saveReadStatus&taskId='+taskId,
									cache: false,
									type: "GET",
									//dataType : "json",
									async: true,
						            cache: false,
									beforeSend: function (xhr) {
										
									},
									complete : function(req, msg) {
										window.location.reload();
										
									},
									success : function (msg) {
										window.location.reload();
									}
								});
							}
						}
				    	jQuery().ready(function (){
				    		$.getJSON("/supervise/superviseInfor.do?method=getNeedDealTasks",function(data) {
								//需要处理的审核实例
								var content = "";
								$.each(data._Tasks, function(i, n) {
									content += "<li><a href='/supervise/superviseInfor.do?method=viewTask&rowId="+n.taskId+"' target='_blank'> [工作督办]"+data._WarningStrs[i]+"："+n.taskName+"</a></li>";
								});
								//修改内容后的提醒
								$.each(data._ChangedTasks, function(i, n) {
									content += "<li><a href='#' onclick='disTaskTip("+n.taskId+");'> [工作督办]"+data._ChangedWarningStrs[i]+"："+n.taskName+"</a></li>";
								});
								$('#taskUl').html(content);
							});
				    	});
				    </script>
			    </ul>
	
			</div>
		  </div>
		</div>
  
  	</c:if>-->
  
  
  <!-------------------- 公告栏 ----------------------->
  <%--<c:if test="${_SYSTEM_USER.userType != 2 && _SYSTEM_USER.userType != 3}">
	--%>
	<c:if test="${_NORMAL_USER}">
		<div id="module_35" class="module listColor">
		  
		  <div class="head">
			  <h4 class="moduleHeader" id="module_35_head">
				  <a class="expand" href="javascript:_resize(35);"><img title="折叠" src="images/mytable_ad.png" id="img_resize_35" class="icon"></a>
				  <span onclick="_resize(35);" class="text" id="module_35_text">公告栏</span>
				  <span style="cursor: move; width: 190px;" class="title" id="module_35_title"> </span>
				  <span class="close" id="module_35_op">
				  <a alt="设置显示条数" href="javascript:_show('35');"><img border="0" alt="设置显示条数" src="images/edit.gif"></a>&nbsp;
				  <a href="/general/calendar/">全部</a>&nbsp;
				  <a href="javascript:_edit();">设置</a>&nbsp;<a href="javascript:_del(35);"><img src="images/close.png"></a></span>
				  <%--<c:if test="${_SYSTEM_USER.userType != 2 && _SYSTEM_USER.userType != 3}">
					  --%>
				  <c:if test="${_NORMAL_USER}">
					  <span class="text" style="float:right;"><a href="/cms/annouce.do?method=list">More...</a></span>
				  </c:if>
			  </h4>
		  </div>
		   
		  
	  <div id="module_35_body" class="module_body">
	    <div id="module_35_ul" class="module_div" style="height:auto;">
	    
	    	
			    <ul id="messageUl"><li></li>
					<script>
				    	function viewInfor(htmlFilePath){
								window.open(<%=request.getContextPath()%>htmlFilePath,'','');
							}
						    jQuery().ready(function (){
						    	$.getJSON("/cms/newsInfor.do?method=getInfors&categoryId="+<%=InforConstant.Cms_Category_Annouce %>,function(data) {
									//需要首页显示的发布信息
									var content = "";
									$.each(data._Infors, function(i, n) {
										content += "<li><a href='javascript:;' onclick='viewInfor(\""+n.htmlFilePath+"?inforId="+n.inforId+"\");'> "+n.inforTitle+" ("+ n.author.person.personName + " " +n.createTime+")</a>";
										if (n.important) {
											content += " <img src='<c:url value='/'/>images/flag_red.gif' border='0'/>";
										}
										content += "</li>";
									});	
									$('#messageUl').html(content);
								});
						    });
				    </script>
			    </ul>
			

		    <!--<ul id="publicTopicUl"><li></li>
				<script>
			    	jQuery().ready(function (){
			    		$.getJSON("/tpwj/topicInfor.do?method=getPublicTopics",function(data) {
							//需要处理的审核实例
							var content = "";
							$.each(data._PublicTopics, function(i, n) {
								var typeName = "";
								if(n.type == 0){
									typeName = "投票"
								}
								if(n.type == 1){
									typeName = "问卷"
								}
								content += "<li><a href='/tpwj/topicInfor.do?method=viewCount&type="+n.type+"&topicId="+n.topicId+"' target='_blank'> [<font color=orange>"+typeName+"</font>]："+n.topicName+" 的统计结果</a></li>";
							});
							$('#publicTopicUl').html(content);
						});
			    	});
			    </script>
		    </ul>

		    <ul id="publicTopicUl"><li></li>
				<script>
			    	jQuery().ready(function (){
			    		$.getJSON("/tpwj/topicInfor.do?method=getPublicTopics",function(data) {
							//需要处理的审核实例
							var content = "";
							$.each(data._PublicTopics, function(i, n) {
								var typeName = "";
								if(n.type == 0){
									typeName = "投票"
								}
								if(n.type == 1){
									typeName = "问卷"
								}
								content += "<li><a href='/tpwj/topicInfor.do?method=viewCount&type="+n.type+"&topicId="+n.topicId+"' target='_blank'> [<font color=orange>"+typeName+"</font>]："+n.topicName+" 的统计结果</a></li>";
							});
							$('#publicTopicUl').html(content);
						});
			    	});
			    </script>
		    </ul>
		    
		    <ul id="pyPublicTopicUl"><li></li>
				<script>
			    	jQuery().ready(function (){
			    		$.getJSON("/extend/pyTopicInfor.do?method=getPublicTopics",function(data) {
							//需要处理的审核实例
							var content = "";
							$.each(data._PublicTopics, function(i, n) {
								var typeName = "评优投票";
							
								content += "<li><a href='/extend/pyTopicInfor.do?method=viewCount&rowId="+n.topicId+"' target='_blank'> [<font color=orange>"+typeName+"</font>]："+n.topicName+" 的统计结果</a></li>";
							});
							$('#pyPublicTopicUl').html(content);
						});
			    	});
			    </script>
		    </ul>
		    

		--></div>
	  </div>
	</div>
	</c:if>
	
	
	<!-------------------- 投票结果公示 ----------------------->
	<div id="module_351" class="module listColor">
		  
		  <div class="head">
			  <h4 class="moduleHeader" id="module_351_head">
				  <a class="expand" href="javascript:_resize(351);"><img title="折叠" src="images/mytable_ad.png" id="img_resize_351" class="icon"></a>
				  <span onclick="_resize(351);" class="text" id="module_351_text">投票结果</span>
				  <span style="cursor: move; width: 190px;" class="title" id="module_351_title"> </span>
				  <span class="close" id="module_351_op">
				  <a alt="设置显示条数" href="javascript:_show('351');"><img border="0" alt="设置显示条数" src="images/edit.gif"></a>&nbsp;
				  <a href="/general/calendar/">全部</a>&nbsp;
				  <a href="javascript:_edit();">设置</a>&nbsp;<a href="javascript:_del(351);"><img src="images/close.png"></a></span>
			  </h4>
		  </div>
		   
		  
	  <div id="module_35_body" class="module_body">
	    <div id="module_35_ul" class="module_div" style="height:auto;">

		    <ul id="countTopicUl"><li></li>
				<script>
			    	jQuery().ready(function (){
			    		$.getJSON("/tpwj/topicInfor.do?method=getCanCountTopics",function(data) {
							//需要处理的审核实例
							var content = "";
							$.each(data._CountTopics, function(i, n) {
								var typeName = "";
								if(n.type == 0){
									typeName = "投票"
								}
								if(n.type == 1){
									typeName = "问卷"
								}
								content += "<li><a href='/tpwj/topicInfor.do?method=viewCount&type="+n.type+"&topicId="+n.topicId+"' target='_blank'> [<font color=orange>"+typeName+"</font>]："+n.topicName+" 的统计结果</a></li>";
							});
							$('#countTopicUl').html(content);
						});
			    	});
			    </script>
		    </ul>

		    <ul id="publicTopicUl"><li></li>
				<script>
			    	jQuery().ready(function (){
			    		$.getJSON("/tpwj/topicInfor.do?method=getPublicTopics",function(data) {
							//需要处理的审核实例
							var content = "";
							$.each(data._PublicTopics, function(i, n) {
								var typeName = "";
								if(n.type == 0){
									typeName = "投票"
								}
								if(n.type == 1){
									typeName = "问卷"
								}
								content += "<li><a href='/tpwj/topicInfor.do?method=viewCount&type="+n.type+"&topicId="+n.topicId+"' target='_blank'> [<font color=orange>"+typeName+"</font>]："+n.topicName+" 的统计结果</a></li>";
							});
							$('#publicTopicUl').html(content);
						});
			    	});
			    </script>
		    </ul>
		    
		    <ul id="pyPublicTopicUl"><li></li>
				<script>
			    	jQuery().ready(function (){
			    		$.getJSON("/extend/pyTopicInfor.do?method=getPublicTopics",function(data) {
							//需要处理的审核实例
							var content = "";
							$.each(data._PublicTopics, function(i, n) {
								var typeName = "评优投票";
							
								content += "<li><a href='/extend/pyTopicInfor.do?method=viewCount&rowId="+n.topicId+"' target='_blank'> [<font color=orange>"+typeName+"</font>]："+n.topicName+" 的统计结果</a></li>";
							});
							$('#pyPublicTopicUl').html(content);
						});
			    	});
			    </script>
		    </ul>
		    

		</div>
	  </div>
	</div>
  
  
  
  <!-------------------- 可配置显示出的模块   右边较窄位置显示5条以下的信息 ----------------------->
  <%--<c:if test="${_SYSTEM_USER.userType != 2 && _SYSTEM_USER.userType != 3}">
	  --%>
	  <c:if test="${_NORMAL_USER}">
	  <c:forEach items="${_IndexFunc}" var="func" varStatus="idc">
	  	<c:if test="${func.displayCount < 5}" > 
	  	<div id="module_100${idc.count}" class="module listColor">
		  <div class="head">
		  	<input type="hidden" id="hiddenPath${idc.count}" value="${func.category.pagePath}" />
			  <h4 id="module_100${idc.count}_head" class="moduleHeader">
			      <a href="javascript:_resize(100${idc.count});" class="expand"><img class="icon" id="img_resize_100${idc.count}" src="images/mytable_ad.png" title="折叠" /></a>
			      <span id="module_100${idc.count}_text" class="text"  onclick="_resize(100${idc.count});">${func.displayName}</span>
			      <span id="module_100${idc.count}_title" class="title"  style="cursor:move;"> </span>
			      <span id="module_100${idc.count}_op" class="close">
			      <a href="javascript:_show('100${idc.count}');" alt="设置显示条数"><img border="0" src="images/edit.gif" alt="设置显示条数"></a>&nbsp;
			      <a href="/general/notify/show">全部</a>&nbsp;
			   	  <a href="javascript:_edit();">设置</a>&nbsp;</span>
			   	  <span class="text" style="float:right;"><a href="/cms/${func.category.pagePath}.do?method=list">More...</a></span>
			  </h4>
		  </div>
		  
		  <div id="module_100${idc.count}_body" class="module_body"  >
		    <div id="module_100${idc.count}_ul" class="module_div">
		   		<ul id="funcUl${idc.count}"><li></li>
					<script>
					    //查看静态页面
						function viewInfor(htmlFilePath){
							window.open(<%=request.getContextPath()%>htmlFilePath,'','');
						}
					    jQuery().ready(function (){
					    	//alert(11);
					    	$.getJSON("/cms/inforDocument.do?method=getInforDocument&categoryId=${func.category.categoryId}&sidx=createTime&sord=desc&_search=false&page=1&rows=${func.displayCount}",function(data) {
								//需要首页显示的发布信息
								//alert(222);
								var content = "";
								$.each(data.rows, function(i, n) {
									if(${func.displayTime==true}){
										content += "<li><a href='javascript:;' onclick='viewInfor(\""+n.htmlFilePath+"?inforId="+n.inforId+"\");'> "+n.inforTitle+" ( " +n.createTime+")</a>";
									}else if(${func.displayTime==false}){
										content += "<li><a href='javascript:;' onclick='viewInfor(\""+n.htmlFilePath+"?inforId="+n.inforId+"\");'> "+n.inforTitle+" </a>";
									}
									
									if (n.important) {
										content += " <img src='<c:url value='/'/>images/flag_red.gif' border='0'/>";
									}
									content += "</li>";
								});	
								//alert(content);
								$('#funcUl${idc.count}').html(content);
							});
					    });
					</script>
				</ul>
			</div>
		  </div>
		</div>
		</c:if>
	  </c:forEach>
  </c:if>
  
  



<!-------------------- 企业新风 --------------------
<div class="module listColor" id="module_30">
	  <div class="head">
		  <h4 class="moduleHeader" id="module_30_head">
			  <a class="expand" href="javascript:_resize(30);"><img title="折叠" src="images/mytable_ad.png" id="img_resize_30" class="icon"></a>
			  <span onclick="_resize(30);" class="text" id="module_30_text">企业新风</span>
			  <span style="cursor: move; width: 190px;" class="title" id="module_30_title"> </span>
			  <span class="close" id="module_30_op">
			  <a alt="设置显示条数" href="javascript:_show('30');"><img border="0" alt="设置显示条数" src="images/edit.gif"></a>&nbsp;
			  <a href="/general/calendar/">全部</a>&nbsp;
			  <a href="javascript:_edit();">设置</a>&nbsp;<a href="javascript:_del(30);"><img src="images/close.png"></a></span>
			  <span class="text" style="float:right;"><a href="/cms/enterprisenew.do?method=list">More...</a></span>
		  </h4>
	  </div>
	 
	  <div class="module_body" id="module_30_body">
	    <div style="height: auto;" class="module_div" id="module_30_ul">
	    	<ul id="enterprisenewUl"><li></li>
				<script>
			    	function viewInfor(htmlFilePath){
						window.open(<%=request.getContextPath()%>htmlFilePath,'','');
					}
				    jQuery().ready(function (){
				    	$.getJSON("/cms/inforDocument.do?method=getInforDocument&categoryId="+<%=InforConstant.Cms_Category_Enterprisenew %>+"&sidx=inforId&sord=asc&_search=false&page=1&rows=10",function(data) {
							//需要首页显示的发布信息
							var content = "";
							$.each(data.rows, function(i, n) {
								content += "<li><a href='javascript:;' onclick='viewInfor(\""+n.htmlFilePath+"\");'> "+n.inforTitle+" ("+ n.author.person.personName + " " +n.createTime+")</a>";
								if (n.important) {
									content += " <img src='<c:url value='/'/>images/flag_red.gif' border='0'/>";
								}
								content += "</li>";
							});	
							$('#enterprisenewUl').html(content);
						});
				    });
			    </script>
		    </ul>
	  	</div>
	  </div>
</div>-->
<script>
function cal_init2()
{
   var elementI=document.getElementsByTagName("DIV");
   for(i=0;i<elementI.length;i++)
   {
      if(elementI[i].id.substr(0,5)!="cal2_")
         continue;
      elementI[i].onmouseover=function() {var op_i=document.getElementById(this.id+"_op");if(op_i) op_i.style.display="";}
      elementI[i].onmouseout =function() {var op_i=document.getElementById(this.id+"_op");if(op_i) op_i.style.display="none";}
   }
}
cal_init2();
</script>

<!-------------------- 规章制度 ---------------------
<div id="module_36" class="module listColor">
	  
	  <div class="head">
		  <h4 class="moduleHeader" id="module_36_head">
			  <a class="expand" href="javascript:_resize(36);"><img title="折叠" src="images/mytable_ad.png" id="img_resize_36" class="icon"></a>
			  <span onclick="_resize(36);" class="text" id="module_36_text">规章制度</span>
			  <span style="cursor: move; width: 190px;" class="title" id="module_36_title"> </span>
			  <span class="close" id="module_36_op">
			  <a alt="设置显示条数" href="javascript:_show('36');"><img border="0" alt="设置显示条数" src="images/edit.gif"></a>&nbsp;
			  <a href="/general/calendar/">全部</a>&nbsp;
			  <a href="javascript:_edit();">设置</a>&nbsp;<a href="javascript:_del(36);"><img src="images/close.png"></a></span>
			  <span class="text" style="float:right;"><a href="/cms/bylaw.do?method=list">More...</a></span>
		  </h4>
	  </div>
	   
	  
  <div id="module_36_body" class="module_body">
    <div id="module_36_ul" class="module_div" style="height:auto;">
	    <ul id="bylawUl"><li></li>
			<script>
		    	function viewInfor(htmlFilePath){
						window.open(<%=request.getContextPath()%>htmlFilePath,'','');
					}
				    jQuery().ready(function (){
				    	$.getJSON("/cms/inforDocument.do?method=getInforDocument&categoryId="+<%=InforConstant.Cms_Category_Bylaw %>+"&sidx=inforId&sord=asc&_search=false&page=1&rows=10",function(data) {
							//需要首页显示的发布信息
							var content = "";
							$.each(data.rows, function(i, n) {
								content += "<li><a href='javascript:;' onclick='viewInfor(\""+n.htmlFilePath+"\");'> "+n.inforTitle+" ("+ n.author.person.personName + " " +n.createTime+")</a>";
								if (n.important) {
									content += " <img src='<c:url value='/'/>images/flag_red.gif' border='0'/>";
								}
								content += "</li>";
							});	
							$('#bylawUl').html(content);
						});
				    });
		    </script>
	    </ul>
	</div>
  </div>
</div>-->

<!-------------------- 新闻中心 ---------------------
<div id="module_37" class="module listColor">
	  
	  <div class="head">
		  <h4 class="moduleHeader" id="module_37_head">
			  <a class="expand" href="javascript:_resize(37);"><img title="折叠" src="images/mytable_ad.png" id="img_resize_37" class="icon"></a>
			  <span onclick="_resize(37);" class="text" id="module_37_text">新闻中心</span>
			  <span style="cursor: move; width: 190px;" class="title" id="module_37_title"> </span>
			  <span class="close" id="module_37_op">
			  <a alt="设置显示条数" href="javascript:_show('37');"><img border="0" alt="设置显示条数" src="images/edit.gif"></a>&nbsp;
			  <a href="/general/calendar/">全部</a>&nbsp;
			  <a href="javascript:_edit();">设置</a>&nbsp;<a href="javascript:_del(37);"><img src="images/close.png"></a></span>
			  <span class="text" style="float:right;"><a href="/cms/newscenter.do?method=list">More...</a></span>
		  </h4>
	  </div>
	   
	  
  <div id="module_37_body" class="module_body">
    <div id="module_37_ul" class="module_div" style="height:auto;">
	    <ul id="newscenterUl"><li></li>
			<script>
		    	function viewInfor(htmlFilePath){
						window.open(<%=request.getContextPath()%>htmlFilePath,'','');
					}
				    jQuery().ready(function (){
				    	$.getJSON("/cms/inforDocument.do?method=getInforDocument&categoryId="+<%=InforConstant.Cms_Category_Newscenter %>+"&sidx=inforId&sord=asc&_search=false&page=1&rows=10",function(data) {
							//需要首页显示的发布信息
							var content = "";
							$.each(data.rows, function(i, n) {
								content += "<li><a href='javascript:;' onclick='viewInfor(\""+n.htmlFilePath+"\");'> "+n.inforTitle+" ("+ n.author.person.personName + " " +n.createTime+")</a>";
								if (n.important) {
									content += " <img src='<c:url value='/'/>images/flag_red.gif' border='0'/>";
								}
								content += "</li>";
							});	
							$('#newscenterUl').html(content);
						});
				    });
		    </script>
	    </ul>
	</div>
  </div>
</div>-->

<!-------------------- 荣誉室 ---------------------
<div id="module_38" class="module listColor">
	  
	  <div class="head">
		  <h4 class="moduleHeader" id="module_38_head">
			  <a class="expand" href="javascript:_resize(38);"><img title="折叠" src="images/mytable_ad.png" id="img_resize_38" class="icon"></a>
			  <span onclick="_resize(38);" class="text" id="module_38_text">荣誉室</span>
			  <span style="cursor: move; width: 190px;" class="title" id="module_38_title"> </span>
			  <span class="close" id="module_38_op">
			  <a alt="设置显示条数" href="javascript:_show('38');"><img border="0" alt="设置显示条数" src="images/edit.gif"></a>&nbsp;
			  <a href="/general/calendar/">全部</a>&nbsp;
			  <a href="javascript:_edit();">设置</a>&nbsp;<a href="javascript:_del(38);"><img src="images/close.png"></a></span>
			  <span class="text" style="float:right;"><a href="/cms/honorroom.do?method=list">More...</a></span>
		  </h4>
	  </div>
	   
	  
  <div id="module_38_body" class="module_body">
    <div id="module_38_ul" class="module_div" style="height:auto;">
	    <ul id="honorroomUl"><li></li>
			<script>
		    	function viewInfor(htmlFilePath){
						window.open(<%=request.getContextPath()%>htmlFilePath,'','');
					}
				    jQuery().ready(function (){
				    	$.getJSON("/cms/inforDocument.do?method=getInforDocument&categoryId="+<%=InforConstant.Cms_Category_Honorroom %>+"&sidx=inforId&sord=asc&_search=false&page=1&rows=10",function(data) {
							//需要首页显示的发布信息
							var content = "";
							$.each(data.rows, function(i, n) {
								content += "<li><a href='javascript:;' onclick='viewInfor(\""+n.htmlFilePath+"\");'> "+n.inforTitle+" ("+ n.author.person.personName + " " +n.createTime+")</a>";
								if (n.important) {
									content += " <img src='<c:url value='/'/>images/flag_red.gif' border='0'/>";
								}
								content += "</li>";
							});	
							$('#honorroomUl').html(content);
						});
				    });
		    </script>
	    </ul>
	</div>
  </div>
</div>-->

<!-------------------- 知识园地 ---------------------
<div id="module_39" class="module listColor">
	  
	  <div class="head">
		  <h4 class="moduleHeader" id="module_39_head">
			  <a class="expand" href="javascript:_resize(39);"><img title="折叠" src="images/mytable_ad.png" id="img_resize_39" class="icon"></a>
			  <span onclick="_resize(39);" class="text" id="module_39_text">知识园地</span>
			  <span style="cursor: move; width: 190px;" class="title" id="module_39_title"> </span>
			  <span class="close" id="module_39_op">
			  <a alt="设置显示条数" href="javascript:_show('39');"><img border="0" alt="设置显示条数" src="images/edit.gif"></a>&nbsp;
			  <a href="/general/calendar/">全部</a>&nbsp;
			  <a href="javascript:_edit();">设置</a>&nbsp;<a href="javascript:_del(39);"><img src="images/close.png"></a></span>
			  <span class="text" style="float:right;"><a href="/cms/knowledgegarden.do?method=list">More...</a></span>
		  </h4>
	  </div>
	   
	  
  <div id="module_39_body" class="module_body">
    <div id="module_39_ul" class="module_div" style="height:auto;">
	    <ul id="knowledgegardenUl"><li></li>
			<script>
		    	function viewInfor(htmlFilePath){
						window.open(<%=request.getContextPath()%>htmlFilePath,'','');
					}
				    jQuery().ready(function (){
				    	$.getJSON("/cms/inforDocument.do?method=getInforDocument&categoryId="+<%=InforConstant.Cms_Category_Knowledgegarden %>+"&sidx=inforId&sord=asc&_search=false&page=1&rows=10",function(data) {
							//需要首页显示的发布信息
							var content = "";
							$.each(data.rows, function(i, n) {
								content += "<li><a href='javascript:;' onclick='viewInfor(\""+n.htmlFilePath+"\");'> "+n.inforTitle+" ("+ n.author.person.personName + " " +n.createTime+")</a>";
								if (n.important) {
									content += " <img src='<c:url value='/'/>images/flag_red.gif' border='0'/>";
								}
								content += "</li>";
							});	
							$('#knowledgegardenUl').html(content);
						});
				    });
		    </script>
	    </ul>
	</div>
  </div>
</div>-->

<!-------------------- 常用网址 ----------------------->
<%--<c:if test="${_SYSTEM_USER.userType != 2 && _SYSTEM_USER.userType != 3}">
--%>
<c:if test="${_NORMAL_USER}">
<div id="module_42" class="module listColor">
  <div class="head">
	  <h4 id="module_42_head" class="moduleHeader">
	      <a href="javascript:_resize(42);" class="expand"><img class="icon" id="img_resize_42" src="images/mytable_ad.png" title="折叠" /></a>
	      <span id="module_42_text" class="text" onclick="_resize(42);">常用网址</span>
	      <span id="module_42_title" class="title"  style="cursor:move;"> </span>
	      <span id="module_42_op" class="close">&nbsp;&nbsp;&nbsp;<a href="javascript:_edit();">设置</a>&nbsp;</span>
	  </h4>
  </div>
  
  <div id="module_42_body" class="module_body" style="overflow:hidden; height:30px;">
    <div id="module_42_ul" class="module_div" style="height:100px;">
		<ul>
			<li><a href="http://www.portcontainer.com" target="_blank">中国港口集装箱网</a></li>
			<li><a href="http://www.portshanghai.com.cn" target="_blank">上港集团</a></li>
		</ul>
	</div>
	<div id="addroll"></div>
  </div>
  </c:if>
  <!-- <script type="text/javascript">
			var speed=40;//数值越大，速度越慢
			var demo2=document.getElementById("addroll");
			var demo1=document.getElementById("module_42_ul");
			var demo=document.getElementById("module_42_body");
			
			var nnn=200/demo1.offsetHeight;
	
			for(i=0;i<nnn;i++){demo1.innerHTML+="<br />"+ demo1.innerHTML}
			demo2.innerHTML = demo1.innerHTML;    //克隆demo2为demo1
			function Marquee(){
				if(demo2.offsetTop-demo.scrollTop<=0)    //当滚动至demo1与demo2交界时
					demo.scrollTop-=demo1.offsetHeight;    //demo跳到最顶端
				else{
					demo.scrollTop++;
				}
			}
			var MyMar = setInterval(Marquee,speed);        //设置定时器
			demo.onmouseover = function(){clearInterval(MyMar);}    //鼠标经过时清除定时器达到滚动停止的目的
			demo.onmouseout = function(){MyMar = setInterval(Marquee,speed);}    //鼠标移开时重设定时器
		
		
		/*demo2.innerHTML=demo1.innerHTML;
		demo.scrollTop=demo.scrollHeight;
		function MarqueeUp(){
		if(demo2.offsetTop-demo.scrollTop<=0)
		demo.scrollTop-=demo2.offsetHeight;
		else{
		demo.scrollTop++;
		}
		}
		var MyMar=setInterval(MarqueeUp,speed);
		demo.onmouseover=function() {clearInterval(MyMar);}
		demo.onmouseout=function() {MyMar=setInterval(MarqueeUp,speed);}*/
  </script> -->
  
</div>



<div class="shadow"></div>
  </td>
 </tr>
</tbody></table>
<script>
<!--
_upc(2);
//>
</script>

<div id="livemargins_control" style="position: absolute; display: none; z-index: 9999;">
		<img height="5" width="77" style="position: absolute; left: -77px; top: -5px;" src="chrome://livemargins/skin/monitor-background-horizontal.png">	
		<img style="position: absolute; left: 0pt; top: -5px;" src="chrome://livemargins/skin/monitor-background-vertical.png">	
		<img style="position: absolute; left: 1px; top: 0pt; opacity: 0.5; cursor: pointer;" onmouseout="this.style.opacity=0.5" onmouseover="this.style.opacity=1" src="chrome://livemargins/skin/monitor-play-button.png" id="monitor-play-button">
</div>

</body></html>