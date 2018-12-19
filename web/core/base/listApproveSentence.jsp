<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">


<script type="text/javascript">
	//新增
	function addApproveSentence(){
		/*var returnRolTag = window.showModalDialog("/core/approveSentence.do?method=edit",null,"dialogWidth:800px;dialogHeight:350px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		if(returnRolTag == "refresh") {
			//保存信息后重新加载tab
			loadTab("listApproveSentence.jsp", "9");
		}*/
        window.open("/core/approveSentence.do?method=edit","_blank");
	}
	//修改
	function editApproveSentence(rowId){
		/*var returnRolTag = window.showModalDialog("/core/approveSentence.do?method=edit&rowId="+rowId,'',"dialogWidth:800px;dialogHeight:350px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		if(returnRolTag == "refresh") {
			//保存信息后重新加载tab
			loadTab("listApproveSentence.jsp", "9");
		}*/
        window.open("/core/approveSentence.do?method=edit&rowId="+rowId,"_blank");
	}
</script>

<title>惯用语信息</title>

  		<div>
			<table id="listApproveSentence"></table>
			<div id="pagerApproveSentence"></div>
		</div>
		
		<script type="text/javascript"> 		
			
			//自定义操作栏的显示内容
		    function formatOperation(cellvalue, options, rowdata) {
	           var returnStr = "<a href='javascript:;' onclick='editApproveSentence("+options.rowId+")'>[修改]</a>";
	          
	              returnStr += " <a href='javascript:;' onclick='deleteApproveSentence("+options.rowId+")'>[删除]</a>";
	           
	           return returnStr;
		    }
		    
			//加载表格数据
			var $mygrid = jQuery("#listApproveSentence").jqGrid({ 
                url:'/core/approveSentence.do?method=list',
                rownumbers: true,
                datatype: "json",                
               	autowidth: true,
				//height: "auto",
                height:document.documentElement.clientHeight-240,
                colNames:['Id', '惯用语','序号', '操作'],
                colModel:[
                    {name:'sentenceId',index:'sentenceId', width:0, sorttype:"int", search:false, key:true, hidden:true},
                    {name:'sentence',index:'sentence', sortable:true, sorttype:"string"},
                    {name:'orderNo',index:'orderNo', sortable:true, sorttype:"string"},
                    {name:'fixed', width:40, align:'center', search:false, sortable:false, formatter:formatOperation}
                ],
                sortname: 'sentenceId',
                sortorder: 'asc',
                viewrecords: true,
                rowNum: 10,
                rowList: [10,20,30],
                scroll: false, 
                scrollrows: false,                          
                jsonReader:{
                   repeatitems: false
                },         
                pager: "#pagerApproveSentence"
                //caption: "角色信息"
        }).navGrid('#pagerApproveSentence',{edit:false,add:false,del:false,search:false}).closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
        //}).navGrid('#pagerRole',{edit:false,add:false,del:false}).searchGrid({multipleSearch:true,autoOpen:false});
		
		//自定义按钮
		jQuery("#listApproveSentence").jqGrid('navButtonAdd','#pagerApproveSentence', {
			caption:"新增", title:"点击新增信息", buttonicon:'ui-icon-plusthick', onClickButton: addApproveSentence
		});
		
		//删除数据
		function deleteApproveSentence(rowId){
			if(rowId!=null || rowId!=0){			
				var yes = window.confirm("确定要删除吗？");
				if (yes) {
					$.ajax({
						url: "/core/approveSentence.do?method=delete&rowId="+rowId,	//删除数据的url
						cache: false,
						//data:{personId: rowId},
						type: "POST",
						dataType: "html",
						beforeSend: function (xhr) {							
						},
						
						complete : function (req, err) {
							alert("数据已经删除！");
							$("#listApproveSentence").trigger("reloadGrid"); 
						}
					});
				}
			}			
		}
		
	</script>