<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>
<%@ page import="com.kwchina.oa.submit.util.SubmitConstant" %>

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
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/tabstyle.css" />

<script type="text/javascript">
	function getViewUsers(obj,instanceId){
		var html = "<br/>";
		
		$.getJSON("/workflow/instanceInfor.do?method=getViewUsers&instanceId="+instanceId,function(data) {
			if (data != null) {
				$.each(data._ViewUsers, function(i, n) {
					//获取人员名称
					$.ajax({
						url: "/core/systemUserInfor.do?method=getPersonName&personId="+n.personId,
						type: "get",
						dateType: "json",
						async: false,
						success: function(datas) {
							var _data = eval("("+datas+")");
							var personName = _data._PersonName;
							html += "<span style='white-space:nowrap;'><font color=blue>"+personName+"</font></span>&nbsp;&nbsp;";
							if((i+1)%7 == 0){
								html += "<br/><br/>";
							}
						}
					});
					
				});
			}
		});
		
		
		$("#viewUser").dialog({
	        autoOpen: false,       
	        modal: true,   
	        resizable: true,       
	        width: 350,   
	        title: "具有浏览权限的人员",   
	        buttons: {   
	            "关闭": function(){$("#viewUser").dialog("close");}
	        }   
	    });
	    $("#viewUser").html("<br/>加载中，请稍候...");
	    $("#viewUser").dialog("open");
		
		setTimeout(function(){$("#viewUser").html(html);},"500");
	}
	
	//获取自定义表单查询字段
	function getSearchFields() {
		$.ajax({
			url: "/workflow/instanceInfor.do?method=getSearchFields&flowId=${param.flowId}",
			type: "post",
			dataType: "json",
			async: false,	//设置为同步
			beforeSend: function (xhr) {
			},
			complete : function (req, err) {
				$('#searchFields').empty();
				var returnValues = eval("("+req.responseText+")");
				if (returnValues["_SearchFields"] != null && returnValues["_SearchFields"].length > 0) {
					for(var i=0;i<returnValues["_SearchFields"].length;i++) {
						var field = returnValues['_SearchFields'][i];
						if (field != null) {
							var searchType = field[0]["searchType"];
							var searchField = field[1]['searchField'];
							var searchOper = field[2]['searchOper'];
							var searchName = field[3]["searchName"];
							var searchFieldHtml = "<tr><td><input type='hidden' class='searchField' value='"+searchField+"'/>"+searchName+"：";
							searchFieldHtml += "<input type='hidden' class='searchOper' value='"+searchOper+"'/></td>";
							if (searchType=="input") {
								//一般input
								searchFieldHtml += "<td><input type='text' class='searchString' size='25'/></td></tr>";
							}else if (searchType=="dateInput") {
								//日期input
								searchFieldHtml += "<td><input type='text' class='searchString' onclick='WdatePicker()' readonly='true' size='12'/></td></tr>";
							}else if (searchType=="orgSelect") {
								//部门下拉
								$.ajax({
									url: "/core/organizeInfor.do?method=getDepartments",
									type: "post",
									dataType: "json",
									async: false,	//设置为同步
									beforeSend: function (xhr) {
									},
									complete : function (req, err) {
										var returnValues = eval("("+req.responseText+")");
										searchFieldHtml += "<td><select class='searchString' id='"+searchField+"'><option value=''>请选择</option>";
										$.each(returnValues["_Departments"], function(i, n) {
											searchFieldHtml += "<option value='"+ n.organizeId + "'>" + n.organizeName + "</option>";
										});
										searchFieldHtml += "</select></td>";
									}
								});
							}else if (searchType=="usrSelect") {
								//用户下拉
								$.ajax({
									url: "/core/systemUserInfor.do?method=getUsers",
									type: "post",
									dataType: "json",
									async: false,	//设置为同步
									beforeSend: function (xhr) {
									},
									complete : function (req, err) {
										var returnValues = eval("("+req.responseText+")");
										searchFieldHtml += "<td><select class='searchString' id='"+searchField+"'><option value=''>请选择</option>";
										$.each(returnValues["_Users"], function(i, n) {
										    searchFieldHtml += "<option value='"+ n.personId + "'>" + n.person.personName + "</option>";
										});
										searchFieldHtml += "</select></td>";
									}
								});
							}
							searchFieldHtml += "</tr>";
							$('#searchFields').append(searchFieldHtml);
						}
					}
				}
			}
		});
	}
	
	//新增
	function addInfor(){
		/** var returnArray = window.showModalDialog("/workflow/instanceInfor.do?method=edit&flowId="+${param.flowId},'',"dialogWidth:800px;dialogHeight:600px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		if(returnArray[0] == "refresh") {
			//保存信息后重新加载tab
			//loadTab(returnArray[1]);
			self.location.reload();
		}*/
		window.open("/workflow/instanceInfor.do?method=edit&flowId="+${param.flowId}, "_blank");
	}
	
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
		//window.showModalDialog("/workflow/instanceInfor.do?method=view&instanceId="+rowId,'',"dialogWidth:900px;dialogHeight:800px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		window.open("/workflow/instanceInfor.do?method=view&instanceId="+rowId, "_blank");
	}		
	
