<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<title>编辑俱乐部活动</title>
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
	//部门,班组,用户下拉联动 
		//下拉数据初始化
		$.fn.selectInit = function(){return $(this).html("<option value=''>请选择</option>");};
		
		//加载部门及联动信息
		$.loadDepartments_Persons = function(departmentId, groupId, personId) {
			//加载部门数据
			$.getJSON("/core/organizeInfor.do?method=getDepartments",function(data) {
				if (data != null) {
				    $.each(data._Departments, function(i, n) {
					    $("#"+departmentId).append("<option value='"+ n.organizeId + "'>" + n.organizeName + "</option>");
					});
				}
			});
			
			if ($("#"+groupId).html() != null) {
				//含下拉班组信息时,改变部门信息,联动班组信息
				$("#"+departmentId).bind("change",function(){
		           $("#"+groupId).selectInit();
		           var depSelectId = $("#"+departmentId+" option:selected").val();
		           var groupUrl = "/core/organizeInfor.do?method=getGroups&departmentId=" + depSelectId;
		           $.loadGroups(groupUrl, groupId);
				});
					
				//含下拉班组信息时,改变班组信息,联动用户信息
				$("#"+groupId).bind("change",function(){
			       $("#"+personId).selectInit();
			       var groSelectId = $("#"+groupId+" option:selected").val(); 
			       var userUrl = "/core/personInfor.do?method=getPersons&groupId=" + groSelectId;
			       $.loadPersons(userUrl, personId);
				});
			}else {
				//不含下拉班组信息时,改变部门信息,联动人员信息
				$("#"+departmentId).bind("change",function(){
		            $("#"+personId).selectInit();
		            var depSelectId = $("#"+departmentId+" option:selected").val();
		            var url = "/core/personInfor.do?method=getPersons&departmentId=" + depSelectId;
		            $.loadPersons(url, personId);                    
				});
			}
		}
			 
		//获取班组信息
		$.loadGroups = function(url, groupId) {
			$.getJSON(url,function(data) {
				if (data != null) {
			 		$.each(data._Groups, function(i, n) {
				 		$("#"+groupId).append("<option value='"+ n.organizeId +"'>" + n.organizeName + "</option>");
				 	});
			 	}
			});
		}
			 
		//获取人员信息
		$.loadPersons = function(url, personId) {
			$.getJSON(url,function(data) {
				if (data != null) {
					$.each(data._Persons, function(i, n) {
						$("#"+personId).append("<option value='"+ n.personId + "'>" + n.personName + "</option>");
					});
				}
			});
		}
</script>
<script>	
	
	//验证输入数据
	function validateClubInfor(){
		//alert(0);
		var actTitle;		
		var actTimeStr;
		var toTimeStr;
		var beginSignDateStr;
		var cutDateStr;
		var manager;
		actTitle = clubInforForm.actTitle.value;
		actTimeStr = clubInforForm.actTimeStr.value;
		toTimeStr = clubInforForm.toTimeStr.value;
		cutDateStr = clubInforForm.cutDateStr.value;
		beginSignDateStr = clubInforForm.beginSignDateStr.value;
		manager = clubInforForm.managerId.value;
		if(actTitle==""||actTitle==null){
			alert("请输入活动名称!");
			clubInforForm.actTitle.focus();
			return false;
		}
		
		if(actTimeStr==""||actTimeStr==null){
			alert("请输入开始时间!");
			clubInforForm.actTimeStr.focus();
			return false;
		}
		
		if(toTimeStr==""||toTimeStr==null){
			alert("请输入截止时间!");
			clubInforForm.toTimeStr.focus();
			return false;
		}
		
		if(beginSignDateStr==""||beginSignDateStr==null){
			alert("请输入报名开始日期!");
			clubInforForm.beginSignDateStr.focus();
			return false;
		}
		
		if(cutDateStr==""||cutDateStr==null){
			alert("请输入报名截止日期!");
			clubInforForm.cutDateStr.focus();
			return false;
		}
		
		if(manager==""||manager==null|| manager=='0'){
			alert("请选择活动管理员!");
			clubInforForm.manager.focus();
			return false;
		}
		//alert(11);
		return true;
	}
	//提交数据
	function submitData() {
		//alert(1);
		var form = document.clubInforForm;
		form.action = '<c:url value="/club/clubInfor"/>.do?method=save';
		form.submit();
		//window.opener.location.reload();
		//window.returnValue = "Y";
		//window.close();
	}
	
	
	$(document).ready(function(){
		//加载部门及联动信息
		$.loadDepartments_Persons("departmentId", null, "managerId");
	});
