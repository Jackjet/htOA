<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
	<title>审批流转</title>
	<%--<script src="<c:url value="/"/>datePicker/WdatePicker.js" type="text/javascript"></script>--%>
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
	<style type="text/css">
		.buttonclass {
			font-weight:bold;
		}
	</style>
	<script language="javaScript">
        function commonFun(path) {
            window.name = "__self";
            window.open(path, "__self");
        }

        //重新加载浏览页面
        function reloadPage(purchaseId){
            var path = "<c:url value='/gcpurchase/purchaseInfor.do'/>?method=view&purchaseId=" + purchaseId;
            commonFun(path);
        }

        //部门审核
        function depCheck(purchaseId){
            var path = "<c:url value='/gcpurchase/purchaseInfor.do'/>?method=editDepCheck&purchaseId=" + purchaseId;
            commonFun(path);
        }
        //修改
        function edit(purchaseId){
            var path = "<c:url value='/gcpurchase/purchaseInfor.do'/>?method=edit&purchaseId=" + purchaseId;
            commonFun(path);
        }
		//查看子流程
		function cksf(ziliuchengId) {
            window.open("/review.do?method=view&sanfangID="+ziliuchengId, "_blank");
        }
        function ckzt(ziliuchengId) {
            window.open("/bid.do?method=view&bidInfoId="+ziliuchengId, "_blank");
        }
        function ckht(ziliuchengId) {
            window.open("/contract.do?method=view&contractID="+ziliuchengId, "_blank");
        }
        //发起合同
        function fqht(flowId) {
            window.open("/workflow/instanceInfor.do?method=edit&flowId="+flowId, "_blank");
        }
        //审核
        function check(purchaseId){

            if(${packageId}==-1){
                var path = "<c:url value='/gcpurchase/checkInfor.do'/>?method=edit&purchaseId=" + purchaseId ;
			}else{
                var path = "<c:url value='/gcpurchase/checkInfor.do'/>?method=edit&purchaseId=" + purchaseId +"&packageId="+${packageId} ;
			}
            commonFun(path);
        }
        //发起子流程
        function start1(purchaseId){

            if(${_Purchase.purchaseWay == 1}){
                var path = "<c:url value='/review.do'/>?method=start&purchaseId=" + purchaseId ;
            }else if(${_Purchase.purchaseWay == 2}){
                var path = "<c:url value='/bid.do'/>?method=start&purchaseId=" + purchaseId ;
            }else{
                var path = "<c:url value='/contract.do'/>?method=start&purchaseId=" + purchaseId ;
            }
            commonFun(path);
        }

        function canSave(purchaseId) {

            if(${packageId} == -1){
                var path = "<c:url value='/gcpurchase/checkInfor.do'/>?method=canSave&purchaseId=" + purchaseId ;
            }else{
                var path = "<c:url value='/gcpurchase/checkInfor.do'/>?method=canSave&purchaseId=" + purchaseId +"&packageId="+${packageId} ;
            }
            commonFun(path);
        }

        //结束审核实例
        function endInstance(purchaseId){
            var path = "<c:url value='/gcpurchase/purchaseInfor.do'/>?method=editEnd&purchaseId=" + purchaseId;
            commonFun(path);
        }

        //授权
        function doAuthorize(rowId){
//			var returnArray = window.showModalDialog("/workflow/instanceInfor.do?method=editInforRight&rowId="+rowId,'',"dialogWidth:800px;dialogHeight:600px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
            window.open("/gcpurchase/instanceInfor.do?method=editInforRight&rowId="+rowId, "_blank");
//			if(returnArray != null && returnArray[0] == "refresh") {
//				self.location.reload();
//			}
        }
	</script>
