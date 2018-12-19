<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
	<title>详细信息</title>
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
        //审核
        function check(supplierID){
            var path = "/supplier.do?method=editCheck&supplierID=" + supplierID;
           commonFun(path);
        }
        function certify(supplierID){
            var path = "/supplier.do?method=certify&supplierID=" + supplierID;
            commonFun(path);
        }
        function certification(supplierID){
            var path = "/supplier.do?method=editCertify&supplierID=" + supplierID;
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
				<span class="ui-jqgrid-title">查看详情</span>
			</div>
			<div style="width: 90%"><hr style="border:0.5px solid #22FBFF;" /></div>
			<%-- 审核实例信息 --%>
			<%@include file="includeInstance.jsp" %>
			<div style="width: 100%" class="ui-state-default ui-jqgrid-pager ui-corner-bottom" dir="ltr">
				<c:if test="${_CanReview}">
					<input style="cursor: pointer;" type="button" value="审核" onclick="check('${supplier.supplierID}');"/>
				</c:if>
				<c:if test="${(supplier.supplierStatus=='潜在'||supplier.supplierStatus=='即将过期')&&_CanStartCetify}">
					<input style="cursor: pointer;" type="button" value="合格认证" onclick="certify('${supplier.supplierID}');"/>
				</c:if>
				<c:if test="${_CanCertify}">
					<input style="cursor: pointer;" type="button" value="认证" onclick="certification('${supplier.supplierID}');"/>
				</c:if>
				&nbsp;&nbsp;
				<input style="cursor: pointer;" type="button" value="关闭" onclick="window.close();"/>
			</div>
		</div>
	</div>
</form>
</body>
</html>

