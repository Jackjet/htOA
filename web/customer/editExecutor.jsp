<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
<title>增加执行人</title>
<script src="<c:url value="/"/>js/inc_javascript.js" type="text/javascript"></script>
<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/css/form.css"/>"/>

<!-- formValidator -->
<link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css"></link>
<script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript" charset="UTF-8"></script>
<script src="<c:url value="/"/>components/formvalidator/js/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
<script src="<c:url value="/"/>datePicker/WdatePicker.js" type="text/javascript" charset="UTF-8"></script>
<!-- ------------- -->

<script language="javaScript">
	var i=0,number;
	$(document).ready(function(){
		//勾选选中的执行人
		var executorIds = document.getElementsByName('executorIds');
		<c:forEach var="_ExecutorIds" items="${_ExecutorIds}">
			var tmpExecutorId = '${_ExecutorIds}';
			if (executorIds != null && executorIds.length > 0) {
				for (var i=0;i<executorIds.length;i++) {
					var executorId = executorIds[i];
					if (tmpExecutorId == executorId.value) {
						executorId.checked = true;
					}
				}
			}
		</c:forEach>
	});
	
	//提交数据
	function submitData() {
		alert('信息编辑成功');
		window.returnValue = "Y";
		window.close();
	}
	
	<%--
	//选择其他权限时自动选上"浏览"权限
	function addBrowseRight(chooseBox,userId){
		var isChecked = false;
		if(chooseBox.checked){
			isChecked = true;
		}
		var browseRight;
		var addRight;
		var deleteRight;
		browseRight = document.getElementsByName('viewIds');
		addRight = document.getElementsByName('createIds');
		deleteRight = document.getElementsByName('deleteIds');
		if(browseRight!=null){
		   if(browseRight.length==null){
				//只有一个,则只需要判断该用户是不是这个分类
				if (addRight.checked || deleteRight.checked){
					isChecked = true;
				}
				<c:forEach var="user" items="${_Users}" varStatus="index">
					var tempId = '${user.personId}';						
					if(userId==tempId){
						browseRight.checked = isChecked;
					}
				</c:forEach>
			}else{			
				//多个用户
				for(var k = 0; k<browseRight.length;k++){								
				    var userId_browse;
					userId_browse = browseRight[k].value;
					<c:forEach var="user" items="${_Users}" varStatus="index">
						var tempUserId = '${user.personId}';
						if( tempUserId==userId_browse && userId_browse==userId){
							if (addRight[k].checked || deleteRight[k].checked){
								isChecked = true;
							}		
						   browseRight[k].checked = isChecked;
						}
					</c:forEach>	
				}			
			}				
		}
	}
	--%>	
		
	//全选操作
	function selectUserId(checkbox,obj,organizeId){
		var isChecked = false;
		if(checkbox.checked){
			isChecked = true;
		}
		var addExecutor;
		addExecutor = document.getElementsByName('executorIds');
			
		executorCheckBox = document.getElementsByName('executorCheckBox');
		if(obj!=null){
			if(obj.length==null){
				//只有一个,则只需要判断该用户是不是这个分类
				<c:forEach var="user" items="${_Users}" varStatus="index">
					var tempDepartmentId = '${user.person.department.organizeId}';
					var tempGroupId = '${user.person.group.organizeId}';
					if(organizeId==tempDepartmentId || organizeId==tempGroupId){
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
					userId = obj[k].value;
					<c:forEach var="user" items="${_Users}" varStatus="index">
						var tempDepartmentId = '${user.person.department.organizeId}';
						var tempGroupId = '${user.person.group.organizeId}';
						var tempUserId = '${user.personId}';
						if((organizeId==tempDepartmentId && tempUserId==userId) || (organizeId==tempGroupId && tempUserId==userId)){
							obj[k].checked = isChecked;
						}
					</c:forEach>
				}
			}
		}
	}
	
	
	//进入添加页面时自动选上"添加"和"浏览"权限
	function init(){
		<%--
		if (${empty param.categoryId}) {
			var params = new Array();
			params[0] = "viewIds";
			params[1] = "createIds";
			params[2] = "editCheckBox";
			params[3] = "addCheckBox";
			initRight(params);
		}
		--%>
	}
	
</script>
<base target="_self"/>
</head>
	 
<body onload="init();">
<br/>
<form:form commandName="taskInforVo" id="taskInforForm" name="taskInforForm" action="/customer/taskInfor.do?method=saveExecutor" method="post">
<form:hidden path="taskId"/>  
<form:hidden path="categoryId"/>
<form:hidden path="taskCode"/>
<form:hidden path="content"/>
<form:hidden path="taskName"/>
<table width="98%" cellpadding="6" cellspacing="1" align="center" border="1" bordercolor="#c5dbec">
   <tr>
	 <td colspan="3" bgcolor="#dfeffc"></td>
   </tr>
                   
   <script language="javaScript">
   	//定义一个数组，记录各个数据点击的次数
	var clickTimes =new Array();	
   </script>	
    <tr> 
       <td valign="top">选择执行人</td> 
       <td colspan="2">
		<table width="100%">
			<tr>
				<td colspan="2"><input type="checkbox" onclick="selectAll(this,taskInforForm.executorCheckBox,taskInforForm.executorIds)" name="addAllExecutor"/>选择全部</td>
			</tr>
			 <c:set var="_Num" value="0"/>
			<c:forEach var="organize" items="${_Organizes}" varStatus="index">
	           <tr height="33">
	              <td valign="bottom" width="100%" style="border-bottom:1px dotted #888888;font-size:10pt" colspan="2">
	                <script language="javaScript">
						clickTimes[${_Num}] = 0;							
					</script>	
	                ${organize.organizeName}&nbsp;<span onclick="show_list('${_Num}')"><img name="img" src="<c:url value="${'/images'}"/>/xpexpand3_s.gif" style="margin-top:0px;margin-bottom:0px;"/></span>
				  </td>
		       </tr>
		       <tr name="tr" style="display:none;padding-top:10px;">
		      	 <td width="10%" align="right" valign="top">
		      		<input type="checkbox" onclick="selectUserId(this,taskInforForm.executorIds,'${organize.organizeId}')" name="executorCheckBox"/>全选
		      	 </td>
		      	 <td style="padding-left:50px;">
		      		<table>
		      			<tr>
		      				<c:set var="_TypeNum" value="0"/>
		      				<c:forEach var="person" items="${_Persons}" varStatus="index">
		      					<c:if test="${person.person.department.organizeId==organize.organizeId || person.person.group.organizeId==organize.organizeId}">
									<c:if test="${_TypeNum!=0 && _TypeNum%6==0}">
										</tr><tr>
									</c:if>
									<td width="16%">
										<input type="checkbox" name="executorIds" value="${person.personId}" />
										 	${person.person.personNo}-${person.person.personName}
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
		      				<c:forEach var="person" items="${_OtherPersons}" varStatus="index">
		      					<c:if test="${person.person.department.organizeId==organize.organizeId || person.person.group.organizeId==organize.organizeId}">
									<c:if test="${_TypeNum!=0 && _TypeNum%6==0}">
										</tr><tr>
									</c:if>
									<td width="16%">
										<input type="checkbox" name="executorIds" value="${person.personId}" />
										 	${person.person.personNo}-${person.person.personName}
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
                          
	<tr> 
        <td colspan="3" bgcolor="#dfeffc">
          <input type="submit" id="button" style="cursor: pointer;" value="提交" onclick="submitData();"/>
             &nbsp;
          <input type="button" value="关闭" style="cursor: pointer;" onclick="window.close();"/>
        </td>
    </tr>
</table>
</form:form>
</body>