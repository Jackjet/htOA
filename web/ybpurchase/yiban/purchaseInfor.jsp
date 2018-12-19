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
<script src="js/jquery-1.9.1.js" type="text/javascript"></script>

<%--<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />--%>
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>css/base/jquery-ui-1.9.2.custom.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/tabstyle.css" />

<script type="text/javascript">

//    window.onfocus = function() {
//        if($("#freshflag").val() == "fresh"){ // 表示要刷新
////            alert($("#freshflag").val());
//            $("#freshflag").value = "";
////            alert($("#freshflag").val());
//            window.location.reload();
//        }
//    };
//    window.onblur = function() {
////        alert($("#freshflag").val());
//
//        $("#freshflag").value = "fresh";
//    };


    // 各种浏览器兼容
    var hidden, state, visibilityChange;
    if (typeof document.hidden !== "undefined") {
        hidden = "hidden";
        visibilityChange = "visibilitychange";
        state = "visibilityState";
    } else if (typeof document.mozHidden !== "undefined") {
        hidden = "mozHidden";
        visibilityChange = "mozvisibilitychange";
        state = "mozVisibilityState";
    } else if (typeof document.msHidden !== "undefined") {
        hidden = "msHidden";
        visibilityChange = "msvisibilitychange";
        state = "msVisibilityState";
    } else if (typeof document.webkitHidden !== "undefined") {
        hidden = "webkitHidden";
        visibilityChange = "webkitvisibilitychange";
        state = "webkitVisibilityState";
    }

    // 添加监听器，在title里显示状态变化
    document.addEventListener(visibilityChange, function() {
        //document.title = document[state];
        if(document.hidden){

//            document.title = '隐藏本页';
        }else{
//            document.title ='显示本页';
//            alert(111);
                window.location.reload();
        }
    }, false);



	function getViewUsers(obj,purchaseId){
		var html = "<br/>";
		
		$.getJSON("/ybpurchase/purchaseInfor.do?method=getViewUsers&purchaseId="+purchaseId,function(data) {
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
							html += "<span style='white-space:nowrap;'><font color=yellow>"+personName+"</font></span>&nbsp;&nbsp;";
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


	//新增
	function addInfor(){
        window.open("/ybpurchase/purchaseInfor.do?method=edit&flowId="+${param.flowId}, "_blank");
	}

	//查看
	function doView(rowId){
		//window.showModalDialog("/ybpurchase/purchaseInfor.do?method=view&purchaseId="+rowId,'',"dialogWidth:900px;dialogHeight:800px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		window.open("/ybpurchase/purchaseInfor.do?method=view&purchaseId="+rowId, "_blank");
	}		
	
	//授权
	function doAuthorize(){
		//获取选择的行的Id
		var rowIds = jQuery("#list"+${param.flowId}).jqGrid('getGridParam','selarrrow'); 
			
		if(rowIds != null && rowIds.length > 0){

            window.open("/ybpurchase/purchaseInfor.do?method=editInforsRight&rowIds="+rowIds, "_blank");
		}else{
			alert("请选择要授权的数据！");
		}
	}
    //批量提交
    function updates(){
        //获取选择的行的Id
        var rowIds = jQuery("#list"+${param.flowId}).jqGrid('getGridParam','selarrrow');
        var packageNames = $("#packageName").val();
//        alert(packageNames);
//        if(rowIds != null && rowIds.length > 0){
//
//            window.open("/ybpurchase/purchaseInfor.do?method=updates&rowIds="+rowIds);
//        }else{
//            alert("请选择要授权的数据！");
//        }
        if(rowIds != null && rowIds.length > 0){
            var yes = window.confirm("确定要提交吗？");
            if (yes) {
                $.ajax({
                    url: "/ybpurchase/purchaseInfor.do?method=updates&rowIds="+rowIds+"&packageName="+packageNames,
                    cache: false,
                    type: "POST",
                    dataType: "html",
                    beforeSend: function (xhr) {
                    },
                    complete : function (req, err) {
//                        var returnValues = eval("("+req.responseText+")");
//                        if (returnValues["_CanRecycle"]) {
                            alert("数据已经提交！");
                            $("#list"+${param.flowId}).trigger("reloadGrid");
//                        }else {
//                            alert("对不起,您无权进行该操作,请与系统管理员联系!");
//                        }
                    }
                });
            }
        }else {
            alert("请选择要提交的数据！");
        }
        $("#packageName1").dialog("close");

    }

	//查看
	function excel(){

		var yes = window.confirm("确定要导出数据吗？");
					if (yes) {
			printStr(${param.flowId});
		}
	}

	//打开查询窗口
	function openMultipleSearchDialog1() {



        $("#packageName1").dialog({
            autoOpen: false,
            modal: true,
            resizable: false,
            width: 350,
            title: "包名",
            buttons: {
                "确定": updates,
                "重置": clearSearch
            }
        });
        $("#packageName1").dialog("open");

	}
</script>
<style>
	li,a:focus{outline:none;!important; color:white!important;}
	#multiSearchDialog86{
		height: 260px!important;
	}
</style>
<title></title>
<body style="border:1px solid #0DE8F5;border-radius: 5px;background-image: url('/img/bgIn.png');background-size: cover;">
  		<div>
			<table id="list${param.flowId}"></table>
			<div id="pager${param.flowId}"></div>
		</div>



		<div id="viewUser" style="display:none;"></div>

		<div id="packageName1" style="display: none;overflow: auto">
			<table >
				<tbody>
				<tr>
					<td>
						<input type="hidden" class="searchField" value="packageName"/>包名：
					</td>
					<td>
						<input type="text" class="searchString" id="packageName" size="35"/>
					</td>
				</tr>
				</tbody>
			</table>
		</div>

		<!-- 查询框 -->
		<div id="multiSearchDialog${param.flowId}" style="display: none;overflow: auto">
		    <table >
		        <tbody>
		            <tr>
		                <td>
		                    <input type="hidden" class="searchField" value="purchaseStr1"/>标题：
		                    <input type="hidden" class="searchOper" value="cn"/>
		                </td>
		                <td>
		                    <input type="text" class="searchString" size="25"/>
		                </td>
		            </tr>
					<tr>
						<td>
							<input type="hidden" class="searchField" value="purchaseTitle"/>采购物品：
							<input type="hidden" class="searchOper" value="cn"/>
						</td>
						<td>
							<input type="text" class="searchString" size="25"/>
						</td>
					</tr>
		            <tr>
		                <td>
		                    <input type="hidden" class="searchField" value="startTime"/>创建时间：
		                    <input type="hidden" class="searchOper" value="ge"/>
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
							<input type="hidden" class="searchField" value="batchNumber"/>申请批次：
							<input type="hidden" class="searchOper" value="eq"/>
						</td>
						<td>
							<input type="text" class="searchString" size="25"/>
						</td>
		            </tr>
					<tr>
						<td>
							<input type="hidden" class="searchField" value="purchaseStr2"/>归属公司：
							<input type="hidden" class="searchOper" value="eq"/>
						</td>
						<td>
							<input type="text" class="searchString" size="25"/>
						</td>
					</tr>


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
				html += "<a href='#none' class='tooltip' onClick='getViewUsers(this,"+rowObject.purchaseId+");'";
				html +=" title='单击查看此公文的浏览人员' style='z-index:9999;'>";
				html += "<img src='<c:url value="/"/>img/llr.png' width='20' height='16' border='0'/>"
				html +=" </a>";
				return html;
			}
			
			//自定义查看统计
			function formatViewCount(cellValue, options, rowObject) {
				var html = '';
				html += "<a href='<c:url value="/"/>purchase/purchaseInfor.do?method=viewCount&purchaseId="+rowObject.purchaseId+"' target='_blank' class='tooltip' ";//  onClick='getViewUsers(this,"+rowObject.purchaseId+");'";
				html +=" title='单击查看此公文的审核统计信息' style='z-index:9999;'>";
				html += "<img src='<c:url value="/"/>images/menu/asset.gif' width='16' height='16' border='0'/>"
				html +=" </a>";
				return html;
			}
			
			var flowName = "一般采购";



			//获取自定义列
			var col_names = ['Id','标题', '采购物品','数量','单位','预算类型', '创建时间', '申请人', '所属部门','归属公司','能否审核','技规部','预计价格','申请批次'];
			var col_model = [{name:'purchaseId',index:'purchaseId', width:0, sorttype:"int", search:false, key:true, hidden:true},
                    {name:'purchaseStr1',index:'purchaseStr1',width:260,align:'center', sortable:true, sorttype:"string", formatter:formatTitle},
                    {name:'purchaseTitle',index:'purchaseTitle',width:100,align:'center', sortable:true, sorttype:"string", formatter:formatTitle},
                    {name:'purchaseNumber',index:'purchaseNumber', width:60, align:'center'},
                    {name:'purchaseGoods',index:'purchaseGoods', width:60, align:'center'},
                    {name:'ysType',index:'ysType', width:60, align:'center'},
                    {name:'startTime',index:'startTime', width:100, align:'center'},
                    {name:'applier',index:'applier', width:100, align:'center'},
                    {name:'department',index:'department', width:100, align:'center'},
                    {name:'purchaseStr2',index:'purchaseStr2', width:100, align:'center'},
                    {name:'canCheck',index:'canCheck', width:50, align:'center',hidden:true},
                    {name:'jiguibu',index:'jiguibu', width:50, align:'center',hidden:true},
                    {name:'purchaseMoney',index:'purchaseMoney', width:100, align:'center'},
                    {name:'batchNumber',index:'batchNumber', width:100, align:'center'}
//                    {name:'do', width:80, align:'center', formatter:formatViewUser},
                  ];

			var index = col_names.length;
			col_names[index] = "状态";
			col_model[index] = eval("({name:'status',index:'status',  align:'center', formatter:formatStatus})");/*width:80,*/
			
			
			//查看统计
			<%--if("${_SYSTEM_USER.userType}" == "1"){--%>
				<%--col_names[index+1] = "统计";--%>
				<%--col_model[index+1] = eval("({name:'viewCount',index:'viewCount',  align:'center',formatter:formatViewCount})");/*width:30,*/--%>
            <%--}--%>
			
			//alert(col_names);alert(col_model);

            var lastjigui = null;
            var lastjigui2 = null;
			//加载表格数据
			var $mygrid = jQuery("#list"+${param.flowId}).jqGrid({
	            url:"/ybpurchase/purchaseInfor.do?method=list&flowId="+${param.flowId},
	            rownumbers: true,
	            datatype: "json",
                width:"90%",
	            autowidth: true,
				height:document.documentElement.clientHeight-140,
	            colNames: col_names,//表的第一行标题栏
	            colModel: col_model,
	            sortname: 'rowObjectId',
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
	            pager: "#pager"+${param.flowId},
//	            caption: flowName,


                onSelectRow:function(rowid,status){
					var rowData = $("#list"+${param.flowId}).jqGrid('getRowData',rowid);
					lastjigui2 = rowData.jiguibu;
					if(lastjigui == null){
						lastjigui = rowData.jiguibu;
					}
					if(!status){
					   lastjigui = null;
					}else {
						if (lastjigui2 != lastjigui){
							$("#list"+${param.flowId}).jqGrid("setSelection", rowid,false);//设置该行不能被选中。
							alert("此采购计划技规部领导选择不同，不能选中！");//提示信息
						}else{
							lastjigui = rowData.jiguibu;
						}
						if(rowData.canCheck == "false") {//禁用条件
							$("#list"+${param.flowId}).jqGrid("setSelection", rowid,false);//设置该行不能被选中。
							alert("此采购计划不能提交！");//提示信息
						}
					}
       		    }
	        }).navGrid('#pager'+${param.flowId},{edit:false,add:false,del:false,search:false}).closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });

			//自定义按钮
            <%--jQuery("#list"+${param.flowId}).jqGrid('navButtonAdd','#pager'+${param.flowId}, {--%>
                <%--caption:"刷新表格", title:"点击刷新表格",buttonicon:'none', onClickButton: function refresh(){window.location.reload();}--%>
            <%--});--%>
			if(${cgjs}){
				jQuery("#list"+${param.flowId}).jqGrid('navButtonAdd','#pager'+${param.flowId}, {
					caption:"新增", title:"点击新增信息", buttonicon:'ui-icon-plusthick', onClickButton: addInfor
				});
			}
			if(${canUpdates}){
            jQuery("#list"+${param.flowId}).jqGrid('navButtonAdd','#pager'+${param.flowId}, {
                caption:"批量提交", title:"点击批量提交", buttonicon:'ui-icon-script', onClickButton: openMultipleSearchDialog1
            });}
            <%--if(${_SYSTEM_USER.userType == 1}){--%>
				<%--jQuery("#list"+${param.flowId}).jqGrid('navButtonAdd','#pager'+${param.flowId}, {--%>
					<%--caption:"<span style='color: red;'>批量删除</span>", title:"点击批量删除", buttonicon:'ui-icon-trash', onClickButton: deleteInfor--%>
				<%--});--%>
            <%--}--%>
			jQuery("#list"+${param.flowId}).jqGrid('navButtonAdd','#pager'+${param.flowId}, {
				caption:"查询", title:"点击查询", buttonicon:'ui-icon-search', onClickButton: openDialog
			});
			
			//如果是主办人或者管理员
			//alert(${_SYSTEM_USER.personId});
			<%--if(${_SYSTEM_USER.personId == param.chargerId || _SYSTEM_USER.userType == 1}){--%>
				<%--jQuery("#list"+${param.flowId}).jqGrid('navButtonAdd','#pager'+${param.flowId}, {--%>
					<%--caption:"批量授权", title:"点击批量授权", buttonicon:'ui-icon-script', onClickButton: doAuthorize--%>
				<%--});--%>
			<%--}--%>
			
			<%--if ((${param.flowId}==<%=SubmitConstant.SubmitFlow_Report_Contract%> && ("${_IsContractRole}" == 'true' || "${_SYSTEM_USER.userType}" == "1" || "${_SYSTEM_USER.personId}" == "207")) --%>
					<%--|| (${param.flowId}==<%=SubmitConstant.SubmitFlow_Report_Publish%> && ("${_SYSTEM_USER.userType}" == "1" || "${_SYSTEM_USER.personId}" == "207"))--%>
					<%--|| (${param.flowId}==<%=SubmitConstant.SubmitFlow_Report_Receive%> && ("${_SYSTEM_USER.userType}" == "1" || "${_SYSTEM_USER.personId}" == "207"))) { // && "${_IsContractRole}" == 'true'--%>
				<%--/*jQuery("#list"+${param.flowId}).jqGrid('navButtonAdd','#pager'+${param.flowId}, {--%>
					<%--caption:"导出excel", title:"点击导出", buttonicon:'ui-icon-search', onClickButton: excel--%>
				<%--});*/--%>
				<%----%>
				<%--jQuery("#list"+${param.flowId}).jqGrid('navButtonAdd','#pager'+${param.flowId}, {--%>
					<%--caption:"导出Excel", title:"点击导出到Excel", buttonicon:'ui-icon-calculator', onClickButton: exportExcel--%>
				<%--});--%>
			<%--}--%>

            //打开查询窗口并进行窗口初始化
			var multiSearchParams = new Array();
			function openDialog() {

			    multiSearchParams[0] = "#list"+${param.flowId};				//列表Id
				multiSearchParams[1] = "#multiSearchDialog"+${param.flowId};//查询模态窗口Id
				
				initSearchDialog();
				
				//获取自定义表单查询字段
//				getSearchFields();
				
			    $(multiSearchParams[1]).dialog("open");
			}
			
			/********导出excel*********/
			function exportExcel(){
				var yes = window.confirm("数据较多时，导出所需时间较长，确定要导出数据吗？");
				if (yes) {
					var rules = "";   
					var param0 = "#list"+${param.flowId};
					var param1 = "#multiSearchDialog"+${param.flowId};
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
				    
				    var url = "/ybpurchase/purchaseInfor.do?method=expertExcel&flowId="+${param.flowId}+"&_search=true&page=1&rows=100000&sidx=purchaseId&sord=desc&filters="+filtersStr;
				    window.location.href=url;
				}
				
			}

			//批量删除
			function deleteInfor(){
				//获取选择的行的Id
				var rowIds = jQuery("#list"+${param.flowId}).jqGrid('getGridParam','selarrrow'); 
					
				if(rowIds != null && rowIds.length > 0){
					var yes = window.confirm("确定要删除吗？");
					if (yes) {
						$.ajax({
							url: "/ybpurchase/purchaseInfor.do?method=recycle&flowId=${param.flowId}&rowIds="+rowIds,	//删除数据的url
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