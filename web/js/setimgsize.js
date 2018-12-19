/**
	设置图片大小，使图片在固定范围内按照比例缩放

**/

	/*function setImgSize(obj,maxwidth,maxheight) { 
		myImage = new Image(); 
		myImage.src = obj.src; 
		if (myImage.width>0 && myImage.height>0) { 
			var rate = 1; 
			if (myImage.width>maxwidth || myImage.height>maxheight) { 
				if (maxwidth/myImage.width<maxheight/myImage.height) { 
					rate = maxwidth/myImage.width; 
				}else { 
					rate = maxheight/myImage.height; 
				} 
			} 
			//浏览器兼容性
			if (window.navigator.appName == "Microsoft Internet Explorer") { 
				obj.style.zoom = rate; 
			} else { 
				obj.width = myImage.width*rate; 
				obj.height = myImage.height*rate; 
			} 
		} 
	}*/
	
	function setImgSize(obj,maxwidth,maxheight){
		if(obj.width> maxwidth || obj.height>maxheight){
			if (maxwidth/obj.width<maxheight/obj.height){
				obj.height*=maxwidth/obj.width;
				obj.width=maxwidth;
			}else{
				obj.width*=maxheight/obj.height;
				obj.height=maxheight;
			}
			
		}	
		
		if(obj.width< maxwidth || obj.height<maxheight){
			if (maxwidth/obj.width<maxheight/obj.height){
				obj.height*=maxwidth/obj.width;
				obj.width=maxwidth;
			}else{
				obj.width*=maxheight/obj.height;
				obj.height=maxheight;
			}
			
		}
	}
	
	/**function setImgSize(objImg,maxWidth,maxHeight){   
		if (typeof(objImg)=='string'){   
		objImg = $(objImg);   
		}   
		var img = new Image();   
		img.onload = function(){   
		var hRatio;   
		var wRatio;   
		var Ratio = 1;   
		var w = img.width;   
		var h = img.height;   
		wRatio = maxWidth / w;   
		hRatio = maxHeight / h;   
		wRatiowRatio = wRatio>1?1:wRatio;   
		hRatiohRatio = hRatio>1?1:hRatio;   
		if (maxWidth ==0 && maxHeight==0){   
		Ratio = 1;   
		}else if (maxWidth==0){//   
		if (hRatio<1) Ratio = hRatio;   
		}else if (maxHeight==0){   
		if (wRatio<1) Ratio = wRatio;   
		}else {   
		Ratio = (wRatio<=hRatio?wRatio:hRatio);   
		}   
		if (Ratio<1){   
		ww = w * Ratio;   
		hh = h * Ratio;   
		}   
		objImg.height = h;   
		objImg.width = w;   
		this.onload = function(){}   
		}   
		img.src = objImg.src;   
		}   **/
