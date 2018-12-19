<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import="com.kwchina.core.cms.util.InforConstant" %>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>
<%--<!DOCTYPE html>--%>
<html>
<head>

    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
    <%--<meta http-equiv="X-UA-Compatible" content="IE=9" />--%>
    <link href="/js/bootstrap.min.css" rel="stylesheet" type="text/css" />
    <%--<link href="/css/font.css" rel="stylesheet" type="text/css" />--%>
    <link href='http://cdn.webfont.youziku.com/webfonts/nomal/113175/46714/5a5c5b5df629d81af4c5d100.css' rel='stylesheet' type='text/css' />
    <title>海通网络智能办公系统</title>

    <style>
        /*鼠标移上去*/
        .nav > li:hover .dropdown-menu { display: block;
            /*background-color: #052532;*/
            color:white;
            z-index: 999;
        }

        .dropdown-submenu{
            margin-bottom: 8px;
        }
        /*下拉菜单向右*/
        .dropdown-submenu>.dropdown-menu {
            text-align: center;
            top: 0;
            left: 100%;
            min-width: 16px;
            margin-top: -90px;
            margin-left: -1px;
            margin-right: -2px;
            -webkit-border-radius: 0 6px 6px 6px;
            -moz-border-radius: 0 6px 6px;
            border: rgba(13, 232, 245, 0.62) 2px solid;
            /*border-radius: 0 6px 6px 6px;*/
            box-shadow: 5px 0 5px rgba(49, 211, 255, 0.20) inset,   /*左边阴影*/
            0 -5px 5px rgba(49, 211, 255, 0.20) inset,  /*上边阴影*/
            -5px 0px 5px rgba(49, 211, 255, 0.20) inset,  /*右边阴影*/
            0 5px 5px rgba(49, 211, 255, 0.20) inset;
            /*box-shadow: -20px -2px 15px -13px #ff8c1a inset;*/
            z-index: 999;
        }
        /*右边有个箭头*/
        /*.dropdown-submenu>a:after {*/
        /*display: block;*/
        /*content: " ";*/
        /*float: right;*/
        /*width: 0;*/
        /*height: 0;*/
        /*border-color: transparent;*/
        /*border-style: solid;*/
        /*border-width: 5px 0 5px 5px;*/
        /*border-left-color: #ccc;*/
        /*margin-top: 5px;*/
        /*margin-right: -10px;*/
        /*}*/
        .dropdown-menu .divider{
            height:1px;
            margin:5px 0;
            overflow:hidden;
            background-color: rgba(255, 249, 254, 0.96);
        }

        .navbar-left{
            background-color: #00688f;
        }
        .navbar-left li a{
            color: #cfcfcf;

        }
        /*link:连接平常的状态*/

        /*visited:连接被访问过之后*/
        /*hover:鼠标放到连接上的时候*/

        /*active:连接被按下的时候*/
        .navbar-left li a{
            background-color: #00688f;
        }
        .navbar-left li a:link{
            background-color: #00688f;
        }
        .navbar-left li a:active{
            background-color: #01b4f6;
        }
        .navbar-left li a:hover{
            background-color: #01b4f6;
        }


        .navbar-left li ul{
            background-color: #062434;
        }
        .navbar-left li ul li a{
            color:#1891bd;
            background-color: #01b4f6;
        }

        .navbar-left li ul li a:link{
            background-color: inherit;
            /*color: white;*/
        }
        .navbar-left li ul li a:hover{
            background-color: inherit;
            /*color: white;*/
        }
        .navbar-left li ul li a:hover{
            background-color: inherit;
            color: white;
        }


        .divider{
            background-color: rgba(0, 180, 246, 0.4) !important;
            /*height: 0.5em;*/
        }

    </style>
    <style>
        .indexTopTd{
            text-align: center;
        }
        #TopQuickContainer{
            position:relative;
            height:190px;
            width:90%;
            top:0px;
            /*margin-left: 7%;*/
        }
        .indexTopQuick{
            position: absolute;
            top:20px;
            border: 0px solid red;
            color: #bcbcbc;
            font-weight: bold;
            font-size: 45px;
            /*cursor: hand*/
        }

        .indexTopQuick span{
            position: absolute;
            left:70px;
            top:10px;
            /*cursor: pointer;*/

        }

        #indexTopQuick1{
            /*left:180px;*/
            left: 25%;
        }
        #indexTopQuick2{
            /*left:330px;*/
            left: 38%;
        }
        #indexTopQuick3{
            /*left:485px;*/
            right: 38%;
            z-index: 1;
        }
        #indexTopQuick4{
            /*left:545px;*/
            right: 25%;
            z-index: 0;
        }

        #indexTopQuick4 span{
            left:155px;
        }
        #indexTopLogo{
            position: relative;
            top:130px;
            /*left:300px;*/
        }

        #indexDealContainer{
            width: 100%;
            height: 100%;
            border: 0 solid red;
            overflow-x: hidden;
            overflow-y: auto;
        }

        /*通过CSS来控制滚动条的样式，代码如下：*/
        /*定义滚动条轨道*/
        /*!*#indexDealContainer*!::-webkit-scrollbar-track*/
        /*{
            background-color: #22394b;
            -webkit-box-shadow: inset 0 0 6px rgba(255, 123, 215, 0.22);
        }
        !*定义滚动条高宽及背景*!
        ::-webkit-scrollbar
        {
            width: 5px;
            height: 200px;
            background-color: rgba(192, 163, 246, 0.34);
        }
        !*定义滚动条*!
        ::-webkit-scrollbar-thumb
        {
            background-color: #8b8b8b;
            border-radius: 10px;
        }*/
        /* .inded td{
             -khtml-opacity:0.6;-moz-opacity:0.6;filter:alpha(opacity=60);filter:"alpha(opacity=60)";opacity:0.6;
         }*/


        #indexDealTab td{
            /*filter:alpha(Opacity=80);-moz-opacity:0.5;opacity: 0.5;*/
            /**背景半透明**/
            /*background: #fdfffb;*/
            /*filter:alpha(opacity=60);*/
            /*-moz-opacity:0.6;*/
            /*-khtml-opacity: 0.6;*/
            /*opacity: 0.6;*/
            height:200px;
            z-index: 33;
            background-color: rgba(255, 255, 255, 0.05);
            color: white;
        }


        #indexMainRight{
            color: white;
            vertical-align: middle;
        }

        .indexMainRightItem{
            width: 100%;
            text-align: center;
            height:80px;
            cursor: pointer;
            margin-bottom: 50px;
        }
        .item_img{
            width: 20%;
            margin-bottom: 8px;
        }
        .item_char{
            font-size: 12px;
        }

    </style>

    <style type="text/css">
        html,
        body {
            overflow: hidden;
            height: 100%;
            margin: auto;
            padding: auto;
            font: 14px/1.8 Georgia, Arial, Simsun;
            /*filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='./img/bg.png',sizingMethod=scale);*/
            /*-ms-filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='./img/bg.png', sizingMethod='scale');*/

            background-image: url(/img/bg.png);
            background-color: #153b49;
            background-size: cover;
            /*background-size:100%;*/
            font-family:  "黑体" ;



        }

        html {
            _padding: 110px 0;
            scrollbar-face-color: rgba(16, 13, 18, 0.61);/*滚动条颜色*/
            scrollbar-highlight-color:#000;
            scrollbar-3dlight-color:#000;
            scrollbar-darkshadow-color:#000;
            scrollbar-Shadow-color:#adadad;/*滑块边色*/
            scrollbar-arrow-color:rgba(0,0,0,0.4);/*箭头颜色*/
            scrollbar-track-color:#eeeeee;/*背景颜色*/
        }

        #bottomContainer{
            /*position: relative;*/
            height: 100%;
        }



        #hd {
            /*position: relative;*/
            top: 0;
            left: 0;
            width: 100%;
            height: 50px;
            z-index: -1;
            /*pointer-events: none; position:fixed;*/

        }

        #topTab{
            color: #6AD7EF;
            font-size: 14px;
        }

        #topTab #opTd{
            cursor: pointer;
        }

        #bd {
            position: absolute;
            top: 100px;
            bottom: 50px;
            left: 0;
            overflow: hidden;
            width: 100%;
            height: 100%;
            z-index: 0;
            border: 0px solid red;
            background-color: rgba(255, 255, 255, 0);
        }

        #topMenu{
            position: absolute;
            /*margin-top:0;*/
            /*top:30%;*/
            left:0;
            top: 20%;
            border: 0px solid red;
            z-index: 10000;
            padding-top:0px;
            width:150px;
        }

        #topMain{
            position: absolute;
            top:0px;
            right:10px;
            left:201px;
            bottom: 50px;
            border: 0px solid black;
            z-index: 99;
        }




        #side {
            position: absolute;
            top: 0;
            left: 0;
            bottom: 0;
            overflow: auto;
            width: 200px;
            _height: 100%;
        }

        #main {
            position: absolute;
            z-index: 0;
            _position: static;
            top: 0;
            bottom: 0;
            left: 156px;
            right:30px;
            overflow: hidden;
            _overflow: hidden;
            /*_height: 100%;*/
            _margin-left: 201px;
        }

        #content {
            _overflow: auto;
            _width: 100%;
            _height: 100%;
        }

        #rightFrame{
            border: 1px #FF4E4E;
            width: 100%;
            height: 100%;
            overflow-y: hidden;
            overflow-x: hidden;
        }

        #ft {
            position: absolute;
            bottom: 0;
            left: 0;
            width: 100%;
            height: 8%;
            background: url(/img/logo-bottom1.png) center center no-repeat;
            background-size:40% 45%;
        }
        .homeUL {
            list-style: none;
        }

    </style>

    <style>
        /*滚动条*/
        ::-webkit-scrollbar{
            width:4px;
            height:10px;
        }
        ::-webkit-scrollbar-track{
            background: #E6E6E6;
        }
        ::-webkit-scrollbar-thumb{
            background: #747474;
            border-radius:2px;
        }
        ::-webkit-scrollbar-corner{
            background: #E6E6E6;
        }

        .main-ul{
            list-style: none;
            margin:0px;padding:0px
        }
        .main-ul li{
            margin-bottom: 0px;
            margin-left:0;

        }
        .main-ul a{
            text-decoration:none;
            font-size: 14px;
            /*color: rgba(255, 255, 255, 0.7);*/
            color: #bcbcbc;!important;
        }

        .homeDIV{
            height:70%;
            width: 100%;
            padding-top: 5px;
            padding-left: 35px;
            padding-right: 20px;
            padding-bottom: 15px;
        }
        .bq{
            font-size: 16px;
            width: 120px;
            display: inline-block;  /*浮动布局*/
            margin-left: 0px;
            margin-top:0px;
            padding-bottom:7px;
        }
        .more{
            font-size: 16px;
            position: relative;
            display: inline-block;
            color: #2643ff;
            /*right:3%!important;*/
            margin-left: 50%;
            /*float: right;*/
            /*padding-right:3%;!important;*/

            margin-top:0px;

        }

        .module_div{overflow-x: auto;!important; white-space:nowrap;!important; }

        /*.module_div ul li{ display:inline-block; display:-moz-inline-stack; *display:inline; }*/
        a:focus{outline:none;!important; color:white!important; }
        a:focus{outline:none;!important; color: #49ffc5 !important; }
    </style>
    <style>
        @media \0screen\,screen\9 {/* 只支持IE6、7、8 */
            #indexDealTab td{
                background-color: #ffffff;
                filter:Alpha(opacity=5);
                position:static; /* IE6、7、8只能设置position:static(默认属性) ，否则会导致子元素继承Alpha值 */
                *zoom:1; /* 激活IE6、7的haslayout属性，让它读懂Alpha */
            }
            body{
                filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(
                        src='/img/bg.png',
                        sizingMethod='scale');
                -ms-filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(
                        src='/img/bg.png',
                        sizingMethod='scale');
            }
            #indexDealTab td div{
                position: relative;/* 设置子元素为相对定位，可让子元素不继承Alpha值 */
            }
            #ft{
                position: absolute;
                bottom: 0;
                left: 0;
                background: url(/img/logo-bottom2.png) center center no-repeat;
            }
        }

        .nav > li > a:focus, .nav > li > a:hover{
            /*background-color: transition ;*/
        }
    </style>


