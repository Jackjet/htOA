<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
<title>编辑任务分类</title>
<script src="<c:url value="/"/>js/inc_javascript.js" type="text/javascript"></script>
<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/css/form.css"/>"/>

<!-- formValidator -->
<link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css"></link>
<link type="text/css" rel="stylesheet" href="/css/myTable.css"></link>
<script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript" charset="UTF-8"></script>
<script src="<c:url value="/"/>components/formvalidator/js/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
<!-- ------------- -->

<script language="javaScript">
	var i=0,number;
	$(document).ready(function(){
		//验证
		$.formValidator.initConfig({formid:"taskCategoryForm",onerror:function(msg){alert(msg)},onsuccess:function(){submitData();}});
		$("#categoryName").formValidator({onshow:"请输入分类名称",onfocus:"分类名称不能为空",oncorrect:"输入正确"}).inputValidator({min:1,onerror:"分类名称不能为空,请确认"});
		$("#categoryCode").formValidator({empty:true,onshow:"请选择分类类型,可以为空",oncorrect:"输入正确"});
		$("#displayNo").formValidator({onshow:"请输入显示次序",onfocus:"只能输入阿拉伯数字",oncorrect:"输入正确"});
		
		//勾选选中的用户(包括创建、删除、浏览)
		var createIds = document.getElementsByName('createIds');
		var deleteIds = document.getElementsByName('deleteIds');
		var viewIds = document.getElementsByName('viewIds');
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
		<c:forEach var="viewId" items="${_ViewIds}">
			var tmpViewId = '${viewId}';
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
		//var form = document.taskCategoryForm;
		//form.action = '<c:url value="/document/taskCategory.do"/>?method=save';
		//form.submit();
		//window.opener.location.reload();
		alert('信息编辑成功');
		window.returnValue = "Y";
		window.close();
	}
	
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
		
	//全选操作
	function selectUserId(checkbox,obj,organizeId){
		var isChecked = false;
		if(checkbox.checked){
			isChecked = true;
		}
		var browseRight;
		var addRight;
		var editRight;
		var deleteRight;
		browseRight = document.getElementsByName('viewIds');
		addRight = document.getElementsByName('createIds');
		deleteRight = document.getElementsByName('deleteIds');
			
		browseCheckBox = document.getElementsByName('browseCheckBox');
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
					if (addRight.checked || deleteRight.checked){
						browseRight.checked = true;
					}else{
						browseRight.checked = isChecked;
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
							if (addRight[k].checked || deleteRight[k].checked){
								browseRight[k].checked = true;
							}else{
								browseRight[k].checked = isChecked;
							}
						}
					</c:forEach>
				}
			}
			//对"浏览"全选按钮的控制
			for(var j=0;j<addCheckBox.length;j++){
				if(checkbox==browseCheckBox[j] || checkbox==addCheckBox[j] || checkbox==deleteCheckBox[j]){
					if(addCheckBox[j].checked || deleteCheckBox[j].checked){
						browseCheckBox[j].checked = true;
					}else{browseCheckBox[j].checked = isChecked;}
				}
			}								
		}
	}
	
	//进入添加页面时自动选上"添加"和"浏览"权限
	function init(){
		if (${empty param.categoryId}) {
			var params = new Array();
			params[0] = "viewIds";
			params[1] = "createIds";
			params[2] = "browseCheckBox";
			params[3] = "addCheckBox";
			initRight(params);
		}
	}
	
</script>
<base target="_self"/>
</head>
	 
