	<%@ page contentType="text/html; charset=utf-8"%>
	<%@ include file="/inc/taglibs.jsp"%>
	
		
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui-lightness/jquery-ui-1.7.2.custom.css" /> 
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" /> 
	
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery.ui.datepicker.css" /> 
	
	<script src="<c:url value="/"/>components/jquery/jquery-1.4.2.js" type="text/javascript"></script> 
	<script src="<c:url value="/"/>components/jqgrid/js/grid.locale-cn.js" type="text/javascript"></script> 
	<script src="<c:url value="/"/>components/jqgrid/js/jquery.jqGrid.min.js" type="text/javascript"></script> 
	
	<script type="text/javascript" src="<c:url value="/"/>components/jqgrid/js/jquery-ui-1.8.2.custom.min.js"></script> 
	<script type="text/javascript" src="<c:url value="/"/>components/jqgrid/js/jquery.ui.datepicker.js"></script>

		<div id="mainnav"></div>	 	
	 	
		 <div id="main_top">		 	
		 	<ul>
		  		<li id="left">
		  			<span><img src="<c:url value="/"/>images/work-wrench.jpg" width="30" height="30" />直接修改提交的例子</span>
		  			
		  		</li>
		  		<div id="testDiv" style="float:right"><strong>状态：</strong>工单下达<strong> 车辆编号</strong>：B009</li></div>
		 	 </ul>
		</div>
		
  		<div id="middle">
			<div id="tabs">
				<ul>
					<li><a href="#tabs-1">工单信息</a></li>								
				</ul>
				<div id="tabs-1" style="margin-top:0px;margin-left:0px;padding-left:0px;padding-top:0px;padding-right:0px">
					<div style="margin-top:0px;">
							<table id="list"></table>
							<div id="pager"></div> 
					</div>
				</div>				
			</div>			
		</div>
		
		
		<br />  
		<script>
			var onecount;
			onecount = 0;
			subcat = new Array();		
			subcat[0] = new Array("001","部门1");
			subcat[1] = new Array("002","部门2");
	        onecount = 2;
	        
	        function changeIt(frm){	   
				theNo = frm.theNo.options[frm.theNo.selectedIndex].value;
				// alert(theNo);
				    
				for (var i=0;i < onecount; i++) {       
				  //alert(subcat[i][0]);
				  if(subcat[i][0] == theNo){
				     frm.personNo.value = subcat[i][1];
				  }       
				}	    
			}
	       
	 </script> 
		
         
       <div>  
            <button onclick="openAdd()">添加联系人</button>  
            <button onclick="openUpdate()">修改联系人</button>  
            <button onclick="openDelete()">删除联系人</button>  
       </div>  
          
        <div id="modalDlg">  
            <div id="formContainer">  
                <form id="personForm">  
                   <input type="hidden" id="personId"/>  
                   
                   <table>  
                       <tr>
							<td width="10%" height="22" nowrap>人员姓名</td>
							<td><input name="personName" type="text" size="20"></td>
						</tr>
						
						<tr>
							<td height="22">员工编号</td>
							<td><input name="personNo" type="text" size="20"></td>
						</tr>
						
						<tr>
							<td height="22">身份证号</td>
							<td><input name="cardNo" type="text" size="30"></td>
						</tr>
						
						<tr>
							<td height="22">员工生日</td>
							<td><input name="birthday" type="text" size="20" onclick="datePick(this);"></td>
						</tr>
						
						<tr>
							<td height="22">家庭地址</td>
							<td>
								<input name="address" type="text" size="50">								
							</td>
						</tr>
						
						<tr>
							<td height="22">手机号码</td>
							<td><input name="mobile" type="text" size="20"></td>
						</tr>  
						
						<tr>
							<td height="22">测试</td>
							<td>
								<select name="theNo" onchange="changeIt(this.form)">
									<option value="001">001</option>
									<option value="002">002</option>
								</select>
							</td>
						<tr>
                
                  </table>  
                </form>  
            </div>  
        </div>  
		
		
		<%		 	
		 	String menuId = request.getParameter("menuId");
		%>

		<script type="text/javascript"> 		
			//默认选择第一个
			$('#tabs').tabs({selected: 0});			
			
			var addContact = function() {   
			   var consoleDlg = $("#modalDlg");   
			    
			   /**        
			   var personName = $.trim(consoleDlg.find("#personName").val());   
			   var personNo = $.trim(consoleDlg.find("#personNo").val());   
			   var cardNo = $.trim(consoleDlg.find("#cardNo").val());   
			   var birthday = $.trim(consoleDlg.find("#birthday").val());   
			   var address = $.trim(consoleDlg.find("#address").val());   
			   var mobile = $.trim(consoleDlg.find("#mobile").val());   
			  			      
			   var params = {   
			        "personName" : personName,   
			        "personNo" : personNo,   
			        "cardNo" : cardNo,   
			        "birthday" : birthday,   
			        "address" : address,   
			        "mobile" : mobile
			    };   			       
			   */
			      
			   $.ajax( {   
			        url: '<c:url value="/"/>personInsert',	                    
	             	data: $('#personForm').serialize(), // 从表单中获取数据	                    
	            	type:'POST', 
	               	async: false,
			       	error : function(textStatus, errorThrown) {   
			           alert("系统ajax交互错误: " + textStatus);   
			       	},
			       	success : function(data, textStatus) {  
						  alert("人员信息添加操作成功!"); 				              
						  consoleDlg.dialog("close");
						  
			              $("#list").trigger("reloadGrid"); 
			              
			              /**
			              var dataRow = {   
			                   id : data.personId,   
			                   personName : personName,   
			                   personNo : personNo,   
			                   cardNo : cardNo,   
			                   birthday : birthday,
			                   address : address,
			                   mobile : mobile   
			               };   
			                   
			               var srcrowid = $("#list").jqGrid("getGridParam", "selrow");   
			                  
			               if(srcrowid) {   
			                 $("#list").jqGrid("addRowData", data.personId, dataRow, "before", srcrowid);   
			               } else {   
			                  $("#list").jqGrid("addRowData", data.personId, dataRow, "first");   
			               }   
			              */ 
			                   
			        }   			         
			   });   
			};  

			var updateContact = function() { 
				var consoleDlg = $("#consoleDlg");   

 				$.ajax( {   
			        url: '<c:url value="/"/>personSave',	                    
	             	data: $('#personForm').serialize(), // 从表单中获取数据	                    
	            	type:'POST', 
	               	async: false,
			       	error : function(textStatus, errorThrown) {   
			           alert("系统ajax交互错误: " + textStatus);   
			       	},
					success : function(data, textStatus) {   
						var dataRow = {   
			                   id : data.personId,   
			                   personName : personName,   
			                   personNo : personNo,   
			                   cardNo : cardNo,   
			                   birthday : birthday,
			                   address : address,
			                   mobile : mobile   
			               };   
			                   
			              
              			$("#list").jqGrid("setRowData", person.personId, dataRow);     
                		alert("联系人信息更新成功!");   
                  
                		consoleDlg.dialog("close");   
       				}   
    			}); 
			}
			
			
			
			jQuery().ready(function (){						
				//Load Button
				getButtons('<%=menuId%>');		
				
				
				// 配置对话框   
			   $("#modalDlg").dialog({   
			        autoOpen: false,       
			        modal: true,    // 设置对话框为模态（modal）对话框   
			        resizable: true,       
			        width: 480,   
			        buttons: {  // 为对话框添加按钮   
			            "创建": addContact,
			            "取消": function() {$("#modalDlg").dialog("close")},
			            "保存": updateContact			           
			        }   
			    });  				
			});
					
					
			var openAdd = function() {   
				var consoleDlg = $("#modalDlg");   
				var dialogButtonPanel = consoleDlg.siblings(".ui-dialog-buttonpane");  
				//alert(dialogButtonPanel); 
				consoleDlg.find("input").removeAttr("disabled").val("");   
				    
				dialogButtonPanel.find("button:not(:contains('取消'))").hide();   
				dialogButtonPanel.find("button:contains('创建')").show();   
				consoleDlg.dialog("option", "title", "创建新联系人").dialog("open");   
			};   
			
			
			var openUpdate = function() {   
				var consoleDlg = $("#modalDlg");   
			    var dialogButtonPanel = consoleDlg.siblings(".ui-dialog-buttonpane");   
			       
			    consoleDlg.find("input").removeAttr("disabled");   
			    dialogButtonPanel.find("button:not(:contains('取消'))").hide();   
			    dialogButtonPanel.find("button:contains('保存')").show();   
			    
			    consoleDlg.dialog("option", "title", "修改人员信息");   
			       
			    loadPersonInfor();  
			};   
			
			
			var loadSelectedRowData = function() {     
				  var selectedRowId = $("#gridTable").jqGrid("getGridParam", "selrow");   
				  if (!selectedRowId) {   
				       alert("请先选择需要编辑的行!");   
				       return false;   
				  } else {   
				        var params = {   
				           "personId" : selectedRowId   
				       }; 
				      
				       $.ajax( {   
				           url: '<c:url value="/"/>personGet',
				           data : params,   
				           dataType : "json",   
				           cache : false,   
				           error : function(textStatus, errorThrown) {   
				               alert("系统ajax交互错误: " + textStatus);   
				           },   
				            
				            success : function(data, textStatus) {   
				                // 如果读取结果成功，则将信息载入到对话框中                    
				               var rowData = data.person;   
				               var consoleDlg = $("#modalDlg");   
				               
				               consoleDlg.find("#personId").val(rowData.personId);   
				
				               consoleDlg.find("#personName").val(rowData.personName);   
				               consoleDlg.find("#personNo").val(rowData.personNo);   
				               consoleDlg.find("#cardNo").val(rowData.cardNo);   
				               consoleDlg.find("#birthday").val(rowData.birthday);   
				               consoleDlg.find("#address").val(rowData.address);   
				               consoleDlg.find("#mobile").val(rowData.mobile);   
				             
				                  
				                // 根据新载入的数据将表格中的对应数据行一并更新一下   
								/**				              
				               var dataRow = {   
				                   id : data.personId,   
				                   personName : personName,   
				                   personNo : personNo,   
				                   cardNo : cardNo,   
				                   birthday : birthday,
				                   address : address,
				                   mobile : mobile   
				               };  			               
				               $("#gridTable").jqGrid("setRowData", data.personId, dataRow);   
				               */
				                       
				               // 打开对话框   
				                consoleDlg.dialog("open");   
				           }   
				       });   				          
				  	}   
				};   

			//加载表格数据
			var lastsel;			
			var $mygrid = jQuery("#list").jqGrid({ 
                url:'/spict/jqGridServlet',
                datatype: "json",   
                mtype: 'POST',                             
               	autowidth: true,
				height: "auto",
				height:300,
                colNames:['编号','工号', '姓名', '身份证', '出生日期','家庭住址','手机号码'],
                colModel:[
                        {name:'personId',index:'personId', width:0, sorttype:"int",search:false,key:true},
                        {name:'personNo',index:'personNo', width:40,align:'center'},
                        {name:'personName',index:'personName', width:50,editable:true,edittype:'text',editoptions:{size:25}},
                        {name:'cardNo',index:'cardNo', width:100,sorttype:"string"},
                        {name:'birthDay',index:'birthDay', width:100,sorttype:"date", searchoptions: { 
                        	dataInit:datePick, attr:{title:'Select Date'}}},                
											//searchoptions:{dataInit:datePick, attr:{title:'Select Date'}}
                        {name:'address',index:'address'},                
                        {name:'mobile',index:'mobile', width:80, sorttype:"date",editable:true,edittype:'select',editoptions: {value: '1:13918288901;2:13901234765'}}                
                ],
               	ondblClickRow: function(rowId,iRow,iCol,e){
               		//alert(rowId);
               		$(this).jqGrid('editGridRow',rowId,{checkOnSubmit:false,checkOnUpdate:false,closeAfterEdit:true,closeOnEscape:true});
               		$(this).jqGrid('setSelection',rowId);
               		
               	},   
               	 /**
               	 edit : {   
				     addCaption: "Add Record",   
				     editCaption: "Edit Record",   
				     bSubmit: "Submit",   
				     bCancel: "Cancel",   
				     bClose: "Close",   
				     saveData: "Data has been changed! Save changes?",   
				     bYes : "Yes",   
				     bNo : "No",   
				     bExit : "Cancel",   
				  },*/      		
				editurl: '<c:url value="/"/>personSave',	
                sortname: 'personId',
                sortorder: 'asc',
                viewrecords: true,
                rowNum: 20,
                rowList: [10,20,30],
                scroll: true, 
                scrollrows: true,                          
                jsonReader:{
                        repeatitems: false
                },            
                pager: "#pager"
               
               
        })
        .navGrid('#pager',{edit:false,add:true,del:false});
        //.navButtonAdd('#pager',{position:'first',title:'新增数据',caption:'',onClickButton:createNewData});
          		
		//搜索部分
		$('#list').jqGrid('filterToolbar','');
			
		function createNewData(){
			jQuery("#list").jqGrid('editGridRow','new',{height:280,closeAfterSave:true,reloadAfterSubmit:true,closeOnEscape:true});
		}	
		
		jQuery("#list").click(function(){
			//获取选择的行的Id
			var rowId = jQuery("#list").jqGrid('getGridParam','selrow'); 
			
			if(rowId!=null || rowId!=0){ 
				var data =$('#list').getRowData(rowId); 
				//alert(data.personName);
				$('#testDiv').html("<b>姓名:</b>" + data.personName + " <b>住址:</b>" + data.address);
			}		
			
			//如果需要更复杂的操作，可以使用JQuery的Ajax事件，参考其他方法			
		});	
		
		
	</script>
		