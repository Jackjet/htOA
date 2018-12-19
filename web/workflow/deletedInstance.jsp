<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<script src="<c:url value="/"/>datePicker/WdatePicker.js" type="text/javascript"></script>
<script src="<c:url value="/"/>components/jquery-1.4.2.js" type="text/javascript"></script> <!--jquery包-->
<script src="<c:url value="/"/>components/jqgrid/js/jquery-ui-1.8.2.custom.min.js" type="text/javascript"></script><!--jquery ui-->
<script src="<c:url value="/"/>components/jquery.layout.js" type="text/javascript"></script><!--jquery 布局-->
<script src="<c:url value="/"/>components/jqgrid/js/grid.locale-cn.js" type="text/javascript"></script> <!--jqgrid 中文包-->
<script src="<c:url value="/"/>components/jqgrid/js/jquery.jqGrid.min.js" type="text/javascript"></script> <!--jqgrid 包-->
<script src="<c:url value="/"/>js/commonFunction.js"></script>
<script src="<c:url value="/"/>js/inc_javascript.js"></script>
<script src="<c:url value="/"/>js/multisearch.js"></script> <!--加载模态多条件查询相关js-->
<script src="<c:url value="/"/>js/changeclass.js"></script> <!--用于改变页面样式-->
<%--<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />--%>
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>css/base/jquery-ui-1.9.2.custom.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/tabstyle.css" />

<script type="text/javascript">
	
	//查看
	function doView(rowId){
//		window.showModalDialog("/workflow/instanceInfor.do?method=view&instanceId="+rowId,'',"dialogWidth:800px;dialogHeight:600px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
        window.open("/workflow/instanceInfor.do?method=view&instanceId="+rowId, "_blank");
	}		
	
</script>

<title></title>
<body style="border:1px solid #0DE8F5;border-radius: 5px">
  		<div>
			<table id="deletedList"></table>
			<div id="deletedPager"></div>
		</div>
		
		<!-- 查询框 -->
		<div id="deletedMultiSearchDialog" style="display: none;">  
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
		                    <select id="delDepartmentId"></select>
		                </td>  
		            </tr>

					<tr>
		                <td>  
		                    <input type="hidden" class="searchField" value="applier.personId"/>申请人：
		                    <input type="hidden" class="searchOper" value="eq"/>
		                </td>  
		                <td>  
		                    <select class="searchString" id="delApplierId"></select>
		                </td>  
		            </tr>
		        </tbody>  
		    </table>
		</div>
		<!-- ----- -->
</body>
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
			var $mygrid = jQuery("#deletedList").jqGrid({
	            url:"/workflow/instanceInfor.do?method=list&deleted=true",
	            rownumbers: true,
	            datatype: "json",                
	            autowidth: true,
                height:document.documentElement.clientHeight-150,
	            colNames: ['Id', '正文标题', '创建时间', '申请人', '所属部门', '所属流程', '主办人', '状态'],//表的第一行标题栏
	            colModel:[
                    {name:'instanceId',index:'instanceId', width:0, sorttype:"int", search:false, key:true, hidden:true},
                    {name:'instanceTitle',index:'instanceTitle', sortable:true, sorttype:"string", formatter:formatTitle},
                    {name:'updateTime',index:'updateTime', width:70, align:'center'},
                    {name:'applier',index:'applier', width:40, align:'center'},
                    {name:'department',index:'department', width:40, align:'center'},
                    {name:'flow',index:'flow', width:40, align:'center'},
                    {name:'charger',index:'charger', width:40, align:'center'},
                    {name:'status',index:'status', width:80, align:'center', formatter:formatStatus}
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
	            pager: "#deletedPager",
//	            caption: "回收站"
	        }).navGrid('#deletedPager',{edit:false,add:false,del:false,search:false}).closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
			
			//自定义按钮
			jQuery("#deletedList").jqGrid('navButtonAdd','#deletedPager', {
				caption:"<span style='color: red;'>彻底删除</span>", title:"点击彻底删除", buttonicon:'ui-icon-closethick', onClickButton: deleteInfor
			});
			jQuery("#deletedList").jqGrid('navButtonAdd','#deletedPager', {
				caption:"恢复", title:"点击恢复", buttonicon:'ui-icon-arrowreturnthick-1-e', onClickButton: recovery
			});
			jQuery("#deletedList").jqGrid('navButtonAdd','#deletedPager', {
				caption:"查询", title:"点击查询", buttonicon:'ui-icon-search', onClickButton: openDeletedDialog
			});
			
			//打开查询窗口并进行窗口初始化
			var multiSearchParams = new Array();
			function openDeletedDialog() {
			    multiSearchParams[0] = "#deletedList";				//列表Id
				multiSearchParams[1] = "#deletedMultiSearchDialog";//查询模态窗口Id
				
				initSearchDialog();
				
			    $(multiSearchParams[1]).dialog("open");
			}
			
			//批量删除
			function deleteInfor(){
				doDelete("/workflow/instanceInfor.do?method=delete","deletedList");
			}
			
			//批量恢复
			function recovery(){
				//获取选择的行的Id
				var rowIds = jQuery("#deletedList").jqGrid('getGridParam','selarrrow'); 
					
				if(rowIds != null && rowIds.length > 0){
					var yes = window.confirm("确定要恢复吗？");
					if (yes) {
						$.ajax({
							url: "/workflow/instanceInfor.do?method=recycle&deleteFlag=0&rowIds="+rowIds,
							cache: false,
							type: "POST",
							dataType: "html",
							beforeSend: function (xhr) {						
							},
								
							complete : function (req, err) {
								alert("数据已经恢复！");
								$("#deletedList").trigger("reloadGrid"); 
							}
						});	
					}
				}else {
					alert("请选择要恢复的数据！");
				}
			}
			
			/** 查询条件中的部门,班组,用户下拉联动 */
			//部门信息初始化
			$('#delDepartmentId').selectInit();
			
			//加载部门及联动信息		
			$.loadDepartments("delDepartmentId", null, "delApplierId");
			/** ******** */
			
		</script>