<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">


<script type="text/javascript">
	//初始化列表和查询窗口Id
	var multiSearchParamsPerson = new Array();
	multiSearchParamsPerson[0] = "#listPerson${param.isvalid}";			//列表Id
	multiSearchParamsPerson[1] = "#multiSearchDialogPerson${param.isvalid}";//查询模态窗口Id


    //新增
	function addPerson(){
		/*var returnPerTag = window.showModalDialog("/core/personInfor.do?method=edit",null,"dialogWidth:800px;dialogHeight:700px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		if(returnPerTag == "refresh") {
			//保存信息后重新加载tab
			loadTab("listPerson.jsp?isvalid="+${param.isvalid}, "2");
		}*/
		
		window.open("/core/personInfor.do?method=edit", "_blank");
	}
	//修改
	function editPerson(rowId){
		/*var returnPerTag = window.showModalDialog("/core/personInfor.do?method=edit&personId="+rowId,'',"dialogWidth:800px;dialogHeight:700px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		if(returnPerTag == "refresh") {
			//保存信息后重新加载tab
			loadTab("listPerson.jsp?isvalid="+${param.isvalid}, "2");
		}*/
		window.open("/core/personInfor.do?method=edit&personId="+rowId, "_blank");
	}
	function reload() {
        loadTab("listPerson.jsp?isvalid="+${param.isvalid}, "2");
    }
