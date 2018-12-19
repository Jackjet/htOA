<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>基本维护</title>

	<script src="<c:url value="/"/>datePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="<c:url value="/"/>components/jquery-1.4.2.js" type="text/javascript"></script>
	<script src="<c:url value="/"/>js/jquery.jqGrid-4.4.5/js/jquery-1.9.0.min.js" type="text/javascript"></script>
	<script src="<c:url value="/"/>js/jquery-ui-1.9.2.custom/js/jquery-ui-1.9.2.custom.min.js" type="text/javascript"></script> <!--jquery ui-->
	<script src="<c:url value="/"/>js/jquery.layout-latest.js" type="text/javascript"></script> <!--jquery 布局-->
	<script src="<c:url value="/"/>js/jquery.jqGrid-4.4.5/js/jquery.jqGrid.src.js" type="text/javascript"></script> <!--jqgrid 包-->
	<script src="<c:url value="/"/>js/jquery.jqGrid-4.4.5/js/i18n/grid.locale-cn.js" type="text/javascript"></script> <!--jqgrid 中文包-->
	<script src="<c:url value="/"/>js/commonFunction.js"></script>
	<script src="<c:url value="/"/>js/inc_javascript.js"></script>
	<%--<script src="<c:url value="/"/>js/multisearch.js"></script> <!--加载模态多条件查询相关js-->--%>
	<script src="<c:url value="/"/>js/changeclass.js"></script> <!--用于改变页面样式-->

	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>css/base/jquery-ui-1.9.2.custom.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/tabstyle.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="/css/treeNormal.css" />
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
			url: "listUser.jsp?isvalid=true",
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
//				loadTabCss();
                $("div[id^='tabs-']").css({'margin-top':'0px','margin-left':'0px','padding-left':'0px','padding-top':'3px','padding-right':'0px',
                    'border':'1px solid #0DE8F5','border-radius':'0 5px 5px 5px'
                });
	        }
	    });
	    
	    //加载Tab内容
		$('#west-grid tr').click(function(){
			var st = "#tabs-"+$(this).find('#tabId').text();
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
		
	});
	
	//保存信息后重新加载Tab
	function loadTab(url, tabId){
		$.ajax({
			url: url,
			cache: false,
			type: "GET",
			dataType: "html",
			beforeSend: function (xhr) {
			},
			complete : function (req, err) {
				$("#tabs-"+tabId).empty().html(req.responseText);
			}
		});		
	}
	
	//更新域用户
	function domainSync(){
		$.ajax({
			url: '/domainSync.do',
			cache: false,
			type: "GET",
			//dataType : "json",
			async: true,
              	cache: false,
			beforeSend: function (xhr) {
//				$("#LeftPane").mask("同步中，请稍等...");
//                $('#warning').text('正在处理，请稍等！');
				showMask()
			},
			complete : function(req, msg) {
                hideMask()
				//var msg = eval("(" + req.responseText + ")");
				//alert(msg+"--");


            },
			success : function (msg) {
                hideMask()
				//alert(msg);
				/*if($("#LeftPane").isMasked()){
				　　$("#LeftPane").unmask();
				}
				$("#LeftPane").unmask();
				*/
				if(msg == 'success'){

					alert("同步成功！");
					window.location.reload();
				}
				if(msg == 'fail'){

					alert("同步失败，请重试！");
				}
			}
		});
	}
	
</script>
	<script type="text/javascript">
        //兼容火狐、IE8
        //显示遮罩层
        function showMask(){
            $("#mask").css("height",$(document).height());
            $("#mask").css("width",$(document).width());
            $("#mask").show();
        }
        //隐藏遮罩层
        function hideMask(){

            $("#mask").hide();
        }

	</script>
	<style type="text/css">
		.mask {
			position: absolute; top: 0px; filter: alpha(opacity=60); background-color: #777;
			z-index: 1002; left: 0px;
			opacity:0.5; -moz-opacity:0.5;
			/*text-align: center;*/
			font-size: 15px;
			padding-top: 80%;
			padding-left: 20%;
			color: #00b4f6;
			display: none;
		}
		.ui-tabs .ui-tabs-nav li.ui-tabs-active {
			margin-left:0;
			margin-bottom: -3px;
			padding-top: 1px;
		}
		.ui-state-active, .ui-widget-content .ui-state-active, .ui-widget-header .ui-state-active {
			/*position: absolute;*/

			border: solid 1px #0de8f5;
			border-radius:5px 5px 0 0;
			/*padding-bottom: 1px!important;*/
			border-bottom: 3px solid rgb(14, 22, 36) !important;
			/*margin-bottom: -3px!important;*/
			/*border-left: 1px solid rgb(13, 232, 245);*/
			/*border-right: 1px solid rgb(13, 232, 245);*/
			/*border-bottom: 1px solid rgba(13, 242, 255, 0);*/
			background: rgba(255, 255, 255, 0);
			font-size:16px;
			font-weight:bold;
			color: #ff8d73;
		}
		.ui-tabs .ui-tabs-nav{
			margin-left: -4px;
		}

	</style>
