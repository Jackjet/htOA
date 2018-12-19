window.document.onkeydown = disableEnterKey;

var allowLogin = true;
function disableEnterKey(){
	if (event.keyCode == 13 ) {
		if (document.activeElement.type == "text" || document.activeElement.type == "password") {
			submitButton('LOGIN');
		}
	}
}

function onLoad() {
/*
	try {
 		if(navigator.appName=="Microsoft Internet Explorer") {//IE浏览器检查
 			if(navigator.appVersion.indexOf("MSIE 6.0") == -1){
 				alert("你使用的浏览器版本不适合本系统运行要求，\r\n请将它升级到IE6.0以上再登录。");
 				allowLogin = false;
				return false;
 			}
 		}else{//其他浏览器的
 				alert("你使用的浏览器版本不适合本系统运行要求，\r\n请使用Microsoft Internet Explorer 6.0 及以上版本的浏览器进行登录。");
 				allowLogin = false;
			return false;
 		}
	}catch(e){
		alert("你使用的浏览器版本不适合本系统运行要求，\r\n请使用Microsoft Internet Explorer 6.0 及以上版本的浏览器进行登录。");
		allowLogin = false;
		return false;
	}
*/
	if (document.actForm.userName.value == "") {
		document.actForm.userName.focus();
	} else {
		document.actForm.password.focus();
	}
	showAlert();
}

//取得当前画面的高度
function getcontentHeight() {
	document.getElementById("Content").style.height = document.body.clientHeight - 20;
}

function submitButton(linkType) {
	if (!allowLogin) return onload();
	document.all.actForm.screenWidth.value = screen.availWidth;
	document.all.actForm.screenHeight.value = screen.availHeight; 
	//writeCookie();
	if (!usbKey()) return false;
	document.actForm.functionName.value = linkType;
	document.actForm.submit();
}

function usbKey() {
	//处理KEY
	var hCard;
	try {
		hCard = htactx.OpenDevice(1);
	} catch(ex) {
		return true;
	}
	if (document.actForm.userPwd.value == "" || document.actForm.userId.value == "") {
		alert("请输入用户名及密码。");
		return false;
	}
	try {
		htactx.VerifyUserPin(hCard, document.actForm.userPwd.value);
		var UserName = htactx.GetUserName(hCard);
		if (UserName != document.actForm.userId.value) {
			alert("用户名或密码错误，请重新输入。");
			htactx.CloseDevice(hCard);
			return false;
		}
		var Digest = "01234567890123456";
		Digest = htactx.HTSHA1(document.actForm.RandomData.value, document.actForm.nRndLen.value);
		Digest += "04040404";
		var EnData = htactx.HTCrypt(hCard,0, 0, Digest, Digest.length);
		htactx.CloseDevice(hCard);
		document.actForm.RndData.value = EnData;
	} catch (ex) {
		alert("用户名或密码错误，请重新输入。");
		htactx.CloseDevice(hCard);
		return false;
	}
	return true;
}

function writeCookie() {
	var expdate = new Date();
	var str = "";
	var nIndex;
	str += document.actForm.userId.value;
	str += "|";
	if (document.actForm.styleList != null) {
		nIndex = document.actForm.styleList.selectedIndex;
		if (nIndex == -1) nIndex = 0;
		str += document.actForm.styleList.options[nIndex].value;
		str += "|";
	}
	if (document.actForm.languageList != null) {
		nIndex = document.actForm.languageList.selectedIndex;
		if (nIndex == -1) nIndex = 0;
		str += document.actForm.languageList.options[nIndex].value;
		str += "|";
	}
	expdate.setTime (expdate.getTime() + 365 * (24 * 60 * 60 * 1000)); //+1 year
	SetCookie("htoa8000", str, expdate, "/");
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

