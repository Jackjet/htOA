<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<title>日程安排</title>
	<script src="<c:url value="/"/>datePicker/WdatePicker.js" language="JavaScript"></script>
	<script src="<c:url value="/"/>components/jquery-1.4.2.js" type="text/javascript"></script> <!--jquery包-->
	<script src="<c:url value="/"/>components/jqgrid/js/jquery-ui-1.8.2.custom.min.js" type="text/javascript"></script><!--jquery ui-->
	<script src="<c:url value="/"/>components/jquery.layout.js" type="text/javascript"></script><!--jquery 布局-->
	<script src="<c:url value="/"/>components/jqgrid/js/grid.locale-cn.js" type="text/javascript"></script> <!--jqgrid 中文包-->
	<script src="<c:url value="/"/>components/jqgrid/js/jquery.jqGrid.min.js" type="text/javascript"></script> <!--jqgrid 包-->
	<script src="<c:url value="/"/>js/inc_javascript.js"></script>
	<script src="<c:url value="/"/>js/commonFunction.js"></script>
	<script src="<c:url value="/"/>js/changeclass.js"></script> <!--用于改变页面样式-->
	
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/tabstyle.css" />

	<script type="text/javascript">
		//初始化列表和查询窗口Id
		var multiSearchParams = new Array();
		$.initCalendar = function() {
			multiSearchParams[0] = "#listCalendar";				//自我日程的列表Id
			multiSearchParams[1] = "#multiSearchDialogCalendar";	//自我日程的查询模态窗口Id
		}
		$.initCalendar();
			
		$.initOther = function() {
			multiSearchParams[0] = "#listOtherCalendar";					//安排他人日程的列表Id
			multiSearchParams[1] = "#multiSearchDialogOther";	//安排他人日程的查询模态窗口Id
		}
		
		$.initOpen = function() {
			multiSearchParams[0] = "#listOpenCalendar";					//公开日程的列表Id
			multiSearchParams[1] = "#multiSearchDialogOpen";	//公开日程的查询模态窗口Id
		}
		
		$().ready(function(){
			//加载首页
			$.ajax({
				url: "listCalendar.jsp",
				type: "GET",
				dataType: "html",
				beforeSend: function (xhr) {
				},
				complete : function (req, err) {
					//格式化tab
					loadTabCss();
					
					$("#tabs-1").html(req.responseText);
				}
			});
			
			$.jgrid.defaults = $.extend($.jgrid.defaults,{loadui:"enable"});
	
			//格式化Tab菜单
			var maintab = jQuery('#tabs','#RightPane').tabs({
		        add: function(e, ui) {
		            //选中刚添加的tab
		            maintab.tabs('select', '#' + ui.panel.id);
		            //可拖动tab页
					maintab.find(".ui-tabs-nav").sortable({axis:'x'});
					//格式化tab
					loadTabCss();
		        }
		    });
		    
		    $('#tabs li:nth-child(1) >a').click(function(){
				//初始化自我日程的列表和查询窗口Id
				$.initCalendar();
			});
		    
			$('#tabs li:nth-child(2) >a').click(function(){
				if($("#tabs-2").html()=="") {
					loadTab2();
				}
				//初始化安排他人日程的列表和查询窗口Id
				$.initOther();
			});
			
			$('#tabs li:nth-child(3) >a').click(function(){
				if($("#tabs-3").html()=="") {
					loadTab3();
				}
				//初始化公开日程Id
				$.initOpen();
			});
		});
		
		function loadTab2(){	
			$.ajax({
				url: 'listOtherCalendar.jsp',
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
		
		//如果tab2内容不为空，则重新加载
		function reloadTab2(){
			loadTab2();
		}	
		
		function loadTab3(){	
			$.ajax({
				url: 'listOpenCalendar.jsp',
				cache: false,
				type: "GET",
				dataType: "html",
				beforeSend: function (xhr) {
				},
				complete : function (req, err) {
					$("#tabs-3").empty().html(req.responseText);
				}
			});		
		}		
		
		//如果tab3内容不为空，则重新加载
		function reloadTab3(){
			loadTab3();
		}	
		
	</script>
	
</head>

<body>
<!-- #RightPane -->
<div class="ui-layout-center ui-helper-reset ui-widget-content" id="RightPane">	
	<div id="tabs" class="jqgtabs">
		<ul>
			<li><a href="#tabs-1">我的日程安排</a></li>
			<li><a href="#tabs-2">安排他人日程</a></li>
			<li><a href="#tabs-3">他人公开日程</a></li>
		</ul>
		<div id="tabs-1"></div>
		<div id="tabs-2"></div>
		<div id="tabs-3"></div>
	</div>
</div>
</body>
</html>

