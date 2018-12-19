<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<script type="text/javascript">
	
	$().ready(function(){
		//配置创建文件的模态窗口
		$("#fileModalDlg"+"${param.fileId}").dialog({
			autoOpen: false,       
			modal: true,    //设置对话框为模态(modal)对话框   
			resizable: true,       
			width: 280,   
			cache: false,
			buttons: {  	//为对话框添加按钮   
				"取消": function() {$("#fileModalDlg"+"${param.fileId}").dialog("close")},
				"保存": addFile
			}
		});
		
	});
	
	//打开创建文件的模态窗口
	function openFileModal() {
		var consoleDlg = $("#fileModalDlg"+"${param.fileId}");
		var dialogButtonPanel = consoleDlg.siblings(".ui-dialog-buttonpane");   
		consoleDlg.dialog("option", "title", "输入创建文件的相关信息");
		consoleDlg.dialog("open"); 
	}
	
	//创建文件
	function addFile(){
		var form = document.getElementById("fileForm"+"${param.fileId}");
		if (form.fileName.value == '') {
			alert("请输入文件名！");
		}else {
			form.action = "/cms/inforTemplate.do?method=addFile&templateStyle=${param.templateStyle}&path=${param.path}";
			form.submit();
			self.location.reload();
			$("#fileModalDlg"+"${param.fileId}").dialog("close");
		}
	}
	
</script>

<title></title>

		<div id="fileModalDlg${param.fileId}" style="display: none;">  
	        <form id="fileForm${param.fileId}" name="fileForm${param.fileId}" method="post">
		        <table>
		        	<tr>  
			           <td>文件类型：</td>
			           <td>  
			               <select name="fileType">
			               		<option value="floder">文件夹</option>
			               		<option value="ftl">ftl</option>
			               		<option value="js">js</option>
			               		<option value="css">css</option>
			               		<option value="html">html</option>
			               		<option value="txt">txt</option>
			               </select>
			           </td>  
			        </tr>
			        <tr>  
			           <td>文件名称：</td>
			           <td>  
			               <input type="text" name="fileName" size="25"/>  
			           </td>  
			        </tr>
		        </table>
	        </form> 
	    </div>

  		<div>
			<table id="list${param.fileId}"></table>
			<div id="pager${param.fileId}"></div>
		</div>
		
		<script type="text/javascript">
			
			//加载表格数据
			var $mygrid = jQuery("#list"+"${param.fileId}").jqGrid({
                url:"/cms/inforTemplate.do?method=listFile&templateStyle=${param.templateStyle}&path=${param.path}",
                datatype: "json",
               	autowidth: true,
				height:300,
                colNames: ['Id', '文件名', '大小', '最后修改时间'],
                colModel:[
                    {name:'path', width:0, search:false, key:true, hidden:true},
                    {name:'fileName',index:'fileName', width:40, sortable:true, sorttype:"string"},
                    {name:'fileSize',index:'fileSize', width:40, align:'right'},
                    {name:'updateDate',index:'updateDate', width:40, align:'center'}
                ],
                sortorder: 'asc',
                multiselect: true,	//是否支持多选,可用于批量删除
                viewrecords: true,
                scroll: false, 
                scrollrows: false,                          
                jsonReader:{
                   repeatitems: false
                },         
                pager: "#pager"+"${param.fileId}"
	        }).navGrid('#pager'+"${param.fileId}",{edit:false,add:false,del:false,search:false});       
			
			//自定义按钮
			jQuery("#list"+"${param.fileId}").jqGrid('navButtonAdd','#pager'+"${param.fileId}", {
				caption:"新增", title:"点击新增信息", buttonicon:'ui-icon-plusthick', onClickButton: openFileModal
			});
			jQuery("#list"+"${param.fileId}").jqGrid('navButtonAdd','#pager'+"${param.fileId}", {
				caption:"<span style='color: red;'>批量删除</span>", title:"点击批量删除", buttonicon:'ui-icon-closethick', onClickButton: deleteInfor
			});
			
			//批量删除
			function deleteInfor(){
				doDelete("/cms/inforTemplate.do?method=delete","list"+"${param.fileId}");
			}
			
		</script>