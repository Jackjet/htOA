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
            font-family:  "黑体" ;

        }
        body{
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
        .main-ul{
            list-style: none;
            margin:0px;padding:0px
        }
        .main-ul li{
            margin-bottom: 5px;
            margin-left:0;

        }
        .main-ul a{
            text-decoration:none;
            font-size: 14px;
            color:white;

        }
    </style>


</head>
<body style="background-color:transparent;width: 100%;overflow-x: hidden;overflow-y: hidden">

    <iframe id="grant_iframe" style="border: 0px solid #0DE8F5;border-radius: 5px" class="tab_iframe" allowtransparency=true  frameborder="0" scrolling="yes" width="99%" height="99%" src="/meeting/meetInfor.do?method=viewIndex"></iframe>


</body>

</html>
