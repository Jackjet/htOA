<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>
<%@ page import="com.kwchina.oa.submit.util.SubmitConstant" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
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

<style>
	li,a:focus{outline:none;!important; color:white!important; }
	#multiSearchDialog86{
		height: 260px!important;
	}
</style>
<title></title>
<body style="border:1px solid #0DE8F5;border-radius: 5px">

<div id="dialog" align="center" style="height:auto;display: none" title="新增选择">
	<table style="text-align: center">
		<tr>
			<td>
				<select id="fs">
					<option value="0">新增</option>
					<option value="1">修改</option>
					<option value="2">销毁</option>
				</select>
			</td>
			<td>
				<select id="st">
					<option value="0">文件</option>
					<option value="1">流程</option>
					<option value="2">文件+流程</option>
				</select>
			</td>

		</tr>
	</table>
</div>

  		<div>
			<table id="list80"></table>
			<div id="pager80"></div>
		</div>
		
		<div id="viewUser" style="display:none;"></div>

		<!-- 查询框 -->
		<div id="multiSearchDialog80" style="display: none;overflow: auto">
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

		            <%--<tr>
		                &lt;%&ndash;<td>
		                    <input type="hidden" class="searchField" value="updateTime"/>结束时间：
		                    <input type="hidden" class="searchOper" value="le"/>
		                </td>
		                <td>
		                    <input class="searchString" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly="true" size="12"/>
		                </td>&ndash;%&gt;
		            </tr>--%>

		            <tr>
		                <td>
		                    <input type="hidden"  class="searchField" value="department.organizeId"/>所属部门：
		                    <input type="hidden" class="searchOper" value="eq"/>
		                </td>
		                <td>
		                    <select class="searchString" id="departmentId"></select>
		                </td>
					</tr>
					<tr>
						<td>
							<input type="hidden" class="searchField" value="applier.personId"/>申请人：
							<input type="hidden" class="searchOper" value="eq"/>
						</td>
						<td>
							<select class="searchString" id="applierId"></select>
						</td>
		            </tr>

					<%--<tr>
		               &lt;%&ndash; <td>
		                    <input type="hidden" class="searchField" value="applier.personId"/>申请人：
		                    <input type="hidden" class="searchOper" value="eq"/>
		                </td>
		                <td>
		                    <select class="searchString" id="applierId"></select>
		                </td>&ndash;%&gt;
		            </tr>--%>
		        </tbody>
		        <tbody id="searchFields"></tbody>
		    </table>
		</div>
