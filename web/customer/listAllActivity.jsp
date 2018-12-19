<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

	<script type="text/javascript">     
		//查看
		function doView(rowId){
			window.showModalDialog("/customer/activityInfor.do?method=viewActivity&rowId="+rowId,'',"dialogWidth:1000px;dialogHeight:600px;center:Yes;dialogTop: 100px; dialogLeft: 200px;");
		}	

		//增加
		function addActivityInfor(){
			var refresh = window.showModalDialog("/customer/activityInfor.do?method=edit",'',"dialogWidth:1000px;dialogHeight:600px;center:Yes;dialogTop: 100px; dialogLeft: 200px;");
			if(refresh == "Y") {
				self.location.reload();
			}
		}	
	</script>
	
  		<div style="margin-top:0px;">
			<table id="listAllActivity" style="width:99%"></table>
			<div id="pagerAllActivity"></div>
		</div>	
		
		<!-- 查询框 -->
		<div id="multiSearchDialogAllActivity" style="display: none;">  
		    <table>  
		        <tbody>  
		            <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="content"/>活动内容：
		                    <input type="hidden" class="searchOper" value="cn"/>
		                </td>  
		                <td>  
		                    <input type="text" class="searchString"/>  
		                </td>  
		            </tr>  
		            
		            <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="planDate"/>起始计划时间：
		                    <input type="hidden" class="searchOper" value="gt"/>
		                </td>  
		                <td>  
		                    <input  class="searchString"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly="true"/>
		                </td>  
		            </tr>
		            
		            <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="planDate"/>结束计划时间：
		                    <input type="hidden" class="searchOper" value="le"/>
		                </td>  
		                <td>  
		                    <input  class="searchString"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly="true"/>
		                </td>  
		            </tr>
		        </tbody>  
		    </table>  
		</div>
		<!-- ----- -->
		
		<script src="<c:url value="/"/>js/multisearch.js"></script> <!--加载模态多条件查询相关js-->
		<script type="text/javascript"> 		
			
			//自定义操作栏的显示内容
		    function formatOperation(cellvalue, options, rowdata) {
	           var returnStr = "<a href='javascript:;' onclick='doView("+options.rowId+")'>[查看]</a>";
	           return returnStr;
		    }
		    
		    //显示活动状态
		    function formatState(cellValue, options, rowObject) {				
				var html = '';
				if(cellValue==0){
					html = '计划'; 
				}else{
					html = '完成'; 
				}
				return html;
			}
		   	
		   //显示公司名称
		   function formatCompanyName(cellValue, options, rowObject) {			
				var html = '';
				html = "<a href='javascript:;' onclick='doView("+options.rowId+")'>" + cellValue + "</a>";				
				return html;
			}
			
			//显示附件图标
			function formatAttachment(cellValue, options, rowObject) {				
				var html = '';
				html = showAttachment(cellValue,'');				
				return html;
			}						
			
			//加载表格数据
			var $mygrid = jQuery("#listAllActivity").jqGrid({ 
                url:'/customer/activityInfor.do?method=list&type=1',
                //rownumbers: true,
                datatype: "json",                
               	autowidth: true,               	
				//height: "auto",
				height:300,
                colNames:['Id','状态','公司名称','活动内容','计划时间','实际时间','附件'],
                colModel:[
                    {name:'activityId',index:'activityId',width:0, sorttype:"int", search:false, key:true, hidden:true},                               
                    {name:'activityType',width:10,align:'center', formatter:formatState, resizable: false, sortable:false}, 
                    {name:'companyName',align:'left',width:60,formatter:formatCompanyName,editable:true,sorttype:"string", sortable:false}, 
                    {name:'content',index:'content',align:'left',editable:true,sorttype:"string", sortable:false}, 
                    {name:'planDateStr',index:'planDate',align:'center',width:50,editable:true,sorttype:"string"}, 
                  	{name:'activityDateStr',index:'activityDate',align:'center',width:50,editable:true,sorttype:"string"}, 
                  	{name:'attachmentStr',width:20,formatter:formatAttachment,sortable:false}
                ],
                sortname: 'activityId',
                multiselect: true,	// 是否支持多选,可用于批量删除
                sortorder: 'desc',
                viewrecords: true,
                rowNum: 10,
                rowList: [10,20,30],
                scroll: false, 
                scrollrows: false,                          
                jsonReader:{
                   repeatitems: false
                },         
                pager: "#pagerAllActivity"
                //caption: "已收到的讯息"
        }).navGrid('#pagerAllActivity',{edit:false,add:false,del:false,search:false});     
        
        $("gbox_listAllActivity").css("width","100%");  
      		
		//自定义按钮
		$("#listAllActivity").jqGrid('navButtonAdd','#pagerAllActivity', {
			caption:"<span style='color: red;'>批量删除</span>", title:"点击批量删除", buttonicon:'ui-icon-closethick', onClickButton: deleteActivity
		});
		
		$("#listAllActivity").jqGrid('navButtonAdd','#pagerAllActivity', {
			caption:"查询", title:"点击查询所有活动信息", buttonicon:'ui-icon-search', onClickButton: openMultipleSearchDialog
		});
		
		$("#listAllActivity").jqGrid('navButtonAdd','#pagerAllActivity', {
			caption:"增加", title:"点击增加活动信息", buttonicon:'ui-icon-plusthick', onClickButton: addActivityInfor
		});
		
		//批量删除
		function deleteActivity(){
			doDelete("/customer/activityInfor.do?method=delete","listAllActivity");
		}
			
	</script>

