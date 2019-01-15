<%@ page import="java.util.UUID" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="/inc/taglibs.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Firefox=1,chorme=1"/>
    <title>开标一览表</title>
    <script src="../js/jquery1.9.1.min.js"></script>
    <script type="text/javascript" src="<c:url value="/"/>components/jqgrid/js/jquery.ui.core.js"></script>
    <script type="text/javascript" src="<c:url value="/"/>components/jqgrid/js/jquery.ui.widget.js"></script>
    <script type="text/javascript" src="<c:url value="/"/>components/jqgrid/js/grid.locale-cn.js"></script>
    <script type="text/javascript" src="<c:url value="/"/>components/jqgrid/js/jquery.ui.datepicker.js"></script>
    <link rel="stylesheet" type="text/css" href="<c:url value="/"/>components/jqgrid/css/jquery.ui.all.css"/>
    <link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css"/>
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
    <link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css">
    <script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript"
            charset="UTF-8"></script>
    <script src="<c:url value="/"/>components/formvalidator/js/formValidatorRegex.js" type="text/javascript"
            charset="UTF-8"></script>
    <!-- ------------- -->
    <link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="../js/dateTimePicker/bootstrap-datetimepicker.zh-CN.js"></script>
    <script src="../js/dateTimePicker/bootstrap-datetimepicker.min.js"></script>
    <link href="../js/dateTimePicker/bootstrap-datetimepicker.min.css" rel="stylesheet">
    <script>
        $(document).ready(function(){
            $(".price").val(0.0);
            $(".price").focus(function(){
                if(this.value==0){
                    this.value='';
                }
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
        $(function () {
            $('#startDate').datetimepicker({
                weekStart: 0, //一周从哪一天开始
                todayBtn:  1, //
                autoclose: 1,
                todayHighlight: 1,
                startView: 2,
                forceParse: 0,
                showMeridian: 1
            });
        });
    </script>
    <script type="text/javascript">
        $(function () {
            $("#ba").change(function () {
                $("#bav").val($(this).val());
            });
            $("#sa1").change(function () {
                $("#sav1").val($(this).val())
            });
            $("#sa2").change(function () {
                $("#sav2").val($(this).val())
            });
            $("#sa3").change(function () {
                $("#sav3").val($(this).val())
            });
            $("#sa4").change(function () {
                $("#sav4").val($(this).val())
            });
            $("#sa5").change(function () {
                $("#sav5").val($(this).val())
            });
        });
        function myCheck() {
            for (var i = 1; i < document.bidInfo.elements.length; i++) {
                if (document.bidInfo.elements[i].value == "" && document.bidInfo.elements[i].type != "file" && (i < 8 || i > 46) && document.bidInfo.elements[i].type != "hidden"
                ) {
                    document.bidInfo.elements[i].focus();
                    alert("表单未填写完整")
                    return false
                }
            }
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
        
        function calTotal(unit,id) {
            var num=$("#purchaseNumber").val();
            if(num==0){
                num=1;
            }
            if(id==1){
                $("#total1").val(unit*num)
            }else if(id==2){
                $("#total2").val(unit*num)
            }else if(id==3){
                $("#total3").val(unit*num)
            }else if(id==4){
                $("#total4").val(unit*num)
            }else if(id==5){
                $("#total5").val(unit*num)
            }
        }
    </script>

    <base target="_self"/>
</head>

<body style="overflow-y: auto;padding: 0 100px" onload="">
<br/>
<form name="bidInfo" id="bidInfo" action="${pageContext.request.contextPath}/bid.do?method=save" method="post"
      onsubmit="return myCheck()" enctype="multipart/form-data">
    <input id="purchaseNumber" type="hidden" value="${purchase.purchaseNumber}"/>
    <div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 100%">
        <div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
            <div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
		    	<span class="ui-jqgrid-title">编辑供方信息 &nbsp;
						【招投标 主办人:${_GLOBAL_PERSON.personName}】
		    	</span>
            </div>
            <div>
                <input type="hidden" name="purchaseId" value="${purchase.purchaseId}"/>
                <table cellspacing="0" cellpadding="0" border="0" style="width: 110%">
                    <tbody id="tblGrid">
                    <tr class="ui-widget-content jqgrow ui-row-ltr">
                        <td class="ui-state-default jqgrid-rownum" style="width: 15%">采购类型：</td>
                        <td>
                            <input name="purchaseTypeMsg" value="${purchase.flowId.flowName}"/>
                        </td>
                    </tr>
                    <tr class="ui-widget-content jqgrow ui-row-ltr">
                        <td class="ui-state-default jqgrid-rownum" style="width: 15%">开标时间：</td>
                        <td>
                            <%
                                Date date = new Date();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                String format = sdf.format(date);
                            %>
                            <input id="startDate" type="text" name="startDate" value="<%=format%>"/>
                        </td>
                    </tr>
                    <tr class="ui-widget-content jqgrow ui-row-ltr">
                        <td class="ui-state-default jqgrid-rownum" style="width: 15%">项目名称：</td>
                        <td>
                            <input name="projectName"/>
                        </td>
                    </tr>
                    <tr class="ui-widget-content jqgrow ui-row-ltr">
                        <td class="ui-state-default jqgrid-rownum" style="width: 15%">唱标人：</td>
                        <td>
                            <input name="readerName" value="${_GLOBAL_PERSON.personName}"/>
                        </td>
                    </tr>
                    <tr class="ui-widget-content jqgrow ui-row-ltr">
                        <td class="ui-state-default jqgrid-rownum" style="width: 15%">开标监督人：</td>
                        <td>
                            <input name="supervisorName" value="${_GLOBAL_PERSON.personName}"/>
                        </td>
                    </tr>
                    <tr class="ui-widget-content jqgrow ui-row-ltr">
                        <td class="ui-state-default jqgrid-rownum" style="width: 15%">招标编号：</td>
                        <td>
                            <input name="zbCode"/>
                        </td>
                    </tr>
                    <tr class="ui-widget-content jqgrow ui-row-ltr">
                        <td class="ui-state-default jqgrid-rownum" style="width: 15%">供方信息：</td>
                        <td>
                            <table>
                                <thead>
                                <tr>
                                    <th class="ui-state-default jqgrid-rownum">投标单位</th>
                                    <th class="ui-state-default jqgrid-rownum">投标单价(元)</th>
                                    <th class="ui-state-default jqgrid-rownum">投标总价(元)</th>
                                    <th class="ui-state-default jqgrid-rownum">投标单位资质</th>
                                    <th class="ui-state-default jqgrid-rownum">交货周期</th>
                                    <th class="ui-state-default jqgrid-rownum">备注</th>
                                    <th class="ui-state-default jqgrid-rownum">附件</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td style="border: none">
                                        <select name="supplierName1" style="background-color: inherit;">
                                            <option value="">--请选择--</option>
                                            <c:forEach var="item" items="${suppliers }">
                                                <option value="${item.supplierName}">${item.supplierName}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                    <td style="border: none"><input class="price" id="unit1" name="unitPrice1" onblur="calTotal(this.value,1);"/></td>
                                    <td style="border: none"><input id="total1" name="totalPrice1"/></td>
                                    <td style="border: none"><input name="qualification1"/></td>
                                    <td style="border: none"><input name="responseTime1"/></td>
                                    <td style="border: none"><input name="memo1"/></td>
                                    <td style="border: none"><input type="file" id="sa1" name="supplierAttach1"/><input
                                            type="hidden" id="sav1" name="sav1"/></td>
                                </tr>
                                <tr>
                                    <td style="border: none">
                                        <select name="supplierName2" style="background-color: inherit;">
                                            <option value="">--请选择--</option>
                                            <c:forEach var="item" items="${suppliers }">
                                                <option value="${item.supplierName}">${item.supplierName}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                    <td style="border: none"><input class="price" id="unit2" name="unitPrice2"  onblur="calTotal(this.value,2);"/></td>
                                    <td style="border: none"><input id="total2" name="totalPrice2"/></td>
                                    <td style="border: none"><input name="qualification2"/></td>
                                    <td style="border: none"><input name="responseTime2"/></td>
                                    <td style="border: none"><input name="memo2"/></td>
                                    <td style="border: none"><input type="file" id="sa2" name="supplierAttach2"/><input
                                            type="hidden" id="sav2" name="sav2"/></td>
                                </tr>
                                <tr>
                                    <td style="border: none">
                                        <select name="supplierName3" style="background-color: inherit;">
                                            <option value="">--请选择--</option>
                                            <c:forEach var="item" items="${suppliers }">
                                                <option value="${item.supplierName}">${item.supplierName}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                    <td style="border: none"><input class="price" id="unit3" name="unitPrice3" onblur="calTotal(this.value,3);"/></td>
                                    <td style="border: none"><input id="total3" name="totalPrice3"/></td>
                                    <td style="border: none"><input name="qualification3"/></td>
                                    <td style="border: none"><input name="responseTime3"/></td>
                                    <td style="border: none"><input name="memo3"/></td>
                                    <td style="border: none"><input type="file" id="sa3" name="supplierAttach3"/><input
                                            type="hidden" id="sav3" name="sav3"/></td>
                                </tr>
                                <tr>
                                    <td style="border: none">
                                        <select name="supplierName4" style="background-color: inherit;">
                                            <option value="">--请选择--</option>
                                            <c:forEach var="item" items="${suppliers }">
                                                <option value="${item.supplierName}">${item.supplierName}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                    <td style="border: none"><input class="price" id="unit4" name="unitPrice4" onblur="calTotal(this.value,4);"/></td>
                                    <td style="border: none"><input id="total4" name="totalPrice4"/></td>
                                    <td style="border: none"><input name="qualification4"/></td>
                                    <td style="border: none"><input name="responseTime4"/></td>
                                    <td style="border: none"><input name="memo4"/></td>
                                    <td style="border: none"><input type="file" id="sa4" name="supplierAttach4"/><input type="hidden" id="sav4" name="sav4"/></td>
                                </tr>
                                <tr>
                                    <td style="border: none">
                                        <select name="supplierName5" style="background-color: inherit;">
                                            <option value="">--请选择--</option>
                                            <c:forEach var="item" items="${suppliers }">
                                                <option value="${item.supplierName}">${item.supplierName}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                    <td style="border: none"><input class="price" id="unit5" name="unitPrice5" onblur="calTotal(this.value,5);"/></td>
                                    <td style="border: none"><input id="total5" name="totalPrice5"/></td>
                                    <td style="border: none"><input name="qualification5"/></td>
                                    <td style="border: none"><input name="responseTime5"/></td>
                                    <td style="border: none"><input name="memo5"/></td>
                                    <td style="border: none"><input type="file" id="sa5" name="supplierAttach5"/><input
                                            type="hidden" id="sav5" name="sav5"/></td>
                                </tr>
                                </tbody>
                            </table>
                        </td>
                    </tr>
                    <tr class="ui-widget-content jqgrow ui-row-ltr">
                        <td class="ui-state-default jqgrid-rownum" style="width: 15%">选择模板:</td>
                        <td>
                            <select name="templateName">
                                <option value="">---请选择---</option>
                                <c:forEach var="item" items="${templates }">
                                    <option value="${item.templateName }">${item.templateName }</option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>
                    <tr class="ui-widget-content jqgrow ui-row-ltr">
                        <td class="ui-state-default jqgrid-rownum" style="width: 15%">附件:</td>
                        <td>
                            <input type="file" id="ba" name="bidAttach"/><input type="hidden" id="bav" name="bav"/>
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
                            <td class="ui-state-default jqgrid-rownum" style="width: 18%">选择评分人员：</td>
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
            </div>
            <div class="ui-state-default ui-jqgrid-pager ui-corner-bottom" dir="ltr"
                 style="width: 100%;margin-top:20px;overflow:visible">
                <input class="buttonclass" style="cursor: pointer;" type="submit" value="提交"/>
                <input class="buttonclass" style="cursor: pointer;" type="button" value="关闭" onclick="window.close();"/>
            </div>
        </div>
    </div>
</form>
</body>
</html>

