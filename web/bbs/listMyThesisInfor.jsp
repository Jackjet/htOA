<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<script type="text/javascript">
	//初始化列表和查询窗口Id
	var multiSearchParams = new Array();
	multiSearchParams[0] = "#listMyThesisInfor";			//列表Id
	multiSearchParams[1] = "#multiSearchDialogMyThesisInfor";//查询模态窗口Id
	var height = window.screen.height;
	//新增
	function addMyThesis(){
		/** var refresh = window.showModalDialog("/bbs/thesisInfor.do?method=edit",null,"dialogWidth:900px;dialogHeight:700px;center:Yes;dialogTop: 100px; dialogLeft: 200px;");
		if(refresh == "Y") {
			self.location.reload();
		} */
		window.open("/bbs/thesisInfor.do?method=edit", "_blank");
		/*var returnArray = window.showModalDialog("/bbs/thesisInfor.do?method=edit",null,"dialogWidth:980px;dialogHeight:"+height+"px;center:Yes;dialogTop: 5px; dialogLeft: 150px;");
		if(returnArray[0] == "refresh") {
			//self.location.reload();
			$("#listMyThesisInfor").trigger("reloadGrid"); 
		}*/
	}
	//查看
	function doView(rowId){
		/*var refresh = window.showModalDialog("/bbs/commentInfor.do?method=edit&thesisId="+rowId,'',"dialogWidth:900px;dialogHeight:"+height+"px;center:Yes;dialogTop: 5px; dialogLeft: 300px;");
		if(refresh == "Y") {
			self.location.reload();
		} */
		window.open("/bbs/commentInfor.do?method=edit&thesisId="+rowId, "_blank");
		//self.location.reload();
		$("#listMyThesisInfor").trigger("reloadGrid"); 
	}
	//编辑
	function doEdit(rowId){
		/*var returnArray = window.showModalDialog("/bbs/thesisInfor.do?method=edit&thesisId="+rowId,null,"dialogWidth:980px;dialogHeight:"+height+"px;center:Yes;dialogTop: 5px; dialogLeft: 150px;");
		if(returnArray[0] == "Y") {
			self.location.reload();
		}*/
		window.open("/bbs/thesisInfor.do?method=edit&thesisId="+rowId, "_blank");
	}
</script>

<title>我的论题列表</title>
<script src="<c:url value="/"/>js/multisearch.js"></script> <!--加载模态多条件查询相关js-->
<script src="<c:url value="/"/>datePicker/WdatePicker.js" language="JavaScript"></script>
<script src="<c:url value="/"/>components/jquery-1.4.2.js" type="text/javascript"></script> <!--jquery包-->
<script src="<c:url value="/"/>components/jqgrid/js/jquery-ui-1.8.2.custom.min.js" type="text/javascript"></script><!--jquery ui-->
<script src="<c:url value="/"/>components/jquery.layout.js" type="text/javascript"></script><!--jquery 布局-->
<script src="<c:url value="/"/>components/jqgrid/js/grid.locale-cn.js" type="text/javascript"></script> <!--jqgrid 中文包-->
<script src="<c:url value="/"/>components/jqgrid/js/jquery.jqGrid.min.js" type="text/javascript"></script> <!--jqgrid 包-->
<script src="<c:url value="/"/>js/inc_javascript.js"></script>
<script src="<c:url value="/"/>js/commonFunction.js"></script>
<script src="<c:url value="/"/>js/changeclass.js"></script> <!--用于改变页面样式-->

<%--<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />--%>
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>css/base/jquery-ui-1.9.2.custom.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/tabstyle.css" />

<body style="border:1px solid #0DE8F5;border-radius: 5px">
	<div>
		<table id="listMyThesisInfor"></table> <!-- 信息列表 -->
		<div id="pagerMyThesis"></div> <!-- 分页 -->
	</div>
	<!-- 查询框 -->
	<div id="multiSearchDialogMyThesisInfor" style="display: none;">  
	    <table>  
	        <tbody>  
	            <tr>  
		           <td>  
		              <input type="hidden" class="searchField" value="title"/>标题：
		              <input type="hidden" class="searchOper" value="cn"/>
		           </td>  
		           <td>  
		              <input type="text" class="searchString"/>  
		           </td>  
		        </tr>

				<tr>
		           <td>  
		               <input type="hidden" class="searchField" value="nickName"/>发布人：
		               <input type="hidden" class="searchOper" value="cn"/>
		           </td>  
		            <td>  
		               <input type="text" class="searchString"/>
		            </td>  
		       </tr>
		       
		       <tr>  
	                <td>  
	                    <input type="hidden" class="searchField" value="updateDate"/>起始时间：
	                    <input type="hidden" class="searchOper" value="ge"/>
	                </td>  
	                <td>  
	                    <input class="searchString" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly="true" size="12"/>
	                </td>  
	            </tr>
	            
	            <tr>  
	                <td>  
	                    <input type="hidden" class="searchField" value="updateDate"/>结束时间：
	                    <input type="hidden" class="searchOper" value="le"/>
	                </td>  
	                <td>  
	                    <input class="searchString" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly="true" size="12"/>
	                </td>  
	            </tr>
	        </tbody>  
	    </table>  
	</div>
