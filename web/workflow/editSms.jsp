<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>
<%@ page language="java" 
		import="org.springframework.web.context.WebApplicationContext,
				org.springframework.web.context.support.*,
				java.util.*,
				com.kwchina.oa.personal.address.service.CompanyAddressManager,
				com.kwchina.oa.personal.address.entity.CompanyAddress" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
	<title>短信</title>
	<script src="<c:url value="/"/>datePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
	<script src="<c:url value="/"/>js/inc_javascript.js"></script>
	<script src="<c:url value="/"/>js/addattachment.js"></script>
	
	<link rel="stylesheet" type="text/css" href="<c:url value="/"/>components/jqgrid/css/jquery.ui.all.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
	
	<!-- formValidator -->


	<!-- ------------- -->
	
	<style type="text/css"> 	
			.buttonclass {
				font-weight:bold;
			}				
			/**input {color:expression(this.type=="button"?"red":"blue") } */		
			
	</style>
	
	<script>		
		

		//保存审核
		/** function save(){
			var form = document.instanceCheckInforForm;
			form.action = "<c:url value='/workflow/checkInfor.do'/>?method=save";
			form.submit();
		} */
		
		
		//去掉初始化的提示信息
		$("#checkCommentTip").html("");
			
		$(document).ready(function(){
			//button字体变粗
			for(i=0;i<document.getElementsByTagName("INPUT").length;i++){ 
						if(document.getElementsByTagName("INPUT")[i].type=="button" || document.getElementsByTagName("INPUT")[i].type=="submit") 
						document.getElementsByTagName("INPUT")[i].className="buttonclass" ;
				}		
		});	
		
		
			
	function addPerson(){
			//alert(frm);
			//alert(frm.personId);
			var frm = document.instanceCheckInforForm;
			var selectPhone;
			selectPhone=frm.personId.options[frm.personId.selectedIndex].value;
			
			var mobiles;
			mobiles=frm.mobiles.value;
			if(mobiles.indexOf(selectPhone)==-1){
				if(frm.mobiles.value==""){
					frm.mobiles.value = selectPhone;
				}else{
					frm.mobiles.value=frm.mobiles.value + "," + selectPhone;
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
</head>
<body>

<form:form commandName="instanceCheckInforVo" id="instanceCheckInforForm" name="instanceCheckInforForm" action="/workflow/checkInfor.do?method=saveSms" method="post" enctype="multipart/form-data">
	<form:hidden path="checkId"/>
	<input type="hidden" name="chargerEdit" value="${param.chargerEdit}"/>
	
	<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 98%">
	<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
	  	<div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
	    	<span class="ui-jqgrid-title">审批文件 &nbsp;【${_Flow.flowName} 主办人:${_Instance.charger.person.personName}】</span>
	  	</div>
	
		
		<%-- 审核实例信息 --%>	
		<%@include file="includeInstance.jsp" %>
	
		<div>
			<table cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable" style="width: 90%" id="addTable">
				<tbody>
					<tr>
						<td class="ui-state-default jqgrid-rownum" style="width: 15%"></td>
						<td></td>
				    </tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr">
						<td colspan="2" height="20"></td>
					</tr>
							
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum" style="width: 15%">提醒内容：</td>
						<td><textarea name="content" rows="5" cols="60">${_Content }</textarea>
							
                        

						</td>						
					</tr>
					
				
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">手机提醒对象：</td>
							<td><textarea name="mobiles" rows="3" cols="60"></textarea>	
	               		(注:如果自行录入手机号码,请使用","隔开各个号码)   </td>
						</tr>
					
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">选择人员：</td>
							<td><select name="personId">
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
							&nbsp;<input type="button" value="加入目标手机" onClick="addPerson();">  </td>
						</tr>
					
					
					

				</tbody>
			</table>			
		</div>
		
		<div style="width: 100%" class="ui-state-default ui-jqgrid-pager ui-corner-bottom" dir="ltr">
			<td><input style="cursor: pointer;" type="submit" value="发送"/></td>
			<td><input style="cursor: pointer;" type="button" value="关闭" onclick="window.close();"/></td>
		</div>
	</div>
	</div>
</form:form>
</body>
</html>
                  
