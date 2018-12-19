<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
	<title>审批流转</title>
	<script src="<c:url value="/"/>datePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
	<script src="<c:url value="/"/>components/jqgrid/js/jquery-ui-1.8.2.custom.min.js" type="text/javascript"></script><!--jquery ui-->
	<script src="<c:url value="/"/>js/inc_javascript.js"></script>
	<script src="<c:url value="/"/>js/addattachment.js"></script>
	<link rel="stylesheet" type="text/css" href="<c:url value="/css/form.css"/>"/>
	<link rel="stylesheet" type="text/css" href="<c:url value="/css/myTable.css"/>"/>

	<style type="text/css"> 	
		.buttonclass {
			font-weight:bold;
		}				
		/**input {color:expression(this.type=="button"?"red":"blue") } */		
		
	</style>


	<script language="javaScript">	
		$().ready(function(){
			
			//button字体变粗
			for(i=0;i<document.getElementsByTagName("INPUT").length;i++){
					if(document.getElementsByTagName("INPUT")[i].type=="button" || document.getElementsByTagName("INPUT")[i].type=="submit") 
					document.getElementsByTagName("INPUT")[i].className="buttonclass" ;
			}
			
		});
		
		
		function commonFun(path) {
			window.name = "__self";
			window.open(path, "__self");
		}
		
		function getDuration(categoryId,layerName,index){
			var beginCheckTime = $("#beginCheckTime"+index).val();
			var endCheckTime = $("#endCheckTime"+index).val();
			//alert(beginCheckTime);
			if(beginCheckTime == '' || beginCheckTime == null || endCheckTime == '' || endCheckTime == null){
				alert("请将开始、结束时间填写完整！");
			}else{
				//alert("/workflow/instanceInfor.do?method=getDuration&beginCheckTime="+beginCheckTime+"&endCheckTime="+ endCheckTime + "&categoryId="+categoryId+"&layerName="+encodeURI(layerName));
				$.ajax({
					url: "/workflow/instanceInfor.do?method=getDuration&beginCheckTime="+beginCheckTime+"&endCheckTime="+ endCheckTime + "&categoryId="+categoryId+"&layerName="+encodeURI(layerName),
					type: "post",
					dataType: "json",
					async: false,	//设置为同步
					beforeSend: function (xhr) {
					},
					complete : function (req, err) {
						//var returnValues = eval("("+req.responseText+")");
						var duration = req.responseText;
						//alert(duration);
						$("#averageDuration"+index).html(duration);
					}
				});
			}
		}
		
	</script>
</head>
<body onload="">

	<table width="60%" cellpadding="6" cellspacing="1" align="center" border="0" bordercolor="#c5dbec">
		<tr>
			<td colspan="3" style="color: yellow">
				<b>查看审批时间统计</b>
			</td>
		</tr>	
		
		<tr>
			<th class="ui-state-default jqgrid-rownum">类别</th>
			<td colspan="2">
				${_Instance.categoryName}					
			</td>			
		</tr>
		<tr>
			<th class="ui-state-default jqgrid-rownum">标题</th>
			<td colspan="2">
				${_Instance.instanceTitle}					
			</td>			
		</tr>
		<tr>
			<th width="20%" style="color: #ff4a00" class="ui-state-default jqgrid-rownum" align="center">审核层</th>
			<th width="45%" style="color: #ff4a00" align="center">审核起止时间</th>
			<th width="35%" style="color: #ff4a00" align="center">历时</th>
		</tr>
		
		<c:forEach items = "${_Instance.layers}" var = "layer">
			<tr>
				<td align="center" nowrap class="ui-state-default jqgrid-rownum">${layer.layerName}</td>
				<td align="center">
					${layer.beginTime}  ~   ${layer.endTime}			
				</td>		
				<td align="center">
					${layer.duration}
				</td>	
			</tr>
		</c:forEach>
	</table>
	<br/><br/>
	<table width="60%" cellpadding="6" cellspacing="1" align="center" border="0" bordercolor="#c5dbec">
		<tr>
			<td colspan="3" style="color: #ff4a00">
				<b>平均审批时间统计</b>
			</td>
		</tr>	
		
		<tr>
			<th class="ui-state-default jqgrid-rownum">类别</th>
			<td colspan="2">
				${_Instance.categoryName}					
			</td>			
		</tr>
		<tr>
			<th width="20%" style="color: #ff4a00" class="ui-state-default jqgrid-rownum" align="center">审核层</th>
			<th width="45%" style="color: #ff4a00" align="center">查询时间段</th>
			<th width="35%" style="color: #ff4a00" align="center">平均历时</th>
		</tr>
		
		<c:forEach items = "${_Instance.layers}" var = "layer" varStatus="index">
			<tr>
				<td align="center" nowrap class="ui-state-default jqgrid-rownum">${layer.layerName}</td>
				<td align="center">
					<input size="12" name="beginCheckTime${index.index}" id="beginCheckTime${index.index}" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'endCheckTime${index.index}\',{d:0,H:0,m:0});}',dateFmt:'yyyy-MM-dd'})" readonly="true"/>
					至
					<input size="12" name="endCheckTime${index.index}" id="endCheckTime${index.index}" onclick="WdatePicker({minDate:'#F{$dp.$D(\'beginCheckTime${index.index}\',{d:0,H:0,m:0});}',dateFmt:'yyyy-MM-dd'})" readonly="true"/>
					<input type="button" value="查询" onclick="getDuration(${_Instance.categoryId},'${layer.layerName}',${index.index});" />			
				</td>		
				<td align="center">
					<span id="averageDuration${index.index}"></span>
				</td>	
			</tr>
		</c:forEach>
	</table>
</body>
</html>
                  
