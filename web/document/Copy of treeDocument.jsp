<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBliC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
		<title>文档大全</title>
		<script src="<c:url value='/'/>datePicker/WdatePicker.js" type="text/javascript"></script>
		<script src="<c:url value="/"/>components/jquery-1.4.2.js" type="text/javascript"></script> <!--jquery包-->
		<script src="<c:url value="/"/>components/jqgrid/js/jquery-ui-1.8.2.custom.min.js" type="text/javascript"></script><!--jquery ui-->
		<script src="<c:url value="/"/>components/jquery.layout.js" type="text/javascript"></script><!--jquery 布局-->
		<script src="<c:url value="/"/>components/jqgrid/js/grid.locale-cn.js" type="text/javascript"></script> <!--jqgrid 中文包-->
		<script src="<c:url value="/"/>components/jqgrid/js/jquery.jqGrid.min.js" type="text/javascript"></script> <!--jqgrid 包-->
		<script src="<c:url value="/"/>js/commonFunction.js"></script>
		<script src="<c:url value="/"/>js/inc_javascript.js"></script>
		<script src="<c:url value="/"/>js/multisearch.js"></script> <!--加载模态多条件查询相关js-->
		<script src="<c:url value="/"/>js/changeclass.js"></script> <!--用于改变页面样式-->
		<script src="<c:url value="/"/>js/jquery.contextmenu.r2.packed.js"></script><!-- 菜单树右键 -->
		<script src="<c:url value="/"/>js/changeclass.js"></script> <!--用于改变页面样式-->
		
		<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />
		<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
		<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/tabstyle.css" />
		
		<script type="text/javascript">
			$().ready(function(){
			//菜单树中的右键事件
			var firstMenu = {   //某个层绑定
			    bindings: {
		          'item_1': function(t) {
		            //alert("id为 "+t.id+" 下面的--新增子节点");
		            doAdd();
		          },
		
		          //'item_2': function(t) {
		          //  alert("id为 "+t.id+" 下面的--设置权限");
		          //},
		
		          'item_3': function(t) {
		            //alert("id为 "+t.id+" 下面的--重命名");
		            openUpdate(t.id);
		          },
		
		          'item_4': function(t) {
		            //alert("id为 "+t.id+" 下面的--删除");
		            deleteCategoryName(t.id);
		          }
		        },
		        menuStyle: {
		          border: '1px solid #85b5d9'
		        },
		        itemStyle : {
		          fontFamily: 'verdana',
		          backgroundColor: '#e1effb',
		          color: '#2e6e9e',
		          border: 'none',
		          padding: '1px'
		        },
		        
		        itemHoverStyle: {
		          color: 'white',
		          backgroundColor: '#87b6d9',
		          border: 'none'
		        }
		      };
			
				$('body').layout({
					resizerClass: 'ui-state-default',
			        west__onresize: function (pane, $Pane) {
			            jQuery("#west-grid").jqGrid('setGridWidth',$Pane.innerWidth()-2);
					}
				});
				
				    
				//加载菜单树
		    	jQuery("#west-grid").jqGrid({
			        url:"/document/document.do?method=list", 			        
			        datatype: "json",
			        //async: true,
			        treedatatype: "json",
			        height: "auto",
			        pager: false,
			        loadui: "disable",
			        colNames: ["Id","分类列表","fromReport","leaf"],
			        colModel: [
			            {name: "categoryId",index:"categoryId", sorttype:"int", width:1, hidden:true, key:true},
			            {name: "categoryName",index:"categoryName", resizable: false, sortable:false,editable:true},//,editable:true,edittype:'text',editoptions:{size:25}
			            {name:'fromReport',index:'fromReport',width:1,hidden:true},
			            {name: "leaf",index:"leaf", width:1, hidden:true}
			        ],
			        treeReader : {
					    level_field: 'layer',
					    left_field: 'leftIndex',
					    right_field: 'rightIndex',
					    leaf_field: "leaf",
					    expanded_field: false
					},
					treeGridModel: 'nested',					
			        sortname: 'categoryName',//默认排序的字段
			        treeGrid: true,		//树形grid
					caption: "文档分类",
			        ExpandColumn: "categoryName",
			        autowidth: true,
			        rowNum: 200,
			        //ExpandColClick: true, 
			        treeIcons: {leaf:'ui-icon-document-b'},
			        jsonReader:{
		               repeatitems: false	//告诉JqGrid,返回的数据的标签是否是可重复的
		            },
	                
	                //加载完成后添加右键事件
	                afterInsertRow: function(rowid,rowdata,rowelem) {
	                	$('#'+rowid).contextMenu('myMenu',firstMenu);
                	}, 
                	
                	loadComplete:function(xhr){
                		var record = jQuery("#west-grid").getRowData(1);
					    
					    jQuery("#west-grid").expandRow(record);
                	},
		            
		            //单击事件
			        onSelectRow: function(rowid) {
			            var treedata = $("#west-grid").jqGrid('getRowData',rowid);
			            
			            //alert(rowid);
			           resetNode(rowid);
			            if(treedata.leaf=="true") {
			                var st = "#tabs-1"+treedata.categoryId;
							if($(st).html() != null ) {
								maintab.tabs('select',st);
							}else if(treedata.fromReport==1){
								maintab.tabs('add',st, treedata.categoryName);
								//$(st,"#tabs").load(treedata.url);
								$.ajax({
									url: "docRightWorkList.jsp?categoryId="+treedata.categoryId,
									cache: false,
									type: "GET",
									dataType: "html",
									beforeSend: function (xhr) {
										//$(st,"#tabs").height(100).addClass("tabpreloading");
										//$(st,"#tabs").css("text-align","center").html("<img src='images/loading.gif' border=0 />");
									},
									complete : function (req, err) {
										$(st,"#tabs").empty().html(req.responseText);
										//alert(req.responseText);
									}
								});
							}else{
								maintab.tabs('add',st, treedata.categoryName);
								//$(st,"#tabs").load(treedata.url);
								$.ajax({
									url: "docRightList.jsp?categoryId="+treedata.categoryId,
									cache: false,
									type: "GET",
									dataType: "html",
									beforeSend: function (xhr) {
										//$(st,"#tabs").height(100).addClass("tabpreloading");
										//$(st,"#tabs").css("text-align","center").html("<img src='images/loading.gif' border=0 />");
									},
									complete : function (req, err) {
										$(st,"#tabs").empty().html(req.responseText);
										//alert(req.responseText);
									}
								});
							}
			            }
			        }
			    });
			    
			    //加载首页
				$.ajax({
					url: "listDocument.jsp?isRoot=true",
					type: "GET",					
					dataType: "html",
					beforeSend: function (xhr) {
					},
					complete : function (req, err) {
						//格式化tab
						loadTabCss();
						
						$("#tabs-1", "#tabs").html(req.responseText);
					}
				});
				
				$.jgrid.defaults = $.extend($.jgrid.defaults,{loadui:"enable"});
		
				//格式化Tab菜单
				var maintab = jQuery('#tabs','#RightPane').tabs({
			        add: function(e, ui) {
			            //添加关闭按钮
			            $(ui.tab).parents('li:first')
			                .append('<span class="ui-tabs-close ui-icon ui-icon-close" title="关闭"></span>')
			                .find('span.ui-tabs-close')
			                .click(function() {
			                    maintab.tabs('remove', $('li', maintab).index($(this).parents('li:first')[0]));
			                });
			            //选中刚添加的tab
			            maintab.tabs('select', '#' + ui.panel.id);
			            //可拖动tab页
						maintab.find(".ui-tabs-nav").sortable({axis:'x'});
						//格式化tab
						loadTabCss();
			        }
			    });
			    
			    //加载Tab内容
				$('#west-grid tr').click(function(){
					var st = "#t"+$(this).find('#tabId').text();
					if($(st).html() != null ) {
						//若该tab的内容已存在则不再重新加载,将tab状态改为选中即可
						maintab.tabs('select',st);
					} else {
						//若tab的内容不存在,则加载
						maintab.tabs('add',st,$(this).find('#tabMenu').text());
						$.ajax({
							url: $(this).find('#tabUrl').text(),
							cache: false,
							type: "GET",
							dataType: "html",
							beforeSend: function (xhr) {
							},
							complete : function (req, err) {
								$(st,"#tabs").empty().html(req.responseText);
							}
						});
					}
				});
				
				function formatLeaf(cellvalue, options, rowdata) {
			           return "true";
				    }
			    
			    //配置更新对话框
			    $("#nameModalDlg").dialog({   
			        autoOpen: false,       
			        modal: true,    // 设置对话框为模态（modal）对话框   
			        resizable: true,       
			        width: 280,   
			        cache: false,
			        buttons: {  // 为对话框添加按钮   
			           "取消": function() {$("#nameModalDlg").dialog("close")},
			           //"创建": rename,			            
			           "保存": rename			           
			        }   
		    	}); 
		    });//ready结束
		    
		   
		   		   
		    	
		    //打开更新窗口
	    	function openUpdate(id) {   
				var consoleDlg = $("#nameModalDlg");   
			    var dialogButtonPanel = consoleDlg.siblings(".ui-dialog-buttonpane");   
	
			    consoleDlg.dialog("option", "title", "修改分类名称");
			   
			   //加载需要修改的信息
			    loadName(id);
			    	
			    // 打开对话框   
	            consoleDlg.dialog("open");     
			}; 
		    	
			//更改名称
			var rename = function() {
				$.ajax( {   
			        url: '<c:url value="/document/document.do"/>?method=rename',	                    
	             	data: $('#nameForm').serialize(), // 从表单中获取数据	                    
	            	type:'POST', 
	               	async: false,
			    	beforeSend: function(){
				       	//验证mrt_fmrc是否为空
				       	if($("#categoryName").val()==null||$("#categoryName").val()==""){
							alert("请填写新分类名称！");
							return false;
					    }
				    },
					success : function(data, textStatus) {  
                		$("#nameModalDlg").dialog("close");   
		             	$("#west-grid").trigger("reloadGrid");
		             	alert("更改成功！");  
       				}   
    			});
			}
			
			//把需要修改的信息加载到对话框
	    	var loadName = function(id){
	    		var data =$('#west-grid').getRowData(id);
	    		var categoryName = data.categoryName;
	    		
	    		$("#oldname").empty().val(categoryName);
	    		$("#cateId").empty().val(id);
	    		
	    		//把新框清空
	    		$("#categoryName").val("");
	    	};
	    	
	    	//删除节点
	    	function deleteCategoryName(id){
	    		$.ajax( {   
			        url: '<c:url value="/document/document.do"/>?method=deleteCategoryName&cateId='+id,	                    
	             	//data: $('#nameForm').serialize(), // 从表单中获取数据	                    
	            	type:'POST', 
	               	async: false,
	               	beforeSend: function(){
	               		var data =$('#west-grid').getRowData(id);
	    				var categoryName = data.categoryName;
	               		return confirm("将删除分类 '"+categoryName+"' 及其子分类，是否确定删除？");
	               	},
					success : function(data, textStatus) {  
		             	$("#west-grid").trigger("reloadGrid");
		             	alert("删除成功！");  
       				}   
    			});
	    	};
	    	
	    	//新增分类
	    	function doAdd(){
				var refresh = window.showModalDialog("/document/documentCategory.do?method=edit",null,"dialogWidth:800px;dialogHeight:600px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
				if(refresh == "Y") {
					self.location.reload();
				}
			}
			
			//保存信息后重新加载tab
			function loadTab(categoryId){
				$.ajax({
					url: "docRightList.jsp?categoryId="+categoryId,
					cache: false,
					type: "GET",
					dataType: "html",
					beforeSend: function (xhr) {
					},
					complete : function (req, err) {
						$("#tabs-1"+categoryId).empty().html(req.responseText);
					}
				});		
			}
			
		</script>
	</head>
	
	<body>
	<!-- #LeftPane -->
		
		<div class="ui-layout-west ui-widget ui-widget-content" id="LeftPane">
			<!-- <a href="#" onclick="expandNode();">TEST</a> -->
			<table id="west-grid"></table>
		</div>
		
		<!-- #RightPane -->
		<div class="ui-layout-center ui-helper-reset ui-widget-content" id="RightPane">
			<!-- Tabs pane -->
			<div id="tabs" class="jqgtabs">
				<ul>
					<li><a href="#tabs-1">全部文档</a></li>
				</ul>
				<div id="tabs-1"></div>
			</div>
		</div>
		
		
		<div class="contextMenu" id="myMenu">
		  <ul>
		    <li id="item_1"><img src="<c:url value="/"/>images/notify_new.gif" />  新增分类</li>
		    <!-- <li id="item_2"><img src="<c:url value="/"/>images/icon_reply.gif" />  设置权限</li> -->
		    <li id="item_3"><img src="<c:url value="/"/>images/extend/chm.gif" />  重命名</li>
		    <li id="item_4"><img src="<c:url value="/"/>images/delete.gif" /> &nbsp;删除</li>
		  </ul>
		</div>
		
		<div id="nameModalDlg">  
            <div id="nameFormContainer">  
                <form name="nameForm" id="nameForm">  
                   <table>  
                       <tr>
						   <td width="10%" height="22" nowrap>原分类名称:&nbsp;</td>
						   <td>
						   	<input type="text" id="oldname" name="oldname" readonly="readonly" size="20"/>
						   	<input type="hidden" id="cateId" name="cateId" value=""/>
						   </td>
					   </tr>
					   <tr>
						   <td width="10%" height="22" nowrap>新分类名称:&nbsp;</td>
						   <td>
						   	<input type="text" id="categoryName" name="categoryName" size="20"/>
						   </td>
					   </tr>
                  </table>  
                </form>  
            </div>  
        </div>  
	</body>
</html>