</head>

<body onresize="setFrameHeight();">

<div id="bottomContainer">
    <div id="hd">
        <table id="topTab" width="100%">
            <tr>
                <td>
                    <span style="margin-bottom: 10px">您好！${_GLOBAL_PERSON.personName}</span>
                    <div id="time">
                        <span class="time_left">
                            <span class="time_right">
                                <span id="date">
                                    <script>
                                        setInterval("document.getElementById('time').innerHTML=new Date().toLocaleString()+' 星期'+'日一二三四五六'.charAt(new Date().getDay());",1000);
                                    </script>
                                </span>
                            </span>
                        </span>
                    </div>
                </td>
                <%-- <td></td>
                 <td align="right" id="opTd" width="28%">

                 </td>--%>
            </tr>
        </table>
    </div>
    <style>
        #oppTd{
            width: 30%;
            height:100px;
            text-align: right;
            z-index: 1000;right: 10px;position: absolute;top:10px;

        }
        #oppTd span{
            cursor: pointer;
            color: #6AD7Ef;
        }
    </style>

    <div id="oppTd" >
        <span title="主页" onclick="toHome();"><img width="5%"  src="/img/zhuye.png"> 主页</span>&nbsp;
        <span title="邮箱" onclick="openEmail();"><img width="5%"  src="/img/youxiang.png"> 邮箱</span>&nbsp;
        <function:viewIf alias="purchase" method="list">
        <span title="采购" onclick="openCaigou();"><img width="5%"  src="/img/zhuye.png"> 采购</span>&nbsp;
        </function:viewIf>
        <span title="文档平台" onclick="openWDPT();"><img width="4.6%"  src="/img/Edoc2.png"> Edoc2</span>&nbsp;
        <span title="原OA系统" onclick="openOldOA();"><img width="5%"  src="/img/原OA.png"> 原OA</span>&nbsp;
        <span title="注销" onclick="re_login();"><img width="5%"  src="/img/zhuxiao.png"> 注销</span>
    </div>

    <div id="topMenu">

        <c:if test="${_NORMAL_USER}">

            <ul class="nav navbar-left" style="width: 125px">
                <c:if test="${_NORMAL_USER}">

                    <%--1个人办公--%>
                    <li class="dropdown-submenu">
                        <a href="javascript:void(0)">
                            <img style="width: 20%;margin-right: 5px;margin-top: 0" src="img/grbg.png"><span>个人办公</span>
                        </a>

                        <ul class="nav nav-list dropdown-menu menuSideBar" style="margin-top: 0px">
                            <li id="tab1-1" mid="tab1-1" >
                                <a class="lia" tabindex="-1" href="javascript:void(0);" onclick="a('FramePersonalMsg.jsp',false)" >
                                    短讯息
                                </a>
                            </li>
                            <li class="divider"></li>
                            <li id="tab1-2" mid="tab1-2" funurl="/personal/schedule/calendarBaseList.jsp">
                                <a class="lia"  tabindex="-1" href="javascript:void(0);" onclick="a('FrameCalendar.jsp',false)">
                                    日程安排
                                </a>
                            </li>
                            <li class="divider"></li>
                            <function:viewIf alias="companyAddress" method="list">
                                <li id="tab1-3" mid="tab1-3" onclick="a('FrameTXL.jsp',false)">
                                    <a class="lia" tabindex="-1" href="javascript:void(0);" >
                                        公司通讯录
                                    </a>
                                </li>
                                <li class="divider"></li>
                            </function:viewIf>
                            <li id="tab1-4" mid="tab1-4" onclick="a('201805.htm',false)">
                                <a class="lia" tabindex="-1" href="javascript:void(0);">
                                    公司电话一览
                                </a>
                            </li>
                            <li class="divider"></li>
                            <li id="tab1-5" mid="tab1-5">
                                <a  tabindex="-1" href="/core/systemUserInfor.do?method=editPassword" target="_blank">
                                    修改密码
                                </a>
                            </li>
                            <li class="divider"></li>
                            <li id="tab1-6" mid="tab1-6" funurl="../core/editPerson.jsp">
                                <a  tabindex="-1" href="/core/personInfor.do?method=editPersonalInfor" target="_blank">
                                    修改个人信息
                                </a>
                            </li>
                            <li class="divider"></li>
                            <li id="tab1-6" onclick="modulesChange()">
                                <a  tabindex="-1" href="javascript:void(0)"  >
                                    修改主页模块
                                </a>
                            </li>
                        </ul>
                    </li>
                    <%--2日常办公--%>
                    <function:viewIf alias="dailyOffice" method="list">
                        <li class="dropdown-submenu">
                            <a onclick="personal_tab()" href="javascript:void(0)">
                                <img style="width: 20%;margin-right: 5px;margin-top: 0" src="img/rcbg.png"><span>日常办公</span>
                            </a>
                            <ul class="nav nav-list dropdown-menu menuSideBar" style="margin-top: 0px">
                                <function:viewIf alias="appImg" method="list">
                                    <li id="tab2-1" mid="tab2-1" onclick="a('FrameAppImg.jsp',false)">
                                        <a class="lia" tabindex="-1" href="javascript:void(0);">
                                            app首页图片
                                        </a>
                                    </li>
                                </function:viewIf>
                                <function:viewIf alias="meeting" method="list">
                                    <function:viewIf alias="appImg" method="list">
                                        <li class="divider"></li>
                                    </function:viewIf>

                                    <li id="tab2-2" mid="tab2-2" onclick="a('FrameMeeting.jsp',false)">
                                        <a  class="lia" tabindex="-1" href="javascript:void(0)">
                                            会务安排
                                        </a>
                                    </li>
                                </function:viewIf>
                                <function:viewIf alias="annouce" method="list">
                                    <function:viewIf alias="meeting" method="list">
                                        <li class="divider"></li>
                                    </function:viewIf>
                                    <li id="tab2-3" mid="tab2-3" onclick="a('FrameAnnouce.jsp',false)">
                                        <a class="lia" tabindex="-1" href="javascript:void(0);">
                                            公司公告
                                        </a>
                                    </li>

                                </function:viewIf>
                                <function:viewIf alias="companynews" method="list">

                                    <function:viewIf alias="annouce" method="list">
                                        <li class="divider"></li>
                                    </function:viewIf>
                                    <li id="tab2-4" mid="tab2-4" onclick="a('FrameCompanynews.jsp',false)">
                                        <a class="lia" tabindex="-1" href="javascript:void(0);">
                                            参考消息
                                        </a>
                                    </li>

                                </function:viewIf>
                                <function:viewIf alias="important" method="list">
                                    <function:viewIf alias="companynews" method="list">
                                        <li class="divider"></li>
                                    </function:viewIf>
                                    <li id="tab2-5" mid="tab2-5" onclick="a('FrameImportant.jsp',false)">
                                        <a class="lia" tabindex="-1" href="javascript:void(0);">
                                            每日情况
                                        </a>
                                    </li>

                                </function:viewIf>
                                <function:viewIf alias="addSms" method="list">
                                        <li class="divider"></li>
                                    <li id="tab2-6" mid="tab2-6" onclick="a('FrameMessage.jsp?msg=1',false)">
                                        <a class="lia" tabindex="-1" href="javascript:void(0);">
                                            手机短信发送
                                        </a>
                                    </li>
                                </function:viewIf>

                                <function:viewIf alias="querySms" method="list">
                                        <li class="divider"></li>
                                    <li id="tab2-7" mid="tab2-6" onclick="a('FrameMessage.jsp?msg=2',false)">
                                        <a class="lia" tabindex="-1" href="javascript:void(0);">
                                            手机短信记录
                                        </a>
                                    </li>
                                </function:viewIf>
                                <li class="divider"></li>
                                <li  onclick="a('FrameBBS.jsp?tabID=1',false)">
                                    <a class="lia" tabindex="-1" href="javascript:void(0);">
                                        主题论坛
                                    </a>
                                </li>
                            </ul>
                        </li>
                    </function:viewIf>
                    <%--常用资料--%>
                    <li class="dropdown-submenu" onclick="a('FrameCmsNomal.jsp',false)">
                        <a onclick="personal_tab()" href="javascript:void(0)" >
                            <img style="width: 20%;margin-right: 5px;margin-top: 0" src="img/cyzl.png"><span >常用资料</span>
                        </a>
                            <%--<ul class="nav nav-list dropdown-menu menuSideBar" style="margin-top: auto" >
                                <li id="normaldatali" mid="normaldatali" funurl="cms/normaldata.jsp">
                                    <a onclick="personal_tab()" href="javascript:void(0)">
                                        <span>常用资料</span>
                                    </a>
                                </li>
                            </ul>--%>
                    </li>
                    <%--3信息发布--%>
                    <c:if test="${!empty _CategoryMenu}">
                        <li class="dropdown-submenu">
                            <a onclick="personal_tab()" href="javascript:void(0)">
                                <img style="width: 20%;margin-right: 5px;margin-top: 0" src="img/xxfb.png"><span>信息发布</span>
                            </a>
                            <ul  class="nav nav-list dropdown-menu menuSideBar" style="margin-top: -160px">

                                <c:forEach var="category" items="${_CategoryMenu}" varStatus="index">
                                    <li  id="tab${index.index}" mid="tab${index.index}" onclick="a('FrameMsg.jsp?urlPath=${category.urlPath}',false)" >
                                        <a class="lia" tabindex="-1" href="javascript:void(0);">${category.categoryName}</a>
                                    </li>
                                    <c:if test="${!index.last}">
                                        <li class="divider"></li>
                                    </c:if>

                                </c:forEach>
                            </ul>
                        </li>
                    </c:if>
                    <%--4工作流--%>
                    <function:viewIf alias="workflow" method="list">
                        <li class="dropdown-submenu">
                            <a onclick="a('FrameWorkFlow.jsp?flowId=84',false)" href="javascript:void(0)">
                                <img style="width: 20%;margin-right: 5px;margin-top: 0" src="img/gzl.png"><span >工作流</span>
                            </a>
                            <ul class="nav nav-list dropdown-menu menuSideBar" style="margin-top: -104px">

                                <c:forEach var="flow" items="${_Flows}" varStatus="status">
                                    <c:if test="${flow.flowId<89}">
                                        <li id="tab${flow.flowId}" mid="tab${flow.flowId}" funurl="workflow/instanceInfor.jsp?flowId=${flow.flowId}">
                                            <a class="lia" tabindex="-1" href="javascript:void(0);" onclick="a('FrameWorkFlow.jsp?flowId=${flow.flowId}',false)">${flow.flowName}</a>
                                        </li>

                                        <c:if test="${!status.last}">
                                            <li class="divider"></li>
                                        </c:if>
                                    </c:if>

                                </c:forEach>
                                <li id="tab${flow.flowId}">
                                    <a class="lia" tabindex="-1" href="javascript:void(0);" onclick="a('FrameWorkFlow.jsp?flowId=80',false)">体系文件</a>
                                </li>
                             <function:viewIf alias="purchase" method="list">
                                <li class="divider"></li>
                                <li  id="tab11-1" mid="tab11-1" onclick="openCaigou()">
                                    <a class="lia" tabindex="-1" href="javascript:void(0);">采购申请</a>
                                </li>
                             </function:viewIf>


                                <c:if test="${_GLOBAL_PERSON.personId == 1}">
                                    <li class="divider"></li>
                                    <li>
                                        <a class="lia"  href="javascript:void(0);" onclick="a('FrameDeleteWorkFlow.jsp',false)">回收站</a>
                                    </li>
                                </c:if>

                                <function:viewIf alias="flowDefinition" method="list">
                                    <li class="divider"></li>
                                    <li>
                                        <a class="lia"  href="javascript:void(0);" onclick="a('FrameFlowDefinition.jsp',false)">流程定义</a>
                                    </li>
                                    <%--<li class="L22"><a id="f150" href="javascript:a('/workflow/listFlowDefinition.jsp','150',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="流程定义" src="images/menu/flowNam.gif"> 流程定义 </span></a></li>--%>
                                </function:viewIf>
                            </ul>
                        </li>
                    </function:viewIf>
                    <%--5文档大全--%>
                    <function:viewIf alias="document" method="list">
                        <li class="dropdown-submenu">
                            <a onclick="a('FrameTreeDocument.jsp',false)" href="javascript:void(0)">
                                <img style="width: 20%;margin-right: 5px;margin-top: 0" src="img/wddq.png"><span >文档大全</span>
                            </a>
                                <%-- <ul class="nav nav-list dropdown-menu menuSideBar" style="margin-top: auto" >
                                     <li id="documentli" mid="documentli" funurl="document/treeDocument.jsp">
                                         <a onclick="personal_tab()" href="javascript:void(0)">
                                             <span>文档大全</span>
                                         </a>
                                     </li>
                                 </ul>--%>
                        </li>
                    </function:viewIf>
                    <%--6工作跟踪--%>
                    <function:viewIf alias="supervise" method="list">
                        <li class="dropdown-submenu">
                            <a onclick="a('FrameTaskCategory1.jsp',false)" href="javascript:void(0)">
                                <img style="width: 20%;margin-right: 5px;margin-top: 0" src="img/gzgz.png"><span>工作跟踪</span>
                            </a>
                            <ul class="nav nav-list dropdown-menu menuSideBar" style="margin-top: -80px">
                                <function:viewIf alias="adminTask" method="list">
                                    <li id="tabc1" mid="tabc1" onclick="a('FrameTaskCategory1.jsp',false)">
                                            <%--<a id="f1221" href="javascript:a('/supervise/taskCategory.do?method=listBase&categoryType=1','1221',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="方针目标类" src="images/menu/@score.gif"> 方针目标类</span></a>--%>
                                        <a class="lia" tabindex="-1" href="javascript:void(0);">方针目标类</a>
                                    </li>
                                    <li class="divider"></li>
                                </function:viewIf>
                                <function:viewIf alias="departmentTask" method="list">
                                    <li id="tabc3" mid="tabc3" onclick="a('FrameTaskCategory3.jsp',false)">
                                            <%--<a id="f1223" href="javascript:a('/supervise/taskCategory.do?method=listBase&categoryType=3','1223',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="部门建设类" src="images/menu/@score.gif"> 部门建设类</span></a>--%>
                                        <a class="lia" tabindex="-1" href="javascript:void(0);">部门建设类</a>
                                    </li>
                                    <li class="divider"></li>
                                </function:viewIf>
                                <function:viewIf alias="partyTask" method="list">
                                    <li id="tabc2" mid="tabc2" onclick="a('FrameTaskCategory2.jsp',false)">
                                            <%--<a id="f1222" href="javascript:a('/supervise/taskCategory.do?method=listBase&categoryType=2','1222',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="党群类" src="images/menu/@score.gif"> 党群工作</span></a>--%>
                                        <a class="lia" tabindex="-1" href="javascript:void(0);">党群工作</a>
                                    </li>
                                    <li class="divider"></li>
                                </function:viewIf>
                                <function:viewIf alias="insideTask" method="list">
                                    <li id="tabc4" mid="tabc4" onclick="a('FrameTaskCategory4.jsp',false)">
                                            <%--<a id="f1224" href="javascript:a('/supervise/taskCategory.do?method=listBase&categoryType=4','1224',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="内控类" src="images/menu/@score.gif"> 内控类</span></a>--%>
                                        <a class="lia" tabindex="-1" href="javascript:void(0);">内控类</a>
                                    </li>
                                </function:viewIf>
                            </ul>
                        </li>
                    </function:viewIf>
                    <%--7系统维护--%>
                    <function:viewIf alias="baseMaintain" method="list">
                        <li class="dropdown-submenu">

                            <a onclick="personal_tab()" href="javascript:void(0)">
                                <img style="width: 20%;margin-right: 5px;margin-top: 0" src="img/xtwh.png"><span>系统维护</span>
                            </a>

                            <ul class="nav nav-list dropdown-menu menuSideBar" style="margin-top: -104px">
                                <function:viewIf alias="base" method="list">
                                    <%--<li class="L21"><a id="f46" href="javascript:a('/core/base/baseList.jsp','46',0);"><span><img height="17" border="0" align="absMiddle" width="19" alt="基本维护" src="images/menu/@unit.gif"> 基本维护 </span></a></li>--%>
                                    <li id="tab7-1" mid="tab7-1" onclick="a('FrameMROBase.jsp',false)">
                                        <a class="lia" tabindex="-1" href="javascript:void(0);">基本维护</a>
                                    </li>
                                    <li class="divider"></li>
                                </function:viewIf>
                                <function:viewIf alias="documentCategory" method="list">
                                    <li id="tab7-2" mid="tab7-2" onclick="a('FrameMRODocCategory.jsp')">
                                        <a class="lia" tabindex="-1" href="javascript:void(0);">文档分类</a>
                                    </li>
                                    <li class="divider"></li>
                                </function:viewIf>
                                <function:viewIf alias="cmsCategory" method="list">
                                    <li id="tab7-3" mid="tab7-3" onclick="a('FrameCmsInforCategory.jsp',false)">
                                        <a class="lia" tabindex="-1" href="javascript:void(0);">资讯分类</a>
                                    </li>
                                    <li class="divider"></li>
                                </function:viewIf>
                                <function:viewIf alias="taskCategory" method="list">
                                    <li id="tab7-4" mid="tab7-4" onclick="a('FrameMROTaskCategory.jsp',false)">
                                        <a class="lia" tabindex="-1" href="javascript:void(0);">工作跟踪分类</a>
                                    </li>
                                    <li class="divider"></li>
                                </function:viewIf>
                                <function:viewIf alias="loginLog" method="list">
                                    <li id="tab7-5" mid="tab7-5" onclick="a('FrameMROLoginLog.jsp',false)">
                                        <a class="lia" tabindex="-1" href="javascript:void(0);">登录日志</a>
                                    </li>

                                </function:viewIf>
                                <function:viewIf alias="loginLog" method="list">
                                    <li class="divider"></li>
                                    <li id="tab7-5" mid="tab7-5" onclick="a('FrameTemplate.jsp',false)">
                                        <a class="lia" tabindex="-1" href="javascript:void(0);">模板维护</a>
                                    </li>

                                </function:viewIf>
                            </ul>
                        </li>
                    </function:viewIf>
                    <%--8专题栏目--%>
                    <li class="dropdown-submenu">
                        <a onclick="a('images/sqyj.bmp',false)" href="javascript:void(0)">
                            <img style="width: 20%;margin-right: 5px;margin-top: 0" src="img/ztlm.png"><span>专题栏目</span>
                        </a>
                        <ul class="nav nav-list dropdown-menu menuSideBar" style="margin-top: -362px">
                            <li id="tab8-1" mid="tab8-1" onclick="a('FrameSQYJ.jsp',false)">
                                <a class="lia" tabindex="-1" href="javascript:void(0);">上汽愿景</a>
                            </li>

                            <li class="divider"></li>
                            <li id="tab8-3" mid="tab8-3" onclick="a('FrameCmsHonor.jsp',false)">
                                <a class="lia" tabindex="-1" href="javascript:void(0);">光荣榜</a>
                            </li>
                            <li class="divider"></li>
                            <li id="tab8-4" mid="tab8-4" onclick="a('FrameCmsNewpaper.jsp',false)">
                                <a class="lia" tabindex="-1" href="javascript:void(0);">上级报刊</a>
                            </li>
                            <li class="divider"></li>
                            <li  id="tab8-5" mid="tab8-5" onclick="a('FrameCmsWarrant.jsp',false)">
                                <a class="lia" tabindex="-1" href="javascript:void(0);">常用凭证</a>
                            </li>
                            <li class="divider"></li>
                            <li  id="tab8-6" mid="tab8-6" onclick="a('FrameQYWH.jsp',false)">
                                <a class="lia" tabindex="-1" href="javascript:void(0);">企业文化</a>
                            </li>
                            <li class="divider"></li>
                            <li  id="tab8-7" mid="tab8-7" onclick="a('FrameCmsJob.jsp',false)">
                                <a class="lia" tabindex="-1" href="javascript:void(0);">岗位改进</a>
                            </li>
                            <li class="divider"></li>
                            <li  id="tab8-8" mid="tab8-8" onclick="a('FrameCmsEnsystem.jsp',false)">
                                <a class="lia" tabindex="-1" href="javascript:void(0);">企业内控体系建设</a>
                            </li>
                            <li class="divider"></li>
                            <li  id="tab8-9" mid="tab8-9" onclick="a('FrameCmsPolitical.jsp',false)">
                                <a class="lia" tabindex="-1" href="javascript:void(0);">党员承诺公示栏</a>
                            </li>
                        </ul>
                    </li>
                    <%--9投票问卷--%>
                    <li class="dropdown-submenu">
                        <a onclick="a('FrameTopic.jsp?type=0',false)" href="javascript:void(0)">
                            <img style="width: 20%;margin-right: 5px;margin-top: 0" src="img/tpwj.png"><span>投票问卷</span>
                        </a>
                        <ul class="nav nav-list dropdown-menu menuSideBar" style="margin-top: -107px">
                            <li  id="tab9-1" mid="tab9-1" onclick="a('FrameTopic.jsp?type=0',false)">
                                <a class="lia" tabindex="-1" href="javascript:void(0);">投票</a>
                            </li>
                            <li class="divider"></li>
                            <li  id="tab9-2" mid="tab9-2" onclick="a('FrameTopic.jsp?type=1',false)">
                                <a class="lia" tabindex="-1" href="javascript:void(0);">问卷</a>
                            </li>
                            <li class="divider"></li>
                            <li  id="tab9-3" mid="tab9-3" onclick="a('FrameTopic.jsp?type=2',false)">
                                <a class="lia" tabindex="-1" href="javascript:void(0);">评优系统</a>
                            </li>

                        </ul>
                    </li>
                    <%--10主题活动--%>
                    <c:if test="${_NORMAL_USER || _TMP_USER}">
                        <li class="dropdown-submenu" onclick="a('FrameClubInfor.jsp',false)">
                            <a onclick="personal_tab()" href="javascript:void(0)">
                                <img style="width: 20%;margin-right: 5px;margin-top: 0" src="img/zthd.png"><span>主题活动</span>
                            </a>
                                <%--<ul class="nav nav-list dropdown-menu menuSideBar" style="margin-top: auto">
                                    <li  id="tab10-1" mid="tab10-1" funurl="club/listClubInfor.jsp">
                                        <a class="lia" tabindex="-1" href="javascript:void(0);">主题活动</a>
                                    </li>
                                </ul>--%>
                        </li>
                    </c:if>

                </c:if>
            </ul>
        </c:if>
    </div>


    <div id="topMain">

        <table border="0" width="100%" height="100%">
            <tr>
                <td colspan="2" height="190" class="indexTopTd" name="indexTopTd" id="indexTopTd">
                    <div id="TopQuickContainer">
                        <div class="indexTopQuick" id="indexTopQuick1" style="z-index:3;" onclick="a('FrameAnnouce.jsp',false)" >
                            <span style="z-index: 99;cursor: pointer;left:0;top:0;width:58%;height: 71%" onclick="a('FrameAnnouce.jsp',false)"></span>
                            <img src="img/announce.png" width="200" />
                            <span id="annouceCount" style="cursor: pointer;left: 61px;background-color: transparent;z-index: 2" onclick="a('FrameAnnouce.jsp',false)">

                            </span><%--公告数--%>

                        </div>
                        <div class="indexTopQuick" style="z-index:4;background-color:transparent;" id="indexTopQuick2" onclick="openCA()">
                            <div style="z-index: 99;position:absolute;top:0;left: 0;width:100%;height: 87%;cursor: pointer" <%--onclick="openCA()"--%> ></div>
                            <img src="img/tongxunlu.png" width="115" />
                            <span style="cursor: pointer;left: 61px;" onclick="openCA()"></span>
                        </div>
                        <div class="indexTopQuick" style="z-index:4;" id="indexTopQuick3" onclick="a('FrameDaiban.jsp',false)">
                            <div  style="z-index: 99;position:absolute;top:0;left: 0;width:100%;height: 87%;cursor:pointer" onclick="a('FrameDaiban.jsp',false)"></div>
                            <img src="img/daiban.png" width="115" />
                            <span style="cursor: pointer;left: 61px;" id="schedulesCount"  onclick="a('FrameDaiban.jsp',false)">

                            </span>
                        </div>
                        <div class="indexTopQuick" style="z-index:3" id="indexTopQuick4" onclick="a('FrameMeet.jsp',false)">
                            <div  style="z-index: 99;position:absolute;top:0;right: 0;width:58%;height: 71%;cursor:pointer" onclick="a('FrameMeet.jsp',false)"></div>
                            <img src="img/meeting.png" width="200" />
                            <span id="meetCount" style="left: 145px;" onclick="a('FrameMeet.jsp',false)"></span>
                        </div>
                        <div id="indexTopLogo">
                            <img id="indexTopLogoImg" src="img/indexLogo.png" width="160" />
                        </div>

                    </div>

                </td>
            </tr>
            <tr>
                <td width="90%">
                    <!--<iframe id="indexMainFrame" scrolling="yes" width="100%" height="100%" src="http://www.jd.com"></iframe>-->
                    <div id="indexDealContainer">
                        <style>

                            .td_contain{
                                width: 100%;
                                height: 100%;
                                /*background: linear-gradient(to left, #517aff, #517aff) left top no-repeat,*/
                                /*linear-gradient(to bottom, #517aff, #517aff) left top no-repeat,*/
                                /*linear-gradient(to left, #517aff, #517aff) right top no-repeat,*/
                                /*linear-gradient(to bottom, #517aff, #517aff) right top no-repeat,*/
                                /*linear-gradient(to left, #517aff, #517aff) left bottom no-repeat,*/
                                /*linear-gradient(to bottom, #517aff, #517aff) left bottom no-repeat,*/
                                /*linear-gradient(to left, #517aff, #517aff) right bottom no-repeat,*/
                                /*linear-gradient(to left, #517aff, #517aff) right bottom no-repeat;*/
                                /*background-size: 2px 10px, 10px 2px, 2px 10px, 10px 2px;*/

                            }
                            #indexDealTab tr td{

                            }
                            .td_title{
                                position:relative;
                                width: 100%;
                                height:20%;
                                margin-bottom: 10px;
                                /*background: -webkit-gradient(linear, 0% 25%, 75% 100%, from(rgba(0,104,142,100)), to(rgba(0, 104, 142, 0)));*/


                                background: -moz-linear-gradient(top, rgba(0,104,142,0.9) 0%, rgba(0,104,142,0) 100%);
                                background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,rgba(0,104,142,0.9)), color-stop(100%,rgba(0,104,142,0)));
                                background: -webkit-linear-gradient(top, rgba(0,104,142,0.9) 0%,rgba(0,104,142,0) 100%);
                                background: -o-linear-gradient(top, rgba(0,104,142,0.9) 0%,rgba(0,104,142,0) 100%);
                                background: -ms-linear-gradient(top, rgba(0,104,142,0.9) 0%,rgba(0,104,142,0) 100%);
                                background: linear-gradient(to bottom, rgba(0,104,142,0.9) 0%,rgba(0,104,142,0) 100%);
                                filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#E500688e', endColorstr='#0000688e',GradientType=0 );

                                /*color: #000000;*/
                                /*background: -moz-linear-gradient(left, rgba(0,104,142,0.9) 0%, rgba(0,104,142,0) 100%);*/
                                /*background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,rgba(0,104,142,0.9)), color-stop(100%,rgba(0,104,142,0)));*/
                                /*background: -webkit-linear-gradient(top, rgba(0,104,142,0.9) 0%,rgba(0,104,142,0) 100%);*/
                                /*background: -o-linear-gradient(left, rgba(0,104,142,0.9) 0%,rgba(0,104,142,0) 100%);*/
                                /*background: -ms-linear-gradient(left, rgba(0,104,142,0.9) 0%,rgba(0,104,142,0) 100%);*/
                                /*background: linear-gradient(to right, rgba(0,104,142,0.9) 0%,rgba(0,104,142,0) 100%);*/
                                /*filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#E500688e', endColorstr='#0000688e',GradientType=1 );*/

                                padding: 0px 20px 0 35px;

                                /*text-align: center;*/
                                vertical-align: center;

                            }
                            .td_title .title_left{
                                font-size: 17px;
                                position: absolute;

                                height: 100%;
                                top:3%;
                                left:5%;
                                vertical-align: center;
                                display:inline;
                                /*font-family:'zongyijtf9dee2b771ba17';
                                margin-right: 50%;*/
                                font-family:  "微软雅黑" ;
                                font-weight: bold;
                            }
                            .td_title .title_right{
                                font-size: 12px;
                                font-family:'zongyijtf9dee2b771ba17';
                                position: absolute;
                                vertical-align: center;
                                height: 100%;
                                top:15%;
                                /*float: right;*/
                                right: 5%;
                                display:inline;

                                /*top:50px;*/
                                /*left:50px;*/

                            }
                            .title_right a{
                                color: #74c6ff;!important;
                            }

                        </style>
                        <table id="indexDealTab" class="inded" cellpadding="0" cellspacing="15"  border="0" width="100%" height="100%">
                            <tr>
                                <td id="td" width="50%" style="height:200px;">
                                    <div class="td_contain">

                                        <div class="td_title"><span style="color:${_PersonModules.color1};" class="title_left">${_PersonModules.name1}</span>
                                            <span class="title_right"><a href="javascript:void(0)" onclick="a('${_PersonModules.moreUrl1}',false)">More</a></span>
                                        </div>
                                        <div class="homeDIV">
                                            <%--<div class="bq" id="bq-1" style="color:#e1dd21;">${_PersonModules.name1}</div>--%>
                                            <%--<div class="more"><a href="javascript:void(0)" onclick="a('${_PersonModules.moreUrl1}',false)">More</a></div>--%>

                                            <div class="module_div">
                                                <%--<c:if test="${_SYSTEM_USER.userType != 2 && _SYSTEM_USER.userType != 3}">--%>
                                                <c:if test="${_NORMAL_USER}">

                                                    <ul class="main-ul" id="${_PersonModules.ulId1}"><li></li>

                                                    </ul>
                                                </c:if>
                                            </div>
                                        </div>
                                    </div>
                                </td>
                                <td>
                                    <div class="td_contain">
                                        <div class="td_title"><span style="color:${_PersonModules.color2}" class="title_left">${_PersonModules.name2}</span>
                                            <span class="title_right"><a href="javascript:void(0)" onclick="a('${_PersonModules.moreUrl2}',false)">More</a></span>
                                        </div>
                                        <div class="homeDIV">
                                            <%--<div class="bq" style="color:#904eff">${_PersonModules.name2}</div>--%>
                                            <%--<div class="more"><a href="javascript:void(0)" onclick="a('${_PersonModules.moreUrl2}',false)">More</a></div>--%>
                                            <div class="module_div">
                                                <%--<c:if test="${_SYSTEM_USER.userType != 2 && _SYSTEM_USER.userType != 3}">
                                                               --%>
                                                <c:if test="${_NORMAL_USER}">

                                                    <ul class="main-ul" id="${_PersonModules.ulId2}"><li></li>

                                                    </ul>
                                                </c:if>

                                            </div>
                                        </div>
                                    </div>

                                    <%--<div class="bq" style="color:#ff2330">海通简报</div>
                                    <div class="more" ></div>
									<ul class="main-ul" id="htreportsUl"><li></li>
										<script>
                                            jQuery().ready(function (){

                                                $.getJSON("/cms/inforDocument.do?method=getInforDocument&categoryId="+<%=InforConstant.Cms_Category_Htreports %>+"&sidx=inforId&sord=desc&_search=false&page=1&rows=7",function(data) {
                                                    //需要首页显示的发布信息
                                                    var content = "";
                                                    $.each(data.rows, function(i, n) {
                                                        content += "<li><a href='javascript:;' onclick='viewInfor(\""+n.htmlFilePath+"\");'> "+n.inforTitle+" ("+ n.author.person.personName + " " +n.createTime+")</a>";
                                                        if (n.important) {
                                                            content += " <img src='<c:url value='/'/>images/flag_red.gif' border='0'/>";
                                                        }
                                                        content += "</li>";
                                                    });
                                                    $('#htreportsUl').html(content);
                                                });

                                            });
										</script>
									</ul>--%>
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" style="height:200px;">
                                    <div class="td_contain">
                                        <div class="td_title"><span style="color:${_PersonModules.color3}" class="title_left">${_PersonModules.name3}</span>
                                            <span class="title_right"><a href="javascript:void(0)" onclick="a('${_PersonModules.moreUrl3}',false)">More</a></span></div>

                                        <div  class="homeDIV">
                                            <%--<div class="bq"  style="color:#d43d47">${_PersonModules.name3}</div>--%>
                                            <%--<div class="more" ><a href="javascript:void(0)" onclick="a('${_PersonModules.moreUrl3}',false)">More</a></div>--%>

                                            <div class="module_div">
                                                <%--<c:if test="${_SYSTEM_USER.userType != 2 && _SYSTEM_USER.userType != 3}">
                                                               --%>
                                                <c:if test="${_NORMAL_USER}">
                                                    <!-- 工作工作跟踪 -->
                                                    <ul class="main-ul" id="${_PersonModules.ulId3}"><li></li>

                                                    </ul>

                                                </c:if>

                                            </div>

                                        </div>

                                    </div>

                                </td>
                                <td>
                                    <div class="td_contain">
                                        <div class="td_title"><span style="color:${_PersonModules.color4}" class="title_left">${_PersonModules.name4}</span>
                                            <span class="title_right"><a href="javascript:void(0)" onclick="a('${_PersonModules.moreUrl4}',false)">More</a></span></div>
                                        <div class="homeDIV">
                                            <%--<div class="bq" style="color:#22ffae">${_PersonModules.name4}</div>--%>
                                            <%--<div class="more"><a href="javascript:void(0)" onclick="a('${_PersonModules.moreUrl4}',false)">More</a></div>--%>
                                            <div class="module_div">
                                                <%--<c:if test="${_SYSTEM_USER.userType != 2 && _SYSTEM_USER.userType != 3}">
                                                               --%>
                                                <c:if test="${_NORMAL_USER}">
                                                    <!-- 工作工作跟踪 -->
                                                    <ul class="main-ul" id="${_PersonModules.ulId4}"><li></li>

                                                    </ul>

                                                </c:if>

                                            </div>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <div class="td_contain">
                                        <div class="td_title"><span style="color:${_PersonModules.color5}" class="title_left">${_PersonModules.name5}</span>
                                            <span class="title_right"><a href="javascript:void(0)" onclick="a('${_PersonModules.moreUrl5}',false)">More</a></span></div>
                                        <div class="homeDIV">
                                            <%--<div class="bq" style="color:#ff6cc0">${_PersonModules.name5}</div>--%>
                                            <%--<div class="more"><a href="javascript:void(0)" onclick="a('${_PersonModules.moreUrl5}',false)">More</a></div>--%>
                                            <div class="module_div">
                                                <%--<c:if test="${_SYSTEM_USER.userType != 2 && _SYSTEM_USER.userType != 3}">
                                                               --%>
                                                <c:if test="${_NORMAL_USER}">
                                                    <!-- 工作工作跟踪 -->
                                                    <ul class="main-ul " id="${_PersonModules.ulId5}" ><li></li>

                                                    </ul>
                                                </c:if>

                                            </div>
                                        </div>
                                    </div>

                                </td>
                                <td>
                                    <div class="td_contain">
                                        <div class="td_title"><span style="color:${_PersonModules.color6}" class="title_left">${_PersonModules.name6}</span>
                                            <span class="title_right"><a href="javascript:void(0)" onclick="a('${_PersonModules.moreUrl6}',false)">More</a></span></div>
                                        <div class="homeDIV">
                                            <%--<div class="bq" style="color:#ffaf78">${_PersonModules.name6}</div>--%>
                                            <%--<div class="more"><a href="javascript:void(0)" onclick="a('${_PersonModules.moreUrl6}',false)">More</a></div>--%>
                                            <div class="module_div">
                                                <%--<c:if test="${_SYSTEM_USER.userType != 2 && _SYSTEM_USER.userType != 3}">
                                                               --%>

                                                <c:if test="${_NORMAL_USER}">

                                                    <ul class="main-ul" id="${_PersonModules.ulId6}" ><li></li>

                                                    </ul>
                                                </c:if>

                                            </div>
                                        </div>

                                    </div>


                                </td>
                            </tr>
                        </table>

                    </div>
                </td>
                <td valign="top">


                </td>
            </tr>
        </table>
    </div>

    <div id="bd">
        <div id="side">

        </div>
        <div id="main" style="display: none">
            <div id="content" style="background-color:rgba(123,31,31,0)">
                <iframe name="rightFrame" allowtransparency=true src="main.html" border="0"  frameborder="0" scrolling="" id="rightFrame" width="100%" height="100%"></iframe>
            </div>
        </div>
    </div>
    <div id="ft">

    </div>
