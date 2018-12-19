<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="/inc/taglibs.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<title>编辑模板信息</title>
<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
<script src="<c:url value="/"/>js/inc_javascript.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/css/form.css"/>"/>
<link rel="stylesheet" type="text/css" media="screen" href="/css/myTable.css"/>
<!-- formValidator -->
<link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css"></link>
<link type="text/css" rel="stylesheet" href="/css/noTdBottomBorder.css"></link>
<script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript"
        charset="UTF-8"></script>
<script src="<c:url value="/"/>components/formvalidator/js/formValidatorRegex.js" type="text/javascript"
        charset="UTF-8"></script>
<!-- ------------- -->

<script>
    $(document).ready(function () {
        //勾选选中的用户
        var templateInfoIds = document.getElementsByName('templateInfoIds');
        <c:forEach var="info" items="${_TemplateInfos}" varStatus="abc">
        var tmpId = '${info.zhaotouTemplateInfoId}';
        if (templateInfoIds != null && templateInfoIds.length > 0) {
            for (var i = 0; i < templateInfoIds.length; i++) {
                var infoId = templateInfoIds[i];
                if (tmpId == infoId.value) {
                    infoId.checked = true;
                }
            }
        }
        </c:forEach>
    });
</script>
<style>
    .name {
        border: solid 1px #18818b;
        color: #bcbcbc;
        border-radius: 3px;
        background-color: rgba(10, 95, 162, 0);
        padding: 5px 5px 5px 5px;
    }
</style>
<base target="_self"/>
<body style="padding:0 100px" onload="hdUsers();">
<br/>
<form:form commandName="templateVo" id="roleInforForm" name="roleInforForm">
<table width="98%" cellpadding="6" cellspacing="1" align="center" border="0" bordercolor="#c5dbec">
    <tr>
        <td width="20%">模板名称：</td>
        <td><form:input path="templateName" id="templateName" size="20" readonly="true"/></td>
    </tr>
    <tr>
        <td width="20%">技术权重：</td>
        <td><form:input path="jsWeight" id="jsWeight" size="20" readonly="true"/></td>
    </tr>
    <tr>
        <td width="20%">商务权重：</td>
        <td><form:input path="swWeight" id="swWeight" size="20" readonly="true"/></td>
    </tr>

    <script language="javaScript">
        //定义一个数组，记录各个数据点击的次数
        var clickTimes = new Array();
    </script>

    <tr id="users" valign="top">
        <td>模板基础信息：</td>
        <td colspan="2">
            <table width="100%">
                <c:set var="_Num" value="0"/>
                <tr height="33">
                    <td valign="bottom" width="100%" style="border-bottom:1px dotted #888888;font-size:10pt">

                        <script language="javaScript">
                            clickTimes[${_Num}] = 0;
                        </script>
                        技术标&nbsp;
                        <span onclick="show_list('${_Num}')">
								<img name="img" src="<c:url value="${'/images'}"/>/xpexpand3_s.gif"
                                     style="margin-top:0px;margin-bottom:0px;"/>
							</span>
                    </td>
                </tr>

                <tr name="tr" style="display:none;padding-top:10px;">
                    <td style="padding-left:10px;" width="92%">
                        <table>
                            <tr>
                                <c:set var="_TypeNum" value="0"/>
                                <c:forEach var="tech" items="${_Tech}" varStatus="index">
                                <c:if test="${_TypeNum!=0 && _TypeNum%4==0}">
                            </tr>
                            <tr>
                                </c:if>
                                <td width="16%" valign="top">
                                    <input type="checkbox" name="templateInfoIds"
                                           value="${tech.zhaotouTemplateInfoId}" disabled/> ${tech.target}
                                </td>
                                <c:set var="_TypeNum" value="${_TypeNum+1}"/>
                                </c:forEach>
                                <c:forEach begin="${_TypeNum%4}" end="3">
                                    <td width="16%">&nbsp;</td>
                                </c:forEach>
                            </tr>
                        </table>
                    </td>
                </tr>
                <c:set var="_Num" value="${_Num+1}"/>
                <tr height="33">
                    <td valign="bottom" width="100%" style="border-bottom:1px dotted #888888;font-size:10pt">

                        <script language="javaScript">
                            clickTimes[${_Num}] = 0;
                        </script>
                        商务标&nbsp;
                        <span onclick="show_list('${_Num}')">
								<img name="img" src="<c:url value="${'/images'}"/>/xpexpand3_s.gif"
                                     style="margin-top:0px;margin-bottom:0px;"/>
                        </span>
                    </td>
                </tr>

                <tr name="tr" style="display:none;padding-top:10px;">
                    <td style="padding-left:10px;" width="92%">
                        <table>
                            <tr>
                                <c:set var="_TypeNum" value="0"/>
                                <c:forEach var="business" items="${_Business}" varStatus="index">
                                <c:if test="${_TypeNum!=0 && _TypeNum%3==0}">
                            </tr>
                            <tr>
                                </c:if>
                                <td width="16%" valign="top">
                                    <input type="checkbox" name="templateInfoIds"
                                           value="${business.zhaotouTemplateInfoId}" disabled/> ${business.target}
                                </td>
                                <c:set var="_TypeNum" value="${_TypeNum+1}"/>
                                </c:forEach>
                                <c:forEach begin="${_TypeNum%4}" end="3">
                                    <td width="16%">&nbsp;</td>
                                </c:forEach>
                            </tr>
                        </table>
                    </td>
                </tr>
                <c:set var="_Num" value="${_Num+1}"/>

                <tr>
                    <td colspan="3">
                        <input type="button" value="关闭" style="cursor: pointer;" onclick="window.close();"/>
                    </td>
                </tr>
            </table>
            </form:form>
</body>