<<<<<<< .mine
	//授权
	function doAuthorize(){
		//获取选择的行的Id
		var rowIds = jQuery("#list"+${param.flowId}).jqGrid('getGridParam','selarrrow'); 
			
		if(rowIds != null && rowIds.length > 0){
			var returnArray = window.showModalDialog("/workflow/instanceInfor.do?method=editInforsRight&rowIds="+rowIds,'',"dialogWidth:800px;dialogHeight:800px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
			if(returnArray != null && returnArray[0] == "refresh") {
				//self.location.reload();
				$("#list"+${param.flowId}).trigger("reloadGrid");
			}
		}else{
			alert("请选择要授权的数据！");
		}
		
	}
	
=======
	//查看
	function excel(){
		//window.showModalDialog("/workflow/instanceInfor.do?method=view&instanceId="+rowId,'',"dialogWidth:900px;dialogHeight:800px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		//window.open("<c:url value="${'/common/'}"/>download.jsp?filepath=uploadfiles/submit/excel/contract.xls", "_blank");
		
		//window.open("/workflow/instanceInfor.do?method=listExcel&flowId="+${param.flowId}, "_blank");
		var yes = window.confirm("确定要导出数据吗？");
					if (yes) {
		printStr(${param.flowId});
		}
	}		
	
>>>>>>> .r2041
</script>

<title></title>
  		<div>
			<table id="list${param.flowId}"></table>
			<div id="pager${param.flowId}"></div>
		</div>
		
		<div id="viewUser" style="display:none;"></div>
		
		<!-- 查询框 -->
		<div id="multiSearchDialog${param.flowId}" style="display: none;">  
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
		        </tbody>
		        <tbody id="searchFields"></tbody>
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
				//getViewUsers
				//html += "<a href='#none' class='tooltip' onmouseover='showUsrMessage(this,"+rowObject.instanceId+");' onmouseout='closeUsrMessage();'";
				html += "<a href='#none' class='tooltip' onClick='getViewUsers(this,"+rowObject.instanceId+");'";
				html +=" title='单击查看此公文的浏览人员' style='z-index:9999;'>";
				html += "<img src='<c:url value="/"/>images/menu/comm.gif' width='16' height='16' border='0'/>"
				html +=" </a>";
				return html;
			}
			
			
			var flowName = "";
			if (${param.flowId}==<%=SubmitConstant.SubmitFlow_Report_Publish%>) {
				flowName = "行政发文";
			}else if (${param.flowId}==<%=SubmitConstant.SubmitFlow_Report_Receive%>) {
				flowName = "行政收文";
			}else if (${param.flowId}==<%=SubmitConstant.SubmitFlow_Party_Publish%>) {
				flowName = "党群发文";
			}else if (${param.flowId}==<%=SubmitConstant.SubmitFlow_Party_Receive%>) {
				flowName = "党群收文";
			}else if (${param.flowId}==<%=SubmitConstant.SubmitFlow_Report_Inside%>) {
				flowName = "内部报告";
			}else if (${param.flowId}==<%=SubmitConstant.SubmitFlow_Party_Inside%>) {
				flowName = "内部报告";
			}else if (${param.flowId}==<%=SubmitConstant.SubmitFlow_Report_Board%>) {
				flowName = "董事会文件";
			}else if (${param.flowId}==<%=SubmitConstant.SubmitFlow_HR_ResignApproval%>) {
				flowName = "员工离职审批表";
			}else if (${param.flowId}==<%=SubmitConstant.SubmitFlow_HR_ResignProcedure%>) {
				flowName = "员工离职手续表";
			}else if (${param.flowId}==<%=SubmitConstant.SubmitFlow_HR_DynamicPersonnel%>) {
				flowName = "人员动态审批表";
			}else if (${param.flowId}==<%=SubmitConstant.SubmitFlow_HR_TrainingApproval%>) {
				flowName = "培训报批处理单";
			}else if (${param.flowId}==<%=SubmitConstant.SubmitFlow_Report_Contract%>) {
				flowName = "合同审批";
			}else if (${param.flowId}==<%=SubmitConstant.SubmitFlow_Report_ContractCheck%>) {
				flowName = "合同验收";
			}
			
			//获取自定义列
			var col_names = ['Id', '正文标题', '创建时间', '申请人', '所属部门', '主办人', '浏览人'];
			var col_model = [{name:'instanceId',index:'instanceId', width:0, sorttype:"int", search:false, key:true, hidden:true},
                    {name:'instanceTitle',index:'instanceTitle', sortable:true, sorttype:"string", formatter:formatTitle},               
                    {name:'updateTime',index:'updateTime', width:70, align:'center'},
                    {name:'applier',index:'applier', width:40, align:'center'},
<<<<<<< .mine
                    {name:'department',index:'department', width:40, align:'center'},
                    {name:'charger',index:'charger', width:40, align:'center'},
                    {name:'do', width:25, align:'center', formatter:formatViewUser}];
