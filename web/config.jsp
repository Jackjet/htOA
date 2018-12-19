%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<html>
  <head>
    
    <title>海通OA后台配置</title>
    <script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
    <script language="javascript" src="/js/jquery.metadata.js"></script>
    <script language="javascript" src="/js/jquery.validate.js"></script>
    
    <!-------------------- 上传预览JS ---------------------->
    <script language="javascript">
		$(function(){
		    var ei1 = $("#large1");
		    var ei2 = $("#large2");
		    ei1.hide();
		    ei2.hide();
		    
		    $("#img1").mousemove(function(e){
		        ei1.css({top:e.pageY,left:e.pageX}).html('<img style="border:1px solid gray;" src="' + this.src + '" />').show();
		    }).mouseout( function(){
		        ei1.hide("slow");
		    })
		    
		    $("#img2").mousemove(function(e){
		        ei2.css({top:e.pageY,left:e.pageX}).html('<img style="border:1px solid gray;" src="' + this.src + '" />').show();
		    }).mouseout( function(){
		        ei2.hide("slow");
		    })
		    
		    $("#loginPic").change(function(){
		        $("#img1").attr("src","file:///"+$("#loginPic").val());
		    })
		    $("#indexPic").change(function(){
		        $("#img2").attr("src","file:///"+$("#indexPic").val());
		    })
		});									
	</script>
	<style type="text/css">
	    #large1,#large2{position:absolute;display:none;z-index:999;}
	</style>
	<!-------------------- 上传预览JS结束 ---------------------->
    
    
    <!----------------- 图片表单提交JS --------------->
    <script>
		//清除jquery ajax缓存影响
		$.ajaxSetup({            
			cache: false        
		});
		
		function refreshfunc(){
			//alert("test");
			//将已显示的选中
			/*$.ajax({
				url: '/core/config.do?method=allreadyDis',
				//data: $("#indexform").serialize,
				type:'POST', 
		        async: false,
		        cache: false,
		        dataType: "json",
		        success: function(data){
		        	$.each(data._AllIndexFun, function(i, n) {
					alert(n.category.categoryId+" --");
						$.each($("#indexform :checkbox"),function(j,m){
							alert($(this).attr("value")+" ++");
							if(n.category.categoryId == $(this).attr("value")){
								var id = $(this).attr("id");
								var val = $(this).attr("value");
								
								$(this).attr("checked","true");
								$("#"+id+"div").css("display","block");
								$("#"+id+"label").css("color","red");
								$("#"+id+"label").css("font-weight","bold");
								$("#"+id+"div_big").css("border","1px red solid");
								
								$("#displayName"+val).val(n.displayName);
								$("#displayCount"+val).val(n.displayCount);
								
								if(n.displayTime == 1){
									$("#"+id+"distime").val("是");
								}else if(n.displayTime == 0){
									$("#"+id+"distime").val("否");
								}
								
								if(n.roll == 1){
									$("#"+id+"ifroll").val("是");
								}else if(n.roll == 0){
									$("#"+id+"ifroll").val("否");
								}
								
								//$("#"+id+"distime").val(n.displayTime);
								//$("#"+id+"ifroll").val(n.roll);
							}
						});
					});
		        }
			});*/
			
			$.getJSON("/core/config.do?method=allreadyDis",function(data) {
				$.each(data._AllIndexFun, function(i, n) {
					//alert(n.category.categoryId+" --");
					$.each($("#indexform :checkbox"),function(j,m){
						//alert($(this).attr("value")+" ++");
						//alert(n.category.categoryId);
						if(n.category.categoryId == $(this).attr("value")){
							var id = $(this).attr("id");
							var val = $(this).attr("value");
							
							//改变div样式
							$(this).attr("checked","true");
							$("#"+id+"div").css("display","block");
							$("#"+id+"label").css("color","red");
							$("#"+id+"label").css("font-weight","bold");
							$("#"+id+"div_big").css("border","1px red solid");
							
							//将已有值显示出来
							$("#displayName"+val).val(n.displayName);
							$("#displayCount"+val).val(n.displayCount);
							
							if(n.displayTime == 1){
								$("#"+id+"distime").val("是");
							}else if(n.displayTime == 0){
								$("#"+id+"distime").val("否");
							}
							
							if(n.roll == 1){
								$("#"+id+"ifroll").val("是");
							}else if(n.roll == 0){
								$("#"+id+"ifroll").val("否");
							}
							
							//为hidden赋值
							$("#funcid"+id).val(n.id);
							$("#_funcid"+id).val(n.id);
							$("#funcname"+id).attr("name","funcname"+n.id);
							
							$("#_funcname"+id).attr("name","_funcname"+n.id);
							
							//alert($("#funcname"+id).val());
							
							//$(".idclass"+deletedid).attr("value","");
							
							//$("#"+id+"distime").val(n.displayTime);
							//$("#"+id+"ifroll").val(n.roll);
						}
					});
				});
			});
		};	
		
		$().ready(function(){
				
				$("#picform").validate({
				
				//验证规则
					rules: {
						
						//图片校验规则中，文件的后缀名跟上面一样用“[]”把要校验的文件后缀名分别列出来，加引号并用逗号隔开，其中后缀名前面带不带点都可以
						loginPic: {
							required: true,
							accept: [".gif"]
						},
						indexPic: {
							required: true,
							accept: [".png"]
						}
						
					},
					
					//错误提示信息
					messages: {
						loginPic: {
							required: "请上传登录页图片，格式必须为'.gif'",
							accept: "请上传'.gif'格式的图片"
						},
						indexPic: {
							required: "请上传主页上方图片，格式必须为'.png'",
							accept: "请上传'.png'格式的图片"
						}
					}
					
				});
				
				//图片表单提交
				$("#picform").submit(function(){
					$.ajax( {   
				        url: '/core/config.do?method=picConfig&loginPic='+$("#loginPic").val()+'&indexPic='+$("#indexPic").val(),	                    
		             	//data: 'loginPic='+$("#loginPic").val()+'&indexPic='+$("#indexPic").val(), // 从表单中获取数据	                    
		            	type:'POST', 
		               	async: false,
		               	
				       	success : function(msg) {  
							alert(msg);	  
				        }   			         
		   			}); 
				});
				
				
				
				//首页显示模块表单提交
				$("#indexform").submit(function(){
					$.ajax( {   
				        url: '/core/config.do?method=setIndexFun',	                    
		             	data: $("#indexform").serialize(), // 从表单中获取数据	                    
		            	type:'POST', 
		               	async: true,
		               	cache: false,
		               	beforeSend: function(){
		               		//checkIndexForm;
		               		var flag = true;
							$.each($("#indexform :checkbox"),function(i,n){
								if($(this).attr("checked") == true){
									var thisVal = $(this).attr("value");
									//alert(thisVal);
									if($("#displayName"+thisVal).val() == ""){
										alert("请填入实际显示名称！");
										flag = false;
									}
									if($("#displayCount"+thisVal).val() == ""){
										alert("请填入实际显示条数！");
										flag = false;
									}else if($("#displayCount"+thisVal).val() < 1 || $("#displayCount"+thisVal).val() > 10){
										alert("请输入1~10之间的条数！");
										flag = false;
									}
									
								}
								
							});
							return flag;
		               	},
				       	success : function(msg) {  
							alert(msg);	  
							refreshfunc();
							//alert("** "+${deletedcategoryid})
				        } 			         
		   			}); 
				});
				
				
			});
			
			
    	
    </script>
    <!----------------- 图片表单提交JS结束 --------------->
    
    
    
    <link href="/css/validateform.css" rel="stylesheet" type="text/css" />
    <style>
    	.anno{
    		color:red;
    		font-size:13px;
    	}
    </style>

  </head>
  
  <body>
  	<div>
	  	<fieldset style="border-color:gray;">
	  		<legend style>图片更改</legend>
	  		<!-- 图片form -->
	  		<form name="picform" id="picform" method="post" action="">
			    <label>登录页面图片<span class="anno">(规格：776*569 , 格式：GIF)</span>：</label><input type="file" name="loginPic" id="loginPic" />
			    <img id="img1" width="60" height="60"><br/><br/>
			    
			    <label>主页顶部图片<span class="anno">(规格：740*55 , 格式：PNG)</span>：</label><input type="file" name="indexPic" id="indexPic" />
			    <img id="img2" width="60" height="60"><br/><br/>
			    
			    <div id="large1"></div>
			    <div id="large2"></div>
			    <input type="submit" value="提交" />
		  	</form>
	  	</fieldset>
  	</div><br/>
  	
  	<div>
  		<fieldset style="border-color:gray;">
  			<legend>首页显示模块</legend>
  			<form name="indexform" id="indexform" method="post" action="">
  				
  				<!-------------------------- ------------------------------->
  				<script>
				     <!----------------- 用hide和show效果显示隐藏 ------------------->
				    function showhide(clickid,divid,bigdivid){
						if($("#"+divid).css("display")=="none" && $("#"+clickid).attr("checked")==true){
							$("#"+divid).show(10);
							$("#"+clickid+"label").css("color","red");
							$("#"+clickid+"label").css("font-weight","bold");
							$("#"+bigdivid).css("border","1px red solid");
							//$("#funcname"+clickid).val($("#"+clickid).val());
							
							//$("#_funcname"+clickid).val($("#"+clickid).val());
							//alert($("#funcname"+clickid).val());
						}else if($("#"+divid).css("display")=="block" && $("#"+clickid).attr("checked")==false){
							$("#"+divid).hide(10);
							$("#"+clickid+"label").css("color","black");
							$("#"+clickid+"label").css("font-weight","normal");
							$("#"+bigdivid).css("border","0");
							$("#funcname"+clickid).val("");
							//alert($("#funcname"+clickid).val());
							//alert($("#funcid"+clickid).val());
						}
					}
					
				    jQuery().ready(function (){
				    	$.getJSON("/core/config.do?method=allInforCategory",function(data) {
							//需要首页显示的发布信息
							
							var content = "";
							
							$.each(data._AllInfoCategory, function(i, n) {
								
								content += "<div style='float:left;width:300px;' id='"+n.pagePath+"div_big'>"
									+"<input type='checkbox' name='category' id='"+n.pagePath+"' value='"+n.categoryId+"' onclick=showhide('"+n.pagePath+"','"+n.pagePath+"div','"+n.pagePath+"div_big'); />"
									+"<label for='"+n.pagePath+"' id='"+n.pagePath+"label'>"+n.categoryName+"</label>"
									+"<input type='hidden' name='id"+n.categoryId+"' id='funcid"+n.pagePath+"' class='idclass"+n.categoryId+"' value='' />"
									+"<input type='hidden' name='id' id='_funcid"+n.pagePath+"' class='idclass"+n.categoryId+"' value='' />"
									+"<input type='hidden' id='funcname"+n.pagePath+"' name='' value='"+n.categoryId+"'/>"
									+"<input type='hidden' id='_funcname"+n.pagePath+"' name='' value='"+n.categoryId+"'/>"
					  				+"<div id='"+n.pagePath+"div' style='display:none;'>"
					  					+"<span>显示名称：</span><input type='text' name='displayName"+n.categoryId+"' id='displayName"+n.categoryId+"' value='"+n.categoryName+"' /><br/>"
					  					+"<span>显示条数：</span><input type='text' name='displayCount"+n.categoryId+"' id='displayCount"+n.categoryId+"' size='5' />"
					  					+"<span style='color:red;font-size:11px;'>最大为10条</span><br/>"
					  					+"<span>显示作者/时间：</span>"
					  					+"<select name='displayTime"+n.categoryId+"' id='"+n.pagePath+"distime'>"
					  					+"<option value='true' selected>是</option>"
					  					+"<option value='false'>否</option></select>"
					  					+"<span style='color:red;font-size:11px;'>如: 系统管理员 2011-01-01</span>"
					  					+"<br/>"
					  					+"<span>是否滚动：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>"
					  					+"<select name='roll"+n.categoryId+"' id='"+n.pagePath+"ifroll' >"
					  					+"<option value='true'>是</option>"
					  					+"<option value='false' selected>否</option></select>"
					  					+"<span style='color:red;font-size:11px;'>由下至上滚动</span>"
					  				+"</div>"
					  				+"<br/><br/></div>";
								
								
							});	
							content += "<br/><input type='submit' value='提交' style='clear:both;' />";
							$('#indexform').html(content);
							//alert(0);
						});
						
						//alert(1);
						refreshfunc();
						//alert(2);
						
				    });
				</script>
				
  			</form>
  		</fieldset>
  	</div>
  	
  </body>
