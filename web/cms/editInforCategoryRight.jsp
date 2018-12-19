<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
<title>编辑权限信息</title>
<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
<script src="<c:url value="/"/>js/inc_javascript.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/css/form.css"/>"/>
<link rel="stylesheet" type="text/css" href="/css/myTable.css"/>

<script language="javaScript">
	var i=0,number;
	//提交数据
	function submitData() {
        var form = document.getElementById('inforCategoryForm');
		form.action = '<c:url value="/cms/inforCategory.do"/>?method=saveCategoryRight';

        var browserName=navigator.userAgent.toLowerCase();
        if(/chrome/i.test(browserName)&&/webkit/i.test(browserName)&&/mozilla/i.test(browserName)){
            //如果是chrome浏览器
            $.ajax({
                type: "POST",
                url:'<c:url value="/cms/inforCategory.do"/>?method=saveCategoryRight',
                data:$('#inforCategoryForm').serialize(),
                async: false
            });
        }else {
            form.submit();
        }
		window.returnValue = 'Y';
		alert('授权成功');
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
		browseRight = document.getElementsByName('viewUserIds');
		addRight = document.getElementsByName('editUserIds');
		deleteRight = document.getElementsByName('deleteUserIds');
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
		browseRight = document.getElementsByName('viewUserIds');
		addRight = document.getElementsByName('editUserIds');
		deleteRight = document.getElementsByName('deleteUserIds');
			
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
	
	//初始化操作
	function init(){
		//进入添加页面时自动选上"添加"和"浏览"权限
		/** var params = new Array();
		params[0] = "viewUserIds";
		params[1] = "editUserIds";
		params[2] = "browseCheckBox";
		params[3] = "addCheckBox";
		initRight(params); **/
		
		//勾选选中的用户(包括编辑、删除、浏览)
		var editUserIds = document.getElementsByName('editUserIds');
		var deleteUserIds = document.getElementsByName('deleteUserIds');
		var viewUserIds = document.getElementsByName('viewUserIds');
		<c:forEach var="editId" items="${_EditUserIds}">
			var tmpEditId = '${editId}';
			if (editUserIds != null && editUserIds.length > 0) {
				for (var i=0;i<editUserIds.length;i++) {
					var editId = editUserIds[i];
					if (tmpEditId == editId.value) {
						editId.checked = true;
					}
				}
			}
		</c:forEach>
		<c:forEach var="deleteId" items="${_DeleteUserIds}">
			var tmpDeleteId = '${deleteId}';
			if (deleteUserIds != null && deleteUserIds.length > 0) {
				for (var i=0;i<deleteUserIds.length;i++) {
					var deleteId = deleteUserIds[i];
					if (tmpDeleteId == deleteId.value) {
						deleteId.checked = true;
					}
				}
			}
		</c:forEach>
		<c:forEach var="viewId" items="${_ViewUserIds}">
			var tmpViewId = '${viewId}';
			if (viewUserIds != null && viewUserIds.length > 0) {
				for (var i=0;i<viewUserIds.length;i++) {
					var viewId = viewUserIds[i];
					if (tmpViewId == viewId.value) {
						viewId.checked = true;
					}
				}
			}
		</c:forEach>
		
		//勾选选中的角色(包括编辑、删除、浏览)
		var editRoleIds = document.getElementsByName('editRoleIds');
		var deleteRoleIds = document.getElementsByName('deleteRoleIds');
		var viewRoleIds = document.getElementsByName('viewRoleIds');
		<c:forEach var="editId" items="${_EditRoleIds}">
			var tmpEditId = '${editId}';
			if (editRoleIds != null && editRoleIds.length > 0) {
				for (var i=0;i<editRoleIds.length;i++) {
					var editId = editRoleIds[i];
					if (tmpEditId == editId.value) {
						editId.checked = true;
					}
				}
			}
		</c:forEach>
		<c:forEach var="deleteId" items="${_DeleteRoleIds}">
			var tmpDeleteId = '${deleteId}';
			if (deleteRoleIds != null && deleteRoleIds.length > 0) {
				for (var i=0;i<deleteRoleIds.length;i++) {
					var deleteId = deleteRoleIds[i];
					if (tmpDeleteId == deleteId.value) {
						deleteId.checked = true;
					}
				}
			}
		</c:forEach>
		<c:forEach var="viewId" items="${_ViewRoleIds}">
			var tmpViewId = '${viewId}';
			if (viewRoleIds != null && viewRoleIds.length > 0) {
				for (var i=0;i<viewRoleIds.length;i++) {
					var viewId = viewRoleIds[i];
					if (tmpViewId == viewId.value) {
						viewId.checked = true;
					}
				}
			}
		</c:forEach>
		
		//设置权限信息的显示(默认显示角色信息)
		var rightTypes = document.getElementsByName("rightType");
		if (${_RightType == 0}) {
			rightTypes[0].checked = true;
		}else {
			rightTypes[1].checked = true;
		}
		changeDisplay();
		
	}
	
	//切换显示用户信息和角色信息
	function changeDisplay() {
		var rightTypes = document.getElementsByName("rightType");
		var roleTr = document.getElementById("roleTr");
		var userTr = document.getElementById("userTr");
		
		if (rightTypes != null && rightTypes.length > 0) {
			if (rightTypes[0].checked) {
				//显示角色信息
				roleTr.style.display = '';
				userTr.style.display = 'none';
			}else if (rightTypes[1].checked) {
				//显示用户信息
				userTr.style.display = '';
				roleTr.style.display = 'none';
			}
		}
	}
</script>
</head>
<base target="_self"/>
<body onload="init();">
<br/>
<form id="inforCategoryForm" name="inforCategoryForm" action="/cms/inforCategory.do?method=saveCategoryRight" method="post">
<input type="hidden" name="categoryId" value="${_Category.categoryId}"/>   
<table width="98%" cellpadding="6" cellspacing="1" align="center" border="0" bordercolor="#c5dbec">
   <%--<tr>
	 <td colspan="2" bgcolor="#dfeffc"></td>
   </tr>--%>
   
   <tr> 
     <td width="20%">分类名称：</td>
     <td>${_Category.categoryName}</td>
   </tr>
                    
   <tr> 
      <td>访问路径：</td>
      <td>${_Category.urlPath}</td>
   </tr>
   
   <tr> 
      <td>权限类型：</td>
      <td><input type="radio" name="rightType" value="0" onclick="changeDisplay();"/>角色 <input type="radio" name="rightType" value="1" onclick="changeDisplay();"/>用户</td>
   </tr>
   
   <tr> 
      <td colspan="2">以下为权限信息：</td>
   </tr> 
   
   <!-- 根据角色设置权限 -->
   <tbody id="roleTr">
   <tr> 
	  <td valign="top">编辑</td>
	  <td colspan="2">				                        	 
		<table width="100%">
			<tr>
				<c:set var="_TypeNum" value="0"/>
				<c:forEach var="role" items="${_Roles}">
					<c:if test="${_TypeNum!=0 && _TypeNum%6==0}">
						</tr><tr>
					</c:if>
					<td width="16%" valign="top">
						<input type="checkbox" name="editRoleIds" value="${role.roleId}"/> ${role.roleName}
					</td>					
					<c:set var="_TypeNum" value="${_TypeNum+1}"/>								
				</c:forEach>
				<c:forEach begin="${_TypeNum%6}" end="5">
					<td width="16%">&nbsp;</td>
				</c:forEach>		      											
		</table>
	  </td> 
   </tr>
   <tr> 
	  <td valign="top">删除</td>
	  <td colspan="2">				                        	 
		<table width="100%">
			<tr>
				<c:set var="_TypeNum" value="0"/>
				<c:forEach var="role" items="${_Roles}">
					<c:if test="${_TypeNum!=0 && _TypeNum%6==0}">
						</tr><tr>
					</c:if>
					<td width="16%" valign="top">
						<input type="checkbox" name="deleteRoleIds" value="${role.roleId}"/> ${role.roleName}
					</td>					
					<c:set var="_TypeNum" value="${_TypeNum+1}"/>								
				</c:forEach>
				<c:forEach begin="${_TypeNum%6}" end="5">
					<td width="16%">&nbsp;</td>
				</c:forEach>		      											
		</table>
	  </td> 
   </tr>
   <tr> 
	  <td valign="top">浏览</td>
	  <td colspan="2">				                        	 
		<table width="100%">
			<tr>
				<c:set var="_TypeNum" value="0"/>
				<c:forEach var="role" items="${_Roles}">
					<c:if test="${_TypeNum!=0 && _TypeNum%6==0}">
						</tr><tr>
					</c:if>
					<td width="16%" valign="top">
						<input type="checkbox" name="viewRoleIds" value="${role.roleId}"/> ${role.roleName}
					</td>					
					<c:set var="_TypeNum" value="${_TypeNum+1}"/>								
				</c:forEach>
				<c:forEach begin="${_TypeNum%6}" end="5">
					<td width="16%">&nbsp;</td>
				</c:forEach>		      											
		</table>
	  </td> 
   </tr>
   </tbody>
	
   <!-- 根据用户设置权限 -->
   <script language="javaScript">
   	//定义一个数组，记录各个数据点击的次数
	var clickTimes =new Array();	
   </script>
   
   <tbody id="userTr" style="display: none;">
   <tr>
      <td valign="top">编辑</td> 
      <td colspan="2">
         <table width="100%">
            <tr>
            	<td colspan="2"><input type="checkbox" onclick="selectAll(this,inforCategoryForm.addCheckBox,inforCategoryForm.editUserIds)" name="addAllCreate"/>选择全部</td>
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
		      		<input type="checkbox" onclick="selectUserId(this,inforCategoryForm.editUserIds,'${organize.organizeId}')" name="addCheckBox"/>全选
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
											<input type="checkbox" name="editUserIds" value="${person.personId}" onclick="addBrowseRight(this,'${person.personId}');"/>
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
										<input type="checkbox" name="editUserIds" value="${person.personId}" onclick="addBrowseRight(this,'${person.personId}');"/>
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
     <td valign="top">删除</td> 
     <td colspan="2">
		<table width="100%">
			<tr>
				<td colspan="2"><input type="checkbox" onclick="selectAll(this,inforCategoryForm.deleteCheckBox,inforCategoryForm.deleteUserIds)" name="addAllDelete"/>选择全部</td>
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
		      			<input type="checkbox" onclick="selectUserId(this,inforCategoryForm.deleteUserIds,'${organize.organizeId}')" name="deleteCheckBox"/>全选
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
											<input type="checkbox" name="deleteUserIds" value="${person.personId}" onclick="addBrowseRight(this,'${person.personId}');"/>
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
											<input type="checkbox" name="deleteUserIds" value="${person.personId}" onclick="addBrowseRight(this,'${person.personId}');"/>
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
       <td valign="top">浏览</td> 
       <td colspan="2">
		<table width="100%">
			<tr>
				<td colspan="2"><input type="checkbox" onclick="selectAll(this,inforCategoryForm.browseCheckBox,inforCategoryForm.viewUserIds)" name="addAllView"/>选择全部</td>
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
		      		<input type="checkbox" onclick="selectUserId(this,inforCategoryForm.viewUserIds,'${organize.organizeId}')" name="browseCheckBox"/>全选
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
										<input type="checkbox" name="viewUserIds" value="${person.personId}" onclick="addBrowseRight(this,'${person.personId}');"/>
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
										<input type="checkbox" name="viewUserIds" value="${person.personId}" onclick="addBrowseRight(this,'${person.personId}');"/>
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
    </tbody>
                    
	<tr> 
        <td colspan="2">
          <input type="button" value="提交" style="cursor: pointer;" onclick="submitData();"/>
             &nbsp;
          <input type="button" value="关闭" style="cursor: pointer;" onclick="window.close();"/>
        </td>
    </tr>
</table>
</form>
</body>