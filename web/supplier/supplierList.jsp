<%@ page import="com.kwchina.oa.util.SysCommonMethod" %>
<%@ page import="com.kwchina.core.base.entity.SystemUserInfor" %>
<%@ page import="com.kwchina.core.base.entity.RoleInfor" %>
<%@ page import="com.kwchina.core.base.service.RoleManager" %>
<%@ page import="org.springframework.beans.factory.annotation.Autowired" %>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="/inc/taglibs.jsp" %>

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
<!--导出excel -->
<script type='text/javascript' src=../js/expertToExcel.js></script>
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

    function addSupplier() {
        var isAdd=false;
        var isDelete=false;
        $.ajax({
            async:false,
            type: "GET",
            url: "/supplier.do?method=canAdd",
            dataType: "json",
            success: function(data){
                isAdd=data.canAdd;
            }
        });
        return isAdd;
    }
    function deleteSupplier() {
        var isDelete=false;
        $.ajax({
            async:false,
            type: "GET",
            url: "/supplier.do?method=canAdd",
            dataType: "json",
            success: function(data){
                isDelete=data.canDelete;
            }
        });
        return isDelete;
    }
</script>

<body style="border:1px solid #0DE8F5;border-radius: 5px;background-image: url('/img/bgIn.png');background-size: cover ">
<div>
    <table id="list"></table>
    <div id="pager"></div>
</div>
<!-- 查询框 -->
<div id="multiSearchDialog" style="display: none;">
    <table>
        <tbody>
        <tr>
            <td>
                <input type="hidden" class="searchField" value="status"/>状态：
                <input type="hidden" class="searchOper" value="eq"/>
            </td>
            <td style="text-align: center">
                <label>供应商状态：</label>
                <select id="status" class="searchString">
                    <option value="">--全部--</option>
                    <option value=0>待审核</option>
                    <option value=3>终审</option>
                    <option value=1>潜在</option>
                    <option value=4>合格</option>
                    <option value=5>即将过期</option>
                    <option value=2>认证中</option>
                </select>
            </td>
        </tr>
        </tbody>
    </table>
