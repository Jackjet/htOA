<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

	<script type="text/javascript">     
		//查看
		function doView(rowId){
			window.showModalDialog("/customer/customerInfor.do?method=viewCustomer&rowId="+rowId,'',"dialogWidth:1000px;dialogHeight:600px;center:Yes;dialogTop: 100px; dialogLeft: 200px;");
		}		
	</script>
	
  		<div style="margin-top:0px;">
			<table id="listCustomer" style="width:99%"></table>
			<div id="pagerCustomer"></div>
		</div>	
		
		<!-- 查询框 -->
		<div id="multiSearchDialogCustomer" style="display: none;">  
		    <table>  
		        <tbody>  
		            <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="companyName"/>公司名称：
		                    <input type="hidden" class="searchOper" value="cn"/>
		                </td>  
		                <td>  
		                    <input type="text" class="searchString"/>  
		                </td>  
		            </tr>  
		            
		            <tr>  
		                <td>  
		                    <input type="hidden"  class="searchField" value="companyAddress"/>公司地址：
		                    <input type="hidden" class="searchOper"  value="cn"/>
		                </td>  
		                <td>  
		                    <input type="text" class="searchString"/> 
		                </td>  
		            </tr>
		            
		            <tr>  
		                <td>  
		                    <input type="hidden"  class="searchField" value="phone"/>公司电话：
		                    <input type="hidden" class="searchOper"  value="cn"/>
		                </td>  
		                <td>  
		                   <input type="text" class="searchString"/> 
		                </td>  
		            </tr> 
		            
		            <tr>  
		                <td>  
		                    <input type="hidden"  class="searchField" value="website"/>公司网址：
		                    <input type="hidden" class="searchOper"  value="cn"/>
		                </td>  
		                <td>  
		                   <input type="text" class="searchString"/> 
		                </td>  
		            </tr>	            		          
		                 
		        </tbody>  
		    </table>  
		</div>
		<!-- ----- -->
		
		<script src="<c:url value="/"/>js/multisearch.js"></script> <!--加载模态多条件查询相关js-->
		<script type="text/javascript"> 		
			
		    
		   	
		   //显示公司名称
		   function formatCompanyName(cellValue, options, rowObject) {			
				var html = '';
				html = "<a href='javascript:;' onclick='doView("+options.rowId+")'>" + cellValue + "</a>";				
				return html;
			}
			
	
			//加载表格数据
			var $mygrid = jQuery("#listCustomer").jqGrid({ 
                url:'/customer/customerInfor.do?method=list&type=2',
                //rownumbers: true,
                datatype: "json",                
               	autowidth: true,               	
				//height: "auto",
				height:300,
                colNames:['Id','公司名称','公司地址','网址','电话'],
                colModel:[
                    {name:'customerId',index:'customerId',width:0, sorttype:"int", search:false, key:true, hidden:true},                               
                    {name:'companyName',index:'companyName',align:'left',width:70,formatter:formatCompanyName,editable:true,sorttype:"string"}, 
                    {name:'companyAddress',index:'companyAddress',align:'left',editable:true,sorttype:"string"}, 
                    {name:'website',index:'website',align:'left',width:50,editable:true,sorttype:"string"}, 
                    {name:'phone',index:'phone',align:'center',width:30,editable:true,sorttype:"string"} 
                ],
                sortname: 'customerId',
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
                pager: "#pagerCustomer"
                //caption: "已收到的讯息"
        }).navGrid('#pagerCustomer',{edit:false,add:false,del:false,search:false});     
        
        $("gbox_listCustomer").css("width","100%");  
      		
		//自定义按钮
		$("#listCustomer").jqGrid('navButtonAdd','#pagerCustomer', {
			caption:"<span style='color: red;'>批量删除</span>", title:"点击批量删除", buttonicon:'ui-icon-closethick', onClickButton: deleteCustomer
		});
		
		$("#listCustomer").jqGrid('navButtonAdd','#pagerCustomer', {
			caption:"查询", title:"点击查询客户信息", buttonicon:'ui-icon-search', onClickButton: openMultipleSearchDialog
		});
		
		//批量删除
		function deleteCustomer(){
			doDelete("/customer/customerInfor.do?method=delete","listCustomer");
		}
		
	</script>

