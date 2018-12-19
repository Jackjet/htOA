<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="/inc/taglibs.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>模板维护</title>
    <link href="<c:url value="/"/>components/loadmask/jquery.loadmask.css" rel="stylesheet" type="text/css"/>
    <script type='text/javascript' src='<c:url value="/"/>components/loadmask/jquery.loadmask.min.js'></script>
    <script src="<c:url value="/"/>datePicker/WdatePicker.js" type="text/javascript"></script>
    <script src="<c:url value="/"/>components/jquery-1.4.2.js" type="text/javascript"></script>
    <script src="<c:url value="/"/>js/jquery.jqGrid-4.4.5/js/jquery-1.9.0.min.js" type="text/javascript"></script>
    <script src="<c:url value="/"/>js/jquery-ui-1.9.2.custom/js/jquery-ui-1.9.2.custom.min.js"
            type="text/javascript"></script> <!--jquery ui-->
    <script src="<c:url value="/"/>js/jquery.layout-latest.js" type="text/javascript"></script> <!--jquery 布局-->
    <script src="<c:url value="/"/>js/jquery.jqGrid-4.4.5/js/jquery.jqGrid.src.js" type="text/javascript"></script>
    <!--jqgrid 包-->
    <script src="<c:url value="/"/>js/jquery.jqGrid-4.4.5/js/i18n/grid.locale-cn.js" type="text/javascript"></script>
    <!--jqgrid 中文包-->

    <script src="<c:url value="/"/>js/commonFunction.js"></script>
    <script src="<c:url value="/"/>js/inc_javascript.js"></script>
    <script src="<c:url value="/"/>js/multisearch.js"></script> <!--加载模态多条件查询相关js-->
    <script src="<c:url value="/"/>js/changeclass.js"></script> <!--用于改变页面样式-->
    <link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>css/base/jquery-ui-1.9.2.custom.css"/>
    <link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css"/>
    <link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/tabstyle.css"/>
    <link rel="stylesheet" type="text/css" media="screen" href="/css/treeNormal.css"/>

    <script type="text/javascript">

        $().ready(function () {
            $('body').layout({
                resizerClass: 'ui-state-default',
                west__onresize: function (pane, $Pane) {
                    jQuery("#west-grid").jqGrid('setGridWidth', $Pane.innerWidth() - 2);
                }
            });

            //加载首页
            $.ajax({
                url: "listTemplateInfo.jsp",
                type: "GET",
                dataType: "html",
                beforeSend: function (xhr) {
                },
                complete: function (req, err) {
                    //格式化tab
                    loadTabCss();

                    $("#tabs-1", "#tabs").html(req.responseText);
                }
            });

            $.jgrid.defaults = $.extend($.jgrid.defaults, {loadui: "enable"});

            //格式化Tab菜单
            var maintab = jQuery('#tabs', '#RightPane').tabs({
                add: function (e, ui) {
                    //添加关闭按钮
                    $(ui.tab).parents('li:first')
                        .append('<span class="ui-tabs-close ui-icon ui-icon-close" title="关闭"></span>')
                        .find('span.ui-tabs-close')
                        .click(function () {
                            maintab.tabs('remove', $('li', maintab).index($(this).parents('li:first')[0]));
                        });
                    //选中刚添加的tab
                    maintab.tabs('select', '#' + ui.panel.id);
                    //可拖动tab页
                    maintab.find(".ui-tabs-nav").sortable({axis: 'x'});
                    //格式化tab
//				loadTabCss();
                    $("div[id^='tabs-']").css({
                        'margin-top': '0px',
                        'margin-left': '0px',
                        'padding-left': '0px',
                        'padding-top': '3px',
                        'padding-right': '0px',
                        'border': '1px solid #0DE8F5',
                        'border-radius': '0 5px 5px 5px'
                    });
                }
            });

            //加载Tab内容
            $('#west-grid tr').click(function () {
                var st = "#tabs-" + $(this).find('#tabId').text();
                if ($(st).html() != null) {
                    //若该tab的内容已存在则不再重新加载,将tab状态改为选中即可
                    maintab.tabs('select', st);
                } else {
                    //若tab的内容不存在,则加载
                    maintab.tabs('add', st, $(this).find('#tabMenu').text());
                    $.ajax({
                        url: $(this).find('#tabUrl').text(),
                        cache: false,
                        type: "GET",
                        dataType: "html",
                        beforeSend: function (xhr) {
                        },
                        complete: function (req, err) {
                            $(st, "#tabs").empty().html(req.responseText);
                        }
                    });
                }
            });

        });

        //保存信息后重新加载Tab
        function loadTab(url, tabId) {
            $.ajax({
                url: url,
                cache: false,
                type: "GET",
                dataType: "html",
                beforeSend: function (xhr) {
                },
                complete: function (req, err) {
                    $("#tabs-" + tabId).empty().html(req.responseText);
                }
            });
        }

        //更新域用户
        function domainSync() {
            $.ajax({
                url: '/domainSync.do',
                cache: false,
                type: "GET",
                //dataType : "json",
                async: true,
                cache: false,
                beforeSend: function (xhr) {
                    $("#LeftPane").mask("同步中，请稍等...");
                },
                complete: function (req, msg) {
                    //var msg = eval("(" + req.responseText + ")");
                    //alert(msg+"--");

                },
                success: function (msg) {
                    //alert(msg);
                    if ($("#LeftPane").isMasked()) {
                        $("#LeftPane").unmask();
                    }
                    $("#LeftPane").unmask();
                    if (msg == 'success') {
                        alert("同步成功！");
                        window.location.reload();
                    }
                    if (msg == 'fail') {
                        alert("同步失败，请重试！");
                    }
                }
            });
        }

    </script>
    <style type="text/css">
        .ui-tabs .ui-tabs-nav li.ui-tabs-active {
            margin-left: 0;
            margin-bottom: -3px;
            padding-top: 1px;
        }

        .ui-state-active, .ui-widget-content .ui-state-active, .ui-widget-header .ui-state-active {
            /*position: absolute;*/

            border: solid 1px #0de8f5;
            border-radius: 5px 5px 0 0;
            /*padding-bottom: 1px!important;*/
            border-bottom: 3px solid rgb(14, 22, 36) !important;
            /*margin-bottom: -3px!important;*/
            /*border-left: 1px solid rgb(13, 232, 245);*/
            /*border-right: 1px solid rgb(13, 232, 245);*/
            /*border-bottom: 1px solid rgba(13, 242, 255, 0);*/
            background: rgba(255, 255, 255, 0);
            font-size: 16px;
            font-weight: bold;
            color: #ff8d73;
        }

        .ui-tabs .ui-tabs-nav {
            margin-left: -4px;
        }

    </style>
