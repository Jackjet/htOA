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
		
		<%		 	
		 	String menuId = request.getParameter("menuId");
		%>

		<script type="text/javascript"> 		
			//默认选择第一个
			$('#tabs').tabs({selected: 0});			
			
			jQuery().ready(function (){						
				//Load Button
				getButtons('<%=menuId%>');							
			});
			
			//用于检验是否修改
			var personName;
			var mobile;
			var currentPersonName;
			var currentMobile;
						
			//加载表格数据
			var lastsel;			
			var $mygrid = jQuery("#list").jqGrid({ 
                url:'/spict/jqGridServlet',
                datatype: "json",                
               	autowidth: true,
				height: "auto",
				height:300,
                colNames:['编号','工号', '姓名', '身份证', '出生日期','家庭住址','手机号码'],
                colModel:[
                        {name:'personId',index:'personId', width:0, sorttype:"int",search:false,key:true,hidden:true},
                        {name:'personNo',index:'personNo', width:40,align:'center'},
                        {name:'personName',index:'personName', width:50,editable:true,edittype:'text',editoptions:{size:25}},
                        {name:'cardNo',index:'cardNo', width:100,sorttype:"string"},
                        {name:'birthDay',index:'birthDay', width:100,sorttype:"date", searchoptions: { 
                        	dataInit:datePick, attr:{title:'Select Date'}}},                
											//searchoptions:{dataInit:datePick, attr:{title:'Select Date'}}
                        {name:'address',index:'address'},                
                        {name:'mobile',index:'mobile', width:80, sorttype:"date",editable:true,edittype:'select',editoptions: {value: '1:13918288901;2:13901234765'}}                
                ],
                beforeSelectRow: function(rowid) {
                	var data =$('#list').getRowData(rowid); 
                	currentPersonName=data.personName;
                	currentMobile=data.mobile;
                	
                	return true;
                }, 
                /**
                cellEdit: true,                
                afterEditCell: function (id,name,val,iRow,iCol){
                	alert(3434);
                }, */              
                onSelectRow: function(id){
					//alert(id);
					//alert(lastsel);
					
					//var rowId = jQuery("#list").jqGrid('getGridParam','selrow'); 
					//var data =$('#list').getRowData(rowId); 						
					//personName = data.personName;	
					//mobile = data.mobile;
					
					if(id && lastsel && id!==lastsel){						
												
						if(checkRowData(lastsel)) {
							jQuery("#list").jqGrid('saveRow',lastsel, checksave);
						}
						//checkRowData(lastsel);
						
						jQuery('#list').jqGrid('restoreRow',lastsel);	
														
						jQuery('#list').jqGrid('editRow',id,true);
						
						lastsel=id;
						
						personName = currentPersonName;
						mobile = currentMobile;						
					}
				},				
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
        .navGrid('#pager',{edit:false,add:true,del:false})
        .navButtonAdd('#pager',{position:'first',title:'新增数据',caption:'',onClickButton:createNewData});
          
		
		//搜索部分
		$('#list').jqGrid('filterToolbar','');
		
				
		//提交数据前的检验
		function checkRowData(lastsel){
			//根据ID拿到一行的数据 
			//var nowRow=$("#list").getRowData(nowSel);  
			var lastRow=$("#list").getRowData(lastsel);  
			
			//需要修改的内容，用来判断是否修改
			//var nowPersonName = nowRow.personName;
			//var nowMobile = nowRow.mobile;			
			var nowPersonName = lastRow.personName;
			var nowMobile = lastRow.mobile;			
			alert(nowPersonName);
			
			
			
			
			//alert(nowMobile);			
			//alert(personName);
			//alert(mobile);
			
			/**
			alert(lastsel + '_personName');
			var nameObj = document.getElementById(lastsel + '_personName');
			alert(nameObj);
			if(nameObj!=null)				
				alert(nameObj.value);
			*/	
			
			//alert($('#list').html());	
			
			/**
			alert(lastsel + '_personName');
			var nameObj = document.getElementById(lastsel + '_personName');
			alert(nameObj);
			if(nameObj!=null)				
				alert(nameObj.value);
			
			if(document.getElementsByName("personName")!=null)
			alert(document.getElementsByName("personName")[0].value);
			*/
			
			
			/**	
			alert('personName');
			var nameObj = document.getElementById('personName');
			alert(nameObj);
			if(nameObj!=null)				
				alert(nameObj.value);	
			*/
			
			//var val = $('#' + lastsel + '_personName').val(); 
			//alert('val==' + val);			
			//var col = $('#list').jqGrid('getCol', 'personName', false); 
 
 			//alert(lastsel);
			//var bbbb = col[lastsel]; 
			//alert('bbb='+bbbb);
			
			
			//alert($('#'+lastsel + '_personName'));
			//alert($('#'+lastsel + '_personName').value);
			
			
			if(nowPersonName==personName && nowMobile==mobile)
				return false;
				
			return true;		
		}
		
		
		//行编辑保存后的检验
		function checksave(result) {
			if (result.responseText=="") {
				alert("Update is missing!"); 
				return false;
			}
			
			return true;
		}
		
		//$("#list").setSelection("5", true);  
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
		
			
		function createNewData(){
			$("#list").addRowData("1", {}, "first");  
		}
		
		
		//删除数据
		function buttonDelete(){
			//获取选择的行的Id
			var rowId = jQuery("#list").jqGrid('getGridParam','selrow'); 
			//alert(rowId);
			
			
			//alert(rowId);
			if(rowId!=null || rowId!=0){ 			
				$.ajax({
						url: "/spict/personDelete",	//删除数据的url
						cache: false,
						data:{personId: rowId},
						type: "POST",
						dataType: "html",
						beforeSend: function (xhr) {							
						},
						
						complete : function (req, err) {
							//$("#tabs-2").empty().html(req.responseText);
							 alert("数据已经删除！");
							 
							 $("#list").trigger("reloadGrid"); 
		             
		             		 $('#tabs').tabs({selected: 0});
						}
				});	
			}		
			
			//$('#tabs').tabs({selected: 1});
		}
	</script>
		