<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
	<title>菜单</title>
	
	<SCRIPT type=text/javascript src="js/jquery-1.9.1.js"></SCRIPT>
	<link href="/css/all.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="/js/leftnav/css/font-awesome.min.css">
	<link rel="stylesheet" href="/js/leftnav/css/leftnav.css" media="screen" type="text/css">
	<link rel="stylesheet" href="/js/mloading/jquery.mloading.css">
	<script src="/js/mloading/jquery.mloading.js"></script>
	
	<script type="text/javascript">
		function showLoad(id){
			$("#"+id).mLoading({
			    text:"正在更新目录，请稍候~",//加载文字，默认值：加载中...
			    icon:"/images/waiting.gif",//加载图标，默认值：一个小型的base64的gif图片
			    html:false,//设置加载内容是否是html格式，默认值是false
			    content:"",//忽略icon和text的值，直接在加载框中显示此值
			    mask:true//是否显示遮罩效果，默认显示
			});
		}		
		function hideLoad(id){
			$("#"+id).mLoading("hide");//隐藏loading组件
		}
		function updateDir(){
			showLoad('accordion');
			if(confirm("更新需要较长时间，点击确定开始。")){
				
				$.ajax({
					url: '/security/document.htm?method=updateCategory',
					type:'POST', 
			        async: false,
			        cache: false,
			        dataType: "json",
			        beforeSend:function(xhr){
			        	
			        },
			        success: function(data){
			        	if(data.success){
			        		alert("更新成功，请退出重新登录！");
			        		hideLoad('accordion');
			        		//loadFiles($("#categoryId").val());
			        	}else{
			        		alert("删除失败，请重试或联系管理员！");
			        		hideLoad('accordion');
			        	}
			        },
			        complete:function(){
			        	hideLoad('accordion');
			        }
				});
				
				//hideLoad('accordion');
			}else{
				hideLoad('accordion');
			}
		}
	</script>
	
</head>
	<body style="background-color:#0B0C15;>
		 <div class="account-l fl" >
	        <%--<a class="list-title">账户概览</a>
	        --%>
	        <ul id="accordion" class="accordion">
	            <li>
	                <div class="link" style="background-color: #0B0C15;height: 60px"><i class="fa fa-file-text"></i>一般采购<i class="fa fa-chevron-down"></i></div>
	                <ul class="submenu" >
						<li><a href="/ybpurchase/purchaseInfor.do?method=listchild&flowId=1" target="mainInfor">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;一般采购</a></li>
						<li><a href="sfbj/sanfanglist.jsp?type=0" target="mainInfor">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;三方比价</a></li>
						<li><a href="/zhaotou/bidlist.jsp?type=0" target="mainInfor">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;招投标</a></li>
						<li><a href="/contract/contractList.jsp?type=0" target="mainInfor">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;合同变更</a></li>
	                </ul>
	            </li>
	            <li>
	                <div class="link" style="background-color: #0B0C15;height: 60px"><i class="fa fa-bell"></i>业务采购<i class="fa fa-chevron-down"></i></div>
	                <ul class="submenu">
						<li><a href="/ywpurchase/purchaseInfor.do?method=listchild&flowId=2" target="mainInfor">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;业务采购</a></li>
						<li><a href="sfbj/sanfanglist.jsp?type=1" target="mainInfor">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;三方比价</a></li>
						<li><a href="/zhaotou/bidlist.jsp?type=1" target="mainInfor">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;招投标</a></li>
						<li><a href="/contract/contractList.jsp?type=1" target="mainInfor">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;合同变更</a></li>
	                </ul>
	            </li>
	            <li>
	                <div class="link" style="background-color: #0B0C15;height: 60px"><i class="fa fa-dashboard"></i>工程采购<i class="fa fa-chevron-down"></i></div>
	                <ul class="submenu">
						<li><a href="/gcpurchase/purchaseInfor.do?method=listchild&flowId=3" target="mainInfor">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;工程项目</a></li>
						<li><a href="sfbj/sanfanglist.jsp?type=2" target="mainInfor">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;三方比价</a></li>
						<li><a href="/zhaotou/bidlist.jsp?type=2" target="mainInfor">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;招投标</a></li>
						<li><a href="/lxpurchase/purchaseInfor.do?method=listchild&flowId=4" target="mainInfor">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;零星工程维修</a></li>
	                </ul>
	            </li>
				<function:viewIf alias="appily" method="list">
					<li>
						<div class="link" style="background-color: #0B0C15;height: 60px"><i class="fa fa-unlock-alt"></i>供应商维护<i class="fa fa-chevron-down"></i></div>
						<ul class="submenu">
							<li><a href="/supplier/supplierList.jsp" target="mainInfor">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;供应商维护</a></li>
							<li><a href="/extend/template/templateBase.jsp" target="mainInfor">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;模板维护</a></li>
						</ul>
					</li>
				</function:viewIf>
	        </ul>
	       <script type="text/javascript" src='/js/leftnav/js/leftnav.js'></script>
	    </div>
	</body>
</html>


