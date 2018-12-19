<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<script type="text/javascript">
	//初始化列表和查询窗口Id
	var multiSearchParamsApp = new Array();
	multiSearchParamsApp[0] = "#listModuleLog";			//列表Id
	multiSearchParamsApp[1] = "#appMultiSearchDialog";//查询模态窗口Id
	//新增
	function addModuleLog(){
		
		window.open("/extend/appModuleLog.do?method=edit", "_blank");
	}
	//修改
	function editModuleLog(rowId){
		window.open("/extend/appModuleLog.do?method=edit&logId="+rowId, "_blank");
	}
</script>
<!--<script src="<c:url value="/"/>js/multisearch.js"></script> 加载模态多条件查询相关js-->
<script type="text/javascript">
			
		/** 自定义多条件查询 */
		//初始化查询窗口
		function initSearchDialogApp() {
		    $(multiSearchParamsApp[1]).dialog({
		        autoOpen: false,       
		        modal: true,   
		        resizable: true,       
		        width: 350,   
		        title: "多条件查询",   
		        buttons: {   
		            "查询": multipleSearchApp,
		            "重置": clearSearchApp
		        }   
		    });
		}
		
		//打开查询窗口
	    function openMultipleSearchDialog() {
		    //初始化窗口
		    initSearchDialogApp();
		    
		    $(multiSearchParamsApp[1]).dialog("open");
		}
		
		//多条件查询
		function multipleSearchApp() {
		    var rules = "";   
		    $("tbody tr", multiSearchParamsApp[1]).each(function(i){    	//(1)从multipleSearchDialog对话框中找到各个查询条件行   
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
		       
		    var postData = $(multiSearchParamsApp[0]).jqGrid("getGridParam", "postData");   
		       
		    $.extend(postData, {filters: filtersStr});   				//(8)将filters参数串加入postData选项
		       
		    $(multiSearchParamsApp[0]).jqGrid("setGridParam", {  
		        search: true    										//(9)将jqGrid的search选项设为true   
		    }).trigger("reloadGrid", [{page:1}]);   					//(10)重新载入Grid表格,且返回第一页  
		       
		    $(multiSearchParamsApp[1]).dialog("close");
		}
		
		//重置查询条件
		function clearSearchApp() {
		    var sdata = {
		        searchString: ""	//将查询数据置空
		    };   
		       
		    var postData = $("#gridTable").jqGrid("getGridParam", "postData");   
		       
		    $.extend(postData, sdata);   
		       
		    $(multiSearchParamsApp[0]).jqGrid("setGridParam", {   
		        search: false  
		    }).trigger("reloadGrid", [{page:1}]);   
		       
		    resetSearchDialogApp(); 
		}
		var resetSearchDialogApp = function() {
		    $("select",multiSearchParamsApp[1]).val("");   
		    $(":text",multiSearchParamsApp[1]).val("");   
		}

		/** ********** */
</script>

<title>App Module Log</title>
  		<div>
			<table id="listModuleLog"></table> <!-- 信息列表 -->
			<div id="pagerModuleLog"></div> <!-- 分页 -->
		</div>
		
		<!-- 查询框 -->
		<div id="appMultiSearchDialog" style="display: none;">  
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
			              <input type="hidden" class="searchField" value="platform"/>平台：
			              <input type="hidden" class="searchOper" value="eq"/>
			           </td>  
			           <td>  
			               <select class="searchString">
			               	<option value="">--全部--</option>
			               	<option value="android">android</option>
			               	<option value="ios">ios</option>
			               </select>
			           </td>  
			        </tr>
			        
			        <tr>  
			           <td>  
			              <input type="hidden" class="searchField" value="moduleName"/>模块：
			              <input type="hidden" class="searchOper" value="eq"/>
			           </td>  
			           <td>  
			               <select class="searchString">
			               	<option value="">--全部--</option>
			               	<option value="待办事项">待办事项</option>
			               	<option value="工作跟踪列表">工作跟踪列表</option>
			               	<option value="工作跟踪审批">工作跟踪审批</option>
			               	<option value="公司公告">公司公告</option>
			               	<option value="公司会议">公司会议</option>
			               	<option value="发文管理">发文管理</option>
			               	<option value="合同审批">合同审批</option>
			               	<option value="内部报告">内部报告</option>
			               	<option value="制度评审">制度评审</option>
			               	<option value="收文管理">收文管理</option>
			               	<option value="通讯录">通讯录</option>
			               	<option value="主题活动">主题活动</option>
			               	<option value="海通简报">海通简报</option>
			               	<option value="管理工作">管理工作</option>
			               	<option value="市场信息">市场信息</option>
			               	<option value="党群园地">党群园地</option>
			               	<option value="员工手册">员工手册</option>
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
	        	   returnStr += "<font color=red>否</font>";
	           }else if(cellvalue == '1'){
	        	   returnStr += "<font color=blue>是</font>";
	           }
	           //returnStr += "<a href='javascript:;' onclick='editModuleLog("+options.rowId+")'>"+cellvalue+"</a>";
	           return returnStr;
		    }
		    
		    
			//加载表格数据
			var $mygrid = jQuery("#listModuleLog").jqGrid({
                url:'/extend/appModuleLog.do?method=list',
                //rownumbers: true,	//是否显示序号
                datatype: "json",   //从后台获取的数据类型              
               	autowidth: true,	//是否自适应宽度
				//height: "auto",
                height:document.documentElement.clientHeight-240,
                colNames:['Id', '模块', '平台', '用户名', '登录时间'],//表的第一行标题栏
                //以下为每列显示的具体数据
                colModel:[
                    {name:'logId',index:'logId', width:0, sorttype:"int", search:false, key:true, hidden:true},
                    {name:'moduleName',index:'userName',align:"center", width:30, sortable:true, sorttype:"string"},
                    {name:'platform',index:'platform', width:20, align:'center'},
                    {name:'userName',index:'userName', width:20, align:'center'},
                    {name:'logTime',index:'logTime', width:30, align:'center'}
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
                pager: "#pagerModuleLog"	//分页工具栏
                //caption: "用户信息"	//表头
        }).navGrid('#pagerModuleLog',{edit:false,add:false,del:false,search:false}).closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
        //}).navGrid('#pagerModuleLog',{edit:false,add:false,del:false}).searchGrid({multipleSearch:true,autoOpen:false});
		
		//显示各个栏目上的搜索栏
		//$('#listModuleLog').jqGrid('filterToolbar','');
		
		//自定义按钮
		/*jQuery("#listModuleLog").jqGrid('navButtonAdd','#pagerModuleLog', {
			caption:"新增", title:"点击新增信息", buttonicon:'ui-icon-plusthick', onClickButton: addModuleLog
		});
		jQuery("#listModuleLog").jqGrid('navButtonAdd','#pagerModuleLog', {
			caption:"<span style='color: red;'>批量删除</span>", title:"点击批量删除", buttonicon:'ui-icon-closethick', onClickButton: deleteModuleLog
		});*/
		jQuery("#listModuleLog").jqGrid('navButtonAdd','#pagerModuleLog', {
			caption:"查询", title:"点击查询", buttonicon:'ui-icon-search', onClickButton: openMultipleSearchDialog
		});
		
		jQuery("#listModuleLog").jqGrid('navButtonAdd','#pagerModuleLog', {
			caption:"导出Excel", title:"点击导出到Excel", buttonicon:'ui-icon-calculator', onClickButton: exportAppExcel
		});
		
		
		
		/********导出excel*********/
		function exportAppExcel(){
			var rules = "";   
			var param0 = "#listModuleLog";
			var param1 = "#appMultiSearchDialog";
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
		    
		    var url = "/extend/appModuleLog.do?method=expertExcel&_search=true&page=1&rows=1000000&sidx=logTime&sord=desc&filters="+filtersStr;
		    //alert(url);
		    window.location.href=url;
		}
		
	</script>