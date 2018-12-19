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
	<script src="<c:url value="/"/>js/changeclass.js"></script> <!--用于改变页面样式-->
	
	<%--<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />--%>
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>css/base/jquery-ui-1.9.2.custom.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/tabstyle.css" />
	
	<script type="text/javascript">     
		//初始化列表和查询窗口Id
		var multiSearchParams = new Array();
		$.initReceive = function() {
			multiSearchParams[0] = "#listFlow";				//通讯录列表Id
			multiSearchParams[1] = "#multiSearchDialogFlow";	//通讯录查询模态窗口Id
		}
		$.initReceive();		
	</script>
	<script type="text/javascript">
	
	//新增
	function addInfor(){
		window.location.href = "/workFlowDefinition.do?method=edit&ac=edit&flowId=";
		//window.showModalDialog("/document/document.do?method=edit&categoryId=",'',"dialogWidth:800px;dialogHeight:600px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
	}
	
	//修改
	function editInfor(rowId){
		window.location.href = "/workFlowDefinition.do?method=edit&ac=edit&flowId="+rowId;
		//window.showModalDialog("/document/document.do?method=edit&rowId="+rowId,'',"dialogWidth:800px;dialogHeight:600px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
	}
	
	//批量删除
	function deleteInfor(){
		doDelete("/workFlowDefinition.do?method=delete","listFlow");
	}
	
	//获取主办人信息
	$.myLoadUsers = function(url,id) {
		$.getJSON(url,function(data) {
			if (data != null) {
				$.each(data._Users, function(i, n) {
					$("#"+id).append("<option value='"+ n.person.personId + "'>" + n.person.personName + "</option>");
				});
			}
		});
	}
</script>
<body style="border:0px solid #0DE8F5;border-radius: 5px">
 		<div style="margin-top:0px;">
		<table id="listFlow" style="width:99%"></table>
		<div id="pagerFlow"></div>
	</div>

	<!-- 查询框 -->
	<div id="multiSearchDialogFlow" style="display: none;">  
	    <table>  
	        <tbody>  
	            <tr>  
	                <td>  
	                    <input type="hidden" class="searchField" value="flowName"/>流程名称：
	                    <input type="hidden" class="searchOper" value="cn"/>
	                </td>  
	                <td>  
	                    <input type="text" class="searchString"/>  
	                </td>  
	            </tr>  
	            
	            <tr>  
	                <td>  
	                    <input type="hidden" class="searchField" value="status"/>状态：
	                    <input type="hidden" class="searchOper" value="eq"/>
	                </td>  
	                <td>  
	                    <select class="searchString">
	                    	<option value="">--请选择--</option>
	                    	<option value="0">停用</option>
	                    	<option value="1">启用</option>
	                    </select>
	                </td>  
	            </tr>
	            
	            <tr>  
	                <td>  
	                    <input type="hidden" class="searchField" value="flowType"/>流程类型：
	                    <input type="hidden" class="searchOper" value="eq"/>
	                </td>  
	                <td>  
	                    <select class="searchString">
	                    	<option value="">--请选择--</option>
	                    	<option value="0">固定</option>
	                    	<option value="1">人工</option>
	                    </select>
	                </td>  
	            </tr>	            		          
	            
	            <tr>  
	                <td>  
	                    <input type="hidden" class="searchField" value="charger.person.personId"/>主办人：
	                    <input type="hidden" class="searchOper" value="eq"/>
	                </td>  
	                <td>  
	                    <select class="searchString" id="personId">
	                    </select>
	                </td>  
	            </tr>  
	            
	            <tr>  
	                <td>  
	                    <input type="hidden" class="searchField" value="transType"/>跳转类型：
	                    <input type="hidden" class="searchOper" value="eq"/>
	                </td>  
	                <td>  
	                    <select class="searchString">
	                    	<option value="">--请选择--</option>
	                    	<option value="1">岗位</option>
	                    	<option value="2">部门</option>
	                    </select>
	                </td>  
	            </tr>
	        </tbody>  
	    </table>  
	</div>