<body onload="init();">
<br/>
<form:form commandName="taskCategoryVo" id="taskCategoryForm" name="taskCategoryForm" action="/supervise/taskCategory.do?method=save" method="post">
<form:hidden path="categoryId"/>   
<table width="98%" cellpadding="6" cellspacing="1" align="center" border="0" bordercolor="#c5dbec">
   <%--<tr>
	 <td colspan="3" bgcolor="#dfeffc"></td>
   </tr>--%>
   
   <tr> 
     <td width="20%">分类名称：</td>
     <td width="60%"><form:input path="categoryName" size="20"/></td>
     <td><div id="categoryNameTip" style="width:250px"></div></td>
   </tr>
                    
   <tr> 
      <td>分类类型：</td>
      <td>
      	<form:select path="categoryType">
      		<form:option value="1">方针目标</form:option>
      		<form:option value="2">党群</form:option>
      		<form:option value="3">部门建设</form:option>
      		<form:option value="4">内控</form:option>
      	</form:select>
      <td><div id="categoryTypeTip" style="width:250px"></div></td>
   </tr>
                    
   <tr> 
      <td>提醒周期：</td>
      <td>
      	<form:select path="period">
      		<form:option value="1">每月25日</form:option>
      		<form:option value="2">每两月25日</form:option>
      	</form:select>
      <td><div id="periodTip" style="width:250px"></div></td>
   </tr>
   
   <tr>                 
	   <td>所属父分类：</td>
	   <td>
	   	<select name="parentId">
			<c:forEach items="${requestScope._TREE}" var="category" varStatus="status"> 
				<c:choose>
					<c:when test="${_Category.parent.categoryId == category.categoryId}">
						<option value="${category.categoryId}" selected="selected">
					</c:when>
					<c:otherwise>
						<option value="${category.categoryId}">
					</c:otherwise>
				</c:choose>
				<c:forEach begin="0" end="${category.layer}">&nbsp;</c:forEach>
				<c:if test="${category.layer==1}"><b>+</b></c:if><c:if test="${category.layer==2}"><b>-</b></c:if>${category.categoryName}				
					</option>
			</c:forEach>
		</select>
	   </td>
	   <td></td>
   </tr>
                    
   <tr> 
      <td>是否叶分类：</td> 
      <td>
		 <form:select path="leaf">
		    <form:option value="false">否</form:option>
		    <form:option value="true">是</form:option>
		 </form:select>
	  </td>
   </tr>
	                       
   <tr> 
      <td>显示次序：</td> 
      <td>
		<form:input path="displayNo" size="20"/>	
	  </td>
	  <td><div id="displayNoTip" style="width:250px"></div></td>
   </tr> 
                    
   <script language="javaScript">
   	//定义一个数组，记录各个数据点击的次数
	var clickTimes =new Array();	
   </script>	
			 
   <tr> 
      <td colspan="3">以下为权限信息：</td>
   </tr> 
                               
   <tr> 
      <td valign="top">创建任务</td> 
      <td colspan="2">
         <table width="100%">
            <tr>
            	<td colspan="2"><input type="checkbox" onclick="selectAll(this,taskCategoryForm.addCheckBox,taskCategoryForm.createIds)" name="addAllCreate"/>选择全部</td>
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
		      		<input type="checkbox" onclick="selectUserId(this,taskCategoryForm.createIds,'${organize.organizeId}')" name="addCheckBox"/>全选
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
											<input type="checkbox" name="createIds" value="${person.personId}" onclick="addBrowseRight(this,'${person.personId}');"/>
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
										<input type="checkbox" name="createIds" value="${person.personId}" onclick="addBrowseRight(this,'${person.personId}');"/>
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
                          
   <tr> 
     <td valign="top">删除任务</td> 
     <td colspan="2">
		<table width="100%">
			<tr>
				<td colspan="2"><input type="checkbox" onclick="selectAll(this,taskCategoryForm.deleteCheckBox,taskCategoryForm.deleteIds)" name="addAllDelete"/>选择全部</td>
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
		      			<input type="checkbox" onclick="selectUserId(this,taskCategoryForm.deleteIds,'${organize.organizeId}')" name="deleteCheckBox"/>全选
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
											<input type="checkbox" name="deleteIds" value="${person.personId}" onclick="addBrowseRight(this,'${person.personId}');"/>
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
											<input type="checkbox" name="deleteIds" value="${person.personId}" onclick="addBrowseRight(this,'${person.personId}');"/>
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
       <td valign="top">浏览任务</td> 
       <td colspan="2">
		<table width="100%">
			<tr>
				<td colspan="2"><input type="checkbox" onclick="selectAll(this,taskCategoryForm.browseCheckBox,taskCategoryForm.viewIds)" name="addAllView"/>选择全部</td>
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
		      		<input type="checkbox" onclick="selectUserId(this,taskCategoryForm.viewIds,'${organize.organizeId}')" name="browseCheckBox"/>全选
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
										<input type="checkbox" name="viewIds" value="${person.personId}" onclick="addBrowseRight(this,'${person.personId}');"/>
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
										<input type="checkbox" name="viewIds" value="${person.personId}" onclick="addBrowseRight(this,'${person.personId}');"/>
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
        <td colspan="3" >
          <input type="submit" id="button" style="cursor: pointer;" value="提交"/>
             &nbsp;
          <input type="button" value="关闭" style="cursor: pointer;" onclick="window.close();"/>
        </td>
    </tr>
</table>
</form:form>
</body>