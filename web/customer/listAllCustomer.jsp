<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

	<script type="text/javascript">     
		//查看
		function doView(rowId){
			window.showModalDialog("/customer/customerInfor.do?method=viewCustomer&rowId="+rowId,'',"dialogWidth:1000px;dialogHeight:600px;center:Yes;dialogTop: 100px; dialogLeft: 200px;");
		}		
	
		//增加
		function addCustomerInfor(){
			var refresh = window.showModalDialog("/customer/customerInfor.do?method=edit",'',"dialogWidth:1000px;dialogHeight:600px;center:Yes;dialogTop: 100px; dialogLeft: 200px;");
			if(refresh == "Y") {
				self.location.reload();
			}
		}
	</script>
	
  		<div style="margin-top:0px;">
			<table id="listAllCustomer" style="width:99%"></table>
			<div id="pagerAllCustomer"></div>
		</div>	
		
		<!-- 查询框 -->
		<div id="multiSearchDialogAllCustomer" style="display: none;">  
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
			
		    
		    //显示客户类型
		    function formatStyle(cellValue, options, rowObject) {				
				var html = '';
				if(cellValue==0){
					html = '潜在'; 
				}else{
					html = '客户'; 
				}
				return html;
			}
		   	
	   
		   //显示公司名称
		   function formatCompanyName(cellValue, options, rowObject) {			
				var html = '';
				html = "<a href='javascript:;' onclick='doView("+options.rowId+")'>" + cellValue + "</a>";				
				return html;
			}
			

			//加载表格数据
			var $mygrid = jQuery("#listAllCustomer").jqGrid({ 
                url:'/customer/customerInfor.do?method=list&type=1',
                //rownumbers: true,
                datatype: "json",                
               	autowidth: true,               	
				//height: "auto",
				height:300,
                colNames:['Id','类型', '公司名称','公司地址','网址','电话'],
                colModel:[
                    {name:'customerId',index:'customerId',width:0, sorttype:"int", search:false, key:true, hidden:true},                    
                    {name:'customerType',index:'customerType',width:10,align:'center', formatter:formatStyle, resizable: false, sortable:false},                    
                    {name:'companyName',index:'companyName',align:'left',width:65,formatter:formatCompanyName,editable:true,sorttype:"string"}, 
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
                pager: "#pagerAllCustomer"
                //caption: "已收到的讯息"
        }).navGrid('#pagerAllCustomer',{edit:false,add:false,del:false,search:false});     
        
        $("gbox_listAllCustomer").css("width","100%");  
      		
		//自定义按钮
		$("#listAllCustomer").jqGrid('navButtonAdd','#pagerAllCustomer', {
			caption:"<span style='color: red;'>批量删除</span>", title:"点击批量删除", buttonicon:'ui-icon-closethick', onClickButton: deleteCustomer
		});
		
		$("#listAllCustomer").jqGrid('navButtonAdd','#pagerAllCustomer', {
			caption:"查询", title:"点击查询客户信息", buttonicon:'ui-icon-search', onClickButton: openMultipleSearchDialog
		});
		
		$("#listAllCustomer").jqGrid('navButtonAdd','#pagerAllCustomer', {
			caption:"增加", title:"点击增加客户信息", buttonicon:'ui-icon-plusthick', onClickButton: addCustomerInfor
		});
		
		//批量删除
		function deleteCustomer(){
			doDelete("/customer/customerInfor.do?method=delete","listAllCustomer");
		}
			
		       	
	</script>