</script>
<!--<script src="<c:url value="/"/>js/multisearch.js"></script> 加载模态多条件查询相关js-->
<script type="text/javascript">
			
		/** 自定义多条件查询 */
		//初始化查询窗口
		function initSearchDialog() {
		    $(multiSearchParamsPerson[1]).dialog({
		        autoOpen: false,       
		        modal: true,   
		        resizable: true,       
		        width: 350,   
		        title: "多条件查询",   
		        buttons: {   
		            "查询": multipleSearch,
		            "重置": clearSearch
		        }   
		    });
		}
		
		//打开查询窗口
	    function openMultipleSearchDialog() {
		    //初始化窗口
		    initSearchDialog();
		    
		    $(multiSearchParamsPerson[1]).dialog("open");
		}
		
		//多条件查询
		function multipleSearch() {
		    var rules = "";   
		    $("tbody tr", multiSearchParamsPerson[1]).each(function(i){    	//(1)从multipleSearchDialog对话框中找到各个查询条件行   
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
		       
		    var postData = $(multiSearchParamsPerson[0]).jqGrid("getGridParam", "postData");   
		       
		    $.extend(postData, {filters: filtersStr});   				//(8)将filters参数串加入postData选项
		       
		    $(multiSearchParamsPerson[0]).jqGrid("setGridParam", {  
		        search: true    										//(9)将jqGrid的search选项设为true   
		    }).trigger("reloadGrid", [{page:1}]);   					//(10)重新载入Grid表格,且返回第一页  
		       
		    $(multiSearchParamsPerson[1]).dialog("close");
		}
		
		//重置查询条件
		function clearSearch() {
		    var sdata = {
		        searchString: ""	//将查询数据置空
		    };   
		       
		    var postData = $("#gridTable").jqGrid("getGridParam", "postData");   
		       
		    $.extend(postData, sdata);   
		       
		    $(multiSearchParamsPerson[0]).jqGrid("setGridParam", {   
		        search: false  
		    }).trigger("reloadGrid", [{page:1}]);   
		       
		    resetSearchDialog(); 
		}
		var resetSearchDialog = function() {
		    $("select",multiSearchParamsPerson[1]).val("");   
		    $(":text",multiSearchParamsPerson[1]).val("");   
		}

		/** ********** */
</script>

<title>用户信息</title>
  		<div>
			<table id="listPerson${param.isvalid}"></table> <!-- 信息列表 -->
			<div id="pagerPerson${param.isvalid}"></div> <!-- 分页 -->
		</div>
		
		<!-- 查询框 -->
		<div id="multiSearchDialogPerson${param.isvalid}" style="display: none;">  
		    <table>  
		        <tbody>  
		            <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="personName"/>姓名：
		                    <input type="hidden" class="searchOper" value="cn"/>
		                </td>  
		                <td>  
		                    <input type="text" class="searchString"/>
		                </td>  
		            </tr>  
		            <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="gender"/>性别：
		                    <input type="hidden" class="searchOper" value="eq"/>
		                </td>  
		                <td>  
		                    <select class="searchString">  
		                        <option value="">--请选择--</option>
		                        <option value="0">男</option>
		                        <option value="1">女</option>
		                    </select>
		                </td>  
		            </tr>
		            <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="department.organizeId"/>所属部门：
		                    <input type="hidden" class="searchOper" value="eq"/>
		                </td>  
		                <td>  
		                    <select class="searchString" id="personDepartmentId">  
		                    </select>
		                </td>  
		            </tr>
		        </tbody>  
		    </table>  
		</div>
		<!-- ----- -->
		
		<script type="text/javascript"> 		
			
			//自定义操作栏的显示内容
		    function formatOperation(cellvalue, options, rowdata) {
	           var returnStr = "";
	           if (cellvalue) {
	              returnStr += " <a href='javascript:;' onclick='cancelOrResumePerson("+options.rowId+")'>[恢复]</a>";
	           }else {
	              returnStr += "<a href='javascript:;' onclick='editPerson("+options.rowId+")'>[修改]</a> <a href='javascript:;' onclick='cancelOrResumePerson("+options.rowId+")'>[注销]</a>";
	           }
	           return returnStr;
		    }
		    
		    //自定义状态栏的显示内容
		    function formatStatus(cellvalue, options, rowdata) {
	           var returnStr;
	           if (cellvalue) {
	              returnStr = "<font color='gray'>无效</font>";
	           }else {
	              returnStr = "有效";
	           }
	           return returnStr;
		    }
		    
		    //自定义用户类型栏的显示内容
		    /** function formatPersonType(cellvalue) {
	           var returnStr;
	           if (cellvalue == '0') {
	              returnStr = "普通用户";
	           }else if (cellvalue == '1'){
	              returnStr = "<font color='red'>系统管理员</font>";
	           }else {
	           	  returnStr = "档案员";
	           }
	           return returnStr;
		    } */
		    
		    //自定义性别的显示内容
		    function formatGender(cellvalue) {
	           var returnStr;
	           if (cellvalue == 0) {
	              returnStr = "男";
	           }else if (cellvalue == 1){
	              returnStr = "女";
	           }
	           return returnStr;
		    }
		    
			//加载表格数据
			var $mygrid = jQuery("#listPerson"+${param.isvalid}).jqGrid({
                url:'/core/personInfor.do?method=list&isvalid='+${param.isvalid},
                //rownumbers: true,	//是否显示序号
                datatype: "json",   //从后台获取的数据类型              
               	autowidth: true,	//是否自适应宽度
				//height: "auto",
                height:document.documentElement.clientHeight-240,
                colNames:['Id', '姓名', '性别', '编号', '部门[班组]', '岗位', '手机', 'email', '操作'],//表的第一行标题栏
                //以下为每列显示的具体数据
                colModel:[
                    {name:'personId',index:'personId', width:0, sorttype:"int", search:false, key:true, hidden:true},
                    {name:'personName',index:'personName', width:30, sortable:true, sorttype:"string"},
                    {name:'gender',index:'gender', width:20, align:'center', formatter:formatGender},
                    {name:'personNo',index:'personNo', width:30, align:'center'},
                    {name:'department.organizeName',index:'department.organizeName', width:40, align:'center'},
                    {name:'structure.structureName',index:'structure.structureName', width:30, align:'center'},
                    {name:'mobile',index:'mobile', width:45, align:'center'},
                    {name:'email',index:'email', width:60, align:'left'},
                    //{name:'deleted',index:'deleted', width:20, align:'center', formatter:formatStatus},
                    {name:'deleted', width:30, align:'center', search:false, sortable:false, formatter:formatOperation}
                ],
                sortname: 'personId',//默认排序的字段
                sortorder: 'asc',	//默认排序形式:升序,降序
                multiselect: true,	//是否支持多选,可用于批量删除
                viewrecords: true,	//是否显示数据的总条数(显示在右下角)
                rowNum: 10,			//每页显示的默认数据条数
                rowList: [10,20,30],//可选的每页显示的数据条数(显示在中间,下拉框形式)
                scroll: false, 		//是否采用滚动分页的形式
                scrollrows: false,	//当选中的行数据隐藏时,grid是否自动滚               
                jsonReader:{
                   repeatitems: false	//告诉JqGrid,返回的数据的标签是否是可重复的
                },         
                pager: "#pagerPerson"+${param.isvalid}	//分页工具栏
                //caption: "用户信息"	//表头
        }).navGrid('#pagerPerson'+${param.isvalid},{edit:false,add:false,del:false,search:false}).closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
        //}).navGrid('#pagerPerson',{edit:false,add:false,del:false}).searchGrid({multipleSearch:true,autoOpen:false});
		
		//显示各个栏目上的搜索栏
		//$('#listPerson').jqGrid('filterToolbar','');
		
		//自定义按钮
		jQuery("#listPerson"+${param.isvalid}).jqGrid('navButtonAdd','#pagerPerson'+${param.isvalid}, {
			caption:"新增", title:"点击新增信息", buttonicon:'ui-icon-plusthick', onClickButton: addPerson
		});
		jQuery("#listPerson"+${param.isvalid}).jqGrid('navButtonAdd','#pagerPerson'+${param.isvalid}, {
			caption:"<span style='color: red;'>批量删除</span>", title:"点击批量删除", buttonicon:'ui-icon-closethick', onClickButton: deletePerson
		});
		jQuery("#listPerson"+${param.isvalid}).jqGrid('navButtonAdd','#pagerPerson'+${param.isvalid}, {
			caption:"查询", title:"点击查询", buttonicon:'ui-icon-search', onClickButton: openMultipleSearchDialog
		});
		
		//批量删除	
		function deletePerson(){
			doDelete("/core/personInfor.do?method=delete","listPerson"+${param.isvalid});
		}
		
		//注销或恢复
		function cancelOrResumePerson(rowId){
			if(rowId!=null || rowId!=0){			
				$.ajax({
					url: "/core/personInfor.do?method=cancelOrResume&personId="+rowId,	//注销或恢复的url
					cache: false,
					//data:{personId: rowId},
					type: "POST",
					dataType: "html",
					beforeSend: function (xhr) {							
					},
					
					complete : function (req, err) {
						alert("操作成功！");
						$("#listPerson"+${param.isvalid}).trigger("reloadGrid"); 
					}
				});	
			}			
		}
		
		jQuery().ready(function (){
		    //获取部门信息(查询条件)
		    $.getJSON("/core/organizeInfor.do?method=getDepartments",function(data) {
		           var options = "<option value=''>--选择部门--</option>";
		           $.each(data._Departments, function(i, n) {
		               options += "<option value='"+n.organizeId+"'>"+n.organizeName+"</option>";   
		           });   
		           $('#personDepartmentId').html(options);   
		        }   
		    );
		});
		
	</script>