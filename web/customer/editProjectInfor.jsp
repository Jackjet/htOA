<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
<title>编辑项目</title>
<script src="<c:url value="/"/>js/inc_javascript.js" type="text/javascript"></script>
<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/css/form.css"/>"/>

<!-- formValidator -->
<link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css"></link>
<script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript" charset="UTF-8"></script>
<script src="<c:url value="/"/>components/formvalidator/js/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
<!-- ------------- -->

<script language="javaScript">
	var i=0,number;
	$(document).ready(function(){
		//验证
		$.formValidator.initConfig({formid:"projectInforForm",onerror:function(msg){alert(msg)},onsuccess:function(){submitData();}});
		$("#projectName").formValidator({onshow:"请输入项目名称",onfocus:"项目名称不能为空",oncorrect:"输入正确"}).inputValidator({min:1,onerror:"项目名称不能为空,请确认"});
		$("#managerId").formValidator({onshow:"请选择项目经理",onfocus:"项目经理不能为空",oncorrect:"选择正确"}).inputValidator({min:1,onerror:"项目经理不能为空,请确认"});
		<%--
		$("#description").formValidator({empty:true,onshow:"请输入分类编号,可以为空",onfocus:"格式例如：XF,XS",oncorrect:"输入正确"});
		$("#managerId").formValidator({onshow:"请输入显示次序",onfocus:"只能输入阿拉伯数字",oncorrect:"输入正确"});
		--%>
		
		//勾选选中的用户(包括创建、删除、浏览)
		var createIds = document.getElementsByName('createIds');
		var deleteIds = document.getElementsByName('deleteIds');
		var viewIds = document.getElementsByName('editIds');
		<c:forEach var="createId" items="${_CreateIds}">
			var tmpCreateId = '${createId}';
			if (createIds != null && createIds.length > 0) {
				for (var i=0;i<createIds.length;i++) {
					var createId = createIds[i];
					if (tmpCreateId == createId.value) {
						createId.checked = true;
					}
				}
			}
		</c:forEach>
		<c:forEach var="deleteId" items="${_DeleteIds}">
			var tmpDeleteId = '${deleteId}';
			if (deleteIds != null && deleteIds.length > 0) {
				for (var i=0;i<deleteIds.length;i++) {
					var deleteId = deleteIds[i];
					if (tmpDeleteId == deleteId.value) {
						deleteId.checked = true;
					}
				}
			}
		</c:forEach>
		<c:forEach var="editId" items="${_EditIds}">
			var tmpViewId = '${editId}';
			if (viewIds != null && viewIds.length > 0) {
				for (var i=0;i<viewIds.length;i++) {
					var viewId = viewIds[i];
					if (tmpViewId == viewId.value) {
						viewId.checked = true;
					}
				}
			}
		</c:forEach>
	});
	
	//提交数据
	function submitData() {
		//var form = document.documentCategoryForm;
		//form.action = '<c:url value="/document/documentCategory.do"/>?method=save';
		//form.submit();
		//window.opener.location.reload();
		alert('信息编辑成功');
		window.returnValue = "Y";
		window.close();
	}
	
		
	//全选操作
	function selectUserId(checkbox,obj,organizeId){
		var isChecked = false;
		if(checkbox.checked){
			isChecked = true;
		}
		var addRight;
		var editRight;
		var deleteRight;
		editRight = document.getElementsByName('editIds');
		addRight = document.getElementsByName('createIds');
		deleteRight = document.getElementsByName('deleteIds');
			
		editCheckBox = document.getElementsByName('editCheckBox');
		addCheckBox = document.getElementsByName('addCheckBox');
		deleteCheckBox = document.getElementsByName('deleteCheckBox');
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
	
	
	//项目权限的设定只能由项目经理和项目创建者来修改
	function init(){
		if(projectInforForm.setRight.value==1){
			var x = document.getElementById("popedom");
			x.style.display = "none";
		}
	}
	
</script>
<base target="_self"/>
</head>
	 
<body onload="init();">
<br/>
<form:form commandName="projectInforVo" id="projectInforForm" name="projectInforForm" action="/customer/projectInfor.do?method=save" method="post">
<form:hidden path="projectId"/>  
<form:hidden path="creatorId"/>   
<form:hidden path="updateDateStr"/>
<form:hidden path="setRight"/>
<table width="98%" cellpadding="6" cellspacing="1" align="center" border="1" bordercolor="#c5dbec">
   <tr>
	 <td colspan="3" bgcolor="#dfeffc"></td>
   </tr>
   
   <tr> 
     <td width="30%">项目名称：</td>
     <td width="60%"><form:input path="projectName" size="20"/></td>
     <td><div id="projectNameTip" style="width:200px"></div></td>
   </tr>
     
   <tr> 
    <td width="30%">项目经理：</td>
    <td width="60%"><form:select path="managerId">
	       <form:option value="">--选择项目经理--</form:option>
	       <c:forEach items="${_AllSystemUsers}" var="user">
	           <form:option value="${user.person.personId}">${user.person.personName}</form:option>
	       </c:forEach>									
	    </form:select></td>
    <td><div id="managerIdTip" style="width:200px"></div></td>
   </tr>        
                  
   <tr> 
     <td width="30%">项目描述：</td>
     <td width="60%"><form:textarea rows="5" cols="50" path="description"></form:textarea></td>
     <td><div id="descriptionTip" style="width:200px"></div></td>
   </tr>
        
     
   <script language="javaScript">
   	//定义一个数组，记录各个数据点击的次数
	var clickTimes =new Array();	
   </script>	
		
<tr id="popedom">
<td colspan="3">
<table width="100%">    
   <tr> 
      <td colspan="3">以下为权限信息：</td>
   </tr> 
                               
    <tr> 
       <td valign="top" style="width: 10%">项目修改</td> 
       <td colspan="2" style="width: 90%">
		<table width="100%">
			<tr>
				<td colspan="2"><input type="checkbox" onclick="selectAll(this,projectInforForm.editCheckBox,projectInforForm.editIds)" name="addAllView"/>选择全部</td>
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
		      		<input type="checkbox" onclick="selectUserId(this,projectInforForm.editIds,'${organize.organizeId}')" name="editCheckBox"/>全选
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
										<input type="checkbox" name="editIds" value="${person.personId}" />
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
										<input type="checkbox" name="editIds" value="${person.personId}" />
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
     <td valign="top">删除项目</td> 
     <td colspan="2">
		<table width="100%">
			<tr>
				<td colspan="2"><input type="checkbox" onclick="selectAll(this,projectInforForm.deleteCheckBox,projectInforForm.deleteIds)" name="addAllDelete"/>选择全部</td>
			</tr>
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
		      			<input type="checkbox" onclick="selectUserId(this,projectInforForm.deleteIds,'${organize.organizeId}')" name="deleteCheckBox"/>全选
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
											<input type="checkbox" name="deleteIds" value="${person.personId}" />
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
											<input type="checkbox" name="deleteIds" value="${person.personId}" />
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
      <td valign="top">添加项目分类</td> 
      <td colspan="2">
         <table width="100%">
            <tr>
            	<td colspan="2"><input type="checkbox" onclick="selectAll(this,projectInforForm.addCheckBox,projectInforForm.createIds)" name="addAllCreate"/>选择全部</td>
            </tr>
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
		      		<input type="checkbox" onclick="selectUserId(this,projectInforForm.createIds,'${organize.organizeId}')" name="addCheckBox"/>全选
		      	</td>
		      	<td style="padding-left:50px;" width="90%">
		      		<table>
		      			<tr>
		      				<c:set var="_TypeNum" value="0"/>
		      				<c:forEach var="person" items="${_Persons}" varStatus="index">
		      					<c:if test="${person.person.department.organizeId==organize.organizeId || person.person.group.organizeId==organize.organizeId}">
									<c:if test="${_TypeNum!=0 && _TypeNum%6==0}">
										</tr><tr>
									</c:if>
										<td width="16%" valign="top">
											<input type="checkbox" name="createIds" value="${person.personId}" />
											 ${person.person.personNo}-${person.person.personName}
										</td>					
										<c:set var="_TypeNum" value="${_TypeNum + 1}"/>
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
									<td width="16%" valign="top">
										<input type="checkbox" name="createIds" value="${person.personId}" />
										 ${person.person.personNo}-${person.person.personName}
									</td>					
									<c:set var="_TypeNum" value="${_TypeNum + 1}"/>
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
</table>
</td>
</tr>
 
	<tr> 
        <td colspan="3" bgcolor="#dfeffc">
          <input type="submit" id="button" style="cursor: pointer;" value="提交"/>
             &nbsp;
          <input type="button" value="关闭" style="cursor: pointer;" onclick="window.close();"/>
        </td>
    </tr>
</table>
</form:form>
</body>