</html>



<!-- 	 <input type="checkbox" name="report" id="report" onclick=showhide('report','reportdiv'); /><label for="report">公司公告</label>
  				<div id="reportdiv" style="display:none;">
  					<span>显示名称：</span><input type="text" name="" id="" /><br/>
  					<span>显示条数：</span><input type="text" name="" id="" /></br/>
  					<span>显示时间：</span>
  					<input type="radio" name="reportdistime" id="" checked="checked" />是
  					<input type="radio" name="reportdistime" id="" />否
  					<br/>
  					<span>是否滚动：</span>
  					<input type="radio" name="reportifroll" id="" />是
  					<input type="radio" name="reportifroll" id="" checked="checked" />否
  				</div>
  				<br/>
  				<br/><input type="checkbox" name="todo" id="todo" onclick=showhide('todo','tododiv'); /><label for="todo">待办事宜</label>
  				<div id="tododiv" style="display:none;">
  					<span>显示名称：</span><input type="text" name="" id="" /><br/>
  					<span>显示条数：</span><input type="text" name="" id="" /></br/>
  					<span>显示时间：</span>
  					<input type="radio" name="tododistime" id="" checked="checked" />是
  					<input type="radio" name="tododistime" id="" />否
  					<br/>
  					<span>是否滚动：</span>
  					<input type="radio" name="todoifroll" id="" />是
  					<input type="radio" name="todoifroll" id="" checked="checked" />否
  				</div>
  				<br/>
  				<br/><input type="checkbox" name="perinfo" id="perinfo" onclick=showhide('perinfo','perinfodiv'); /><label for="perinfo">个人讯息</label>
  				<div id="perinfodiv" style="display:none;">
  					<span>显示名称：</span><input type="text" name="" id="" /><br/>
  					<span>显示条数：</span><input type="text" name="" id="" /></br/>
  					<span>显示时间：</span>
  					<input type="radio" name="perdistime" id="" checked="checked" />是
  					<input type="radio" name="perdistime" id="" />否
  					<br/>
  					<span>是否滚动：</span>
  					<input type="radio" name="perifroll" id="" />是
  					<input type="radio" name="perifroll" id="" checked="checked" />否
  				</div>
  				
  				<br/><br/>-->