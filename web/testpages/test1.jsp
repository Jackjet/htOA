<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<script src="<c:url value="/"/>components/jquery-1.4.2.js" type="text/javascript"></script> <!--jquery包-->
<script src="<c:url value="/"/>components/jqgrid/js/jquery-ui-1.8.2.custom.min.js" type="text/javascript"></script><!--jquery ui-->
<script src="<c:url value="/"/>components/jquery.layout.js" type="text/javascript"></script><!--jquery 布局-->
<script src="<c:url value="/"/>components/jqgrid/js/grid.locale-cn.js" type="text/javascript"></script> <!--jqgrid 中文包-->
<script src="<c:url value="/"/>components/jqgrid/js/jquery.jqGrid.min.js" type="text/javascript"></script> <!--jqgrid 包-->
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
		//$("#tabs-1", "#tabs").load("tab1.jsp");
		$.ajax({
			url: "../core/base/listUser.jsp",
			type: "GET",
			dataType: "html",
			beforeSend: function (xhr) {
				//$(st,"#tabs").height(100).addClass("tabpreloading");
				//$(st,"#tabs").css("text-align","center").html("<img src='../components/jqgrid/css/images_extend/loading.gif' border=0 />");
			},
			complete : function (req, err) {
				//$(st,"#tabs").removeClass("tabpreloading").append(req.responseText);
				$("#tabs-1", "#tabs").html(req.responseText);
			}
		});
		
		$.jgrid.defaults = $.extend($.jgrid.defaults,{loadui:"enable"});

		var maintab = jQuery('#tabs','#RightPane').tabs({
	        add: function(e, ui) {
	            // append close thingy
	            $(ui.tab).parents('li:first')
	                .append('<span class="ui-tabs-close ui-icon ui-icon-close" title="关闭"></span>')
	                .find('span.ui-tabs-close')
	                .click(function() {
	                    maintab.tabs('remove', $('li', maintab).index($(this).parents('li:first')[0]));
	                });
	            // select just added tab
	            //alert(ui.tab);
	            maintab.tabs('select', '#' + ui.panel.id);
				//可拖动TAB页
				//maintab.find(".ui-tabs-nav").sortable({axis:'x'});
	        }
	    });
	    //alert(maintab.tabs);
	    
	    //加载Tab
		$('#west-grid tr').click(function(){
			var st = "#t"+$(this).find('#tabId').text();
			if($(st).html() != null ) {
				maintab.tabs('select',st);
			} else {
				maintab.tabs('add',st,$(this).find('#tabMenu').text());
				//alert($(this).find('#tabUrl').text());
				$.ajax({
					url: $(this).find('#tabUrl').text(),
					cache: false,
					type: "GET",
					dataType: "html",
					beforeSend: function (xhr) {
						$(st,"#tabs").css("text-align","center").html("<img src='../components/jqgrid/css/images_extend/loading.gif' border=0 />");
					},
					complete : function (req, err) {
						$(st,"#tabs").empty().html(req.responseText);
						//$(st, "#tabs").load("../core/base/listRole.jsp");
						//alert(1111111111);
					}
				});
				//$(st,"#tabs").html("<iframe width='100%' height='400' border='0' frameborder='0' scrolling='no' SCROLLING=NO src='../core/base/listRole.jsp'></iframe>")
			}
		});
	    
	});
	
		
</script>

</head>

<body>
<!-- #LeftPane -->
<div class="ui-layout-west ui-widget ui-widget-content" id="LeftPane">
	<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all">
		<div class="ui-jqgrid-view">
			<div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
				<span class="ui-jqgrid-title">基本维护</span>
			</div>
			<div class="ui-state-default ui-jqgrid-hdiv">
				<table cellspacing="0" cellpadding="0" border="0" style="width: 198px;" class="ui-jqgrid-htable">
					<thead>
						<tr>
							<th>模块列表</th>
						</tr>
					</thead>
				</table>
			</div>
			<div class="ui-jqgrid-bdiv">
				<table cellspacing="0" cellpadding="0" border="0" id="west-grid">
					<tbody>
						<tr class="ui-widget-content jqgrow ui-row-ltr">
							<td style="display: none; width: 1px;" id="tabId">abs-1</td>
							<td title="用户信息" style="width: 193px;" id="tabMenu"><div style="width: 36px;" class="tree-wrap tree-wrap-ltr"><div class="ui-icon ui-icon-document-b tree-leaf" style="left: 18px;"></div></div><span style="cursor: pointer;">用户信息</span></td>
							<td style="display: none; width: 1px;" id="tabUrl">../core/base/listUser.jsp</td>
						</tr>
						<tr class="ui-widget-content jqgrow ui-row-ltr">
							<td style="display: none; width: 1px;" id="tabId">1</td>
							<td title="人员信息" id="tabMenu"><div style="width: 36px;" class="tree-wrap tree-wrap-ltr"><div class="ui-icon ui-icon-document-b tree-leaf" style="left: 18px;"></div></div><span style="cursor: pointer;">人员信息</span></td>
							<td style="display: none; width: 1px;" id="tabUrl">/core/personInfor.do?method=list</td>
						</tr>
						<tr class="ui-widget-content jqgrow ui-row-ltr">
							<td style="display: none; width: 1px;" id="tabId">2</td>
							<td title="角色信息" id="tabMenu"><div style="width: 36px;" class="tree-wrap tree-wrap-ltr"><div class="ui-icon ui-icon-document-b tree-leaf" style="left: 18px;"></div></div><span style="cursor: pointer;">角色信息</span></td>
							<td style="display: none; width: 1px;" id="tabUrl">../core/base/listRole.jsp</td>
						</tr>
						<tr class="ui-widget-content jqgrow ui-row-ltr">
							<td style="display: none; width: 1px;" id="tabId">3</td>
							<td title="部门信息" id="tabMenu"><div style="width: 36px;" class="tree-wrap tree-wrap-ltr"><div class="ui-icon ui-icon-document-b tree-leaf" style="left: 18px;"></div></div><span style="cursor: pointer;">部门信息</span></td>
							<td style="display: none; width: 1px;" id="tabUrl">/core/organizeInfor.do?method=list</td>
						</tr>
						<tr class="ui-widget-content jqgrow ui-row-ltr">
							<td style="display: none; width: 1px;" id="tabId">4</td>
							<td title="岗位信息" id="tabMenu"><div style="width: 36px;" class="tree-wrap tree-wrap-ltr"><div class="ui-icon ui-icon-document-b tree-leaf" style="left: 18px;"></div></div><span style="cursor: pointer;">岗位信息</span></td>
							<td style="display: none; width: 1px;" id="tabUrl">/core/structureInfor.do?method=list</td>
						</tr>
						<tr class="ui-widget-content jqgrow ui-row-ltr">
							<td style="display: none; width: 1px;" id="tabId">5</td>
							<td title="系统功能及权限" id="tabMenu"><div style="width: 36px;" class="tree-wrap tree-wrap-ltr"><div class="ui-icon ui-icon-document-b tree-leaf" style="left: 18px;"></div></div><span style="cursor: pointer;">系统功能及权限</span></td>
							<td style="display: none; width: 1px;" id="tabUrl">/core/virtualResource.do?method=list</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>
<!-- #RightPane -->
<div class="ui-layout-center ui-helper-reset ui-widget-content" id="RightPane">
	<!-- Tabs pane -->
	<div id="tabs" class="jqgtabs">
		<ul>
			<li><a href="#tabs-1">用户信息</a></li>
		</ul>
		<div id="tabs-1">
							
		</div>
	</div>
</div>
</body>
</html>

