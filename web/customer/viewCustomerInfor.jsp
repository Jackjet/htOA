<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
<title>客户信息查看</title>
<link type="text/css" href="<c:url value="/"/>components/jqgrid/css/jquery.ui.all.css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
<script>

	//添加联系人
    function addContact(contactId,customerId){
		var path;
		path = "<c:url value='/customer/customerInfor.do'/>?method=editContact&contactId="+contactId+"&customerId="+customerId;
		window.name = "__self";
		window.open(path, "__self");  //注意是2个下划线 	
	}
	//修改
	function changeInfor(customerId){
		var path;
		path = "<c:url value='/customer/customerInfor.do'/>?method=edit&customerId="+customerId;
		window.name = "__self";
		window.open(path, "__self");  //注意是2个下划线 
	}
	//修改联系人
	function changeContact(contactId,customerId){
		var path;   
		path = "<c:url value='/customer/customerInfor.do'/>?method=editContact&contactId="+contactId+"&customerId="+customerId;
		window.name = "__self";
		window.open(path, "__self");  //注意是2个下划线 
	}
	//删除联系人
	function deleteContact(contactId,customerId){
		var path;
		path = "<c:url value='/customer/customerInfor.do'/>?method=deleteContact&contactId="+contactId+"&customerId="+customerId;
		window.name = "__self";
		window.open(path, "__self");  //注意是2个下划线 	
	}
	
</script>
</head>
<body>	 
<form>
<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 100%">
<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
  <div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
    <span class="ui-jqgrid-title">客户信息查看</span>
  </div>

	<div class="ui-jqgrid-bdiv" style="height: auto; width: 100%">
		<div style="position: relative;">
			<div>
				<table cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable" style="width: 100%">
					<tbody>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 10px;">
							<td style="width: 15%;" class="ui-state-default jqgrid-rownum">公司名称：</td>
							<td style="width: 85%;">${_customerinfor.companyName}</td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 10px;">
							<td class="ui-state-default jqgrid-rownum" >客户类型：</td>
							<td valign="top"><c:if test="${_customerinfor.customerType==0}">潜在客户</c:if><c:if test="${_customerinfor.customerType==1}">客户</c:if></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 50px;">
							<td class="ui-state-default jqgrid-rownum">公司介绍：</td>
							<td valign="top">${_customerinfor.companyIntroduction}</td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 10px;">
							<td class="ui-state-default jqgrid-rownum">公司网址：</td>
							<td>${_customerinfor.website}</td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 10px;">
							<td class="ui-state-default jqgrid-rownum">公司电话：</td>
							<td>${_customerinfor.phone}</td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 20px;">
							<td class="ui-state-default jqgrid-rownum">公司地址：</td>
							<td>${_customerinfor.companyAddress}</td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 70px;">
							<td class="ui-state-default jqgrid-rownum">备注：</td>
							<td valign="top">${_customerinfor.memo}</td>
						</tr>
						
						
						<c:if test="${!empty _ContactInforList}">
							<tr>
								<td colspan="2">
									<table width="100%"  border="0" cellpadding="0" cellspacing="0">
										 <tr><td>&nbsp;</td></tr>
										 <tr><td align="left" style="font-family:Verdana;font-size:20px;font-weight:bold">联系人信息:</td></tr>							
										 <tr><td>&nbsp;</td></tr>	
									</table>		
								</td>
							</tr>
							
							<tr>
								<td colspan="2">
									<table cellspacing="0" cellspacing="5" cellpadding="2" border="1"  width="95%">		
									<tr style="background-color:#97FFFF"><th width="10%">联系人姓名</th>
										<th width="10%" align="center">职务</th>
										<th width="14%" align="center">固定电话</th>
										<th width="13%" align="center">手机</th>
										<th width="13%" align="center">传真</th>
										<th width="18%" align="center">电子邮箱</th>
										<th width="12%" align="center">生日</th>
										<th width="10%"	align="center">操作</th>
									</tr>												
									<c:forEach items="${_ContactInforList}" var="contact" varStatus="status">													
											<tr>
												<td>${contact.contactName}</td>
												<td>${contact.duty}</td>
												<td align="center">${contact.phone}</td>
												<td align="center">${contact.mobile}</td>
												<td>${contact.fax}</td>
												<td>${contact.email}</td>
												<td align="center">${contact.birthday}</td>
												<td align="center"><a onclick="changeContact(${contact.contactId},${_customerinfor.customerId});" href="#" style="text-decoration: none;">修改</a>
													/<a onclick="deleteContact(${contact.contactId},${_customerinfor.customerId});" href="#" style="text-decoration: none;" >删除</a>
												</td>
											</tr>
									</c:forEach>
									</table>
								</td>
							</tr>
							<tr><td colspan="2">&nbsp;</td></tr>
						</c:if>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	
	<div style="width: 100%" class="ui-state-default ui-jqgrid-pager ui-corner-bottom" dir="ltr">
		<div role="group" class="ui-pager-control">
			<table cellspacing="0" cellpadding="0" border="0" style="width: 100%; table-layout: fixed;" class="ui-pg-table">
				<tbody>
					<tr>
						<td align="left">
							<table cellspacing="0" cellpadding="0" border="0" style="float: left; table-layout: auto;" class="ui-pg-table navtable">
								<tbody>
									<tr><td><input style="cursor: pointer;" type="button" value="关闭" onclick="window.close();"/></td>
										<td><input style="cursor: pointer;" type="button" value="添加联系人" onclick="addContact('0','${_customerinfor.customerId}');"/></td>
										<td><input style="cursor: pointer;" type="button" value="修改" onclick="changeInfor('${_customerinfor.customerId}');"/></td>
									</tr>
								</tbody>
							</table>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</div>
</div>
</form>
</body>
</html>
