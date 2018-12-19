<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>咨询信息</title>
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
			url: "inforRightList.jsp?urlPath=${_Category.urlPath}&isRoot=true&categoryId="+${_Category.categoryId},
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
	        url: "/cms/${_Category.urlPath}.do?method=getCategoryTree&categoryId="+${_Category.categoryId},
	        datatype: "json",
	        treedatatype: "json",
	        height: "auto",
	        pager: false,	        
	        colNames: ["Id","分类列表","url"],
	        colModel: [
	            {name: "categoryId", width:1, hidden:true, key:true},
	            {name: "categoryName", resizable: false, sortable:false},
	            {name: "urlPath", width:1, hidden:true}
	        ],
	        treeReader : {
			    level_field: "layer",
			    left_field: 'leftIndex',
				right_field: 'rightIndex',
			    leaf_field: "leaf",
			    expanded_field: true
			},
	        treeGrid: true,		//树形grid	     
			caption: "资讯分类",
	        ExpandColumn: "categoryName",	 	       
	        autowidth: true,  
	        //ExpandColClick: true,
	        treeIcons: {leaf:'ui-icon-document-b'},
	        jsonReader:{
               repeatitems: false
            },
            loadComplete:function(xhr){
	            //菜单树默认展开
	            var record = jQuery("#west-grid").getRowData(8);
				jQuery("#west-grid").expandRow(record);
	        },
	        onSelectRow: function(rowid) {
	           	//加载Tab内容
	            var treedata = $("#west-grid").jqGrid('getRowData',rowid);
	            
	            //处理树状菜单点击时的展开和收拢
	            resetNode(rowid);
	            
	            if(treedata.leaf=="true") {
	                //treedata.url
	                var st = "#tabs-"+treedata.categoryId;
					if($(st).html() != null ) {
						maintab.tabs('select',st);
					}else {
						maintab.tabs('add',st, treedata.categoryName);
						//$(st,"#tabs").load(treedata.url);
						
						$.ajax({
							url: "inforRightList.jsp?urlPath=${_Category.urlPath}&categoryId="+treedata.categoryId,
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
	        }
	    });
	    
	});
	
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
		</ul>
		<div id="tabs-1"></div>
	</div>
</div>
</body>

</html>

