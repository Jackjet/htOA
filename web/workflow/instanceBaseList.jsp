<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>审核实例</title>
<script src="<c:url value="/"/>datePicker/WdatePicker.js" type="text/javascript"></script>
<script src="<c:url value="/"/>components/jquery-1.4.2.js" type="text/javascript"></script> <!--jquery包-->
<script src="<c:url value="/"/>components/jqgrid/js/jquery-ui-1.8.2.custom.min.js" type="text/javascript"></script><!--jquery ui-->
<script src="<c:url value="/"/>components/jquery.layout.js" type="text/javascript"></script><!--jquery 布局-->
<script src="<c:url value="/"/>components/jqgrid/js/grid.locale-cn.js" type="text/javascript"></script> <!--jqgrid 中文包-->
<script src="<c:url value="/"/>components/jqgrid/js/jquery.jqGrid.min.js" type="text/javascript"></script> <!--jqgrid 包-->
<script src="<c:url value="/"/>js/commonFunction.js"></script>
<script src="<c:url value="/"/>js/inc_javascript.js"></script>
<script src="<c:url value="/"/>js/multisearch.js"></script> <!--加载模态多条件查询相关js-->
<script src="<c:url value="/"/>js/changeclass.js"></script> <!--用于改变页面样式-->
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/tabstyle.css" />
</head>
<script type="text/javascript">
	
	$().ready(function(){
		$('body').layout({
			resizerClass: 'ui-state-default',
	        west__onresize: function (pane, $Pane) {
	            jQuery("#west-grid").jqGrid('setGridWidth',$Pane.innerWidth()-2);
			}
		});
		
		//加载首页
		$.ajax({
			url: "needDealInstance.jsp",
			type: "GET",
			dataType: "html",
			beforeSend: function (xhr) {
			},
			complete : function (req, err) {
				//格式化tab
				loadTabCss();
				
				$("#tabs-1", "#tabs").html(req.responseText);
			}
		});
		
		$.jgrid.defaults = $.extend($.jgrid.defaults,{loadui:"enable"});

		//格式化Tab菜单
		var maintab = jQuery('#tabs','#RightPane').tabs({
	        add: function(e, ui) {
	            //添加关闭按钮
	            $(ui.tab).parents('li:first')
	                .append('<span class="ui-tabs-close ui-icon ui-icon-close" title="关闭"></span>')
	                .find('span.ui-tabs-close')
	                .click(function() {
	                    maintab.tabs('remove', $('li', maintab).index($(this).parents('li:first')[0]));
	                });
	            //选中刚添加的tab
	            maintab.tabs('select', '#' + ui.panel.id);
	            //可拖动tab页
				maintab.find(".ui-tabs-nav").sortable({axis:'x'});
				//格式化tab
				loadTabCss();
	        }
	    });
	    
		//加载菜单树
    	jQuery("#west-grid").jqGrid({
	        url: "/workFlowDefinition.do?method=listTree",
	        datatype: "json",
	        treedatatype: "json",
	        height: "auto",
	        pager: false,	        
	        colNames: ["Id","流程信息"],
	        colModel: [
	            {name: "flowId", width:1, hidden:true, key:true},
	            {name: "flowName", resizable: false, sortable:false}
	        ],
	        treeReader : {
			    level_field: "level",
			    left_field: 1,
			    right_field: 1,
			    leaf_field: "leaf",
			    expanded_field: true
			},
	        treeGrid: true,		//树形grid	     
			caption: "流程信息",
			sortname: 'flowId',
	        ExpandColumn: "flowName",	 	       
	        autowidth: true,  
	        ExpandColClick: true,
	        treeIcons: {leaf:'ui-icon-document-b'},
	        jsonReader:{
               repeatitems: false
            },
	        onSelectRow: function(rowid) {
	           	//加载Tab内容
	            var treedata = $("#west-grid").jqGrid('getRowData',rowid);
	            
	            var st = "#tabs-1"+treedata.flowId;
				if($(st).html() != null ) {
					maintab.tabs('select',st);
				}else {
					maintab.tabs('add',st, treedata.flowName);
					//$(st,"#tabs").load(treedata.url);
						
					$.ajax({
						url: "instanceInfor.jsp?flowId="+treedata.flowId,
						cache: false,
						type: "GET",
						dataType: "html",
						beforeSend: function (xhr) {
							//$(st,"#tabs").height(100).addClass("tabpreloading");
							//$(st,"#tabs").css("text-align","center").html("<img src='images/loading.gif' border=0 />");
						},
						complete : function (req, err) {
							//$(st,"#tabs").removeClass("tabpreloading").append(req.responseText);
							$(st,"#tabs").empty().html(req.responseText);
						}
					});
				}
	        }
	    });
	    
	    //加载回收站页面
	    $('#tabs li:nth-child(2) >a').click(function(){
			if($("#tabs-2").html()=="") {
				loadTab2();
			}
		});
	    
	});
	
	//加载回收站页面
	function loadTab2(){
		$.ajax({
			url: "deletedInstance.jsp",
			cache: false,
			type: "GET",
			dataType: "html",
			beforeSend: function (xhr) {
			},
			complete : function (req, err) {
				$("#tabs-2").empty().html(req.responseText);
			}
		});		
	}
	
	//保存信息后重新加载tab
	function loadTab(flowId){
		$.ajax({
			url: "instanceInfor.jsp?flowId="+flowId,
			cache: false,
			type: "GET",
			dataType: "html",
			beforeSend: function (xhr) {
			},
			complete : function (req, err) {
				$("#tabs-1"+flowId).empty().html(req.responseText);
			}
		});		
	}
	
</script>

<body>
<!-- #LeftPane -->
<div class="ui-layout-west ui-widget ui-widget-content" id="LeftPane">
	<table id="west-grid"></table>
</div>
<!-- #RightPane -->
<div class="ui-layout-center ui-helper-reset ui-widget-content" id="RightPane">
	<!-- Tabs pane -->
	<div id="tabs" class="jqgtabs">
		<ul>
			<li><a href="#tabs-1">所有信息</a></li>
			<li><a href="#tabs-2">回收站</a></li>
		</ul>
		<div id="tabs-1"></div>
		<div id="tabs-2" ></div>
	</div>
</div>
</body>

</html>

