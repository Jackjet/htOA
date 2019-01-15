<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="/inc/taglibs.jsp" %>
<script>
    $(document).ready(function(){
        $(".price").val(0.0)
        $(".price").focus(function(){
            this.value='';
        });
        $(".price").blur(function(){
            if (this.value==''){
                this.value=0.0;
            }
            this.val(this.value);
        });

    });
    $(function () {
        $(".price").keyup(function(){
            $(this).val($(this).val().replace(/[^0-9.]/g,''));
        }).bind("paste",function(){  //CTR+V事件处理
            $(this).val($(this).val().replace(/[^0-9.]/g,''));
        }).css("ime-mode", "disabled"); //CSS设置输入法不可用
    });
    function outPrice(name){
        var tel="";
        $.ajax({
            type : 'POST',
            url : "${pageContext.request.contextPath}/bid.do?method=outPrice",
            //需要页面传入值
            data :{supplierName:name,bidInfoId:$("#bidInfoId").val()},
            dataType : "JSON",
            success : function(data) {
                $("#finalPrice").val(data);
            },
            error:function (data) {

            }
        });
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
            <td colspan="2">${bidInfo.projectName}</td>
        </tr>
        <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
            <td class="ui-state-default jqgrid-rownum" style="width:60px">招标编号：</td>
            <td style="width: 70%">${bidInfo.zbCode}</td>
        </tr>
        <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
            <td class="ui-state-default jqgrid-rownum" style="width: 10%">采购类型：</td>
            <td style="width: 70%">${bidInfo.purchaseType}</td>
        </tr>
        <c:if test="${bidInfo.zhaotouStatus==2 && canSave}">
            <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
                <td class="ui-state-default jqgrid-rownum" style="width: 10%">采购预算：</td>
                <td style="width: 70%"><input name="zhaotouBudget" value="${purchase.purchaseMoney}"></td>
            </tr>
        </c:if>
        <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
            <td class="ui-state-default jqgrid-rownum" style="width: 20%">申请人：</td>
            <td style="width: 70%">${bidInfo.zhaotouApplier.person.personName}</td>
        </tr>
        <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
            <td class="ui-state-default jqgrid-rownum" style="width: 20%">申请部门：</td>
            <td style="width: 70%">${bidInfo.zhaotouDepartment.organizeName}</td>
        </tr>
        <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
            <td class="ui-state-default jqgrid-rownum" style="width: 20%">项目情况说明：</td>
            <td>${bidInfo.zhaotouMemo}</td>
        </tr>
        <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
            <td class="ui-state-default jqgrid-rownum" style="width: 20%">采购经办人：</td>
            <td>${bidInfo.purchaseExecutor.person.personName}</td>
        </tr>
        <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
            <td class="ui-state-default jqgrid-rownum" style="width: 20%">供方信息：</td>
            <td style="width: 70%">
                <table>
                    <tr>
                        <td style="width: 5%">投标单位</td>
                        <td style="width: 5%">投标单价(元)</td>
                        <td style="width: 5%">投标总价(元)</td>
                        <td style="width: 5%">单位资质</td>
                        <td style="width: 5%">交货周期</td>
                        <td style="width: 5%">备注</td>
                        <td style="width: 5%">附件</td>
                    </tr>
                    <c:forEach var="item" items="${bidInfo.suppliers}">
                        <tr>
                            <td style="border: none">${item.supplierName}</td>
                            <td style="border: none">${item.unitPrice}</td>
                            <td style="border: none">${item.totalPrice}</td>
                            <td style="border: none">${item.qualification}</td>
                            <td style="border: none">${item.responseTime}</td>
                            <td style="border: none">${item.memo}</td>
                            <td style="border: none"><c:forEach var="file" items="${item.attach}" varStatus="status"><a
                                    style="text-decoration:underline"
                                    href="<c:url value="${'/common/'}"/>download.jsp?filepath=${file}"><span
                                    color="white">${fn:split(file, "/")[3]}</span></a><br/></c:forEach></td>
                        </tr>
                    </c:forEach>
                </table>
            </td>
        </tr>
        <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
            <td class="ui-state-default jqgrid-rownum" style="width: 20%">评标小组审核:</td>
            <td class="ui-state-default jqgrid-rownum">
                <br/>
                <c:forEach var="s" varStatus="a" items="${bidInfo.scores}"
                           step="${fn:length(bidInfo.template.templateInfos)*fn:length(bidInfo.suppliers)}">
                    <c:if test="${s.score==null}">
                        ${s.checker.person.personName}:
                        <span style="height: 100px">
                            <font color="red">未评分</font><br/><br/>
                        </span>
                    </c:if>
                    <c:if test="${s.score!=null}">
                        ${s.checker.person.personName}:
                        <span style="height: 100px">
                            <font color="#adff2f">已评分</font>&nbsp;&nbsp;
                            <a href="/bid.do?method=detail&bidInfoId=${bidInfo.bidInfoId}&checkerId=${s.checker.personId}" target="_blank">查看详情</a><br/><br/>
                        </span>
                    </c:if>
                </c:forEach>
            </td>
        </tr>
        <c:if test="${bidInfo.zhaotouStatus==2 && canSave}">
        <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
            <td class="ui-state-default jqgrid-rownum" style="width: 20%">招标详细情况:</td>
            <td class="ui-state-default jqgrid-rownum">
                <table>
                <tr>
                    <td>招投供方名称</td>
                    <c:forEach var="total" varStatus="t" items="${bidInfo.totals}">
                        <td><input name="supplierName${t.count}" value="${total.supplierName}"></td>
                    </c:forEach>
                </tr>
                <tr>
                    <td>招投价格</td>
                    <c:forEach var="total" varStatus="a" items="${bidInfo.totals}">
                        <td><input  name="price${a.count}" value="${total.price}" readonly/></td>
                    </c:forEach>
                </tr>
                <tr>
                    <td>技术平均分</td>
                    <c:forEach var="total" varStatus="b" items="${bidInfo.totals}">
                        <td><input name="jsAvgScore${b.count}" value="${total.jsAvgScore}" readonly/></td>
                    </c:forEach>
                </tr>
                <tr>
                    <td>商务平均分</td>
                    <c:forEach var="total" varStatus="c" items="${bidInfo.totals}">
                        <td><input name="swAvgScore${c.count}" value="${total.swAvgScore}" readonly/></td>
                    </c:forEach>
                </tr>
                <tr>
                    <td>总分</td>
                    <c:forEach var="total" varStatus="d" items="${bidInfo.totals}">
                        <td><input name="totalScore${d.count}" value="${total.totalScore}" readonly/></td>
                    </c:forEach>
                </tr>
            </table>
            </td>
        </tr>

            <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
                <td class="ui-state-default jqgrid-rownum" style="width: 20%">定标结论:</td>
                <td class="ui-state-default jqgrid-rownum">
                    推荐供应商： <select name="zhaotouFinalSupplierName" onchange="outPrice(this.value)">
                    <option value="">--请选择--</option>
                    <c:forEach var="supplier" items="${bidInfo.suppliers}">
                        <option value="${supplier.supplierName}">${supplier.supplierName}</option>
                    </c:forEach>
                </select>
                    &nbsp;&nbsp;&nbsp;价格： <input class="price" name="zhaotouFinalMoney" type="text" id="finalPrice"><br/>
                    <textarea name="zhaotouConclusion" cols="100">${bidInfo.zhaotouConclusion}</textarea>
                </td>
            </tr>
        </c:if>
        <c:if test="${bidInfo.zhaotouStatus>=3||bidInfo.zhaotouStatus==-1&&bidInfo.zhaotouFinalSupplierInfor!=null}">
            <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
                <td class="ui-state-default jqgrid-rownum" style="width: 20%">招标详细情况:</td>
                <td class="ui-state-default jqgrid-rownum">
                    <table>
                        <tr>
                            <td>招投供方名称</td>
                            <c:forEach var="total" varStatus="t" items="${bidInfo.totals}">
                                <td><input name="supplierName${t.count}" value="${total.supplierName}" readonly></td>
                            </c:forEach>
                        </tr>
                        <tr>
                            <td>招投价格</td>
                            <c:forEach var="total" varStatus="a" items="${bidInfo.totals}">
                                <td><input name="price${a.count}" value="${total.price}" readonly/></td>
                            </c:forEach>
                        </tr>
                        <tr>
                            <td>技术平均分</td>
                            <c:forEach var="total" varStatus="b" items="${bidInfo.totals}">
                                <td><input name="jsAvgScore${b.count}" value="${total.jsAvgScore}" readonly/></td>
                            </c:forEach>
                        </tr>
                        <tr>
                            <td>商务平均分</td>
                            <c:forEach var="total" varStatus="c" items="${bidInfo.totals}">
                                <td><input  name="swAvgScore${c.count}" value="${total.swAvgScore}" readonly/></td>
                            </c:forEach>
                        </tr>
                        <tr>
                            <td>总分</td>
                            <c:forEach var="total" varStatus="d" items="${bidInfo.totals}">
                                <td><input name="totalScore${d.count}" value="${total.totalScore}" readonly/></td>
                            </c:forEach>
                        </tr>
                    </table>
                </td>
            </tr>

            <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
                <td class="ui-state-default jqgrid-rownum" style="width: 20%">定标结论:</td>
                <td class="ui-state-default jqgrid-rownum">
                    推荐供应商：<input name="zhaotouFinalSupplierName" value="${bidInfo.zhaotouFinalSupplierInfor.supplierName}" readonly>
                   &nbsp;&nbsp; &nbsp;&nbsp; 价格： <input name="zhaotouFinalMoney" type="text" value="${bidInfo.zhaotouFinalMoney}" readonly><br/>
                    <textarea name="zhaotouConclusion" cols="100" readonly>${bidInfo.zhaotouConclusion}</textarea>
                </td>
            </tr>
            <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
                <td class="ui-state-default jqgrid-rownum" style="width: 20%">采购委员会批录:</td>
                <td class="ui-state-default jqgrid-rownum">
                    <br/>
                    <c:forEach var="checkInfo" items="${bidInfo.checkInfors}">
                        ${checkInfo.checker.person.personName}: &nbsp;&nbsp;<c:if test="${checkInfo.checkResult==null}"><font
                            color="red">未审核</font><br/><br/></c:if><c:if test="${checkInfo.checkResult==1}"><font
                            color="#adff2f">同意</font> &nbsp;&nbsp;${checkInfo.checkTime}<br/><br/></c:if><c:if
                            test="${checkInfo.checkResult==0}"><font color="red">不同意</font>&nbsp;&nbsp;${checkInfo.checkTime}<br/><br/></c:if>
                    </c:forEach>
                </td>
            </tr>
        </c:if>
        </tbody>
    </table>
</div>
