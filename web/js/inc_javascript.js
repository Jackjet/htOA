  document.write( "<script language= 'javascript' src='/js/commonFunction.js'></script>");
  
  function getOptsValue(sel,str)
    {
	//alert(sel+str);    
	//alert(sel.length);
	//alert(str);
     for (var opts=0; opts<sel.length; ++opts) {
          if (sel.options[opts].value==str)
            {
		//alert(sel.options[opts].value);
            sel.options[opts].selected=true;
            return true;
            }
        }
     return false;
   }
   
   
   //设置相关参数时候，临时显示总值
   function changeTotalValue(form,detailItemName,totalItemName){
		//alert("-----------");		
		var objTotal;
		var totalValue;
		objTotal = document.getElementById(totalItemName);
		totalValue=0;
	
		//共几人
		var objTotalPerson;
		objTotalPerson = document.getElementById("lab0");
		
		var objDetail;
		var objLength;
		objDetail = document.getElementsByName(detailItemName);
		if(objDetail != null){
			objLength = objDetail.length;
			//alert(objLength);
			if (objLength == null){
				objTotal.innerHTML = objDetail.value;
				objTotalPerson.innerHTML = "1";
				
			}else{
				for (var k=0;k<objLength;k++){
					if(is_number(objDetail[k].value))
						totalValue = totalValue + parseFloat(objDetail[k].value);
				}
				
				objTotalPerson.innerHTML = objLength;
			}
			
		}else{
			objTotalPerson.innerHTML = "0";
		}
		totalValue = FormatNumber(totalValue,2);
		objTotal.innerHTML = totalValue;
	}
	
	
   function FormatNumber(srcStr,nAfterDot){ 
		var srcStr,nAfterDot; 
		var resultStr,nTen; 
		srcStr = ""+srcStr+""; 
		strLen = srcStr.length; 
		dotPos = srcStr.indexOf(".",0); 
		if (dotPos == -1){ 
			resultStr = srcStr+"."; 
			for (i=0;i<nAfterDot;i++){ 
				resultStr = resultStr+"0"; 
			} 
			return resultStr; 
		}else{ 
			if ((strLen - dotPos - 1) >= nAfterDot){ 
				nAfter = dotPos + nAfterDot + 1; 
				nTen =1; 
				for(j=0;j<nAfterDot;j++){ 
					nTen = nTen*10; 
				} 
				resultStr = Math.round(parseFloat(srcStr)*nTen)/nTen; 
				return resultStr; 
			}else{ 
				resultStr = srcStr; 
				for (i=0;i<(nAfterDot - strLen + dotPos + 1);i++){ 
					resultStr = resultStr+"0"; 
				} 
				return resultStr; 
			} 
		} 
	} 


	
	//部门/班组联动,form为调用页面form对象
	function changeDepartment(form)  {	    
	    var departmentId;
	    departmentId = form.departmentId.options[form.departmentId.selectedIndex].value;
	    //alert(locationid);
	    form.groupId.length = 1; 	    
	   
	    //alert(1111);
	    for (var i=0;i < onecount; i++) {       
	       	if(subcat[i][1]!="" && departmentId!=""){
	        		//alert(parseInt(subcat[i][1]) == parseInt(locationid));
		            if (parseInt(subcat[i][1]) == parseInt(departmentId))   { 
		                form.groupId.options[form.groupId.length] = new Option(subcat[i][0], subcat[i][2]);
		            } 
	            }       
	        }	    
    }
    
    //部门/班组联动,form为调用页面form对象
	function changePersonDepartment(form)  {	    
	    var personDepartmentId;
	    personDepartmentId = form.personDepartmentId.options[form.personDepartmentId.selectedIndex].value;
	    //alert(locationid);
	    form.personGroupId.length = 1; 	    
	   
	    //alert(1111);
	    for (var i=0;i < onecount; i++) {       
	       	if(subcat[i][1]!="" && personDepartmentId!=""){
	        		//alert(parseInt(subcat[i][1]) == parseInt(locationid));
		            if (parseInt(subcat[i][1]) == parseInt(personDepartmentId))   { 
		                form.personGroupId.options[form.personGroupId.length] = new Option(subcat[i][0], subcat[i][2]);
		            } 
	            }       
	        }	    
    }
    
    	
	//初始化调整页面大
	function winfix() {
		var width;
		var height;
		if (document.layers) {	
			width=screen.availWidth-10;  	
			height=screen.availHeight-20; 	
		} else {
			width=screen.availWidth-2; 
			height=screen.availHeight;  
		}
		//alert(width);
		//alert(height);
		self.resizeTo(width, height); 
		self.moveTo(0, 0); 
	} 
	
	
	//根据日期型字符串(2006-07-12)比较两个日期大?
	function isBeginAfterEnd(dayFrom,dayTo){
		var iFrom=Date.parse(dayFrom.substr(5,2) +  "-" + dayFrom.substr(8,2) + "-" + dayFrom.substr(0,4));
		var iTo=Date.parse(dayTo.substr(5,2) +  "-" + dayTo.substr(8,2) + "-" + dayTo.substr(0,4));
		if(iFrom>iTo){
		      return true;
		}
	   	
	   	return false;	
	}	
    
   
   //显示一个提示层   
   function displayTip(tipId,content){
   		alert('123456');
   	  //var tip;
   	  //tip = document.getElementById(tipId);
	  //tip.style.visibility="visible";
	  //tip.style.left = document.documentElement.scrollLeft + event.clientX;
	  //tip.style.top = document.documentElement.scrollTop + event.clientY;
	  //tip.innerHTML = content;
	}
	
	
	function getElementsByName(tag,eltname){ 
		var elts=document.getElementsByTagName(tag); 		
		var count=0; 		
		var elements=[]; 		
		for(var i=0;i<elts.length;i++){ 		
		     if(elts[i].getAttribute("name")==eltname){ 		
		        elements[count++]=elts[i]; 		
		     } 		
		} 
		
		return elements; 	
	} 
	
	
	//点击显示tr信息
	function show_list(objNum) {
		number=0;
		var tr;
		var img;
		var trs = getElementsByName('tr','tr');
		var imgs = getElementsByName('img','img');
		
		//alert(trs);
		//alert(trs.length);
		if (trs.length==null){
			tr = trs;
			img = imgs;
		}else{
			tr = trs[objNum];
			img = imgs[objNum];
		}
		
		if(clickTimes[objNum]%2==0){
			tr.style.display="";
			img.src="../images/xpcollapse3_s.gif"
		}else{				
			tr.style.display="none";
			img.src="../images/xpexpand3_s.gif";
		}	
		
		clickTimes[objNum] += 1;								
	}
	
	//选择某个权限中的全部人员
	function selectAll(controlbox,checkbox,rightbox){
		var isChecked = false;
		if(controlbox.checked){
			isChecked = true;
		}
		//当只有一个人员时
		//此人员按钮
		if (rightbox.length == null) {
			if (isChecked) {
				rightbox.checked = true;
			}else {
				rightbox.checked = false;
			}
		}else {
			//当有多个人员时
			//各个人员按钮
			if (isChecked) {
				for(var i = 0;i<rightbox.length;i++){
					rightbox[i].checked = true;
				}
			}else {
				for(var i = 0;i<rightbox.length;i++){
					rightbox[i].checked = false;
				}
			}
		}
		//各个全选按钮
		if (isChecked) {
			for(var i = 0;i<checkbox.length;i++){
				checkbox[i].checked = true;
			}
		}else {
			for(var i = 0;i<checkbox.length;i++){
				checkbox[i].checked = false;
			}
		}
	}
	
	//进入添加页面时自动选上"添加"和"浏览"权限
	function initRight(params){
		var viewUserIds = document.getElementsByName(params[0]);
		var editUserIds = document.getElementsByName(params[1]);
		var browseCheckBox = document.getElementsByName(params[2]);
		var addCheckBox = document.getElementsByName(params[3]);
		for(var i = 0;i<viewUserIds.length;i++){
			viewUserIds[i].checked = true;
			editUserIds[i].checked = true;
		}
		for(var j = 0;j<browseCheckBox.length;j++){
			browseCheckBox[j].checked = true;
			addCheckBox[j].checked = true;
		}
	}
	
	//显示附件信息
	function showAttachment(attachment,contextPath) {
		var returnAttachment = "";
		if (attachment!=null && attachment!=""){
			attachment = attachment.Trim();
			var filePaths = attachment.split("\|");
			for(var k =0; k<filePaths.length;k++){
				if (k!=0) returnAttachment += "&nbsp;";
				
				var tempFile = filePaths[k];
				
				if (tempFile.length > 0) {
					returnAttachment += "<a style=\"text-decoration:none\" href=\"" + contextPath + "/common/download.jsp?filepath=";				
					//alert(URLEncode(tempFile));
					returnAttachment += URLEncode(tempFile);
					returnAttachment += " \" target=\"_blank\">";
					
					var fileName = "";				
					var pos = tempFile.lastIndexOf("/");
					if (pos>0){
						fileName = tempFile.substring(pos+1);
					}
					
					returnAttachment += "<img alt=\"" + fileName + "\" width=\"22\" height=\"20\"  border=\"0\" src=\"" + contextPath + "/images/";
					//根据后缀显示不同的图片׺
					var suffix="";
					pos = fileName.lastIndexOf(".");
					if(pos>0){
						suffix = fileName.substring(pos+1);
					}
					if(suffix!=""){
						suffix = suffix.toUpperCase();
						if(suffix == "DOC"){
							returnAttachment += "word.png\"";
						}else if(suffix == "PDF"){
							returnAttachment += "pdf.png\"";
						}else if(suffix == "XLS"){
							returnAttachment += "excel.png\"";
						}else if(suffix == "TXT"){
							returnAttachment += "txt.png\"";
						}else{
							returnAttachment += "unknow.png\"";
						}
					}
					returnAttachment += ">";
					
					//printStr += fileName;
					returnAttachment += "</a>";
				}
			}
		}
		
		return returnAttachment;
	}
	
	//字符串转换为utf-8编码
	function URLEncode(Str){
		if(Str==null||Str==""){
		    return "";
		}
		var newStr="";
		function toCase(sStr){
		    return sStr.toString(16).toUpperCase();
		}
		for(var i=0,icode,len=Str.length;i<len;i++){
		    icode=Str.charCodeAt(i);
		    if(icode<0x10){
		      newStr+="%0"+icode.toString(16).toUpperCase();
		    }else if(icode<0x80){
		      if(icode==0x20){
		        newStr+="+";
		      }else if((icode>=0x30&&icode<=0x39)||(icode>=0x41&&icode<=0x5A)||(icode>=0x61&&icode<=0x7A)){
		        newStr+=Str.charAt(i);
		      }else {
		        newStr+="%"+toCase(icode);
		      }
		    }else if(icode<0x800){
		      newStr+="%"+toCase(0xC0+(icode>>6));
		      newStr+="%"+toCase(0x80+icode%0x40);
		    }else{
		      newStr+="%"+toCase(0xE0+(icode>>12));
		      newStr+="%"+toCase(0x80+(icode>>6)%0x40);
		      newStr+="%"+toCase(0x80+icode%0x40);
		   }
		}
		return newStr;
	}

