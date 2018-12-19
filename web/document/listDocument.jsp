<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<script type="text/javascript">
	
	//新增
	function addInfor_base(){
//		var returnArray = window.showModalDialog("/document/document.do?method=edit",'',"dialogWidth:800px;dialogHeight:600px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
//		if(returnArray != null && returnArray[0] == "refresh") {
//			self.location.reload();
//		}
        window.open("/document/document.do?method=edit","_blank");
	}
	
	//修改
	function editInfor_base(rowId){
		/*var returnArray = window.showModalDialog("/document/document.do?method=edit&documentId="+rowId,'',"dialogWidth:800px;dialogHeight:600px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		if(returnArray != null && returnArray[0] == "refresh") {
			self.location.reload();
		}*/
        window.open("/document/document.do?method=edit&documentId="+rowId,"_blank");
	}
	
	//授权
	function doAuthorize_base(rowId){
		/*var returnArray = window.showModalDialog("/document/document.do?method=editInforRight&rowId="+rowId,'',"dialogWidth:800px;dialogHeight:600px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		if(returnArray != null && returnArray[0] == "refresh") {
			self.location.reload();
		}*/
		window.open("/document/document.do?method=editInforRight&rowId="+rowId,"_blank");
	}
	
	//查看
	function viewInfor_base(rowId){
		//window.showModalDialog("/document/document.do?method=view&rowId="+rowId,'',"dialogWidth:600px;dialogHeight:600px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		window.open("/document/document.do?method=view&rowId="+rowId,"_blank");
	}
</script>

<title></title>
<body style="border:1px solid #0DE8F5;border-radius: 5px">
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
		                    <input type="hidden" class="searchField" value="documentTitle"/>标题：
		                    <input type="hidden" class="searchOper" value="cn"/>
		                </td>  
		                <td>  
		                    <input type="text" class="searchString" size="25"/>  
		                </td>  
		            </tr>
		            <tr>  
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
		            <!-- <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="description"/>说明：
		                    <input type="hidden" class="searchOper" value="cn"/>
		                </td>  
		                <td>  
		                    <input type="text" class="searchString"/>
		                </td>  
		            </tr> -->
		            <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="category.categoryId"/>分类：
		                    <input type="hidden" class="searchOper" value="eq"/>
		                </td>  
		                <td>  
		                    <select class="searchString" id="searchCategoryId">
		                    	
		                    </select>
		                </td>  
		            </tr>
		            <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="author.person.personId"/>作者：
		                    <input type="hidden" class="searchOper" value="eq"/>
		                </td>  
		                <td>  
		                    <select id="departmentId"></select>
		                    <select class="searchString" id="personId"></select>
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
		            <!-- <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="editor.person.personName"/>最后更新者：
		                    <input type="hidden" class="searchOper" value="cn"/>
		                </td>  
		                <td>  
		                    <input type="text" class="searchString"/>
		                </td>  
		            </tr> -->
		            
		            <!-- <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="updateTime"/>更新时间：
		                    <input type="hidden" class="searchOper" value="le"/>
		                </td>  
		                <td>  
		                    <input class="searchString" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly="true" size="12"/>
		                </td>  
		            </tr> -->
		            <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="commended"/>精华文档：
		                    <input type="hidden" class="searchOper" value="eq"/>
		                </td>  
		                <td>  
		                    <select class="searchString" id="commended">
		                    	<option value="">--请选择--</option>
		                    	<option value="1">推荐</option>
		                    	<option value="0">不推荐</option>
		                    </select>
		                </td>  
		            </tr>
		            
		        </tbody>  
		    </table>  
		</div>
</body>
		<script type="text/javascript"><!-- 		
			
			//自定义操作栏的显示内容
		    function formatOperation(cellvalue, options, rowdata) {
	           var returnStr = "<a href='javascript:;' onclick='editInfor_base("+options.rowId+")'>[修改]</a>";
	           returnStr += " <a href='javascript:;' onclick='doAuthorize_base("+options.rowId+")'>[授权]</a>";
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
		   
			//加载表格数据
			var $mygrid = jQuery("#list").jqGrid({
			
                url:"/document/document.do?method=getDocumentInfor",
                rownumbers: true,
                datatype: "json",

               	autowidth: true,
                height:document.documentElement.clientHeight-235,
                colNames:['Id','标题','所属分类','更新时间','精华文档','所属部门','附件','操作'],//表的第一行标题栏
	            colModel:[
	                {name:'documentId',index:'documentId', width:0, sorttype:"int", search:false, key:true, hidden:true},
	                {name:'documentTitle',index:'documentTitle',width:200,align:'center', sortable:true,sorttype:"string",formatter:formatTitle},
	                //{name:'documentCode',index:'documentCode',align:'center', width:20},
	                //{name:'keyword',index:'keyword', width:25,align:'left'},
	                //{name:'description',index:'description', width:40,align:'left'},
	                {name:'category.categoryName',index:'category.categoryName', /*width:40,*/align:'center'},
	                //{name:'author.person.personName',index:'author.person.personName', width:40, align:'center'},
	                //{name:'createTime',index:'createTime', width:40, align:'center'},
	                //{name:'editor.person.personName',index:'editor.person.personName', width:40, align:'center'},
	                {name:'updateTime',index:'updateTime', /*width:55,*/ align:'center'},
	                {name:'commended',index:'commended', /*width:30, */align:'center',formatter:formatBol},
	                {name:'department.organizeName',index:'department.organizeName', /*width:50,*/ align:'center'},
	                {name:'attachment',index:'attachment', /*width:25,*/ align:'left',formatter:formatAttachment},
	                {align:'center', /*width:50,*/ search:false, sortable:false, formatter:formatOperation}
	            ],
                sortname: 'documentId',
                sortorder: 'asc',
                multiselect: true,	//是否支持多选,可用于批量删除
                viewrecords: true,
                rowNum: 10,
				forceFit:true,
                rowList: [10,20,30],
                scroll: false,
                scrollrows: false,
                jsonReader:{
                   repeatitems: false
                },         
                pager: "#pager",

            }).navGrid('#pager',{edit:false,add:false,del:false,search:false}).closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
			
			//自定义按钮
			if (${param.isRoot != 'true'}) {
				//为根分类时不显示
				jQuery("#list").jqGrid('navButtonAdd','#pager', {
					caption:"新增", title:"点击新增文档", buttonicon:'ui-icon-plusthick', onClickButton: addInfor_base
				});
			}
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
				doDelete("/document/document.do?method=delete","list");
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
		           var options = "<option value=''>--请选择--</option>";
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