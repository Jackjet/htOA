<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>发表论题</title>
<link href="<c:url value="/"/>css/party/css/dangqun.css" rel="stylesheet" type="text/css" />
<%--<link rel="stylesheet" type="text/css" href="<c:url value="/"/>css/edit.css" />
<script src="<c:url value="/"/>js/jquery-1.9.1.js" type="text/javascript"></script>--%>
<script src="<c:url value="/"/>js/kindeditor-4.1.7/kindeditor.js" type="text/javascript"></script>

<%--<SCRIPT src="<c:url value='/js/xheditor/jquery-1.4.2.min.js'/>" type=text/javascript></SCRIPT>
<SCRIPT src="<c:url value='/js/xheditor/xheditor-zh-cn.min.js'/>" type=text/javascript></SCRIPT>
--%>
<SCRIPT src="<c:url value='/js/xheditor-1.2.1/jquery/jquery-1.4.4.min.js'/>" type=text/javascript></SCRIPT>
<SCRIPT src="<c:url value='/js/xheditor-1.2.1/xheditor-1.2.1.min.js'/>" type=text/javascript></SCRIPT>
<SCRIPT src="<c:url value='/js/xheditor-1.2.1/xheditor_lang/zh-cn.js'/>" type=text/javascript></SCRIPT>

<!-- 按钮样式 -->
<link rel="stylesheet" type="text/css" href="<c:url value="/"/>css/button.css" />

<!-- 表单验证 -->
<script src="<c:url value='/'/>js/Validform_v5.3.2/Validform_v5.3.2_min.js"></script>
<link rel="stylesheet" type="text/css" href="<c:url value='/'/>js/Validform_v5.3.2/css/style.css" />
<!--  Uploadify -->
<link href="<c:url value='/'/>js/uploadify/uploadify.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<c:url value='/'/>js/uploadify/jquery.uploadify.min.js"></script>
<script type="text/javascript" src="<c:url value='/'/>js/uploadify/swfobject.js"></script>
<script type="text/javascript" src="<c:url value='/'/>js/upload.js"></script>

<style>
	body{
		font-size:13px;
	}
</style>

<script>
	//KindEditor.ready(function(K) {
	//    window.editor = K.create('#content');
	//});
	/*var editor;
	KindEditor.ready(function(K) {
		editor = K.create('textarea[name="content"]', {
			resizeType : 1,
			allowPreviewEmoticons : false,
			allowImageUpload : false,
			items : [
				'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
				'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
				'insertunorderedlist', '|', 'emoticons', 'image', 'link']
		});
	});*/
	function getPath(obj){   
		if(obj) {   
				if (window.navigator.userAgent.indexOf("MSIE")>=1){   
	   			obj.select();   
	  			return document.selection.createRange().text;   
	 		} else if(window.navigator.userAgent.indexOf("Firefox")>=1){   
	  			if(obj.files){   
	    			return obj.files.item(0).getAsDataURL();   
	   			}   
	  			return obj.value;   
	 		}   
			return obj.value;   
			}   
	}  
	
	$(function(){
		//初始化附件上传
		initUpload("${_AttachStrs}","fileQueue","uploadify","uploadifyAttachment","fileList","uploadDiv");
		
		//初始化图片上传
		initUploadImg("${_ImgAttachStrs}","fileQueue1","uploadify1",true,20,"uploadifyImgAttachment","fileList1","logoPicDiv");
		
		$("#thesisForm").Validform({
			tiptype:3,
			btnSubmit:"#btn_sub",
			showAllError:true,
			beforeSubmit:function(curform){
				/*//原附件
	            var str = "";
	            $(".attachList").each(function() {
	                str +=  $(this).val() + ",";
	            });
	            $("#attachmentArray").val(str);
	            
	            //新附件,得到真实路径
	            var filePath = getPath($("#attachmentNew"));
	            $("#hideAttachment").val(filePath);
	            
	            //新图片
	            var img = ""
	            $(".imgClass").each(function() {
	            	//alert($(this).val());
	                img += $(this).val() + "," ;
	                //img += getPath($(this)) + ",";
	            });
	            
	            $("#hideImg").val(img);
	           // var picPath = getPath($("#hideImg"));
	            //$("#hideImg").val(picPath);
	            
	            //取得编辑器内容
	           // $("#content").html(editor.html());
	            //alert($("#title").val());*/
			},
			callback:function(data){
				alert('信息编辑成功！');
				window.returnValue = "refresh";
				window.close();
				window.opener.location.reload();
			}
		}); 
		
		/*$(".deleteAttachment").click(function(){
            $($(this)[0].parentNode).remove()
        });*/
	});
</script>

</head>
<base target="_self" />
<body>
	<div id="header">
		<div class="logo">
			上海港国际客运中心
		</div>
	</div>
	<div id="wrap">

		<!--网站主题部分-->
		<div id="right">
			<div class="emil"></div>
			<div class="module">
				<div class="content">
					<div class="xinxi">
						<strong>发表论题</strong>
					</div>
					<p>&nbsp;</p>
					<div class="news">
						 <form:form commandName="thesisInforVo" action="/bbs/thesisInfor.do?method=save" method="post" enctype="multipart/form-data" id="thesisForm" name="thesisForm">
   							 <form:hidden path="thesisId"/>
							<table width="800" border="0" cellpadding="0" cellspacing="0"
								class="doc">
								<tr>
									<td width="15%" align="right" nowrap="nowrap"><span class="blues">标题：</span>&nbsp;&nbsp;</td>
									<td width="85%">
										<form:input path="title" size="80" class="textfield" datatype="*" sucmsg="填写正确！" nullmsg="请输入标题！" />
										<font color="red">*</font>
									</td>
								</tr>
								<tr>
									<td align="right" valign="top" nowrap="nowrap"><span class="blues">内容：</span>&nbsp;&nbsp;</td>
									<td>
										<form:textarea path="content" cols="80" rows="20" class="xheditor"/> 
									</td>
								</tr>
								<tr>
									<td align="right" valign="top" nowrap="nowrap"><span class="blues">附件：</span>&nbsp;&nbsp;</td>
									<td>
										<div id="uploadDiv"></div>
										<%--<form:hidden path="attachment" id="attachment" />
										--%><input type="hidden" id="uploadifyAttachment" name="uploadifyAttachment" />
									</td>
								</tr>
								<tr>
									<td align="right" valign="top" nowrap="nowrap"><span class="blues">图片：</span>&nbsp;&nbsp;</td>
									<td>
										<div id="logoPicDiv"></div>
										<%--<form:hidden path="imgAttachment" id="imgAttachment" />
										--%><input type="hidden" id="uploadifyImgAttachment" name="uploadifyImgAttachment" />
									</td>
								</tr>

							</table>
							<div id="buttons">
								<table width="300" border="0" align="center" cellpadding="0"
									cellspacing="0">
									<tr>
										<td height="12"></td>
										<td height="12"></td>
									</tr>
									<tr>
										<td>
											<a href="javascript:void(0);" class="btn_blue" id="btn_sub"><span>提 交</span></a>
										</td>
										<td>
											<a href="javascript:void(0);" class="btn_red" onclick="window.close();"><span>关 闭</span></a>
										</td>
									</tr>
									<tr>
										<td height="20"></td>
										<td height="20"></td>
									</tr>
								</table>
							</div>
						</form:form>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="clearit"></div>

	<!--网站底部部分-->
	<div id="dibu">上海慧智计算机技术有限公司 技术支持</div>

</body>
</html>
