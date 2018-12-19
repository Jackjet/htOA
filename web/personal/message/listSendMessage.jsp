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
<script src="<c:url value="/"/>js/changeclass.js"></script> <!--用于改变页面样式-->
<%--<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />--%>
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>css/base/jquery-ui-1.9.2.custom.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/tabstyle.css" />
	<script type="text/javascript">
        var multiSearchParams = new Array();
        multiSearchParams[0] = "#listSend";			//列表Id
        multiSearchParams[1] = "#multiSearchDialogSend";//查询模态窗口Id
		//查看
		function doViewS(rowId){
			//window.showModalDialog("/personal/messageInfor.do?method=viewSendMesage&rowId="+rowId, "viewMessage" ,"dialogHeight:400px;dialogWidth:500px;center:Yes;dialogTop:100px; dialogLeft:200px;status:no;scroll:yes;");
			window.open("/personal/messageInfor.do?method=viewSendMesage&rowId="+rowId,"_blank");
		}
		//发送新数据
		function sendNewMessage(){
			var refresh = window.showModalDialog("/personal/messageInfor.do?method=sendMessage","editMessage","dialogHeight:400px;dialogWidth:600px;center:Yes;dialogTop: 100px; dialogLeft: 200px;");
			if(refresh == "Y") {
				reloadTab2();
			}
		}
	</script>
<body style="border:1px solid #0DE8F5;border-radius: 5px">
  		<div style="margin-top:0px;">
			<table id="listSend" style="width:99%"></table>
			<div id="pagerSend"></div>
		</div>	
		
		<!-- 查询 -->
		<div id="multiSearchDialogSend" style="display: none;">  
		    <table>  
		        <tbody>  
		            <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="messageTitle"/>标题：
		                    <input type="hidden" class="searchOper" value="cn"/>
		                </td>  
		                <td>  
		                    <input type="text" class="searchString"/>  
		                </td>
		            </tr>  
		            <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="isImportant"/>是否重要：
		                    <input type="hidden" class="searchOper" value="eq"/>
		                </td>  
		                <td>  
		                    <select class="searchString">  
		                        <option value="">所有</option>
		                        <option value="0">否</option>
		                        <option value="1">是</option>
		                    </select>
		                </td>  
		            </tr>
		            <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="sendTime"/>起始时间：
		                    <input type="hidden" class="searchOper" value="gt"/>
		                </td>  
		                <td>  
		                    <input  class="searchString"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly="true"/>
		                </td>  
		            </tr>
		             <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="sendTime"/>结束时间：
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
</body>
		<script src="<c:url value="/"/>js/multisearch.js"></script> <!--加载模态多条件查询相关js-->
		<script type="text/javascript"> 	
			//自定义操作栏的显示内容
		    function formatOperation(cellvalue, options, rowdata) {
	           var returnStr = "<a href='javascript:;' onclick='doSee("+options.rowId+")'>[查看]</a>";
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
		    
		     	 //显示是否回复
		    function formatReply(cellValue, options, rowObject) {				
				var html = '';
				if(cellValue==1){
					html = '<img src="/images//icon_reply.gif">'; 
				}
				return html;
			}
			
		     //显示标题
		   function formatTitleS(cellValue, options, rowObject) {				
				var html = '';
				html = "<a href='javascript:;' onclick='doViewS("+options.rowId+")'>" + cellValue + "</a>";				
				return html;
			}
			
			//显示是否重要
		   	function formatImportant(cellValue, options, rowObject) {				
				var html = '';
				if(cellValue==1){
					html = '<img src="/images/flag_red.gif">'; 
				}
				return html;
			}	
			
			function formatReceive(cellValue, options, rowObject) {				
				var html = '';
				//alert(cellValue);
				html = showReceive(cellValue,'');				
				return html;
			}		
			
			//显示接收者
			function showReceive(attachment,contextPath) {
				var returnAttachment = "";
				if (attachment!=null && attachment!=""){
					attachment = attachment.Trim();
					var filePaths = attachment.split("\|");
					for(var k =0; k<filePaths.length;k++){
						returnAttachment += filePaths[k];
						if (k!=0&&k!=4&&k!=(filePaths.length-1)) returnAttachment += "&nbsp;&nbsp,";
						if(k==4){
							returnAttachment+="......"
							return returnAttachment;
						}
					}
				}
				return returnAttachment;
			}
			
		    //自定义用户类型栏的显示内容
		    function formatUserType(cellvalue) {
	           var returnStr;
	           if (cellvalue == '0') {
	              returnStr = "普通用户";
	           }else if (cellvalue == '1'){
	              returnStr = "<font color='red'>系统管理员</font>";
	           }else {
	           	  returnStr = "档案员";
	           }
	           return returnStr;
		    }
			
			function formatAttachment(cellValue, options, rowObject) {				
				var html = '';
				//alert(cellValue);
				html = showAttachment(cellValue,'');				
				return html;
			}		
			
			//加载表格数据
			var $mygrid = jQuery("#listSend").jqGrid({ 
                url:'/personal/messageInfor.do?method=listSend',
                //rownumbers: true,
                datatype: "json",                
                autowidth: true,
                height:document.documentElement.clientHeight-140,
                colNames:['Id','回复', '标题','接收者','发送时间','重要','附件'],
                colModel:[
                    {name:'messageId',index:'messageId', width:0, sorttype:"int",search:false, key:true, hidden:true},
                   	{name:'isReply',index:'isReply',width:10,align:'center', formatter:formatReply, resizable: false, sortable:false}, 
                    {name:'messageTitle', align:'left',sortable:true, formatter:formatTitleS,sorttype:"string"},
                    {name:'personNames', align:'left',width:60,sortable:false, formatter:formatReceive,sorttype:"string"},
                    {name:'sendTimeStr',index:'sendTime',align:'center',width:60,editable:true,sorttype:"string"}, 
                    {name:'isImportant',index:'isImportant',align:'center',width:20,formatter:formatImportant,resizable: false, sortable:true}, 
                    {name:'attachmentStr',width:20,formatter:formatAttachment,sortable:false}
                ],
                sortname: 'messageId',
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
                pager: "#pagerSend"
                //caption: "已发送的讯息"
        }).navGrid('#pagerSend',{edit:false,add:false,del:false,search:false}).closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
        //}).navGrid('#pagerSend',{edit:false,add:false,del:false}).searchGrid({multipleSearch:true,autoOpen:false});
		
		//显示各个栏目上的搜索栏
		// $('#listSend').jqGrid('filterToolbar','');
		
		//自定义按钮
		$("#listSend").jqGrid('navButtonAdd','#pagerSend', {
			caption:"<span style='color: red;'>批量删除</span>", title:"点击批量删除", buttonicon:'ui-icon-closethick', onClickButton: deleteSendMessage
		});
		
		$("#listSend").jqGrid('navButtonAdd','#pagerSend', {
			caption:"查询", title:"点击查询接收邮件", buttonicon:'ui-icon-search', onClickButton: openMultipleSearchDialog
		});
		
		$("#listSend").jqGrid('navButtonAdd','#pagerSend', {
			caption:"新建", title:"点击发送新邮件", buttonicon:'ui-icon-plusthick', onClickButton: sendNewMessage
		});
		
		//批量删除
		function deleteSendMessage(){
			doDelete("/personal/messageInfor.do?method=sendDelete","listSend");
		}
		
	</script>
	
	