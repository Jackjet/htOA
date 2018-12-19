	<%@ page contentType="text/html; charset=utf-8"%>
	<%@ include file="/inc/taglibs.jsp"%>
	
		
	<link type="text/css" href="/spict/components/jqgrid/css/jquery.ui.all.css" rel="stylesheet" /> 
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" /> 
	
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery.ui.datepicker.css" /> 
	
	<script src="<c:url value="/"/>components/jquery/jquery-1.4.2.js" type="text/javascript"></script> 
	<script src="<c:url value="/"/>components/jqgrid/js/grid.locale-cn.js" type="text/javascript"></script> 
	<script src="<c:url value="/"/>components/jqgrid/js/jquery.jqGrid.min.js" type="text/javascript"></script> 
	
	<script type="text/javascript" src="/spict/components/jqgrid/js/jquery.ui.widget.js"></script>
	<script type="text/javascript" src="/spict/components/jqgrid/js/jquery.ui.tabs.js"></script>
	
	<script type="text/javascript" src="<c:url value="/"/>components/jqgrid/js/jquery.ui.datepicker.js"></script>
	
	<script type="text/javascript" src="<c:url value="/"/>components/jquery/jquery.messager.js"></script>
	<script type="text/javascript" src="<c:url value="/"/>js/commonFunction.js"></script>

		<div id="mainnav"></div>	 	
	 	
		 <div id="main_top">		 	
		 	<ul>
		  		<li id="left">
		  			<span><img src="<c:url value="/"/>images/work-wrench.jpg" width="30" height="30" />工单：33697</span>
		  			报修内容：启不动1 
		  		</li>
		  		<div id="testDiv" style="float:right">
		  		<input type="button" value="我也来一个" onclick="showSystemMessage('This is a test','Content Test',0,0);"/>
		  		<strong>状态：</strong>工单下达<strong> 车辆编号</strong>：B009</li>
		  		</div>
		 	 </ul>
		</div>
		
  		<div id="middle">
			<div id="tabs">
				<ul>
					<li><a href="#tabs-1">工单信息</a></li>
					<li><a href="#tabs-2" onclick="viewData()">浏览视图</a></li>					
				</ul>
				<div id="tabs-1" style="margin-top:0px;margin-left:0px;padding-left:0px;padding-top:0px;padding-right:0px">
					<div style="margin-top:0px;">
							<table id="list"></table>
							<div id="pager"></div> 
					</div>
				</div>
				<div id="tabs-2">
					<p>Morbi tincidunt, dui sit amet facilisis feugiat, odio metus gravida ante, ut pharetra massa metus id nunc. Duis scelerisque molestie turpis. Sed fringilla, massa eget luctus malesuada, metus eros molestie lectus, ut tempus eros massa ut dolor. Aenean aliquet fringilla sem. Suspendisse sed ligula in ligula suscipit aliquam. Praesent in eros vestibulum mi adipiscing adipiscing. Morbi facilisis. Curabitur ornare consequat nunc. Aenean vel metus. Ut posuere viverra nulla. Aliquam erat volutpat. Pellentesque convallis. Maecenas feugiat, tellus pellentesque pretium posuere, felis lorem euismod felis, eu ornare leo nisi vel felis. Mauris consectetur tortor et purus.</p>
					<table id="list"></table>
							<div id="pager"></div> 
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
			
			
			//加载表格数据
			
			var $mygrid = jQuery("#list").jqGrid({ 
                url:'/spict/jqGridServlet',
                datatype: "json",                
               	autowidth: true,
				//height: "auto",
				height:300,
                colNames:['编号','工号', '姓名', '身份证', '出生日期','家庭住址','手机号码'],
                colModel:[
                        {name:'personId',index:'personId', width:0, sorttype:"int",search:false,key:true,hidden:true},
                        {name:'personNo',index:'personNo', width:40,align:'center'},
                        {name:'personName',index:'personName', width:50},
                        {name:'cardNo',index:'cardNo', width:100,sorttype:"string"},
                        {name:'birthDay',index:'birthDay', width:100,sorttype:"date", searchoptions: { 
                        	dataInit:datePick, attr:{title:'Select Date'}}},                
											//searchoptions:{dataInit:datePick, attr:{title:'Select Date'}}
                        {name:'address',index:'address'},                
                        {name:'mobile',index:'mobile', width:80, sorttype:"date"}                
                ],
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
                subGrid: true,
                subGridRowExpanded: function(subgrid_id, row_id) {
					$("#"+subgrid_id).html('<div style="padding:5px;"><button class="ui-state-default ui-corner-all" type="button">下 载</button> <button class="ui-state-default ui-corner-all" type="button">评 论</button> <button class="ui-state-default ui-corner-all" type="button">详 细</button></div>');
				},   
                          
                pager: "#pager"
               
        }).navGrid('#pager',{edit:false,add:false,del:false});
		
		$('#list').jqGrid('filterToolbar','');
		
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
		
		
		
		//jQuery("#list").setGridParam({rowNum:150}).trigger("reloadGrid");
		
		//浏览数据
		function viewData(){			
			//获取选择的行的Id
			var rowId = jQuery("#list").jqGrid('getGridParam','selrow'); 
					
			if(rowId==null){				
				$("#list").setSelection("2", true); 
				rowId=2;
			}
			
			//alert(rowId);
			if(rowId!=null || rowId!=0){ 
				//这里进行想要的操作,如这里显示人员信息
				$.ajax({
					url: "/spict/personServlet",
					data:{personId: rowId},
					cache: false,
					type: "GET",
					dataType: "html",
					beforeSend: function (xhr) {
						//Erase center part content
						$("#tabs-2").html('');
					},
					
					complete : function (req, err) {
						$("#tabs-2").empty().html(req.responseText);
					}
				});				
				
				$('#tabs').tabs({selected: 1});
			} else {							
				var msg = "请先选择要浏览的人员信息！(<span style='color:red;'>单击人员所在行即可选择</span>)";
				var title = "<font color='red'>提示信息</font>";
				
				showMsgModal(title,msg);			
			}
		}
		
		//新增数据
		function buttonInsert(){
			$.ajax({
					url: "<c:url value="/"/>example/addPerson.jsp",					
					cache: false,
					type: "GET",
					dataType: "html",
					beforeSend: function (xhr) {
						//Erase center part content
						$("#tabs-2").html('');
					},
					
					complete : function (req, err) {
						$("#tabs-2").empty().html(req.responseText);
					}
			});				
			
			$('#tabs').tabs({selected: 1});
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
		