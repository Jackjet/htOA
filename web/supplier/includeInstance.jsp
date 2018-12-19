<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="/inc/taglibs.jsp" %>

<style>
    .ui-jqgrid tr.jqgrow td {
        white-space: normal;
    }
</style>
<link href="<c:url value="/"/>css/submit.css" type="text/css" rel="stylesheet">

<div>
    <table cellspacing="0" cellpadding="0" border="0" style="width: 90%">
        <tbody id="tblGrid">
        <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
            <td class="ui-state-default jqgrid-rownum" style="width:60px">供方名称：</td>
            <td style="width: 70%">${supplier.supplierName}</td>
        </tr>
        <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
            <td class="ui-state-default jqgrid-rownum" style="width: 10%">联系方式：</td>
            <td style="width: 70%">${supplier.supplierContact}</td>
        </tr>
        <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
            <td class="ui-state-default jqgrid-rownum" style="width: 20%">地址：</td>
            <td style="width: 70%">${supplier.supplierAddress}</td>
        </tr>
        <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
            <td class="ui-state-default jqgrid-rownum" style="width: 20%">采购分类：</td>
            <td style="width: 70%">${supplier.purchaseTypeMsg}</td>
        </tr>
        <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
            <td class="ui-state-default jqgrid-rownum" style="width: 20%">服务明细：</td>
            <td>${supplier.serviceDetail}</td>
        </tr>
        <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
            <td class="ui-state-default jqgrid-rownum" style="width: 20%">服务时间：</td>
            <td>${supplier.serviceYear}</td>
        </tr>
        <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
            <td class="ui-state-default jqgrid-rownum" style="width: 20%">单一供方：</td>
            <td>${supplier.single}</td>
        </tr>
        <c:if test="${supplier.supplierStatus=='合格认证中'||supplier.supplierStatus=='合格终审中'||supplier.supplierStatus=='合格'}">
            <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
                <td class="ui-state-default jqgrid-rownum" style="width: 20%">质量评定：</td>
                <td>${certify.quality}</td>
            </tr>
            <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
                <td class="ui-state-default jqgrid-rownum" style="width: 20%">价格评定：</td>
                <td>${certify.price}</td>
            </tr>
            <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
                <td class="ui-state-default jqgrid-rownum" style="width: 20%">服务评定：</td>
                <td>${certify.service}</td>
            </tr>
            <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
                <td class="ui-state-default jqgrid-rownum" style="width: 20%">交期评定：</td>
                <td>${certify.delivery}</td>
            </tr>
            <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
                <td class="ui-state-default jqgrid-rownum" style="width: 20%">管理评定：</td>
                <td>${certify.management}</td>
            </tr>
            <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
                <td class="ui-state-default jqgrid-rownum" style="width: 20%">有效期至：</td>
                <td>${fn:substring(certify.endDate,0,19)}</td>
            </tr>
            <c:forEach var="item" varStatus="abc" items="${supplier.checkVOS}">
                <c:if test="${item.layer==2&&item.lastOne}">
                    <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
                        <c:if test="${abc.count==lv_1+1}">
                        <td class="ui-state-default jqgrid-rownum" rowspan="${rows}" style="width: 20%">合格认证:</td>
                        </c:if>
                        <td class="ui-state-default jqgrid-rownum">
                                ${item.checkerName}：
                            <span style="height: 100px"><c:if test="${item.checkResult==0}">不同意</c:if><c:if
                                    test="${item.checkResult==1}">同意</c:if><c:if
                                    test="${item.checkResult==null}"><font
                                    color="red">未审核</font></c:if></span>&nbsp;&nbsp;
                            <span style="height: 100px">${item.checkDate}</span>
                        </td>
                    </tr>
                </c:if>
                <c:if test="${item.layer==3&&item.lastOne}">
                    <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
                        <td class="ui-state-default jqgrid-rownum" style="width: 20%">合格终审:</td>
                        <td class="ui-state-default jqgrid-rownum">
                                ${item.checkerName}：
                            <span style="height: 100px"><c:if test="${item.checkResult==0}">不同意</c:if><c:if
                                    test="${item.checkResult==1}">同意</c:if><c:if
                                    test="${item.checkResult==null}"><font
                                    color="red">未审核</font></c:if></span>&nbsp;&nbsp;
                            <span style="height: 100px">${item.checkDate}</span>
                        </td>
                    </tr>
                </c:if>
            </c:forEach>
            <c:if test="${supplier.supplierStatus=='合格终审中'}">
                <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
                    <td class="ui-state-default jqgrid-rownum" style="width: 20%">合格终审:</td>
                    <td class="ui-state-default jqgrid-rownum">
                        于晔：<font color="red">审核中</font>
                    </td>
                </tr>
            </c:if>
        </c:if>
        </tbody>
    </table>
</div>
