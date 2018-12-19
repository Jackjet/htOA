<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
	<script src="<c:url value="/"/>datePicker/WdatePicker.js" language="JavaScript"></script>
	<script src="<c:url value="/"/>components/jquery-1.4.2.js" type="text/javascript"></script> <!--jquery包-->
	<script src="<c:url value="/"/>components/jqgrid/js/jquery-ui-1.8.2.custom.min.js" type="text/javascript"></script><!--jquery ui-->
	<script src="<c:url value="/"/>components/jquery.layout.js" type="text/javascript"></script><!--jquery 布局-->
	<script src="<c:url value="/"/>components/jqgrid/js/grid.locale-cn.js" type="text/javascript"></script> <!--jqgrid 中文包-->
	<script src="<c:url value="/"/>components/jqgrid/js/jquery.jqGrid.min.js" type="text/javascript"></script> <!--jqgrid 包-->
	<script src="<c:url value="/"/>js/inc_javascript.js"></script>
	<script src="<c:url value="/"/>js/commonFunction.js"></script>
	<script src="<c:url value="/"/>js/changeclass.js"></script> <!--用于改变页面样式-->
	
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/tabstyle.css" />
	
	<script type="text/javascript">     
		//初始化列表和查询窗口Id
		var multiSearchParams = new Array();
		$.initReceive = function() {
			multiSearchParams[0] = "#listAddress";				//通讯录列表Id
			multiSearchParams[1] = "#multiSearchDialogAddress";	//通讯录查询模态窗口Id
		}
		$.initReceive();		
	</script>
	
 		<div style="margin-top:0px;">
		<table id="listAddress" style="width:99%"></table>
		<div id="pagerAddress"></div>
	</div>	
	
	<!-- 查询框 -->
	<div id="multiSearchDialogAddress" style="display: none;">  
	    <table>  
	        <tbody>  
	            <tr>  
	                <td>  
	                    <input type="hidden" class="searchField" value="personNo"/>编号：
	                    <input type="hidden" class="searchOper" value="cn"/>
	                </td>  
	                <td>  
	                    <input type="text" class="searchString"/>  
	                </td>  
	            </tr>  
	            
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
	                    <select class="searchString" id="gender"> 
	                    	<option value=''>--选择性别--</option>
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
		               <select class="searchString" id="departmentId"></select>
		           </td>  
		        </tr>
	        </tbody>  
	    </table>  
	</div>
	
	<script src="<c:url value="/"/>js/multisearch.js"></script> <!--加载模态多条件查询相关js-->
	<script type="text/javascript"> 		
	   $(document).ready(function(){
	   	
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
		var $mygrid = jQuery("#listAddress").jqGrid({ 
            url:'/address.do?method=listAddress',
            rownumbers: true,
            datatype: "json",                
           	autowidth: true,               	
			height: "auto",
			height:300,
            colNames:['Id','姓名','性别','编号','所属部门','手机','邮箱','办公室电话','办公室地址','家庭电话','家庭地址'],
            colModel:[
                {name:'personId',index:'personId',width:0, sorttype:"int", search:false, key:true, hidden:true },                    
                {name:'personName',index:'personName',width:20, align:'left', search:false,sortable:true,sorttype:"string"},
                {name:'gender',index:'gender',width:10,align:'center', search:false, sortable:true,sorttype:"int",formatter:formatGender},
                {name:'personNo',index:'personNo',width:10,align:'center', search:false, sortable:true,sorttype:"int"},
                {name:'department.organizeName',index:'department.organizeName',align:'center', search:false,width:20,sortable:true,sorttype:"string"},
                {name:'mobile',index:'mobile',align:'center', search:false,width:20,sortable:true,sorttype:"string"},
                {name:'email',index:'email', align:'left', search:false,width:20,sortable:true,sorttype:'string'},
                {name:'officePhone',index:'officePhone',align:'center', search:false,width:20,sortable:true,sorttype:'string'},
                {name:'officeAddress',index:'officeAddress',align:'center', search:false,width:30,sortable:true,sorttype:'string'},
                {name:'homePhone',index:'homePhone',align:'center', search:false,width:20,sortable:true,sorttype:'string'},
                {name:'homeAddress',index:'homeAddress',align:'center', search:false,width:30,sortable:true,sorttype:'string'}
                
            ],
            caption: "公共通讯录",
            sortname: 'personId',
            // multiselect: true,	// 是否支持多选,可用于批量删除
            sortorder: 'asc',
            viewrecords: true,
            rowNum: 10,
            rowList: [10,20,30],
            scroll: false, 
            scrollrows: false,                          
            jsonReader:{
               repeatitems: false
            },         
            pager: "#pagerAddress"
	     }).navGrid('#pagerAddress',{edit:false,add:false,del:false,search:false});     
	        
	     $("gbox_listAddress").css("width","100%");  
		
		 //自定义按钮
		 jQuery("#listAddress").jqGrid('navButtonAdd','#pagerAddress', {
			caption:"查询", title:"点击查询接收邮件", buttonicon:'ui-icon-search', onClickButton: openMultipleSearchDialog
		 });
	   });
	   //显示标题
	   function formatTitle(cellValue, options, rowObject) {				
			var html = '';
			html = "<a href='javascript:;' onclick='doView("+options.rowId+")'>" + cellValue + "</a>";				
			return html;
	   }
		
		function formatAttachment(cellValue, options, rowObject) {				
			var html = '';
			//alert(cellValue);
			html = showAttachment(cellValue,'');				
			return html;
	   }						
		

		/** 查询条件中的部门,班组,用户下拉联动 */
		//部门信息初始化
		$('#departmentId').selectInit();
		
		//加载部门及联动信息		
		$.loadDepartments("departmentId", null, null);
		/** ******** */
	       	
</script>

