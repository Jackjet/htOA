//------FUNCTION transinfor() add the input/select infor to display------------------
	
  var tempnum=0;
  var tempsum=0; 
	
	function transinfor(){			 	
		if(frm.conditionItem.value=="0"){
	       alert("��ѡ�������ֶΣ�");
	       return false;
	     } 	  
	     if(frm.limittype.value=="0"){
	       alert("��ѡ���������");
	       return false;
	     } 	   
	       
	     if(frm.gettext.value==""){
	       alert("����������ֵ");
	       return false;
	     } 	      
	     
	     //-----------judge if has input the data-----------------  	   
	     for (v=1;v<=tempsum;v++) {
			var fieldName;	
			elementName=document.getElementById("selectField"+v);	
			if(elementName!=null){
				if(elementName.value==frm.conditionItem.value){
					alert("�ò�ѯ�����Ѿ����ڣ�")
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
	        con="���ڵ���"
	    }else if(frm.limittype.value==(">")){
	        con="����"
	    }else if(frm.limittype.value==("=")){
	        con="����"
	    }else if(frm.limittype.value==("<")){
	        con="С��"
	    }else if(frm.limittype.value==("<=")){
	        con="С�ڵ���"
	    }else if(frm.limittype.value==("<>")){
	        con="������"
	    }else if(frm.limittype.value==("like")){
	        con="��"
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
	