</body>
	<script type="text/javascript"> 		
	  
		//显示标题
		function formatTitle(cellValue, options, rowObject) {				
			var html = "";
			
			//如果是精品
			if(rowObject.essence){
				html += "<img src='<c:url value='/images'/>/bbs/essence.gif' align='absmiddle' alt='精品帖子'/>&nbsp;&nbsp;";
			}
			
			//如果是置顶，标题设为红色
			if(rowObject.topThesis){
				html += "<a href='javascript:;' style='color:red;font-weight:bold;' onclick='doView("+options.rowId+")'>" + cellValue + "</a>";
			}else{
				html += "<a href='javascript:;' onclick='doView("+options.rowId+")'>" + cellValue + "</a>";
			} 
			
			//如果有附件
			if(rowObject.attachment != null){
				html += "&nbsp;<img src='<c:url value='/images'/>/bbs/icon_attach.gif' width='16' height='16' align='absmiddle' alt='附件'/>&nbsp;";
			}
			
			//如果有图片
			if(rowObject.imgAttachment != null){
				html += "&nbsp;<img src='<c:url value='/images'/>/bbs/image_s.gif' align='absmiddle' alt='图片'/>&nbsp;";
			}
			
			
			
			return html;
		}
		
		//置顶图片
		function formatTop(cellValue, options, rowObject) {				
			var html = "";
           if (cellValue) {
              html += "<img src='/images/bbs/top.gif' width='11' height='11' alt='置顶'/>";
           }else{
           	  html +="&nbsp;";
           }
           return html;
		}
		
		//跟帖/查看 
		function formatView(cellValue, options, rowObject){
			var returnStr;
			
			returnStr = rowObject.comments+"/"+cellValue;
			
			return returnStr;
		}
		
		//编辑操作
		function formatEdit(cellValue,options,rowObject){
			var returnStr;
			
			returnStr = "[<a href='javascript:;' onclick='doEdit("+rowObject.thesisId+")'>编辑</a>]"
			return returnStr;
		}
		
		
		//加载表格数据
		var $mygrid = jQuery("#listMyThesisInfor").jqGrid({
            url:'/bbs/thesisInfor.do?method=myList',
            //rownumbers: true,	//是否显示序号
            datatype: "json",   //从后台获取的数据类型              
           	autowidth: true,	//是否自适应宽度
            height:document.documentElement.clientHeight-140,
            colNames:['Id', '置顶', '精品', '图片', '附件','标题', '昵称', '跟帖', '跟帖/查看', '更新时间', '操作'],//表的第一行标题栏
            //以下为每列显示的具体数据
            colModel:[
                {name:'thesisId',index:'thesisId', width:0, search:false, hidden:true, key:true},            
                {name:'topThesis',index:'topThesis', width:10, align:'center', formatter:formatTop},
                {name:'essence',index:'essence', width:0, align:'center', hidden:true},
                {name:'imgAttachment',index:'imgAttachment', width:0, align:'center', hidden:true},
                {name:'attachment',index:'attachment', width:0, align:'center', hidden:true},
                {name:'title',index:'title', width:160, sortable:true, formatter:formatTitle,sorttype:"string"},
                {name:'nickName',index:'nickName', width:40,align:'center'},
                {name:'comments',index:'comments', width:0,align:'center',hidden:true},
                {name:'viewsCount',index:'viewsCount', width:30,align:'center',formatter:formatView},
                {name:'updateDate',index:'updateDate', width:40,align:'center'},
                {name:'edit', width:20,align:'center',formatter:formatEdit}
            ],
            //caption: "我的论题列表",
            sortname: 'topThesis', //默认排序的字段
            sortorder: 'desc',	//默认排序形式:升序,降序
            multiselect: true,	//是否支持多选,可用于批量删除
            viewrecords: true,	//是否显示数据的总条数(显示在右下角)
            rowNum: 10,			//每页显示的默认数据条数
            rowList: [10,20,30],//可选的每页显示的数据条数(显示在中间,下拉框形式)
            scroll: false, 		//是否采用滚动分页的形式
            scrollrows: false,	//当选中的行数据隐藏时,grid是否自动滚               
            jsonReader:{
               repeatitems: false	//告诉JqGrid,返回的数据的标签是否是可重复的
            },         
            pager: "#pagerMyThesis"	//分页工具栏
            //caption: "用户信息"	//表头
       	}).navGrid('#pagerMyThesis',{edit:false,add:false,del:false,search:false}).closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
	
		
		
		//$.ajax( {   
		       // url: "/bbs/thesisInfor.do?method=judgeRight",	                    
            	//type:'POST', 
               //	async: false,
               	
		       	//success : function(data) {  
					//alert(data);
					//if(data == 1){
					if("${_SYSTEM_USER.userType == 1}" == 'true' || "${_BBS_OPERATOR}" == 'true'){	
						
						//自定义按钮
						jQuery("#listMyThesisInfor").jqGrid('navButtonAdd','#pagerMyThesis', {
							caption:"[发表", title:"点击发表论题", buttonicon:'none', onClickButton: addMyThesis
						});
						jQuery("#listMyThesisInfor").jqGrid('navButtonAdd','#pagerMyThesis', {
							caption:"|",buttonicon: 'none'
						});
						jQuery("#listMyThesisInfor").jqGrid('navButtonAdd','#pagerMyThesis', {
							caption:"<font color=red>删除</font>]", title:"点击批量删除", buttonicon:'none', onClickButton: deleteThesis
						});
						
						
						jQuery("#listMyThesisInfor").jqGrid('navButtonAdd','#pagerMyThesis', {
							caption:"&nbsp;&nbsp;[",buttonicon: 'none'
						});
						
						jQuery("#listMyThesisInfor").jqGrid('navButtonAdd','#pagerMyThesis', {
							caption:"置顶", title:"点击置顶",buttonicon: 'none', onClickButton: setTopThesis
						});
						
						jQuery("#listMyThesisInfor").jqGrid('navButtonAdd','#pagerMyThesis', {
							caption:"|",buttonicon: 'none'
						});
						
						jQuery("#listMyThesisInfor").jqGrid('navButtonAdd','#pagerMyThesis', {
							caption:"取消", title:"点击取消置顶", buttonicon: 'none',onClickButton: cancelTop
						});
						
						jQuery("#listMyThesisInfor").jqGrid('navButtonAdd','#pagerMyThesis', {
							caption:"]",buttonicon: 'none'
						});
						
						jQuery("#listMyThesisInfor").jqGrid('navButtonAdd','#pagerMyThesis', {
							caption:"&nbsp;&nbsp;[",buttonicon: 'none'
						});
						
						jQuery("#listMyThesisInfor").jqGrid('navButtonAdd','#pagerMyThesis', {
							caption:"加精", title:"点击加精",buttonicon: 'none', onClickButton: setEssence
						});
						
						jQuery("#listMyThesisInfor").jqGrid('navButtonAdd','#pagerMyThesis', {
							caption:"|",buttonicon: 'none'
						});
						
						jQuery("#listMyThesisInfor").jqGrid('navButtonAdd','#pagerMyThesis', {
							caption:"取消", title:"点击取消加精", buttonicon: 'none',onClickButton: cancelEssence
						});
						
						jQuery("#listMyThesisInfor").jqGrid('navButtonAdd','#pagerMyThesis', {
							caption:"]&nbsp;&nbsp;&nbsp;&nbsp;",buttonicon: 'none'
						});
					}
		        //}   			         
   			//});
		
		
		////////////////////////////////////////////////
		jQuery("#listMyThesisInfor").jqGrid('navButtonAdd','#pagerMyThesis', {
			caption:"查询", title:"点击查询", buttonicon:'ui-icon-search', onClickButton: openMultipleSearchDialog
		});
		
		//删除数据
		function deleteThesis(){
			doDelete("/bbs/thesisInfor.do?method=delete","listMyThesisInfor");
		}
		
		
		//置顶
		function setTopThesis(){
			setBBS("/bbs/thesisInfor.do?method=setTop&topThesis=true","listMyThesisInfor","置顶");
		}
		//取消置顶
		function cancelTop(){
			setBBS("/bbs/thesisInfor.do?method=setTop&topThesis=false","listMyThesisInfor","取消置顶");
		}
		//加精
		function setEssence(){
			setBBS("/bbs/thesisInfor.do?method=setEssence&essence=true","listMyThesisInfor","设为精华");
		}
		//取消加精
		function cancelEssence(){
			setBBS("/bbs/thesisInfor.do?method=setEssence&essence=false","listMyThesisInfor","取消精华");
		}
		
		
		function setBBS(url,listId,description){
			var rowIds = jQuery("#"+listId).jqGrid('getGridParam','selarrrow');
			
			if(rowIds != null && rowIds.length > 0){
				var yes = window.confirm("确定要"+description+"吗？");
				if (yes) {
					$.ajax({
						url: url+"&rowIds="+rowIds,	//url
						cache: false,
						type: "POST",
						dataType: "html",
						beforeSend: function (xhr) {						
						},
							
						complete : function (req, err) {
							alert(description+"成功！");
							$("#"+listId).trigger("reloadGrid"); 
						}
					});	
				}
			}else {
				alert("请选择要"+description+"的帖子！");
			}	
		}
		
		
		
	</script>