//******************************************************
/**
* 窗口隐含域处理
* @AUTHOR	sissi
* @VERSION 	2004.10.18 新建
*/
//******************************************************

function init() {
	setTopTitle();
	showAlert();
	showConfirm();
	openNewWindow();
	openNewUrl();
	writeCookie("");
	writeQuickIdeaDefault();
	getFrameHeight();
	window.inerror = false;
	if (document.all.functionName != null && opener != null) {
		if (document.all.functionName.value == "^^0509CLOSE") {
			document.all.functionName.value = "";
			opener.document.actForm.submit();
			window.close();
			return true;
		}
	}
	if (document.all.functionName != null && document.all.functionName.value == "^^CLOSE") {
		document.all.functionName.value = "";
		if (opener != null) {
			opener.document.actForm.submit();
		}
		window.close();
		return true;
	}
}

function menu() {
	st_onload();
}

//******************************************************
/**
* 初使化窗口时，根据父窗口传的值设置窗口标题
*/
//******************************************************
function setTopTitle() {
	if (document.homeForm.topTitle.value != "") {
		top.document.title = document.homeForm.topTitle.value;
	}
}

function showAlert() {
	if (document.homeForm.alertMsg.value != "") {
		alert(document.homeForm.alertMsg.value);
		document.homeForm.alertMsg.value = "";
	}
}

function openNewUrl() {
	if (document.homeForm.newUrl.value != "") {
		openUrl(document.homeForm.newUrl.value);
		document.homeForm.newUrl.value = "";
	}
}

function openUrl(urlStr) {
	window.open(urlStr, 'newWindow',
			   'toolbar=yes,location=yes,directories=yes,status=yes,menubar=yes,scrollbars=yes,resizable=yes');
}

function openNewWindow() {
	if (document.homeForm.newWindow.value != "") {
	    var varOption = "toolbar=no,location=no,status=yes,menubar=no,resizable=yes,scrollbars=yes,width=" + screen.availWidth + ",height=" + screen.availHeight + ",left=0,top=0";
	    window.open(document.homeForm.newWindow.value, 'autoOpenWin', varOption);
	    document.homeForm.newWindow.value = "";
	}
}

function showConfirm() {
	if (document.homeForm.confirmMsg.value != "") {
		if (confirm(document.homeForm.confirmMsg.value)) {
    			document.homeForm.submit();
		}
		document.homeForm.confirmMsg.value = "";
	}
}

//******************************************************
/**
* 输入框内的提示文字显示与设置
*/
//******************************************************
function inputAreaClick(inputElement, showText) {
	if (inputElement.value == showText) {
		inputElement.value = "";
	}
	inputElement.select;
}

function inputAreaBlur(inputElement, showText) {
	if (inputElement.value == "") {
		inputElement.value = showText;
	}
}

function writeCookie(str) {
	var expdate = new Date();
	SetCookie("oaDesktop", str, expdate, "/");
}

function SetCookie (nameValue, valueValue) {
	var argv = SetCookie.arguments;
	var argc = SetCookie.arguments.length;
	var expires = (argc > 2) ? argv[2] : null;
	var path = (argc > 3) ? argv[3] : null;
	var domain = (argc > 4) ? argv[4] : null;
	var secure = (argc > 5) ? argv[5] : false;
	document.cookie = nameValue + "=" + escape (valueValue) +
		((expires == null) ? "" : ("; expires=" + expires.toGMTString())) +
		((path == null) ? "" : ("; path=" + path)) +
		((domain == null) ? "" : ("; domain=" + domain)) +
		((secure == true) ? "; secure" : "");
}

function printDOC() {
	var hkey_root,hkey_path,hkey_key;
	hkey_root="HKEY_CURRENT_USER";
	hkey_path="\\Software\\Microsoft\\Internet Explorer\\PageSetup\\";
	try{
	  var RegWsh = new ActiveXObject("WScript.Shell") ;
	  hkey_key="header" ;
	  RegWsh.RegWrite(hkey_root+hkey_path+hkey_key,"") ;
	  hkey_key="footer" ;
	  RegWsh.RegWrite(hkey_root+hkey_path+hkey_key,"") ;
	  }
	 catch(e){}
	window.print();
}

function privewDOC() {
	var hkey_root,hkey_path,hkey_key;
	hkey_root="HKEY_CURRENT_USER";
	hkey_path="\\Software\\Microsoft\\Internet Explorer\\PageSetup\\";
	try{
	  var RegWsh = new ActiveXObject("WScript.Shell") ;
	  hkey_key="header" ;
	  RegWsh.RegWrite(hkey_root+hkey_path+hkey_key,"") ;
	  hkey_key="footer" ;
	  RegWsh.RegWrite(hkey_root+hkey_path+hkey_key,"") ;
	  }
	 catch(e){}
	var obj = document.all.WebBrowser;
	
 	obj.ExecWB(7,1);
 	
}

function writeQuickIdeaDefault() {
	if (document.all.quickIdeaList == null || document.all.ideaText == null) return false;
	if (document.all.quickIdeaList.length == 0) return false;
	var count = document.all.quickIdeaList.length;
	for (var i = 0; i < count; i++) {
		if (document.all.quickIdeaList.options[i].value == 1) {
			document.all.ideaText.value = document.all.quickIdeaList.options[i].text;
			document.all.quickIdeaList.selectedIndex = i;
			break;
		}
	}
}

function writeQuickIdea() {
	if (document.all.quickIdeaList == null || document.all.ideaText == null) return false;
	var selectedItem = document.all.quickIdeaList.selectedIndex;
	if (selectedItem == -1) return false;
	document.all.ideaText.value = document.all.quickIdeaList.options[selectedItem].text;
}

function showQueryArea() {
	if (document.all.queryArea.style.display == "none") {
		document.all.queryArea.style.display = "";
		document.all.quickQueryState.value = "";
	} else {
		document.all.queryArea.style.display = "none";
		document.all.quickQueryState.value = "display:none";
	}
}

function getFrameHeight() {
	var obj = document.all("msgFrame");
	if (obj == null || obj.style == undefined) return true;
	var formHeight = document.body.clientHeight;
	obj.style.height = formHeight - 210;
}

window.onresize = getFrameHeight

var hrefBaseValue = null;

//编辑器文件下载
function downloadFile(downloadSrc, uploadFileName) {
	var obj = document.getElementById("downloadLnk");
	if (hrefBaseValue == null) hrefBaseValue = obj.href;
	obj.href = hrefBaseValue + "?downloadSrc=" + downloadSrc + "&fileName=" + encodeURI(uploadFileName);
	obj.target = "_blank";
	obj.click();

}
//去除主机地址
function removeLocationAddress(html) {
	var hostAdd = window.location.protocol + "//" + window.location.host;
	var findSite = html.indexOf(hostAdd);
	while(findSite > -1) {
		html = html.substring(0, findSite) + html.substring(findSite + hostAdd.length);
		findSite = html.indexOf(hostAdd);
	}
	return html;
}
