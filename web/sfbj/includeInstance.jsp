<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="/inc/taglibs.jsp" %>

<script>
    //编辑审核层
    function editLayer(layerId) {
        var path = "<c:url value='/workflow/layerInfor.do'/>?method=edit&layerId=" + layerId;
        window.name = "__self";
        window.open(path, "__self");
        //window.location.href = path;
    }

    //编辑审核信息
    function editCheckInfor(checkId) {
        var path = "<c:url value='/workflow/checkInfor.do'/>?method=edit&chargerEdit=true&instanceId=${_Instance.instanceId}&checkId=" + checkId;
        window.name = "__self";
        window.open(path, "__self");
        //window.location.href = path;
    }

    //删除审核层
    function deleteLayer(layerId) {
        var yes = window.confirm("确定要删除该审核层吗？");
        if (yes) {
            $.ajax({
                url: "/workflow/layerInfor.do?method=delete&layerId=" + layerId,
                type: "post",
                dataType: "json",
                async: false,	//设置为同步
                beforeSend: function (xhr) {
                },
                complete: function (req, err) {
                    var returnValues = eval("(" + req.responseText + ")");
                    alert(returnValues["message"]);
                    reloadPage('${_Instance.instanceId}');
                }
            });
        }
    }

</script>
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
            <td colspan="2">${sanfangInfor.sanfangTitle}</td>
        </tr>
        <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
            <td class="ui-state-default jqgrid-rownum" style="width: 10%">采购类型：</td>
            <td style="width: 70%">${sanfangInfor.purchaseType}</td>
        </tr>
        <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
            <td class="ui-state-default jqgrid-rownum" style="width: 20%">申请人：</td>
            <td style="width: 70%">${sanfangInfor.sanfangApplier.person.personName}</td>
        </tr>
        <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
            <td class="ui-state-default jqgrid-rownum" style="width: 20%">申请部门：</td>
            <td style="width: 70%">${sanfangInfor.sanfangDepartment.organizeName}</td>
        </tr>
        <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
            <td class="ui-state-default jqgrid-rownum" style="width: 20%">申请时间：</td>
            <td>
                <fmt:formatDate var="applyTime" value="${sanfangInfor.applyDate}" pattern="yyyy-MM-dd" />
                ${applyTime}
            </td>
        </tr>
        <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
            <td class="ui-state-default jqgrid-rownum" style="width: 20%">需求说明：</td>
            <td>${sanfangInfor.sanfangDesc}</td>
        </tr>
        <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
            <td class="ui-state-default jqgrid-rownum" style="width: 20%">需求附件：</td>
            <td><c:forEach var="file" items="${sanfangAttachNames}" varStatus="status"><a
                    style="text-decoration:underline"
                    href="<c:url value="${'/common/'}"/>download.jsp?filepath=${sanfangAttachments[status.index]}"><span
                    color="white">${file}</span></a><br/></c:forEach></td>
        </tr>
        <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
            <td class="ui-state-default jqgrid-rownum" style="width: 20%">供方信息：</td>
            <td style="width: 70%">
                <table>
                    <thead>
                    <tr>
                        <th class="ui-state-default jqgrid-rownum" style="width: 15%">供应商名称</th>
                        <th class="ui-state-default jqgrid-rownum" style="width: 15%">联系电话</th>
                        <th class="ui-state-default jqgrid-rownum" style="width: 28%">方案描述</th>
                        <th class="ui-state-default jqgrid-rownum" style="width: 16%">方案附件</th>
                        <th class="ui-state-default jqgrid-rownum" style="width: 8%">总报价</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="item" items="${sanfangInfor.supplierInfors}" varStatus="s">
                        <tr>
                            <td style="border: none">${item.supplierInfor.supplierName}</td>
                            <td style="border: none">${item.supplierInfor.supplierContact}</td>
                            <td style="border: none">${item.supplierDesc}</td>
                            <td style="border: none">
                                <c:if test="${s.index==0}">
                                    <c:forEach var="file" items="${supplierAttachNames0}" varStatus="status"><a
                                            style="text-decoration:underline"
                                            href="<c:url value="${'/common/'}"/>download.jsp?filepath=${supplierAttachments0[status.index]}"><span
                                            color="white">${file}</span></a><br/></c:forEach>
                                </c:if>
                                <c:if test="${s.index==1}">
                                    <c:forEach var="file" items="${supplierAttachNames1}" varStatus="status"><a
                                            style="text-decoration:underline"
                                            href="<c:url value="${'/common/'}"/>download.jsp?filepath=${supplierAttachments1[status.index]}"><span
                                            color="white">${file}</span></a><br/></c:forEach>
                                </c:if>
                                <c:if test="${s.index==2}">
                                    <c:forEach var="file" items="${supplierAttachNames2}" varStatus="status"><a
                                            style="text-decoration:underline"
                                            href="<c:url value="${'/common/'}"/>download.jsp?filepath=${supplierAttachments2[status.index]}"><span
                                            color="white">${file}</span></a><br/></c:forEach>
                                </c:if>
                            </td>
                            <td style="border: none">${item.price}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </td>
        </tr>
        <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
            <td class="ui-state-default jqgrid-rownum" style="width: 20%">结论:</td>
            <td style="width: 70%">
                ${sanfangInfor.sanfangFinalSupplier}<br/>
                ${sanfangInfor.sanfangConclusion}
            </td>
        </tr>
        <c:forEach var="item" varStatus="abc" items="${sanfangInfor.checkInfors}">
            <c:if test="${item.layer==1}">
                <tr class="ui-widget-content jqgrow ui-row-ltr">
                    <td class="ui-state-default jqgrid-rownum" style="width: 20%">采购部领导审核:</td>
                    <td class="ui-state-default jqgrid-rownum" style="width: 70%">
                            ${item.checker.person.personName}：
                        <span><c:if test="${item.checkResult==1}">同意</c:if><c:if
                                test="${item.checkResult==0}">不同意</c:if></span>&nbsp;&nbsp;
                        <span>${item.checkTime}</span>
                        <span><c:forEach var="file" items="${item.checkAttach}" varStatus="status"><a
                                style="text-decoration:underline"
                                href="<c:url value="${'/common/'}"/>download.jsp?filepath=${file}"><span
                                color="white">${fn:split(file, "/")[3]}</span></a><br/></c:forEach></span>
                    </td>
                </tr>
            </c:if>
            <c:if test="${item.layer==2 && abc.count==2&&(sanfangInfor.sanfangStatus>=2||sanfangInfor.sanfangStatus==-1)}">
                <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
                    <td class="ui-state-default jqgrid-rownum" style="width: 20%" >归口部领导审核:</td>
                    <td class="ui-state-default jqgrid-rownum">
                        <c:forEach var="item2" varStatus="index" items="${sanfangInfor.checkInfors}">
                            <c:if test="${item2.layer==2}">
                                ${item2.checker.person.personName}：
                                <span><c:if test="${item2.checkResult==1}">同意</c:if><c:if
                                        test="${item2.checkResult==0}">不同意</c:if></span><c:if
                                    test="${item2.checkResult==null}"><font
                                    color="red">未审核</font></c:if></span>&nbsp;&nbsp;&nbsp;&nbsp;
                                <span>${item2.checkTime}</span>
                                <span><c:forEach var="file" items="${item2.checkAttach}" varStatus="status"><a
                                        style="text-decoration:underline"
                                        href="<c:url value="${'/common/'}"/>download.jsp?filepath=${file}"><span
                                        color="white">${fn:split(file, "/")[3]}</span></a></c:forEach></span><br/>
                            </c:if>
                        </c:forEach>
                    </td>
                </tr>
            </c:if>
            <%--<c:if test="${item.layer==2 && abc.count==2}">--%>
                <%--<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">--%>
                    <%--<td class="ui-state-default jqgrid-rownum" style="width: 20%" >归口部领导审核:</td>--%>
                    <%--<td class="ui-state-default jqgrid-rownum">--%>
                        <%--<c:forEach var="item2" varStatus="index" items="${sanfangInfor.checkInfors}">--%>
                            <%--<c:if test="${item2.layer==2}">--%>
                                <%--${item2.checker.person.personName}：--%>
                                <%--<span><c:if test="${item2.checkResult==1}">同意</c:if><c:if--%>
                                        <%--test="${item2.checkResult==0}">不同意</c:if></span><c:if--%>
                                    <%--test="${item2.checkResult==null}"><font--%>
                                    <%--color="red">未审核</font></c:if></span>&nbsp;&nbsp;&nbsp;&nbsp;--%>
                                <%--<span>${item2.checkTime}</span>--%>
                                <%--<span><c:forEach var="file" items="${item2.checkAttach}" varStatus="status"><a--%>
                                        <%--style="text-decoration:underline"--%>
                                        <%--href="<c:url value="${'/common/'}"/>download.jsp?filepath=${file}"><span--%>
                                        <%--color="white">${fn:split(file, "/")[3]}</span></a></c:forEach></span><br/>--%>
                            <%--</c:if>--%>
                        <%--</c:forEach>--%>
                    <%--</td>--%>
                <%--</tr>--%>
            <%--</c:if>--%>
            <c:if test="${item.layer==3 && abc.count==layer2+2}">
                <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
                    <td class="ui-state-default jqgrid-rownum" style="width: 20%">审批小组审核:</td>
                    <td class="ui-state-default jqgrid-rownum">
                        <c:forEach var="item2" varStatus="index" items="${sanfangInfor.checkInfors}">
                            <c:if test="${item2.layer==3}">
                                ${item2.checker.person.personName}：
                                <span style="height: 100px"><c:if test="${item2.checkResult==0}">不同意</c:if><c:if
                                        test="${item2.checkResult==1}">同意</c:if><c:if
                                        test="${item2.checkResult==null}"><font
                                        color="red">未审核</font></c:if></span>&nbsp;&nbsp;
                                <span style="height: 100px">${item2.checkTime}</span>
                                <span><c:forEach var="file" items="${item2.checkAttach}" varStatus="status"><a
                                        style="text-decoration:underline"
                                        href="<c:url value="${'/common/'}"/>download.jsp?filepath=${file}"><span
                                        color="white">${fn:split(file, "/")[3]}</span></a></c:forEach></span><br/>
                            </c:if>
                        </c:forEach>
                    </td>
                </tr>
            </c:if>
        </c:forEach>
        <c:choose>
            <c:when test="${sanfangInfor.sanfangStatus==1}">
                <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
                    <td class="ui-state-default jqgrid-rownum" style="width: 20%">采购部领导审核:</td>
                    <td style="width: 70%">
                        <font color="red">${purchaseLeader}&nbsp;审核中</font>
                    </td>
                </tr>
            </c:when>
            <c:when test="${sanfangInfor.sanfangStatus==2}">
                <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
                    <td class="ui-state-default jqgrid-rownum" style="width: 20%">归口部领导审核:</td>
                    <td style="width: 70%">
                        <font color="red"><c:if test="${purchaseInfo.guikouDepartment.organizeName=='技术规划部'}">
                            <c:forEach items="${jgers}" var="jg">
                                【${jg.person.personName}】
                            </c:forEach>
                        </c:if><c:if
                                test="${purchaseInfo.guikouDepartment.organizeName!='技术规划部'}">【${purchaseInfo.guikouDepartment.director.personName}】</c:if>&nbsp;&nbsp;审核中</font>
                    </td>
                </tr>
            </c:when>
        </c:choose>
        </tbody>
    </table>
</div>
