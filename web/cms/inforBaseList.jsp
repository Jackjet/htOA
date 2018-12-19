<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>咨询信息</title>
	<script src="<c:url value="/"/>datePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="<c:url value="/"/>components/jquery-1.4.2.js" type="text/javascript"></script>
		<%--<script src="<c:url value="/"/>components/jqgrid/js/jquery-ui-1.8.2.custom.min.js" type="text/javascript"></script>--%>
		
		<script src="<c:url value="/"/>js/jquery.jqGrid-4.4.5/js/jquery-1.9.0.min.js" type="text/javascript"></script>
		<%--<script src="<c:url value="/"/>js//jquery-1.9.1.min.js" type="text/javascript"></script>--%>

		<script src="<c:url value="/"/>js/jquery-ui-1.9.2.custom/js/jquery-ui-1.9.2.custom.min.js" type="text/javascript"></script> <!--jquery ui-->
		
		
<!--<script src="<c:url value="/"/>components/jquery.layout.js" type="text/javascript"></script>jquery 布局-->
		<script src="<c:url value="/"/>js/jquery.layout-latest.js" type="text/javascript"></script> <!--jquery 布局-->
		
<!-- <script src="<c:url value="/"/>components/jqgrid/js/grid.locale-cn.js" type="text/javascript"></script> 
		<script src="<c:url value="/"/>components/jqgrid/js/jquery.jqGrid.min.js" type="text/javascript"></script> -->
		
		<script src="<c:url value="/"/>js/jquery.jqGrid-4.4.5/js/jquery.jqGrid.src.js" type="text/javascript"></script> <!--jqgrid 包-->
		<script src="<c:url value="/"/>js/jquery.jqGrid-4.4.5/js/i18n/grid.locale-cn.js" type="text/javascript"></script> <!--jqgrid 中文包-->
		
		<script src="<c:url value="/"/>js/commonFunction.js"></script>
		<script src="<c:url value="/"/>js/inc_javascript.js"></script>
		<script src="<c:url value="/"/>js/multisearch.js"></script> <!--加载模态多条件查询相关js-->
		<script src="<c:url value="/"/>js/changeclass.js"></script> <!--用于改变页面样式-->
<!--<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />
		<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
		<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/tabstyle.css" />-->
		
		<%--<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>js/jquery-ui-1.9.2.custom/css/cupertino/jquery-ui-1.9.2.custom.css" />--%>
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>css/base/jquery-ui-1.9.2.custom.css" />
		<%--<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>js/jquery.jqGrid-4.4.5/css/ui.jqgrid.css" />--%>
		<%--<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>js/jquery.jqGrid-4.4.5/css/tabstyle.css" />--%>
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/tabstyle.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="/css/treeNormal.css" />
</head>
<script type="text/javascript">
	
	$().ready(function(){
//	    var hei = document.documentElement.clientHeight-600;
//	    $('#LeftPane').height(hei);
		$('body').layout({
			resizerClass: 'ui-state-default',
	        west__onresize: function (pane, $Pane) {
	            jQuery("#west-grid").jqGrid('setGridWidth',$Pane.innerWidth()-2);
			}
		});

       /* jQuery().ready(function (){
            $("#RightPane").resize(function(){
                <%--alert('${param.categoryId}')--%>
                $("#list"+${_Category.categoryId}+"0").setGridWidth($("#RightPane").width()-20);
            });
        });*/


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
	                
	                $(ui.tab).dblclick(function(){   //双击关闭事件绑定
			    		 	var li = $(ui.tab).parent();
			    		 	var index = $('#tabs li').index(li.get(0));
			    		 	$("#tabs").tabs("remove",index);
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
	    }).closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
	    
	});
	
</script>
<style type="text/css">
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

	 .ui-jqgrid tr.jqgrow td { /*jqgrid中的td*/
		 font-weight: normal;
		 overflow: hidden;

		 height: 35px;       /*td的高度*/
		 padding: 10px 2px 10px 2px;
		 border-bottom: 1px solid rgb(10, 84, 94);
		 white-space: nowrap;!important;
	 }
	.ui-layout-pane-west{
		margin:8px 0px 49.8px 11px!important;
		height: auto;
	}

</style>
<%--左边树样式--%>
<style type="text/css">

</style>
<body>
<script language="JavaScript" src="/js/jquery.nicescroll.min.js"></script>
<script language="JavaScript" src="/js/nicescroll.js"></script>
<script>
    $(document).ready(function() {
//            $('#LeftPane').slimScroll({
//                //                width:document.getElementById("td").clientWidth - 60,
//                height: "100%",
//                color: '#8b8b8b',
//                alwaysVisible: true
//            });


        $("#LeftPane").niceScroll({
            cursorcolor: "#747474",//滚动条的颜色
            background: "rgba(255,255,255,0.05)", // 轨道的背景颜色
            cursoropacitymax: 1, //滚动条的透明度，从0-1
//                touchbehavior: true, //使光标拖动滚动像在台式电脑触摸设备
            gesturezoom: true,
            cursorwidth: "8px", //滚动条的宽度
            cursorborder: "0", // 游标边框css定义
            horizrailenabled: false,
            cursorborderradius: "5px",//以像素为光标边界半径  圆角
            autohidemode: false, //是否隐藏滚动条  true的时候默认不显示滚动条，当鼠标经过的时候显示滚动条
            zindex:"auto",//给滚动条设置z-index值
            oneaxismousemode: "false",// 当只有水平滚动时可以用鼠标滚轮来滚动，如果设为false则不支持水平滚动，如果设为auto支持双轴滚动
            railpadding: {top:0, right:0, left:0, bottom:0 },//滚动条的位置
            iframeautoresize: true, // 在加载事件时自动重置iframe大小
            sensitiverail: true, // 单击轨道产生滚动
            preventmultitouchscrolling: true, // 防止多触点事件引发滚动*/

        });
    });
</script>
<!-- #LeftPane -->
<div  class="ui-layout-west ui-widget ui-widget-content" id="LeftPane" style="background-color:#062434;overflow-x:hidden;position:relative;	border: 1px solid #0de8f5;border-radius:5px;padding: 0 auto;">
	<table id="west-grid"></table>
</div>
<!-- #RightPane -->
	<div class="ui-layout-center ui-helper-reset ui-widget-content" id="RightPane" style="overflow-x:hidden;padding: 0 auto;margin: 0 auto;">
		<!-- Tabs pane -->
		<div id="tabs" class="jqgtabs">
			<ul>
				<li style="padding-left: 2px"><a href="#tabs-1" >所有信息</a></li>
			</ul>
			<div id="tabs-1" style="border:1px solid #0DE8F5;border-radius:0 5px 5px 5px;"></div>
		</div>
	</div>
</body>

</html>