</div>
</body>
<script type="text/javascript">
    jQuery("#list").jqGrid(
        {
            url: "/supplier.do?method=supplierList",
            rownumbers: true,
            datatype: "json",
            autowidth: true,
            height: document.documentElement.clientHeight - 140,
            colNames: ['Id', '供方名称', '联系电话', '服务明细', '服务时间', '采购类型', '单一供方', '状态', '有效期', '发起人','相关操作'],
            colModel: [
                {name: 'supplierID', index: 'bidInfoId', width: 0, align: 'center', hidden: true},
                {name: 'supplierName', index: 'supplierName', width: 150, align: 'center', formatter: formatTitle},
                {name: 'supplierContact', index: 'supplierContact', width: 100, align: 'center'},
                {name: 'serviceDetail', index: 'serviceDetail', width: 100, align: 'center'},
                {name: 'serviceYear', index: 'serviceYear', width: 80, align: 'center'},
                {name: 'purchaseTypeMsg', index: 'purchaseTypeMsg', width: 100, align: 'center'},
                {name: 'single', index: 'single', width: 80, align: 'center'},
                {name: 'supplierStatus', index: 'supplierStatus', width: 100, align: 'center', formatter: formatStatus},
                {name: 'expirationTime', index: 'expirationTime', width: 100, align: 'center'},
                {name: 'sponsorName', index: 'sponsorName', width: 80, align: 'center'},
                {name: 'fixed', width: 100, align: 'center', search: false, sortable: false, formatter: formatOperation}
            ],
            sortorder: 'desc',
            multiselect: true,	//是否支持多选,可用于批量删除
            viewrecords: true,
//            autoWidth:true,
//            shrinkToFit: false,
//            autoScroll: true,
            rowNum: 10,
            rowList: [10, 20, 30],
            jsonReader: {
                repeatitems: false
            },
            pager: "#pager",
            sortname: 'supplierID',
        }).navGrid('#pager', {
        edit: false,
        add: false,
        del: false,
        search: false
    });
    jQuery("#list").jqGrid('navButtonAdd', '#pager', {
        caption: "新增", title: "点击新增", buttonicon: 'ui-icon-add', onClickButton: addInfor
    });
    jQuery("#list").jqGrid('navButtonAdd', '#pager', {
        caption: "查询", title: "点击查询", buttonicon: 'ui-icon-search', onClickButton: openMultipleSearchDialogLog
    });
    jQuery("#list").jqGrid('navButtonAdd', '#pager', {
        caption: "导出Excel", title: "点击导出到Excel", buttonicon: 'ui-icon-calculator', onClickButton: exportExcel
    });
    //自定义显示状态
    function formatStatus(cellValue, options, rowObject) {
        var html = '';
        if (cellValue == '合格') {
            html = "<font color='green'>" + cellValue + "</font>";
        } else if (cellValue == '潜在') {
            html = "<font color='#daa520'>" + cellValue + "</font>";
        } else if(cellValue=='审核未通过') {
            html = "<font color='#a9a9a9'>" + cellValue + "</font>";
        }else if(cellValue=='即将过期'){
            html = "<font color='#ff4500'>" + cellValue + "</font>";
        }else{
            html = "<font color='red'>" + cellValue + "</font>";
        }
        return html;
    }
    //自定义显示标题
    function formatTitle(cellValue, options, rowObject) {
        var html = '';
        html = "<a href='javascript:;' onclick='doView(" + rowObject.supplierID + ")'>" + cellValue + "</a>";
        return html;
    }
    //审核
    function doView(supplierID) {
        window.open("/supplier.do?method=view&supplierID=" + supplierID, "_blank");
    }
    //自定义操作栏的显示内容
    function formatOperation(cellvalue, options, rowObject) {
        var returnStr = "";
        if(rowObject.supplierStatus !='审核未通过' && (rowObject.sponsorName =='${_GLOBAL_PERSON.personName}'||'${_GLOBAL_PERSON.personName}'=='系统管理员')){
            returnStr += "<a href='javascript:;' onclick='editInfo(" + rowObject.supplierID + ")'>[修改]</a>";
        }else{
            returnStr+="<a href='javascript:;' onclick='viewInfo(" + rowObject.supplierID + ")'>[查看]</a>";
        }
        if(deleteSupplier() && (rowObject.sponsorName =='${_GLOBAL_PERSON.personName}'||'${_GLOBAL_PERSON.personName}'=='系统管理员')){
            returnStr += "<a href='javascript:;' onclick='deleteInfo(" + rowObject.supplierID + ")'>[删除]</a>";
        }
        return returnStr;
    }
    //修改
    function editInfo(supplierID) {
        window.open("/supplier.do?method=edit&supplierID=" + supplierID, "_blank");
    }
    function viewInfo(supplierID) {
        window.open("/supplier.do?method=detail&supplierID=" + supplierID, "_blank");
    }
    function deleteInfo(supplierID) {
        if(supplierID != null){
            var url ="/supplier.do?method=delete&supplierID=" + supplierID;
            var yes = window.confirm("确定要删除吗？");
            if (yes) {
                $.ajax({
                    url: url,	//删除数据的url
                    cache: false,
                    type: "POST",
                    dataType: "html",
                    beforeSend: function (xhr) {
                    },
                    complete : function (req,err) {
                        alert("删除成功！");
                        $("#list").trigger("reloadGrid");
                    }
                });
            }
        }

    }

    //新增
    function addInfor() {
        if(addSupplier()){
            window.open("addInfor.jsp", "_blank");
        }else{
         alert("您没有新增权限！");
        }
    }
    //初始化列表和查询窗口Id
    var multiSearchParamsLog = new Array();
    multiSearchParamsLog[0] = "#listLoginLog";			//列表Id
    multiSearchParamsLog[1] = "#multiSearchDialog";//查询模态窗口Id

    //打开查询窗口
    function openMultipleSearchDialogLog() {
        //初始化窗口
        initSearchDialogLog();

        $(multiSearchParamsLog[1]).dialog("open");
    }
    /** 自定义多条件查询 */
    //初始化查询窗口
    function initSearchDialogLog() {
        $(multiSearchParamsLog[1]).dialog({
            autoOpen: false,
            modal: true,
            resizable: true,
            width: 350,
            title: "状态查询",
            buttons: {
                "查询": gridReload,
            }
        });
    }

    //筛选
    function gridReload() {
        var status = jQuery("#status").val() || "";
        jQuery("#list").jqGrid('setGridParam', {
            url: "/supplier.do?method=supplierList&status=" + status,
            page: 1
        }).trigger("reloadGrid");
        $(multiSearchParamsLog[1]).dialog("close");
    }

    /********导出excel*********/
    function exportExcel() {
        var rules = "";
        var param1 = "#multiSearchDialog";
        $("tbody tr", param1).each(function (i) {    	//(1)从multipleSearchDialog对话框中找到各个查询条件行
            var searchField = $(".searchField", this).val();    	//(2)获得查询字段
            var searchOper = $(".searchOper", this).val();  		//(3)获得查询方式
            var searchString = $(".searchString", this).val();  	//(4)获得查询值

            if (searchField && searchOper && searchString) { 		//(5)如果三者皆有值且长度大于0，则将查询条件加入rules字符串
                rules += ',{"field":"' + searchField + '","op":"' + searchOper + '","data":"' + searchString + '"}';
            }
        });
        if (rules) {
            rules = rules.substring(1);								//(6)如果rules不为空，且长度大于0，则去掉开头的逗号
        }

        var filtersStr = '{"groupOp":"AND","rules":[' + rules + ']}';//(7)串联好filtersStr字符串
        //alert(filtersStr);
        var url = "/supplier.do?method=expertExcel&_search=true&page=1&rows=1000000&sidx=supplierID&sord=desc&filters=" + filtersStr;
        window.location.href = url;
    }

</script>