<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<html>
<head>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">

<link href="<c:url value="/"/>css/theme/7/style.css" type="text/css" rel="stylesheet">
<link href="<c:url value="/"/>css/theme/7/menu.css" type="text/css" rel="stylesheet">

<title>主菜单</title>

<script src="<c:url value="/"/>inc/ccorrect_btn.js"></script>
<script>
function my_on_status()
{
	location="menu.php?ISPIRIT=&amp;ON_STATUS_SET="+ON_STATUS.value;
}
function displayUL(UlName) {
	var Uls = document.getElementsByName(UlName);
	for (var i=0;i<Uls.length;i++) {
		if (Uls[i].style.display == 'none') {
			Uls[i].style.display = 'block';
		}else {
			Uls[i].style.display = 'none';
		}
	}
}
</script>
</head>

<body class="panel">
<div id="body">
<!-- OA树开始-->
<%--<a href="javascript:menu_expand();" id="expand_link"><u><span id="expand_text">展开</span></u></a>--%>

<ul id="menu">
	<%--<c:if test="${_SYSTEM_USER.userType != 2 && _SYSTEM_USER.userType != 3}">
		--%>
	<c:if test="${_NORMAL_USER}">
		<li class="L1">
			<a id="m01" href="javascript:c('m01');">
				<span><img height="17" border="0" align="absMiddle" width="19" alt="个人办公" src="images/menu/mytable.gif"> 个人办公</span>
			</a>
		</li>
		
		<ul class="U1" id="m01d">
			<%--<li class="L21">--%>
					<%--<a id="f46" href="javascript:a('/personal/message/messageBaseList.jsp','46',0);">--%>
					<%--<span><img height="17" border="0" align="absMiddle" width="19" alt="短讯息" src="images/menu/@sms.gif"> 短讯息</span>--%>
				<%--</a>--%>
			<%--</li>--%>
			
		  	<li class="L22"><a id="f8" href="javascript:a('/personal/schedule/calendarBaseList.jsp','8',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="日程安排" src="images/menu/calendar.gif"> 日程安排 </span></a></li>
			
			<%--<li class="L22">
				<a id="f9" href="javascript:c('f9');"><!-- javascript:a('/personal/basePersonalAddressList.jsp','9',0); -->
					<span>
						<img height="17" border="0" align="absMiddle" width="19" alt="个人通讯录" src="images/menu/address.gif"> 
						个人通讯录 
					</span>
				</a>
			</li>
			<ul style="display: none;" id="f9d">
				<li class="L3">
					<a id="f999" href="javascript:a('/personal/address/basePersonalAddressList.jsp','999',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="通讯录查看" src="images/menu/work_plan.gif"> 通讯录查看</span></a>
				</li>
				<li class="L3">
					<a id="f9999" href="javascript:a('/personal/address/listAddressCategory.jsp','9999',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="通讯录分类" src="images/menu/work_plan.gif"> 通讯录分类</span></a>
				</li>
			</ul>--%>
			
			
			<function:viewIf alias="companyAddress" method="list">
			<li class="L22">
				<a id="f106" href="javascript:a('/personal/address/listCompanyAddress.jsp','106',0);">
					<span>
						<img height="17" border="0" align="absMiddle" width="19" alt="公司通讯录" src="images/menu/address.gif"> 
						 公司通讯录 
					</span>
				</a>
			</li>
			</function:viewIf>
			
			<li class="L22">
				<a id="f1060" href="javascript:a('/personal/address/companyAddress-180202.htm','1060',0);"><!-- javascript:displayUL('c1'); -->
					<span>
						<img height="17" border="0" align="absMiddle" width="19" alt="公司电话一览" src="images/menu/flowCat.gif"> 
						公司电话一览
					</span>
				</a>
			</li>
			<ul style="display: none;" name="c1" id="c1">
				<li class="L3">
					<a id="f1061" href="javascript:a('/personal/address/matou.html','1061',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="码头" src="images/menu/flowNam.gif"> 码头</span></a>
				</li>
				<li class="L3">
					<a id="f1062" href="javascript:a('/personal/address/listAddress.jsp','1062',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="物流" src="images/menu/flowNam.gif"> 物流</span></a>
				</li>
				<li class="L3">
					<a id="f1063" href="javascript:a('/personal/address/listAddress.jsp','1063',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="子公司" src="images/menu/flowNam.gif"> 子公司</span></a>
				</li>
			</ul>
			
			
			<li class="L22"><a id="f19" href="javascript:a('/core/systemUserInfor.do?method=editPassword','19',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="修改密码" src="images/menu/diary.gif"> 修改密码 </span></a></li>
			<li class="L22"><a id="f20" href="javascript:a('/core/personInfor.do?method=editPersonalInfor','20',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="修改个人信息" src="images/menu/diary.gif"> 修改个人信息 </span></a></li>
			</ul>
	
		<function:viewIf alias="dailyOffice" method="list">
			<li class="L1">
				<a id="m044" href="javascript:c('m044');">
					<span><img height="17" border="0" align="absMiddle" width="19" alt="日常办公" src="images/menu/@source.gif"> 日常办公</span>
				</a>
			</li>
		</function:viewIf>
		
		<ul class="U1" style="display: none;" id="m044d">
			<function:viewIf alias="appImg" method="list">
				<li class="L22"><a id="f1307" href="javascript:a('/core/base/listAppImg.jsp','1307',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="app首页图片" src="images/menu/book.gif"> app首页图片 </span></a></li>
			</function:viewIf>
			<function:viewIf alias="meeting" method="list">
				<li class="L22"><a id="f1300" href="javascript:a('/meeting/listMeeting.jsp','1300',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="会务安排" src="images/menu/book.gif"> 会务安排 </span></a></li>
			</function:viewIf>
			<function:viewIf alias="annouce" method="list">
				<li class="L22"><a id="f1301" href="javascript:a('/cms/annouce.do?method=list','1301',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="公司公告" src="images/menu/@bbs.gif"> 公司公告 </span></a></li>
			</function:viewIf>
			<function:viewIf alias="companynews" method="list">
				<li class="L22"><a id="f1302" href="javascript:a('/cms/companynews.do?method=list','1302',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="参考消息" src="images/menu/@bbs.gif"> 参考消息 </span></a></li>
			</function:viewIf>
			<function:viewIf alias="important" method="list">
				<li class="L22"><a id="f1303" href="javascript:a('/cms/important.do?method=list','1303',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="每日情况" src="images/menu/@bbs.gif"> 每日情况 </span></a></li>
			</function:viewIf>
			<function:viewIf alias="addSms" method="list">
				<li class="L22"><a id="f1304" href="javascript:a('/sms/addMessage.jsp','1304',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="手机短信发送" src="images/menu/book.gif"> 手机短信发送 </span></a></li>
			</function:viewIf>
			<function:viewIf alias="querySms" method="list">
				<li class="L22"><a id="f1305" href="javascript:a('/sms/messageQuery.jsp','1305',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="手机短信记录" src="images/menu/book.gif"> 手机短信记录 </span></a></li>
			</function:viewIf>
			
			<li class="L22"><a id="f1309" href="javascript:a('/bbs/bbsBaseList.jsp?tabID=1','1309',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="主题论坛" src="images/menu/book.gif"> 主题论坛 </span></a></li>
			
			<!--<function:viewIf alias="BBSMenu" method="list">
				<li class="L22">
					<a id="f1309" href="javascript:c('f1309');">
						<span>
							<img height="17" border="0" align="absMiddle" width="19" alt="主题论坛" src="images/menu/book.gif"> 
							主题论坛
						</span>
					</a>
				</li>
				<ul style="display: none;" id="f1309d">
					
						<li class="L3">
							<a id="f13091" href="javascript:a('/bbs/listThesisInfor.jsp','13091',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="意见建议" src="images/menu/sms.gif"> 论题列表</span></a>
						</li>
					<li class="L3">
						<a id="f13092" href="javascript:a('/bbs/listMyThesisInfor.jsp','13092',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="意见建议" src="images/menu/sms.gif"> 我的论题</span></a>
					</li>
					<li class="L3">
						<a id="f13093" href="javascript:a('/bbs/commentInfor.do?method=myList','13093',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="意见建议" src="images/menu/sms.gif"> 我的评论</span></a>
					</li>
				</ul>
			</function:viewIf>-->
			
			
		</ul>
		
		<li class="L1"><!-- m033 -->
			<a id="f1306" href="javascript:a('cms/normaldata.do?method=list','1306',0);">
				<span><img height="17" border="0" align="absMiddle" width="19" alt="常用资料" src="images/menu/world_time.gif"> 常用资料</span>
			</a>
		</li>
		
		<c:if test="${!empty _CategoryMenu}">
			<li class="L1"><a id="m09" href="javascript:c('m09');"><span><img height="17" border="0" align="absMiddle" width="19" alt="信息发布" src="images/menu/comm.gif"> 信息发布</span></a></li>
		
			<ul class="U1" style="display: none;" id="m09d">
				<c:forEach var="category" items="${_CategoryMenu}" varStatus="index">
					<li class="L22"><a id="f15${index.index}" href="javascript:a('/cms/${category.urlPath}.do?method=list','15${index.index}',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="${category.categoryName}" src="images/menu/@bbs.gif"> ${category.categoryName} </span></a></li>
				</c:forEach>
			</ul>
		</c:if>
	
		<function:viewIf alias="workflow" method="list">
			<li class="L1">
				<a id="m03" href="javascript:c('m03');">
					<span><img height="17" border="0" align="absMiddle" width="19" alt="工作流" src="images/menu/workflow.gif"> 工作流</span>
				</a>
			</li>
		</function:viewIf>
	
		<ul class="U1" style="display: none;" id="m03d">
			<%--<li class="L22"><a id="f5" href="javascript:a('/workflow/instanceBaseList.jsp','5',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="审批流程" src="images/menu/workflow.gif"> 审批流程 </span></a></li>
			--%>
			<%-- 设置了流程分类的 --%>
			<c:forEach var="flowCategory" items="${_FlowCategorys}" varStatus="index">
				<li class="L22">
					<a href="javascript:displayUL('f${index.index}');">
						<span>
							<img height="17" border="0" align="absMiddle" width="19" alt="${flowCategory}" src="images/menu/flowCat.gif"> 
							${flowCategory}
						</span>
					</a>
				</li>
				<c:forEach var="flow" items="${_Flows}" varStatus="status">
					<c:if test="${!empty flow.categoryName && flow.categoryName == flowCategory}">
						<ul style="display: none;" name="f${index.index}" id="f${index.index}">
							<function:viewIf alias="${flow.flowName}" method="list">
								<li class="L3">
									<a id="f14${status.index}" href="javascript:a('/workflow/instanceInfor.jsp?flowId=${flow.flowId}&chargerId=${flow.charger.personId}','14${status.index}',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="${flow.flowName}" src="images/menu/flowNam.gif"> ${flow.flowName}</span></a>
								</li>
							</function:viewIf>
						</ul>
					</c:if>
				</c:forEach>
			</c:forEach>
			<%-- 未设置流程分类的 --%>
			<c:forEach var="flow" items="${_Flows}" varStatus="status">
				<function:viewIf alias="${flow.flowName}" method="list">
					<c:if test="${empty flow.categoryName}">
						<li class="L22"><a id="f13${status.index}" href="javascript:a('/workflow/instanceInfor.jsp?flowId=${flow.flowId}','13${status.index}',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="${flow.flowName}" src="images/menu/flowNam.gif"> ${flow.flowName} </span></a></li>
					</c:if>
				</function:viewIf>
			</c:forEach>
			<li class="L22"><a id="f156" href="javascript:a('/workflow/deletedInstance.jsp','156',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="回收站" src="images/menu/flowNam.gif"> 回收站 </span></a></li>
			<function:viewIf alias="flowDefinition" method="list">
				<li class="L22"><a id="f150" href="javascript:a('/workflow/listFlowDefinition.jsp','150',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="流程定义" src="images/menu/flowNam.gif"> 流程定义 </span></a></li>
			</function:viewIf>
		</ul>
	
		<function:viewIf alias="document" method="list">
			<li class="L1">
				<a id="m033" href="javascript:a('/document/treeDocument.jsp','1300',0);">
					<span><img height="17" border="0" align="absMiddle" width="19" alt="文档大全" src="images/menu/file_folder.gif"> 文档大全</span>
				</a>
			</li>
		</function:viewIf>
		
		<!--<function:viewIf alias="supervise" method="list">
			<li class="L1">
				<a id="m331" href="javascript:a('/supervise/taskBaseList.jsp','331',0);">
					<span><img height="17" border="0" align="absMiddle" width="19" alt="工作督办" src="images/menu/file_folder.gif"> 工作督办</span>
				</a>
			</li>
		</function:viewIf>-->
		<function:viewIf alias="supervise" method="list">
			<li class="L1"><a id="m331" href="javascript:c('m331');"><span><img height="17" border="0" align="absMiddle" width="19" alt="工作跟踪" src="images/menu/hrms.gif"> 工作跟踪</span></a></li>
		</function:viewIf>
	
		<ul class="U1" style="display: none;" id="m331d">
			<function:viewIf alias="adminTask" method="list">
				<li class="L21">
					<a id="f1221" href="javascript:a('/supervise/taskCategory.do?method=listBase&categoryType=1','1221',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="方针目标类" src="images/menu/@score.gif"> 方针目标类</span></a>
				</li>
			</function:viewIf>
			<function:viewIf alias="departmentTask" method="list">
				<li class="L21">
					<a id="f1223" href="javascript:a('/supervise/taskCategory.do?method=listBase&categoryType=3','1223',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="部门建设类" src="images/menu/@score.gif"> 部门建设类</span></a>
				</li>
			</function:viewIf>
			<function:viewIf alias="partyTask" method="list">
				<li class="L21">
					<a id="f1222" href="javascript:a('/supervise/taskCategory.do?method=listBase&categoryType=2','1222',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="党群类" src="images/menu/@score.gif"> 党群工作</span></a>
				</li>
			</function:viewIf>
			<function:viewIf alias="insideTask" method="list">
				<li class="L21">
					<a id="f1224" href="javascript:a('/supervise/taskCategory.do?method=listBase&categoryType=4','1224',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="内控类" src="images/menu/@score.gif"> 内控类</span></a>
				</li>
			</function:viewIf>
		</ul>
	
		<!--<function:viewIf alias="customerManager" method="list">
			<li class="L1"><a id="m10" href="javascript:c('m10');"><span><img height="17" border="0" align="absMiddle" width="19" alt="客户管理" src="images/menu/hrms.gif"> 客户管理</span></a></li>
		</function:viewIf>-->
	
		<ul class="U1" style="display: none;" id="m10d">
			<function:viewIf alias="customer" method="list">
				<li class="L21">
					<a id="f122" href="javascript:a('/customer/customerBaseList.jsp','122',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="客户信息" src="images/menu/@score.gif"> 客户信息</span></a>
				</li>
			</function:viewIf>
			<function:viewIf alias="activity" method="list">
				<li class="L21">
					<a id="f123" href="javascript:a('/customer/activityBaseList.jsp','123',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="活动信息" src="images/menu/@score.gif"> 活动信息</span></a>
				</li>
			</function:viewIf>
			<function:viewIf alias="customerManager" method="list">
				<li class="L21">
					<a id="f124" href="javascript:a('/customer/treeProject.jsp','124',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="项目" src="images/menu/@score.gif"> 项目</span></a>
				</li>
			</function:viewIf>
			<function:viewIf alias="customerManager" method="list">
				<li class="L21">
					<a id="f125" href="javascript:a('/customer/taskBaseList.jsp','125',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="任务信息" src="images/menu/@score.gif"> 任务信息</span></a>
				</li>
			</function:viewIf>
		</ul>
	
		<function:viewIf alias="baseMaintain" method="list">
			<li class="L1"><a id="m30" href="javascript:c('m30');"><span><img height="17" border="0" align="absMiddle" width="19" alt="系统维护" src="images/menu/info_query.gif"> 系统维护</span></a></li>
		</function:viewIf>
		<%--<ul class="text">
			<li></li>
		</ul>--%>
		<ul class="U1" style="display: none;" id="m30d">
			<function:viewIf alias="base" method="list">
				<li class="L21"><a id="f46" href="javascript:a('/core/base/baseList.jsp','46',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="基本维护" src="images/menu/@unit.gif"> 基本维护 </span></a></li>
			</function:viewIf>
			<function:viewIf alias="documentCategory" method="list">
				<li class="L21"><a id="f26" href="javascript:a('/document/documentCategory.do?method=list','26',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="文档分类" src="images/menu/file_folder.gif"> 文档分类 </span></a></li>
			</function:viewIf>
			<function:viewIf alias="cmsCategory" method="list">
				<li class="L21"><a id="f27" href="javascript:a('/cms/inforCategory.do?method=list','27',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="资讯分类" src="images/menu/@source.gif"> 资讯分类 </span></a></li>
			</function:viewIf>
			<function:viewIf alias="taskCategory" method="list">
				<li class="L21"><a id="f271" href="javascript:a('/supervise/taskCategory.do?method=list','271',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="工作跟踪分类" src="images/menu/file_folder.gif"> 工作跟踪分类 </span></a></li>
			</function:viewIf>
			<function:viewIf alias="loginLog" method="list">
				<li class="L21"><a id="f272" href="javascript:a('/extend/loginLog/logBaseList.jsp','272',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="登录日志" src="images/menu/comm.gif"> 登录日志 </span></a></li>
			</function:viewIf>
			<%--<li class="L21"><a id="f28" href="javascript:a('/cms/templateBaseList.jsp?templateStyle=cms','28',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="资讯模板" src="images/menu/person_info.gif"> 资讯模板 </span></a></li>
			<li class="L21"><a id="f29" href="javascript:a('/cms/templateBaseList.jsp?templateStyle=workflow','29',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="流程模板" src="images/menu/person_info.gif"> 流程模板 </span></a></li>--%>
		</ul>
		
		
		<!--------------------------------- 海通添加 ----------------------------------->
		<li class="L1"><a id="m31" href="javascript:c('m31');"><span><img height="17" border="0" align="absMiddle" width="19" alt="专题栏目" src="images/menu/mydesign.gif"> 专题栏目</span></a></li>
		
		<ul class="U1" style="display: none;" id="m31d">
			<li class="L22"><a id="f28" href="javascript:a('images/sqyj.bmp','28',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="上汽愿景" src="images/menu/game.gif"> 上汽愿景 </span></a></li>
			<li class="L22"><a id="f29" href="http://localhost:8085/bbs" target="_blank"><span><img height="17" border="0" align="absMiddle" width="19" alt="海通论坛" src="images/menu/game.gif"> 海通论坛 </span></a></li>
			<li class="L22"><a id="f30" href="javascript:a('cms/honor.do?method=list','30',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="光荣榜" src="images/menu/game.gif"> 光荣榜 </span></a></li>
			<li class="L22"><a id="f31" href="javascript:a('cms/newspaper.do?method=list','31',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="上级报刊" src="images/menu/game.gif"> 上级报刊 </span></a></li>
			<li class="L22"><a id="f32" href="javascript:a('cms/warrant.do?method=list','32',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="常用凭证" src="images/menu/game.gif"> 常用凭证 </span></a></li>
			<li class="L22"><a id="f33" href="javascript:a('images/qiyewenhua.jsp','33',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="企业文化" src="images/menu/game.gif"> 企业文化 </span></a></li>
			<li class="L22"><a id="f34" href="javascript:a('cms/job.do?method=list','34',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="岗位改进" src="images/menu/game.gif"> 岗位改进 </span></a></li>
			<li class="L22"><a id="f35" href="javascript:a('cms/ensystem.do?method=list','35',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="企业内控体系建设" src="images/menu/game.gif"> 企业内控体系建设 </span></a></li>
			<li class="L22"><a id="f36" href="javascript:a('cms/political.do?method=list','36',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="党员承诺公示栏" src="images/menu/game.gif"> 党员承诺公示栏 </span></a></li>
			
			<%--<li class="L21"><a id="f28" href="javascript:a('/cms/templateBaseList.jsp?templateStyle=cms','28',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="资讯模板" src="images/menu/person_info.gif"> 资讯模板 </span></a></li>
			<li class="L21"><a id="f29" href="javascript:a('/cms/templateBaseList.jsp?templateStyle=workflow','29',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="流程模板" src="images/menu/person_info.gif"> 流程模板 </span></a></li>--%>
		</ul>
	</c:if>
	
	<!----------------------------------------  ----------------------------------->
	<li class="L1"><a id="m32" href="javascript:c('m32');"><span><img height="17" border="0" align="absMiddle" width="19" alt="投票问卷" src="images/menu/mydesign.gif"> 投票问卷</span></a></li>
	
	<ul class="U1" style="display: none;" id="m32d">
		<li class="L22"><a id="f37" href="javascript:a('/extend/tpwj/listTopicInfor.jsp?type=0','37',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="投票" src="images/menu/game.gif"> 投票 </span></a></li>
		<li class="L22"><a id="f38" href="javascript:a('/extend/tpwj/listTopicInfor.jsp?type=1','38',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="问卷" src="images/menu/game.gif"> 问卷 </span></a></li>
		<li class="L22"><a id="f39" href="javascript:a('/extend/evaluationvote/listTopicInfor.jsp','39',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="评优系统" src="images/menu/game.gif"> 评优系统 </span></a></li>
	</ul>

	<!--<function:viewIf alias="club" method="list">-->
	<c:if test="${_NORMAL_USER || _TMP_USER}">
		<li class="L1"><!-- m033 -->
			<a id="f13061" href="javascript:a('/club/listClubInfor.jsp','13061',0);">
				<span><img height="17" border="0" align="absMiddle" width="19" alt="主题活动" src="images/menu/mydesign.gif"> 主题活动</span>
			</a>
		</li>
	</c:if>
	<!--</function:viewIf>-->
