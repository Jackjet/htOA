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
	multiSearchParams[0] = "#listTopicInfor";			//列表Id
	multiSearchParams[1] = "#multiSearchDialog";//查询模态窗口Id


	//新增
	function addTopicInfor(){
		<%--var refresh = window.showModalDialog("/tpwj/topicInfor.do?method=edit&type=${param.type}",null,"dialogWidth:1000px;dialogHeight:600px;center:Yes;dialogTop: 100px; dialogLeft: 200px;");--%>
		<%--if(refresh == "refresh") {--%>
			<%--self.location.reload();--%>
		<%--} --%>
		window.open("/tpwj/topicInfor.do?method=edit&type=${param.type}", "_blank");
	}
	//修改
	function editTopicInfor(rowId){
		<%--var refresh = window.showModalDialog("/tpwj/topicInfor.do?method=edit&type=${param.type}&rowId="+rowId,null,"dialogWidth:1000px;dialogHeight:600px;center:Yes;dialogTop: 100px; dialogLeft: 200px;");--%>
		<%--if(refresh == "refresh") {--%>
			<%--self.location.reload();--%>
		<%--} --%>
        window.open("/tpwj/topicInfor.do?method=edit&type="+${param.type}+"&rowId="+rowId, "_blank");
	}
	
	//新增条目信息
	function editItemInfor(rowId){
		/*var refresh = window.showModalDialog("/tpwj/itemInfor.do?method=edit&topicId="+rowId,null,"dialogWidth:1000px;dialogHeight:600px;center:Yes;dialogTop: 100px; dialogLeft: 200px;");
		if(refresh == "refresh") {
			self.location.reload();
		}*/
		window.open("/tpwj/itemInfor.do?method=edit&topicId="+rowId, "_blank");
	}
	//查看
	function doView(rowId){
		/** var refresh = window.showModalDialog("/tpwj/topicInfor.do?method=viewTopicInfor&rowId="+rowId,'',"dialogWidth:900px;dialogHeight:700px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		if(refresh == "Y") {
			self.location.reload();
		} */
		window.open("/tpwj/topicInfor.do?method=viewTopicInfor&type=${param.type}&topicId="+rowId, "_blank");
	}
	//查看统计
	function viewCount(rowId){
		window.open("/tpwj/topicInfor.do?method=viewCount&type=${param.type}&topicId="+rowId, "_blank");
	}
	//按人查看
	function viewVoters(rowId){
		window.open("/tpwj/topicInfor.do?method=viewVoters&type=${param.type}&topicId="+rowId, "_blank");
	}
	
	//公示或取消
	function display(topicId,value){
		$.ajax({
			url: "/tpwj/topicInfor.do?method=display&topicId="+topicId+"&display="+value,	
			cache: false,
			type: "POST",
			dataType: "html",
			beforeSend: function (xhr) {						
			},
				
			complete : function (req, err) {
				//alert("数据已经删除！");
				//self.location.reload();
				$("#listTopicInfor").trigger("reloadGrid"); 
			}
		});
	}
</script>

