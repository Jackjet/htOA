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
	<script src="<c:url value="/"/>js/changeclass.js"></script> <!--用于改变页面样式-->
	
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/tabstyle.css" />

<script type="text/javascript">
	//初始化列表和查询窗口Id
	var multiSearchParams = new Array();
	multiSearchParams[0] = "#listCategory";			//列表Id
	multiSearchParams[1] = "#multiSearchDialog";//查询模态窗口Id
	
	//新增
	function addCategory(){
		var refresh = window.showModalDialog("/personal/address/AddressCategory.do?method=edit",null,"dialogWidth:700px;dialogHeight:400px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		if(refresh == "Y") {
			self.location.reload();
		}
	}
	
	//修改
	function editCategory(rowId){
		var refresh = window.showModalDialog("/personal/address/AddressCategory.do?method=edit&rowId="+rowId,'',"dialogWidth:700px;dialogHeight:400px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		if(refresh == "Y") {
			self.location.reload();
		}
	}
</script>

<title>个人通讯录分类</title>
	<div>
		<table id="listCategory"></table> <!-- 信息列表 -->
		<div id="pagerCategory"></div> <!-- 分页 -->
	</div>
	
	<!-- 查询框 -->
	<div id="multiSearchDialog" style="display: none;">  
	    <table>  
	        <tbody>  
	            <tr>  
	                <td>  
	                    <input type="hidden" class="searchField" value="categoryName"/>分类名称：
	                    <input type="hidden" class="searchOper" value="cn"/>
	                </td>  
	                <td>  
	                    <%--<select class="searchString" id="addressCategoryId">  
	                    </select>--%>
	                    <input type="text" class="searchString"/>
	                </td>  
	            </tr>  
	            <%--<tr>  
	                <td>  
	                    <input type="hidden" class="searchField" value="orderNo"/>排序序号：
	                    <input type="hidden" class="searchOper" value="eq"/>
	                </td>  
	                <td>  
	                    <input type="text" class="searchString"/>
	                </td>  
	            </tr>--%> 
	            
	        </tbody>  
	    </table>  
	</div>
	
	<script type="text/javascript"> 		
		
		//自定义操作栏的显示内容
	    function formatOperation(cellvalue, options, rowdata) {
           var returnStr = "<a href='javascript:;' onclick='editCategory("+options.rowId+")'>[修改]</a>";
           return returnStr;
	    }

	    
		//加载表格数据
		var $mygrid = jQuery("#listCategory").jqGrid({
            url:'/personal/address/AddressCategory.do?method=getCategoryTree',
            datatype: "json",   //从后台获取的数据类型              
           	autowidth: true,	//是否自适应宽度
			height:300,
            colNames:['categoryId', '分类名称', '排序序号', '操作', ''],//表的第一行标题栏
            //以下为每列显示的具体数据
            colModel:[
                {name:'categoryId',index:'categoryId', width:0, key:true, search:false, hidden:true},
                {name:'categoryName',index:'categoryName', width:50, align:'center'},
                {name:'orderNo',index:'orderNo', width:20, align:'center'},
                {name:'categoryId', width:20, align:'center', search:false, sortable:false, formatter:formatOperation},
                {align:'center'}
            ],
            caption: "个人通讯录分类",
            sortname: 'categoryId',//默认排序的字段
            sortorder: 'asc',	//默认排序形式:升序,降序
            multiselect: true,	//是否支持多选,可用于批量删除
            viewrecords: true,	//是否显示数据的总条数(显示在右下角)
            rowNum: 10,			//每页显示的默认数据条数
            rowList: [10,20,30],//可选的每页显示的数据条数(显示在中间,下拉框形式)
            scroll: false, 		//是否采用滚动分页的形式
            scrollrows: false,	//当选中的行数据隐藏时,grid是否自动滚               
            jsonReader:{
               repeatitems: false	//告诉JqGrid,返回的数据的标签是否是可重复的
            },         
            pager: "#pagerCategory"	//分页工具栏
            //caption: "用户信息"	//表头
       	}).navGrid('#pagerCategory',{edit:false,add:false,del:false,search:false});       
	
		//自定义按钮
		jQuery("#listCategory").jqGrid('navButtonAdd','#pagerCategory', {
			caption:"新增", title:"点击新增信息", buttonicon:'ui-icon-plusthick', onClickButton: addCategory
		});
		
		jQuery("#listCategory").jqGrid('navButtonAdd','#pagerCategory', {
			caption:"<span style='color: red;'>批量删除</span>", title:"点击批量删除", buttonicon:'ui-icon-closethick', onClickButton: deleteCategory
		});
		
		jQuery("#listCategory").jqGrid('navButtonAdd','#pagerCategory', {
			caption:"查询", title:"点击查询", buttonicon:'ui-icon-search', onClickButton: openMultipleSearchDialog
		});
		
		//删除数据
		function deleteCategory(){
			doDelete("/personal/address/AddressCategory.do?method=delete","listCategory");
		}
	
		
		jQuery().ready(function (){
		    //获取分类信息(查询条件)
		    $.getJSON("/personal/address/AddressCategory.do?method=getCategoryName",function(data) {
		           var options = "<option value=''>--选择分类--</option>";
		           $.each(data._CategoryNames, function(i, n) {
		               options += "<option value='"+n.categoryName+"'>"+n.categoryName+"</option>";   
		           });   
		           $('#addressCategoryId').html(options);   
		        }   
		    );
		});
		
	</script>