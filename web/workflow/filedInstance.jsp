<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
<title>归档</title>
<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
<script src="<c:url value="/"/>datePicker/WdatePicker.js" type="text/javascript"></script>
<script src="<c:url value="/"/>js/inc_javascript.js"></script>
<script src="<c:url value="/"/>js/addattachment.js"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/"/>components/jqgrid/css/jquery.ui.all.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
	<script>
		/** function filed() {
			var returnArray = ["refresh","${_Flow.flowId}"];
			window.returnValue = returnArray;
			alert('信息已归档！');
			window.close();
		} */
		
		//定义一个数组，记录各个数据点击的次数
		var clickTimes = new Array();
		var clickTimes_2 = new Array();
		
		//点击显示tr信息
		function show_list_2(objNum) {
			number=0;
			var tr;
			var img;
			var trs = getElementsByName('tr','tr_2');
			var imgs = getElementsByName('img','img_2');
			
			if (trs.length==null){
				tr = trs;
				img = imgs;
			}else{
				tr = trs[objNum];
				img = imgs[objNum];
			}
			
			if(clickTimes_2[objNum]%2==0){
				tr.style.display="";
				img.src="../images/xpcollapse3_s.gif"
			}else{
				tr.style.display="none";
				img.src="../images/xpexpand3_s.gif";
			}
			clickTimes_2[objNum] += 1;
		}
		
		$(document).ready(function(){
			//是否公告初始化为"是"
			$("input[name='isBulletin'][value=1]").attr("checked",true);
			displayDates();
			
			//切换显示"首页显示起止时间"
			$("input[name='isBulletin']").click(function displayDates(){
				var checkedVal = $("input[name='isBulletin']:checked").val();
				if (checkedVal == 1) {
					$("#dates").css("display","block");
				}else {
					$("#dates").css("display","none");
				}
			});
		});
		
		function submitData() {
			if ($("input[name='categoryIds']:checked").length == 0) {
				alert("请选择需要归入的分类！");
				$("#filedCategorys").focus();
			}else {
				var form = document.instanceInforForm;
				form.action = "<c:url value='/'/>workflow/instanceInfor.do?method=saveFiled";
				form.submit();
				alert("已归入所选分类！\r\n若有文件正在归档到档案系统，请勿刷新，并耐心等待！");
			}
		}
		
	</script>