<title>投票</title>
<body style="border:1px solid #0DE8F5;border-radius: 5px">
	<div>
		<table id="listTopicInfor"></table> <!-- 信息列表 -->
		<div id="pagerTopicInfor"></div> <!-- 分页 -->
	</div>
	
	<!-- 查询框 -->
	<div id="multiSearchDialog" style="display: none;">  
	    <table>  
	        <tbody>  
	            <tr>  
		           <td>  
		              <input type="hidden" class="searchField" value="topicName"/>主题：
		              <input type="hidden" class="searchOper" value="cn"/>
		           </td>  
		           <td>  
		              <input type="text" class="searchString"/>  
		           </td>  
		        </tr>
		        
	        </tbody>  
	    </table>  
	</div>
	
	<!-- 查询框（导出excel） -->
	<div id="excelDialog" style="display: none;">  
	    <table>  
	        <tbody>  
	            <tr>  
		           <td>  
		              <input type="hidden" class="searchField" value="topicName"/>主题：
		              <input type="hidden" class="searchOper" value="cn"/>
		           </td>  
		           <td>  
		              <input type="text" class="searchString"/>  
		           </td>  
		        </tr>
		        
		        <tr>  
	                <td>  
	                    <input type="hidden" class="searchField" value="departmentId"/>部门：
	                    <input type="hidden" class="searchOper" value="eq"/>
	                </td>  
	                <td>  
	                	<select class="searchString" id="excelDepartmentId"></select>
	                </td>  
	            </tr>  
		        
		        
		        <tr>  
	                <td>  
	                    <input type="hidden" class="searchField" value="personId"/>参与人员：
	                    <input type="hidden" class="searchOper" value="eq"/>
	                </td>  
	                <td>  
	                    <select class="searchString" id="excelPersonId"></select>
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
			/*if(rowObject.voter){
				html = "<a title='点击投票或者查看已投内容' href='javascript:;' onclick='doView("+options.rowId+")'>" + cellValue + "</a>";	
			}else{
				html = "<span title='您不是参与人'>"+cellValue+"</span>";
			}*/
			html = "<a href='javascript:;' onclick='doView("+options.rowId+")'>" + cellValue + "</a>";				
			return html;
		}
		
		//是否有效 
		function formatValid(cellValue, options, rowObject) {				
			var html = '';
			if(cellValue){
				html = '有效';
			}
			if(!cellValue){
				html = '<font color=red>无效</font>';
			}
			return html;
		}
		
		//相关操作
		function formatOperate(cellValue, options, rowObject) {		
			var returnStr = "";		
			if("${_SYSTEM_USER.userType == 1}" == 'true' || "${_TOPIC_OPERATOR}" == 'true'){
				returnStr += "<a href='javascript:;' onclick='editTopicInfor("+options.rowId+")'><font color=white>[修改]</font></a>";
	        	returnStr += "&nbsp;&nbsp;<a href='javascript:;' onclick='editItemInfor("+options.rowId+")'><font color=white>[设计]</font></a>";
	        	returnStr += "&nbsp;&nbsp;<a href='javascript:;' onclick='viewCount("+options.rowId+")'><font color=white>[查看统计]</font></a>";
	        	returnStr += "&nbsp;&nbsp;<a href='javascript:;' onclick='viewVoters("+options.rowId+")'><font color=white>[按人查看]</font></a>";
	        	
	        	if(rowObject.display){
	        		returnStr += "&nbsp;&nbsp;<a href='javascript:;' onclick='display("+options.rowId+",0)'><font color=yellow>[取消公示]</font></a>";
	        	}
	        	if(rowObject.display == null || !rowObject.display){
	        		returnStr += "&nbsp;&nbsp;<a href='javascript:;' onclick='display("+options.rowId+",1)'><font color=white>[首页公示]</font></a>";
	        	}
	        	
			}else{
				if(rowObject.checkCount){
					returnStr += "&nbsp;&nbsp;<a href='javascript:;' onclick='viewCount("+options.rowId+")'><font color=white>[查看统计结果]</font></a>";
				}
				
			}
			
        	return returnStr;
		}
	    
	    var caption = "";
	    if(${param.type} == 0){
	    	caption = "投票";
	    }
	    if(${param.type} == 1){
	    	caption = "问卷";
	    }
	    
		//加载表格数据
		var $mygrid = jQuery("#listTopicInfor").jqGrid({
            url:'/tpwj/topicInfor.do?method=list&type=${param.type}',
            rownumbers: true,	//是否显示序号
            datatype: "json",   //从后台获取的数据类型              
           	autowidth: true,	//是否自适应宽度
			//height: "auto",
            height:document.documentElement.clientHeight-140,
            colNames:['Id','主题', '开始时间', '结束时间', '是否有效','相关操作'],//表的第一行标题栏
            //以下为每列显示的具体数据
            colModel:[
                {name:'topicId',index:'topicId', width:0, search:false, hidden:true, key:true},
                {name:'topicName',index:'topicName',width:100,  sortable:true, formatter:formatTitle,sorttype:"string"},
                {name:'startTime',index:'startTime',width:50, align:'center'},
                {name:'endTime',index:'endTime', width:50,align:'center'},
                {name:'valid',index:'valid', width:40,align:'center',formatter:formatValid},
                {name:'operate',align:'center',formatter:formatOperate}
            ],
//            caption: caption,
            sortname: 'topicId', //默认排序的字段
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
            pager: "#pagerTopicInfor"	//分页工具栏
            //caption: "用户信息"	//表头
       	}).navGrid('#pagerTopicInfor',{edit:false,add:false,del:false,search:false}).closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
	
		//自定义按钮
		if("${_SYSTEM_USER.userType == 1}" == 'true' || "${_TOPIC_OPERATOR}" == 'true'){
			jQuery("#listTopicInfor").jqGrid('navButtonAdd','#pagerTopicInfor', {
				caption:"新增", title:"点击新增信息", buttonicon:'ui-icon-plusthick', onClickButton: addTopicInfor
			});
			
			jQuery("#listTopicInfor").jqGrid('navButtonAdd','#pagerTopicInfor', {
				caption:"<span style='color: red;'>批量删除</span>", title:"点击批量删除", buttonicon:'ui-icon-closethick', onClickButton: deleteTopicInfor
			});
			
			jQuery("#listTopicInfor").jqGrid('navButtonAdd','#pagerTopicInfor', {
				caption:"导出Excel", title:"点击导出到Excel", buttonicon:'ui-icon-calculator', onClickButton: excelDialog
			});
		}
		jQuery("#listTopicInfor").jqGrid('navButtonAdd','#pagerTopicInfor', {
			caption:"查询", title:"点击查询", buttonicon:'ui-icon-search', onClickButton: openMultipleSearchDialog
		});
		
		//删除数据
		function deleteTopicInfor(){
		
			doDelete("/tpwj/topicInfor.do?method=delete","listTopicInfor");
			
		}
		
		function closeDia(){
			$("#excelDialog").dialog("close");
		}
		
		function excelDialog() {
		    $("#excelDialog").dialog({
		        autoOpen: false,       
		        modal: true,   
		        resizable: true,       
		        width: 400,   
		        title: "导出统计结果到excel",   
		        buttons: {   
		            "导出": exportExcel,
		            "关闭": closeDia
		        }   
		    });
		    
		    $("#excelDialog").dialog("open");
		}
		
		/********导出excel*********/
		function exportExcel(){
			var yes = window.confirm("数据较多时，导出所需时间较长，确定要导出数据吗？");
			if (yes) {
				var rules = "";   
				var param0 = "#listClubInfor";
				var param1 = "#excelDialog";
			    $("tbody tr", param1).each(function(i){    	//(1)从multipleSearchDialog对话框中找到各个查询条件行   
			        var searchField = $(".searchField", this).val();    	//(2)获得查询字段
			        var searchString = $(".searchString", this).val();  	//(4)获得查询值   
			        
			        if(searchField && searchString) { 		//(5)如果三者皆有值且长度大于0，则将查询条件加入rules字符串   
			            //rules += '&{"field":"' + searchField + '","op":"' + searchOper + '","data":"' + searchString + '"}';   
			            rules += "&" + searchField + "=" + searchString;
			        }   
			    });   
			    if(rules) { 
			        rules = rules.substring(1);								//(6)如果rules不为空，且长度大于0，则去掉开头的逗号
			    }   
			       
			    var filtersStr = '{"groupOp":"AND","rules":[' + rules + ']}';//(7)串联好filtersStr字符串
			    
			    var url = "/tpwj/topicInfor.do?method=expertExcel&type=${param.type}&"+rules;
			    alert(url);
			    window.location.href=url;
			}
			
		}
		
		/** 查询条件中的部门,班组,用户下拉联动 */
		//部门信息初始化
		$('#excelDepartmentId').selectInit();
		//加载部门及联动信息		
		$.loadDepartments("excelDepartmentId", null, "excelPersonId");
		
		
	</script>