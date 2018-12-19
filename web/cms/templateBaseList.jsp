<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>模板信息</title>
<script src="<c:url value="/"/>components/jquery-1.4.2.js" type="text/javascript"></script> <!--jquery包-->
<script src="<c:url value="/"/>components/jqgrid/js/jquery-ui-1.8.2.custom.min.js" type="text/javascript"></script><!--jquery ui-->
<script src="<c:url value="/"/>components/jquery.layout.js" type="text/javascript"></script><!--jquery 布局-->
<script src="<c:url value="/"/>components/jqgrid/js/grid.locale-cn.js" type="text/javascript"></script> <!--jqgrid 中文包-->
<script src="<c:url value="/"/>components/jqgrid/js/jquery.jqGrid.min.js" type="text/javascript"></script> <!--jqgrid 包-->
<script src="<c:url value="/"/>js/commonFunction.js"></script>
<script src="<c:url value="/"/>js/inc_javascript.js"></script>
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
			url: "templateRightList.jsp?templateStyle=${param.templateStyle}",
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
	        url: "/cms/inforTemplate.do?method=listTree&templateStyle=${param.templateStyle}",
	        datatype: "json",
	        treedatatype: "json",
	        height: "auto",
	        pager: false,	        
	        colNames: ["Id","文件夹列表","文件路径"],
	        colModel: [
	            {name: "id", width:1, hidden:true, key:true},
	            {name: "fileName", resizable: false, sortable:false},
	            {name: "path", width:1, hidden:true}
	        ],
	        treeReader : {
			    level_field: "level",
			    left_field: 1,
			    right_field: 1,
			    leaf_field: "leaf",
			    expanded_field: true
			},
	        treeGrid: true,		//树形grid	     
			caption: "文件夹列表",
	        ExpandColumn: "fileName",	 	       
	        autowidth: true,  
	        //ExpandColClick: true,
	        treeIcons: {leaf:'ui-icon-document-b'},
	        jsonReader:{
               repeatitems: false
            },
	        onSelectRow: function(rowid) {
	           	//加载Tab内容
	            var treedata = $("#west-grid").jqGrid('getRowData',rowid);
	            var url = "";
	            if(treedata.leaf=="true") {
	                url = "/cms/inforTemplate.do?method=edit&templateStyle=${param.templateStyle}&path="+treedata.path;
	            }else {
	            	url = "templateRightList.jsp?templateStyle=${param.templateStyle}&fileId="+treedata.id+"&path="+treedata.path;
	            }
	            var st = "#tabs-1"+treedata.id;
				if($(st).html() != null ) {
					maintab.tabs('select',st);
				}else {
					maintab.tabs('add',st, treedata.fileName);
					
					$.ajax({
						url: url,
						cache: false,
						type: "GET",
						dataType: "html",
						beforeSend: function (xhr) {
						},
						complete : function (req, err) {
							$(st,"#tabs").empty().html(req.responseText);
						}
					});
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

