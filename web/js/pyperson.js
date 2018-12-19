//------FUNCTION transinfor() add the input/select infor to display------------------
	
  var tempnum=1;
  var tempsum=1; 
	
	function transinfor(){			 	
		if(frm.conditionItem.value=="0"){
	       alert("璇烽�夋嫨鏉′欢瀛楁锛�");
	       return false;
	     } 	  
	     if(frm.limittype.value=="0"){
	       alert("璇烽�夋嫨闄愬埗绫诲埆锛�");
	       return false;
	     } 	   
	       
	     if(frm.gettext.value==""){
	       alert("璇疯緭鍏ユ潯浠跺��");
	       return false;
	     } 	      
	     
	     //-----------judge if has input the data-----------------  	   
	     for (v=1;v<=tempsum;v++) {
			var fieldName;	
			elementName=document.getElementById("selectField"+v);	
			if(elementName!=null){
				if(elementName.value==frm.conditionItem.value){
					alert("璇ユ煡璇㈡潯浠跺凡缁忓瓨鍦紒")
					return false;
				}			
			}
		 }
		 
		 //-----if all is right,then add the data------------------
	     addnum();
	     addtable();  
	 }
	 
	 
     function changeFieldMeta(){
     	frm.metaField.value=frm.conditionItem.options[frm.conditionItem.selectedIndex].text;     
     	//alert(frm.metaField.value);
     }
     
     function changeTypeMeta(){
     	var con;    
	    if(frm.limittype.value==(">=")){
	        con="澶т簬绛変簬"
	    }else if(frm.limittype.value==(">")){
	        con="澶т簬"
	    }else if(frm.limittype.value==("=")){
	        con="绛変簬"
	    }else if(frm.limittype.value==("<")){
	        con="灏忎簬"
	    }else if(frm.limittype.value==("<=")){
	        con="灏忎簬绛変簬"
	    }else if(frm.limittype.value==("<>")){
	        con="涓嶇瓑浜�"
	    }else if(frm.limittype.value==("like")){
	        con="璞�"
	    }   
	    
	    frm.metaType.value=con;   
     }
     
     
	 function addnum(){
		tempnum=tempnum+1;
		tempsum=tempsum+1;
	 }
	
	 function addtable(elId){
		addnum();

	    var htmlsrc;  	    	    
	    htmlsrc='<table id="inputNo'+tempsum+'" cellpadding="0" cellspacing="0" style="margin-bottom:0;margin-top:0">';
	    htmlsrc=htmlsrc+'<tr>';	   
		htmlsrc=htmlsrc+'<td><input type="text" name="displayOrder" size="10" />	</td>';
	    htmlsrc=htmlsrc+'<td><input type="text" name="department" size="20" /></td>';
	    htmlsrc=htmlsrc+'<td><input type="text" name="personName" size="15" />	</td>';
	    htmlsrc=htmlsrc+'<td><input type="text" name="descrip" size="50" />	<input type="button" value="del.." onclick="deltable('+tempsum+')" class="bt" /> </td>  ';  
	    htmlsrc=htmlsrc+'</tr>';	   
	    htmlsrc=htmlsrc+'</table>';    
	    //alert(tempsum);
	    //alert(elId);
	    
	    //htmlsrc='<br><input type="file" name="' + elId + tempsum +'" size="50">';
	    //newstyle.insertAdjacentHTML ("BeforeEnd",htmlsrc);
	    var htmlEl = document.getElementById(elId);
	    //alert(htmlEl);
	    insertHtml("beforeEnd",htmlEl,htmlsrc);
     }
	
	 function deltable(varid){
	 	
	     var tempname="inputNo"+''+varid;
	     //alert(tempname);
	     eval(tempname).outerHTML="";
	     tempsum=tempsum-1;     
     }
     
     /** Ext妗嗘灦婧愮爜涓殑insertHtml鏂规硶,璇ユ柟娉曞疄鐜颁簡鍏煎FF鐨刬nsertAdjacentHTML鏂规硶 
      *  @param where锛氭彃鍏ヤ綅缃�,鍖呮嫭beforeBegin,beforeEnd,afterBegin,afterEnd;
	  *  @param el锛氱敤浜庡弬鐓ф彃鍏ヤ綅缃殑html鍏冪礌瀵硅薄;
	  *  @param html锛氳鎻掑叆鐨刪tml浠ｇ爜. 
      */
     function insertHtml(where, el, html){
        where = where.toLowerCase();
        if(el.insertAdjacentHTML){
            switch(where){
                case "beforebegin":
                    el.insertAdjacentHTML('BeforeBegin', html);
                    return el.previousSibling;
                case "afterbegin":
                    el.insertAdjacentHTML('AfterBegin', html);
                    return el.firstChild;
                case "beforeend":
                    el.insertAdjacentHTML('BeforeEnd', html);
                    return el.lastChild;
                case "afterend":
                    el.insertAdjacentHTML('AfterEnd', html);
                    return el.nextSibling;
            }
            throw 'Illegal insertion point -> "' + where + '"';
        }
  		var range = el.ownerDocument.createRange();
        var frag;
        switch(where){
             case "beforebegin":
                range.setStartBefore(el);
                frag = range.createContextualFragment(html);
                el.parentNode.insertBefore(frag, el);
                return el.previousSibling;
             case "afterbegin":
                if(el.firstChild){
                    range.setStartBefore(el.firstChild);
                    frag = range.createContextualFragment(html);
                    el.insertBefore(frag, el.firstChild);
                    return el.firstChild;
                }else{
                    el.innerHTML = html;
                    return el.firstChild;
                }
            case "beforeend":
                if(el.lastChild){
                    range.setStartAfter(el.lastChild);
                    frag = range.createContextualFragment(html);
                    el.appendChild(frag);
                    return el.lastChild;
                }else{
                    el.innerHTML = html;
                    return el.lastChild;
                }
            case "afterend":
                range.setStartAfter(el);
                frag = range.createContextualFragment(html);
                el.parentNode.insertBefore(frag, el.nextSibling);
                return el.nextSibling;
            }
            throw 'Illegal insertion point -> "' + where + '"';
    }
	/***********/
     
	