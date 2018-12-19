<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.Date" %>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="/inc/taglibs.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <title>详细信息</title>
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
    <link href="/js/timepicker/css/lyz.calendar.css" rel="stylesheet" type="text/css"/>
    <%--<script src="http://www.jq22.com/jquery/1.4.4/jquery.min.js"></script>--%>
    <link href="/js/timepicker/css/lyz.calendar.css" rel="stylesheet" type="text/css"/>
    <script src="/js/timepicker/js/lyz.calendar.min.js" type="text/javascript"></script>

    <style type="text/css">
        .buttonclass {
            font-weight: bold;
        }

        /**input {color:expression(this.type=="button"?"red":"blue") } */
    </style>

    <script type="text/javascript">
        $(document).ready(function () {
            for (i = 0; i < document.getElementsByTagName("INPUT").length; i++) {
                if (document.getElementsByTagName("INPUT")[i].type == "button" || document.getElementsByTagName("INPUT")[i].type == "submit")
                    document.getElementsByTagName("INPUT")[i].className = "buttonclass";
            }
        });
        $(function () {
            $("#endTime").calendar();
        });
        function commonFun(path) {
            window.name = "__self";
            window.open(path, "__self");
        }

        //审核
        function check(supplierID) {
            var path = "/supplier.do?method=editCheck&supplierID=" + supplierID;
            commonFun(path);
        }
        function certify(supplierID) {
            var path = "/supplier.do?method=certify&supplierID=" + supplierID;
            commonFun(path);
        }
        var i = 0, number;
        //全选操作
        function selectUserId(checkbox, organizeId) {
            var isChecked = false;
            if (checkbox.checked) {
                isChecked = true;
            }
            var obj;
            obj = document.certifyForm.personIds;
            if (obj != null) {
                if (obj.length == null) {
                    //只有一个,则只需要判断该用户是不是这个分类
                    <c:forEach var="user" items="${_Users}" varStatus="index">
                    var tempOrganizeId = '${user.person.department.organizeId}';
                    if (organizeId == tempOrganizeId) {
                        obj.checked = isChecked;
                    }
                    </c:forEach>

                    <c:forEach var="user" items="${_OtherUsers}" varStatus="index">
                    var tempOrganizeId = '${user.person.department.organizeId}';

                    if (organizeId == tempOrganizeId) {
                        obj.checked = isChecked;
                    }
                    </c:forEach>
                } else {
                    //多个用户
                    var personNum;
                    personNum = 0;
                    var personNum = obj.length;
                    for (var k = 0; k < personNum; k++) {
                        var userId;
                        personId = obj[k].value;
                        <c:forEach var="user" items="${_Users}" varStatus="index">
                        var tempOrganizeId = '${user.person.department.organizeId}';
                        var tempPersonId = '${user.person.personId}';

                        if (organizeId == tempOrganizeId && tempPersonId == personId) {
                            obj[k].checked = isChecked;
                        }
                        </c:forEach>

                        <c:forEach var="user" items="${_OtherUsers}" varStatus="index">
                        var tempOrganizeId = '${user.person.department.organizeId}';
                        var tempPersonId = '${user.person.personId}';

                        if (organizeId == tempOrganizeId && tempPersonId == personId) {
                            obj[k].checked = isChecked;
                        }
                        </c:forEach>
                    }
                }
            }
        }
        function show_listall() {
            var _trs = getElementsByName('tr', 'tr');
            var _img = getElementsByName('_img');
            for (var i = 0; i < _trs.length; i++) {
                _trs[i].style.display = "";
            }
            _img.src = "../images/xpcollapse3_s.gif"
        }
        function show_hideall() {
            var _trs = getElementsByName('tr', 'tr');
            var _img = getElementsByName('_img');
            for (var i = 0; i < _trs.length; i++) {
                _trs[i].style.display = "none";
            }
            _img.src = "../images/xpcollapse3_s.gif"
        }
    </script>