</ul>
</div>
<div id="bottom"></div>

<script src="<c:url value="/"/>inc/js/utility.js"></script>
<script language="JavaScript">
var cur_id="",cur_expand="";
var flag=0,sflag=0;

//-------- 菜单点击事件 -------
function c(id)
{
  var targetid,targetelement;
  var strbuf;

  var el=$(id);
  if(!el)
     return;
  //-------- 如果点击了展开或收缩按钮---------
  targetid=el.id+"d";
  targetelement=document.getElementById(targetid);
  var expandUL=document.getElementById(cur_expand+"d");
  var expandLink=document.getElementById(cur_expand);

  if (targetelement.style.display=="none")
  {
     el.className="active";
     targetelement.style.display='';

     menu_flag=0;
     //$("expand_link").src="<c:url value="/"/>images/green_minus.gif";
  }
  else
  {
     el.className="";
     targetelement.style.display="none";

     menu_flag=1;
     //$("expand_link").src="<c:url value="/"/>images/green_plus.gif";
     var links=document.getElementsByTagName("A");
     for (i=0; i<links.length; i++)
     {
       el=links[i];
       if(el.parentNode.className.toUpperCase()=="L1" && el.className=="active" && el.id.substr(0,1)=="m")
       {
          menu_flag=0;
          //$("expand_link").src="<c:url value="/"/>images/green_minus.gif";
          break;
       }
     }
  }
}
function set_current(id)
{
   //先将原有的样式清除
   cur_link=document.getElementById("f"+cur_id)
   if(cur_link)
      cur_link.className="";
   //再给指定页面设置样式active,并将指定id赋值给cur_id
   cur_link=document.getElementById("f"+id);
   if(cur_link)
      cur_link.className="active";
   cur_id=id;
}

