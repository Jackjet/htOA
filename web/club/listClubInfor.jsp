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
	multiSearchParams[0] = "#listClubInfor";			//列表Id
	multiSearchParams[1] = "#multiSearchDialog";//查询模态窗口Id


	//新增
	function addClubInfor(){
		/** var refresh = window.showModalDialog("/club/clubInfor.do?method=edit",null,"dialogWidth:900px;dialogHeight:700px;center:Yes;dialogTop: 100px; dialogLeft: 200px;");
		if(refresh == "Y") {
			self.location.reload();
		} */
		window.open("/club/clubInfor.do?method=edit", "_blank");
	}
	//查看
	function doView(rowId){
		/** var refresh = window.showModalDialog("/club/clubInfor.do?method=viewClubInfor&rowId="+rowId,'',"dialogWidth:900px;dialogHeight:700px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		if(refresh == "Y") {
			self.location.reload();
		} */
		window.open("/club/clubInfor.do?method=viewClubInfor&rowId="+rowId, "_blank");
	}
</script>

<title>俱乐部活动</title>
<body style="border:1px solid #0DE8F5;border-radius: 5px">
	<div>
		<table id="listClubInfor"></table> <!-- 信息列表 -->
		<div id="pagerClubInfor"></div> <!-- 分页 -->
	</div>
	
	<!-- 查询框 -->
	<div id="multiSearchDialog" style="display: none;">  
	    <table>  
	        <tbody>  
	            <tr>  
		           <td>  
		              <input type="hidden" class="searchField" value="actTitle"/>活动名称：
		              <input type="hidden" class="searchOper" value="cn"/>
		           </td>  
		           <td>  
		              <input type="text" class="searchString"/>  
		           </td>  
		        </tr>
		        
		        <tr>  
		          <td>  
		              <input type="hidden" class="searchField" value="actItem"/>活动项目：
		              <input type="hidden" class="searchOper" value="cn"/>
		           </td>  
		           <td>  
		              <input type="text" class="searchString"/>  
		           </td>  
		        </tr>
		        
		        <tr>  
		          <td>  
		              <input type="hidden" class="searchField" value="league"/>社团：
		              <input type="hidden" class="searchOper" value="cn"/>
		           </td>  
		           <td>  
		              <select class="searchString" id="selLeague"> 
	                    	<option value=''>--选择社团--</option>
	                    	<option value="“海之声”合唱团">“海之声”合唱团</option>
							<option value="博雅读书社">博雅读书社</option>
							<option value="光影流年">光影流年</option>
							<option value="酷跑团">酷跑团</option>
							<option value="轻“羽”飞扬">轻“羽”飞扬</option>
							<option value="勇“网”直前">勇“网”直前</option>
							<option value="“乒”博社">“乒”博社</option>
							<option value="奔跑吧，足球！">奔跑吧，足球！</option>
							<option value="力挽狂“篮”">力挽狂“篮”</option>
	                    </select>
		           </td>  
		        </tr>
		        
		        <tr>  
	                <td>  
	                    <input type="hidden" class="searchField" value="creater.personId"/>发起人：
	                    <input type="hidden" class="searchOper" value="eq"/>
	                </td>  
	                <td>  
	                	<select id="createDepartmentId"></select>
	                    <select  class="searchString" id="createrId">		                    	
	                    </select>
	                </td>  
	            </tr>  
		        
		        <tr>  
	                <td>  
	                    <input type="hidden" class="searchField" value="manager.personId"/>管理员：
	                    <input type="hidden" class="searchOper" value="eq"/>
	                </td>  
	                <td>  
	                	<select id="manageDepartmentId"></select>
	                    <select  class="searchString" id="managerId">		                    	
	                    </select>
	                </td>  
	            </tr> 
		        
		        <tr>  
	                <td nowrap>  
	                    <input type="hidden" class="searchField" value="actTime"/>活动开始时间起：
	                    <input type="hidden" class="searchOper" value="gt"/>
	                </td>  
	                <td>  
	                    <input  class="searchString"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly="true"/>
	                </td>  
	            </tr>
	            
	            <tr>  
	                <td>  
	                    <input type="hidden" class="searchField" value="actTime"/>活动开始时间止：
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
	<script src="<c:url value="/"/>js/multisearch.js"></script> <!--加载模态多条件查询相关js-->		
	<script type="text/javascript"> 		
		
		//自定义操作栏的显示内容
	  
		//显示标题
		function formatTitle(cellValue, options, rowObject) {				
			var html = '';
			html = "<a href='javascript:;' onclick='doView("+options.rowId+")'>" + cellValue + "</a>";				
			return html;
		}
		
		//显示状态
		function formatStatus(cellValue, options, rowObject) {				
			var html = '<font color=red>';
			if(cellValue == 0){
				html += "暂停";
			}
			if(cellValue == 1){
				html += "未开始报名";
			}
			if(cellValue == 2){
				html += "报名中";
			}
			if(cellValue == 3){
				html += "报名截止，未开始";
			}
			if(cellValue == 4){
				html += "开始签到";
			}
			if(cellValue == 5){
				html += "活动结束";
			}
			html += "</font>";	
			return html;
		}
	    
		//加载表格数据
		var $mygrid = jQuery("#listClubInfor").jqGrid({
            url:'/club/clubInfor.do?method=list',
            //rownumbers: true,	//是否显示序号
            datatype: "json",   //从后台获取的数据类型              
           	autowidth: true,	//是否自适应宽度
			//height: "auto",
            height:document.documentElement.clientHeight-140,
            colNames:['Id','活动名称', '社团','报名开始日期','报名截止日期','活动开始时间', '活动结束时间',  '活动地点','发起人','管理员',' 创建时间','状态'],//表的第一行标题栏
            //以下为每列显示的具体数据
            colModel:[
                {name:'actId',index:'actId', width:0, search:false, hidden:true, key:true},            
                {name:'actTitle',align:'center',index:'actTitle', width:120, sortable:true, formatter:formatTitle,sorttype:"string"},
                {name:'league',index:'league', width:100,align:'center'},
                {name:'beginSignDateStr',index:'beginSignDateStr', width:80,align:'center'},
                {name:'cutDateStr',index:'cutDateStr', width:80,align:'center'},
                {name:'actTimeStr',index:'actTimeStr', width:130,align:'center'},
                {name:'toTimeStr',index:'toTimeStr', width:130,align:'center'},
                {name:'actPlace',index:'actPlace', width:70,align:'center'},
                {name:'createrName',index:'createrName', width:60,align:'center'},
                {name:'managerName',index:'managerName', width:60,align:'center'},
                {name:'createTimeStr',index:'createTimeStr', width:70,align:'center'},
                {name:'status',index:'status',align:'center',formatter:formatStatus}
            ],
//            caption: "俱乐部活动",
            sortname: 'actId', //默认排序的字段
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
            pager: "#pagerClubInfor"	//分页工具栏
            //caption: "用户信息"	//表头
       	}).navGrid('#pagerClubInfor',{edit:false,add:false,del:false,search:false}).closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
	
		//自定义按钮
		jQuery("#listClubInfor").jqGrid('navButtonAdd','#pagerClubInfor', {
			caption:"新增", title:"点击新增信息", buttonicon:'ui-icon-plusthick', onClickButton: addClubInfor
		});
		
		jQuery("#listClubInfor").jqGrid('navButtonAdd','#pagerClubInfor', {
			caption:"<span style='color: red;'>批量删除</span>", title:"点击批量删除", buttonicon:'ui-icon-closethick', onClickButton: deleteClubInfor
		});
		
		jQuery("#listClubInfor").jqGrid('navButtonAdd','#pagerClubInfor', {
			caption:"查询", title:"点击查询", buttonicon:'ui-icon-search', onClickButton: openMultipleSearchDialog
		});
		
		jQuery("#listClubInfor").jqGrid('navButtonAdd','#pagerClubInfor', {
			caption:"导出Excel", title:"点击导出到Excel", buttonicon:'ui-icon-calculator', onClickButton: exportExcel
		});
		
		//删除数据
		function deleteClubInfor(){
		
			doDelete("/club/clubInfor.do?method=delete","listClubInfor");
			
		}
		
		/********导出excel*********/
		function exportExcel(){
			var yes = window.confirm("数据较多时，导出所需时间较长，确定要导出数据吗？");
			if (yes) {
				var rules = "";   
				var param0 = "#listClubInfor";
				var param1 = "#multiSearchDialog";
			    $("tbody tr", param1).each(function(i){    	//(1)从multipleSearchDialog对话框中找到各个查询条件行   
			        var searchField = $(".searchField", this).val();    	//(2)获得查询字段
			        var searchOper = $(".searchOper", this).val();  		//(3)获得查询方式   
			        var searchString = $(".searchString", this).val();  	//(4)获得查询值   
			        
			        if(searchField && searchOper && searchString) { 		//(5)如果三者皆有值且长度大于0，则将查询条件加入rules字符串   
			            rules += ',{"field":"' + searchField + '","op":"' + searchOper + '","data":"' + searchString + '"}';   
			        }   
			    });   
			    if(rules) { 
			        rules = rules.substring(1);								//(6)如果rules不为空，且长度大于0，则去掉开头的逗号
			    }   
			       
			    var filtersStr = '{"groupOp":"AND","rules":[' + rules + ']}';//(7)串联好filtersStr字符串
			    
			    var url = "/club/clubInfor.do?method=expertExcel&_search=true&page=1&rows=100000&sidx=league&sord=asc&filters="+filtersStr;
			    window.location.href=url;
			}
			
		}
		
		/** 查询条件中的部门,班组,用户下拉联动 */
		//部门信息初始化
		$('#createDepartmentId').selectInit();
		$('#manageDepartmentId').selectInit();
		
		//加载部门及联动信息		
		$.loadDepartments("createDepartmentId", null, "createrId");
		$.loadDepartments("manageDepartmentId", null, "managerId");
		
		
	</script>