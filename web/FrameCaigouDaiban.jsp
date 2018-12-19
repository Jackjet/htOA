<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2017/11/2
  Time: 13:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Title</title>
    <link href="css/base/jquery-ui-1.9.2.custom.css" rel="stylesheet">
    <script src="js/jquery-1.8.3.js"></script>
    <script src="js/jquery-ui-1.9.2.custom.js"></script>

    <style>
        html,
        body {
            overflow: hidden;
            height: 100%;
            margin: auto;
            padding: auto;
            font: 14px/1.8 Georgia, Arial, Simsun;

            background-size: cover;
            font-family: "黑体";

        }

        body {
            font: 14px "黑体", sans-serif;
            /*margin: 50px;*/
        }

        .demoHeaders {
            margin-top: 2em;
        }

        #dialog-link {
            padding: .4em 1em .4em 20px;
            text-decoration: none;
            position: relative;
        }

        #dialog-link span.ui-icon {
            margin: 0 5px 0 0;
            position: absolute;
            left: .2em;
            top: 50%;
            margin-top: -8px;
        }

        #icons {
            margin: 0;
            padding: 0;
        }

        #icons li {
            margin: 2px;
            position: relative;
            padding: 4px 0;
            cursor: pointer;
            float: left;
            list-style: none;
        }

        #icons span.ui-icon {
            float: left;
            margin: 0 4px;
        }

        .fakewindowcontain .ui-widget-overlay {
            position: absolute;
        }

        .ui-tabs .ui-tabs-panel {
            display: block;
            border-width: 0;
            padding: 0em 0em;
            background: none;
        }

        .tabs_li {
            padding: 5px 3px 5px 3px !important;
            margin: 0 0 -3px 0 !important;
        }

        .tabs_li a {
            font-size: 16px;
            font-weight: normal;
        }

        .tabs_li a:hover {
            font-size: 18px;
            font-weight: normal;
        }

        .main-ul {
            list-style: none;
            margin: 0px;
            padding: 0px
        }

        .main-ul li {
            margin-bottom: 5px;
            margin-left: 0;

        }

        .main-ul a {
            text-decoration: none;
            font-size: 14px;
            color: #bcbebc;

        }
    </style>
</head>
<body style="background-image: url('/img/bgIn.png');width: 100%;overflow-x: hidden;overflow-y: hidden">
<!-- Tabs -->

<div class="module_div"
     style="height:90%;padding:25px 25px 25px 36px;border:0px solid #0DE8F5;margin-right: 50px;overflow-x: hidden;overflow-y: auto">

    <div class="bq" id="bq-1" style="color:#8f96a4;font-size: 16px">待办事宜</div>
    <%--<c:if test="${_SYSTEM_USER.userType != 2 && _SYSTEM_USER.userType != 3}">
            --%>
    <br/>
    <c:if test="${_NORMAL_USER}">
        <!-- 工作工作跟踪 -->
        <ul class="main-ul" style="height: 0px" id="taskUl">
            <li></li>

        </ul>
    </c:if>
    <br/>

</div>
</body>

