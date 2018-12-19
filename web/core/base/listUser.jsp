<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">


<script type="text/javascript">

	//新增
	function addUser(){
		/*var returnUsrTag = window.showModalDialog("/core/systemUserInfor.do?method=edit",null,"dialogWidth:700px;dialogHeight:400px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		if(returnUsrTag == "refresh") {
			//保存信息后重新加载tab
			loadTab("listUser.jsp?isvalid="+${param.isvalid}, "1");
		}*/
        window.open("/core/systemUserInfor.do?method=edit","_blank");
	}
	//修改
	function editUser(rowId){
		/*var returnUsrTag = window.showModalDialog("/core/systemUserInfor.do?method=edit&rowId="+rowId,'',"dialogWidth:700px;dialogHeight:400px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		if(returnUsrTag == "refresh") {
			//保存信息后重新加载tab
			loadTab("listUser.jsp?isvalid="+${param.isvalid}, "1");
		}*/
        window.open("/core/systemUserInfor.do?method=edit&rowId="+rowId,"_blank");
	}
	function reload(){
        loadTab("listUser.jsp?isvalid="+${param.isvalid}, "1");
	}
</script>
<script type="text/javascript">

    /** 自定义多条件查询 */
    //初始化查询窗口
    function initSearchDialog(){

        $(multiSearchParamsUser[1]).dialog({
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
    //初始化列表和查询窗口Id
    var multiSearchParamsUser = new Array();
    multiSearchParamsUser[0] = "#listUser${param.isvalid}";			//列表Id
    multiSearchParamsUser[1] = "#multiSearchDialogUser${param.isvalid}";//查询模态窗口Id

    function openMultipleSearchDialog() {

        //初始化窗口
        initSearchDialog();

        $(multiSearchParamsUser[1]).dialog("open");
    }

    //多条件查询
    function multipleSearch() {
        var rules = "";
        $("tbody tr", multiSearchParamsUser[1]).each(function(i){    	//(1)从multipleSearchDialog对话框中找到各个查询条件行
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

        var postData = $(multiSearchParamsUser[0]).jqGrid("getGridParam", "postData");

        $.extend(postData, {filters: filtersStr});   				//(8)将filters参数串加入postData选项

        $(multiSearchParamsUser[0]).jqGrid("setGridParam", {
            search: true    										//(9)将jqGrid的search选项设为true
        }).trigger("reloadGrid", [{page:1}]);   					//(10)重新载入Grid表格,且返回第一页

        $(multiSearchParamsUser[1]).dialog("close");
    }

    //重置查询条件
    function clearSearch() {
        var sdata = {
            searchString: ""	//将查询数据置空
        };

        var postData = $("#gridTable").jqGrid("getGridParam", "postData");

        $.extend(postData, sdata);

        $(multiSearchParamsUser[0]).jqGrid("setGridParam", {
            search: false
        }).trigger("reloadGrid", [{page:1}]);

        resetSearchDialog();
    }
    var resetSearchDialog = function() {
        $("select",multiSearchParamsUser[1]).val("");
        $(":text",multiSearchParamsUser[1]).val("");
    }

    /** ********** */
</script>
<!--<script src="<c:url value="/"/>js/multisearch.js"></script> 加载模态多条件查询相关js-->

<title>用户信息</title>
  		<div>
			<table id="listUser${param.isvalid}"></table> <!-- 信息列表 -->
			<div id="pagerUser${param.isvalid}"></div> <!-- 分页 -->
		</div>
		
		<!-- 查询框 -->
		<div id="multiSearchDialogUser${param.isvalid}" style="display: none;">
		    <table>  
		        <tbody>  
		            <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="userName"/>用户名：
		                    <input type="hidden" class="searchOper" value="cn"/>
		                </td>  
		                <td>  
		                    <input type="text" class="searchString"/>  
		                </td>  
		            </tr>  
		            <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="person.personName"/>姓名：
		                    <input type="hidden" class="searchOper" value="cn"/>
		                </td>  
		                <td>  
		                    <input type="text" class="searchString"/>
		                </td>  
		            </tr>  
		            <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="userType"/>用户类型：
		                    <input type="hidden" class="searchOper" value="eq"/>
		                </td>  
		                <td>  
		                    <select class="searchString">  
		                        <option value="">所有类型</option>
		                        <option value="0">普通用户</option>
		                        <option value="1">系统管理员</option>
		                        <%--<option value="2">档案员</option>--%>
		                        <option value="2">投票用户</option>
		                        <option value="3">非合同制用工</option>
		                    </select>
		                </td>  
		            </tr>
		            <%--<tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="invalidate"/>状态：
		                    <input type="hidden" class="searchOper" value="eq"/>
		                </td>  
		                <td>  
		                    <select class="searchString">  
		                        <option value="">所有状态</option>
		                        <option value="0">有效</option>
		                        <option value="1">无效</option>
		                    </select>
		                </td>  
		            </tr>
		            --%><tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="person.department.organizeId"/>所属部门：
		                    <input type="hidden" class="searchOper" value="eq"/>
		                </td>  
		                <td>  
		                    <select class="searchString" id="departmentId">  
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
	              returnStr += " <a href='javascript:;' onclick='cancelOrResume("+options.rowId+")'>[恢复]</a>";
	           }else {
	              returnStr += "<a href='javascript:;' onclick='editUser("+options.rowId+")'>[修改]</a> <a href='javascript:;' onclick='cancelOrResume("+options.rowId+")'>[注销]</a>";
	           }
	           return returnStr;
		    }
		    
		    //自定义状态栏的显示内容
		    function formatStatus(cellvalue) {
	           var returnStr;
	           if (cellvalue) {
	              returnStr = "<font color='gray'>无效</font>";
	           }else {
	              returnStr = "有效";
	           }
	           return returnStr;
		    }
		    
		    //自定义用户类型栏的显示内容
		    function formatUserType(cellvalue) {
	           var returnStr;
	           if (cellvalue == '0') {
	              returnStr = "普通用户";
	           }else if (cellvalue == '1'){
	              returnStr = "<font color='red'>系统管理员</font>";
	           } else if (cellvalue == '2') {
	           	  returnStr = "<font color='green'>投票用户</font>";
	           } else if (cellvalue == '3') {
	           	  returnStr = "<font color='blue'>非合同制用工</font>";
	           } /* else {
	           	  returnStr = "档案员";
	           } */
	           return returnStr;
		    }
		    
			//加载表格数据
			var $mygrid = jQuery("#listUser"+${param.isvalid}).jqGrid({
                url:'/core/systemUserInfor.do?method=list&isvalid='+${param.isvalid},
                //rownumbers: true,	//是否显示序号
                datatype: "json",   //从后台获取的数据类型              
               	autowidth: true,	//是否自适应宽度
				//height: "auto",
                height:document.documentElement.clientHeight-240,
                colNames:['Id', '用户名', '姓名', '编号', '部门[班组]', '用户类型', '操作'],//表的第一行标题栏
                //以下为每列显示的具体数据
                colModel:[
                    {name:'personId',index:'personId', width:0, sorttype:"int", search:false, key:true, hidden:true},
                    {name:'userName',index:'userName', width:40, sortable:true, sorttype:"string"},
                    {name:'person.personName',index:'person.personName', width:40},
                    {name:'person.personNo',index:'person.personNo', width:40, align:'center'},
                    {name:'person.department.organizeName',index:'person.department.organizeName', width:40, align:'center'},
                    {name:'userType',index:'userType', width:40, align:'center', formatter:formatUserType},
                    //{name:'invalidate',index:'invalidate', width:40, align:'center', formatter:formatStatus},
                    {name:'invalidate', width:40, align:'center', search:false, sortable:false, formatter:formatOperation}
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
                pager: "#pagerUser"+${param.isvalid}	//分页工具栏
                //caption: "用户信息"	//表头
        }).navGrid('#pagerUser'+${param.isvalid},{edit:false,add:false,del:false,search:false}).closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
        //}).navGrid('#pagerUser',{edit:false,add:false,del:false}).searchGrid({multipleSearch:true,autoOpen:false});
		
		//显示各个栏目上的搜索栏
		//$('#listUser').jqGrid('filterToolbar','');
		
		//自定义按钮
		jQuery("#listUser"+${param.isvalid}).jqGrid('navButtonAdd','#pagerUser'+${param.isvalid}, {
			caption:"新增", title:"点击新增信息", buttonicon:'ui-icon-plusthick', onClickButton: addUser
		});
		jQuery("#listUser"+${param.isvalid}).jqGrid('navButtonAdd','#pagerUser'+${param.isvalid}, {
			caption:"<span style='color: red;'>批量删除</span>", title:"点击批量删除", buttonicon:'ui-icon-closethick', onClickButton: deleteUser
		});
		jQuery("#listUser"+${param.isvalid}).jqGrid('navButtonAdd','#pagerUser'+${param.isvalid}, {
			caption:"查询", title:"点击查询", buttonicon:'ui-icon-search', onClickButton: openMultipleSearchDialog
		});
		
		//批量删除
		function deleteUser(){
			doDelete("/core/systemUserInfor.do?method=delete","listUser"+${param.isvalid});
		}
		
		//注销或恢复
		function cancelOrResume(rowId){
			//alert(rowId);
			if(rowId!=null || rowId!=0){			
				$.ajax({
					url: "/core/systemUserInfor.do?method=cancelOrResume&rowId="+rowId,	//注销或恢复的url
					cache: false,
					//data:{personId: rowId},
					type: "POST",
					dataType: "html",
					beforeSend: function (xhr) {							
					},
					
					complete : function (req, err) {
						alert("操作成功！");
						$("#listUser"+${param.isvalid}).trigger("reloadGrid"); 
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
		           $('#departmentId').html(options);   
		        }   
		    );
		});
		
	</script>
