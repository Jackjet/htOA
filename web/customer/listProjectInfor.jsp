<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<script type="text/javascript">
	
	//新增
	function addInfor_base(){
		var returnArray = window.showModalDialog("/customer/projectInfor.do?method=edit",'',"dialogWidth:800px;dialogHeight:600px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		if(returnArray != null && returnArray[0] == "refresh") {
			self.location.reload();
		}
	}
	
	//修改
	function editInfor_base(rowId){
		var returnArray = window.showModalDialog("/customer/projectInfor.do?method=edit&projectId="+rowId,'',"dialogWidth:800px;dialogHeight:600px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		if(returnArray != null && returnArray[0] == "refresh") {
			self.location.reload();
		}
	}
	
	//查看
	function viewInfor_base(rowId){
		var returnArray = window.showModalDialog("/customer/projectInfor.do?method=view&projectId="+rowId,'',"dialogWidth:800px;dialogHeight:600px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		if(returnArray != null && returnArray[0] == "refresh") {
			self.location.reload();
		}
	}
</script>

<title></title>

  		<div>
			<table id="list"></table>
			<div id="pager"></div>
		</div>
		
		<!-- 查询框 -->
		<div id="multiSearchDialog" style="display: none;">  
		    <table>
		        <tbody> 
		            
		            <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="projectName"/>任务名称：
		                    <input type="hidden" class="searchOper" value="cn"/>
		                </td>  
		                <td>  
		                    <input type="text" class="searchString" size="25"/>
		                </td>  
		            </tr>
		            
		            <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="updateDate"/>创建时间从：
		                    <input type="hidden" class="searchOper" value="ge"/>
		                </td>  
		                <td>  
		                    <input class="searchString" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly="true" size="12"/>
		                </td>  
		            </tr>
		            
		            <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="updateDate"/>创建时间到：
		                    <input type="hidden" class="searchOper" value="le"/>
		                </td>  
		                <td>  
		                    <input class="searchString" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly="true" size="12"/>
		                </td>  
		            </tr>

		        </tbody>  
		    </table>  
		</div>
		
		<script type="text/javascript">	
			//自定义操作栏的显示内容
		    function formatOperation(cellvalue, options, rowdata) {
	           var returnStr = "<a href='javascript:;' onclick='editInfor_base("+options.rowId+")'>[修改]</a>";
	           return returnStr;
		    }
		    
			//标题查看链接
			function formatTitle(cellValue, options, rowObject) {				
				var html = '';
				html = "<a href='javascript:;' onclick='viewInfor_base("+options.rowId+")'>" + cellValue + "</a>";				
				return html;
			}
		   
			//加载表格数据
			var $mygrid = jQuery("#list").jqGrid({
                url:"/customer/projectInfor.do?method=listProject",
                datatype: "json",                
               	autowidth: true,
				height:300,
                colNames:['Id','项目名称','创建时间','创建者','项目经理','操作'],//表的第一行标题栏
	            colModel:[
	                {name:'projectId',index:'projectId', width:0, sorttype:"int", search:false, key:true, hidden:true},
	                {name:'projectName',index:'projectName',align:'left', sortable:true,sorttype:"string",formatter:formatTitle},
	                {name:'updateDateStr',index:'updateDateStr', width:45, align:'center',sortable:true},
	                {name:'createorName',index:'createorName', width:45, align:'center'},
	                {name:'managerName',index:'managerName', width:45, align:'center'},
	                {align:'center', width:50, search:false, sortable:false, formatter:formatOperation}
	            ],
                sortname: 'projectId',
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
			
			//自定义按钮
			jQuery("#list").jqGrid('navButtonAdd','#pager', {
				caption:"新增", title:"点击新增项目", buttonicon:'ui-icon-plusthick', onClickButton: addInfor_base
			});
			
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
				doDelete("/customer/projectInfor.do?method=deleteProject","list");
			}
			
		</script>