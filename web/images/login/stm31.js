var alreadyOpenTableId = null;
var hrefBaseValue = null;

function closeMenu() {
	if (alreadyOpenTableId == null) return;
	alreadyOpenTable = document.all(alreadyOpenTableId);
	targetTableId = alreadyOpenTableId + "d";
	targetTable = document.all(targetTableId);
	targetTable.style.display = "none";
	alreadyOpenTableId = null;
}

function menuClick(tableSrc) {
	var currentTableSrc = tableSrc;
	if (currentTableSrc.id != alreadyOpenTableId) closeMenu();
    targetTableId = currentTableSrc.id+"d";
    targetTable = document.all(targetTableId);
    if (targetTable.style.display == "none") {
        targetTable.style.display = '';
		 		alreadyOpenTableId = currentTableSrc.id;
    } else {
        targetTable.style.display = "none";
		}
}

function subMenuClick(tableSrc) {
    subTableId = tableSrc.id+"d";
    subTable = document.all(subTableId);
	var tableSrcImgId = tableSrc.id + "_img";
	var tableSrcImg = document.all(tableSrcImgId);
	if (subTable != null) {
	    if (subTable.style.display == "none") {
	        subTable.style.display = '';
	        tableSrcImg.src = SubImgOpen;
	    } else {
	        subTable.style.display = "none";
	        tableSrcImg.src = SubImg;
	   }
	}
}

function actionPage(urlValue, urlName) {
	if (urlValue == "") return false;
	document.all.pageName.value = urlName;
	if (urlValue.indexOf("act") > -1) {
		var linkEle = document.getElementById(urlValue.substring(3));
		if (linkEle != null) {
			if (linkEle.href == "") {
				alert(errMsg);
				return true;
			}
			linkEle.click();
		    return true;
		}
	}
	if (hrefBaseValue == null) hrefBaseValue = document.all.autoEnter.href
	document.all.autoEnter.href = hrefBaseValue + "?PAGEID=" + urlValue;
	document.all.autoEnter.click();
}

function st_onload() {
	return;
}
