<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2018/1/4
  Time: 16:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />


    <script src="/js/jquery-1.8.3.js" type="text/javascript"></script>
    <script src="/js/jquery.cxcolor.min.js" type="text/javascript"></script>

    <link href="/js/bootstrap.min.css" rel="stylesheet" type="text/css" />
    <link href="/css/jquery.cxcolor.css" rel="stylesheet" type="text/css" />
    <title>海通网络智能办公系统</title>
    <style>
        /*鼠标移上去*/
        .nav > li:hover .dropdown-menu { display: block;
            /*background-color: #052532;*/
            color:white;
            z-index: 999;
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

        .dropdown-menu .divider{
            height:1px;
            margin:5px 0;
            overflow:hidden;
            background-color: rgba(255, 249, 254, 0.96);
        }


        .navbar-left li a{
            color: #cfcfcf;

        }

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

        .indexTopQuick span{
            position: absolute;
            left:70px;
            top:10px;
            cursor: pointer;
        }



        #indexTopQuick4 span{
            left:155px;
        }

        #indexDealContainer{
            width: 100%;
            height: 100%;
            border: 0 solid red;
            overflow-x: hidden;
            overflow-y: auto;

        }


        #indexDealTab td{
            height:200px;
            z-index: 33;
            background-color: rgba(255, 255, 255, 0.05);
            color: white;
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

            background-image: url(/img/bg.png);
            background-color: #153b49;
            background-size: cover;
            /*background-size:100%;*/
            font-family:  "黑体" ;

        }

        /*html {
            _padding: 110px 0;
            scrollbar-face-color: rgba(16, 13, 18, 0.61);!*滚动条颜色*!
            scrollbar-highlight-color:#000;
            scrollbar-3dlight-color:#000;
            scrollbar-darkshadow-color:#000;
            scrollbar-Shadow-color:#adadad;!*滑块边色*!
            scrollbar-arrow-color:rgba(0,0,0,0.4);!*箭头颜色*!
            scrollbar-track-color:#eeeeee;!*背景颜色*!
        }*/

        #bottomContainer{
            height: 100%;

        }




        #topTab #opTd{
            cursor: pointer;
        }



        #topMain{
            position: absolute;
            top:0px;
            right:100px;
            left:100px;
            bottom: 100px;
            border: 0px solid black;
            z-index: 1;
            overflow-y: auto;
        }
        .homeDIV{
            margin-top: 20px;
        }
        .homeDIV select{
            width: 150px;
            font-size: 1em;
            border: 1px solid #18818b;
            border-radius: 3px;
            background-color: #0e1827;
            padding: 6px 6px 6px 6px;
        }
        .homeDIV select option{
            color: white;
            font-size: 1em;
            border: 1px solid #22FBFF;
            border-radius: 3px;
            background-color: rgb(16, 33, 51);

        }
        #ft {
            position: absolute;
            bottom: 50px;
            left: 0;
            width: 100%;
            height: 8%;
            margin-top: 10px;
            text-align: center;
            /*background: url(/img/logo-bottom1.png) center center no-repeat;*/
            background-size:40% 45%;
            z-index: 999;
        }
        #ft button{
            width: 80px;
            height: 40px;
            font-family: Lucida Grande, Lucida Sans, Arial, sans-serif;
            color: white;
            font-size: 1em;
            border: none;
            border-radius: 3px;
            background-color: rgb(80, 80, 80);
        }

    </style>

    <%--<script src="/js/jquery1.9.1.min.js"></script>--%>
    <script>
        $(function () {
            var color1=$("#color_1");
            var color2=$("#color_2");
            var color3=$("#color_3");
            var color4=$("#color_4");
            var color5=$("#color_5");
            var color6=$("#color_6");
            var module1=$("#module1");
            var module2=$("#module2");
            var module3=$("#module3");
            var module4=$("#module4");
            var module5=$("#module5");
            var module6=$("#module6");

            color1.cxColor();
            color2.cxColor();
            color3.cxColor();
            color4.cxColor();
            color5.cxColor();
            color6.cxColor();

            color1.bind("change",function(){
                module1.css("color",this.value)
            });
            color2.bind("change",function(){
                module2.css("color",this.value)
            });
            color3.bind("change",function(){
                module3.css("color",this.value)
            });
            color4.bind("change",function(){
                module4.css("color",this.value)
            });
            color5.bind("change",function(){
                module5.css("color",this.value)
            });
            color6.bind("change",function(){
                module6.css("color",this.value)
            });

            if(${!empty _PersonModules}){
                $("#module1").val("${_PersonModules.name1}");
                $("#module2").val("${_PersonModules.name2}");
                $("#module3").val("${_PersonModules.name3}");
                $("#module4").val("${_PersonModules.name4}");
                $("#module5").val("${_PersonModules.name5}");
                $("#module6").val("${_PersonModules.name6}");
            if(${!empty _PersonModules.color1}){

                $("#color_1").val("${_PersonModules.color1}");
                $("#color_2").val("${_PersonModules.color2}");
                $("#color_3").val("${_PersonModules.color3}");
                $("#color_4").val("${_PersonModules.color4}");
                $("#color_5").val("${_PersonModules.color5}");
                $("#color_6").val("${_PersonModules.color6}");
            }else {
                $("#color_1").val("#e1dd21");
                $("#color_2").val("#b682ff");
                $("#color_3").val("#ff4955");
                $("#color_4").val("#22ffae");
                $("#color_5").val("#ff6cc0");
                $("#color_6").val("#ffaf78");
            }

            }else {
                $("#module1").val("待办事宜");
                $("#module2").val("公告栏");
                $("#module3").val("海通简报");
                $("#module4").val("管理工作");
                $("#module5").val("党群园地");
                $("#module6").val("市场信息及研究");

                $("#color_1").val("#e1dd21");
                $("#color_2").val("#b682ff");
                $("#color_3").val("#ff4955");
                $("#color_4").val("#22ffae");
                $("#color_5").val("#ff6cc0");
                $("#color_6").val("#ffaf78");
            }
            module1.css("color",color1.val());
            module2.css("color",color2.val());
            module3.css("color",color3.val());
            module4.css("color",color4.val());
            module5.css("color",color5.val());
            module6.css("color",color6.val());
//            alert(color3.val())

        })
    </script>

