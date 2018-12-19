$(document).ready(function() {
	// initUpload();
});

/**
 * 在页面上调用此初始化方法
 * 参数说明：
 * 	exsitFiles------------已存在的图片附件，在修改时传入，格式为："相对路径|文件名|文件大小",新增时为空即可
 * 	fileQueue-------------uploadify所需要元素的ID，此ID随便填写即可，只要不与其它同名
 * 	uploadify-------------uploadify所需要元素的ID，此ID随便填写即可，只要不与其它同名
 * 	filesId---------------后台得到附件路径的name及id名，需要在页面中定义此容器元素，如input，即name为此的元素必须存在于页面中，且不能为vo中的属性名，否则会出错
 * 	fileList--------------组件上传后显示到页面的容器ID，此ID同样要中队伍填写，只要不与其它同名
 * 	uploadDiv-------------初始化uploadify时，包裹此组件的容器元素ID，一般为div，也必须实际存在于页面中
 * 
 */
function initUpload(exsitFiles,fileQueue,uploadify,filesId,fileList,uploadDiv) {
	var initElement = "";
	initElement = "<div>";
	initElement += "<div id='"+fileQueue+"'></div>";
	initElement += "<input type='file' name='uploadify' id='"+uploadify+"' multiple='true'/>";
	//initElement += "<input type='hidden' class='textfield' size='70' name='uploadify_files' id='files' value=''/>";
	initElement += "</div>";
	initElement += "<div id='"+fileList+"'></div>";

	$("#"+uploadDiv).append(initElement);

	if (exsitFiles != null && exsitFiles != "") {
		var exsits = new Array();
		exsits = exsitFiles.split(";"); // 字符分割

		for ( var i = 0; i < exsits.length; i++) {
			var tempFile = exsits[i];
			appendFileHtml($.trim(tempFile),fileList,filesId);
		}
	}

	addPlugin(uploadify,fileQueue,fileList,filesId);
}

function addPlugin(uploadify,fileQueue,fileList,filesId) {
	var filePaths = "";
	$("#"+uploadify).uploadify({
		// 开启调试
		debug : false,

		method : 'post',
		// successTimeout:99999,
		auto : true, // 不自动提交
		multi : true,
		// fileTypeDesc : '支持上传的文件格式：',
		// fileTypeExts : '*.jpg;*.jpeg;*.gif;*.png',
		buttonText : '上传附件',
		// buttonImage : '/lib/uploadify/btn.png',
		// fileObjName: 'file', //后台接受文件对象名，保持一致
		height : 20,
		// removeCompleted : true, //上传后是否从队列中移除
		swf : '/js/uploadify/uploadify.swf',
		uploader : '/servlet/Upload',
		queueID : fileQueue,

		width : 80,
		displayData : 'speed',
		fileSizeLimit : '300MB',
		queueSizeLimit : 50,
		uploadLimit : 50,
		onUploadComplete : function(file) {
		},
		onQueueComplete : function(queueData) {
			// postInfo(filePaths);
			filePaths = "";
		},
		onSelectError : function(file, errorCode, errorMsg) {
			// file选择失败后触发
			alert(errorMsg);
		},
		onFallback : function() {
			// flash报错触发
			alert("请您先安装flash控件");
		},
		//onSelect : function(file) {  
	   //     this.addPostParam("file_name",encodeURI(file.name));//改变文件名的编码
	    //},
			
		onUploadSuccess : function(file, data, response) {
			// 上传成功后触发
			if ("sizeError" == data) {
				alert("文件大小不能超过100M");
			} else if ("typeError" == data) {
				alert("不支持的文件类型");
			}
	
			// filePaths += $.trim(data) + ";";
			// alert(filePaths);
			 //alert('-------'+$.trim(data));
			appendFileHtml($.trim(data),fileList,filesId);
		}
	});
}

function appendFileHtml(fileData,fileList,filesId) {
	var fileHtml;
	//alert(fileData);
	var strs = new Array();
	strs = fileData.split("|"); // 字符分割
	var folder = strs[0];
	var name = strs[1];
	var size = strs[2];
	
	var i = folder.lastIndexOf("/");
	var uPath = folder.substr(i+1);
	//alert(decodeURI(name));
	
	fileHtml = "<div id='" + uPath + "'>";
	fileHtml += "<span>" + name + "</span>";
	fileHtml += "<span>(" + size + ")</span>";
	fileHtml += "&nbsp;";
	fileHtml += "<a href='#' onclick='javascript:delExistAttach(\"" + folder + "\",\""+filesId+"\");return false;'>删除</a><br>";
	fileHtml += "<div>";
	$("#"+fileList).append(fileHtml);

	// 放入隐藏域
	//var filePath = "";
	//filePath = folder + "/" + name;
	var files = $("#"+filesId).val();
	if (files == null || files == "") {
		$("#"+filesId).val(fileData);
	} else {
		files = files + ";" + fileData;
		$("#"+filesId).val(files);
	}
}

