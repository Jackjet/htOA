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
            <td class="ui-state-default jqgrid-rownum" style="width: 15%">供方名称：</td>
            <td>${supplier.supplierName}</td>
        </tr>
        <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
            <td class="ui-state-default jqgrid-rownum" style="width: 18%">地址：</td>
            <td>${supplier.supplierAddress}</td>
        </tr>
        <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
            <td class="ui-state-default jqgrid-rownum" style="width: 18%">联系方式：</td>
            <td>${supplier.supplierTel}</td>
        </tr>
        <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
            <td class="ui-state-default jqgrid-rownum" style="width: 18%">服务明细：</td>
            <td>${supplier.serviceDetail}</td>
        </tr>
        <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
            <td class="ui-state-default jqgrid-rownum" style="width: 18%">服务时间：</td>
            <td>${supplier.serviceYear}</td>
        </tr>
        <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
            <td class="ui-state-default jqgrid-rownum" style="width: 18%">公司性质：</td>
            <td>${supplier.companyType}</td>
        </tr>
        <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
            <td class="ui-state-default jqgrid-rownum" style="width: 18%">采购分类：</td>
            <td>${supplier.purchaseTypeMsg}</td>
        </tr>
        <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
            <td class="ui-state-default jqgrid-rownum" style="width: 18%">单一供方：</td>
            <td>${supplier.singleOne}</td>
        </tr>
        <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
            <td class="ui-state-default jqgrid-rownum" style="width: 18%">是否通过质量体系认证：</td>
            <td>${supplier.pass}</td>
        </tr>
        <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
            <td class="ui-state-default jqgrid-rownum" style="width: 18%">与海通业务相关性：</td>
            <td>${supplier.relevance}</td>
        </tr>
        <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
            <td class="ui-state-default jqgrid-rownum">归口部门：</td>
            <td>
                <c:forEach items="${supplier.organizeNames}" var="deptName">
                    ${deptName}&nbsp;&nbsp;
                </c:forEach>
            </td>
        </tr>
        <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
            <td class="ui-state-default jqgrid-rownum" style="width: 15%">供方相关资质：</td>
            <td>
                <table id="quaTable" cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable"
                       style="width: 90%;">
                    <thead>
                    <tr>
                        <th class="ui-state-default jqgrid-rownum" style="width: 10%;text-align: left">编号</th>
                        <th class="ui-state-default jqgrid-rownum" style="width: 10%;text-align: left">证书资质名称</th>
                        <th class="ui-state-default jqgrid-rownum" style="width: 10%;text-align: left">到期时间</th>
                        <th class="ui-state-default jqgrid-rownum" style="width: 10%;text-align: left">相关附件</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${supplier.qualificationVOS}" var="quas">
                        <tr>
                            <td>${quas.qualificationCode}</td>
                            <td>${quas.qualificationName}</td>
                            <td>${quas.endTime}</td>
                            <td>
                                <a style="text-decoration:underline" href="<c:url value="${'/common/'}"/>download.jsp?filepath=${quas.attach}"><span color="white">${fn:split(quas.attach, "/")[3]}</span></a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </td>
        </tr>
        <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
            <td class="ui-state-default jqgrid-rownum" style="width: 15%">供方行业背景调查：</td>
            <td>
                <table id="bgTable" cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable"
                       style="width: 90%;">
                    <thead>
                    <tr>
                        <th class="ui-state-default jqgrid-rownum" style="width: 10%;text-align: left">编号</th>
                        <th class="ui-state-default jqgrid-rownum" style="width: 10%;text-align: left">客户名称</th>
                        <th class="ui-state-default jqgrid-rownum" style="width: 10%;text-align: left">服务内容</th>
                        <th class="ui-state-default jqgrid-rownum" style="width: 10%;text-align: left"></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${supplier.backgroundVOS}" var="back">
                        <tr>
                            <td>${back.backCode}</td>
                            <td>${back.clientName}</td>
                            <td>${back.serviceContent}</td>
                            <td></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </td>
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
