<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<script src="<c:url value="/"/>datePicker/WdatePicker.js" type="text/javascript"></script>
<script src="<c:url value="/"/>components/jquery-1.4.2.js" type="text/javascript"></script> <!--jquery包-->
<script src="<c:url value="/"/>components/jqgrid/js/jquery-ui-1.8.2.custom.min.js" type="text/javascript"></script><!--jquery ui-->
<script src="<c:url value="/"/>components/jquery.layout.js" type="text/javascript"></script><!--jquery 布局-->
<script src="<c:url value="/"/>components/jqgrid/js/grid.locale-cn.js" type="text/javascript"></script> <!--jqgrid 中文包-->
<script src="<c:url value="/"/>components/jqgrid/js/jquery.jqGrid.min.js" type="text/javascript"></script> <!--jqgrid 包-->

<script src="<c:url value="/"/>js/inc_javascript.js"></script>
<script src="<c:url value="/"/>js/commonFunction.js"></script>
<script src="<c:url value="/"/>js/changeclass.js"></script> <!--用于改变页面样式-->

<%--<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />--%>
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>css/base/jquery-ui-1.9.2.custom.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/tabstyle.css" />

<script type="text/javascript">
    var multiSearchParams = new Array();
    multiSearchParams[0] = "#listOpenCalendar";			//列表Id
    multiSearchParams[1] = "#multiSearchDialogOpen";//查询模态窗口Id
	//查看
	function doView(rowId){
		//window.showModalDialog("/personal/personalJobInfor.do?method=viewCalendar&rowId="+rowId+"&scheduleType=2",'',"dialogWidth:700px;dialogHeight:400px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		window.open("/personal/personalJobInfor.do?method=viewCalendar&rowId="+rowId+"&scheduleType=2","_blank");
	}
	
</script>

<title>自我制定工作</title>
<body style="border:1px solid #0DE8F5;border-radius: 5px">
	  	<div>
			<table id="listOpenCalendar"></table> <!-- 信息列表 -->
			<div id="pagerOpenCalendar"></div> <!-- 分页 -->
		</div>
		
			<!-- 查询框 -->
		<div id="multiSearchDialogOpen" style="display: none;">  
		    <table>  
		        <tbody>  
		            <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="jobTitle"/>标题：
		                    <input type="hidden" class="searchOper" value="cn"/>
		                </td>  
		                <td>  
		                    <input type="text" class="searchString"/>  
		                </td>  
		            </tr>
		            
		            <tr>  
		                <td>  
		                    <input type="hidden" value="assigner.person.department.organizeId"/>所属部门：
		                    <input type="hidden" value="eq"/>
		                </td>  
		                <td>  
		                    <select id="openDepartmentId"></select>
		                </td>  
		            </tr>

					<tr>
		                <td>  
		                    <input type="hidden" class="searchField" value="assigner.personId"/>安排人：
		                    <input type="hidden" class="searchOper" value="eq"/>
		                </td>  
		                <td>  
		                    <select  class="searchString" id="openAssignerId"></select>
		                </td>  
		            </tr>
		            
		            <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="startTimeStr"/>起始时间：
		                    <input type="hidden" class="searchOper" value="ge"/>
		                </td>  
		                <td>  
		                    <input  class="searchString"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly="true"/>
		                </td>  
		            </tr>
		            
		            <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="endTimeStr"/>结束时间：
		                    <input type="hidden" class="searchOper" value="le"/>
		                </td>  
		                <td>  
		                    <input  class="searchString"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly="true"/>
		                </td>  
		            </tr>
		        </tbody>  
		    </table>  
		</div>
</body>
		<!-- ----- -->
		<script src="<c:url value="/"/>js/multisearch.js"></script><!--加载模态多条件查询相关js-->
		<script type="text/javascript"> 		
			
		   //显示标题
		   function formatTitle(cellValue, options, rowObject) {				
				var html = '';
				html = "<a href='javascript:;' onclick='doView("+options.rowId+")'>" + cellValue + "</a>";				
				return html;
			}
		    //自定义状态栏的显示内容
		    function formatStatus(cellvalue) {
	           var returnStr;
	           if (cellvalue == '0') {
	              returnStr = "正在进行";
		           }else if (cellvalue == '1') {
					returnStr = "<font color='gray'>暂时停止</font>";
	           }else if(cellvalue == '2'){
	           		returnStr = "已经完成";
	           }
	           return returnStr;
		    }
		     
			//加载表格数据
			var $mygrid = jQuery("#listOpenCalendar").jqGrid({
                url:'/personal/personalJobInfor.do?method=list&Type=2',
                //rownumbers: true,	//是否显示序号
                datatype: "json",   //从后台获取的数据类型              
               	autowidth: true,	//是否自适应宽度
				//height: "auto",
                height:document.documentElement.clientHeight-140,
                colNames:['Id', '标题','开始时间', '结束时间', '安排人', '状态'],//表的第一行标题栏
                //以下为每列显示的具体数据
                colModel:[
                    {name:'scheduleId',index:'scheduleId', width:0, sorttype:"int", search:false, key:true, hidden:true},
                   	{name:'jobTitle',index:'jobTitle', width:100, sortable:true, formatter:formatTitle,sorttype:"string"},
                    {name:'startTimeStr',index:'startTimeStr', width:40},
                    {name:'endTimeStr',index:'endTimeStr', width:40},
                    {name:'personName',index:'personName',align:'center', width:40},
                    {name:'status',index:'status', width:40, align:'center', formatter:formatStatus}
                ],
                sortname: 'scheduleId',//默认排序的字段
                sortorder: 'desc',	//默认排序形式:升序,降序
               	//multiselect: true,//是否支持多选,可用于批量删除
                viewrecords: true,	//是否显示数据的总条数(显示在右下角)
                rowNum: 10,			//每页显示的默认数据条数
                rowList: [10,20,30],//可选的每页显示的数据条数(显示在中间,下拉框形式)
                scroll: false, 		//是否采用滚动分页的形式
                scrollrows: false,	//当选中的行数据隐藏时,grid是否自动滚               
                jsonReader:{
                	repeatitems: false	//告诉JqGrid,返回的数据的标签是否是可重复的
                },         
                pager: "#pagerOpenCalendar"	//分页工具栏
                //caption: "用户信息"	//表头
        }).navGrid('#pagerOpenCalendar',{edit:false,add:false,del:false,search:false}).closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
        //}).navGrid('#pagerUser',{edit:false,add:false,del:false}).searchGrid({multipleSearch:true,autoOpen:false});
		
		//显示各个栏目上的搜索栏
		//$('#listUser').jqGrid('filterToolbar','');
		
		//自定义按钮
		jQuery("#listOpenCalendar").jqGrid('navButtonAdd','#pagerOpenCalendar', {
			caption:"查询", title:"点击查询", buttonicon:'ui-icon-search', onClickButton: openMultipleSearchDialog
		});
		
		/** 查询条件中的部门,用户下拉联动 */
		//部门信息初始化
		$('#openDepartmentId').selectInit();
		
		//加载部门及联动信息		
		$.loadDepartments("openDepartmentId", null, "openAssignerId");

		/** ********** */
		
	</script>