</head>

<body>
<!-- #LeftPane -->
<%--<a href="javascript:;" onclick="hideMask()" >点我显示遮罩层</a><br />--%>
<div class="ui-layout-west ui-widget ui-widget-content" id="LeftPane" style="background-color:#062434;overflow-x:hidden;position:relative;	border: 1px solid #0de8f5;border-radius:5px;padding: 0 auto;">
	<div id="mask" class="mask"><font>同步中，请稍等...</font></div>
	<%--<br/>--%>
	<%--<a href="javascript:;" onclick="showMask()" >点我显示遮罩层</a><br />--%>


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
			<div class="ui-jqgrid-bdiv"  style="overflow-x: hidden">
				<table cellspacing="0" cellpadding="0" border="0" id="west-grid">
					<tbody>
						<tr class="ui-widget-content jqgrow ui-row-ltr" onmouseover="over(this,'ui-state-hover jqgrow ui-row-ltr')" onmouseout="out(this)" onclick="clicked(this)">
							<td style="display: none; width: 1px;" id="tabId">1</td>
							<td title="用户信息" style="width: 193px;" id="tabMenu"><div style="width: 36px;" class="tree-wrap tree-wrap-ltr"><div class="ui-icon ui-icon-document-b tree-leaf" style="left: 18px;"></div></div><span style="cursor: pointer;">用户信息</span></td>
							<td style="display: none; width: 1px;" id="tabUrl">listUser.jsp?isvalid=true</td>
						</tr>
						<tr class="ui-widget-content jqgrow ui-row-ltr" onmouseover="over(this,'ui-state-hover jqgrow ui-row-ltr')" onmouseout="out(this)" onclick="clicked(this)">
							<td style="display: none; width: 1px;" id="tabId">2</td>
							<td title="人员信息" id="tabMenu"><div style="width: 36px;" class="tree-wrap tree-wrap-ltr"><div class="ui-icon ui-icon-document-b tree-leaf" style="left: 18px;"></div></div><span style="cursor: pointer;">人员信息</span></td>
							<td style="display: none; width: 1px;" id="tabUrl">listPerson.jsp?isvalid=true</td>
						</tr>
						<tr class="ui-widget-content jqgrow ui-row-ltr" onmouseover="over(this,'ui-state-hover jqgrow ui-row-ltr')" onmouseout="out(this)" onclick="clicked(this)">
							<td style="display: none; width: 1px;" id="tabId">3</td>
							<td title="角色信息" id="tabMenu"><div style="width: 36px;" class="tree-wrap tree-wrap-ltr"><div class="ui-icon ui-icon-document-b tree-leaf" style="left: 18px;"></div></div><span style="cursor: pointer;">角色信息</span></td>
							<td style="display: none; width: 1px;" id="tabUrl">listRole.jsp</td>
						</tr>
						<tr class="ui-widget-content jqgrow ui-row-ltr" onmouseover="over(this,'ui-state-hover jqgrow ui-row-ltr')" onmouseout="out(this)" onclick="clicked(this)">
							<td style="display: none; width: 1px;" id="tabId">4</td>
							<td title="部门信息" id="tabMenu"><div style="width: 36px;" class="tree-wrap tree-wrap-ltr"><div class="ui-icon ui-icon-document-b tree-leaf" style="left: 18px;"></div></div><span style="cursor: pointer;">部门信息</span></td>
							<td style="display: none; width: 1px;" id="tabUrl">/core/organizeInfor.do?method=list</td>
						</tr>
						<tr class="ui-widget-content jqgrow ui-row-ltr" onmouseover="over(this,'ui-state-hover jqgrow ui-row-ltr')" onmouseout="out(this)" onclick="clicked(this)">
							<td style="display: none; width: 1px;" id="tabId">5</td>
							<td title="岗位信息" id="tabMenu"><div style="width: 36px;" class="tree-wrap tree-wrap-ltr"><div class="ui-icon ui-icon-document-b tree-leaf" style="left: 18px;"></div></div><span style="cursor: pointer;">岗位信息</span></td>
							<td style="display: none; width: 1px;" id="tabUrl">/core/structureInfor.do?method=list</td>
						</tr>
						<tr class="ui-widget-content jqgrow ui-row-ltr" onmouseover="over(this,'ui-state-hover jqgrow ui-row-ltr')" onmouseout="out(this)" onclick="clicked(this)">
							<td style="display: none; width: 1px;" id="tabId">9</td>
							<td title="惯用语" id="tabMenu"><div style="width: 36px;" class="tree-wrap tree-wrap-ltr"><div class="ui-icon ui-icon-document-b tree-leaf" style="left: 18px;"></div></div><span style="cursor: pointer;">惯用语</span></td>
							<td style="display: none; width: 1px;" id="tabUrl">listApproveSentence.jsp</td>
						</tr>
						<tr class="ui-widget-content jqgrow ui-row-ltr" onmouseover="over(this,'ui-state-hover jqgrow ui-row-ltr')" onmouseout="out(this)" onclick="clicked(this)">
							<td style="display: none; width: 1px;" id="tabId">6</td>
							<td title="系统功能及权限" id="tabMenu"><div style="width: 36px;" class="tree-wrap tree-wrap-ltr"><div class="ui-icon ui-icon-document-b tree-leaf" style="left: 18px;"></div></div><span style="cursor: pointer;">系统功能及权限</span></td>
							<td style="display: none; width: 1px;" id="tabUrl">/core/virtualResource.do?method=list</td>
						</tr>
						<tr class="ui-widget-content jqgrow ui-row-ltr" onmouseover="over(this,'ui-state-hover jqgrow ui-row-ltr')" onmouseout="out(this)" onclick="clicked(this)">
							<td style="display: none; width: 1px;" id="tabId">7</td>
							<td title="已注销用户" id="tabMenu"><div style="width: 36px;" class="tree-wrap tree-wrap-ltr"><div class="ui-icon ui-icon-document-b tree-leaf" style="left: 18px;"></div></div><span style="cursor: pointer;">已注销用户</span></td>
							<td style="display: none; width: 1px;" id="tabUrl">listUser.jsp?isvalid=false</td>
						</tr>
						<tr class="ui-widget-content jqgrow ui-row-ltr" onmouseover="over(this,'ui-state-hover jqgrow ui-row-ltr')" onmouseout="out(this)" onclick="clicked(this)">
							<td style="display: none; width: 1px;" id="tabId">8</td>
							<td title="已注销人员" id="tabMenu"><div style="width: 36px;" class="tree-wrap tree-wrap-ltr"><div class="ui-icon ui-icon-document-b tree-leaf" style="left: 18px;"></div></div><span style="cursor: pointer;">已注销人员</span></td>
							<td style="display: none; width: 1px;" id="tabUrl">listPerson.jsp?isvalid=false</td>
						</tr>
						
						<!-- <tr class="ui-widget-content jqgrow ui-row-ltr" onmouseover="over(this,'ui-state-hover jqgrow ui-row-ltr')" onmouseout="out(this)" onclick="clicked(this);">
							<td style="display: none; width: 1px;" id="tabId">11</td>
							<td title="app首页图片" id="tabMenu"><div style="width: 36px;" class="tree-wrap tree-wrap-ltr"><div class="ui-icon ui-icon-document-b tree-leaf" style="left: 18px;"></div></div><span style="cursor: pointer;">app首页图片</span></td>
							<td style="display: none; width: 1px;" id="tabUrl">listAppImg.jsp</td>
						</tr> -->
					</tbody>
				</table>
				<table cellspacing="0" cellpadding="0" border="0" id="west-grid-1" style="width: 195px;">
					
					<tr class="ui-widget-content jqgrow ui-row-ltr" onmouseover="over(this,'ui-state-hover jqgrow ui-row-ltr')" onmouseout="out(this)" onclick="domainSync();">
						<td style="display: none; width: 1px;" id="tabId">10</td>
						<td title="同步域用户" id="tabMenu"><div style="width: 36px;" class="tree-wrap tree-wrap-ltr"><div class="ui-icon ui-icon-document-b tree-leaf" style="left: 18px;"></div></div><span style="cursor: pointer;">同步域用户</span></td>
						<td style="display: none; width: 1px;" id="tabUrl"></td>
					</tr>
				</table>
			</div>
		</div>
	</div>
</div>
<!-- #RightPane -->
<div class="ui-layout-center ui-helper-reset ui-widget-content" id="RightPane" style="overflow-x:hidden;padding: 0 auto;margin: 0 auto;">
	<!-- Tabs pane -->
	<div id="tabs" class="jqgtabs">
		<ul>
			<li><a href="#tabs-1" >用户信息</a></li>
		</ul>
		<div id="tabs-1" style="border:1px solid #0DE8F5;border-radius:0 5px 5px 5px;"></div>
	</div>
</div>
</body>
</html>

