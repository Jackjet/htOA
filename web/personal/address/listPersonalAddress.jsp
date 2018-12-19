<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<script src="<c:url value="/"/>js/multisearch.js"></script> <!--加载模态多条件查询相关js-->

<script type="text/javascript">
	
	//新增
	function addAddress(){
		var returnArray = window.showModalDialog("/personal/address/personalAddressInfor.do?method=edit",null,"dialogWidth:700px;dialogHeight:600px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		if(returnArray[0] == "refresh") {
			self.location.reload();
		}
	}
	//修改
	function editAddress(rowId){
		var returnArray = window.showModalDialog("/personal/address/personalAddressInfor.do?method=edit&rowId="+rowId,'',"dialogWidth:700px;dialogHeight:600px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		if(returnArray[0] == "refresh") {
			self.location.reload();
		}
	}
</script>

<script type="text/javascript">     
	//查看
	function doView(rowId){
		window.showModalDialog("/personal/address/personalAddressInfor.do?method=viewAddress&rowId="+rowId,'',"dialogWidth:700px;dialogHeight:600px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
	}		
</script>

<title>个人通讯录</title>
	<div>
		<table id="listPersonalAddress"></table> <!-- 信息列表 -->
		<div id="pagerAddress"></div> <!-- 分页 -->
	</div>
	
	<!-- 查询框 -->
	<div id="multiSearchDialog" style="display: none;">  
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
	                    <select class="searchString" id="gender"> 
	                    	<option value=''>--选择性别--</option>
	                    	<option value="0">男</option> 
	                    	<option value="1">女</option>
	                    </select>
	                </td>  
	            </tr>
	        </tbody>  
	    </table>  
	</div>
	
	<script type="text/javascript"> 
	
		//显示详细
	   function formatName(cellValue, options, rowObject) {				
			var html = '';
			html = "<a href='javascript:;' onclick='doView("+options.rowId+")'>" + cellValue + "</a>";				
			return html;
		}		
		
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
		
		//自定义操作栏的显示内容
	    function formatOperation(cellvalue, options, rowdata) {
           var returnStr = "<a href='javascript:;' onclick='editAddress("+options.rowId+")'>[修改]</a>";
          
           return returnStr;
	    }
	  
	    
		//加载表格数据
		var $mygrid = jQuery("#listPersonalAddress").jqGrid({
            url:'/personal/address/personalAddressInfor.do?method=list',
            //rownumbers: true,	//是否显示序号
            datatype: "json",   //从后台获取的数据类型              
           	autowidth: true,	//是否自适应宽度
			//height: "auto",
			height:300,
            colNames:['Id',  '姓名', '分类', '性别', '手机', '办公室电话', '家庭电话', '邮件', '操作'],//表的第一行标题栏
            //以下为每列显示的具体数据
            colModel:[
                {name:'personId',index:'personId', width:0, sorttype:"int", search:false, key:true, hidden:true},
                {name:'personName',index:'personName',align:'left', width:40, sortable:true,sorttype:"string",formatter:formatName},
                {name:'category.categoryName',index:'category.categoryName',align:'center', width:40},
                {name:'gender',index:'gender', width:20,align:'center',formatter:formatGender},
                {name:'mobile',index:'mobile', width:40,align:'center'},
                {name:'officePhone',index:'officePhone', width:40, align:'center'},
                {name:'homePhone',index:'homePhone', width:40, align:'center'},
                {name:'email',index:'email', width:60, align:'left'},
                {name:'operate',index:'operate', width:40, align:'center', search:false, sortable:false, formatter:formatOperation}
            ],
            sortname: 'personId',//默认排序的字段
            sortorder: 'desc',	//默认排序形式:升序,降序
            multiselect: true,	//是否支持多选,可用于批量删除
            viewrecords: true,	//是否显示数据的总条数(显示在右下角)
            rowNum: 10,			//每页显示的默认数据条数
            rowList: [10,20,30],//可选的每页显示的数据条数(显示在中间,下拉框形式)
            scroll: false, 		//是否采用滚动分页的形式
            scrollrows: false,	//当选中的行数据隐藏时,grid是否自动滚               
            jsonReader:{
               repeatitems: false	//告诉JqGrid,返回的数据的标签是否是可重复的
            },         
            pager: "#pagerAddress"	//分页工具栏
            //caption: "用户信息"	//表头
       	}).navGrid('#pagerAddress',{edit:false,add:false,del:false,search:false});       
	
		//自定义按钮
		jQuery("#listPersonalAddress").jqGrid('navButtonAdd','#pagerAddress', {
			caption:"新增", title:"点击新增信息", buttonicon:'ui-icon-plusthick', onClickButton: addAddress
		});
		
		jQuery("#listPersonalAddress").jqGrid('navButtonAdd','#pagerAddress', {
			caption:"<span style='color: red;'>批量删除</span>", title:"点击批量删除", buttonicon:'ui-icon-closethick', onClickButton: deleteAddress
		});
		
		jQuery("#listPersonalAddress").jqGrid('navButtonAdd','#pagerAddress', {
			caption:"查询", title:"点击查询", buttonicon:'ui-icon-search', onClickButton: openDialog
		});
		
		//打开查询窗口并进行窗口初始化
		var multiSearchParams = new Array();
		function openDialog() {
		    multiSearchParams[0] = "#listPersonalAddress";				//列表Id
			multiSearchParams[1] = "#multiSearchDialog";//查询模态窗口Id
			
			initSearchDialog();
			
		    $(multiSearchParams[1]).dialog("open");
		}
		
		//删除数据
		function deleteAddress(){
			doDelete("/personal/address/personalAddressInfor.do?method=delete","listPersonalAddress");
		}
	
	</script>