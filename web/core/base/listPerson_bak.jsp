<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<link href="<c:url value="/"/>css/jmesa/jmesa.css" rel="stylesheet" type="text/css" />
<link href="<c:url value="/"/>css/jmesa/jmesa-pdf.css" rel="stylesheet" type="text/css" />
<link href="<c:url value="/"/>css/form.css" rel="stylesheet" type="text/css" />
<script src="<c:url value="/"/>js/jmesa/jquery.jmesa.js" type="text/javascript"></script>
<script src="<c:url value="/"/>js/jmesa/jmesa.js" type="text/javascript"></script>
<script src="<c:url value="/"/>components/jquery.messager.js" type="text/javascript"></script>
<script src="<c:url value="/"/>js/commonFunction.js" type="text/javascript"></script>

<!-- formValidator -->
<link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css"></link>
<script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript" charset="UTF-8"></script>
<script src="<c:url value="/"/>components/formvalidator/js/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
<!-- ------------- -->

<script type="text/javascript">
	//验证
	$(document).ready(function(){
		//alert(${param.rowId});
		$.formValidator.initConfig({formid:"personForm",onerror:function(msg){alert(msg)},onsuccess:function(){return true;}});
		$("#personName").formValidator({onshow:"请输入姓名",onfocus:"员工姓名不能为空",oncorrect:"输入正确"}).inputValidator({min:1,onerror:"员工姓名不能为空,请确认"});
		$("#departmentId").formValidator({onshow:"请选择部门",onfocus:"部门必须选择",oncorrect:"选择正确",defaultvalue:"0"}).inputValidator({min:1,onerror: "请选择部门"});
		$("#positionLayer").formValidator({onshow:"请输入职级",onfocus:"只能输入阿拉伯数字,数值越小职级越高",oncorrect:"输入正确"}).inputValidator({min:1,max:99,type:"value",onerrormin:"输入的值必须大于等于1",onerror:"只能输入阿拉伯数字,请确认"});//.defaultPassed();
		$("#mobile").formValidator({empty:true,onshow:"请输入手机号码,可以为空",onfocus:"输入时要按手机格式",oncorrect:"输入正确"}).inputValidator({min:11,max:11,onerror:"手机号码必须是11位的,请确认"}).regexValidator({regexp:"mobile",datatype:"enum",onerror:"输入的手机号码格式不正确"});
		$("#email").formValidator({empty:true,onshow:"请输入邮箱,可以为空",onfocus:"邮箱6-100个字符",oncorrect:"输入正确",defaultvalue:"@"}).inputValidator({min:6,max:100,onerror:"输入的邮箱长度非法,请确认"}).regexValidator({regexp:"^([\\w-.]+)@(([[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.)|(([\\w-]+.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(]?)$",onerror:"输入的邮箱格式不正确"});
		$("#officePhone").formValidator({empty:true,onshow:"请输入办公室电话,可以为空",onfocus:"格式例如：021-888888",oncorrect:"输入正确"}).regexValidator({regexp:"^[[0-9]{3}-|\[0-9]{4}-]?([0-9]{8}|[0-9]{7})?$",onerror:"输入的电话格式不正确"});
		$("#homePhone").formValidator({empty:true,onshow:"请输入家庭电话,可以为空",onfocus:"格式例如：021-888888",oncorrect:"输入正确"}).regexValidator({regexp:"^[[0-9]{3}-|\[0-9]{4}-]?([0-9]{8}|[0-9]{7})?$",onerror:"输入的电话格式不正确"});
	});
	
	function onInvokeAction(id) {
	    setExportToLimit(id, '');
	    createHiddenInputFieldsForLimitAndSubmit(id);
	}
	//排序
	function onInvokeExportAction(id) {
	    var parameterString = createParameterStringForLimit(id);
	    location.href = '/core/personInfor.do?method=list' + parameterString;
	}
	//新增
	function doAdd(){
		form.action = '/core/personInfor.do?method=edit'
		createHiddenInputFieldsForLimitAndSubmit('jmesa','filter');
	}
	//修改
	function modify(id){
		form.action = '/core/personInfor.do?method=edit&personId=' + id;
		createHiddenInputFieldsForLimitAndSubmit('jmesa','filter');
	}
	//删除
	function remove(id){
		form.action = "/core/personInfor.do?method=delete&personId=" +id;
		createHiddenInputFieldsForLimitAndSubmit('jmesa','filter');
	}
	//注销或恢复
	function cancelOrResume(id){
		form.action = "/core/personInfor.do?method=cancelOrResume&personId=" +id;
		createHiddenInputFieldsForLimitAndSubmit('jmesa','filter');
	}
