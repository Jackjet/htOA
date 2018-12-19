<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<script type="text/javascript">
    $(function(){
        $(window).resize(function(){
            $mygrid.setGridWidth($(window).width());
        });
    });
    //新增
	function addInfor(){

		/*var returnArray = window.showModalDialog("/cms/${param.urlPath}.do?method=edit&categoryId="+${param.categoryId},'',"dialogWidth:800px;dialogHeight:600px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		if(returnArray[0] == "refresh") {
			//保存信息后重新加载tab
			loadTab(returnArray[1]);
		}*/
		window.open("/cms/${param.urlPath}.do?method=edit&categoryId="+${param.categoryId},"_blank");
	}
	
	//修改
	function editInfor(rowId){
		/*var returnArray = window.showModalDialog("/cms/${param.urlPath}.do?method=edit&rowId="+rowId+"&categoryId="+${param.categoryId},'',"dialogWidth:800px;dialogHeight:600px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		//alert(returnArray[0]);
		if(returnArray[0] == "refresh") {
			//保存信息后重新加载tab
			loadTab(returnArray[1]);
		}*/
		<%--window.open("/cms/${param.urlPath}.do?method=edit&isRootCategory="+${param.isRoot}+"&rowId="+rowId+"&categoryId="+${param.categoryId},"_blank");--%>
		window.open("/cms/${param.urlPath}.do?method=edit&rowId="+rowId+"&categoryId="+${param.categoryId},"_blank");
	}
	
	//授权
	function doAuthorize(rowId){
		/*var returnArray = window.showModalDialog("/cms/${param.urlPath}.do?method=editInforRight&rowId="+rowId+"&categoryId="+${param.categoryId},'',"dialogWidth:800px;dialogHeight:600px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		if(returnArray[0] == "refresh") {
			//保存信息后重新加载tab
			loadTab(returnArray[1]);
		}*/
        window.open("/cms/${param.urlPath}.do?method=editInforRight&rowId="+rowId+"&categoryId="+${param.categoryId},"_blank");
	}
	
	//查看静态页面
	function viewInfor(htmlFilePath){
		window.open(<%=request.getContextPath()%>htmlFilePath,'','');
	}
</script>

<title></title>
<body style="border:1px solid #0DE8F5;border-radius:0 5px 5px 5px;">
  		<div>
			<table id="list${param.categoryId}"></table>
			<div id="pager${param.categoryId}"></div>
		</div>
		
		<!-- 查询框 -->
		<div id="multiSearchDialog${param.categoryId}" style="display: none;">  
		    <table>
		        <tbody> 
		            <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="inforTitle"/><cms:displayTitle categoryId="${param.categoryId}" fieldName="inforTitle"/>：
		                    <input type="hidden" class="searchOper" value="cn"/>
		                </td>  
		                <td>  
		                    <input type="text" class="searchString" size="25"/>  
		                </td>  
		            </tr>
		            <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="issueUnit"/><!--<cms:displayTitle categoryId="${param.categoryId}" fieldName="issueUnit"/>-->发布单位：
		                    <input type="hidden" class="searchOper" value="cn"/>
		                </td>  
		                <td>  
		                    <input type="text" class="searchString"/>
		                </td>  
		            </tr>
		            <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="keyword"/><!--<cms:displayTitle categoryId="${param.categoryId}" fieldName="keyword"/>-->关键字：
		                    <input type="hidden" class="searchOper" value="cn"/>
		                </td>  
		                <td>  
		                    <input type="text" class="searchString"/>
		                </td>  
		            </tr>
		            <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="createTime"/>起始时间：
		                    <input type="hidden" class="searchOper" value="ge"/>
		                </td>  
		                <td>  
		                    <input class="searchString" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly="true" size="12"/>
		                </td>  
		            </tr>
		            
		            <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="createTime"/>结束时间：
		                    <input type="hidden" class="searchOper" value="le"/>
		                </td>  
		                <td>  
		                    <input class="searchString" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly="true" size="12"/>
		                </td>  
		            </tr>
		            <tr>  
		                <td>  
		                    <input type="hidden" class="searchField" value="author.personId"/>提交者：
		                    <input type="hidden" class="searchOper" value="eq"/>
		                </td>  
		                <td>  
		                    <select id="departmentId${param.categoryId}"></select>
		                    <select class="searchString" id="personId${param.categoryId}"></select>
		                </td>  
		            </tr>
		            
		            <!-- /*****荣誉室时*****/ -->
					<c:if test="${param.urlPath == 'honorroom'}">
						<tr>  
			                <td>  
			                    <input type="hidden" class="searchField" value="kind"/>荣誉类别：
			                    <input type="hidden" class="searchOper" value="eq"/>
			                </td>  
			                <td>  
			                    <select class="searchString" id="kind">
			                    	<option value="">--选择类别--</option>
									<option value="上海市">上海市</option>
									<option value="集团">集团</option>
									<option value="安吉物流">安吉物流</option>
									<option value="客户">客户</option>
									<option value="行业协会">行业协会</option>
									<option value="其他">其他</option>
			                    </select>
			                </td>  
			            </tr>
			            
			            <tr>  
			                <td>  
			                    <input type="hidden" class="searchField" value="honorKind"/>荣誉类型：
			                    <input type="hidden" class="searchOper" value="eq"/>
			                </td>  
			                <td>  
			                    <select class="searchString" id="honorKind">
									<option value="">--选择荣誉类别--</option>
									<option value="集团荣誉">集团荣誉</option>
									<option value="个人荣誉（合同工）">个人荣誉（合同工）</option>
									<option value="个人荣誉（中介工）">个人荣誉（中介工）</option>
									<option value="其他">其他</option>
			                    </select>
			                </td>  
			            </tr>
					</c:if>
		        </tbody>  
		    </table>  
		</div>
		<!-- ----- -->
