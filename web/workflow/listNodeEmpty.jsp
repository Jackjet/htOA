<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>
	  				
	<script type="text/javascript">
		//加载表格数据
				var $mygrid = jQuery("#listNode").jqGrid({ 
					url:'/workFlowDefinition.do?method=nodeList',
		            //rownumbers: true,
		            datatype: "json",                
		           	autowidth: true,               	
					height: "auto",
					//height:300,
		            colNames:['Id','节点名称', '节点类型','层次','是否分叉类节点','来自分叉的节点','能否打印','能否下载','能否上传','操作'],
		            colModel:[
		                {name:'nodeId',index:'nodeId',width:0, sorttype:"int", key:true, hidden:true },                    
		                {name:'nodeName',index:'nodeName',width:30,align:'left', formatter:formatTitle},
		                {name:'nodeType',index:'nodeType',width:20,align:'center', formatter:formatNodeType},                       
		                {name:'layer',index:'layer',align:'center', width:20},
		                {name:'forked',index:'forked', align:'center',width:30,formatter:formatForked},
		                {name:'forkedNode',index:'forkedNode',align:'center', width:30},
		                {name:'printable',index:'printable',align:'center',width:20,formatter:formatOperate},
		                {name:'download',align:'download', align:'center',width:20,formatter:formatOperate},
		                {name:'upload',align:'upload', align:'center',width:20,formatter:formatOperate},
		                {name:'operate',align:'center', width:20,formatter:formatOperation}
		                
		            ],
		            //caption: "工作流流程定义",
		            sortname: 'nodeId',
		            multiselect: true,	// 是否支持多选,可用于批量删除
		            sortorder: 'asc',
		            viewrecords: true,
		            rowNum: 5,
		            rowList: [5,10],
		            scroll: false, 
		            scrollrows: false,                          
		            jsonReader:{
		               repeatitems: false
		            },         
		            pager: "#pagerNode"
			     }).navGrid('#pagerNode',{edit:false,add:false,del:false,search:false}); 
			     
			     //自定义按钮
				jQuery("#listNode").jqGrid('navButtonAdd','#pagerNode', {
					caption:"新增", title:"点击新增节点", buttonicon:'ui-icon-plusthick', onClickButton: addInfor
				});
				jQuery("#listNode").jqGrid('navButtonAdd','#pagerNode', {
					caption:"<span style='color: red;'>删除</span>", title:"点击删除", buttonicon:'ui-icon-closethick', onClickButton: deleteInfor
				});
	</script>