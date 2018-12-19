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
        body{
            font: 14px "黑体", sans-serif;
            margin: 50px;
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
        .tabs_li{
            padding:5px 3px 5px 3px!important;
            margin: 0 0 -3px 0!important;
        }
        .tabs_li a{
            font-size:16px;
            font-weight:normal;
        }
        .tabs_li a:hover{
            font-size:18px;
            font-weight:normal;
        }
    </style>
</head>
<body style="background-color:transparent;width: 100%;margin:0;overflow-x: hidden;overflow-y: auto">
<!-- Tabs -->
    <div id="tabs" >
        <ul id="tabs_ul" style="margin-left: 80px;height: 5px;display: none">
            <li id="tabs_li_1" class="tabs_li"><a href="#tabs-grant">回收站</a></li>

        </ul>
        <div id="tabs-grant">
            <iframe  id="grant_iframe" class="tab_iframe" allowtransparency=true src="" frameborder="0" scrolling="yes" width="100%" ></iframe>
        </div>

        <%--<iframe  id="tabs-2" class="tab_iframe" allowtransparency=true src="workflow/instanceInfor.jsp?flowId=85" frameborder="0" scrolling="yes" width="100%" ></iframe>--%>
        <%--<iframe  id="tabs-3" class="tab_iframe" allowtransparency=true src="workflow/instanceInfor.jsp?flowId=86" frameborder="0" scrolling="yes" width="100%" ></iframe>--%>
        <%--<iframe  id="tabs-4" class="tab_iframe" allowtransparency=true src="workflow/instanceInfor.jsp?flowId=87" frameborder="0" scrolling="yes" width="100%" ></iframe>--%>
    </div>
</body>
<script>

    function ViewPortWidth() {
        var width = 0;

        if ((document.documentElement) && (document.documentElement.clientWidth)) {
            width = document.documentElement.clientWidth;
        } else if ((document.body) && (document.body.clientWidth)) {
            width = document.body.clientWidth;
        } else if (window.innerWidth) {
            width = window.innerWidth;
        }
        return width;
    }

    // Returns height of viewable area in the browser
    function ViewPortHeight() {
        var height = 0;
        if (window.innerHeight) {
            height = window.innerHeight;}
        else if ((document.documentElement) && (document.documentElement.clientHeight)) {
            height = document.documentElement.clientHeight;
        }
        return height;
    }

    $(function() {

        var browser = navigator.appName;
        var heightAdjust = 23;
        var widthAdjust = 7+20;

        if (browser != "Microsoft Internet Explorer") {
            heightAdjust = 18;
            widthAdjust = 9+20;
        }
        $( "#tabs" ).tabs({
            cache: true, // This ensures selecting a tab does not refresh the page
            load: function(event, ui) {
                $('.tab_iframe').width((ViewPortWidth() - widthAdjust));
                $('.tab_iframe').height((ViewPortHeight() - $('#tabs_ul').height()/* - heightAdjust*/));
            }
        });



                $(this).addClass("hover");
                $(this).siblings().removeClass("hover");

                var url = "/workflow/deletedInstance.jsp";

                $("#grant_iframe").attr("src", url);


        $(window).resize(function(){
            $("#grant_iframe").width((ViewPortWidth() - widthAdjust));
            $('#grant_iframe').height((ViewPortHeight() - $('#tabs_ul').height() - heightAdjust));
        });
        $('#grant_iframe').height((ViewPortHeight() - $('#tabs_ul').height() - heightAdjust));

    });



</script>
</html>
