<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="/inc/taglibs.jsp" %>
<%@ page import="com.kwchina.oa.submit.util.SubmitConstant" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1"/>
<script src="/datePicker/WdatePicker.js" type="text/javascript"></script>
<script src="/components/jquery-1.4.2.js" type="text/javascript"></script>
<!--jquery包-->

<script src="/components/jqgrid/js/jquery-ui-1.8.2.custom.min.js" type="text/javascript"></script>
<!--jquery ui-->
<script src="/components/jquery.layout.js" type="text/javascript"></script>
<!--jquery 布局-->
<script src="/components/jqgrid/js/grid.locale-cn.js" type="text/javascript"></script>
<!--jqgrid 中文包-->
<script src="/components/jqgrid/js/jquery.jqGrid.min.js" type="text/javascript"></script>
<!--jqgrid 包-->
<script src="/js/commonFunction.js"></script>
<script src="/js/inc_javascript.js"></script>
<script src="/js/multisearch.js"></script>
<!--加载模态多条件查询相关js-->
<script src="/js/changeclass.js"></script>
<!--用于改变页面样式-->

<%--<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />--%>
<link rel="stylesheet" type="text/css" media="screen" href="/css/base/jquery-ui-1.9.2.custom.css"/>
<link rel="stylesheet" type="text/css" media="screen" href="/components/jqgrid/css/ui.jqgrid.css"/>
<link rel="stylesheet" type="text/css" media="screen" href="/components/jqgrid/css/tabstyle.css"/>

<title></title>
<script type="text/javascript">
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
//            alert(111);
            window.location.reload();
        }
    }, false);

</script>

<body style="border:1px solid #0DE8F5;border-radius: 5px;background-image: url('/img/bgIn.png');background-size: cover">
<div>
    <table id="list"></table>
    <div id="pager"></div>
</div>
</body>
<script type="text/javascript">

    function GetQueryString(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);//search,查询？后面的参数，并匹配正则
        if (r != null)return unescape(r[2]);
        return null;
    }
    jQuery("#list").jqGrid(
        {
            url: "/review.do?method=sanfangList&type="+GetQueryString("type"),
            rownumbers: true,
            datatype: "json",
            width: "90%",
            autowidth: true,
            height: document.documentElement.clientHeight - 140,
            colNames: ['Id', '三方标题', '申请人', '申请部门', '申请时间', '采购类型', '采购编号', '三方状态'],
            colModel: [
                {name: 'sanfangID', index: 'sanfangID', width: 0, align: 'center', hidden: true},
                {name: 'sanfangTitle', index: 'sanfangTitle', width: 100, align: 'center', formatter: formatTitle},
                {name: 'sanfangApplier', index: 'sanfangApplier', width: 100, align: 'center'},
                {name: 'sanfangDepartment', index: 'sanfangDepartment', width: 100, align: 'center'},
                {name: 'applyDate', index: 'applyDate', width: 100, align: 'center'},
                {name: 'purchaseType', index: 'applyDate', width: 100, align: 'center'},
                {name: 'purchaseCode', index: 'purchaseCode', width: 100, align: 'center'},
                {name: 'sanfangStatus', index: 'sanfangStatus', width: 100, align: 'center', formatter: formatStatus}
            ],

            sortorder: 'desc',
            multiselect: true,	//是否支持多选,可用于批量删除
            viewrecords: true,
            rowNum: 10,
            rowList: [10, 20, 30],
            shrinkToFit: true,
            jsonReader: {
                repeatitems: false
            },
            pager: "#pager",
            sortname: 'sanfangID',
        }).navGrid('#pager', {
        edit: false,
        add: false,
        del: false,
        search: false
    }).closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});

    jQuery("#list").jqGrid('navButtonAdd', '#pager', {
        caption: "<span style='color: red;'>批量删除</span>",
        title: "点击批量删除",
        buttonicon: 'ui-icon-trash',
        onClickButton: deleteInfor
    });
    jQuery("#list").jqGrid('navButtonAdd', '#pager', {
        caption: "查询", title: "点击查询", buttonicon: 'ui-icon-search', onClickButton: openDialog
    });

    function deleteInfor() {
        //获取选择的行的Id
        var rowIds = jQuery("#list").jqGrid('getGridParam', 'selarrrow');

        if (rowIds != null && rowIds.length > 0) {
            var yes = window.confirm("确定要删除吗？");
            if (yes) {
                $.ajax({
                    url: "/review.do?method=delete&rowIds=" + rowIds,	//删除数据的url
                    cache: false,
                    type: "POST",
                    dataType: "html",
                    beforeSend: function (xhr) {
                    },

                    complete: function (req, err) {
                        var returnValues = eval("(" + req.responseText + ")");
                        if (returnValues["_CanRecycle"]) {
                            alert("数据已经删除！");
                            $("#list").trigger("reloadGrid");
                        } else {
                            alert("对不起,您无权进行该操作,请与系统管理员联系!");
                        }
                    }
                });
            }
        } else {
            alert("请选择要删除的数据！");
        }
    }
    var multiSearchParams = new Array();
    function openDialog() {

        multiSearchParams[0] = "#list";				//列表Id
        multiSearchParams[1] = "#multiSearchDialog";//查询模态窗口Id

        initSearchDialog();

        //获取自定义表单查询字段
        getSearchFields();

        $(multiSearchParams[1]).dialog("open");
    }
    //自定义显示状态
    function formatStatus(cellValue, options, rowObject) {
        var html = '';
        html = "<font color='red'>" + cellValue + "</font>";
        return html;
    }
    //自定义显示标题
    function formatTitle(cellValue, options, rowObject) {
        var html = '';
        html = "<a href='javascript:;' onclick='doView(" + rowObject.sanfangID + ")'>" + cellValue + "</a>";
        return html;
    }
    //查看
    function doView(sanfangID) {
        window.open("/review.do?method=view&sanfangID=" + sanfangID, "_blank");
    }
</script>