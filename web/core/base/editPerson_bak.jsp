<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

			<form:form commandName="personInforVo" id="personForm" action="/core/personInfor.do?method=save" method="post" enctype="multipart/form-data">
			<form:hidden path="personId"/>
			    <table>
			        <tr> 
                      <td width="110">员工姓名：</td>
                      <td>
                      	<form:input path="personName" size="20"/>
                      </td>
                      <td><div id="personNameTip" style="width:200px"></div></td>
                    </tr>
                    
                    <tr> 
                      <td>人员编号：</td>
                      <td colspan="2"><form:input path="personNo" size="20"/></td>
                    </tr>
                    
                    <tr> 
                      <td>性别：</td>
                      <td colspan="2"> 
                      	<form:select path="gender">
                      		<form:option value="0">男</form:option>
                      		<form:option value="1">女</form:option>
                      	</form:select>
                      </td>
                    </tr>    
                    
                    <tr> 
                      <td>所属部门：</td>
                      <td> 
                      	<form:select path="departmentId">
                      		<form:option value="0">--选择部门--</form:option>
                      		<c:forEach items="${_Departments}" var="department">
								<form:option value="${department.organizeId}">${department.organizeName}</form:option>
							</c:forEach>
                      	</form:select>
                      </td>
                      <td><div id="departmentIdTip" style="width:200px"></div></td>
                    </tr>
                    
                    <tr> 
                      <td>所属班组：</td>
                      <td colspan="2"> 
                      	<form:select path="groupId">
                      		<form:option value="0">--选择班组--</form:option>
                      		<c:forEach items="${_Groups}" var="group">
								<form:option value="${group.organizeId}">${group.organizeName}</form:option>
							</c:forEach>
                      	</form:select>
                      </td>
                    </tr>                    
                    
                    <c:if test="${!empty _Structures}">
	                    <tr> 
	                      <td>工作岗位：</td>
	                      <td colspan="2">
	                      	<form:select path="structureId">
	                      		<c:forEach items="${_Structures}" var="structure">
									<form:option value="${structure.structureId}">
										<c:forEach begin="0" end="${structure.layer}">&nbsp;&nbsp;&nbsp;</c:forEach>
										<c:if test="${structure.layer==1}"><b>+</b></c:if><c:if test="${structure.layer==2}"><b>-</b></c:if>${structure.structureName}				
									</form:option>
								</c:forEach>
	                      	</form:select>
	                        <font color=#ff0000>*</font>
	                      </td>                      
	                    </tr>
                    </c:if>
                    
                    <tr> 
                      <td>职级：</td>
                      <td>
                      	<form:input path="positionLayer" size="20"/>
                      </td>
                      <td><div id="positionLayerTip" style="width:200px"></div></td>                   
                    </tr>
                    
                    <tr> 
                      <td>手机：</td>
                      <td>
                      	<form:input path="mobile" size="40"/>
                      </td>
                      <td><div id="mobileTip" style="width:200px"></div></td>
                    </tr>
                    
                    <tr> 
                      <td>email：</td>
                      <td>
                      	<form:input path="email" size="40"/>
                      </td>
                      <td><div id="emailTip" style="width:200px"></div></td>
                    </tr>
                    
                    <tr> 
                      <td>出生日期：</td>
                      <td colspan="2"><form:input path="birthday" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly="true"/></td>
                    </tr>

					<tr> 
                      <td>办公室地址：</td>
                      <td colspan="2"><form:input path="officeAddress" size="60"/></td>
                    </tr>
                    
                    <tr> 
                      <td>办公室电话：</td>
                      <td><form:input path="officePhone" size="60"/></td>
                      <td><div id="officePhoneTip" style="width:200px"></div></td>
                    </tr>
                    
                    <tr> 
                      <td>办公室邮编：</td>
                      <td colspan="2"><form:input path="officeCode" size="60"/></td>
                    </tr>


					<tr> 
                      <td>家庭地址：</td>
                      <td colspan="2"><form:input path="homeAddress" size="60"/></td>
                    </tr>
                    
                    <tr> 
                      <td>家庭电话：</td>
                      <td><form:input path="homePhone" size="60"/></td>
                      <td><div id="homePhoneTip" style="width:200px"></div></td>
                    </tr>
                    
                    <tr> 
                      <td>家庭邮编：</td>
                      <td colspan="2"><form:input path="postCode" size="60"/></td>
                    </tr>
	
                    <tr> 
                      <td>备注内容：</td>
                      <td colspan="2"> 
                      	<form:textarea path="memo" cols="50" rows="5"/>
                      </td>
                    </tr>
					
					<%--<input type="submit" value="Submit" />--%>
					
			    </table>
			</form:form>