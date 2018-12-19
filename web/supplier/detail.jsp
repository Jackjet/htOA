<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="/inc/taglibs.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1"/>
    <%--<meta http-equiv="X-UA-Compatible" content="IE=9" />--%>
    <title>供应商详情</title>
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
    <base target="_self"/>
</head>

<body style="overflow-y: auto;padding: 0 100px" onload="">
<br/>
<form id="instanceInforForm" name="instanceInforForm" action="/supplier.do?method=save" method="post">
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
                        <td style="padding-top: 5px;padding-bottom: 5px;">${supplier.supplierName}</td>
                    </tr>
                    <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
                        <td class="ui-state-default jqgrid-rownum" style="width: 18%">联系方式：</td>
                        <td colspan="3">${supplier.supplierContact}</td>
                    </tr>
                    <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
                        <td class="ui-state-default jqgrid-rownum" style="width: 18%">地址：</td>
                        <td colspan="3">${supplier.supplierAddress}</td>
                    </tr>

                    <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
                        <td class="ui-state-default jqgrid-rownum" style="width: 18%">采购分类：</td>
                        <td colspan="3">${supplier.purchaseTypeMsg}</td>
                    </tr>
                    <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
                        <td class="ui-state-default jqgrid-rownum" style="width: 18%">服务明细：</td>
                        <td colspan="3">${supplier.serviceDetail}</td>
                    </tr>
                    <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
                        <td class="ui-state-default jqgrid-rownum" style="width: 18%">服务时间：</td>
                        <td colspan="3">${supplier.serviceYear}</td>
                    </tr>
                    <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
                        <td class="ui-state-default jqgrid-rownum" style="width: 18%">单一供方：</td>
                        <td colspan="3">${supplier.single}</td>
                    </tr>
                    </tbody>
                </table>
                <div class="ui-state-default ui-jqgrid-pager ui-corner-bottom" dir="ltr"
                     style="width: 100%;margin-top:20px;overflow:visible">
                    <input style="cursor: pointer;" type="button" value="关闭" onclick="window.close();"/>
                </div>
            </div>
        </div>
    </div>
</form>
</body>

</html>
                  
