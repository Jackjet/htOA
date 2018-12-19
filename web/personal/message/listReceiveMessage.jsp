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
        multiSearchParams[0] = "#listReceive";			//列表Id
        multiSearchParams[1] = "#multiSearchDialogReceive";//查询模态窗口Id
		//查看
		function doViewR(rowId){
			//window.showModalDialog("/personal/receiveMessage.do?method=viewReceiveMesage&rowId="+rowId,'',"dialogWidth:1000px;dialogHeight:600px;center:Yes;dialogTop: 100px; dialogLeft: 200px;");
			window.open("/personal/receiveMessage.do?method=viewReceiveMesage&rowId="+rowId,"_blank");
		}		
	</script>
	<body style="border:1px solid #0DE8F5;border-radius: 5px">
		<div style="margin-top:0px;">
			<table id="listReceive" style="width:99%"></table>
			<div id="pagerReceive"></div>
		</div>

		<!-- 查询框 -->
		<div id="multiSearchDialogReceive" style="display: none;">
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
						<input type="hidden" value="person.department.organizeId"/>所属部门：
						<input type="hidden" value="eq"/>
					</td>
					<td>
						<select id="departmentId"></select>
					</td>
				</tr>

				<%--<tr>
                    <td>
                        <input type="hidden" value="person.group.organizeId"/>所属班组：
                        <input type="hidden" value="eq"/>
                    </td>
                    <td>
                        <select id="groupId"></select>
                    </td>
                </tr>

                --%><tr>
					<td>
						<input type="hidden" class="searchField" value="sender.personId"/>发送者：
						<input type="hidden" class="searchOper" value="eq"/>
					</td>
					<td>
						<select  class="searchString" id="personId">
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
	</body>

		<!-- ----- -->
		
		<%--<script src="<c:url value="/"/>js/multisearch.js"></script> <!--加载模态多条件查询相关js-->--%>
		<script type="text/javascript"> 		
			
			//自定义操作栏的显示内容
		    function formatOperation(cellvalue, options, rowdata) {
	           var returnStr = "<a href='javascript:;' onclick='doView("+options.rowId+")'>[查看]</a>";
	           return returnStr;
		    }
		    
		    //显示是否阅读
		    function formatRead(cellValue, options, rowObject) {				
				var html = '';
				if(cellValue==1){
					html = '<img src="<c:url value="/"/>images/icon_read.gif">';
				}else {
					html = '<img src="<c:url value="/"/>images/icon_readnot.gif">';
				}
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
		   
		   //显示标题
		   function formatTitleR(cellValue, options, rowObject) {			
				var html = '';
				html = "<a href='javascript:;' onclick='doViewR("+options.rowId+")'>" + cellValue + "</a>";				
				return html;
			}
			
			function formatAttachment(cellValue, options, rowObject) {				
				var html = '';
				//alert(cellValue);
				html = showAttachment(cellValue,'');				
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
	
			//加载表格数据
			var $mygrid = jQuery("#listReceive").jqGrid({ 
                url:'/personal/receiveMessage.do?method=listRecive',
                rownumbers: true,
                datatype: "json",                
               	autowidth: true,
                height:document.documentElement.clientHeight-140,
                colNames:['Id','阅读', '标题','发送时间','发送者','接收者','重要','附件'],
                colModel:[
                    {name:'messageId',index:'messageId',/*width:0, */sorttype:"int", search:false, key:true, hidden:true},
                    {name:'isReaded',index:'isReaded',/*width:10,*/align:'center', formatter:formatRead, resizable: false, sortable:false},
                    {name:'messageTitle', align:'left',sortable:true, formatter:formatTitleR,sorttype:"string"},
                    {name:'sendTimeStr',index:'sendTime',align:'center',/*width:60,*/editable:true,sorttype:"string"},
                    {name:'senderName', align:'center',/*width:30,*/sortable:false},
                    {name:'personNames', align:'center',/*width:60,*/sortable:false, formatter:formatReceive,sorttype:"string"},
                    {name:'isImportant',index:'isImportant',align:'center',/*width:20,*/formatter:formatImportant,resizable: false, sortable:true},
                    {name:'attachmentStr',align:'center',/*width:20,*/formatter:formatAttachment,sortable:false}
                    //,{name:'messageId', width:40, align:'center', search:false, sortable:false, formatter:formatOperation}
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
                pager: "#pagerReceive"
                //caption: "已收到的讯息"
        }).navGrid('#pagerReceive',{edit:false,add:false,del:false,search:false}).closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
        
        $("gbox_listReceive").css("width","100%");  
      		
		//显示各个栏目上的搜索栏
		// $('#listReceive').jqGrid('filterToolbar','');
		
		//自定义按钮
		$("#listReceive").jqGrid('navButtonAdd','#pagerReceive', {
			caption:"<span style='color: red;'>批量删除</span>", title:"点击批量删除", buttonicon:'ui-icon-closethick', onClickButton: deleteReceiveMessage
		});
		
		$("#listReceive").jqGrid('navButtonAdd','#pagerReceive', {
			caption:"查询", title:"点击查询接收讯息", buttonicon:'ui-icon-search', onClickButton: openMultipleSearchDialog
		});
		
		//批量删除
		function deleteReceiveMessage(){
			doDelete("/personal/receiveMessage.do?method=receiveDelete","listReceive");
		}
			
		/** 查询条件中的部门,班组,用户下拉联动 */
		//部门信息初始化
		$('#departmentId').selectInit();
		
		//加载部门及联动信息		
		$.loadDepartments("departmentId", null, "personId");
		/** ******** */
		       	
	</script>