</head>
<base target="_self"/>
<body style="overflow-y: auto;padding: 0 100px" onload="">
<br/>
<form:form commandName="flowInstanceInforVo" id="instanceInforForm" name="instanceInforForm" action="/workflow/instanceInfor.do?method=saveFiled" method="post" enctype="multipart/form-data">
<form:hidden path="instanceId"/>
<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 98%">
	<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
	<div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
		<span class="ui-jqgrid-title">归档 &nbsp;【${_Flow.flowName} 主办人：${_Instance.charger.person.personName}】</span>
	</div>

	<%-- 审核实例信息 --%>	
	<%@include file="includeInstance.jsp" %>

				<table cellspacing="0" cellpadding="0" border="0" style="width: 90%">
					<tbody>
						<tr class="ui-widget-content jqgrow ui-row-ltr">
							<td class="ui-state-default jqgrid-rownum" colspan="3"><b>归档操作如下</b>：</td>
						</tr>
						

						<c:if test="${_FileCan}">
							<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
								<td class="ui-state-default jqgrid-rownum" rowspan="2">所有附件：</td>

								<td>
									<c:if test="${!_IsFile}">
									<font color=#0DE8F5>
									请选择需要归入档案系统中的附件</font>
									</c:if>
								</td>

							</tr>

							<tr class="ui-widget-content jqgrow ui-row-ltr">
								<td style="padding:5px;">
									<c:forEach var="file" items="${_AllAttachments}" varStatus="index">
										<input type="checkbox" name="attatchmentArray" value="${file}" />
										<a href="<%=request.getRealPath("/")%>${file}">${_AllAttachment_Names[index.index]}</a><br/>
									</c:forEach>
								</td>
							</tr>
						</c:if>
						<c:if test="${!_FileCan}">
							<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
								<td class="ui-state-default jqgrid-rownum" rowspan="2">所有附件：</td>

							</tr>

							<tr class="ui-widget-content jqgrow ui-row-ltr">
								<td style="padding:5px;">
									<c:forEach var="file" items="${_AllAttachments}" varStatus="index">
										<a href="<%=request.getRealPath("/")%>${file}">${_AllAttachment_Names[index.index]}</a><br/>
									</c:forEach>
								</td>
							</tr>
						</c:if>
						
						   <c:if test="${_Contract}">
					   <tr class="ui-widget-content jqgrow ui-row-ltr"> 
                        <td class="ui-state-default jqgrid-rownum" style="width: 15%" id="filedCategorys">合同编号：</td> 
                        <td>
						<input name="contractNo" id="contractNo" size="20" value="${_ContractNo}" />
						</td> 
                      </tr>
						</c:if>
						<tr > 
                        <td class="ui-state-default jqgrid-rownum" style="width: 15%" id="filedCategorys">归入分类：</td> 
                        <td>
							
							<table width="100%">
								<tr>
								   <c:forEach items="${_FiledCategory}" var="category" varStatus="status">
								   		<c:if test="${status.index!=0 && status.index%5==0}">
											</tr><tr>
										</c:if>
	                            		<td><form:checkbox path="categoryIds" value="${category.categoryId}"/> ${category.categoryName}</td>
	                               </c:forEach>
	                           </tr>
							</table>
							
							<%--<table width="100%">
								<tr><td>
								 <c:set var="_Num" value="0"/>
								 <c:forEach items="${_Parent_Category}" var="parent" varStatus="index">
								 	<script language="javaScript">
										clickTimes[${_Num}] = 0;							
									</script> 
								 
								   <tr height="33">
	                            	<td valign="bottom" width=100% style='border-bottom:1px dotted #888888;font-size:10pt' onclick="show_list('${_Num}')">
	                            		${parent.categoryName}
								        &nbsp;&nbsp;
										<img name="img" src="<c:url value="${'/images'}"/>/xpexpand3_s.gif" style="margin-top:0px;margin-bottom:0px;"/>
									</td>
		      					  </tr>
								   
								   <tr name="tr" style="display:none;padding-top:10px;">
								   <td style="padding-left:50px;">
								   <table>
								   	<tr>
								     	<c:set var="_Category_Num" value="0"/>
								   		<c:forEach items="${_ReturnList[index.index]}" var="leaf" varStatus="status">
								   			<c:if test="${_Category_Num!=0 && _Category_Num%5==0}">
												</tr><tr>
											</c:if>
	                            			<td><form:checkbox path="categoryIds" value="${leaf.categoryId}"/> ${leaf.categoryName}</td>
	                            			<c:set var="_Category_Num" value="${_Category_Num+1}"/>
	                               		</c:forEach>  
	                               	</tr>
	                               </table>
	                               </td>
		      				      </tr>
		      				      <c:set var="_Num" value="${_Num+1}"/>
								 </c:forEach>
								</td></tr>
							</table>--%>
						</td> 
                      </tr>
                      
                      <c:if test="${_BulletinAndMessage}">
	                      <tr class="ui-widget-content jqgrow ui-row-ltr">
							<td class="ui-state-default jqgrid-rownum" colspan="3"></td>
						  </tr>
	                      
	                      <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;"> 
	                        <td class="ui-state-default jqgrid-rownum" style="width: 15%">是否公告：</td>
	                        <td><input type="radio" name="isBulletin" value="0"/>否 <input type="radio" name="isBulletin" value="1"/>是</td>
	                      </tr>
	                      <tr class="ui-widget-content jqgrow ui-row-ltr" id="dates" style="height: 30px;"> 
	                        <td class="ui-state-default jqgrid-rownum" style="width: 15%">首页显示起止时间：</td>
	                        <td><input type="text" name="startDate" size="12" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly="true" value="${_SystemDate}"/> 至 <input type="text" name="endDate" size="12" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly="true"/></td>
	                      </tr>
	                      
	                      <tr> 
	                        <td class="ui-state-default jqgrid-rownum" style="width: 15%">邮件通知：</td>
	                        <td>
	                        	<table width="100%" border="0">
									<c:set var="_Num_2" value="0"/>
										<c:forEach var="department" items="${_Departments}">
					                       <tr height="33">
					                          <td valign="bottom" width=100% style='border-bottom:1px dotted #888888;font-size:10pt' colspan="2">
					                            <script language="javaScript">
													clickTimes_2[${_Num_2}] = 0;							
												</script>	
					                            ${department.organizeName}
					                            &nbsp;	                            				
												<span onclick="show_list_2('${_Num_2}')">
													<img name="img_2" src="<c:url value="${'/images'}"/>/xpexpand3_s.gif" style="margin-top:0px;margin-bottom:0px;"/>
												</span>
											  </td>
						      			   </tr>
											
										   <tr name="tr_2" style="display:none;padding-top:10px;">
											 <td width="8%" align="right" valign="top">
						      					<input type="checkbox" onclick="selectUserId(this,'${department.organizeId}')"/><font color="#0DE8F5">全选</font>
						      				 </td>
						      								
						      				 <td style="padding-left:10px;" width="92%">
												<table>
						      						<tr>
						      							<c:set var="_TypeNum_2" value="0"/>
						      							<c:forEach var="user" items="${_Users}">
						      								<c:if test="${user.person.department.organizeId==department.organizeId}">
																<c:if test="${_TypeNum_2!=0 && _TypeNum_2%6==0}">
																	</tr><tr>
																</c:if>
																<td width="16%" valign="top">
																	<form:checkbox path="personIds" value="${user.personId}"/> ${user.person.personNo}-${user.person.personName}
																</td>					
																			
																<c:set var="_TypeNum_2" value="${_TypeNum_2+1}"/>
															</c:if>																			
														</c:forEach>
																		
														<c:forEach begin="${_TypeNum_2 % 6}" end="5">
															<td width="16%">&nbsp;</td>
														</c:forEach>
													</tr>
					                            	<c:set var="_Num_2" value="${_Num_2+1}"/>
												</table>
	                        				</td>
	                      				</tr>
	                      			</c:forEach>
	                      		</table>
	                      	</td>
	                    </tr>
                    </c:if>
                    
                    
                    
								
					<tr class="ui-widget-content jqgrow ui-row-ltr">
						<td class="ui-state-default jqgrid-rownum" colspan="3"><input style="cursor: pointer;" type="button" onclick="submitData();" value="保存"/>&nbsp;<input style="cursor: pointer;" type="button" value="关闭" onclick="window.close();"/></td>
					</tr>
						
					</tbody>
				</table>
	
</div>
</div>
</form:form>
</body>
</html>
                  
