<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<title>编辑评优信息</title>
<script src="<c:url value="/"/>datePicker/WdatePicker.js" charset="UTF-8" language="JavaScript"></script>
<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
<script src="<c:url value="/"/>js/inc_javascript.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/css/form.css"/>"/>
<link rel="stylesheet" type="text/css" href="<c:url value="/css/myTable.css"/>"/>

<!-- formValidator -->
<link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css"></link>
<script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript" charset="UTF-8"></script>

<!-- ------------- -->

<script>
	
	$(document).ready(function(){
		//验证
		$.formValidator.initConfig({formid:"topicInforForm",onerror:function(msg){alert(msg)},onsuccess:function(){submitData();}});
		$("#topicName").formValidator({onshow:"请输入主题名",onfocus:"主题名不能为空",oncorrect:"输入正确"}).inputValidator({min:1,onerror:"主题名不能为空,请确认"});
		
		//勾选选中的用户
		var personIds = document.getElementsByName('personIds');
		<c:forEach var="personId" items="${_PersonIds}">
			var tmpPersonId = '${personId}';
			if (personIds != null && personIds.length > 0) {
				for (var i=0;i<personIds.length;i++) {
					var personId = personIds[i];
					if (tmpPersonId == personId.value) {
						personId.checked = true;
					}
				}
			}
		</c:forEach>
	});
	
	//提交数据
	function submitData() {
		alert('信息编辑成功！');
		window.returnValue = "refresh";
		window.close();
	}
	
	var i=0,number;
	//全选操作
	function selectUserId(checkbox,organizeId){
		var isChecked = false;
		if(checkbox.checked){
			isChecked = true;
		}
		var obj;
		obj = document.topicInforForm.personIds;		
		if(obj!=null){
			if(obj.length==null){
				//只有一个,则只需要判断该用户是不是这个分类
				<c:forEach var="user" items="${_Users}" varStatus="index">
					var tempOrganizeId = '${user.person.department.organizeId}';
					if(organizeId==tempOrganizeId){
						obj.checked = isChecked;
					}
				</c:forEach>
				
				<c:forEach var="user" items="${_OtherUsers}" varStatus="index">
					var tempOrganizeId = '${user.person.department.organizeId}';
						
					if(organizeId==tempOrganizeId){
						obj.checked = isChecked;
					}
				</c:forEach>
			}else{
				//多个用户
				var personNum;
				personNum = 0;
				var personNum = obj.length;			
				for(var k = 0; k<personNum;k++){
					var userId;
					personId = obj[k].value;
					<c:forEach var="user" items="${_Users}" varStatus="index">
						var tempOrganizeId = '${user.person.department.organizeId}';
						var tempPersonId = '${user.person.personId}';
							
						if(organizeId==tempOrganizeId && tempPersonId==personId){
							obj[k].checked = isChecked;
						}
					</c:forEach>
						
					<c:forEach var="user" items="${_OtherUsers}" varStatus="index">
						var tempOrganizeId = '${user.person.department.organizeId}';
						var tempPersonId = '${user.person.personId}';
							
						if(organizeId==tempOrganizeId && tempPersonId==personId){
							obj[k].checked = isChecked;
						}
					</c:forEach>
				}
			}				
		}
	}
	//显示或隐藏用户信息
	function hdUsers() {
		var form = document.topicInforForm;
		var openType = form.openType.value;
		var users = document.getElementById('users');
		
		if (openType == 0) {
			users.style.display = '';
		}else {
			users.style.display = 'none';
		}
	}
