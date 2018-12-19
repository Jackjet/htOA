<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
	<title>审批流转</title>
	<script src="<c:url value="/"/>datePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
	<script src="<c:url value="/"/>components/jqgrid/js/jquery-ui-1.8.2.custom.min.js" type="text/javascript"></script><!--jquery ui-->
	<script src="<c:url value="/"/>js/inc_javascript.js"></script>
	<script src="<c:url value="/"/>js/addattachment.js"></script>
	<link rel="stylesheet" type="text/css" href="<c:url value="/"/>components/jqgrid/css/jquery.ui.all.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="/css/border.css" />

	<!-- formValidator -->
	<link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css"></link>
	<script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript" charset="UTF-8"></script>
	<script src="<c:url value="/"/>components/formvalidator/js/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
	<!-- ------------- -->

	<style type="text/css">

		.buttonclass {
			font-weight:bold;
		}
		/**input {color:expression(this.type=="button"?"red":"blue") } */



	</style>


	<script language="javaScript">
        $().ready(function(){
            //配置审核层选择框
            $("#layerModalDlg").dialog({
                autoOpen: false,
                modal: true,    //设置对话框为模态(modal)对话框
                resizable: true,
                width: 280,
                cache: false,
                buttons: {  	//为对话框添加按钮
                    "取消": function() {$("#layerModalDlg").dialog("close")},
                    "保存": saveStop
                }
            });

            //配置节点选择框
            $("#nodeModalDlg").dialog({
                autoOpen: false,
                modal: true,    //设置对话框为模态(modal)对话框
                resizable: true,
                width: 280,
                cache: false,
                buttons: {  	//为对话框添加按钮
                    "取消": function() {$("#nodeModalDlg").dialog("close")},
                    "保存": saveNext
                }
            });

            //配置中止部门审核框
            $("#stopDepCheckDlg").dialog({
                autoOpen: false,
                modal: true,    //设置对话框为模态(modal)对话框
                resizable: true,
                width: 500,
                cache: false,
                buttons: {  	//为对话框添加按钮
                    "取消": function() {$("#stopDepCheckDlg").dialog("close")},
                    "保存": stopDepCheck
                }
            });

            //button字体变粗
            for(i=0;i<document.getElementsByTagName("INPUT").length;i++){
                if(document.getElementsByTagName("INPUT")[i].type=="button" || document.getElementsByTagName("INPUT")[i].type=="submit")
                    document.getElementsByTagName("INPUT")[i].className="buttonclass" ;
            }

        });


        function commonFun(path) {
            window.name = "__self";
            window.open(path, "__self");
        }

        //重新加载浏览页面
        function reloadPage(instanceId){
            var path = "<c:url value='/workflow/instanceInfor.do'/>?method=view&instanceId=" + instanceId;
            commonFun(path);
        }

        //修改
        function edit(instanceId){
            var path = "<c:url value='/workflow/instanceInfor.do'/>?method=edit&instanceId=" + instanceId;
            commonFun(path);
        }

        //重新流转
        function newedit(instanceId){
            var yes = window.confirm("确定要中止现在的合同流程，重新开始流转吗？");
            if (yes) {
                //var path = "<c:url value='/workflow/instanceInfor.do'/>?method=newEdit&oldInstanceId=" + instanceId;
                var path = "<c:url value='/workflow/instanceInfor.do'/>?method=readyToNewEdit&instanceId=" + instanceId;
                commonFun(path);

            }
        }

        //部门审核
        function depCheck(instanceId){
            var path = "<c:url value='/workflow/instanceInfor.do'/>?method=editDepCheck&instanceId=" + instanceId;
            commonFun(path);
        }

        //开始流转
        function startInstance(instanceId){
            var yes = window.confirm("确定要开始流转吗？");
            if (yes) {
                var path = "<c:url value='/workflow/instanceInfor.do'/>?method=startInstance&instanceId=" + instanceId;
                commonFun(path);
                alert("审核实例已进入流转！");
            }
        }

        //审核
        function check(instanceId){
            var path = "<c:url value='/workflow/checkInfor.do'/>?method=edit&instanceId=" + instanceId;
            commonFun(path);
        }

        //发短息
        function sms(instanceId){
            var path = "<c:url value='/workflow/checkInfor.do'/>?method=sms&instanceId=" + instanceId;
            commonFun(path);
        }

        //修改记录
        function getModify(instanceId){
            var path = "<c:url value='/workflow/checkInfor.do'/>?method=getModify&instanceId=" + instanceId;
            commonFun(path);
        }

        //添加决议附件
        function addResAttach(instanceId){
            var path = "<c:url value='/workflow/instanceInfor.do'/>?method=addResAttach&instanceId=" + instanceId;
            commonFun(path);
        }

        //设定审核层
        function setLayer(instanceId){

            var path = "<c:url value='/workflow/layerInfor.do'/>?method=edit&instanceId=" + instanceId;
            commonFun(path);
        }

        //暂停
        function pause(instanceId){
            var yes = window.confirm("确定要中止吗？");
            if (yes) {
                //var path = "<c:url value='/workflow/instanceInfor.do'/>?method=alterStatus&instanceId=" + instanceId;
                var path = "<c:url value='/workflow/instanceInfor.do'/>?method=readyToAlterStatus&instanceId=" + instanceId;
                commonFun(path);
                //alert("审核实例已被中止！");
            }
        }

        //取消暂停
        function cancelPause(instanceId){
            var yes = window.confirm("确定要取消中止吗？");
            if (yes) {
                var path = "<c:url value='/workflow/instanceInfor.do'/>?method=alterStatus&suspended=false&instanceId=" + instanceId;
                commonFun(path);
                alert("中止已被取消！");
            }
        }

        //结束审核实例
        function endInstance(instanceId){
            var path = "<c:url value='/workflow/instanceInfor.do'/>?method=editEnd&instanceId=" + instanceId;
            commonFun(path);
        }

        //恢复流转
        function restore(instanceId){
            var yes = window.confirm("确定要恢复流转吗？");
            if (yes) {
                var path = "<c:url value='/workflow/instanceInfor.do'/>?method=restore&instanceId=" + instanceId;
                commonFun(path);
                alert("审核实例已经恢复流转！");
            }
        }

        //盖章
        function applyStamp(instanceId){
            var yes = window.confirm("确定要执行该操作吗？执行该操作后流转将结束");
            if (yes) {
                var path = "<c:url value='/workflow/instanceInfor.do'/>?method=applyStamp&instanceId=" + instanceId;
                commonFun(path);
                alert("操作已执行！");
            }
        }

        //盖章
        function stamp(instanceId){
            var yes = window.confirm("确定要执行该操作吗？");
            if (yes) {
                var path = "<c:url value='/workflow/instanceInfor.do'/>?method=stamp&instanceId=" + instanceId;
                commonFun(path);
                alert("操作已执行！");
            }
        }



        //归档
        function filedInstance(instanceId){
            var path = "<c:url value='/workflow/instanceInfor.do'/>?method=editFiled&instanceId=" + instanceId;
            commonFun(path);
        }

        /********中止审核*********/
        //中止审核(判断中止的审核层有一个还是多个,只有一个时,程序直接中止,多个时打开审核层选择框,由用户选择)
        function endCheck(instanceId){
            var yes = window.confirm("确定要中止审核吗？");
            if (yes) {
                $.ajax({
                    url: "/workflow/instanceInfor.do?method=endCheck&instanceId=" + instanceId,
                    type: "post",
                    dataType: "json",
                    async: false,	//设置为同步
                    beforeSend: function (xhr) {
                    },
                    complete : function (req, err) {
                        var returnValues = eval("("+req.responseText+")");
                        if (!returnValues["needChoose"]){
                            alert(returnValues["message"]);
                            reloadPage(instanceId);
                        }else {
                            /** 由用户对需要中止的审核层进行选择 */
                                //打开审核层选择框
                            var consoleDlg = $("#layerModalDlg");
                            var dialogButtonPanel = consoleDlg.siblings(".ui-dialog-buttonpane");
                            consoleDlg.dialog("option", "title", "选择需要中止的审核层");

                            //加载需要的信息
                            loadLayer(returnValues["processLayers"]);

                            consoleDlg.dialog("open");
                        }
                    }
                });
            }
        }

        //把需要的信息加载到选择框
        function loadLayer(processLayers){
            var content = "";
            for(var i=0;i<processLayers.length;i++){
                content += "<tr><td><input type='checkbox' name='endLayers' value='"+processLayers[i]['layerId']+"'/>"+processLayers[i]['layerName']+"</td></tr>";
            }
            $('#layerTable').html(content);
        }

        //中止审核(中止用户所选审核层)
        function saveStop() {
            var endLayers = document.getElementsByName("endLayers");
            var layerNum = 0;
            var layerIds = new Array();
            for (var i=0;i<endLayers.length;i++) {
                if (endLayers[i].checked) {
                    layerNum++;
                    layerIds[i] = endLayers[i].value;
                }
            }
            if (layerNum > 0) {
                $.ajax({
                    url: "/workflow/instanceInfor.do?method=saveStop&layerIds="+layerIds,
                    type: "post",
                    dataType: "json",
                    async: false,	//设置为同步
                    beforeSend: function (xhr) {
                    },
                    complete : function (req, err) {
                        var returnValues = eval("("+req.responseText+")");
                        alert(returnValues["message"]);
                        $("#layerModalDlg").dialog("close");
                        reloadPage('${_Instance.instanceId}');
                    }
                });
            }else {
                alert("请选择需要中止的审核层！");
            }
        }
        /*****************/

        /********跳转到下一节点*********/
        //跳转到下一节点(判断预设的下一步节点是否有多个,假如有多个则需要用户选择)
        function nextNode(instanceId){
            var yes = window.confirm("确定要跳转到下一节点吗？");
            if (yes) {
                $.ajax({
                    url: "/workflow/instanceInfor.do?method=nextNode&instanceId=" + instanceId,
                    type: "post",
                    dataType: "json",
                    //async: false,	//设置为同步
                    beforeSend: function (xhr) {
                    },
                    complete : function (req, err) {
                        var returnValues = eval("("+req.responseText+")");
                        if (!returnValues["needChoose"]){
                            alert(returnValues["message"]);
                            reloadPage(instanceId);
                        }else {
                            /** 由用户选择跳转到的下一节点 */
                                //打开节点选择框
                            var consoleDlg = $("#nodeModalDlg");
                            var dialogButtonPanel = consoleDlg.siblings(".ui-dialog-buttonpane");
                            consoleDlg.dialog("option", "title", "选择要跳转到的节点");

                            //加载需要的信息
                            loadNode(returnValues["nextNodes"]);

                            consoleDlg.dialog("open");
                        }
                    }
                });
            }
        }

        //把需要的信息加载到选择框
        function loadNode(nextNodes){
            var content = "";
            for(var i=0;i<nextNodes.length;i++){
                content += "<tr><td><input type='radio' name='nextNodes' value='"+nextNodes[i]['fromLayerId']+"'/>"+nextNodes[i]['nodeName']+"</td></tr>";
            }
            content += "<tr><td><input type='radio' name='nextNodes'/>手动设定</td></tr>";
            $('#nodeTable').html(content);
        }

        //跳转到下一节点(跳转到用户所选节点)
        function saveNext() {
            var nextNodes = document.getElementsByName("nextNodes");
            var nodeNum = 0;
            var fromLayerId;
            var checkedIndex = 0;
            for (var i=0;i<nextNodes.length;i++) {
                if (nextNodes[i].checked) {
                    fromLayerId = nextNodes[i].value;
                    nodeNum++;
                    checkedIndex = i;
                    break;
                }
            }
            if (nodeNum > 0) {
                if (checkedIndex == nextNodes.length-1) {
                    //跳转到手动添加审核层页面
                    setLayer('${_Instance.instanceId}');
                }else {
                    $.ajax({
                        url: "/workflow/instanceInfor.do?method=saveNext&fromLayerId="+fromLayerId,
                        type: "post",
                        dataType: "json",
                        async: false,	//设置为同步
                        beforeSend: function (xhr) {
                        },
                        complete : function (req, err) {
                            var returnValues = eval("("+req.responseText+")");
                            alert(returnValues["message"]);
                            $("#nodeModalDlg").dialog("close");
                            reloadPage('${_Instance.instanceId}');
                        }
                    });
                }
            }else {
                alert("请选择要跳转到的节点！");
            }
        }
        /*****************/

        //中止部门审核
        function openDepStop(){
            //打开审核层选择框
            var consoleDlg = $("#stopDepCheckDlg");
            var dialogButtonPanel = consoleDlg.siblings(".ui-dialog-buttonpane");
            consoleDlg.dialog("option", "title", "中止部门审核");
            consoleDlg.dialog("open");
        }

        function stopDepCheck() {
            var submiterWord = document.getElementById("submiterWord");
            if (submiterWord.value != null && submiterWord.value != "") {
                $.ajax({
                    url: "/workflow/instanceInfor.do?method=saveDepStop&instanceId=${_Instance.instanceId}&submiterWord="+encodeURI(submiterWord.value),
                    type: "post",
                    dataType: "json",
                    async: false,	//设置为同步
                    beforeSend: function (xhr) {
                    },
                    complete : function (req, err) {
                        var returnValues = eval("("+req.responseText+")");
                        alert(returnValues["message"]);
                        $("#stopDepCheckDlg").dialog("close");
                        reloadPage('${_Instance.instanceId}');
                    }
                });
            }else {
                alert("请填写中止意见！");
            }
        }
        /*****************/

        //如果审核人审核完后可以进行手动流转,则调用nextNode方法
        if (${param.needValidate == 'true'}) {
            nextNode(${param.instanceId});
        }

        //打印操作
        function print() {
            var path = "<c:url value='/workflow/submit.do'/>?method=printSubmit&instanceId=" + ${_Instance.instanceId};
            window.open(path, "_blank");
        }

        //授权
        function doAuthorize(rowId){
//			var returnArray = window.showModalDialog("/workflow/instanceInfor.do?method=editInforRight&rowId="+rowId,'',"dialogWidth:800px;dialogHeight:600px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
            window.open("/workflow/instanceInfor.do?method=editInforRight&rowId="+rowId, "_blank");
//			if(returnArray != null && returnArray[0] == "refresh") {
//				self.location.reload();
//			}
        }
	</script>
