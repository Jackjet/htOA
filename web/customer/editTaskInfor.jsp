<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
<title>编辑任务</title>
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
		//验证
		$.formValidator.initConfig({formid:"taskInforForm",onerror:function(msg){alert(msg)},onsuccess:function(){submitData();}});
		$("#taskName").formValidator({onshow:"请输入项目名称",onfocus:"项目名称不能为空",oncorrect:"输入正确"}).inputValidator({min:1,onerror:"项目名称不能为空,请确认"});
		$("#startDateStr").formValidator({onshow:"请输入开始时间",onfocus:"开始时间不能为空",oncorrect:"输入正确"}).inputValidator({min:1,onerror:"开始时间不能为空,请确认"});
		$("#endDateStr").formValidator({onshow:"请输入结束时间",onfocus:"结束时间不能为空",oncorrect:"输入正确"}).inputValidator({min:1,onerror:"结束时间不能为空,请确认"});
		$("#checkerId").formValidator({onshow:"请选择审核人",onfocus:"审核人不能为空",oncorrect:"输入正确"}).inputValidator({min:1,onerror:"审核人不能为空,请确认"});
		$("#signerId").formValidator({onshow:"请选择签核人",onfocus:"签核人不能为空",oncorrect:"输入正确"}).inputValidator({min:1,onerror:"签核人不能为空,请确认"});
		<%--
		$("#description").formValidator({empty:true,onshow:"请输入分类编号,可以为空",onfocus:"格式例如：XF,XS",oncorrect:"输入正确"});
		$("#managerId").formValidator({onshow:"请输入显示次序",onfocus:"只能输入阿拉伯数字",oncorrect:"输入正确"});
		--%> 
		
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
		window.close();
	}
	
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
	
	}
	
</script>
<base target="_self"/>
</head>
	 
<body onload="init();">
<br/>
<form:form commandName="taskInforVo" id="taskInforForm" name="taskInforForm" action="/customer/taskInfor.do?method=save" method="post">
<form:hidden path="taskId"/>  
<form:hidden path="categoryId"/>
<form:hidden path="taskCode"/>
<table width="98%" cellpadding="6" cellspacing="1" align="center" border="1" bordercolor="#c5dbec">
   <tr>
	 <td colspan="3" bgcolor="#dfeffc"></td>
   </tr>
   
   <tr> 
     <td width="30%">任务名称：</td>
     <td width="70%"><form:input path="taskName" size="20"/></td>
     <td><div id="taskNameTip" style="width:200px"></div></td>
   </tr>
          
   <tr> 
      <td>开始时间：</td>
      <td><form:input path="startDateStr" class="searchString" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly="true" size="20" /></td>
      <td><div id="startDateStrTip" style="width:200px"></div></td>
   </tr>
   
   <tr> 
      <td>结束时间：</td>
      <td><form:input path="endDateStr" class="searchString" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly="true" size="20" /></td>
      <td><div id="endDateStrTip" style="width:200px"></div></td>
   </tr>
   
   <tr> 
     <td width="30%">审核人：</td>
     <td width="70%"><form:select path="checkerId">
		       <form:option value="">--选择审核人--</form:option>
		       <c:forEach items="${_AllSystemUsers}" var="user">
		           <form:option value="${user.person.personId}">${user.person.personName}</form:option>
		       </c:forEach>									
		    </form:select></td>
     <td><div id="checkerIdTip" style="width:200px"></div></td>
   </tr>         
   
    <tr> 
     <td width="30%">签核人：</td>
     <td width="70%"><form:select path="signerId">
		       <form:option value="">--选择签核人--</form:option>
		       <c:forEach items="${_AllSystemUsers}" var="user">
		           <form:option value="${user.person.personId}">${user.person.personName}</form:option>
		       </c:forEach>									
		    </form:select></td>
     <td><div id="signerIdTip" style="width:200px"></div></td>
   </tr>               
      
   <tr> 
     <td width="30%">内容简介：</td>
     <td width="70%"><form:textarea rows="5" cols="50" path="content"></form:textarea></td>
     <td><div id="contentTip" style="width:200px"></div></td>
   </tr>
   
    <tr> 
     <td width="30%">备注：</td>
     <td width="70%"><form:textarea rows="5" cols="50" path="memo"></form:textarea></td>
     <td><div id="memoTip" style="width:200px"></div></td>
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
          <input type="submit" id="button" style="cursor: pointer;" value="提交" />
             &nbsp;
          <input type="button" value="关闭" style="cursor: pointer;" onclick="window.close();"/>
        </td>
    </tr>
</table>
</form:form>
</body>