<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="/inc/taglibs.jsp" %>
<%--<%@ include file="/inc/css.jsp" %>--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>


<head>
	<style>
	body{
		font-size:13px;
		color: #b9b9b9;
	}
	</style>
	<SCRIPT src="<c:url value='/js/xheditor-1.2.1/jquery/jquery-1.4.4.min.js'/>" type=text/javascript></SCRIPT>
	<SCRIPT src="<c:url value='/js/xheditor-1.2.1/xheditor-1.2.1.min.js'/>" type=text/javascript></SCRIPT>
	<SCRIPT src="<c:url value='/js/xheditor-1.2.1/xheditor_lang/zh-cn.js'/>" type=text/javascript></SCRIPT>
	<!--  Uploadify -->
	<link href="<c:url value='/'/>js/uploadify/uploadify.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<c:url value='/'/>js/uploadify/jquery.uploadify.min.js"></script>
	<script type="text/javascript" src="<c:url value='/'/>js/uploadify/swfobject.js"></script>
	<script type="text/javascript" src="<c:url value='/'/>js/upload.js"></script>
	
	<!-- 按钮样式 -->
	<link rel="stylesheet" type="text/css" href="<c:url value="/"/>css/button.css" />
	<link rel="stylesheet" type="text/css" href="/css/myTable.css" />
    <title>${_Thesis.title}_OA主题论坛</title>
    <script type="text/javascript">
        $(function() {
        	//初始化图片上传
    		initUploadImg("${_ImgAttachStrs}","fileQueue1","uploadify1",true,20,"uploadImgAttachment","fileList1","logoPicDiv");
        	
        	$("#commentInforFrom")[0].action = '<c:url value="/bbs"/>/commentInfor.do?method=save';
            $("#replyContent").val("");
            $(".quoteButton").click(function(){
                var commentId=($(this)[0].id).replace("quote","");
                $("#replyContent").val("<div style='border:1px #9DB3C5 solid;padding:5px;margin:5px; color:#778899;'>"+
                        "<span style='color:#666666;'><b>&nbsp;&nbsp;引用</b>&nbsp;&nbsp;"+$(this).attr("name")+"楼</span><br> "
                        +"&nbsp;&nbsp;原帖由:&nbsp;&nbsp;<i>"+$("#nickName"+commentId)[0].innerHTML+"</i>&nbsp;&nbsp;发表<br>"
                        +$("#comment"+commentId)[0].innerHTML+"</div><div></div>");
            });
        });
        function addInfor() {
            if ($("#replyContent").val() == '') {
                alert('请填内容!');
                return false;
            }else{
               $("#commentInforFrom").submit();
            }
        }
        
    </script>
    <style type="text/css">
        * {
            padding: 0;
            margin: 0;
        }
        body{
        }
        #main {
            margin:0 auto 0 auto;
            width: 80%;
            border:1px #9DB3C5 solid;
        }

        dl{
            width:100%;
            overflow-y:auto;
            /*background-color:#E8F3FD;*/
        }
        div.aComment{
            width:100%;
            margin:5px 0 5px 0;
            padding:3px 0 3px 0;
            overflow-y:auto;
            border-top:1px #4cbdc5 dashed;
            border-bottom:1px #9DB3C5 dashed;
        }
        dt{
            float:left;
            width:20%;
            padding:5px 0  0 5px;
        }
       dd{
           float:right;
           width:77%;
           min-height:210px;
           padding:5px;
           /*background-color:#ffffff;*/
        }
         #title{
            padding-top:5px;
            width:100%;
            height:29px;
            font-size:18px;
            /*background:url(../images/bbs/title_bg.jpg) repeat-x;*/
            text-align:center;
            color:#ffffff;
        }

       /* .bt {
            background: url(../images/bbs/b_s.gif);
            border:0;
            width: 72px;
            color: #ededed;
            height: 21px;
        }*/
        .quote{
            /*background-color:#F7F7F7;*/
            text-align:right;
        }
        .quoteButton{
            border:0;
            font-size:13px;
            padding:2px 0 0 0;
            margin:0;
            /*background-color:#F7F7F7;*/
        }
        a,a:hover,a:visited{
            text-decoration:none;
            color: #cdcdcd;
            font-size:13px;
            padding:0;
            margin:0;
        }
        ul{
        	list-style: none;
        	vertical-align: middle;
            /*line-height:45px;*/
            color: #cfcfcf;
        }
    
    </style>
    
    <script>
    	function setImgSize(obj,maxwidth,maxheight){
			if(obj.width< maxwidth && obj.height<maxheight){
				return;
			}
			
			if(obj.width> maxwidth || obj.height>maxheight){
				if (maxwidth/obj.width<maxheight/obj.height){
					obj.height*=maxwidth/obj.width;
					obj.width=maxwidth;
				}else{
					obj.width*=maxheight/obj.height;
					obj.height=maxheight;
				}
				
			}	
			
			if(obj.width< maxwidth || obj.height<maxheight){
				if (maxwidth/obj.width<maxheight/obj.height){
					obj.height*=maxwidth/obj.width;
					obj.width=maxwidth;
				}else{
					obj.width*=maxheight/obj.height;
					obj.height=maxheight;
				}
				
			}
			
		}
    </script>

