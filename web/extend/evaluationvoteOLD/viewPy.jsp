<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<title>评优投票信息</title>
<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
<script src="<c:url value="/"/>datePicker/WdatePicker.js" type="text/javascript"></script>
<script src="<c:url value="/"/>js/inc_javascript.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/css/form.css"/>"/>

<!-- formValidator -->
<link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css"></link>
<script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript" charset="UTF-8"></script>
<!--<script src="<c:url value="/"/>components/formvalidator/js/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>

--><script>
	
	$(document).ready(function(){
		//验证
		//$.formValidator.initConfig({formid:"itemInforForm",onerror:function(msg){alert(msg)},onsuccess:function(){submitData();}});
		//$("#itemName").formValidator({onshow:"请输入投票标题",onfocus:"投票标题不能为空",oncorrect:"输入正确"}).inputValidator({min:1,onerror:"投票标题不能为空,请确认"});
		
	});
	var isArray = false;
	//提交数据
	function submitData() {
		
		if(check()){
			var form = document.pyVoteInforForm;
			form.submit();
			alert('评优投票成功！');
			//window.returnValue = "refresh";
			//window.close();
		}
	}
	
	   
 	var radioArr = new Array();   
    function isCheck(obj){   
        var hasCheck = false;   
           
        for(var i=0; i<radioArr.length; i++){   
            if(radioArr[i]==obj){   
                radioArr[i].checked = false;   
                radioArr.splice(i,1); //移除对象   
                hasCheck = true;   
                break;   
           }   
        }   
  
       if(!hasCheck){   
            initRadioArr();   
        }   
       //alert(radioArr.join("-"));   
    }   
    //初始化选中的radio   
   function initRadioArr(){   
       radioArr = new Array();   
 
        //可根据实际情况修改radio的范围。   
        var radios = document.getElementsByTagName("input");   
       for(var i=0; i<radios.length; i++){   
            if(radios[i].type.toLowerCase()=="radio" && radios[i].checked){   
                radioArr.push(radios[i]);   
            }   
        }   
    }   
    
    
    //查看某个人的投票信息
	function fillData(){
		if("${!empty _VoteInfor}" == 'true'){
		
			<c:forEach items="${_Topic.personInfors}" var="person" varStatus="index">
			var personId = "${person.PId}";
			<c:forEach items="${_Topic.itemInfors}" var="item" varStatus="index">
				//判断题型
				var itemType = "${item.itemType}";
				var itemId = "${item.itemId}";
				
			
					<c:if test="${!empty _VoteInfor.voteItemInfors}">
						
						<c:forEach items="${_VoteInfor.voteItemInfors}" var="voteItem">
							<c:if test="${voteItem.itemInfor.itemId == item.itemId &&  voteItem.personInfor.PId == person.PId}">
								<c:forEach items="${fn:split(voteItem.voteValue,',')}" var="voteValue">
    								var voteValue = "${voteValue}";
    								if(itemType == "0"){
    								
    									$(":radio[name='option_"+itemId+"_"+personId+"'][value='"+voteValue+"']").attr("checked","true");
    								}
    								if(itemType == "1"){
    									$(":checkbox[name='option_"+itemId+"_"+personId+"'][value='"+voteValue+"']").attr("checked","true");
    								}
	    						</c:forEach>
							</c:if>
						</c:forEach>
					</c:if>
				
			</c:forEach>
			</c:forEach>
		}
	}	  
              
	
	//判断表单内容是否为空
	function check(){
		var flag = true;
		
	
		
			
			
				<c:forEach items="${_Topic.itemInfors}" var="item" varStatus="index">
					var itemId = "${item.itemId}";
					//alert();
					<c:forEach items="${item.optionInfors}" var="option" varStatus="index">
					var optionId= ${option.optionId};
					var num=${option.selectNum};
					
					
					var i=0;
					
					if(num>0){
						$(".option_"+optionId+"").each(function () {               
							
							//alert($(this).attr("checked"));//
							 if($(this).attr("checked")){
							 i=i+1;
							 }
						
						 });
						if(i>num){
						
							alert("选项【${option.optionName}】最多只能选择"+num+"个");
							return false;
						}
				 }
				//alert(i+"i");
				
	
					</c:forEach>
				</c:forEach>
		
	
			$.ajax({
						url: "/extend/pyVoteInfor.do?method=checkPy&rowId=<%=request.getParameter("rowId")%>",
						type: "post",
						dataType: "json",
						async: false,	//设置为同步
						beforeSend: function (xhr) {
						},
						complete : function (req, err) {
							var returnValues = eval("("+req.responseText+")");
							flag = returnValues["flag"];
							if(!flag){
							alert(returnValues["message"]);
							}
						}
					});
		
		
		
		return flag;
	}
	
	function is_float(js_value) {
	   var re = /^\s*$/;
	   var re1 = /^[0-9]{1,}\.{0,1}[0-9]{0,2}0*$/;
	   if(js_value.match(re)) {
	      return true;
	   }
	   if(js_value.match(re1))
	      return true;
	   return false;
	}
