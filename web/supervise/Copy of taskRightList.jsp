<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<script src="<c:url value="/"/>js/export.js" type="text/javascript"></script>

<script type="text/javascript">
	
	//新增
	function addInfor(){
		/*var returnArray = window.showModalDialog("/supervise/superviseInfor.do?method=edit&categoryId="+${param.categoryId},'',"dialogWidth:800px;dialogHeight:600px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		if(returnArray != null && returnArray[0] == "refresh") {
			//保存信息后重新加载tab
			loadTab(returnArray[1]);
		}*/
		window.open("/supervise/superviseInfor.do?method=edit&categoryId="+${param.categoryId}+"&departmentId="+${param.departmentId}, "_blank");
	}
	
	//修改
	function editInfor(rowId){
		/*var returnArray = window.showModalDialog("/supervise/superviseInfor.do?method=edit&documentId="+rowId,'',"dialogWidth:800px;dialogHeight:600px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		if(returnArray != null && returnArray[0] == "refresh") {
			//保存信息后重新加载tab
			loadTab(returnArray[1]);
		}*/
		window.open("/supervise/superviseInfor.do?method=edit&taskId="+rowId+"&categoryId="+${param.categoryId}, "_blank");
	}
	
	//授权
	function doAuthorize(rowId){
		var returnArray = window.showModalDialog("/supervise/superviseInfor.do?method=editInforRight&rowId="+rowId,'',"dialogWidth:800px;dialogHeight:600px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		if(returnArray != null && returnArray[0] == "refresh") {
			//保存信息后重新加载tab
			loadTab(returnArray[1]);
		}
	}
	
	//查看
	function viewInfor(rowId){
		//window.showModalDialog("/supervise/superviseInfor.do?method=view&rowId="+rowId,'',"dialogWidth:600px;dialogHeight:600px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		window.open("/supervise/superviseInfor.do?method=viewTask&rowId="+rowId,"_blank");
	}
</script>
<script type="text/javascript">
	//alert(${param.categoryId});
	//加载部门及联动信息
	$.myLoadDepartments = function(id1,id2) {
		//加载部门数据
		$.getJSON("/core/organizeInfor.do?method=getDepartments",function(data) {
			if (data != null) {
			    $.each(data._Departments, function(i, n) {
				    $("#"+id1).append("<option value='"+ n.organizeId + "'>" + n.organizeName + "</option>");
				});
				$("#"+id2).selectInit();
			}
		});
		
		//不含下拉班组信息时,改变部门信息,联动用户信息
		$("#"+id1).bind("change",function(){
            
            var depSelectId = $("#"+id1+" option:selected").val();
            var url = "/core/systemUserInfor.do?method=getUsers&departmentId=" + depSelectId;
            //alert(url);
            $.myLoadUsers(url,id2);     
		});
	}
		 
	//获取用户信息
	$.myLoadUsers = function(url,id2) {
		$.getJSON(url,function(data) {
			if (data != null) {
				$.each(data._Users, function(i, n) {
					$("#"+id2).append("<option value='"+ n.person.personId + "'>" + n.person.personName + "</option>");
				});
			}
		});
	}
</script>