</head>
<base target="_self" />
<body>
<br/>
<div id="main">
    <div id="title">
    	<c:if test="${_Thesis.essence}">
			<img src='<c:url value='/images'/>/bbs/essence.gif' align='absmiddle' alt="精品"/>
		</c:if>
    	${_Thesis.title}</div>
    <c:if test="${_Page.page == 1}">
     <div class="aComment">
    <dl>
        <dt>
            <ul>
            	<%--<li>
					<img src="<c:url value="/images"/>/bbs/default_person_s.gif" style="border:3px white solid;" height="110" alt="头像"/>
            	</li>--%><br/>
            	<li>作者：${_Thesis.nickName}</li>
            	<li>查看次数：${_Thesis.viewsCount}次</li>
            	<li>更新时间：<fmt:formatDate value="${_Thesis.updateDate}" pattern="yyyy-MM-dd HH:mm"/></li>
            </ul>
        </dt>
        <dd>
        	${_Thesis.content}
        	<c:if test="${!empty _ImgAttachment_Names}">
		    	<br/><br/>
		    	<ul>
		                	
		        	<c:forEach var="file" items="${_ImgAttachment_Names}" varStatus="status">
			        	<li>
				        	<a href="${_ImgAttachments[status.index]}" target="_blank" title="${file}&#13;${_Attachment_Sizes[status.index]}&#13;点击查看原图" >
				            	<img src="${_ImgAttachments[status.index]}" onload="setImgSize(this,400,400)" style="border:0px;" alt="点击查看原图"/>
				        	</a>
			        	</li>
			       		<li>&nbsp;</li>
			     	</c:forEach>
		    	</ul>
            </c:if>
            <br/>
            <c:if test="${!empty _Thesis.attachment}">
            	<hr style="background-color:#dcdcdc;height:1px;"/><br/> 
                <img src="<c:url value="/images"/>/bbs/icon_attach.gif" width="16" height="16" align="absmiddle" alt="附件" />   
                	附件：<br/>&nbsp;&nbsp;
            </c:if>

			<c:forEach var="file" items="${_Attachment_Names}" varStatus="status">
	        	<a href="<c:url value="${'/common/'}"/>download.jsp?filepath=${_Attachments[status.index]}" title="点击下载">
	            	<font color="red">${file}(${_Attachment_Sizes[status.index]})</font>
	            </a><br/>&nbsp;&nbsp;
	        </c:forEach>
         </dd>
    </dl>
     </div>
    </c:if>
    <c:forEach var="comment" items="${_Comments}" varStatus="status">
         <div class="aComment">
        <dl>
            <dt>
               <ul>
            	<%--<li>
					<img src="<c:url value="/images"/>/bbs/default_person_s.gif" style="border:3px white solid;" height="110" alt="头像"/>
            	</li>--%><br/>
            	<li>评论人：<span id="nickName${comment.commentId}">${comment.nickName}</span></li>
            	<li></li>
            	<li>评论时间：<fmt:formatDate value="${comment.replyDate}" pattern="yyyy-MM-dd HH:mm"/></li>
               </ul>
            </dt>
            <dd>
                <div class="quote">
                	<div id="floor_${(status.index)+1}" style="color:red;margin-left:5px;font-size:14px;float:left;line-height:15px;">
                		#<c:choose>
                			<c:when test="${(_Pl.pages.currPage-1)*10 +(status.index)+1==1}">沙发</c:when>
                			<c:when test="${(_Pl.pages.currPage-1)*10 +(status.index)+1==2}">板凳</c:when>
                			<c:when test="${(_Pl.pages.currPage-1)*10 +(status.index)+1==3}">地板</c:when>
                			<c:otherwise>${(_Pl.pages.currPage-1)*10 +(status.index)+1}楼</c:otherwise>
                		</c:choose>
                	</div>
                    <a href="#top">[TOP]</a>
                    <a href="#write_comment">[写评论]</a>
                    <a class="quoteButton" href="#write_comment" id="quote${comment.commentId}" name="${(_Pl.pages.currPage-1)*10 +(status.index)+1}">[引用]</a>
                </div>
                <div class="clear"></div>
                <div id="comment${comment.commentId}">
                	${comment.replyContent}
                	<c:if test="${!empty comment.imgAttachment}">
                		<ul>
							<c:forEach items="${fn:split(comment.imgAttachment,';')}" var="comImg">
								<c:if test="${!empty comImg}">
	                				<li>
		                				<a href="${fn:split(comImg,'|')[0]}/${fn:split(comImg,'|')[1]}" target="_blank" title="${fn:split(comImg,'|')[1]}&#13;${fn:split(comImg,'|')[2]}&#13;点击查看原图" >
		                					<img src="${fn:split(comImg,'|')[0]}/${fn:split(comImg,'|')[1]}" onload="setImgSize(this,400,400)" style="border:0px;" alt="点击查看原图"/>
			                			</a>
		                			</li>
	                			</c:if>
							</c:forEach>
	                		
                		</ul>
                	</c:if>
                	
                </div>
            </dd>
        </dl>
             </div>
    </c:forEach>
    <div>${_Pl.pageShowString}</div>
    <div class="aComment">
    <dl>

        <dt>
           写评论<a name="write_comment" href="#"></a>
        </dt>
        <dd>
            <form:form commandName="commentInforVo" action="<c:url value='/bbs'/>/commentInfor.do?method=save" method="post" id="commentInforFrom" name="commentInforFrom" enctype="multipart/form-data">
                <input type="hidden" id="thesisId" name="thesisId" value="${_Thesis.thesisId}"/>
                <table>
                    <%--<tr>
                        <td>昵称：</td><td> <html:text property="nickName" value=""/><br></td>
                    </tr>
                    --%>
                    <tr>
                        <td valign="top">内容：</td>
                        <td> 
                        	<textarea name="replyContent" cols="75" rows="13" class="xheditor" id="replyContent"></textarea>
                        </td>
                    </tr>
                    <tr> 
                        <td valign="top">图片：</td>
					    <td>
					    	<table cellpadding="0" cellspacing="0" style="margin-bottom:0;margin-top:0">
								<tr><td>
									<div id="logoPicDiv"></div>
									<%--<form:hidden path="imgAttachment" id="imgAttachment" />
									--%><input type="hidden" id="uploadImgAttachment" name="uploadImgAttachment" />
									<%--<input type="file" name="attachment1" size="50"/>
									&nbsp;
									<input type="button" value="更多.." onClick="addtable('newstyle')" class="bt" style="cursor:hand;"/> 
								--%></td></tr>
							</table>
					    	<span id="newstyle"></span> 
					    </td>
 				 	</tr>
                    <tr>
                        <td></td>
                        <td>
                        	<input type="submit" value="提交" onclick="addInfor();return false;" id="btn_sub" style="cursor:pointer;" class="bt"/>
			                &nbsp;
							<input type="button" value="关闭" Class="bt" onclick="window.close();" id="btn_clo" style="cursor:pointer;"/>
						</td>
                    </tr>
                </table>
            </form:form>
        </dd>

    </dl>
   </div>
   </div>

</body>
</html>