=======
                    {name:'department',index:'department', width:40, align:'center'}
                  ];
>>>>>>> .r2041
			$.ajax({
				url: "/workflow/instanceInfor.do?method=getSearchFields&flowId=${param.flowId}",
				type: "post",
				dataType: "json",
				async: false,	//设置为同步
				beforeSend: function (xhr) {
				},
				complete : function (req, err) {
					var returnValues = eval("("+req.responseText+")");
					if (returnValues["_SearchFields"] != null && returnValues["_SearchFields"].length > 0) {
						for(var i=0;i<returnValues["_SearchFields"].length;i++) {
							var field = returnValues['_SearchFields'][i];
							if (field != null) {
								var searchType = field[0]["searchType"];
								var searchField = field[1]['searchField'];
								var searchOper = field[2]['searchOper'];
								var searchName = field[3]["searchName"];
								
<<<<<<< .mine
								col_names[7+i] = searchName;
=======
								col_names[5+i] = searchName;
>>>>>>> .r2041
								if (searchType == "orgSelect") {
<<<<<<< .mine
									col_model[7+i] = eval("({name:'"+searchField+"',index:'"+searchField+"', align:'center', width:'70', formatter:formatOrg})");
=======
									col_model[5+i] = eval("({name:'"+searchField+"',index:'"+searchField+"', align:'center', width:'70', formatter:formatOrg})");
>>>>>>> .r2041
								}else if (searchType == "usrSelect") {
<<<<<<< .mine
									col_model[7+i] = eval("({name:'"+searchField+"',index:'"+searchField+"', align:'center', width:'70', formatter:formatUsr})");
=======
									col_model[5+i] = eval("({name:'"+searchField+"',index:'"+searchField+"', align:'center', width:'70', formatter:formatUsr})");
>>>>>>> .r2041
								}else {
<<<<<<< .mine
									col_model[7+i] = eval("({name:'"+searchField+"',index:'"+searchField+"', align:'center', width:'70'})");
=======
									col_model[5+i] = eval("({name:'"+searchField+"',index:'"+searchField+"', align:'center', width:'70'})");
>>>>>>> .r2041
								}
							}
						}
					}
				}
			});
			var index = col_names.length;
			col_names[index] = "状态";
			col_model[index] = eval("({name:'status',index:'status', width:80, align:'center', formatter:formatStatus})");
			//alert(col_names);alert(col_model);
			    
			//加载表格数据
			var $mygrid = jQuery("#list"+${param.flowId}).jqGrid({
	            url:"/workflow/instanceInfor.do?method=list&flowId="+${param.flowId},
	            rownumbers: true,
	            datatype: "json",                
	            autowidth: true,
				height:300,
	            colNames: col_names,//表的第一行标题栏
	            colModel: col_model,
	            sortname: 'instanceId',
	            sortorder: 'desc',
	            multiselect: true,	//是否支持多选,可用于批量删除
	            viewrecords: true,
	            rowNum: 20,
	            rowList: [10,20,30],
	            scroll: false, 
	            scrollrows: false,                          
	            jsonReader:{
	                repeatitems: false
	            },
	            pager: "#pager"+${param.flowId},
	            caption: flowName
	        }).navGrid('#pager'+${param.flowId},{edit:false,add:false,del:false,search:false});       
			
			//自定义按钮
			jQuery("#list"+${param.flowId}).jqGrid('navButtonAdd','#pager'+${param.flowId}, {
				caption:"新增", title:"点击新增信息", buttonicon:'ui-icon-plusthick', onClickButton: addInfor
			});
			jQuery("#list"+${param.flowId}).jqGrid('navButtonAdd','#pager'+${param.flowId}, {
				caption:"<span style='color: red;'>批量删除</span>", title:"点击批量删除", buttonicon:'ui-icon-trash', onClickButton: deleteInfor
			});
			jQuery("#list"+${param.flowId}).jqGrid('navButtonAdd','#pager'+${param.flowId}, {
				caption:"查询", title:"点击查询", buttonicon:'ui-icon-search', onClickButton: openDialog
			});
			//如果是主办人或者管理员
			//alert(${_SYSTEM_USER.personId});
			if(${_SYSTEM_USER.personId == param.chargerId || _SYSTEM_USER.userType == 1}){
				jQuery("#list"+${param.flowId}).jqGrid('navButtonAdd','#pager'+${param.flowId}, {
					caption:"批量授权", title:"点击批量授权", buttonicon:'ui-icon-script', onClickButton: doAuthorize
				});
			}
			
			if (${param.flowId}==<%=SubmitConstant.SubmitFlow_Report_Contract%> && "${_IsContractRole}" == 'true') {
			jQuery("#list"+${param.flowId}).jqGrid('navButtonAdd','#pager'+${param.flowId}, {
				caption:"导出excel", title:"点击导出", buttonicon:'ui-icon-search', onClickButton: excel
			});
			}
			
			//打开查询窗口并进行窗口初始化
			var multiSearchParams = new Array();
			function openDialog() {
			    multiSearchParams[0] = "#list"+${param.flowId};				//列表Id
				multiSearchParams[1] = "#multiSearchDialog"+${param.flowId};//查询模态窗口Id
				
				initSearchDialog();
				
				//获取自定义表单查询字段
				getSearchFields();
				
			    $(multiSearchParams[1]).dialog("open");
			}
			
			//批量删除
			function deleteInfor(){
				//获取选择的行的Id
				var rowIds = jQuery("#list"+${param.flowId}).jqGrid('getGridParam','selarrrow'); 
					
				if(rowIds != null && rowIds.length > 0){
					var yes = window.confirm("确定要删除吗？");
					if (yes) {
						$.ajax({
							url: "/workflow/instanceInfor.do?method=recycle&flowId=${param.flowId}&rowIds="+rowIds,	//删除数据的url
							cache: false,
							type: "POST",
							dataType: "html",
							beforeSend: function (xhr) {						
							},
								
							complete : function (req, err) {
								var returnValues = eval("("+req.responseText+")");
								if (returnValues["_CanRecycle"]) {
									alert("数据已经删除！");
									$("#list"+${param.flowId}).trigger("reloadGrid");
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