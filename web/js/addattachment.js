//------FUNCTION transinfor() add the input/select infor to display------------------
	
  var tempnum=1;
  var tempsum=1; 
	
	function transinfor(){			 	
		if(frm.conditionItem.value=="0"){
	       alert("请选择条件字段！");
	       return false;
	     } 	  
	     if(frm.limittype.value=="0"){
	       alert("请选择限制类别！");
	       return false;
	     } 	   
	       
	     if(frm.gettext.value==""){
	       alert("请输入条件值");
	       return false;
	     } 	      
	     
	     //-----------judge if has input the data-----------------  	   
	     for (v=1;v<=tempsum;v++) {
			var fieldName;	
			elementName=document.getElementById("selectField"+v);	
			if(elementName!=null){
				if(elementName.value==frm.conditionItem.value){
					alert("该查询条件已经存在！")
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
	        con="大于等于"
	    }else if(frm.limittype.value==(">")){
	        con="大于"
	    }else if(frm.limittype.value==("=")){
	        con="等于"
	    }else if(frm.limittype.value==("<")){
	        con="小于"
	    }else if(frm.limittype.value==("<=")){
	        con="小于等于"
	    }else if(frm.limittype.value==("<>")){
	        con="不等于"
	    }else if(frm.limittype.value==("like")){
	        con="象"
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
	    htmlsrc='<table id="inputNo" cellpadding="0" cellspacing="0" style="margin-bottom:0;margin-top:0">';
	    htmlsrc=htmlsrc+'<tr><td>';	   
	    htmlsrc=htmlsrc+'<input type="file" name="' + elId + tempsum +'" size="50">';	    
	    htmlsrc=htmlsrc+'</td></tr>';	   
	    htmlsrc=htmlsrc+'</table>';    
	    //alert(htmlsrc);
	    //alert(htmlEl);
	    
	    htmlsrc='<br><input type="file" name="' + elId + tempsum +'" size="50">';
	    //newstyle.insertAdjacentHTML ("BeforeEnd",htmlsrc);
	    var htmlEl = document.getElementById(elId);
	    insertHtml("beforeEnd",htmlEl,htmlsrc);
     }
     
     /**
      * 
      * @param {} elId   注入到的ID
      * @param {} na     新加的文本域的name
      * @param {} cl     新加的文本域的class
      */
     function addpic(elId,na,cl){
		//addnum();

	    var htmlsrc;  	    	    
	    htmlsrc='<table id="inputNo" cellpadding="0" cellspacing="0" style="margin-bottom:0;margin-top:0">';
	    htmlsrc=htmlsrc+'<tr><td>';	   
	    htmlsrc=htmlsrc+'<input type="file" name="' + na  +'" class="'+ cl +'" size="50">';	    
	    htmlsrc=htmlsrc+'</td></tr>';	   
	    htmlsrc=htmlsrc+'</table>';    
	    //alert(htmlsrc);
	    //alert(htmlEl);
	    
	    htmlsrc='<br><input type="file" name="' + na  +'" class="'+ cl +'" size="50">';
	    //newstyle.insertAdjacentHTML ("BeforeEnd",htmlsrc);
	    var htmlEl = document.getElementById(elId);
	    //alert(htmlEl+"--"+htmlsrc);
	    insertHtml("beforeEnd",htmlEl,htmlsrc);
     }
	
	 function deltable(varid){
	     var tempname="inputNo"+''+varid;
	     eval(tempname).outerHTML="";
	     tempsum=tempsum-1;     
     }
     
     /** Ext框架源码中的insertHtml方法,该方法实现了兼容FF的insertAdjacentHTML方法 
      *  @param where：插入位置,包括beforeBegin,beforeEnd,afterBegin,afterEnd;
	  *  @param el：用于参照插入位置的html元素对象;
	  *  @param html：要插入的html代码. 
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
     
	