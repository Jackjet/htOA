<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>
<%@ page language="java" 
		import="org.springframework.web.context.WebApplicationContext,
				org.springframework.web.context.support.*,
				java.util.*,
				com.kwchina.oa.personal.address.service.CompanyAddressManager,
				com.kwchina.oa.personal.address.entity.CompanyAddress " %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<title>编辑会议</title>
<script src="<c:url value="/"/>datePicker/WdatePicker.js" charset="UTF-8" language="JavaScript"></script>
<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
<script src="<c:url value="/"/>js/inc_javascript.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/css/form.css"/>" />
<link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css"></link>
<script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript" charset="UTF-8"></script>
<script src="<c:url value="/"/>components/formvalidator/js/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
<script src="<c:url value='/js'/>/addattachment.js"></script>
<script src="<c:url value='/fckeditor'/>/fckeditor.js"></script>
<link type="text/css" rel="stylesheet" href="/css/myTable.css"></link>
<script>	
	//验证输入会议结束日期是否小于会议日期
	function validateDate(){
		var meetDate;
		var endMeetDate;
		meetDate = meetInforForm.meetDate.value;
		endMeetDate = meetInforForm.endMeetDate.value;
		if(endMeetDate!=""){
			if(meetDate > endMeetDate){
				alert("请输入大于会议日期的会议结束日期!");
				meetInforForm.endMeetDate.focus();
			}	
		}
	}
	
	//验证输入数据
	function validateMeetInfor(){
		var meetName;		
		var meetDate;
		var meetRoom;
		var endMeetDate;
		meetName = meetInforForm.meetName.value;
		meetDate = meetInforForm.meetDate.value;
		meetRoom = meetInforForm.meetRoom.value;
		endMeetDate = meetInforForm.endMeetDate.value;
		if(meetName==""||meetName==null){
			alert("请输入会议名称!");
			meetInforForm.meetName.focus();
			return false;
		}
		
		if(meetDate==""||meetDate==null){
			alert("请输入会议时间!");
			meetInforForm.meetDate.focus();
			return false;
		}
		
		if(meetRoom==""||meetRoom==null){
			alert("请输入会议地点!");
			meetInforForm.meetRoom.focus();
			return false;
		}
		if(endMeetDate!=""){
			if(meetDate > endMeetDate){
				alert("请输入大于会议日期的会议结束日期!");
				meetInforForm.endMeetDate.focus();
				return false;
			}
		}	
		return true;
	}
	//提交数据
	function submitData() {
		var form = document.meetInforForm;
		form.action = '<c:url value="/meeting/meetInfor"/>.do?method=save';
		form.submit();
		//window.opener.location.reload();
		//window.returnValue = "Y";
		//window.close();
	}
	
	var i=0,number;
	//全选操作
	function selectUserId(checkbox,organizeId){
		var isChecked = false;
		if(checkbox.checked){
			isChecked = true;
		}
		var obj;
		obj = document.meetInforForm.AttendIds;		
		if(obj!=null){
			if(obj.length==null){
					//只有一个,则只需要判断该用户是不是这个分类
				<c:forEach var="user" items="${_Persons}" varStatus="index">
					var tempOrganizeId = '${user.person.department.organizeId}';
					if(organizeId==tempOrganizeId){
						obj.checked = isChecked;
					}
				</c:forEach>
				
				<c:forEach var="user" items="${_OtherPersons}" varStatus="index">
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
					<c:forEach var="user" items="${_Persons}" varStatus="index">
						var tempOrganizeId = '${user.person.department.organizeId}';
						var tempPersonId = '${user.person.personId}';
							
						if(organizeId==tempOrganizeId && tempPersonId==personId){
							obj[k].checked = isChecked;
						}
					</c:forEach>
						
					<c:forEach var="user" items="${_OtherPersons}" varStatus="index">
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
	
	//定义一个数组，记录各个数据点击的次数
	var clickTimes =new Array();		
	
	
	function addPerson(){
			//alert(frm);
			//alert(frm.personId);
			var frm = document.meetInforForm;
			var selectPhone;
			var selectName;
			var selectMobilesName;
			selectPhone=frm.personId.options[frm.personId.selectedIndex].value;
			selectName=frm.personId.options[frm.personId.selectedIndex].innerHTML;
			selectMobilesName=selectName+"("+selectPhone+")";
			var mobiles;
			var mobilesName;
			mobiles=frm.mobiles.value;
			mobiles=frm.mobilesName.value;
			mobilesName=frm.mobilesName.value;
			var attendInfor;
			attendInfor = frm.attendInfor.value;
			if(attendInfor.indexOf(selectName)==-1){
			
				if(frm.attendInfor.value==""){
				frm.attendInfor.value=selectName;
				}else{
				frm.attendInfor.value=frm.attendInfor.value+","+selectName;
				}
			}
			if(mobiles.indexOf(selectPhone)==-1){
				if(frm.mobiles.value==""){
					frm.mobiles.value = selectPhone;
					
				}else{
					frm.mobiles.value=frm.mobiles.value + "," + selectPhone;
					
				}
			}
			
			if(mobilesName.indexOf(selectMobilesName)==-1){
				if(frm.mobilesName.value==""){
					frm.mobilesName.value = selectMobilesName;
					
				}else{
					frm.mobilesName.value=frm.mobilesName.value + "," + selectMobilesName;
					
				}
			}
		}		
</script>
<%
 ServletContext context = request.getSession().getServletContext();
 WebApplicationContext webContext = WebApplicationContextUtils.getWebApplicationContext(context);
 
 //获取全部有手机号码的客户
	CompanyAddressManager companyAddressManager = (CompanyAddressManager) webContext.getBean("companyAddressManager");
	List persons = companyAddressManager.getMobilePerson();
 
 
 //SystemUserManager userManager = (SystemUserManager) webContext.getBean("systemUserManager");
 //List users = userManager.getAllUser();
 //request.setAttribute("_Users", users); 
%>
<base target="_self"/>
<body onload="">
	<br/>
	<form:form commandName="meetInforVo" name="meetInforForm" id="meetInforForm" action="" method="post" enctype="multipart/form-data">
		<form:hidden path="meetId" />
		<form:hidden path="summary" />
		<input type="hidden" name="handle" value="<%=request.getParameter("handle") %>"/>	
		<input type="hidden" name="handle" value="<%=request.getParameter("handle") %>"/>	
		
		<table width="98%" cellpadding="6" cellspacing="1" align="center"
			border="0" bordercolor="#c5dbec">

			<tr>
				<td style="width: 130px;" class="ui-state-default jqgrid-rownum">
					会议名称：
				</td>
				<td>
					<form:input path="meetName" size="50" />
					&nbsp;
					<font color="red">*</font>
				</td>
			</tr>
			<tr>
				<td style="width: 100px;" class="ui-state-default jqgrid-rownum">
					所属部门：
				</td>
				<td>
				<form:select path="organizeId">
					<form:option value="0">--选择部门--</form:option>
					<c:forEach items="${_Departments}" var="department">
					<form:option value="${department.organizeId}">${department.organizeName}</form:option>
					</c:forEach>
				</form:select>
					
				</td>
			</tr>
			<tr>
				<td style="width: 100px;" class="ui-state-default jqgrid-rownum">
					会议日期：
				</td>
				<td>
					<form:input path="meetDate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly="true"/>
					&nbsp;
					<font color="red">*</font>
				</td>
			</tr>
			<tr>
				<td style="width: 100px;" class="ui-state-default jqgrid-rownum">
					会议结束日期：
				</td>
				<td>
					<input type="text" name="endMeetDate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" onchange="validateDate();" readonly="readonly" value="${_EndMeetDate}"/>
				</td>
			</tr>
			<tr>
				<td style="width: 100px;" class="ui-state-default jqgrid-rownum">
					时间：
				</td>
				<td>
					<form:select path="startHour">
												<form:option value="0">
													0
												</form:option>
												<form:option value="1">
													1
												</form:option>
												<form:option value="2">
													2
												</form:option>
												<form:option value="3">
													3
												</form:option>
												<form:option value="4">
													4
												</form:option>
												<form:option value="5">
													5
												</form:option>
												<form:option value="6">
													6
												</form:option>
												<form:option value="7">
													7
												</form:option>
												<form:option value="8">
													8
												</form:option>
												<form:option value="9">
													9
												</form:option>
												<form:option value="10">
													10
												</form:option>
												<form:option value="11">
													11
												</form:option>
												<form:option value="12">
													12
												</form:option>
												<form:option value="13">
													13
												</form:option>
												<form:option value="14">
													14
												</form:option>
												<form:option value="15">
													15
												</form:option>
												<form:option value="16">
													16
												</form:option>
												<form:option value="17">
													17
												</form:option>
												<form:option value="18">
													18
												</form:option>
												<form:option value="19">
													19
												</form:option>
												<form:option value="20">
													20
												</form:option>
												<form:option value="21">
													21
												</form:option>
												<form:option value="22">
													22
												</form:option>
												<form:option value="23">
													23
												</form:option>

											</form:select>
											时
											<form:select path="startMinutes">
												<form:option value="0">
													0
												</form:option>
												<form:option value="15">
													15
												</form:option>
												<form:option value="30">
													30
												</form:option>
												<form:option value="45">
													45
												</form:option>
											</form:select>
											分 &nbsp;&nbsp;
											<font color="RED">至</font> &nbsp;&nbsp;
											<form:select path="endHour">
												<form:option value="0">
													0
												</form:option>
												<form:option value="1">
													1
												</form:option>
												<form:option value="2">
													2
												</form:option>
												<form:option value="3">
													3
												</form:option>
												<form:option value="4">
													4
												</form:option>
												<form:option value="5">
													5
												</form:option>
												<form:option value="6">
													6
												</form:option>
												<form:option value="7">
													7
												</form:option>
												<form:option value="8">
													8
												</form:option>
												<form:option value="9">
													9
												</form:option>
												<form:option value="10">
													10
												</form:option>
												<form:option value="11">
													11
												</form:option>
												<form:option value="12">
													12
												</form:option>
												<form:option value="13">
													13
												</form:option>
												<form:option value="14">
													14
												</form:option>
												<form:option value="15">
													15
												</form:option>
												<form:option value="16">
													16
												</form:option>
												<form:option value="17">
													17
												</form:option>
												<form:option value="18">
													18
												</form:option>
												<form:option value="19">
													19
												</form:option>
												<form:option value="20">
													20
												</form:option>
												<form:option value="21">
													21
												</form:option>
												<form:option value="22">
													22
												</form:option>
												<form:option value="23">
													23
												</form:option>

											</form:select>
											时
											<form:select path="endMinutes">
												<form:option value="0">
													0
												</form:option>
												<form:option value="15">
													15
												</form:option>
												<form:option value="30">
													30
												</form:option>
												<form:option value="45">
													45
												</form:option>
											</form:select>
											分
											<font color="red">*</font>
				</td>
			</tr>
			<tr>
				<td style="width: 100px;" class="ui-state-default jqgrid-rownum">
					会议地点：
				</td>
				<td>
					<form:input path="meetRoom" size="25" />
					&nbsp;
					<font color="red">*</font>
				</td>
			</tr>
			
			
			<tr>
				<td style="width: 100px;" class="ui-state-default jqgrid-rownum">
					预定人：
				</td>
				<td>
					<form:select path="bookId">
					<form:option value="0">--选择人员--</form:option>
					<c:forEach items="${_Users}" var="user">
					<form:option value="${user.person.personId}">${user.person.personName}</form:option>
					</c:forEach>
					</form:select>
				</td>
			</tr>
			
			<tr>
				<td style="width: 100px;" class="ui-state-default jqgrid-rownum">
					会议记录者：
				</td>
				<td>
					<form:select path="recordId">
					<form:option value="0">--选择人员--</form:option>
					<c:forEach items="${_Users}" var="user">
					<form:option value="${user.person.personId}">${user.person.personName}</form:option>
					</c:forEach>
					</form:select>
				</td>
			</tr>
			<tr>
				<td valign="top">
					内容：
				</td>
				<td>
					<table cellspacing=0 cellpadding=0>
						<tr>
							<td>
								<%--<div id='cdiv' bgcolor='#deddde' style='position:relative; left:0px; top:0px; height:350; width:500; border:solid 1px #9c9c9c;'>
									<FCK:editor id="content" basePath="/fckeditor/" width="700" height="400" skinPath="/fckeditor/editor/skins/default/" toolbarSet="Default">
										${_Meet.content}
									</FCK:editor>
								</div>--%>
								<form:textarea path="content" cols="65" rows="8"/>
							</td>

						</tr>
					</table>
				</td>
			</tr>

			<tr>
				<td style="width: 100px;" class="ui-state-default jqgrid-rownum">
					主持人：
				</td>
				<td>
					<form:input path="compere" size="20" />
					
				</td>
			</tr>
			
			<tr>
				<td style="width: 100px;" class="ui-state-default jqgrid-rownum">
					参加者：
				</td>
				<td>
					<form:input path="attendInfor" size="50" />
					
				</td>
			</tr>
			
			<tr>
				<td valign="top">
					会议要求：
				</td>
				<td>
					<table cellspacing=0 cellpadding=0>
						<tr>
							<td>
								<form:textarea path="demand" cols="65" rows="8"/>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			
			<tr>
				<td valign="top">
					手机提醒对象：
				</td>
				<td>
					<table cellspacing=0 cellpadding=0>
						<tr>
							<td>
								<textarea  name="mobiles" rows="3" cols="60"></textarea>(注:如果自行录入手机号码,请使用","隔开各个号码) </br>	
								<textarea  name="mobilesName" rows="3" cols="60" readonly></textarea>人员与手机号对比（只读）	
	               		  
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td valign="top">
					手机提醒方式：
				</td>
				<td>
					<table cellspacing=0 cellpadding=0>
						<tr>
							<td>
							<select name="status">
                     				<option value="1">会议开始前1小时发送</option>
                    				 <option value="0">立即发送</option>
               			 </select> 
							</td>
						</tr>
					</table>
				</td>
			</tr>
			
			<tr>
				<td style="width: 100px;" class="ui-state-default jqgrid-rownum">
					<!--选择员工-->选择手机提醒对象：
				</td>
				<td>
					<select name="personId">
								<option value="">-全部人员-</option>
								<%	               					
	               					for (Iterator it = persons.iterator();it.hasNext();) {
										CompanyAddress person = (CompanyAddress)it.next();
	               						if(person.getMobile()!=null && !person.getMobile().equals("")){
	               				%>
	               							<option value="<%=person.getMobile()%>"><%=person.getPersonName()%></option>
	               				<%
	               						}
	               					}
	               					
	               				 %>
							</select>
							&nbsp;<input type="button" value="加入目标手机" onClick="addPerson();">
					
				</td>
			</tr>
			

			<tr id="users" valign="top">
				<td>
					邮件提醒对象：
				</td>
				<td>
					<table width="100%">
						<c:set var="_Num" value="0" />
						<c:forEach var="department" items="${_Departments}">
							<script language="javaScript">
								clickTimes[${_Num}] = 0;							
							</script>

							<tr height="33">
								<td valign="bottom" width="100%"
									style="border-bottom:1px dotted #888888;font-size:10pt"
									colspan="2">
									${department.organizeName}&nbsp;
									<span onclick="show_list('${_Num}')"> <img name="img"
											src="<c:url value="${'/images'}"/>/xpexpand3_s.gif"
											style="margin-top:0px;margin-bottom:0px;" /> </span>
								</td>
							</tr>

							<tr name="tr" style="display:none;padding-top:10px;">
								<td width="9%" align="right" valign="top">
									<input type="checkbox" onclick="selectUserId(this,'${department.organizeId}')" />全选
								</td>
									<td style="padding-left:10px;" width="92%">
									<table>
										<tr>
				      						<c:set var="_TypeNum" value="0"/>
				      						<c:forEach var="user" items="${_Persons}" varStatus="index">
				      							<c:if test="${user.person.department.organizeId==department.organizeId}">
													<c:if test="${_TypeNum!=0 && _TypeNum%6==0}">
														</tr><tr>
													</c:if>
													<td width="16%" valign="top">
														<form:checkbox path="AttendIds" value="${user.personId}"/> ${user.person.personName}
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
				      						<c:forEach var="user" items="${_OtherPersons}" varStatus="index">
				      							<c:if test="${user.person.department.organizeId==department.organizeId}">
													<c:if test="${_TypeNum!=0 && _TypeNum%6==0}">
														</tr><tr>
													</c:if>
													<td width="16%" valign="top">
														<form:checkbox path="AttendIds" value="${user.personId}"/> ${user.person.personName}
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
							<c:set var="_Num" value="${_Num+1}" />
						</c:forEach>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					附件：
				</td>
				<td>

					<table cellpadding="0" cellspacing="0"
						style="margin-bottom:0;margin-top:0">
						<tr>
							<td>
								<input type="file" name="attachment" size="50" />
								&nbsp;
								<input type="button" value="更多附件.." onclick="addtable('newstyle')"
									class="bt" />
							</td>
						</tr>
					</table>
					<span id="newstyle"></span>
				</td>
			</tr>
			<c:if test="${!empty meetInforVo.attachmentStr}">
				<tr>
					<td colspan="2" valign="top">
						原附件信息(
						<font color=red>如果要删除某个附件，请选择该附件前面的选择框</font>)：
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<c:forEach var="file" items="${_Attachment_Names}"
							varStatus="index">
							<form:checkbox path="attatchmentArray" value="${index.index}" />
							<a href="<%=request.getRealPath("/")%>${file}">${_Attachment_Names[index.index]}
							</a>
							<br/>
						</c:forEach>
					</td>
				</tr>
			</c:if>
			<tr>
				<td colspan="2" >
					<input type="button" id="button" style="cursor: pointer;"
						value="提交"
						onclick="javaScript:if(validateMeetInfor()){submitData();}" />
					&nbsp;
					<input type="button" value="取消" style="cursor: pointer;"
						onclick="window.close();" />
				</td>
			</tr>
		</table>
	</form:form>
</body>

