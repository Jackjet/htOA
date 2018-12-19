<html>
<head>
<title>[#if _InforDocument.inforTitle??]${_InforDocument.inforTitle}[/#if]</title>
<meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
<link rel="stylesheet" type="text/css" href="${base}/cms/templates/css/whole.css"/>
<link rel="stylesheet" type="text/css" href="${base}/cms/templates/css/layout.css"/>
<script src="/js/jquery.js" type="text/javascript"></script> <!--jquery包-->

<script type="text/javascript"> 




function getParameter(sProp) {
  var re = new RegExp(sProp + "=([^\&]*)", "i");
  var a = re.exec(document.location.search);
  if (a == null)
  return null;
  return a[1];
};

var sss=getParameter('inforId');






 $.ajax({


	url: "/cms/inforDocument.do?method=getHits&inforId="+sss

	});
		
		  	
</script>
</head>


<body style="text-align:center;">
<input type="hidden" name="QueryString" value="QueryString"/>
<div class="page_row">
	<div class="list pic_news">
		<div class="list_bar">
			 信息发布 &gt; ${_InforDocument.category.categoryName}
		</div>
		<div class="ctitle ctitle1">
			[#if _InforDocument.inforTitle??]${_InforDocument.inforTitle}[/#if]
		</div>
		<div class="ctitleinfo">作者：${_InforDocument.zuozhe}&nbsp;&nbsp;&nbsp;

			发布日期：${_InforDocument.createTime}&nbsp;&nbsp;&nbsp;
			[#if _InforDocument.inforTime??]信息时间：${_InforDocument.inforTime}&nbsp;&nbsp;&nbsp;[/#if]
			[#if _InforDocument.issueUnit??]发布单位：${_InforDocument.issueUnit}&nbsp;&nbsp;&nbsp;[/#if]
			[#if _InforDocument.keyword??]关键字：${_InforDocument.keyword}&nbsp;&nbsp;&nbsp;[/#if]
			
			查看次数:	<font id="summaryUl"><script>
			
			function getParameter(sProp) {
  			var re = new RegExp(sProp + "=([^\&]*)", "i");
 			 var a = re.exec(document.location.search);
  			if (a == null)
  			return null;
 			 return a[1];
			};

			var sss=getParameter('inforId');
				    	jQuery().ready(function (){
				    		$.getJSON("/cms/inforDocument.do?method=getNos&inforId="+sss,function(data) {
				    		var num = data._Infors;
				    		
				    		$('#summaryUl').html(num);
							
							});
				    	});
				    </script></font> 次&nbsp;&nbsp;&nbsp;
			[#if _InforDocument.source??]信息来源：<a href="${_InforDocument.relateUrl}" target="_blank">${_InforDocument.source}</a>[/#if]
		</div>	
		<div class="pbox">
			[#if _InforDocument.inforContent??]${_InforDocument.inforContent}[/#if]
		</div>
		<div>&nbsp;</div>
		<div class="ctitleinfo2">
			[#if _ArrayFiles??]
				附件下载：
				[#list _ArrayFiles as file]
				    <br>
				    <a href="${base}/common/download.jsp?filepath=${file}" target="_blank">
						<font color="#b40000">${_AttachmentNames[file_index]}</font>
					</a>
				[/#list]
			[/#if]
			[#if _ArrayPics??]
				<br>图片下载：
				[#list _ArrayPics as pic]
				    <br>
				    <a href="${base}/common/download.jsp?filepath=${pic}" target="_blank">
						<font color="#b40000">${_PicNames[pic_index]}</font>
					</a>
				[/#list]
			[/#if]
		</div>
		<div class="list_bar"></div>
	</div>
</div>
</body>
</html>
