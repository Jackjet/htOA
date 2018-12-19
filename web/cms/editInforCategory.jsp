<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>
<title>编辑资讯分类</title>
<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
<script src="<c:url value="/"/>js/inc_javascript.js"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/"/>components/jqgrid/css/jquery.ui.all.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
<link rel="stylesheet" type="text/css" media="screen" href="/css/noTdBottomBorder.css" />

<!-- formValidator -->
<link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css"></link>
<script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript" charset="UTF-8"></script>
<script src="<c:url value="/"/>components/formvalidator/js/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
<!-- ------------- -->

<script>
	var fieldNames = document.getElementsByName("fieldName");		//字段名
	var displayTitles = document.getElementsByName("displayTitle");	//显示标题
	var displayOrders = document.getElementsByName("displayOrder");	//添加时排序
	var displayeds = document.getElementsByName("displayed");		//添加时是否显示
	var listOrders = document.getElementsByName("listOrder");		//列表里的排序
	var listDisplayeds = document.getElementsByName("listDisplayed");//列表里是否显示
	
	//验证
	$(document).ready(function(){
		$.formValidator.initConfig({formid:"inforCategoryForm",onerror:function(msg){alert(msg)},onsuccess:function(){alert('信息编辑成功');submitData();}});
		$("#categoryName").formValidator({onshow:"请输入分类名称",onfocus:"分类名称不能为空",oncorrect:"输入正确"}).inputValidator({min:1,onerror:"分类名称不能为空,请确认"});
		//$("#inherit").formValidator({onshow:"继承的信息有:模板,字段,附件路径",onfocus:"继承的信息有:模板,字段,附件路径",oncorrect:"选择正确"});
		$("#orderNo").formValidator({onshow:"请输入排序编号",onfocus:"只能输入阿拉伯数字",oncorrect:"输入正确"}).inputValidator({min:1,max:99,type:"value",onerrormin:"输入的值必须大于等于1",onerror:"只能输入1~99的数字"});
		$("#urlPath").formValidator({onshow:"请输入访问路径",onfocus:"如：party",oncorrect:"输入正确"}).inputValidator({min:1,onerror:"访问路径不能为空,请确认"});
		for (var i=1;i<18;i++) {
			$("#displayTitle"+i).formValidator({tipid:"fieldTip"+i,onshow:"请输入显示标题",onfocus:"显示标题不能为空",oncorrect:"输入正确"}).inputValidator({min:1,onerror:"显示标题不能为空,请确认"});
			$("#displayOrder"+i).formValidator({tipid:"fieldTip"+i,onshow:"请输入添加时排序",onfocus:"只能输入阿拉伯数字",oncorrect:"输入正确"}).inputValidator({min:1,max:99,type:"value",onerrormin:"输入的值必须大于等于1",onerror:"只能输入1~99的数字"});
			$("#listOrder"+i).formValidator({tipid:"fieldTip"+i,onshow:"请输入列表时排序",onfocus:"只能输入阿拉伯数字",oncorrect:"输入正确"}).inputValidator({min:1,max:99,type:"value",onerrormin:"输入的值必须大于等于1",onerror:"只能输入1~99的数字"});
		}
		
		//初始化显示字段的显示标题及排序
		if (${empty param.categoryId}) {
			//显示标题
			displayTitles[0].value = "信息主题";
			displayTitles[1].value = "发布单位";
			displayTitles[2].value = "是否重要";
			displayTitles[3].value = "关键字";
			displayTitles[4].value = "信息内容";
			displayTitles[5].value = "附件路径";
			displayTitles[6].value = "信息时间";
			displayTitles[7].value = "相关链接";
			displayTitles[8].value = "信息来源";
			displayTitles[9].value = "图片路径";
			displayTitles[10].value = "自定义字符1";
			displayTitles[11].value = "自定义字符2";
			displayTitles[12].value = "自定义字符3";
			displayTitles[13].value = "自定义时间1";
			displayTitles[14].value = "自定义时间2";
			displayTitles[15].value = "自定义Boolean型";
			displayTitles[16].value = "自定义Decimal";
			//添加,列表时排序
			for (var i=0;i<17;i++) {
				var j = i+1;
				displayOrders[i].value = j;
				listOrders[i].value = j;
			}
		}else {
			for (var i=0;i<fieldNames.length;i++) {
				<c:forEach var="field" items="${_Category.fields}" varStatus="index">
					if (fieldNames[i].value == "${field.fieldName}") {
						displayTitles[i].value = "${field.displayTitle}";	//实际显示标题
						displayOrders[i].value = "${field.displayOrder}";	//实际添加时的排序
						listOrders[i].value = "${field.listOrder}";			//实际列表的排序
						//添加时是否显示
						if (${field.displayed}) {
							displayeds[i].checked = true;
						}else {
							displayeds[i].checked = false;
						}
						//列表里是否显示
						if (${field.listDisplayed}) {
							listDisplayeds[i].checked = true;
						}else {
							listDisplayeds[i].checked = false;
						}
					}
				</c:forEach>
			}
		}
		
	});
	
	//提交数据
	function submitData() {
		var form = document.getElementById('inforCategoryForm');
		form.action = '<c:url value="/cms/inforCategory.do"/>?method=save';

        var browserName=navigator.userAgent.toLowerCase();
        if(/chrome/i.test(browserName)&&/webkit/i.test(browserName)&&/mozilla/i.test(browserName)){
            //如果是chrome浏览器
            $.ajax({
                type: "POST",
                url:'<c:url value="/cms/inforCategory.do"/>?method=save',
                data:$('#inforCategoryForm').serialize(),
                async: false
            });
        }else {
            form.submit();
		}
		//window.opener.location.reload();
		window.returnValue = "Y";
		window.close();
	}
	
	//若需要继承父类,则获取父类数据并填充到对应框内
	function getParentInfor() {
		var inherit = $("#inherit").val();
		if (inherit == "true") {
			var parentId = $("#parentId").val(); 
			$.getJSON("/cms/inforCategory.do?method=getParentInfor&parentId="+parentId,function(data) {
				//将父类的模板信息和附件路径赋值给对应字段
				var attchmentPath = document.getElementById("attchmentPath");
				var contentTemplate = document.getElementById("contentTemplate");
				attchmentPath.value = data._Parent.attchmentPath;
				if (contentTemplate != null) {
					var templateName = "";
					if (data._Parent.contentTemplate != null) {
						//获取内容模板名称
						templateName = data._Parent.contentTemplate.split("/")[data._Parent.contentTemplate.split("/").length-1];
					}
					
					//定位到父类的内容模板
					for (var i=0;i<contentTemplate.length;i++) {
						if (contentTemplate.options[i].value == templateName) {
							contentTemplate.options[i].selected = true;
							break;
						}
					}
				}
				
				//将父类的字段信息填充到对应框内
				if (data._Fields != null && data._Fields != "") {
					//有信息时填充
					$.each(data._Fields, function(i, n) {
			              for (var i=0;i<fieldNames.length;i++) {
			              	if (n.fieldName == fieldNames[i].value) {
				              	displayTitles[i].value = n.displayTitle;	//显示标题
				              	displayOrders[i].value = n.displayOrder;	//添加时排序
				              	listOrders[i].value = n.listOrder;			//列表里排序
				              	if (n.displayed) {							//添加时是否显示
				              		displayeds[i].checked = true;
				              	}else {
				              		displayeds[i].checked = false;
				              	}
				              	if (n.listDisplayed) {						//列表里是否显示
				              		listDisplayeds[i].checked = true;
				              	}else {
				              		listDisplayeds[i].checked = false;
				              	}
				          	}
			              }
			        });
				}else {
					//无信息时清空
					for (var i=0;i<fieldNames.length;i++) {
				       displayTitles[i].value = "";	//显示标题
				       displayOrders[i].value = "";	//添加时排序
				       listOrders[i].value = "";	//列表里排序
				       displayeds[i].checked = false;
				       listDisplayeds[i].checked = false;
			        }
				}
			});
		}
	}
