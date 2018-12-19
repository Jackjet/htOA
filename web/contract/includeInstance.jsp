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
            <td class="ui-state-default jqgrid-rownum" style="width:60px">合同标题：</td>
            <td style="width: 70%">${contractInfo.contractName}</td>
        </tr>
        <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
            <td class="ui-state-default jqgrid-rownum" style="width: 10%">采购类型：</td>
            <td style="width: 70%">${contractInfo.purchaseType}</td>
        </tr>
        <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
            <td class="ui-state-default jqgrid-rownum" style="width: 10%">供应商名称：</td>
            <td style="width: 70%">${contractInfo.supplierName}</td>
            &nbsp;&nbsp;<span class="ui-state-default jqgrid-rownum">联系方式：</span>${contractInfo.supplierContact}
        </tr>
        <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
            <td class="ui-state-default jqgrid-rownum" style="width: 20%">变更内容：</td>
            <td>${contractInfo.contractDesc}</td>
        </tr>
        <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
            <td class="ui-state-default jqgrid-rownum" style="width: 20%">变更附件：</td>
            <td><c:forEach var="file" items="${contractAttachNames}" varStatus="status"><a
                    style="text-decoration:underline"
                    href="<c:url value="${'/common/'}"/>download.jsp?filepath=${contractAttachments[status.index]}"><span
                    color="white">${file}</span></a><br/></c:forEach></td>
        </tr>
        <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
            <td class="ui-state-default jqgrid-rownum" style="width: 20%">申请人：</td>
            <td style="width: 70%">${contractInfo.contractApplier.person.personName}</td>
        </tr>
        <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
            <td class="ui-state-default jqgrid-rownum" style="width: 20%">申请部门：</td>
            <td style="width: 70%">${contractInfo.submitDepartment.organizeName}</td>
        </tr>
        <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
            <td class="ui-state-default jqgrid-rownum" style="width: 20%">申请时间：</td>
            <td>
                <fmt:formatDate var="applyTime" value="${contractInfo.submitDate}" pattern="yyyy-MM-dd" />
                ${applyTime}
            </td>
        </tr>
        <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
            <td class="ui-state-default jqgrid-rownum" style="width: 20%">方案评估：</td>
            <td>${contractInfo.techSolution}</td>
        </tr>
        <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
            <td class="ui-state-default jqgrid-rownum" style="width: 20%">方案附件：</td>
            <td><c:forEach var="file" items="${solutionAttachNames}" varStatus="status"><a
                    style="text-decoration:underline"
                    href="<c:url value="${'/common/'}"/>download.jsp?filepath=${solutionAttachments[status.index]}"><span
                    color="white">${file}</span></a><br/></c:forEach></td>
        </tr>

        <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
            <td class="ui-state-default jqgrid-rownum" style="width: 20%">价格评估：</td>
            <td style="width: 70%">
                <table>
                    <thead>
                    <tr>
                        <th class="ui-state-default jqgrid-rownum">采购项目</th>
                        <th class="ui-state-default jqgrid-rownum">规格型号</th>
                        <th class="ui-state-default jqgrid-rownum">数量</th>
                        <th class="ui-state-default jqgrid-rownum">去年价格</th>
                        <th class="ui-state-default jqgrid-rownum">今年价格</th>
                        <th class="ui-state-default jqgrid-rownum">备注</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td style="border: none">${contractInfo.projectName}</td>
                        <td style="border: none">${contractInfo.projectModel}</td>
                        <td style="border: none">${contractInfo.projectCount}</td>
                        <td style="border: none">${contractInfo.lastYearPrice}</td>
                        <td style="border: none">${contractInfo.thisYearPrice}</td>
                        <td style="border: none">${contractInfo.projectMemo}</td>
                    </tr>
                    </tbody>
                </table>
            </td>
        </tr>
        <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
            <td class="ui-state-default jqgrid-rownum" style="width: 20%">结论:</td>
            <td style="width: 70%">${contractInfo.conclusion}</td>
        </tr>
        <c:forEach var="item" varStatus="abc" items="${contractInfo.checkInfos}">
            <c:if test="${item.layer==1&&not empty item.checkResult}">
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
            <%--<c:if test="${item.layer==2&&contractInfo.contractStatus==2}">--%>
                <%--<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">--%>
                    <%--<td class="ui-state-default jqgrid-rownum" style="width: 20%" rowspan="2">归口部领导审核:</td>--%>
                    <%--<td class="ui-state-default jqgrid-rownum">--%>
                            <%--${item.checker.person.personName}：--%>
                                <%--<span><c:if test="${item.checkResult==null}"><font color="red">未审核</font></c:if><c:if test="${item.checkResult==1}">同意</c:if><c:if--%>
                                <%--test="${item.checkResult==0}">不同意</c:if></span>&nbsp;&nbsp;--%>
                        <%--<span>${item.checkTime}</span>--%>
                        <%--<span><c:forEach var="file" items="${item.checkAttach}" varStatus="status"><a--%>
                                <%--style="text-decoration:underline"--%>
                                <%--href="<c:url value="${'/common/'}"/>download.jsp?filepath=${file}"><span--%>
                                <%--color="white">${fn:split(file, "/")[3]}</span></a></c:forEach></span><br/>--%>
                    <%--</td>--%>
                <%--</tr>--%>
            <%--</c:if>--%>

            <c:if test="${item.layer==2 && abc.count==2&&(contractInfo.contractStatus>=2||contractInfo.contractStatus==-1)}">
                <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
                    <td class="ui-state-default jqgrid-rownum" style="width: 20%" >归口部领导审核:</td>
                    <td class="ui-state-default jqgrid-rownum">
                        <c:forEach var="item2" varStatus="index" items="${contractInfo.checkInfos}">
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

            <c:if test="${item.layer==3&&(contractInfo.contractStatus>=3||contractInfo.contractStatus==-1)}">
                <tr class= "ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
                    <c:if test="${abc.count==layer2+2}">
                     <td rowspan="${layers}" class="ui-state-default jqgrid-rownum" style="width: 20%">采购审批小组审核:</td>
                    </c:if>
                    <td class="ui-state-default jqgrid-rownum">
                            ${item.checker.person.personName}：
                        <span style="height: 100px"><c:if test="${item.checkResult==0}">不同意</c:if><c:if
                                test="${item.checkResult==1}">同意</c:if><c:if
                                test="${item.checkResult==null}"><font
                                color="red">未审核</font></c:if></span>&nbsp;&nbsp;
                        <span style="height: 100px">${item.checkTime}</span>
                        <span><c:forEach var="file" items="${item.checkAttach}" varStatus="status"><a
                                style="text-decoration:underline"
                                href="<c:url value="${'/common/'}"/>download.jsp?filepath=${file}"><span
                                color="white">${fn:split(file, "/")[3]}</span></a></c:forEach></span><br/>
                    </td>
                </tr>
            </c:if>
        </c:forEach>
        <c:choose>
            <c:when test="${contractInfo.contractStatus==1}">
                <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
                    <td class="ui-state-default jqgrid-rownum" style="width: 20%">采购部领导:</td>
                    <td style="width: 70%">
                        <font color="red">${purchaseLeader}&nbsp;审核中</font>
                    </td>
                </tr>
            </c:when>
            <%--<c:when test="${contractInfo.contractStatus==2}">--%>
                <%--<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">--%>
                    <%--<td class="ui-state-default jqgrid-rownum" style="width: 20%">归口部领导:</td>--%>
                    <%--<td style="width: 70%">--%>
                        <%--<font color="red"><c:if test="${purchaseInfo.guikouDepartment.organizeName=='技术规划部'}">--%>
                            <%--<c:forEach items="${jgers}" var="jg">--%>
                                <%--【${jg.person.personName}】--%>
                            <%--</c:forEach>--%>
                        <%--</c:if><c:if test="${purchaseInfo.guikouDepartment.organizeName!='技术规划部'}">【${purchaseInfo.guikouDepartment.director.personName}】</c:if>&nbsp;&nbsp;审核中</font>--%>
                    <%--</td>--%>
                <%--</tr>--%>
            <%--</c:when>--%>
        </c:choose>
        </tbody>
    </table>
</div>
