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
	//初始化列表和查询窗口Id
	var multiSearchParams = new Array();
	multiSearchParams[0] = "#listMeeting";			//列表Id
	multiSearchParams[1] = "#multiSearchDialog";//查询模态窗口Id


	//新增
	function addMeeting(){
		/** var refresh = window.showModalDialog("/meeting/meetInfor.do?method=edit",null,"dialogWidth:900px;dialogHeight:700px;center:Yes;dialogTop: 100px; dialogLeft: 200px;");
		if(refresh == "Y") {
			self.location.reload();
		} */
		window.open("/meeting/meetInfor.do?method=edit", "_blank");
	}
	//查看
	function doView(rowId){
		/** var refresh = window.showModalDialog("/meeting/meetInfor.do?method=viewMeeting&rowId="+rowId,'',"dialogWidth:900px;dialogHeight:700px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		if(refresh == "Y") {
			self.location.reload();
		} */
		window.open("/meeting/meetInfor.do?method=viewMeeting&rowId="+rowId, "_blank");
	}
</script>

<title>会务安排</title>
<body style="border:1px solid #0DE8F5;border-radius: 5px">
	<div>
		<table id="listMeeting"></table> <!-- 信息列表 -->
		<div id="pagerMeeting"></div> <!-- 分页 -->
	</div>
	
	<!-- 查询框 -->
	<div id="multiSearchDialog" style="display: none;">  
	    <table>  
	        <tbody>  
	            <tr>  
		           <td>  
		              <input type="hidden" class="searchField" value="meetName"/>会议标题：
		              <input type="hidden" class="searchOper" value="cn"/>
		           </td>  
		           <td>  
		              <input type="text" class="searchString"/>  
		           </td>  
		        </tr>
		        
		        <tr>  
		           <td>  
		              <input type="hidden" value="organize.organizeId"/>责任部室：
		              <input type="hidden" value="eq"/>
		           </td>  
		           <td>  
		               <select id="departmentId"></select>
		           </td>  
		        </tr>

				<tr>
		           <td>  
		               <input type="hidden" class="searchField" value="author.personId"/>发布人：
		               <input type="hidden" class="searchOper" value="eq"/>
		           </td>  
		            <td>  
		               <select  class="searchString" id="authorId"></select>
		            </td>  
		       </tr>
	        </tbody>  
	    </table>  
	</div>
</body>
	<script src="<c:url value="/"/>js/multisearch.js"></script> <!--加载模态多条件查询相关js-->		
	<script type="text/javascript"> 		
		
		//自定义操作栏的显示内容
	  
		//显示标题
		function formatTitle(cellValue, options, rowObject) {				
			var html = '';
			html = "<a href='javascript:;' onclick='doView("+options.rowId+")'>" + cellValue + "</a>";				
			return html;
		}
	    
		//加载表格数据
		var $mygrid = jQuery("#listMeeting").jqGrid({
            url:'/meeting/meetInfor.do?method=list',
            //rownumbers: true,	//是否显示序号
            datatype: "json",   //从后台获取的数据类型              
           	autowidth: true,	//是否自适应宽度
			//height: "auto",
            height:document.documentElement.clientHeight-140,
            colNames:['Id','会议名称', '与会人员', '会议时间', '会议地点',' 创建时间'],//表的第一行标题栏
            //以下为每列显示的具体数据
            colModel:[
                {name:'meetId',index:'meetId', width:0, search:false, hidden:true, key:true},            
                {name:'meetName',index:'meetName', width:100, sortable:true, formatter:formatTitle,sorttype:"string"},
                {name:'attendInfor',index:'attendInfor', width:40,align:'left'},
                {name:'meetDate',index:'meetDate', width:80,align:'center'},
                {name:'meetRoom',index:'meetRoom', width:60,align:'center'},
                {name:'createTimeStr',index:'createTimeStr', width:40,align:'center'}
            ],
//            caption: "会务安排",
            sortname: 'meetId', //默认排序的字段
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
            pager: "#pagerMeeting"	//分页工具栏
            //caption: "用户信息"	//表头
       	}).navGrid('#pagerMeeting',{edit:false,add:false,del:false,search:false}).closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
	
		//自定义按钮
		jQuery("#listMeeting").jqGrid('navButtonAdd','#pagerMeeting', {
			caption:"新增", title:"点击新增信息", buttonicon:'ui-icon-plusthick', onClickButton: addMeeting
		});
		
		jQuery("#listMeeting").jqGrid('navButtonAdd','#pagerMeeting', {
			caption:"<span style='color: red;'>批量删除</span>", title:"点击批量删除", buttonicon:'ui-icon-closethick', onClickButton: deleteMeeting
		});
		
		jQuery("#listMeeting").jqGrid('navButtonAdd','#pagerMeeting', {
			caption:"查询", title:"点击查询", buttonicon:'ui-icon-search', onClickButton: openMultipleSearchDialog
		});
		
		//删除数据
		function deleteMeeting(){
		
		doDelete("/meeting/meetInfor.do?method=delete","listMeeting");
			
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
		
		/** 查询条件中的部门,班组,用户下拉联动 */
		//部门信息初始化
		$('#departmentId').selectInit();
		
		//加载部门及联动信息		
		$.loadDepartments("departmentId", null, "authorId");
		/** ******** */
		
	</script>