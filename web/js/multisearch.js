		
		/** 自定义多条件查询 */
		//初始化查询窗口
		function initSearchDialog() {

		    $(multiSearchParams[1]).dialog({
		        autoOpen: false,       
		        modal: true,
		        resizable: true,
                width: 350,
				height:300,
		        title: "多条件查询",
		        buttons: {   
		            "查询": multipleSearch,
		            "重置": clearSearch
		        }   
		    });
		}
		
		//打开查询窗口
	    function openMultipleSearchDialog() {
		    //初始化窗口
		    initSearchDialog();
		    
		    $(multiSearchParams[1]).dialog("open");
		}
		
		//多条件查询
		function multipleSearch() {

		    var rules = "";   
		    $("tbody tr", multiSearchParams[1]).each(function(i){    	//(1)从multipleSearchDialog对话框中找到各个查询条件行
		        var searchField = $(".searchField", this).val();    	//(2)获得查询字段

                // console.info(searchField);
		        var searchOper = $(".searchOper", this).val();  		//(3)获得查询方式

				// console.info(searchOper);
		        var searchString = $(".searchString", this).val();  	//(4)获得查询值   
		        
		        if(searchField && searchOper && searchString) { 		//(5)如果三者皆有值且长度大于0，则将查询条件加入rules字符串
		            rules += ',{"field":"' + searchField + '","op":"' + searchOper + '","data":"' + searchString + '"}';   
		        }
		    });   
		    if(rules) { 
		        rules = rules.substring(1);								//(6)如果rules不为空，且长度大于0，则去掉开头的逗号
		    }
		       
		    var filtersStr = '{"groupOp":"AND","rules":[' + rules + ']}';//(7)串联好filtersStr字符串
		       
		    var postData = $(multiSearchParams[0]).jqGrid("getGridParam", "postData");   
		       
		    $.extend(postData, {filters: filtersStr});   				//(8)将filters参数串加入postData选项
		       
		    $(multiSearchParams[0]).jqGrid("setGridParam", {  
		        search: true    										//(9)将jqGrid的search选项设为true   
		    }).trigger("reloadGrid", [{page:1}]);   					//(10)重新载入Grid表格,且返回第一页  
		       
		    $(multiSearchParams[1]).dialog("close");
		}
		
		// 多条件查询----打印
		function printStr(flowId) {
			var rules = "";
			$("tbody tr", multiSearchParams[1]).each(function(i){ // (1)从multipleSearchDialog对话框中找到各个查询条件行
				var searchField = $(".searchField", this).val(); // (2)获得查询字段
				var searchOper = $(".searchOper", this).val(); // (3)获得查询方式
				var searchString = $(".searchString", this).val(); // (4)获得查询值
				if (searchField && searchOper && searchString) { // (5)如果三者皆有值且长度大于0，则将查询条件加入rules字符串
					rules += ',{"field":"' + searchField + '","op":"' + searchOper + '","data":"' + searchString + '"}';
				}
			});
			if (rules) {
				rules = rules.substring(1); // (6)如果rules不为空，且长度大于0，则去掉开头的逗号
				//alert(rules);
			}
			var filtersStr = '{"groupOp":"AND","rules":[' + rules + ']}';// (7)串联好filtersStr字符串
			var postData = $(multiSearchParams[0]).jqGrid("getGridParam", "postData");
			$.extend(postData, {filters : filtersStr}); // (8)将filters参数串加入postData选项
			$(multiSearchParams[0]).jqGrid("setGridParam", {search : true// (9)将jqGrid的search选项设为true
				}).trigger("reloadGrid", [{page : 1 }]); // (10)重新载入Grid表格,且返回第一页
			//alert(filtersStr);
			window.open("/workflow/instanceInfor.do?method=excel&flowId="+flowId+"&sidx=instanceId&sord=desc&_search=true&filters=" + filtersStr, "_blank");
		}
		
		
		//重置查询条件
		function clearSearch() {
		    var sdata = {
		        searchString: ""	//将查询数据置空
		    };   
		       
		    var postData = $("#gridTable").jqGrid("getGridParam", "postData");   
		       
		    $.extend(postData, sdata);   
		       
		    $(multiSearchParams[0]).jqGrid("setGridParam", {   
		        search: false  
		    }).trigger("reloadGrid", [{page:1}]);   
		       
		    resetSearchDialog(); 
		}
		var resetSearchDialog = function() {
		    $("select",multiSearchParams[1]).val("");   
		    $(":text",multiSearchParams[1]).val("");   
		}

		/** ********** */