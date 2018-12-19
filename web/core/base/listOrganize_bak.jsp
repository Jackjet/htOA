<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>

<title>部门[班组]信息</title>

		<script src="<c:url value="/"/>components/jquery-1.4.2.js" type="text/javascript"></script> <!--jquery包-->
		<script src="<c:url value="/"/>components/jqgrid/js/jquery-ui-1.8.2.custom.min.js" type="text/javascript"></script><!--jquery ui-->
		<script src="<c:url value="/"/>components/jquery.layout.js" type="text/javascript"></script><!--jquery 布局-->
		<script src="<c:url value="/"/>components/jqgrid/js/grid.locale-cn.js" type="text/javascript"></script> <!--jqgrid 中文包-->
		<script src="<c:url value="/"/>components/jqgrid/js/jquery.jqGrid.min.js" type="text/javascript"></script> <!--jqgrid 包-->
		<script src="<c:url value="/"/>js/commonFunction.js"></script>
		<script src="<c:url value="/"/>js/inc_javascript.js"></script>
		<script src="<c:url value="/"/>js/multisearch.js"></script> <!--加载模态多条件查询相关js-->
		<script src="<c:url value="/"/>js/changeclass.js"></script> <!--用于改变页面样式-->
		<script src="<c:url value="/"/>js/jquery.contextmenu.r2.packed.js"></script><!-- 菜单树右键 -->
		<script src="<c:url value="/"/>js/changeclass.js"></script> <!--用于改变页面样式-->
		
		<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />
		<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
		<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/tabstyle.css" />

		<script type="text/javascript">
			//新增
			function addOrganize(){
				var returnOrgTag = window.showModalDialog("/core/organizeInfor.do?method=edit",null,"dialogWidth:700px;dialogHeight:400px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
				if(returnOrgTag == "refresh") {
					//保存信息后重新加载tab
					loadTab("listOrganize.jsp", "4");
				}
			}
			//修改
			function editOrganize(organizeId){
				var returnOrgTag = window.showModalDialog("/core/organizeInfor.do?method=edit&organizeId="+organizeId,'',"dialogWidth:700px;dialogHeight:400px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
				if(returnOrgTag == "refresh") {
					//保存信息后重新加载tab
					loadTab("listOrganize.jsp", "4");
				}
			}
			//删除
			function doDelete(organizeId){
				var yes = window.confirm("确定要删除吗？");
				if (yes) {
					var form = document.getElementById('listForm');
					form.action = "<c:url value='/core/organizeInfor.do'/>?method=delete&organizeId="+organizeId;
					form.submit();
					window.location.reload();
				}
			}
			
			function formatOperation(cellvalue, options, rowdata) {
	           var returnStr;
	           
	           return returnStr;
		    }
			
			$().ready(function(){
				//树状显示
		    	jQuery("#listOrg").jqGrid({
			        url:"/core/organizeInfor.do?method=list", 			        
			        datatype: "json",
			        rownumbers: true,
			        //async: true,
			        treedatatype: "json",
			        height: "auto",
			        pager: false,
			        loadui: "disable",
			        colNames: ["Id","部门[班组]名称","部门简称","编号","排序编号","部门经理[主管]","相关操作"],
			        colModel: [
			            {name: "organizeId",index:"organizeId", sorttype:"int", width:1, hidden:true, key:true},
			            {name: "organizeName",index:"organizeName", sortable:true, sorttype:"string"},
			            {name: "shortName",index:"shortName", sortable:true, sorttype:"string", align:"center"},
			            {name: "organizeNo",index:"organizeNo", sortable:true, sorttype:"string", align:"center"},
			            {name: "orderNo",index:"orderNo", sortable:true, sorttype:"string", align:"center"},
			            {name: "director",index:"director", sortable:true, sorttype:"string", align:"center"},
			            {name: "organizeId", sortable:false, align:"center", formatter:formatOperation}
			        ],
			        treeReader : {
					    level_field: 'layer',
					    left_field: 1,
					    right_field: 1,
					    leaf_field: "leaf",
					    expanded_field: true
					},
			        sortname: 'organizeId',//默认排序的字段
			        treeGrid: true,		//树形grid
			        autowidth: true,
			        rowNum: 200,
			        treeIcons: {leaf:'ui-icon-document-b'},
			        jsonReader:{
		               repeatitems: false	//告诉JqGrid,返回的数据的标签是否是可重复的
		            },         
               		pager: "#pagerOrg"
			    }).navGrid('#pagerOrg',{edit:false,add:false,del:false,search:false});
			    
			    //自定义按钮
				jQuery("#listOrg").jqGrid('navButtonAdd','#pagerOrg', {
					caption:"新增", title:"点击新增信息", buttonicon:'ui-icon-plusthick', onClickButton: addOrganize
				});
			    
			});
			
		</script>
</head>
<body>
<div>
	<table id="listOrg"></table>
	<div id="pagerOrg"></div>
</div>
</body>
</html>