</body>
	<script src="<c:url value="/"/>js/multisearch.js"></script> <!--加载模态多条件查询相关js-->
	<script type="text/javascript"> 		
	   
	   	//自定义操作栏的显示内容
		    function formatOperation(cellvalue, options, rowdata) {
	           var returnStr = "<a href='javascript:;' onclick='editInfor("+options.rowId+")'>[修改]</a>";
	           //returnStr += " <a href='javascript:;' onclick='doAuthorize("+options.rowId+")'>[授权]</a>";
	           //  <a href='javascript:;' onclick='viewInfor("+options.rowId+")'>[查看]</a>
	           return returnStr;
		    }
		    
		    //自定义显示状态内容
		    function formatSta(cellvalue) {
	           var returnStr;
	           if (cellvalue==1) {
	              returnStr = "启用";
	           }else if(cellvalue==0) {
	              returnStr = "停用";
	           }
	           return returnStr;
		    }
		    
		    //自定义显示流程类型内容
		    function formatType(cellvalue) {
	           var returnStr;
	           if (cellvalue==1) {
	              returnStr = "人工";
	           }else if(cellvalue==0) {
	              returnStr = "固定";
	           }
	           return returnStr;
		    }
		     //自定义显示跳转类型内容
		     /**
		    function formatTransType(cellvalue) {
	           var returnStr;
	           if (cellvalue==1) {
	              returnStr = "岗位";
	           }else if(cellvalue==2) {
	              returnStr = "部门";
	           }else {
	           	  returnStr = "";
	           }
	           return returnStr;
		    } */
			
			//标题查看链接
			function formatTitle(cellValue, options, rowObject) {				
				var html = '';
				//window.location.href = "/workFlowDefinition.do?method=edit&flowId="+rowId;
				html = "<a href='/workFlowDefinition.do?method=edit&flowId="+options.rowId+"&ac=view'>" + cellValue + "</a>";				
				return html;
			}
			
			//自定义模板显示
		    function formatTemplate(cellValue, options, rowObject) {				
				if (cellValue != null && cellValue != '') {
					var pathArray = cellValue.split('/');
					return pathArray[pathArray.length-1];
				}else {
					return '';
				}
			}
	   	
	   	//加载表格数据
		var $mygrid = jQuery("#listFlow").jqGrid({ 
            url:'/workFlowDefinition.do?method=listFlowDefinition',
            //rownumbers: true,
            datatype: "json",                
           	autowidth: true,
            height:document.documentElement.clientHeight-150,
            colNames:['Id','流程名称', '状态','流程分类','流程类型','主办人','流程模板','操作'],
            colModel:[
                {name:'flowId',index:'flowId',width:0, sorttype:"int", key:true, hidden:true },                    
                {name:'flowName',index:'flowName',align:'left', formatter:formatTitle},                    
                {name:'status',index:'status',width:20, align:'center',formatter:formatSta},
                {name:'categoryName',index:'categoryName',align:'center', width:20},
                {name:'flowType',index:'flowType',align:'center', width:20,formatter:formatType},
                {name:'charger.person.personName',index:'charger.person.personName', align:'center',width:20},
                {name:'template',index:'template',align:'left',width:40,formatter:formatTemplate},
                {name:'operate',align:'center', width:20,formatter:formatOperation}
                
            ],
//            caption: "工作流流程定义",
            sortname: 'flowId',
            multiselect: true,	// 是否支持多选,可用于批量删除
            sortorder: 'asc',
            viewrecords: true,
            rowNum: 10,
            rowList: [10,20,30],
            scroll: false, 
            scrollrows: false,                          
            jsonReader:{
               repeatitems: false
            },         
            pager: "#pagerFlow"
	     }).navGrid('#pagerFlow',{edit:false,add:false,del:false,search:false}).closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
	        
	     $("gbox_listFlow").css("width","100%");
		
		 //自定义按钮
		 jQuery("#listFlow").jqGrid('navButtonAdd','#pagerFlow', {
			caption:"新增", title:"点击新增流程", buttonicon:'ui-icon-plusthick', onClickButton: addInfor
		});
		jQuery("#listFlow").jqGrid('navButtonAdd','#pagerFlow', {
				caption:"<span style='color: red;'>批量删除</span>", title:"点击批量删除", buttonicon:'ui-icon-closethick', onClickButton: deleteInfor
			});
		 jQuery("#listFlow").jqGrid('navButtonAdd','#pagerFlow', {
			caption:"查询", title:"点击查询流程", buttonicon:'ui-icon-search', onClickButton: openMultipleSearchDialog
		 });
		
   	//获取查询条件

	//主办人信息初始化
	$('#personId').html("<option value=''>--请选择--</option>");
	
	var url = "/core/systemUserInfor.do?method=getUsers&departmentId=";
	$.myLoadUsers(url,"personId");
	       	
</script>

