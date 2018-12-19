<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>
	  				
	<script type="text/javascript">
		//加载表格数据
				var $mygrid = jQuery("#listNode").jqGrid({
					url:'/workFlowDefinition.do?method=nodeList&flowId='+${_FlowDefinition.flowId},
		            //rownumbers: true,
		            datatype: "json",                
		           	autowidth: true,               	
					height: "auto",
					//height:300,
		            colNames:['Id','节点名称', '节点类型','层次','分叉内节点','来自分叉的节点','打印','下载附件','上传附件','操作'],
		            colModel:[
		                {name:'nodeId',index:'nodeId',width:0, sorttype:"int", key:true, hidden:true },                    
		                {name:'nodeName',index:'nodeName',width:30,align:'left', formatter:formatTitle},
		                {name:'nodeType',index:'nodeType',width:20,align:'center', formatter:formatNodeType},                       
		                {name:'layer',index:'layer',align:'center', width:20},
		                {name:'forked',index:'forked', align:'center',width:30,formatter:formatForked},
		                {name:'forkedNode.nodeName',index:'forkedNode.nodeName',align:'center', width:30},
		                {name:'printable',index:'printable',align:'center',width:20,formatter:formatOperate},
		                {name:'download',align:'download', align:'center',width:20,formatter:formatOperate},
		                {name:'upload',align:'upload', align:'center',width:20,formatter:formatOperate},
		                {name:'operate',align:'center', width:20,formatter:formatOperation}
		                
		            ],
		            //caption: "工作流流程定义",
		            sortname: 'layer',
		            multiselect: true,	// 是否支持多选,可用于批量删除
		            sortorder: 'asc',
		            viewrecords: true,
		            rowNum: 10,
		            rowList: [5,10],
		            scroll: false, 
		            scrollrows: false,                          
		            jsonReader:{
		               repeatitems: false
		            },         
		            pager: "#pagerNode"
			     }).navGrid('#pagerNode',{edit:false,add:false,del:false,search:false}).closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
			     
			     //自定义按钮
				jQuery("#listNode").jqGrid('navButtonAdd','#pagerNode', {
					caption:"新增", title:"点击新增节点", buttonicon:'ui-icon-plusthick', onClickButton: addInfor
				});
				jQuery("#listNode").jqGrid('navButtonAdd','#pagerNode', {
					caption:"<span style='color: red;'>删除</span>", title:"点击删除", buttonicon:'ui-icon-closethick', onClickButton: deleteNode
				});
				
				function deleteNode(url, listId){
					//获取选择的行的Id
					var rowIds = jQuery("#listNode").jqGrid('getGridParam','selarrrow'); 
					var url = "/workFlowDefinition.do?method=deleteNode";
						
					if(rowIds != null && rowIds.length > 0){
						var yes = window.confirm("确定要删除吗？");
						if (yes) {
							$.ajax({
								url: url+"&rowIds="+rowIds,	//删除数据的url
								cache: false,
								type: "POST",
								dataType: "html",
								beforeSend: function (xhr) {						
								},
									
								complete : function (req, err) {
									alert("数据已经删除！");
									$("#listNode").trigger("reloadGrid"); 
								}
							});	
						}
					}else {
						alert("请选择要删除的数据！");
					}		
				}
	</script>