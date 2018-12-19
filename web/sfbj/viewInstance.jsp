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
	</style>


	<script type="text/javascript">
        $().ready(function(){
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
        //审核
        function check(sanfangID){
            var path = "/review.do?method=edit&sanfangID=" + sanfangID;
           commonFun(path);
        }


	</script>
</head>
<body style="overflow-y: auto;padding: 0 100px" onload="">

<br/>
<form id="instanceInforForm" name="instanceInforForm" method="post">
	<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 98%">
		<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
			<div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix" style="color: #22FBFF">
				<span class="ui-jqgrid-title">查看审批信息 &nbsp;【三方比价 主办人:${sanfangInfor.purchaseCharge.person.personName}  发起时间:${sanfangInfor.startDate}】</span>
			</div>
			<div style="width: 90%"><hr style="border:0.5px solid #22FBFF;" /></div>
			<%-- 审核实例信息 --%>
			<%@include file="includeInstance.jsp" %>
			<div style="width: 100%" class="ui-state-default ui-jqgrid-pager ui-corner-bottom" dir="ltr">
				<c:if test="${_CanReview}">
					<input style="cursor: pointer;" type="button" value="审核文件" onclick="check('${sanfangInfor.sanfangID}');"/>
				</c:if>
				&nbsp;&nbsp;
				<input style="cursor: pointer;" type="button" value="关闭" onclick="window.close();"/>
			</div>
		</div>
	</div>
</form>
</body>
</html>

