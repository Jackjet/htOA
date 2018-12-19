<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<script type="text/javascript">
	//初始化列表和查询窗口Id
	var multiSearchParamsLog = new Array();
	multiSearchParamsLog[0] = "#listLoginLog";			//列表Id
	multiSearchParamsLog[1] = "#multiSearchDialog";//查询模态窗口Id
	//新增
	function addLoginLog(){
		
		window.open("/extend/loginLog.do?method=edit", "_blank");
	}
	//修改
	function editLoginLog(rowId){
		window.open("/extend/loginLog.do?method=edit&logId="+rowId, "_blank");
	}
</script>
<!--<script src="<c:url value="/"/>js/multisearch.js"></script> 加载模态多条件查询相关js-->
<script type="text/javascript">
			
		/** 自定义多条件查询 */
		//初始化查询窗口
		function initSearchDialogLog() {
		    $(multiSearchParamsLog[1]).dialog({
		        autoOpen: false,       
		        modal: true,   
		        resizable: true,       
		        width: 350,   
		        title: "多条件查询",   
		        buttons: {   
		            "查询": multipleSearchLog,
		            "重置": clearSearchLog
		        }   
		    });
		}
		
		//打开查询窗口
	    function openMultipleSearchDialogLog() {
		    //初始化窗口
		    initSearchDialogLog();
		    
		    $(multiSearchParamsLog[1]).dialog("open");
		}
		
		//多条件查询
		function multipleSearchLog() {
		    var rules = "";   
		    $("tbody tr", multiSearchParamsLog[1]).each(function(i){    	//(1)从multipleSearchDialog对话框中找到各个查询条件行   
		    	//alert(i);
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
		       //alert(filtersStr);
		    var postData = $(multiSearchParamsLog[0]).jqGrid("getGridParam", "postData");   
		       
		    $.extend(postData, {filters: filtersStr});   				//(8)将filters参数串加入postData选项
		       
		    $(multiSearchParamsLog[0]).jqGrid("setGridParam", {  
		        search: true    										//(9)将jqGrid的search选项设为true   
		    }).trigger("reloadGrid", [{page:1}]);   					//(10)重新载入Grid表格,且返回第一页  
		       
		    $(multiSearchParamsLog[1]).dialog("close");
		}
		
		//重置查询条件
		function clearSearchLog() {
		    var sdata = {
		        searchString: ""	//将查询数据置空
		    };   
		       
		    var postData = $("#gridTable").jqGrid("getGridParam", "postData");   
		       
		    $.extend(postData, sdata);   
		       
		    $(multiSearchParamsLog[0]).jqGrid("setGridParam", {   
		        search: false  
		    }).trigger("reloadGrid", [{page:1}]);   
		       
		    resetSearchDialogLog(); 
		}
		var resetSearchDialogLog = function() {
		    $("select",multiSearchParamsLog[1]).val("");   
		    $(":text",multiSearchParamsLog[1]).val("");   
		}

		/** ********** */
</script>

<title>Login Log</title>
  		<div>
			<table id="listLoginLog"></table> <!-- 信息列表 -->
			<div id="pagerLoginLog"></div> <!-- 分页 -->
		</div>
		
		<!-- 查询框 -->
		<div id="multiSearchDialog" style="display: none;">  
		    <table>  
		        <tbody>  
		            <tr>  
			           <td>  
			              <input type="hidden" class="searchField" value="userName"/>用户名：
			              <input type="hidden" class="searchOper" value="cn"/>
			           </td>  
			           <td>  
			              <input type="text" class="searchString"/>  
			           </td>  
			        </tr>
			        
			        <tr>  
			           <td>  
			              <input type="hidden" class="searchField" value="logFrom"/>登录途径：
			              <input type="hidden" class="searchOper" value="eq"/>
			           </td>  
			           <td>  
			               <select class="searchString">
			               	<option value="">--全部--</option>
			               	<option value="pc">pc</option>
			               	<option value="app">app</option>
			               </select>
			           </td>  
			        </tr>
			        
			        <tr>  
			           <td>  
			              <input type="hidden" class="searchField" value="sucTag"/>登录成功：
			              <input type="hidden" class="searchOper" value="eq"/>
			           </td>  
			           <td>  
			               <select class="searchString">
			               	<option value="">--全部--</option>
			               	<option value="1">是</option>
			               	<option value="0">否</option>
			               </select>
			           </td>  
			        </tr>
	
					<tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="logTime"/>起始时间：
		                    <input type="hidden" class="searchOper" value="ge"/>
		                </td>  
		                <td>  
		                    <input  class="searchString"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd 00:00:00'})" readonly="true"/>
		                </td>  
		            </tr>
		            
		            <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="logTime"/>结束时间：
		                    <input type="hidden" class="searchOper" value="le"/>
		                </td>  
		                <td>  
		                    <input  class="searchString"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd 23:59:59'})" readonly="true"/>
		                </td>  
		            </tr>
		        </tbody>  
		    </table>  
		</div>
		
		<script type="text/javascript"> 		
			
			//自定义操作栏的显示内容
		    function formatSuc(cellvalue, options, rowdata) {
	           var returnStr = "";
	           if(cellvalue == '0'){
	        	   returnStr += "<font color=yellow>否</font>";
	           }else if(cellvalue == '1'){
	        	   returnStr += "<font color=white>是</font>";
	           }
	           //returnStr += "<a href='javascript:;' onclick='editLoginLog("+options.rowId+")'>"+cellvalue+"</a>";
	           return returnStr;
		    }
		    
		    
			//加载表格数据
			var $mygrid = jQuery("#listLoginLog").jqGrid({
                url:'/extend/loginLog.do?method=list',
                //rownumbers: true,	//是否显示序号
                datatype: "json",   //从后台获取的数据类型              
               	autowidth: true,	//是否自适应宽度
				//height: "auto",
                height:document.documentElement.clientHeight-240,
                colNames:['Id', '用户名', '登录途径', '是否成功', '登录时间'],//表的第一行标题栏
                //以下为每列显示的具体数据
                colModel:[
                    {name:'logId',index:'logId', width:0, sorttype:"int", search:false, key:true, hidden:true},
                    {name:'userName',index:'userName',align:"center", width:50, sortable:true, sorttype:"string"},
                    {name:'logFrom',index:'logFrom', width:40, align:'center'},
                    {name:'sucTag',index:'sucTag', width:30, align:'center',formatter:formatSuc},
                    {name:'logTime',index:'logTime',/* width:30,*/ align:'center'}
                ],
                //caption: "登录日志",
                sortname: 'logTime',//默认排序的字段
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
                pager: "#pagerLoginLog"	//分页工具栏
                //caption: "用户信息"	//表头
        }).navGrid('#pagerLoginLog',{edit:false,add:false,del:false,search:false}).closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
        //}).navGrid('#pagerLoginLog',{edit:false,add:false,del:false}).searchGrid({multipleSearch:true,autoOpen:false});
		
		//显示各个栏目上的搜索栏
		//$('#listLoginLog').jqGrid('filterToolbar','');
		
		//自定义按钮
		/*jQuery("#listLoginLog").jqGrid('navButtonAdd','#pagerLoginLog', {
			caption:"新增", title:"点击新增信息", buttonicon:'ui-icon-plusthick', onClickButton: addLoginLog
		});
		jQuery("#listLoginLog").jqGrid('navButtonAdd','#pagerLoginLog', {
			caption:"<span style='color: red;'>批量删除</span>", title:"点击批量删除", buttonicon:'ui-icon-closethick', onClickButton: deleteLoginLog
		});*/
		jQuery("#listLoginLog").jqGrid('navButtonAdd','#pagerLoginLog', {
			caption:"查询", title:"点击查询", buttonicon:'ui-icon-search', onClickButton: openMultipleSearchDialogLog
		});
		
		jQuery("#listLoginLog").jqGrid('navButtonAdd','#pagerLoginLog', {
			caption:"导出Excel", title:"点击导出到Excel", buttonicon:'ui-icon-calculator', onClickButton: exportExcel
		});
		
		
		
		/********导出excel*********/
		function exportExcel(){
			var rules = "";   
			var param0 = "#listLoginLog";
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
		    //alert(filtersStr);
		    var url = "/extend/loginLog.do?method=expertExcel&_search=true&page=1&rows=1000000&sidx=logTime&sord=desc&filters="+filtersStr;
		    window.location.href=url;
		}
		
	</script>