function a(URL,id,open_window)
{
   set_current(id);
   //if(URL.substr(0,7)!="http://" && URL.substr(0,6)!="ftp://")
   //URL = "/general/"+URL;
   parent.openURL(URL,open_window);
}
function b(URL,id)
{
   set_current(id);
   URL = "/app/"+URL;
   parent.openURL(URL,0);
}
//-------- 菜单全部展开/收缩 -------
var menu_flag=1;
function menu_expand()
{
  if(menu_flag==1)
     expand_text.innerHTML="收缩";
  else
     expand_text.innerHTML="展开";

  menu_flag=1-menu_flag;

  var links=document.getElementsByTagName("A");
  for (i=0; i<links.length; i++)
  {
    var el=links[i];
    if(el.parentNode.className.toUpperCase()=="L1" || el.parentNode.className.toUpperCase()=="L21")
    {
      var elUL=$(el.id+"d");
      if(menu_flag==0)
      {
        elUL.style.display='';
        el.className="active";
      }
      else
      {
        elUL.style.display="none";
        el.className="";
      }
    }
  }
}

//-------- 打开windows程序 -------
function winexe(NAME,PROG)
{
   URL="/general/winexe?PROG="+PROG+"&amp;NAME="+NAME;
   window.open(URL,"winexe","height=100,width=350,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,top=0,left=0,resizable=no");
}
</script>

<div id="livemargins_control" style="position: absolute; display: none; z-index: 9999;">
	<img height="5" width="77" style="position: absolute; left: -77px; top: -5px;" src="chrome://livemargins/skin/monitor-background-horizontal.png">	
	<img style="position: absolute; left: 0pt; top: -5px;" src="chrome://livemargins/skin/monitor-background-vertical.png">	
	<img style="position: absolute; left: 1px; top: 0pt; opacity: 0.5; cursor: pointer;" onmouseout="this.style.opacity=0.5" onmouseover="this.style.opacity=1" src="chrome://livemargins/skin/monitor-play-button.png" id="monitor-play-button">
</div>

</body></html>