<title></title>

  		<div>
			<table id="list${param.categoryId}${param.departmentId}"></table>
			<div id="pager${param.categoryId}${param.departmentId}"></div>
		</div>
		
		<!-- 查询框 -->
		<div id="multiSearchDialog${param.categoryId}${param.departmentId}" style="display: none;">  
		    <table>
		        <tbody> 
		            <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="taskName"/>督办名称：
		                    <input type="hidden" class="searchOper" value="cn"/>
		                </td>  
		                <td>  
		                    <input type="text" class="searchString" size="25"/>  
		                </td>  
		            </tr>
		           <!-- <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="documentCode"/>编号：
		                    <input type="hidden" class="searchOper" value="cn"/>
		                </td>  
		                <td>  
		                    <input type="text" class="searchString"/>
		                </td>  
		            </tr>
		            <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="keyword"/>关键字：
		                    <input type="hidden" class="searchOper" value="cn"/>
		                </td>  
		                <td>  
		                    <input type="text" class="searchString"/>
		                </td>  
		            </tr>
		            --><!-- <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="description"/>说明：
		                    <input type="hidden" class="searchOper" value="cn"/>
		                </td>  
		                <td>  
		                    <input type="text" class="searchString"/>
		                </td>  
		            </tr> -->
		            <!-- <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="category.categoryId"/>分类：
		                    <input type="hidden" class="searchOper" value="eq"/>
		                </td>  
		                <td>  
		                    <select class="searchString" id="searchCategoryId">
		                    	<c:forEach items="${requestScope._TREE}" var="cate" varStatus="status"> 
									<option value="${cate.categoryId}">
										<c:forEach begin="0" end="${cate.layer}">&nbsp;</c:forEach>
										<c:if test="${cate.layer==1}"><b>+</b></c:if>
										<c:if test="${cate.layer==2}"><b>-</b></c:if>
										${cate.categoryName}				
									</option>
								</c:forEach> 
		                    </select>
		                </td>  
		            </tr> --><!--
		            <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="author.person.personId"/>作者：
		                    <input type="hidden" class="searchOper" value="eq"/>
		                </td>  
		                <td>  
		                    <select id="departmentId${param.categoryId}${param.departmentId}"></select>
		                    <select class="searchString" id="personId${param.categoryId}${param.departmentId}"></select>
		                </td>  
		            </tr>
		            <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="createTime"/>创建时间：
		                    <input type="hidden" class="searchOper" value="ge"/>
		                </td>  
		                <td>  
		                    <input class="searchString" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly="true" size="12"/>
		                </td>  
		            </tr>
		            --><!-- <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="editor.person.personName"/>最后更新者：
		                    <input type="hidden" class="searchOper" value="cn"/>
		                </td>  
		                <td>  
		                    <input type="text" class="searchString"/>
		                </td>  
		            </tr>
		            
		            <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="updateTime"/>更新时间：
		                    <input type="hidden" class="searchOper" value="le"/>
		                </td>  
		                <td>  
		                    <input class="searchString" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly="true" size="12"/>
		                </td>  
		            </tr> -->
		            <!--<tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="commended"/>精华文档：
		                    <input type="hidden" class="searchOper" value="eq"/>
		                </td>  
		                <td>  
		                    <select class="searchString" id="commended${param.categoryId}${param.departmentId}">
		                    	<option value="">--请选择--</option>
		                    	<option value="1">推荐</option>
		                    	<option value="0">不推荐</option>
		                    </select>
		                </td>  
		            </tr>
		        --></tbody>  
		    </table>  
		</div>
		
		<script type="text/javascript"> 		
			
		    
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
				html = "<a href='javascript:;' onclick='viewInfor("+options.rowId+")'>" + cellValue + "</a>";				
				return html;
			}
			//自定义操作栏的显示内容
		    function formatOperation(cellvalue, options, rowdata) {
	           var returnStr = "<a href='javascript:;' onclick='editInfor("+options.rowId+")'>[修改]</a>";
	           //returnStr += " <a href='javascript:;' onclick='doAuthorize("+options.rowId+")'>[授权]</a>";
	           //  <a href='javascript:;' onclick='viewInfor("+options.rowId+")'>[查看]</a>
	           return returnStr;
		    }
		   	//状态
			function formatStatus(cellvalue, options, rowdata) {
				var returnStr = "<font color=black><b>";
				if(cellvalue == 1){
					returnStr += "任务已下达，待指定责任人";
				}
				if(cellvalue == 2){
					returnStr += "工作进行中";//已指定责任人，
				}
				/*if(cellvalue == 3){
					returnStr += "已到期，完成报告制作中";
				}*/
				if(cellvalue == 3){
					if(rowdata.delayDate != null && rowdata.delayDate != "undefined"){
						returnStr += "延迟工作进行中";
					}else if(rowdata.delayDate == null && rowdata.delayDate2 == null && rowdata.delayDate3 == null){
						returnStr += "已提交完成报告，部门审核中";
					}
					
				}
				if(cellvalue == 4){
					returnStr += "行政助理审核中";
				}
				if(cellvalue == 5){
					returnStr += "领导审核中";
				}
				if(cellvalue == 6){
					if(rowdata.delayDate == null || rowdata.delayDate == "undefined"){
						returnStr += "延迟时间设定中";
						//returnStr += "延迟工作设定中，等待部门负责人提供延迟时间";
					}else{
						returnStr += "延迟工作进行中";
					}
					
				}
				if(cellvalue == 7){
				
					
			        
					//if(rowdata.endTime != null && rowdata.endTime != 'undefined'){
						if(dateCompare(rowdata.endTime,rowdata.finishDate,1)){
							$.ajaxSetup({async: false});
							$.getJSON("/supervise/superviseInfor.do?method=getLastReportTime&taskId="+options.rowId,function(data) {
					        	var lastTime = data._LastTime;
					        	//alert(dateCompare(lastTime,rowdata.finishDate,1));
					        	if(dateCompare(lastTime,rowdata.finishDate,4)){
					           		returnStr += "已完成";
					        	}
					        	if(dateCompare(lastTime,rowdata.finishDate,1)){
					        		returnStr += "<font color=blue>完成（延迟）</font>";
					        	}
					        });
						    
							//alert(returnStr);
						}
						if(dateCompare(rowdata.endTime,rowdata.finishDate,4)){
							returnStr += "已完成";
						}
					//}else{
					//	returnStr += "已完成";
					//}
				}
				returnStr += "</b></font>";
				return returnStr;
			}
			
			var colNames = ['Id','督办事项','下达时间','计划完成日期','实际完成日期','责任部门','责任人','下发人','状态'];
			var colModel=[
	                {name:'taskId',index:'taskId', width:0, sorttype:"int", search:false, key:true, hidden:true},
	                {name:'taskName',index:'taskName',align:'left',width:40, sortable:true,sorttype:"string",formatter:formatTitle},
	                {name:'createDate',index:'createDate', width:20, align:'center'},
	                {name:'finishDate',index:'finishDate', width:25, align:'center'},
	                {name:'endTime',index:'endTime', width:25, align:'center'},
	                {name:'organizeInfor.organizeName',index:'organizeInfor.organizeName', width:20, align:'center'},
	                {name:'operator.person.personName',index:'operator.person.personName', width:15, align:'center'},
	                {name:'creater.person.personName',index:'creater.person.personName', width:15, align:'center'},
	                {name:'status',index:'status', align:'center', width:50, search:false,formatter:formatStatus}
	                //{name:'operate', align:'center', width:50, search:false, sortable:false, formatter:formatOperation}
	            ];
	        if(${!param.leaf}){
	        	colNames = ['Id','分类','督办事项','下达时间','计划完成日期','实际完成日期','责任部门','责任人','下发人','状态'];
				colModel=[
	                {name:'taskId',index:'taskId', width:0, sorttype:"int", search:false, key:true, hidden:true},
	                {name:'taskCategory.categoryName',index:'taskCategory.categoryName', width:25, align:'center'},
	                {name:'taskName',index:'taskName',align:'left',width:40, sortable:true,sorttype:"string",formatter:formatTitle},
	                {name:'createDate',index:'createDate', width:20, align:'center'},
	                {name:'finishDate',index:'finishDate', width:25, align:'center'},
	                {name:'endTime',index:'endTime', width:25, align:'center'},
	                {name:'organizeInfor.organizeName',index:'organizeInfor.organizeName', width:20, align:'center'},
	                {name:'operator.person.personName',index:'operator.person.personName', width:15, align:'center'},
	                {name:'creater.person.personName',index:'creater.person.personName', width:15, align:'center'},
	                {name:'status',index:'status', align:'center', width:50, search:false,formatter:formatStatus}
	                //{name:'operate', align:'center', width:50, search:false, sortable:false, formatter:formatOperation}
	            ];
	        }
		   
			//加载表格数据
			var $mygrid = jQuery("#list"+${param.categoryId}${param.departmentId}).jqGrid({
			
                url:"/supervise/superviseInfor.do?method=getTaskInfor&categoryId="+${param.categoryId}+"&departmentId="+${param.departmentId},
                rownumbers: true,
                datatype: "json",                
               	autowidth: true,
				height:300,//document.documentElement.clientHeight-97
                colNames:colNames,//表的第一行标题栏  ,'相关操作'
	            colModel:colModel,
                sortname: 'taskId',
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
                pager: "#pager"+${param.categoryId}${param.departmentId}
	        }).navGrid('#pager'+${param.categoryId}${param.departmentId},{edit:false,add:false,del:false,search:false});       
			
			//自定义按钮
			if (${param.isRoot != 'true' && param.leaf}) {
				//为根分类时不显示
				jQuery("#list"+${param.categoryId}${param.departmentId}).jqGrid('navButtonAdd','#pager'+${param.categoryId}${param.departmentId}, {
					caption:"新增", title:"点击新增督办", buttonicon:'ui-icon-plusthick', onClickButton: addInfor
				});
				
				jQuery("#list"+${param.categoryId}${param.departmentId}).jqGrid('navButtonAdd','#pager'+${param.categoryId}${param.departmentId}, {
					caption:"<span style='color: red;'>批量删除</span>", title:"点击批量删除", buttonicon:'ui-icon-closethick', onClickButton: deleteInfor
				});
			}
			
			jQuery("#list"+${param.categoryId}${param.departmentId}).jqGrid('navButtonAdd','#pager'+${param.categoryId}${param.departmentId}, {
				caption:"查询", title:"点击查询", buttonicon:'ui-icon-search', onClickButton: openDialog
			});
			
			jQuery("#list"+${param.categoryId}${param.departmentId}).jqGrid('navButtonAdd','#pager'+${param.categoryId}${param.departmentId}, {
				caption:"导出Excel", title:"点击导出到Excel", buttonicon:'ui-icon-calculator', onClickButton: exportExcel
			});
			
			/*jQuery("#list"+${param.categoryId}${param.departmentId}).jqGrid('navButtonAdd','#pager'+${param.categoryId}${param.departmentId}, {
				caption:"导出到Excel", title:"点击导出到Excel", buttonicon:'ui-icon-calculator', onClickButton: function(){
					//jQuery("#list"+${param.categoryId}).jqGrid('excelExport',{
					//	url:'/workflow/instanceInfor.do?method=list&sidx=instanceId&sord=desc&_search=false&rows=10&page=1&flowId='+${param.categoryId}
					//});
					//jQuery("#list"+${param.categoryId}${param.departmentId}).jqGrid('jqgridToExcel',{fileName:'test',fdate:'2014-12-11'});
					jQuery("#list"+${param.categoryId}${param.departmentId}).jqgridToExcel('test','2014-12-11');
				}
			});*/
			
			/********导出excel*********/
			function exportExcel(){
				var rules = "";   
				var param0 = "#list"+${param.categoryId}${param.departmentId};
				var param1 = "#multiSearchDialog"+${param.categoryId}${param.departmentId};
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
			    
			    var url = "/supervise/superviseInfor.do?method=expertExcel&categoryId="+${param.categoryId}+"&departmentId="+${param.departmentId}+"&_search=true&page=1&rows=100000&sidx=taskId&sord=desc&filters="+filtersStr;
			    window.location.href=url;
			}
			
			
			
			//打开查询窗口并进行窗口初始化
			var multiSearchParams = new Array();
			function openDialog() {
			    multiSearchParams[0] = "#list"+${param.categoryId}${param.departmentId};				//列表Id
				multiSearchParams[1] = "#multiSearchDialog"+${param.categoryId}${param.departmentId};//查询模态窗口Id
				
				initSearchDialog();
				
			    $(multiSearchParams[1]).dialog("open");
			}
			
			//批量删除
			function deleteInfor(){
				//doDelete("/supervise/superviseInfor.do?method=delete","list"+${param.categoryId}${param.departmentId});
				var listId = "list"+${param.categoryId}${param.departmentId};
				var url = "/supervise/superviseInfor.do?method=delete&categoryId="+${param.categoryId};
				
				//获取选择的行的Id
				var rowIds = jQuery("#"+listId).jqGrid('getGridParam','selarrrow'); 
					
				if(rowIds != null && rowIds.length > 0){
					var yes = window.confirm("确定要删除吗？");
					if (yes) {
						$.ajax({
							url: url+"&rowIds="+rowIds,	//删除数据的url
							cache: false,
							type: "POST",
							dataType: "html",
							beforeSend: function (xhr) {						
							},
								
							complete : function (req, err) {
								//alert("数据已经删除！");
								//$("#"+listId).trigger("reloadGrid"); 
							},
							success : function (msg) {
								//alert(msg);
								if(msg == 'suc'){
									alert("数据删除成功！");
									$("#"+listId).trigger("reloadGrid"); 
								}
								if(msg == 'fail'){
									//alert("同步失败，请重试！");
									var errorMsg = "对不起,您无权进行该操作,请与系统管理员联系!";
									var returnArray = window.showModalDialog("/common/error.jsp?_ErrorMessage="+errorMsg,'',"dialogWidth:800px;dialogHeight:600px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
								}
							}
						});	
					}
				}else {
					alert("请选择要删除的数据！");
				}	
			}
			
			//部门信息初始化
			$('#departmentId'+${param.categoryId}${param.departmentId}).selectInit();
			
			//加载部门及联动信息		
			$.myLoadDepartments("departmentId"+${param.categoryId}${param.departmentId},"personId"+${param.categoryId}${param.departmentId});
			
			/*jQuery().ready(function (){
		    //获取部门信息(查询条件)
		    $.getJSON("/core/organizeInfor.do?method=getDepartments",function(data) {
		           var options = "<option value=''>--选择部门--</option>";
		           $.each(data._Departments, function(i, n) {
		               options += "<option value='"+n.organizeId+"'>"+n.organizeName+"</option>";   
		           });   
		           $('#departmentId').html(options);
		        }   
		    );
		});*/
			
		</script>