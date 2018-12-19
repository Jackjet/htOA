<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<title>批量归档到档案系统</title>
<script src="<c:url value="/"/>datePicker/WdatePicker.js" charset="UTF-8" language="JavaScript"></script>
<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
<script src="<c:url value="/"/>js/inc_javascript.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/css/form.css"/>" />
<link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css"></link>
<script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript" charset="UTF-8"></script>
<script src="<c:url value="/"/>components/formvalidator/js/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
<script src="<c:url value='/js'/>/addattachment.js"></script>
<script src="<c:url value='/fckeditor'/>/fckeditor.js"></script>
<link href="<c:url value="/"/>components/loadmask/jquery.loadmask.css" rel="stylesheet" type="text/css" />
<script type='text/javascript' src='<c:url value="/"/>components/loadmask/jquery.loadmask.min.js'></script>

<script>
	function batchArchive(){
		//flowIds 
		$("#formContent").mask("处理中，请稍等...");
		var flowIds = "";
		$("input[name='flowId']:checked").each(function(){ 
			
       		var tmpFlowId = $(this).val();
       		flowIds += tmpFlowId + ",";
       	}); 
		//alert(flowIds);

		var data = "beginDate="+$("#beginDate").val()+"&endDate=" + $("#endDate").val() + "&flowIds="+flowIds;
		//alert(data);
		$.ajax({
			url: "/workflow/instanceInfor.do?method=batchToArchive",
			data: data,
			type: "post",
			dataType: "json",
			async: false,	//设置为同步
			beforeSend: function (xhr) {
				
			},
			complete : function (req, err) {
				
			},
			success : function (msg) {
				if($("#formContent").isMasked()){
				　　$("#formContent").unmask();
				}
				$("#formContent").unmask();
				//var returnValues = eval("("+req.responseText+")");
				if (msg == '1'){
			        alert("成功");
			    }else {
			    	alert("失败");
			    }
			}
		});
	}
	
</script>

<base target="_self"/>
<body onload="">
	<br/>
	<div id="formContent">
	<form method="post" action="<c:url value="/workflow/instanceInfor"/>.do?method=batchToArchive">
		开始时间：<input type="text" name="beginDate" id="beginDate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" value="2015-01-01"/><br/><br/>
		结束时间：<input type="text" name="endDate" id="endDate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" value="2015-12-31"/><br/><br/>
		流程:<input type="checkbox" name="flowId" value="84" />收文管理&nbsp;&nbsp;
		<input type="checkbox" name="flowId" value="85" />发文管理&nbsp;&nbsp;
		<input type="checkbox" name="flowId" value="86" />合同&nbsp;&nbsp;
		<input type="checkbox" name="flowId" value="87" />内部报告&nbsp;&nbsp;
		<input type="checkbox" name="flowId" value="88" />制度评审
		
		<br/><br/>
		<input type="button" value="批量生成" onclick="batchArchive();" />
	</form>
	</div>
</body>

