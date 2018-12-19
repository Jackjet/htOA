

	/** 捕捉onmouseover,onmouseout,onclick事件改变tr样式 */
	var objchecked=null;//用于存放click事件的tr对象
	//鼠标放上tr时的样式
	function over(obj,className){
		if(obj.checked=="checked") {return false;}
		obj.oldClassName=obj.className;
		obj.className = className;
	}
	//鼠标离开tr时的样式
	function out(obj){
		if(obj.checked=="checked") {return false;}
		obj.className = obj.oldClassName;
	}
	//鼠标点击tr时的样式
	function clicked(obj){
		//将之前点击过的tr的class还原
		if(objchecked!==null){
			objchecked.className = objchecked.oldClassName;
			objchecked.checked="";
		}
		//将当前tr的对象和class保存到objchecked,并改变当前tr的class和状态
		objchecked=obj;
		objchecked.oldClassName=obj.oldClassName;
		obj.className = "ui-widget-content jqgrow ui-row-ltr ui-state-highlight";
		obj.checked="checked";
	}
	/** ******************* */
	
	//格式化tab(仅针对id以'tabs-'开头的div)
	function loadTabCss() {
		$("div[id^='tabs-']").css({'margin-top':'0px','margin-left':'0px','padding-left':'0px','padding-top':'3px','padding-right':'0px'
		});
	}