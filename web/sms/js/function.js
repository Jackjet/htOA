//------FUNCTION transinfor() add the input/select infor to display------------------
	
  var tempnum=0;
  var tempsum=0; 
	
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
	
					
									
	
	 
	
	function deltable(varid){    
	      var tempname="inputNo"+''+varid;
	      eval(tempname).outerHTML="";
	      tempsum=tempsum-1;     
    }
	