function delExistAttach(folder,filesId) {
	var i = folder.lastIndexOf("/");
	var uPath = folder.substr(i+1);
	$.ajax({
		url : "/servlet/deleteFile?folder=" + folder,
		cache : false,
		// data:{folder: folder},
		type : "POST",
		dataType : "html",
		beforeSend : function(xhr) {
		},

		complete : function(req, err) {
			refreshHtml(uPath,filesId);
		}
	});
}

function refreshHtml(uPath,filesId) {
	// 清空html;
	var files = $("#"+filesId).val();

	var fileArray = new Array();
	fileArray = files.split(";"); // 字符分割

	var newFiles = "";
	for ( var i = 0; i < fileArray.length; i++) {
		var tempFile = fileArray[i];

		if (tempFile.indexOf(uPath) < 0) {
			if (newFiles == "") {
				newFiles = tempFile;
			} else {
				newFiles = newFiles + ";" + tempFile;
			}
		} else {
			// $("#"+folder).remove();
			// alert(folder);
			// $("#fileList").remove("div[folder='"+folder+"']");//
			// $("div[folder='"+folder+"']").remove();
			// $("#" +folder).remove();

			// alert($("#" +folder));

			// $("#" +folder).html("");
		}
	}

	$("#"+filesId).val(newFiles);
	$("#" + uPath).remove();
}


/*******************************上传图片后即时预览（限一张）*******************************/
/**
 * 在页面上调用此初始化方法
 * 参数说明：
 * 	exsitFiles------------已存在的图片附件，在修改时传入，格式为："相对路径|文件名|文件大小",新增时为空即可
 * 	fileQueue-------------uploadify所需要元素的ID，此ID随便填写即可，只要不与其它同名
 * 	uploadify-------------uploadify所需要元素的ID，此ID随便填写即可，只要不与其它同名
 * 	multi-----------------是否支持多附件上传
 * 	count-----------------限制附件的个数
 * 	files-----------------后台得到附件路径的name名，需要在页面中定义此容器元素，如input，即name为此的元素必须存在于页面中，且不能为vo中的属性名，否则会出错
 * 	fileList--------------组件上传后显示到页面的容器ID，此ID同样要中队伍填写，只要不与其它同名
 * 	uploadDiv-------------初始化uploadify时，包裹此组件的容器元素ID，一般为div，也必须实际存在于页面中
 * 
 * 具体使用示例，可参见web/sys/editConfig.jsp中的使用
 */
function initUploadImg(exsitFiles,fileQueue,uploadify,multi,count,files,fileList,uploadDiv) {
	var initElement = "";
	initElement = "<div>";
	initElement += "<div id='"+fileQueue+"'></div>";
	initElement += "<input type='file' name='uploadify' id='"+uploadify+"' multiple='false'/>";
	//initElement += "<input type='hidden' size='70' name='uploadify_files' id='"+files+"' value=''/>";
	initElement += "</div>";
	initElement += "<div id='"+fileList+"'></div>";

	$("#"+uploadDiv).append(initElement);

	if (exsitFiles != null && exsitFiles != "") {
		var exsits = new Array();
		exsits = exsitFiles.split(";"); // 字符分割
		
		for ( var i = 0; i < exsits.length; i++) {
			var tempFile = exsits[i];
			appendImgHtml($.trim(tempFile),fileList,files);
		}
	}
	addImgPlugin(uploadify,fileQueue,multi,count,fileList,files);
}

