<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
	<head>
	
	<title>编辑流程</title>
	<script src="<c:url value="/"/>js/changeclass.js"></script> <!--用于改变页面样式-->
	<script src="<c:url value="/"/>js/inc_javascript.js" type="text/javascript"></script>
	<script src="<c:url value="/"/>js/addattachment.js"></script>
	<script src="<c:url value="/"/>js/multisearch.js"></script> <!--加载模态多条件查询相关js-->
	<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
	<script src="<c:url value="/"/>datePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="<c:url value="/"/>components/jqgrid/js/jquery-ui-1.8.2.custom.min.js" type="text/javascript"></script><!--jquery ui-->
	<script src="<c:url value="/"/>components/jquery.layout.js" type="text/javascript"></script><!--jquery 布局-->
	<script src="<c:url value="/"/>components/jqgrid/js/grid.locale-cn.js" type="text/javascript"></script> <!--jqgrid 中文包-->
	<script src="<c:url value="/"/>components/jqgrid/js/jquery.jqGrid.min.js" type="text/javascript"></script> <!--jqgrid 包-->
	<script src="<c:url value="/"/>js/commonFunction.js"></script>
	<link type="text/css" href="<c:url value="/"/>components/jqgrid/css/jquery.ui.all.css" rel="stylesheet" />
	<%--<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />--%>
		<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>css/base/jquery-ui-1.9.2.custom.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
	
	<!-- formValidator -->
	<link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css"></link>
	<script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript" charset="UTF-8"></script>
	<script src="<c:url value="/"/>components/formvalidator/js/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
	
	<script type="text/javascript">
		
		 $(document).ready(function(){

			//流程信息验证
			$.formValidator.initConfig({formid:"definitionForm",onerror:function(msg){alert(msg)},onsuccess:function(){submitData();}});
			$("#flowName").formValidator({oncorrect:"输入正确"}).inputValidator({min:1,onerror: "请输入流程名称"});
			$("#chargerId").formValidator({oncorrect:"选择正确"}).inputValidator({min:1,onerror: "请选择主办人"});
			
			selectFileType();
		}); 
		//提交流程数据
		function submitData() {
			/** $.ajax({
					url: '<c:url value="/workFlowDefinition.do"/>?method=save',
					data: $('#definitionForm').serialize(), // 从表单中获取数据	                    
	            	type:'POST', 
	               	async: false,
	               	beforeSend: function (xhr) {
						alert("正在提交...");
					},
					complete : function (req, err) {
						//格式化tab
						//var returnValues = eval("("+req.responseText+")");
						//alert(req.include_page);
					},
					success : function (data, textStatus) {
						alert("提交完毕！");
						location.href = "workflow/inforFlowDefinition.jsp";
					}
				}); */
			alert('信息编辑成功！');
			/**
			var form = document.definitionForm;
			form.action = '<c:url value="/workFlowDefinition.do"/>?method=save';
			form.submit();
			*/
			//window.opener.location.reload();
			//window.close();
		}
		
		//新增节点
		function addInfor(){
			if(!$("#flowId").val()){
				alert("无对应的所属流程，请先添加流程信息!");
				return;
			}else{
//				var returnArray = window.showModalDialog("/workFlowDefinition.do?method=editNode&nodeId=&flowId="+$("#flowId").val(),'',"dialogWidth:800px;dialogHeight:600px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
//
//				if(returnArray == "refresh") {
//					self.location.reload();
//				}

				window.open("/workFlowDefinition.do?method=editNode&nodeId=&flowId="+$("#flowId").val(),'_blank')
			}
		}
		
		//修改节点
		function editInfor(rowId){
			//window.location.href = "/workFlowDefinition.do?method=edit&ac=edit&flowId="+rowId;
//			var returnArray = window.showModalDialog("/workFlowDefinition.do?method=editNode&nodeId="+rowId+"&flowId="+$("#flowId").val(),'',"dialogWidth:800px;dialogHeight:600px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
//			if(returnArray == "refresh") {
//				self.location.reload();
//			}
            window.open("/workFlowDefinition.do?method=editNode&nodeId="+rowId+"&flowId="+$("#flowId").val(),'_blank')
		}
		
		//查看
		function viewInfor(rowId){
			window.showModalDialog("/workFlowDefinition.do?method=viewNode&nodeId="+rowId+"&flowId="+$("#flowId").val(),'',"dialogWidth:800px;dialogHeight:600px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		}
	
		//批量删除节点
		function deleteInfor(){
			doDelete("/workFlowDefinition.do?method=deleteNode","listNode");
		}
		
		//选择归档人类型
		function selectFileType(){
			var selectedType = $(":radio[name='filerType']:checked").val();
			
			if(selectedType == "0"){
				$("#fileRoleDiv").css("display","none");
			}	
			if(selectedType == "1"){
				$("#fileRoleDiv").css("display","");
			}		
		}
	</script>

	</head>
	<body style="border:0px solid #0DE8F5;border-radius: 5px;overflow-y: scroll">
			<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 100%;">
				<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%;">
					
	  				<div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
	    				<span class="ui-jqgrid-title">工作流程</span>&nbsp;&nbsp;
	    				<input type="button" value="返回流程列表" onclick="javascript:window.location.href='/workflow/listFlowDefinition.jsp'" />
	  				</div>
	  				
	  				 <!-- 如果是编辑，则引入编辑页面 -->
	  				<c:if test="${include_page == 'edit'}">
                      	<%@ include file="editFlowDefinition.jsp"%>                       
                    </c:if>
                    
                     <!-- 如果是浏览，则引入浏览页面 -->
	  				<c:if test="${include_page == 'view'}">
                      	<%@ include file="viewFlowDefinition.jsp"%>                       
                    </c:if>
					
					<br/>
					<div><!-- 流程图 -->
						<table width="98%" cellpadding="6" cellspacing="1" align="center" border="0" >
							<tr>
								<td colspan="10"  align="left">流程图</td>
							</tr>
							<tr>
								<td  colspan="10">&nbsp;</td>
							</tr>
						</table>
					</div>
					<br/>
					<div><!-- 流程节点列表 -->
						<table width="98%" cellpadding="6" cellspacing="1" align="center" border="0" >
							<tr>
								<td colspan="10"  align="left">流程节点</td>
							</tr>
							<tr>
								<td  colspan="10">
									<table id="listNode" style="width:99%"></table>
									<div id="pagerNode"></div>
								</td>
							</tr>
						</table>
					</div>
					<br/>
				</div>
			</div>
			<script type="text/javascript">
			
				//标题查看链接
				function formatTitle(cellValue, options, rowObject) {				
				var html = '';
				html = "<a href='javascript:;' onclick='viewInfor("+options.rowId+")'>" + cellValue + "</a>";				
				return html;
			}
			    
			    //自定义显示节点类型内容
			    function formatNodeType(cellvalue) {
		           var returnStr;
		           if (cellvalue==1) {
		              returnStr = "普通节点";
		           }else if(cellvalue==2) {
		              returnStr = "分叉节点";
		           }else if(cellvalue==3){
		           	  returnStr = "聚合节点";
		           }else if(cellvalue==4){
		           	  returnStr = "状态节点";
		           }
		           return returnStr;
			    }
			    
			    //自定义显示是否是分叉类节点
			    function formatForked(cellvalue) {
		           var returnStr;
		           if (cellvalue==1) {
		              returnStr = "是";
		           }else if(cellvalue==0) {
		              returnStr = "否";
		           }
		           return returnStr;
			    }
			    
			    //自定义显示是否是分叉类节点
			    function formatOperate(cellvalue) {
		           var returnStr;
		           if (cellvalue==1) {
		              returnStr = "可以";
		           }else if(cellvalue==0) {
		              returnStr = "不可以";
		           }
		           return returnStr;
			    }
			    //自定义操作栏的显示内容
			    function formatOperation(cellvalue, options, rowdata) {
		           var returnStr = "<a href='javascript:;' onclick='editInfor("+options.rowId+")'>[修改]</a>";
		           return returnStr;
			    }
				
	     </script>
	     <!-- 如果是add，则引入listNodeEmpty.jsp页面 -->
	     <!-- 如果是view或edit，则引入listNode页面 -->
	     <c:choose>
	     	<c:when test="${empty _FlowDefinition.flowId}">
	     		<%@ include file="listNodeEmpty.jsp"%>
	     	</c:when>
	     	
	     	<c:otherwise>
	     		<%@ include file="listNode.jsp"%>
	     	</c:otherwise>
	     </c:choose>
		 
	</body>
</html>
