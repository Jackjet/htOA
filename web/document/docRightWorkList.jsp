<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">


<script type="text/javascript">
	
	
	//查看
	function viewInfor(rowId){
		//window.showModalDialog("/document/document.do?method=view&rowId="+rowId,'',"dialogWidth:600px;dialogHeight:600px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		window.open("/document/document.do?method=view&rowId="+rowId+"&categoryId="+${param.categoryId},"_blank");
	}
	
	//授权
	function doAuthorize(rowId){
//		var returnArray = window.showModalDialog("/document/document.do?method=editInforRight&rowId="+rowId,'',"dialogWidth:800px;dialogHeight:600px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
//		if(returnArray != null && returnArray[0] == "refresh") {
//			//保存信息后重新加载tab
//			loadTab(returnArray[1]);
//		}
        window.open("/document/document.do?method=editInforRight&rowId="+rowId+"&categoryId="+${param.categoryId},"_blank");
	}
	
	
	//获取自定义表单查询字段
	function getSearchFields(categoryid) {
		$.ajax({
			url: "/document/document.do?method=getSearchFields&categoryId="+categoryid,
			type: "post",
			dataType: "json",
			async: false,	//设置为同步
			beforeSend: function (xhr) {
			},
			complete : function (req, err) {
				$('#searchFields${param.categoryId}').empty();
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
							$('#searchFields${param.categoryId}').append(searchFieldHtml);
						}
					}
				}
			}
		});
	}
	
</script>


<title></title>
<body style="border:1px solid #0DE8F5;border-radius:0 5px 5px 5px;">
  		<div>
			<table id="list${param.categoryId}"></table>
			<div id="pager${param.categoryId}"></div>
		</div>
		
		<!-- 查询框 -->
		<div id="multiSearchDialog${param.categoryId}" style="display: none;">  
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
		                    <select id="departmentId${param.categoryId}"></select>
		                </td>  
		            </tr>

					<tr>
		                <td>  
		                    <input type="hidden" class="searchField" value="applier.personId"/>申请人：
		                    <input type="hidden" class="searchOper" value="eq"/>
		                </td>  
		                <td>  
		                    <select class="searchString" id="applierId${param.categoryId}"></select>
		                </td>  
		            </tr>
		        </tbody>
		        <tbody id="searchFields${param.categoryId}"></tbody>
		    </table>  
		</div>
</body>
		<script type="text/javascript"> 		
			
			//自定义操作栏的显示内容
		    function formatOperation(cellvalue, options, rowdata) {
	           var returnStr = " <a href='javascript:;' onclick='doAuthorize("+options.rowId+")'>[授权]</a>";
	           return returnStr;
		    }
		    
			//标题查看链接
			function formatTitle(cellValue, options, rowObject) {				
				var html = '';
				html = "<a href='javascript:;' onclick='viewInfor("+options.rowId+")'>" + cellValue + "</a>";
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
			
			//获取自定义列
			var col_names = ['Id', '正文标题', '创建时间', '申请人', '所属部门', '主办人'];
			var col_model = [{name:'instanceId',index:'instanceId', width:0, sorttype:"int", search:false, key:true, hidden:true},
                    {name:'instanceTitle',index:'instanceTitle', sortable:true, sorttype:"string", formatter:formatTitle},
                    {name:'updateTime',index:'updateTime', width:70, align:'center'},
                    {name:'applier',index:'applier', width:40, align:'center'},
                    {name:'department',index:'department', width:40, align:'center'},
                    {name:'charger',index:'charger', width:40, align:'center'}];
			$.ajax({
				url: "/document/document.do?method=getSearchFields&categoryId=${param.categoryId}",
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
								
								col_names[6+i] = searchName;
								if (searchType == "orgSelect") {
									col_model[6+i] = eval("({name:'"+searchField+"',index:'"+searchField+"', align:'center', width:'70', formatter:formatOrg})");
								}else if (searchType == "usrSelect") {
									col_model[6+i] = eval("({name:'"+searchField+"',index:'"+searchField+"', align:'center', width:'70', formatter:formatUsr})");
								}else {
									col_model[6+i] = eval("({name:'"+searchField+"',index:'"+searchField+"', align:'center', width:'70'})");
								}
							}
						}
					}
				}
			});
			var index = col_names.length;
			col_names[index] = "操作";
			col_model[index] = eval("({name:'htmlFilePath', align:'center', width:50, search:false, sortable:false, formatter:formatOperation})");
			//alert(col_names);alert(col_model);
		   
			//加载表格数据
			var $mygrid = jQuery("#list"+${param.categoryId}).jqGrid({
			
                url:"/document/document.do?method=getDocumentInfor&categoryId="+${param.categoryId},
                rownumbers: true,
                datatype: "json",                
               	autowidth: true,
                height:document.documentElement.clientHeight-235,
                colNames: col_names,//表的第一行标题栏
	            colModel: col_model,
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
                pager: "#pager"+${param.categoryId}
	        }).navGrid('#pager'+${param.categoryId},{edit:false,add:false,del:false,search:false}).closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
			
			//自定义按钮
			jQuery("#list"+${param.categoryId}).jqGrid('navButtonAdd','#pager'+${param.categoryId}, {
				caption:"<span style='color: red;'>批量删除</span>", title:"点击批量删除", buttonicon:'ui-icon-closethick', onClickButton: deleteInfor
			});
			jQuery("#list"+${param.categoryId}).jqGrid('navButtonAdd','#pager'+${param.categoryId}, {
				caption:"查询", title:"点击查询", buttonicon:'ui-icon-search', onClickButton: openDialog
			});
			
			//打开查询窗口并进行窗口初始化
			var multiSearchParams = new Array();
			function openDialog() {
			    multiSearchParams[0] = "#list"+${param.categoryId};				//列表Id
				multiSearchParams[1] = "#multiSearchDialog"+${param.categoryId};//查询模态窗口Id
				
				initSearchDialog();
				
				//获取自定义表单查询字段
				getSearchFields(${param.categoryId});
				
			    $(multiSearchParams[1]).dialog("open");
			}
			
			//批量删除
			function deleteInfor(){
				doDelete("/document/document.do?method=delete","list"+${param.categoryId});
			}
			
			/** 查询条件中的部门,班组,用户下拉联动 */
			//部门信息初始化
			$('#departmentId${param.categoryId}').selectInit();
			
			//加载部门及联动信息		
			$.loadDepartments("departmentId${param.categoryId}", null, "applierId${param.categoryId}");
			/** ******** */		
				
		</script>