</html>
<script>
    function disTaskTip(taskId) {
        if (confirm("请确认已知悉本提醒，之后将不再显示！")) {
            $.ajax({
                url: '/supervise/superviseInfor.do?method=saveReadStatus&taskId=' + taskId,
                cache: false,
                type: "GET",
                //dataType : "json",
                async: true,
                beforeSend: function (xhr) {

                },
                complete: function (req, msg) {
                    //var msg = eval("(" + req.responseText + ")");
                    //alert(msg+"--");
                    window.location.reload();

                },
                success: function (msg) {
                    window.location.reload();
                }
            });
        }
    }
    jQuery().ready(qidong());
    function qidong() {

        var contentgz6 = "";
        var contentgz7 = "";
        var contentgz8 = "";
        var contentgz9 = "";
        var contentgz10 = "";
        var contentgz11 = "";
        var contentgz12 = "";
        var contentgz13 = "";

        ajax6 = $.ajax(
            {
                url: "/ybpurchase/purchaseInfor.do?method=getNeedDealInstances",
                dataType: "json",
                type: "post",
                success: function (data) {
                    if (data._type == 0) {
                        $.each(data._Instances, function (i, n) {
                            contentgz6 += "<li><a href='/ybpurchase/purchaseInfor.do?method=open&type=19&packageId="+n.packageId+"' target='_blank'> [一般采购][<font color=red>请进行相关操作</font>]：" + n.packageName + " (" + n.startDate + ")</a></li>";
                        });
                    } else if (data._type == 1) {
                        $.each(data._Instances, function (i, n) {
//                            if(n.flowId.flowId == 1){
                            contentgz6 += "<li><a href='/ybpurchase/purchaseInfor.do?method=view&purchaseId="+n.purchaseId +"' target='_blank'> [一般采购][<font color=red>请进行相关操作</font>]：" + n.purchaseTitle + " (" + n.startTime + ")</a></li>";
//                            }
//                            if(n.flowId.flowId == 2){
//                                contentgz6 += "<li><a href='/ywpurchase/purchaseInfor.do?method=listchild&flowId=2' target='mainInfor'> [采购][<font color=red>请进行相关操作</font>]："+n.purchaseStr1+" ("+n.startTime+")</a></li>";
//                            }
//                            if(n.flowId.flowId == 3){
//                                contentgz6 += "<li><a href='/gcpurchase/purchaseInfor.do?method=listchild&flowId=3' target='mainInfor'> [采购][<font color=red>请进行相关操作</font>]："+n.purchaseStr1+" ("+n.startTime+")</a></li>";
//                            }
//                            if(n.flowId.flowId == 4){
//                                contentgz6 += "<li><a href='/lxpurchase/purchaseInfor.do?method=listchild&flowId=4' target='mainInfor'> [采购][<font color=red>请进行相关操作</font>]："+n.purchaseStr1+" ("+n.startTime+")</a></li>";
//                            }
                        });
                    }
                }
            });
        ajax7 = $.ajax(
            {
                url: "/ywpurchase/purchaseInfor.do?method=getNeedDealInstances",
                dataType: "json",
                type: "post",
                success: function (data) {
                    if (data._type == 0) {
                        $.each(data._Instances, function (i, n) {
                            contentgz7 += "<li><a href='/ywpurchase/purchaseInfor.do?method=viewPackage&flowId=1&packageId=" + n.packageId + "' target='mainInfor'> [业务采购][<font color=red>请进行相关操作</font>]：" + n.packageName + " (" + n.startDate + ")</a></li>";
                        });
                    } else if (data._type == 1) {
                        $.each(data._Instances, function (i, n) {
                            contentgz7 += "<li><a href='/ywpurchase/purchaseInfor.do?method=view&purchaseId="+n.purchaseId+"' target='_blank'> [业务采购][<font color=red>请进行相关操作</font>]：" + n.purchaseTitle + " (" + n.startTime + ")</a></li>";
                        });
                    }
                }
            });
        ajax8 = $.ajax(
            {
                url: "/gcpurchase/purchaseInfor.do?method=getNeedDealInstances",
                dataType: "json",
                type: "post",
                success: function (data) {
                    if (data._type == 0) {
                        $.each(data._Instances, function (i, n) {
                            contentgz8 += "<li><a href='/gcpurchase/purchaseInfor.do?method=viewPackage&flowId=1&packageId=" + n.packageId + "' target='mainInfor'> [工程项目][<font color=red>请进行相关操作</font>]：" + n.packageName + " (" + n.startDate + ")</a></li>";
                        });
                    } else if (data._type == 1) {
                        $.each(data._Instances, function (i, n) {
                            contentgz8 += "<li><a href='/gcpurchase/purchaseInfor.do?method=view&purchaseId="+n.purchaseId+"' target='_blank'> [工程项目][<font color=red>请进行相关操作</font>]：" + n.purchaseTitle + " (" + n.startTime + ")</a></li>";
                        });
                    }
                }
            });
        ajax9 = $.ajax(
            {
                url: "/lxpurchase/purchaseInfor.do?method=getNeedDealInstances",
                dataType: "json",
                type: "post",
                success: function (data) {
                    if (data._type == 0) {
                        $.each(data._Instances, function (i, n) {
                            contentgz9 += "<li><a href='/ybpurchase/purchaseInfor.do?method=open&type=20&packageId="+n.packageId+"' target='_blank'> [零星工程维修][<font color=red>请进行相关操作</font>]：" + n.packageName + " (" + n.startDate + ")</a></li>";
                        });
                    } else if (data._type == 1) {
                        $.each(data._Instances, function (i, n) {
                            contentgz9 += "<li><a href='/lxpurchase/purchaseInfor.do?method=view&purchaseId="+n.purchaseId +"' target='_blank'> [零星工程维修][<font color=red>请进行相关操作</font>]：" + n.purchaseTitle + " (" + n.startTime + ")</a></li>";
                        });
                    }
                }
            });
        ajax10 = $.ajax(
            {
                url: "/review.do?method=getNeedDeal",
                dataType: "json",
                type: "post",
                success: function (data) {
                    $.each(data._Instances, function (i, n) {
                        contentgz10 += "<li><a href='/review.do?method=view&sanfangID=" + n.sanfangID + "' target='_blank'> [三方比价][<font color=red>请进行相关操作</font>]：" + n.sanfangTitle + " (" + n.startDate + ")</a></li>";
                    });
                },
                error:function (data) {
//                    alert('sf');
                }
            });
        ajax11 = $.ajax(
            {
                url: "/bid.do?method=getNeedDeal",
                dataType: "json",
                type: "post",
                success: function (data) {
                    $.each(data._Instances, function (i, n) {
                        contentgz11 += "<li><a href='/bid.do?method=view&bidInfoId=" + n.bidInfoId + "' target='_blank'> [招投标][<font color=red>请进行相关操作</font>]：" + n.projectName + " (" + n.startTime + ")</a></li>";
                    });
                },
                error:function (data) {
//                    alert('bid');
                }
            });
        ajax12 = $.ajax(
            {
                url: "/contract.do?method=getNeedDeal",
                dataType: "json",
                type: "post",
                success: function (data) {
                    $.each(data._Instances, function (i, n) {
                        contentgz12 += "<li><a href='/contract.do?method=view&contractID=" + n.contractID + "' target='_blank'> [合同变更][<font color=red>请进行相关操作</font>]：" + n.contractName + " (" + n.submitDate + ")</a></li>";
                    });
                },
                error:function (data) {
//                    alert('ht')
                }
            });
        ajax13 = $.ajax(
            {
                url: "/supplier.do?method=getNeedDeal",
                dataType: "json",
                type: "post",
                success: function (data) {
                    $.each(data._Instances, function (i, n) {
                        contentgz13 += "<li><a href='/supplier.do?method=view&supplierID=" + n.supplierID + "' target='_blank'> [供应商维护][<font color=red>请进行相关操作</font>]：" + n.supplierName + " (" + n.serviceDetail + ")</a></li>";
                    });
                },
                error:function (data) {
//                    alert('su')
                }
            });


//        $.when(ajax6,ajax7,ajax8,ajax9).done(function () {
        $.when(ajax6, ajax7, ajax8, ajax9,ajax10,ajax11,ajax12,ajax13).done(function () {
            //所做操作
            if (contentgz6 + contentgz7 + contentgz8 + contentgz9+contentgz10 +contentgz11+contentgz12+contentgz13== "") {
//                alert("11")
                $('#taskUl').html("<font color='#0DE8F5'>无</font>")
            } else {
//                alert("22"+contenttpwj+contentpy+contenthy+contentgz1+contentgz2+contentgzl)
//                $('#taskUl').html(contentgz6+contentgz7+contentgz8+contentgz9);
                $('#taskUl').html(contentgz6 + contentgz7 + contentgz8 + contentgz9 + contentgz10 + contentgz11 + contentgz12+contentgz13);
            }


        });
    }

    // 各种浏览器兼容
    var hidden, state, visibilityChange;
    if (typeof document.hidden !== "undefined") {
        hidden = "hidden";
        visibilityChange = "visibilitychange";
        state = "visibilityState";
    } else if (typeof document.mozHidden !== "undefined") {
        hidden = "mozHidden";
        visibilityChange = "mozvisibilitychange";
        state = "mozVisibilityState";
    } else if (typeof document.msHidden !== "undefined") {
        hidden = "msHidden";
        visibilityChange = "msvisibilitychange";
        state = "msVisibilityState";
    } else if (typeof document.webkitHidden !== "undefined") {
        hidden = "webkitHidden";
        visibilityChange = "webkitvisibilitychange";
        state = "webkitVisibilityState";
    }

    // 添加监听器，在title里显示状态变化
    document.addEventListener(visibilityChange, function() {
        //document.title = document[state];
        if(document.hidden){

//            document.title = '隐藏本页';
        }else{
//            document.title ='显示本页';
            qidong();
        }
    }, false);


</script>