</body>
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
			
			//自定义部门信息
		   	function formatOrg(cellValue, options, rowObject) {
				var html = '';
				//获取部门名称
				$.ajax({
					url: "/core/organizeInfor.do?method=getOrganizeName&departmentId="+cellValue,
					type: "get",
					dateType: "json",
					async: false,
					success: function(datas) {
						var data = eval("("+datas+")");
						html += data._OrganizeName;
					}
				});
				return html;
			}
			
			//自定义用户信息
		   	function formatUsr(cellValue, options, rowObject) {
				var html = '';
				//获取人员名称
				$.ajax({
					url: "/core/systemUserInfor.do?method=getPersonName&personId="+cellValue,
					type: "get",
					dateType: "json",
					async: false,
					success: function(datas) {
						var data = eval("("+datas+")");
						html += data._PersonName;
					}
				});
				return html;
			}
			
			//自定义具有浏览权限人员
			function formatViewUser(cellValue, options, rowObject) {
				var html = '';
				html += "<a href='#none' class='tooltip' onClick='getViewUsers(this,"+rowObject.instanceId+");'";
				html +=" title='单击查看此公文的浏览人员' style='z-index:9999;'>";
				html += "<img src='<c:url value="/"/>img/llr.png' width='20' height='16' border='0'/>"
				html +=" </a>";
				return html;
			}
			
			//自定义查看统计
			function formatViewCount(cellValue, options, rowObject) {
				var html = '';
				html += "<a href='<c:url value="/"/>workflow/instanceInfor.do?method=viewCount&instanceId="+rowObject.instanceId+"' target='_blank' class='tooltip' ";//  onClick='getViewUsers(this,"+rowObject.instanceId+");'";
				html +=" title='单击查看此公文的审核统计信息' style='z-index:9999;'>";
				html += "<img src='<c:url value="/"/>images/menu/asset.gif' width='16' height='16' border='0'/>"
				html +=" </a>";
				return html;
			}
			
			var flowName = "文件流程";

			
			//获取自定义列
			var col_names = ['Id', '科室', '流程名', '文件名', '文件编号', '流程版本','文件版本','流程支持文件','流程修改位置及原因','文件修改位置及原因',
				'流程修改后内容','文件修改后内容','受影响的其他流程及文件','流程销毁原因','文件销毁原因'];
			var col_model = [{name:'fid',index:'fid', width:0, sorttype:"int", search:false, key:true, hidden:true},
                    {name:'department',index:'department',width:40, sortable:true, sorttype:"string"},
					{name:'flowName',index:'flowName',width:40, sortable:true, sorttype:"string"},
					{name:'fileName',index:'fileName',width:40, sortable:true, sorttype:"string"},
					{name:'fileNo',index:'fileNo',width:40, sortable:true, sorttype:"string"},
					{name:'flowEdition',index:'flowEdition',width:40, sortable:true, sorttype:"string"},
					{name:'fileEdition',index:'fileEdition',width:40, sortable:true, sorttype:"string"},
					{name:'flowSup',index:'flowSup',width:40, sortable:true, sorttype:"string"},
					{name:'flowUpdatePo',index:'flowUpdatePo',width:40, sortable:true, sorttype:"string"},
					{name:'fileUpdatePo',index:'fileUpdatePo',width:40, sortable:true, sorttype:"string"},
					{name:'flowNew',index:'flowNew',width:40, sortable:true, sorttype:"string"},
					{name:'fileNew',index:'fileNew',width:40, sortable:true, sorttype:"string"},
					{name:'beAff',index:'beAff',width:40, sortable:true, sorttype:"string"},
					{name:'flowDe',index:'flowDe',width:40, sortable:true, sorttype:"string"},
					{name:'fileDe',index:'fileDe',width:40, sortable:true, sorttype:"string"}

                  ];

			var index = col_names.length;
			
			//查看统计
			if("${_SYSTEM_USER.userType}" == "1"){
				col_names[index+1] = "统计";
				col_model[index+1] = eval("({name:'viewCount',index:'viewCount',  align:'center',formatter:formatViewCount})");/*width:30,*/
            }
			
			//alert(col_names);alert(col_model);
			    
			//加载表格数据
			var $mygrid = jQuery("#list80").jqGrid({
	            url:"/workflow/instanceInfor.do?method=list_File&flowId=80",
	            rownumbers: true,
	            datatype: "json",
                width:"90%",
	            autowidth: true,
				height:document.documentElement.clientHeight-140,
	            colNames: col_names,//表的第一行标题栏
	            colModel: col_model,
	            sortname: 'instanceId',
	            sortorder: 'desc',
	            multiselect: true,	//是否支持多选,可用于批量删除
	            viewrecords: true,
	            rowNum: 20,
	            rowList: [10,20,30],
//	            scroll: false,
//	            scrollrows: false,
                shrinkToFit:true,
	            jsonReader:{
	                repeatitems: false
	            },
	            pager: "#pager80"
//	            caption: flowName,

	        }).navGrid('#pager80',{edit:false,add:false,del:false,search:false}).closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
			
			//自定义按钮
			jQuery("#list80").jqGrid('navButtonAdd','#pager80', {
				caption:"新增", title:"点击新增信息", buttonicon:'ui-icon-plusthick', onClickButton: addInfor
			});
			jQuery("#list80").jqGrid('navButtonAdd','#pager80', {
				caption:"<span style='color: red;'>批量删除</span>", title:"点击批量删除", buttonicon:'ui-icon-trash', onClickButton: deleteInfor
			});
			jQuery("#list80").jqGrid('navButtonAdd','#pager80', {
				caption:"查询", title:"点击查询", buttonicon:'ui-icon-search', onClickButton: openDialog
			});
			
			//如果是主办人或者管理员
			//alert(${_SYSTEM_USER.personId});
			if(${_SYSTEM_USER.personId == param.chargerId || _SYSTEM_USER.userType == 1}){
				jQuery("#list80").jqGrid('navButtonAdd','#pager80', {
					caption:"批量授权", title:"点击批量授权", buttonicon:'ui-icon-script', onClickButton: doAuthorize
				});
			}
			
			/*if ((${param.flowId}==<%=SubmitConstant.SubmitFlow_Report_Contract%> && ("${_IsContractRole}" == 'true' || "${_SYSTEM_USER.userType}" == "1" || "${_SYSTEM_USER.personId}" == "207"))
					|| (${param.flowId}==<%=SubmitConstant.SubmitFlow_Report_Publish%> && ("${_SYSTEM_USER.userType}" == "1" || "${_SYSTEM_USER.personId}" == "207"))
					|| (${param.flowId}==<%=SubmitConstant.SubmitFlow_Report_Receive%> && ("${_SYSTEM_USER.userType}" == "1" || "${_SYSTEM_USER.personId}" == "207"))) { // && "${_IsContractRole}" == 'true'
				/!*jQuery("#list"+${param.flowId}).jqGrid('navButtonAdd','#pager'+${param.flowId}, {
					caption:"导出excel", title:"点击导出", buttonicon:'ui-icon-search', onClickButton: excel
				});*!/

				jQuery("#list"+${param.flowId}).jqGrid('navButtonAdd','#pager'+${param.flowId}, {
					caption:"导出Excel", title:"点击导出到Excel", buttonicon:'ui-icon-calculator', onClickButton: exportExcel
				});
			}*/

            //打开查询窗口并进行窗口初始化
			var multiSearchParams = new Array();
			function openDialog() {

			    multiSearchParams[0] = "#list80";				//列表Id
				multiSearchParams[1] = "#multiSearchDialog80";//查询模态窗口Id
				
				initSearchDialog();
				
				//获取自定义表单查询字段
				getSearchFields();
				
			    $(multiSearchParams[1]).dialog("open");
			}
			
			/********导出excel*********/
			function exportExcel(){
				var yes = window.confirm("数据较多时，导出所需时间较长，确定要导出数据吗？");
				if (yes) {
					var rules = "";   
					var param0 = "#list80";
					var param1 = "#multiSearchDialog80";
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
				    
				    var url = "/workflow/instanceInfor.do?method=expertExcel&flowId=80"+"&_search=true&page=1&rows=100000&sidx=instanceId&sord=desc&filters="+filtersStr;
				    window.location.href=url;
				}
				
			}
			
			//批量删除
			function deleteInfor(){
				//获取选择的行的Id
				var rowIds = jQuery("#list80").jqGrid('getGridParam','selarrrow');
					
				if(rowIds != null && rowIds.length > 0){
					var yes = window.confirm("确定要删除吗？");
					if (yes) {
						$.ajax({
							url: "/workflow/instanceInfor.do?method=recycle&flowId=80&rowIds="+rowIds,	//删除数据的url
							cache: false,
							type: "POST",
							dataType: "html",
							beforeSend: function (xhr) {						
							},
								
							complete : function (req, err) {
								var returnValues = eval("("+req.responseText+")");
								if (returnValues["_CanRecycle"]) {
									alert("数据已经删除！");
									$("#list80").trigger("reloadGrid");
								}else {
									alert("对不起,您无权进行该操作,请与系统管理员联系!");
								}
							}
						});	
					}
				}else {
					alert("请选择要删除的数据！");
				}
			}
			
			/** 查询条件中的部门,班组,用户下拉联动 */
			//部门信息初始化
			$('#departmentId').selectInit();
			
			//加载部门及联动信息		
			$.loadDepartments("departmentId", null, "applierId");
			/** ******** */
			
		</script>