<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="/inc/taglibs.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <title>评分</title>
    <script src="<c:url value="/"/>datePicker/WdatePicker.js" type="text/javascript"></script>
    <script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
    <script src="<c:url value="/"/>components/jqgrid/js/jquery-ui-1.8.2.custom.min.js" type="text/javascript"></script>
    <!--jquery ui-->
    <script src="<c:url value="/"/>js/inc_javascript.js"></script>
    <script src="<c:url value="/"/>js/addattachment.js"></script>
    <link rel="stylesheet" type="text/css" href="<c:url value="/"/>components/jqgrid/css/jquery.ui.all.css"/>
    <link rel="stylesheet" type="text/css" media="screen"
          href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css"/>
    <link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css"/>
    <link rel="stylesheet" type="text/css" media="screen" href="/css/border.css"/>
    <!-- formValidator -->
    <link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css"></link>
    <script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript"
            charset="UTF-8"></script>
    <script src="<c:url value="/"/>components/formvalidator/js/formValidatorRegex.js" type="text/javascript"
            charset="UTF-8"></script>
    <!-- ------------- -->

    <style type="text/css">
        .buttonclass {
            font-weight: bold;
        }
    </style>


    <script type="text/javascript">
        $().ready(function () {
            //button字体变粗
            for (i = 0; i < document.getElementsByTagName("INPUT").length; i++) {
                if (document.getElementsByTagName("INPUT")[i].type == "button" || document.getElementsByTagName("INPUT")[i].type == "submit")
                    document.getElementsByTagName("INPUT")[i].className = "buttonclass";
            }

        });
        //表单非空验证
        function myCheck()
        {
            for(var i=0;i < document.scoreForm.elements.length-1;i++)
            {
                if(document.scoreForm.elements[i].value==""&&document.scoreForm.elements[i].type!="file")
                {
                    document.scoreForm.elements[i].focus();
                    alert("表单未填写完整！")
                    return false;
                }
            }
            return true;
        }
        //审核
        function check(bidInfoId,checkerId) {
            var path = "/bid.do?method=score&bidInfoId=" + bidInfoId+"&checkerId="+checkerId;
            commonFun(path);
        }
        function validateNum(obj,score,value) {
                if(value>=0 && value <= score){

                }else{
                    alert("请输入小于分值的整数!");
                    obj.value=null;
                }
            var tempArray1=document.getElementsByClassName("tech1");
            var tempArray2=document.getElementsByClassName("tech2");
            var tempArray3=document.getElementsByClassName("tech3");
            var tempArray4=document.getElementsByClassName("tech4");
            var tempArray5=document.getElementsByClassName("tech5");
            var arrayValue1=0;
            var arrayValue2=0;
            var arrayValue3=0;
            var arrayValue4=0;
            var arrayValue5=0;
            for(i=0;i<tempArray1.length;i++)
            {
                arrayValue1+=Number(tempArray1[i].value);
            }
            for(i=0;i<tempArray2.length;i++)
            {
                arrayValue2+=Number(tempArray2[i].value);
            }
            for(i=0;i<tempArray3.length;i++)
            {
                arrayValue3+=Number(tempArray3[i].value);
            }
            for(i=0;i<tempArray4.length;i++)
            {
                arrayValue4+=Number(tempArray4[i].value);
            }
            for(i=0;i<tempArray5.length;i++)
            {
                arrayValue5+=Number(tempArray5[i].value);
            }
            $("#totalTech0").val(arrayValue1);
            $("#totalTech1").val(arrayValue2);
            $("#totalTech2").val(arrayValue3);
            $("#totalTech3").val(arrayValue4);
            $("#totalTech4").val(arrayValue5);

            var array1=document.getElementsByClassName("biz1");
            var array2=document.getElementsByClassName("biz2");
            var array3=document.getElementsByClassName("biz3");
            var array4=document.getElementsByClassName("biz4");
            var array5=document.getElementsByClassName("biz5");
            var value1=0;
            var value2=0;
            var value3=0;
            var value4=0;
            var value5=0;
            for(i=0;i<array1.length;i++)
            {
               value1+=Number(array1[i].value);
            }
            for(i=0;i<array2.length;i++)
            {
                value2+=Number(array2[i].value);
            }
            for(i=0;i<array3.length;i++)
            {
                value3+=Number(array3[i].value);
            }
            for(i=0;i<array4.length;i++)
            {
                value4+=Number(array4[i].value);
            }
            for(i=0;i<array5.length;i++)
            {
                value5+=Number(array5[i].value);
            }
            $("#totalBiz0").val(value1);
            $("#totalBiz1").val(value2);
            $("#totalBiz2").val(value3);
            $("#totalBiz3").val(value4);
            $("#totalBiz4").val(value5);
        }

        $(function () {
            $(".score").keyup(function(){
                $(this).val($(this).val().replace(/[^0-9]/g,''));
            }).bind("paste",function(){  //CTR+V事件处理
                $(this).val($(this).val().replace(/[^0-9]/g,''));
            }).css("ime-mode", "disabled"); //CSS设置输入法不可用
        });
    </script>
