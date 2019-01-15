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
        function addLine(isTable) {
            var index = isTable.rows.length;
            if (index < 6) {
                var html = "<tr><td style='text-align: center'><input value=" + index + " name='qualificationCode" + index + "'/></td><td style='text-align: center'><input name='qualificationName" + index + "'/></td><td style='text-align: center'><input name='endTime" + index + "'/></td><td style='text-align: center'><input id='qua" + index + "' name='qua" + index + "' type='file' onchange='changeFile(this.value," + index + ");'/><input type='hidden' name='quav" + index + "' id='quav" + index + "' /></td></tr>";
                $("#quaTable").append(html);
            } else {
                alert("不能再加啦~")
            }
        }
        function changeFile(value, index) {
            if (index == 1) {
                $("#quav1").val(value);
            }
            if (index == 2) {
                $("#quav2").val(value);
            }
            if (index == 3) {
                $("#quav3").val(value);
            }
            if (index == 4) {
                $("#quav4").val(value);
            }
            if (index == 5) {
                $("#quav5").val(value);
            }
        }
        function addLine1(isTable) {
            var index = isTable.rows.length;
            if (index < 6) {
                var html = "<tr><td style='text-align: center'><input value=" + index + " name='backCode" + index + "'/></td><td style='text-align: center'><input name='clientName" + index + "'/></td><td style='text-align: center'><input name='serviceContent" + index + "'/></td></tr>";
                $("#bgTable").append(html);
            } else {
                alert("不能再加啦~");
            }
        }
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
            $("#supplierName").formValidator({onshow: "请输入供应商名称", onfocus: "供应商名称不能为空"}).inputValidator({
                min: 1,
                onerror: "请输入供应商名称"
            });
            $("#supplierContact").formValidator({onshow: "请输入联系电话", onfocus: "联系电话不能为空"}).inputValidator({
                min: 1,
                onerror: "请输入联系方式"
            });
            $("#contactName").formValidator({onshow: "请输入联系人", onfocus: "联系人不能为空"}).inputValidator({
                min: 1,
                onerror: "请输入联系人"
            });
            $("#serviceYear").formValidator({onshow: "请输入服务时间", onfocus: "服务时间不能为空"}).inputValidator({
                min: 1,
                onerror: "请输入服务时间"
            });
            $("#serviceDetail").formValidator({onshow: "请输入服务明细", onfocus: "服务明细不能为空"}).inputValidator({
                min: 1,
                onerror: "请输入服务明细"
            });
            $("#companyType").formValidator({onshow: "请输入公司性质", onfocus: "公司性质不能为空"}).inputValidator({
                min: 1,
                onerror: "请输入公司性质"
            });
            $("#purchaseTypeMsg").formValidator({onshow: "请选择采购类型", onfocus: "类型不能为空"}).inputValidator({
                min: 1,
                onerror: "请选择采购类型"
            });
            $("#contactName").formValidator({onshow: "请输入联系人", onfocus: "联系人不能为空"}).inputValidator({
                min: 1,
                onerror: "请输入联系人"
            });
            $("#singleOne").formValidator({onshow: "请选择是否单一供方", onfocus: "单一供方不能为空"}).inputValidator({
                min: 1,
                onerror: "请选择是否单一供方"
            });
            $("#pass").formValidator({onshow: "请选择", onfocus: "不能为空"}).inputValidator({min: 1, onerror: "请选择"});

            //验证是否重复
            $("#supplierName").blur(function () {
                $.ajax({
                    type: "post",
                    dataType: "json",
                    url: "/supplier.do?method=validName",
                    data: "supplierName=" + $("#supplierName").val(),
                    success: function (data) {
                        if (!data._Valid) {
                            $("#instanceSupplierNameTip").html("该供应商已存在");
                            $("#supplierName").val("")
                        }
                    }
                });
                $("#instanceSupplierNameTip").html("");
            });

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
<form id="instanceInforForm" name="instanceInforForm" action="/supplier.do?method=save" method="post"
      enctype="multipart/form-data">
    <div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 98%">
        <div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
            <div>
                <div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
                    <span class="ui-jqgrid-title">供方基础信息</span>
                </div>
                <table cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable" style="width: 90%;">
                    <tbody>
                    <tr>
                        <td style="width: 10%"><input type="hidden" name="supplierID" value="${supplier.supplierID}"/>
                        </td>
                        <td style="width: 20%"></td>
                        <td style="width: 10%"></td>
                        <td style="width: 20%"></td>
                    </tr>
                    <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
                        <td class="ui-state-default jqgrid-rownum" style="width: 15%">供方名称：</td>
                        <td style="padding-top: 5px;padding-bottom: 5px;"><input id="supplierName" name="supplierName"
                                                                                 value="${supplier.supplierName}"/>
                            <div id="instanceSupplierNameTip"></div>
                        </td>
                        <td class="ui-state-default jqgrid-rownum" style="width: 18%">地址：</td>
                        <td><input id="supplierAddress" name="supplierAddress"
                                   value="${supplier.supplierAddress}"/>
                            <div id="instanceSupplierAddressTip"></div>
                        </td>
                    </tr>

                    <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
                        <td class="ui-state-default jqgrid-rownum" style="width: 18%">联系人：</td>
                        <td><input id="contactName" name="contactName"
                                   value="${supplier.contactName}"/>
                            <div id="instanceContactNameTip"></div>
                        </td>
                        <td class="ui-state-default jqgrid-rownum" style="width: 18%">联系电话：</td>
                        <td><input id="supplierContact" name="supplierContact"
                                   value="${supplier.supplierContact}"
                                   onkeyup="value=value.replace(/[^0-9]/g,'')"/>
                            <div id="instanceSupplierContactTip"></div>
                        </td>
                    </tr>
                    <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
                        <td class="ui-state-default jqgrid-rownum" style="width: 18%">服务明细：</td>
                        <td><input id="serviceDetail" name="serviceDetail"
                                   value="${supplier.serviceDetail}"/>
                            <div id="instanceServiceDetailTip"></div>
                        </td>
                        <td class="ui-state-default jqgrid-rownum" style="width: 18%">公司性质：</td>
                        <td><input id="companyType" name="companyType" value="${supplier.companyType}"/>
                            <div id="instanceCompanyTypeTip"></div>
                        </td>
                    </tr>
                    <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
                        <td class="ui-state-default jqgrid-rownum" style="width: 18%">服务时间：</td>
                        <td><input id="serviceYear" name="serviceYear"
                                   value="${supplier.serviceYear}"/>
                            <div id="instanceServiceYearTip"></div>
                        </td>
                        <td class="ui-state-default jqgrid-rownum" style="width: 18%">单一供方：</td>
                        <td>
                            <select id="singleOne" name="singleOne">
                                <option value="">-请选择-</option>
                                <option value="否">否</option>
                                <option value="是">是</option>
                            </select>
                            <div id="instanceSingleTip"></div>
                        </td>
                    </tr>
                    <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
                        <td class="ui-state-default jqgrid-rownum" style="width: 18%">采购分类：</td>
                        <td>
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
                        <td class="ui-state-default jqgrid-rownum" style="width: 18%">是否通过质量体系认证：</td>
                        <td>
                            <input id="singleValue" type="hidden" value="${supplier.pass}"/>
                            <select id="pass" name="pass">
                                <option value="">-请选择-</option>
                                <option value="否">否</option>
                                <option value="是">是</option>
                            </select>
                            <div id="instancePassTip"></div>
                        </td>
                    </tr>
                    <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
                        <td class="ui-state-default jqgrid-rownum" style="width: 18%">与海通业务相关性：</td>
                        <td colspan="3"><textarea rows="3" cols="80" name="relevance"></textarea>
                            <div id="instanceRelevanceTip"></div>
                        </td>
                    </tr>
                    <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
                        <td class="ui-state-default jqgrid-rownum" style="width: 18%">归口部门：</td>
                        <td colspan="3">
                            <table style="width: 80%">
                                <tr>
                                    <c:set var="_TypeNum" value="0"/>
                                    <c:forEach var="dept" items="${_ALL_GuiKou}" varStatus="index">
                                    <c:if test="${_TypeNum!=0 && _TypeNum%4==0}">
                                </tr>
                                <tr>
                                    </c:if>
                                    <td width="25%" valign="top">
                                        <input name="organizeIds" type="checkbox"
                                               value="${dept.organizeId}"/>${dept.organizeName}
                                    </td>
                                    <c:set var="_TypeNum" value="${_TypeNum+1}"/>
                                    </c:forEach>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    </tbody>
                </table>
                <div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
                    <span class="ui-jqgrid-title">供方相关资质（附件--证书资质复印件）</span>
                </div>
                <table id="quaTable" cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable"
                       style="width: 90%;">
                    <thead>
                    <tr>
                        <th class="ui-state-default jqgrid-rownum" style="width: 10%">编号</th>
                        <th class="ui-state-default jqgrid-rownum" style="width: 10%">证书资质名称</th>
                        <th class="ui-state-default jqgrid-rownum" style="width: 10%">到期时间</th>
                        <th class="ui-state-default jqgrid-rownum" style="width: 10%">相关附件</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td style="text-align: center"><input name="qualificationCode1" value="1"/></td>
                        <td style="text-align: center"><input name="qualificationName1"/></td>
                        <td style="text-align: center"><input name="endTime1"/></td>
                        <td style="text-align: center"><input id='qua1' name="qua1" type="file"
                                                              onchange="changeFile(this.value,1)"/><input type="hidden"
                                                                                                          name="quav1"
                                                                                                          id='quav1'/>
                        </td>
                    </tr>
                    </tbody>
                </table>
                <div style="padding-left: 85%">
                    <input style="cursor: pointer" type="button" value="添加一行" onclick="addLine(quaTable);"/>
                </div>
                <div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
                    <span class="ui-jqgrid-title">供方行业背景调查</span>
                </div>
                <table id="bgTable" cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable"
                       style="width: 90%;">
                    <thead>
                    <tr>
                        <th class="ui-state-default jqgrid-rownum" style="width: 10%">编号</th>
                        <th class="ui-state-default jqgrid-rownum" style="width: 10%">客户名称</th>
                        <th class="ui-state-default jqgrid-rownum" style="width: 10%">服务内容</th>
                        <th class="ui-state-default jqgrid-rownum" style="width: 10%"></th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td style="text-align: center"><input name="backCode1" value="1"/></td>
                        <td style="text-align: center"><input name="clientName1"/></td>
                        <td style="text-align: center"><input name="serviceContent1"/></td>
                        <td style="text-align: center"></td>
                    </tr>
                    </tbody>
                </table>
                <div style="padding-left: 85%">
                    <input style="cursor: pointer" type="button" value="添加一行" onclick="addLine1(bgTable);"/>
                </div>
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
                  