</head>

<body >

    <div id="bottomContainer">

        <div id="topMain">
            <form id="moduleForm">
                <table border="0" width="100%" >

                    <tr>
                        <td width="90%">
                            <!--<iframe id="indexMainFrame" scrolling="yes" width="100%" height="100%" src="http://www.jd.com"></iframe>-->
                            <div id="indexDealContainer">
                                <table id="indexDealTab" class="inded" cellpadding="0" cellspacing="15"  border="0" width="100%" >
                                    <tr >
                                        <td id="td" width="50%"  align="center">
                                            <div>模块一，请选择：</div>
                                            <div class="homeDIV">
                                                <select id="module1" name="name1" style="color:${_PersonModules.color1} ">
                                                    <option value="待办事宜">待办事宜</option>
                                                    <option value="海通简报">海通简报</option>
                                                    <option value="管理工作">管理工作</option>
                                                    <option value="市场信息及研究">市场信息及研究</option>
                                                    <option value="党群园地">党群园地</option>
                                                    <option value="公告栏">公告栏</option>
                                                    <option value="投票结果">投票结果</option>
                                                    <option value="企业新风">企业新风</option>
                                                    <option value="规章制度">规章制度</option>
                                                    <option value="新闻中心">新闻中心</option>
                                                    <option value="荣誉室">荣誉室</option>
                                                    <option value="知识园地">知识园地</option>
                                                </select>
                                               <input id="color_1" name="color1" value="${_PersonModules.color1}" type="text" class="input_cxcolor" readonly>
                                            </div>
                                        </td>
                                        <td width="50%" style="height:200px;" align="center">
                                            <div>模块二，请选择：</div>
                                            <div class="homeDIV">
                                                <select id="module2" name="name2">
                                                    <option value="待办事宜">待办事宜</option>
                                                    <option value="海通简报">海通简报</option>
                                                    <option value="管理工作">管理工作</option>
                                                    <option value="市场信息及研究">市场信息及研究</option>
                                                    <option value="党群园地">党群园地</option>
                                                    <option value="公告栏">公告栏</option>
                                                    <option value="投票结果">投票结果</option>
                                                    <option value="企业新风">企业新风</option>
                                                    <option value="规章制度">规章制度</option>
                                                    <option value="新闻中心">新闻中心</option>
                                                    <option value="荣誉室">荣誉室</option>
                                                    <option value="知识园地">知识园地</option>
                                                </select>
                                                <input id="color_2" name="color2" value="${_PersonModules.color2}" type="text" class="input_cxcolor" readonly>
                                            </div>

                                        </td>
                                    </tr>
                                    <tr>
                                        <td width="50%" style="height:200px;"align="center">

                                            <div>模块三，请选择：</div>
                                            <div class="homeDIV">
                                                <select id="module3" name="name3">
                                                    <option value="待办事宜">待办事宜</option>
                                                    <option value="海通简报">海通简报</option>
                                                    <option value="管理工作">管理工作</option>
                                                    <option value="市场信息及研究">市场信息及研究</option>
                                                    <option value="党群园地">党群园地</option>
                                                    <option value="公告栏">公告栏</option>
                                                    <option value="投票结果">投票结果</option>
                                                    <option value="企业新风">企业新风</option>
                                                    <option value="规章制度">规章制度</option>
                                                    <option value="新闻中心">新闻中心</option>
                                                    <option value="荣誉室">荣誉室</option>
                                                    <option value="知识园地">知识园地</option>
                                                </select>
                                                <input id="color_3" name="color3" value="${_PersonModules.color3}" type="text" class="input_cxcolor" readonly>
                                            </div>
                                        </td>
                                        <td width="50%" style="height:200px;"align="center">
                                            <div>模块四，请选择：</div>
                                            <div class="homeDIV" >
                                                <select id="module4" name="name4">
                                                    <option value="待办事宜">待办事宜</option>
                                                    <option value="海通简报">海通简报</option>
                                                    <option value="管理工作">管理工作</option>
                                                    <option value="市场信息及研究">市场信息及研究</option>
                                                    <option value="党群园地">党群园地</option>
                                                    <option value="公告栏">公告栏</option>
                                                    <option value="投票结果">投票结果</option>
                                                    <option value="企业新风">企业新风</option>
                                                    <option value="规章制度">规章制度</option>
                                                    <option value="新闻中心">新闻中心</option>
                                                    <option value="荣誉室">荣誉室</option>
                                                    <option value="知识园地">知识园地</option>
                                                </select>
                                                <input id="color_4" name="color4" value="${_PersonModules.color4}" type="text" class="input_cxcolor" readonly>
                                            </div>

                                        </td>
                                    </tr>
                                    <tr>
                                        <td width="50%" style="height:200px;"align="center">
                                            <div>模块五，请选择：</div>
                                            <div class="homeDIV">
                                                <select id="module5" name="name5">
                                                    <option value="待办事宜">待办事宜</option>
                                                    <option value="海通简报">海通简报</option>
                                                    <option value="管理工作">管理工作</option>
                                                    <option value="市场信息及研究">市场信息及研究</option>
                                                    <option value="党群园地">党群园地</option>
                                                    <option value="公告栏">公告栏</option>
                                                    <option value="投票结果">投票结果</option>
                                                    <option value="企业新风">企业新风</option>
                                                    <option value="规章制度">规章制度</option>
                                                    <option value="新闻中心">新闻中心</option>
                                                    <option value="荣誉室">荣誉室</option>
                                                    <option value="知识园地">知识园地</option>
                                                </select>
                                                <input id="color_5" name="color5" value="${_PersonModules.color5}" type="text" class="input_cxcolor" readonly>
                                            </div>
                                        </td>
                                        <td width="50%" style="height:200px;"align="center">
                                            <div>模块六，请选择：</div>
                                            <div class="homeDIV">
                                                <select id="module6" name="name6">
                                                    <option value="待办事宜">待办事宜</option>
                                                    <option value="海通简报">海通简报</option>
                                                    <option value="管理工作">管理工作</option>
                                                    <option value="市场信息及研究">市场信息及研究</option>
                                                    <option value="党群园地">党群园地</option>
                                                    <option value="公告栏">公告栏</option>
                                                    <option value="投票结果">投票结果</option>
                                                    <option value="企业新风">企业新风</option>
                                                    <option value="规章制度">规章制度</option>
                                                    <option value="新闻中心">新闻中心</option>
                                                    <option value="荣誉室">荣誉室</option>
                                                    <option value="知识园地">知识园地</option>
                                                </select>
                                                <input id="color_6" name="color6" value="${_PersonModules.color6}" type="text" class="input_cxcolor" readonly>
                                            </div>

                                        </td>
                                    </tr>

                                </table>

                            </div>
                        </td>
                    </tr>
                </table>

            </form>
        </div>
        <div id="ft">

                <button style="z-index: 9999" onclick="saveModule()">确&nbsp;定</button>

        </div>


    </div>
</body>
<script>
    function saveModule() {
        var myModuleNames=new Array(6);
        myModuleNames[0]  =  $("#module1").val();
        myModuleNames[1]  =  $("#module2").val();
        myModuleNames[2]  =  $("#module3").val();
        myModuleNames[3]  =  $("#module4").val();
        myModuleNames[4]  =  $("#module5").val();
        myModuleNames[5]  =  $("#module6").val();
//        console.info(myModuleNames)
        var nary=myModuleNames.sort();

//        console.info(nary)
        var flag = 0;
        for(var i=0;i<myModuleNames.length;i++) {

            if (nary[i] == nary[i + 1]) {

                alert("模块不能重复");
                return false;
            }
        }
            $.ajax({
                type:"post",
                url:"/core/personModules.do?method=savePersonModules", //cms
                data:$("#moduleForm").serialize(),
                success:function (data) {
                    window.parent.location.reload();
                    var index = parent.layer.getFrameIndex(window.name);
                    setTimeout(function(){parent.layer.close(index)}, 500);
                }
            })


    }

</script>
</html>
