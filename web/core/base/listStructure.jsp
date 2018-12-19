<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>

<title>岗位信息</title>

<script type="text/javascript">
	//新增
	function addStructure(){
		/*var returnStrTag = window.showModalDialog("/core/structureInfor.do?method=edit",null,"dialogWidth:700px;dialogHeight:400px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		if(returnStrTag == "refresh") {
			self.location.reload();
		}*/
        window.open("/core/structureInfor.do?method=edit","_blank");
	}
	//修改
	function editStructure(structureId){
		/*var returnStrTag = window.showModalDialog("/core/structureInfor.do?method=edit&structureId="+structureId,'',"dialogWidth:700px;dialogHeight:400px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		if(returnStrTag == "refresh") {
			self.location.reload();
		}*/
        window.open("/core/structureInfor.do?method=edit&structureId="+structureId,"_blank");
	}
	//删除
	function deleteStructure(structureId){
		var yes = window.confirm("确定要删除吗？");
		if (yes) {
			var form = document.getElementById('listForm');
			form.action = "<c:url value='/core/structureInfor.do'/>?method=delete&structureId="+structureId;
			form.submit();
			window.location.reload();
		}
	}
	$(function () {
        $(".ui-jqgrid-bdiv").css('height',document.documentElement.clientHeight-240).css('overflow-y','scroll')
		$("#jqgrid-maindiv").css('height',document.documentElement.clientHeight-240).css('overflow-y','scroll')
    })
</script>
</head>
<body>
<form id="listForm" action="/core/structureInfor.do?method=list" method="post">
<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 100%">
<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
  <%--<div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
    <span class="ui-jqgrid-title">岗位信息</span>
  </div>--%>
  <div style="width: 100%" class="ui-state-default ui-jqgrid-hdiv">
    <div class="ui-jqgrid-hbox" style="width: 100%">
      <table cellspacing="0" cellpadding="0" border="0" aria-labelledby="gbox_grid_35" role="grid" style="width: 100%" class="ui-jqgrid-htable">
          <thead>
              <tr role="rowheader" class="ui-jqgrid-labels">
                <th class="ui-state-default ui-th-column ui-th-ltr" onmouseover="over(this,'ui-state-hover ui-th-column ui-th-ltr')" onmouseout="out(this)" style="width: 2%;">
                  <div id="jqgh_rn"></div>
				</th>
				
				<th class="ui-state-default ui-th-column ui-th-ltr" onmouseover="over(this,'ui-state-hover ui-th-column ui-th-ltr')" onmouseout="out(this)" style="width: 80%;">
				    <span class="ui-jqgrid-resize ui-jqgrid-resize-ltr" style="cursor: col-resize;">&nbsp;</span>
				    <div id="jqgh_title" class="ui-jqgrid-sortable">
						岗位名称
				    </div>
				</th>
				
				<th class="ui-state-default ui-th-column ui-th-ltr" onmouseover="over(this,'ui-state-hover ui-th-column ui-th-ltr')" onmouseout="out(this)" style="width: 8%;">
				    <span class="ui-jqgrid-resize ui-jqgrid-resize-ltr" style="cursor: col-resize;">&nbsp;</span>
				    <div id="jqgh_title" class="ui-jqgrid-sortable">
						排序编号
				    </div>
				</th>
		
				<th class="ui-state-default ui-th-column ui-th-ltr" onmouseover="over(this,'ui-state-hover ui-th-column ui-th-ltr')" onmouseout="out(this)" style="width: 10%;">
				    <div id="jqgh_url" class="ui-jqgrid-sortable">
						相关操作
				    </div>
				</th>
				
	    	  </tr>
		   </thead>
       </table>
     </div>
   </div>

	<div class="ui-jqgrid-bdiv" id="jqgrid-maindiv" style=" width: 100%">
		<div style="position: relative;">
			<div>
				<table cellspacing="0" cellpadding="0" border="0" id="grid_35" class="ui-jqgrid-btable" role="grid" aria-multiselectable="false" aria-labelledby="gbox_grid_35" style="width: 100%">
					<tbody>
						<c:forEach items="${_StructureTree}" var="structure" begin="1" varStatus="index">
							<tr class="ui-widget-content jqgrow ui-row-ltr" onmouseover="over(this,'ui-state-hover jqgrow ui-row-ltr')" onmouseout="out(this)" onclick="clicked(this)">
								<td style="text-align: center; width: 2%;" class="ui-state-default jqgrid-rownum">${index.index}</td>
								<td style="width: 80%;"><c:choose><c:when test="${structure.layer == 1}"><img src="<c:url value='/'/>images/tree_folder4.gif" border="0"/></c:when><c:otherwise><c:forEach begin="0" end="${structure.layer}">&nbsp;</c:forEach><img src="<c:url value='/'/>images/tree_folder3.gif" border="0"/></c:otherwise></c:choose>${structure.structureName}</td>
								<td style="text-align: center; width: 8%;">${structure.orderNo}</td>
								<td style="text-align: center; width: 10%;"><a href="javascript:;" onclick="editStructure('${structure.structureId}');">[修改]</a> <a href="javascript:;" onclick="deleteStructure('${structure.structureId}');">[删除]</a></td>
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
										<td class="ui-pg-button ui-corner-all" title="点击新增信息" style="cursor: pointer;">
											<div class="ui-pg-div">
												<span class="ui-icon ui-icon-plusthick"></span>
												<span onclick="addStructure();">新增</span>
											</div>
										</td>
									</tr>
								</tbody>
							</table>
						</td>
						<td align="right" id="pager_35_right">
							<div class="ui-paging-info" style="text-align: right;" dir="ltr">共 ${fn:length(_StructureTree)} 条</div>
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