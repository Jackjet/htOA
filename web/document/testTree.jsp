<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>


<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
		<title>文档大全</title>
		<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.8.1.custom.css" />
		<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
		<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/tabstyle.css" />
		<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.multiselect.css" />
		
		<style> 
html, body {
	margin: 0;			/* Remove body margin/padding */
	padding: 0;
	overflow: hidden;	/* Remove scroll bars on browser window */	
    font-size: 75%;
}
/*Splitter style */
 
 
#LeftPane {
	/* optional, initial splitbar position */
	overflow: auto;
}
/*
 
 Right-side element of the splitter.
*/
 
#RightPane {
	padding: 2px;
	overflow: auto;
}
.ui-tabs-nav li {position: relative;}
.ui-tabs-selected a span {padding-right: 10px;}
.ui-tabs-close {display: none;position: absolute;top: 3px;right: 0px;z-index: 800;width: 16px;height: 14px;font-size: 10px; font-style: normal;cursor: pointer;}
.ui-tabs-selected .ui-tabs-close {display: block;}
.ui-layout-west .ui-jqgrid tr.jqgrow td { border-bottom: 0px none;}
.ui-datepicker {z-index:1200;}
.rotate
    {
        /* for Safari */
        -webkit-transform: rotate(-90deg);
 
        /* for Firefox */
        -moz-transform: rotate(-90deg);
 
        /* for Internet Explorer */
        filter: progid:DXImageTransform.Microsoft.BasicImage(rotation=3);
    }
 
</style>
		
		
		<script src="<c:url value="/"/>components/jquery.js" type="text/javascript"></script> <!--jquery包-->
		<script src="<c:url value="/"/>components/jqgrid/js/jquery-ui-1.8.1.custom.min.js" type="text/javascript"></script><!--jquery ui-->
		<script src="<c:url value="/"/>components/jquery.layout.js" type="text/javascript"></script><!--jquery 布局-->
		<script src="<c:url value="/"/>components/jqgrid/js/grid.locale-cn.js" type="text/javascript"></script> <!--jqgrid 中文包-->
		
		<script type="text/javascript"> 
			$.jgrid.no_legacy_api = true;
			$.jgrid.useJSON = true;
		</script>
		<script src="<c:url value="/"/>components/jqgrid/js/ui.multiselect.js" type="text/javascript"></script>
		
		<script src="<c:url value="/"/>components/jqgrid/js/jquery.jqGrid.min.js" type="text/javascript"></script> <!--jqgrid 包-->
		<script src="<c:url value="/"/>components/jqgrid/js/jquery.tablednd.js" type="text/javascript"></script>
		<script src="<c:url value="/"/>components/jqgrid/js/jquery.contextmenu.js" type="text/javascript"></script>
		
		
		
		

		
		
		
		
		<script type="text/javascript">
			$().ready(function(){
				$('body').layout({
					resizerClass: 'ui-state-default',
			        west__onresize: function (pane, $Pane) {
			            jQuery("#west-grid").jqGrid('setGridWidth',$Pane.innerWidth()-2);
					}
				});
				
				$.jgrid.defaults = $.extend($.jgrid.defaults,{loadui:"enable"});
				
				    
				//加载菜单树
		    	jQuery("#west-grid").jqGrid({
			        url:"/document/document.do?method=list", 			        
			        datatype: "json",
			        //async: true,
			        //treedatatype: "json",
			        height: "auto",
			        pager: false,
			        loadui: "disable",
			        colNames: ["Id","分类列表","url","leaf"],
			        colModel: [
			            {name: "categoryId",index:"categoryId", sorttype:"int", width:1, hidden:true, key:true},
			            {name: "categoryName",index:"categoryName", resizable: false, sortable:false},
			            {name: "urlPath", index:"urlPath", width:1, hidden:true},
			            {name: "leaf",index:"leaf", width:1, hidden:true}
			        ],
			        treeReader : {
					    level_field: 'layer',
					    left_field: 'leftIndex',
					    right_field: 'rightIndex',
					    leaf_field: "leaf",
					    expanded_field: true
					},
					treeGridModel: 'nested',					
			        //sortname: 'categoryName',//默认排序的字段
			        treeGrid: true,		//树形grid
					caption: "文档分类",
			        ExpandColumn: "categoryName",
			        autowidth: true,
			        rowNum: 200,
			        ExpandColClick: true, 
			        treeIcons: {leaf:'ui-icon-document-b'},
			        jsonReader:{
		              repeatitems: false	//告诉JqGrid,返回的数据的标签是否是可重复的
		            },
	                
	               
                	
                	loadComplete:function(xhr){
                		//var record = $("#west-grid").getRowData(1);
					    //alert(record);
					    //var depth = jQuery("#west-grid").getNodeChildren(record);
					    //alert(depth);
					    
					   // $("#west-grid").expandRow(record);
                	}
			    });
			    
			   
				
				$.jgrid.defaults = $.extend($.jgrid.defaults,{loadui:"enable"});
		
				
			    
		    });//ready结束
		    
		   
	    	//新增分类
	    	function doAdd(){
				var refresh = window.showModalDialog("/document/documentCategory.do?method=edit",null,"dialogWidth:800px;dialogHeight:600px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
				if(refresh == "Y") {
					self.location.reload();
				}
			}
			
		</script>
	</head>
	
	<body>
	<!-- #LeftPane -->
		
		<div class="ui-layout-west ui-widget ui-widget-content" id="LeftPane">
			<!-- <a href="#" onclick="expandNode();">TEST</a> -->
			<table id="west-grid"></table>
		</div>
		
	</body>
</html>