</div>
</body>

</html>


<script src="/js/jquery1.9.1.min.js"></script>
<%--<script language="JavaScript" src="components/jquery-1.4.2.js"></script>--%>

<script language="JavaScript" src="/js/bootstrap.min.js"></script>
<script language="JavaScript" src="/js/jquery.nicescroll.min.js"></script>
<script language="JavaScript" src="/js/nicescroll.js"></script>
<script src="/js/html5shiv.min.js"></script>  <%--让bootstrap兼容ie--%>
<script src="/js/respond.min.js"></script>  <%--让bootstrap兼容ie--%>
<script src="/js/jquery.mCustomScrollbar.concat.min.js"></script>
<script src="/js/jquery.slimscroll.min.js"></script>
<script src="layer/layer.js"></script>

<script>
    $(document).ready(function(){
        var Sys = {};
        var ua = navigator.userAgent.toLowerCase();
        if (window.ActiveXObject) {
            Sys.ie = ua.match(/msie ([\d.]+)/)[1];
            //获取版本
            var ie_version = 6;
            if (Sys.ie.indexOf("7") > -1) {
                ie_version = 7;
            }
            if (Sys.ie.indexOf("8") > -1) {
                ie_version = 8;
                alert("为了更好的浏览体验，建议升级至IE9及其以上版本。")
//                    #indexDealTab td
//                    $("#indexDealTab td").css("background-color","#1c2c3d");
//                    $("#indexDealTab td").css({"position":"static","background-color":"rgb(255, 255, 255)","opacity":"0.1","filter":"progid:DXImageTransform.Microsoft.Alpha(opacity=10)"});
//
//                    $("#indexDealTab td .bq").css({"opacity":"1","filter":"progid:DXImageTransform.Microsoft.Alpha(opacity=100)"})
            }
            if (Sys.ie.indexOf("9") > -1) {
                ie_version = 9;
            }
            if (Sys.ie.indexOf("10") > -1) {
                ie_version = 10;
            }
            if (Sys.ie.indexOf("11") > -1) {
                ie_version = 11;
            }
        }
//        var b_name = navigator.appName;
//        var b_version = navigator.appVersion;
//        var version = b_version.split(";");
//        var trim_version = version[1].replace(/[ ]/g, "");
//        if (b_name == "Microsoft Internet Explorer") {
//            /*如果是IE6或者IE7*/
//            if (trim_version == "MSIE7.0" || trim_version == "MSIE6.0" || trim_version == "MSIE8.0") {
//                alert("为了更好的浏览体验，建议升级至IE9及其以上版本。");
//                //然后跳到需要连接的下载网站
//                //window.location.href="http://jiaoxueyun.com/download.jsp";
//            }
//        }
        /*  $(".navbar-left").find("li").find("a").onmouseout(function(){
              $(this).css("backgroundColor", "#f64781");
          });*/
        if(document.documentElement.clientHeight<=700){

            $("#indexTopQuick1").css("left","20%");
            $("#indexTopQuick2").css("left","35%");
            $("#indexTopQuick3").css("right","35%");
            $("#indexTopQuick4").css("right","20%");
            $("#topMenu").css("top","13%");
            $("#indexTopLogoImg").css("width","140px");
        }
        if(document.documentElement.clientWidth>=1700){

            $("#indexTopQuick1").css("left","28%");
            $("#indexTopQuick2").css("left","40%");
            $("#indexTopQuick3").css("right","40%");
            $("#indexTopQuick4").css("right","29%");

        }
        $("#bd").css("height",document.documentElement.clientHeight - 120);

        setFrameHeight();
        $('.module_div').slimScroll({
//                width:document.getElementById("td").clientWidth - 60,
            height:"130px",
            color: '#8b8b8b',
            alwaysVisible: true
        });
        $('.module_div').css("width",document.getElementById("td").clientWidth - 60);

        $("#indexDealContainer").niceScroll({
            cursorcolor: "rgba(255,255,255,0.2)",//滚动条的颜色
            background: "rgba(255,255,255,0.05)", // 轨道的背景颜色
            cursoropacitymax: 1, //滚动条的透明度，从0-1
//                touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
            gesturezoom: true,
            cursorwidth: "5px", //滚动条的宽度
            cursorborder: "0", // 游标边框css定义
            horizrailenabled: false,
            cursorborderradius: "5px",//以像素为光标边界半径  圆角
            autohidemode: false, //是否隐藏滚动条  true的时候默认不显示滚动条，当鼠标经过的时候显示滚动条
            zindex:"auto",//给滚动条设置z-index值
            oneaxismousemode: "false",// 当只有水平滚动时可以用鼠标滚轮来滚动，如果设为false则不支持水平滚动，如果设为auto支持双轴滚动
            railpadding: {top:0, right:0, left:0, bottom:0 },//滚动条的位置
            iframeautoresize: true, // 在加载事件时自动重置iframe大小
            sensitiverail: true, // 单击轨道产生滚动
            preventmultitouchscrolling: true // 防止多触点事件引发滚动*/
//                touchbehavior: true, // 激活拖拽滚动

            /*cursorcolor: "#4d5a67", // 改变滚动条颜色，使用16进制颜色值
            cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
            cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
            cursorwidth: "5px", // 滚动条的宽度，单位：便素
//                cursorborder: "1px solid #fff", // CSS方式定义滚动条边框
            cursorborderradius: "5px", // 滚动条圆角（像素）
            zindex: "auto" , // 改变滚动条的DIV的z-index值
            scrollspeed: 60, // 滚动速度
            mousescrollstep: 40, // 鼠标滚轮的滚动速度 (像素)
            touchbehavior: false, // 激活拖拽滚动
            hwacceleration: true, // 激活硬件加速
            boxzoom: false, // 激活放大box的内容
            dblclickzoom: true, // (仅当 boxzoom=true时有效)双击box时放大
            gesturezoom: true, // (仅 boxzoom=true 和触屏设备时有效)  激活变焦当out/in（两个手指外张或收缩）
            grabcursorenabled: true ,// (仅当 touchbehavior=true) 显示“抓住”图标display "grab" icon
            autohidemode: false, // 隐藏滚动条的方式, 可用的值:

            iframeautoresize: true, // 在加载事件时自动重置iframe大小
            cursorminheight: 32, // 设置滚动条的最小高度 (像素)
            preservenativescrolling: true, // 你可以用鼠标滚动可滚动区域的滚动条和增加鼠标滚轮事件
            railoffset: false, // 可以使用top/left来修正位置
            bouncescroll: false, // (only hw accell) 启用滚动跳跃的内容移动
            spacebarenabled: true, // 当按下空格时使页面向下滚动
            railpadding: { top: 0, right: 0, left: 0, bottom: 0 }, // 设置轨道的内间距
            disableoutline: true, // 当选中一个使用nicescroll的div时，chrome浏览器中禁用outline
            horizrailenabled: true, // nicescroll可以管理水平滚动
//                railalign: right, // 对齐垂直轨道
//                railvalign: bottom, // 对齐水平轨道
            enabletranslate3d: true, // nicescroll 可以使用CSS变型来滚动内容
            enablemousewheel: true, // nicescroll可以管理鼠标滚轮事件
            enablekeyboard: true, // nicescroll可以管理键盘事件
            smoothscroll: true, // ease动画滚动
            sensitiverail: true, // 单击轨道产生滚动
            enablemouselockapi: true, // 可以用鼠标锁定API标题 (类似对象拖动)
            cursorfixedheight: false, // 修正光标的高度（像素）
            hidecursordelay: 400, // 设置滚动条淡出的延迟时间（毫秒）
            directionlockdeadzone: 6, // 设定死区，为激活方向锁定（像素）
            nativeparentscrolling: true, // 检测内容底部便于让父级滚动
            enablescrollonselection: true, // 当选择文本时激活内容自动滚动
            cursordragspeed: 0.3, // 设置拖拽的速度
            rtlmode: "auto", // DIV的水平滚动从左边开始
            cursordragontouch: false, // 使用触屏模式来实现拖拽
            oneaxismousemode: "auto", // 当只有水平滚动时可以用鼠标滚轮来滚动，如果设为false则不支持水平滚动，如果设为auto支持双轴滚动
            scriptpath: "" ,// 为boxmode图片自定义路径 ("" => same script path)
            preventmultitouchscrolling: true // 防止多触点事件引发滚动*/
        });


        /*$('.module_div').niceScroll({
            autohidemode: true

        });*/
//            $('#indexDealContainer').slimScroll({
//                height:"200px",
//                alwaysVisible: false
//            });
//            $("#indexDealContainer").mCustomScrollbar();
//            if(document.documentElement.clientHeight>)

    });

    function setFrameHeight(){
        $("#rightFrame").css("height",document.documentElement.clientHeight-120);

        $("#indexMainFrame").css("height",document.documentElement.clientHeight - document.getElementById("indexTopTd").height - 30);
        $("#indexDealContainer").css("height",document.documentElement.clientHeight - document.getElementById("indexTopTd").height - 68);

    }


    function toHome(){
        $("body").css("background-image","url(/img/bg.png)");
        $("body").css("background-size","cover");
        $("#ft").css("display","block");
        //$("#rightFrame").attr("src","main.html");
        $("#topMain").css("display","block");
        $("#main").css("display","none");

//            document.getElementById('rightFrame').contentWindow.location.reload(true);
//            location.reload()
    }

    function a(url,isHome){
        setFrameHeight();
        if(isHome){
        }else{
            $("body").css("background","url(/img/bgIn.png)");
            $("body").css("background-size","cover");
            $("#ft").css("display","none");
            $("#topMain").css("display","none");
            $("#main").css("display","block");
        }
        //$("#rightFrame").attr("src",url);
        document.getElementById('rightFrame').src=url;
    }
