<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="/inc/taglibs.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1"/>
    <%--<meta http-equiv="X-UA-Compatible" content="IE=9" />--%>
    <title>新增供应商</title>
    <script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
    <script type="text/javascript" src="<c:url value="/"/>components/jqgrid/js/jquery.ui.core.js"></script>
    <script type="text/javascript" src="<c:url value="/"/>components/jqgrid/js/jquery.ui.widget.js"></script>
    <script type="text/javascript" src="<c:url value="/"/>components/jqgrid/js/grid.locale-cn.js"></script>
    <script type="text/javascript" src="<c:url value="/"/>components/jqgrid/js/jquery.ui.datepicker.js"></script>

    <link rel="stylesheet" type="text/css" href="<c:url value="/"/>components/jqgrid/css/jquery.ui.all.css"/>
    <link rel="stylesheet" type="text/css" media="screen"
          href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css"/>

    <link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css"/>
    <link rel="stylesheet" type="text/css" media="screen" href="/css/noTdBottomBorder.css"/>

    <script src="/js/inc_javascript.js"></script>
    <script src="/js/addattachment.js"></script>
    <script src="/js/commonFunction.js"></script>

    <style type="text/css">
        .buttonclass {
            font-weight: bold;
        }
    </style>

    <!-- formValidator -->
    <link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css"></link>
    <script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript"
            charset="UTF-8"></script>
    <script src="<c:url value="/"/>components/formvalidator/js/formValidatorRegex.js" type="text/javascript"
            charset="UTF-8"></script>
    <!-- ------------- -->

    <script>
        if ("${msg}" == "success") {
            alert("保存成功")
        } else if ("${msg}" == "fail") {
            alert("保存失败")
        }
        $(document).ready(function () {
            //验证
            $.formValidator.initConfig({
                formid: "instanceInforForm", onerror: function (msg) {
                    alert(msg)
                }, onsuccess: function () {
                }
            });
            $("#supplierName").formValidator({onshow: "请输入名称", onfocus: "名称不能为空"}).inputValidator({
                min: 1,
                onerror: "请输入名称"
            });
            $("#supplierContact").formValidator({onshow: "请输入联系方式", onfocus: "联系方式不能为空"}).inputValidator({
                min: 1,
                onerror: "请输入联系方式"
            });
            $("#purchaseTypeMsg").formValidator({onshow: "请选择采购类型", onfocus: "类型不能为空"}).inputValidator({
                min: 1,
                onerror: "请选择采购类型"
            });
            $("#single").formValidator({onshow: "请选择", onfocus: "不能为空"}).inputValidator({min: 1, onerror: "请选择"});

            //去掉初始化的提示信息
            $("#instanceTitleTip").html("");
            //button字体变粗
            for (i = 0; i < document.getElementsByTagName("INPUT").length; i++) {
                if (document.getElementsByTagName("INPUT")[i].type == "button" || document.getElementsByTagName("INPUT")[i].type == "submit")
                    document.getElementsByTagName("INPUT")[i].className = "buttonclass";
            }

            var Flag = $("#purchaseType").val();
            $("#purchaseTypeMsg option[value='" + Flag + "']").attr("selected", "selected");
            var Flag1 = $("#singleValue").val();
            $("#single option[value='" + Flag1 + "']").attr("selected", "selected");
        });
        //审核
        function check(supplierID) {
            var path = "/supplier.do?method=edit&supplierID=" + supplierID;
            commonFun(path);
        }

    </script>
    <base target="_self"/>
</head>

<body style="overflow-y: auto;padding: 0 100px" onload="">
<br/>
<form id="instanceInforForm" name="instanceInforForm" action="/supplier.do?method=saveEdit" method="post">
    <div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 98%">
        <div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
            <div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
                <span class="ui-jqgrid-title">新增供方信息 &nbsp;</span>
            </div>

            <div>
                <table cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable" style="width: 90%;">
                    <tbody id="flowTemplate">
                    <tr>
                        <td style="width: 18%"><input type="hidden" name="supplierID" value="${supplier.supplierID}"/>
                        </td>
                        <td style="width: 40%"></td>
                        <td style="width: 15%"></td>
                    </tr>
                    <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
                        <td class="ui-state-default jqgrid-rownum" style="width: 15%">供方名称：</td>
                        <td style="padding-top: 5px;padding-bottom: 5px;"><input id="supplierName" <c:if test="${supplier.supplierStatus != '潜在待审核'}"> readonly</c:if> name="supplierName"
                                                                                 value="${supplier.supplierName}"/>
                            <div id="instanceSupplierNameTip"></div>
                        </td>
                    </tr>
                    <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
                        <td class="ui-state-default jqgrid-rownum" style="width: 18%">联系方式：</td>
                        <td colspan="3"><input id="supplierContact" name="supplierContact"
                                               value="${supplier.supplierContact}"
                                               onkeyup="value=value.replace(/[^0-9]/g,'')"/>
                            <div id="instanceSupplierContactTip"></div>
                        </td>
                    </tr>
                    <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
                        <td class="ui-state-default jqgrid-rownum" style="width: 18%">地址：</td>
                        <td colspan="3"><input id="supplierAddress" name="supplierAddress"
                                               value="${supplier.supplierAddress}"/>
                            <div id="instanceSupplierAddressTip"></div>
                        </td>
                    </tr>

                    <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
                        <td class="ui-state-default jqgrid-rownum" style="width: 18%">采购分类：</td>
                        <td colspan="3">
                            <input type="hidden" id="purchaseType" value="${supplier.purchaseTypeMsg}"/>
                            <select id="purchaseTypeMsg" name="purchaseTypeMsg">
                                <option value="">-请选择-</option>
                                <option value="一般采购">一般采购</option>
                                <option value="业务采购">业务采购</option>
                                <option value="工程采购">工程采购</option>
                                <option value="零星采购">零星采购</option>
                            </select>
                            <div id="instancePurchaseTypeMsgTip"></div>
                        </td>
                    </tr>
                    <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
                        <td class="ui-state-default jqgrid-rownum" style="width: 18%">服务明细：</td>
                        <td colspan="3"><input id="serviceDetail" name="serviceDetail"
                                               value="${supplier.serviceDetail}"/>
                            <div id="instanceServiceDetailTip"></div>
                        </td>
                    </tr>
                    <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
                        <td class="ui-state-default jqgrid-rownum" style="width: 18%">服务时间：</td>
                        <td colspan="3"><input id="serviceYear" name="serviceYear" value="${supplier.serviceYear}"/>
                            <div id="instanceServiceYearTip"></div>
                        </td>
                    </tr>
                    <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
                        <td class="ui-state-default jqgrid-rownum" style="width: 18%">单一供方：</td>
                        <td colspan="3">
                            <input id="singleValue" type="hidden" value="${supplier.single}"/>
                            <select id="single" name="single">
                                <option value="">-请选择-</option>
                                <option value="否">否</option>
                                <option value="是">是</option>
                            </select>
                            <div id="instanceSingleTip"></div>
                        </td>
                    </tr>
                    </tbody>
                </table>
                <div class="ui-state-default ui-jqgrid-pager ui-corner-bottom" dir="ltr"
                     style="width: 100%;margin-top:20px;overflow:visible">
                    <input style="cursor: pointer;" type="submit" value="保存"/>
                    <input style="cursor: pointer;" type="button" value="关闭" onclick="window.close();"/>
                </div>
            </div>
        </div>
    </div>
</form>
</body>

</html>
                  
