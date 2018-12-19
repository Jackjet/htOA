<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta name='apple-itunes-app' content='app-id=1050553275'>
		
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no,minimal-ui">
		<meta name="format-detection" content="telephone=no">
		<meta name="apple-mobile-web-app-capable" content="yes"/>
		<meta name="apple-mobile-web-app-status-bar-style" content="black" />
			
		<title>app下载</title>
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
 
            //if (browser.versions.ios || browser.versions.iPhone || browser.versions.iPad) {
            if (browser.versions.wechat){
                //window.location="http://htoa.haitongauto.com/upload/apps/htOA_151012_real.ipa";
               // window.location="https://appsto.re/cn/7UIN-.i";
                //https://itunes.apple.com/cn/app/hai-tongoa/id1050553275?mt=8
                //var wechatForm = document.getElementById("wechatForm");
                //wechatForm.submit();
                //https://itunes.apple.com/cn/app/hai-ci-ci-dian-cha-ci-fan/id443374471?mt=8
                //location.href="http://mp.weixin.qq.com/mp/redirect?url=https://appsto.re/cn/7UIN-.i";
                //setTimeout(function() {window.location.href="https://appsto.re/cn/7UIN-.i";}, 1000);
                
            }else if (browser.versions.ios || browser.versions.iPhone || browser.versions.iPad) {
            	//window.location="https://appsto.re/cn/7UIN-.i";
            	window.location = "https://itunes.apple.com/cn/app/hai-tongoa/id1050553275?mt=8";
            }else if (browser.versions.android) {
            	//http://shouji.baidu.com/soft/item?docid=8090151
                //window.location="http://htoa.haitongauto.com/upload/apps/htOA_151030_real.apk";
                //window.location="http://m.baidu.com/app?action=shoujicheckas&&_w-m_=F4Tg5O81GTQDGVn/7xOh6Q==&docid=8090151";
                
                
                //window.location="http://mobile.baidu.com/simple?action=content&docid=8090151&ala=";  //9629275
                window.location="http://mobile.baidu.com/simple?action=content&docid=11080180&ala=";
            }else{
            	//window.location="https://appsto.re/cn/7UIN-.i";
            	window.location = "https://itunes.apple.com/cn/app/hai-tongoa/id1050553275?mt=8";
            }

            //document.writeln("语言版本: " + browser.language);
            //document.writeln(" 是否为移动终端: " + browser.versions.mobile);
            //document.writeln(" ios终端: " + browser.versions.ios);
            //document.writeln(" android终端: " + browser.versions.android);
            //document.writeln(" 是否为iPhone: " + browser.versions.iPhone);
            //document.writeln(" 是否iPad: " + browser.versions.iPad);
            //document.writeln(navigator.userAgent);

        </script>
        <style type="text/css">
			html,body,figure,h2,h3,dl,dt,dd,ul,li,p {
				margin: 0; padding: 0; display: block;
			}
			* {
				-webkit-tap-highlight-color:rgba(0,0,0,0);
			}
			body {
				font-size: 16px;
				font-family:"Microsoft YaHei", Helvetica, Arial, sans-serif;
				background: #ff2d4b;
			}
	
			.up{
				margin: 7px;
				text-align: right;
			}
			.up img{
				width: 25px;
				height: 32px;
			}
	
			.tip{
				margin: 0 12px;
				padding: 9px 20px 7px 14px;
				border-radius: 8px;
				background: #fff;
			}
			.tip img{
				float: left;
				width: 40px;
				height: 40px;
				margin-right: 12px;
			}
	
			.tip p{
				font-size: 0.9375em;
				line-height: 1.5em;
			}
	
			.line1{			
				color: #333;
			}
	
			.line2{
				color: #ff2d4b;
			}
		</style>
	        

	</head>

	<body>
		<div style="text-align:center;width:100%;display:none;">
			<br/><br/>
			<h3>若本页无跳转，请点击右上角，选择“在浏览器中打开”</h3>
		</div>
		<div style="display:none;"><!--   -->
			<form id="wechatForm" name="wechatForm" action="https://itunes.apple.com/cn/app/hai-tongoa/id1050553275?mt=8" method="post">
				<input type="image" src="/images/down-ios.png"/>
			</form>
			<a href="http://mp.weixin.qq.com/mp/redirect?url=https://itunes.apple.com/cn/app/hai-tongoa/id1050553275?mt=8"><img src="/images/down-ios.png" width="150" height="42" border=0/></a>
		</div>
		
		
		<div id="text">
			<figure class="up" ><img src="/images/app-up.jpg"></figure>
			<div class="tip">
				<img src="/images/app-icon.png">
				<p class="line1">下载应用请点击右上角按钮选择</p>
				<p class="line2">[在浏览器中打开]</p>
			</div>
		</div>
			
		
	</body>

</html>