<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>

<title>系统功能及权限</title>

<script type="text/javascript">
	//修改
	function editResource(resourceId,operationId){
		/*var returnResTag = window.showModalDialog("/core/virtualResource.do?method=edit&resourceId="+resourceId+"&operationId="+operationId,'',"dialogWidth:700px;dialogHeight:400px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		if(returnResTag == "refresh") {
			self.location.reload();
		}*/
		window.open("/core/virtualResource.do?method=edit&resourceId="+resourceId+"&operationId="+operationId,"_blank")
	}
    $(function () {
//        alert(document.documentElement.clientHeight)
        $("#jqgrid-maindiv").css('height',document.documentElement.clientHeight-240).css('overflow-y','scroll');
        $(".ui-jqgrid-bdiv").css('height',document.documentElement.clientHeight-240).css('overflow-y','scroll')
//		$(".ui-jqgrid-btable").css('overflow-y','scroll')
    })
</script>
</head>
<body>
<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 100%">
<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
  <%--<div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
    <span class="ui-jqgrid-title">系统功能及权限</span>
  </div>--%>
  <div style="width: 100%" class="ui-state-default ui-jqgrid-hdiv">
    <div class="ui-jqgrid-hbox" style="width: 100%">
      <table cellspacing="0" cellpadding="0" border="0" aria-labelledby="gbox_grid_35" role="grid" style="width: 100%" class="ui-jqgrid-htable">
          <thead>
              <tr role="rowheader" class="ui-jqgrid-labels">
                <th class="ui-state-default ui-th-column ui-th-ltr" onmouseover="over(this,'ui-state-hover ui-th-column ui-th-ltr')" onmouseout="out(this)" style="width: 2%;">
                  <div id="jqgh_rn"></div>
				</th>
				
				<th class="ui-state-default ui-th-column ui-th-ltr" onmouseover="over(this,'ui-state-hover ui-th-column ui-th-ltr')" onmouseout="out(this)" style="width: 78%;">
				    <span class="ui-jqgrid-resize ui-jqgrid-resize-ltr" style="cursor: col-resize;">&nbsp;</span>
				    <div id="jqgh_title" class="ui-jqgrid-sortable">
						模块名称
				    </div>
				</th>
		
				<th class="ui-state-default ui-th-column ui-th-ltr" onmouseover="over(this,'ui-state-hover ui-th-column ui-th-ltr')" onmouseout="out(this)" style="width: 20%;">
				    <div id="jqgh_url" class="ui-jqgrid-sortable">
						相关操作
				    </div>
				</th>
				
	    	  </tr>
		   </thead>
       </table>
     </div>
   </div>

	<div class="ui-jqgrid-bdiv" id="jqgrid-maindiv" style=" width: 100%;overflow-y: scroll">
		<div style="position: relative;">
			<div>
				<table cellspacing="0" cellpadding="0" border="0" id="grid_35" class="ui-jqgrid-btable" role="grid" aria-multiselectable="false" aria-labelledby="gbox_grid_35" style="width: 100%">
					<tbody>
						<c:forEach items="${_Resources}" var="resource" varStatus="index">
							<tr class="ui-widget-content jqgrow ui-row-ltr" onmouseover="over(this,'ui-state-hover jqgrow ui-row-ltr')" onmouseout="out(this)" onclick="clicked(this)">
								<td style="text-align: center; width: 2%;" class="ui-state-default jqgrid-rownum" role="gridcell">${index.index+1}</td>
								<c:choose>
									<c:when test="${resource.layer == 1}">
										<td style="width: 78%;" role="gridcell"><img src="<c:url value='/'/>images/tree_folder4.gif" border="0"/>${resource.resourceName}</td>
										<td style="text-align: left; width: 20%;" role="gridcell">&nbsp;<img src="<c:url value='/images'/>/t_016.gif" border="0"/><a href="javascript:;" onclick="editResource('${resource.resourceId}','3');">浏览</a></td>
									</c:when>
									<c:otherwise>
										<td style="width: 78%;" role="gridcell"><c:forEach begin="0" end="${resource.layer}">&nbsp;</c:forEach><img src="<c:url value='/'/>images/tree_folder3.gif" border="0"/>${resource.resourceName}</td>
										<td style="text-align: left; width: 20%;" role="gridcell"><c:forEach items="${_OperationTREE}" var="operation"><c:choose><c:when test="${!empty operation.childs}"><img src="<c:url value='/images'/>/tree_folder4.gif" border="0"/></c:when><c:otherwise>&nbsp;<img src="<c:url value='/images'/>/t_016.gif" border="0"/></c:otherwise></c:choose><a href="javascript:;" onclick="editResource('${resource.resourceId}','${operation.operationId}');">${operation.operationName}</a><br/></c:forEach></td>
									</c:otherwise>
								</c:choose>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	
	<div id="pager_35" style="width: 100%" class="ui-state-default ui-jqgrid-pager ui-corner-bottom" dir="ltr">
		<div role="group" class="ui-pager-control" id="pg_pager_35">
			<table cellspacing="0" cellpadding="0" border="0" role="row" style="width: 100%; table-layout: fixed;" class="ui-pg-table">
				<tbody>
					<tr>
						<td align="left" id="pager_35_left">
							<table cellspacing="0" cellpadding="0" border="0" style="float: left; table-layout: auto;" class="ui-pg-table navtable">
								<tbody>
									<tr>
										<td class="ui-pg-button ui-corner-all" title="刷新表格" id="refresh_grid_35">
											<div class="ui-pg-div">
												<span class="ui-icon ui-icon-refresh" onclick="javascript:location.reload();"></span>
											</div>
										</td>
									</tr>
								</tbody>
							</table>
						</td>
						<td align="right" id="pager_35_right">
							<div class="ui-paging-info" style="text-align: right;" dir="ltr">共 ${fn:length(_Resources)} 条</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</div>
</div>
</body>
</html>