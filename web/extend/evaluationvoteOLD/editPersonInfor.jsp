<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<title>编辑人员信息</title>
<script src="<c:url value="/"/>datePicker/WdatePicker.js" charset="UTF-8" language="JavaScript"></script>
<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
<script src="<c:url value="/"/>js/inc_javascript.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/css/form.css"/>"/>
<script src="<c:url value="/"/>js/commonFunction.js"></script>
<!-- formValidator -->
<link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css"></link>
<script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript" charset="UTF-8"></script>
<script src="<c:url value='/js'/>/pyperson.js"></script>
<!-- ------------- -->

<script>
	
	$(document).ready(function(){
		//验证
		$.formValidator.initConfig({formid:"personInforForm",onerror:function(msg){alert(msg)},onsuccess:function(){submitData();}});
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
	
		//验证输入数据
	function validatePersonInfor(){
		var displayOrder;		
		var department;
		var personName;
		var descrip;
		displayOrder = personInforForm.displayOrder.value;
		//alert(displayOrder);
		department = personInforForm.department.value;
		personName = personInforForm.personName.value;
		descrip = personInforForm.descrip.value;
		if(displayOrder==""||displayOrder==null){
			alert("请输入排序号!");
			personInforForm.displayOrder.focus();
			return false;
		}
		
		if(department==""||department==null){
			alert("请输入部门名称!");
			personInforForm.department.focus();
			return false;
		}
		
		if(personName==""||personName==null){
			alert("请输入姓名!");
			personInforForm.personName.focus();
			return false;
		}
		
	

		
		return true;
	}
	//提交数据
	function submitData() {
		var form = document.personInforForm;
		form.action = '<c:url value="/extend/pyPersonInfor"/>.do?method=save';
		form.submit();
		//window.opener.location.reload();
		//window.returnValue = "Y";
		//window.close();
	}
	
	function del(pId) {
		var form = document.personInforForm;
		
		form.action = '<c:url value="/extend/pyPersonInfor"/>.do?method=delete&pId='+pId;
		form.submit();
		//window.opener.location.reload();
		//window.returnValue = "Y";
		//window.close();
	}
	
	//添加控件
	var tempsum=1;
	function addnum(){
		tempsum=tempsum+1;
	}
	function addControl(){
	//alert(1212);
		addnum();
	    var htmlsrc="";
	  
	    htmlsrc=htmlsrc + '<tr>';    	    
	    htmlsrc=htmlsrc + '<td bgcolor="#FFFFFF"><textarea rows="5" cols="80" name="content" ></textarea></td></tr>';
	    alert( document.getElementById("newControl"));
	    document.getElementById("newControl").insertAdjacentHTML ("beforeEnd",htmlsrc);
	    
    }
    
 
</script>
<base target="_self"/>
<body onload="hdUsers();">
<br/>
<form:form commandName="pyPersonInforVo" id="personInforForm" name="personInforForm" action="" method="post">
<form:hidden path="topicId"/>
<form:hidden path="pId"/>
<input type="hidden" name="rowId" value="<%=request.getParameter("rowId") %>"/>
<table width="80%" cellpadding="6" cellspacing="1" align="center" border="1" bordercolor="#c5dbec">
	<tr>
		<td colspan="3" bgcolor="#dfeffc" align="center"><h2><font color="#15A7BC">${_Topic.topicName}</font></h2></td>
	</tr>
	

              
  <tr>
	
			<td colspan="3" bgcolor="#FFFFFF">
			
			
			
		<table cellpadding="0" cellspacing="0" border="1" width="80%" style="margin-bottom:0;margin-top:0">
						
						<tr bgcolor="#dfeffc">
							<td bgcolor="#dfeffc" width="20">
								<b>排序号
									
							</td >
							<td bgcolor="#dfeffc" width="80">
									
								<b>部门
							</td>
							<td bgcolor="#dfeffc" width="80">
								<b>人员	
									
							</td>
							
							<td bgcolor="#dfeffc"width="200">
								<b>描述
									
							</td>
							<td bgcolor="#dfeffc" width="40">
								<b>操作
									
							</td>
						</tr>
						<c:forEach var="person" items="${_Topic.personInfors}" varStatus="index">
						<tr>
							<td>
								
								${person.displayOrder }	
							</td>
							<td>
								${person.department }	
									
							</td>
							<td>
								${person.personName }	
								
							</td>
							<td>
								${person.descrip }	
							</td>
							
							<td>
								<a href="/extend/pyPersonInfor.do?method=edit&rowId=${_Topic.topicId}&pId=${person.PId}"> 修改 </a>  &nbsp;	<a href="javaScript:del(${person.PId})"> 删除 </a>  
							</td>
						</tr>
						
						</c:forEach>
					</table>
				
	
		</td>
	</tr>

                    
          

	
	<tr>
		<td colspan=3 width="40%" bgcolor="#dfeffc" align="left">&nbsp;&nbsp;&nbsp;&nbsp;<font color="#15A7BC"><b>编辑人员信息</b></font></td>
	</tr>
	
	<tr>
		<td colspan=3 style="padding-left:20px;padding-top:10px;" valign=top>
			<!--<fieldset style="padding-left:20px;border:2px solid #15A7BC">
				<legend><font color=red><b>编辑单个条目信息</b></font></legend>-->
				<table>
				
					<tr> 
				       <td nowrap align=right>排序号：</td>
				       <td colspan="2"><form:input path="displayOrder" size="20" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/>    <font color=red> * </font></td>
				    
				    </tr>
				    
					<tr> 
				       <td nowrap align=right width="20%">部门：</td>
				       <td><form:input path="department" />   
				       	<select id="departmentId" name="departmentId" onchange="personInforForm.department.value =  document.getElementById('departmentId').options[document.getElementById('departmentId').selectedIndex].text;">
							
						<select>
				       <font color=red> * </font>
				       </td>
				       
				    </tr>
				    
				    	<tr> 
				       <td nowrap align=right width="20%">姓名：</td>
				       <td><form:input  path="personName" />
				    
				          <select id ="personId" name="personId" onchange="personInforForm.personName.value =  document.getElementById('personId').options[document.getElementById('personId').selectedIndex].text;">
						
						</select>
				           <font color=red> * </font>
				       </td>
				   
				    </tr>
				
        
				                    
					  <tr> 
				       <td nowrap align=right width="20%">描述：</td>
				       <td >
				        <form:textarea path="descrip" cols="65" rows="8"/>    
				       </td>
				    </tr>

				
				</table>
			<!--</fieldset>-->
		</td>
		
	</tr>
	
<script>	
  	/** 查询条件中的部门,班组,用户下拉联动 */
		//部门信息初始化
		$('#departmentId').selectInit();
		
		//加载部门及联动信息		
		$.loadDepartments("departmentId", null, "personId");
		/** ******** */
</script>
     <tr> 
        <td colspan="3" bgcolor="#dfeffc">
         	<input type="button" id="button" style="cursor: pointer;"
						value="提交"
						onclick="javaScript:if(validatePersonInfor()){submitData();}" />
             &nbsp;
          <input type="button" value="关闭" style="cursor: pointer;" onclick="window.close();"/>
        </td>
     </tr>
</table>
</form:form>
</body>