</script>
<base target="_self"/>
<body onload="hdUsers();">
<br/>
<form:form commandName="pyTopicInforVo" id="topicInforForm" name="topicInforForm" action="/extend/pyTopicInfor.do?method=save" method="post">
<form:hidden path="topicId"/>
<table width="98%" cellpadding="6" cellspacing="1" align="center" border="0" bordercolor="#c5dbec">
	<tr>
		<td colspan="3" ></td>
	</tr>
	
	<tr> 
       <td width="20%">主题名称：</td>
       <td><form:input path="topicName" size="80"/></td>
       <td><div id="topicNameTip" style="width:250px"></div></td>
    </tr>
    
    <tr> 
       <td>开始时间：</td>
       <td colspan="2">
         <form:input path="startTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly="true"/>
       </td>
    </tr>
     <tr> 
       <td>结束时间：</td>
       <td colspan="2">
        <form:input path="endTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly="true"/>
       </td>
    </tr>
                    
    <tr> 
       <td>是否有效：</td>
       <td colspan="2">
           <form:select path="valid">
			  <form:option value="true">有效</form:option>
			  <form:option value="false">无效</form:option>
		   </form:select>
       </td>
    </tr>
    
       <tr> 
       <td>同部门规避：</td>
       <td colspan="2">
           <form:select path="sameDept">
			  <form:option value="true">是</form:option>
			  <form:option value="false">否</form:option>
		   </form:select>
       </td>
    </tr>
    
   <tr> 
       <td>描述：</td>
       <td colspan="2">
        <form:textarea path="descrip" cols="65" rows="8"/>
       </td>
    </tr>
    <tr> 
       <td>投票规则：</td>
       <td colspan="2">
        <form:textarea path="ruler" cols="65" rows="8"/>
       </td>
    </tr>             
    <tr> 
       <td>是否公开：</td>
       <td colspan="2">
           <form:select path="openType" onchange="hdUsers();">
			  <form:option value="0">自定义</form:option>
			  <form:option value="1">全体用户</form:option>
		   </form:select>
       </td>
    </tr>
                    
    <script language="javaScript">
		//定义一个数组，记录各个数据点击的次数
		var clickTimes =new Array();	
	</script>	
					  
    <c:if test="${!empty _Users}">                    
        <tr id="users" valign="top"> 
           <td>包含用户：</td>
           <td colspan="2"> 
              <table width="100%">
				<c:set var="_Num" value="0"/>
				<c:forEach var="department" items="${_Departments}">
	                <tr height="33">
	                   <td valign="bottom" width="100%" style="border-bottom:1px dotted #888888;font-size:10pt" colspan="2">
	                     
						  <script language="javaScript">
							clickTimes[${_Num}] = 0;							
						  </script>	
	                        ${department.organizeName}&nbsp;	                            				
							<span onclick="show_list('${_Num}')">
								<img name="img" src="<c:url value="${'/images'}"/>/xpexpand3_s.gif" style="margin-top:0px;margin-bottom:0px;"/>
							</span>
						</td>
		      		</tr>
							
					<tr name="tr" style="display:none;padding-top:10px;">
						<td width="9%" align="right" valign="top">
		      				<input type="checkbox" onclick="selectUserId(this,'${department.organizeId}')"/>全选
		      			</td>
		      			<td style="padding-left:10px;" width="92%">
							<table>
		      					<tr>
		      						<c:set var="_TypeNum" value="0"/>
		      						<c:forEach var="user" items="${_Users}" varStatus="index">
		      							<c:if test="${user.person.department.organizeId==department.organizeId}">
											<c:if test="${_TypeNum!=0 && _TypeNum%6==0}">
												</tr><tr>
											</c:if>
											<td width="16%" valign="top">
												<input type="checkbox" name="personIds" value="${user.personId}"/> ${user.person.personName}
											</td>				
											<c:set var="_TypeNum" value="${_TypeNum+1}"/>
										</c:if>																			
									</c:forEach>
									<c:forEach begin="${_TypeNum%6}" end="5">
										<td width="16%">&nbsp;</td>
									</c:forEach>		      											
								</tr>
								
								<tr>
		      						<c:set var="_TypeNum" value="0"/>
		      						<c:forEach var="user" items="${_OtherUsers}" varStatus="index">
		      							<c:if test="${user.person.department.organizeId==department.organizeId}">
											<c:if test="${_TypeNum!=0 && _TypeNum%6==0}">
												</tr><tr>
											</c:if>
											<td width="16%" valign="top">
												<input type="checkbox" name="personIds" value="${user.personId}"/> ${user.person.personName}
											</td>					
											<c:set var="_TypeNum" value="${_TypeNum+1}"/>
										</c:if>																			
									</c:forEach>
									<c:forEach begin="${_TypeNum%6}" end="5">
										<td width="16%">&nbsp;</td>
									</c:forEach>		      											
								</tr>
							</table>
						</td>
		      		</tr>
	                <c:set var="_Num" value="${_Num+1}"/>
				</c:forEach>	
			</table>             
         </td>
       </tr>
    </c:if>
                    
     <tr> 
        <td colspan="3" >
          <input type="submit" id="button" style="cursor: pointer;" value="提交"/>
             &nbsp;
          <input type="button" value="关闭" style="cursor: pointer;" onclick="window.close();"/>
        </td>
     </tr>
</table>
</form:form>
</body>