function addImgPlugin(uploadify,fileQueue,multi,count,fileList,files) {
	var filePaths = "";
	$("#"+uploadify).uploadify({
		// 开启调试
		debug : false,

		method : 'post',
		auto : true, // 不自动提交
		multi : multi,
		fileTypeDesc : '支持的格式：',
		fileTypeExts : '*.jpg;*.jpeg;*.gif;*.png',
		buttonText : '上传附件',
		// buttonImage : '/lib/uploadify/btn.png',
		// fileObjName: 'file', //后台接受文件对象名，保持一致
		height : 20,
		// removeCompleted : true, //上传后是否从队列中移除
		swf : '/js/uploadify/uploadify.swf',
		uploader : '/servlet/Upload',
		queueID : fileQueue,

		width : 80,
		displayData : 'speed',
		fileSizeLimit : '5MB',
		queueSizeLimit : count,
		uploadLimit : count,
		onUploadComplete : function(file) {
		},
		onQueueComplete : function(queueData) {
			// postInfo(filePaths);
			filePaths = "";
		},
		onSelectError : function(file, errorCode, errorMsg) {
			// file选择失败后触发
			alert(errorMsg);
		},
		onFallback : function() {
			// flash报错触发
			alert("请您先安装flash控件");
		},
		onUploadSuccess : function(file, data, response) {
			// 上传成功后触发
			if ("sizeError" == data) {
				alert("文件大小不能超过5M");
			} else if ("typeError" == data) {
				alert("不支持的文件类型");
			}

			// filePaths += $.trim(data) + ";";
			// alert(filePaths);
			 //alert('-------'+$.trim(data));
			appendImgHtml($.trim(data),fileList,files);
		}
	});
}

function appendImgHtml(fileData,fileList,filesId) {
	var fileHtml;

	var strs = new Array();
	strs = fileData.split("|"); // 字符分割
	var folder = strs[0];
	var name = strs[1];
	var size = strs[2];
	//var contextPath = strs[3]; //项目路径
	
	var i = folder.lastIndexOf("/");
	var uPath = folder.substr(i+1);
	var realPath = folder + "/" + name;  //实际路径
	
	fileHtml = "<div id='" + uPath + "'>";
	//fileHtml += "<span style='color:red;cursor:pointer;' onmouseover=showBigPic('big_"+uPath+"','"+realPath+"'); onmouseout=hidBigPic('big_"+uPath+"');>" + name;
	fileHtml += "<a href='/common/download.jsp?filepath="+folder+"/"+name+"' title='点击下载'><font color='red'>" + name;
	fileHtml += "(" + size + ")</font></a>";
	fileHtml += "<img src='"+folder+"/"+name+"' border=0 onload=\"setImgSize(this,32,32)\" >";
	//onmouseover=\"showBigPic('big_"+uPath+"','"+realPath+"');\" onmouseout=\"hidBigPic('big_"+uPath+"');\"
	fileHtml += "<div id='big_"+uPath+"' style='position:absolute;display:none;z-index:999;'></div>";
	fileHtml += "&nbsp;";
	fileHtml += "<a href='#' onclick='javascript:delExistImg(\"" + folder + "\",\""+filesId+"\");return false;'>删除</a><br>";
	fileHtml += "<div>";
	$("#"+fileList).append(fileHtml);
	
	// 放入隐藏域
	//var filePath = "";
	//filePath = folder + "/" + name;
	var files = $("#"+filesId).val();
	if (files == null || files == "") {
		$("#"+filesId).val(fileData);
	} else {
		files = files + ";" + fileData;
		$("#"+filesId).val(files);
	}
}

function delExistImg(folder,files) {
	var i = folder.lastIndexOf("/");
	var uPath = folder.substr(i+1);
	//alert(uPath);
	$.ajax({
		url : "/servlet/deleteFile?folder=" + folder,
		cache : false,
		// data:{folder: folder},
		type : "POST",
		dataType : "html",
		beforeSend : function(xhr) {
		},

		complete : function(req, err) {
			refreshImgHtml(uPath,files);
		}
	});
}

function refreshImgHtml(uPath,filesId) {
	// 清空html;
	var files = $("#"+filesId).val();

	var fileArray = new Array();
	fileArray = files.split(";"); // 字符分割

	var newFiles = "";
	for ( var i = 0; i < fileArray.length; i++) {
		var tempFile = fileArray[i];

		if (tempFile.indexOf(uPath) < 0) {
			if (newFiles == "") {
				newFiles = tempFile;
			} else {
				newFiles = newFiles + ";" + tempFile;
			}
		} else {
		}
	}

	$("#"+filesId).val(newFiles);
	$("#" + uPath).remove();
}

//显示上传图片大图
function showBigPic(bigID,realPath){
	//alert(bigID+"---"+realPath);
	$("#"+bigID).html("<img style='border:0px solid gray;' src='" + realPath + "' >").show();
}
function hidBigPic(bigID){
	 $("#"+bigID).hide("slow");
}

/**
设置图片大小，使图片在固定范围内按照比例缩放

**/
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