</script>
<script>
    function viewInfor(htmlFilePath){
        window.open(<%=request.getContextPath()%>htmlFilePath,'','');
    }
    function disTaskTip(taskId){
        if(confirm("请确认已知悉本提醒，之后将不再显示！")){
            $.ajax({
                url: '/supervise/superviseInfor.do?method=saveReadStatus&taskId='+taskId,
                cache: false,
                type: "GET",
                //dataType : "json",
                async: true,
                beforeSend: function (xhr) {

                },
                complete : function(req, msg) {
                    //var msg = eval("(" + req.responseText + ")");
                    //alert(msg+"--");
                    window.location.reload();

                },
                success : function (msg) {
                    window.location.reload();
                }
            });
        }
    }
</script>
<script>
    function openCA(){
        window.open("/address.do?method=listCompanyAddress")
    }
    function openWDPT(){
        window.open("http://192.168.61.37/")
    }
    function openOldOA() {
        window.open("http://oa.haitongauto.com/")
    }
    function openEmail(){
        window.open("http://mail.haitongauto.com/owa/");
    }
    function openCaigou(){
//        window.open("/indexHome.jsp");
        window.open("/cggl.jsp");
    }

    function re_login()
    {
        msg="您好，${_GLOBAL_PERSON.personName}\n您正在使用 海通网络智能办公系统\n确认要注销么？";
        if(window.confirm(msg))
            parent.parent.location="/logout.do";
        //parent.parent.location="http://192.168.61.86:8899/cas/logout";
    }
    $(document).ready(qidong());
    function qidong(){



        //会议数
        $.ajax({
            type:"post",
            url:"/meeting/meetInfor.do?method=getMeetCounts",
//                data:"number",
            data:"number",
            success:function (data) {
                if(data==0){

                }else {
                    $("#meetCount").html(data)
                }

            }
        });

        //日程数
        /*$.ajax({
            type:"post",
            url:"/personal/personalJobInfor.do?method=getSchedulesCounts",
            data:"number",
            success:function (data) {
                $("#schedulesCount").html(data)
            }
        })*/

        //公告数
        $.ajax({
            type:"post",
            url:"/inforPraise.do?method=annouceCounts", //cms
            data:"number",
            success:function (data) {
                $("#annouceCount").html(data)
            }
        });





        var myModuleNames=new Array(6);
        myModuleNames[0]  =  '${_PersonModules.name1}';
        myModuleNames[1]  =  '${_PersonModules.name2}';
        myModuleNames[2]  =  '${_PersonModules.name3}';
        myModuleNames[3]  =  '${_PersonModules.name4}';
        myModuleNames[4]  = '${_PersonModules.name5}';
        myModuleNames[5]  =  '${_PersonModules.name6}';


        //---------------获取待办开始---------------




//if($.inArray("待办事宜",myModuleNames)!=-1){

        var contenttpwj = "";
        var contentpy = "";
        var contenthy = "";
        var contentgz1 = "";
        var contentgz2 = "";
        var contentgzl = "";

        var countDaiban1 ;
        var countDaiban2 ;
        var countDaiban3 ;
        var countDaiban4 ;
        var countDaiban5 ;



        var contentgz6 = "";
        var contentgz7 = "";
        var contentgz8 = "";
        var contentgz9 = "";

        var contentgz10 = "";
        var contentgz11 = "";
        var contentgz12 = "";
        var contentgz13 = "";

        var count6=0;
        var count7=0;
        var count8=0;
        var count9=0;
        var count10=0;
        var count11=0;
        var count12=0;
        var count13=0;

        var ajax1 = $.ajax(
            {
                url: "/tpwj/topicInfor.do?method=getTopics",
                dataType: "json",
                type: "post",
                success: function (data) {
                    countDaiban1=data._Topics.length;
//                    console.info(countDaiban1)
                    $.each(data._Topics, function(i, n) {
                        var typeName = "";
                        if(n.type == 0){
                            typeName = "投票"
                        }
                        if(n.type == 1){
                            typeName = "问卷"
                        }
                        contenttpwj += "<li><a href='/tpwj/topicInfor.do?method=viewTopicInfor&type="+n.type+"&topicId="+n.topicId+"' target='_blank'> [<font color=orange>"+typeName+"</font>]："+n.topicName+"</a></li>";
                    });
                }
            });


        var ajax2 = $.ajax(
            {
                url: "/extend/pyTopicInfor.do?method=getPyTopics",
                dataType: "json",
                type: "post",
                success: function (data) {
                    countDaiban2=data._Topics.length;
//                    console.info(countDaiban2)
                    $.each(data._Topics, function(i, n) {
                        var typeName = "评优投票";

                        contentpy += "<li><a href='/extend/pyVoteInfor.do?method=viewpy&rowId="+n.topicId+"' target='_blank'> [<font color=orange>"+typeName+"</font>]："+n.topicName+"</a></li>";
                    });
                }
            });

        var ajax3 = $.ajax(
            {
                url: "/meeting/meetInfor.do?method=getSummaryMeet",
                dataType: "json",
                type: "post",
                success: function (data) {
                    countDaiban3=data._Meets.length;
//                    console.info(countDaiban3)
                    $.each(data._Meets, function(i, n) {
                        contenthy += "<li><a href='/meeting/meetInfor.do?method=viewMeeting&rowId="+n.meetId+"' target='_blank'>[会议纪要]： "+n.meetName+" ("+n.author.person.personName+" "+n.meetDate+")</a></li>";
                    });
                }
            });

        var  ajax4 = $.ajax(
            {
                url: "/supervise/superviseInfor.do?method=getNeedDealTasks",
                dataType: "json",
                type: "post",
                success: function (data) {
                    countDaiban4=data._Tasks.length;
//                    console.info(countDaiban4)
                    $.each(data._Tasks, function(i, n) {
                        var workType = n.taskCategory.categoryType;
                        var typeStr = '';
                        if(workType == '1'){
                            typeStr = '方针目标';
                        }else if(workType == '2'){
                            typeStr = '党群';
                        }else if(workType == '3'){
                            typeStr = '部门建设';
                        }else if(workType == '4'){
                            typeStr = '内控';
                        }
                        contentgz1 += "<li><a href='/supervise/superviseInfor.do?method=viewTask&rowId="+n.taskId+"' target='_blank' title='"+n.taskName+"'> [工作跟踪-"+typeStr+"]"+data._WarningStrs[i]+"："+n.taskName+"</a></li>";
                    });
                    //修改内容后的提醒
                    var json5 = $.each(data._ChangedTasks, function(i, n){
                        var workType = n.taskCategory.categoryType;
                        var typeStr = '';
                        if(workType == '1'){
                            typeStr = '方针目标';
                        }else if(workType == '2'){
                            typeStr = '党群';
                        }else if(workType == '3'){
                            typeStr = '部门建设';
                        }else if(workType == '4'){
                            typeStr = '内控';
                        }
                        contentgz2 += "<li ><a href='#' onclick='disTaskTip("+n.taskId+");' title='"+n.taskName+"'> [工作跟踪-"+typeStr+"]"+data._ChangedWarningStrs[i]+"："+n.taskName+"</a></li>";
                    });
                }
            });

        var ajax5 = $.ajax(
            {
                url: "/workflow/instanceInfor.do?method=getNeedDealInstances",
                dataType: "json",
                type: "post",
                success: function (data) {

                    countDaiban5=data._Instances.length;
//                    console.info(countDaiban5)
                    $.each(data._Instances, function(i, n) {

                        contentgzl += "<li><a href='/workflow/instanceInfor.do?method=view&instanceId="+n.instanceId+"' target='_blank'> ["+n.flowDefinition.flowName+"]["+data._WarningStrs[i]+"]："+n.instanceTitle+" ("+n.department.organizeName+" "+n.updateTime+")</a></li>";
                    });
                }
            });
        ajax6 = $.ajax(
            {
                url: "/ybpurchase/purchaseInfor.do?method=getNeedDealInstances",
                dataType: "json",
                type: "post",
                success: function (data) {
                    if(data._type== 0){
                        $.each(data._Instances, function(i, n) {
                            contentgz6 += "<li><a href='/ybpurchase/purchaseInfor.do?method=open&type=19&packageId="+n.packageId+"' target='_blank'> [一般采购][<font color=red>请进行相关操作</font>]："+n.packageName+" ("+n.startDate+")</a></li>";
                            count6 = count6 +1;
                    });
                    }else if (data._type== 1){
                        $.each(data._Instances, function(i, n) {
//                            if(n.flowId.flowId == 1){
                            contentgz6 += "<li><a href='/ybpurchase/purchaseInfor.do?method=view&purchaseId="+n.purchaseId +"' target='_blank'> [一般采购][<font color=red>请进行相关操作</font>]："+n.purchaseTitle+" ("+n.startTime+")</a></li>";
                            count6 = count6 +1;
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
                    if(data._type== 0){
                        $.each(data._Instances, function(i, n) {
                            contentgz7 += "<li><a href='/ywpurchase/purchaseInfor.do?method=viewPackage&flowId=1&packageId="+n.packageId+"' target='_blank'> [业务采购][<font color=red>请进行相关操作</font>]："+n.packageName+" ("+n.startDate+")</a></li>";
                            count7 = count7 +1;
                        });
                    }else if (data._type== 1){
                        $.each(data._Instances, function(i, n) {
                            contentgz7 += "<li><a href='/ywpurchase/purchaseInfor.do?method=view&purchaseId="+n.purchaseId +"' target='_blank'> [业务采购][<font color=red>请进行相关操作</font>]："+n.purchaseTitle+" ("+n.startTime+")</a></li>";
                            count7 = count7 +1;
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
                    if(data._type== 0){
                        $.each(data._Instances, function(i, n) {
                            contentgz8 += "<li><a href='/gcpurchase/purchaseInfor.do?method=viewPackage&flowId=1&packageId="+n.packageId+"' target='_blank'> [工程项目][<font color=red>请进行相关操作</font>]："+n.packageName+" ("+n.startDate+")</a></li>";
                            count8 = count8 +1;
                        });
                    }else if (data._type== 1){
                        $.each(data._Instances, function(i, n) {
                            contentgz8 += "<li><a href='/gcpurchase/purchaseInfor.do?method=view&purchaseId="+n.purchaseId +"' target='_blank'> [工程项目][<font color=red>请进行相关操作</font>]："+n.purchaseTitle+" ("+n.startTime+")</a></li>";
                            count8 = count8 +1;
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
                    if(data._type== 0){
                        $.each(data._Instances, function(i, n) {
                            contentgz9 += "<li><a href='/ybpurchase/purchaseInfor.do?method=open&type=20&packageId="+n.packageId+"' target='_blank'> [零星工程维修][<font color=red>请进行相关操作</font>]："+n.packageName+" ("+n.startDate+")</a></li>";
                            count9 = count9 +1;
                        });
                    }else if (data._type== 1){
                        $.each(data._Instances, function(i, n) {
                            contentgz9 += "<li><a href='/lxpurchase/purchaseInfor.do?method=view&purchaseId="+n.purchaseId +"' target='_blank'> [零星工程维修][<font color=red>请进行相关操作</font>]："+n.purchaseTitle+" ("+n.startTime+")</a></li>";
                            count9 = count9 +1;
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
                        count10 = count10 +1;
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
                        count11 = count11 +1;
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
                        count12= count12 +1;
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
                        count13=count13+1
                    });
                },
                error:function (data) {
//                    alert('su')
                }
            });
        $.when(ajax1, ajax2, ajax3, ajax4, ajax5, ajax6, ajax7, ajax8, ajax9, ajax10, ajax11, ajax12,ajax13).done(function () {
            //所做操作
            $('#taskUl').html(contenttpwj+contentpy+contenthy+contentgz1+contentgz2+contentgzl+contentgz6+contentgz7+contentgz8+contentgz9+contentgz10+contentgz11+contentgz12+contentgz13);
            var countDaiban = countDaiban1+countDaiban2+countDaiban3+countDaiban4+countDaiban5+count6+count7+count8+count9+count10+count11+count12+count13;
            if(countDaiban>0){
                $("#schedulesCount").html(countDaiban1+countDaiban2+countDaiban3+countDaiban4+countDaiban5+count6+count7+count8+count9+count10+count11+count12+count13);
            }

        });

//}

//---------------获取待办结束---------------

//---------------获取公告栏开始---------------
        if($.inArray("公告栏",myModuleNames)!=-1){
            $.getJSON("/cms/newsInfor.do?method=getInfors&categoryId="+<%=InforConstant.Cms_Category_Annouce %>,function(data){
                //需要首页显示的发布信息
                var content = "";
                $.each(data._Infors, function(i, n) {
                    content += "<li><a href='javascript:;' onclick='viewInfor(\""+n.htmlFilePath+"?inforId="+n.inforId+"\");'> "+n.inforTitle+" ("+ n.author.person.personName + " " +n.createTime+")</a>";
                    if (n.important) {
                        content += " <img src='<c:url value='/'/>images/flag_red.gif' border='0'/>";
                    }
                    content += "</li>";
                });
                $('#messageUl').html(content);
            });
        }

//---------------获取公告栏结束---------------

// ---------------获取海通简报开始---------------

        if($.inArray("海通简报",myModuleNames)!=-1){
            $.getJSON("/cms/inforDocument.do?method=getInforDocument&categoryId="+<%=InforConstant.Cms_Category_Htreports %>+"&sidx=topp&sord=desc&_search=false&page=1&rows=5",function(data) {
                //需要首页显示的发布信息
                var content = "";
                $.each(data.rows, function(i, n) {
                    content += "<li><a href='javascript:;' onclick='viewInfor(\""+n.htmlFilePath+"?inforId="+n.inforId+"\");'> "+n.inforTitle+" ("+n.issueUnit + " " +n.createTime+")</a>";
                    if (n.important) {
                        content += " <img src='<c:url value='/'/>images/flag_red.gif' border='0'/>";
                    }
                    content += "</li>";
                });
                $('#htreportsUl').html(content);
            });
        }

// ---------------获取海通简报结束---------------

// ---------------获取管理工作开始---------------
        if($.inArray("管理工作",myModuleNames)!=-1){
            $.getJSON("/cms/inforDocument.do?method=getInforDocument&categoryId="+<%=InforConstant.Cms_Category_Managework %>+"&sidx=topp&sord=desc&_search=false&page=1&rows=5",function(data) {
                //需要首页显示的发布信息
                var content = "";
                $.each(data.rows, function(i, n) {
                    content += "<li><a href='javascript:;' onclick='viewInfor(\""+n.htmlFilePath+"?inforId="+n.inforId+"\");'> "+n.inforTitle+" ("+n.issueUnit + " " +n.createTime+")</a>";
                    if (n.important) {
                        content += " <img src='<c:url value='/'/>images/flag_red.gif' border='0'/>";
                    }
                    content += "</li>";
                });
                $('#manageworkUl').html(content);
            });
        }

// ---------------获取管理工作结束---------------

// ---------------获取党群园地开始---------------
        if($.inArray("党群园地",myModuleNames)!=-1){
            $.getJSON("/cms/inforDocument.do?method=getInforDocument&categoryId="+<%=InforConstant.Cms_Category_Partygarden %>+"&sidx=topp&sord=desc&_search=false&page=1&rows=5",function(data) {
                //需要首页显示的发布信息
                var content = "";
                $.each(data.rows, function(i, n) {
                    content += "<li><a href='javascript:;' onclick='viewInfor(\""+n.htmlFilePath+"?inforId="+n.inforId+"\");'> "+n.inforTitle+" ("+ n.issueUnit + " " +n.createTime+")</a>";
                    if (n.important) {
                        content += " <img src='<c:url value='/'/>images/flag_red.gif' border='0'/>";
                    }
                    content += "</li>";
                });
                $('#partygardenUl').html(content);
            });
        }

// ---------------获取党群园地结束---------------

// ---------------获取市场信息开始---------------
        if($.inArray("市场信息及研究",myModuleNames)!=-1){
            $.getJSON("/cms/inforDocument.do?method=getInforDocument&categoryId="+<%=InforConstant.Cms_Category_Marketinfo %>+"&sidx=topp&sord=desc&_search=false&page=1&rows=5",function(data) {
                //需要首页显示的发布信息
                var content = "";
                $.each(data.rows, function(i, n) {
                    content += "<li><a href='javascript:;' onclick='viewInfor(\""+n.htmlFilePath+"?inforId="+n.inforId+"\");'> "+n.inforTitle+" ("+ n.issueUnit + " " +n.createTime+")</a>";
                    if (n.important) {
                        content += " <img src='<c:url value='/'/>images/flag_red.gif' border='0'/>";
                    }
                    content += "</li>";
                });
                $('#marketinfoUl').html(content);
            });
        }

// ---------------获取市场信息结束---------------

// ---------------获取企业新风开始---------------
        if($.inArray("企业新风",myModuleNames)!=-1){
            $.getJSON("/cms/inforDocument.do?method=getInforDocument&categoryId="+<%=InforConstant.Cms_Category_Enterprisenew %>+"&sidx=topp&sord=desc&_search=false&page=1&rows=5",function(data) {
                //需要首页显示的发布信息
                var content = "";
                $.each(data.rows, function(i, n) {
                    content += "<li><a href='javascript:;' onclick='viewInfor(\""+n.htmlFilePath+"?inforId="+n.inforId+"\");'> "+n.inforTitle+" ("+ n.author.person.personName + " " +n.createTime+")</a>";
                    if (n.important) {
                        content += " <img src='<c:url value='/'/>images/flag_red.gif' border='0'/>";
                    }
                    content += "</li>";
                });
                $('#enterprisenewUl').html(content);
            });
        }

        // ---------------获取企业新风结束---------------

        // ---------------获取规章制度开始---------------
        if($.inArray("规章制度",myModuleNames)!=-1){
            $.getJSON("/cms/inforDocument.do?method=getInforDocument&categoryId="+<%=InforConstant.Cms_Category_Bylaw %>+"&sidx=topp&sord=desc&_search=false&page=1&rows=5",function(data) {
                //需要首页显示的发布信息
                var content = "";
                $.each(data.rows, function(i, n) {
                    content += "<li><a href='javascript:;' onclick='viewInfor(\""+n.htmlFilePath+"?inforId="+n.inforId+"\");'> "+n.inforTitle+" ("+ n.author.person.personName + " " +n.createTime+")</a>";
                    if (n.important) {
                        content += " <img src='<c:url value='/'/>images/flag_red.gif' border='0'/>";
                    }
                    content += "</li>";
                });
                $('#bylawUl').html(content);
            });
        }

        // ---------------获取规章制度结束---------------

        // ---------------获取新闻中心开始---------------
        if($.inArray("新闻中心",myModuleNames)!=-1){
            $.getJSON("/cms/inforDocument.do?method=getInforDocument&categoryId="+<%=InforConstant.Cms_Category_Newscenter %>+"&sidx=topp&sord=desc&_search=false&page=1&rows=5",function(data) {
                //需要首页显示的发布信息
                var content = "";
                $.each(data.rows, function(i, n) {
                    content += "<li><a href='javascript:;' onclick='viewInfor(\""+n.htmlFilePath+"?inforId="+n.inforId+"\");'> "+n.inforTitle+" ("+ n.author.person.personName + " " +n.createTime+")</a>";
                    if (n.important) {
                        content += " <img src='<c:url value='/'/>images/flag_red.gif' border='0'/>";
                    }
                    content += "</li>";
                });
                $('#newscenterUl').html(content);
            });
        }

        // ---------------获取新闻中心结束---------------

        // ---------------获取荣誉室开始---------------
        if($.inArray("荣誉室",myModuleNames)!=-1){
            $.getJSON("/cms/inforDocument.do?method=getInforDocument&categoryId="+<%=InforConstant.Cms_Category_Honorroom %>+"&sidx=topp&sord=desc&_search=false&page=1&rows=5",function(data) {
                //需要首页显示的发布信息
                var content = "";
                $.each(data.rows, function(i, n) {
                    content += "<li><a href='javascript:;' onclick='viewInfor(\""+n.htmlFilePath+"?inforId="+n.inforId+"\");'> "+n.inforTitle+" ("+ n.author.person.personName + " " +n.createTime+")</a>";
                    if (n.important) {
                        content += " <img src='<c:url value='/'/>images/flag_red.gif' border='0'/>";
                    }
                    content += "</li>";
                });
                $('#honorroomUl').html(content);

            });
        }
// ---------------获取知识园地开始---------------
        if($.inArray("知识园地",myModuleNames)!=-1){
            $.getJSON("/cms/inforDocument.do?method=getInforDocument&categoryId="+<%=InforConstant.Cms_Category_Knowledgegarden %>+"&sidx=topp&sord=desc&_search=false&page=1&rows=5",function(data) {
                //需要首页显示的发布信息
                var content = "";
                $.each(data.rows, function(i, n) {
                    content += "<li><a href='javascript:;' onclick='viewInfor(\""+n.htmlFilePath+"?inforId="+n.inforId+"\");'> "+n.inforTitle+" ("+ n.author.person.personName + " " +n.createTime+")</a>";
                    if (n.important) {
                        content += " <img src='<c:url value='/'/>images/flag_red.gif' border='0'/>";
                    }
                    content += "</li>";
                });
                $('#knowledgegardenUl').html(content);
            });
        }
        // ---------------获取知识园地结束---------------
        // ---------------获取荣誉室结束---------------

        // ---------------获取投票问卷开始---------------
        if($.inArray("投票结果",myModuleNames)!=-1){
            var contentTp = "";
            $.ajaxSettings.async = false;
            $.getJSON("/tpwj/topicInfor.do?method=getCanCountTopics",function(data) {
                //需要处理的审核实例
                var content = "";
                $.each(data._CountTopics, function(i, n) {
                    var typeName = "";
                    if(n.type == 0){
                        typeName = "投票"
                    }
                    if(n.type == 1){
                        typeName = "问卷"
                    }
                    contentTp += "<li><a href='/tpwj/topicInfor.do?method=viewCount&type="+n.type+"&topicId="+n.topicId+"' target='_blank'> [<font color=orange>"+typeName+"</font>]："+n.topicName+" 的统计结果</a></li>";
                });
            });
            $.getJSON("/tpwj/topicInfor.do?method=getPublicTopics",function(data) {
                //需要处理的审核实例
                var content = "";
                $.each(data._PublicTopics, function(i, n) {
                    var typeName = "";
                    if(n.type == 0){
                        typeName = "投票"
                    }
                    if(n.type == 1){
                        typeName = "问卷"
                    }
                    contentTp += "<li><a href='/tpwj/topicInfor.do?method=viewCount&type="+n.type+"&topicId="+n.topicId+"' target='_blank'> [<font color=orange>"+typeName+"</font>]："+n.topicName+" 的统计结果</a></li>";
                });

            });
            $.getJSON("/extend/pyTopicInfor.do?method=getPublicTopics",function(data) {
                //需要处理的审核实例
                var content = "";
                $.each(data._PublicTopics, function(i, n) {
                    var typeName = "评优投票";

                    contentTp += "<li><a href='/extend/pyTopicInfor.do?method=viewCount&rowId="+n.topicId+"' target='_blank'> [<font color=orange>"+typeName+"</font>]："+n.topicName+" 的统计结果</a></li>";
                });

            });
            $.ajaxSettings.async = true;
//        document.getElementById("taskUl").html(content0)
            $('#publicTopicUl').html(contentTp);


            // ---------------获取投票问卷结束---------------
        }



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
<%--自定义模块层--%>
<script>
    function modulesChange() {
        layer.open({
            type: 2,
            title: '修改主页模板',
            shadeClose: true,
            shade: 0.8,
            area: ['90%', '90%'],
            content: '/core/personModules.do?method=editPersonModules' //iframe的url
        });
    }
</script>
