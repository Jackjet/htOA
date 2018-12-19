<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
		<title>个人通讯录</title>
		<script src="<c:url value="/"/>js/changeclass.js"></script> <!--用于改变页面样式-->
		<script src="<c:url value="/"/>datePicker/WdatePicker.js" type="text/javascript"></script>
		<script src="<c:url value="/"/>components/jquery-1.4.2.js" type="text/javascript"></script> <!--jquery包-->
		<script src="<c:url value="/"/>components/jqgrid/js/jquery-ui-1.8.2.custom.min.js" type="text/javascript"></script><!--jquery ui-->
		<script src="<c:url value="/"/>components/jquery.layout.js" type="text/javascript"></script><!--jquery 布局-->
		<script src="<c:url value="/"/>components/jqgrid/js/grid.locale-cn.js" type="text/javascript"></script> <!--jqgrid 中文包-->
		<script src="<c:url value="/"/>components/jqgrid/js/jquery.jqGrid.min.js" type="text/javascript"></script> <!--jqgrid 包-->
		<script src="<c:url value="/"/>js/changeclass.js"></script>
		<script src="<c:url value="/"/>js/commonFunction.js"></script>
		<script src="<c:url value="/"/>js/inc_javascript.js"></script>
		<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />
		<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
		<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/tabstyle.css" />
		
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
					url: "listPersonalAddress.jsp",
					type: "GET",
					dataType: "html",
					beforeSend: function (xhr) {
					},
					complete : function (req, err) {
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
						loadTabCss();
			        }
			    });
			    
			    //加载Tab内容
				$('#west-grid tr').click(function(){
					var st = "#t"+$(this).find('#tabId').text();
					if($(st).html() != null ) {
						//若该tab的内容已存在则不再重新加载,将tab状态改为选中即可
						maintab.tabs('select',st);
					} else {
						//若tab的内容不存在,则加载
						maintab.tabs('add',st,$(this).find('#tabMenu').text());
						$.ajax({
							url: $(this).find('#tabUrl').text(),
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
				});
				
				function formatLeaf(cellvalue, options, rowdata) {
		           return "true";
			    }
			    function formatLayer(cellvalue, options, rowdata) {
		           return "2";
			    }
				    
				//加载菜单树
		    	jQuery("#west-grid").jqGrid({
			        url: "/personal/address/AddressCategory.do?method=getCategoryTree",
			        datatype: "json",
			        treedatatype: "json",
			        height: "auto",
			        pager: false,
			        loadui: "disable",
			        colNames: ["Id","分类名称"],
			        colModel: [
			            {name: "categoryId", width:1, hidden:true, key:true},
			            {name: "categoryName",index:"categoryName", resizable: false, sortable:false}
			        ],
			        treeReader : {
					    level_field: "level",
					    left_field: 1,
					    right_field: 1,
					    leaf_field: "leaf",
					    expanded_field: true
					}, 
			        sortname: 'categoryName',//默认排序的字段
			        treeGrid: true,		//树形grid
					caption: "个人通讯录分类",
			        ExpandColumn: "categoryName",
			        autowidth: true,
			        rowNum: 200,
			        //ExpandColClick: true,
			        treeIcons: {leaf:'ui-icon-document-b'},
			        jsonReader:{
		               repeatitems: false	//告诉JqGrid,返回的数据的标签是否是可重复的
		            },
			        onSelectRow: function(rowid) {
			        
			            var treedata = $("#west-grid").jqGrid('getRowData',rowid);
		                var st = "#tabs-1"+treedata.categoryId;
						if($(st).html() != null ) {
							maintab.tabs('select',st);
						}else {
							maintab.tabs('add',st, treedata.categoryName);
							$.ajax({
								url: "categoryRightList.jsp?categoryId="+treedata.categoryId,
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
			
			//保存信息后重新加载tab
			function loadTab(categoryId){
				$.ajax({
					url: "categoryRightList.jsp?categoryId="+categoryId,
					cache: false,
					type: "GET",
					dataType: "html",
					beforeSend: function (xhr) {
					},
					complete : function (req, err) {
						$("#tabs-1"+categoryId).empty().html(req.responseText);
					}
				});		
			}
				
		</script>
	</head>
	
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
					<li><a href="#tabs-1">全部</a></li>
				</ul>
				<div id="tabs-1"></div>
			</div>
		</div>
	</body>
</html>

