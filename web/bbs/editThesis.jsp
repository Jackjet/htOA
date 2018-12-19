
<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
<title>发表论题</title>

<style>
	body{
		font-size:13px;
	}
</style>


<script src="<c:url value='/js/addattachment.js'/>" type="text/javascript"></script>


<!--<SCRIPT src="<c:url value='/js/xheditor/jquery-1.4.2.min.js'/>" type=text/javascript></SCRIPT>
<SCRIPT src="<c:url value='/js/xheditor/xheditor-zh-cn.min.js'/>" type=text/javascript></SCRIPT>-->


<SCRIPT src="<c:url value='/js/xheditor-1.2.1/jquery/jquery-1.4.4.min.js'/>" type=text/javascript></SCRIPT>
<SCRIPT src="<c:url value='/js/xheditor-1.2.1/xheditor-1.2.1.min.js'/>" type=text/javascript></SCRIPT>
<SCRIPT src="<c:url value='/js/xheditor-1.2.1/xheditor_lang/zh-cn.js'/>" type=text/javascript></SCRIPT>

<link rel="stylesheet" type="text/css" href="<c:url value="/"/>components/jqgrid/css/jquery.ui.all.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />

<!--  Uploadify -->
<link href="<c:url value='/'/>js/uploadify/uploadify.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<c:url value='/'/>js/uploadify/jquery.uploadify.min.js"></script>
<script type="text/javascript" src="<c:url value='/'/>js/uploadify/swfobject.js"></script>
<script type="text/javascript" src="<c:url value='/'/>js/upload.js"></script>

<script>
	$.ready(function(){
		//格式化tr,td
		$("#addTable tr").addClass("ui-widget-content jqgrow ui-row-ltr").height(30).find("td:nth-child(1)").addClass("ui-state-default jqgrid-rownum");
		
	});
	
	
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
	
</script>
<script type="text/javascript">
    $(function(){
        $("#thesisFrom")[0].action = '<c:url value="/bbs"/>/thesisInfor.do?method=save';
        $(".deleteAttachment").click(function(){
            $($(this)[0].parentNode).remove()
        });
        
        //初始化附件上传
		initUpload("${_AttachStrs}","fileQueue","uploadify","uploadifyAttachment","fileList","uploadDiv");
		
		//初始化图片上传
		initUploadImg("${_ImgAttachStrs}","fileQueue1","uploadify1",true,20,"uploadifyImgAttachment","fileList1","logoPicDiv");
        //$('#content').xheditor({tools:'Cut,Copy,Paste,Pastetext,|,Source,Fullscreen,About'});
    });
    function addInfor() {
        if ($("#f_title").val() == '') {
            alert('请填标题!');
            return false;
        } else {
        	//原附件
            var str = "";
            $(".attachList").each(function() {
                str +=  $(this).val() + ",";
            });
            $("#attachmentArray").val(str);
            
            //新附件,得到真实路径
            /*var file = document.getElementById("attachmentNew");
            file.select();
            var filePath = document.selection.createRange().text;*/
            var filePath = getPath($("#attachmentNew"));
            //alert("=="+filePath);
            $("#hideAttachment").val(filePath);
            
            //新图片
            var img = ""
            $(".imgClass").each(function() {
                //alert($(this).val());
                img += $(this).val() + "," ;
            });
            $("#hideImg").val(img);
           // alert($("#hideImg").val());
            
            $("#thesisFrom").submit();
        }
    }
</script>
<base target="_self"/>
</head>

	<body> 
	<br>
    <form:form commandName="thesisInforVo" action="/bbs/thesisInfor" method="post" enctype="multipart/form-data" id="thesisFrom" name="thesisFrom">

    <form:hidden path="thesisId"/>
	
	
	<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 100%">
	<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
	  <div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
	    <span class="ui-jqgrid-title">编辑论题</span>
	  </div>
	
		<div class="ui-jqgrid-bdiv" style="height: auto; width: 100%">
			<div style="position: relative;">
				<div>
					<table cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable" style="width: 100%" id="addTable">
						<tbody>
						
							<tr class="ui-widget-content jqgrow ui-row-ltr" style="height:30px;">
								<td style="width: 15%" class="ui-state-default jqgrid-rownum"><center>标题：</center></td>
								<td>
									<form:input path="title" size="148" id="f_title"/>
                      				<font color="red">*</font>
								</td>
							</tr>
							
							<tr class="ui-widget-content jqgrow ui-row-ltr">
								<td class="ui-state-default jqgrid-rownum"><center>内容：</center></td>
								<td>
									<form:textarea path="content" cols="150" rows="30" class="xheditor"/><!--  class="xheditor" -->
								</td>
								<!--  {tools:'Blocktag,Fontface,FontSize,Bold,Italic,Underline,Strikethrough,FontColor,BackColor,SelectAll,
									Removeformat,Align,List,Outdent,Indent,Link,Unlink,Anchor,Hr,Emot,Table,Source,Preview,Print,Fullscreen,About',skin:'default'} -->
							</tr>
							
							<tr class="ui-widget-content jqgrow ui-row-ltr" style="height:30px;">
								<td valign="middle" class="ui-state-default jqgrid-rownum">
									<center>附件：</center>
								</td>
								<td>
									<div id="uploadDiv"></div>
									<input type="hidden" id="uploadifyAttachment" name="uploadifyAttachment" />
								</td>
							</tr>
							
							<tr class="ui-widget-content jqgrow ui-row-ltr" style="height:30px;">
								<td valign="middle" class="ui-state-default jqgrid-rownum">
									<center>图片：</center>
								</td>
								<td>
									<div id="logoPicDiv"></div>
									<input type="hidden" id="uploadifyImgAttachment" name="uploadifyImgAttachment" />
								</td>
							</tr>
							
						</tbody>
					</table>
				</div>
			</div>
		</div>
		
		<div style="width: 100%" class="ui-state-default ui-jqgrid-pager ui-corner-bottom" dir="ltr">
			<div role="group" class="ui-pager-control">
				<table cellspacing="0" cellpadding="0" border="0" style="width: 100%; table-layout: fixed;" class="ui-pg-table">
					<tbody>
						<tr>
							<td align="left">
								<table cellspacing="0" cellpadding="0" border="0" style="float: left; table-layout: auto;" class="ui-pg-table navtable">
									<tbody>
										<tr>
											<td><input style="cursor: pointer;" type="button" value="提交" onclick="addInfor();return false;"/></td>
											<td><input style="cursor: pointer;" type="button" value="关闭" onclick="window.close();"/></td>
										</tr>
									</tbody>
								</table>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	</div>
				
  	</form:form>
    </body>
</html>