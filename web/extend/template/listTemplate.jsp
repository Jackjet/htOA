<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="/inc/taglibs.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">


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
    //新增
    function addRole() {
        if(addSupplier()){
            window.open("/extend/template.do?method=edit", "_blank");
        }else{
            alert("您没有新增权限！")
        }
    }
    //查看  修改
    function editRole(rowId) {
        window.open("/extend/template.do?method=edit&rowId=" + rowId, "_blank");
    }
    function doView(rowId) {
        window.open("/extend/template.do?method=edit&type=view&rowId=" + rowId, "_blank");
    }

    function deleteTemplate(rowId) {
        if(rowId != null){
            var url ="/extend/template.do?method=delete&rowId="+ rowId;
            var yes = window.confirm("确定要删除吗？");
            if (yes) {
                $.ajax({
                    url: url,	//删除数据的url
                    cache: false,
                    type: "POST",
                    dataType: "json",
                    beforeSend: function (xhr) {
                    },
                    success:function (data) {
                        if(data.msg){
                            alert("使用中，无法删除");
                            $("#listRole").trigger("reloadGrid");
                        }else{
                            alert("删除成功");
                            $("#listRole").trigger("reloadGrid");
                        }
                    }
                });
            }
        }

    }
</script>

<title></title>
<div>
    <table id="listRole"></table>
    <div id="pagerRole"></div>
</div>
<script type="text/javascript">

    //自定义操作栏的显示内容
    function formatOperation(cellvalue, options, rowObject) {
        var returnStr = "";
            if((addSupplier()||'${_GLOBAL_PERSON.personName}'=='系统管理员')){
                returnStr +=  "<a href='javascript:;' onclick='editRole(" +  rowObject.templateId + ")'>[复制]</a>";
            }
        if(addSupplier() && (rowObject.adderName =='${_GLOBAL_PERSON.personName}'||'${_GLOBAL_PERSON.personName}'=='系统管理员')){
            returnStr += "<a href='javascript:;' onclick='deleteTemplate(" + rowObject.templateId + ")'>[删除]</a>";
        }
        return returnStr;
    }
    //自定义显示标题
    function formatTitle(cellValue, options, rowObject) {
        var html = '';
        html = "<a href='javascript:;' onclick='doView(" + rowObject.templateId + ")'>" + cellValue + "</a>";
        return html;
    }

    //加载表格数据nfo
    var $mygrid = jQuery("#listRole").jqGrid({
        url: '/extend/template.do?method=list',
        rownumbers: true,
        datatype: "json",
        autowidth: true,
        //height: "auto",
        height: document.documentElement.clientHeight - 240,
        colNames: ['Id', '模板标题', '添加人','操作'],
        colModel: [
            {name: 'templateId', align: 'center',index: 'templateId', width: 0, sorttype: "int", search: false, key: true, hidden: true},
            {name: 'templateName',align: 'center', index: 'templateName', width:30, sortable: true, sorttype: "string",formatter: formatTitle},
            {name: 'adderName',align: 'center', index: 'adderName',width:30, sortable: true, sorttype: "string"},
            {name: 'fixed', align: 'center',width:30, search: false, sortable: false, formatter: formatOperation}
        ],
        sortname: 'templateId',
        sortorder: 'asc',
        viewrecords: true,
        rowNum: 10,
        rowList: [10, 20, 30],
        scroll: false,
        scrollrows: false,
        jsonReader: {
            repeatitems: false
        },
        pager: "#pagerRole"
    }).navGrid('#pagerRole', {
        edit: false,
        add: false,
        del: false,
        search: false
    }).closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
    //}).navGrid('#pagerRole',{edit:false,add:false,del:false}).searchGrid({multipleSearch:true,autoOpen:false});

    //自定义按钮
    jQuery("#listRole").jqGrid('navButtonAdd', '#pagerRole', {
        caption: "新增", title: "点击新增信息", buttonicon: 'ui-icon-plusthick', onClickButton: addRole
    });


</script>