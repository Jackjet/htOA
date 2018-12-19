<%@ page contentType="text/html; charset=utf-8"%>
<link rel="stylesheet" type="text/css" href="components/easyui/easyui.css">
<link rel="stylesheet" type="text/css" href="components/easyui/icon.css">

<script type="text/javascript" src="components/easyui/jquery.easyui.min.js"></script>

<div style="height:30px" id="buttonDiv">
				
</div>
			
<div id="tabs" class="easyui-tabs" fit="true" border="false">
		<div title="列表显示" style="padding:0px;overflow:hidden;"> 
			<br>
			<h3>This place display the List infor.</h3>
		</div>
		
		<div title="其它操作" closable="false" style="padding:20px;">
			<br>		
				<h3>jQuery EasyUI framework help you build your web page easily.</h3>
				<li>easyui is a collection of user-interface plugin based on jQuery.</li> 
				<li>using easyui you don't write many javascript code, instead you defines user-interface by writing some HTML markup.</li> 
				<li>easyui is very easy but powerful.</li> 
		</div>
		
		
</div>


<%
 //int menuId;
 String menuId = request.getParameter("menuId");
%>

<script type="text/javascript"> 
	jQuery().ready(function (){
		//Load Button
		$.ajax({
			type: "get",				
			url: "buttonServlet",	
			
			//async: false,
			data:{menuId: <%=menuId%>},
			beforeSend: function(XMLHttpRequest){				
				$("#buttonDiv").html('');
			},					
									
			success: function(data, textStatus){	
				var dataObj=eval("("+data+")");					
									
				$.each(dataObj.rows,function(i,button){
					var buttonHtml;
					buttonHtml = '<img src="' + button.iconPath + '" onclick="operButton(\'' + button.url + '\',\'' + button.target + '\')" alt="' + button.buttonName + '"/> ';
							
					//alert(buttonHtml);
					$(buttonHtml).appendTo($("#buttonDiv"));		
				});														
			}
		});
		
		
	});
</script>