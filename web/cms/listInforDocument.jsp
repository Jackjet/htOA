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
<%--<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />--%>
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>css/base/jquery-ui-1.9.2.custom.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/tabstyle.css" />

<script type="text/javascript">
	//初始化列表和查询窗口Id
	var multiSearchParams = new Array();
	$.init = function() {
		multiSearchParams[0] = "#list"+${_Category.categoryId};				//列表Id
		multiSearchParams[1] = "#multiSearchDialog"+${_Category.categoryId};//查询模态窗口Id
	}
	$.init();
	
	//新增
	function addInfor(){
		/*var returnArray = window.showModalDialog("/cms/${_Category.urlPath}.do?method=edit&categoryId="+${_Category.categoryId},null,"dialogWidth:800px;dialogHeight:600px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		if(returnArray[0] == "refresh") {
			self.location.reload();
		}*/
		window.open("/cms/${_Category.urlPath}.do?method=edit&categoryId="+${_Category.categoryId},"_blank");
	}
	
	//修改
	function editInfor(rowId){
		/*var returnArray = window.showModalDialog("/cms/${_Category.urlPath}.do?method=edit&rowId="+rowId+"&categoryId="+${_Category.categoryId},'',"dialogWidth:800px;dialogHeight:600px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		if(returnArray[0] == "refresh") {
			self.location.reload();
		}*/
		window.open("/cms/${_Category.urlPath}.do?method=edit&rowId="+rowId+"&categoryId="+${_Category.categoryId},"_blank");
	}
	
	//授权
	function doAuthorize(rowId){
		/*var returnArray = window.showModalDialog("/cms/${_Category.urlPath}.do?method=editInforRight&rowId="+rowId+"&categoryId="+${_Category.categoryId},'',"dialogWidth:800px;dialogHeight:600px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		if(returnArray[0] == "refresh") {
			self.location.reload();
		}*/
        window.open("/cms/${_Category.urlPath}.do?method=editInforRight&rowId="+rowId+"&categoryId="+${_Category.categoryId},"_blank");
	}
	
	//查看静态页面
	function viewInfor(htmlFilePath){
		window.open(<%=request.getContextPath()%>htmlFilePath,'','');
	}
</script>

<title>${_Category.categoryName}</title>
<body style="border:1px solid #0DE8F5;border-radius: 5px">
  		<div style="margin: 8px;">
			<table id="list${_Category.categoryId}"></table>
			<div id="pager${_Category.categoryId}"></div>
		</div>
		
		<!-- 查询框 -->
		<div id="multiSearchDialog${_Category.categoryId}" style="display: none;">  
		    <table>
		        <tbody> 
		            <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="inforTitle"/><cms:displayTitle categoryId="${_Category.categoryId}" fieldName="inforTitle"/>：
		                    <input type="hidden" class="searchOper" value="cn"/>
		                </td>  
		                <td>  
		                    <input type="text" class="searchString" size="25"/>  
		                </td>  
		            </tr>
		            <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="issueUnit"/><cms:displayTitle categoryId="${_Category.categoryId}" fieldName="issueUnit"/>：
		                    <input type="hidden" class="searchOper" value="cn"/>
		                </td>  
		                <td>  
		                    <input type="text" class="searchString"/>
		                </td>  
		            </tr>
		            <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="keyword"/><cms:displayTitle categoryId="${_Category.categoryId}" fieldName="keyword"/>：
		                    <input type="hidden" class="searchOper" value="cn"/>
		                </td>  
		                <td>  
		                    <input type="text" class="searchString"/>
		                </td>  
		            </tr>
		            <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="createTime"/>起始时间：
		                    <input type="hidden" class="searchOper" value="ge"/>
		                </td>  
		                <td>  
		                    <input class="searchString" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly="true" size="12"/>
		                </td>  
		            </tr>
		            
		            <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="createTime"/>结束时间：
		                    <input type="hidden" class="searchOper" value="le"/>
		                </td>  
		                <td>  
		                    <input class="searchString" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly="true" size="12"/>
		                </td>  
		            </tr>
		            <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="author.personId"/>提交者：
		                    <input type="hidden" class="searchOper" value="eq"/>
		                </td>  
		                <td>  
		                    <select id="departmentId"></select>
		                    <select class="searchString" id="personId"></select>
		                </td>  
		            </tr>
		        </tbody>  
		    </table>
		</div>
