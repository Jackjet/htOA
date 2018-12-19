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
            <li id="tabs_li_1" class="tabs_li"><a href="#tabs-grant">文档大全</a></li>

        </ul>
        <div id="tabs-grant">
            <iframe  id="grant_iframe" class="tab_iframe" allowtransparency=true src="" frameborder="0" scrolling="yes" width="100%" ></iframe>
        </div>
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
        <%--alert("${param.flowId}");--%>
        var browser = navigator.appName;
        var heightAdjust = 23;
        var widthAdjust = 7+20;
//            setFrameHeight()
        if (browser != "Microsoft Internet Explorer") {
            heightAdjust = 18;
            widthAdjust = 9+20;
        }
        $( "#tabs" ).tabs({
            cache: true, // This ensures selecting a tab does not refresh the page
            load: function(event, ui) {
                // Keep links, form submissions, etc. contained within the tab
                //$(ui.panel).hijack();
                // Adjust the IFRAME size correctly in the browser window
                $('.tab_iframe').width((ViewPortWidth() - widthAdjust));
                $('.tab_iframe').height((ViewPortHeight() - heightAdjust));
            }
        });

        $("#tabs_ul li").click(function(){
            var flag = false;
            var tabId = $(this).attr("id");
            var selectedItems = $("#tabs_ul li.selected");
            if(selectedItems.length > 0){
                var selectedItemId = selectedItems[0].id;
                if(tabId != selectedItemId){ // 尚未选中
                    flag = true;
                }
            } else {
                flag = true;
            }
            if(flag){
                $(this).addClass("hover");
                $(this).siblings().removeClass("hover");

                var url = "";
                if(tabId == "tabs_li_1"){
                    url = "/document/treeDocument.jsp";
                }
                $("#grant_iframe").attr("src", url);
            }
        });
            $( "#tabs" ).tabs({ selected: 0 });
            $("#tabs_li_1").click();




        // Adjust tab header width and visible iframe window height and width after the window is resized
        $(window).resize(function(){
            $("#grant_iframe").width((ViewPortWidth() - widthAdjust));
            $('#grant_iframe').height((ViewPortHeight() - $('#tabs_ul').height() - heightAdjust));
        });
        $('#grant_iframe').height((ViewPortHeight() - $('#tabs_ul').height() - heightAdjust));
        // Hover states on the static widgets
//        $( "#dialog-link, #icons li" ).hover(
//            function() {
//                $( this ).addClass( "ui-state-hover" );
//            },
//            function() {
//                $( this ).removeClass( "ui-state-hover" );
//            }
//        );
    });

    //        function setFrameHeight(){
    //            $(".tab_iframe").css("height",document.documentElement.clientHeight-80);
    //
    //        }

</script>
</html>
