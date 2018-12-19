<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">


<script type="text/javascript">
	//新增
	function addRole(){
		/*var returnRolTag = window.showModalDialog("/core/roleInfor.do?method=edit",null,"dialogWidth:800px;dialogHeight:350px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		if(returnRolTag == "refresh") {
			//保存信息后重新加载tab
			loadTab("listRole.jsp", "3");
		}*/
        window.open("/core/roleInfor.do?method=edit","_blank");
	}
	//修改
	function editRole(rowId){
		/*var returnRolTag = window.showModalDialog("/core/roleInfor.do?method=edit&rowId="+rowId,'',"dialogWidth:800px;dialogHeight:350px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		if(returnRolTag == "refresh") {
			//保存信息后重新加载tab
			loadTab("listRole.jsp", "3");
		}*/
        window.open("/core/roleInfor.do?method=edit&rowId="+rowId,"_blank");
	}
</script>

<title>角色信息</title>

  		<div>
			<table id="listRole"></table>
			<div id="pagerRole"></div>
		</div>
		
		<script type="text/javascript"> 		
			
			//自定义操作栏的显示内容
		    function formatOperation(cellvalue, options, rowdata) {
	           var returnStr = "<a href='javascript:;' onclick='editRole("+options.rowId+")'>[修改]</a>";
	           if (cellvalue) {
	              returnStr += " <a href='javascript:;'><font color='gray'>[删除]</font></a>";
	           }else {
	              returnStr += " <a href='javascript:;' onclick='deleteRole("+options.rowId+")'>[删除]</a>";
	           }
	           return returnStr;
		    }
		    
			//加载表格数据
			var $mygrid = jQuery("#listRole").jqGrid({ 
                url:'/core/roleInfor.do?method=list',
                rownumbers: true,
                datatype: "json",                
               	autowidth: true,
				//height: "auto",
                height:document.documentElement.clientHeight-240,
                colNames:['Id', '角色', '操作'],
                colModel:[
                    {name:'roleId',index:'roleId', width:0, sorttype:"int", search:false, key:true, hidden:true},
                    {name:'roleName',index:'roleName', sortable:true, sorttype:"string"},
                    {name:'fixed', width:40, align:'center', search:false, sortable:false, formatter:formatOperation}
                ],
                sortname: 'roleId',
                sortorder: 'asc',
                viewrecords: true,
                rowNum: 10,
                rowList: [10,20,30],
                scroll: false, 
                scrollrows: false,                          
                jsonReader:{
                   repeatitems: false
                },         
                pager: "#pagerRole"
                //caption: "角色信息"
        }).navGrid('#pagerRole',{edit:false,add:false,del:false,search:false}).closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
        //}).navGrid('#pagerRole',{edit:false,add:false,del:false}).searchGrid({multipleSearch:true,autoOpen:false});
		
		//自定义按钮
		jQuery("#listRole").jqGrid('navButtonAdd','#pagerRole', {
			caption:"新增", title:"点击新增信息", buttonicon:'ui-icon-plusthick', onClickButton: addRole
		});
		
		//删除数据
		function deleteRole(rowId){
			if(rowId!=null || rowId!=0){			
				var yes = window.confirm("确定要删除吗？");
				if (yes) {
					$.ajax({
						url: "/core/roleInfor.do?method=delete&rowId="+rowId,	//删除数据的url
						cache: false,
						//data:{personId: rowId},
						type: "POST",
						dataType: "html",
						beforeSend: function (xhr) {							
						},
						
						complete : function (req, err) {
							alert("数据已经删除！");
							$("#listRole").trigger("reloadGrid"); 
						}
					});
				}
			}			
		}
		
	</script>