</script>

<title>企业人员</title>
	<form action="/core/personInfor.do?method=list" method="post" name="form">
		<div id="jmesa">
			<input type="hidden" name="pageNo" id="pageNo"/>
			<input type="hidden" name="pageSize" id="pageSize"/>
			<input type="hidden" name="orderBy" id="orderBy"/>
			<input type="hidden" name="order" id="order"/>
			${jmesaHtml}
		</div>
		<%--<div>
			<input type="button" value="新增" onclick="doAdd();"/>
		</div>--%>
	</form>
	
	<div>  
        <button onclick="openAdd()">添加人员</button>
    </div>  
          
    <div id="modalDlg">  
        <div id="formContainer" style="display: none;">  
               
           <%@include file="editPerson.jsp" %>
               
        </div>  
    </div>
        
    <script type="text/javascript"> 
        var addInfor = function() {
			var consoleDlg = $("#modalDlg");   
			$.ajax({  
			   url: '<c:url value="/core/personInfor.do"/>?method=save',	                    
	           data: $('#personForm').serialize(), //从表单中获取数据	                    
	           type:'POST', 
	           async: false,
			   error : function(textStatus, errorThrown) { 
			      alert("系统ajax交互错误: " + textStatus);   
			   },
			   success : function(data, textStatus) {
			     alert("信息添加成功!");		              
				 consoleDlg.dialog("close");
				 
				 //重新加载List
		         parent.main.location.reload();
			   }   			         
			});   
		};
		
		
        var updateInfor = function() {
			var consoleDlg = $("#modalDlg");   

 			$.ajax({
			   url: '<c:url value="/core/personInfor.do"/>?method=save',	                    
	           data: $('#personForm').serialize(), //从表单中获取数据                  
	           type:'POST', 
	           //dataType : "json", 
	           async: false,
			   error : function( textStatus, errorThrown) {
			     alert("系统ajax交互错误: " + textStatus);
			     //alert(data.errorMs);
			     //alert(consoleDlg.find("#personName").val);
			     
			   },
			   success : function(data, textStatus) { 
				 	/** if(data.status==0){
				 		parent.parent.parent.showMessage("",data.errorMs);
				 	} else{ */
					   alert("信息更新成功!");
		               consoleDlg.dialog("close");    
		               
					   //重新加载List
		               parent.main.location.reload();
	               /** } */
       		   }
    		});
		}
        
        jQuery().ready(function (){				
			//配置对话框   
			$("#modalDlg").dialog({   
				autoOpen: false,       
				modal: true,    //设置对话框为模态（modal）对话框   
				resizable: true,
				width: 750,
				buttons: {  	//为对话框添加按钮   
				   "创建": function() {if(jQuery.formValidator.pageIsValid()){addInfor();}},
				   "取消": function() {$("#modalDlg").dialog("close")},
				   "保存": function() {if(jQuery.formValidator.pageIsValid()){updateInfor();}}
				}   
			});  				
		});
		
		
        var openAdd = function() {
			$('#formContainer').css("display","block");//将编辑页面显示出来
			$('#departmentId').empty();	//清空部门下拉信息(多次打开后会造成重复载入,故打开前需要清空)
			$('#groupId').empty();		//清空班组下拉信息(多次打开后会造成重复载入,故打开前需要清空)
			var consoleDlg = $("#modalDlg");   
			var dialogButtonPanel = consoleDlg.siblings(".ui-dialog-buttonpane");  
			consoleDlg.find("input").removeAttr("disabled").val("");   
			dialogButtonPanel.find("button:not(:contains('取消'))").hide(); 
			dialogButtonPanel.find("button:contains('创建')").show(); 
			
			$.ajax({
				url: '<c:url value="/core/personInfor.do"/>?method=edit',
				dataType : "json",
				cache : false,   
				error : function(textStatus, errorThrown) {
					alert("系统ajax交互错误: " + textStatus);   
				},   
				success : function(data, textStatus) {
					//如果读取结果成功，则将信息载入到对话框中                    
				    $("#departmentId").append("<option value='0'>--选择部门--</option>");
				    if (data.departments != null) {
				    	for (var i=0;i<data.departments.length;i++) {
					    	$("#departmentId").append("<option value='"+data.departments[i].organizeId+"'>"+data.departments[i].organizeName+"</option>");
					    }
				    }
				    $("#groupId").append("<option value='0'>--选择班组--</option>");
				    if (data.groups != null) {
				    	for (var i=0;i<data.groups.length;i++) {
					    	$("#groupId").append("<option value='"+data.groups[i].organizeId+"'>"+data.groups[i].organizeName+"</option>");
					    }
				    }
				    //打开对话框   
				    consoleDlg.dialog("option", "title", "创建人员").dialog("open"); 
				}
			});
		}; 
		
		var openUpdate = function(id) {
			$('#formContainer').css("display","block");//将编辑页面显示出来
			$('#departmentId').empty();	//清空部门下拉信息(多次打开后会造成重复载入,故打开前需要清空)
			$('#groupId').empty();		//清空班组下拉信息(多次打开后会造成重复载入,故打开前需要清空)
			var consoleDlg = $("#modalDlg");   
			var dialogButtonPanel = consoleDlg.siblings(".ui-dialog-buttonpane");   
			consoleDlg.find("input").removeAttr("disabled");   
			dialogButtonPanel.find("button:not(:contains('取消'))").hide();   
			dialogButtonPanel.find("button:contains('保存')").show(); 
			
			var params = {
				"personId" : id
			}; 
			
			$.ajax({
				url: '<c:url value="/core/personInfor.do"/>?method=edit',
				data : params,
				dataType : "json",
				cache : false,   
				error : function(textStatus, errorThrown) {
					alert("系统ajax交互错误: " + textStatus);   
				},   
				success : function(data, textStatus) {
					//如果读取结果成功，则将信息载入到对话框中
				    var person = data.person; 
				    $("#departmentId").append("<option value='0'>--选择部门--</option>");
				    if (data.departments != null) {
				    	for (var i=0;i<data.departments.length;i++) {
					    	$("#departmentId").append("<option value='"+data.departments[i].organizeId+"'>"+data.departments[i].organizeName+"</option>");
					    }
				    }
				    $("#groupId").append("<option value='0'>--选择班组--</option>");
				    if (data.groups != null) {
				    	for (var i=0;i<data.groups.length;i++) {
					    	$("#groupId").append("<option value='"+data.groups[i].organizeId+"'>"+data.groups[i].organizeName+"</option>");
					    }
				    }
				         
				    consoleDlg.find("#personId").val(person.personId);   
				    consoleDlg.find("#personName").val(person.personName);   
				    consoleDlg.find("#personNo").val(person.personNo);
				    consoleDlg.find("#gender").val(person.gender);
				    consoleDlg.find("#departmentId").val(person.departmentId);
				    consoleDlg.find("#groupId").val(person.groupId);
				    consoleDlg.find("#positionLayer").val(person.positionLayer);
				    consoleDlg.find("#mobile").val(person.mobile);
				    consoleDlg.find("#email").val(person.email);
				    consoleDlg.find("#birthday").val(person.birthday);
				    consoleDlg.find("#officeAddress").val(person.officeAddress); 
				    consoleDlg.find("#officePhone").val(person.officePhone); 
				    consoleDlg.find("#officeCode").val(person.officeCode); 
				    consoleDlg.find("#homeAddress").val(person.homeAddress); 
				    consoleDlg.find("#homePhone").val(person.homePhone); 
				    consoleDlg.find("#postCode").val(person.postCode); 
				    consoleDlg.find("#memo").val(person.memo); 
				    
				    //打开对话框   
				    consoleDlg.dialog("option", "title", "修改人员信息").dialog("open");
				}
			});
		};
			
	</script>
