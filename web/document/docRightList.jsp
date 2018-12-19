<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<script type="text/javascript">
	
	//新增
	function addInfor(){
		/*var returnArray = window.showModalDialog("/document/document.do?method=edit&categoryId="+${param.categoryId},'',"dialogWidth:800px;dialogHeight:600px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		if(returnArray != null && returnArray[0] == "refresh") {
			//保存信息后重新加载tab
			loadTab(returnArray[1]);
		}*/
        window.open("/document/document.do?method=edit&categoryId="+${param.categoryId},"_blank");
	}
	
	//修改
	function editInfor(rowId){
		/*var returnArray = window.showModalDialog("/document/document.do?method=edit&documentId="+rowId,'',"dialogWidth:800px;dialogHeight:600px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		if(returnArray != null && returnArray[0] == "refresh") {
			//保存信息后重新加载tab
			loadTab(returnArray[1]);
		}*/
        window.open("/document/document.do?method=edit&documentId="+rowId,"_blank");
	}
	
	//授权
	function doAuthorize(rowId){
		/*var returnArray = window.showModalDialog("/document/document.do?method=editInforRight&rowId="+rowId,'',"dialogWidth:800px;dialogHeight:600px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		if(returnArray != null && returnArray[0] == "refresh") {
			//保存信息后重新加载tab
			loadTab(returnArray[1]);
		}*/
		window.open("/document/document.do?method=editInforRight&rowId="+rowId,"_blank");
	}
	
	//查看
	function viewInfor(rowId){
		//window.showModalDialog("/document/document.do?method=view&rowId="+rowId,'',"dialogWidth:600px;dialogHeight:600px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		window.open("/document/document.do?method=view&rowId="+rowId,"_blank");
	}
</script>
<script type="text/javascript">
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
			<table id="list${param.categoryId}"></table>
			<div id="pager${param.categoryId}"></div>
		</div>
		
		<!-- 查询框 -->
		<div id="multiSearchDialog${param.categoryId}" style="display: none;">  
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
		            </tr> -->
		            <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="author.person.personId"/>作者：
		                    <input type="hidden" class="searchOper" value="eq"/>
		                </td>  
		                <td>  
		                    <select id="departmentId${param.categoryId}"></select>
		                    <select class="searchString" id="personId${param.categoryId}"></select>
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
		            <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="commended"/>精华文档：
		                    <input type="hidden" class="searchOper" value="eq"/>
		                </td>  
		                <td>  
		                    <select class="searchString" id="commended${param.categoryId}">
		                    	<option value="">--请选择--</option>
		                    	<option value="1">推荐</option>
		                    	<option value="0">不推荐</option>
		                    </select>
		                </td>  
		            </tr>
		        </tbody>  
		    </table>  
		</div>
		
		<script type="text/javascript"> 		
			
			//自定义操作栏的显示内容
		    function formatOperation(cellvalue, options, rowdata) {
	           var returnStr = "<a href='javascript:;' onclick='editInfor("+options.rowId+")'>[修改]</a>";
	           returnStr += " <a href='javascript:;' onclick='doAuthorize("+options.rowId+")'>[授权]</a>";
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
				html = "<a href='javascript:;' onclick='viewInfor("+options.rowId+")'>" + cellValue + "</a>";
				return html;
			}
		   
			//加载表格数据
			var $mygrid = jQuery("#list"+${param.categoryId}).jqGrid({
			
                url:"/document/document.do?method=getDocumentInfor&categoryId="+${param.categoryId},
                rownumbers: true,
                datatype: "json",                
               	autowidth: true,
                height:document.documentElement.clientHeight-235,
                colNames:['Id','标题','更新时间','精华文档','所属部门','附件','操作'],//表的第一行标题栏
	            colModel:[
	                {name:'documentId',index:'documentId', width:0, sorttype:"int", search:false, key:true, hidden:true},
	                {name:'documentTitle',index:'documentTitle',width:200,align:'left', sortable:true,sorttype:"string",formatter:formatTitle},
	                //{name:'documentCode',index:'documentCode',align:'center', width:20},
	                //{name:'keyword',index:'keyword', width:25,align:'left'},
	                //{name:'description',index:'description', width:40,align:'left'},
	                //{name:'category.categoryName',index:'category.categoryName', width:40,align:'center'},
	                //{name:'author.person.personName',index:'author.person.personName', width:40, align:'center'},
	                //{name:'createTime',index:'createTime', width:40, align:'center'},
	                //{name:'editor.person.personName',index:'editor.person.personName', width:40, align:'center'},
	                {name:'updateTime',index:'updateTime', /*width:40,*/ align:'center'},
	                {name:'commended',index:'commended',/* width:30,*/ align:'center',formatter:formatBol},
	                {name:'department.organizeName',index:'department.organizeName', /*width:35,*/ align:'center'},
	                {name:'attachment',index:'attachment',/* width:25,*/ align:'left',formatter:formatAttachment},
	                {name:'htmlFilePath', align:'center',/* width:50,*/ search:false, sortable:false, formatter:formatOperation}
	            ],
                sortname: 'documentId',
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
                pager: "#pager"+${param.categoryId}
	        }).navGrid('#pager'+${param.categoryId},{edit:false,add:false,del:false,search:false});       
			
			//自定义按钮
			if (${param.isRoot != 'true'}) {
				//为根分类时不显示
				jQuery("#list"+${param.categoryId}).jqGrid('navButtonAdd','#pager'+${param.categoryId}, {
					caption:"新增", title:"点击新增文档", buttonicon:'ui-icon-plusthick', onClickButton: addInfor
				});
			}
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
				
			    $(multiSearchParams[1]).dialog("open");
			}
			
			//批量删除
			function deleteInfor(){
				doDelete("/document/document.do?method=delete","list"+${param.categoryId});
			}
			
			//部门信息初始化
			$('#departmentId'+${param.categoryId}).selectInit();
			
			//加载部门及联动信息		
			$.myLoadDepartments("departmentId"+${param.categoryId},"personId"+${param.categoryId});
			
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