</head>
<body style="overflow-y: auto;padding: 0 100px" onload="">
<br/>
<form id="purchaseInforForm" name="purchaseInforForm" method="post">
	<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 98%">
		<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
			<div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix" style="color: #22FBFF">
				<span class="ui-jqgrid-title">查看审批信息&nbsp;【${_Flow.flowName} 经办人:${_Purchase.applier.person.personName}】</span>
			</div>
			<div style="width: 90%"><hr style="border:0.5px solid #22FBFF;" /></div>

			<%-- 审核实例信息 --%>
			<%@include file="includePurchase.jsp" %>
			<div style="width: 100%" class="ui-state-default ui-jqgrid-pager ui-corner-bottom" dir="ltr">
				<c:if test="${_Purchase.deleteFlag != 1}"><!--  && !_Purchase.suspended -->
				<c:if test="${(_Purchase.flowId.flowId == 3 &&  _Purchase.purchaseStatus == 0  && _Purchase.applier.personId == _SYSTEM_USER.personId)  || _SYSTEM_USER.userType == 1 }">
					<c:if test="${(empty _Purchase.startTime && _Purchase.applier.personId == _SYSTEM_USER.personId) || _SYSTEM_USER.userType == 1}">
						<td><input style="cursor: pointer;" type="button" value="修改" onclick="edit('${_Purchase.purchaseId}');"/></td>
					</c:if>
				</c:if>
				<c:if test="${empty _Purchase.checkTime}">
					<c:if test="${_CanDepCheck && _Purchase.purchaseStatus != 21}">
						<td><input style="cursor: pointer;" type="button" value="审核文件" onclick="depCheck('${_Purchase.purchaseId}');"/></td>
					</c:if>
				</c:if>
				<c:if test="${_CanCheck && _Purchase.purchaseStatus != 9 && _Purchase.purchaseStatus != 4 && _Purchase.purchaseStatus != 1 && _Purchase.purchaseStatus != 5 && _Purchase.purchaseStatus != 2 && _Purchase.purchaseStatus != 21}">
					<td><input style="cursor: pointer;" type="button" value="审核文件" onclick="check('${_Purchase.purchaseId}');"/></td>
				</c:if>
				<c:if test="${_Purchase.purchaseStatus == 5 || _Purchase.purchaseStatus == 2}">
					<td><input style="cursor: pointer;" type="button" value="单条提交" onclick="check('${_Purchase.purchaseId}');"/></td>
				</c:if>
				<c:if test="${_CanCheck && _Purchase.purchaseStatus == 9}">
					<c:if test="${_Purchase.purchaseWay == 1 && _Purchase.sanfangId == 0}">
						<td><input style="cursor: pointer;" type="button" value="发起三方比价" onclick="start1('${_Purchase.purchaseId}');"/></td>
					</c:if>
					<c:if test="${_Purchase.purchaseWay == 2 && _Purchase.zhaotouId == 0}">
						<td><input style="cursor: pointer;" type="button" value="发起招投标" onclick="start1('${_Purchase.purchaseId}');"/></td>
					</c:if>
					<c:if test="${_Purchase.purchaseWay == 3 && _Purchase.hetongId == 0}">
						<td><input style="cursor: pointer;" type="button" value="发起合同变更" onclick="start1('${_Purchase.purchaseId}');"/></td>
					</c:if>
					<c:if test="${_Purchase.purchaseWay == 1 && _Purchase.sanfangId == -1}">
						<td><input style="cursor: pointer;" type="button" value="原三方比价未通过，发起新三方比价" onclick="start1('${_Purchase.purchaseId}');"/></td>
					</c:if>
					<c:if test="${_Purchase.purchaseWay == 2 && _Purchase.zhaotouId == -1}">
						<td><input style="cursor: pointer;" type="button" value="原招投标未通过，发起新招投标" onclick="start1('${_Purchase.purchaseId}');"/></td>
					</c:if>
					<c:if test="${_Purchase.purchaseWay == 3 && _Purchase.hetongId == -1}">
						<td><input style="cursor: pointer;" type="button" value="原合同变更未通过，发起新合同变更" onclick="start1('${_Purchase.purchaseId}');"/></td>
					</c:if>
				</c:if>
				<c:if test="${_CanSave && (_Purchase.purchaseStatus == 4 || _Purchase.purchaseStatus == 1 )}">
					<td><input style="cursor: pointer;" type="button" value="处理采购" onclick="canSave('${_Purchase.purchaseId}');"/></td>
				</c:if>
				<c:if test="${_CanSave && (_Purchase.purchaseStatus == 5 || _Purchase.purchaseStatus == 2)}">
					<td><input style="cursor: pointer;" type="button" value="修改" onclick="canSave('${_Purchase.purchaseId}');"/></td>
				</c:if>
				&nbsp;&nbsp;&nbsp;&nbsp;
					<c:choose>
						<c:when test="${empty _Purchase.endTime}">
							<c:if test="${_SYSTEM_USER.userType == 1 || (_SYSTEM_USER.personId == _Purchase.applier.personId && _Purchase.purchaseStatus<3)}">
								<td><input style="cursor: pointer;" type="button" value="删除" onclick="endInstance('${_Purchase.purchaseId}');"/></td>
							</c:if>
						</c:when>
					</c:choose>
				</c:if>
				<c:if test="${_Purchase.sanfangId != 0 && _Purchase.sanfangId != -1}">
					&nbsp;&nbsp;
					<input style="cursor: pointer;" type="button" value="查看三方比价" onclick="cksf('${_Purchase.sanfangId}')" />
				</c:if>
				<c:if test="${_Purchase.zhaotouId != 0 && _Purchase.zhaotouId != -1}">
					&nbsp;&nbsp;
					<input style="cursor: pointer;" type="button" value="查看招投标" onclick="ckzt('${_Purchase.zhaotouId}')"/>
				</c:if>
				<c:if test="${_Purchase.hetongId != 0 && _Purchase.hetongId != -1}">
					&nbsp;&nbsp;
					<input style="cursor: pointer;" type="button" value="查看合同变更" onclick="ckht('${_Purchase.hetongId}')"/>
				</c:if>
				<c:if test="${_Purchase.purchaseStatus == 11}">
					&nbsp;&nbsp;
					<input style="cursor: pointer;" type="button" value="发起合同审批" onclick="fqht('86')"/>
				</c:if>
				&nbsp;&nbsp;
				<input style="cursor: pointer;" type="button" value="关闭" onclick="window.close();"/>
			</div>
			<div id="layerModalDlg" style="display: none;">
				<table id="layerTable"></table>
			</div>
			<div id="nodeModalDlg" style="display: none;">
				<table id="nodeTable"></table>
			</div>
			</div>
		</div>
	</div>
</form>
</body>
</html>
                  
