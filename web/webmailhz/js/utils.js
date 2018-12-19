
/*cp list,param is $('listname')*/
function ListAdd(srcListId, destListId) {
    var srcList= document.getElementById(srcListId);//jq(srcListId);
    var destList= document.getElementById(destListId); //jq(destListId);
    
    //alert(srcList
    var len = destList.length;
    //alert(addressform.FromList.length);
    //alert(srcList.length);
    for(var i = 0; i < srcList.length; i++) {
        if ((srcList.options[i] != null) && (srcList.options[i].selected)) {
            var found = false;
            for(var count = 0; count < len; count++) {
                if (destList.options[count] != null) {
                    if (srcList.options[i].value == destList.options[count].value) {
                        found = true;
                        break;
                    }
                }
            }
            if (found != true) {
                destList.options[len] = new Option(srcList.options[i].text, srcList.options[i].value);
                len++;
            }
        }
    }
}
/*remove List selected items*/
function ListRemove(ListId) {
    var List= document.getElementById(ListId);//$(ListId);
    var len = List.options.length;
    for(var i = (len-1); i >= 0; i--) {
        if ((List.options[i] != null) && (List.options[i].selected == true)) {
            List.options[i] = null;
        }
    }
}

/**
function ListunSelectAll(List) {
        var destList=$(List);
        if (destList != null) {
                len = destList.options.length;
                for(var i = (len-1); i >= 0; i--) {
                        destList.options[i].selected = false;
                }
        }
}
function ListSelectAll(List){
        var destList=$(List);
        if (destList != null) {
                len = destList.options.length;
                for(var i = (len-1); i >= 0; i--) {
                        destList.options[i].selected = true;
                }
        }
}

function ListClear(List1){
	var selList=$(List1);
         for(var i = (selList.length-1); i >= 0; i--) {
             if (selList.options[i] != null) {
                      selList.options[i] = null;
            }
        }
}*/
