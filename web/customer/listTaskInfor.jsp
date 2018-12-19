<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<script type="text/javascript">
	
	//新增
	function addInfor_base(){
		var returnArray = window.showModalDialog("/customer/taskInfor.do?method=edit&flag=0&categoryId="+${param.projectId},'',"dialogWidth:800px;dialogHeight:600px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		if(returnArray != null && returnArray[0] == "refresh") {
			self.location.reload();
		}
	}
	
	//修改
	function editInfor_base(rowId){
		var returnArray = window.showModalDialog("/customer/taskInfor.do?method=edit&flag=0&taskId="+rowId,'',"dialogWidth:800px;dialogHeight:600px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		if(returnArray != null && returnArray[0] == "refresh") {
			self.location.reload();
		}
	}
	
	//增加执行人
	function addReport_base(rowId){
		var returnArray = window.showModalDialog("/customer/taskInfor.do?method=edit&flag=1&taskId="+rowId,'',"dialogWidth:800px;dialogHeight:600px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		if(returnArray != null && returnArray[0] == "refresh") {
			self.location.reload();
		}
	}
	
	//查看
	function viewInfor_base(rowId){
		var returnArray = window.showModalDialog("/customer/taskInfor.do?method=view&taskId="+rowId,'',"dialogWidth:800px;dialogHeight:600px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		if(returnArray != null && returnArray[0] == "refresh") {
			self.location.reload();
		}
	}
</script>

<title></title>

  		<div>
			<table id="list${param.projectId}"></table>
			<div id="pager${param.projectId}"></div>
		</div>
		
		<!-- 查询框 -->
		<div id="multiSearchDialog${param.projectId}" style="display: none;">  
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
		                    <select class="searchString" id="status${param.projectId}">
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
		
		<script type="text/javascript">	
			//自定义操作栏的显示内容
		    function formatOperation(cellvalue, options, rowdata) {
	           var returnStr = "<a href='javascript:;' onclick='editInfor_base("+options.rowId+")'>[修改]</a>";
	           returnStr += " <a href='javascript:;' onclick='addReport_base("+options.rowId+")'>[增加执行人]</a>";
	           //  <a href='javascript:;' onclick='viewInfor("+options.rowId+")'>[查看]</a>
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
			var $mygrid = jQuery("#list"+${param.projectId}).jqGrid({
			
                url:"/customer/taskInfor.do?method=list&categoryId="+${param.projectId},
                datatype: "json",                
               	autowidth: true,
				height:300,
                colNames:['Id','任务编号','任务名称','所属项目分类','开始日期','结束日期','状态','操作'],//表的第一行标题栏
	            colModel:[
	                {name:'taskId',index:'taskId', width:0, sorttype:"int", search:false, key:true, hidden:true},
	                {name:'taskCode',index:'taskCode',align:'center', sortable:true,width:30,sorttype:"string"},
	                {name:'taskName',index:'taskName',align:'left', sortable:true,sorttype:"string",formatter:formatTitle},
	                {name:'categoryName',index:'categoryName',width:40,align:'center'},
	                {name:'startDateStr',index:'startDateStr', width:45, align:'center'},
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
                pager: "#pager"+${param.projectId}
	        }).navGrid('#pager'+${param.projectId},{edit:false,add:false,del:false,search:false});       
			
			//自定义按钮
			if (${param.isRoot != 'true'}) {
				//为根分类时不显示
				jQuery("#list"+${param.projectId}).jqGrid('navButtonAdd','#pager'+${param.projectId}, {
					caption:"新增", title:"点击新增文档", buttonicon:'ui-icon-plusthick', onClickButton: addInfor_base
				});
			}
			
			jQuery("#list"+${param.projectId}).jqGrid('navButtonAdd','#pager'+${param.projectId}, {
				caption:"<span style='color: red;'>批量删除</span>", title:"点击批量删除", buttonicon:'ui-icon-closethick', onClickButton: deleteInfor
			});
			jQuery("#list"+${param.projectId}).jqGrid('navButtonAdd','#pager'+${param.projectId}, {
				caption:"查询", title:"点击查询", buttonicon:'ui-icon-search', onClickButton: openDialog
			});
			
			//打开查询窗口并进行窗口初始化
			var multiSearchParams = new Array();
			function openDialog() {
			    multiSearchParams[0] = "#list"+${param.projectId};				//列表Id
				multiSearchParams[1] = "#multiSearchDialog"+${param.projectId};//查询模态窗口Id
				
				initSearchDialog();
				
			    $(multiSearchParams[1]).dialog("open");
			}
			
			//批量删除
			function deleteInfor(){
				doDelete("/customer/taskInfor.do?method=delete","list"+${param.projectId});
			}
			//部门信息初始化
			$('#departmentId').selectInit();
			
			//加载部门及联动信息		
			$.loadDepartments("departmentId", null, "personId");
			
			jQuery().ready(function (){
			
		    //获取部门信息(查询条件)
		   /* $.getJSON("/core/organizeInfor.do?method=getDepartments",function(data) {
		           var options = "<option value=''>--选择部门--</option>";
		           $.each(data._Departments, function(i, n) {
		               options += "<option value='"+n.organizeId+"'>"+n.organizeName+"</option>";   
		           });   
		           $('#departmentId').html(options);
		        }   
		    );*/
		    
		    //获取分类信息(查询条件)
		    $.getJSON("/document/document.do?method=getCategoryName",function(data) {
		           var options = "";
		           $.each(data._CategoryNames, function(i, n) {
		               options += "<option value='"+n.categoryId+"'>";
		                
		               for(var j=0;j<=n.layer;j++){
		               	options += "&nbsp;";
		               };
		               
		               if(n.layer==1){
		               	options += "<b>+</b>";
		               }
		               if(n.layer==2){
		               	options += "<b>-</b>"; 
		               }
		               
		               options += n.categoryName+"</option>";
		           });   
		           $('#searchCategoryId').html(options);
		        }   
		    );
		});
			
		</script>