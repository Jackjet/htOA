<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>
<html>
	<head>
		<title></title>
		<script src="<c:url value="/"/>components/jquery-1.4.2.js" type="text/javascript"></script> <!--jquery包-->

		<script>

			function init() {

                if(navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion .split(";")[1].replace(/[ ]/g,"")=="MSIE6.0")
                {
                    alert("为了更好的浏览体验，建议升级至IE9及其以上版本。");
                    $(document.body).css({
                        'background': 'url("/img/cggl.png") no-repeat center',
                        '-moz-background-size': '100% 100%',
                        '-o-background-size': '100% 100%',
                        '-webkit-background-size': '100% 100%',
                        'background-size': '100% 100%',
                        '-moz-border-image': 'url("/img/cggl.png") 0',
                        'filter':'progid:DXImageTransform.Microsoft.AlphaImageLoader(src="/img/cggl.png",sizingMethod="scale")',
                        '-ms-filter':'progid:DXImageTransform.Microsoft.AlphaImageLoader(src="/img/cggl.png",sizingMethod="scale")'

                    });
                }
                else if(navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion .split(";")[1].replace(/[ ]/g,"")=="MSIE7.0")
                {
                    alert("为了更好的浏览体验，建议升级至IE9及其以上版本。");
                    $(document.body).css({
                        'background': 'url("/img/cggl.png") no-repeat center',
                        '-moz-background-size': '100% 100%',
                        '-o-background-size': '100% 100%',
                        '-webkit-background-size': '100% 100%',
                        'background-size': '100% 100%',
                        '-moz-border-image': 'url("/img/cggl.png") 0',
                        'filter':'progid:DXImageTransform.Microsoft.AlphaImageLoader(src="/img/cggl.png",sizingMethod="scale")',
                        '-ms-filter':'progid:DXImageTransform.Microsoft.AlphaImageLoader(src="/img/cggl.png",sizingMethod="scale")'

                    });
                }
                else if(navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion .split(";")[1].replace(/[ ]/g,"")=="MSIE8.0")
                {
                    alert("为了更好的浏览体验，建议升级至IE9及其以上版本。");
                    $(document.body).css({
                        'background': 'url("/img/cggl.png") no-repeat center',
                        '-moz-background-size': '100% 100%',
                        '-o-background-size': '100% 100%',
                        '-webkit-background-size': '100% 100%',
                        'background-size': '100% 100%',
                        '-moz-border-image': 'url("/img/cggl.png") 0',
                        'filter':'progid:DXImageTransform.Microsoft.AlphaImageLoader(src="/img/cggl.png",sizingMethod="scale")',
                        '-ms-filter':'progid:DXImageTransform.Microsoft.AlphaImageLoader(src="/img/cggl.png",sizingMethod="scale")'

                    });
                }
                else if(navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion .split(";")[1].replace(/[ ]/g,"")=="MSIE9.0")
                {
                    $(document.body).css({
//						'background-image':'url("/img/cggl.png")',
                        'background-size': '100% 100%',
                        '-moz-background-size':'100% 100%',
                        'filter':'progid:DXImageTransform.Microsoft.AlphaImageLoader(src="/img/cggl.png",sizingMethod="scale")'
                    });

                }else if(navigator.userAgent.indexOf("Firefox") > -1)
                {
                    $(document.body).css({
//                        'background-image':'url("/img/cggl.png")',
                        'background-repeat':'no-repeat',
                        'background-size':'100% 126%',
                        '-moz-background-size':'100% 100%'
                    });
                }else{
                    $(document.body).css({
//						'background-image':'url("/img/cggl.png")',
                        'background-size': '100% 100%',
                        '-moz-background-size':'100% 100%',
                        '-webkit-background-size':'100% 100%',
                   		'-o-background-size':'100% 100%'
                    });
				}
			}
		</script>

		<%--<style type="text/css">--%>
			<%--span{border:1px solid blue;width:200px;height:200px;display:block;}--%>
		<%--</style>--%>
		<style>
		a{text-decoration : none}
		</style>

	</head>
	<%--<body onload='init()' style="background-image: url('/img/cggl.png');background-size: 100% 100%;-moz-background-size:100% 100%;">--%>
	<%--<body onload='init()'  style="background: url('/img/cggl.png') no-repeat center;  -moz-background-size: 100% 100%;--%>
		 <%---o-background-size: 100% 100%; -webkit-background-size: 100% 100%; background-size: 100% 100%;--%>
	 	 <%---moz-border-image: url('/img/cggl.png') 0; filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='/img/cggl.png',sizingMethod='scale';--%>
		 <%---ms-filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='/img/cggl.png',sizingMethod='scale');">--%>
	<body onload='init()' style="background-image: url('/img/cggl.png')">
	<br>
	<div style="text-align: center ;width: 100%;font-size: 50%;height: 8%"><a style="align-content: center;font-size: 42px;color: white" >采购管理平台</a></div>
	<div style="text-align: center ;width: 100%;font-size: 50%;height: 7%"><a style="align-content: center" ><font size="500px"></font> </a></div>
	<div style="text-align: center ;width: 100%;font-size: 50%;height: 7%"><a style="align-content: center" ><font size="500px"></font> </a></div>
	<div style="width: 100%;font-size: 50%;height: 9%">
		<span style="font-size: 40px;display:inline-block;width: 47.9%;text-align: center"><a style="align-content: center;color: white" >一般采购&nbsp; </a></span>
		<span style="font-size: 40px;display:inline-block;width: 49.5%;text-align: center"><a style="align-content: center;color: white" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;业务采购 </a></span>

	</div>
	<div style="width: 100%;font-size: 50%;height: 5%"><a style="align-content: center" ><font size="500px"></font> </a>
		<span style="font-size: 18px;display:inline-block;width:14%;visibility: hidden"><a style="align-content: center;color: white" >占位符号 </a></span>
		<span style="font-size: 18px;display:inline-block;width:13.5%"><a style="align-content: center;color: #0FC5D2" href="/ybpurchase/purchaseInfor.do?method=open&type=1"  >采购列表 </a></span>
		<span style="font-size: 18px;display:inline-block;width:13.5%"><a style="align-content: center;color: #0FC5D2" href="/ybpurchase/purchaseInfor.do?method=open&type=11">合同变更 </a></span>
		<span style="font-size: 18px;display:inline-block;width:13.5%;visibility: hidden"><a style="align-content: center;color: white" >占位符号 </a></span>
		<span style="font-size: 18px;display:inline-block;width:11.5%;visibility: hidden"><a style="align-content: center;color: white" >占位符号 </a></span>
		<span style="font-size: 18px;display:inline-block;width:12.5%"><a style="align-content: center;color: #0FC5D2" href="/ybpurchase/purchaseInfor.do?method=open&type=2">采购列表 </a></span>
		<span style="font-size: 18px;display:inline-block;width:12.5%"><a style="align-content: center;color: #0FC5D2" href="/ybpurchase/purchaseInfor.do?method=open&type=12">合同变更 </a></span>

	</div>
	<div style="width: 100%;font-size: 50%;height: 5%"><a style="align-content: center" ><font size="500px"></font> </a>
		<span style="font-size: 18px;display:inline-block;width:14%;visibility: hidden"><a style="align-content: center;color: white" >占位符号 </a></span>
		<span style="font-size: 18px;display:inline-block;width:13.5%"><a style="align-content: center;color: #0FC5D2" href="/ybpurchase/purchaseInfor.do?method=open&type=15" target="_blank">新增采购 </a></span>
		<span style="font-size: 18px;display:inline-block;width:13.5%"><a style="align-content: center;color: #0FC5D2" href="/ybpurchase/purchaseInfor.do?method=open&type=8">招投标 </a></span>
		<span style="font-size: 18px;display:inline-block;width:13.5%;visibility: hidden"><a style="align-content: center;color: white" >占位符号 </a></span>
		<span style="font-size: 18px;display:inline-block;width:11.5%;visibility: hidden"><a style="align-content: center;color: white" >占位符号 </a></span>
		<span style="font-size: 18px;display:inline-block;width:12.5%"><a style="align-content: center;color: #0FC5D2" href="/ybpurchase/purchaseInfor.do?method=open&type=16" target="_blank">新增采购 </a></span>
		<span style="font-size: 18px;display:inline-block;width:12.5%"><a style="align-content: center;color: #0FC5D2" href="/ybpurchase/purchaseInfor.do?method=open&type=9" >招投标 </a></span>

	</div>
	<div style="width: 100%;font-size: 50%;height: 4%"><a style="align-content: center" ><font size="500px"></font> </a>
		<span style="font-size: 18px;display:inline-block;width:14%;visibility: hidden"><a style="align-content: center;color: white" >占位符号 </a></span>

		<span style="font-size: 18px;display:inline-block;width:13.5%;visibility: hidden"><a style="align-content: center;color: white" >占位符号 </a></span>
		<span style="font-size: 18px;display:inline-block;width:13.5%"><a style="align-content: center;color: #0FC5D2" href="/ybpurchase/purchaseInfor.do?method=open&type=5" >三方比价 </a></span>
		<span style="font-size: 18px;display:inline-block;width:13.5%;visibility: hidden"><a style="align-content: center;color: white" >占位符号 </a></span>
		<span style="font-size: 18px;display:inline-block;width:11.5%;visibility: hidden"><a style="align-content: center;color: white" >占位符号 </a></span>

		<span style="font-size: 18px;display:inline-block;width:12.5%;visibility: hidden"><a style="align-content: center;color: white" >占位符号 </a></span>

		<span style="font-size: 18px;display:inline-block;width:12.5%"><a style="align-content: center;color: #0FC5D2" href="/ybpurchase/purchaseInfor.do?method=open&type=6">三方比价 </a></span>
	</div>

	<div style="text-align: center ;width: 100%;font-size: 50%;height: 8%;opacity: 0;filter: progid:DXImageTransform.Microsoft.Alpha(opacity=0);"><a style="align-content: center;font-size: 70px;" href="/ybpurchase/purchaseInfor.do?method=open&type=14" >待办</a></div>
	<div style="width: 100%;font-size: 50%;height: 9%">
		<span style="font-size: 40px;display:inline-block;width: 47.9%;text-align: center"><a style="align-content: center;color: white" >工程采购&nbsp; </a></span>
		<span style="font-size: 40px;display:inline-block;width: 49.5%;text-align: center"><a style="align-content: center;color: white" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;零星采购 </a></span>

	</div>
	<div style="width: 100%;font-size: 50%;height: 5%"><a style="align-content: center" ><font size="500px"></font> </a>
		<span style="font-size: 18px;display:inline-block;width:14%;visibility: hidden"><a style="align-content: center;color: white" >占位符号 </a></span>
		<span style="font-size: 18px;display:inline-block;width:13.5%"><a style="align-content: center;color: #0FC5D2" href="/ybpurchase/purchaseInfor.do?method=open&type=3" >采购列表 </a></span>
		<span style="font-size: 18px;display:inline-block;width:13.5%"><a style="align-content: center;color: #0FC5D2" href="/ybpurchase/purchaseInfor.do?method=open&type=13">合同变更 </a></span>
		<span style="font-size: 18px;display:inline-block;width:13.5%;visibility: hidden"><a style="align-content: center;color: white" >占位符号 </a></span>
		<span style="font-size: 18px;display:inline-block;width:11.5%;visibility: hidden"><a style="align-content: center;color: white" >占位符号 </a></span>
		<span style="font-size: 18px;display:inline-block;width:12.5%"><a style="align-content: center;color: #0FC5D2" href="/ybpurchase/purchaseInfor.do?method=open&type=4">采购列表 </a></span>
		<span style="font-size: 18px;display:inline-block;width:12.5%;visibility: hidden"><a style="align-content: center;color: white" >占位符号 </a></span>

	</div>
	<div style="width: 100%;font-size: 50%;height: 5%"><a style="align-content: center" ><font size="500px"></font> </a>
		<span style="font-size: 18px;display:inline-block;width:14%;visibility: hidden"><a style="align-content: center;color: white" >占位符号 </a></span>
		<span style="font-size: 18px;display:inline-block;width:13.5%"><a style="align-content: center;color: #0FC5D2" href="/ybpurchase/purchaseInfor.do?method=open&type=17" target="_blank" >新增采购 </a></span>
		<span style="font-size: 18px;display:inline-block;width:13.5%"><a style="align-content: center;color: #0FC5D2" href="/ybpurchase/purchaseInfor.do?method=open&type=10">招投标 </a></span>
		<span style="font-size: 18px;display:inline-block;width:13.5%;visibility: hidden"><a style="align-content: center;color: white" >占位符号 </a></span>
		<span style="font-size: 18px;display:inline-block;width:11.5%;visibility: hidden"><a style="align-content: center;color: white" >占位符号 </a></span>
		<span style="font-size: 18px;display:inline-block;width:12.5%"><a style="align-content: center;color: #0FC5D2" href="/ybpurchase/purchaseInfor.do?method=open&type=18" target="_blank">新增采购 </a></span>
		<span style="font-size: 18px;display:inline-block;width:12.5%;visibility: hidden"><a style="align-content: center;color: white" >占位符号 </a></span>

	</div>
	<div style="width: 100%;font-size: 50%;height: 5%"><a style="align-content: center" ><font size="500px"></font> </a>
		<span style="font-size: 18px;display:inline-block;width:14%;visibility: hidden"><a style="align-content: center;color: white" >占位符号 </a></span>

		<span style="font-size: 18px;display:inline-block;width:13.5%;visibility: hidden"><a style="align-content: center;color: white" >零星采购 </a></span>
		<span style="font-size: 18px;display:inline-block;width:13.5%"><a style="align-content: center;color: #0FC5D2" href="/ybpurchase/purchaseInfor.do?method=open&type=7">三方比价 </a></span>
		<span style="font-size: 18px;display:inline-block;width:13.5%;visibility: hidden"><a style="align-content: center;color: white" >占位符号 </a></span>
		<span style="font-size: 18px;display:inline-block;width:11.5%;visibility: hidden"><a style="align-content: center;color: white" >占位符号 </a></span>
		<span style="font-size: 18px;display:inline-block;width:12.5%;visibility: hidden"><a style="align-content: center;color: white" >占位符号 </a></span>
		<span style="font-size: 18px;display:inline-block;width:12.5%;visibility: hidden"><a style="align-content: center;color: white" >占位符号 </a></span>

	</div>


	</div>










	</body>

</html>