</script>
<base target="_self"/>
<body onload="fillData();">
<br/>
<form:form commandName="pyVoteInforVo" id="pyVoteInforForm" name="pyVoteInforForm" action="/extend/pyVoteInfor.do?method=save" method="post" enctype="multipart/form-data">
<form:hidden path="topicId" />
<input type="hidden" name="rowId" value="<%=request.getParameter("rowId") %>"/>
<table width="98%" cellpadding="6" cellspacing="1" align="center" border="1" bordercolor="#c5dbec">
	
		<tr>
		<td bgcolor="#dfeffc" align="center">
			<h2><font color="#15A7BC">${_Topic.topicName}</font></h2>
			<div style="text-align:left;padding-left:10px;"><font color=gray>${_Topic.descrip}</font></div>
			<c:if test="${!empty _Topic.ruler}">
				<br/>
				<div style="text-align:left;padding-left:10px;"><font color=orange><b>投票规则：</b></font><font color=gray>${_Topic.descrip}</font></div>
			</c:if>
			<br/>
			
		</td>
	</tr>
	<c:if test="${!_Topic.valid || !_IsInDate}">
			<tr>
		<td bgcolor="#dfeffc" align="center">

				<font color=red><h2>当前投票已过期或者无效，您不能进行操作！</h2></font>


			
		</td>
	</tr>
	</c:if>
	
	<c:if test="${_Topic.valid && _IsInDate}">
	<c:if test="${_IsVote}">
		<tr>
		<td bgcolor="#dfeffc" align="center">

				<font color=red><h2>你已经投票!</h2></font>


			
		</td>
	</tr>
	</c:if>
	</c:if>
	
	<tr>
		<td valign=top style="padding-top:10px;">
			<table id="optionTab" class="optionTab" width="90%" border="1">
				<tr>
					<td width="5%" nowrap align="center">序号</td>
					<td align="center">部门</td>
					<td align="center">姓名</td>
					<td align="center">项目名称</td>
					<c:forEach items="${_Topic.itemInfors}" var="item" varStatus="index">
						<c:forEach items="${item.optionInfors}" var="option" varStatus="index">
						
						<td align="center">${option.optionName}</td>
						</c:forEach>	
					</c:forEach>
				</tr>
					<c:forEach items="${_Topic.personInfors}" var="person" varStatus="index">
					<tr>
					<td nowrap align=center>${index.index+1}</td>
					<td align="center">${person.department }</td>
					<td align="center">${person.personName }</td>
					<td align="center">${person.descrip }</td>
					<c:forEach items="${_Topic.itemInfors}" var="item" varStatus="index">
						<c:forEach items="${item.optionInfors}" var="option" varStatus="index">
						
						<td align="center">
							<c:if test="${item.itemType==0}">
								<c:if test="${_Topic.sameDept}">
								
									<c:if test="${_Person.department.organizeName==person.department}"></c:if>
									<c:if test="${_Person.department.organizeName!=person.department}">
									<input type="radio" name="option_${item.itemId}_${person.PId }" value="${option.optionId}" class="option_${option.optionId}" onclick='isCheck(this)' />
									</c:if>
								</c:if>
								<c:if test="${!_Topic.sameDept}">
								<input type="radio" name="option_${item.itemId}_${person.PId }" value="${option.optionId}" class="option_${option.optionId}" onclick='isCheck(this)' />
								</c:if>
							</c:if>
						<c:if test="${item.itemType==1}">
						<c:if test="${_Topic.sameDept}">
						
							<c:if test="${_Person.department.organizeName==person.department}"></c:if>
							<c:if test="${_Person.department.organizeName!=person.department}">
								<input type="checkbox" name="option_${item.itemId}_${person.PId }" value="${option.optionId}" class="option_${option.optionId}" />
							</c:if>
						</c:if>
						<c:if test="${_Topic.sameDept}">
							<input type="checkbox" name="option_${item.itemId}_${person.PId }" value="${option.optionId}" class="option_${option.optionId}" />
						</c:if>
						
						</c:if>
						
						
						</td>
						</c:forEach>	
					</c:forEach>
					</tr>
				</c:forEach>
				
			</table>
		</td>
	</tr>
	
     <tr> 
        <td colspan="2" bgcolor="#dfeffc" align=left>
        
        <c:if test="${_Topic.valid && _IsInDate}">
          <input type="button" id="button" style="cursor: pointer;" onclick="submitData();" value="提交"/>
          </c:if>
             &nbsp;
          <input type="button" value="关闭" style="cursor: pointer;" onclick="window.close();"/>
        </td>
     </tr>
</table>
</form:form>
</body>