</body>
		<script type="text/javascript"> 		
			
			//自定义操作栏的显示内容
		    function formatOperation(cellvalue, options, rowdata) {
	           var returnStr = "<a href='javascript:;' onclick='editInfor("+options.rowId+")'>[修改]</a>";
	           returnStr += " <a href='javascript:;' onclick='doAuthorize("+options.rowId+")'>[授权]</a> <a href='javascript:;' onclick='viewInfor(\""+cellvalue+"?inforId="+options.rowId+"\")'>[查看]</a>"
	           return returnStr;
		    }
		    
		    //自定义显示boolean型内容
		    function formatBol(cellvalue) {
	           var returnStr;
	           if (cellvalue) {
	              returnStr = "<font color='red'>是</font>";
	           }else {
	              returnStr = "否";
	           }
	           return returnStr;
		    }
            //自定义显示信息内容型内容
            function formatTitle(cellvalue) {

				//            <img hspace="400" src="http://htoa.haitongauto.com:80/upload/xheditor/20161027/image/bac4803c-d931-40ef-9e22-28d8116f0e55.jpg" width="300" alt="" />
				var value = cellvalue.toString();

//		        value = value.replace("src","id");
		        var val = value.substr(0,120);
                var result="<span title='"+value+"'>"+val+"</span>";
//                console.info(result)
                return result;
            }
		    
		    //自定义显示附件
		    function formatAttach(cellValue, options, rowObject) {			
				var html = '';
				html = showAttachment(cellValue,'');				
				return html;
			}
		    
		    //获取该分类下需要显示的字段信息
			var col_names = [];
			var col_model = [];
		    $.ajax({
				url: "/cms/inforDocument.do?method=getColData&categoryId="+${param.categoryId},
				cache: false,
				async: false,
				type: "GET",
				dataType: "json",
				success : function (data) {
					if (data != null) {
					    	//列名
					    	$.each(data.names, function(i,n){
					    		col_names[i] = n;
					    	});
					    	
					    	//列数据
					    	$.each(data.model, function(i,n){
					    		if (i == 0) {
					    			col_model[i] = eval("({name:'"+n+"',index:'"+n+"', sorttype:'int', search:false, key:true, hidden:true})");
					    		}else {
					    			if (n == 'important' || n == 'defBool1') {
					    				//boolean型
					    				col_model[i] = eval("({name:'"+n+"',index:'"+n+"', align:'center', width:'70px', formatter:formatBol})");
					    			}else if (n == 'inforTitle' ) {
					    				//标题,内容
					    				col_model[i] = eval("({name:'"+n+"', align:'center',width:'150px',align:'center',index:'"+n+"',formatter:formatTitle})");
					    			}else if ( n == 'inforContent') {
                                        //标题,内容
                                        col_model[i] = eval("({name:'"+n+"', align:'center',width:'150px',align:'center',index:'"+n+"',formatter:formatTitle})");
                                    }else if (n == 'author.person.personName') {
                                        //发布人
                                        col_model[i] = eval("({name:'"+n+"', align:'center',width:'60px',align:'center',index:'"+n+"'})");
                                    }else if (n == 'attachment' || n == 'defaultPicUrl') {
					    				//附件,图片
					    				col_model[i] = eval("({name:'"+n+"',width:'35px',align:'center',index:'"+n+"', formatter:formatAttach})");
					    			}else {
					    				col_model[i] = eval("({name:'"+n+"',width:'50px',index:'"+n+"', align:'center'})");
					    			}
					    		}
					    	});
				    }
				}
			});
			var index = col_names.length;
			
			/*****荣誉室时*****/
			if('${param.urlPath}' == 'honorroom'){
				col_names[index] = "荣誉类别";
				col_model[index] = eval("({name:'kind', width:'80px',index:'kind', align:'center'})");

				col_names[index + 1] = "荣誉类型";
				col_model[index + 1] = eval("({name:'honorKind', width:'80px',index:'honorKind', align:'center'})");
				
				col_names[index + 2] = "相关操作";
				col_model[index + 2] = eval("({name:'htmlFilePath', align:'center', search:false, sortable:false, formatter:formatOperation})");
			}else{
				col_names[index] = "相关操作";
				col_model[index] = eval("({name:'htmlFilePath', align:'center', search:false, sortable:false, formatter:formatOperation})");
			}
			
			
		    
			//加载表格数据
			var $mygrid = jQuery("#list"+${param.categoryId}).jqGrid({
                url:"/cms/inforDocument.do?method=getInforDocument&categoryId="+${param.categoryId},
                rownumbers: true,
                datatype: "json",                
               	autowidth: true,
				autoHeight:false,
                height:document.documentElement.clientHeight-240,
                colNames: col_names,
                colModel: col_model,
                sortname: 'createTime',
                sortorder: 'desc',
                multiselect: true,	//是否支持多选,可用于批量删除
                viewrecords: true,
                rowNum: 10,
				fit:false,
                rowList: [10,20,30],
                scroll: false,
                scrollrows: false,
                jsonReader:{
                   repeatitems: false
                },         
                pager: "#pager"+${param.categoryId}
	        }).navGrid('#pager'+${param.categoryId},{edit:false,add:false,del:false,search:false}).closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });

			//自定义按钮
			if (${param.isRoot != 'true'}) {
				//为根分类时不显示
				jQuery("#list"+${param.categoryId}).jqGrid('navButtonAdd','#pager'+${param.categoryId}, {
					caption:"新增", title:"点击新增信息", buttonicon:'ui-icon-plusthick', onClickButton: addInfor
				});
			}
			jQuery("#list"+${param.categoryId}).jqGrid('navButtonAdd','#pager'+${param.categoryId}, {
				caption:"<span style='color: red;'>批量删除</span>", title:"点击批量删除", buttonicon:'ui-icon-closethick', onClickButton: deleteInfor
			});
			jQuery("#list"+${param.categoryId}).jqGrid('navButtonAdd','#pager'+${param.categoryId}, {
				caption:"查询", title:"点击查询", buttonicon:'ui-icon-search', onClickButton: openDialog
			});
			

			/*****荣誉室时*****/
			if('${param.urlPath}' == 'honorroom'){
				jQuery("#list"+${param.categoryId}).jqGrid('navButtonAdd','#pager'+${param.categoryId}, {
					caption:"导出Excel", title:"点击导出到Excel", buttonicon:'ui-icon-calculator', onClickButton: exportExcel
				});
			}
			
			/********导出excel*********/
			function exportExcel(){
				var yes = window.confirm("数据较多时，导出所需时间较长，确定要导出数据吗？");
				if (yes) {
					var rules = "";   
					var param0 = "#list"+${param.categoryId};
					var param1 = "#multiSearchDialog"+${param.categoryId};
				    $("tbody tr", param1).each(function(i){    	//(1)从multipleSearchDialog对话框中找到各个查询条件行   
				        var searchField = $(".searchField", this).val();    	//(2)获得查询字段
				        var searchOper = $(".searchOper", this).val();  		//(3)获得查询方式   
				        var searchString = $(".searchString", this).val();  	//(4)获得查询值   
				        
				        if(searchField && searchOper && searchString) { 		//(5)如果三者皆有值且长度大于0，则将查询条件加入rules字符串   
				            rules += ',{"field":"' + searchField + '","op":"' + searchOper + '","data":"' + searchString + '"}';   
				        }   
				    });   
				    if(rules) { 
				        rules = rules.substring(1);								//(6)如果rules不为空，且长度大于0，则去掉开头的逗号
				    }   
				       
				    var filtersStr = '{"groupOp":"AND","rules":[' + rules + ']}';//(7)串联好filtersStr字符串
				    
				    var url = "/cms/inforDocument.do?method=expertExcel&categoryId="+${param.categoryId}+"&_search=true&page=1&rows=100000&sidx=inforId&sord=desc&filters="+filtersStr;
				    window.location.href=url;
				}
				
			}
			
			//打开查询窗口并进行窗口初始化
			var multiSearchParams = new Array();
			function openDialog() {
//                $mygrid.setGridWidth($(window).width());
			    multiSearchParams[0] = "#list"+${param.categoryId};				//列表Id
				multiSearchParams[1] = "#multiSearchDialog"+${param.categoryId};//查询模态窗口Id
				//alert(${param.categoryId});
				
				initSearchDialog();
				
			    $(multiSearchParams[1]).dialog("open");
			}
			
			//批量删除
			function deleteInfor(){
				doDelete("/cms/${param.urlPath}.do?method=delete","list"+${param.categoryId});
			}
			
			/** 查询条件中的部门,班组,用户下拉联动 */
			//部门信息初始化
			$('#departmentId'+${param.categoryId}).selectInit();
			
			//加载部门及联动信息		
			$.loadDepartments("departmentId"+${param.categoryId}, null, "personId"+${param.categoryId});
			/** ******** */
			
			//保存信息后重新加载tab
			function loadTab(categoryId){
				$.ajax({
					url: "inforRightList.jsp?urlPath=${param.urlPath}&categoryId="+categoryId,
					cache: false,
					type: "GET",
					dataType: "html",
					beforeSend: function (xhr) {
					},
					complete : function (req, err) {
						$("#tabs-"+categoryId).empty().html(req.responseText);
					}
				});		
			}
			
		</script>