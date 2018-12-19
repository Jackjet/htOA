<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<title>投票信息</title>
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
	});
	//提交数据
	function submitData() {
		
		if(check()){
			var form = document.voteInforForm;
			form.submit();
			alert('信息编辑成功！');
			window.returnValue = "refresh";
			window.close();
		}
	}
	
	//查看某个人的投票信息
	function fillData(){
		if("${!empty _VoteInfor}" == 'true'){
		
			<c:forEach items="${_TopicInfor.personInfors}" var="person" varStatus="index">
			var personId = "${person.PId}";
			<c:forEach items="${_TopicInfor.itemInfors}" var="item" varStatus="index">
				//判断题型
				var itemType = "${item.itemType}";
				var itemId = "${item.itemId}";
				
			
					<c:if test="${!empty _VoteInfor.voteItemInfors}">
						
						<c:forEach items="${_VoteInfor.voteItemInfors}" var="voteItem">
							<c:if test="${voteItem.itemInfor.itemId == item.itemId &&  voteItem.personInfor.PId == person.PId}">
								<c:forEach items="${fn:split(voteItem.voteValue,',')}" var="voteValue">
    								var voteValue = "${voteValue}";
    								if(itemType == "0"){
    								
    									$(":radio[name='option_"+itemId+"_"+personId+"'][value='"+voteValue+"']").attr("checked","true");
    								}
    								if(itemType == "1"){
    									$(":checkbox[name='option_"+itemId+"_"+personId+"'][value='"+voteValue+"']").attr("checked","true");
    								}
	    						</c:forEach>
							</c:if>
						</c:forEach>
					</c:if>
				
			</c:forEach>
			</c:forEach>
		}
	}	
	
</script>
<style>
	img{
		border:0;
	}
	li{
		list-style:none;
	}
	input,label { vertical-align:middle;} 
</style>
<base target="_self"/>
<body onload="fillData();">
<br/>
<form id="voteInforForm" name="voteInforForm" action="/tpwj/voteInfor.do?method=save" method="post">
<input type="hidden" name="topicId" id="topicId" value="${_TopicInfor.topicId}"/>

<table style="float:left;" border=1 bordercolor="#c5dbec" width="15%">
	<tr onclick="disUsers('votedUsers');" style="cursor:pointer;">
		<th bgcolor="#dfeffc" height="40" colspan=3>
			<font color="blue" style="font-size:15px;font-family:微软雅黑;"><b>已投票人员</b></font>
			<br/>
			<font color=gray style="font-family:微软雅黑;">（点击显示/隐藏）</font>
		</th>
	</tr>
	
	<tbody id="votedUsers">
	<c:forEach items="${_TopicInfor.voteInfors}" var="voteInfor" varStatus="index">
		<tr>
			<td bgcolor="#fafafa" align="center" colspan=3 style="font-family:微软雅黑;">
				<c:if test="${voteInfor.person.personId == _VoteInfor.person.personId}">
					<font color=orange style="font-size:14px;"><b>
						${index.index+1}、${voteInfor.person.person.personName}
						<br/><font color=gray>（${fn:substring(voteInfor.voteTime,0,19)}）</font>
					</b></font>
				</c:if>
				<c:if test="${voteInfor.person.personId != _VoteInfor.person.personId}">
					<a title="点击查看此人投票信息" href="/extend/pyTopicInfor.do?method=viewVoters&topicId=${_TopicInfor.topicId}&voterId=${voteInfor.person.personId}">${index.index+1}、${voteInfor.person.person.personName}</a>
					<br/><font color=gray>（${fn:substring(voteInfor.voteTime,0,19)}）</font>
				</c:if>
			</td>
		</tr>
	</c:forEach>
	</tbody>
	
	<script>
		function disUsers(id){
			$("#"+id).toggle();
		}
	</script>
	
	<tr onclick="disUsers('notVoteUsers');" style="cursor:pointer;">
		<th bgcolor="#dfeffc" height="40" colspan=3><font color="red" style="font-size:15px;font-family:微软雅黑;"><b>未投票人员</b></font>
			<br/>
			<font color=gray style="font-family:微软雅黑;">（点击显示/隐藏）</font>
		</th>
	</tr>
	<tbody id="notVoteUsers" style="display:none;font-family:微软雅黑;">
	<tr>
		<c:set var="notVoteIndex" value="0" />
		<c:forEach items="${_NotVoteUsers}" var="user" varStatus="index">
			<td bgcolor="#fafafa" align="center">
				${user.person.personName}
			</td>
			<c:if test="${(notVoteIndex+1) % 3 == 0}">
				</tr><tr>
			</c:if>
			<c:set var="notVoteIndex" value="${notVoteIndex+1}" />
		</c:forEach>
	</tr>
	</tbody>
	
</table>
<table style="float:left;" width="80%" cellpadding="6" cellspacing="1" align="center" border="1" bordercolor="#c5dbec">
	<tr>
		<td bgcolor="#dfeffc" align="center">
			<h2><font color="#15A7BC">${_TopicInfor.topicName}</font></h2>
			<div style="text-align:left;padding-left:10px;"><font color=gray>${_TopicInfor.descrip}</font></div>
			<c:if test="${!empty _TopicInfor.ruler}">
				<br/>
				<div style="text-align:left;padding-left:10px;"><font color=orange><b>投票规则：</b></font><font color=gray>${_TopicInfor.ruler}</font></div>
			</c:if>
			<br/>
			<div style="text-align:left;padding-left:10px;"><font color=orange><b>投票人：</b></font><font><b>${_VoteInfor.person.person.personName}</b></font></div>
		</td>
	</tr>
	

	
		<tr>
		<td valign=top style="padding-top:10px;">
			<table id="optionTab" class="optionTab" width="90%" border="1">
				<tr>
					<td width="5%" nowrap align="center">序号</td>
					<td align="center">部门</td>
					<td align="center">姓名</td>
					<td align="center">项目名称</td>
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
						
						<td align="center">
						<c:if test="${item.itemType==0}">
						<input type="radio" name="option_${item.itemId}_${person.PId }" value="${option.optionId}" onclick='isCheck(this)' />
						</c:if>
						<c:if test="${item.itemType==1}">
						<input type="checkbox" name="option_${item.itemId}_${person.PId }" value="${option.optionId}" />
						</c:if>
						
						</td>
						</c:forEach>	
					</c:forEach>
					</tr>
				</c:forEach>
				
			</table>
		</td>
	</tr>
                    
     <tr> 
        <td bgcolor="#dfeffc" align=left>
          <!--<input type="button" id="button" style="cursor: pointer;" onclick="submitData();" value="提交"/>
             &nbsp;
          --><input type="button" value="关闭" style="cursor: pointer;" onclick="window.close();"/>
        </td>
     </tr>
</table>
</form>
</body>