</head>
<body style="border:1px solid #0DE8F5;border-radius: 5px;background-image: url('/img/bgIn.png');background-size: cover ">
<!-- #LeftPane -->
<div class="ui-layout-west ui-widget ui-widget-content" id="LeftPane"
     style="overflow:hidden;position:relative;	border: 1px solid #0de8f5;border-radius:5px;padding: 0 auto;">
    <div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all">
        <div class="ui-jqgrid-view">
            <div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
                <span class="ui-jqgrid-title">模板维护</span>
            </div>

            <div class="ui-jqgrid-bdiv">
                <table cellspacing="0" cellpadding="0" border="0" id="west-grid">
                    <tbody>
                    <tr class="ui-widget-content jqgrow ui-row-ltr"
                        onmouseover="over(this,'ui-state-hover jqgrow ui-row-ltr')" onmouseout="out(this)"
                        onclick="clicked(this)">
                        <td style="display: none; width: 1px;" id="tabId">1</td>
                        <td title="模板基础信息" style="width: 193px;" id="tabMenu">
                            <div style="width: 36px;" class="tree-wrap tree-wrap-ltr">
                                <div class="ui-icon ui-icon-document-b tree-leaf" style="left: 18px;"></div>
                            </div>
                            <span style="cursor: pointer;">模板基础信息</span></td>
                        <td style="display: none; width: 1px;" id="tabUrl">listTemplateInfor.jsp</td>
                    </tr>
                    <tr class="ui-widget-content jqgrow ui-row-ltr"
                        onmouseover="over(this,'ui-state-hover jqgrow ui-row-ltr')" onmouseout="out(this)"
                        onclick="clicked(this)">
                        <td style="display: none; width: 1px;" id="tabId">2</td>
                        <td title="模板维护" id="tabMenu">
                            <div style="width: 36px;" class="tree-wrap tree-wrap-ltr">
                                <div class="ui-icon ui-icon-document-b tree-leaf" style="left: 18px;"></div>
                            </div>
                            <span style="cursor: pointer;">模板维护</span></td>
                        <td style="display: none; width: 1px;" id="tabUrl">listTemplate.jsp</td>
                    </tr>

                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<!-- #RightPane -->
<div class="ui-layout-center ui-helper-reset ui-widget-content" id="RightPane" style="overflow: inherit">
    <!-- Tabs pane -->
    <div id="tabs" class="jqgtabs">
        <ul>
            <li><a href="#tabs-1">模板基础信息</a></li>
        </ul>
        <div id="tabs-1" style="border:1px solid #0DE8F5;border-radius:0 5px 5px 5px;"></div>
    </div>
</div>
</body>
</html>

