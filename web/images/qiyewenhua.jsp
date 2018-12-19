<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>
<html>
	<head>
		<title></title>
		<script src="<c:url value="/"/>components/jquery-1.4.2.js" type="text/javascript"></script> <!--jquery包-->
		<script type="text/javascript" src="js/setimgsize.js"></script>
		<style>
			content{
				width:100%;
				height:500px;
			}
			.fullBg {
				position: fixed;top: 0;left: 0;overflow: hidden;}
		</style>
		<script type="text/javascript">
			(function($) {
				$.fn.fullBg = function(){
					var bgImg = $(this);
					bgImg.addClass('fullBg');
					function resizeImg() {
						var imgwidth = bgImg.width();
						var imgheight = bgImg.height();
						var winwidth = $(window).width();
						var winheight = $(window).height();
						var widthratio = winwidth/imgwidth;
						var heightratio = winheight/imgheight;
						var widthdiff = heightratio*imgwidth;
						var heightdiff = widthratio*imgheight;
						alert(imgwidth+"--"+winwidth+"--"+widthdiff);
						if(heightdiff>winheight) {
							bgImg.css({
								width: winwidth+'px',
								height: heightdiff+'px'
							});
						} else {
							bgImg.css({
								width: widthdiff+'px',
								height: winheight+'px'
							});
						}
					}
					resizeImg();
					$(window).resize(function() {
						resizeImg();
					});
				};
			})(jQuery)
			
			$(window).load(function() {
				//$("#bakImg").fullBg();
			});
			
			
			function AutoResizeImage(maxWidth,maxHeight,objImg){
				if(maxHeight == -1){
					maxHeight = $(window).height()-22;
				}
				var img = new Image();
				img.src = objImg.src;
				var hRatio;
				var wRatio;
				var Ratio = 1;
				var w = img.width;
				var h = img.height;
				wRatio = maxWidth / w;
				hRatio = maxHeight / h;
				if (maxWidth ==0 && maxHeight==0){
				Ratio = 1;
				}else if (maxWidth==0){//
				if (hRatio<1) Ratio = hRatio;
				}else if (maxHeight==0){
				if (wRatio<1) Ratio = wRatio;
				}else if (wRatio<1 || hRatio<1){
				Ratio = (wRatio<=hRatio?wRatio:hRatio);
				}
				if (Ratio<1){
				w = w * Ratio;
				h = h * Ratio;
				}
				objImg.height = h;
				objImg.width = w;
			}
			
			function setHeight(obj){
				alert(ojb);
				alert($(window).height());
				setImgSize(obj,$(window).height(),$(window).height());
			}
		</script>
	</head>
	<body>
		<img src="qiyewenhua.jpg" onload="AutoResizeImage(0,-1,this)" id="bakImg" />
	</body>

</html>