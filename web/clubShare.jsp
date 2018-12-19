<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <!-- <meta name="viewport" content="user-scalable=no,width=640,target-densitydpi=device-dpi"> -->
    <!--<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">-->
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no"/>
    <meta name='apple-itunes-app' content='app-id=1050553275'>
    
    <meta name="apple-mobile-web-app-title"
    content="" />
    <meta name="apple-mobile-web-app-status-bar-style"
    content="black-translucent" />
    <meta name="HandheldFriendly" content="true" />
    
    <title>海通码头主题活动</title>
	<script src="/components/jquery-1.4.2.js" type="text/javascript"></script> <!--jquery包-->

    <style>
        body{
            margin:0 auto;
            overflow-x:hidden;
            font-family:Helvetica,​Arial,​sans-serif,微软雅黑;
        }
        #downHeader{
        	display:none;
            width:100%;
            height:70px;
            background: #F1F1F1;
            border-top:1px solid silver;
            border-bottom:1px solid silver;
            font-family:黑体;
            color:black;
        }
        .mainTitle{
            font-size:15px;
        }
        .subTitle{
            font-size:12px;
        }
        a{
            //color: #007AFF;
            color:white;
            text-decoration: none;
        }
        #downHeader td{
            overflow: hidden;
            text-overflow: ellipsis;
        }
        
        #mainContent{
        	width:100%;
        	border:0px solid red;
        	min-heigth:300px;
        	overflow-x:hidden;
        	//background: url(/images/clubBg.png) no-repeat;
        }
        
        #mainContent table{
        	text-align:left;
        	font-size:14px;
        }
    </style>