</script>
<base target="_self"/>
<body onload="">
	<br/>
	<form:form commandName="clubInforVo" name="clubInforForm" id="clubInforForm" action="" method="post" enctype="multipart/form-data">
		<form:hidden path="actId" />
		<form:hidden path="status" />
		
		<table width="98%" cellpadding="6" cellspacing="1" align="center"
			border="0" bordercolor="#c5dbec">

			<tr>
				<td style="width: 130px;" class="ui-state-default jqgrid-rownum">
					活动名称：
				</td>
				<td>
					<form:input path="actTitle" size="50" />
					&nbsp;
					<font color="red">*</font>
				</td>
			</tr>
			
			<tr>
				<td style="width: 130px;" class="ui-state-default jqgrid-rownum">
					活动项目：
				</td>
				<td>
					<form:textarea path="actItem" cols="65" rows="3"/>
				</td>
			</tr>
			
			<tr>
				<td style="width: 130px;" class="ui-state-default jqgrid-rownum">
					活动社团：
				</td>
				<td>
					<form:select path="league">
                    	<form:option value="">--选择社团--</form:option>
						<form:option value="“海之声”合唱团">“海之声”合唱团</form:option>
						<form:option value="博雅读书社">博雅读书社</form:option>
						<form:option value="光影流年">光影流年</form:option>
						<form:option value="酷跑团">酷跑团</form:option>
						<form:option value="轻“羽”飞扬">轻“羽”飞扬</form:option>
						<form:option value="勇“网”直前">勇“网”直前</form:option>
						<form:option value="“乒”博社">“乒”博社</form:option>
						<form:option value="奔跑吧，足球！">奔跑吧，足球！</form:option>
						<form:option value="力挽狂“篮”">力挽狂“篮”</form:option>
                    			
                   	</form:select>
                   	&nbsp;
					<font color="red">*</font>
				</td>
			</tr>
			
			<tr>
				<td style="width: 130px;" class="ui-state-default jqgrid-rownum">
					活动管理员：
				</td>
				<td>
					<select name="departmentId" id="departmentId">
                    	<option value="">--选择部门--</option>
                    </select>&nbsp;&nbsp;
					<form:select path="managerId">
                    	<form:option value="0">--选择管理员--</form:option>
                   		<c:forEach items="${_Users}" var="user">
							<form:option value="${user.personId}">${user.person.personName}</form:option>
						</c:forEach>
                    			
                   	</form:select>
                   	&nbsp;
					<font color="red">*</font>
				</td>
			</tr>
			
			
			
			<tr>
				<td style="width: 100px;" class="ui-state-default jqgrid-rownum">
					报名开始日期：
				</td>
				<td>
					<%--<form:input path="beginSignDateStr" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'cutDateStr\',{d:0});}',dateFmt:'yyyy-MM-dd'})" readonly="true"/>--%>
					<input name="beginSignDateStr" value="${_BeginSignDateStr}" id="beginSignDateStr" onclick="WdatePicker({startDate:'%y-%M-%d 00:00:00',maxDate:'#F{$dp.$D(\'cutDateStr\',{d:0,H:0,m:0});}',dateFmt:'yyyy-MM-dd 00:00:00'})" readonly="true"/>
					&nbsp;
					<font color="red">*</font>
				</td>
			</tr>
			
			<tr>
				<td style="width: 100px;" class="ui-state-default jqgrid-rownum">
					报名截止日期：
				</td>
				<td>
					<%--<form:input path="cutDateStr" onclick="WdatePicker({minDate:'#F{$dp.$D(\'beginSignDateStr\',{d:0});}',dateFmt:'yyyy-MM-dd'})" readonly="true"/>--%>
					<input name="cutDateStr" value="${_CutDateStr}" id="cutDateStr" onclick="WdatePicker({startDate:'%y-%M-%d 00:00:00',minDate:'#F{$dp.$D(\'beginSignDateStr\',{d:0,H:0,m:0});}',maxDate:'#F{$dp.$D(\'actTimeStr\',{d:0});}',dateFmt:'yyyy-MM-dd 00:00:00'})" readonly="true"/>
					&nbsp;
					<font color="red">*</font>
				</td>
			</tr>
			
			<tr>
				<td style="width: 100px;" class="ui-state-default jqgrid-rownum">
					活动时间起：
				</td>
				<td>
					<!--<form:input path="actTimeStr" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'toTimeStr\',{d:0});}',dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="true"/>-->
					<input name="actTimeStr" value="${_ActTimeStr}" id="actTimeStr" onclick="WdatePicker({startDate:'%y-%M-%d 00:00:00',minDate:'#F{$dp.$D(\'cutDateStr\',{d:0,H:0,m:0});}',maxDate:'#F{$dp.$D(\'toTimeStr\',{d:0,H:0,m:0});}',dateFmt:'yyyy-MM-dd HH:mm:00'})" readonly="true"/>
					&nbsp;
					<font color="red">*</font>
				</td>
			</tr>
			
			<tr>
				<td style="width: 100px;" class="ui-state-default jqgrid-rownum">
					活动时间止：
				</td>
				<td>
					<!--<form:input path="toTimeStr" onclick="WdatePicker({minDate:'#F{$dp.$D(\'actTimeStr\',{d:0});}',dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="true"/>-->
					<input name="toTimeStr" value="${_ToTimeStr}" id="toTimeStr" onclick="WdatePicker({startDate:'%y-%M-%d 00:00:00',minDate:'#F{$dp.$D(\'actTimeStr\',{d:0,H:0,m:0});}',dateFmt:'yyyy-MM-dd HH:mm:00'})" readonly="true"/>
					&nbsp;
					<font color="red">*</font>
				</td>
			</tr>
			
			<%--<tr>
				<td style="width: 130px;" class="ui-state-default jqgrid-rownum">
					活动社团：
				</td>
				<td>
					<select>
						<option>“海之声”合唱团</option>
						<option>博雅读书社</option>
						<option>光影流年</option>
						<option>酷跑团</option>
						<option>轻“羽”飞扬</option>
						<option>勇“网”直前</option>
						<option>“乒”博社</option>
						<option>奔跑吧，足球！</option>
						<option>力挽狂“篮”</option>
					</select>
					
					<font color="red">*</font>
				</td>
			</tr>--%>
			
			
			<c:if test="${_IsNew}">
				<tr>
					<td style="width: 130px;" class="ui-state-default jqgrid-rownum">
						<font color=red>间隔天数：</font>
					</td>
					<td>
						<input size="10" id="days" name="days" onkeyup="value=value.replace(/[^\d]/g,'')" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"/>
						&nbsp;
						<font color="red"><small>批量新增时填写，报名起止时间、活动起止时间均将推后相应的天数</small></font>
					</td>
				</tr>
				
				<tr>
					<td style="width: 130px;" class="ui-state-default jqgrid-rownum">
						<font color=red>循环次数：</font>
					</td>
					<td>
						<select id="loopCount" name="loopCount">
							<option value="1">1</option>
							<option value="2">2</option>
							<option value="3">3</option>
							<option value="4">4</option>
							<option value="5">5</option>
							<option value="6">6</option>
							<option value="7">7</option>
							<option value="8">8</option>
							<option value="9">9</option>
							<option value="10">10</option>
						</select>
						
						<font color="red"><small>批量新增时填写，为1时，总计生成1条数据，依此类推</small></font>
					</td>
				</tr>
			</c:if>
			
			
			<tr>
				<td style="width: 130px;" class="ui-state-default jqgrid-rownum">
					活动地点：
				</td>
				<td>
					<form:textarea path="actPlace" cols="65" rows="3"/>
				</td>
			</tr>
			
			<tr>
				<td style="width: 130px;" class="ui-state-default jqgrid-rownum">
					报名方法：
				</td>
				<td>
					<form:textarea path="registerWay" cols="65" rows="3"/>
				</td>
			</tr>
			
			<tr>
				<td style="width: 130px;" class="ui-state-default jqgrid-rownum">
					活动规则：
				</td>
				<td>
					<form:textarea path="actRule" cols="65" rows="3"/>
				</td>
			</tr>
			
			
			<tr>
				<td style="width: 130px;" class="ui-state-default jqgrid-rownum">
					备注：
				</td>
				<td>
					<form:textarea path="memo" cols="65" rows="3"/>
				</td>
			</tr>
			
			
			<tr>
				<td class="ui-state-default jqgrid-rownum">活动主图：</td>
				<td>
					<table cellpadding="0" cellspacing="0" style="margin-bottom:0;margin-top:0">
						<tr>
							<td><input type="file" name="attachment" size="50" />&nbsp;
								<br/>
								<font color=red><small>建议上传比例为16：10的图片，以获得最佳显示效果</small></font>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			
			<c:if test="${!empty clubInforVo.mainPicStr}">
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
						onclick="javaScript:if(validateClubInfor()){submitData();}" />
					&nbsp;
					<input type="button" value="取消" style="cursor: pointer;"
						onclick="window.close();" />
				</td>
			</tr>
		</table>
	</form:form>
</body>