</head>
<body style="overflow-y: auto;padding: 0 100px" onload="">
<br/>
<form id="instanceInforForm" name="instanceInforForm" method="post">
	<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 98%">
		<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
			<div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix" style="color: #22FBFF">
				<span class="ui-jqgrid-title">查看审批信息 &nbsp;【${_Flow.flowName} 主办人:${_Instance.charger.person.personName}】</span>
			</div>
			<div style="width: 90%"><hr style="border:0.5px solid #22FBFF;" /></div>
			<%-- 审核实例信息 --%>
			<%@include file="includeInstance.jsp" %>

			<div style="width: 100%" class="ui-state-default ui-jqgrid-pager ui-corner-bottom" dir="ltr">
				<c:if test="${_Instance.suspended}">
					<c:if test="${_Instance.flowDefinition.flowId == 86 && _SYSTEM_USER.userType == 1}">
						<td><input style="cursor: pointer;" type="button" value="修改" onclick="edit('${_Instance.instanceId}');"/></td>
					</c:if>
				</c:if>

				<c:if test="${_Instance.suspended}">
					<c:if test="${_Instance.flowDefinition.flowId >89 && _Instance.flowDefinition.flowId <101 && _SYSTEM_USER.userType == 1}">
						<td><input style="cursor: pointer;" type="button" value="修改" onclick="edit('${_Instance.instanceId}');"/></td>
					</c:if>
				</c:if>

				<c:if test="${_Instance.deleteFlag != 1}"><!--  && !_Instance.suspended -->
					<%--<c:if test="${_Flow.flowId!=86}">
						<c:if test="${(empty _Instance.startTime && _Instance.applier.personId == _SYSTEM_USER.personId) || _IsCharger}">
							<td><input style="cursor: pointer;" type="button" value="修改" onclick="edit('${_Instance.instanceId}');"/></td>
						</c:if>
					</c:if> --%>
				<!-- 体系文件， 文件流转审核人未结束时可以修改 -->
				<c:if test="${!_Instance.suspended ||  _SYSTEM_USER.userType != 1}">
					<c:if test="${_Instance.flowDefinition.flowId >89 && _Instance.flowDefinition.flowId <101 && (!_CanModyFF || _SYSTEM_USER.userType == 1)}">
						<c:if test="${(empty _Instance.startTime && _Instance.applier.personId == _SYSTEM_USER.personId) || _IsCharger || _SYSTEM_USER.userType == 1}">
							<td><input style="cursor: pointer;" type="button" value="修改" onclick="edit('${_Instance.instanceId}');"/></td>
						</c:if>
					</c:if>
				</c:if>

					<%--<c:if test="${_Instance.flowDefinition.flowId >89 && _Instance.flowDefinition.flowId <101 && _CanModyFF && _Instance.stamped < 2}">
                        <c:if test="${(empty _Instance.startTime && _Instance.applier.personId == _SYSTEM_USER.personId) || _IsCharger}">
                            <td><input style="cursor: pointer;" type="button" value="重新流转" onclick="newedit('${_Instance.instanceId}');"/></td>
                        </c:if>
                    </c:if>--%>
				<!-- 合同， 法务意见未结束时可以修改 -->
					<c:if test="${!_Instance.suspended ||  _SYSTEM_USER.userType != 1}">
						<c:if test="${_Instance.flowDefinition.flowId == 86 && (!_CanMody || _SYSTEM_USER.userType == 1)}">
							<c:if test="${(empty _Instance.startTime && _Instance.applier.personId == _SYSTEM_USER.personId) || _IsCharger || _SYSTEM_USER.userType == 1}">
								<td><input style="cursor: pointer;" type="button" value="修改" onclick="edit('${_Instance.instanceId}');"/></td>
							</c:if>
						</c:if>
					</c:if>

				<!-- 添加“盖章后不允许重新流转限制 -->
				<c:if test="${_Instance.flowDefinition.flowId == 86 && _CanMody && _Instance.stamped < 2}">
					<c:if test="${(empty _Instance.startTime && _Instance.applier.personId == _SYSTEM_USER.personId) || _IsCharger}">
						<td><input style="cursor: pointer;" type="button" value="重新流转" onclick="newedit('${_Instance.instanceId}');"/></td>
					</c:if>
				</c:if>

				<c:if test="${_Instance.flowDefinition.flowId >89 && _Instance.flowDefinition.flowId <101 && _CanModyFF && _Instance.stamped < 2}">
					<c:if test="${(empty _Instance.startTime && _Instance.applier.personId == _SYSTEM_USER.personId) || _IsCharger}">
						<td><input style="cursor: pointer;" type="button" value="重新流转" onclick="newedit('${_Instance.instanceId}');"/></td>
					</c:if>
				</c:if>
				<!-- 制度评审，综合审查意见未结束时可以修改 -->
				<c:if test="${_Instance.flowDefinition.flowId == 88 && (!_CanEditZdps || _SYSTEM_USER.userType == 1)}">
					<c:if test="${(empty _Instance.startTime && _Instance.applier.personId == _SYSTEM_USER.personId) || _IsCharger || _SYSTEM_USER.userType == 1}">
						<td><input style="cursor: pointer;" type="button" value="修改" onclick="edit('${_Instance.instanceId}');"/></td>
					</c:if>
				</c:if>

				<!-- 内部报告，部门审核前可以修改 -->
				<c:if test="${_Instance.flowDefinition.flowId == 87 && ((!_Instance.managerChecked && !_Instance.viceManagerChecked) || _SYSTEM_USER.userType == 1) }">
					<c:if test="${(empty _Instance.startTime && _Instance.applier.personId == _SYSTEM_USER.personId) || _SYSTEM_USER.userType == 1}">
						<td><input style="cursor: pointer;" type="button" value="修改" onclick="edit('${_Instance.instanceId}');"/></td>
					</c:if>
				</c:if>

				<c:if test="${(_Instance.flowDefinition.flowId == 84||_Instance.flowDefinition.flowId == 85) && (!_CanMody || _SYSTEM_USER.userType == 1)}">
					<c:if test="${(empty _Instance.startTime && _Instance.applier.personId == _SYSTEM_USER.personId) || _SYSTEM_USER.userType == 1}">
						<td><input style="cursor: pointer;" type="button" value="修改" onclick="edit('${_Instance.instanceId}');"/></td>
					</c:if>
				</c:if>



				<c:if test="${empty _Instance.startTime}">
					<c:if test="${_CanDepCheck}">
						<td><input style="cursor: pointer;" type="button" value="审核文件" onclick="depCheck('${_Instance.instanceId}');"/></td>
					</c:if>
					<c:if test="${_CanStopDepCheck}">
						<td><input style="cursor: pointer;" type="button" value="中止部门审核" onclick="openDepStop('${_Instance.instanceId}');"/></td>
					</c:if>
					<c:if test="${_CanStart}">
						<td><input style="cursor: pointer;" type="button" value="提交到主办人" onclick="startInstance('${_Instance.instanceId}');"/></td>
					</c:if>
				</c:if>

				<c:if test="${_CanCheck}">
					<td><input style="cursor: pointer;" type="button" value="审核文件" onclick="check('${_Instance.instanceId}');"/></td>
				</c:if>

				&nbsp;&nbsp;&nbsp;&nbsp;
				<c:if test="${_CanAddResAttach}">
					<td><input style="cursor: pointer;" type="button" value="添加决议附件" onclick="addResAttach('${_Instance.instanceId}');"/></td>
				</c:if>
				<c:if test="${_Instance.stamped < 1}">

					<c:if test="${_CanAddLayer}">
						<c:if test="${!_Instance.suspended}">
							<td><input style="cursor: pointer;" type="button" value="设定审核层" onclick="setLayer('${_Instance.instanceId}');"/></td>
						</c:if>
					</c:if>
				</c:if>
				<c:if test="${(_IsCharger || _IsFileRole || _IsViewalls) && !empty _Instance.startTime}">
					<c:choose>
						<c:when test="${empty _Instance.endTime}">

							<c:if test="${_Instance.stamped < 1}">
								<c:if test="${!_Instance.suspended}">

									<c:if test="${_Instance.flowDefinition.flowId != 86}">
										<c:if test="${_CanEndCheck}">
											<td><input style="cursor: pointer;" type="button" value="终止该层审核" onclick="endCheck('${_Instance.instanceId}');"/></td>
										</c:if>
									</c:if>

									<c:if test="${_Instance.flowDefinition.flowId == 86}">
										<c:if test="${_CanEndCheck && _CanNext_FW}">
											<td><input style="cursor: pointer;" type="button" value="终止该层审核" onclick="endCheck('${_Instance.instanceId}');"/></td>
										</c:if>
									</c:if>

									<c:if test="${_CanNext && _Instance.flowDefinition.flowId != 86 && (_Instance.flowDefinition.flowId < 90 || _Instance.flowDefinition.flowId > 100)}">
										<td><input style="cursor: pointer;" type="button" value="下一步" onclick="nextNode('${_Instance.instanceId}');"/></td>
									</c:if>

									<c:if test="${_CanNext && _CanNext_FW &&_Instance.flowDefinition.flowId == 86 }">
										<td><input style="cursor: pointer;" type="button" value="下一步" onclick="nextNode('${_Instance.instanceId}');"/></td>
									</c:if>
									<c:if test="${_Instance.flowDefinition.flowId > 89 && _Instance.flowDefinition.flowId < 101 && _CanNext_File}">
										<td><input style="cursor: pointer;" type="button" value="下一步" onclick="nextNode('${_Instance.instanceId}');"/></td>
									</c:if>
									<%--<c:if test="${_CanNext}">
										<td><input style="cursor: pointer;" type="button" value="下一步" onclick="nextNode('${_Instance.instanceId}');"/></td>
									</c:if>--%>
								</c:if>
							</c:if>

							<c:if test="${_CanApplyStamp && !_Instance.suspended && (_Instance.flowDefinition.flowId < 90 || _Instance.flowDefinition.flowId > 100)}">
								&nbsp;&nbsp;
								<td><input style="cursor: pointer;" type="button" value="申请盖章" onclick="applyStamp('${_Instance.instanceId}');"/></td>
								&nbsp;&nbsp;
							</c:if>

							<c:if test="${_Instance.flowDefinition.flowId > 89 && _Instance.flowDefinition.flowId < 101 && _CanApplyStamp && !_Instance.suspended}">
								&nbsp;&nbsp;
								<td><input style="cursor: pointer;" type="button" value="申请提交文档" onclick="applyStamp('${_Instance.instanceId}');"/></td>
								&nbsp;&nbsp;
							</c:if>

							<c:if test="${_IsFileRole}">
								<c:if test="${_CanStamp && (_Instance.flowDefinition.flowId < 90 || _Instance.flowDefinition.flowId > 100)}">
									&nbsp;&nbsp;
									<td><input style="cursor: pointer;" type="button" value="盖章" onclick="stamp('${_Instance.instanceId}');"/></td>
									&nbsp;&nbsp;
								</c:if>
								<c:if test="${_Instance.flowDefinition.flowId > 89 && _Instance.flowDefinition.flowId < 101 && _CanStamp}">
									&nbsp;&nbsp;
									<td><input style="cursor: pointer;" type="button" value="提交文档" onclick="stamp('${_Instance.instanceId}');"/></td>
									&nbsp;&nbsp;
								</c:if>
							</c:if>

							<c:if test="${_Instance.stamped == 2 && _Instance.flowDefinition.flowId == 86 && _IsFileRole}">
								<td><input style="cursor: pointer;" type="button" value="结束" onclick="endInstance('${_Instance.instanceId}');"/></td>
							</c:if>

							<c:if test="${ _Instance.flowDefinition.flowId != 86 && (_Instance.flowDefinition.flowId < 90 || _Instance.flowDefinition.flowId > 100)}">
								<td><input style="cursor: pointer;" type="button" value="结束" onclick="endInstance('${_Instance.instanceId}');"/></td>
							</c:if>

							<%--	<c:if test="${_Instance.stamped == 2 && _Instance.flowDefinition.flowId >89 && _Instance.flowDefinition.flowId <101 && _IsFileRole}">
									<td><input style="cursor: pointer;" type="button" value="结束" onclick="endInstance('${_Instance.instanceId}');"/></td>
								</c:if>--%>
							<c:if test="${_Instance.flowDefinition.flowId >89 && _Instance.flowDefinition.flowId <101 && _Instance.stamped == 2  && _IsFileRole}">
								<%--<c:if test="${(empty _Instance.startTime && _Instance.applier.personId == _SYSTEM_USER.personId) || _IsCharger}">--%>
									<td><input style="cursor: pointer;" type="button" value="结束" onclick="endInstance('${_Instance.instanceId}');"/></td>
								<%--</c:if>--%>
							</c:if>

							<td><input style="cursor: pointer;" type="button" value="短信提醒" onclick="sms('${_Instance.instanceId}');"/></td>

							<c:if test="${ _Instance.flowDefinition.flowId == 86}">
								<c:choose>
									<c:when test="${!_Instance.suspended && _Instance.stamped < 2}">
										<td><input style="cursor: pointer;" type="button" value="流转暂停" onclick="pause('${_Instance.instanceId}');"/></td>
									</c:when>
									<c:when test="${_Instance.suspended}">
										<td><input style="cursor: pointer;" type="button" value="取消暂停" onclick="cancelPause('${_Instance.instanceId}');"/></td>
									</c:when>
								</c:choose>
							</c:if>
							<c:if test="${ _Instance.flowDefinition.flowId >89 && _Instance.flowDefinition.flowId <101}">
								<c:choose>
									<c:when test="${!_Instance.suspended && _Instance.stamped < 2}">
										<td><input style="cursor: pointer;" type="button" value="流转暂停" onclick="pause('${_Instance.instanceId}');"/></td>
									</c:when>
									<c:when test="${_Instance.suspended}">
										<td><input style="cursor: pointer;" type="button" value="取消暂停" onclick="cancelPause('${_Instance.instanceId}');"/></td>
									</c:when>
								</c:choose>
							</c:if>
							<c:if test="${ _Instance.flowDefinition.flowId != 86 && (_Instance.flowDefinition.flowId < 90 || _Instance.flowDefinition.flowId > 100)}">

								<c:choose>
									<c:when test="${!_Instance.suspended}">
										<td><input style="cursor: pointer;" type="button" value="暂停" onclick="pause('${_Instance.instanceId}');"/></td>
									</c:when>
									<c:otherwise>
										<td><input style="cursor: pointer;" type="button" value="取消暂停" onclick="cancelPause('${_Instance.instanceId}');"/></td>
									</c:otherwise>
								</c:choose>
							</c:if>
						</c:when>
						<c:otherwise>
							<%--<td><input style="cursor: pointer;" type="button" value="恢复流转" onclick="restore('${_Instance.instanceId}');"/></td>--%>
							<c:if test="${_Instance.flowDefinition.filerType == '0' && _IsCharger  && !_Instance.filed}">
								<td><input style="cursor: pointer;" type="button" value="归档" onclick="filedInstance('${_Instance.instanceId}');"/></td>
							</c:if>

							<c:if test="${_Instance.flowDefinition.filerType == '1' && _IsFileRole && !_Instance.filed}">
								<td><input style="cursor: pointer;" type="button" value="归档" onclick="filedInstance('${_Instance.instanceId}');"/></td>
							</c:if>
							<c:if test="${_Instance.flowDefinition.filerType == '1' && _IsFileRole && _Instance.filed}">
								<td><input style="cursor: pointer;" type="button" value="同步到档案系统" onclick="filedInstance('${_Instance.instanceId}');"/></td>
							</c:if>
							<c:if test="${_Instance.flowDefinition.filerType == '0' && _IsCharger  && _Instance.filed}">
								<td><input style="cursor: pointer;" type="button" value="同步到档案系统" onclick="filedInstance('${_Instance.instanceId}');"/></td>
							</c:if>

						</c:otherwise>
					</c:choose>
					<td><input style="cursor: pointer;" type="button" value="授权查看" onclick="doAuthorize('${_Instance.instanceId}');"/></td>
				</c:if>
				</c:if>

				&nbsp;&nbsp;
				<input style="cursor: pointer;" type="button" value="打印" onclick="print();"/>
				&nbsp;&nbsp;
				<input style="cursor: pointer;" type="button" value="修改记录" onclick="getModify('${_Instance.instanceId}');"/>

				&nbsp;&nbsp;
				<input style="cursor: pointer;" type="button" value="关闭" onclick="window.close();"/>
			</div>

			<div id="layerModalDlg" style="display: none;">
				<table id="layerTable"></table>
			</div>

			<div id="nodeModalDlg" style="display: none;">
				<table id="nodeTable"></table>
			</div>

			<div id="stopDepCheckDlg" style="display: none;">
				<table id="stopDepCheck">
					<tr>
						<td style="width: 25%">中止意见：</td>
						<td><textarea name="submiterWord" id="submiterWord" cols="30" rows="5"></textarea></td>
					</tr>
				</table>
			</div>

		</div>
	</div>
</form>
</body>
</html>
                  