</head>
<body>
    <div id="downHeader" style="display:none;">
        <table align="center" width="100%" border="0" valign="middle">
            <tr>
                <td width="20" align="center">
                    &nbsp;
                    <img src="/images/close.png" onclick="closeBlock('downHeader');">
                    &nbsp;
                </td>
                <td width="120" align="center">
                    <img src="/images/app-icon.png" width="60" height="60">
                </td>
                <td valign="middle" nowrap="nowrap">
                    <span class="mainTitle"><b>海通OA</b></span><br/>
                    <span class="subTitle">上海慧智计算机技术有限公司</span>
                </td>
                <td align="center" nowrap="nowrap">
                    <span class="subTitle">若未安装，可</span><br/>
                    <a href="http://mobile.baidu.com/simple?action=content&docid=9680700&ala=" target="_blank">下载</a>
                </td>
            </tr>
        </table>
    </div>
    
    <script type="text/javascript">
        /*
         * 智能机浏览器版本信息:
         *
         */
        var browser = {
            versions: function() {
                var u = navigator.userAgent, app = navigator.appVersion;
                return {//移动终端浏览器版本信息 
                    wechat: u.indexOf('MicroMessenger') > -1,//微信  || u.indexof('NetType') > -1
                    trident: u.indexOf('Trident') > -1, //IE内核
                    presto: u.indexOf('Presto') > -1, //opera内核
                    webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
                    gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核
                    mobile: !!u.match(/AppleWebKit.*Mobile.*/) || !!u.match(/AppleWebKit/), //是否为移动终端
                    ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
                    android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或者uc浏览器
                    iPhone: u.indexOf('iPhone') > -1 || u.indexOf('Mac') > -1, //是否为iPhone或者QQHD浏览器
                    iPad: u.indexOf('iPad') > -1, //是否iPad
                    webApp: u.indexOf('Safari') == -1 //是否web应该程序，没有头部与底部
                };
            }(),
            language: (navigator.browserLanguage || navigator.language).toLowerCase()
        }
    
    	//alert (browser.versions.android);
        if (browser.versions.android) {
            //document.getElementById("downHeader").style.display = "block";
            //alert($("#downHeader").css("display"));
            
            //$("#downHeader").css("display","block");
        }else{
        	//document.getElementById("downHeader").style.display = "none";
        	$("#downHeader").css("display","none");
        }
        
        /*document.addEventListener('touchmove', function(event) {
            if(event.target.type == 'range') return;
            event.preventDefault();
        })*/
        function stopScrolling( touchEvent ) {   
            touchEvent.preventDefault();   
        }  
        //document.addEventListener( 'touchstart' , stopScrolling , false );  
        //document.addEventListener( 'touchmove' , stopScrolling , false );  

        function closeBlock(id){
        	$("#" + id).css("display","none");
        }

    </script>
    
    <style>
    	.sInfor{
    		font-size:14px;
    		color:gray;
    		line-height:200%;
    	}
    </style>
    
    <div id="mainContent">
    	<table align="center" border="0" width="100%" cellspacing=0 cellpadding=8>
    		<tr>
    			<td align="left" valign="bottom" style="background:#F1F1F1;border-bottom:0px solid #DEDEDE;height:auto;">
    				<img src="/images/fulllogo.png" width="112" height="19" border=0/><br/>
    				<span style="font-size:26px;font-weight:bold;"><font color="#E85206">主题活动</font></span>
    			</td>
    		</tr>
    		<tr>
    			<td align="left">
    				<span style="font-size:18px;color:black;font-weight:bold;">${_ClubInfor.actTitle}</span><br/>
    				<span style="font-size:14px;color:gray;"><c:if test="${empty _ClubInfor.league}">海通码头</c:if><c:if test="${!empty _ClubInfor.league}">${_ClubInfor.league}</c:if></span>
    			</td>
    		</tr>
    		<tr>
    			<td align="center">
    				<img src="<c:url value="/"/>${_ClubInfor.mainPic}" width="90%">
    			</td>
    		</tr>
    		<tr>
    			<td>
    				<span class="sInfor">时&nbsp;&nbsp;&nbsp;&nbsp;间：</span>
    				<span>
    					<fmt:formatDate value="${_ClubInfor.actTime}" pattern="yyyy-MM-dd HH:mm:00"/>  
						至  <fmt:formatDate value="${_ClubInfor.toTime}" pattern="yyyy-MM-dd HH:mm:00"/> 
					</span><br/>
    				<span class="sInfor">地&nbsp;&nbsp;&nbsp;&nbsp;点：</span><span>${_ClubInfor.actPlace}</span><br/>
    				<span class="sInfor">管理员：</span><span>${_ClubInfor.manager.person.personName}</span><br/>
    			</td>
    		</tr>
    		<tr>
    			<td>
    				<span style="line-height:200%;color:#3377AA;">目前已有 <font color="black"><b>${fn:length(_ClubInfor.registers)}</b></font> 人报名</span>
    				&nbsp;&nbsp;
    				<img src="/images/search-hot.gif">
    				
    				<br/><br/>
    				<span style="font-size:16px;color:#007722;">活动详情</span><br/><br/>
    				<img src="/images/t_016.gif" /><span class="sInfor">报名日期：</span>
    					<span>
    						<fmt:formatDate value="${_ClubInfor.beginSignDate}" pattern="yyyy-MM-dd"/> 
								至  <fmt:formatDate value="${_ClubInfor.cutDate}" pattern="yyyy-MM-dd"/>
    					</span><br/><br/>
    				<img src="/images/t_016.gif" /><span class="sInfor">活动项目：</span><span>${_ClubInfor.actItem}</span><br/><br/>
    				<img src="/images/t_016.gif" /><span class="sInfor">报名方法：</span><span>${_ClubInfor.registerWay}</span><br/><br/>
    				<img src="/images/t_016.gif" /><span class="sInfor">活动规则：</span><span>${_ClubInfor.actRule}</span><br/><br/>
    				<img src="/images/t_016.gif" /><span class="sInfor">备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注：</span><span>${_ClubInfor.memo}</span><br/>
    			</td>
    		</tr>
    		<tr>
    			<td style="border-top:1px dotted silver;color:gray;font-size:12px;" align="center">
    				copyright©2013 <br/>上海海通国际汽车码头有限公司<br/>上海海通国际汽车物流有限公司<br/>
    				（沪ICP备14025296-1号-3）版权所有
    			</td>
    		</tr>
    	</table>
    </div>
    <div id="footer" style="text-align:right;width:100%;background:rgba(0, 0, 0, 0.4);position:fixed;bottom:0px;border:0px solid red;height:50px;vertical-align:middle;">
    	<table width="100%">
    		<tr>
    			<td align="left" valign="middle" onclick="closeBlock('footer');">
    				&nbsp;&nbsp;
    				<img src="/images/reset.gif" onclick="closeBlock('footer');" >
    			</td>
    			<td valign="middle">
    				<a target="_blank" href="http://htoa.haitongauto.com/appDown.jsp">
    					<span style="line-height:50px;color:white;font-weight:bold;">请下载海通OA——></span>&nbsp;
    					<img src="/images/app-icon.png" align=absmiddle width="45" height="45" border=0>&nbsp;&nbsp;
    				</a>
    			</td>
    		</tr>
    	</table>
    	<!-- <img src="/images/app-icon.png" width="45" height="45">&nbsp;&nbsp; -->
    	
    	<div>
    		
    	</div>
    </div>
    
</body>
</html>