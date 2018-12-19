<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
	<%--<meta http-equiv="X-UA-Compatible" content="IE=9" />--%>
	<title>新增模板信息</title>
	<script src="/js/jquery-1.9.1.js" type="text/javascript"></script>
	<script type="text/javascript" src="<c:url value="/"/>components/jqgrid/js/jquery.ui.core.js"></script>
	<script type="text/javascript" src="<c:url value="/"/>components/jqgrid/js/jquery.ui.widget.js"></script>
	<script type="text/javascript" src="<c:url value="/"/>components/jqgrid/js/grid.locale-cn.js"></script>
	<script type="text/javascript" src="<c:url value="/"/>components/jqgrid/js/jquery.ui.datepicker.js"></script>

	<link rel="stylesheet" type="text/css" href="<c:url value="/"/>components/jqgrid/css/jquery.ui.all.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />

	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="/css/noTdBottomBorder.css" />

	<script src="/js/inc_javascript.js"></script>
	<script src="/js/addattachment.js"></script>
	<script src="/js/commonFunction.js"></script>

	<style type="text/css">
		.buttonclass {
			font-weight:bold;
		}
	</style>

	<!-- formValidator -->
	<link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css"></link>
	<script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript" charset="UTF-8"></script>
	<script src="<c:url value="/"/>components/formvalidator/js/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
	<!-- ------------- -->

	<script>
        $(document).ready(function(){
            //验证
            $.formValidator.initConfig({formid:"instanceInforForm",onerror:function(msg){alert(msg)},onsuccess:function(){}});
            $("#target").formValidator({onshow:"请输入指标信息",onfocus:"指标信息不能为空"}).inputValidator({min:1,onerror: "请输入指标信息"});
            $("#score").formValidator({onshow:"请输入分值",onfocus:"分值不能为空"}).inputValidator({min:1,onerror: "请输入分值"});
            $("#standard").formValidator({onshow:"请输入标准信息",onfocus:"标准信息不能为空"}).inputValidator({min:1,onerror: "请输入标准信息"});
            $("#type").formValidator({onshow:"请选择类型",onfocus:"类型不能为空"}).inputValidator({min:1,onerror: "请选择类型"});

            //去掉初始化的提示信息
            $("#instanceTitleTip").html("");
            //button字体变粗
            for(i=0;i<document.getElementsByTagName("INPUT").length;i++){
                if(document.getElementsByTagName("INPUT")[i].type=="button" || document.getElementsByTagName("INPUT")[i].type=="submit")
                    document.getElementsByTagName("INPUT")[i].className="buttonclass" ;
            }
        });
	</script>

	<script type="text/javascript">
        //验证是否重复
       function validTarget(){
            $.ajax({
                type: "post",
                dataType:"json",
                url: "/extend/templateInfo.do?method=validTarget",
                data: "target=" + $("#target").val(),
                success: function(data){
                    if(!data._Valid){
                        $("#instanceTargetTip").html("该指标已存在");
                        $("#target").val("")
                    }
                }
            });
            $("#instanceTargetTip").html("");
        }
	</script>
	<base target="_self"/>
</head>

<body style="overflow-y: auto;padding: 0 100px" onload="">
<br/>
<form  id="instanceInforForm" name="instanceInforForm" action="/extend/templateInfo.do?method=save" method="post" >
	<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 98%">
		<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
			<div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
				<span class="ui-jqgrid-title">新增模板基础信息 &nbsp;</span>
			</div>

			<div>
				<table cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable" style="width: 90%;">
					<tbody id="flowTemplate">
					<tr>
						<td style="width: 18%"></td>
						<td style="width: 40%"></td>
						<td style="width: 15%"></td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum" style="width: 15%">指标：</td>
						<td style="padding-top: 5px;padding-bottom: 5px;"><input id="target" name="target" onblur="validTarget();"/><div id="instanceTargetTip"></div></td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum" style="width: 18%">分值：</td>
						<td colspan="3"><input id="score" name="score" onkeyup="value=value.replace(/[^0-9.]/g,'')"/><div id="instanceScoreTip"></div></td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum" style="width: 18%">标准：</td>
						<td colspan="3"><input id="standard" name="standard"/><div id="instanceStandardTip"></div></td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
						<td class="ui-state-default jqgrid-rownum" style="width: 18%">类型：</td>
						<td colspan="3">
							<select id="type" name="type">
								<option value="">-请选择-</option>
								<option value="技术标">技术标</option>
								<option value="商务标">商务标</option>
							</select>
							<div id="instanceTypeTip"></div>
						</td>
					</tr>
					</tbody>
				</table>
				<div class="ui-state-default ui-jqgrid-pager ui-corner-bottom" dir="ltr" style="width: 100%;margin-top:20px;overflow:visible" >
					<input style="cursor: pointer;" type="submit" value="保存"/>
					<input style="cursor: pointer;" type="button" value="关闭" onclick="window.close();"/>
				</div>
			</div>
		</div>
	</div>
</form>
</body>

</html>

