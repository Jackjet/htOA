<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
		<title>工作跟踪信息</title>
		<script src="<c:url value="/"/>datePicker/WdatePicker.js" type="text/javascript"></script>
		
		<!-- <script src="<c:url value="/"/>components/jquery-1.4.2.js" type="text/javascript"></script> 
		<script src="<c:url value="/"/>components/jqgrid/js/jquery-ui-1.8.2.custom.min.js" type="text/javascript"></script> -->
		
		<script src="<c:url value="/"/>js/jquery.jqGrid-4.4.5/js/jquery-1.9.0.min.js" type="text/javascript"></script>
		<script src="<c:url value="/"/>js/jquery-ui-1.9.2.custom/js/jquery-ui-1.9.2.custom.min.js" type="text/javascript"></script> <!--jquery ui-->
		
		
		<!--<script src="<c:url value="/"/>components/jquery.layout.js" type="text/javascript"></script>jquery 布局-->
		<script src="<c:url value="/"/>js/jquery.layout-latest.js" type="text/javascript"></script> <!--jquery 布局-->
		
		
		<!-- <script src="<c:url value="/"/>components/jqgrid/js/grid.locale-cn.js" type="text/javascript"></script> 
		<script src="<c:url value="/"/>components/jqgrid/js/jquery.jqGrid.min.js" type="text/javascript"></script> -->
		
		<script src="<c:url value="/"/>js/jquery.jqGrid-4.4.5/js/jquery.jqGrid.src.js" type="text/javascript"></script> <!--jqgrid 包-->
		<script src="<c:url value="/"/>js/jquery.jqGrid-4.4.5/js/i18n/grid.locale-cn.js" type="text/javascript"></script> <!--jqgrid 中文包-->
		
		
		<script src="<c:url value="/"/>js/commonFunction.js"></script>
		<script src="<c:url value="/"/>js/inc_javascript.js"></script>
		<script src="<c:url value="/"/>js/multisearch.js"></script> <!--加载模态多条件查询相关js-->
		<script src="<c:url value="/"/>js/changeclass.js"></script> <!--用于改变页面样式-->


		<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>css/base/jquery-ui-1.9.2.custom.css" />
		<%--<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>js/jquery.jqGrid-4.4.5/css/ui.jqgrid.css" />--%>
		<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>js/jquery.jqGrid-4.4.5/css/tabstyle.css" />
		<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
		<%--<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/tabstyle.css" />--%>
		<link rel="stylesheet" type="text/css" media="screen" href="/css/treeNormal.css" />
		<script type="text/javascript">
			var maintab;
			$().ready(function(){
				$('body').layout({
					resizerClass: 'ui-state-default',
			        west__onresize: function (pane, $Pane) {
			            jQuery("#west-grid").jqGrid('setGridWidth',$Pane.innerWidth()-2);
					}
				});
				
				//加载首页
				$.ajax({
					url: "taskRightList.jsp?isRoot=true&categoryId="+${_Category.categoryId}+"&departmentId=0",
					type: "GET",
					dataType: "html",
					beforeSend: function (xhr) {
					},
					complete : function (req, err) {
						//格式化tab
						loadTabCss();
						//alert(req.responseText);
						$("#tabs-1", "#tabs").html(req.responseText);
					}
				});
				
				$.jgrid.defaults = $.extend($.jgrid.defaults,{loadui:"enable"});
		
				//格式化Tab菜单
				maintab = jQuery('#tabs','#RightPane').tabs({
			        add: function(e, ui) {
			            //添加关闭按钮
			            $(ui.tab).parents('li:first')
			                .append('<span class="ui-tabs-close ui-icon ui-icon-close" title="关闭"></span>')
			                .find('span.ui-tabs-close')
			                .click(function() {
			                    maintab.tabs('remove', $('li', maintab).index($(this).parents('li:first')[0]));
			                });
			                
			              $(ui.tab).dblclick(function(){   //双击关闭事件绑定
			    		 	var li = $(ui.tab).parent();
			    		 	var index = $('#tabs li').index(li.get(0));
			    		 	$("#tabs").tabs("remove",index);
			    		 });
			             
			            //选中刚添加的tab
			            maintab.tabs('select', '#' + ui.panel.id);
			            //可拖动tab页
						maintab.find(".ui-tabs-nav").sortable({axis:'x'});
						//格式化tab
//						loadTabCss();
                        $("div[id^='tabs-']").css({'margin-top':'0px','margin-left':'0px','padding-left':'0px','padding-top':'3px','padding-right':'0px',
                            'border':'1px solid #0DE8F5','border-radius':'0 5px 5px 5px'
                        });
			        }
			    });
			    
				
			    
			    
			    
			    
			    /*//加载Tab内容
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
				});*/
			    
			});
			
			function makeTree(id,departmentId,departmentName){
				//加载菜单树
		    	jQuery("#"+id).jqGrid({
			        url: "/supervise/taskCategory.do?method=getCategoryTree&categoryId="+${_Category.categoryId}+"&departmentId="+departmentId,
			        datatype: "json",
			        treedatatype: "json",
			        height: "auto",
			        pager: false,	        
			        colNames: ["Id","","url"],
			        colModel: [
			            {name: "categoryId", width:1, hidden:true, key:true},
			            {name: "categoryName", resizable: false, sortable:false},//,formatter:formatName
			            {name: "urlPath", width:1, hidden:true}//,
			            //{name: "leaf",index:"leaf", width:1, hidden:true}
			        ],
			        treeReader : {
					    level_field: "layer",
					    left_field: 'leftIndex',
						right_field: 'rightIndex',
					    leaf_field: "leaf",
					    expanded_field: true
					},
			        treeGrid: true,		//树形grid	     
					//caption: "工作跟踪",
			        ExpandColumn: "categoryName",	 	       
			        autowidth: true,  
			        //ExpandColClick: true,
			        treeIcons: {leaf:'ui-icon-document-b'},
			        jsonReader:{
		               repeatitems: false
		            },
		            //加载完成后事件
	                afterInsertRow: function(rowid,rowdata,rowelem) {
	                	$('#'+rowid).css('cursor',"pointer");
                	}, 
		            loadComplete:function(xhr){
			            //菜单树默认展开
			            var record = jQuery("#"+id).getRowData(2);
						jQuery("#"+id).expandRow(record);
						
						var record1 = jQuery("#"+id).getRowData(3);
						jQuery("#"+id).expandRow(record1);
			        },
			        onSelectRow: function(rowid) {
			           	//加载Tab内容
			            var treedata = $("#"+id).jqGrid('getRowData',rowid);
			            
			            //处理树状菜单点击时的展开和收拢
			            //resetNode(id+" #"+rowid+" ");
			            //alert(treedata.categoryId);
			            //alert(departmentName);
			            if(treedata.categoryName!="方针目标类" && treedata.categoryName!="部门建设类" && treedata.categoryName!="党群工作" && treedata.categoryName!="内控类") {
			            //alert(treedata.parent);
			            //if(treedata.parent.categoryId != 1){
			                //treedata.url
			                var st = "#tabs-"+treedata.categoryId+"-"+departmentId;
							if($(st).html() != null ) {
								maintab.tabs('select',st);
							}else {
								//alert(treedata.categoryName);
								maintab.tabs('add',st, departmentName+"-"+treedata.categoryName);
								//$(st,"#tabs").load(treedata.url);
								//alert(2);
								$.ajax({
									url: "taskRightList.jsp?categoryId="+treedata.categoryId+"&leaf="+treedata.leaf+"&departmentId="+departmentId,
									cache: false,
									type: "GET",
									dataType: "html",
									beforeSend: function (xhr) {
										//$(st,"#tabs").height(100).addClass("tabpreloading");
										//$(st,"#tabs").css("text-align","center").html("<img src='images/loading.gif' border=0 />");
									},
									complete : function (req, err) {
										//$(st,"#tabs").removeClass("tabpreloading").append(req.responseText);
										$(st,"#tabs").empty().html(req.responseText);
									}
								});
							}
			            }
			        }
			    });
			}
			
			function formatName(cellvalue, options, rowdata){
				var returnHtml = "";
				if(cellvalue == '行政类' || cellvalue == '党群类'){
					returnHtml = "<font color='#fafbfc'>"+cellvalue+"</font>";
				}else{
					returnHtml = cellvalue;
				}
				return returnHtml;
			}
			
			function disTree(id,name,departmentId){
				$("#"+id).toggle();
				createTab(departmentId,name);
			}
			
			function createTab(departmentId,departmentName){
				var st = "#tabs-0-"+departmentId;
				if($(st).html() != null ) {
					maintab.tabs('select',st);
				}else {
					maintab.tabs('add',st, departmentName);
					$.ajax({
						url: "taskRightList.jsp?categoryId="+${_Category.categoryId}+"&leaf=false&departmentId="+departmentId,
						cache: false,
						type: "GET",
						dataType: "html",
						beforeSend: function (xhr) {
							//alert("taskRightList.jsp?categoryId=0&leaf=false&departmentId="+departmentId);
						},
						complete : function (req, err) {
							//alert(req.responseText);
							$(st,"#tabs").empty().html(req.responseText);
						}
					});
				}
			}
		</script>
	</head>
	<style type="text/css">
		.ui-tabs .ui-tabs-nav li.ui-tabs-active {
			margin-left:-1px;
			margin-bottom: -3px;
			padding-top: 1px;
		}
		.ui-state-active, .ui-widget-content .ui-state-active, .ui-widget-header .ui-state-active {
			/*position: absolute;*/

			border: solid 1px #0de8f5;
			border-radius:5px 5px 0 0;
			/*padding-bottom: 1px!important;*/
			border-bottom: 3px solid rgb(14, 22, 36) !important;
			/*margin-bottom: -3px!important;*/
			/*border-left: 1px solid rgb(13, 232, 245);*/
			/*border-right: 1px solid rgb(13, 232, 245);*/
			/*border-bottom: 1px solid rgba(13, 242, 255, 0);*/
			background: rgba(255, 255, 255, 0);
			font-size:16px;
			font-weight:bold;
			color: #ff8d73;
		}

		.ui-layout-west{
			overflow-x: hidden;
		}
		.ui-tabs .ui-tabs-nav{
			padding: 0px;
		}
		.asdf .ui-jqgrid-view .ui-jqgrid-hdiv{

			display: none;

		}
		.ui-layout-pane-west{
			margin:4px 0px 50px 9px!important;
			height: auto;
		}
		.ui-jqgrid-bdiv{
			overflow-x: hidden!important;
		}
		.ui-layout-pane-west{
			margin:3px 2px 50.5px 9px!important;
			height: auto;
		}
	</style>

	<body>
	<script language="JavaScript" src="/js/jquery.nicescroll.min.js"></script>
	<script language="JavaScript" src="/js/nicescroll.js"></script>
	<script>
        $(document).ready(function() {
//            $('#LeftPane').slimScroll({
//                //                width:document.getElementById("td").clientWidth - 60,
//                height: "100%",
//                color: '#8b8b8b',
//                alwaysVisible: true
//            });


            $("#LeftPane").niceScroll({
                cursorcolor: "#747474",//滚动条的颜色
                background: "rgba(255,255,255,0.05)", // 轨道的背景颜色
                cursoropacitymax: 1, //滚动条的透明度，从0-1
//                touchbehavior: true, //使光标拖动滚动像在台式电脑触摸设备
                gesturezoom: true,
                cursorwidth: "8px", //滚动条的宽度
                cursorborder: "0", // 游标边框css定义
                horizrailenabled: false,
                cursorborderradius: "5px",//以像素为光标边界半径  圆角
                autohidemode: false, //是否隐藏滚动条  true的时候默认不显示滚动条，当鼠标经过的时候显示滚动条
                zindex:"auto",//给滚动条设置z-index值
                oneaxismousemode: "false",// 当只有水平滚动时可以用鼠标滚轮来滚动，如果设为false则不支持水平滚动，如果设为auto支持双轴滚动
                railpadding: {top:0, right:0, left:0, bottom:0 },//滚动条的位置
                iframeautoresize: true, // 在加载事件时自动重置iframe大小
                sensitiverail: true, // 单击轨道产生滚动
                preventmultitouchscrolling: true, // 防止多触点事件引发滚动*/

            });
        });
	</script>
		<!-- #LeftPane
		<div class="ui-layout-west ui-widget ui-widget-content" id="LeftPane" >
			<table id="west-grid"></table>
			
		</div> -->

		<div class="ui-layout-west ui-widget ui-widget-content" id="LeftPane" style="background-color:#062434;border: 1px solid #0de8f5;border-radius:5px;padding: 0;margin: 0;">
			<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all">
				<div class="ui-jqgrid-view">
					<div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
						<span class="ui-jqgrid-title">工作跟踪</span>
					</div>
					
					<div class="ui-jqgrid-bdiv asdf" style="border:0px solid red;overflow-x:hidden;cursor: hand">
						<c:forEach items="${_Departs}" var="department">
							
							<div style="overflow-x: hidden;cursor: hand"  class="ui-state-default ui-jqgrid-hdiv" onclick="disTree('west-grid-${department.organizeId}','${department.organizeName}',${department.organizeId})" title="点击展开/收缩" style="cursor:pointer;border:0px solid red;">
								<table cellspacing="0" cellpadding="0" border="0" <%--style="width: 230px;"--%> class="ui-jqgrid-htable">
									<thead>
										<tr>
											<th align="left" style="cursor: hand;padding-left: 20px">${department.organizeName}</th>
										</tr>
									</thead>
								</table>
							</div>
							
							<table id="west-grid-${department.organizeId}" style="overflow-x:hidden;display:none;"></table>

							<script>
								makeTree('west-grid-'+${department.organizeId},${department.organizeId},'${department.organizeName}');							
							</script>
						</c:forEach>
					</div>
				</div>
			</div>
		</div>
			
		<!-- #RightPane -->
		<div class="ui-layout-center ui-helper-reset ui-widget-content" id="RightPane" style="overflow-x:hidden;padding: 0;margin: 0;">
			<!-- Tabs pane -->
			<div id="tabs" class="jqgtabs">
				<ul>
					<li><a href="#tabs-1">所有信息</a></li>
				</ul>
				<div id="tabs-1" style="border:1px solid #0DE8F5;border-radius:0 5px 5px 5px;"></div>
			</div>
		</div>
	</body>

</html>

