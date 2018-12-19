<%@ page contentType="text/html; charset=gbk" %>
<%@ include file="/inc/taglibs.jsp" %>
<%--<%@ include file="/inc/css.jsp" %>--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>


<head>
    <script src="<c:url value="/"/>js/multisearch.js"></script> <!--加载模态多条件查询相关js-->
    <script src="<c:url value="/"/>datePicker/WdatePicker.js" language="JavaScript"></script>
    <script src="<c:url value="/"/>components/jquery-1.4.2.js" type="text/javascript"></script> <!--jquery包-->
    <script src="<c:url value="/"/>components/jqgrid/js/jquery-ui-1.8.2.custom.min.js" type="text/javascript"></script><!--jquery ui-->
    <script src="<c:url value="/"/>components/jquery.layout.js" type="text/javascript"></script><!--jquery 布局-->
    <script src="<c:url value="/"/>components/jqgrid/js/grid.locale-cn.js" type="text/javascript"></script> <!--jqgrid 中文包-->
    <script src="<c:url value="/"/>components/jqgrid/js/jquery.jqGrid.min.js" type="text/javascript"></script> <!--jqgrid 包-->
    <script src="<c:url value="/"/>js/inc_javascript.js"></script>
    <script src="<c:url value="/"/>js/commonFunction.js"></script>
    <script src="<c:url value="/"/>js/changeclass.js"></script> <!--用于改变页面样式-->

    <%--<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />--%>
    <link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>css/base/jquery-ui-1.9.2.custom.css" />
    <link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
    <link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/tabstyle.css" />
    <link rel="stylesheet" type="text/css" media="screen" href="/css/myTabll.css" />

    <title>论坛</title>
    <style>
	body{
		font-size:13px;
	}
	</style>
    
    <%--<script src="<c:url value='/js/xheditor/jquery-1.4.2.min.js'/>" type=text/javascript></script>--%>

        <style type="text/css">
        * {
            padding: 0;
            margin: 0;
        }
        #main {
            margin:0 auto 0 auto;
            width: 100%;
            border:1px #9DB3C5 solid;
            text-align:left;
            overflow-y:auto;
            color: #d3d3d3;
        }

        dl{
            width:100%;
            min-height:100px;
            overflow-y:auto;
            /*background-color:#E8F3FD;*/
        }
        div.aComment{
            width:100%;
            margin:5px 0 5px 0;
            padding:3px 0 3px 0;
            overflow-y:auto;
            border-top:1px #4eb8c5 solid;
            border-bottom:1px #4eb8c5 solid;
        }
        dt{
            float:left;
            width:20%;
            padding:5px 0  0 5px;
        }
       dd{
           float:right;
           width:77%;
           min-height:100px;
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

        .bt {
            /*background: url(../images/b_s.gif);*/
            border:0;
            width: 72px;
            color: #000000;
            height: 21px;
        }
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
            color:#000000;
            font-size:13px;
            padding:0;
            margin:0;
        }
        ul{
        	list-style: none;
        	vertical-align: middle;
            /*line-height:45px;*/
            color: #333333;
        }

    </style>

    <script type="text/javascript">
    $(document).ready(function(){
    	$("#main").css("height",document.documentElement.clientHeight-50);
    });
	function doQuery () {
		var form = document.commentInforForm;
		form.action="<c:url value='/bbs/commentInfor'/>.do?method=myList";
		form.submit();
	}
	function loadSearchTab(){
		$.ajax({
			url: '/bbs/commentInfor.do?method=myList',
			data: $("#commentInforForm").serialize(),
			cache: false,
			type: "GET",
			dataType: "html",
			beforeSend: function (xhr) {
			},
			complete : function (req, err) {
				$("#tabs-3").empty().html(req.responseText);
			}
		});		
	}	
</script>

</head>
<base target="_self" />
<body  style="border:1px solid #0DE8F5;border-radius: 5px">

<div id="main">
    <%--<div id="title">我的评论</div>
    --%><form:form commandName="commentInforVo" action="<c:url value='/bbs'/>/commentInfor.do?method=myList" method="post" id="commentInforForm" name="commentInforForm">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
            <td height="40" valign="middle" style="border-bottom:1px #44c2c5 solid;">
              &nbsp;  
                论题标题：
                <form:input path="thesisTitle" size="15"/>&nbsp; &nbsp;
                内容：
                <form:input path="replyContent" size="15"/>&nbsp;&nbsp;
                昵称：
                <form:input path="nickName" size="10"/>&nbsp;&nbsp;
                评论时间：
                <form:select path="replyDateAsc">
                    <form:option value="0">降序</form:option>
                    <form:option value="1">升序</form:option>
                </form:select>

                <span class="font_black">
				     <a href="javascript:;" onClick="loadSearchTab(); return false;">
                     <input type="button" value="GO" width="23" height="19" border="0" align="absmiddle"  style=" cursor:pointer; "/>
                    </a>
                </span>
            </td>
        </tr>
    </table>
    </form:form>
    <c:forEach var="comment" items="${_Comments}" varStatus="status">
        <div class="aComment">
            <dl>
                <dt>
                <ul>
                    <%--<li>
						<img src="<c:url value="/images"/>/bbs/default_person_s.gif" style="border:3px white solid;" height="110" alt="头像"/>
                    </li>--%><br/>
                    <li>昵称：<span id="nickName${comment.commentId}">${comment.nickName}</span></li>
                    <li>评论时间：<fmt:formatDate value="${comment.replyDate}" pattern="yyyy-MM-dd HH:mm"/></li>
                </ul>
                </dt>
                <dd>
                    <div class="quote">
                        对应主题：<a target="_blank" style="color:red;" href="<c:url value='/bbs/commentInfor'/>.do?method=edit&thesisId=${comment.thesisInfor.thesisId}">
                            ${comment.thesisInfor.title}
                    </a>
                    </div>
                    <div class="clear"></div>
                    <div id="comment${comment.commentId}">
                    	${comment.replyContent}
                    	<c:if test="${!empty comment.imgAttachment}">
	                		<ul>
		                		<c:forEach var="comImg" items="${fn:split(comment.imgAttachment,'|')}">
		                			<c:if test="${!empty comImg}">
		                				<li>
			                				<a href="${comImg}" target="_blank" title="点击查看原图" >
				                				<img src="/${comImg}" onload="setImgSize(this,400,400)" style="border:0px;" alt="点击查看原图"/>
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
   </div>

</body>
</html>