</body>
		<!-- ----- -->
		
		<script type="text/javascript"> 		
				
			//自定义操作栏的显示内容
			function formatOperation(cellvalue, options, rowdata) {
		       var returnStr = "<a href='javascript:;' onclick='editInfor("+options.rowId+")'>[修改]</a>";
		       returnStr += " <a href='javascript:;' onclick='doAuthorize("+options.rowId+")'>[授权]</a> <a href='javascript:;' onclick='viewInfor(\""+cellvalue+"?inforId="+options.rowId+"\")'>[查看]</a>"
		       return returnStr;
			}
			
			//自定义显示boolean型内容
		    function formatBol(cellvalue) {
	           var returnStr;
	           if (cellvalue) {
	              returnStr = "<font color='red'>是</font>";
	           }else {
	              returnStr = "否";
	           }
	           return returnStr;
		    }
		    
		    //自定义显示附件
		    function formatAttach(cellValue, options, rowObject) {	
				var html = '';
				html = showAttachment(cellValue,'');				
				return html;
			}
			    
			//获取该分类下需要显示的字段信息
			var col_names = [];
			var col_model = [];
		    $.ajax({
				url: "/cms/inforDocument.do?method=getColData&categoryId="+${_Category.categoryId},
				cache: false,
				async: false,
				type: "GET",
				dataType: "json",
				success : function (data) {
					if (data != null) {
					    	//列名
					    	$.each(data.names, function(i,n){
					    		col_names[i] = n;
					    	});
					    	
					    	//列数据
					    	$.each(data.model, function(i,n){
					    		if (i == 0) {
					    			col_model[i] = eval("({name:'"+n+"',index:'"+n+"', sorttype:'int', search:false, key:true, hidden:true})");
					    		}else {
					    			if (n == 'important' || n == 'defBool1') {
					    				//boolean型
					    				col_model[i] = eval("({name:'"+n+"',index:'"+n+"', align:'center', width:'50px', formatter:formatBol})");
					    			}else if (n == 'inforTitle' || n == 'inforContent') {
					    				//标题,内容
					    				col_model[i] = eval("({name:'"+n+"',align:'center', width:'200px',index:'"+n+"'})");
					    			}else if (n == 'attachment' || n == 'defaultPicUrl') {
					    				//附件,图片
					    				col_model[i] = eval("({name:'"+n+"',align:'center',index:'"+n+"', width:'50px', formatter:formatAttach})");
					    			}else if(n=='createTime'){
                                        col_model[i] = eval("({name:'"+n+"',align:'center',index:'"+n+"', width:'70px'})")
                                    }else if(n=='author.person.personName'){
                                        col_model[i] = eval("({name:'"+n+"',align:'center',index:'"+n+"', width:'70px'})")
									}else {
					    				col_model[i] = eval("({name:'"+n+"',index:'"+n+"', align:'center'})");
					    			}
					    		}
					    	});
				    }
				}
			});
			var index = col_names.length;
			col_names[index] = "相关操作";
			col_model[index] = eval("({name:'htmlFilePath',  align:'center', search:false, sortable:false, formatter:formatOperation})");
			//alert(col_names);
			//alert(col_model);
			    
			//加载表格数据
			var $mygrid = jQuery("#list"+${_Category.categoryId}).jqGrid({
	            url:"/cms/${_Category.urlPath}.do?method=getInforDocument",
	            rownumbers: true,
	            datatype: "json",                
	            autowidth: true,
                height:document.documentElement.clientHeight-180,
	            colNames: col_names,
	            colModel: col_model,
	            sortname: 'inforId',
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
	            pager: "#pager"+${_Category.categoryId},
	            <%--caption: "${_Category.categoryName}"--%>
	        }).navGrid('#pager'+${_Category.categoryId},{edit:false,add:false,del:false,search:false}).closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
			
			//自定义按钮
			jQuery("#list"+${_Category.categoryId}).jqGrid('navButtonAdd','#pager'+${_Category.categoryId}, {
				caption:"新增", title:"点击新增信息", buttonicon:'ui-icon-plusthick', onClickButton: addInfor
			});
			jQuery("#list"+${_Category.categoryId}).jqGrid('navButtonAdd','#pager'+${_Category.categoryId}, {
				caption:"<span style='color: red;'>批量删除</span>", title:"点击批量删除", buttonicon:'ui-icon-closethick', onClickButton: deleteInfor
			});
			jQuery("#list"+${_Category.categoryId}).jqGrid('navButtonAdd','#pager'+${_Category.categoryId}, {
				caption:"查询", title:"点击查询", buttonicon:'ui-icon-search', onClickButton: openMultipleSearchDialog
			});
			
			//批量删除
			function deleteInfor(){
				doDelete("/cms/${_Category.urlPath}.do?method=delete","list"+${_Category.categoryId});
			}
			
			/** 查询条件中的部门,班组,用户下拉联动 */
			//部门信息初始化
			$('#departmentId').selectInit();
			
			//加载部门及联动信息		
			$.loadDepartments("departmentId", null, "personId");
			/** ******** */
			
			function loadTab(categoryId){
				self.location.reload();		
			}
			
		</script>