</head>
<body style="overflow-y: auto;padding: 0 100px" onload="">
<br/>
<form id="certifyForm" name="certifyForm" action="/supplier.do?method=saveCertify" method="post">
    <div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 98%">
        <div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
            <div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix" style="color: #22FBFF">
                <span class="ui-jqgrid-title">发起认证</span>
            </div>
            <div style="width: 90%">
                <hr style="border:0.5px solid #22FBFF;"/>
            </div>
            <%-- 审核实例信息 --%>
            <%@include file="includeInstance.jsp" %>
            <div>
                <table cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable" style="width: 90%;">
                    <tbody id="flowCertify">
                    <tr>
                        <td style="width: 18%"></td>
                        <td style="width: 77%"><input type="hidden" name="supplierID" value="${supplier.supplierID}"/>
                        </td>
                        <td style="width: 5%"></td>
                    </tr>
                    <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
                        <td class="ui-state-default jqgrid-rownum" style="width: 18%">质量评定：</td>
                        <td colspan="2">
                            <textarea name="quality" cols="150"></textarea>
                        </td>
                    </tr>
                    <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
                        <td class="ui-state-default jqgrid-rownum" style="width: 18%">价格评定：</td>
                        <td colspan="2">
                            <textarea name="price" cols="150"></textarea>
                        </td>
                    </tr>
                    <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
                        <td class="ui-state-default jqgrid-rownum" style="width: 18%">服务评定：</td>
                        <td colspan="2">
                            <textarea name="service" cols="150"></textarea>
                        </td>
                    </tr>

                    <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
                        <td class="ui-state-default jqgrid-rownum" style="width: 18%">交期评定：</td>
                        <td colspan="2">
                            <textarea name="delivery" cols="150"></textarea>
                        </td>
                    </tr>
                    <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
                        <td class="ui-state-default jqgrid-rownum" style="width: 18%">管理评定：</td>
                        <td colspan="2">
                            <textarea name="management" cols="150"></textarea>
                        </td>
                    </tr>
                    <tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
                        <td class="ui-state-default jqgrid-rownum" style="width: 18%">有效期至：</td>
                        <%
                            Calendar cal = Calendar.getInstance();
                            cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 1);
                            Date date = cal.getTime();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            String format = sdf.format(date);
                        %>
                        <td colspan="2">
                            <input id="endTime" type="text" name="endTime" value="<%=format%>"/>
                        </td>
                    </tr>

                    <script language="javaScript">
                        //定义一个数组，记录各个数据点击的次数
                        var clickTimes = new Array();
                    </script>
                    <tr>
                        <td>&nbsp;</td>
                        <td colspan="2">
                            全部展开<span onclick="show_listall()"><img name="_img"
                                                                    src="<c:url value="${'/images'}"/>/xpexpand3_s.gif"/></span>
                            全部隐藏<span onclick="show_hideall()"><img name="_img"
                                                                    src="<c:url value="${'/images'}"/>/xpcollapse3_s.gif"/></span>
                        </td>
                    </tr>
                    <c:if test="${not empty _Users}">
                        <tr id="users" valign="top">
                            <td class="ui-state-default jqgrid-rownum" style="width: 18%">选择评审人员：</td>
                            <td colspan="2">
                                <table width="100%">
                                    <c:set var="_Num" value="0"/>
                                    <c:forEach var="department" items="${_Departments}">
                                        <tr height="33">
                                            <td valign="bottom" width="100%"
                                                style="border-bottom:1px dotted #888888;font-size:10pt" colspan="2">

                                                <script language="javaScript">
                                                    clickTimes[${_Num}] = 0;
                                                </script>
                                                    ${department.organizeName}&nbsp;
                                                <span onclick="show_list('${_Num}')">
								<img name="img" src="<c:url value="${'/images'}"/>/xpexpand3_s.gif"
                                     style="margin-top:0px;margin-bottom:0px;"/>
							</span>
                                            </td>
                                        </tr>

                                        <tr name="tr" style="display:none;padding-top:10px;">
                                            <td width="9%" align="right" valign="top">
                                                <input type="checkbox"
                                                       onclick="selectUserId(this,'${department.organizeId}')"/>全选
                                            </td>
                                            <td style="padding-left:10px;" width="92%">
                                                <table>
                                                    <tr>
                                                        <c:set var="_TypeNum" value="0"/>
                                                        <c:forEach var="user" items="${_Users}" varStatus="index">
                                                        <c:if test="${user.person.department.organizeId==department.organizeId}">
                                                        <c:if test="${_TypeNum!=0 && _TypeNum%6==0}">
                                                    </tr>
                                                    <tr>
                                                        </c:if>
                                                        <td width="16%" valign="top">
                                                            <input type="checkbox" name="personIds"
                                                                   value="${user.personId}"/> ${user.person.personName}
                                                        </td>
                                                        <c:set var="_TypeNum" value="${_TypeNum+1}"/>
                                                        </c:if>
                                                        </c:forEach>
                                                        <c:forEach begin="${_TypeNum%6}" end="5">
                                                            <td width="16%">&nbsp;</td>
                                                        </c:forEach>
                                                    </tr>

                                                    <tr>
                                                        <c:set var="_TypeNum" value="0"/>
                                                        <c:forEach var="user" items="${_OtherUsers}" varStatus="index">
                                                        <c:if test="${user.person.department.organizeId==department.organizeId}">
                                                        <c:if test="${_TypeNum!=0 && _TypeNum%6==0}">
                                                    </tr>
                                                    <tr>
                                                        </c:if>
                                                        <td width="16%" valign="top">
                                                            <input type="checkbox" name="personIds"
                                                                   value="${user.personId}"/> ${user.person.personName}
                                                        </td>
                                                        <c:set var="_TypeNum" value="${_TypeNum+1}"/>
                                                        </c:if>
                                                        </c:forEach>
                                                        <c:forEach begin="${_TypeNum%6}" end="5">
                                                            <td width="16%">&nbsp;</td>
                                                        </c:forEach>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        <c:set var="_Num" value="${_Num+1}"/>
                                    </c:forEach>
                                </table>
                            </td>
                        </tr>
                    </c:if>
                    </tbody>
                </table>
                <div class="ui-state-default ui-jqgrid-pager ui-corner-bottom" dir="ltr"
                     style="width: 100%;margin-top:20px;overflow:visible">
                    <input id="submit" style="cursor: pointer;" type="submit" value="提交"/>
                    <input style="cursor: pointer;" type="button" value="关闭" onclick="window.close();"/>
                </div>
            </div>
        </div>
    </div>
</form>
<script type="text/javascript">
    $('#certifyForm').submit(function(){
        var a=$(':checked').length;
        if(a==0){
            alert("请选择评审人员！")
        }
        return a>0;
    });
</script>
</body>
</html>