</head>
<body style="overflow-y: auto;padding: 0 100px" onload="">
<br/>
<form id="scoreForm" name="scoreForm" method="post" action="/bid.do?method=score" onsubmit="return myCheck();">
    <div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 98%">
        <div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
            <div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix" style="color: #22FBFF">
                <span class="ui-jqgrid-title">小组评分 &nbsp;【评分人:${checker.person.personName}}】</span>
            </div>
            <div style="width: 90%">
                <hr style="border:0.5px solid #22FBFF;"/>
            </div>
            <%-- 打分信息 --%>
            <div>
                <input type="hidden" name="bidInfoId" value="${bidInfo.bidInfoId}" />
                <input type="hidden" name="checkerId" value="${checker.personId}" />
                <table cellspacing="0" cellpadding="0" border="0" style="width: 90%">
                    <tbody id="tblGrid">
                    <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
                        <td colspan="2" class="ui-state-default jqgrid-rownum" style="width: 10%">招标项目名称：</td>
                        <td colspan="2">${bidInfo.projectName}</td>
                        <td class="ui-state-default jqgrid-rownum" style="width: 10%">招标编号：</td>
                        <td colspan="2">${bidInfo.zbCode}</td>
                    </tr>
                    <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
                        <td colspan="2" class="ui-state-default jqgrid-rownum" style="width: 10%">评标人姓名：</td>
                        <td colspan="2">${checker.person.personName}</td>
                        <td class="ui-state-default jqgrid-rownum" style="width:60px">工作部门：</td>
                        <td colspan="2">${checker.person.department.organizeName}</td>
                    </tr>
                    <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
                        <td colspan="2" class="ui-state-default jqgrid-rownum" style="width: 10%">联系电话：</td>
                        <td colspan="2">${checker.person.officePhone}</td>
                        <td class="ui-state-default jqgrid-rownum" style="width: 10%">邮件：</td>
                        <td colspan="2">${checker.person.email}</td>
                    </tr>

                    <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
                        <td colspan="2" class="ui-state-default jqgrid-rownum" style="width: 20%">供方信息：</td>
                        <td colspan="5" style="width: 70%">
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
                        <td colspan="7" class="ui-state-default jqgrid-rownum" style="width: 10%">技术标评审 权重(${bidInfo.template.jsWeight})</td>
                    </tr>
                    <tr>
                        <td style="width: 5%">编号</td>
                        <td style="width: 5%">指标</td>
                        <td style="width: 5%">分值</td>
                        <td style="width: 5%">标准</td>
                        <c:forEach items="${bidInfo.suppliers}" var="supplier">
                            <td style="width: 10%">${supplier.supplierName}</td>
                        </c:forEach>
                    </tr>
                    <c:forEach var="template" varStatus="a" items="${bidInfo.template.templateInfos}">
                    <c:if test="${template.type eq '技术标'}">
                    <tr>
                        <td>${a.count}</td>
                        <td>${template.target}</td>
                        <td><div id="topj${a.count}">${template.score}</div></td>
                        <td>${template.standard}</td>
                        <c:forEach items="${bidInfo.suppliers}" varStatus="c" var="supplier">
                            <td>
                                <input class="score tech${c.count}" placeholder="请输入整数" name="score${a.count}${c.count}" value="<c:forEach var="score" items="${bidInfo.scores}"><c:if test="${score.zhaotouTemplateInfo.zhaotouTemplateInfoId eq template.zhaotouTemplateInfoId && score.supplierDesc.supplierName eq supplier.supplierName && score.checker.personId eq checker.personId}">${score.score}</c:if></c:forEach>" onblur="validateNum(this,${template.score},this.value)"/>
                            </td>
                        </c:forEach>
                    </tr>
                    </c:if>
                    </c:forEach>
                    <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
                        <td colspan="4" class="ui-state-default jqgrid-rownum" style="width: 10%">技术标总分</td>
                        <c:forEach items="${bidInfo.totals}" varStatus="s" var="total">
                            <td style="width: 10%"><input id="totalTech${s.index}" name="totalTech${s.index}" value="<c:forEach var="each" items="${total.eachTotals}"><c:if test="${each.scorer.personId eq checker.personId}">${each.totalTech}</c:if></c:forEach>" readonly/></td>
                        </c:forEach>
                    </tr>
                    <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
                        <td colspan="7" class="ui-state-default jqgrid-rownum" style="width: 10%">商务标评审 权重(${bidInfo.template.swWeight})</td>
                    </tr>
                    <tr>
                        <td>编号</td>
                        <td>指标</td>
                        <td>分值</td>
                        <td>标准</td>
                        <c:forEach items="${bidInfo.suppliers}" var="supplier">
                            <td>${supplier.supplierName}</td>
                        </c:forEach>
                    </tr>
                    <c:forEach var="template" varStatus="a" items="${bidInfo.template.templateInfos}">
                    <c:if test="${template.type eq '商务标'}">
                    <tr>
                        <td>${a.count}</td>
                        <td>${template.target}</td>
                        <td>${template.score}</td>
                        <td>${template.standard}</td>
                        <c:forEach items="${bidInfo.suppliers}" varStatus="b" var="supplier">
                            <td>
                                <input id="score${a.count}${b.count}" class="score biz${b.count}" placeholder="请输入整数" name="score${a.count}${b.count}" value="<c:forEach var="score" items="${bidInfo.scores}"><c:if test="${score.zhaotouTemplateInfo.zhaotouTemplateInfoId eq template.zhaotouTemplateInfoId && score.supplierDesc.supplierName eq supplier.supplierName && score.checker.personId eq checker.personId}">${score.score}</c:if></c:forEach>" onblur="validateNum(this,${template.score},this.value)"/>
                            </td>
                        </c:forEach>
                    </tr>
                    </c:if>
                    </c:forEach>
                    <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 15px;">
                        <td colspan="4" class="ui-state-default jqgrid-rownum" style="width: 10%">商务标总分</td>
                        <c:forEach items="${bidInfo.totals}" varStatus="s" var="total">
                            <td style="width: 10%"><input id="totalBiz${s.index}" name="totalBiz${s.index}" value="<c:forEach var="each" items="${total.eachTotals}"><c:if test="${each.scorer.personId eq checker.personId}">${each.totalBiz}</c:if></c:forEach>" readonly/></td>
                        </c:forEach>
                    </tr>
                </table>
            </div>

            <div style="width: 100%" class="ui-state-default ui-jqgrid-pager ui-corner-bottom" dir="ltr">
                <c:if test="${canEdit}">
                <input style="cursor: pointer;" type="submit" value="提交"/>
                </c:if>
                &nbsp;&nbsp;
                <input style="cursor: pointer;" type="button" value="关闭" onclick="window.close();"/>
            </div>
        </div>
    </div>
</form>
</body>
</html>

