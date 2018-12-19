	//Global Button Operation Function
	/**
	CreateScript("../components/jquery/jquery.messager.js");
	
	var head=document.getElementsByTagName('mainnav').item(0);
	function CreateScript(file){
	    var new_element;
	    new_element=document.createElement("script");
	    new_element.setAttribute("type","text/javascript");
	    new_element.setAttribute("src",file);
	    void(head.appendChild(new_element));
	}
	*/
	
	//Show Modal Dialog
	/**
		<div class="ui-dialog ui-widget ui-widget-content ui-corner-all undefined ui-draggable ui-resizable">
		   <div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">
		      <span id="ui-dialog-title-dialog" class="ui-dialog-title">Dialog title</span>
		      <a class="ui-dialog-titlebar-close ui-corner-all" href="#"><span class="ui-icon ui-icon-closethick">close</span></a>
		   </div>
		   <div style="height: 200px; min-height: 109px; width: auto;" class="ui-dialog-content ui-widget-content" id="dialog">
		      <p>Dialog content goes here.</p>
		   </div>
		</div>
	*/
	function showMsgModal(title,content){		
		$("<div></div>").html(content).dialog({
				bgiframe: true,
				modal: true,
				title: title,
				buttons: {
						Ok: function() {
							$(this).dialog('close');
							$(this).remove();
						}
					}
		});	
		
		var objTitle = $("#ui-dialog-title-dialog");
		//alert(objTitle);		
		//objTitle.style.color = "blue";
		//objTitle.removeClass('ui-dialog-title');
		//objTitle.addClass('dialog-title');
		//objTitle.css('fontSize',20);
		objTitle.addClass('dialog-title');
	}
	
	
	//show custom message
	function showSystemMessage(title,content,height,width,times){
		//alert(22);
		var mWidth;
		
		if(width==0)
			mWidth = document.body.clientWidth;
		else
			mWidth = width;	
		
		$.messager.lays(mWidth, height);
		//$.messager.lays(1000, 70);
		
		//alert(44);
		/**
		if(!times || times != '')
		{
			times = 2000;
		} */
		
		//alert(33);
		$.messager.show(title, content, times);		
	}

	/**
	 * �жϱ?�����Ƿ��޸�
	 * 
	 * @param frm
	 *            �?����	
	 * @return
	 */

	function isFormChanged(frm) {   
		var isChanged = false;   
	        var form = frm;   
		for (var i = 0; i < form.elements.length; i++) {   
			var element = form.elements[i];   
			var type = element.type;   
						
			if (type == "text" || type == "hidden" || type == "textarea" || type == "button") {   
				if (element.value != element.defaultValue) {   
					//alert(1111);
					isChanged = true;   
					break;   
				}   
			} else if (type == "radio" || type == "checkbox") {   
				if (element.checked != element.defaultChecked) {   
					//alert(2222);
					isChanged = true;   
					break;   
				}   
			} else if (type == "select-one"|| type == "select-multiple") {   
				for (var j = 0; j < element.options.length; j++) {   
					//alert(element.options[j].defaultSelected);
					//alert(element.options[j].selected);
					if (element.options[j].selected != element.options[j].defaultSelected) {   
						//alert(3333);
						isChanged = true;   
						break;   
					}   
				}   
			} else {    
				//  etc...   
			}        
		}
					           
		//alert( isChanged?"change the value.":"dont change value.");   
		return isChanged;   
	}   

	
	/**
	 * ȫѡ
	 * 
	 * @param nameStr
	 *            ��ѯ��name��checkbox
	 * @param obj
	 *            ��ǰȫѡ��checkbox�Ķ���
	 * @return
	 */
	function checkAll(nameStr, obj) {
		var allLine = $("input[name='" + nameStr + "']");
		allLine.each(function() {
			if (obj.checked) {
				if (!$(this).attr("checked")) {
					$(this).attr("checked", obj.checked);
					$(this).triggerHandler("click");
				}
			}
			if (!obj.checked) {
				if ($(this).attr("checked")) {
					$(this).attr("checked", obj.checked);
					$(this).triggerHandler("click");
				}
			}
		});
	}
	
	/**
	 * �������option��append��Select��-����
	 * @param content
	 * 		���飬��-����������
	 * @param to
	 * 		select��jquery���󣬴���4�ľ���$("#id")
	 * @return
	 */
	function addOption(content,to)
	{
		if(content && content.length)
		{
			for ( var i = 0; i < content.length; i++) 
			{
				var option = "<option value='" + content[i].code + "'>"
						+ content[i].descp + "</option>";
				to.append(option);
			}
		}
	}

	
	/**
	 * ͨ�õ�ȡ��-���ֵcode��descģʽ�����ҽ�ȡ������ݷŵ���Ӧ��id����
	 * @param params
	 * 		��-�б����Դ��sql�ļ��ж�Ӧ��id�ţ��˴����Դ��ݶ���ö��ŷָ�
	 * @param toObjArr
	 * 		���ص�data�����(map)���ĸ�keyֵ��ӦҪ�ŵ��ĸ�jquery��select������
	 * @return
	 * 		û�з���ֵ
	 * 	e.g.
	 * 		���ط��õ�һ�����Դ����ȡһ�����Դ���ŵ�table��ÿһ�е���-����
	 * 		getDataAndAddOption("findAllCGJZC",{"findAllCGJZC":[$("#JZ_JZC")]});
	 * 		һ��Է��õ�һ�����Դ
	 * 		getDataAndAddOption("findAllCGJZC",{"findAllCGJZC":$("#JZ_JZC")});
	 * 
	 * 		���ط��õ�������ȡ���������Դ���ŵ�form�Ĳ�ͬ����-����
	 * 		getDataAndAddOption("findAllCGJZC,findAllStores",{"findAllCGJZC":$("#JZ_JZC"),"findAllStores":$("#JZ_STORE")});
	 * 		����ĳ�����ԴҪ�ŵ����ط�
	 * 		getDataAndAddOption("findAllCGJZC,findAllStores",{"findAllCGJZC":[$("#JZ_JZC")],"findAllStores":]$("#JZ_STORE")]});
	 * 
	 */
	function getDataAndAddOption(params,toObjArr)
	{	
		$.get("getCodeDescMap.do",{"sqlIds":params},function(data)
		{
			for(var key in data)
			{
				if(toObjArr[key].length && toObjArr[key].length > 1){
					toObjArr[key].each(function(){
						addOption(data[key],$(this));
					});
				}else{
					addOption(data[key],toObjArr[key]);
				}
					
			}			
		});
	}
	
	/**
	 * �õ�code��Descģ�͵Ķ���
	 * @param params
	 * 		getCodeDescData("findAllCGJZC,findAllStores")
	 * 		getCodeDescData("findAllCGJZC")	
	 * ����data��
	 * 		{"findAllCGJZC":[{"code":"desc"},{"code1":"desc1"}]}
	 */
	function getCodeDescData(params){
		
		$.get("getCodeDescMap.do",{"sqlIds":params},function(data)
		{
			return data;		
		});
	}
	

	/**
	 * ���code�������е�CodeDescModel����õ�desc����
	 * ����磺company = [{code:111,descp:223},{code:323,descp:13}]
	 * ��company��ݿ���ͨ��  getCodeDescData("findAllCGJZC").findAllCGJZC  	�õ�
	 * @param companyCode
	 */
	function getDesc(arrCodeDescObj,code){
		var desc;
		$.each(arrCodeDescObj,function(idx,content){
			if(content.code == companyCode){
				desc = content.descp;
				return;
			}
		});
		return desc;
	}
	
	/**
	 * 
	 * @param formObj
	 * 		��Ҫ�ύ����̨��formԪ��
	 * @param tableSelectRow
	 * 		table��ѡ���У���ѡ��checkbox����û��id������name���ԣ�����input���Ա��붼��id
	 * 		��ѡ��valueֵ�����������ÿ��input id����ֵ
	 * @return
	 * 		�ύ����̨�����
	 * e.g.
	 * 	var data = getFormValue($("form[name=credenceForm] :text"),$("input[name='hasCheckd']:checked"));
	 */
	function getFormValue(formObj,tableSelectRow)
	{
		var postData = {};//�����ύ�����
		
		//ѭ���ύ��form����
		if(formObj && formObj != '')
		{
			formObj.each(function(){
				var name = $(this).attr("id");
				var value = $(this).val();
				postData[name] = value;
			});
		}
		//ѭ��tableѡ����
		if(tableSelectRow && tableSelectRow != '')
		{
			var c = 0;	
			$.each(tableSelectRow,function(i){
				c = i + 1;
				var allInput = $(this).parent().parent().find("input");
				var allSelect = $(this).parent().parent().find("select");
				console.log(allSelect);
				if(allSelect && allSelect.length > 0)
				{
					allSelect.each(function(){
						postData[$(this).attr("id")] = $(this).val();
					});
				}
				if(allInput && allInput.length > 0)
				{
					allInput.each(function(){
						if(!$(this).attr("id"))
						{
							postData[c] = $(this).val();
						}else
						{
							postData[$(this).attr("id")] = $(this).val();
							//postData[$(this).attr("id")] = $(this).find('option:selected').text();
						}
					});
				}
			});
			postData.postCount = c;
		}
		return postData;
	
	}
	
	function getCurrentDay(){
		var d = new Date();
		var vYear = d.getFullYear();
		var vMon = d.getMonth() + 1;
		var vDay = d.getDate();
		vMon = vMon<10 ? "0" + vMon : vMon;
		vDay = vDay<10 ?  "0"+ vDay : vDay;
		return vYear+'-'+vMon+'-'+vDay;
	}
	
	//Trim函数
	String.prototype.Trim = function() { return this.replace(/(^\s*)|(\s*$)/g, ""); } 
	
	//LTrim函数
	String.prototype.LTrim = function() { return this.replace(/(^\s*)/g, ""); }  
	
	//RTrim函数
	String.prototype.RTrim = function() { return this.replace(/(\s*$)/g, ""); }
	
	//重新设置弹出窗口的大小
	function resetDialogHeight(){
	  //alert(window.dialogArguments);
	  if(window.dialogArguments == null){
	    return; //忽略非模态窗口
	   }
	
	  var ua = navigator.userAgent;
	  var height = document.body.offsetHeight;
	  var width = document.body.offsetWidth;
	  //alert(ua);
	  //alert(document.body.offsetHeight);
	  //alert(document.body.clientHeight);
	  if(ua.lastIndexOf("MSIE") != -1){
	  	 window.dialogHeight=(height+100)+"px";
	  	 window.dialogWidth = (width+200)+"px";
	  	 /**
		  if(ua.lastIndexOf("Windows NT 5.1") != -1){
		    alert("xp.ie6.0");
		    var height = document.body.offsetHeight;
		     window.dialogHeight=(height+102)+"px";
		   }
		  else if(ua.lastIndexOf("Windows NT 5.0") != -1){
		    //alert("w2k.ie6.0");
		    var height = document.body.offsetHeight;
		     window.dialogHeight=(height+49)+"px";
		   }*/
		}
	}	
	
	
	//部门,班组,用户下拉联动 
	//下拉数据初始化
	$.fn.selectInit = function(){return $(this).html("<option value=''>请选择</option>");};
	
	//加载部门及联动信息
	$.loadDepartments = function(departmentId, groupId, userId) {
		//加载部门数据
		$.getJSON("/core/organizeInfor.do?method=getDepartments",function(data) {
			if (data != null) {
			    $.each(data._Departments, function(i, n) {
				    $("#"+departmentId).append("<option value='"+ n.organizeId + "'>" + n.organizeName + "</option>");
				});
			}
		});
		
		if ($("#"+groupId).html() != null) {
			//含下拉班组信息时,改变部门信息,联动班组信息
			$("#"+departmentId).bind("change",function(){
	           $("#"+groupId).selectInit();
	           var depSelectId = $("#"+departmentId+" option:selected").val();
	           var groupUrl = "/core/organizeInfor.do?method=getGroups&departmentId=" + depSelectId;
	           $.loadGroups(groupUrl, groupId);
			});
				
			//含下拉班组信息时,改变班组信息,联动用户信息
			$("#"+groupId).bind("change",function(){
		       $("#"+userId).selectInit();
		       var groSelectId = $("#"+groupId+" option:selected").val(); 
		       var userUrl = "/core/systemUserInfor.do?method=getUsers&groupId=" + groSelectId;
		       $.loadUsers(userUrl, userId);
			});
		}else {
			//不含下拉班组信息时,改变部门信息,联动用户信息
			$("#"+departmentId).bind("change",function(){
	            $("#"+userId).selectInit();
	            var depSelectId = $("#"+departmentId+" option:selected").val();
	            var url = "/core/systemUserInfor.do?method=getUsers&departmentId=" + depSelectId;
	            $.loadUsers(url, userId);                    
			});
		}
	}
		 
	//获取班组信息
	$.loadGroups = function(url, groupId) {
		$.getJSON(url,function(data) {
			if (data != null) {
		 		$.each(data._Groups, function(i, n) {
			 		$("#"+groupId).append("<option value='"+ n.organizeId +"'>" + n.organizeName + "</option>");
			 	});
		 	}
		});
	}
		 
	//获取用户信息
	$.loadUsers = function(url, userId) {
		$.getJSON(url,function(data) {
			if (data != null) {
				$.each(data._Users, function(i, n) {
					$("#"+userId).append("<option value='"+ n.personId + "'>" + n.person.personName + "</option>");
				});
			}
		});
	}
	/** **************** */
	

	datePick = function(elem) {   
		$.datepicker.regional['zh-CN'] = {clearText: '清除', clearStatus: '清除已选日期',
					closeText: '关闭', closeStatus: '不改变当前选择',
					prevText: '&lt;上月', prevStatus: '显示上月',
					nextText: '下月&gt;', nextStatus: '显示下月',
					currentText: '今天', currentStatus: '显示本月',
					monthNames: ['一月','二月','三月','四月','五月','六月',
					'七月','八月','九月','十月','十一月','十二月'],
					monthNamesShort: ['一','二','三','四','五','六',
					'七','八','九','十','十一','十二'],
					monthStatus: '选择月份', yearStatus: '选择年份',
					weekHeader: '周', weekStatus: '年内周次',
					dayNames: ['星期日','星期一','星期二','星期三','星期四','星期五','星期六'],
					dayNamesShort: ['周日','周一','周二','周三','周四','周五','周六'],
					dayNamesMin: ['日','一','二','三','四','五','六'],
					dayStatus: '设置 DD 为一周起始', dateStatus: '选择 m月 d日, DD',
					dateFormat: 'yy-mm-dd', firstDay: 1, 
					initStatus: '请选择日期', isRTL: false};
					
		$.datepicker.setDefaults($.datepicker.regional['zh-CN']);					
		jQuery(elem).datepicker({dateFormat:"yy-mm-dd"});	
	}
		
	function doDelete(url, listId){
		//获取选择的行的Id
		var rowIds = jQuery("#"+listId).jqGrid('getGridParam','selarrrow'); 
			
		if(rowIds != null && rowIds.length > 0){
			var yes = window.confirm("确定要删除吗？");
			if (yes) {
				$.ajax({
					url: url+"&rowIds="+rowIds,	//删除数据的url
					cache: false,
					type: "POST",
					dataType: "html",
					beforeSend: function (xhr) {						
					},
						
					complete : function (req, err) {
						alert("数据已经删除！");
						$("#"+listId).trigger("reloadGrid"); 
					}
				});	
			}
		}else {
			alert("请选择要删除的数据！");
		}		
	}
	
	//格式化日期
	function formatDate(dateData) {
		var today = new Date();
		
		//获取本周日的日期
		var this_day = today.getDay(); 	//今天是这周的第几天
		var step_m = 7 - this_day; 		//周日距离今天的天数（负数表示）
		var thisTime = today.getTime();
		var sunday = new Date(thisTime + step_m*24*3600*1000);
		var sunDate = new Date(sunday.getFullYear() + '/' + (sunday.getMonth()+1) + '/' + sunday.getDate()); //转换为'yyyy-MM-dd'格式
		
		//获取本周一的日期
		var monday = new Date(thisTime - (this_day-1)*24*3600*1000);
		var monDate = new Date(monday.getFullYear() + '/' + (monday.getMonth()+1) + '/' + monday.getDate()); //转换为'yyyy-MM-dd'格式
		
		//格式化日期
		var dateArray = new Array();
		dateArray = dateData.split('-');
		var compDate = new Date(dateArray[0]+ '/' + dateArray[1] + '/' + dateArray[2]); //转换为'yyyy-MM-dd'格式
		
		//获取日期对应的星期
		var time = new Date(dateArray[0]+'/'+dateArray[1]+'/'+dateArray[2]);
		var day = time.getDay();
		var week = dateData;
		if (compDate-sunDate <= 0 && compDate-monDate > -691200000) {
			//显示本周内及上周的日期
			var todDate = new Date(today.getFullYear() + '/' + (today.getMonth()+1) + '/' + today.getDate()); //转换为'yyyy-MM-dd'格式
			var tDays = parseInt(Math.abs(compDate-todDate)/1000/60/60/24); //计算日期与系统时间相差的天数
			if (tDays == 0) {
				week = "今天";
			}else if (tDays == 1) {
				if (compDate-todDate < 0) {
					week = "昨天";
				}else {
					week = "明天";
				}
			}else if (tDays == 2) {
				if (compDate-todDate < 0) {
					week = "前天";
				}else {
					week = "后天";
				}
			}else if (tDays > 2) {
				var tmpWeek = "";
				if (compDate-monDate < 0) {
					tmpWeek += "上";
				}
				if (day == 0) {
					tmpWeek += "周日";
				}else if (day == 1) {
					tmpWeek += "周一";
				}else if (day == 2) {
					tmpWeek += "周二";
				}else if (day == 3) {
					tmpWeek += "周三";
				}else if (day == 4) {
					tmpWeek += "周四";
				}else if (day == 5) {
					tmpWeek += "周五";
				}else if (day == 6) {
					tmpWeek += "周六";
				}
				week = tmpWeek;
			}
		}else if (compDate-sunDate > 0 && compDate-sunDate <= 604800000) {
			//显示下周的日期
			if (day == 0) {
				week = "下周日";
			}else if (day == 1) {
				week = "下周一";
			}else if (day == 2) {
				week = "下周二";
			}else if (day == 3) {
				week = "下周三";
			}else if (day == 4) {
				week = "下周四";
			}else if (day == 5) {
				week = "下周五";
			}else if (day == 6) {
				week = "下周六";
			}
		}
		return week;									
	}
	
	/**
	*JQGrid树形结构，折叠与展开的处理
	*@param rowid:点击行的Key值
	*/
	function resetNode(rowid){//alert('#' + rowid + '> td[aria-describedby=west-grid_leftIndex]');
						var leftIndexStr = $('#' + rowid + '> td[aria-describedby=west-grid_leftIndex]').attr("title");
			            var rightIndexStr = $('#' + rowid + '> td[aria-describedby=west-grid_rightIndex]').attr("title");
			            var layerStr = $('#' + rowid + '> td[aria-describedby=west-grid_layer]').attr("title");
			            var leftIndex = parseInt(leftIndexStr);
			            var rightIndex = parseInt(rightIndexStr);			          		            
			            var layer = parseInt(layerStr);
			          			      
			          			            
			            $('#west-grid tr').each(function(){
			             	//alert($(this > td[aria-describedby=west-grid_layer]));
			             	
			             	//alert($("td[aria-describedby=west-grid_layer]", $(this)).attr("title"));
			             	 
			             	var tempLeftIndexStr = $("td[aria-describedby=west-grid_leftIndex]", $(this)).attr("title"); 			             	
			             	var tempLayerStr = $("td[aria-describedby=west-grid_layer]", $(this)).attr("title");
			            	//alert(tempLeftIndexStr);
			            	//alert(tempLayerStr);
			            				            	
			            	if(tempLayerStr!=null && tempLayerStr!=''){
			            		var tempLayer = parseInt(tempLayerStr);
			            		var tempLeftIndex = parseInt(tempLeftIndexStr);
			            		
			            		var currentNode = $(this);
			            		
			            		if(tempLayer>layer && tempLeftIndex>leftIndex && tempLeftIndex<rightIndex){			            	
				            		if(tempLayer==layer+1){
					            		var style = currentNode.attr('style');	
					            		//alert(style);		            	
						            	if(style=='DISPLAY: block' || style=='display: block;' || style==null || style==''){
						            		currentNode.attr('style','display: none;');
						            	}else{
						            		currentNode.attr('style','DISPLAY: block;');
						            	}	
						            }else{
						            	/**
						            	*当前点击节点，子节点之后的节点
						            	*首先找到其父节点，即leftIndex<tempLeftIndex且rightIndex>tempLeftIndex的节点
						            	*/
						            	$('#west-grid tr').each(function(){
						            		var liStr =  $("td[aria-describedby=west-grid_leftIndex]", $(this)).attr("title");
								            var riStr =  $("td[aria-describedby=west-grid_rightIndex]", $(this)).attr("title");
								            var laStr = $('#' + rowid + '> td[aria-describedby=west-grid_layer]').attr("title");
								            var li = parseInt(leftIndexStr);
								            var ri = parseInt(rightIndexStr);		
								            var la = parseInt(laStr);
								            
								            if(li<tempLeftIndex && ri>tempLeftIndex){
								            	var style = $(this).attr('style');		
								            	//alert(style);	            	
								            	if(style=='display: none;' || style=='DISPLAY: none'){
								            		//如果父为隐藏，则本节点也要隐藏
								            		currentNode.attr('style','display: none;');
								            	}	
								            }
						            	})						            	
						            }	         
				            	}   	
			            	}	
			             });
		   
		   }
	
	/**
 * 日期对比(如果a>=b，则返回true)
 * @param a
 * @param b
 * @param tag(1->,2->=,3-=,4-<=,5-<)
 * @returns {Boolean}
 */
function dateCompare(a, b ,tag) {
    var arr = a.split("-");
    var starttime = new Date(arr[0], arr[1]-1, arr[2]);
    var starttimes = starttime.getTime();

    var arrs = b.split("-");
    var endtime = new Date(arrs[0], arrs[1]-1, arrs[2]);
    var endtimes = endtime.getTime();

	if(tag == 1){
		if (starttimes > endtimes) {
	        return true;
	    } else {
	        return false;
		}
	}else if(tag == 2){
		if (starttimes >= endtimes) {
	        return true;
	    } else {
	        return false;
		}
	}else if(tag == 3){
		if (starttimes = endtimes) {
	        return true;
	    } else {
	        return false;
		}
	}else if(tag == 4){
		if (starttimes <= endtimes) {
	        return true;
	    } else {
	        return false;
		}
	}else if(tag == 5){
		if (starttimes < endtimes) {
	        return true;
	    } else {
	        return false;
		}
	}else{
		return false;
	}
	
    
}
	