</script>
<base target="_self"/>
</head>

<body style="padding: 0 100px">
<br/>
<form id="inforCategoryForm" <%--action="/cms/inforCategory.do?method=save"--%>onsubmit="return false" method="post">
<input type="hidden" name="categoryId" value="${_Category.categoryId}"/>
<input type="hidden" name="deleted" value="${_Category.deleted}"/>
<input type="hidden" name="attchmentPath" id="attchmentPath" value="${_Category.attchmentPath}"/>
<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 100%">
<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
  <div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
    <span class="ui-jqgrid-title">编辑资讯分类</span>
  </div>
<style>
	/*code{*/
		/*td: #b40000;*/
	/*}*/
</style>
	<div class="ui-jqgrid-bdiv" style="height: auto; width: 100%">
		<div style="position: relative;">
			<div>
				<table cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable" style="width: 100%">
					<tbody>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td style="width: 20%;" class="ui-state-default jqgrid-rownum">分类名称：</td>
							<td style="width: 57%;"><input type="text" id="categoryName" name="categoryName" size="20" value="${_Category.categoryName}"/></td>
							<td style="width: 23%;"><div id="categoryNameTip" style="width:250px"></div></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">所属分类：</td>
							<td><select id="parentId" name="parentId" onchange="getParentInfor();">
									<c:forEach items="${requestScope._TreeCategorys}" var="category" varStatus="status">
										<c:choose>
											<c:when test="${_Category.parent.categoryId == category.categoryId}">
												<option value="${category.categoryId}" selected="selected">
											</c:when>
											<c:otherwise>
												<option value="${category.categoryId}">
											</c:otherwise>
										</c:choose>
											<c:forEach begin="0" end="${category.layer}">&nbsp;</c:forEach>
											<c:if test="${category.layer==1}"><b>+</b></c:if><c:if test="${category.layer==2}"><b>-</b></c:if>${category.categoryName}				
										</option>
									</c:forEach>
						        </select></td>
							<td></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">是否子分类：</td>
							<td><select id="leaf" name="leaf"><option value="false">否</option><option value="true">是</option></select><script language="javaScript">
									var form = document.getElementById('inforCategoryForm');
									getOptsValue(form.leaf,"${_Category.leaf}");
								</script></td>
							<td></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">是否继承父类信息：</td>
							<td><select id="inherit" name="inherit" onchange="getParentInfor();"><option value="false">否</option><option value="true">是</option></select><script language="javaScript">
									getOptsValue(form.inherit,"${_Category.inherit}");
								</script></td>
							<td><div id="inheritTip" style="width:250px"></div></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">排序编号：</td>
							<td><input type="text" id="orderNo" name="orderNo" size="10" value="${_Category.orderNo}"/></td>
							<td><div id="orderNoTip" style="width:250px"></div></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">访问路径：</td>
							<td><input type="text" id="urlPath" name="urlPath" size="30" value="${_Category.urlPath}"/></td>
							<td><div id="urlPathTip" style="width:250px"></div></td>
						</tr>
						
						<%--<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">列表模板路径：</td>
							<td><input type="text" id="listTemplate" name="listTemplate" size="30" value="${_Category.listTemplate}"/></td>
							<td><div id="listTemplateTip" style="width:250px"></div></td>
						</tr>--%>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td class="ui-state-default jqgrid-rownum">内容模板：</td>
							<td><select name="contentTemplate" id="contentTemplate"></select><script>
											//获取内容模板信息
											$.getJSON("/cms/inforCategory.do?method=getCmsTemplates",function(data) {
												if (data != null) {
													var content = "<option value=''>--选择模板--</option>";
													$.each(data.templates, function(i, n) {
														if ('${_TemplateName}'==n.templateName) {
															content += "<option value='"+ n.templateName + "' selected='selected'>" + n.templateName + "</option>";
														}else {
															content += "<option value='"+ n.templateName + "'>" + n.templateName + "</option>";
														}
													});
													$("#contentTemplate").html(content);
												}
											});
										</script></td>
							<td><div id="contentTemplateTip" style="width:250px"></div></td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr">
							<td class="ui-state-default jqgrid-rownum" colspan="3">以下为添加及列表展示该分类信息时需要显示的字段：</td>
						</tr>
						
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td colspan="2">字段名：<input type="text" readonly="readonly" name="fieldName" value="inforTitle" size="12"/> 显示标题：<input type="text" id="displayTitle1" name="displayTitle" size="20"/> 添加时排序：<input type="text" id="displayOrder1" name="displayOrder" size="3"/> 添加时显示：<input type="checkbox" name="displayed" value="1"/> 列表时排序：<input type="text" id="listOrder1" name="listOrder" size="3"/> 列表时显示：<input type="checkbox" name="listDisplayed" value="1"/></td>
							<td><div id="fieldTip1" style="width:250px"></div></td>
						</tr>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td colspan="2">字段名：<input type="text" readonly="readonly" name="fieldName" value="issueUnit" size="12"/> 显示标题：<input type="text" id="displayTitle2" name="displayTitle" size="20"/> 添加时排序：<input type="text" id="displayOrder2" name="displayOrder" size="3"/> 添加时显示：<input type="checkbox" name="displayed" value="2"/> 列表时排序：<input type="text" id="listOrder2" name="listOrder" size="3"/> 列表时显示：<input type="checkbox" name="listDisplayed" value="2"/></td>
							<td><div id="fieldTip2" style="width:250px"></div></td>
						</tr>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td colspan="2">字段名：<input type="text" readonly="readonly" name="fieldName" value="important" size="12"/> 显示标题：<input type="text" id="displayTitle3" name="displayTitle" size="20"/> 添加时排序：<input type="text" id="displayOrder3" name="displayOrder" size="3"/> 添加时显示：<input type="checkbox" name="displayed" value="3"/> 列表时排序：<input type="text" id="listOrder3" name="listOrder" size="3"/> 列表时显示：<input type="checkbox" name="listDisplayed" value="3"/></td>
							<td><div id="fieldTip3" style="width:250px"></div></td>
						</tr>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td colspan="2">字段名：<input type="text" readonly="readonly" name="fieldName" value="keyword" size="12"/> 显示标题：<input type="text" id="displayTitle4" name="displayTitle" size="20"/> 添加时排序：<input type="text" id="displayOrder4" name="displayOrder" size="3"/> 添加时显示：<input type="checkbox" name="displayed" value="4"/> 列表时排序：<input type="text" id="listOrder4" name="listOrder" size="3"/> 列表时显示：<input type="checkbox" name="listDisplayed" value="4"/></td>
							<td><div id="fieldTip4" style="width:250px"></div></td>
						</tr>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td colspan="2">字段名：<input type="text" readonly="readonly" name="fieldName" value="inforContent" size="12"/> 显示标题：<input type="text" id="displayTitle5" name="displayTitle" size="20"/> 添加时排序：<input type="text" id="displayOrder5" name="displayOrder" size="3"/> 添加时显示：<input type="checkbox" name="displayed" value="5"/> 列表时排序：<input type="text" id="listOrder5" name="listOrder" size="3"/> 列表时显示：<input type="checkbox" name="listDisplayed" value="5"/></td>
							<td><div id="fieldTip5" style="width:250px"></div></td>
						</tr>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td colspan="2">字段名：<input type="text" readonly="readonly" name="fieldName" value="attachment" size="12"/> 显示标题：<input type="text" id="displayTitle6" name="displayTitle" size="20"/> 添加时排序：<input type="text" id="displayOrder6" name="displayOrder" size="3"/> 添加时显示：<input type="checkbox" name="displayed" value="6"/> 列表时排序：<input type="text" id="listOrder6" name="listOrder" size="3"/> 列表时显示：<input type="checkbox" name="listDisplayed" value="6"/></td>
							<td><div id="fieldTip6" style="width:250px"></div></td>
						</tr>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td colspan="2">字段名：<input type="text" readonly="readonly" name="fieldName" value="inforTime" size="12"/> 显示标题：<input type="text" id="displayTitle7" name="displayTitle" size="20"/> 添加时排序：<input type="text" id="displayOrder7" name="displayOrder" size="3"/> 添加时显示：<input type="checkbox" name="displayed" value="7"/> 列表时排序：<input type="text" id="listOrder7" name="listOrder" size="3"/> 列表时显示：<input type="checkbox" name="listDisplayed" value="7"/></td>
							<td><div id="fieldTip7" style="width:250px"></div></td>
						</tr>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td colspan="2">字段名：<input type="text" readonly="readonly" name="fieldName" value="relateUrl" size="12"/> 显示标题：<input type="text" id="displayTitle8" name="displayTitle" size="20"/> 添加时排序：<input type="text" id="displayOrder8" name="displayOrder" size="3"/> 添加时显示：<input type="checkbox" name="displayed" value="8"/> 列表时排序：<input type="text" id="listOrder8" name="listOrder" size="3"/> 列表时显示：<input type="checkbox" name="listDisplayed" value="8"/></td>
							<td><div id="fieldTip8" style="width:250px"></div></td>
						</tr>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td colspan="2">字段名：<input type="text" readonly="readonly" name="fieldName" value="source" size="12"/> 显示标题：<input type="text" id="displayTitle9" name="displayTitle" size="20"/> 添加时排序：<input type="text" id="displayOrder9" name="displayOrder" size="3"/> 添加时显示：<input type="checkbox" name="displayed" value="9"/> 列表时排序：<input type="text" id="listOrder9" name="listOrder" size="3"/> 列表时显示：<input type="checkbox" name="listDisplayed" value="9"/></td>
							<td><div id="fieldTip9" style="width:250px"></div></td>
						</tr>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td colspan="2">字段名：<input type="text" readonly="readonly" name="fieldName" value="defaultPicUrl" size="12"/> 显示标题：<input type="text" id="displayTitle10" name="displayTitle" size="20"/> 添加时排序：<input type="text" id="displayOrder10" name="displayOrder" size="3"/> 添加时显示：<input type="checkbox" name="displayed" value="10"/> 列表时排序：<input type="text" id="listOrder10" name="listOrder" size="3"/> 列表时显示：<input type="checkbox" name="listDisplayed" value="10"/></td>
							<td><div id="fieldTip10" style="width:250px"></div></td>
						</tr>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td colspan="2">字段名：<input type="text" readonly="readonly" name="fieldName" value="defStr1" size="12"/> 显示标题：<input type="text" id="displayTitle11" name="displayTitle" size="20"/> 添加时排序：<input type="text" id="displayOrder11" name="displayOrder" size="3"/> 添加时显示：<input type="checkbox" name="displayed" value="11"/> 列表时排序：<input type="text" id="listOrder11" name="listOrder" size="3"/> 列表时显示：<input type="checkbox" name="listDisplayed" value="11"/></td>
							<td><div id="fieldTip11" style="width:250px"></div></td>
						</tr>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td colspan="2">字段名：<input type="text" readonly="readonly" name="fieldName" value="defStr2" size="12"/> 显示标题：<input type="text" id="displayTitle12" name="displayTitle" size="20"/> 添加时排序：<input type="text" id="displayOrder12" name="displayOrder" size="3"/> 添加时显示：<input type="checkbox" name="displayed" value="12"/> 列表时排序：<input type="text" id="listOrder12" name="listOrder" size="3"/> 列表时显示：<input type="checkbox" name="listDisplayed" value="12"/></td>
							<td><div id="fieldTip12" style="width:250px"></div></td>
						</tr>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td colspan="2">字段名：<input type="text" readonly="readonly" name="fieldName" value="defStr3" size="12"/> 显示标题：<input type="text" id="displayTitle13" name="displayTitle" size="20"/> 添加时排序：<input type="text" id="displayOrder13" name="displayOrder" size="3"/> 添加时显示：<input type="checkbox" name="displayed" value="13"/> 列表时排序：<input type="text" id="listOrder13" name="listOrder" size="3"/> 列表时显示：<input type="checkbox" name="listDisplayed" value="13"/></td>
							<td><div id="fieldTip13" style="width:250px"></div></td>
						</tr>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td colspan="2">字段名：<input type="text" readonly="readonly" name="fieldName" value="defDate1" size="12"/> 显示标题：<input type="text" id="displayTitle14" name="displayTitle" size="20"/> 添加时排序：<input type="text" id="displayOrder14" name="displayOrder" size="3"/> 添加时显示：<input type="checkbox" name="displayed" value="14"/> 列表时排序：<input type="text" id="listOrder14" name="listOrder" size="3"/> 列表时显示：<input type="checkbox" name="listDisplayed" value="14"/></td>
							<td><div id="fieldTip14" style="width:250px"></div></td>
						</tr>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td colspan="2">字段名：<input type="text" readonly="readonly" name="fieldName" value="defDate2" size="12"/> 显示标题：<input type="text" id="displayTitle15" name="displayTitle" size="20"/> 添加时排序：<input type="text" id="displayOrder15" name="displayOrder" size="3"/> 添加时显示：<input type="checkbox" name="displayed" value="15"/> 列表时排序：<input type="text" id="listOrder15" name="listOrder" size="3"/> 列表时显示：<input type="checkbox" name="listDisplayed" value="15"/></td>
							<td><div id="fieldTip15" style="width:250px"></div></td>
						</tr>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td colspan="2">字段名：<input type="text" readonly="readonly" name="fieldName" value="defBool1" size="12"/> 显示标题：<input type="text" id="displayTitle16" name="displayTitle" size="20"/> 添加时排序：<input type="text" id="displayOrder16" name="displayOrder" size="3"/> 添加时显示：<input type="checkbox" name="displayed" value="16"/> 列表时排序：<input type="text" id="listOrder16" name="listOrder" size="3"/> 列表时显示：<input type="checkbox" name="listDisplayed" value="16"/></td>
							<td><div id="fieldTip16" style="width:250px"></div></td>
						</tr>
						<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
							<td colspan="2">字段名：<input type="text" readonly="readonly" name="fieldName" value="defDec1" size="12"/> 显示标题：<input type="text" id="displayTitle17" name="displayTitle" size="20"/> 添加时排序：<input type="text" id="displayOrder17" name="displayOrder" size="3"/> 添加时显示：<input type="checkbox" name="displayed" value="17"/> 列表时排序：<input type="text" id="listOrder17" name="listOrder" size="3"/> 列表时显示：<input type="checkbox" name="listDisplayed" value="17"/></td>
							<td><div id="fieldTip17" style="width:250px"></div></td>
						</tr>
						
						
					</tbody>
				</table>
			</div>
		</div>
	</div>
	
	<div style="width: 100%" class="ui-state-default ui-jqgrid-pager ui-corner-bottom" dir="ltr">
		<div role="group" class="ui-pager-control">
			<table cellspacing="0" cellpadding="0" border="0" style="width: 100%; table-layout: fixed;" class="ui-pg-table">
				<tbody>
					<tr>
						<td align="left">
							<table cellspacing="0" cellpadding="0" border="0" style="float: left; table-layout: auto;" class="ui-pg-table navtable">
								<tbody>
									<tr>
										<td><input style="cursor: pointer;" type="submit" id="button"  value="提交"/></td>
										<td><input style="cursor: pointer;" type="button" value="关闭" onclick="window.close();"/></td>
									</tr>
								</tbody>
							</table>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</div>
</div>
</form>
</body>
</html>
                  
