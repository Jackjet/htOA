<%@ page contentType="text/html; charset=GBK"%>
<%@ include file="/inc/taglibs.jsp"%>
<%@ page language="java" 
		import="org.springframework.web.context.WebApplicationContext,
				org.springframework.web.context.support.*,
				java.util.*,
				com.kwchina.oa.personal.address.service.CompanyAddressManager,
				com.kwchina.oa.personal.address.entity.CompanyAddress " %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<title>Add Mobile Message</title>
</head>	


<link type="text/css" rel="stylesheet" href="<c:url value="/"/>sms/css/default.css">
<link type="text/css" rel="stylesheet" href="/css/gundongtiao.css">
<%--<link type="text/css" rel="stylesheet" href="/css/myTable.css">--%>

<%
 ServletContext context = request.getSession().getServletContext();
 WebApplicationContext webContext = WebApplicationContextUtils.getWebApplicationContext(context);
 
 //��ȡȫ�����ֻ�����Ŀͻ�
 CompanyAddressManager companyAddressManager = (CompanyAddressManager) webContext.getBean("companyAddressManager");
 List persons = companyAddressManager.getMobilePerson();
 
 
 //SystemUserManager userManager = (SystemUserManager) webContext.getBean("systemUserManager");
 //List users = userManager.getAllUser();
 //request.setAttribute("_Users", users); 
%>

<script language="javascript">  
	  function checkinfor(){	    	
		if(frm.mobiles.value==""){      
		   	alert("���������ѡ��Ŀ���ֻ�!");
		   	frm.mobiles.focus();
		   	return false;
		}	
		
		if(frm.content.value=="0"){		
			alert("���������Ϣ����!")
			frm.content.focus();
			return false;
		}
		
	    return true;	
	 }
 					
		function addPerson(){
			//alert(frm);
			//alert(frm.personId);
			var selectPhone;
			var selectName;
			var selectMobilesName;
			selectPhone=frm.personId.options[frm.personId.selectedIndex].value;
			selectName=frm.personId.options[frm.personId.selectedIndex].innerHTML;
			selectMobilesName=selectName+"("+selectPhone+")";
			
			var mobiles;
			var mobilesName;
			mobiles=frm.mobiles.value;
			mobilesName=frm.mobilesName.value;
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

<style>
	input,input[type="text"],textarea,select,input[type="file"]{
		border: solid 1px #18818b;
		color: white;
		border-radius: 3px;
		background-color: rgba(10, 95, 162, 0);
		padding: 5px 5px 5px 5px;
	}
	select option{
		background-color: #102133;
		color: white;
	}
	input[type="button"],input[type="submit"]{
		border: none;
		background-color: rgba(255, 255, 255, 0.2);
		color: white;
		box-shadow: none;
		/*height: auto;!important;*/
	}
	td{
		padding: 20px 0 10px 0;
		overflow-y: visible;
	}
</style>
<body style="padding: 0 100px;border:0px solid #0DE8F5;border-radius: 5px">
<br/>
<TABLE cellSpacing=0 cellPadding=2 width="100%" border=0 style="background-color: rgba(255,255,255,0)">
  <tr> 
    <td> 
    	<form action="saveMessage.jsp" method="post" name="frm" onsubmit="return checkinfor();">
    	<table width="100%"  border="0" cellpadding="0" cellspacing="4" bordercolor="#004A80"> 
	    	<tr>
	    		<td> 
	    		</td>
	    	</tr>
	        <tr> 
	          <td> 
	          
	            <!---------     -------------->              	             	             
	             <table border="0" width="100%" cellpadding="4" cellspacing="1" style="background-color: rgba(255,255,255,0)">
	               	<tr >
	               		<td width="100" style="color: #22FBFF">Ŀ���ֻ�</td>
	               		<td>
	               		<textarea name="mobiles" rows="3" cols="60"></textarea>	
							<span style="color: red">(ע:�������¼���ֻ�����,��ʹ��Ӣ�Ķ���","������������)</span></br>
	               		<textarea  name="mobilesName" rows="3" cols="60" readonly></textarea><span style="color: #f2ebff">��Ա���ֻ��ŶԱȣ�ֻ����  </span>
	               		</td>
	               	</tr>
	               	<tr >
	               		<td width="100" style="color: #22FBFF">ѡ������Ա��</td>
	               		<td>	               			
	               			<select name="personId">
								<option value="">-ȫ����Ա-</option>
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
							&nbsp;<input type="button" value="����Ŀ���ֻ�" onClick="addPerson();">
	               		</td>
	               	</tr>
	               	
	               	<tr >
	               		<td width="100" style="color: #22FBFF">����Ϣ����</td>
	               		<td>
	               		<textarea name="content" rows="5" cols="60"></textarea>	
	               		</td>
	               	</tr>
	             </table>	           
	           
	           	 <br>
				  <b><font color=#e89aff>˵��: һ�����ŵ����ݲ��ܳ���70���ַ����������ϵͳ���Զ����Ϊ�������</font></b>
	          </td>

	        </tr> 
	        <tr>
	          <td class="SpaceDotLIne" ></td> 
	        </tr>
      	</table>
   		
   		 <div>
			<%--<hr size="1" noshade />				--%>
			<input name="submit1" type='submit' class="button" value='ȷ�ϱ���' style="cursor:hand">&nbsp;
			<input name="reset" onclick="frm.content.value='';frm.mobiles.value='';" type='button' class="button" style="cursor:hand" value='������д'>&nbsp;
        </div>
        </form>
      </td> 
  </tr> 
</TABLE>
</body>