<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<script src="<c:url value="/"/>datePicker/WdatePicker.js" language="JavaScript"></script>
	<script src="<c:url value="/"/>components/jquery-1.4.2.js" type="text/javascript"></script> <!--jquery包-->
	<script src="<c:url value="/"/>components/jqgrid/js/jquery-ui-1.8.2.custom.min.js" type="text/javascript"></script><!--jquery ui-->
	<script src="<c:url value="/"/>components/jquery.layout.js" type="text/javascript"></script><!--jquery 布局-->
	<script src="<c:url value="/"/>components/jqgrid/js/grid.locale-cn.js" type="text/javascript"></script> <!--jqgrid 中文包-->
	<script src="<c:url value="/"/>components/jqgrid/js/jquery.jqGrid.min.js" type="text/javascript"></script> <!--jqgrid 包-->
	<script src="<c:url value="/"/>js/inc_javascript.js"></script>
	<script src="<c:url value="/"/>js/commonFunction.js"></script>
	<script src="<c:url value="/"/>js/multisearch.js"></script> <!--加载模态多条件查询相关js-->
	
	<%--<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />--%>
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>css/base/jquery-ui-1.9.2.custom.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/tabstyle.css" />

<script type="text/javascript">
	
	//新增
	function addAppImg(){
		
		window.open("/core/app.do?method=edit", "_blank");
	}
	//修改
	function editAppImg(rowId){
		window.open("/core/app.do?method=edit&imgId="+rowId, "_blank");
	}
</script>
<!--<script src="<c:url value="/"/>js/multisearch.js"></script> 加载模态多条件查询相关js-->

<title>APP Img</title>
<body style="border:1px solid #0DE8F5;border-radius: 5px">
  		<div>
			<table id="listAppImg"></table> <!-- 信息列表 -->
			<div id="pagerAppImg"></div> <!-- 分页 -->
		</div>
</body>
		<script type="text/javascript"> 		
			
			//自定义操作栏的显示内容
		    function formatTitle(cellvalue, options, rowdata) {
	           var returnStr = "";
	           returnStr += "<a href='javascript:;' onclick='editAppImg("+options.rowId+")'>"+cellvalue+"</a>";
	           return returnStr;
		    }
		    
		    
			//加载表格数据
			var $mygrid = jQuery("#listAppImg").jqGrid({
                url:'/core/app.do?method=list',
                //rownumbers: true,	//是否显示序号
                datatype: "json",   //从后台获取的数据类型              
               	autowidth: true,	//是否自适应宽度
				//height: "auto",
                height:document.documentElement.clientHeight-140,
                colNames:['Id', '标题', '排序号', '更新日期'],//表的第一行标题栏
                //以下为每列显示的具体数据
                colModel:[
                    {name:'imgId',index:'imgId',align:'center', width:0, sorttype:"int", search:false, key:true, hidden:true},
                    {name:'imgTitle',align:'center',index:'imgTitle', width:30, sortable:true, sorttype:"string", formatter:formatTitle},
                    {name:'imgOrder',index:'imgOrder', width:20, align:'center'},
                    {name:'updateDate',index:'updateDate', width:30, align:'center'}
                ],
//                caption: "app首页图片",
                sortname: 'updateDate',//默认排序的字段
                sortorder: 'desc',	//默认排序形式:升序,降序
                multiselect: true,	//是否支持多选,可用于批量删除
                viewrecords: true,	//是否显示数据的总条数(显示在右下角)
                rowNum: 10,			//每页显示的默认数据条数
                rowList: [10,20,30],//可选的每页显示的数据条数(显示在中间,下拉框形式)
                scroll: false, 		//是否采用滚动分页的形式
                scrollrows: false,	//当选中的行数据隐藏时,grid是否自动滚               
                jsonReader:{
                   repeatitems: false	//告诉JqGrid,返回的数据的标签是否是可重复的
                },         
                pager: "#pagerAppImg"	//分页工具栏
                //caption: "用户信息"	//表头
        }).navGrid('#pagerAppImg',{edit:false,add:false,del:false,search:false}).closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
        //}).navGrid('#pagerAppImg',{edit:false,add:false,del:false}).searchGrid({multipleSearch:true,autoOpen:false});
		
		//显示各个栏目上的搜索栏
		//$('#listAppImg').jqGrid('filterToolbar','');
		
		//自定义按钮
		jQuery("#listAppImg").jqGrid('navButtonAdd','#pagerAppImg', {
			caption:"新增", title:"点击新增信息", buttonicon:'ui-icon-plusthick', onClickButton: addAppImg
		});
		jQuery("#listAppImg").jqGrid('navButtonAdd','#pagerAppImg', {
			caption:"<span style='color: red;'>批量删除</span>", title:"点击批量删除", buttonicon:'ui-icon-closethick', onClickButton: deleteAppImg
		});
		/*jQuery("#listAppImg").jqGrid('navButtonAdd','#pagerAppImg', {
			caption:"查询", title:"点击查询", buttonicon:'ui-icon-search', onClickButton: openMultipleSearchDialog
		});*/
		
		//批量删除	
		function deleteAppImg(){
			doDelete("/core/app.do?method=delete","listAppImg");
		}
		
	</script>