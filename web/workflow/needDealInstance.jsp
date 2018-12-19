<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<script type="text/javascript">
	
	//修改
	/**
	function editInfor(rowId){
		var returnArray = window.showModalDialog("/workflow/instanceInfor.do?method=edit&instanceId="+rowId,'',"dialogWidth:800px;dialogHeight:600px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		if(returnArray[0] == "refresh") {
			//保存信息后重新加载tab
			loadTab(returnArray[1]);
		}
	}
	*/
	
	//查看
	function doView(rowId){
		window.showModalDialog("/workflow/instanceInfor.do?method=view&instanceId="+rowId,'',"dialogWidth:800px;dialogHeight:600px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
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
		                    <input type="hidden" class="searchField" value="instanceTitle"/>正文标题：
		                    <input type="hidden" class="searchOper" value="cn"/>
		                </td>  
		                <td>  
		                    <input type="text" class="searchString" size="25"/>  
		                </td>  
		            </tr>
		            <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="updateTime"/>起始时间：
		                    <input type="hidden" class="searchOper" value="ge"/>
		                </td>  
		                <td>  
		                    <input class="searchString" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly="true" size="12"/>
		                </td>  
		            </tr>
		            
		            <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="updateTime"/>结束时间：
		                    <input type="hidden" class="searchOper" value="le"/>
		                </td>  
		                <td>  
		                    <input class="searchString" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly="true" size="12"/>
		                </td>  
		            </tr>
		            
		            <tr>  
		                <td>  
		                    <input type="hidden" value="department.organizeId"/>所属部门：
		                    <input type="hidden" value="eq"/>
		                </td>  
		                <td>  
		                    <select id="nedDepartmentId"></select>
		                </td>  
		            </tr>

					<tr>
		                <td>  
		                    <input type="hidden" class="searchField" value="applier.personId"/>申请人：
		                    <input type="hidden" class="searchOper" value="eq"/>
		                </td>  
		                <td>  
		                    <select class="searchString" id="nedApplierId"></select>
		                </td>  
		            </tr>
		        </tbody>  
		    </table>
		</div>
		<!-- ----- -->
		
		<script type="text/javascript"> 		
				
			//自定义显示标题
		   	function formatTitle(cellValue, options, rowObject) {
				var html = '';
				html = "<a href='javascript:;' onclick='doView("+options.rowId+")'>" + cellValue + "</a>";
				return html;
			}
			
			//自定义显示状态
		    function formatStatus(cellValue, options, rowObject) {
				var html = '';
				html = "<font color='red'>" + cellValue + "</font>";
				return html;
			}
			    
			//加载表格数据
			var $mygrid = jQuery("#list").jqGrid({
	            url:"/workflow/instanceInfor.do?method=list",
	            rownumbers: true,
	            datatype: "json",                
	            autowidth: true,
				height:300,
	            colNames: ['Id', '正文标题', '创建时间', '申请人', '所属部门', '所属流程', '主办人', '状态'],//表的第一行标题栏
	            colModel:[
                    {name:'instanceId',index:'instanceId', width:0, sorttype:"int", search:false, key:true, hidden:true},
                    {name:'instanceTitle',index:'instanceTitle', sortable:true, sorttype:"string", formatter:formatTitle},
                    {name:'updateTime',index:'updateTime', width:70, align:'center'},
                    {name:'applier',index:'applier', width:40, align:'center'},
                    {name:'department',index:'department', width:40, align:'center'},
                    {name:'flow',index:'flow', width:40, align:'center'},
                    {name:'charger',index:'charger', width:40, align:'center'},
                    {name:'status',index:'status', width:40, align:'center', formatter:formatStatus}
                ],
	            sortname: 'instanceId',
	            sortorder: 'desc',
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
				caption:"<span style='color: red;'>批量删除</span>", title:"点击批量删除", buttonicon:'ui-icon-trash', onClickButton: deleteInfor
			});
			jQuery("#list").jqGrid('navButtonAdd','#pager', {
				caption:"查询", title:"点击查询", buttonicon:'ui-icon-search', onClickButton: openSearchDialog
			});
			
			//打开查询窗口并进行窗口初始化
			var multiSearchParams = new Array();
			function openSearchDialog() {
			    multiSearchParams[0] = "#list";				//列表Id
				multiSearchParams[1] = "#multiSearchDialog";//查询模态窗口Id
				
				initSearchDialog();
				
			    $(multiSearchParams[1]).dialog("open");
			}
			
			//批量删除
			function deleteInfor(){
				doDelete("/workflow/instanceInfor.do?method=recycle","list");
			}
			
			/** 查询条件中的部门,班组,用户下拉联动 */
			//部门信息初始化
			$('#nedDepartmentId').selectInit();
			
			//加载部门及联动信息		
			$.loadDepartments("nedDepartmentId", null, "nedApplierId");
			/** ******** */
			
		</script>