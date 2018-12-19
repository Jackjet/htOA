<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

	<script type="text/javascript">     
			//增加报告
			function addReport_base(rowId){
				var returnArray = window.showModalDialog("/customer/taskReport.do?method=editReport&taskId="+rowId,'',"dialogWidth:800px;dialogHeight:600px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
				if(returnArray != null && returnArray[0] == "refresh") {
					self.location.reload();
				}
			}
			
			//更改状态
			function checkStatu_base(rowId){
				var returnStr = window.showModalDialog("/customer/taskInfor.do?method=statuEdit&taskId="+rowId,'',"dialogWidth:250px;dialogHeight:140px;center:Yes;dialogTop: 250px; dialogLeft: 500px;");
				if(returnStr=="Y"){
					self.location.reload();
				}
			}
			
			//审核
			function checkTask_base(rowId){
				var returnArray = window.showModalDialog("/customer/taskInfor.do?method=checkEdit&taskId="+rowId,'',"dialogWidth:800px;dialogHeight:600px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
				if(returnArray != null && returnArray[0] == "refresh") {
					self.location.reload();
				}
			}
			
			//修改
			function editInfor_base(rowId){
				window.showModalDialog("/customer/taskInfor.do?method=edit&flag=0&taskId="+rowId,'',"dialogWidth:800px;dialogHeight:600px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
				self.location.reload();
			}
			
			//查看
			function viewInfor_base(rowId){
				var returnArray = window.showModalDialog("/customer/taskInfor.do?method=view&taskId="+rowId,'',"dialogWidth:800px;dialogHeight:600px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
				if(returnArray != null && returnArray[0] == "refresh") {
					self.location.reload();
				}
			}	
	</script>
	
  		<div style="margin-top:0px;">
			<table id="list" style="width:99%"></table>
			<div id="pager"></div>
		</div>
		
		<!-- 查询框 -->
		<div id="multiSearchDialog" style="display: none;">  
		    <table>
		        <tbody> 
		            <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="taskCode"/>任务编号：
		                    <input type="hidden" class="searchOper" value="cn"/>
		                </td>  
		                <td>  
		                    <input type="text" class="searchString" size="10"/>  
		                </td>  
		            </tr>
		            <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="taskName"/>任务名称：
		                    <input type="hidden" class="searchOper" value="cn"/>
		                </td>  
		                <td>  
		                    <input type="text" class="searchString" size="25"/>
		                </td>  
		            </tr>
		            <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="status"/>状态：
		                    <input type="hidden" class="searchOper" value="eq"/>
		                </td>  
		                <td>  
		                    <select class="searchString" id="status">
		                    	<option value="">--全部--</option>
		                    	<option value="0">进行</option>
		                    	<option value="1">通过</option>
		                    	<option value="2">暂停</option>
		                    	<option value="3">过期</option>
		                    </select>
		                </td>  
		            </tr>
		            
		            <!-- 
		            <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="createTime"/>创建时间：
		                    <input type="hidden" class="searchOper" value="ge"/>
		                </td>  
		                <td>  
		                    <input class="searchString" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly="true" size="12"/>
		                </td>  
		            </tr>
		             -->
		        </tbody>  
		    </table>  
		</div>
		<!--  -->
		
		<script src="<c:url value="/"/>js/multisearch.js"></script> <!--加载模态多条件查询相关js-->
		<script type="text/javascript"> 		
			
			//自定义操作栏的显示内容
		    function formatOperation(cellvalue, options, rowdata) {
	           var returnStr = "<a href='javascript:;' onclick='addReport_base("+options.rowId+")'>[添加报告]</a>";checkTask_base
	           returnStr += "<a href='javascript:;' onclick='checkTask_base("+options.rowId+")'>[审核]</a>";
	           returnStr += "<a href='javascript:;' onclick='checkStatu_base("+options.rowId+")'>[状态]</a>";
	           return returnStr;
		    }
		    
		    //自定义显示boolean型内容
		    function formatBol(cellvalue) {
	           var returnStr;
	           if (cellvalue) {
	              returnStr = "推荐";
	           }else {
	              returnStr = "不推荐";
	           }
	           return returnStr;
		    }
		    
		    //自定义附件显示
		    function formatAttachment(cellValue, options, rowObject) {				
				var html = '';
				//alert(cellValue);
				html = showAttachment(cellValue,'');				
				return html;
			}
			
			//标题查看链接
			function formatTitle(cellValue, options, rowObject) {				
				var html = '';
				html = "<a href='javascript:;' onclick='viewInfor_base("+options.rowId+")'>" + cellValue + "</a>";				
				return html;
			}
		   
		    //显示任务状态状态
		    function formatState(cellValue, options, rowObject) {				
				var html = '';
				if(cellValue==0){
					html = '进行'; 
				}else if(cellValue==1){
					html = '通过'; 
				}else if(cellValue==2){
					html = '暂停'; 
				}else{
					html = '过期'; 
				}
				return html;
			}
			
			//加载表格数据
			var $mygrid = jQuery("#list").jqGrid({
                url:"/customer/taskInfor.do?method=list&categoryId=0",
                datatype: "json",                
               	autowidth: true,
				height:300,
                colNames:['Id','任务编号','任务名称','所属项目分类','开始日期','结束日期','状态','操作'],//表的第一行标题栏
	            colModel:[
	                {name:'taskId',index:'taskId', width:0, sorttype:"int", search:false, key:true, hidden:true},
	                {name:'taskCode',index:'taskCode',align:'center', sortable:true,width:30,sorttype:"string"},
	                {name:'taskName',index:'taskName',align:'left', sortable:false,sorttype:"string",formatter:formatTitle},
	                {name:'categoryName',index:'categoryName',width:40,align:'center'},
	                {name:'startDateStr',index:'startDate', width:45, align:'center'},
	                {name:'endDateStr',index:'endDate', width:45, align:'center'},
	                {name:'status',index:'status', width:45,formatter:formatState, align:'center'},
	                {align:'center', width:50, search:false, sortable:false, formatter:formatOperation}
	            ],
                sortname: 'taskCode',
                sortorder: 'asc',
                multiselect: true,	//是否支持多选,可用于批量删除
                viewrecords: true,
                rowNum: 10,
                rowList: [10,20,30],
                scroll: false, 
                scrollrows: false,                          
                jsonReader:{
                   repeatitems: false
                },         
                pager: "#pager"
	        }).navGrid('#pager',{edit:false,add:false,del:false,search:false});         
        
        //为根分类时不显示
			jQuery("#list").jqGrid('navButtonAdd','#pager', {
				caption:"<span style='color: red;'>批量删除</span>", title:"点击批量删除", buttonicon:'ui-icon-closethick', onClickButton: deleteInfor
			});
			jQuery("#list").jqGrid('navButtonAdd','#pager', {
				caption:"查询", title:"点击查询", buttonicon:'ui-icon-search', onClickButton: openDialog
			});
			
			//打开查询窗口并进行窗口初始化
			var multiSearchParams = new Array();
			function openDialog() {
			    multiSearchParams[0] = "#list";				//列表Id
				multiSearchParams[1] = "#multiSearchDialog";//查询模态窗口Id
				
				initSearchDialog();
				
			    $(multiSearchParams[1]).dialog("open");
			}
			
			//批量删除
			function deleteInfor(){
				doDelete("/customer/taskInfor.do?method=delete","list");
			}
			//部门信息初始化
			$('#departmentId').selectInit();
			
			//加载部门及联动信息		
			$.loadDepartments("departmentId", null, "personId");
			
	</script>

		